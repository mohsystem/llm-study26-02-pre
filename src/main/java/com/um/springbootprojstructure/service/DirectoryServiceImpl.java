package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.LdapUserResponse;
import com.um.springbootprojstructure.service.exception.InvalidOperationException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class DirectoryServiceImpl implements DirectoryService {

    private final LdapTemplate ldapTemplate;

    // Allow simple dc strings like: example.com OR sub.example.com
    private static final Pattern DC_PATTERN = Pattern.compile("^[a-zA-Z0-9.-]{1,253}$");
    // Username basic safety (avoid LDAP injection; we also use filters which escape values)
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9._-]{1,64}$");

    public DirectoryServiceImpl(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    @Override
    public List<LdapUserResponse> searchUser(String dc, String username) {
        if (dc == null || dc.isBlank() || !DC_PATTERN.matcher(dc.trim()).matches()) {
            throw new InvalidOperationException("INVALID_DC", "Invalid dc parameter");
        }
        if (username == null || username.isBlank() || !USERNAME_PATTERN.matcher(username.trim()).matches()) {
            throw new InvalidOperationException("INVALID_USERNAME", "Invalid username parameter");
        }

        String baseDn = dcToBaseDn(dc.trim());

        // Query: (uid=<username>)
        // You can change attribute to sAMAccountName for AD, etc.
        EqualsFilter filter = new EqualsFilter("uid", username.trim());

        return ldapTemplate.search(
                baseDn,
                filter.encode(),
                (AttributesMapper<LdapUserResponse>) attrs -> mapUser(attrs)
        );
    }

    private LdapUserResponse mapUser(Attributes attrs) throws NamingException {
        // DN is not directly in Attributes; LdapTemplate can provide it via ContextMapper instead.
        // For minimal output, we omit DN here or set null.
        String uid = getAttr(attrs, "uid");
        String cn = getAttr(attrs, "cn");
        String sn = getAttr(attrs, "sn");
        String givenName = getAttr(attrs, "givenName");
        String mail = getAttr(attrs, "mail");
        return new LdapUserResponse(null, uid, cn, sn, givenName, mail);
    }

    private String getAttr(Attributes attrs, String name) throws NamingException {
        return attrs.get(name) == null ? null : String.valueOf(attrs.get(name).get());
    }

    private String dcToBaseDn(String dc) {
        // "example.com" -> "dc=example,dc=com"
        String[] parts = dc.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            String p = parts[i].trim();
            if (p.isEmpty()) continue;
            if (sb.length() > 0) sb.append(",");
            sb.append("dc=").append(p);
        }
        if (sb.length() == 0) {
            throw new InvalidOperationException("INVALID_DC", "Invalid dc parameter");
        }
        return sb.toString();
    }
}
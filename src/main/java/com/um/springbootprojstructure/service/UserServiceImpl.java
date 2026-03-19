package com.um.springbootprojstructure.service;

import com.um.springbootprojstructure.dto.UserResponse;
import com.um.springbootprojstructure.entity.AccountStatus;
import com.um.springbootprojstructure.entity.Role;
import com.um.springbootprojstructure.entity.UserAccount;
import com.um.springbootprojstructure.mapper.UserMapper;
import com.um.springbootprojstructure.repository.UserAccountRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserAccountRepository userAccountRepository;

    public UserServiceImpl(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public Page<UserResponse> listUsers(int page, int size, Role role, AccountStatus status) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        Specification<UserAccount> spec = Specification.where(null);

        if (role != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("role"), role));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }

        return userAccountRepository.findAll(spec, pageable).map(UserMapper::toResponse);
        // If no filters provided, spec is "empty" -> returns all with pagination.
    }
}
package com.finflx.investmenttask.application.service;

import com.finflx.investmenttask.domain.entity.User;
import com.finflx.investmenttask.domain.enumuration.UserRole;
import com.finflx.investmenttask.presentation.exception.EmailExistsException;
import com.finflx.investmenttask.domain.repository.UserRepository;
import com.finflx.investmenttask.infrastructure.security.UserDetailsDto;
import com.finflx.investmenttask.presentation.exception.EntityNotFoundException;
import com.finflx.investmenttask.presentation.exception.NotEligibleToUpdateEntityException;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UserDetailsDto loadUserByUsername(String email) {
        User user = this.getByEmail(email);
        return new UserDetailsDto(user);
    }

    public void validateUserExistingAndHasInvestorRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), "id", userId));
        if (!user.getScopeType().equals(UserRole.INVESTOR)) {
            throw new NotEligibleToUpdateEntityException(User.class.getSimpleName(), userId);
        }
    }

    public void validateEmailNotExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailExistsException(email);
        }
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), "email", email));
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class.getSimpleName(), "id", id));
    }

    public User create(String email, String password, UserRole scopeType) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setScopeType(scopeType);
        return userRepository.save(user);
    }
}

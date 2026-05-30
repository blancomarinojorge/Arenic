package com.arenic.backend.modules.identity.api;

import com.arenic.backend.modules.identity.internal.dto.CreateUserDto;
import com.arenic.backend.modules.identity.internal.model.User;
import com.arenic.backend.modules.identity.internal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createUser(CreateUserDto createUserDto){
        if (userRepository.findByEmail(createUserDto.email()).isPresent()){
            throw new IllegalArgumentException("User " + createUserDto.email() + " already exists.");
        }

        return userRepository.save(
            User.builder()
                    .email(createUserDto.email())
                    .name(createUserDto.name())
                    .surname1(createUserDto.surname1())
                    .surname2(createUserDto.surname2())
                    .password(passwordEncoder.encode(createUserDto.password()))
                    .build()
        );
    }
}

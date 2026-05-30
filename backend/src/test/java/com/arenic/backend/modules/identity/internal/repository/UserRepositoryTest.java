package com.arenic.backend.modules.identity.internal.repository;

import com.arenic.backend.config.persistence.JpaAuditConfig;
import com.arenic.backend.modules.identity.internal.model.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@ActiveProfiles("test")
@Import(JpaAuditConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldSaveUserAndAuditCreation(){
        User user = User.builder()
                .email("test@arenic.com")
                .password("encoded_password")
                .name("John")
                .surname1("Doe")
                .surname2("Smith")
                .build();

        User savedUser = userRepository.save(user);

        System.out.println(user.toString());

        assertThat(savedUser.getId()).isNotNull();

        userRepository.delete(user);
        userRepository.flush();
        entityManager.clear();

        assertThat(userRepository.getUserById(savedUser.getId())).isNotPresent();
    }
}

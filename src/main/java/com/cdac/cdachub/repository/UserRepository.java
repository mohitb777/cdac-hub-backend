package com.cdac.cdachub.repository;

import com.cdac.cdachub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByGoogleId(String googleId);
}
// how this was working 
// 1. User logs in with Google OAuth2
// email and googlr id was not default curd
// so how spring data jpa does this
// 1. Spring Data JPA looks at the method na
// me and infers the query from it. In this case, 
//findByEmail means it will look for a User entity where the email field matches the provided email parameter.
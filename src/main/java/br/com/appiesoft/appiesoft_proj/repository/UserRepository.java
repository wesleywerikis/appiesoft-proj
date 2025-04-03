package br.com.appiesoft.appiesoft_proj.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.appiesoft.appiesoft_proj.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);

}

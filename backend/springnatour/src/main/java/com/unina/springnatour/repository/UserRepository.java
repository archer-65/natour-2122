package com.unina.springnatour.repository;

import com.unina.springnatour.model.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByCognitoId(String cognitoId);

    List<User> findByUsernameContainingIgnoreCaseAndIdNot(String username, Long id, Pageable page);
}

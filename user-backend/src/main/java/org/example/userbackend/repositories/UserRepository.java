package org.example.userbackend.repositories;


import org.example.userbackend.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AppUser, UUID> {

    Optional<AppUser> findByUsername(String username);

    @Modifying
    @Query("update AppUser user set user.username = :username, user.isAdmin = :isAdmin where user.id = :id")
    void updateUser(@Param("id") UUID id, @Param("username") String username, @Param("isAdmin") boolean isAdmin);

}

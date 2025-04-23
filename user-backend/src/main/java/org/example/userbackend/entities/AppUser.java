package org.example.userbackend.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.userbackend.dto.UserDTO;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUser implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid2")
    private UUID id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "isAdmin", nullable = false)
    private boolean isAdmin;


    public static UserDTO toDTO(AppUser appUser) {
        return new UserDTO(
                appUser.getId(),
                appUser.getUsername(),
                appUser.isAdmin()
        );
    }
}

package com.siuao.shopdt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(	name = "user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(max = 120)
    private String password;

    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    private Role role;

    @Column(name = "status")
    private Integer status;

    @Column(name = "created_user", nullable = false)
    private String createdUser;

    @Column(name = "updated_user", nullable = true)
    private String updatedUser;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date", nullable = true)
    private LocalDateTime updatedDate;

    public User(String username, String email, String encode, Integer status, String createdUser) {
        this.username = username;
        this.email = email;
        this.password = encode;
        this.status = status;
        this.createdUser = createdUser;
    }
}


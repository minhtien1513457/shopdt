package com.siuao.shopdt.entity;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
    
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<ReviewEntity> reviews = new HashSet<ReviewEntity>();
    
    @OneToOne(mappedBy = "user")
    private CartEntity cart;
    
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<HistoryBuy> historyBuy = new HashSet<HistoryBuy>();
    
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<HistorySearch> historySearch = new HashSet<HistorySearch>();

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


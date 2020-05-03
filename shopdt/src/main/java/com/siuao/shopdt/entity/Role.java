package com.siuao.shopdt.entity;

import com.siuao.shopdt.constant.ERole;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private Set<User> users = new HashSet<User>();
}


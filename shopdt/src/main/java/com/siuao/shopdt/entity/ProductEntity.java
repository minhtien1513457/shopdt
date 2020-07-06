package com.siuao.shopdt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Integer status;

    @Column(name = "price")
    private Long price;

    @ManyToOne
    @JoinColumn(name = "os_id")
    @JsonIgnore
    private OsEntity os;

    @ManyToOne
    @JoinColumn(name = "type_id")
    @JsonIgnore
    private TypeEntity type;

    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private Set<ActionEntity> actions = new HashSet<ActionEntity>();

    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private Set<GalleryEntity> galleries = new HashSet<GalleryEntity>();
}

package com.siuao.shopdt.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product")
public class ProductEntity implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 3797024588590530579L;

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
    
    @Column(name = "created_user", nullable = false)
    private String createdUser;

    @Column(name = "updated_user")
    private String updatedUser;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
    
    @Column(name = "deleted")
    private Integer deleted;
    
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;
    
    @Column(name = "deleted_by")
    private Long deletedBy;

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
    
    @OneToMany(mappedBy = "product")
    @JsonIgnore
    private Set<ReviewEntity> reviews = new HashSet<ReviewEntity>();
}

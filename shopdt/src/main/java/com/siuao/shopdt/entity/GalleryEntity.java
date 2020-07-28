package com.siuao.shopdt.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gallery")
public class GalleryEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5221098010010058521L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false)
    private Long id;

    @Column(name = "img_url")
    private String imgUrl;
    
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
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private ProductEntity product;
}
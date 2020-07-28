package com.siuao.shopdt.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "review")
public class ReviewEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5460748572417564452L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "star")
    private Integer star;

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
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    
    
}

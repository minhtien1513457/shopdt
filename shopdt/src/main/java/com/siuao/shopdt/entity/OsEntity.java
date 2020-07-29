package com.siuao.shopdt.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "os")
public class OsEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6979846642038233109L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="id", insertable = false, nullable = false)
    private Long id;

    @Column(name= "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "os")
    @JsonIgnore
    private Set<ProductEntity> products = new HashSet<ProductEntity>();

    @Column(name = "created_user", nullable = false)
    private String createdUser;

    @Column(name= "updated_user")
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
}

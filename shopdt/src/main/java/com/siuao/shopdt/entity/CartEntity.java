package com.siuao.shopdt.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Table(name = "cart")
public class CartEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2830054227163851523L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", insertable = false, nullable = false)
	private Long id;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@OneToMany(mappedBy = "cart")
	@JsonIgnore
	private Set<CartDetailEntity> cartDetail = new HashSet<CartDetailEntity>();
	
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
}

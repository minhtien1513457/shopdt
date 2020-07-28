package com.siuao.shopdt.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

	@Column(name = "amount", nullable = false)
	private Integer amount;

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
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
}

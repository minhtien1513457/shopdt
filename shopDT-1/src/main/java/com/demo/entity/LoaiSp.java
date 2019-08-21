package com.demo.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "loaisp")
public class LoaiSp implements java.io.Serializable{
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable=false)
	private Integer id;
	
	@Column(name = "ten")
	private String ten;
	
	@Column(name = "soluong")
	private String soluong;
	
	 public String getSoluong() {
		return soluong;
	}

	public void setSoluong(String soluong) {
		this.soluong = soluong;
	}

	public Set<Sanpham> getChitietSps() {
		return sanphams;
	}

	public void setChitietSps(Set<Sanpham> sanphams) {
		this.sanphams = sanphams;
	}

	@OneToMany(mappedBy="loaiSp")
	    private Set<Sanpham> sanphams;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}
}

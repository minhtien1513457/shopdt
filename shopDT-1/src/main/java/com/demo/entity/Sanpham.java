package com.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name = "sanpham")
public class Sanpham implements java.io.Serializable{
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Integer id;
	
	@ManyToOne
    @JoinColumn(name="loaiID", nullable=false)
    private LoaiSp loaiSp;
	
	@Column(name = "ten")
	private String ten;
	
	@Column(name = "gia")
	private Integer gia;
	
	@Column(name = "soluong")
	private Integer soluong;

	public Integer getSoluong() {
		return soluong;
	}

	public void setSoluong(Integer soluong) {
		this.soluong = soluong;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LoaiSp getLoaiSp() {
		return loaiSp;
	}

	public void setLoaiSp(LoaiSp loaiSp) {
		this.loaiSp = loaiSp;
	}

	public String getTen() {
		return ten;
	}

	public void setTen(String ten) {
		this.ten = ten;
	}

	public Integer getGia() {
		return gia;
	}

	public void setGia(Integer gia) {
		this.gia = gia;
	}
}

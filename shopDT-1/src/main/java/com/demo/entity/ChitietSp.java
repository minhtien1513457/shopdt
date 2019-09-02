package com.demo.entity;

import javax.persistence.*;

@Entity
public class ChitietSp implements java.io.Serializable{
	public static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id", nullable=false)
	private Integer id;
	
	@Column(name = "mota")
	private String mota;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMota() {
		return mota;
	}

	public void setMota(String mota) {
		this.mota = mota;
	}

	public String getHinh() {
		return hinh;
	}

	public void setHinh(String hinh) {
		this.hinh = hinh;
	}

	public Sanpham getSanpham() {
		return sanpham;
	}

	public void setSanpham(Sanpham sanpham) {
		this.sanpham = sanpham;
	}

	@Column(name = "hinh")
	private String hinh;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "sanphamID")
	private Sanpham sanpham;
}

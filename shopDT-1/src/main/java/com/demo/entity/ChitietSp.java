package com.demo.entity;

import javax.persistence.*;

@Entity
public class ChitietSp {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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
	    @JoinColumn(name = "sanphamId")
	    private Sanpham sanpham;
}

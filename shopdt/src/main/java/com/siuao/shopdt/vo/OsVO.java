package com.siuao.shopdt.vo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.siuao.shopdt.entity.ProductEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OsVO {
    private Long id;
    private String name;
    private String description;
    private Set<ProductEntity> products = new HashSet<ProductEntity>();
    private String createdUser;
    private String updatedUser;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
    private Integer deleted;
    private LocalDateTime deletedAt;
    private Long deletedBy;
}

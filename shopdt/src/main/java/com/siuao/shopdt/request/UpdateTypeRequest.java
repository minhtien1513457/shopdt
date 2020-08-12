package com.siuao.shopdt.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTypeRequest {
	private Long id;
    private String name;
    private String description;
}

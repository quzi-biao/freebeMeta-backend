package com.freebe.code.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "公共分页查询")
public class PageBean {
	@ApiModelProperty("组织ID")
	private Long organizationId;
	
    @ApiModelProperty(value = "当前页")
    private long currPage;
    
    @ApiModelProperty(value = "显示条数")
    private long limit;
    
    @ApiModelProperty(value = "排序方式")
    private Boolean desc;
    
    @ApiModelProperty(value = "字段排序 desc，asc")
    private String order;
    
}


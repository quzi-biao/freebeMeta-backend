package com.freebe.code.business.meta.controller.param;


import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


/**
 *
 * @author zhengbiaoxie
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ApiModel("内容草稿箱查询参数")
public class ContentDraftQueryParam extends ContentQueryParam {

}

package com.snow.oauth.uaa.entity.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户登录",description = "用于用户登录是需要提供的信息")
public class UserVo {
    @ApiModelProperty(value = "账号",required=true,dataType = "String")
    private String username;
    @ApiModelProperty(value = "密码",required=true,dataType = "String")
    private String password;
}

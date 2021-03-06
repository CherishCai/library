



package cn.cherish.library.web.request;

import lombok.Data;

import javax.validation.constraints.Pattern;

/**
 * 更改密码的参数
 *
 */
@Data
public class ModifyPasswordReq implements java.io.Serializable {

    private static final long serialVersionUID = 6675494721221351239L;

    @Pattern(regexp="[0-9A-Za-z]{6,16}$", message="密码必须是6~16位字母和数字的组合")
    private String oldPassword;
    @Pattern(regexp="[0-9A-Za-z]{6,16}$", message="密码必须是6~16位字母和数字的组合")
    private String password;
    @Pattern(regexp="[0-9A-Za-z]{6,16}$", message="密码必须是6~16位字母和数字的组合")
    private String repeatPassword;

}

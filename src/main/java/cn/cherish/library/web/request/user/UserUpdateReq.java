



package cn.cherish.library.web.request.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

@Data
public class UserUpdateReq implements java.io.Serializable {

    @Min(value = 1, message = "{user.id}")
    private Long id;

    @Length(min = 1 ,max = 16 ,message = "{user.nickname}")
    private String nickname;

    @Pattern(regexp = "^[1][34578][0-9]{9}$", message = "请输入正确的手机号码")
    private String telephone;

    private String email;

    @Length(min = 1 ,max = 32 ,message = "{user.position}")
    private String position;

    @Range(min = 0, max = 1, message = "{user.active}")
    private Integer active;

    @Length(min = 0, max = 1024, message = "{user.description}")
    private String description;

}





package cn.cherish.library.web.request.role;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 *
 */
@Data
public class RoleSaveReq implements java.io.Serializable {

    private static final long serialVersionUID = -6371855737632998948L;

    @Length(min = 1, max = 16, message="权限名必须是1~16位字母")
    private String name;

    @Length(min = 0, max = 1024, message = "{user.description}")
    private String description;

}

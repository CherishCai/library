



package cn.cherish.library.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 */
@Data
@AllArgsConstructor
public class Response<T> implements java.io.Serializable {
    private static final long serialVersionUID = -222983483999088181L;
    private String code;
    private Boolean success;
    private String message;
    private T data;
}





package cn.cherish.library.commom.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO implements java.io.Serializable {

    private static final long serialVersionUID = -4265666189867911606L;

    private Long id;

    private String title;

    private String content;

    private Integer readSum;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date createdTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifiedTime;


}
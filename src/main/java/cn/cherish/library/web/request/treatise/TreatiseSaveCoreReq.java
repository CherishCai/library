


package cn.cherish.library.web.request.treatise;

import cn.cherish.library.commom.enums.Language;
import cn.cherish.library.commom.enums.PublicationMode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TreatiseSaveCoreReq implements java.io.Serializable {

    private static final long serialVersionUID = -4815718221777847521L;

    /**
     * 出版日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd",iso = DateTimeFormat.ISO.DATE)
    private Date publishDate;
    /**
     * 编号
     */
    @Length(min = 1,message = "编号不可为空")
    private String no;
    /**
     * ISBN编号
     */
    @Length(min = 1,message = "ISBN不可为空")
    private String ISBN;
    /**
     * 书名
     */
    @Length(min = 1,message = "书名不可为空")
    private String bookName;
    /**
     * 简介
     */
    private String description;
    /**
     * 出版社
     */
    private String publishHouse;
    /**
     * 出版地
     */
    private String publishPlace;
    /**
     * 关键词
     */
    private String keywords;
    /**
     * 分类
     */
    private Long categoryId;
    /**
     * 作者
     */
    private Long authorId;
    /**
     * 出版形式
     * @see cn.cherish.library.commom.enums.PublicationMode
     */
    private String publicationMode = PublicationMode.DC.getCode();
    /**
     * 语种
     * @see cn.cherish.library.commom.enums.Language
     */
    private String language = Language.CHN.getCode();

}

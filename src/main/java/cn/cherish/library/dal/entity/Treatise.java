


package cn.cherish.library.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "t_treatise")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "treatises")
public class Treatise implements java.io.Serializable {

    private static final long serialVersionUID = 2855297224736248527L;

    public Treatise(Long id,String bookName) {
        this.id = id;
        this.bookName = bookName;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    /**
     * 总页数,单位:页
     */
    private Integer pageNumber = 0;
    /**
     * 字数,单位:千字
     */
    private Integer words = 0;
    /**
     * 定价,单位:分
     */
    private String price = "0";
    /**
     * 出版日期
     */
    @Temporal(TemporalType.DATE)
    @Column(name = "publish_date")
    private Date publishDate;
    /**
     * 编号
     */
    @Column(name = "no", length = 40)
    private String no;
    /**
     * ISBN编号
     */
    @Column(name = "ISBN", nullable = false , length = 40)
    private String ISBN;
    /**
     * 书名
     */
    @Column(name = "book_name", nullable = false, length = 40)
    private String bookName;
    /**
     * 简介
     */
    @Column(name = "description")
    private String description;
    /**
     * 出版社
     */
    @Column(name = "publish_house", length = 40)
    private String publishHouse = "";
    /**
     * 出版地
     */
    @Column(name = "publish_place", length = 40)
    private String publishPlace = "";

    @Transient
    private List<String> introductoryList;

    @Transient
    private List<String> reviewList;
}



package cn.cherish.library.dal.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_record")
public class Record implements java.io.Serializable {

	private static final long serialVersionUID = 2285174464789310329L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Long id;
    /**
     * 姓名
     */
    private String nickname;
    /**
     * 学号
     */
    @Column(name = "sno", nullable = false)
    private String username;
    /**
     * 书籍
     */
    @Column(name = "book_name")
    private String bookName;
    /**
     * ISBN
     */
    @Column(name = "ISBN", nullable = false)
    private String ISBN;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", length = 19)
    private Date createdTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_time", length = 19)
    private Date modifiedTime;

}


package cn.cherish.library.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "t_card")
public class Customer implements java.io.Serializable {

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
     * 密码
     */
    @JsonIgnore
    private String password;
    /**
     * -1: 申请状态 0：冻结 1：激活
     */
    @Column(name = "is_active", nullable = false)
    private Integer active;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_time", nullable = false, length = 19)
    private Date createdTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_time", nullable = false, length = 19)
    private Date modifiedTime;

}

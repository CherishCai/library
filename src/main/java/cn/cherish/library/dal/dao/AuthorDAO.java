



package cn.cherish.library.dal.dao;

import cn.cherish.library.dal.entity.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 著作者
 *
 */
public interface AuthorDAO extends IBaseDAO<Author, Long> {

    Author findByTelephone(String telephone);

    @Query("SELECT au FROM Author AS au ")
    List<Author> listAllPaged(Pageable pageable);

    Author findByEmail(String email);

    Author findByUsername(String username);

}

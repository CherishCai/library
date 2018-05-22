
package cn.cherish.library.dal.dao;

import cn.cherish.library.dal.entity.Record;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordDAO extends IBaseDAO<Record, Long> {

    @Query("SELECT c FROM Record AS c ")
    List<Record> listAllPaged(Pageable pageable);

    Record findByUsername(String username);


}

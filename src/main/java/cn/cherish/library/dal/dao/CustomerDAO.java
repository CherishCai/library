



package cn.cherish.library.dal.dao;

import cn.cherish.library.dal.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerDAO extends IBaseDAO<Customer, Long> {


    @Query("SELECT c FROM Customer AS c ")
    List<Customer> listAllPaged(Pageable pageable);

    Customer findByUsername(String username);

}

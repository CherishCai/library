



package cn.cherish.library.dal.dao;

import cn.cherish.library.dal.entity.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 */
public interface PermissionDAO extends IBaseDAO<Permission,Long> {

    List<Permission> findAll();

    Permission findByPermit(String permit);

    @Query(value = "SELECT p.id,p.permit,p.description FROM t_permission AS p join t_role_permission AS rp " +
            " WHERE p.id = rp.permission_id AND rp.role_id = :roleId ", nativeQuery = true)
    List<Permission> findByRoleId(@Param("roleId") Long roleId);

}

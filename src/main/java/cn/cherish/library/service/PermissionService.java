



package cn.cherish.library.service;

import cn.cherish.library.commom.dto.PermissionDTO;
import cn.cherish.library.dal.dao.IBaseDAO;
import cn.cherish.library.dal.dao.PermissionDAO;
import cn.cherish.library.dal.entity.Permission;
import cn.cherish.library.util.ObjectConvertUtil;
import cn.cherish.library.web.request.BasicSearchReq;
import cn.cherish.library.web.request.permission.PermissionSaveReq;
import cn.cherish.library.web.request.permission.PermissionUpdateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PermissionService extends ABaseService<Permission, Long> {

    private final PermissionDAO permissionDAO;

    @Autowired
    public PermissionService(PermissionDAO permissionDAO) {
        this.permissionDAO = permissionDAO;
    }

    @Override
    protected IBaseDAO<Permission, Long> getEntityDAO() {
        return permissionDAO;
    }

    public Page<PermissionDTO> findAll(BasicSearchReq basicSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        Page<Permission> permissionPage = this.findAll(pageNumber, basicSearchReq.getPageSize());

        return permissionPage.map(this::getPermissionDTO);
    }


    @Transactional
    public void update(PermissionUpdateReq permissionUpdateReq) {

        Permission permission = this.findById(permissionUpdateReq.getId());
        ObjectConvertUtil.objectCopy(permission, permissionUpdateReq);
        this.update(permission);
    }

    public boolean exist(String permit) {
        return permissionDAO.findByPermit(permit) != null;
    }

    @Transactional
    public void save(PermissionSaveReq permissionSaveReq) {

        Permission permission = new Permission();
        ObjectConvertUtil.objectCopy(permission, permissionSaveReq);
        this.save(permission);
    }

    public List<Permission> listByRoleId(Long roleId){
        return permissionDAO.findByRoleId(roleId);
    }

    /*不建议如此强硬，该手动去除关联再删除
    @Transactional(readOnly = false)
    public void delete(Long permissionId){
        //先删除t_role_permission表的外键关联
        customizedDAO.deleteRolePermissionRelation(permissionId);
        //再删除permission
        permissionDAO.delete(permissionId);
    }*/

    private PermissionDTO getPermissionDTO(Permission source) {
        PermissionDTO roleDTO = new PermissionDTO();
        ObjectConvertUtil.objectCopy(roleDTO, source);
        return roleDTO;
    }


}

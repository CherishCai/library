



package cn.cherish.library.service;

import cn.cherish.library.commom.dto.UserDTO;
import cn.cherish.library.commom.enums.ActiveEnum;
import cn.cherish.library.commom.shiro.CryptographyUtil;
import cn.cherish.library.dal.dao.IBaseDAO;
import cn.cherish.library.dal.dao.UserDAO;
import cn.cherish.library.dal.entity.User;
import cn.cherish.library.util.ObjectConvertUtil;
import cn.cherish.library.web.request.BasicSearchReq;
import cn.cherish.library.web.request.user.UserSaveReq;
import cn.cherish.library.web.request.user.UserSearchReq;
import cn.cherish.library.web.request.user.UserUpdateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Scope("prototype")//Shiro的配置影响到了动态代理，现在就这样吧，可以去看MShiroRealm
@Service
@Transactional(readOnly = true)
public class UserService extends ABaseService<User, Long> {

    private final UserDAO userDAO;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    protected IBaseDAO<User, Long> getEntityDAO() {
        return userDAO;
    }

    public User findByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public boolean exist(String username) {
        return userDAO.findByUsername(username) != null;
    }

    public Long getCount() {
        return userDAO.count();
    }

    @Transactional
    public void freezeOrActive(Long userId) {
        User user = findById(userId);
        Integer active = user.getActive();
        user.setActive(1 - active);
        update(user);
    }

    @Transactional
    public void updateByReq(UserUpdateReq userUpdateReq) {
        User user = findById(userUpdateReq.getId());
        ObjectConvertUtil.objectCopy(user, userUpdateReq);
        user.setModifiedTime(new Date());
        update(user);
    }

    @Transactional
    public void saveByReq(UserSaveReq userSaveReq) {
        if (exist(userSaveReq.getUsername())) {
            return;
        }
        User user = new User();
        ObjectConvertUtil.objectCopy(user, userSaveReq);
        user.setCreatedTime(new Date());
        user.setModifiedTime(new Date());
        user.setPassword(CryptographyUtil.cherishSha1(user.getPassword()));
        save(user);
    }

    public Page<UserDTO> findAll(UserSearchReq userSearchReq, BasicSearchReq basicSearchReq) {

        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        PageRequest pageRequest = super.buildPageRequest(pageNumber, basicSearchReq.getPageSize());

        //除了分页条件没有特定搜索条件的，为了缓存count
        if (ObjectConvertUtil.objectFieldIsBlank(userSearchReq)){
            log.debug("没有特定搜索条件的");
            List<User> userList = userDAO.listAllPaged(pageRequest);
            List<UserDTO> userDTOList = userList.stream().map(this::getUserDTO).collect(Collectors.toList());

            //为了计算总数使用缓存，加快搜索速度
            Long count = getCount();
            return new PageImpl<>(userDTOList, pageRequest, count);
        }

        //有了其它搜索条件
        Page<User> userPage = super.findAllBySearchParams(
                buildSearchParams(userSearchReq), pageNumber, basicSearchReq.getPageSize());

        return userPage.map(this::getUserDTO);

    }

    /**
     * 转成DTO
     * @param source User
     * @return UserDTO
     */
    private UserDTO getUserDTO(User source) {
        UserDTO userDTO = new UserDTO();
        ObjectConvertUtil.objectCopy(userDTO, source);
        userDTO.setActiveStr(ActiveEnum.getDesc(source.getActive()));
        return userDTO;
    }



}

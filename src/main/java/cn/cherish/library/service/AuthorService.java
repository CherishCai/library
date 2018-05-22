



package cn.cherish.library.service;

import cn.cherish.library.commom.dto.AuthorDTO;
import cn.cherish.library.commom.enums.ActiveEnum;
import cn.cherish.library.commom.shiro.CryptographyUtil;
import cn.cherish.library.dal.dao.AuthorDAO;
import cn.cherish.library.dal.dao.IBaseDAO;
import cn.cherish.library.dal.entity.Author;
import cn.cherish.library.util.IPv4Util;
import cn.cherish.library.util.MStringUtils;
import cn.cherish.library.util.ObjectConvertUtil;
import cn.cherish.library.util.RequestHolder;
import cn.cherish.library.web.request.BasicSearchReq;
import cn.cherish.library.web.request.author.AuthorRegisterReq;
import cn.cherish.library.web.request.author.AuthorSaveReq;
import cn.cherish.library.web.request.author.AuthorSearchReq;
import cn.cherish.library.web.request.author.AuthorUpdateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Scope("prototype")
@Service
public class AuthorService extends ABaseService<Author, Long> {

    private final AuthorDAO authorDAO;

    @Autowired
    public AuthorService(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    @Override
    protected IBaseDAO<Author, Long> getEntityDAO() {
        return authorDAO;
    }

    public Author findByTelephone(String telephone) {
        return authorDAO.findByTelephone(telephone);
    }

    public Author findByUsername(String username) {
        return authorDAO.findByUsername(username);
    }

    public Long getCount() {
        return authorDAO.count();
    }

    @Transactional
    public void freezeOrActive(Long authorId) {
        Author author = findById(authorId);
        Integer active = author.getActive();
        author.setActive(1 - active);
        update(author);
    }

    @Transactional
    public void save(AuthorSaveReq authorSaveReq) {
        log.info("【新增著作者】 {}", authorSaveReq);
        Author author = new Author();
        ObjectConvertUtil.objectCopy(author, authorSaveReq);
        author.setCreatedTime(new Date());
        author.setModifiedTime(new Date());
        this.update(author);
    }

    @Transactional
    public void update(AuthorUpdateReq authorUpdateReq) {
        log.info("【更新著作者】 {}", authorUpdateReq);
        Author author = findById(authorUpdateReq.getId());
        ObjectConvertUtil.objectCopy(author, authorUpdateReq);
        author.setModifiedTime(new Date());
        log.info("【更改后】 {}", author);
        this.update(author);
    }

    @Transactional
    public void register(AuthorRegisterReq authorRegisterReq) {
        log.info("【注册著作者】 {}", authorRegisterReq);
        Author author = new Author();
        ObjectConvertUtil.objectCopy(author, authorRegisterReq);
        author.setCreatedTime(new Date());
        author.setModifiedTime(new Date());
        // 设置密码 ip
        author.setPassword(CryptographyUtil.cherishSha1(author.getPassword()));
        author.setIp(IPv4Util.ipToInt(MStringUtils.getIpAddress(RequestHolder.getRequest())));
        // 待邮箱激活
        author.setActive(ActiveEnum.UN_CHECK_EMAIL.getNum());

        author = this.save(author);
    }

    public Page<AuthorDTO> findAll(BasicSearchReq basicSearchReq, AuthorSearchReq authorSearchReq) {
        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        Page<Author> authorPage = super.findAllBySearchParams(
                buildSearchParams(authorSearchReq), pageNumber, basicSearchReq.getPageSize());
        return authorPage.map(this::getAuthorDTO);
    }

    /**
     * 转成DTO
     * @param source Author
     * @return AuthorDTO
     */
    private AuthorDTO getAuthorDTO(Author source) {
        AuthorDTO authorDTO = new AuthorDTO();
        ObjectConvertUtil.objectCopy(authorDTO, source);
        authorDTO.setActiveStr(ActiveEnum.getDesc(source.getActive()));
        return authorDTO;
    }

    @Transactional
    public boolean existUsername(String username) {
        Author author = authorDAO.findByUsername(username);
        if (author == null) {
            return false;
        }
        if (author.getActive().equals(ActiveEnum.UN_CHECK_EMAIL.getNum())){
            //邮箱未验证的，删除他，返回false
            authorDAO.delete(author);
            return false;
        }
        return true;
    }



}

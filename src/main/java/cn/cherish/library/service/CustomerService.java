



package cn.cherish.library.service;

import cn.cherish.library.commom.dto.CustomerDTO;
import cn.cherish.library.commom.enums.ActiveEnum;
import cn.cherish.library.commom.shiro.CryptographyUtil;
import cn.cherish.library.dal.dao.CustomerDAO;
import cn.cherish.library.dal.dao.IBaseDAO;
import cn.cherish.library.dal.entity.Customer;
import cn.cherish.library.util.ObjectConvertUtil;
import cn.cherish.library.web.request.BasicSearchReq;
import cn.cherish.library.web.request.customer.CustomerRegisterReq;
import cn.cherish.library.web.request.customer.CustomerSaveReq;
import cn.cherish.library.web.request.customer.CustomerSearchReq;
import cn.cherish.library.web.request.customer.CustomerUpdateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class CustomerService extends ABaseService<Customer, Long> {

    private final CustomerDAO customerDAO;

    @Autowired
    public CustomerService(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    @Override
    protected IBaseDAO<Customer, Long> getEntityDAO() {
        return customerDAO;
    }

    public Customer findByUsername(String username) {
        return customerDAO.findByUsername(username);
    }

    public Long getCount() {
        return customerDAO.count();
    }

    @Transactional
    public void freezeOrActive(Long customerId) {
        Customer customer = findById(customerId);
        Integer active = customer.getActive();
        customer.setActive(1 - active);
        customerDAO.save(customer);
    }

    @Transactional
    public Customer save(CustomerSaveReq customerSaveReq) {
        log.info("【新增借阅卡】 {}", customerSaveReq);
        Customer customer = new Customer();
        ObjectConvertUtil.objectCopy(customer, customerSaveReq);
        customer.setCreatedTime(new Date());
        customer.setModifiedTime(new Date());
        return customerDAO.save(customer);
    }

    @Transactional
    public Customer update(CustomerUpdateReq customerUpdateReq) {
        log.info("【更新借阅卡】 {}", customerUpdateReq);
        Customer customer = findById(customerUpdateReq.getId());
        ObjectConvertUtil.objectCopy(customer, customerUpdateReq);
        customer.setModifiedTime(new Date());
        return customerDAO.save(customer);
    }

    @Transactional
    public void register(CustomerRegisterReq customerRegisterReq) {
        log.info("【注册借阅卡】 {}", customerRegisterReq);
        Customer customer = new Customer();
        ObjectConvertUtil.objectCopy(customer, customerRegisterReq);
        customer.setCreatedTime(new Date());
        customer.setModifiedTime(new Date());
        // 设置密码 此时的ip
        customer.setPassword(CryptographyUtil.cherishSha1(customer.getPassword()));
        // 待邮箱激活
        customer.setActive(ActiveEnum.UN_CHECK_EMAIL.getNum());

        customer = this.save(customer);
    }

    public Page<CustomerDTO> findAll(BasicSearchReq basicSearchReq, CustomerSearchReq customerSearchReq) {
        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        Page<Customer> customerPage = super.findAllBySearchParams(
                buildSearchParams(customerSearchReq), pageNumber, basicSearchReq.getPageSize());
        return customerPage.map(this::getCustomerDTO);
    }

    /**
     * 转成DTO
     * @param source Customer
     * @return CustomerDTO
     */
    private CustomerDTO getCustomerDTO(Customer source) {
        CustomerDTO customerDTO = new CustomerDTO();
        ObjectConvertUtil.objectCopy(customerDTO, source);
        customerDTO.setActiveStr(ActiveEnum.getDesc(source.getActive()));
        return customerDTO;
    }

    @Transactional
    public boolean existUsername(String username) {
        Customer customer = customerDAO.findByUsername(username);
        return customer != null;
    }


}

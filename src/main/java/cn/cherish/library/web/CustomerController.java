



package cn.cherish.library.web;

import cn.cherish.library.commom.dto.CustomerDTO;
import cn.cherish.library.commom.enums.ActiveEnum;
import cn.cherish.library.commom.shiro.CryptographyUtil;
import cn.cherish.library.dal.entity.Customer;
import cn.cherish.library.service.CustomerService;
import cn.cherish.library.util.IPv4Util;
import cn.cherish.library.util.MStringUtils;
import cn.cherish.library.util.RequestHolder;
import cn.cherish.library.util.SessionUtil;
import cn.cherish.library.web.request.BasicSearchReq;
import cn.cherish.library.web.request.LoginReq;
import cn.cherish.library.web.request.ModifyPasswordReq;
import cn.cherish.library.web.request.customer.CustomerRegisterReq;
import cn.cherish.library.web.request.customer.CustomerSaveReq;
import cn.cherish.library.web.request.customer.CustomerSearchReq;
import cn.cherish.library.web.request.customer.CustomerUpdateReq;
import cn.cherish.library.web.response.Response;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("customer")
public class CustomerController extends ABaseController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 借阅卡中心页面
     * @return ModelAndView
     */
    @GetMapping({"","/","/index"})
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("customer/index");
        // 查询个人信息
        Customer customer = SessionUtil.getCustomer();
        if (customer == null) {
            mv.setViewName("redirect:/customer/login");
            SessionUtil.add("url", MStringUtils.getRequestURLWithQueryString(RequestHolder.getRequest()));
        }else {
            mv.addObject("customer", customer);
        }
        return mv;
    }
    /**
     * 借阅卡注册页面
     * @return ModelAndView
     */
    @GetMapping("/register")
    public ModelAndView register(){
        ModelAndView mv = new ModelAndView("customer/register");
        return mv;
    }
    /**
     * 管理员所看的 借阅卡列表页面
     * @return ModelAndView
     */
    @RequiresRoles("admin")
    @GetMapping("/list")
    public ModelAndView list(){
        ModelAndView mv = new ModelAndView("admin/customer/list");
        return mv;
    }

    /**
     * 返回新增页面
     */
    @RequiresRoles("admin")
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/customer/add");
        return mv;
    }

    /**
     * 返回修改信息页面
     */
    @RequiresRoles("admin")
    @GetMapping("/{customerId}/update")
    public ModelAndView updateForm(@PathVariable("customerId") Long customerId){
        ModelAndView mv = new ModelAndView("admin/customer/edit");
        Customer customer = customerService.findById(customerId);
        mv.addObject(customer);
        return mv;
    }

    /**
     * 分页查询
     * @param basicSearchReq 基本搜索条件
     * @return JSON
     * @date 2016年8月30日 下午5:30:18
     */
    @RequiresRoles("admin")
    @GetMapping("/page")
    @ResponseBody
    public Response toPage(BasicSearchReq basicSearchReq, CustomerSearchReq customerSearchReq){
        Page<CustomerDTO> page = customerService.findAll(basicSearchReq, customerSearchReq);
        return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
    }

    /**
     * 删除
     * @param customerId ID
     * @return JSON
     */
    @RequiresRoles("admin")
    @DeleteMapping("/{customerId}/delete")
    @ResponseBody
    public Response delete(@PathVariable("customerId") Long customerId){
        customerService.delete(customerId);
        return buildResponse(Boolean.TRUE, "删除成功", null);
    }
    /**
     * 冻结或激活
     * @param customerId ID
     * @return JSON
     */
    @RequiresRoles("admin")
    @GetMapping("/{customerId}/freezeOrActive")
    @ResponseBody
    public Response freezeOrActive(@PathVariable("customerId") Long customerId){
        customerService.freezeOrActive(customerId);
        return buildResponse(Boolean.TRUE, "操作成功", null);
    }
    /**
     * 新增借阅卡
     * @param customerSaveReq 参数
     * @param bindingResult 验证
     * @return ModelAndView
     */
    @RequiresRoles("admin")
    @PostMapping("/save")
    public ModelAndView save(@Validated CustomerSaveReq customerSaveReq, BindingResult bindingResult) {
        log.info("【新增借阅卡】 {}", customerSaveReq);
        ModelAndView mv = new ModelAndView("admin/customer/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("customer", customerSaveReq);
            return mv;
        }
        try {
            boolean existUsername = customerService.existUsername(customerSaveReq.getUsername());
            if (existUsername){
                errorMap.put("msg", "该登录账号已被注册，请更换");
                mv.addObject("customer", customerSaveReq);
                return mv;
            }
            // 设置密码 此时的ip
            customerSaveReq.setPassword(CryptographyUtil.cherishSha1(customerSaveReq.getPassword()));
            customerSaveReq.setIp(IPv4Util.ipToInt(MStringUtils.getIpAddress(RequestHolder.getRequest())));
            customerService.save(customerSaveReq);

            errorMap.put("msg", "添加成功");
        } catch (Exception e) {
            errorMap.put("msg", "系统繁忙");
            log.error("【添加失败】 {}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }

    /**
     * 更改信息
     * @param updateReq 更新信息
     * @return ModelAndView
     */
    @RequiresRoles("admin")
    @PostMapping("/update")
    public ModelAndView update(@Validated CustomerUpdateReq updateReq, BindingResult bindingResult){
        log.info("【更改信息】 {}", updateReq);
        ModelAndView mv = new ModelAndView("admin/customer/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(updateReq == null || updateReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }
        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("customer", updateReq);
            return mv;
        }
        try {
            customerService.update(updateReq);
            mv.addObject("customer", customerService.findById(updateReq.getId()));
            errorMap.put("msg", "修改成功");
        } catch (Exception e) {
            errorMap.put("msg", "系统繁忙");
            log.error("修改错误:{}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }

    /**
     * 注册借阅卡
     * @param customerRegisterReq 参数
     * @param bindingResult 验证
     * @return ModelAndView
     */
    @PostMapping("/register")
    public ModelAndView register(@Validated CustomerRegisterReq customerRegisterReq, BindingResult bindingResult) {
        log.info("【注册借阅卡】 {}",customerRegisterReq);
        ModelAndView mv = new ModelAndView("customer/register");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("customer", customerRegisterReq);
            return mv;
        }
        if (StringUtils.isBlank(customerRegisterReq.getPassword())
                ||!StringUtils.equals(customerRegisterReq.getPassword(), customerRegisterReq.getRepeatPwd())) {
            errorMap.put("msg", "两处密码不一致");
            mv.addObject("customer", customerRegisterReq);
            return mv;
        }
        try {
            boolean existUsername = customerService.existUsername(customerRegisterReq.getUsername());
            if (existUsername){
                errorMap.put("msg", "该登录账号已被注册，请更换");
                mv.addObject("customer", customerRegisterReq);
                return mv;
            }

           customerService.register(customerRegisterReq);
            errorMap.put("msg", "信息提交成功，请登录您的邮箱激活账号");
            mv.setViewName("index");
        } catch (Exception e) {
            errorMap.put("msg", "系统繁忙");
            log.error("添加失败:{}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }


    /**
     * 提交密码更改请求
     * @return ResponseBody
     */
    @PostMapping("/modifyPwd")
    @ResponseBody
    public Response modifyPwd(ModifyPasswordReq modifyPasswordReq) {
        log.info("【密码更改】 {}", modifyPasswordReq);
        Customer customer = SessionUtil.getCustomer();
        if (customer == null) {
            return buildResponse(Boolean.FALSE, "未有记录，请去登陆", Boolean.FALSE);
        }

        if (StringUtils.isBlank(modifyPasswordReq.getPassword())
                || StringUtils.isBlank(modifyPasswordReq.getRepeatPassword())
                || !StringUtils.equals(modifyPasswordReq.getPassword(), modifyPasswordReq.getRepeatPassword())
            ) {
            return buildResponse(Boolean.FALSE, "两次输入的密码不一致", null);
        }

        if (!customer.getPassword().equals(CryptographyUtil.cherishSha1(modifyPasswordReq.getOldPassword()))) {
            return buildResponse(Boolean.FALSE, "密码认证错误", null);
        }

        customer.setPassword(CryptographyUtil.cherishSha1(modifyPasswordReq.getPassword()));
        customer = customerService.update(customer);
        SessionUtil.addCustomer(customer);

        return buildResponse(Boolean.TRUE, "更改成功", null);
    }

    /**
     * 借阅卡信息修改请求
     * @return ResponseBody
     */
    @PostMapping("/updateMyself")
    @ResponseBody
    public Response updateMyself(CustomerUpdateReq customerUpdateReq) {
        log.info("【信息修改】 {}", customerUpdateReq);
        Customer customer = SessionUtil.getCustomer();
        if (customer == null) {
            return buildResponse(Boolean.FALSE, "未有记录，请去登陆", Boolean.FALSE);
        }

        customerUpdateReq.setId(customer.getId());
        customer = customerService.update(customerUpdateReq);
        SessionUtil.addCustomer(customer);
        return buildResponse(Boolean.TRUE, "更改成功", null);
    }

    /**
     * 借阅卡登陆
     * @return ResponseBody
     */
    @PostMapping("/login")
    public ModelAndView login(@Validated LoginReq loginReq, BindingResult bindingResult, HttpServletRequest request) {
        log.info("【借阅卡登陆】 {}", loginReq);
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customer/login");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        String code = (String) request.getSession().getAttribute("validateCode");
        String submitCode = WebUtils.getCleanParam(request, "validateCode");
        log.debug("code: {}  submitCode: {}", code, submitCode);

        //判断验证码
        if (StringUtils.isBlank(submitCode) || !StringUtils.equalsIgnoreCase(code, submitCode.toLowerCase())) {
            log.debug("验证码不正确");
            errorMap.put("validateCodeError", "验证码不正确");
            return mv;
        }
        //表单验证是否通过
        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            return mv;
        }

        Customer customer = customerService.findByUsername(loginReq.getUsername());
        if (customer == null) {
            errorMap.put("username", "账户或密码错误，请重新输入");
            return mv;
        }
        
        String password = CryptographyUtil.cherishSha1(loginReq.getPassword());
        if (!StringUtils.equals(password, customer.getPassword())) {
            log.debug("密码不正确");
            log.debug("输入的密码：{} 真实密码：{}", password, customer.getPassword());
            errorMap.put("username", "账户或密码错误，请重新输入");
            return mv;
        }
        
        // 被禁用
        if (!Objects.equals(ActiveEnum.ACTIVE.getNum(), customer.getActive())) {
            log.debug("账号被禁用");
            errorMap.put("username", "账号被禁用");
            return mv;
        }
        
        // 登陆成功
        SessionUtil.addCustomer(customer);
        SessionUtil.add("isLogin", true);
        SessionUtil.add("customerName", customer.getNickname());

        String url = (String)SessionUtil.get("url");
        if (url != null) {
            mv.setViewName("redirect:" + url);
            SessionUtil.del("url");
        }else {
            mv.setViewName("redirect:/customer");
        }
        return mv;
    }

    /**
     * 是否已登陆
     * @return ResponseBody
     */
    @PostMapping("/isLogin")
    @ResponseBody
    public Response isLogin() {
        Customer customer = SessionUtil.getCustomer();
        if (customer == null) {
            return buildResponse(Boolean.FALSE, "未有记录，请去登陆", Boolean.FALSE);
        }
        return buildResponse(Boolean.TRUE, "已登陆", customer.getNickname());
    }

    /**
     * 登录页面
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView("customer/login");
        String msg = (String)SessionUtil.get("msg");
        mv.addObject("msg", msg);
        SessionUtil.del("msg");
        return mv;
    }
    /**
     * 登出
     * @return ModelAndView
     */
    @GetMapping("/logout")
    public ModelAndView logout() {
        SessionUtil.delCustomer();
        SessionUtil.add("isLogin", false);
        return new ModelAndView("redirect:/");
    }


}

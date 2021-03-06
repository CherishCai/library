
package cn.cherish.library.web;

import cn.cherish.library.commom.dto.AuthorDTO;
import cn.cherish.library.commom.shiro.CryptographyUtil;
import cn.cherish.library.commom.shiro.ShiroUserUtil;
import cn.cherish.library.dal.entity.Author;
import cn.cherish.library.service.AuthorService;
import cn.cherish.library.service.TreatiseService;
import cn.cherish.library.util.IPv4Util;
import cn.cherish.library.util.MStringUtils;
import cn.cherish.library.util.RequestHolder;
import cn.cherish.library.web.request.BasicSearchReq;
import cn.cherish.library.web.request.ModifyPasswordReq;
import cn.cherish.library.web.request.author.AuthorSaveReq;
import cn.cherish.library.web.request.author.AuthorSearchReq;
import cn.cherish.library.web.request.author.AuthorUpdateReq;
import cn.cherish.library.web.response.Response;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 著作者控制器
 *
 */
@Controller
@RequestMapping("author")
public class AuthorController extends ABaseController {

    private final AuthorService authorService;
    private final TreatiseService treatiseService;

    @Autowired
    public AuthorController(AuthorService authorService, TreatiseService treatiseService) {
        this.authorService = authorService;
        this.treatiseService = treatiseService;
    }

    /**
     * 著作者中心页面
     * @return ModelAndView
     */
    @RequiresRoles("author")
    @GetMapping({"","/","/index"})
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("author/index");
        // 查询个人信息
        Author author = authorService.findById(ShiroUserUtil.getUserId());
        mv.addObject("author", author);
        // 著作信息
        return mv;
    }
    /**
     * 著作者注册页面
     * @return ModelAndView
     */
    /*@GetMapping("/register")
    public ModelAndView register(){
        ModelAndView mv = new ModelAndView("author/register");
        return mv;
    }*/
    /**
     * 管理员所看的 著作者列表页面
     * @return ModelAndView
     */
    @RequiresRoles("admin")
    @GetMapping("/list")
    public ModelAndView list(){
        ModelAndView mv = new ModelAndView("admin/author/list");
        return mv;
    }

    /**
     * 返回新增页面
     */
    @RequiresRoles("admin")
    @GetMapping("/add")
    public ModelAndView addForm(){
        ModelAndView mv = new ModelAndView("admin/author/add");
        return mv;
    }

    /**
     * 返回修改信息页面
     */
    @RequiresRoles("admin")
    @GetMapping("/{authorId}/update")
    public ModelAndView updateForm(@PathVariable("authorId") Long authorId){
        ModelAndView mv = new ModelAndView("admin/author/edit");
        Author author = authorService.findById(authorId);
        mv.addObject(author);
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
    public Response toPage(BasicSearchReq basicSearchReq, AuthorSearchReq authorSearchReq){
        Page<AuthorDTO> page = authorService.findAll(basicSearchReq, authorSearchReq);
        return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
    }

    /**
     * 冻结或激活
     * @param authorId ID
     * @return JSON
     */
    @RequiresRoles("admin")
    @GetMapping("/{authorId}/freezeOrActive")
    @ResponseBody
    public Response freezeOrActive(@PathVariable("authorId") Long authorId){
        authorService.freezeOrActive(authorId);
        return buildResponse(Boolean.TRUE, "操作成功", null);
    }
    /**
     * 新增著作者
     * @param authorSaveReq 参数
     * @param bindingResult 验证
     * @return ModelAndView
     */
    @RequiresRoles("admin")
    @PostMapping("/save")
    public ModelAndView save(@Validated AuthorSaveReq authorSaveReq, BindingResult bindingResult) {
        log.info("【新增著作者】 {}", authorSaveReq);
        ModelAndView mv = new ModelAndView("admin/author/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("author", authorSaveReq);
            return mv;
        }
        try {
            boolean existUsername = authorService.existUsername(authorSaveReq.getUsername());
            if (existUsername){
                errorMap.put("msg", "该登录账号已被注册，请更换");
                mv.addObject("author", authorSaveReq);
                return mv;
            }
            // 设置密码 此时的ip
            authorSaveReq.setPassword(CryptographyUtil.cherishSha1(authorSaveReq.getPassword()));
            authorSaveReq.setIp(IPv4Util.ipToInt(MStringUtils.getIpAddress(RequestHolder.getRequest())));
            authorService.save(authorSaveReq);
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
    public ModelAndView update(@Validated AuthorUpdateReq updateReq, BindingResult bindingResult){
        log.info("【更改信息】 {}", updateReq);
        ModelAndView mv = new ModelAndView("admin/author/edit");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(updateReq == null || updateReq.getId() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }
        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("author", updateReq);
            return mv;
        }
        try {
            Author old = authorService.findById(updateReq.getId());
            if (old == null) {
                errorMap.put("msg", "数据错误");
                return mv;
            }
            authorService.update(updateReq);
            mv.addObject("author", authorService.findById(updateReq.getId()));
            errorMap.put("msg", "修改成功");
        } catch (Exception e) {
            errorMap.put("msg", "系统繁忙");
            log.error("修改错误:{}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }

    /**
     * 注册著作者
     * @param authorRegisterReq 参数
     * @param bindingResult 验证
     * @return ModelAndView
     */
    /*@PostMapping("/register")
    public ModelAndView register(@Validated AuthorRegisterReq authorRegisterReq, BindingResult bindingResult) {
        log.info("【注册著作者】 {}",authorRegisterReq);
        ModelAndView mv = new ModelAndView("author/register");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
            mv.addObject("author", authorRegisterReq);
            return mv;
        }
        if (StringUtils.isBlank(authorRegisterReq.getPassword())
                ||!StringUtils.equals(authorRegisterReq.getPassword(), authorRegisterReq.getRepeatPwd())) {
            errorMap.put("msg", "两处密码不一致");
            mv.addObject("author", authorRegisterReq);
            return mv;
        }
        try {
            boolean existEmail = authorService.existEmail(authorRegisterReq.getEmail());
            if (existEmail){
                errorMap.put("msg", "该邮箱已注册");
                mv.addObject("author", authorRegisterReq);
                return mv;
            }
            boolean existUsername = authorService.existUsername(authorRegisterReq.getUsername());
            if (existUsername){
                errorMap.put("msg", "该登录账号已被注册，请更换");
                mv.addObject("author", authorRegisterReq);
                return mv;
            }

            authorService.register(authorRegisterReq);
            errorMap.put("msg", "信息提交成功，请登录您的邮箱激活账号");
            mv.setViewName("login");
        } catch (Exception e) {
            errorMap.put("msg", "系统繁忙");
            log.error("添加失败:{}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }*/

    /**
     * 提交密码更改请求
     * @return ResponseBody
     */
    @RequiresRoles("author")
    @PostMapping("/modifyPwd")
    @ResponseBody
    public Response modifyPwd(ModifyPasswordReq modifyPasswordReq) {
        log.info("【密码更改】 {}", modifyPasswordReq);
        if (StringUtils.isBlank(modifyPasswordReq.getPassword())
                || StringUtils.isBlank(modifyPasswordReq.getRepeatPassword())
                || !StringUtils.equals(modifyPasswordReq.getPassword(), modifyPasswordReq.getRepeatPassword())
            ) {
            return buildResponse(Boolean.FALSE, "两次输入的密码不一致", null);
        }

        Author author = authorService.findById(ShiroUserUtil.getUserId());
        if (!author.getPassword().equals(CryptographyUtil.cherishSha1(modifyPasswordReq.getOldPassword()))) {
            return buildResponse(Boolean.FALSE, "密码认证错误", null);
        }

        author.setPassword(CryptographyUtil.cherishSha1(modifyPasswordReq.getPassword()));
        authorService.update(author);
        return buildResponse(Boolean.TRUE, "更改成功", null);
    }

    /**
     * 著作者信息修改请求
     * @return ResponseBody
     */
    @RequiresRoles("author")
    @PostMapping("/updateMyself")
    @ResponseBody
    public Response updateMyself(AuthorUpdateReq authorUpdateReq) {
        log.info("【信息修改】 {}", authorUpdateReq);
        authorUpdateReq.setId(ShiroUserUtil.getUserId());
        authorService.update(authorUpdateReq);
        return buildResponse(Boolean.TRUE, "更改成功", null);
    }


}

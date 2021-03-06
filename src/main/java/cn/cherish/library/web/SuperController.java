



package cn.cherish.library.web;

import cn.cherish.library.service.SuperService;
import cn.cherish.library.web.request.su.SuperRolePermissionReq;
import cn.cherish.library.web.request.su.SuperUserRoleReq;
import com.google.common.base.Throwables;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;


/**
 * 超级用户控制器
 *
 */
@Controller
@RequestMapping("super")
@RequiresRoles("super")
public class SuperController extends ABaseController {

    private final SuperService superService;

    @Autowired
    public SuperController(SuperService superService) {
        this.superService = superService;
    }

    @ModelAttribute
    public void roles(Model model) {
        model.addAttribute("roles", superService.listAllRole());
    }

    @GetMapping
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("admin/super/manage");
        return mv;
    }

    @RequestMapping("/findUser")
    public ModelAndView findUser(String username) {
        ModelAndView mv = new ModelAndView("admin/super/manage");
        mv.addObject("username", username);
        mv.addObject("superUserRole", superService.findByUsername(username));
        return mv;
    }

    @RequestMapping("/findRole")
    public ModelAndView findRole(String rolename) {
        ModelAndView mv = new ModelAndView("admin/super/manage");
        mv.addObject("rolename", rolename);
        mv.addObject("superRolePermission", superService.findByRolename(rolename));
        return mv;
    }

    /**
     * 更改用户的角色
     * @param superUserRoleReq 用户ID与角色ID
     * @return ModelAndView
     */
    @PostMapping("/updateUserRole")
    public ModelAndView updateUserRole(@Validated SuperUserRoleReq superUserRoleReq, BindingResult bindingResult){

        ModelAndView mv = new ModelAndView("admin/super/manage");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(superUserRoleReq == null || superUserRoleReq.getUsername() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
        }else {
            try {
                superService.updateUserRole(superUserRoleReq);
                errorMap.put("msg", "修改成功");
            } catch (Exception e) {
                errorMap.put("msg", "系统繁忙");
                log.error("修改错误:{}", Throwables.getStackTraceAsString(e));
            }
        }
        return mv;
    }

    /**
     * 更改角色的权限
     * @param superRolePermissionReq 角色ID与权限ID
     * @return ModelAndView
     */
    @PostMapping("/updateRolePermission")
   public ModelAndView updateRolePermission(@Validated SuperRolePermissionReq superRolePermissionReq, BindingResult bindingResult){

        ModelAndView mv = new ModelAndView("admin/super/manage");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if(superRolePermissionReq == null || superRolePermissionReq.getRolename() == null){
            errorMap.put("msg", "数据错误");
            return mv;
        }
        log.debug("superRolePermissionReq = " + superRolePermissionReq);

        if (bindingResult.hasErrors()) {
            errorMap.putAll(getErrors(bindingResult));
        }else {
            try {
                superService.updateRolePermission(superRolePermissionReq);
                errorMap.put("msg", "修改角色权限成功");
            } catch (Exception e) {
                errorMap.put("msg", "系统繁忙");
                log.error("修改错误:{}", Throwables.getStackTraceAsString(e));
            }
        }
        return mv;
    }



}

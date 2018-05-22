package cn.cherish.library.web;

import cn.cherish.library.dal.entity.Record;
import cn.cherish.library.service.RecordService;
import cn.cherish.library.web.request.BasicSearchReq;
import cn.cherish.library.web.response.Response;
import com.google.common.base.Throwables;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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

@Controller
@RequestMapping("record")
@RequiresAuthentication
public class RecordController extends ABaseController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping({"", "/list"})
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("admin/record/list");
        return mv;
    }

    /**
     * 分页查询
     * @param basicSearchReq 基本搜索条件
     * @return JSON
     */
    @GetMapping("/page")
    @ResponseBody
    public Response toPage(BasicSearchReq basicSearchReq, Record recordSearchReq) {
        log.info("【分页查询】 {}", recordSearchReq);
        Page<Record> page = recordService.findAll(recordSearchReq, basicSearchReq);
        return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
    }

    /**
     * 返回新增的页面
     */
    @GetMapping("/add")
    public ModelAndView addForm() {
        ModelAndView mv = new ModelAndView("admin/record/add");
        return mv;
    }

    /**
     * 返回修改信息的页面
     */
    @GetMapping("/{recordId}/update")
    public ModelAndView updateForm(@PathVariable("recordId") Long recordId) {
        ModelAndView mv = new ModelAndView("admin/record/edit");
        Record record = recordService.findById(recordId);
        mv.addObject("record", record);
        return mv;
    }

    /**
     * 返回修改信息的详情页面
     */
    @GetMapping("/{recordId}/info")
    @RequiresPermissions("record:update")
    public ModelAndView infoForm(@PathVariable("recordId") Long recordId) {
        ModelAndView mv = new ModelAndView("admin/record/info");
        Record record = recordService.findById(recordId);
        mv.addObject("record", record);
        return mv;
    }

    /**
     * 删除
     * @param recordId ID
     * @return JSON
     */
    @DeleteMapping("/{recordId}/delete")
    @ResponseBody
    public Response delete(@PathVariable("recordId") Long recordId) {
        recordService.delete(recordId);
        return buildResponse(Boolean.TRUE, "删除成功", null);
    }

    /**
     * 更改信息
     * @param recordUpdateReq 更新信息
     * @return ModelAndView
     */
    @PostMapping("/update")
    @ResponseBody
    public Response update(Record recordUpdateReq) {
        log.info("【更改信息】 {}", recordUpdateReq);
        Record record = recordService.update(recordUpdateReq);
        return buildResponse(Boolean.TRUE, "保存成功", record);
    }

    /**
     * 更改核心信息
     * @param recordUpdateReq 更新信息
     * @return ModelAndView
     */
    @PostMapping("/updateCore")
    @ResponseBody
    public Response updateCore(Record recordUpdateReq) {
        log.info("【更改信息】 {}", recordUpdateReq);
        Record record = recordService.update(recordUpdateReq);
        return buildResponse(Boolean.TRUE, "保存成功", record);
    }

    /**
     * 保存核心信息
     * @param record1 保存的信息
     * @return ModelAndView
     */
    @PostMapping("/saveCore")
    @ResponseBody
    public Response saveCore(Record record1) {
        log.info("【保存核心信息】 {}", record1);
        Record record = recordService.save(record1);
        return buildResponse(Boolean.TRUE, "保存成功", record);
    }

    /**
     * 返回修改信息
     */
    @GetMapping("/{recordId}")
    @ResponseBody
    public Response info(@PathVariable("recordId") Long recordId) {
        Record record = recordService.findById(recordId);
        if (record == null) {// 404
            return buildResponse(Boolean.FALSE, "此资源找不到", null);
        }
        return buildResponse(Boolean.TRUE, "", record);
    }


    /**
     * 新增借阅卡
     * @param record 参数
     * @param bindingResult 验证
     * @return ModelAndView
     */
    @RequiresRoles("admin")
    @PostMapping("/save")
    public ModelAndView save(@Validated Record record, BindingResult bindingResult) {
        log.info("【新增借阅卡】 {}", record);
        ModelAndView mv = new ModelAndView("admin/record/add");
        Map<String, Object> errorMap = new HashMap<>();
        mv.addObject("errorMap", errorMap);

        if (bindingResult.hasErrors()) {
            mv.addObject("record", record);
            return mv;
        }
        try {
            recordService.save(record);
            errorMap.put("msg", "添加成功");
        } catch (Exception e) {
            errorMap.put("msg", "系统繁忙");
            log.error("【添加失败】 {}", Throwables.getStackTraceAsString(e));
        }
        return mv;
    }


}

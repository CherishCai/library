


package cn.cherish.library.web;

import cn.cherish.library.commom.dto.TreatiseDTO;
import cn.cherish.library.commom.enums.Language;
import cn.cherish.library.commom.enums.PublicationMode;
import cn.cherish.library.dal.entity.Author;
import cn.cherish.library.dal.entity.Treatise;
import cn.cherish.library.service.AuthorService;
import cn.cherish.library.service.TreatiseService;
import cn.cherish.library.web.request.BasicSearchReq;
import cn.cherish.library.web.request.treatise.TreatiseSaveCoreReq;
import cn.cherish.library.web.request.treatise.TreatiseSearchReq;
import cn.cherish.library.web.request.treatise.TreatiseUpdateReq;
import cn.cherish.library.web.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("treatise")
@RequiresAuthentication
public class TreatiseController extends ABaseController {
    private static final String SPLITER = "`/`/`/`/`/`";
    private final TreatiseService treatiseService;
    private final AuthorService authorService;

    @Autowired
    public TreatiseController(TreatiseService treatiseService, AuthorService authorService) {
        this.treatiseService = treatiseService;
        this.authorService = authorService;
    }

    @ModelAttribute("language")
    public List<Language> language() {
        return Arrays.asList(Language.values());
    }

    @ModelAttribute("publicationMode")
    public List<PublicationMode> publicationMode() {
        return Arrays.asList(PublicationMode.values());
    }

    @ModelAttribute("authors")
    public List<Author> authors() {
        return authorService.findAll();
    }

    @GetMapping({"", "/list"})
    @RequiresPermissions("treatise:show")
    public ModelAndView list() {
        ModelAndView mv = new ModelAndView("admin/treatise/list");
        return mv;
    }

    /**
     * 分页查询
     *
     * @param basicSearchReq 基本搜索条件
     * @return JSON
     * @date 2016年8月30日 下午5:30:18
     */
    @GetMapping("/page")
    @ResponseBody
    public Response toPage(BasicSearchReq basicSearchReq, TreatiseSearchReq treatiseSearchReq) {
        log.info("【分页查询】 {}", treatiseSearchReq);
        Page<TreatiseDTO> page = treatiseService.findAll(treatiseSearchReq, basicSearchReq);
        return buildResponse(Boolean.TRUE, basicSearchReq.getDraw(), page);
    }

    /**
     * 返回新增著作的页面
     */
    @GetMapping("/add")
    @RequiresPermissions("treatise:add")
    public ModelAndView addForm() {
        ModelAndView mv = new ModelAndView("admin/treatise/add");
        return mv;
    }

    /**
     * 返回修改著作信息的页面
     */
    @GetMapping("/{treatiseId}/update")
    @RequiresPermissions("treatise:update")
    public ModelAndView updateForm(@PathVariable("treatiseId") Long treatiseId) {
        ModelAndView mv = new ModelAndView("admin/treatise/edit");
        Treatise treatise = treatiseService.findById(treatiseId);
        mv.addObject("treatise", treatise);
        return mv;
    }

    private static List<String> getStringList(String review) {
        List<String> list = new ArrayList<>();
        String[] strings = review.split(SPLITER);
        for(String s : strings) {
            if(!StringUtils.isEmpty(s)) list.add(s);
        }
        if(list.size() == 0) list.add("");
        return list;
    }

    /**
     * 返回修改著作信息的详情页面
     */
    @GetMapping("/{treatiseId}/info")
    @RequiresPermissions("treatise:update")
    public ModelAndView infoForm(@PathVariable("treatiseId") Long treatiseId) {
        ModelAndView mv = new ModelAndView("admin/treatise/info");
        Treatise treatise = treatiseService.findById(treatiseId);
        mv.addObject("treatise", treatise);
        return mv;
    }

    @ResponseBody
    @RequestMapping("/{treatiseId}/jsonInfo")
    public TreatiseDTO getTreatiseById(@PathVariable("treatiseId") Long treatiseId) {
        TreatiseDTO dto = treatiseService.findOne(treatiseId);
        dto.setIntroductoryList(getStringList(dto.getIntroductory()));
        dto.setReviewList(getStringList(dto.getReview()));
        return dto;
    }

    /**
     * 删除
     * @param treatiseId ID
     * @return JSON
     */
    @DeleteMapping("/{treatiseId}/delete")
    @ResponseBody
    @RequiresPermissions("treatise:delete")
    public Response delete(@PathVariable("treatiseId") Long treatiseId) {
        treatiseService.delete(treatiseId);
        return buildResponse(Boolean.TRUE, "删除成功", null);
    }

    /**
     * 更改著作信息
     * @param treatiseUpdateReq 更新信息
     * @return ModelAndView
     */
    @PostMapping("/update")
    @RequiresPermissions("treatise:update")
    @ResponseBody
    public Response update(TreatiseUpdateReq treatiseUpdateReq) {
        log.info("【更改信息】 {}", treatiseUpdateReq);
        TreatiseDTO treatise = treatiseService.update(treatiseUpdateReq);
        return buildResponse(Boolean.TRUE, "保存成功", treatise);
    }

    /**
     * 更改著作核心信息
     * @param treatiseUpdateReq 更新信息
     * @return ModelAndView
     */
    @PostMapping("/updateCore")
    @RequiresPermissions("treatise:update")
    @ResponseBody
    public Response updateCore(TreatiseUpdateReq treatiseUpdateReq) {
        log.info("【更改信息】 {}", treatiseUpdateReq);
        TreatiseDTO treatise = treatiseService.updateCore(treatiseUpdateReq);
        return buildResponse(Boolean.TRUE, "保存成功", treatise);
    }

    /**
     * 保存核心信息
     * @param treatiseSaveReq 保存的信息
     * @return ModelAndView
     */
    @PostMapping("/saveCore")
    @RequiresPermissions("treatise:add")
    @ResponseBody
    public Response saveCore(TreatiseSaveCoreReq treatiseSaveReq) {
        log.info("【保存核心信息】 {}", treatiseSaveReq);
        TreatiseDTO treatise = treatiseService.saveCore(treatiseSaveReq);
        return buildResponse(Boolean.TRUE, "保存成功", treatise);
    }

    /**
     * 返回修改著作信息
     */
    @GetMapping("/{treatiseId}")
    @ResponseBody
    public Response info(@PathVariable("treatiseId") Long treatiseId) {
        TreatiseDTO treatise = treatiseService.findOne(treatiseId);
        if (treatise == null) {// 404
            return buildResponse(Boolean.FALSE, "此资源找不到", null);
        }
        return buildResponse(Boolean.TRUE, "", treatise);
    }


    /*
            统计量信息，点击量、阅读量
     */

    /**
     * 返回点击量的页面
     */
    @GetMapping("/hits/list")
    @RequiresPermissions("treatise:show")
    public ModelAndView hitsList() {
        ModelAndView mv = new ModelAndView("admin/statistics/hits");
        return mv;
    }

    /**
     * 返回阅读量的页面
     */
    @GetMapping("/reading/list")
    @RequiresPermissions("treatise:show")
    public ModelAndView readingList() {
        ModelAndView mv = new ModelAndView("admin/statistics/reading");
        return mv;
    }


}




package cn.cherish.library.service;

import cn.cherish.library.commom.dto.TreatiseDTO;
import cn.cherish.library.dal.dao.AuthorDAO;
import cn.cherish.library.dal.dao.IBaseDAO;
import cn.cherish.library.dal.dao.TreatiseDAO;
import cn.cherish.library.dal.entity.Treatise;
import cn.cherish.library.util.ObjectConvertUtil;
import cn.cherish.library.web.request.BasicSearchReq;
import cn.cherish.library.web.request.treatise.TreatiseSaveCoreReq;
import cn.cherish.library.web.request.treatise.TreatiseSearchReq;
import cn.cherish.library.web.request.treatise.TreatiseUpdateReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class TreatiseService extends ABaseService<Treatise, Long> {

    private final TreatiseDAO treatiseDAO;
    private final AuthorDAO authorDAO;

    @Autowired
    public TreatiseService(TreatiseDAO treatiseDAO, AuthorDAO authorDAO) {
        this.treatiseDAO = treatiseDAO;
        this.authorDAO = authorDAO;
    }

    @Override
    protected IBaseDAO<Treatise, Long> getEntityDAO() {
        return treatiseDAO;
    }


    public Page<TreatiseDTO> findAll(TreatiseSearchReq treatiseSearchReq, BasicSearchReq basicSearchReq) {
        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;

        Page<Treatise> treatisePage = super.findAllBySearchParams(
                buildSearchParams(treatiseSearchReq), pageNumber, basicSearchReq.getPageSize());

        return treatisePage.map(this::getTreatiseDTO);
    }

    public Long getCount() {
        return treatiseDAO.count();
    }

    @Transactional
    public TreatiseDTO update(TreatiseUpdateReq treatiseUpdateReq) {
        Treatise treatise = findById(treatiseUpdateReq.getId());
        ObjectConvertUtil.objectCopy(treatise, treatiseUpdateReq);
        Treatise update = update(treatise);
        return getTreatiseDTO(update);
    }


    @Transactional
    public TreatiseDTO saveCore(TreatiseSaveCoreReq treatiseSaveReq) {
        Treatise treatise = new Treatise();
        ObjectConvertUtil.objectCopy(treatise, treatiseSaveReq);
        Treatise save = save(treatise);
        return getTreatiseDTO(save);
    }

    private TreatiseDTO getTreatiseDTO(Treatise source) {
        TreatiseDTO treatiseDTO = new TreatiseDTO();
        ObjectConvertUtil.objectCopy(treatiseDTO, source);
        treatiseDTO.setISBN(source.getISBN());
        return treatiseDTO;
    }


    public TreatiseDTO findOne(Long treatiseId) {
        Treatise treatise = treatiseDAO.findOne(treatiseId);
        if (treatise == null) {
            return null;
        }
        return getTreatiseDTO(treatise);
    }

    public TreatiseDTO updateCore(TreatiseUpdateReq treatiseUpdateReq) {
        Treatise treatise = findById(treatiseUpdateReq.getId());
        treatise.setISBN(treatiseUpdateReq.getISBN());
        treatise.setNo(treatiseUpdateReq.getNo());
        treatise.setBookName(treatiseUpdateReq.getBookName());
        return null;
    }

}

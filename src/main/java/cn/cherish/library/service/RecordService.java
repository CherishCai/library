
package cn.cherish.library.service;

import cn.cherish.library.dal.dao.IBaseDAO;
import cn.cherish.library.dal.dao.RecordDAO;
import cn.cherish.library.dal.entity.Record;
import cn.cherish.library.util.ObjectConvertUtil;
import cn.cherish.library.web.request.BasicSearchReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional(readOnly = true)
public class RecordService extends ABaseService<Record, Long> {

    private final RecordDAO recordDAO;

    @Autowired
    public RecordService(RecordDAO recordDAO) {
        this.recordDAO = recordDAO;
    }

    @Override
    protected IBaseDAO<Record, Long> getEntityDAO() {
        return recordDAO;
    }


    public Page<Record> findAll(Record recordSearchReq, BasicSearchReq basicSearchReq) {
        int pageNumber = basicSearchReq.getStartIndex() / basicSearchReq.getPageSize() + 1;
        return super.findAllBySearchParams(
                buildSearchParams(recordSearchReq), pageNumber, basicSearchReq.getPageSize());
    }

    @Transactional
    public Record update(Record recordUpdateReq) {
        Record record = findById(recordUpdateReq.getId());
        ObjectConvertUtil.objectCopy(record, recordUpdateReq);
        return update(record);
    }


    @Transactional
    public Record save(Record record) {
        record.setCreatedTime(new Date());
        return super.save(record);
    }

}

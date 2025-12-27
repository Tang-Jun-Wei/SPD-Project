package net.lab1024.sa.admin.module.spd.labeltrack.service;

import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.spd.labeltrack.dao.SpdLabelTrackDao;
import net.lab1024.sa.admin.module.spd.labeltrack.domain.form.SpdLabelTrackQueryForm;
import net.lab1024.sa.admin.module.spd.labeltrack.domain.vo.SpdLabelTrackDetailVO;
import net.lab1024.sa.admin.module.spd.labeltrack.domain.vo.SpdLabelTrackVO;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签追溯Service
 *
 * @author 1024创新实验室
 */
@Slf4j
@Service
public class SpdLabelTrackService {

    @Autowired
    private SpdLabelTrackDao spdLabelTrackDao;

    /**
     * 查询标签追溯信息
     */
    public ResponseDTO<SpdLabelTrackVO> queryTrack(SpdLabelTrackQueryForm queryForm) {
        // 查询标签基本信息
        SpdLabelTrackVO trackVO = spdLabelTrackDao.selectByLabelCode(queryForm.getLabelCode());
        if (trackVO == null) {
            return ResponseDTO.userErrorParam("标签不存在");
        }

        // 查询轨迹列表
        List<SpdLabelTrackDetailVO> trackList = spdLabelTrackDao.selectTrackList(queryForm.getLabelCode());
        trackVO.setTrackList(trackList);

        return ResponseDTO.ok(trackVO);
    }
}

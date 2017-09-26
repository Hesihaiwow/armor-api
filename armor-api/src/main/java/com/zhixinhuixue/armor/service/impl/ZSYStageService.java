package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYStageMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.response.StageResDTO;
import com.zhixinhuixue.armor.model.pojo.Stage;
import com.zhixinhuixue.armor.service.IZSYStageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lang on 2017/8/7 0007.
 */
@Service
public class ZSYStageService implements IZSYStageService {

    @Autowired
    private IZSYStageMapper stageMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;

    /**
     * 获取阶段列表
     * @return
     */
    @Override
    public List<StageResDTO> getStage(){
        List<Stage> stags = stageMapper.selectStage();
        List<StageResDTO> stageResDTOS = new ArrayList<>();
        stags.stream().forEach(stage -> {
            StageResDTO stageResDTO = new StageResDTO();
            BeanUtils.copyProperties(stage, stageResDTO);
            stageResDTO.setSort(stage.getSort());
            stageResDTOS.add(stageResDTO);
        });
        return stageResDTOS;
    }

    /**
     * 添加阶段
     * @param stageResDTO
     */
    @Override
    public Long addStage(StageResDTO stageResDTO){
        if(stageMapper.validateStage(stageResDTO.getName().replace(" ", ""))>0){
            throw new ZSYServiceException("阶段名称已存在");
        }
            Stage stage = new Stage();
            stage.setCreateBy(ZSYTokenRequestContext.get().getUserId());
            stage.setCreateTime(new Date());
            stage.setId(snowFlakeIDHelper.nextId());
            stage.setName(stageResDTO.getName());
            stage.setSort(stageResDTO.getSort());
            stageMapper.insert(stage);
            return stage.getId();
    }

    /**
     * 删除阶段
     * @param id
     */
    @Override
    public void deleteStage(Long id){
        if(stageMapper.deleteStage(id)==0){
            throw new ZSYServiceException("删除失败");
        }
    }
}

package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYStageMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.response.StageDTO;
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
    public List<StageDTO> getStage(){
        List<Stage> stags = stageMapper.selectStage();
        List<StageDTO> stageDTOS = new ArrayList<>();
        stags.stream().forEach(stage -> {
            StageDTO stageDTO = new StageDTO();
            BeanUtils.copyProperties(stags,stageDTO);
            stageDTO.setName(stage.getName());
            stageDTO.setId(stage.getId());
            stageDTOS.add(stageDTO);
        });
        return stageDTOS;
    }

    /**
     * 添加阶段
     * @param name
     */
    @Override
    public void addStage(String name){
        if(stageMapper.validateStage(name.replace(" ", ""))>0){
            throw new ZSYServiceException("阶段名称已存在");
        }else{
            Stage stage = new Stage();
            stage.setCreateBy(ZSYTokenRequestContext.get().getUserId());
            stage.setCreateTime(new Date());
            stage.setId(snowFlakeIDHelper.nextId());
            stage.setName(name);
            stageMapper.insert(stage);
        }
    }

    /**
     * 删除阶段
     * @param id
     */
    @Override
    public void deleteStage(String id){
        if(stageMapper.deleteStage(new Long(id))==0){
            throw new ZSYServiceException("删除失败");
        }
    }
}

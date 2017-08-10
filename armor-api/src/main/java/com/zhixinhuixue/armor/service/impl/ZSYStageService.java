package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYStageMapper;
import com.zhixinhuixue.armor.dao.IZSYTagMapper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.pojo.Stage;
import com.zhixinhuixue.armor.model.pojo.Tag;
import com.zhixinhuixue.armor.service.IZSYStageService;
import com.zhixinhuixue.armor.service.IZSYTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 获取标签列表
     * @return
     */
    public List<Stage> getStage(){
        List<Stage> tags = stageMapper.selectStage();
        return tags;
    }

    /**
     * 添加标签
     * @param name
     */
    public void addStage(String name){
        Stage stage = new Stage();
        stage.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        stage.setCreateTime(new Date());
        stage.setId(snowFlakeIDHelper.nextId());
        stage.setName(name);
        stageMapper.insert(stage);
    }

    /**
     * 删除标签
     * @param id
     */
    public void deleteStage(String id){
        stageMapper.deleteStage(new Long(id));
    }
}

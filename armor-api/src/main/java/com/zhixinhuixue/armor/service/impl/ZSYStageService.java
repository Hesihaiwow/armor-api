package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYStageMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.response.StageResDTO;
import com.zhixinhuixue.armor.model.pojo.Stage;
import com.zhixinhuixue.armor.service.IZSYStageService;
import com.zhixinhuixue.armor.source.ZSYResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    private static final Logger logger = LoggerFactory.getLogger(ZSYTaskService.class);

    /**
     * 获取阶段列表
     * @return
     */
    @Override
    public List<StageResDTO> getStage(){
        List<StageResDTO> stageResDTOS = stageMapper.selectStage(ZSYTokenRequestContext.get().getDepartmentId());
        return stageResDTOS;
    }

    /**
     * 添加阶段
     * @param stageResDTO
     */
    @Override
    public Long addStage(StageResDTO stageResDTO){
        if(stageMapper.validateStage(stageResDTO.getName().trim(),ZSYTokenRequestContext.get().getDepartmentId())>0){
            throw new ZSYServiceException("阶段名称已存在，请修改后重试");
        }
            Stage stage = new Stage();
            stage.setCreateBy(ZSYTokenRequestContext.get().getUserId());
            stage.setCreateTime(new Date());
            stage.setId(snowFlakeIDHelper.nextId());
            stage.setName(stageResDTO.getName());
            stage.setSort(new Date().getTime());
            stage.setDepartmentId(ZSYTokenRequestContext.get().getDepartmentId());
            stageMapper.insert(stage);
            return stage.getId();
    }

    /**
     * 编辑阶段
     * @param stageResDTO
     */
    @Override
    public void editStage(StageResDTO stageResDTO){
        Stage stage = stageMapper.selectById(stageResDTO.getId());
        if(stage.getName()==stageResDTO.getName()&&stageMapper.validateStage(stageResDTO.getName().trim(),ZSYTokenRequestContext.get().getDepartmentId())>0){
            throw new ZSYServiceException("阶段名称或优先级已存在");
        }
        stage.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        stage.setId(stageResDTO.getId());
        stage.setName(stageResDTO.getName());
        if( stageMapper.update(stage)==0){
            throw new ZSYServiceException("更新阶段信息失败");
        }

    }

    /**
     * 删除阶段
     * @param id
     */
    @Override
    public void deleteStage(Long id){
        if(stageMapper.countStage(id)!=0){
            throw new ZSYServiceException("阶段正在使用中，请修改或停止使用后删除");
        }
        if(stageMapper.deleteStage(id)==0){
            throw new ZSYServiceException("删除失败");
        }
    }

    /**
     * 移动阶段顺序
     *
     * @param id
     * @return
     */
    @Override
    public ZSYResult moveStage(Long id, int sortType) {
        Stage stage = stageMapper.selectById(id);
        if (stage == null) {
            return ZSYResult.fail().msg("移动失败，该阶段信息不存在");
        }
        Long parentId = stage.getDepartmentId();
        List<Stage> children = stageMapper.selectByDeptId(parentId);
        if (CollectionUtils.isEmpty(children)) {
            return ZSYResult.fail().msg("移动失败，组织下无阶段信息");
        }
        // 上移
        if (sortType == 0) {
            for (int i = 0; i < children.size(); i++) {
                if (stage.getId().equals(children.get(i).getId())) {
                    if (i == 0) {
                        return ZSYResult.fail().msg("移动失败，已经在最上面");
                    }
                    Stage prev = children.get(i - 1);

                    // 交换位置
                    Stage prevPo = stageMapper.selectById(prev.getId());
                    prevPo.setSort(stage.getSort());
                    stageMapper.update(prevPo);
                    stage.setId(stage.getId());
                    stage.setSort(prev.getSort());
                    stageMapper.update(stage);
                }
            }
            // 下移
        } else if (sortType == 1) {
            for (int i = 0; i < children.size(); i++) {
                if (stage.getId().equals(children.get(i).getId())) {
                    if (i == children.size() - 1) {
                        return ZSYResult.fail().msg("移动失败，已经在最下面");
                    }
                    Stage next = children.get(i + 1);

                    // 交换位置
                    Stage nextPo = stageMapper.selectById(next.getId());
                    nextPo.setSort(stage.getSort());
                    stageMapper.update(nextPo);
                    stage.setSort(next.getSort());
                    stageMapper.update(stage);
                }
            }
        } else {
            return ZSYResult.fail().msg("参数错误");
        }
        return ZSYResult.success().msg("移动位置成功");
    }


//    /**
//     * board移动阶段
//     * @param stageResDTO
//     */
//    @Override
//    public void moveStage(StageResDTO stageResDTO){
//        if(stageResDTO.getId()==0){
//            throw new ZSYServiceException("移动失败，阶段不存在");
//        }
//        Stage stage = stageMapper.selectById(stageResDTO.getId());
//        if (stage == null) {
//            throw new ZSYServiceException("移动失败，阶段不存在");
//        }
//        Long sort = 0L;//优先级
//        if(stageResDTO.getNum()!=1){//直接移动到最后
//            if( stageMapper.updateSortById(sort,stage.getId())==0){
//                throw new ZSYServiceException("更新优先级失败，请稍后重试");
//            }
//        }
//        if(stageResDTO.getNum()!=2){//移动到开始
//            //根据顺序查询目标上一个阶段的优先级，+1获得当前优先级
//            sort = stageMapper.selectBySort(stage.getSort()-1L)+1L;
//        }
//        stage.setSort(sort);
//        if( stageMapper.update(stage)==0){
//            throw new ZSYServiceException("更新阶段信息失败")   ;
//        }
//    }

    /**
     * board移动阶段
     * @param stageResDTO
     * @return
     */
    @Override
    public void moveStage(StageResDTO stageResDTO) {
        Stage stage = stageMapper.selectById(stageResDTO.getId());
        Long parentId = stage.getDepartmentId();
        List<Stage> children = stageMapper.selectByDeptId(parentId);

        Stage change = children.get(stageResDTO.getSort().intValue());

        stage.setId(stage.getId());
        if (stage.getSort()<change.getSort()) {
            stage.setSort(change.getSort()+1L);
        }else{
            stage.setSort(change.getSort()-1L);
        }
        if( stageMapper.update(stage)==0){
            throw new ZSYServiceException("更新阶段信息失败");
        }
    }

}

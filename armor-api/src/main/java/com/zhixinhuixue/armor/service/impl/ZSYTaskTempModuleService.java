package com.zhixinhuixue.armor.service.impl;

import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYTaskTempModuleMapper;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.dto.request.ProjectReqDTO;
import com.zhixinhuixue.armor.model.dto.response.TaskTempModuleResDTO;
import com.zhixinhuixue.armor.model.pojo.TaskTempModule;
import com.zhixinhuixue.armor.service.IZSYTaskTempModuleService;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sch
 * @DATE 2019/7/29 10:45
 */
@Service
public class ZSYTaskTempModuleService implements IZSYTaskTempModuleService {

    @Autowired
    private IZSYTaskTempModuleMapper projectMapper;
    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;
    /**
     * 新增
     * @author sch
     * @param reqDTO
     */
    @Override
    @Transactional
    public void add(ProjectReqDTO reqDTO) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()){
            throw new ZSYServiceException("当前用户没有权限");
        }
        //校验是否已存在
        TaskTempModule existProject = projectMapper.selectByName(reqDTO.getName().trim());
        if (existProject != null){
            throw new ZSYServiceException("项目名称已存在,请重新创建");
        }

        TaskTempModule project = new TaskTempModule();
        BeanUtils.copyProperties(reqDTO,project);
        project.setId(snowFlakeIDHelper.nextId());
        project.setCreateBy(ZSYTokenRequestContext.get().getUserId());
        project.setCreateTime(new Date());
        project.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());

        if (projectMapper.insert(project) == 0){
            throw new ZSYServiceException("新增临时任务涉及项目失败");
        }
    }

    /**
     * 查看列表
     * @author sch
     * @return
     */
    @Override
    public List<TaskTempModuleResDTO> getList() {
        List<TaskTempModule> projectList = projectMapper.selectList();
        List<TaskTempModuleResDTO> list = new ArrayList<>();
        if (!CollectionUtils.isEmpty(projectList)){
            projectList.forEach(project->{
                TaskTempModuleResDTO resDTO = new TaskTempModuleResDTO();
                BeanUtils.copyProperties(project,resDTO);
                list.add(resDTO);
            });
        }
        return list;
    }

    /**
     * 修改
     * @author sch
     * @param reqDTO
     * @param id
     */
    @Override
    @Transactional
    public void update(ProjectReqDTO reqDTO, Long id) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()){
            throw new ZSYServiceException("当前用户没有权限");
        }
        //校验是否已存在
        TaskTempModule existProject = projectMapper.selectByPrimary(id);
        if (existProject == null){
            throw new ZSYServiceException("当前项目不存在,请检查");
        }

        BeanUtils.copyProperties(reqDTO,existProject);
        if (projectMapper.update(existProject) == 0){
            throw new ZSYServiceException("修改项目失败");
        }
    }

    /**
     * 删除
     * @author sch
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()){
            throw new ZSYServiceException("当前用户没有权限");
        }
        //校验是否已存在
        TaskTempModule existProject = projectMapper.selectByPrimary(id);
        if (existProject == null){
            throw new ZSYServiceException("当前模块不存在,请检查");
        }
        //校验是否关联任务
        Integer count = projectMapper.selectCountInUse(id);
        if (count > 0){
            throw new ZSYServiceException("当前模块已经关联任务,无法删除");
        }
        existProject.setIsDelete(ZSYDeleteStatus.DELETED.getValue());
        if (projectMapper.update(existProject) == 0){
            throw new ZSYServiceException("删除模块失败");
        }
    }
}

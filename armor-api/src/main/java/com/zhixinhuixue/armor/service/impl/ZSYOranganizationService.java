package com.zhixinhuixue.armor.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.sun.tools.corba.se.idl.constExpr.Or;
import com.zhixinhuixue.armor.context.ZSYTokenRequestContext;
import com.zhixinhuixue.armor.dao.IZSYDepartmentMapper;
import com.zhixinhuixue.armor.dao.IZSYOranganizationMapper;
import com.zhixinhuixue.armor.dao.IZSYUserMapper;
import com.zhixinhuixue.armor.exception.ZSYAuthException;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import com.zhixinhuixue.armor.helper.MD5Helper;
import com.zhixinhuixue.armor.helper.SHA1Helper;
import com.zhixinhuixue.armor.helper.SnowFlakeIDHelper;
import com.zhixinhuixue.armor.model.bo.OranganizationBo;
import com.zhixinhuixue.armor.model.dto.request.OrgReqDTO;
import com.zhixinhuixue.armor.model.dto.request.OrgUserReqDTO;
import com.zhixinhuixue.armor.model.dto.request.UserCheckPeopleReqDTO;
import com.zhixinhuixue.armor.model.dto.response.ExtraWorkResDTO;
import com.zhixinhuixue.armor.model.dto.response.OrgResDTO;
import com.zhixinhuixue.armor.model.pojo.*;
import com.zhixinhuixue.armor.service.IZSYOranganizatinService;
import com.zhixinhuixue.armor.source.ZSYConstants;
import com.zhixinhuixue.armor.source.enums.ZSYDeleteStatus;
import com.zhixinhuixue.armor.source.enums.ZSYUserRole;
import com.zhixinhuixue.armor.source.enums.ZSYUserStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.github.pagehelper.page.PageMethod.startPage;

/**
 * 机构服务
 *
 * @author hsh
 * @create 2020年12月24日
 */
@Service
public class ZSYOranganizationService implements IZSYOranganizatinService {

    @Autowired
    private IZSYOranganizationMapper oranganizationMapper;

    @Autowired
    private SnowFlakeIDHelper snowFlakeIDHelper;


    @Autowired
    private IZSYDepartmentMapper izsyDepartmentMapper;

    @Autowired
    private IZSYUserMapper izsyUserMapper;

    /**
     * 查询机构
     *
     * @param pageNum 页码
     * @return
     */
    @Override
    public PageInfo<OranganizationBo> search(Integer pageNum) {

        startPage(Optional.ofNullable(pageNum).orElse(1), ZSYConstants.PAGE_SIZE_WAIT);
        Page<OranganizationBo> page = oranganizationMapper.selectOrgByPage();
        for(Oranganization oranganization: page.getResult()){
            System.out.println(oranganization.toString());
        }

        return new PageInfo<>(page);
    }

    /**
     * 添加机构
     *
     * @param orgReqDTO
     */
    @Transactional
    @Override
    public void addOrg(OrgReqDTO orgReqDTO) {

        // 机构名称是否存在
        Oranganization oranganization = oranganizationMapper.selectByName(orgReqDTO.getName());

        if(oranganization!=null){
            throw new ZSYServiceException("该机构名称已存在，请重新命名！");
        }

        // 插入到机构表
        long uuid = snowFlakeIDHelper.nextId();
        Date date = new Date();

        Oranganization org = new Oranganization();
        org.setCreateTime(date);
        org.setName(orgReqDTO.getName());
        org.setDescription(orgReqDTO.getDesc());
        org.setId(uuid);
        org.setIsDelete(0);
        oranganizationMapper.insert(org);

        // 插入到部门表
        Department department = new Department();
        department.setCreateTime(date);
        department.setDescription(orgReqDTO.getDesc());
        department.setId(uuid);
        department.setName(orgReqDTO.getName());
        department.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        department.setParentId(uuid);
        department.setOrgId(uuid);
        izsyDepartmentMapper.insertDept(department);

    }

    @Transactional
    @Override
    public void modifyOrg(OrgReqDTO orgReqDTO, Long orgId) {
        if(oranganizationMapper.selectById(orgId) == null){
            throw new ZSYServiceException("该机构不存在!");
        }
        Oranganization oranganization = oranganizationMapper.selectByName(orgReqDTO.getName());
        if(oranganization != null){
            if(!oranganization.getId().equals(orgId)){
                throw new ZSYServiceException("该机构名称已存在!");
            }
        }
        oranganizationMapper.updateOrg(orgReqDTO, orgId);
    }

    @Transactional
    @Override
    public void addOrgUser(OrgUserReqDTO userReqDTO) {

        //校验邮箱是否存在
        if (izsyUserMapper.selectByEmail(userReqDTO.getEmail()) > 0) {
            throw new ZSYServiceException(String.format("用户邮箱[%s]已存在", userReqDTO.getAccount()));
        }
        if (userReqDTO.getStatus() == null) {
            userReqDTO.setStatus(ZSYUserStatus.NORMAL.getValue());
        }
        //校验用户账户是否存在
        List<User> existUsers = izsyUserMapper.selectByAccount(userReqDTO.getAccount());
        if (!CollectionUtils.isEmpty(existUsers)) {
            throw new ZSYServiceException(String.format("用户账户[%s]已存在", userReqDTO.getAccount()));
        }


        User user = new User();
        BeanUtils.copyProperties(userReqDTO, user);
        user.setJobNumber(userReqDTO.getJobNumber().trim());
        user.setId(snowFlakeIDHelper.nextId());
        user.setCreateTime(new Date());
        user.setIsDelete(ZSYDeleteStatus.NORMAL.getValue());
        user.setIsAdmin(0);
        user.setUserRole(0);
        user.setDepartmentId(userReqDTO.getOrgId());
        user.setOrgId(userReqDTO.getOrgId());
        user.setIntegral(new BigDecimal(ZSYConstants.DEFAULT_INTEGRAL));
        if (userReqDTO.getPassword() == null || "".equals(userReqDTO.getPassword())) {
            user.setPassword(MD5Helper.convert(
                    String.format("%s%s", SHA1Helper.Sha1(ZSYConstants.DEFAULT_PASSWORD),
                            ZSYConstants.HINT_PASSWORD_KEY), 32, false));
        }

        if(oranganizationMapper.selectById(user.getOrgId()) == null){
            throw new ZSYServiceException("该机构不存在");
        }

        izsyUserMapper.insertUser(user);

        oranganizationMapper.updateOrgUser(user.getOrgId(),user.getId());
    }

    @Transactional
    @Override
    public void modifyUser(OrgUserReqDTO userReqDTO) {

        if (ZSYTokenRequestContext.get().getUserRole() > ZSYUserRole.PROJECT_MANAGER.getValue()) {
            throw new ZSYAuthException("没有权限执行此操作");
        }

        User user = new User();
        BeanUtils.copyProperties(userReqDTO, user);

        user.setId(userReqDTO.getUserId());
        user.setJobNumber(userReqDTO.getJobNumber().trim());

        if (izsyUserMapper.updateSelectiveById(user) == 0) {
            throw new ZSYServiceException("更新用户失败");
        }

    }

    @Transactional
    @Override
    public void deleteByUserId(Long userId,Long orgId) {
        // 删除user表中超管
        izsyUserMapper.deleteById(userId);

        // 删除org表 表中超管
        oranganizationMapper.updateOrgUser(orgId,null);
    }

    @Transactional
    @Override
    public void deleteByOrgId(Long orgId) {

        Oranganization oranganization = oranganizationMapper.selectById(orgId);
        if(oranganization.getUserId() != null){
            throw new ZSYServiceException("请先删除该机构管理员！");
        }

        oranganizationMapper.deleteOrgById(orgId);

    }


}
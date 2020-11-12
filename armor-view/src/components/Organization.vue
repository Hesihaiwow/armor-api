<template>
    <div class="organization-con clearfix">
        <div class="department-structure fl">
            <div style="height: 50%">
                <div class="ds-title">部门结构<span class="add-department" @click="addDepartDlgShow">+</span></div>
                <el-tree :data="departmentTree"
                         :props="defaultProps"
                         default-expand-all
                         :expand-on-click-node="false"
                         @node-click="handleNodeClick"/>
            </div>

            <div>
                <div class="ds-title">团队<span class="add-department" @click="addGroupDlgShow">+</span></div>
                <div>
                    <div v-for="item in groupList" style="margin-left: 20px;margin-top: 8px">
                        <span style="font-size: 14px;cursor: pointer;color:#606266" v-if="item.name.length>7"
                              @click="fetchUserPage(item.id)">{{item.name.substring(0,7)}}...</span>
                        <span style="font-size: 14px;cursor: pointer;color:#606266" v-else
                              @click="fetchUserPage(item.id)">{{item.name}}</span>
                        <span class="fr" style="color: #26a2ff;margin-top: 3px;cursor: pointer;margin-right: 5px"
                              @click="deleteGroup(item.id)">删除</span>
                        <span class="fr" style="margin-right: 5px;color: #26a2ff;margin-top: 3px;cursor: pointer"
                              @click="editGroupDlgShow(item.id)">编辑</span>
                        <span class="fr" style="color: #26a2ff;margin-top: 3px;cursor: pointer;margin-right: 5px"
                              @click="setUser(item)">成员</span>
                    </div>
                </div>
            </div>


        </div>
        <div class="department-member fl">
            <div class="dm-title" style="">
                成员
                <el-select v-model="queryForm.jobRole" placeholder="请选择角色" @change="changeJobRole(queryForm.jobRole)"
                           clearable>
                    <el-option
                            v-for="item in jobRoleList"
                            :key="item.roleId"
                            :label="item.roleName"
                            :value="item.roleId"></el-option>
                </el-select>
                <el-select v-model="queryForm.userType" placeholder="请选择用户类型"
                           @change="changeUserType(queryForm.userType)" clearable>
                    <el-option
                            v-for="item in userTypeList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id"></el-option>
                </el-select>
                <span class="add-department" @click="addUser">+</span>
            </div>
            <!--<div class="dm-title" style="width: 50%;">成员<span class="add-department" @click="addUserDlgShow">+</span></div>-->
            <div class="white-bg">
                <div class="department-member-table">
                    <el-table :data="tableData" stripe style="width: 100%"
                              :header-cell-style="{background:'#D9D9D9',color:'black'}">
                        <el-table-column prop="name" label="姓名" width="80" align="center"></el-table-column>
                        <el-table-column prop="account" label="用户名" width="120" align="center"></el-table-column>
                        <el-table-column prop="jobName" label="职位" width="120" align="center"></el-table-column>
                        <el-table-column prop="phone" label="手机号" width="116" align="center"></el-table-column>
                        <el-table-column prop="levelName" label="级别" width="90" align="center"></el-table-column>
                        <el-table-column prop="deptName" label="部门" width="90" align="center"></el-table-column>
                        <el-table-column prop="groupName" label="团队" width="116" align="center"></el-table-column>
                        <!--<el-table-column prop="createTime" label="创建日期" width="106" align="center"></el-table-column>-->
                        <!--<el-table-column prop="lastLogin" label="最后登录" width="106" align="center"></el-table-column>-->
                        <el-table-column prop="operate" label="操作" align="center">
                            <template slot-scope="scope">
                                <!-- <el-button type="text" size="small" @click.native.prevent="authorityOpt(scope.$index, tableData)">权限</el-button> -->
                                <el-button type="text" size="small" @click="editUser(scope.row.id)">编辑</el-button>
                                <el-button type="text" size="small"
                                           @click.native.prevent="resetUserPwdDlgShow(scope.$index)">重置密码
                                </el-button>
                                <el-button type="text" size="small" v-show="scope.row.isDelete===0"
                                           @click.native.prevent="deleteUserDlgShow(scope.$index)">删除
                                </el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
            <el-pagination
                    :current-page="pagination.pageIndex"
                    @current-change="handleCurrentChange"
                    :page-size="pagination.pageSize"
                    :layout="pagination.layout"
                    :total="pagination.total">
            </el-pagination>
        </div>
        <add-member ref="showAddMember" @handleUserDataRefresh="handleUserDataRefresh"></add-member>
        <modify-member ref="showModifyMember" @handleUserDataRefresh="handleUserDataRefresh"></modify-member>
        <add-department ref="showAddDepartment" @handleDeptDataRefresh="handleDeptDataRefresh"></add-department>
        <el-dialog title="新增团队"
                   class="add-group"
                   size="tiny"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :visible.sync="addGroupVisible"
                   @close="clearAddForm">
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">名称</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="addGroupForm.name" clearable placeholder="请输入团队名称"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">描述</div>
                <div class="ftp-msg fl">
                    <el-input type="textarea" class="w280" v-model="addGroupForm.description" clearable
                              placeholder="请输入团队描述"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">负责人</div>
                <div class="ftp-msg fl">
                    <el-select class="w280" v-model="addGroupForm.leader" filterable clearable placeholder="请选择负责人">
                        <el-option
                                v-for="item in userList"
                                :key="item.userId"
                                :label="item.userName"
                                :value="item.userId">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="ctpc-btns">
                <input type="button" class="ctpc-cancel" value="取消" @click="addGroupVisible = false">
                <input type="button" class="ctpc-save" value="保存" :loading="isSaving" @click="saveAddGroup">
            </div>
        </el-dialog>
        <el-dialog title="编辑团队"
                   size="tiny"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   :visible.sync="editGroupVisible"
                   @close="clearEditForm">
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">名称</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="editGroupForm.name" clearable placeholder="请输入团队名称"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">描述</div>
                <div class="ftp-msg fl">
                    <el-input type="textarea" class="w280" v-model="editGroupForm.description" clearable
                              placeholder="请输入团队描述"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">负责人</div>
                <div class="ftp-msg fl">
                    <el-select class="w280" v-model="editGroupForm.leader" filterable clearable placeholder="请选择负责人">
                        <el-option
                                v-for="item in userList"
                                :key="item.userId"
                                :label="item.userName"
                                :value="item.userId">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="ctpc-btns">
                <input type="button" class="ctpc-cancel" value="取消" @click="editGroupVisible = false">
                <input type="button" class="ctpc-save" value="保存" :loading="isSaving" @click="saveEditGroup">
            </div>
        </el-dialog>

        <el-dialog title="配置团队成员"
                   :visible.sync="setUserVisible"
                   size="tiny"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   @close="closeSetUser"
        >
            <el-form ref="setUserForm" :model="setUserDTO" label-width="100px">
                <el-form-item label="团队名称" prop="groupName">
                    {{ setUserDTO.groupName }}
                </el-form-item>
                <el-form-item label="关联用户">
                    <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate"
                                 @change="handleCheckAllChange(checkAll)">全选
                    </el-checkbox>
                    <!--<el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">全选</el-checkbox>-->
                    <div style="margin: 15px 0;"/>
                    <el-checkbox-group v-model="setUserDTO.userIds" @change="handleCheckedUsersChange">
                        <el-checkbox v-for="user in userList" :key="user.userId" :label="user.userId"
                                     :value="user.userId">
                            {{ user.userName }}
                        </el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click="closeSetUser">取 消</el-button>
                <el-button :loading="isSaving" type="primary" @click="saveSetUser(setUserDTO.groupId)">确定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="添加用户"
                   top="3%"
                   width="536px"
                   :visible.sync="addUserVisible"
                   size="tiny"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false">
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">姓名</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="addUserForm.name" placeholder="请输入姓名"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">账号</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="addUserForm.account" placeholder="请输入账号"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">职位</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="addUserForm.jobName" placeholder="请输入职位"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">角色</div>
                <div class="ftp-msg fl">
                    <el-select class="w280" v-model="addUserForm.jobRole" placeholder="请选择角色">
                        <el-option
                                v-for="item in rolesList"
                                :key="item.roleId"
                                :label="item.roleName"
                                :value="item.roleId">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">级别</div>
                <div class="ftp-msg fl">
                    <el-select class="w280" v-model="addUserForm.level" placeholder="请选择级别">
                        <el-option
                                v-for="item in levelList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">手机号</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="addUserForm.phone" placeholder="请输入手机号"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">邮箱地址</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="addUserForm.email" placeholder="请输入邮箱地址"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">用户权限</div>
                <div class="ftp-msg fl">
                    <el-select class="w280" v-model="addUserForm.userRole" placeholder="请选择权限">
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">考勤序号</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="addUserForm.checkSort" placeholder="请输入考勤序号"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">工号</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="addUserForm.jobNumber" placeholder="请输入员工工号"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">审核人</div>
                <div class="ftp-msg fl">
                    <i style="margin-left: 0px" class="el-icon-plus" v-show="num>=1 && num < 3" @click="plus"></i>
                    <i class="el-icon-minus" v-show="num>1" @click="minus(num-1)"></i>
                    <div v-for="i in num"><span style="margin-right: 20px">{{i}}</span>
                        <el-select placeholder="请选择审核人" @change="addCheckUser(i)" v-model="checkUserIdList[i-1]"
                                   clearable>
                            <el-option
                                    v-for="item in userList"
                                    :key="item.userId"
                                    :label="item.userName"
                                    :value="item.userId">
                            </el-option>
                        </el-select>
                    </div>

                </div>
            </div>

            <!--<div class="am-warn">{{amWarn}}</div>-->
            <div class="ctpc-btns">
                <input type="button" class="ctpc-cancel" value="取消" @click="hideAddUser">
                <input type="button" class="ctpc-save" value="保存" @click="saveAddUser">
            </div>
        </el-dialog>

        <el-dialog title="编辑用户"
                   width="536px"
                   top="3%"
                   :visible.sync="editUserVisible"
                   size="tiny"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false">
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">姓名</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="userDetail.name" placeholder="请输入姓名"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">账号</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="userDetail.account" placeholder="请输入账号"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">职位</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="userDetail.jobName" placeholder="请输入职位"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">角色</div>
                <div class="ftp-msg fl">
                    <el-select class="w280" v-model="userDetail.jobRole" placeholder="请选择角色">
                        <el-option
                                v-for="item in rolesList"
                                :key="item.roleId"
                                :label="item.roleName"
                                :value="item.roleId">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">级别</div>
                <div class="ftp-msg fl">
                    <el-select class="w280" v-model="userDetail.level" placeholder="请选择级别">
                        <el-option
                                v-for="item in levelList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">手机号</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="userDetail.phone" placeholder="请输入手机号"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">邮箱地址</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="userDetail.email" placeholder="请输入邮箱地址"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">用户权限</div>
                <div class="ftp-msg fl">
                    <el-select class="w280" v-model="userDetail.userRole" placeholder="请选择权限">
                        <el-option
                                v-for="item in options"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">用户状态</div>
                <div class="ftp-msg fl">
                    <el-select class="w280" v-model="userDetail.status" placeholder="请选择状态">
                        <el-option
                                v-for="item in statusOptions"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">考勤序号</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="userDetail.checkSort" placeholder="请输入考勤序号"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">工号</div>
                <div class="ftp-msg fl">
                    <el-input class="w280" v-model="userDetail.jobNumber" placeholder="请输入员工工号"></el-input>
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">审核人</div>
                <div class="ftp-msg fl">
                    <i style="margin-left: 0px" class="el-icon-plus" v-show="num2>=1 && num2 < 3" @click="plus2"></i>
                    <i class="el-icon-minus" v-show="num2>1" @click="minus2(num2-1)"></i>
                    <div v-for="i in num2"><span style="margin-right: 20px">{{i}}</span>
                        <el-select placeholder="请选择审核人" @change="addCheckUser2(i)" v-model="checkUserIdList2[i-1]"
                                   clearable>
                            <el-option
                                    v-for="item in userList"
                                    :key="item.userId"
                                    :label="item.userName"
                                    :value="item.userId">
                            </el-option>
                        </el-select>
                    </div>

                </div>
            </div>

            <div class="ctpc-btns">
                <input type="button" class="ctpc-cancel" value="取消" @click="hideEditUser">
                <input type="button" class="ctpc-save" value="保存" @click="saveEditUser">
            </div>
        </el-dialog>
    </div>
</template>
<style type="text/css" scoped>
    .organization-con .ctpc-top {
        font-size: 16px;
        line-height: 30px;
        font-weight: 700;
        color: #333;
    }

    .organization-con .ctpc-top-close {
        font-size: 30px;
        width: 30px;
        height: 30px;
        line-height: 27px;
        cursor: pointer;
        transition: 0.8s ease all;
        text-align: center;
    }

    .organization-con .ftp-list {
        margin: 20px 0;
        font-size: 14px;
    }

    .organization-con .ftp-menus {
        width: 130px;
        text-align: right;
        margin-right: 16px;
        line-height: 30px;
    }

    .organization-con .ftp-msg {
        line-height: 30px;
    }

    .organization-con .am-warn {
        height: 20px;
        line-height: 20px;
        text-indent: 84px;
        color: red;
    }

    .organization-con .ctpc-btns {
        text-align: right;
    }

    .organization-con .ctpc-btns > input {
        display: inline-block;
        width: 80px;
        height: 28px;
        margin-left: 12px;
        cursor: pointer;
        border-radius: 4px;
    }

    .organization-con .ctpc-cancel {
        background: #fff;
        border: 1px solid #ccc;
    }

    .organization-con .ctpc-cancel:hover {
        background: #fff;
        border: 1px solid #36A8FF;
        color: #36A8FF;
        font-weight: 700;
    }

    .organization-con .ctpc-save {
        background: #36A8FF;
        color: #fff;
    }


    .organization-con {
        width: 1300px;
        margin: auto;
    }

    .department-structure {
        width: 260px;
        background: #fff;
        padding-bottom: 30px;
        height: 564px;
        overflow-y: scroll;
    }

    .department-structure::-webkit-scrollbar {
        width: 3px;
        background-color: #F5F5F5;
    }

    .department-structure::-webkit-scrollbar-thumb {
        background-color: rgb(255, 255, 255);
        background-image: -webkit-gradient(linear, 40% 0%, 75% 84%, from(rgb(54, 168, 255)), color-stop(0.6, rgb(54, 229, 255)), to(rgb(54, 192, 255)));
        border-radius: 10px;
    }

    .department-structure::-webkit-scrollbar-track {
        background-color: #f5f5f5;
        border-radius: 10px;
    }

    .ds-title, .dm-title {
        position: relative;
        line-height: 34px;
        background: #36A8FF;
        color: #fff;
        font-size: 14px;
        padding: 0 10px;
        margin-bottom: 10px;
    }

    .add-department {
        position: absolute;
        display: block;
        border: 1px solid #fff;
        width: 22px;
        height: 22px;
        border-radius: 50%;
        text-align: center;
        line-height: 20px;
        right: 10px;
        top: 5px;
        font-size: 24px;
        cursor: pointer;
    }

    .department-member {
        width: 1000px;
        height: 594px;
        margin-left: 20px;
        background: #fff;
    }

    .dm-title {
        margin-bottom: 0;
    }

    .white-bg {
        background: #fff;
    }
</style>
<style>
    .add-group .el-dialog {
        width: 536px;
    }
</style>
<script type="text/javascript">

    import AddMember from './AddMember'
    import ModifyMember from './ModifyMember'
    import AddDepartment from './AddDepartment'
    import Http from '../lib/Http'

    export default {
        name: 'Organization',
        data() {
            return {
                //查询表单
                queryForm: {
                    pageIndex: 1,
                    deptId: '',
                    groupId: '',
                    jobRole: '',
                    userType: 1,
                },
                userTypeList: [
                    {id: 0, name: '全部用户'},
                    {id: 1, name: '正常用户'},
                    {id: 2, name: '冻结用户'},
                ],
                jobRoleList: [
                    {
                        roleId: 3,
                        roleName: '产品经理'
                    },
                    {
                        roleId: 0,
                        roleName: '测试'
                    },
                    {
                        roleId: 2,
                        roleName: 'UI设计'
                    },
                    {
                        roleId: 1,
                        roleName: 'JAVA开发'
                    },
                    {
                        roleId: 4,
                        roleName: 'C++开发'
                    },
                    {
                        roleId: 5,
                        roleName: 'PHP开发'
                    },
                    {
                        roleId: 6,
                        roleName: '前端开发'
                    },
                    {
                        roleId: 7,
                        roleName: 'IOS开发'
                    },
                    {
                        roleId: 8,
                        roleName: 'Android开发'
                    },
                    {
                        roleId: 9,
                        roleName: '人工智能'
                    },
                    {
                        roleId: 10,
                        roleName: '其他'
                    }
                ],
                //分页信息
                pagination: {
                    pageIndex: 1,
                    total: 0,
                    pageSize: 10,
                    layout: 'total, pager'
                },
                //部门树结构数据
                departmentTree: [],
                //部门树结构父子传递字段

                //分页表数据
                tableData: [],
                editStatusIndex: '',
                nowCreateDate: '',
                newLastLogin: '',
                currentPage1: 5,
                defaultProps: {
                    id: 'id',
                    label: 'label',
                    children: 'children'
                },
                groupProps: {
                    id: 'id',
                    label: 'label',
                    children: 'children'
                },
                groupList: [],
                addGroupVisible: false,
                editGroupVisible: false,
                groupDetail: {},
                isSaving: false,
                addGroupForm: {
                    name: '',
                    description: '',
                    leader: null
                },
                editGroupForm: {
                    id: null,
                    name: '',
                    description: '',
                    leader: null
                },
                userList: [],
                userIdList: [],
                //团队树结构数据
                groupTree: [],
                setUserVisible: false,
                setUserDTO: {
                    groupId: null,
                    groupName: '',
                    userIds: []
                },
                checkAll: false,
                isIndeterminate: false,
                addUserVisible: false,
                editUserVisible: false,
                addUserForm: {
                    name: '',
                    account: '',
                    jobName: '',
                    phone: '',
                    userRole: '',
                    groupId: null,
                    jobRole: '',
                    level: '',
                    departmentId: '',
                    checkSort: '',
                    jobNumber: '',
                    email: '',
                    checkUserList: []
                },
                userDetail: {},
                editUserForm: {
                    id: null,
                    status: null,
                    name: '',
                    account: '',
                    jobName: '',
                    phone: '',
                    userRole: '',
                    groupId: null,
                    jobRole: '',
                    level: '',
                    departmentId: '',
                    checkSort: '',
                    jobNumber: '',
                    email: '',
                    checkUserList: []
                },
                statusOptions: [{
                    value: 0,
                    label: '正常使用'
                }, {
                    value: 1,
                    label: '冻结使用'
                }],
                //用户权限
                options: [
                    {
                        value: 0,
                        label: '超级管理员'
                    },
                    {
                        value: 1,
                        label: '项目管理者'
                    },
                    {
                        value: 2,
                        label: '普通成员'
                    },
                    {
                        value: 3,
                        label: '人事行政'
                    }
                ],
                //角色
                rolesList: [
                    {
                        roleId: 3,
                        roleName: '产品经理'
                    },
                    {
                        roleId: 0,
                        roleName: '测试'
                    },
                    {
                        roleId: 2,
                        roleName: 'UI设计'
                    },
                    {
                        roleId: 1,
                        roleName: 'JAVA开发'
                    },
                    {
                        roleId: 4,
                        roleName: 'C++开发'
                    },
                    {
                        roleId: 5,
                        roleName: 'PHP开发'
                    },
                    {
                        roleId: 6,
                        roleName: '前端开发'
                    },
                    {
                        roleId: 7,
                        roleName: 'IOS开发'
                    },
                    {
                        roleId: 8,
                        roleName: 'Android开发'
                    },
                    {
                        roleId: 9,
                        roleName: '人工智能'
                    },
                    {
                        roleId: 10,
                        roleName: '其他'
                    }
                ],
                //级别
                levelList: [
                    {id: 1, name: '一级'},
                    {id: 2, name: '二级'},
                    {id: 3, name: '三级'},
                    {id: 4, name: '四级'},
                    {id: 5, name: '五级'},
                    {id: 6, name: '六级'},
                    {id: 7, name: '七级'},
                    {id: 8, name: '八级'},
                    {id: 9, name: '九级'}
                ],
                num: 1,
                num2: 1,
                checkUserIdList: [],
                checkUserIdList2: [],
            };
        },
        //数据初始化
        beforeMount() {
            let _this = this;
            //选中组织tab
            _this.$root.eventBus.$emit("handleTabSelected", "organization");
            //初始化部门树结构数据
            // _this.fetchGroupTree();
            Http.zsyGetHttp('/dept/tree', null, (res) => {
                _this.departmentTree.push(res.data);
                _this.queryForm.deptId = _this.departmentTree[0].id;
                _this.queryForm.pageIndex = 1;
                _this.queryForm.groupId = null;
                _this.userPaging(_this.queryForm);
            });
            _this.fetchGroupList();
            _this.fetchSignInUser();
        },
        methods: {
            //分页上一页和下一页样式
            pagingLayout(total) {
                if (total > 0) {
                    this.pagination.layout = 'total,prev,pager,next';
                } else {
                    this.pagination.layout = 'total,pager';
                }
            },
            //用户按部门分页查询
            userPaging(queryForm) {
                Http.zsyPostHttp(`/user/page`, queryForm, (res) => {
                    this.tableData = res.data.list;
                    this.pagination.pageSize = res.data.pageSize;
                    this.pagination.total = res.data.total;
                    this.pagination.pageIndex = res.data.pageNum;
                    this.pagingLayout(this.pagination.total);
                });
            },
            //点击页码
            handleCurrentChange(val) {
                this.queryForm.pageIndex = val
                this.userPaging(this.queryForm);
            },
            // 部门结构树点击
            handleNodeClick(data) {
                //最小子节点,可点击
                this.queryForm.groupId = null;
                this.queryForm.deptId = data.id;
                this.queryForm.pageIndex = 1;
                this.userPaging(this.queryForm);
            },
            //团队树点击
            handleGroupNodeClick(data) {
                //最小子节点,可点击
                this.fetchUserPage(data.id)
            },
            // 重置用户密码确认框
            resetUserPwdDlgShow(index) {
                let userId = this.tableData[index].id;
                this.$confirm('此操作将重置密码, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.resetUserPwd(userId);
                }).catch(() => {
                });
            },
            // 重置用户密码
            resetUserPwd(userId) {
                Http.zsyPutHttp(`/user/repwd/${userId}`, null, (res) => {
                    this.$message({showClose: true, message: '重置密码成功', type: 'success'});
                });
            },
            //删除用户确认框
            deleteUserDlgShow(index) {
                let userId = this.tableData[index].id;
                // 删除
                this.$confirm('此操作将删除该条数据, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.deleteUser(userId);
                }).catch(() => {
                });
            },
            //删除用户
            deleteUser(userId) {
                Http.zsyDeleteHttp(`/user/${userId}`, null, (res) => {
                    this.$message({showClose: true, message: '删除用户成功', type: 'success'});
                    this.queryForm.deptId = this.departmentTree[0].id;
                    this.queryForm.pageIndex = 1;
                    this.userPaging(this.queryForm);
                });
            },
            //添加部门弹窗
            addDepartDlgShow() {
                // 添加部门
                this.$refs.showAddDepartment.show();
            },
            //添加部门成功,刷新部门数据
            handleDeptDataRefresh() {
                // 部门结构数据
                Http.zsyGetHttp('/dept/tree', null, (res) => {
                    this.departmentTree = [];
                    this.departmentTree.push(res.data);
                });
            },
            //添加用户弹窗
            addUserDlgShow() {
                this.$refs.showAddMember.setDeptId(this.queryForm.deptId);
                this.$refs.showAddMember.show();
            },
            //修改用户弹窗
            modifyUserDlgShow(index) {
                let userId = this.tableData[index].id;
                this.$refs.showModifyMember.setUserId(userId);
                this.$refs.showModifyMember.show();
            },
            //添加用户成功,刷新用户数据
            handleUserDataRefresh() {
                this.queryForm.deptId = this.departmentTree[0].id;
                this.queryForm.pageIndex = 1;
                this.userPaging(this.queryForm);
            },
            //根据不同角色查询
            changeJobRole() {
                this.queryForm.groupId = null;
                this.queryForm.deptId = this.departmentTree[0].id;
                this.queryForm.pageIndex = 1;
                this.userPaging(this.queryForm)
            },

            //根据不同用户类型查询
            changeUserType() {
                this.queryForm.groupId = null;
                this.queryForm.deptId = this.departmentTree[0].id;
                this.queryForm.pageIndex = 1;
                this.userPaging(this.queryForm)
            },

            //查询团队树结构
            fetchGroupTree() {
                Http.zsyGetHttp('/work-group/tree', {}, res => {
                    this.groupTree.push(res.data);
                })
            },
            //查询所有可用团队
            fetchGroupList() {
                Http.zsyGetHttp('/work-group/list', {}, res => {
                    this.groupList = res.data;
                })
            },

            //添加团队弹窗
            addGroupDlgShow() {
                this.addGroupVisible = true
            },
            //编辑团队弹窗
            editGroupDlgShow(id) {
                Http.zsyGetHttp('/work-group/' + id, {}, res => {
                    this.groupDetail = res.data;
                    this.editGroupForm.id = this.groupDetail.id;
                    this.editGroupForm.name = this.groupDetail.name;
                    this.editGroupForm.description = this.groupDetail.description;
                    this.editGroupForm.leader = this.groupDetail.leader;
                    this.editGroupVisible = true
                })

            },
            fetchUserPage(id) {
                this.queryForm.groupId = id;
                this.queryForm.pageIndex = 1;
                Http.zsyPostHttp(`/user/page`, this.queryForm, (res) => {
                    this.tableData = res.data.list;
                    this.pagination.pageSize = res.data.pageSize;
                    this.pagination.total = res.data.total;
                    this.pagination.pageIndex = res.data.pageNum;
                    this.pagingLayout(this.pagination.total);
                });
            },
            //删除团队
            deleteGroup(id) {
                this.$confirm('此操作将删除该团队, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    Http.zsyDeleteHttp(`/work-group/delete/` + id, {}, (resp) => {
                        this.$message({showClose: true, message: '删除成功', type: 'success'});
                        this.fetchGroupList();
                        this.editGroupVisible = false;
                    })
                }).catch(() => {
                });
            },
            //查询团队详情
            fetchGroupDetail(id) {
                Http.zsyGetHttp('/work-group/' + id, {}, res => {
                    this.groupDetail = res.data;
                })
            },

            //获取考勤人员列表
            fetchSignInUser() {
                Http.zsyGetHttp('/sign-in/users', {}, (res) => {
                    if (res) {
                        this.userList = res.data
                        this.userIdList = []
                        if (this.userList.length > 0) {
                            this.userList.forEach(user => {
                                this.userIdList.push(user.userId)
                            })
                        }
                    }
                })
            },
            //清空表单
            clearAddForm() {
                this.addGroupForm.name = '';
                this.addGroupForm.description = '';
                this.addGroupForm.leader = null;
            },

            clearEditForm() {
                this.editGroupForm.name = '';
                this.editGroupForm.description = '';
                this.editGroupForm.leader = null;
                this.editGroupForm.id = null;
            },
            closeSetUser() {
                this.setUserDTO.groupId = null
                this.setUserDTO.groupName = ''
                this.setUserDTO.userIds = []
                this.$refs.setUserForm.resetFields()
                this.setUserVisible = false
                this.isSaving = false
            },
            //配置成员
            setUser(group) {
                this.setUserDTO.groupId = group.id;
                this.setUserDTO.groupName = group.name;
                this.fetchGroupUsers(group.id);
                this.setUserVisible = true
            },
            //保存设置团队成员
            saveSetUser(groupId) {
                Http.zsyPostHttp('work-group/add-user', this.setUserDTO, res => {
                    this.$message({
                        showClose: true,
                        message: '配置成员成功',
                        type: 'success'
                    });
                    this.closeSetUser();
                    this.queryForm.pageIndex = 1;
                    this.queryForm.deptId = this.departmentTree[0].id;
                    this.queryForm.groupId = groupId;
                    this.userPaging(this.queryForm)
                })
            },
            //新增团队
            saveAddGroup() {
                this.isSaving = true;
                if (this.addGroupForm.name == null || this.addGroupForm.name === undefined
                    || this.addGroupForm.name.trim() === '') {
                    this.$message({
                        showClose: true,
                        message: '请输入团队名称',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                if (this.addGroupForm.description == null || this.addGroupForm.description === undefined
                    || this.addGroupForm.description.trim() === '') {
                    this.$message({
                        showClose: true,
                        message: '请输入团队描述',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                if (this.addGroupForm.leader == null || this.addGroupForm.leader === undefined
                    || this.addGroupForm.leader === '') {
                    this.$message({
                        showClose: true,
                        message: '请选择团队负责人',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                Http.zsyPostHttp('work-group/add', this.addGroupForm, res => {
                        this.$message({
                            showClose: true,
                            message: '添加团队成功',
                            type: 'success'
                        });
                        this.addGroupVisible = false;
                        this.isSaving = false;
                        this.groupTree = [];
                        this.fetchGroupList();
                    },
                    err => {
                        this.$message({
                            showClose: true,
                            message: err.errMsg,
                            type: 'error'
                        });
                        this.isSaving = false;
                    },
                    sysError => {
                        this.$message({
                            showClose: true,
                            message: sysError.errMsg,
                            type: 'error'
                        });
                        this.isSaving = false;
                    })
            },

            //编辑团队
            saveEditGroup() {
                this.isSaving = true;
                if (this.editGroupForm.id == null || this.editGroupForm.id === undefined
                    || this.editGroupForm.id === '') {
                    this.$message({
                        showClose: true,
                        message: '团队ID不能为空',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                if (this.editGroupForm.name == null || this.editGroupForm.name === undefined
                    || this.editGroupForm.name.trim() === '') {
                    this.$message({
                        showClose: true,
                        message: '请输入团队名称',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                if (this.editGroupForm.description == null || this.editGroupForm.description === undefined
                    || this.editGroupForm.description.trim() === '') {
                    this.$message({
                        showClose: true,
                        message: '请输入团队描述',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                if (this.editGroupForm.leader == null || this.editGroupForm.leader === undefined
                    || this.editGroupForm.leader === '') {
                    this.$message({
                        showClose: true,
                        message: '请选择团队负责人',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                Http.zsyPutHttp('work-group/update', this.editGroupForm, res => {
                        this.$message({
                            showClose: true,
                            message: '编辑团队成功',
                            type: 'success'
                        });
                        this.editGroupVisible = false;
                        this.isSaving = false;
                        this.fetchGroupList();
                    },
                    err => {
                        this.$message({
                            showClose: true,
                            message: err.errMsg,
                            type: 'error'
                        });
                        this.isSaving = false;
                    },
                    sysError => {
                        this.$message({
                            showClose: true,
                            message: sysError.errMsg,
                            type: 'error'
                        });
                        this.isSaving = false;
                    })
            },

            handleCheckAllChange(val) {
                this.setUserDTO.userIds = val ? this.userIdList : []
                this.isIndeterminate = false
            },
            handleCheckedUsersChange(value) {
                const checkedCount = value.length
                this.checkAll = checkedCount === this.userList.length
                this.isIndeterminate = checkedCount > 0 && checkedCount < this.userList.length
            },
            //查询团队成员
            fetchGroupUsers(groupId) {
                Http.zsyGetHttp('work-group/user/' + groupId, {}, res => {
                    var users = []
                    users = res.data
                    if (users.length > 0) {
                        this.isIndeterminate = true
                        users.forEach(user => {
                            this.setUserDTO.userIds.push(user.id)
                        })
                    }
                })
            },
            //打开新增用户弹框
            addUser() {
                this.addUserForm = {};
                this.addUserForm.checkUserList = [];
                this.addUserForm.departmentId = this.queryForm.deptId;
                this.addUserVisible = true;
            },
            //保存新增用户
            saveAddUser() {
                if (this.addUserForm.name == null || this.addUserForm.name === undefined || this.addUserForm.name.trim() === '') {
                    this.warnMsg("请填写用户名称");
                    return;
                }
                if (this.addUserForm.account == null || this.addUserForm.account === undefined || this.addUserForm.account.trim() === '') {
                    this.warnMsg("请填写用户账号");
                    return;
                }
                if (this.addUserForm.jobName == null || this.addUserForm.jobName === undefined || this.addUserForm.jobName.trim() === '') {
                    this.warnMsg("请填写用户职位");
                    return;
                }
                if (this.addUserForm.phone == null || this.addUserForm.phone === undefined || this.addUserForm.phone.trim() === '') {
                    this.warnMsg("请填写用户手机号");
                    return;
                }
                if (this.addUserForm.userRole == null || this.addUserForm.userRole === undefined || this.addUserForm.userRole === '') {
                    this.warnMsg("请选择用户权限");
                    return;
                }
                if (this.addUserForm.jobRole == null || this.addUserForm.jobRole === undefined || this.addUserForm.jobRole === '') {
                    this.warnMsg("请选择用户角色");
                    return;
                }
                if (this.addUserForm.level == null || this.addUserForm.level === undefined || this.addUserForm.level === '') {
                    this.warnMsg("请选择用户级别");
                    return;
                }
                if (this.addUserForm.checkSort == null || this.addUserForm.checkSort === undefined || this.addUserForm.checkSort === '') {
                    this.warnMsg("请输入考勤序号");
                    return;
                }
                if (this.addUserForm.jobNumber == null || this.addUserForm.jobNumber === undefined || this.addUserForm.jobNumber === '') {
                    this.warnMsg("请输入用户工号");
                    return;
                }
                let checkUsers = this.addUserForm.checkUserList;
                let checkUserIds = [];
                checkUsers.forEach(checkUser => {
                    if (checkUser.id != null && checkUser.id !== '') {
                        checkUserIds.push(checkUser.id)
                    }
                });
                if (checkUserIds == null || checkUserIds === [] || checkUserIds.length === 0 || checkUserIds.length !== checkUsers.length) {
                    this.warnMsg("请选择审核人");
                    return;
                }
                let nary = checkUserIds.sort();
                for (let i = 0; i < checkUserIds.length; i++) {
                    if (nary[i] === nary[i + 1]) {
                        this.warnMsg("多级审核人重复,请检查");
                        return;
                    }
                }
                Http.zsyPostHttp('/user/add', this.addUserForm, (res) => {
                    this.hideAddUser();
                    this.$message({
                        showClose: true,
                        message: '用户添加成功',
                        type: 'success'
                    });
                    this.queryForm.pageIndex = 1;
                    this.queryForm.deptId = this.departmentTree[0].id;
                    this.queryForm.groupId = null;
                    this.userPaging(this.queryForm);
                    this.fetchSignInUser()
                });
            },
            warnMsg(msg) {
                this.$message({
                    showClose: true,
                    message: msg,
                    type: 'warning'
                });
            },
            //关闭添加用户弹框
            hideAddUser() {
                this.addUserForm = {};
                this.addUserForm.checkUserList = [];
                this.num = 1;
                this.checkUserIdList = [];
                this.addUserVisible = false
            },
            //打开编辑用户弹框
            editUser(userId) {
                this.editUserForm = {};
                this.editUserForm.checkUserList = [];
                this.num2 = 1;
                this.checkUserIdList2 = [];
                Http.zsyGetHttp(`/user/${userId}`, null, (res) => {
                    this.userDetail = res.data;
                    this.editUserForm.id = res.data.id;
                    this.editUserForm.name = res.data.name;
                    this.editUserForm.account = res.data.account;
                    this.editUserForm.jobName = res.data.jobName;
                    this.editUserForm.phone = res.data.phone;
                    this.editUserForm.userRole = res.data.userRole;
                    this.editUserForm.jobRole = res.data.jobRole;
                    this.editUserForm.level = res.data.level;
                    this.editUserForm.status = res.data.status;
                    this.editUserForm.email = res.data.email;
                    this.editUserForm.departmentId = res.data.departmentId;
                    this.editUserForm.checkSort = res.data.checkSort;
                    this.editUserForm.jobNumber = res.data.jobNumber;
                    this.editUserForm.groupId = res.data.groupId;
                    this.editUserForm.checkUserList = res.data.checkUsers;
                    if (this.editUserForm.checkUserList != null && this.editUserForm.checkUserList !== [] && this.editUserForm.checkUserList.length > 0) {
                        this.num2 = this.editUserForm.checkUserList.length;
                        this.editUserForm.checkUserList.forEach(checkUser => {
                            this.checkUserIdList2.push(checkUser.id)
                        })
                    }
                    this.editUserVisible = true;
                });
            },
            //关闭编辑用户弹框
            hideEditUser() {
                this.editUserForm = {};
                this.editUserForm.checkUserList = [];
                this.editUserVisible = false;
            },
            //保存编辑用户
            saveEditUser() {
                if (this.userDetail.id == null || this.userDetail.id === undefined || this.userDetail.id === '') {
                    this.warnMsg("用户id不能为空");
                    return;
                }
                if (this.userDetail.name == null || this.userDetail.name === undefined || this.userDetail.name.trim() === '') {
                    this.warnMsg("请填写用户名称");
                    return;
                }
                if (this.userDetail.account == null || this.userDetail.account === undefined || this.userDetail.account.trim() === '') {
                    this.warnMsg("请填写用户账号");
                    return;
                }
                if (this.userDetail.jobName == null || this.userDetail.jobName === undefined || this.userDetail.jobName.trim() === '') {
                    this.warnMsg("请填写用户职位");
                    return;
                }
                if (this.userDetail.phone == null || this.userDetail.phone === undefined || this.userDetail.phone.trim() === '') {
                    this.warnMsg("请填写用户手机号");
                    return;
                }
                if (this.userDetail.userRole == null || this.userDetail.userRole === undefined || this.userDetail.userRole === '') {
                    this.warnMsg("请选择用户权限");
                    return;
                }
                if (this.userDetail.jobRole == null || this.userDetail.jobRole === undefined || this.userDetail.jobRole === '') {
                    this.warnMsg("请选择用户角色");
                    return;
                }
                if (this.userDetail.level == null || this.userDetail.level === undefined || this.userDetail.level === '') {
                    this.warnMsg("请选择用户级别");
                    return;
                }
                if (this.userDetail.status == null || this.userDetail.status === undefined || this.userDetail.status === '') {
                    this.warnMsg("请选择状态");
                    return;
                }
                if (this.userDetail.checkSort == null || this.userDetail.checkSort === undefined || this.userDetail.checkSort === '') {
                    this.warnMsg("请输入考勤序号");
                    return;
                }
                if (this.userDetail.jobNumber == null || this.userDetail.jobNumber === undefined || this.userDetail.jobNumber === '') {
                    this.warnMsg("请输入用户工号");
                    return;
                }

                let checkUsers = this.editUserForm.checkUserList;
                let checkUserIds = [];
                checkUsers.forEach(checkUser => {
                    if (checkUser.id != null && checkUser.id !== '') {
                        checkUserIds.push(checkUser.id)
                    }
                });
                if (checkUserIds == null || checkUserIds === [] || checkUserIds.length === 0 || checkUserIds.length !== checkUsers.length) {
                    this.warnMsg("请选择审核人");
                    return;
                }
                let nary = checkUserIds.sort();
                for (let i = 0; i < checkUserIds.length; i++) {
                    if (nary[i] === nary[i + 1]) {
                        this.warnMsg("多级审核人重复,请检查");
                        return;
                    }
                }
                this.userDetail.checkUserList = checkUsers;
                Http.zsyPutHttp('/user/' + this.userDetail.id, this.userDetail, (res) => {
                    this.hideEditUser();
                    this.$message({
                        showClose: true,
                        message: '用户修改成功',
                        type: 'success'
                    });
                    this.queryForm.pageIndex = 1;
                    this.queryForm.deptId = this.departmentTree[0].id;
                    this.queryForm.groupId = null;
                    this.userPaging(this.queryForm);
                    this.fetchSignInUser()
                });
            },
            //查询用户详情
            getUserDetail(userId) {
                Http.zsyGetHttp(`/user/${userId}`, null, (res) => {
                    this.editUserForm.id = res.data.id;
                    this.editUserForm.name = res.data.name;
                    this.editUserForm.account = res.data.account;
                    this.editUserForm.jobName = res.data.jobName;
                    this.editUserForm.phone = res.data.phone;
                    this.editUserForm.userRole = res.data.userRole;
                    this.editUserForm.jobRole = res.data.jobRole;
                    this.editUserForm.level = res.data.level;
                    this.editUserForm.status = res.data.status;
                    this.editUserForm.email = res.data.email;
                    this.editUserForm.departmentId = res.data.departmentId;
                    this.editUserForm.checkSort = res.data.checkSort;
                    this.editUserForm.jobNumber = res.data.jobNumber;
                    this.editUserForm.groupId = res.data.groupId;
                    this.editUserForm.checkUserList = res.data.checkUsers;
                    if (this.editUserForm.checkUserList != null && this.editUserForm.checkUserList !== [] && this.editUserForm.checkUserList.length > 0) {
                        this.num = this.editUserForm.checkUserList.length;
                        this.editUserForm.checkUserList.forEach(checkUser => {
                            this.checkUserIdList.push(checkUser.id)
                        })
                    }
                });
            },
            plus() {
                this.num = this.num + 1;
            },
            minus(i) {
                this.num = this.num - 1;
                this.checkUserIdList.splice(i);
                this.addUserForm.checkUserList.splice(i);
            },
            plus2() {
                this.num2 = this.num2 + 1;
            },
            minus2(i) {
                this.num2 = this.num2 - 1;
                this.checkUserIdList2.splice(i);
                this.editUserForm.checkUserList.splice(i);
            },
            addCheckUser(i) {
                let checkUsers = this.addUserForm.checkUserList;
                let flag = true;
                checkUsers.forEach(user => {
                    if (user.level === i) {
                        user.id = this.checkUserIdList[i - 1];
                        flag = false;
                    }
                });
                this.addUserForm.checkUserList = checkUsers;
                if (flag) {
                    let checkUser = {
                        'level': i,
                        'id': this.checkUserIdList[i - 1]
                    };
                    this.addUserForm.checkUserList.push(checkUser);
                }
            },
            addCheckUser2(i) {
                let checkUsers = this.editUserForm.checkUserList;
                let flag = true;
                checkUsers.forEach(user => {
                    if (user.level === i) {
                        user.id = this.checkUserIdList2[i - 1];
                        flag = false;
                    }
                });
                this.editUserForm.checkUserList = checkUsers;
                if (flag) {
                    let checkUser = {
                        'level': i,
                        'id': this.checkUserIdList2[i - 1]
                    };
                    this.editUserForm.checkUserList.push(checkUser);
                }
            },

        },
        components: {
            AddMember: AddMember,
            ModifyMember: ModifyMember,
            AddDepartment: AddDepartment
        }
    }
</script>

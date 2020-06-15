<template>
  <div class="organization-con clearfix">
    <div class="department-structure fl">
      <div style="height: 50%">
        <div class="ds-title">部门结构<span class="add-department" @click="addDepartDlgShow">+</span></div>
        <el-tree :data="departmentTree"
                 :props="defaultProps"
                 default-expand-all
                 :expand-on-click-node="false"
                 @node-click="handleNodeClick" />
      </div>
      <!--<div style="height: 50%">-->
        <!--<div class="ds-title">团队结构<span class="add-department" @click="addGroupDlgShow">+</span></div>-->
        <!--<el-tree :data="groupTree"-->
                 <!--:props="groupProps"-->
                 <!--default-expand-all-->
                 <!--:expand-on-click-node="false"-->
                 <!--@node-click="handleGroupNodeClick"-->
                 <!--:render-content="renderContent"/>-->
      <!--</div>-->

      <div>
        <div class="ds-title">团队<span class="add-department" @click="addGroupDlgShow">+</span></div>
        <div>
          <div v-for="item in groupList" style="margin-left: 20px;margin-top: 8px">
            <span style="font-size: 14px;cursor: pointer;color:#606266" @click="fetchUserPage(item.id)">{{item.name}}</span>
            <span class="fr" style="color: #26a2ff;margin-top: 3px;cursor: pointer;margin-right: 5px" @click="deleteGroup(item.id)">删除</span>
            <span class="fr" style="margin-right: 5px;color: #26a2ff;margin-top: 3px;cursor: pointer" @click="editGroupDlgShow(item.id)">编辑</span>
            <span class="fr" style="color: #26a2ff;margin-top: 3px;cursor: pointer;margin-right: 5px" @click="setUser(item)">配置成员</span>
          </div>
        </div>
      </div>


    </div>
    <div class="department-member fl">
      <div class="dm-title" style="">
        成员
        <el-select v-model="queryForm.jobRole" placeholder="请选择角色" @change="changeJobRole(queryForm.jobRole)" clearable>
          <el-option
          v-for="item in jobRoleList"
          :key="item.roleId"
          :label="item.roleName"
          :value="item.roleId"></el-option>
        </el-select>
        <el-select v-model="queryForm.userType" placeholder="请选择用户类型" @change="changeUserType(queryForm.userType)" clearable>
          <el-option
                  v-for="item in userTypeList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"></el-option>
        </el-select>
        <span class="add-department" @click="addUserDlgShow">+</span>
      </div>
      <!--<div class="dm-title" style="width: 50%;">成员<span class="add-department" @click="addUserDlgShow">+</span></div>-->
      <div class="white-bg">
        <div class="department-member-table">
          <el-table :data="tableData" stripe style="width: 100%">
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
                <el-button type="text" size="small" @click.native.prevent="modifyUserDlgShow(scope.$index)">编辑</el-button>
                <el-button type="text" size="small" @click.native.prevent="resetUserPwdDlgShow(scope.$index)">重置密码</el-button>
                <el-button type="text" size="small" v-show="scope.row.isDelete===0" @click.native.prevent="deleteUserDlgShow(scope.$index)">删除</el-button>
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
          <el-input type="textarea" class="w280" v-model="addGroupForm.description" clearable placeholder="请输入团队描述"></el-input>
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
          <el-input type="textarea" class="w280" v-model="editGroupForm.description" clearable placeholder="请输入团队描述"></el-input>
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
          <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange(checkAll)">全选</el-checkbox>
          <!--<el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">全选</el-checkbox>-->
          <div style="margin: 15px 0;" />
          <el-checkbox-group v-model="setUserDTO.userIds" @change="handleCheckedUsersChange">
            <el-checkbox v-for="user in userList" :key="user.userId" :label="user.userId" :value="user.userId">
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
  </div>
</template>
<style type="text/css" scoped>
  .organization-con .ctpc-top{
    font-size: 16px;line-height: 30px;font-weight: 700;color: #333;
  }
  .organization-con .ctpc-top-close{font-size: 30px;width: 30px;height: 30px;line-height: 27px;cursor: pointer;transition:0.8s ease all;text-align: center;}
  .organization-con .ftp-list{margin: 20px 0;font-size: 14px;}
  .organization-con .ftp-menus{width: 130px;text-align: right;margin-right: 16px;line-height: 30px;}
  .organization-con .ftp-msg{line-height: 30px;}
  .organization-con .am-warn{height: 20px;line-height: 20px;text-indent: 84px;color: red;}
  .organization-con .ctpc-btns{text-align: right;}
  .organization-con .ctpc-btns > input{display: inline-block;width: 80px;height: 28px;margin-left: 12px;cursor: pointer;border-radius: 4px;}
  .organization-con .ctpc-cancel{background: #fff;border: 1px solid #ccc;}
  .organization-con .ctpc-cancel:hover{background: #fff;border: 1px solid #36A8FF;color: #36A8FF;font-weight: 700;}
  .organization-con .ctpc-save{background: #36A8FF;color: #fff;}



.organization-con{width: 1300px;margin: auto;}
.department-structure{width: 260px;background: #fff;padding-bottom: 30px;height: 564px;overflow-y: scroll;}
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
.ds-title,.dm-title{position: relative;line-height: 34px;background: #36A8FF;color: #fff;font-size: 14px;padding: 0 10px;margin-bottom: 10px;}
.add-department{position: absolute;display: block;border: 1px solid #fff;width: 22px;height: 22px;border-radius: 50%;text-align: center;line-height: 20px;right: 10px;top: 5px;font-size: 24px;cursor: pointer;}
.department-member{width: 1000px;height: 594px;margin-left: 20px;background: #fff;}
.dm-title{margin-bottom: 0;}
.white-bg{background: #fff;}
</style>
<style>
  .add-group .el-dialog{ width: 536px;}
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
                userTypeList:[
                    {id:0,name:'全部用户'},
                    {id:1,name:'正常用户'},
                    {id:2,name:'冻结用户'},
                ],
                jobRoleList:[
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
                defaultProps:{
                    id: 'id',
                    label: 'label',
                    children: 'children'
                },
                groupProps:{
                    id: 'id',
                    label: 'label',
                    children: 'children'
                },
                groupList:[],
                addGroupVisible:false,
                editGroupVisible:false,
                groupDetail:{},
                isSaving:false,
                addGroupForm:{
                    name:'',
                    description:'',
                    leader:null
                },
                editGroupForm:{
                    id:null,
                    name:'',
                    description:'',
                    leader:null
                },
                userList:[],
                userIdList:[],
                //团队树结构数据
                groupTree: [],
                setUserVisible: false,
                setUserDTO: {
                    groupId: null,
                    groupName:'',
                    userIds: []
                },
                checkAll: false,
                isIndeterminate: false,
            };
        },
        //数据初始化
        beforeMount(){
            let _this = this;
            //选中组织tab
            _this.$root.eventBus.$emit("handleTabSelected", "organization");
            //初始化部门树结构数据
            // _this.fetchGroupTree();
            Http.zsyGetHttp('/dept/tree',null,(res)=>{
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
                if (total>0){
                    this.pagination.layout = 'total,prev,pager,next';
                }else{
                    this.pagination.layout = 'total,pager';
                }
            },
            //用户按部门分页查询
            userPaging(queryForm) {
                Http.zsyPostHttp(`/user/page`,queryForm,(res)=>{
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
            handleGroupNodeClick(data){
                //最小子节点,可点击
                this.fetchUserPage(data.id)
            },
            // 重置用户密码确认框
            resetUserPwdDlgShow (index) {
                let userId = this.tableData[index].id;
                this.$confirm('此操作将重置密码, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.resetUserPwd(userId);
                }).catch(() => {});
            },
            // 重置用户密码
            resetUserPwd (userId){
                Http.zsyPutHttp(`/user/repwd/${userId}`,null,(res)=>{
                    this.$message({showClose: true,message:'重置密码成功',type: 'success'});
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
                }).catch(() => {});
            },
            //删除用户
            deleteUser (userId){
                Http.zsyDeleteHttp(`/user/${userId}`,null,(res)=>{
                    this.$message({showClose: true, message: '删除用户成功',type: 'success'});
                    this.queryForm.deptId = this.departmentTree[0].id;
                    this.queryForm.pageIndex = 1;
                    this.userPaging(this.queryForm);
                });
            },
            //添加部门弹窗
            addDepartDlgShow () {
                // 添加部门
                this.$refs.showAddDepartment.show();
            },
            //添加部门成功,刷新部门数据
            handleDeptDataRefresh () {
                // 部门结构数据
                Http.zsyGetHttp('/dept/tree',null,(res)=>{
                    this.departmentTree = [];
                    this.departmentTree.push(res.data);
                });
            },
            //添加用户弹窗
            addUserDlgShow () {
                this.$refs.showAddMember.setDeptId(this.queryForm.deptId);
                this.$refs.showAddMember.show();
            },
            //修改用户弹窗
            modifyUserDlgShow (index) {
                let userId = this.tableData[index].id;
                this.$refs.showModifyMember.setUserId(userId);
                this.$refs.showModifyMember.show();
            },
            //添加用户成功,刷新用户数据
            handleUserDataRefresh () {
                this.queryForm.deptId = this.departmentTree[0].id;
                this.queryForm.pageIndex = 1;
                this.userPaging(this.queryForm);
            },
            //根据不同角色查询
            changeJobRole(){
                this.queryForm.groupId = null;
                this.queryForm.deptId = this.departmentTree[0].id;
                this.queryForm.pageIndex = 1;
                this.userPaging(this.queryForm)
            },

            //根据不同用户类型查询
            changeUserType(){
                this.queryForm.groupId = null;
                this.queryForm.deptId = this.departmentTree[0].id;
                this.queryForm.pageIndex = 1;
                this.userPaging(this.queryForm)
            },

            //查询团队树结构
            fetchGroupTree(){
              Http.zsyGetHttp('/work-group/tree',{},res=>{
                  this.groupTree.push(res.data);
              })
            },
            //查询所有可用团队
            fetchGroupList(){
                Http.zsyGetHttp('/work-group/list',{},res=>{
                    this.groupList= res.data;
                })
            },

            //添加团队弹窗
            addGroupDlgShow () {
                this.addGroupVisible = true
            },
            //编辑团队弹窗
            editGroupDlgShow(id){
                Http.zsyGetHttp('/work-group/'+id,{},res=>{
                    this.groupDetail = res.data;
                    this.editGroupForm.id = this.groupDetail.id;
                    this.editGroupForm.name = this.groupDetail.name;
                    this.editGroupForm.description = this.groupDetail.description;
                    this.editGroupForm.leader = this.groupDetail.leader;
                    this.editGroupVisible = true
                })

            },
            fetchUserPage(id){
                this.queryForm.groupId = id;
                this.queryForm.pageIndex = 1;
                Http.zsyPostHttp(`/user/page`,this.queryForm,(res)=>{
                    this.tableData = res.data.list;
                    this.pagination.pageSize = res.data.pageSize;
                    this.pagination.total = res.data.total;
                    this.pagination.pageIndex = res.data.pageNum;
                    this.pagingLayout(this.pagination.total);
                });
            },
            //删除团队
            deleteGroup(id){
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
            fetchGroupDetail(id){
                Http.zsyGetHttp('/work-group/'+id,{},res=>{
                    this.groupDetail = res.data;
                })
            },

            //获取考勤人员列表
            fetchSignInUser(){
                Http.zsyGetHttp('/sign-in/users',{},(res)=>{
                    if (res){
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
            clearAddForm(){
                this.addGroupForm.name = '';
              this.addGroupForm.description = '';
              this.addGroupForm.leader = null;
            },

            clearEditForm(){
                this.editGroupForm.name = '';
              this.editGroupForm.description = '';
              this.editGroupForm.leader = null;
              this.editGroupForm.id = null;
            },
            closeSetUser(){
                this.setUserDTO.groupId = null
                this.setUserDTO.groupName = ''
                this.setUserDTO.userIds = []
                this.$refs.setUserForm.resetFields()
                this.setUserVisible = false
                this.isSaving = false
            },
            //配置成员
            setUser(group){
                this.setUserDTO.groupId = group.id;
                this.setUserDTO.groupName = group.name;
                this.fetchGroupUsers(group.id);
                this.setUserVisible = true
            },
            //保存设置团队成员
            saveSetUser(groupId) {
                Http.zsyPostHttp('work-group/add-user',this.setUserDTO,res=>{
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
            saveAddGroup(){
                this.isSaving = true;
                if (this.addGroupForm.name == null || this.addGroupForm.name === undefined
                    || this.addGroupForm.name.trim() === ''){
                    this.$message({
                        showClose: true,
                        message: '请输入团队名称',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                if (this.addGroupForm.description == null || this.addGroupForm.description === undefined
                    || this.addGroupForm.description.trim() === ''){
                    this.$message({
                        showClose: true,
                        message: '请输入团队描述',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                if (this.addGroupForm.leader == null || this.addGroupForm.leader === undefined
                    || this.addGroupForm.leader === ''){
                    this.$message({
                        showClose: true,
                        message: '请选择团队负责人',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                Http.zsyPostHttp('work-group/add',this.addGroupForm,res=>{
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
                    err=>{
                        this.$message({
                            showClose: true,
                            message: err.errMsg,
                            type: 'error'
                        });
                        this.isSaving = false;
                    },
                    sysError=>{
                        this.$message({
                            showClose: true,
                            message: sysError.errMsg,
                            type: 'error'
                        });
                        this.isSaving = false;
                    })
            },

            //编辑团队
            saveEditGroup(){
                this.isSaving = true;
                if (this.editGroupForm.id == null || this.editGroupForm.id === undefined
                    || this.editGroupForm.id === ''){
                    this.$message({
                        showClose: true,
                        message: '团队ID不能为空',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                if (this.editGroupForm.name == null || this.editGroupForm.name === undefined
                    || this.editGroupForm.name.trim() === ''){
                    this.$message({
                        showClose: true,
                        message: '请输入团队名称',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                if (this.editGroupForm.description == null || this.editGroupForm.description === undefined
                    || this.editGroupForm.description.trim() === ''){
                    this.$message({
                        showClose: true,
                        message: '请输入团队描述',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                if (this.editGroupForm.leader == null || this.editGroupForm.leader === undefined
                    || this.editGroupForm.leader === ''){
                    this.$message({
                        showClose: true,
                        message: '请选择团队负责人',
                        type: 'warning'
                    });
                    this.isSaving = false;
                    return;
                }
                Http.zsyPutHttp('work-group/update',this.editGroupForm,res=>{
                    this.$message({
                        showClose: true,
                        message: '编辑团队成功',
                        type: 'success'
                    });
                    this.editGroupVisible = false;
                    this.isSaving = false;
                    this.fetchGroupList();
                },
                    err=>{
                        this.$message({
                            showClose: true,
                            message: err.errMsg,
                            type: 'error'
                        });
                        this.isSaving = false;
                    },
                    sysError=>{
                        this.$message({
                            showClose: true,
                            message: sysError.errMsg,
                            type: 'error'
                        });
                        this.isSaving = false;
                    })
            },

            renderContent(h, { node, data, store }) {
                // return (
                //     <span>
                //       <span>
                //         <span>{node.label}</span>
                //       </span>
                //    <span style="float: right; margin-right: 20px">
                //       <el-button size="mini" on-click={ () => this.append(store, data) }>Append</el-button>
                //       <el-button size="mini" on-click={ () => this.remove(store, data) }>Delete</el-button>
                //     </span>
                //     </span>);
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
            fetchGroupUsers(groupId){
                Http.zsyGetHttp('work-group/user/'+groupId,{},res=>{
                    var users = []
                    users = res.data
                    if (users.length > 0) {
                        this.isIndeterminate = true
                        users.forEach(user => {
                            this.setUserDTO.userIds.push(user.id)
                        })
                    }
                })
            }
        },
        components: {
            AddMember: AddMember,
            ModifyMember: ModifyMember,
            AddDepartment: AddDepartment
        }
    }
</script>

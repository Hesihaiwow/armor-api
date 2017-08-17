<template>
  <div class="organization-con clearfix">
    <div class="department-structure fl">
      <div class="ds-title">部门结构<span class="add-department" @click="addDepartDlgShow">+</span></div>
      <el-tree :data="departmentTree"
               :props="defaultProps"
               default-expand-all
               :expand-on-click-node="false"
               @node-click="handleNodeClick" />
    </div>
    <div class="department-member fl">
      <div class="dm-title">成员<span class="add-department" @click="addUserDlgShow">+</span></div>
      <div class="white-bg">
        <div class="department-member-table">
          <el-table :data="tableData" stripe style="width: 100%">
            <el-table-column prop="name" label="姓名" width="100" align="center"></el-table-column>
            <el-table-column prop="account" label="用户名" width="100" align="center"></el-table-column>
            <el-table-column prop="jobName" label="职位" width="100" align="center"></el-table-column>
            <el-table-column prop="phone" label="手机号" width="116" align="center"></el-table-column>
            <el-table-column prop="createTime" label="创建日期" width="106" align="center"></el-table-column>
            <el-table-column prop="lastLogin" label="最后登录" width="106" align="center"></el-table-column>
            <el-table-column prop="operate" label="操作" align="center">
              <template scope="scope">
                <!-- <el-button type="text" size="small" @click.native.prevent="authorityOpt(scope.$index, tableData)">权限</el-button> -->
                <el-button type="text" size="small" @click.native.prevent="modifyUserDlgShow(scope.$index)">编辑</el-button>
                <el-button type="text" size="small" @click.native.prevent="resetUserPwdDlgShow(scope.$index)">重置密码</el-button>
                <el-button type="text" size="small" @click.native.prevent="deleteUserDlgShow(scope.$index)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <el-pagination
              :current-page="queryForm.pageIndex"
              @current-change="handleCurrentChange"
              :page-size="pagination.pageSize"
              :layout="pagination.layout"
              :total="pagination.total">
      </el-pagination>
    </div>
    <add-member ref="showAddMember" @handleUserDataRefresh="handleUserDataRefresh"></add-member>
    <modify-member ref="showModifyMember" @handleUserDataRefresh="handleUserDataRefresh"></modify-member>
    <add-department ref="showAddDepartment" @handleDeptDataRefresh="handleDeptDataRefresh"></add-department>
  </div>
</template>
<style type="text/css" scoped>
.organization-con{width: 1100px;margin: auto;}
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
.department-member{width: 820px;height: 594px;margin-left: 20px;background: #fff;}
.dm-title{margin-bottom: 0;}
.white-bg{background: #fff;}
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
                  deptId: ''
                },
                //分页信息
                pagination: {
                    total: 0,
                    pageSize: 10,
                    layout: 'total, pager'
                },
                //部门树结构数据
                departmentTree: [],
                //部门树结构父子传递字段
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
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
                }
            };
        },
        //数据初始化
        beforeMount(){
            let _this = this;
            //选中组织tab
            _this.$root.eventBus.$emit("handleTabSelected", "organization");
            //初始化部门树结构数据
            Http.zsyGetHttp('/dept/tree',null,(res)=>{
                _this.departmentTree.push(res.data);
                _this.userPaging(_this.departmentTree[0].id,_this.queryForm.pageIndex);
            });
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
            userPaging(deptId,pageIndex) {
                Http.zsyGetHttp(`/user/page/${deptId}/${pageIndex}`,null,(res)=>{
                    this.queryForm.pageIndex = pageIndex;
                    this.queryForm.deptId = deptId;
                    this.tableData = res.data.list;
                    this.pagination.pageSize = res.data.pageSize;
                    this.pagination.total = res.data.total;
                    this.pagingLayout(this.pagination.total);
                });
            },
            //点击页码
            handleCurrentChange(val) {
                this.userPaging(this.queryForm.deptId,val);
            },
            // 部门结构树点击
            handleNodeClick(data) {
                //最小子节点,可点击
                this.userPaging(data.id,1);
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
                    this.$message.success('重置密码成功');
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
                    this.$message.success('删除用户成功');
                    this.userPaging(this.queryForm.deptId,this.queryForm.pageIndex);
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
                this.userPaging(this.queryForm.deptId,this.queryForm.pageIndex);
            }
        },
        components: {
            AddMember: AddMember,
            ModifyMember: ModifyMember,
            AddDepartment: AddDepartment
        }
    }
</script>
<template>
  <div class="organization-con clearfix">
    <div class="department-structure fl">
      <div class="ds-title">部门结构<span class="add-department" @click="addDepart">+</span></div>
      <el-tree :data="departmentTree"
               :props="defaultProps"
               default-expand-all
               :expand-on-click-node="false"
               @node-click="handleNodeClick" />
    </div>
    <div class="department-member fl">
      <div class="dm-title">成员<span class="add-department" @click="AddMemberShow">+</span></div>
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
                <el-button type="text" size="small" @click.native.prevent="editRow(scope.$index, tableData)">编辑</el-button>
                <el-button type="text" size="small" @click.native.prevent="resetUserPwdDlgShow(scope.$index)">重置密码</el-button>
                <el-button type="text" size="small" @click.native.prevent="deleteUserDlgShow(scope.$index, tableData)">删除</el-button>
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
    <add-member ref="showAddMember" :ftpList="ftpList" @addMemberDetail="newMember"></add-member>
    <add-department ref="showAddDepartment" @addNewDept="newDeptMsg"></add-department>
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
                //部门层级结构数据
                departmentLevel:[],
                //部门树结构父子传递字段
                defaultProps: {
                    children: 'children',
                    label: 'label'
                },
                //分页表数据
                tableData: [],
                /*添加或编辑成员的数据*/
                ftpList: [
                    {
                        label: '姓名',
                        value: ''
                    },
                    {
                        label: '用户名',
                        value: ''
                    },
                    {
                        label: '职位',
                        value: ''
                    },
                    {
                        label: '手机号',
                        value: ''
                    }
                ],
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
            _this.$emit('handleTabSelected',10);
            //初始化部门树结构数据
            Http.zsyGetHttp('/dept/tree',null,(res)=>{
                _this.departmentTree.push(res.data);
                _this.userPaging(_this.departmentTree[0].id,_this.queryForm.pageIndex);
            });
            //初始化部门层级结构数据
            Http.zsyGetHttp('/dept/level',null,(res)=>{
                _this.departmentLevel.push(res.data);
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
            editRow(index, rows) {
                // 点击编辑
                this.editStatusIndex = index;
                this.ftpList[0].value = rows[index].name;
                this.ftpList[1].value = rows[index].username;
                this.ftpList[2].value = rows[index].job;
                this.ftpList[3].value = rows[index].tel;
                this.nowCreateDate = rows[index].createDate;
                this.newLastLogin = rows[index].lastLogin;
                // console.log(this.ftpList);
                this.$refs.showAddMember.show();
            },
            AddMemberShow () {
                // 添加数据
                for (var i = 0;i < this.ftpList.length; i++){
                    this.ftpList[i].value = "";
                }
                this.editStatusIndex = '';
                this.$refs.showAddMember.show();
            },
            newMember (val) {
                // 新增成员
                // 获取时间
                var now = new Date();
                var month = now.getMonth()+1;
                var day = now.getDate();
                if (month < 10){ month = '0'+month;}
                if (day < 10){ day = '0'+day;}
                var currentDate = now.getFullYear()+'-'+month+'-'+day;

                var newMember = {};
                newMember.name = val[0].value;
                newMember.username = val[1].value;
                newMember.job = val[2].value;
                newMember.tel = val[3].value;

                if (this.editStatusIndex === '') {
                    newMember.createDate = currentDate;
                    newMember.lastLogin = '';
                    this.tableData.push(newMember);
                } else {
                    newMember.createDate = this.nowCreateDate;
                    newMember.lastLogin = this.newLastLogin;
                    this.tableData.splice(this.editStatusIndex,1,newMember);
                    // this.tableData[this.editStatusIndex] = newMember;
                    console.log(this.tableData[this.editStatusIndex]);
                }
            },
            addDepart () {
                // 添加部门
                this.$refs.showAddDepartment.show();
            },
            newDeptMsg (val) {
                // 部门结构数据
                if (val.addFatherDept === ""){
                    var dptree = {};
                    dptree.label = val.addNewDept;
                    this.departmentTree[0].children.push(dptree);
                }
            },
        },
        components: {
            AddMember: AddMember,
            AddDepartment: AddDepartment
        }
    }
</script>
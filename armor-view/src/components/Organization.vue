<template>
  <div class="organization-con clearfix">
    <div class="department-structure fl">
      <div class="ds-title">部门结构<span class="add-department" @click="addDepart">+</span></div>
      <el-tree :data="departmentTree" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
    </div>
    <div class="department-member fl">
      <div class="dm-title">成员<span class="add-department" @click="AddMemberShow">+</span></div>
      <div class="department-member-table">
        <el-table :data="tableData" stripe style="width: 100%">
          <el-table-column prop="name" label="姓名" width="100" align="center"></el-table-column>
          <el-table-column prop="username" label="用户名" width="100" align="center"></el-table-column>
          <el-table-column prop="job" label="职位" width="100" align="center"></el-table-column>
          <el-table-column prop="tel" label="手机号" width="116" align="center"></el-table-column>
          <el-table-column prop="createDate" label="创建日期" width="106" align="center"></el-table-column>
          <el-table-column prop="lastLogin" label="最后登录" width="106" align="center"></el-table-column>
          <el-table-column prop="operate" label="操作" align="center">
            <template scope="scope">
              <!-- <el-button type="text" size="small" @click.native.prevent="authorityOpt(scope.$index, tableData)">权限</el-button> -->
              <el-button type="text" size="small" @click.native.prevent="editRow(scope.$index, tableData)">编辑</el-button>
              <el-button type="text" size="small" @click.native.prevent="resetRow(scope.$index, tableData)">重置密码</el-button>
              <el-button type="text" size="small" @click.native.prevent="deleteRow(scope.$index, tableData)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <add-member ref="showAddMember" :ftpList="ftpList" @addMemberDetail="newMember"></add-member>
    <add-department ref="showAddDepartment" @addNewDept="newDeptMsg"></add-department>
  </div>
</template>
<script>
import AddMember from './AddMember'
import AddDepartment from './AddDepartment'

  export default {
    name: 'Organization',
    data() {
      return {
        departmentTree: [
          {
            label: '信息技术中心',
            children: [
              {
                label: '产品'
              },
              {
                label: 'UI',
                children: [
                  {
                    label: '产品11'
                  },
                  {
                    label: '产品222'
                  }
                ]
              },
              {
                label: '开发'
              },
              {
                label: '测试'
              }
            ]
          }
        ],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        tableData: [
          {
            name: '小花',
            username: 'i hate you and his',
            job: '产品经理经理',
            tel: 18288888888,
            createDate: '2014-02-02',
            lastLogin: '2014-02-02'
          }, 
          {
            name: '小花1',
            username: '122',
            job: '开发',
            tel: 18288888888,
            createDate: '2014-02-02',
            lastLogin: '2014-02-02'
          },
          {
            name: '小花2',
            username: '122',
            job: '开发',
            tel: 18288888888,
            createDate: '2014-02-02',
            lastLogin: '2014-02-02'
          },
          {
            name: '小花3',
            username: '122',
            job: '开发',
            tel: 18288888888,
            createDate: '2014-02-02',
            lastLogin: '2014-02-02'
          }
        ],
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
        newLastLogin: ''
      };
    },
    methods: {
      handleNodeClick(data) {
        // 部门结构树点击
        console.log(data.label);
      },
      // authorityOpt (index, rows) {
      //   // 点击权限
      //   console.log(index);
      // },
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
      resetRow(index, rows) {
        // 重置密码
        this.$confirm('此操作将重置密码, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$message({
            type: 'success',
            message: '重置密码成功!'
          });
        }).catch(() => {
          // this.$message({
          //   type: 'info',
          //   message: '已取消重置密码'
          // });          
        });
      },
      deleteRow(index, rows) {
        // 删除
        this.$confirm('此操作将删除该条数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          rows.splice(index, 1);
          // this.$message({
          //   type: 'success',
          //   message: '删除数据成功!'
          // });
        }).catch(() => {
          // this.$message({
          //   type: 'info',
          //   message: '已取消删除数据'
          // });          
        });
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
      }
    },
    components: {
      AddMember: AddMember,
      AddDepartment: AddDepartment
    }
  }
</script>
<style scoped>
.organization-con{width: 1100px;margin: auto;}
.department-structure{width: 260px;background: #fff;padding-bottom: 30px;min-height: 500px;}
.ds-title,.dm-title{position: relative;line-height: 34px;background: #36A8FF;color: #fff;font-size: 14px;padding: 0 10px;margin-bottom: 10px;}
.add-department{position: absolute;display: block;border: 1px solid #fff;width: 22px;height: 22px;border-radius: 50%;text-align: center;line-height: 20px;right: 10px;top: 5px;font-size: 24px;cursor: pointer;}
.department-member{width: 820px;margin-left: 20px;}
.dm-title{margin-bottom: 0;}


</style>
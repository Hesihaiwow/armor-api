<template>
  <div class="organization-con clearfix">
    <div class="department-structure fl">
      <div class="ds-title">部门结构<span class="add-department">+</span></div>
      <el-tree :data="departmentTree" :props="defaultProps" @node-click="handleNodeClick"></el-tree>
    </div>
    <div class="department-member fl">
      <div class="dm-title">成员<span class="add-department">+</span></div>
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
              <el-button type="text" size="small">权限</el-button>
              <el-button type="text" size="small">编辑</el-button>
              <el-button type="text" size="small">重置密码</el-button>
              <el-button type="text" size="small">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>
<script>
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
                label: 'UI'
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
            name: '小花',
            username: '122',
            job: '开发',
            tel: 18288888888,
            createDate: '2014-02-02',
            lastLogin: '2014-02-02'
          },
          {
            name: '小花',
            username: '122',
            job: '开发',
            tel: 18288888888,
            createDate: '2014-02-02',
            lastLogin: '2014-02-02'
          },
          {
            name: '小花',
            username: '122',
            job: '开发',
            tel: 18288888888,
            createDate: '2014-02-02',
            lastLogin: '2014-02-02'
          }
        ]
      };
    },
    methods: {
      handleNodeClick(data) {
        console.log(data);
      }
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
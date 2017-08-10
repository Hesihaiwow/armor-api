<template>
<div class="nav-con"> 
  <div class="nav-top">
    <div class="logo" @click="showIndexEvent"><img src="../assets/img/site-icon.png" alt=""></div>
    <div class="el-tab-bar">
      <el-tabs v-model="activeName" @tab-click="handleClick">
        <el-tab-pane :label="item.label" :name="item.name" :key="idx" v-for="(item,idx) in tabs">
          <component :is="item.name"></component>
        </el-tab-pane>
      </el-tabs>
    </div>
    <div class="personal-name" @click="personalOpt">  小花
      <div class="personal-opt" v-show="showPerOpt">
        <div class="alter-pwd" @click.stop.prevent="alterPwd">修改密码</div>
        <div class="logout-btn" @click.stop.prevent="logoutBtn">退出登录</div>
      </div>
    </div>
  </div>
  <nav-index v-show="showIndex"></nav-index>
  <alter-password ref="alterPwdPop"></alter-password>
</div>
</template>
<script>
import Task from './Task'
import Project from './Project'
import Intergral from './Intergral'
import Organization from './Organization'
import NavIndex from './NavIndex'
import AlterPassword from './AlterPassword'



export default {
    data() {
      return {
        activeName: '',
        tabs: [
            {
              label: '任务',
              name: 'task'
            },
            {
              label: '项目',
              name: 'project'
            },
            {
              label: '积分',
              name: 'intergral'
            },
            {
              label: '组织',
              name: 'organization'
            }
        ],
        showIndex: true,
        showPerOpt: false,
        oldPwd: '',
        newPwd: '',
        sureNewPwd: '',
        showAlterPwd: false
      };
    },
    methods: {
      handleClick(tab, event) {
        // 点击tab
        console.log(tab, event);
        this.showIndex = false;
      },
      showIndexEvent () {
        // 显示首页
        this.showIndex = true;
        this.activeName = '';
      },
      personalOpt () {
        // 个人
        this.showPerOpt = true;
      },
      alterPwd () {
        // 修改密码
        this.showPerOpt = false;
        this.$refs.alterPwdPop.show();
      },
      logoutBtn () {
        // 退出登录
        this.showPerOpt = false;
      }
    },
    components: {
      Task: Task,
      Project: Project,
      Intergral: Intergral,
      Organization: Organization,
      NavIndex: NavIndex,
      AlterPassword: AlterPassword
    }
}
</script>
<style scoped>
.nav-top{position: relative;}
.logo{position: absolute;z-index: 10;width: 40px;height: 40px;left: 140px;top: 10px;cursor: pointer;}
.personal-name{position: absolute;right: 140px;width: 40px;height: 40px;background: #69C8FA;border-radius: 50%;line-height: 40px;text-align: center;top: 10px;color: #fff;cursor: pointer;}
.personal-opt{position: absolute;color: #000;background: #fff;width: 120px;box-shadow: 0 0 10px #ccc;top: 39px;right: 0;z-index: 30;padding: 16px 0;}
.personal-opt > div{cursor: pointer;line-height: 30px;}
.personal-opt > div:hover{background: #69C8FA;color: #fff;}

</style>
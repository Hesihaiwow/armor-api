<template>
<div class="nav-con" @click="hidePerOpt"> 
  <div class="nav-top-bg">
    <div class="ntb-con">
      <div class="logo" @click="showIndexEvent"><img src="../assets/img/site-icon.png" alt=""></div>
      <div class="personal-name" @click.prevent.stop="personalOpt">  小花
        <div class="personal-opt" v-show="showPerOpt">
          <div class="alter-pwd" @click.stop.prevent="alterPwd">修改密码</div>
          <div class="alter-avatar" @click.stop.prevent="alterAvatar">修改头像</div>
          <div class="logout-btn" @click.stop.prevent="logoutBtn">退出登录</div>
        </div>
      </div>
    </div>
  </div>
  <div class="nav-top">
    <div class="el-tab-bar">
      <el-tabs v-model="activeName" @tab-click="handleClick(activeName)">
        <el-tab-pane :label="item.label" :name="item.name" :key="idx" v-for="(item,idx) in tabs">
          <component :is="item.name"></component>
        </el-tab-pane>
      </el-tabs>
    </div>
    
  </div>
  <router-view></router-view>
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
      handleClick(activeName) {
        // 点击tab
        this.showIndex = false;
      },
      handleClick(path){
        /*tab切换*/
        // this.$store.commit("SET_ACTIVETAB",path);
        this.$router.push(`/topicindex/${path}`);
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
      hidePerOpt () {
        this.showPerOpt = false;
      },
      alterPwd () {
        // 修改密码
        this.showPerOpt = false;
        this.$refs.alterPwdPop.show();
      },

      logoutBtn () {
        // 退出登录
        this.showPerOpt = false;
        this.$router.push('/');
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
.nav-top-bg{position: fixed;left: 0;top: 0;right: 0;background: #fff;box-shadow: 0 0 10px #ccc;height: 61px;z-index: 100;}
.nav-top{position: relative;width: 1100px;margin: auto;}
.logo{position: absolute;z-index: 100;width: 40px;height: 40px;left: 0;top: 10px;cursor: pointer;}
.personal-name{position: absolute;right: 0;width: 40px;height: 40px;background: #69C8FA;border-radius: 50%;line-height: 40px;text-align: center;top: 10px;color: #fff;cursor: pointer;z-index: 100;}
.personal-opt{position: absolute;color: #000;background: #fff;width: 120px;box-shadow: 0 0 10px #ccc;top: 56px;right: -36px;z-index: 30;padding: 16px 0;}
.personal-opt > div{cursor: pointer;line-height: 30px;}
.personal-opt > div:hover{background: #69C8FA;color: #fff;}
.ntb-con{position: absolute;width: 1080px;height: 61px;left: 50%;margin-left: -540px;}
</style>
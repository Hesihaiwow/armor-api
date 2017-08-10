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
        <div class="alter-pwd" @click="alterPwd">修改密码</div>
        <div class="logout-btn">退出登录</div>
      </div>
      <div class="alter-pwd-pop" v-show="showAlterPwd">
        <div class="alter-top clearfix"><span class="fl">修改密码</span><span class="close-alter-pwd fr" @click="closeAlterPwd">×</span></div>
        <div class="alter-con">
            <div class="alter-list clearfix">
              <div class="alter-menu fl">旧密码</div>
              <div class="alter-con fl"><input type="password" v-model="oldPwd" class="pwd-inp"></div>
            </div>
            <div class="alter-list clearfix">
              <div class="alter-menu fl">新密码</div>
              <div class="alter-con fl"><input type="password" v-model="newPwd" class="pwd-inp"></div>
            </div>
            <div class="alter-list clearfix">
              <div class="alter-menu fl">确认新密码</div>
              <div class="alter-con fl"><input type="password" v-model="sureNewPwd" class="pwd-inp"></div>
            </div>
            <input type="button" class="save-alter-btn" value="保存">
        </div>
      </div>
    </div>
  </div>
  <nav-index v-show="showIndex"></nav-index>

</div>
</template>
<script>
import Task from './Task'
import Project from './Project'
import Intergral from './Intergral'
import Organization from './Organization'
import NavIndex from './NavIndex'



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
        console.log(tab, event);
        this.showIndex = false;
      },
      showIndexEvent () {
        this.showIndex = true;
        this.activeName = '';
      },
      personalOpt () {
        this.showPerOpt = true;
      },
      personalHide () {
        this.showPerOpt = false;
      },
      alterPwd () {
        this.showAlterPwd = true;
        this.showPerOpt = false;
      },
      closeAlterPwd () {
        this.showAlterPwd = false;

      }
    },
    components: {
      Task: Task,
      Project: Project,
      Intergral: Intergral,
      Organization: Organization,
      NavIndex: NavIndex
    },
    mounted() {    

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
.alter-pwd-pop{position: absolute;color: #000;background: #fff;box-shadow: 0 0 10px #ccc;top: 40px;right: 0;z-index: 50;padding: 0 16px 20px;width: 360px;cursor: auto;}
.alter-top{border-bottom: 1px solid #ccc;color: #666;font-size: 14px;}
.alter-top > span{display: inline-block;}
.close-alter-pwd{font-size: 26px;width: 30px;cursor: pointer;}
.alter-list{margin: 10px;}
.alter-menu{width: 100px;text-align: left;font-size: 14px;}
.pwd-inp{border: 1px solid #ccc;height: 30px;width: 220px;border-radius: 3px;text-indent: 10px;}
.save-alter-btn{width: 100px;height: 30px;background: #3DA7F5;color: #fff;border-radius: 3px;display: block;margin-left: 110px;cursor: pointer;}
</style>
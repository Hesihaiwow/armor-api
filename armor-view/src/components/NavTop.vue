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
    <div class="personal-name">小花</div>
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
      showIndex: true

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
      }
    },
    components: {
      Task: Task,
      Project: Project,
      Intergral: Intergral,
      Organization: Organization,
      NavIndex: NavIndex
    },
}
</script>
<style scoped>
.nav-top{position: relative;}
.logo{position: absolute;z-index: 10;width: 40px;height: 40px;left: 140px;top: 10px;cursor: pointer;}
.personal-name{position: absolute;right: 140px;width: 40px;height: 40px;background: #69C8FA;border-radius: 50%;line-height: 40px;text-align: center;top: 10px;color: #fff;cursor: pointer;}
</style>
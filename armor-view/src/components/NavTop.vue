<template>
<div> 
  <el-menu :default-active="defaulActiveIndex" class="el-menu-demo white" mode="horizontal" @select="handleSelect">
    <el-menu-item index="1" class="margin-left-20">任务</el-menu-item>
    <el-menu-item index="2">项目</el-menu-item>
    <el-menu-item index="3">积分</el-menu-item>
    <el-menu-item index="4">组织</el-menu-item>
  </el-menu>

</div>
</template>
<script>
  export default {
    data() {
      return {
        defaulActiveIndex: '1'
      };
    },
    methods: {
      handleSelect(key, keyPath) {
        console.log(key, keyPath);
      }
    }
  }
</script>
<style scoped>
.white {  background-color: white;}
.margin-left-20{
  margin-left: 20%;
}
.user-info {
  margin-right: 10%;
}
</style>
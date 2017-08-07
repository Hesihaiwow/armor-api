<template>
  <div class="task-list-con">
  	<div class="task-lis" v-for="list in taskItem">
      <div class="head-img"><img src="../assets/img/head.jpg" alt="" class=""></div>
      <div class="main-task-detail">
        <div class="task-name">{{list.taskName}}</div>
        <div class="task-state">
          <span class="task-end" :class="{'red': list.endMark=='-1','orange': list.endMark=='0','blue': list.endMark=='1'||taskStatus=='WaitAuditing','green': taskStatus=='TaskFinished'||taskStatus=='WaitAssess'}">{{list.taskEnd}} <span v-show="taskStatus=='TaskDoing'||taskStatus=='WaitAuditing'">截止</span><span v-show="taskStatus=='TaskFinished'||taskStatus=='WaitAssess'">已完成</span></span>
          <span class="task-time-opt">
            <i v-show="taskStatus=='TaskDoing'" class="el-icon-circle-check"></i>
            <i v-show="taskStatus=='WaitAssess'" class="el-icon-star-off"></i>
            <i v-show="taskStatus=='WaitAuditing'" class="el-icon-edit"></i>
          </span>
        </div>
      </div>
      <div class="task-data-show" v-show="taskStatus=='TaskFinished'">
        <span class="task-score">+40分</span>
        <span class="task-level first">A</span>
      </div>
      <div class="task-mark">
        <img src="../assets/img/u431.png" alt="">
        <span class="mark-msg">知心慧学</span>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'TaskFinished',
    props: {
      taskItem: {
        type: Array
      },
      taskStatus: {
        type: String
      }
    },
    data() {
      return {
        
      };
    },
    methods: {
       
    }
  }
</script>
<style scoped>
.task-lis{background: #fff;display: -webkit-flex;display: -moz-flex;display: -ms-flex;display: -o-flex;display: flex;margin-bottom: 20px;box-shadow: 0 0 10px #ccc;margin: 10px 10px 20px;}
.task-lis:hover{box-shadow: 0 0 10px #5EC4FA;}
.head-img{width: 60px;height: 60px;border-radius: 50%;overflow: hidden;margin: 16px;}
.main-task-detail{flex: 1;-webkit-flex:1;-moz-flex:1;-ms-flex:1;-o-flex:1;}
.task-mark{line-height: 90px;font-size: 18px;}
.task-mark > img,.task-mark > span{vertical-align: middle;}
.task-mark > span{margin-right: 20px;margin-left: 10px;}
.task-name{margin-top: 18px;font-size: 16px;}
.task-state{margin-top: 10px;}
.task-state> span,.task-data-show > span{display: inline-block;vertical-align: middle;}
.task-end{padding: 2px 10px;color: #fff;}
.task-end.red{background: #FF4515;}
.task-end.orange{background: #FF9900;}
.task-end.blue{background: #36A8FF;}
.task-end.green{background: #339933;}
.task-time-opt{color: #36A8FF;font-size: 20px;margin-left: 16px;cursor: pointer;}
.task-data-show{margin: 0 40px 0 20px;}
.task-score{font-size: 18px;line-height: 92px;}
.task-level{width: 44px;height: 44px;line-height: 44px;text-align: center;border-radius: 50%;color: #fff;font-size: 20px;margin-left: 16px;}
.task-level.first{background: #FF9900;}
.task-level.second{background: #99CC66;}
.task-level.third{background: #999900;}
.task-level.forth{background: #9993F1;}
</style>
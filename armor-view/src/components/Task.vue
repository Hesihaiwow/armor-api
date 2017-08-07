<template>
  <div class="task-con">
    <p class="mic-title">我参与的项目</p>
    <div class="task-item" v-for="list in TaskItem">
      <img src="../assets/img/u431.png" alt="" class="task-logo">
      <div class="task-info">
        <div class="task-name">{{list.taskName}}</div>
        <div class="task-sub-name">{{list.subTaskName}}</div>
      </div>
    </div>
    <div class="add-task-item">
      <div class="add-task-btn">＋</div>
      <div class="add-task-msg">创建新项目</div>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'Task',
    data() {
      return {
        TaskItem: [
          {
            taskName: '知心慧学',
            subTaskName: '知心慧学'
          },
          {
            taskName: '慧学本',
            subTaskName: '知心慧学'
          }
        ]
      };
    },
    methods: {
       
    }
  }
</script>
<style scoped>
.task-con{width: 1100px;margin: auto;}
.mic-title{font-size: 18px;color: #000;position: relative;}
.task-item,.add-task-item{background: #fff;display: -webkit-flex;display: -moz-flex;display: -ms-flex;display: -o-flex;display: flex;padding: 14px 16px;margin: 16px 0;cursor: pointer;}
.task-item:hover,.add-task-item:hover{box-shadow: 0 0 20px #ccc;}
.task-info{flex: 1;-webkit-flex:1;-moz-flex:1;-ms-flex:1;-o-flex:1;}
.task-name{font-size: 18px;line-height: 26px;}
.task-sub-name{font-size: 12px;color: #999999;line-height: 20px;}
.task-logo{margin-right: 20px;height: 30px;margin-top: 6px;}
</style>
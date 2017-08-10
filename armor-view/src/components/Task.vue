<template>
  <div class="task-con">
    <div class="task-top clearfix">
      <div class="task-top-list fl">
        <span class="ttl-name">项目</span>
        <el-select v-model="projectValue" placeholder="请选择">
          <el-option v-for="item in projectItem" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>
      <div class="task-top-list fl">
        <span class="ttl-name">成员</span>
        <el-select v-model="memberValue" placeholder="请选择">
          <el-option v-for="item in memberItem" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>
      <div class="task-top-list fl">
        <span class="ttl-name">阶段</span>
        <el-select v-model="stageValue" placeholder="请选择">
          <el-option v-for="item in stageItem" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>
      <div class="task-top-list fl">
        <span class="ttl-name">标签</span>
        <el-select v-model="tagValue" placeholder="请选择">
          <el-option v-for="item in tagItem" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>
      <div class="task-top-list fl">
        <span class="ttl-name">状态</span>
        <el-select v-model="statusValue" placeholder="请选择">
          <el-option v-for="item in statusItem" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>
      <div class="task-top-list fl">
        <span class="ttl-name">截止日期</span>
        <el-date-picker v-model="value1" type="date" placeholder="选择日期" :picker-options="pickerOptions0"></el-date-picker><span class="div-line">-</span>
        <el-date-picker v-model="value2" type="date" placeholder="选择日期" :picker-options="pickerOptions0"></el-date-picker>
        <img src="../assets/img/u1221.png" alt="" class="serch-btn">
      </div>
      <div class="task-top-list fl creat-task" @click="createTaskClick">
        <span class="ttl-add-icon">+</span>
        <span class="ttl-add-msg">建任务</span>
      </div>
    </div>
    
    <task-list :taskItem="taskItem" taskStatus="Task"></task-list>
    <create-task ref="createTaskPop"></create-task>







  </div>
</template>
<script>
import TaskList from './TaskList.vue'
import CreateTask from './CreateTask'

  export default {
    name: 'Task',
    data() {
      return {
        projectItem: [{
          value: '选项1',
          label: '知心慧学'
        }, {
          value: '选项2',
          label: '会学本'
        }],
        projectValue: '',

        memberItem: [{
          value: '选项1',
          label: '知心慧学'
        }, {
          value: '选项2',
          label: '会学本'
        }],
        memberValue: '',

        stageItem: [{
          value: '选项1',
          label: '知心慧学'
        }, {
          value: '选项2',
          label: '会学本'
        }],
        stageValue: '',

        tagItem: [{
          value: '选项1',
          label: '知心慧学'
        }, {
          value: '选项2',
          label: '会学本'
        }],
        tagValue: '',

        statusItem: [{
          value: '选项1',
          label: '知心慧学'
        }, {
          value: '选项2',
          label: '会学本'
        }],
        statusValue: '',
        pickerOptions0: {
          disabledDate(time) {
            return time.getTime() < Date.now() - 8.64e7;
          }
        },
        value1: '',
        value2: '',
        taskItem: [
          {
            taskName: '教师端选题组卷流程优化',
            taskEnd: '昨天',
            endMark: '-1',
            key: [
              {
                keyMsg: '教师端'
              }
            ]
          },
          {
            taskName: '散步提分宝散步提分宝散步提分宝散步提分宝',
            taskEnd: '今天 19:00',
            endMark: '0',
            key: [
              {
                keyMsg: '抽题'
              },
              {
                keyMsg: '接口'
              }
            ]
          },
          {
            taskName: '教师端教师端选题组卷流程优化选题组卷流程优化',
            taskEnd: '明天',
            endMark: '1',
            key: [
              {
                keyMsg: '题库'
              }
            ]
          },
          {
            taskName: '教师端选题教师端选题组卷流程优化教师端选题组卷流程优化组卷流程优化',
            taskEnd: '昨天',
            endMark: 1,
            key: [
              {
                keyMsg: 'CRM'
              }
            ]
          }
        ]
      };
    },
    methods: {
      createTaskClick () {
        this.$refs.createTaskPop.show();
      }
    },
    components: {
      TaskList: TaskList,
      CreateTask: CreateTask
    }
  }
</script>
<style scoped>
.task-con{width: 1100px;margin: auto;}
.task-top{position: relative;margin-bottom: 26px;}
.task-top-list{margin: 10px 20px 0 10px;}
.task-top-list:last-child{margin-top: 16px;}
.task-top-list .el-select{width: 148px;}
.ttl-name{margin-right: 10px;font-size: 14px;}
.div-line{margin-left: 10px;margin-right: 6px;}
.serch-btn{vertical-align: middle;margin-left: 10px;}
.creat-task{cursor: pointer; position: absolute;right: 0;bottom: 0;}
.creat-task > span{display: inline-block;vertical-align: middle;}
.ttl-add-msg{font-size: 16px;color: #36A8FF;margin-left: 10px;}
.ttl-add-icon{width: 24px;height: 24px;line-height: 22px;text-align: center;font-size: 24px;border-radius: 50%;color: #fff;background: #36A8FF;}
</style>
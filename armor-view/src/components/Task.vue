<template>
  <div class="task-con">
    <div class="task-top clearfix">
      <div class="task-top-list fl" v-for="slist in taskTop">
        <span class="ttl-name">{{slist.label}}</span>
        <el-select v-model="slist.selValue" placeholder="请选择">
          <el-option v-for="item in slist.ListItem" :key="item.value" :label="item.label" :value="item.value"></el-option>
        </el-select>
      </div>

      <div class="task-top-list fl">
        <span class="ttl-name">截止日期</span>
        <el-date-picker v-model="startValue" type="date" placeholder="选择日期" :picker-options="pickerOptions0"></el-date-picker><span class="div-line">-</span>
        <el-date-picker v-model="endValue" type="date" placeholder="选择日期" :picker-options="pickerOptions0"></el-date-picker>
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
        taskTop: [
          {
            label: '项目',
            selValue: '',
            ListItem:  [{
              value: '选项1',
              label: '知心慧学'
            }, {
              value: '选项2',
              label: '会学本'
            }]
          },
          {
            label: '成员',
            selValue: '',
            ListItem:  [{
              value: '选项1',
              label: '知心慧学'
            }, {
              value: '选项2',
              label: '会学本'
            }]
          },
          {
            label: '阶段',
            selValue: '',
            ListItem:  [{
              value: '选项1',
              label: '知心慧学'
            }, {
              value: '选项2',
              label: '会学本'
            }]
          },
          {
            label: '标签',
            selValue: '',
            ListItem:  [{
              value: '选项1',
              label: '知心慧学'
            }, {
              value: '选项2',
              label: '会学本'
            }]
          },
          {
            label: '状态',
            selValue: '',
            ListItem:  [{
              value: '选项1',
              label: '知心慧学'
            }, {
              value: '选项2',
              label: '会学本'
            }]
          }
        ],
        pickerOptions0: {
          disabledDate(time) {
            return time.getTime() < Date.now() - 8.64e7;
          }
        },
        startValue: '',
        endValue: '',
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
        // 点击建任务
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
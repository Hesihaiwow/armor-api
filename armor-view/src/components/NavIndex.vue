<template>
  <div class="nav-index-con">
    <div class="my-integral-con">
      <p class="mic-title">我的积分</p>
      <div class="mic-main clearfix">
        <div class="mic-item fl" v-for="item in integralItem">
          <div class="mic-item-title">{{item.label}}</div>
          <div class="mic-item-integral">{{item.score}}</div>
        </div>
      </div>
    </div>

    <div class="my-task-con">
      <div class="add-task" @click="createTaskClick">
        <span class="add-task-icon"><i class="el-icon-plus"></i></span>
        <span>建任务</span>
      </div>
      <p class="mic-title">我的任务</p>
      <div class="my-task-detail">
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane :label="item.label" :name="item.name" :key="idx" v-for="(item,idx) in tabs">
            <component @showFinishedPop="showFinishedPopHook" :is="item.name" ></component>
          </el-tab-pane>
        </el-tabs>
      </div>

      <p class="mic-title">待评价任务</p>
      <task-list :taskItem="taskAseess" @showAssessPop="showAssessPopHook"  taskStatus="WaitAssess"></task-list>
      <p class="mic-title">待审核</p>
      <task-list :taskItem="taskAudit" @showAuditPop="showAuditPopHook" taskStatus="WaitAuditing"></task-list>
    </div>
    <create-task ref="createTaskPop"></create-task>
    <finished-task-pop ref="finishedPop"></finished-task-pop>
    <assess-task-pop ref="assessPop"></assess-task-pop>
  </div>
</template>
<script>
import TaskDoing from './TaskDoing'
import TaskFinished from './TaskFinished'
import TaskList from './TaskList'
import CreateTask from './CreateTask'
import FinishedTaskPop from './FinishedTaskPop'
import AssessTaskPop from './AssessTaskPop'


  export default {
    name: 'NavIndex',
    data() {
      return {
        integralItem: [
          {
            label: '本周',
            score: '+42'
          },
          {
            label: '本月',
            score: '+422'
          },
          {
            label: '年度总积分',
            score: '9000'
          },
          {
            label: '季度积分排名',
            score: '12'
          },
          {
            label: '年度积分排名',
            score: '22'
          }
        ],
        activeName: 'taskDoing',
        tabs: [
            {
              label: '进行中',
              name: 'taskDoing'
            },
            {
              label: '已完成',
              name: 'taskFinished'
            }
        ],
        taskAseess: [
          {
            taskName: '教师端选题组卷流程优化',
            taskEnd: '昨天'
          },
          {
            taskName: '散步提分宝散步提分宝散步提分宝散步提分宝',
            taskEnd: '今天 19:00'
          },
          {
            taskName: '教师端教师端选题组卷流程优化选题组卷流程优化',
            taskEnd: '明天'
          },
          {
            taskName: '教师端选题教师端选题组卷流程优化教师端选题组卷流程优化组卷流程优化',
            taskEnd: '昨天'
          }
        ],
        taskAudit: [
          {
            taskName: '教师端选题组卷流程优化',
            taskEnd: '昨天'
          },
          {
            taskName: '散步提分宝散步提分宝散步提分宝散步提分宝',
            taskEnd: '今天 19:00'
          }
        ]
      };
    },
    methods: {
      handleClick(tab, event) {
        console.log(tab, event);
      },
      createTaskClick () {
        this.$refs.createTaskPop.show();
      },
      showAuditPopHook (val) {
        console.log('aaa');
        if (val) {
          this.$refs.createTaskPop.show();
        }
      },
      showAssessPopHook (val) {
        if (val) {
          this.$refs.assessPop.show();
        }
      },
      showFinishedPopHook (val) {
        if (val) {
          this.$refs.finishedPop.show();
        }
      }

    },
    components: {
      TaskDoing: TaskDoing,
      TaskFinished: TaskFinished,
      TaskList: TaskList,
      CreateTask: CreateTask,
      FinishedTaskPop: FinishedTaskPop,
      AssessTaskPop: AssessTaskPop
    },
  }
</script>
<style scoped>
.nav-index-con{width: 1100px;margin: auto;}
.mic-title{font-size: 18px;color: #000;position: relative;}
.mic-main{}
.mic-item{margin: 18px 12px;width: 200px;text-align: center;box-shadow: 0 0 20px #ccc;}
.mic-item-title{line-height: 40px;background: #D7D7D7;font-size: 20px;}
.mic-item-integral{font-size: 26px;color: #000;background: #fff;line-height: 30px;padding: 14px 0; word-wrap:break-word;}
.mic-item:first-child{margin-left: 0;}
.mic-item:last-child{margin-right: 0;}
.my-integral-con,.my-task-con{margin-top: 16px;position: relative;}
.my-task-detail{margin-top: 6px;}
.add-task{position: absolute;right: 20px;font-size: 16px;cursor: pointer;color: #36A8FF;z-index: 30;}
.add-task > span{display: inline-block;vertical-align: middle;}
.add-task-icon{width: 22px;height: 22px;line-height: 22px;border-radius: 50%;background: #36A8FF;color: #fff;text-align: center;font-size: 14px;cursor: pointer;}



</style>
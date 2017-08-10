<template>
	<div class="task-doing">
    <task-list :taskItem="taskItem" @showFinishedPop="showFinishedPopHook" taskStatus="TaskDoing"></task-list>
	</div>
</template>
<script>
import TaskList from './TaskList.vue'

  export default {
    name: 'TaskDoing',
    data() {
      return {
        taskItem: [
          {
            taskName: '教师端选题组卷流程优化',
            taskEnd: '昨天',
            endMark: '-1'
          },
          {
            taskName: '散步提分宝散步提分宝散步提分宝散步提分宝',
            taskEnd: '今天 19:00',
            endMark: '0'
          },
          {
            taskName: '教师端教师端选题组卷流程优化选题组卷流程优化',
            taskEnd: '明天',
            endMark: '1'
          },
          {
            taskName: '教师端选题教师端选题组卷流程优化教师端选题组卷流程优化组卷流程优化',
            taskEnd: '昨天',
            endMark: 1
          }
        ]
      };
    },
    methods: {
     showFinishedPopHook (val) {
        if (val) {
          this.$emit("showFinishedPop", true);
        }
      }
    },
    components: {
      TaskList: TaskList
    }
  }
</script>
<style scoped>

</style>
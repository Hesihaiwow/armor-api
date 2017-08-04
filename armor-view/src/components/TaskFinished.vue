<template>
	<div class="task-finished">
    <task-list :taskItem="taskItem" taskStatus="TaskFinished"></task-list>
  </div>
</template>
<script>
import TaskList from './TaskList.vue'

  export default {
    name: 'TaskFinished',
    data() {
      return {
        taskItem: [
          {
            taskName: '教师端选题组卷流程优化',
            taskEnd: '昨天'
          },
          {
            taskName: '散步提分宝散22222222步提提分宝',
            taskEnd: '今天 19:00'
          },
          {
            taskName: '教师端明天3333333333明天选题优化',
            taskEnd: '2月2号'
          },
          {
            taskName: '教师端选题卷44444444流程优化组卷流程优化',
            taskEnd: '2月2号'
          }
        ]
      };
    },
    methods: {
       
    },
    components: {
      TaskList: TaskList
    }
  }
</script>
<style scoped>

</style>
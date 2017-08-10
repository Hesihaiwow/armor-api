<template>
  <div class="assess-task-pop" v-show="showAssessTask">
    <div class="assess-task-pop-con">
      <div class="ctpc-top clearfix">
        <span class="fl">评价</span><span class="ctpc-top-close fr" @click="hide">×</span>
      </div> 
      <div class="assess-task">
        <div class="assess-task-list" v-for="(list,index) in assessList">
          <div class="assess-man-detail">
            <span class="amd-job">{{list.job}}</span>
            <span class="amd-job-time">工作量：{{list.jobTime}}工时</span>
            <span class="amd-during-time">起止日期：{{list.jobDuring}}</span>
            <span class="amd-name">{{list.name}}</span>
          </div>
          <div class="assess-level-opt">
            <span class="alo-menu">请评价：</span>
            <span class="alo-radio">
              <input type="radio" :name="'level'+index">
              <label>A</label>
            </span>
            <span class="alo-radio">
              <input type="radio" :name="'level'+index">
              <label>B</label>
            </span>
            <span class="alo-radio">
              <input type="radio" :name="'level'+index">
              <label>C</label>
            </span>
          </div>
          <textarea class="assess-msg-inp" placeholder="请描述你的评价"></textarea>
        </div>
      </div>

      <div class="atp-btns">
        <input type="button" class="ctpc-cancel" value="取消">
        <input type="button" class="ctpc-save" value="完成评价">
      </div>
    </div>
  </div>
</template>
<script>

  export default {
    name: 'FinishedTaskPop',
    data() {
      return {
        showAssessTask: false,
        value1: '',
        assessList: [
          {
            job: '产品设计',
            jobTime: 22,
            jobDuring: '8月18日-8月20日',
            name: '小花'
          },
          {
            job: 'UI设计',
            jobTime: 22,
            jobDuring: '8月18日-8月20日',
            name: '大花儿'
          },
          {
            job: 'web前端开发',
            jobTime: 22,
            jobDuring: '8月18日-8月20日',
            name: '萌小希'
          },
          {
            job: '产品设计',
            jobTime: 22,
            jobDuring: '8月18日-8月20日',
            name: '小花'
          }
        ]
      };
    },
    methods: {
      show () {
        this.showAssessTask = true;
      },
      hide () {
        this.showAssessTask = false;
      }
    },
    components: {
      
    },
  }
</script>
<style scoped>
.assess-task-pop{position: fixed;top: 0;left: 0;bottom: 0;right: 0;z-index: 100;background: rgba(0,0,0,0.5);}
.assess-task-pop-con{position: absolute;background: #F2F2F2;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 536px;padding: 16px;font-size: 14px;}
.assess-man-detail{background: #fff;border: 1px solid #ccc;padding: 2px 6px;line-height: 36px;}
.assess-man-detail > span{display: inline-block;}
.amd-job{width: 130px;}
.amd-job-time{width: 120px;}
.amd-during-time{width: 200px;}
.amd-name{width: 36px;height: 36px;text-align: center;line-height: 36px;border-radius: 50%;background: #006699;color: #fff;font-size: 10px;}
.assess-level-opt > span{display: inline-block;vertical-align: middle;line-height: 36px;}
.assess-msg-inp{border: 1px solid #ccc;padding: 10px;width: 496px;}
.alo-radio{margin-left: 10px;}
.alo-radio > input, .alo-radio > label{cursor: pointer;}
.assess-task-list{margin-top: 16px;}
.assess-task{height: 500px;overflow: auto;}


.atp-btns{text-align: right;margin-top: 10px;}
.atp-btns > input{display: inline-block;width: 80px;height: 24px;margin-left: 12px;cursor: pointer;}
.ctpc-cancel{}
.ctpc-save{background: #36A8FF;color: #fff;}







</style>
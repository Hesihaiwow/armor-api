<template>
  <div class="finished-task-pop" v-show="showFinishedTask">
    <div class="finished-task-pop-con">
      <div class="ctpc-top clearfix">
        <span class="fl">完成</span><span class="ctpc-top-close fr" @click="hide">×</span>
      </div> 
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">实际消耗</div>
        <div class="ftp-msg fl">
          <input type="text" class="actual-expend-time">
          <span class="star-warn">*</span>小时
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">实际完成时间</div>
        <div class="ftp-msg fl">
          <el-date-picker v-model="value1" type="datetime" placeholder="选择日期时间">
          </el-date-picker>
        </div>
      </div>  
      <div class="ftp-btns">
        <input type="button" class="ctpc-cancel" value="取消">
        <input type="button" class="ctpc-save" value="保存">
      </div>
    </div>
  </div>
</template>
<script>

  export default {
    name: 'FinishedTaskPop',
    data() {
      return {
        showFinishedTask: false,
        value1: ''
      };
    },
    methods: {
      show () {
        this.showFinishedTask = true;
      },
      hide () {
        this.showFinishedTask = false;
      }
    },
    components: {
      
    },
  }
</script>
<style scoped>
.finished-task-pop{position: fixed;top: 0;left: 0;bottom: 0;right: 0;z-index: 100;background: rgba(0,0,0,0.5);}
.finished-task-pop-con{position: absolute;background: #F2F2F2;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 536px;padding: 16px;height: 196px;}
.ftp-list{margin: 20px 0;font-size: 14px;}
.ftp-menus{width: 90px;text-align: right;margin-right: 16px;line-height: 30px;}
.actual-expend-time{width: 200px;height: 30px;border: 1px solid #bfcbd9;border-radius: 4px;text-indent: 6px;}
.star-warn{color: red;font-size: 18px;margin: 0 6px;}
.ftp-btns{text-align: right;}
.ftp-btns > input{display: inline-block;width: 80px;height: 24px;margin-left: 12px;cursor: pointer;}
.ctpc-cancel{}
.ctpc-save{background: #36A8FF;color: #fff;}










</style>
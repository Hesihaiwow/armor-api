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
      <div class="ctpc-btns">
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
.finished-task-pop-con{position: absolute;background: #fff;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 536px;padding: 16px;height: 196px;}
.ctpc-top{font-size: 16px;line-height: 30px;font-weight: 700;color: #333;margin-bottom: 10px;}
.ctpc-top-close{font-size: 30px;width: 30px;height: 30px;line-height: 27px;cursor: pointer;transition:0.8s ease all;text-align: center;}
.ctpc-top-close:hover{color:#36A8FF;transform:rotate(360deg);}
.ftp-list{margin: 20px 0;font-size: 14px;}
.ftp-menus{width: 90px;text-align: right;margin-right: 16px;line-height: 30px;}
.actual-expend-time{width: 200px;height: 30px;border: 1px solid #bfcbd9;border-radius: 4px;text-indent: 6px;}
.star-warn{color: red;font-size: 18px;margin: 0 6px;}

.ctpc-btns{text-align: right;}
.ctpc-btns > input{display: inline-block;width: 80px;height: 28px;margin-left: 12px;cursor: pointer;border-radius: 4px;}
.ctpc-cancel{background: #fff;border: 1px solid #ccc;}
.ctpc-cancel:hover{background: #fff;border: 1px solid #36A8FF;color: #36A8FF;font-weight: 700;}
.ctpc-save{background: #36A8FF;color: #fff;}

.ftp-msg .el-date-editor.el-input{
  width: 200px;
}





</style>
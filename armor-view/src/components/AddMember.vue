<template>
	<div class="add-member-pop" v-show="showAddPop">
    <div class="add-member-pop-con">
      <div class="ctpc-top clearfix">
        <span class="fl">添加成员</span><span class="ctpc-top-close fr" @click="hide">×</span>
      </div>
      <div class="ftp-list clearfix" v-for="(list,index) in ftpList">
        <div class="ftp-menus fl">{{list.label}}</div>
        <div class="ftp-msg fl">
          <input type="text" v-model="list.value">
        </div>
      </div>
      <div class="am-warn">{{amWarn}}</div>
      <div class="ctpc-btns">
        <input type="button" class="ctpc-cancel" value="取消" @click="hide">
        <input type="button" class="ctpc-save" value="保存" @click="saveMember">
      </div>
    </div>
	</div>
</template>
<script>
  export default {
    props: {
      ftpList: {
        type: Array
      },
    },
    data() {
      return {
        showAddPop: false,
        amWarn: ''
      };
    },
    methods: {
      show () {
        this.showAddPop = true;
      },
      hide () {
        this.showAddPop = false;
        this.amWarn = '';
      },
      saveMember () {
        // 保存
        for (var i = 0; i < this.ftpList.length; i++) {
          if (this.ftpList[i].value==""){
            this.amWarn = this.ftpList[i].label+'不能为空';
            return;
          }
        }
        this.$emit("addMemberDetail", this.ftpList);
        this.showAddPop = false;
        this.amWarn = '';
      }
    }
  }
</script>
<style scoped>
.add-member-pop{position: fixed;top: 0;left: 0;bottom: 0;right: 0;z-index: 100;background: rgba(0,0,0,0.5);}
.add-member-pop-con{position: absolute;background: #fff;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 536px;padding: 16px;padding-bottom: 30px;box-shadow: 0 0 10px #fff;}
.ctpc-top{font-size: 16px;line-height: 30px;font-weight: 700;color: #333;}
.ctpc-top-close{font-size: 30px;width: 30px;height: 30px;line-height: 22px;cursor: pointer;transition:0.8s ease all;text-align: center;}
.ctpc-top-close:hover{color:#36A8FF;transform:rotate(360deg);}
.ftp-list{margin: 20px 0;font-size: 14px;}
.ftp-menus{width: 130px;text-align: right;margin-right: 16px;line-height: 30px;}
.ftp-msg{line-height: 30px;}
.ftp-msg > input{height: 28px;width: 280px;border: 1px solid #ccc;border-radius: 3px;text-indent: 4px;box-shadow: 0 0 10px #ccc;}
.am-warn{height: 20px;line-height: 20px;text-indent: 84px;color: red;}



.ctpc-btns{text-align: right;}
.ctpc-btns > input{display: inline-block;width: 80px;height: 28px;margin-left: 12px;cursor: pointer;border-radius: 4px;}
.ctpc-cancel{background: #fff;border: 1px solid #ccc;}
.ctpc-cancel:hover{background: #fff;border: 1px solid #36A8FF;color: #36A8FF;font-weight: 700;}
.ctpc-save{background: #36A8FF;color: #fff;}



</style>
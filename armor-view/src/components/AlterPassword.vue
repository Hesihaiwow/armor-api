<template>
	<div class="add-member-pop" v-show="showAlterPwd">
    <div class="alter-pwd-pop">
      <div class="alter-top clearfix"><span class="fl">修改密码</span><span class="close-alter-pwd fr" @click="hide">×</span></div>
      <div class="alter-con">
          <div class="alter-list clearfix">
            <div class="alter-menu fl">旧密码</div>
            <div class="alter-con fl"><input type="password" v-model="oldPwd" class="pwd-inp"></div>
          </div>
          <div class="alter-list clearfix">
            <div class="alter-menu fl">新密码</div>
            <div class="alter-con fl"><input type="password" v-model="newPwd" class="pwd-inp"></div>
          </div>
          <div class="alter-list clearfix">
            <div class="alter-menu fl">确认新密码</div>
            <div class="alter-con fl"><input type="password" v-model="sureNewPwd" class="pwd-inp"></div>
          </div>
          <input type="button" class="save-alter-btn" value="保存">
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
        showAlterPwd: false,
        amWarn: '',
        oldPwd: '',
        newPwd: '',
        sureNewPwd: ''
      };
    },
    methods: {
      show () {
        this.showAlterPwd = true;
      },
      hide () {
        this.showAlterPwd = false;
        this.amWarn = '';
      },
      saveMember () {
        if (this.newDept=='') {
          this.amWarn = '新加部门不能为空';
          return;
        }
        var newDept = {};
        newDept.addNewDept = this.newDept;
        newDept.addFatherDept = this.fatherDept;
        this.$emit("addNewDept", newDept);
        this.showAlterPwd = false;
        this.amWarn = '';
      }
    }
  }
</script>
<style scoped>
.add-member-pop{position: fixed;top: 0;left: 0;bottom: 0;right: 0;z-index: 100;background: rgba(0,0,0,0.5);}
.alter-pwd-pop{position: absolute;left: 50%;top: 50%;color: #000;background: #fff;transform: translate(-50%,-50%);z-index: 50;padding: 0 16px 20px;width: 460px;cursor: auto;}
.alter-top{border-bottom: 1px solid #ccc;color: #666;font-size: 14px;padding: 10px 0 6px;}
.alter-top > span{display: inline-block;line-height: 36px;}
.close-alter-pwd{font-size: 26px;width: 30px;height: 30px;line-height: 26px !important;cursor: pointer;text-align: center;transition:0.8s ease all;}
.close-alter-pwd:hover{color:#36A8FF;}
.alter-list{margin: 20px 30px;}
.alter-menu{width: 120px;text-align: left;font-size: 14px;}
.pwd-inp{border: 1px solid #ccc;height: 30px;width: 220px;border-radius: 3px;text-indent: 10px;}
.save-alter-btn{width: 100px;height: 30px;background: #3DA7F5;color: #fff;border-radius: 3px;display: block;margin-left: 150px;cursor: pointer;}

</style>
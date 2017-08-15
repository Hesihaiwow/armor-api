<template>
	<div class="add-member-pop" v-show="showAlterPwd">
    <div class="alter-pwd-pop">
      <div class="alter-top clearfix"><span class="fl">修改密码</span><span class="close-alter-pwd fr" @click="hide">×</span></div>
      <div class="alter-con">
          <div class="alter-list clearfix">
            <div class="alter-menu fl">旧密码</div>
            <div class="alter-con fl">
                <!--<input type="password" v-model="oldPwd" class="pwd-inp">-->
                <el-input type="text" v-model="modifyForm.originalPassword" placeholder="请输入旧密码"></el-input>
            </div>
          </div>
          <div class="alter-list clearfix">
            <div class="alter-menu fl">新密码</div>
            <div class="alter-con fl">
                <!--<input type="password" v-model="newPwd" class="pwd-inp">-->
                <el-input type="password" v-model="modifyForm.newPassword" placeholder="请输入新密码"></el-input>
            </div>
          </div>
          <div class="alter-list clearfix">
            <div class="alter-menu fl">确认新密码</div>
            <div class="alter-con fl">
                <!--<input type="password" v-model="sureNewPwd" class="pwd-inp">-->
                <el-input type="password" v-model="modifyForm.sureNewPwd" placeholder="请再次输入新密码"></el-input>
            </div>
          </div>
          <!--<input type="button" class="save-alter-btn" @click="handleModifyPwd" value="保存">-->
          <el-button type="primary" size="small" class="save-alter-btn" :loading="button.loading" @click="handleModifyPwd">{{button.btnName}}</el-button>
      </div>
    </div>
	</div>
</template>
<script>
    import Helper from '../lib/Helper'
    import Http from '../lib/Http'
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
          modifyForm:{
              originalPassword: '',
              newPassword: '',
              sureNewPwd: ''
          },
          button:{
              loading:false,
              btnName:'保存'
          }
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
      //修改用户密码
      handleModifyPwd(){
          let _this = this;
          let oldPwd = Helper.trim(this.modifyForm.originalPassword);
          let newPwd = Helper.trim(this.modifyForm.newPassword);
          let sureNewPwd = Helper.trim(this.modifyForm.sureNewPwd);
          if (oldPwd==''){
              this.$message.warning("请输入旧密码");
              return;
          }
          if (newPwd==''){
              this.$message.warning("请输入新密码");
              return;
          }
          if (sureNewPwd==''){
              this.$message.warning("请再次输入新密码");
              return;
          }
          if (newPwd!=sureNewPwd){
              this.$message.warning("两次输入的新密码不一致");
              return;
          }
          this.button.loading = true;
          this.button.btnName = '保存中';
          Http.zsyPutHttp('/user/password',this.modifyForm,(res)=> {
              this.$message.success("修改成功");
              setTimeout(function () {
                  _this.$router.push("/");
              },1000)
          },(fail)=>{
              this.$message.error(fail.errMsg);
              this.button.loading = false;
              this.button.btnName = '保存';
          },(err)=>{
              this.button.loading = false;
              this.button.btnName = '保存';
          });
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
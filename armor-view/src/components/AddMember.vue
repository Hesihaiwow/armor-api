<template>
	<div class="add-member-pop" v-show="showAddPop">
    <div class="add-member-pop-con">
      <div class="ctpc-top clearfix">
        <span class="fl">添加成员</span><span class="ctpc-top-close fr" @click="hide">×</span>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">姓名</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="addForm.name" placeholder="请输入姓名"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">账号</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="addForm.account" placeholder="请输入账号"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">职位</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="addForm.jobName" placeholder="请输入职位"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">手机号</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="addForm.phone" placeholder="请输入手机号"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">邮箱地址</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="addForm.email" placeholder="请输入邮箱地址"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">用户权限</div>
        <div class="ftp-msg fl">
          <el-select class="w280" v-model="addForm.userRole" placeholder="请选择权限">
            <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
      <!--<div class="am-warn">{{amWarn}}</div>-->
      <div class="ctpc-btns">
        <input type="button" class="ctpc-cancel" value="取消" @click="hide">
        <input type="button" class="ctpc-save" value="保存" @click="handleAddUserClick">
      </div>
    </div>
	</div>
</template>
<script>
  import Http from '../lib/Http'
  import Helper from '../lib/Helper'

  export default {
    data() {
      return {
        //添加用户表单
        addForm:{
          name:'',
          account:'',
          jobName:'',
          phone:'',
          userRole:'',
          departmentId:'',
          email:''
        },
        //用户权限手
        options: [{
              value: 0,
              label: '超级管理员'
          }, {
              value: 1,
              label: '项目管理者'
          }, {
              value: 2,
              label: '普通成员'
        }],
        showAddPop: false
      };
    },
    methods: {
      //显示弹框
      show () {
        this.showAddPop = true;
      },
      //隐藏弹框
      hide () {
        this.showAddPop = false;
        this.addForm.name='';
        this.addForm.account='';
        this.addForm.jobName='';
        this.addForm.phone='';
        this.addForm.userRole='';
        this.addForm.email='';
      },
      //部门ID
      setDeptId(deptId){
          this.addForm.departmentId=deptId;
      },
      //添加用户
      handleAddUserClick () {
        if (Helper.trim(this.addForm.name)==''){
            this.warnMsg("请填写用户名称");
            return;
        }
        if (Helper.trim(this.addForm.account)==''){
              this.warnMsg("请填写用户账号");
              return;
        }
        if (Helper.trim(this.addForm.jobName)==''){
              this.warnMsg("请填写用户职位");
              return;
        }
        if (Helper.trim(this.addForm.phone)==''){
              this.warnMsg("请填写用户手机号");
              return;
        }
        if (Helper.trim(this.addForm.userRole)==''){
              this.warnMsg("请选择用户权限");
            return;
        }
        Http.zsyPostHttp('/user/add',this.addForm,(res)=>{
            this.hide();
            this.$message({
                showClose: true,
                message: '用户添加成功',
                type: 'success'
            });
            this.$emit("handleUserDataRefresh");
        });
      },
      warnMsg(msg) {
          this.$message({
              showClose: true,
              message: msg,
              type: 'warning'
          });
      },
    }
  }
</script>
<style scoped>
.add-member-pop{position: fixed;top: 0;left: 0;bottom: 0;right: 0;z-index: 100;background: rgba(0,0,0,0.5);}
.add-member-pop-con{position: absolute;background: #fff;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 536px;padding: 16px;padding-bottom: 30px;}
.ctpc-top{font-size: 16px;line-height: 30px;font-weight: 700;color: #333;}
.ctpc-top-close{font-size: 30px;width: 30px;height: 30px;line-height: 27px;cursor: pointer;transition:0.8s ease all;text-align: center;}
.ctpc-top-close:hover{color:#36A8FF;transform:rotate(360deg);}
.ftp-list{margin: 20px 0;font-size: 14px;}
.ftp-menus{width: 130px;text-align: right;margin-right: 16px;line-height: 30px;}
.ftp-msg{line-height: 30px;}
.ftp-msg > input{height: 28px;width: 280px;border: 1px solid #ccc;border-radius: 3px;text-indent: 4px;}
.am-warn{height: 20px;line-height: 20px;text-indent: 84px;color: red;}



.ctpc-btns{text-align: right;}
.ctpc-btns > input{display: inline-block;width: 80px;height: 28px;margin-left: 12px;cursor: pointer;border-radius: 4px;}
.ctpc-cancel{background: #fff;border: 1px solid #ccc;}
.ctpc-cancel:hover{background: #fff;border: 1px solid #36A8FF;color: #36A8FF;font-weight: 700;}
.ctpc-save{background: #36A8FF;color: #fff;}



</style>
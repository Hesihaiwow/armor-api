<template>
  <div class="add-member-pop" v-show="showAddPop">
    <div class="alter-pwd-pop">
      <div class="alter-top clearfix"><span class="fl">修改信息</span><span class="close-alter-pwd fr" @click="hide">×</span></div>
      <el-form :model="alterForm" ref="alterForm" :rules="rules" label-width="110px">
        <el-form-item label="姓名" prop="name">
          <el-input type="text" v-model="alterForm.name"></el-input>
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input type="text" v-model="alterForm.phone"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input type="text" v-model="alterForm.email"></el-input>
        </el-form-item>
        <div class="el-dialog__footer" style=" margin-top: 30px;margin-right: 20px;">
          <el-button @click="handleAddUserClick" type="primary">保存</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>
<script>
    import Helper from '../lib/Helper'
    import Http from '../lib/Http'
    export default {
        data() {
            return {
                name:'',
                phone:'',
                email:'',
                //修改用户表单
                alterForm:{
                    name:'',
                    phone:'',
                    email:''
                },
                showAddPop: false,
                rules:{
                    name: [
                        // {required: true, message: '姓名不能为空', trigger: 'blur'},
                        {min: 1, max: 10, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                    ],
                    phone: [
                        // {required: true, message: '手机号不能为空', trigger: 'blur'},
                        {pattern: /^1[345789]\d{9}$/, message: '目前只支持中国大陆的手机号码' }
                    ],
                    email: [
                        // {required: true, message: '邮箱不能为空', trigger: 'blur'},
                        {pattern:/^\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}$/,message:'邮箱格式有误'}
                    ],
                }

            };
        },
        methods: {
            //显示弹框
            show () {
                this.showAddPop = true;
                this.fetchMyProfile();
            },
            //隐藏弹框
            hide () {
                this.showAddPop = false;
            },
            //添加用户
            handleAddUserClick () {
                if (Helper.trim(this.alterForm.name)==''){
                    this.warnMsg("请填写用户名称");
                    return;
                }
                if (Helper.trim(this.alterForm.phone)==''){
                    this.warnMsg("请填写用户手机号");
                    return;
                }
                if (Helper.trim(this.alterForm.email)==''){
                    this.warnMsg("请填写用户邮箱");
                    return;
                }
                Http.zsyPutHttp('/user/modify/info',this.alterForm,(res)=>{
                    this.hide();
                    this.$message({
                        showClose: true,
                        message: '用户信息修改成功',
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
            fetchMyProfile(){
                // 获取我的个人信息
                Http.zsyGetHttp('/user/myProfile',{},(res)=>{
                    if (res.data) {
                        this.alterForm.name = res.data.name;
                        this.alterForm.phone = res.data.phone;
                        this.alterForm.email = res.data.email;
                    }
                })
            },
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

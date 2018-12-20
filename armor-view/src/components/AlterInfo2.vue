<template>
  <div class="add-member-pop" v-show="showAddPop">
    <div class="add-member-pop-con">
      <div class="ctpc-top clearfix">
        <span class="fl">修改信息</span><span class="ctpc-top-close fr" @click="hide">×</span>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">姓名</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="addForm.name" placeholder="请输入姓名"></el-input>
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
      <div class="ctpc-btns">
        <input type="button" class="ctpc-cancel" value="取消" @click="hide">
        <input type="button" class="ctpc-save" value="保存" @click="handleAddUserClick">
      </div>
    </div>
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
      <div class="ctpc-btns">
        <input type="button" class="ctpc-cancel" value="取消" @click="hide">
        <input type="button" class="ctpc-save" value="保存" @click="handleAddUserClick">
      </div>
    </el-form>
  </div>
</template>
<script>
    import Http from '../lib/Http'
    import Helper from '../lib/Helper'

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
                        {required: true, message: '姓名不能为空', trigger: 'blur'},
                        {min: 1, max: 10, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                    ],
                    phone: [
                        {required: true, message: '手机号不能为空', trigger: 'blur'},
                        {pattern: /^1[34578]\d{9}$/, message: '目前只支持中国大陆的手机号码' }
                    ],
                    email: [
                        {required: true, message: '邮箱不能为空', trigger: 'blur'},
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
                this.alterForm.name='';
                this.alterForm.phone='';
                this.alterForm.email='';
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
                Http.zsyPostHttp('/user/modify/info',this.alterForm,(res)=>{
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

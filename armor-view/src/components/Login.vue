<template>
    <div class="login">
        <!-- <div class="login-title">用户积分管理系统</div> -->
        <el-form  label-position="left" label-width="0px" class="demo-ruleForm login-container">
            <h1 class="title">系统登录 <em> SYSTEM LOGIN</em></h1>
            <div class="form-items">
                <el-form-item prop="account">
                    <el-input type="text" v-model="loginForm.account" auto-complete="off" placeholder="账号" class="form-input"></el-input>
                </el-form-item>
                <el-form-item prop="checkPass">
                    <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码" class="form-input"></el-input>
                </el-form-item>
                <!--<el-checkbox  checked class="remember">记住密码</el-checkbox>-->
                <el-form-item style="width:100%;">
                    <el-button type="primary" style="width:100%;" @click="login" :loading="button.loading" class="form-input">登录</el-button>
                </el-form-item>
            </div>
        </el-form>
    </div>
</template>
<script>
    import { Message } from 'element-ui';
    import Helper from '../lib/Helper'
    import Http from "../lib/Http";

    export default {
        data() {
            return {
                loginForm:{
                    account:'',
                    password:''
                },
                button:{
                    loading:false
                }
            };
        },
        methods: {
            login () {
                let _this = this;
                if (Helper.trim(_this.loginForm.account) == ''){
                    Message.warning('请输入用户名');
                    return;
                }
                if (Helper.trim(_this.loginForm.password) == ''){
                    Message.warning('请输入密码');
                    return;
                }
                _this.button.loading = true;
                Http.zsyPostHttp(Http.API_URI.LOGIN, _this.loginForm,(res)=>{
                    if (res.errCode!='00'){
                        _this.button.loading = false;
                        Message.error(res.errMsg);
                    }else{
                        _this.button.btnName = '登录成功,跳转中...';
                        window.localStorage.setItem("token", res.data);
                        _this.$router.push('/index/navIndex');
                    }
                },(res)=>{
                    _this.button.loading = false;
                    Message.error(res.errMsg);
                },(e)=>{
                    _this.button.loading = false;
                });
            },
        },
        created(){
            if (window.localStorage.getItem("token")!=null &&  window.localStorage.getItem("token")!== '') {
               console.log(window.localStorage.getItem("token"))
                 this.$router.push('/index/navIndex');
            }
        },
        mounted() {

          },
    }
</script>
<style  scoped>
    .login{
        height: 100%;
        background: url(../assets/img/login_bg.png) no-repeat;
        -webkit-background-size: 100% 100%;
        background-size: 100% 100%;
    }
    /*  .login-title{
       padding-left: 40px;
       height: 70px;
       line-height: 70px;
       font-size: 22px;
       background-color: #fff;
       background-color: rgba(255,255,255,.9);
       border-bottom:5px solid #20a0ff;
       color: #20a0ff;
       font-family: "黑体"
        }  */
    .login-container {
        position: absolute;
        right: 12%;
        top:50%;
        transform: translateY(-50%);
        box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);
        /*-webkit-border-radius: 5px;*/
        /*border-radius: 5px;*/
        /*-moz-border-radius: 5px;*/
        background-clip: padding-box;
        /*margin: 200px auto;*/
        width: 385px;
        /*padding: 0 35px 10px 35px;*/
        background: rgba(255,255,255,.6);
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6;
        box-sizing:border-box;

    }
    .login-container .title {/*  margin: 0px auto 40px auto;*/ font-family: "黑体";padding:0 20px; font-size: 22px;line-height:58px;  text-align: left;color: #000; border-bottom: 1px solid #a0a0a0; }
    .login-container .title em{font-size: 14px;}
    .login-container .remember {  margin: 0px 0px 35px 0px;  }
    .form-items{ padding:42px;border-top:1px solid #fff;}
    .form-input{height: 56px;}


</style>
<style>
    .form-input .el-input__inner{height: 56px!important;}
    input:-webkit-autofill {  
        -webkit-box-shadow: 0 0 0px 1000px white inset !important;  
    } 
    input{outline: none;}
</style>
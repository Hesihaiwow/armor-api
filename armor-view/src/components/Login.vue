<template>
    <el-form  label-position="left" label-width="0px" class="demo-ruleForm login-container">
        <h1 class="title">系统登录</h1>
        <el-form-item prop="account">
            <el-input type="text" v-model="loginForm.account" auto-complete="off" placeholder="账号"></el-input>
        </el-form-item>
        <el-form-item prop="checkPass">
            <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码"></el-input>
        </el-form-item>
        <!--<el-checkbox  checked class="remember">记住密码</el-checkbox>-->
        <el-form-item style="width:100%;">
            <el-button type="primary" style="width:100%;" @click.native.prevent="login" :loading="button.loading">登录</el-button>
        </el-form-item>
    </el-form>
</template>
<style  scoped>
    .login-container {
        box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);
        -webkit-border-radius: 5px;
        border-radius: 5px;
        -moz-border-radius: 5px;
        background-clip: padding-box;
        margin: 200px auto;
        width: 350px;
        padding: 35px 35px 15px 35px;
        background: #fff;
        border: 1px solid #eaeaea;
        box-shadow: 0 0 25px #cac6c6
    }
    .login-container .title {  margin: 0px auto 40px auto;font-size: 20px;  text-align: center;  color: #505458;  }
    .login-container .remember {  margin: 0px 0px 35px 0px;  }

</style>
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
            }
        }
    }
</script>
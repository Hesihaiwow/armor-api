<template>
    <div class="login">
        <!-- <div class="login-title">用户积分管理系统</div> -->
        <el-form  label-position="left" label-width="0px" class="demo-ruleForm login-container" v-show="loginShow">
            <h1 class="title">机构登录 <em> ORANGANIZATION LOGIN</em></h1>
            <div class="form-items">
                <el-form-item prop="account">
                    <el-input type="text" v-model="loginForm.account" auto-complete="off" placeholder="账号" class="form-input"></el-input>
                </el-form-item>
                <el-form-item prop="checkPass">
                    <el-input type="password" v-model="loginForm.password" auto-complete="off" placeholder="密码" @keyup.enter.native="login" class="form-input"></el-input>
                </el-form-item>
                <!--<el-checkbox  checked class="remember">记住密码</el-checkbox>-->
                <el-form-item style="width:100%;">
                    <el-button type="primary" style="width:100%;" @click="login" :loading="button.loading" class="form-input">登录</el-button>
                </el-form-item>
                <!--<el-form-item style="width:100%;height: 50%">-->
                    <!--<el-button type="text" style="width:100%;height: 50%" @click="registerShow=true,loginShow=false" class="form-input">没有账号？请注册</el-button>-->
                <!--</el-form-item>-->
                <el-form-item style="width:30%;height: 10%;float:right">
                    <el-button type="text" style="width:100%;height: 50%;" size="mini" @click="jump" class="form-input">系统登录</el-button>
                </el-form-item>
            </div>
        </el-form>
        <el-form  label-position="left" label-width="0px" class="demo-ruleForm login-container" :rules="rules" ref="userForm" :model="userForm" v-show="registerShow">
            <h1 class="title">用户注册 <em> USER REGISTER</em></h1>
            <div class="form-items">
                <el-form-item prop="account">
                    <el-input type="text" v-model="userForm.account" auto-complete="off" placeholder="账号" class="form-input"></el-input>
                </el-form-item>
                <el-form-item prop="name">
                    <el-input type="text" v-model="userForm.name" auto-complete="off" placeholder="真实姓名" class="form-input"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" v-model="userForm.password" auto-complete="off" placeholder="密码" class="form-input"></el-input>
                </el-form-item>
                <el-form-item prop="jobName">
                    <el-input type="text" v-model="userForm.jobName" auto-complete="off" placeholder="职位" class="form-input"></el-input>
                </el-form-item>
                <el-form-item prop="phone">
                    <el-input type="text" v-model="userForm.phone" auto-complete="off" placeholder="手机" class="form-input"></el-input>
                </el-form-item>
                <el-form-item prop="email">
                    <el-input type="text" v-model="userForm.email" auto-complete="off" placeholder="邮箱" class="form-input"></el-input>
                </el-form-item>
                <el-form-item style="width:100%;">
                    <el-button type="primary" style="width:100%;" @click="register('userForm')" :loading="button.loading" class="form-input">注册</el-button>
                </el-form-item>
                <el-form-item style="width:100%;height: 50%">
                    <el-button type="text" style="width:100%;height: 50%" @click="registerShow=false,loginShow = true" class="form-input">返回登录</el-button>
                </el-form-item>
            </div>
        </el-form>
    </div>
</template>
<script>
    import Helper from '../lib/Helper'
    import Http from "../lib/Http";
    import ElButton from "../../node_modules/element-ui/packages/button/src/button";
    import ElInput from "../../node_modules/element-ui/packages/input/src/input";

    export default {
        components: {
            ElInput,
            ElButton},
        data() {
            var validateEmpty = (rule, value, callback) => {
                if (value.trim() == '') {
                    callback(new Error());
                } else {
                    callback();
                }
            };
            return {
                registerShow:false,
                loginShow:true,
                loginForm:{
                    account:'',
                    password:''
                },
                button:{
                    loading:false
                },
                userForm:{
                    account:'',
                    name:'',
                    password:'',
                    jobName:'',
                    phone:'',
                    email:'',
                    userRole:Math.floor(Math.random()*10)
                },
                loginForm:{
                    account:'',
                    password:''
                },
                button:{
                    loading:false
                },
                rules: {
                    account: [{required: true, message: '账号不能为空且长度在5~16之间(不支持中文)', trigger: 'change',min: 5, max: 16, validator: validateEmpty}],
                    name: [{required: true, message: '真实姓名不能为空且长度小于20', trigger: 'change',min: 1, max: 20,validator: validateEmpty}],
                    password: [{ required: true, message: '密码长度在6~16之间', trigger: 'change', min: 6, max: 16,validator: validateEmpty}],
                    jobName: [{ required: true,message: '职位不能超过10字', trigger: 'change', min: 1, max: 10,validator: validateEmpty}],
                    phone: [{ required: true,message: '手机格式错误', trigger: 'change', pattern:/0?(13|14|15|18)[0-9]{9}/ }],
                    email: [{ required: true,message: '邮箱格式错误', trigger: 'change', pattern:/\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/ }],
                }
            };
        },
        methods: {
            jump () {
              this.$router.push("/")
            },
            login () {
                let _this = this;
                if (Helper.trim(_this.loginForm.account) == ''){
                    this.$message({
                        showClose: true,
                        message: '请输入用户名',
                        type: 'warning'
                    });
                    return;
                }
                if (Helper.trim(_this.loginForm.password) == ''){
                    this.$message({
                        showClose: true,
                        message: '请输入密码',
                        type: 'warning'
                    });
                    return;
                }
                _this.button.loading = true;
                Http.zsyPostHttp("/user/admin/login", _this.loginForm,(res)=>{
                    if (res.errCode!='00'){
                        _this.button.loading = false;
                        this.$message({
                            showClose: true,
                            message: res.errMsg,
                            type: 'error'
                        });

                    }else{
                        _this.button.btnName = '登录成功,跳转中...';
                        window.localStorage.setItem("token", res.data);
                        _this.$router.push('/indexAdmin/navIndexAdmin');
                    }
                },(res)=>{
                    _this.button.loading = false;
                    this.$message({
                        showClose: true,
                        message: res.errMsg,
                        type: 'error'
                    });
                },(e)=>{
                    _this.button.loading = false;
                });
            },
            register (userForm) {
                let _this = this;
                _this.button.loading = true;
                this.$refs[userForm].validate((valid) => {
                    if (valid) {
                        Http.zsyPostHttp('/user/register', _this.userForm,(res)=>{
                            _this.loginForm.account =_this.userForm.account ;
                            _this.loginForm.password =_this.userForm.password ;
                            Http.zsyPostHttp(Http.API_URI.LOGIN, _this.loginForm,(res)=>{
                                if (res.errCode!='00'){
                                    _this.button.loading = false;
                                    this.$message({
                                        showClose: true,
                                        message: res.errMsg,
                                        type: 'error'
                                    });
                                }else{
                                    _this.button.btnName = '登录成功,跳转中...';
                                    window.localStorage.setItem("token", res.data);
                                    _this.$router.push('/index/navIndex');
                                }
                            });
                        },(res)=>{
                            _this.button.loading = false;
                            this.$message({
                                showClose: true,
                                message: res.errMsg,
                                type: 'error'
                            });
                        },(e)=>{
                            _this.button.loading = false;
                        });
                    }else{
                        _this.button.loading = false;
                        return
                    }
                });

            },
        },
        created(){
            if (window.localStorage.getItem("token")!=null &&  window.localStorage.getItem("token")!== '') {
                 this.$router.push('/indexAmin/navIndexAdmin');
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
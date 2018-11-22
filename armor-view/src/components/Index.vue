<template>
    <div class="nav-con" @click="hidePerOpt">
        <div class="nav-top-bg">
            <div class="ntb-con">
                <div class="logo"><img src="../assets/img/site-icon.png" alt=""></div>
                <div class="personal-name" @click.prevent.stop="personalOpt">
                    <img v-if="avatarUrl!=''" :src="avatarUrl" alt="" class="personal-headimg">
                    <span v-else>{{getUserName}}</span>
                    <div class="personal-opt" v-show="showPerOpt">
                        <div class="alter-pwd" @click.stop.prevent="alterPwd">修改密码</div>
                        <div class="alter-avatar" @click.stop.prevent="alterAvatar">修改头像</div>
                        <div class="logout-btn" @click.stop.prevent="handleLogout">退出登录</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="nav-top">
            <div class="el-tab-bar">
                <el-tabs v-model="activeName" @tab-click="handleClick(activeName)">
                    <el-tab-pane :label="item.label" :name="item.name" :key="idx" v-for="(item,idx) in tabs">
                        <!-- <component :is="item.name"></component> -->
                    </el-tab-pane>
                </el-tabs>
            </div>
        </div>
        <router-view></router-view>
        <!-- <nav-index v-show="showIndex"></nav-index> -->
        <alter-password ref="alterPwdPop"></alter-password>
        <!-- 上传头像 -->
        <upload-avatar ref="uploadAvatar"></upload-avatar>
        <el-dialog title="邮箱验证"
                   :visible.sync="emailVisible" :show-close=false :close-on-press-escape=false :close-on-click-modal=false
                   size="tiny">
            <el-input style="width: 100%" placeholder="请输入邮箱获取的验证码" v-model="emailCode"></el-input>
            <el-button type="warning" style="margin-top: 10px" @click="sendEmail">没有收到邮件？点击重新发送</el-button>
            <el-button type="primary" style="margin-left: 200px"   @click="vaidateEmail">确定</el-button>
        </el-dialog>
        <el-dialog title="选择部门"
                   :visible.sync="deptVisible" :show-close=false :close-on-press-escape=false :close-on-click-modal=false
                   size="tiny">
            <el-tree :data="deptOptions" :props="dept" ref="tree"  @node-click="deptChoose"></el-tree>
            <div style="margin-bottom: 10px;margin-top: 20px">
                <el-button type="warning" style="margin-left: 25px;" @click="addDeptVisible=true,deptName=''">新建组织</el-button>
                <el-button type="primary" style="margin-left: 330px;" @click="deptChange">确定</el-button>
            </div>
        </el-dialog>
        <el-dialog title="新增组织"
                   :visible.sync="addDeptVisible"
                   size="tiny">
            <el-input style="width: 80%" placeholder="请输入组织名称" v-model="organization"></el-input>
            <div style="margin-bottom: 10px;margin-top: 20px">
                <el-button type="primary" style="margin-left: 330px;" @click="addDept">确定</el-button>
            </div>
        </el-dialog>
    </div>
</template>
<script>
    import Task from './Task'
    import Project from './Project'
    import Demand from './Demand3'
    import Intergral from './Intergral'
    import Organization from './Organization'
    import NavIndex from './NavIndex'
    import AlterPassword from './AlterPassword'
    import Helper from "../lib/Helper";
    import Http from "../lib/Http";
    import IntegralHistory from './IntegralHistory'
    import Stats from './Stats'
    import UploadAvatar from './UploadAvatar.vue'
    import Calculate from  './Calculate.vue'
    import Plan from './Plan.vue'

    export default {
        data() {
            return {
                activeName: '',
                tabs: [
                    {
                        label: '主页',
                        name: 'navIndex'
                    },
                    {
                        label: '任务',
                        name: 'task'
                    },
                    {
                        label: '项目',
                        name: 'project'
                    },
                    {
                        label: '统计',
                        name: 'stats'
                    },
                    {
                        label: '需求',
                        name: 'demand'
                    },
                    {
                        label: '计划',
                        name: 'plan'
                    },
                ],
                showIndex: true,
                showPerOpt: false,
                oldPwd: '',
                newPwd: '',
                sureNewPwd: '',
                showAlterPwd: false,
                avatarUrl:'',
                deptVisible:false,
                deptOptions:[],
                dept:{
                    children: 'children',
                    label: 'label',
                    id:'id'
                },
                departmentId:'',
                organization:'',
                addDeptVisible:false,
                emailVisible:false,
                emailCode:''
            };
        },
        created() {
            this.fetchMyProfile();
            this.activeName = 'navIndex';
            //this.$router.push(`/index/navIndex`);
        },
        beforeMount() {
            //监听子组件传过来的tab选中事件
            this.$root.eventBus.$on("handleTabSelected", (val) => {
                this.activeName = val;
            });
            if (Helper.decodeToken().userRole < 1) {
                this.tabs.push({
                    label: '积分',
                    name: 'intergral'
                },);
                this.tabs.push({
                    label: '计算',
                    name: 'calculate'
                });
                this.tabs.push({
                    label: '评价',
                    name: 'comments'
                });
                this.tabs.push({
                    label: '组织',
                    name: 'organization'
                });
            }
            if (Helper.decodeToken().departmentId == 0) {
                Http.zsyGetHttp(`/dept/all`,null,(res)=>{
                    this.deptOptions=res.data;
                });
                this.emailVisible=true
            }
        },
        watch: {
            activeName(curVal, oldVal) {
                if (oldVal == null) return;
            },
        },
        computed: {
            //获取用户名称
            getUserName() {
                return Helper.decodeToken().userName;
            }
        },
        methods: {
            handleClick(path) {
                this.showIndex = false;
                /*tab切换*/
                this.$router.push(`/index/${path}`);
            },
            showIndexEvent() {
                // 显示首页
                // this.showIndex = true;
                this.activeName = '';
                this.$router.push(`/index/navIndex`);
            },
            personalOpt() {
                // 个人
                this.showPerOpt = true;
            },
            hidePerOpt() {
                this.showPerOpt = false;
            },
            alterPwd() {
                // 修改密码
                this.showPerOpt = false;
                this.$refs.alterPwdPop.show();
            },
            // 退出登录
            handleLogout() {
                this.showPerOpt = false;
                window.localStorage.clear();
                this.$router.push('/');
            },
            alterAvatar() {
                // 修改头像
                this.showPerOpt = false;
                this.$refs.uploadAvatar.show();
            },
            fetchMyProfile(){
                // 获取我的个人信息
                Http.zsyGetHttp('/user/myProfile',{},(res)=>{
                    if (res.data.avatarUrl && res.data.avatarUrl!='') {
                        this.avatarUrl = res.data.avatarUrl
                    }
                })
            },
            deptChoose(data){
                this.departmentId=data.id;
            },
            addDept(){
                if(this.organization.trim()!=''&&this.organization!=null&&this.organization.length<20){
                    Http.zsyPostHttp('/dept/addOrganization?name='+this.organization,null,(res)=>{
                        this.$message({
                            showClose: true,
                            message: '创建组织成功',
                            type: 'success'
                        });
                        Http.zsyGetHttp(`/dept/all`,null,(res)=>{
                            this.deptOptions=res.data;
                        });
                        this.addDeptVisible = false
                    });

                }else{
                    this.$message({
                        showClose: true,
                        message: '组织名称不能为空且长度小于20',
                        type: 'warning'
                    });
                    return
                }
            },
            deptChange(){
                if(this.departmentId==''){
                    this.$message({
                        showClose: true,
                        message: '请选择组织',
                        type: 'warning'
                    });
                    return;
                }
                Http.zsyPutHttp('/user/modifyDept/'+this.departmentId,null,(res)=>{
                    this.$message({
                        showClose: true,
                        message: '组织修改成功',
                        type: 'success'
                    });
                    window.localStorage.setItem("token", res.data);
                    this.$router.go(0);
                })

                this.deptVisible = false
            },
            sendEmail(){
                Http.zsyGetHttp('/user/sendEmail',{},(res)=>{
                    this.$message({
                        showClose: true,
                        message: "已重新发送",
                        type: 'success'
                    });
                })
            },
            vaidateEmail(){
                if(this.emailCode!=null&&this.emailCode!=''){
                    Http.zsyGetHttp('/user/validateEmail/'+this.emailCode,{},(res)=>{
                        if (res.errCode!='00'){
                            this.$message({
                                showClose: true,
                                message: "邮箱验证失败，请检查后重试",
                                type: 'error'
                            });
                        }else{
                            this.$message({
                                showClose: true,
                                message: "邮箱验证成功",
                                type: 'success'
                            });
                            this.emailVisible = false;
                            this.deptVisible = true;
                        }
                    })
                }
            }
        },
        components: {
            Task: Task,
            Demand: Demand,
            Project: Project,
            Intergral: Intergral,
            Organization: Organization,
            Calculate:Calculate,
            NavIndex: NavIndex,
            AlterPassword: AlterPassword,
            IntegralHistory: IntegralHistory,
            Stats: Stats,
            UploadAvatar: UploadAvatar
        }
    }
</script>
<style scoped>
    .nav-top-bg {
        position: fixed;
        left: 0;
        top: 0;
        right: 0;
        background: #fff;
        box-shadow: 0 0 10px #ccc;
        height: 61px;
        z-index: 100;
    }

    .nav-top {
        position: relative;
        width: 1100px;
        margin: auto;
    }

    .logo {
        position: absolute;
        z-index: 100;
        width: 40px;
        height: 40px;
        left: 0;
        top: 10px; /*cursor: pointer;*/
    }

    .personal-name {
        position: absolute;
        right: 0;
        width: 40px;
        height: 40px;
        background: #69C8FA;
        border-radius: 50%;
        line-height: 40px;
        text-align: center;
        top: 10px;
        color: #fff;
        cursor: pointer;
        z-index: 100;
    }

    .personal-headimg{
        width: 40px;
        height: 40px;
        border-radius: 50%;
    }

    .personal-opt {
        position: absolute;
        color: #000;
        background: #fff;
        width: 120px;
        box-shadow: 0 0 10px #ccc;
        top: 56px;
        right: -36px;
        z-index: 30;
        padding: 16px 0;
    }

    .personal-opt > div {
        cursor: pointer;
        line-height: 30px;
    }

    .personal-opt > div:hover {
        background: #69C8FA;
        color: #fff;
    }

    .ntb-con {
        position: absolute;
        width: 1080px;
        height: 61px;
        left: 50%;
        margin-left: -540px;
    }
</style>

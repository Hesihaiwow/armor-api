<template>
    <div class="nav-con" @click="hidePerOpt">
        <div class="nav-top-bg">
            <div class="ntb-con">
                    <div class="logo"><img src="../assets/img/site-icon.png" alt=""></div>
                <div class="personal-name" @click.prevent.stop="personalOpt">
                    <img v-if="avatarUrl!==''" :src="avatarUrl" alt="" class="personal-headimg">
                    <span v-else>{{getUserName}}</span>
                    <div class="personal-opt" v-show="showPerOpt">
                        <div class="alter-pwd" @click.stop.prevent="alterPwd">修改密码</div>
                        <div class="alter-pwd" @click.stop.prevent="alterInfo">修改信息</div>
                        <div class="alter-pwd" @click.stop.prevent="showInfo">查看信息</div>
                        <div class="alter-avatar" @click.stop.prevent="alterAvatar">修改头像</div>
                        <div class="logout-btn" @click.stop.prevent="handleLogout">退出登录</div>
                    </div>
                </div>
            </div>
        </div>
        <div class="nav-top">
            <div class="el-tab-bar" style="width: 104px">
                <el-tabs v-model="activeName" @tab-click="handleClick(activeName)" >
                    <!--<el-tab-pane :label="item.label" :name="item.name" :key="idx" v-for="(item,idx) in tabs">
                        </el-tab-pane>-->

                    <el-tab-pane v-for="(item,idx) in tabs" :name="item.name" :key="idx">
                        <span v-if="item.name === 'notice' && unreadNoticeNum > 0" slot="label">{{item.label}}<span style="color: red">({{unreadNoticeNum}})</span></span>
                        <span v-else slot="label">{{item.label}}</span>
                    </el-tab-pane>
                </el-tabs>
            </div>
        </div>
        <router-view></router-view>
        <!-- <nav-index v-show="showIndex"></nav-index> -->
        <alter-password ref="alterPwdPop"></alter-password>
        <!--修改信息-->
        <alter-info ref="alterInfoPop"></alter-info>
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
        <el-dialog title="所有通知" :visible.sync="allShow">
            <el-table :data="noticeData" border>
                <el-table-column property="no" label="序号" align="center" width="70" type="index"></el-table-column>
                <el-table-column property="content" label="内容" align="center">
                    <template scope="scope">
                        <span v-if="scope.row.status === 0">
                            <a style="color:orangered;cursor: pointer;"@click="showTaskDetails(scope.row.taskId)">{{scope.row.content}}</a>
                        </span>
                        <span v-else><a style="color:gray;cursor: pointer;"@click="showTaskDetails(scope.row.taskId)">{{scope.row.content}}</a></span>
                    </template>
                </el-table-column>
                <el-table-column property="time" width="200" label="时间" align="center">
                    <template scope="scope">
                        <span>{{scope.row.createTime | formatTime}}</span>
                    </template>
                </el-table-column>
                <el-table-column label="状态" align="center" width="70">
                    <template scope="scope">
                        <span v-if="scope.row.status == 0">未读</span>
                        <span v-else>已读</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" width="100">
                    <template scope="scope">
                        <a style="color:#20a0ff;cursor: pointer;"
                           @click="readNotice(scope.row.nid)" v-if="scope.row.status === 0">标记已阅</a>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        @current-change="handleCurrentChange"
                        :current-page.sync="pageNum"
                        :page-size="noticePage.pageSize"
                        :layout="pageLayout()"
                        :total="noticePage.total">
                </el-pagination>
            </div>
        </el-dialog>
        <el-dialog
                title="任务详情"
                top="10%"
                :visible.sync="showTaskDetail"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                custom-class="myDialog"
                size="tiny"
                :before-close="hideTaskDetail">
            <el-form>
                <el-form-item class="task-form" label="任务名称：">{{taskDetail.name}}</el-form-item>
                <el-form-item class="task-form" style="white-space: pre-wrap" label="任务描述：">{{taskDetail.description}}</el-form-item>
                <el-form-item class="task-form" label="项目：">{{taskDetail.projectName}}</el-form-item>
                <el-form-item class="task-form" label="阶段：" style="margin-bottom: -36px;">{{taskDetail.stageName}}</el-form-item>
                <el-form-item class="task-form" label="优先级：" style="margin-left: 200px;"><span v-for="item in priorityList"
                                                                                               v-if="item.value == taskDetail.priority">{{item.label}}</span>
                </el-form-item>
                <el-form-item class="task-form" label="难易度："  style="margin-bottom: -36px;"><span v-for="item in facilityList"
                                                                                                  v-if="item.value == taskDetail.facility">{{item.label}}</span>
                </el-form-item>
                <el-form-item class="task-form" label="设计完成时间：" style="margin-left: 200px;">{{taskDetail.beginTime | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="开发完成时间：" style="margin-bottom: -36px;">{{taskDetail.testTime | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="截止时间：" style="margin-left: 200px;">{{taskDetail.endTime | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="标签：">
                    <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
                        {{item.name}}
                    </el-tag>
                </el-form-item>
                <div class="ctpc-member-con" v-if="taskDetail.type==2">
                    <div class="ctpc-member-list clearfix" :class="taskStepStatus(item, taskDetail.users.length)"
                         v-for="(item,index) in taskDetail.users">
                        <el-tooltip  placement="top">
                            <div slot="content">
                                <span>进行中任务:</span>
                                <div v-for="(userTask,userIndex) in item.userTask">
                                    <div class="fl" style="margin-left: 20px;">{{userIndex+1}}:任务名称:{{userTask.taskName}}</div>
                                    <div class="fl" style="margin-left: 20px;">工作量:{{userTask.taskHours}}</div>
                                    <div>&nbsp;&nbsp;开始时间:{{userTask.beginTime | formatDate}}</div>
                                    <div>&nbsp;&nbsp;结束时间:{{userTask.endTime | formatDate}}</div>
                                    <!--<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 任务描述:{{userTask.description}}</div>-->
                                </div>
                                <div v-if="item.userTask&&item.userTask.length==0">无</div>
                            </div>
                            <span class="fl ctpc-member-head" >{{item.userName}}</span>
                        </el-tooltip>
                        <span class="fl ctpc-member-job-time">工作量:{{item.taskHours}}工时</span>
                        <span class="fl ctpc-member-end-time">截止:{{item.endTime | formatDate}}</span>
                        <span class="fl ctpc-member-assess" v-show="item.status==3">评价：{{item.commentGrade}}</span>
                        <el-tooltip placement="top">
                            <div slot="content">{{item.description}}<br/>开始时间:{{item.beginTime | formatDate}}</div>
                            <span class="fl" style="margin-left: 25px"><i class="el-icon-information"></i></span>
                        </el-tooltip>
                        <span v-if="item.proTest && !taskDetail.testing" class="fl ctpc-member-end-time" style="margin-left:20px;color: #66ccff">测试中</span>
                    </div>
                    <div class="bdl-line"></div>
                </div>
                <div v-else="taskDetail.type==1" v-for="(item,index) in taskDetail.users">
                    <el-form-item class="task-form" label="工作量：">{{item.taskHours}} 工时</el-form-item>
                    <el-form-item class="task-form" label="负责人：">{{item.userName}}</el-form-item>
                </div>

            </el-form>

            <div class="trends" v-show="taskLog.list.length>0">
                <div class="trends-title clearfix">
                    <b class="fl">动态</b>
                    <a class="fr" href="javascript:;" @click="taskLogMore(taskDetail.id)" v-show="taskLog.hasNextPage">显示较早的动态</a>
                </div>
                <ul style="height: 100px; overflow: auto">
                    <li v-for="(item,index) in taskLog.list" :key="index" class="clearfix">
                        <div style="float: left;width: 350px;"> {{item.title}} <div class="task-title-detail" v-show="item.content!==''" ><em></em>{{item.content}}</div></div>
                        <span style="float: right;font-size: 13px;padding-right: 10px"> {{item.createTime | formatTime}}</span>
                    </li>
                </ul>
            </div>
        </el-dialog>

        <el-dialog
                title="个人详情"
                top="10%"
                :visible.sync="showUserInfoVisible"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                class="user-class">
            <el-form>
                <el-form-item class="task-form" label="姓名：" prop="name">{{userInfo.name}}</el-form-item>
                <el-form-item class="task-form" label="账号：" prop="account">{{userInfo.account}}</el-form-item>
                <el-form-item class="task-form" label="角色：" prop="jobRoleName">{{userInfo.jobRoleName}}</el-form-item>
                <el-form-item class="task-form" label="级别：" prop="levelName">{{userInfo.levelName}}</el-form-item>
                <el-form-item class="task-form" label="职位：" prop="jobName">{{userInfo.jobName}}</el-form-item>
                <el-form-item class="task-form" label="手机号：" prop="phone">{{userInfo.phone}}</el-form-item>
                <el-form-item class="task-form" label="邮箱：" prop="email">{{userInfo.email}}</el-form-item>
                <el-form-item class="task-form" label="所属部门：" prop="deptName">{{userInfo.deptName}}</el-form-item>
                <el-form-item class="task-form" label="用户权限：" prop="userRoleName">{{userInfo.userRoleName}}</el-form-item>
                <el-form-item class="task-form" label="考勤序号：" prop="checkSort">{{userInfo.checkSort}}</el-form-item>
                <el-form-item class="task-form" label="用户状态：" prop="statusName">{{userInfo.statusName}}</el-form-item>
            </el-form>
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
    import Stats from './Stats2'
    import UploadAvatar from './UploadAvatar.vue'
    import Calculate from  './Calculate.vue'
    import AlterInfo from './AlterInfo'
    import moment from 'moment';
    import Notice from './Notice'
    import SummaryNav from './SummaryNav'
    import helper from '../lib/Helper'


    export default {
        data() {
            return {
                activeName: '',
                tabs: [
                    {
                        label: '主页',
                        name: 'navIndex'
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
                emailCode:'',

                // sch --
                //未读通知
                unreadNoticeData:[],
                //所有通知
                noticeData:[],
                //所有未读通知数量
                unreadNoticeNum:0,
                //所有通知弹窗可见
                allShow:false,
                reqDTO:{
                    pageNum:1
                },
                noticePage:{
                    pageSize:10,
                    total:0
                },
                pageNum:1,
                showTaskDetail:false,
                taskDetail: {},
                taskLog: {
                    list: [],
                    hasNextPage: false,
                    pageNum: 1
                },
                priorityList: [
                    {label: '普通', value: 0},
                    {label: '紧急', value: 1},
                    {label: '非常紧急', value: 2},
                ],
                facilityList: [
                    {label: '容易', value: 1},
                    {label: '简单', value: 2},
                    {label: '一般', value: 3},
                    {label: '较难', value: 4},
                    {label: '困难', value: 5},
                ],
                userInfo:{
                    id:'',
                    name:'',
                    account:'',
                    jobRoleName:'',
                    levelName:'',
                    jobName:'',
                    phone:'',
                    email:'',
                    deptName:'',
                    userRoleName:'',
                    checkSort:'',
                    statusName:'',
                },
                showUserInfoVisible:false
            };
        },
        filters:{
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY/MM/DD');
            },
            formatTime: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD HH:mm:ss');
            }
        },
        created() {
            this.fetchMyProfile();
            this.activeName = 'navIndex';
            this.fetchUnreadNoticeNum();
            this.checkNotice()
        },
        mounted(){

        },
        beforeMount() {
            //监听子组件传过来的tab选中事件
            this.$root.eventBus.$on("handleTabSelected", (val) => {
                this.activeName = val;
            });
            if (Helper.decodeToken().userRole < 3) {
                this.tabs.push({
                    label: '任务',
                    name: 'task'
                },);
                this.tabs.push({
                    label: '项目',
                    name: 'project'
                },);
                this.tabs.push({
                    label: 'BUG',
                    name: 'bug'
                },);
                this.tabs.push({
                    label: '统计',
                    name: 'stats'
                },);
                this.tabs.push({
                    label: '需求',
                    name: 'demand'
                },);
                this.tabs.push({
                    label: '计划',
                    name: 'plan'
                },);
                this.tabs.push({
                    label: '通知',
                    name: 'notice'
                },);
            }
            if (Helper.decodeToken().userRole < 1) {
                this.tabs.push({
                    label: '积分',
                    name: 'intergral'
                },);
                this.tabs.push({
                    label: '计算',
                    name: 'calculate'
                });
                // this.tabs.push({
                //     label: '评价',
                //     name: 'comments'
                // });
                this.tabs.push({
                    label:'评价',
                    name:'evaluation'
                });
                this.tabs.push({
                    label: '组织',
                    name: 'organization'
                });
            }
            if (Helper.decodeToken().userRole > 0 && Helper.decodeToken().userRole < 3){
                this.tabs.push({
                    label:'总结',
                    name:'summaryNav'
                });
            }
            if (Helper.decodeToken().departmentId === 0) {
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
                this.fetchUnreadNoticeNum();
                window.localStorage.removeItem("justMine")
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


            // sch --
            alterInfo(){
                this.showPerOpt = false;
                this.$refs.alterInfoPop.show()
            },
            //查看信息
            showInfo(){
                Http.zsyGetHttp('/user/'+helper.decodeToken().userId,null,(res)=>{
                    this.userInfo = res.data;
                    this.showUserInfoVisible = true;
                })
            },
            //查询最近5条未读通知
            fetchUnreadNotice(){
                Http.zsyGetHttp('/task/notification/un-read',{},(res)=>{
                    this.unreadNoticeData = res.data;
                })
            },
            //查询所有未读数量
            fetchUnreadNoticeNum(){
                if (Helper.decodeToken().userRole < 3){
                    Http.zsyGetHttp('/task/notification/un-read/num',{},(res)=>{
                        this.unreadNoticeNum = res.data.count;
                        window.localStorage.setItem("unreadNum",res.data.count);
                        this.tabs[6].value = this.unreadNoticeNum

                    })
                }
            },
            //查询所有通知
            fetchAllNotice(){
                this.allShow = true;
                Http.zsyPostHttp('/task/notification/all',this.reqDTO,(res)=>{
                    this.noticeData = res.data.list;
                    this.noticePage.total = res.data.total
                })
            },
            //标记当前通知为已读
            readNotice(nid){
                Http.zsyPutHttp('/task/notification/read/'+nid,{},(res) => {
                    this.$message({showClose: true, message: '标记成功', type: 'success'});
                    this.fetchAllNotice();
                    this.fetchUnreadNoticeNum()
                })
            },
            handleCurrentChange(currentPage) {
                this.reqDTO.pageNum = currentPage;
                this.fetchAllNotice()
            },
            pageLayout() {
                if (this.noticePage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            showTaskDetails(taskId){
                this.showTaskDetail = true;
                Http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    this.taskDetail = resp.data
                });
                this.getTaskLog(taskId)
            },
            getTaskLog(taskId) {
                Http.zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, (resp) => {
                    this.taskLog.list = resp.data.list;
                    this.taskLog.hasNextPage = resp.data.hasNextPage;
                });
            },
            taskLogMore(taskId) {
                this.taskLog.pageNum += 1;
                Http.zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, (resp) => {
                    let logs = resp.data.list;
                    this.taskLog.list = this.taskLog.list.concat(logs);
                    this.taskLog.hasNextPage = resp.data.hasNextPage;
                });
            },
            taskStepStatus(item, taskUserNum){
                const commented = item.commentNum > 0 && item.commentNum === taskUserNum - 1;
                let className = 'in';
                if (item.status === 1) {
                    // 进行中
                    className = "in"
                }else if(item.status>1 && !commented){
                    // 已完成未评级
                    className = "done"
                }else {
                    // 已评价
                    className = "finished"
                }
                return className;
            },
            hideTaskDetail() {
                this.showTaskDetail = false;
                this.taskDetail = {};
                this.taskLog.list = [];
                this.taskLog.hasNextPage = false;
                this.taskLog.pageNum = 1;
            },
            //每5分钟定时检查当前用户是否由未读通知
            checkNotice(){
                if (Helper.decodeToken().userRole < 3){
                    setInterval(() => {
                        this.unreadNoticeNum = window.localStorage.getItem("unreadNum")
                        // console.log(this.unreadNoticeNum)
                    },1000)
                }
            },
            // -- sch


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
                    if (res.data.avatarUrl && res.data.avatarUrl!=='') {
                        this.avatarUrl = res.data.avatarUrl
                    }
                })
            },
            deptChoose(data){
                this.departmentId=data.id;
            },
            addDept(){
                if(this.organization.trim()!==''&&this.organization!=null&&this.organization.length<20){
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
                if(this.departmentId===''){
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
                });

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
                if(this.emailCode!=null&&this.emailCode!==''){
                    Http.zsyGetHttp('/user/validateEmail/'+this.emailCode,{},(res)=>{
                        if (res.errCode!=='00'){
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
            UploadAvatar: UploadAvatar,
            AlterInfo:AlterInfo,
            Notice:Notice,
            SummaryNav:SummaryNav
        }
    }
</script>
<style>
    /*.el-tabs__item{
        width: 100px;
        line-height: 32px;
    }*/
    .user-class .el-dialog--small {
        width: 500px;
    }
</style>
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

    .el-dialog__wrapper .myDialog {
        width: 600px !important;
    }

    .task-form {
        margin-bottom: 0;
    }

    .ctpc-member-con {
        margin: 15px 0;
        padding-left: 10px; /* border-left: 1px solid #ccc; */
        margin-left: 6px;
        position: relative;
    }

    .ctpc-member-list {
        height: 42px;
        background: #fff;
        border: 1px solid #ccc;
        line-height: 42px;
        color: #000;
        padding: 0 4px;
        position: relative;
        margin-bottom: 10px;
        box-shadow: 0 0 10px #ccc;
        display: -webkit-flex;
        display: -moz-flex;
        display: -ms-flex;
        display: -o-flex;
        display: flex;
    }

    .ctpc-member-list.done:before {
        content: '';
        position: absolute;
        left: -17px;
        top: 12px;
        width: 12px;
        height: 12px;
        border-radius: 50%;
        background: #008000;
        z-index: 110;
    }

    .ctpc-member-list.finished:before {
        content: '';
        position: absolute;
        left: -17px;
        top: 12px;
        width: 12px;
        height: 12px;
        border-radius: 50%;
        background: #006699;
        z-index: 110;
    }

    .ctpc-member-list.in:before {
        content: '';
        position: absolute;
        left: -17px;
        top: 12px;
        width: 12px;
        height: 12px;
        border-radius: 50%;
        background: #e4e8f1;
        z-index: 110;
    }

    .ctpc-member-job-time {
        width: 110px;
    }

    .ctpc-member-end-time {
        /*width: 110px;*/
    }

    .ctpc-member-assess {
        width: 70px;
        margin-left: 30px;
    }

    .bdl-line {
        position: absolute;
        left: 0;
        bottom: 20px;
        top: 14px;
        border-left: 1px solid #ccc;
    }

    .trends {
        /*background-color: #f2f2f2; */
        /*padding-left: 10px;*/
        line-height: 30px;
        border: 1px solid #e4e8f1;

    }

    .trends ul{
        padding-left: 10px;
        list-style: circle;
    }
    .trends li{
        /*list-style: circle!important;*/
    }
    .trends li:before{
        content:"*";
        float: left;
        margin-right: 5px;
        color: #f40;
    }

    .trends-title {
        padding: 0 10px;
        line-height: 30px;
        background-color: #e4e8f1;
    }

    .trends-title a {
        color: #20a0ff;

    }

    .ctpc-member-head {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        background: #006699;
        color: #fff;
        font-size: 10px;
        text-align: center;
        line-height: 36px;
        margin-top: 3px;
        overflow: hidden;
        margin-right: 10px;
    }

    .item {
        size: 10px;
        margin-top: 10px;
        margin-right: 40px;
    }
</style>

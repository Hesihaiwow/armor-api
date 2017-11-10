<template>
    <div class="stats-con">
        <el-tabs v-model="activeName" @tab-click="" style="position:relative;margin-bottom: 20px;">
            <el-tab-pane label="任务统计" name="stat"  style="">
                <el-table :data="statsData" >
                    <el-table-column prop="name" label="成员" align="center" ></el-table-column>
                    <el-table-column prop="inProcess" label="我的任务/进行中任务" align="center" >
                        <template scope="sco">
                            <el-button type="text" @click="getTask(sco.$index)">{{sco.row.inProcess}} / {{sco.row.multiTask}}</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="hours" label="进行中任务耗时（小时）" align="center"></el-table-column>
                    <el-table-column prop="delay" label="超时任务" align="center" >
                        <template scope="scope">
                            <span type="text" style="color: red;">{{scope.row.delay}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="complete" label="已完成任务" align="center" ></el-table-column>
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="线上bug" name="bug"  style="">
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="bugList.userId" clearable filterable   placeholder="筛选用户">
                        <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </div>
                <div class="add-member-basic-msg fl"><el-date-picker v-model="bugList.startTime" type="date" placeholder="选择开始日期"></el-date-picker></div>
                <div class="add-member-basic-msg fl"><el-date-picker v-model="bugList.endTime" type="date" placeholder="选择结束日期"></el-date-picker></div>
                <div class="add-member-basic-msg fl" ><img src="../assets/img/u1221.png" alt="" @click="getBugList()" class="search-btn"></div>
                <el-button type="primary" style="margin-left: 300px;margin-bottom: 10px;" class="add-member-basic-msg fl" @click="createBugSolve" v-show="permit">创建bug处理</el-button>
                <el-table :data="bugManage" border>
                    <el-table-column prop="projectId" label="序号" align="center" width="100"></el-table-column>
                    <el-table-column prop="description" label="问题描述" align="center"></el-table-column>
                    <el-table-column prop="projectName" label="问题项目" align="center" width="130"></el-table-column>
                    <el-table-column prop="users" label="负责人" align="center" width="200"></el-table-column>
                    <el-table-column prop="createTime" label="发现日期"  width="130"></el-table-column>
                    <el-table-column prop="processTime" label="处理日期"  width="130"></el-table-column>
                    <el-table-column label="操作" width="100">
                        <template scope="scope">
                            <el-button @click="bugDetail(scope.row)" type="text" size="small" >查看</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination
                            @current-change="handleCurrentChange"
                            :current-page.sync="bugList.pageNum"
                            :page-size="bugFormPage.pageSize"
                            :layout="pageLayout"
                            :total="bugFormPage.total">
                    </el-pagination>
                </div>
            </el-tab-pane>
        </el-tabs>
        <el-dialog
                title="创建Bug处理结果"
                style="width:auto;"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="createBugSolvingVisible">
                <div class="ctpc-con">
                    <div  style="display: inline"><span class="star">*</span>项目</div>
                    <div style="display: inline;margin-left: 60px">
                        <el-select v-model="bugForm.projectId" placeholder="请选择">
                            <el-option  v-for="item in projectForm" :key="item.id" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </div>
                    <div style="margin-top: 20px"><span class="star">*</span>bug描述</div>
                        <el-input type="textarea" style="position: relative;margin-left: 100px;margin-top:-20px;width: 600px" v-model="bugForm.description" :rows="3"></el-input>
                    <div style="margin-top: 20px;margin-bottom: -20px"><span class="star">*</span>	发现日期</div>
                        <el-date-picker v-model="bugForm.createTime" type="date" placeholder="选择发现日期" style="position: relative;margin-left: 100px"></el-date-picker>
                    <div style="margin-top: 20px;margin-bottom: -20px"><span class="star">*</span>	处理日期</div>
                        <el-date-picker v-model="bugForm.processTime" type="date" placeholder="选择处理日期" style="position: relative;margin-left: 100px"></el-date-picker>
                </div>

                <div class="ctpc-member-con">
                    <div class="ctpc-member-list clearfix in" v-for="(item,index) in bugUsers"  :class="item.cssClass">
                        <span class="fl ctpc-member-head">{{item.userName}}</span>
                        <span class="fl ctpc-member-job-time">积分:{{item.integral}}</span>
                        <span style="position: absolute;right: 10px;">
                                <el-button type="text" icon="edit" @click="modifyMember(index,bugUsers)"></el-button>
                            <el-button type="text" icon="close" @click="deleteMember(index)"></el-button>
                        </span>
                    </div>
                </div>
                <div class="ctpc-add-member-detail" v-if="showAddDetail">
                    <div class="add-member-basic">
                        <div class="add-member-basic-list clearfix">
                            <div class="add-member-basic-menu fl"><span class="star">*</span>姓名：</div>
                            <div class="add-member-basic-msg fl">
                                <el-select v-model="addMemberIndex.userId" filterable  placeholder="请选择" @change="stepUserChange">
                                    <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>积分：
                            </div>
                            <div class="add-member-basic-msg fl">
                                <input class="member-time-count" v-model="addMemberIndex.integral" :maxlength="6" style="width:80px">
                            </div>
                        </div>
                    </div>
                    <div class="ctpc-btns">
                        <input type="button" class="ctpc-cancel" @click="cancelAddMember" value="取消">
                        <input type="button" class="ctpc-save" @click="saveAddMember" value="确定">
                    </div>
                </div>
                <div class="add-member-opt" v-show="!showAddDetail" @click="showAddDetail = !showAddDetail;">
                    <span class="add-member-icon">+</span>
                    <span class="add-member-msg" style="">添加成员</span>
                </div>
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="saveBugForm('bugForm')">立即创建</el-button>
            <el-button @click="createBugSolvingVisible = false">取 消</el-button>
          </span>
        </el-dialog>

        <el-dialog title="Bug处理结果详情" size="tiny" :close-on-click-modal="false"
            :close-on-press-escape="false"
            :visible.sync="bugDetailVisible">
            <el-form>
                <el-form-item class="task-form" label="项目名称：">{{bugDetailForm.projectName}}</el-form-item>
                <el-form-item class="task-form" label="问题描述：">{{bugDetailForm.description}}</el-form-item>
                <div class="ctpc-member-list clearfix"  v-for="(item,index) in bugDetailForm.bugUsers">
                    <span class="fl ctpc-member-head">{{item.userName}}</span>
                    <span class="fl ctpc-member-job-time">积分:{{item.integral}}</span>
                </div>
            </el-form>


        </el-dialog>

    </div>
</template>
<script>
    import Http from '../lib/Http'
    import Helper from '../lib/Helper'
    import ElButton from "../../node_modules/element-ui/packages/button/src/button";
    import ElDialog from "../../node_modules/element-ui/packages/dialog/src/component";
    import moment from 'moment';
    import helper from '../lib/Helper'

    export default {
        name: 'IntegralHistory',
        data() {
            return {
                activeName:'stat',
                createBugSolvingVisible:false,
                statsData:[],
                bugList:{
                  userId:'',
                  startTime:'',
                  endTime:'',
                  pageNum:1,
                },
                bugFormPage:{
                    pageSize: 10,
                    total: 0,
                },
                bugForm:{
                    projectId:'',
                    description:'',
                    createTime:'',
                    processTime:''
                },
                bugDetailForm:{},
                bugManage:[],
                addMemberIndex:{
                    index:'',
                    userId:'',
                    integral:'',
                    userName:'',
                },
                stepTemp: {},
                bugDetailVisible:false,
                projectForm:[],
                userList:[],
                bugUsers:[],
                showAddDetail:false,
                statsPage: {
                    layout: "total, pager",
                    currentPage: 1,
                    pageSize: 10,
                    totals: 0,
                    pageNum: 0
                },
            }
        },
        beforeMount:function () {
            this.getStats(this.statsPage.currentPage);
            //选中任务tab
            this.$root.eventBus.$emit("handleTabSelected", "stats");
            this.fetchUserList();
            this.fetchProjectList();
            this.fetchProjectList();
            this.getBugList();
        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole < 1;
            },
            pageLayout() {
                if (this.bugFormPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            }
        },
        methods: {
            getStats(currentPage){
                Http.zsyGetHttp(`/stats/list/`, {}, (resp) => {
                    this.statsData =  resp.data;
                });
            },
            getTask(index){
                this.$router.push({name:'taskList', params:{ userId:this.statsData[index].id }})
            },
            getBugList(){
                if( this.bugList.startTime!=''){
                    this.bugList.startTime = moment(this.bugList.startTime).format('YYYY-MM-DD HH:mm:ss')
                }
                if( this.bugList.endTime!=''){
                    this.bugList.endTime = moment(this.bugList.endTime).format('YYYY-MM-DD HH:mm:ss')
                }
                Http.zsyGetHttp(`/bug/list/`, this.bugList, (resp) => {
                    this.bugManage =  resp.data.list;
                    this.bugFormPage.total = resp.data.total;
                });
            },
            saveBugForm(){
                if (this.bugForm.projectId == ''||this.bugForm.description == '') {
                    this.errorMsg('请将Bug信息填写完整');
                    return
                }
                if (this.bugForm.createTime == ''||this.bugForm.processTime == '') {
                    this.errorMsg('请将Bug信息填写完整');
                    return
                }
                this.bugForm.createTime  = moment(this.bugForm.createTime ).format('YYYY-MM-DD HH:mm:ss')
                this.bugForm.processTime  = moment(this.bugForm.processTime ).format('YYYY-MM-DD HH:mm:ss')
                let param = this.bugForm;
                param.projectId = param.projectId.trim()
                param.description = param.description.trim()
                param['bugUsers'] = this.bugUsers;
                Http.zsyPostHttp('/bug/add', param, (resp) => {
                    this.$message({
                        showClose: true,
                        message: 'Bug处理结果创建成功',
                        type: 'success'
                    });
                    this.bugForm.projectId = this.bugForm.description = '';
                    this.bugForm.createTime = this.bugForm.processTime = '';
                    this.createBugSolvingVisible = false
                    this.bugUsers = [];
                    this.getBugList();
                })

            },
            createBugSolve(){
                this.createBugSolvingVisible = true;
                this.bugForm.description = this.bugForm.projectId = this.bugForm.createTime = '';
                this.bugUsers = [];
                this.showAddDetail = false
                this.addMemberIndex = {
                    index: '',
                    userId: '',
                    userName: '',
                    integral: '',
                }
            },
            saveAddMember(){
                if (this.addMemberIndex.userId == ''||this.addMemberIndex.integral == '') {
                    this.errorMsg('请将积分信息填写完整');
                    return
                }
                this.showAddDetail = !this.showAddDetail;
                if (this.addMemberIndex.index === '') {
                    let bugUser = {}
                    bugUser.userId = this.addMemberIndex.userId
                    bugUser.userName = this.addMemberIndex.userName
                    bugUser.integral = this.addMemberIndex.integral
                    this.bugUsers.push(bugUser)
                } else {
                    // 取消css
                    this.bugUsers[this.addMemberIndex.index].cssClass = ''
                }

                this.addMemberIndex = {
                    index: '',
                    userId: '',
                    userName: '',
                    integral: '',
                }
                this.stepTemp = {}

            },
            modifyMember(index, stages) {
                this.stepTemp = {
                    userId: stages[index].userId,
                    userName: stages[index].userName,
                    integral: stages[index].integral,
                }
                this.bugUsers.forEach((item) => {
                    item.cssClass = ''
                })
                this.bugUsers[index].cssClass = 'stepActive'
                this.addMemberIndex = stages[index]
                this.addMemberIndex.index = index
                this.showAddDetail = true;
            },
            deleteMember(index) {
                this.bugUsers.splice(index, 1);
                if (this.bugUsers.length == 0) {
                    this.showAddDetail = false
                    this.addMemberIndex = {
                        index: '',
                        userId: '',
                        userName: '',
                        integral: '',
                    }
                }
            },
            cancelAddMember(){
                this.showAddDetail = !this.showAddDetail;
            },
            fetchUserList() {
                let vm = this
                Http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.userList = resp.data
                })
            },
            fetchProjectList() {
                let vm = this
                Http.zsyGetHttp('/project/list', {}, (resp) => {
                    vm.projectForm = resp.data
                })
            },
            stepUserChange(val) {
                let vm = this;
                this.userList.forEach((user) => {
                    if (user.id === val) {
                        vm.addMemberIndex.userName = user.name
                    }
                })
            },
            bugDetail(row){
                this.bugDetailVisible = true;
                Http.zsyGetHttp('/bug/'+row.id, null, (resp) => {
                    this.bugDetailForm = resp.data
                })
            },
            handleCurrentChange(currentPage){
                this.bugList.pageNum = currentPage;
                this.getBugList();
            },
            isDecimal(str) {
                var regu = /^[-]{0,1}[0-9]{1,}$/;
                if (regu.test(str)) {
                    return true;
                }
                var re = /^[-]{0,1}(\d+)[\.]+(\d+)$/;
                if (re.test(str)) {
                    if (RegExp.$1 == 0 && RegExp.$2 == 0) return false;
                    return true;
                } else {
                    return false;
                }
            },
            errorMsg(msg) {
                this.$message({
                    showClose: true,
                    message: msg,
                    type: 'error'
                });
            }

        }
    }


</script>
<style scoped>

    .stats-con {
        width: 1100px;
        font-size: 14px;
        background: #fff;
        padding: 30px 0;
        box-shadow: 0 0 10px #ccc;
        margin: auto;
    }

    .star {
        color: red;
        padding: 1px;
    }

    .ctpc-tags > li {
        display: inline-block;
        line-height: 30px;
    }

    .ctpc-tag-lis > span {
        display: inline-block;
        vertical-align: middle;
        font-size: 14px;
    }

    .ctpc-tag-lis .tag-lis-delete:hover {
        text-shadow: 0 0 8px #ccc;
        font-size: 18px !important;
    }

    .ctpc-member-list > span {
        display: inline-block;
        vertical-align: middle;
    }

    .add-member-opt {
        cursor: pointer;
        margin: 20px 10px;
        width: 80px;
    }

    .add-member-opt > span {
        display: inline-block;
        vertical-align: middle;
    }

    .add-member-icon {
        background: #36A8FF;
        color: #fff;
        width: 20px;
        height: 20px;
        text-align: center;
        font-size: 20px;
        line-height: 16px;
        font-weight: bold;
        border-radius: 50%;
    }

    .add-member-msg {
        color: #36A8FF;
        margin-left: 24px;
        margin-top: -45px;
    }

    .ctpc-btns {
        text-align: right;
        margin-top: 20px;
    }

    .ctpc-btns > input {
        display: inline-block;
        width: 80px;
        height: 28px;
        margin-left: 12px;
        cursor: pointer;
        border-radius: 4px;
    }

    .ctpc-cancel {
        background: #fff;
        border: 1px solid #ccc;
    }

    .ctpc-cancel:hover {
        background: #fff;
        border: 1px solid #36A8FF;
        color: #36A8FF;
        font-weight: 700;
    }

    .ctpc-save {
        background: #36A8FF;
        color: #fff;
    }

    .add-member-basic {
        background: #fff;
        border: 1px solid #ccc;
        margin-top: 10px;
        padding: 10px;
        font-size: 14px;
    }

    .add-member-basic-menu {
        width: 82px;
        text-align: right;
    }

    .add-member-basic-list { /* border-bottom: 1px solid #ccc; */
        padding: 5px 0;
        line-height: 34px;
    }

    .add-member-basic-list:last-child {
        border: none;
    }

    .add-member-basic-msg .el-select {
        width: 140px;
        margin-left: 10px;
    }

    .member-time-count {
        width: 40px;
        border: 1px solid #ccc;
        height: 26px;
        border-radius: 4px;
        margin-right: 4px;
        text-indent: 4px;
    }

    .add-member-basic-time {
        margin-left: 40px;
    }

    .add-member-basic-msg .el-date-editor.el-input {
        width: 140px;
    }

    .add-member-basic-msg {
        margin-left: 10px;
        margin-bottom: 10px;
    }

    .tag-add-sel .el-select {
        width: 188px;
    }

    .ctpc-ins-edit .ctpc-btns {
        margin-top: 0;
    }

    .ctpc-member-con {
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


    .ctpc-member-list > span {
        display: inline-block;
        vertical-align: middle;
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

    .ctpc-member-job-time {
        width: 110px;
    }

    .stepActive {
        box-shadow: 0 0 10px #20A0FF !important;
    }

    .search-btn {
        vertical-align: middle;
        cursor: pointer;
        margin-left: 100px;
        margin-top: 5px;
    }

    .pagination .el-pagination {
        text-align: right;
        padding-right: 0;
        margin-top: 20px;
        margin-right: 10px;
    }

    .ctpc-list-menu {
        line-height: 30px;
        width: 80px;
        font-size: 14px;
    }

    .ctpc-list-con {
        width: 410px;
    }

    .ctpc-con {
        height: 300px;
        overflow-y: scroll;
        padding-right: 10px;
    }

    .ctpc-con::-webkit-scrollbar {
        width: 10px;
        background-color: #fff;
    }

    .ctpc-con::-webkit-scrollbar-thumb {
        background-color: rgb(255, 255, 255);
        background-image: -webkit-gradient(linear, 40% 0%, 75% 84%, from(rgb(54, 168, 255)), color-stop(0.6, rgb(54, 229, 255)), to(rgb(54, 192, 255)));
        border-radius: 10px;
    }

    .ctpc-con::-webkit-scrollbar-track {
        /* -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.1); */
        background-color: #fff;
        border-radius: 10px;
    }


</style>


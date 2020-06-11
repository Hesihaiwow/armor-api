<template>
    <div>
        <div class="userCommentsBlock">
            <el-form :inline="true" >
                <el-form-item label="">
                    <el-select filterable clearable v-model="queryDTO.taskUserId" placeholder="被评价人">
                        <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="">
                    <el-date-picker
                            v-model="queryDTO.beginTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="任务完成时间开始时间"
                    >
                    </el-date-picker>
                    <span style="font-size: 14px;color: #606266;">-</span>
                    <el-date-picker
                            v-model="queryDTO.endTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="任务完成时间截止时间"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit">查询</el-button>
                </el-form-item>
            </el-form>
            <el-table :data="userEvaluationList" border>
                <el-table-column type="expand">
                    <template slot-scope="scope">
                        <el-form>
                            <div v-for="task in scope.row.taskBaseResDTOS">
                                <el-form-item align="center">
                                    <a style="cursor: pointer;color: #1d90e6"
                                       @click="getTaskDetail(task.id,scope.row.jobRole,scope.row.userId,scope.row.userName)">{{task.name}}</a>
                                </el-form-item>
                            </div>
                        </el-form>
                    </template>
                </el-table-column>
                <el-table-column label="姓名" prop="userName" align="center"></el-table-column>
                <el-table-column label="各项评价" align="center">
                    <template slot-scope="scope">
                        <el-form>
                            <div v-for="item in scope.row.scoreResDTOS">
                                <el-form-item :label="item.evaluationOptionName">
                                    <el-rate
                                            v-model="item.avgScore"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            disabled
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{item.avgScore}}</span>
                                </el-form-item>
                            </div>
                        </el-form>

                    </template>
                </el-table-column>
                <el-table-column label="综合评价" prop="avgTotalScore" align="center" sortable></el-table-column>
                <el-table-column label="任务数量" prop="taskNum" align="center" sortable></el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        @current-change="handleCurrentChange"
                        :current-page.sync="evaluationPage.pageNum"
                        :page-size="evaluationPage.pageSize"
                        :layout="pageLayout"
                        :total="evaluationPage.total">
                </el-pagination>
            </div>
        </div>
        <el-dialog
            title="评价详情"
            top="10%"
            :visible.sync="showTaskEvaluationDetail"
            size="tiny"
            :before-close="hideTaskEvaluationDetail"
            :close-on-click-modal="false"
            :close-on-press-escape="false">
            <div style="border: gray;border-style: solid;border-width: 1px;padding: 10px;">
                <span style="font-size: 20px;margin-bottom: 20px" v-if="hasAvgEvalution">被评价人：{{taskUserName}}</span>
                <h2 style="font-size: 20px;margin-bottom: 20px" v-if="hasAvgEvalution">总体评价：</h2>
                <div  v-for="(item,index) in avgEvaluation" v-if="hasAvgEvalution">
                    <el-form label-position="left" inline class="demo-table-expand">
                        <el-form-item class="task-form" :label="item.option" style="margin-top: -10px">
                            <el-rate v-model="item.score"
                                     :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                     :allow-half=true
                                     disabled
                                     show-text
                                     text-template="{value}"
                                     style="float: left;margin-top: 7px">
                            </el-rate>
                            <!--<span>{{item.score}}</span>-->
                        </el-form-item>
                    </el-form>
                </div>
                <div v-show="!hasAvgEvalution" class="empty">
                    <h2>暂无数据</h2>
                </div>
            </div>
            <div style="border: gray;border-style: solid;border-width: 1px;padding: 10px;margin-top: 10px">
                <h2 style="font-size: 20px;margin-bottom: 20px" v-show="hasAvgEvalution">用户评价：</h2>
                <div v-for="(item,index) in taskEvaluation" v-show="hasAvgEvalution">
                    <el-form label-position="left" inline class="demo-table-expand">
                        <el-form-item class="task-form" label="评价人">
                            <span>{{ item.evaluateUserName }}</span>
                        </el-form-item>
                        <div v-for="evaluation in item.evaluationScoreResDTOS">
                            <el-form-item class="task-form" :label="evaluation.evaluationOptionName" style="margin-top: -10px">
                                <el-rate v-model="evaluation.score"
                                         :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                         :allow-half=true
                                         disabled
                                         show-text
                                         text-template="{value}"
                                         style="float: left;margin-top: 7px">
                                </el-rate>
                                <!--<span>{{evaluation.score}}</span>-->
                            </el-form-item>
                        </div>

                    </el-form>
                </div>
            </div>
        <span slot="footer" class="dialog-footer">
                <el-button @click="hideTaskEvaluationDetail" type="primary">确 定</el-button>
            </span>
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
                <el-form-item class="task-form" label="需求文档：">
                    <a v-if="taskDetail.doc !== undefined && taskDetail.doc !== null && taskDetail.doc !== ''" style="cursor: pointer;"
                       @click="toFile(taskDetail.doc)">{{taskDetail.doc.substring(0,50)}}...
                    </a>
                </el-form-item>
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
                        <span class="fl ctpc-member-assess" v-show="item.status==3 && item.avgScore">评分：{{item.avgScore}}</span>
                        <a href="javascript:;" v-show="taskDetail.status>1 && userRole===0 && item.status==3"
                           @click="evaluateDetail(item.id,item.jobRole,item.userName)">查看评价</a>
                        <el-tooltip placement="top">
                            <div slot="content">
                                {{item.description}}<br/>开始时间:{{item.beginTime | formatDate}}
                                <div v-if="item.functionStrs !== undefined && item.functionStrs.length > 0">
                                    <div>功能点:</div>
                                    <div v-for="functionStr in item.functionStrs">{{functionStr}}</div>
                                </div>
                            </div>
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
    </div>
</template>
<script>
    import Http from '../lib/Http'
    import helper from '../lib/Helper'
    import moment from 'moment';
    export default {
        name: 'Evaluation',
        data() {
            return {
                data:{},
                form:{
                    pageNum: 1,
                    userId:'',
                    grade:''
                },
                userList: [],
                gradeList:['A','B','C'],
                queryDTO:{
                    pageNum:1,
                    taskUserId:'',
                    beginTime:'',
                    endTime:''
                },
                userEvaluationList:[],
                evaluationPage:{
                    total:0,
                    pageSize:10,
                    pageNum:1
                },
                activeName:1,
                taskDetail:{},
                taskUserName:'',
                hasAvgEvalution:false,
                showTaskDetail:false,
                showTaskEvaluationDetail:false,
                taskEvaluation:[],
                avgEvaluation:[],
                taskLog: {
                    list: [],
                    hasNextPage: false,
                    pageNum: 1
                },
                priorityList: [
                    {label: '普通', value: 1},
                    {label: '紧急', value: 2},
                    {label: '非常紧急', value: 3},
                ],
                facilityList: [
                    {label: '容易', value: 1},
                    {label: '简单', value: 2},
                    {label: '一般', value: 3},
                    {label: '较难', value: 4},
                    {label: '困难', value: 5},
                ],
                statusOptions:[
                    {name: '进行中', id: 1},
                    {name: '已完成', id: 2},
                ],
            }
        },
        created(){
            this.fetchUserComments();
            this.fetchUserList();
            this.fetchUserEvaluationPage();
        },
        beforeMount:function () {
            //选中任务tab
            this.$root.eventBus.$emit("handleTabSelected", "evaluation");
        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole < 2;
            },
            userRole() {
                let userRole = helper.decodeToken().userRole;
                return userRole;
            },
            showDelete() {
                // 不包含完成的阶段才显示删除
                if (this.taskDetail.users) {
                    let done = this.taskDetail.users.filter((user) => {
                        return user.status == 2
                    })
                    return done.length == 0 && this.taskDetail.status != 3;
                }
            },
            pageLayout() {
                if (this.evaluationPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            }
        },
        filters: {
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD');
            },
            formatTime: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD HH:mm:ss');
            }
        },
        methods: {
            fetchUserComments(){
                let vue = this;
                Http.zsyGetHttp('/stats/userComments', this.form, (resp)=>{
                    vue.data = resp.data
                })
            },
            handleCurrentChange(currentPage){
                this.queryDTO.pageNum = currentPage
                this.fetchUserEvaluationPage();
            },
            fetchUserList() {
                let vm = this
                Http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.userList = resp.data
                })
            },
            onSubmit(){
                // this.evaluationPage.pageNum = 1;
                // this.queryDTO.pageNum  =1;
                if (this.queryDTO.beginTime != null && this.queryDTO.beginTime != ''){
                    this.queryDTO.beginTime = moment(this.queryDTO.beginTime).format('YYYY-MM-DD 00:00:00');
                }
                if (this.queryDTO.endTime != null && this.queryDTO.endTime != ''){
                    this.queryDTO.endTime = moment(this.queryDTO.endTime).format('YYYY-MM-DD 23:59:59');
                }
                this.fetchUserEvaluationPage();
            },
            toTaskList(taskId){
                this.$router.push({name:'taskListFormComments', params:{ taskId:taskId }})
            },
            fetchUserEvaluationPage(){
                Http.zsyPostHttp('/evaluation/average/user/page',this.queryDTO,(res)=>{
                    this.userEvaluationList = res.data.list;
                    this.userEvaluationList.forEach(userEvaluation=>{
                        var avgScore = 0;
                        userEvaluation.scoreResDTOS.forEach(evaluation=>{
                            avgScore += evaluation.avgScore;
                        })
                        userEvaluation.avgTotalScore = Number((avgScore/(userEvaluation.scoreResDTOS.length)).toFixed(2));
                    })
                    this.evaluationPage.total = res.data.total;
                })
            },
            getTaskDetail(taskId,jobRole,userId,userName){
                Http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    this.taskDetail = resp.data
                    this.showTaskDetail = true
                    // this.evaluateDetail(userId,jobRole,userName)
                    this.getTaskLog(taskId)
                });
            },
            getTaskLog(taskId) {
                Http.zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, (resp) => {
                    this.taskLog.list = resp.data.list;
                    this.taskLog.hasNextPage = resp.data.hasNextPage;
                });
            },
            taskStepStatus(item, taskUserNum){
                // const commented = item.commentNum > 0 && item.commentNum == taskUserNum - 1;
                const commented = item.isEvaluated == 1;
                let className = 'in';
                if (item.status == 1) {
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
            evaluateDetail(taskUserId,jobRole,userName) {
                this.taskUserName = userName;
                let vm = this;
                this.taskDetail.users.forEach((user) => {
                    if (user.id == taskUserId) {
                        vm.taskEvaluation = user.evaluationResDTOS;
                        this.avgEvaluation = []
                        var length = vm.taskEvaluation.length
                        //测试
                        if (vm.taskEvaluation.length>0){
                            this.hasAvgEvalution = true
                            if (jobRole == 0){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                            }
                            else if (jobRole == 1){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalQualityScore = 0;
                                var totalEfficiencyScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalQualityScore += evaluation.evaluationScoreResDTOS[3].score
                                    totalEfficiencyScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'效率',
                                    'score':Number((totalEfficiencyScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'质量',
                                    'score':Number((totalQualityScore/length).toFixed(2))
                                })


                            }
                            else if (jobRole == 4){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalQualityScore = 0;
                                var totalEfficiencyScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalQualityScore += evaluation.evaluationScoreResDTOS[3].score
                                    totalEfficiencyScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'效率',
                                    'score':Number((totalEfficiencyScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'质量',
                                    'score':Number((totalQualityScore/length).toFixed(2))
                                })


                            }
                            else if (jobRole == 5){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalQualityScore = 0;
                                var totalEfficiencyScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalQualityScore += evaluation.evaluationScoreResDTOS[3].score
                                    totalEfficiencyScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'效率',
                                    'score':Number((totalEfficiencyScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'质量',
                                    'score':Number((totalQualityScore/length).toFixed(2))
                                })


                            }
                            else if (jobRole == 6){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalQualityScore = 0;
                                var totalEfficiencyScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalQualityScore += evaluation.evaluationScoreResDTOS[3].score
                                    totalEfficiencyScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'效率',
                                    'score':Number((totalEfficiencyScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'质量',
                                    'score':Number((totalQualityScore/length).toFixed(2))
                                })


                            }
                            else if (jobRole == 7){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalQualityScore = 0;
                                var totalEfficiencyScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalQualityScore += evaluation.evaluationScoreResDTOS[3].score
                                    totalEfficiencyScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'效率',
                                    'score':Number((totalEfficiencyScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'质量',
                                    'score':Number((totalQualityScore/length).toFixed(2))
                                })


                            }
                            else if (jobRole == 8){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalQualityScore = 0;
                                var totalEfficiencyScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalQualityScore += evaluation.evaluationScoreResDTOS[3].score
                                    totalEfficiencyScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'效率',
                                    'score':Number((totalEfficiencyScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'质量',
                                    'score':Number((totalQualityScore/length).toFixed(2))
                                })


                            }
                            else if (jobRole == 9){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalQualityScore = 0;
                                var totalEfficiencyScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalQualityScore += evaluation.evaluationScoreResDTOS[3].score
                                    totalEfficiencyScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'效率',
                                    'score':Number((totalEfficiencyScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'质量',
                                    'score':Number((totalQualityScore/length).toFixed(2))
                                })


                            }
                            else if (jobRole == 2){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalDesignScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalDesignScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'美感',
                                    'score':Number((totalDesignScore/length).toFixed(2))
                                })
                            }
                            else if (jobRole == 3){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalEfficiencyScore = 0;
                                var totalDocumentScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalEfficiencyScore += evaluation.evaluationScoreResDTOS[3].score
                                    totalDocumentScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'文档',
                                    'score':Number((totalDocumentScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'效率',
                                    'score':Number((totalEfficiencyScore/length).toFixed(2))
                                })
                            }
                        }
                        return
                    }
                })
                this.showTaskEvaluationDetail = true;
                this.showTaskDetail = false;
            },
            hideTaskEvaluationDetail() {
                this.showTaskEvaluationDetail = false;
                this.hasAvgEvalution = false;
                this.showTaskDetail = true;
            },
            hideTaskDetail() {
                this.showTaskDetail = false;
                this.showTaskEvaluationDetail = false;
                this.taskDetail = {};
                this.taskLog.list = [];
                this.taskLog.hasNextPage = false;
                this.taskLog.pageNum = 1;
            },
            taskLogMore(taskId) {
                this.taskLog.pageNum += 1;
                Http.zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, (resp) => {
                    let logs = resp.data.list;
                    this.taskLog.list = this.taskLog.list.concat(logs);
                    this.taskLog.hasNextPage = resp.data.hasNextPage;
                });
            },
            //跳转到任务关联文档URL
            toFile(url){
                if (url !== null && url !== ''){
                    window.open(url,'_blank')
                }
            },
        }
    }


</script>
<style>

</style>
<style scoped>
    .el-form-item {
        margin-bottom: 0px;
    }
    .task-title-detail{
        margin-top:-5px;
        line-height: 20px;
        font-size:12px;
        color: #ccc;
    }
    .task-title-detail em{
        margin-right: 5px;
        border-left:3px solid #ccc;
    }
    .bdl-line {
        position: absolute;
        left: 0;
        bottom: 20px;
        top: 14px;
        border-left: 1px solid #ccc;
    }
    .ctpc-member-assess {
        width: 70px;
        margin-left: 30px;
    }
    .ctpc-member-job-time {
        width: 110px;
    }

    .ctpc-member-end-time {
        /*width: 110px;*/
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
    .userCommentsBlock{
        width: 1100px;
        margin: auto;
    }
    .pagination .el-pagination {
        text-align: right;
        padding-right: 0;
        margin-top: 20px;
        margin-right: 10px;
    }
    .tb-taskname .cell{
        width: 100px;
        text-overflow: ellipsis; overflow: hidden; white-space: nowrap;
    }
</style>


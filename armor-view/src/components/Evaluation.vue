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
            <el-table :data="userEvaluationList">
                <el-table-column type="expand">
                    <template scope="scope">
                        <el-form>
                            <div v-for="task in scope.row.taskBaseResDTOS">
                                <el-form-item>
                                    <a style="cursor: pointer;color: #1d90e6"
                                       @click="getTaskDetail(task.id,scope.row.jobRole,scope.row.userId,scope.row.userName)">{{task.name}}</a>
                                </el-form-item>
                            </div>
                        </el-form>
                    </template>
                </el-table-column>
                <el-table-column label="姓名" prop="userName"></el-table-column>
                <el-table-column label="综合评价">
                    <template scope="scope">
                        <el-form>
                            <div v-for="item in scope.row.scoreResDTOS">
                                <el-form-item :label="item.evaluationOptionName">
                                    <el-rate
                                            v-model="item.avgScore"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{item.avgScore}}</span>
                                </el-form-item>
                            </div>
                        </el-form>

                    </template>
                </el-table-column>
                <el-table-column label="任务数量" prop="taskNum"></el-table-column>
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
            :before-close="hideTaskEvaluationDetail">
        <span style="font-size: 20px;margin-bottom: 20px" v-if="hasAvgEvalution">被评价人：{{taskUserName}}</span>
        <h2 style="font-size: 20px;margin-bottom: 20px" v-if="hasAvgEvalution">总体评价：</h2>
        <div  v-for="(item,index) in avgEvaluation" v-if="hasAvgEvalution">
            <el-form label-position="left" inline class="demo-table-expand">
                <el-form-item class="task-form" :label="item.option" style="margin-top: -10px">
                    <el-rate v-model="item.score"
                             :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                             :allow-half=true
                             disabled
                             style="float: left;margin-top: 7px">
                    </el-rate>
                    <span>{{item.score}}</span>
                </el-form-item>
            </el-form>
        </div>
        <div v-show="!hasAvgEvalution" class="empty">
            <h2>暂无数据</h2>
        </div>
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
                                 style="float: left;margin-top: 7px">
                        </el-rate>
                        <span>{{evaluation.score}}</span>
                    </el-form-item>
                </div>

            </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
                <el-button @click="hideTaskEvaluationDetail" type="primary">确 定</el-button>
            </span>
    </el-dialog>
    </div>
</template>
<script>
    import Http from '../lib/Http'
    import Helper from '../lib/Helper'
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
                showTaskEvaluationDetail:false,
                taskEvaluation:[],
                avgEvaluation:[],
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
            pageLayout() {
                if (this.evaluationPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
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
                this.queryDTO.pageNum  =1;
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
                    this.evaluationPage.total = res.data.total;
                })
            },
            getTaskDetail(taskId,jobRole,userId,userName){
                Http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    this.taskDetail = resp.data
                    this.evaluateDetail(userId,jobRole,userName)
                });
            },
            evaluateDetail(taskUserId,jobRole,userName) {
                console.log(taskUserId)
                this.taskUserName = userName;
                let vm = this;
                this.taskDetail.users.forEach((user) => {
                    if (user.userId == taskUserId) {
                        vm.taskEvaluation = user.evaluationResDTOS;
                        console.log(vm.taskEvaluation)
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
                            if (jobRole == 1){
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
                            if (jobRole == 2){
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
                            if (jobRole == 3){
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
                console.log(this.taskEvaluation)


                this.showTaskEvaluationDetail = true
            },
            hideTaskEvaluationDetail() {
                this.showTaskEvaluationDetail = false;
                this.hasAvgEvalution = false;
            },
        }
    }


</script>
<style>
    .el-form-item {
        margin-bottom: 0px;
    }
</style>
<style scoped>
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


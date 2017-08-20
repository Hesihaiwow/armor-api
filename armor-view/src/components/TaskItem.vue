<template>
    <div>
        <div class="task-lis" v-for="task in taskItems" @click="taskItemClick(task.id)">
            <div class="head-img"><img src="" alt="" class=""></div>
            <div class="main-task-detail">
                <div class="task-name">{{task.name}}</div>
                <div class="task-state">
                    <span class="task-end" :class="task.endColor">{{task.endText}}</span>
                    <span class="task-time-opt">
                    <i v-show="taskStatus=='TaskDoing'" class="el-icon-circle-check"
                       @click="showFinishedPop(task.id,task.taskUsers[0].id,task.type)"></i>
                    <i v-show="taskStatus=='WaitAssess'" class="el-icon-star-off" @click="showWaitAssess(task.id)"></i>
                    <i v-show="taskStatus=='WaitAuditing' && permit" class="el-icon-edit"
                       @click="showAuditPop(task.id,task.taskUsers[0].id)"></i>
                         <!--<i v-show="task.status<3" class="el-icon-edit" v-on:click.stop="modifyTask(task.id)"></i>-->
                  </span>
                    <ul class="task-key-tag">
                        <li class="task-key-lis" v-for="tag in task.tags">
                            <span class="circle" :style="{background:tag.colorValue}"></span>
                            <span class="task-key-msg">{{tag.name}}</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="task-data-show" v-show="isPrivate && task.status==3">
                <span class="task-score">+{{task.userIntegral}}</span>
                <span class="task-level first">{{task.integralGrade}}</span>
            </div>
            <div class="task-mark" v-show="isPrivate">
                <img src="../assets/img/u431.png" alt="">
                <span class="mark-msg">{{task.projectName}}</span>
            </div>
        </div>
        <div v-show="taskItems.length==0" class="empty">
            <h2>暂无数据</h2>
        </div>
        <el-dialog
                title="完成"
                :visible.sync="showFinishedTask"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                size="tiny"
                :before-close="hideFinishedPop">
            <el-form>
                <el-form-item label="实际耗时">
                    <el-input v-model="finishForm.completeHours" auto-complete="off" style="width:100px"></el-input>
                    小时
                </el-form-item>
                <el-form-item label="实际完成时间">
                    <el-date-picker
                            v-model="finishForm.completeTime"
                            type="datetime"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="finishTask">确 定</el-button>
            <el-button @click="hideFinishedPop">取 消</el-button>
          </span>
        </el-dialog>
        <el-dialog
                title="审核"
                :visible.sync="showAuditTask"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                size="tiny"
                :before-close="hideAuditPop">
            <el-form>
                <el-form-item label="任务名称">{{taskDetail.name}}</el-form-item>
                <el-form-item label="任务描述">{{taskDetail.description}}</el-form-item>
                <el-form-item label="项目">{{taskDetail.projectName}}</el-form-item>
                <el-form-item label="截止时间">{{taskDetail.endTime | formatDate}}</el-form-item>
                <el-form-item label="标签">
                    <el-tag type="gray" v-for="(item, key) in taskDetail.tags" :key="key">{{item.name}}</el-tag>
                </el-form-item>
                <div class="ctpc-member-con">
                    <div class="ctpc-member-list clearfix" v-for="(item,index) in taskDetail.users">
                        <span class="fl ctpc-member-head">{{item.userName}}</span>
                        <span class="fl ctpc-member-job ellipsis">{{item.stageName}}</span>
                        <span class="fl ctpc-member-job-time">工作量:{{item.taskHours}}工时</span>
                        <span class="fl ctpc-member-end-time">截止:{{item.endTime | formatDate}}</span>
                    </div>
                    <div class="bdl-line"></div>
                </div>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button type="text" @click="rejectTask">打回</el-button>
            <el-button type="primary" @click="acceptTask">审核通过</el-button>
          </span>
        </el-dialog>
        <el-dialog
            title="任务详情"
            :visible.sync="showTaskDetail"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            size="tiny"
            :before-close="hideTaskDetail">
        <el-form>
            <el-form-item label="任务名称">{{taskDetail.name}}</el-form-item>
            <el-form-item label="任务描述">{{taskDetail.description}}</el-form-item>
            <el-form-item label="项目">{{taskDetail.projectName}}</el-form-item>
            <el-form-item label="截止时间">{{taskDetail.endTime | formatDate}}</el-form-item>
            <el-form-item label="标签">
                <el-tag type="gray" v-for="(item, key) in taskDetail.tags" :key="key">{{item.name}}</el-tag>
            </el-form-item>
            <div class="ctpc-member-con">
                <div class="ctpc-member-list clearfix" v-for="(item,index) in taskDetail.users">
                    <span class="fl ctpc-member-head">{{item.userName}}</span>
                    <span class="fl ctpc-member-job ellipsis">{{item.stageName}}</span>
                    <span class="fl ctpc-member-job-time">工作量:{{item.taskHours}}工时</span>
                    <span class="fl ctpc-member-end-time">截止:{{item.endTime | formatDate}}</span>
                    <span class="fl ctpc-member-assess" v-show="item.commentGrade">评价：{{item.commentGrade}}</span>
                </div>
                <div class="bdl-line"></div>
            </div>
        </el-form>

        <div>
            <b>动态</b>
            <a href="#" @click="taskLogMore(taskDetail.id)" v-show="taskLog.hasNextPage">显示较早的2条动态</a>
            <div v-for="(item,index) in taskLog.list" :key="index">
                {{item.title}}
            </div>
        </div>
        <span slot="footer" class="dialog-footer" v-show="permit">
                 <el-button type="text" @click="deleteTask" v-show="taskDetail.status!=3">删除</el-button>
                <el-button type="primary" @click="completeTask" v-show="taskDetail.status!=3">完成</el-button>
                <el-button type="primary" @click="showTaskDetail = false" v-show="taskDetail.status==3">确定</el-button>
          </span>
    </el-dialog>
        <el-dialog
                title="评价"
                :visible.sync="showTaskComment"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                size="tiny"
                :before-close="hideWaitAssess">
            <div class="assess-task">
                <div class="assess-task-list">
                    <div class="assess-man-detail">
                        <span class="amd-job">{{myStage.stageName}}</span>
                        <span class="amd-job-time">工作量：{{myStage.taskHours}}小时</span>
                        <span class="amd-during-time">截止：{{myStage.completeTime | formatDate}}</span>
                        <span class="amd-name">{{myStage.userName}}</span>
                    </div>
                </div>
                <div class="assess-task-list" v-for="(stage,index) in commentStages" v-show="stage.status==2">
                    <div class="assess-man-detail">
                        <span class="amd-job">{{stage.stageName}}</span>
                        <span class="amd-job-time">工作量：{{stage.taskHours}}小时</span>
                        <span class="amd-during-time">截止：{{stage.completeTime | formatDate}}</span>
                        <span class="amd-name">{{stage.userName}}</span>
                    </div>
                    <div v-if="stage.status==2">
                        <el-form>
                            <el-form-item label="请评价">
                                <el-radio-group v-model="assessForm.comments[index].grade">
                                    <el-radio class="radio" label="A">A</el-radio>
                                    <el-radio class="radio" label="B">B</el-radio>
                                    <el-radio class="radio" label="C">C</el-radio>
                                </el-radio-group>
                                <el-input type="textarea" v-model="assessForm.comments[index].description"
                                          placeholder="请输入你的评价"></el-input>
                            </el-form-item>
                        </el-form>
                    </div>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                 <el-button @click="hideWaitAssess">取消</el-button>
                <el-button type="primary" @click="taskAssess" v-show="commentStages.length>0">完成</el-button>
          </span>
        </el-dialog>
    </div>
</template>
<script>
    import http from '../lib/Http'
    import moment from 'moment';
    import helper from '../lib/Helper'

    moment.locale('zh-cn');
    export default {
        name: 'TaskItem',
        props: {
            taskItems: Array,
            taskStatus: String,
            isPrivate: Boolean
        },
        data() {
            return {
                loginUserId: '',
                showFinishedTask: false,
                showAuditTask: false,
                showTaskDetail: false,
                showTaskComment: false,
                finishForm: {
                    taskId: '',
                    taskUserId: '',
                    taskType: '',
                    completeHours: '',
                    completeTime: ''
                },
                auditForm: {},
                assessForm: {
                    taskId: '',
                    comments: []
                },
                commentStages: [],
                myStage: {},
                taskDetail: {},
                taskLog: {
                    list: [],
                    hasNextPage: false,
                    pageNum: 1
                }
            };
        },
        created() {
            this.loginUserId = helper.decodeToken().userId
        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole <= 1;
            },
        },
        filters: {
            formatDate: function (value) {
                if (!value) return ''
                return moment(value).format('YYYY-MM-DD HH:mm:ss')
            }
        },
        methods: {
            showFinishedPop(taskId, userId, taskType) {
                this.finishForm.taskId = taskId;
                this.finishForm.taskUserId = userId
                this.finishForm.taskType = taskType
                this.showFinishedTask = true;
            },
            hideFinishedPop() {
                this.resetFinishForm()
                this.showFinishedTask = false;
            },
            showAuditPop(taskId, userId) {
                this.auditForm.taskId = taskId;
                this.auditForm.taskUserId = userId;
                http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    this.taskDetail = resp.data
                })
                this.showAuditTask = true;
            },
            hideAuditPop() {
                this.auditForm.taskId = '';
                this.auditForm.taskUserId = '';
                this.showAuditTask = false;
            },
            // 审核通过任务
            acceptTask() {
                http.zsyPutHttp(`task/auditing/accept/${this.auditForm.taskId}`, {}, (resp) => {
                    this.$message.success("任务审核成功");
                    this.$emit('reload');
                    this.auditForm.taskId = '';
                    this.auditForm.taskUserId = '';
                })
                this.showAuditTask = false;
            },
            // 打回任务
            rejectTask() {
                http.zsyPutHttp(`task/auditing/reject/${this.auditForm.taskId}`, {}, (resp) => {
                    this.$message.success("任务打回成功");
                    this.$emit('reload');
                    this.auditForm.taskId = '';
                    this.auditForm.taskUserId = '';
                })
                this.showAuditTask = false;
            },
            // 完成任务
            finishTask() {
                if (this.finishForm.completeHours == '') {
                    this.$message.warning("请输入实际消耗");
                    return
                }
                if (this.finishForm.completeTime == '') {
                    this.$message.warning("请选择实际完成时间");
                    return
                }
                var param = this.finishForm
                param.completeTime = moment(param.completeTime).format('YYYY-MM-DD HH:mm:ss')
                http.zsyPutHttp('/task/complete', param, (resp) => {
                    this.resetFinishForm()
                    this.showFinishedTask = false;
                    this.$message.success("操作成功");
                    this.$emit('reload');
                })
            },
            resetFinishForm() {
                this.finishForm.taskId = '';
                this.finishForm.taskUserId = ''
                this.finishForm.taskType = ''
                this.finishForm.completeTime = ''
                this.finishForm.completeHours = ''
            },
            hideTaskDetail() {
                this.showTaskDetail = false
                this.taskDetail = {}
                this.taskLog.list = []
                this.taskLog.hasNextPage = false
            },
            taskItemClick(taskId) {
                this.showTaskDetail = !this.isPrivate
                http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    this.taskDetail = resp.data
                });
                this.getTaskLog(taskId)
            },
            getTaskLog(taskId) {
                console.log(taskId)
                http.zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, (resp) => {
                    this.taskLog.list = resp.data.list
                    this.taskLog.hasNextPage = resp.data.hasNextPage
                });
            },
            taskLogMore(taskId) {
                this.taskLog.pageNum += 1
                http.zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, (resp) => {
                    let logs = resp.data.list
                    this.taskLog.list = logs.concat(resp.data.list)
                    this.taskLog.hasNextPage = resp.data.hasNextPage
                });
            },
            completeTask() {
                this.$confirm('此操作将完成该任务, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyPutHttp(`/task/complete/master/${this.taskDetail.id}`, {}, (resp) => {
                        this.$emit('reload');
                        this.$message.success("操作成功");
                        this.hideTaskDetail()
                    })
                }).catch(() => {
                });

            },
            deleteTask() {
                this.$confirm('此操作将删除该任务, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyDeleteHttp(`/task/delete/${this.taskDetail.id}`, {}, (resp) => {
                        this.$emit('reload');
                        this.$message.success("删除成功");
                        this.hideTaskDetail()
                    })
                }).catch(() => {
                });
            },
            showWaitAssess(taskId) {
                let vm = this
                http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    vm.assessForm.taskId = taskId
                    let users = resp.data.users
                    vm.commentStages = users.filter((user) => {
                        return (user.userId != vm.loginUserId && user.status == 2)
                    })
                    vm.myStage = users.filter((user) => {
                        return user.userId == vm.loginUserId
                    })[0]
                    for (let i = 0; i < vm.commentStages.length; i++) {
                        vm.assessForm.comments.push({
                            'taskUserId': vm.commentStages[i].id,
                            'description': '',
                            'grade': ''
                        })
                    }
                })
                this.showTaskComment = true
            },
            hideWaitAssess() {
                this.showTaskComment = false
                this.commentStages = []
                this.myStage = {}
                this.assessForm = {
                    taskId: '',
                    comments: []
                }
            },
            // 评价任务
            taskAssess() {
                http.zsyPostHttp('/task/comment', this.assessForm, (resp) => {
                    this.$message.success("评价成功");
                    this.showTaskComment = false
                    this.commentStages = []
                    this.myStage = {}
                    this.assessForm = {
                        taskId: '',
                        comments: []
                    }
                })
            },
            // 修改任务
            modifyTask(taskId) {
            }
        }
    }
</script>
<style scoped>

    .empty {
        text-align: center;
        font-size: 16px;
        padding: 20px;
    }

    .task-lis {
        background: #fff;
        display: -webkit-flex;
        display: -moz-flex;
        display: -ms-flex;
        display: -o-flex;
        display: flex;
        margin-bottom: 20px;
        margin: 10px 10px 20px;
        cursor: pointer;
    }

    .task-lis:hover {
        box-shadow: 0 0 10px #ccc;
    }

    .head-img {
        width: 60px;
        height: 60px;
        border-radius: 50%;
        overflow: hidden;
        margin: 16px;
    }

    .main-task-detail {
        flex: 1;
        -webkit-flex: 1;
        -moz-flex: 1;
        -ms-flex: 1;
        -o-flex: 1;
    }

    .task-mark {
        line-height: 90px;
        font-size: 18px;
    }

    .task-mark > img, .task-mark > span {
        vertical-align: middle;
    }

    .task-mark > span {
        margin-right: 20px;
        margin-left: 10px;
    }

    .task-name {
        margin-top: 18px;
        font-size: 16px;
    }

    .task-state {
        margin-top: 10px;
    }

    .task-state > span, .task-data-show > span {
        display: inline-block;
        vertical-align: middle;
    }

    .task-end {
        padding: 2px 10px;
        color: #fff;
    }

    .task-end.red {
        background: #FF4515;
    }

    .task-end.orange {
        background: #FF9900;
    }

    .task-end.blue {
        background: #36A8FF;
    }

    .task-end.green {
        background: #339933;
    }

    .task-time-opt {
        color: #36A8FF;
        font-size: 20px;
        margin-left: 16px;
        cursor: pointer;
    }

    .task-data-show {
        margin: 0 40px 0 20px;
    }

    .task-score {
        font-size: 18px;
        line-height: 92px;
    }

    .task-level {
        width: 44px;
        height: 44px;
        line-height: 44px;
        text-align: center;
        border-radius: 50%;
        color: #fff;
        font-size: 20px;
        margin-left: 16px;
    }

    .task-level.first {
        background: #FF9900;
    }

    .task-level.second {
        background: #99CC66;
    }

    .task-level.third {
        background: #999900;
    }

    .task-level.forth {
        background: #9993F1;
    }

    .task-key-tag {
        margin-left: 10px;
        display: inline-block;
        vertical-align: middle;
    }

    .task-key-tag li {
        display: inline-block;
        margin-right: 20px;
    }

    .task-key-lis > span {
        display: inline-block;
        vertical-align: middle;
    }

    .task-key-lis .circle {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        margin-right: 4px;
    }

    .task-key-lis .circle.pink {
        background: #FF9966;
    }

    .task-key-lis .circle.purple {
        background: #D863B0;
    }

    .task-key-lis .circle.red {
        background: #CC0000;
    }

    .task-key-lis .circle.gray {
        background: #999999;
    }

    .task-key-lis .circle.blue {
        background: #0E0E9D;
    }
</style>
<style scoped>
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

    .ctpc-member-list:before {
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

    .ctpc-member-list > span {
        display: inline-block;
        vertical-align: middle;
    }

    .ctpc-member-job {
        width: 110px;
        /*  -webkit-flex: 1;
          -moz-flex: 1;
          -ms-flex: 1;
          -o-flex: 1;
          flex: 1;*/
    }

    .ctpc-member-job-time {
        width: 110px;
    }

    .ctpc-member-end-time {
        /*width: 110px;*/
    }

    .ctpc-member-assess {
        width: 70px;
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

</style>
<style scoped>
    .assess-man-detail {
        background: #fff;
        border: 1px solid #ccc;
        padding: 2px 6px;
        line-height: 36px;
        margin-right: 10px;
    }

    .assess-man-detail > span {
        display: inline-block;
    }

    .amd-job {
        width: 100px;
    }

    .amd-job-time {
        width: 120px;
    }

    .amd-during-time {
        width: 200px;
    }

    .amd-name {
        width: 36px;
        height: 36px;
        text-align: center;
        line-height: 36px;
        border-radius: 50%;
        background: #006699;
        color: #fff;
        font-size: 10px;
    }

    .assess-level-opt > span {
        display: inline-block;
        vertical-align: middle;
        line-height: 36px;
    }

    .assess-msg-inp {
        border: 1px solid #ccc;
        padding: 10px;
        width: 496px;
    }

    .alo-radio > input, .alo-radio > label {
        cursor: pointer;
    }

    /*.assess-task-list{margin-top: 16px;}*/
    /*.assess-task{height: 500px;overflow-y: scroll;}*/
    .assess-task::-webkit-scrollbar {
        width: 10px;
        background-color: #fff;
    }

    .assess-task::-webkit-scrollbar-thumb {
        background-color: rgb(255, 255, 255);
        background-image: -webkit-gradient(linear, 40% 0%, 75% 84%, from(rgb(54, 168, 255)), color-stop(0.6, rgb(54, 229, 255)), to(rgb(54, 192, 255)));
        border-radius: 10px;
    }

    .assess-task::-webkit-scrollbar-track {
        /* -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.1); */
        background-color: #fff;
        border-radius: 10px;
    }
</style>
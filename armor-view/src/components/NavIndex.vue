<template>
    <div class="nav-index-con">
        <div class="my-integral-con" v-show="userRole>0">
            <p class="mic-title">我的积分</p>
            <div class="mic-main clearfix">
                <div class="mic-item fl" v-for="item in integralItem">
                    <div class="mic-item-title" >{{item.label}}</div>
                    <div class="mic-item-title" style="font-size: 15px">({{item.time}})</div>
                    <div class="mic-item-integral">{{item.score}}</div>
                </div>
            </div>
        </div>

        <div class="my-task-con">
            <div v-show="userRole>0">
                <div class="add-task" @click="createTaskClick">
                    <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                    <span>创建个人任务</span>
                </div>
                <p class="mic-title">我的任务</p>
                <div class="my-task-detail">
                    <el-tabs v-model="activeName" @tab-click="handleClick">
                        <el-tab-pane label="进行中" name="doing">
                            <task-item :taskItems="task.doing" :isPrivate="true"
                                       taskStatus="TaskDoing" @reload="reload"></task-item>
                        </el-tab-pane>
                        <el-tab-pane label="已完成" name="completed">
                            <task-item :taskItems="task.finished" :isPrivate="true"></task-item>
                            <div class="pagination" v-show="this.task.finished.length>0">
                                <el-pagination
                                        @current-change="handleFinishedPage"
                                        :current-page.sync="finishedPage.pageNum"
                                        :page-size="finishedPage.pageSize"
                                        :layout="finishedPageLayout"
                                        :total="finishedPage.total">
                                </el-pagination>
                            </div>
                        </el-tab-pane>
                        <!--<el-tab-pane label="审核失败" name="applyFail">
                            <task-item :taskItems="task.applyFail" :isPrivate="true"
                                       taskStatus="ApplyFail" @reload="reload"></task-item>
                        </el-tab-pane>-->
                    </el-tabs>
                </div>
               <div v-show="task.waitAssess.length>0">
                   <p class="mic-title">待评价任务</p>
                   <task-item :taskItems="task.waitAssess" :isPrivate="true" @reload="reload"
                              taskStatus="WaitAssess"></task-item>
               </div>
            </div>
            <div v-show="userRole===0">
                <el-tabs v-model="auditTabsActiveName" @tab-click="handleClick">
                    <el-tab-pane label="待审核" name="wait">
                        <task-item :taskItems="task.waitAudit" :isPrivate="true" @reload="reload"
                                   taskStatus="WaitAuditing"></task-item>
                    </el-tab-pane>
                    <el-tab-pane label="审核通过" name="completed">
                        <task-item :taskItems="task.auditSuccess" :isPrivate="true" @reload="reload"
                                   taskStatus="auditSuccess"></task-item>
                        <div class="pagination" v-show="this.task.auditSuccess.length>0">
                            <el-pagination
                                    @current-change="handleAuditSuccessPage"
                                    :current-page.sync="auditSuccessPage.pageNum"
                                    :page-size="auditSuccessPage.pageSize"
                                    :layout="auditSuccessPageLayout"
                                    :total="auditSuccessPage.total">
                            </el-pagination>
                        </div>
                    </el-tab-pane>
                </el-tabs>
            </div>
        </div>
        <el-dialog
                title="创建个人任务"
                size="tiny"
                custom-class="myDialog"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="createTaskVisible">
            <el-form :model="taskForm" :rules="rules" ref="taskForm" label-width="80px">
                <el-form-item label="项目" prop="projectId">
                    <el-select v-model="taskForm.projectId" placeholder="请选择">
                        <el-option
                                v-for="item in projectList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="截止日期" prop="endTime">
                    <el-date-picker
                            v-model="taskForm.endTime"
                            type="datetime"
                            :picker-options="pickerOptions0"
                            format="yyyy-MM-dd HH:mm"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="工作量" prop="taskHours">
                    <el-input style="width:100px" v-model="taskForm.taskHours" :maxlength="2"></el-input>
                    小时
                </el-form-item>
                <el-form-item label="任务名称" prop="taskName">
                    <el-input v-model="taskForm.taskName"></el-input>
                </el-form-item>

                <el-form-item label="任务描述" prop="description">
                    <el-input type="textarea" v-model="taskForm.description" :rows="3"></el-input>
                </el-form-item>
                <el-form-item label="阶段" prop="stageId">
                    <el-select
                            v-model="taskForm.stageId"
                            filterable
                            default-first-option
                            placeholder="请选择阶段">
                        <el-option
                                v-for="item in stageList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="标签" prop="tags">
                    <el-select
                            v-model="taskForm.tags"
                            multiple
                            placeholder="请选择标签">
                        <el-option
                                v-for="item in tagList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="saveTaskInfo('taskForm')">立即创建</el-button>
            <el-button @click="createTaskVisible = false">取 消</el-button>
          </span>
        </el-dialog>
    </div>
</template>
<script>
    import TaskItem from './TaskItem'
    import Integral from './Intergral.vue'

    import http from '../lib/Http'
    import helper from '../lib/Helper'
    import moment from 'moment';
    import ElTabPane from "../../node_modules/element-ui/packages/tabs/src/tab-pane.vue";

    moment.locale('zh-cn');

    export default {
        name: 'NavIndex',
        data() {
            var validateEmpty = (rule, value, callback) => {
                if (value.trim() == '') {
                    callback(new Error());
                } else {
                    callback();
                }
            };
            return {
                activeName: 'doing',
                auditTabsActiveName: 'wait',
                createTaskVisible: false,
                finishedPage: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                auditSuccessPage: {
                    pageNum: 1,
                    pageSize: 10,
                    total: 0,
                },
                pickerOptions0: {
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7;
                    }
                },
                taskForm: {
                    taskName: '',
                    description: '',
                    projectId: '',
                    endTime: '',
                    priority: 1,
                    tags: [],
                    taskType: 1,
                    taskHours: '',
                    stageId: ''
                },
                rules: {
                    projectId: [
                        {required: true, message: '项目不能为空', trigger: 'change'},
                    ],
                    endTime: [
                        {type: 'date', required: true, message: '截止时间不能为空', trigger: 'change'},
                    ],
                    taskHours: [
                        {required: true, validator: validateEmpty, message: '工作量不能为空', trigger: 'blur'},
                    ],
                    taskName: [
                        {required: true, validator: validateEmpty, message: '任务名称不能为空', trigger: 'blur'},
                    ],
                    description: [
                        {required: true, validator: validateEmpty, message: '描述不能为空', trigger: 'blur'},
                    ],
                    stageId: [
                        {required: true, message: '阶段不能为空', trigger: 'change'},
                    ],
                    tags: [
                        {type: 'array', required: true, message: '请至少选择一个标签', trigger: 'change'},
                    ]
                }, task: {
                    doing: [],
                    finished: [],
                    waitAssess: [],
                    waitAudit: [],
                    auditSuccess: [],
                    applyFail: []
                },
                projectList: [],
                stageList: [],
                tagList: [],
                integralItem: [
                    {
                        label: '本周',
                        score: ''
                    },
                    {
                        label: '本月',
                        score: ''
                    },
                    {
                        label: '年度总积分',
                        score: ''
                    },
                    {
                        label: '季度积分排名',
                        score: ''
                    },
                    {
                        label: '年度积分排名',
                        score: ''
                    }
                ]
            };
        },
        created() {
            this.reload();
        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole <= 1;
            },
            userRole(){
                let userRole = helper.decodeToken().userRole;
                return userRole;
            },
            finishedPageLayout() {
                if (this.task.finished.length > this.finishedPage.pageSize) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            auditSuccessPageLayout() {
                if (this.task.auditSuccess.length > this.auditSuccessPage.pageSize) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            }
        },
        methods: {
            handleClick(tab, event) {
                // 点击进行中和已完成
                console.log(tab, event);
            },
            createTaskClick() {
                // 建个人任务
                this.createTaskVisible = true
            },
            reload() {
                this.fetchIntegral()
                this.fetchTaskDoing()
                this.fetchTaskFinished()
                this.fetchTaskWaitAssess()
                this.fetchTaskWaitAudit()
                this.fetchProjectList()
                this.fetchStageList()
                this.fetchTagList()
                //this.fetchApplyFailTask();
                if (this.userRole === 0) {
                    // 所有审核通过的数据
                    this.fetchTaskAuditSuccess()
                }
            },
            saveTaskInfo(formName) {
                let vm = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let userId = helper.decodeToken().userId;
                        var param = this.taskForm;
                        param.taskName = param.taskName.trim();
                        param.endTime = moment(param.beginTime).format('YYYY-MM-DD HH:mm:ss')
                        var taskUsers = [{
                            userId: userId,
                            taskHours: param.taskHours.trim(),
                            beginTime: moment().format('YYYY-MM-DD HH:mm:ss'),
                            endTime: param.endTime,
                            description: param.description.trim()
                        }];
                        param['taskUsers'] = taskUsers;
                        http.zsyPostHttp('/task/create', param, (resp) => {
                            vm.$message.success('任务创建成功');
                            this.$refs[formName].resetFields();
                            this.createTaskVisible = false
                            vm.reload()
                        });
                    } else {
                        return false;
                    }
                });
            },
            makeUpItems(items) {
                const list = items;
                list.forEach((el) => {
                    let endTime = '';
                    if (el.status >= 2) {
                        endTime = el.completeTime
                    } else if ((el.reviewStatus == 1 || el.reviewStatus == 3) && el.taskUsers[0].status == 1) {
                        endTime = el.endTime
                    } else {
                        endTime = el.taskUsers[0].completeTime
                    }
                    const diffDays = Math.round(moment().diff(moment(endTime), 'days', true))
                    let endColor = '', endText = ''
                    endText = moment(endTime).calendar(null, {
                        sameDay: '[今天]LT',
                        nextDay: '[明天]LT',
                        nextWeek: 'L',
                        lastDay: '[昨天]LT',
                        lastWeek: 'L',
                        sameElse: 'L'
                    })
                    if (el.status < 3 && el.taskUsers[0].status == 1) {
                        if (diffDays == 0) {
                            endColor = 'orange'
                        } else if (diffDays > 0) {
                            endColor = 'red'
                        } else if (diffDays < 0) {
                            endColor = 'blue'
                        }
                        endText += ' 截止'
                    } else {
                        endColor = 'green'
                        endText += ' 完成'
                    }
                    el['endColor'] = endColor
                    el['endText'] = endText
                })
                return list
            },
            fetchIntegral() {
                http.zsyGetHttp(http.API_URI.USERINTEGRAL, null, (res) => {
                    let data = res.data;
                    let items = [];
                    items.push({label: '本周 ', score: data.week,time:this.getDateString('week')});
                    items.push({label: '本月', score: data.month,time:this.getDateString('month')});
                    items.push({label: '本年', score: data.year,time:this.getDateString('year')});
                    items.push({label: '季度积分排名', score: data.quarterRank,time:this.getDateString('quarter')});
                    items.push({label: '年度积分排名', score: data.yearRank,time:this.getDateString('year')});
                    this.integralItem = items;
                })
            },
            // 获取用户正在进行的任务
            fetchTaskDoing() {
                let vm = this
                http.zsyGetHttp('/task/doing', {}, (resp) => {
                    vm.task.doing = this.makeUpItems(resp.data)
                    vm.fetchMyTaskWaitAudit()
                })
            },
            // 获取用户已完成的任务
            fetchTaskFinished() {
                let vm = this
                http.zsyGetHttp(`/task/finished/${vm.finishedPage.pageNum}`, {}, (resp) => {
                    vm.finishedPage.pageNum = resp.data.pageNum
                    vm.finishedPage.total = resp.data.total
                    vm.task.finished = this.makeUpItems(resp.data.list)
                })
            },
            // 获取用户被打回任务
            fetchApplyFailTask() {
                let vm = this
                http.zsyGetHttp('/task/apply/fail', {}, (resp) => {
                    vm.task.applyFail = this.makeUpItems(resp.data)
                })
            },
            // 获取用户待评价的任务
            fetchTaskWaitAssess() {
                let vm = this
                http.zsyGetHttp('/task/waitAssess', {}, (resp) => {
                    vm.task.waitAssess = this.makeUpItems(resp.data)
                })
            },
            // 获取所有待审核的任务
            fetchTaskWaitAudit() {
                let vm = this
                http.zsyGetHttp('/task/pending/all', {}, (resp) => {
                    vm.task.waitAudit = this.makeUpItems(resp.data)
                })
            },
            // 获取所有审核通过的任务
            fetchTaskAuditSuccess() {
                let vm = this
                http.zsyGetHttp(`/task/audit/success/all/${vm.auditSuccessPage.pageNum}`, {}, (resp) => {
                    vm.auditSuccessPage.pageNum = resp.data.pageNum
                    vm.auditSuccessPage.total = resp.data.total
                    vm.task.auditSuccess = this.makeUpItems(resp.data.list)
                })
            },
            // 获取我的待审核任务
            fetchMyTaskWaitAudit() {
                let vm = this
                http.zsyGetHttp('/task/pending', {}, (resp) => {
                    resp.data.forEach((task)=>{
                        task.name +='(待审核)'
                    })
                    vm.task.doing = vm.task.doing.concat(this.makeUpItems(resp.data))
                })
            },
            fetchProjectList() {
                let vm = this
                http.zsyGetHttp('/project/list', {}, (resp) => {
                    vm.projectList = resp.data
                })
            },
            fetchStageList() {
                let vm = this
                http.zsyGetHttp('/stage/list', {}, (resp) => {
                    vm.stageList = resp.data
                })
            },
            fetchTagList() {
                let vm = this
                http.zsyGetHttp('/tag/list', {}, (resp) => {
                    vm.tagList = resp.data
                })
            },
            handleFinishedPage(currentPage){
                this.finishedPage.pageNum = currentPage
                this.fetchTaskFinished()
            },
            handleAuditSuccessPage(currentPage){
                this.auditSuccessPage.pageNum = currentPage
                this.fetchTaskAuditSuccess()
            },
          getDateString(date){//时间期限
            let now = new Date();
            let curMonth = now.getMonth();
            let curYear =  now.getFullYear();;
            let startMonth = 0 ;
            if(date=="month"){//本月的开始结束时间
              return  moment(new Date(curYear, curMonth, 1)).format('YYYY-MM-DD')+"--"+moment(new Date(curYear,curMonth+1,1)-1).format('YYYY-MM-DD');
            }else if(date=="week"){//本季度的开始结束时间
              return  moment(new Date(curYear, curMonth, now.getDate() - now.getDay()+1)).format('YYYY-MM-DD')+"--"+moment(new Date(curYear, curMonth, now.getDate()+( 6 - now.getDay())+1)).format('YYYY-MM-DD');
            }else if(date=="year"){//本年的开始结束时间
              return  moment(new Date(now.getFullYear(),0,1)).format('YYYY-MM-DD')+"--"+moment(new Date(now.getFullYear()+1,0,1)-1).format('YYYY-MM-DD');
            }else if(date=="quarter"){
              if (curMonth >= 1 && curMonth <= 3){
                startMonth = 0;
              }else if (curMonth >= 4 && curMonth <= 6){
                startMonth = 3;
              }else if (curMonth >= 7 && curMonth <= 9){
                startMonth = 6;
              }else if (curMonth >= 10 && curMonth <= 12){
                startMonth = 9;
              }
              return  moment(new Date(curYear, startMonth, 1)).format('YYYY-MM-DD')+"--"+moment(new Date(curYear, startMonth+3,1)-1).format('YYYY-MM-DD');
            }
          }
        },
        components: {
            ElTabPane,
            TaskItem: TaskItem
        },
    }
</script>
<style>
    .myDialog {
        width: 600px;
    }
</style>
<style scoped>

    .pagination {
        margin: 20px 0;
        text-align: right;
    }

    .nav-index-con {
        width: 1100px;
        margin: auto;
    }

    .mic-title {
        font-size: 18px;
        color: #000;
        position: relative;
    }

    .mic-item {
        margin: 18px 12px;
        width: 200px;
        text-align: center;
        box-shadow: 0 0 20px #ccc;
    }

    .mic-item-title {
        line-height: 40px;
        background: #D7D7D7;
        font-size: 20px;
    }

    .mic-item-integral {
        font-size: 26px;
        color: #000;
        background: #fff;
        line-height: 30px;
        padding: 14px 0;
        word-wrap: break-word;
    }

    .mic-item:first-child {
        margin-left: 0;
    }

    .mic-item:last-child {
        margin-right: 0;
    }

    .my-integral-con, .my-task-con {
        margin-top: 16px;
        position: relative;
    }

    .my-task-detail {
        margin-top: 6px;
    }

    .add-task {
        position: absolute;
        right: 20px;
        font-size: 16px;
        cursor: pointer;
        color: #36A8FF;
        z-index: 30;
    }

    .add-task > span {
        display: inline-block;
        vertical-align: middle;
    }

    .add-task-icon {
        width: 22px;
        height: 22px;
        line-height: 22px;
        border-radius: 50%;
        background: #36A8FF;
        color: #fff;
        text-align: center;
        font-size: 14px;
        cursor: pointer;
    }
</style>

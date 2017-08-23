<template>
    <div class="task-con">
        <div class="task-top clearfix">
            <div class="task-top-list fl">
                <span class="ttl-name">项目</span>
                <el-select clearable v-model="form.projectId" placeholder="请选择">
                    <el-option v-for="item in projectList" :key="item.id" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </div>
            <div class="task-top-list fl">
                <span class="ttl-name">成员</span>
                <el-select clearable v-model="form.userId" placeholder="请选择">
                    <el-option v-for="item in userList" :key="item.id" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </div>
            <div class="task-top-list fl">
                <span class="ttl-name">阶段</span>
                <el-select clearable v-model="form.stageId" placeholder="请选择">
                    <el-option v-for="item in stageList" :key="item.id" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </div>
            <div class="task-top-list fl">
                <span class="ttl-name">标签</span>
                <el-select clearable v-model="form.tagId" placeholder="请选择">
                    <el-option v-for="item in tagList" :key="item.id" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </div>
            <div class="task-top-list fl">
                <span class="ttl-name">状态</span>
                <el-select clearable v-model="form.status" placeholder="请选择">
                    <el-option v-for="item in status" :key="item.value" :label="item.name"
                               :value="item.value"></el-option>
                </el-select>
            </div>

            <div class="task-top-list fl">
                <span class="ttl-name">截止日期</span>
                <el-date-picker v-model="timeRange" type="daterange" :picker-options="pickerOptions" placeholder="选择日期"
                                @change="timeChange"></el-date-picker>
            </div>
            <div class="task-top-list fl">
                <el-button type="primary" icon="search" size="small" @click="fetchTaskList()" :loading="loading">查询
                </el-button>
            </div>
            <div class="task-top-list fl creat-task" @click="createTaskClick" v-show="permit">
                <span class="ttl-add-icon">+</span>
                <span class="ttl-add-msg">创建多人任务</span>
            </div>
        </div>
        <div class="task-lis-con">
            <task-item :taskItems="taskItems" :isPrivate="false" @reload="fetchTaskList"></task-item>
            <create-task ref="createTaskPop" @handleFetchTaskList="fetchTaskList"></create-task>
        </div>
        <div class="pagination">
            <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page.sync="page.pageNum"
                    :page-size="page.pageSize"
                    :layout="pageLayout"
                    :total="page.total">
            </el-pagination>
        </div>
    </div>
</template>
<script>
    import CreateTask from './CreateTask'
    import TaskItem from './TaskItem'
    import http from '../lib/Http'
    import helper from '../lib/Helper'
    import moment from 'moment';

    moment.locale('zh-cn');
    export default {
        name: 'Task',
        data() {
            return {
                loading: true,
                timeRange: '',
                projectList: [],
                userList: [],
                stageList: [],
                tagList: [],
                status: [{value: 1, name: '进行中'}, {value: 2, name: '已完成'}],
                taskItems: [],
                page: {
                    pageNum: 0,
                    pageSize: 10,
                    total: 0,
                },
                form: {
                    projectId: '',
                    userId: '',
                    stageId: '',
                    tagId: '',
                    status: '',
                    beginTime: '',
                    endTime: ''
                }, pickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                }
            };
        },
        created() {
            this.fetchProjectList()
            this.fetchUserList()
            this.fetchStageList()
            this.fetchTagList()
            this.fetchTaskList()
            //选中任务tab
            this.$root.eventBus.$emit("handleTabSelected", "task");
        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole <2;
            },
            pageLayout() {
                if (this.taskItems.length > this.page.pageSize) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            }
        },
        methods: {
            handleCurrentChange(currentPage) {
                this.page.pageNum = currentPage
                this.fetchTaskList()
            },
            createTaskClick() {
                // 点击建任务
                this.$refs.createTaskPop.show();
            }, timeChange(time) {
                // 选择结束时间
                time = time.split(' - ')
                if (time && time.length == 2) {
                    this.form.beginTime = `${time[0]} 00:00:00`
                    this.form.endTime = `${time[1]} 23:59:59`
                } else {
                    this.form.beginTime = this.form.endTime = ''
                }
            },
            fetchProjectList() {
                let vm = this
                http.zsyGetHttp('/project/list', {}, (resp) => {
                    vm.projectList = resp.data
                })
            },
            fetchUserList() {
                let vm = this
                http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.userList = resp.data
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
            fetchTaskList() {
                this.loading = true
                this.taskItems = []
                let vm = this
                let param = this.form
                param['pageNum'] = this.page.pageNum || 1
                param['pageSize'] = this.page.pageSize
                http.zsyGetHttp('/task/public/master/all', param, (resp) => {
                    const list = resp.data.list
                    list.forEach((el) => {
                        let endTime = '';
                        if (el.status ==1) {
                            endTime = el.endTime
                        } else {
                            endTime = el.completeTime
                        }
                        const diffDays = moment().diff(moment(endTime), 'days')
                        let endColor = '', endText = ''
                        endText = moment(endTime).calendar(null, {
                            sameDay: '[今天]LT',
                            nextDay: '[明天]',
                            nextWeek: '[下]ddd',
                            lastDay: '[昨天]',
                            lastWeek: '[上]ddd',
                            sameElse: 'L'
                        })
                        if (el.status == 1) {

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
                    vm.loading = false
                    vm.taskItems = resp.data.list
                    vm.page.pageNum = resp.data.pageNum
                    vm.page.pageSize = 10
                    vm.page.total = resp.data.total
                })
            }
        },
        components: {
            TaskItem: TaskItem,
            CreateTask: CreateTask
        }
    }
</script>
<style scoped>
    .pagination {
        margin: 20px 0;
        text-align: right;
    }

    .task-con {
        width: 1100px;
        margin: auto;
    }

    .task-top {
        position: fixed;
        top: 80px;
        width: 1080px;
        left: 50%;
        transform: translateX(-50%);
        background: #fff;
        padding-top: 20px;
        border-radius: 4px;
        box-shadow: 0 0 10px #ccc;
        margin-bottom: 24px;
    }

    .task-top-list {
        margin: 0 20px 20px 20px;
    }

    .task-top-list:last-child {
        margin-top: 16px;
    }

    .task-top-list .el-select {
        width: 148px;
    }

    .ttl-name {
        margin-right: 10px;
        font-size: 14px;
    }

    .div-line {
        margin-left: 10px;
        margin-right: 6px;
    }

    .serch-btn {
        vertical-align: middle;
        margin-left: 10px;
    }

    .creat-task {
        cursor: pointer;
        position: absolute;
        right: 0;
        bottom: 0;
    }

    .creat-task > span {
        display: inline-block;
        vertical-align: middle;
    }

    .ttl-add-msg {
        font-size: 16px;
        color: #36A8FF;
        margin-left: 10px;
    }

    .ttl-add-icon {
        width: 24px;
        height: 24px;
        line-height: 22px;
        text-align: center;
        font-size: 24px;
        border-radius: 50%;
        color: #fff;
        background: #36A8FF;
    }
    .task-lis-con {
        margin-top: 140px;
    }
</style>

<template>
    <div class="task-con">
        <div class="task-top clearfix">
            <div class="task-top-list fl">
                <span class="ttl-name">项目</span>
                <el-select v-model="form.projectId" placeholder="请选择">
                    <el-option v-for="item in projectList" :key="item.id" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </div>
            <div class="task-top-list fl">
                <span class="ttl-name">成员</span>
                <el-select v-model="form.userId" placeholder="请选择">
                    <el-option v-for="item in userList" :key="item.id" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </div>
            <div class="task-top-list fl">
                <span class="ttl-name">阶段</span>
                <el-select v-model="form.stageId" placeholder="请选择">
                    <el-option v-for="item in stageList" :key="item.id" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </div>
            <div class="task-top-list fl">
                <span class="ttl-name">标签</span>
                <el-select v-model="form.tagId" placeholder="请选择">
                    <el-option v-for="item in tagList" :key="item.id" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </div>
            <div class="task-top-list fl">
                <span class="ttl-name">状态</span>
                <el-select v-model="form.status" placeholder="请选择">
                    <el-option v-for="item in status" :key="item.value" :label="item.name"
                               :value="item.value"></el-option>
                </el-select>
            </div>

            <div class="task-top-list fl">
                <span class="ttl-name">截止日期</span>
                <el-date-picker v-model="timeRange" type="daterange" :picker-options="pickerOptions" placeholder="选择日期"
                                @change="timeChange"></el-date-picker>
            </div>
            <div class="task-top-list fl creat-task" @click="createTaskClick">
                <span class="ttl-add-icon">+</span>
                <span class="ttl-add-msg">建任务</span>
            </div>
        </div>
        <div class="task-lis-con">
            <create-task ref="createTaskPop"></create-task>
        </div>
    </div>
</template>
<script>
    import CreateTask from './CreateTask'
    import http from '../lib/Http'
    import moment from 'moment';
    moment.locale('zh-CN');

    export default {
        name: 'Task',
        data() {
            return {
                timeRange: '',
                projectList: [],
                userList: [],
                stageList: [],
                tagList: [],
                status: [{value: 1, name: '进行中'}, {value: 2, name: '已结束'}],
                taskList: [],
                form: {
                    pageSize: 10,
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
        created(){
            console.info('created..')
            console.info(moment('2017-08-16').calendar(null, {
                sameDay: '[今天]',
                nextDay: '[明天]',
                nextWeek: '[下]ddd',
                lastDay: '[昨天]',
                lastWeek: '[上]ddd',
                sameElse: 'L'
            }))
            let diffDays = moment('2017-08-16').diff(moment('2017-08-20'), 'days');
            if (diffDays == 0) {
                console.info('yellow')
            } else if (diffDays > 0) {
                console.info('red')
            } else if (diffDays < 0) {
                console.info('blue')
            }
            this.fetchProjectList()
            this.fetchUserList()
            this.fetchStageList()
            this.fetchTagList()
            this.fetchTaskList()
        },
        methods: {
            createTaskClick() {
                // 点击建任务
                this.$refs.createTaskPop.show();
            }, timeChange() {
                // 选择结束时间
                if (this.timeRange && this.timeRange.length == 2) {
                    this.form.beginTime = this.timeRange[0]
                    this.form.endTime = this.timeRange[1]
                } else {
                    this.form.beginTime = this.form.endTime = ''
                }
            },
            fetchProjectList(){
                let vm = this
                http.zsyGetHttp('/project/list', {}, (resp) => {
                    vm.projectList = resp.data
                })
            },
            fetchUserList(){
                let vm = this
                http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.userList = resp.data
                })
            },
            fetchStageList(){
                let vm = this
                http.zsyGetHttp('/stage/list', {}, (resp) => {
                    vm.stageList = resp.data
                })
            },
            fetchTagList(){
                let vm = this
                http.zsyGetHttp('/tag/list', {}, (resp) => {
                    vm.tagList = resp.data
                })
            },
            fetchTaskList(){
                let vm = this
                http.zsyGetHttp('/task/public/master/all', this.form, (resp) => {
                    vm.taskList = resp.data
                })
            }
        },
        components: {
            CreateTask: CreateTask
        }
    }
</script>
<style scoped>
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
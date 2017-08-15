<template>
    <div class="task-con">
        <div class="task-top clearfix">
            <div class="task-top-list fl">
                <span class="ttl-name">项目</span>
                <el-select v-model="form.projectId" placeholder="请选择">
                    <el-option v-for="item in origin.projectList" :key="item.id" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </div>
            <div class="task-top-list fl">
                <span class="ttl-name">成员</span>
                <el-select v-model="form.userId" placeholder="请选择">
                    <el-option v-for="item in origin.usersList" :key="item.id" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </div>
            <div class="task-top-list fl">
                <span class="ttl-name">阶段</span>
                <el-select v-model="form.stageId" placeholder="请选择">
                    <el-option v-for="item in origin.stageList" :key="item.id" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </div>
            <div class="task-top-list fl">
                <span class="ttl-name">标签</span>
                <el-select v-model="form.tagId" placeholder="请选择">
                    <el-option v-for="item in origin.tagList" :key="item.id" :label="item.name"
                               :value="item.id"></el-option>
                </el-select>
            </div>
            <div class="task-top-list fl">
                <span class="ttl-name">状态</span>
                <el-select v-model="form.status" placeholder="请选择">
                    <el-option v-for="item in origin.status" :key="item.value" :label="item.name"
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
    import TaskList from './TaskList.vue'
    import CreateTask from './CreateTask'

    export default {
        name: 'Task',
        data() {
            return {
                timeRange: '',
                form: {
                    projectId: '',
                    userId: '',
                    stageId: '',
                    tagId: '',
                    status: '',
                    beginTime: '',
                    endTime: ''
                },
                origin: {
                    projectList: [{
                        id: 1,
                        name: "测试"
                    }],
                    usersList: [{
                        id: 1,
                        name: "测试"
                    }],
                    stageList: [{
                        id: 1,
                        name: "测试"
                    }],
                    tagList: [{
                        id: 1,
                        name: "测试"
                    }],
                    status: [{value: 1, name: '进行中'}, {value: 2, name: '已结束'}]
                },
                taskItem: [
                    {
                        taskName: '教师端选题组卷流程优化',
                        taskEnd: '昨天',
                        endMark: '-1',
                        key: [
                            {
                                keyMsg: '教师端'
                            }
                        ]
                    }
                ], pickerOptions: {
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
            }
        },
        components: {
            TaskList: TaskList,
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
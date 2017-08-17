<template>
    <div class="nav-index-con">
        <div class="my-integral-con">
            <p class="mic-title">我的积分</p>
            <div class="mic-main clearfix">
                <div class="mic-item fl" v-for="item in integralItem">
                    <div class="mic-item-title">{{item.label}}</div>
                    <div class="mic-item-integral">{{item.score}}</div>
                </div>
            </div>
        </div>

        <div class="my-task-con">
            <div class="add-task" @click="createTaskClick">
                <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                <span>建任务</span>
            </div>
            <p class="mic-title">我的任务</p>
            <div class="my-task-detail">
                <el-tabs v-model="activeName" @tab-click="handleClick">
                    <el-tab-pane label="进行中" name="doing">
                        <task-item :taskItems="task.doing" :isPrivate="true" taskStatus="TaskDoing"></task-item>
                    </el-tab-pane>
                    <el-tab-pane label="已完成" name="completed">
                        <task-item :taskItems="task.finished" :isPrivate="true"></task-item>
                    </el-tab-pane>
                </el-tabs>
            </div>
            <p class="mic-title">待评价任务</p>
            <task-item :taskItems="task.waitAssess" :isPrivate="true" taskStatus="WaitAssess"></task-item>
            <p class="mic-title">待审核</p>
            <task-item :taskItems="task.waitAudit" :isPrivate="true" taskStatus="WaitAuditing"></task-item>
        </div>
        <create-task ref="createTaskPop"></create-task>
        <finished-task-pop ref="finishedPop"></finished-task-pop>
        <assess-task-pop ref="assessPop"></assess-task-pop>
    </div>
</template>
<script>
    import TaskItem from './TaskItem'
    import CreateTask from './CreateTask'
    import FinishedTaskPop from './FinishedTaskPop'
    import AssessTaskPop from './AssessTaskPop'

    import http from '../lib/Http'

    export default {
        name: 'NavIndex',
        data() {
            return {
                activeName: 'doing',
                integralItem: [
                    {
                        label: '本周',
                        score: '+42'
                    },
                    {
                        label: '本月',
                        score: '+422'
                    },
                    {
                        label: '年度总积分',
                        score: '9000'
                    },
                    {
                        label: '季度积分排名',
                        score: '12'
                    },
                    {
                        label: '年度积分排名',
                        score: '22'
                    }
                ], task: {
                    doing: [],
                    finished: [],
                    waitAssess: [],
                    waitAudit: []
                }
            };
        },
        created(){
            this.fetchIntegral()
            this.fetchTaskDoing()
            this.fetchTaskFinished()
            this.fetchTaskWaitAssess()
            this.fetchTaskWaitAudit()
        },
        methods: {
            handleClick(tab, event) {
                // 点击进行中和已完成
                console.log(tab, event);
            },
            createTaskClick() {
                // 建任务
                this.$refs.createTaskPop.show();
            },
            fetchIntegral(){
                Http.zsyGetHttp(Http.API_URI.USERINTEGRAL,null,(res)=>{
                    let data = res.data;
                    let items=[];
                    items.push({label:'本周',score:'+'+data.week});
                    items.push({label:'本月',score:'+'+data.month});
                    items.push({label:'本年',score:'+'+data.year});
                    items.push({label:'季度积分排名',score:'+'+data.quarterRank});
                    items.push({label:'年度积分排名',score:'+'+data.yearRank});
                    this.integralItem = items;
                })
            },
            // 获取用户正在进行的任务
            fetchTaskDoing(){
                let vm = this
                http.zsyGetHttp('/task/doing', {}, (resp) => {
                    vm.task.doing = resp.data
                })
            },
            // 获取用户已完成的任务
            fetchTaskFinished(){
                let vm = this
                http.zsyGetHttp('/task/finished', {}, (resp) => {
                    vm.task.finished = resp.data
                })
            },
            // 获取用户待评价的任务
            fetchTaskWaitAssess(){
                let vm = this
                http.zsyGetHttp('/task/completed', {}, (resp) => {
                    vm.task.waitAssess = resp.data
                })
            },
            // 获取用户待审核的任务
            fetchTaskWaitAudit(){
                let vm = this
                http.zsyGetHttp('/task/pending', {}, (resp) => {
                    vm.task.waitAudit = resp.data
                })
            },
        },
        components: {
            TaskItem: TaskItem,
            CreateTask: CreateTask,
            FinishedTaskPop: FinishedTaskPop,
            AssessTaskPop: AssessTaskPop
        },
    }
</script>
<style scoped>
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

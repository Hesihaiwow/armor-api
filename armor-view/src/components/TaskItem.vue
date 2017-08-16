<template>
    <div>
        <div class="task-lis" v-for="task in taskItems">
            <div class="head-img"><img src="" alt="" class=""></div>
            <div class="main-task-detail">
                <div class="task-name">{{task.name}}</div>
                <div class="task-state">
                    <span class="task-end" :class="task.endColor">{{task.endText}}</span>
                    <span class="task-time-opt">
                   <i v-show="taskStatus=='TaskDoing'" class="el-icon-circle-check" @click="finishedPop"></i>
                    <i v-show="taskStatus=='WaitAssess'" class="el-icon-star-off" @click="assessPop"></i>
                    <i v-show="taskStatus=='WaitAuditing'" class="el-icon-edit" @click="auditPop"></i>
                  </span>
                    <ul class="task-key-tag">
                        <li class="task-key-lis" v-for="tag in task.tags">
                            <span class="circle" :style="{background:tag.colorValue}"></span>
                            <span class="task-key-msg">{{tag.name}}</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="task-data-show" v-show="isPrivate && taskStatus === 'completed'">
                <span class="task-score">+{{task.userIntegral}}</span>
                <span class="task-level first">{{task.integralGrade}}</span>
            </div>
            <div class="task-mark" v-show="isPrivate">
                <img src="../assets/img/u431.png" alt="">
                <span class="mark-msg">{{task.projectName}}</span>
            </div>
        </div>
    </div>
</template>
<script>
    export default {
        name: 'TaskItem',
        props: {
            taskItems: Array,
            taskStatus: String,
            isPrivate: Boolean
        },
        data() {
            return {};
        },
        created() {
        },
        computed: {},
        methods: {
            finishedPop() {
                this.$emit("showFinishedPop", true);
            },
            assessPop() {
                this.$emit("showAssessPop", true);
            },
            auditPop() {
                this.$emit("showAuditPop", true);
            }
        }
    }
</script>
<style scoped>
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
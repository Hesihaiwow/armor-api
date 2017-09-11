<template>
    <div class="task-board">
        <div class="task-board-list clearfix" :style="{'width':taskBoxWidth}">
            <div class="fl task-list" v-for="(item,key) in stageList" @drop='drop($event)'
                 @dragover='allowDrop($event)'>
                <header :data-id="item.id">{{item.name}} · {{item.tasks ? item.tasks.length : 0}}</header>
                <ul class="task-item" :stageId="item.id">
                    <li class="clearfix" draggable='true' @dragstart='drag($event)'
                        v-for="(task,keyTask) in item.tasks" @click="handleTaskItemClick(task.id)" :taskId="task.id"
                        :createBy="task.createBy">
                        <div class="fl complate" data-title="">
                        </div>
                        <div class="fl task-name">
                            <div>{{task.name}}</div>
                            <span class="tips" :class="task.endColor">{{task.endText}}</span>
                        </div>
                        <div class="master-info fr">
                            {{task.userName}}
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>
<script>
    var dom;
    import moment from 'moment';
    import helper from '../../lib/Helper'

    moment.locale('zh-cn');
    export default {
        name: "task-board",
        data() {
            return {
                stageList: [],
                taskBoxWidth: '1200px'
            }
        },
        computed: {
            loginUserRole() {
                let userRole = helper.decodeToken().userRole;
                return userRole;
            },
            loginUserId() {
                let userId = helper.decodeToken().userId;
                return userId;
            },
        },
        methods: {
            drag: function (event) {
                // 自己创建的任务及超管才可以拖动任务
                let taskCreateBy = event.currentTarget.getAttribute('createby');
                if(this.loginUserId === taskCreateBy || this.loginUserRole===0 ){
                    dom = event.currentTarget;
                }else{
                    event.preventDefault();
                    return;
                }
            },
            drop: function (event) {
                let stageId = '', originId = dom.getAttribute('taskid'), targetId = '', vm = this;
                event.preventDefault();
                if (event.target.tagName.toLowerCase() == "li") {
                    targetId = event.target.getAttribute('taskid');
                } else if (event.target.tagName.toLowerCase() == "ul") {
                    stageId = event.target.getAttribute('stageid');
                } else {
                    let li = this.findParent(event.target);
                    targetId = li.getAttribute('taskid');
                }
                this.http.zsyPutHttp('/task/move', {
                    originId: originId,
                    targetId: targetId,
                    targetStageId: stageId
                }, (res) => {

                    if (event.target.tagName.toLowerCase() == "li") {
                        event.target.parentNode.insertBefore(dom, event.target);
                    } else if (event.target.tagName.toLowerCase() == "ul") {
                        event.target.appendChild(dom);
                    } else {
                        let li = vm.findParent(event.target);
                        li.parentNode.insertBefore(dom, li);
                    }
                    vm.getData();
                });

            },
            allowDrop: function (event) {
                event.preventDefault();
            },
            findParent(obj) {
                while (obj.parentNode && obj.tagName.toLowerCase() != "li") {
                    obj = obj.parentNode;
                }
                return obj;
            },
            handleTaskItemClick(taskId) {
                console.log('emit')
                 this.$root.eventBus.$emit("handleBoardClick2", taskId);
            },
            getData() {
                // 获取阶段
                let that = this;
                that.http.zsyGetHttp('/stage/list', {}, (res) => {
                    that.stageList = res.data;
                    that.taskBoxWidth = that.stageList.length * 270 + "px";
                    that.stageList.forEach((stage) => {
                        // 获取任务
                        that.http.zsyGetHttp(`/task/tasksByStage/${stage.id}`, {}, (res) => {
                            let list = res.data;
                            list.forEach((el) => {
                                let endTime = '', today = moment().format('YYYY-MM-DD')
                                if (el.status == 1) {
                                    endTime = el.endTime
                                } else {
                                    endTime = el.completeTime
                                }
                                endTime = moment(endTime).format('YYYY-MM-DD')
                                const diffDays = moment(today).diff(moment(endTime), 'days')
                                let endColor = '', endText = ''
                                endText = moment(endTime).calendar(null, {
                                    sameDay: '[今天]',
                                    nextDay: '[明天]',
                                    nextWeek: 'L',
                                    lastDay: '[昨天]',
                                    lastWeek: 'L',
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

                                // 优先级样式
                                if (el.priority == 2) {
                                    el.borderClass = 'orange-border'
                                } else if (el.priority == 3) {
                                    el.borderClass = 'red-border'
                                }
                            })
                            that.$set(stage, 'tasks', list)
                        })
                    });
                });
            }
        },
        created() {
            this.getData();
        },
        beforeMount() {
            let vm = this;
            this.$root.eventBus.$on("reloadBoard", () => {
                vm.getData();
            });
        },
        mounted() {
        }
    }
</script>
<style lang="less" scoped>
    .task-board {
        position: fixed;
        top: 120px;
        right: 0;
        bottom: 0;
        left: 0;
        padding: 10px;
        /*width: 1080px;*/
        margin: 0 auto;
        background-color: #fff;
        overflow-x: auto;
        .task-board-list {
            width: 400%;
            height: 100%;
            .task-list {
                position: relative;
                width: 260px;
                margin-right: 10px;
                height: 100%;
                background-color: #eee;
                padding-bottom: 10px;
                box-sizing: border-box;
                header {
                    position: absolute;
                    width: 100%;
                    top: 0;
                    left: 0;
                    padding-left: 10px;
                    line-height: 30px;
                    font-size: 14px;
                    font-weight: bold;
                    background-color: #eee;
                    box-sizing: border-box;
                }
                .task-item {

                    padding: 30px 10px 0;
                    height: 100%;
                    box-sizing: border-box;
                    overflow-y: auto;

                    li {
                        border-bottom: 10px solid #eee;
                        padding: 5px 10px;
                        background-color: #fff;
                        border-radius: 3px;
                        cursor: pointer;
                        .complate {
                            margin-right: 10px;
                            span {
                                display: inline-block;
                                margin-top: 4px;
                                width: 20px;
                                height: 20px;
                                text-align: justify;
                                background-color: #eee;
                                border-radius: 3px;
                            }
                        }
                        .task-name {
                            width: 150px;
                            font-size: 12px;
                            line-height: 18px;
                            word-break: break-all;
                            .tips {
                                padding: 0 8px 2px;
                                /*   background-color: #FFAF38;*/
                                color: #fff;
                                border-radius: 3px;
                            }
                            .tips.red {
                                background: #FF4515;
                            }
                            .tips.orange {
                                background: #FF9900;
                            }
                            .tips.blue {
                                background: #36A8FF;
                            }
                            .tips.green {
                                background: #339933;
                            }

                        }
                        .master-info {
                            /* img {
                                 width: 40px;
                                 height: 40px;
                                 border-radius: 50%;
                             }*/
                            height: 40px;
                            background: #69C8FA;
                            border-radius: 50%;
                            line-height: 40px;
                            text-align: center;
                            color: #fff;
                            cursor: pointer;
                            width: 40px;
                        }
                    }
                    li:last-child {
                        border-bottom: 0;
                    }
                }
            }
            .task-list:last-child {
                margin-right: 0;

            }
        }

    }
</style>
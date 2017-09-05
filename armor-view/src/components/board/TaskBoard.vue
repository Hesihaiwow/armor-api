<template>
    <div class="task-board">
        <div class="task-board-list clearfix" :style="{'width':taskBoxWidth}">
            <div class="fl task-list" v-for="(item,key) in stageList" @drop='drop($event)'
                 @dragover='allowDrop($event)'>
                <header :data-id="item.id">{{item.name}}</header>
                <ul class="task-item">
                    <li class="clearfix" draggable='true' @dragstart='drag($event)'
                        v-for="(task,keyTask) in item.tasks">
                        <div class="fl complate" data-title="">
                            <span></span>
                        </div>
                        <div class="fl task-name">
                            <div>{{task.name}}</div>
                            <span class="tips">{{task.endTime}}</span>
                        </div>
                        <div class="master-info fr">
                            <!--<img src="../../assets/img/man.png" alt="">-->
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>
<script>
    var dom;
    export default {
        name: "task-board",
        data() {
            return {
                stageList: [],
                taskBoxWidth: '1200px'
            }
        },
        methods: {
            drag: function (event) {
                dom = event.currentTarget
                console.log(dom)
            },
            drop: function (event) {
                event.preventDefault();
                if (event.target.tagName.toLowerCase() == "li") {
                    event.target.parentNode.insertBefore(dom, event.target);
                } else if (event.target.tagName.toLowerCase() == "ul") {

                    event.target.appendChild(dom);
                }
            },
            allowDrop: function (event) {
                event.preventDefault();
            }
        },
        created() {
            /* 获取阶段任务 */
            let that = this;
            that.http.zsyGetHttp('/stage/list', {}, (res) => {
                that.stageList = res.data;
                that.taskBoxWidth = that.stageList.length * 270 + "px";
                that.stageList.forEach((stage) => {
                    that.http.zsyPostHttp('/task/public/master/all', {"stageId": [stage.id]}, (res) => {
                        that.$set(stage, 'tasks', res.data.list)
                    })
                });
            });
        },
        mounted() {
            console.log(this.stageList)
        }
    }
</script>
<style lang="less" scoped>
    .task-board {
        padding: 10px;
        width: 1080px;
        margin: 0 auto;
        background-color: #fff;
        overflow-x: auto;
        .task-board-list {
            width: 400%;
            .task-list {
                width: 260px;
                margin-right: 10px;

                background-color: #eee;
                header {
                    padding-left: 10px;
                    line-height: 30px;
                    font-size: 14px;
                    font-weight: bold;
                }
                .task-item {
                    padding: 10px;
                    padding-top: 0;
                    height: 500px;
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
                                background-color: #FFAF38;
                                color: #fff;
                                border-radius: 3px;
                            }
                        }
                        .master-info {
                            width: 40px;
                            img {
                                width: 40px;
                                height: 40px;
                                border-radius: 50%;
                            }
                        }
                    }
                }
            }
            .task-list:last-child {
                margin-right: 0;
            }
        }

    }
</style>
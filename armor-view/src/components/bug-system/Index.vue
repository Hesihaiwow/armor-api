<template>
    <div class="bug-index">
        <div class="title">
            <div class="left fl">
                <span class="name">{{taskName}}</span>
            </div>
            <div class="right fr">
                <el-radio-group v-model="isTaskAll" style="margin-right: 40px;margin-top: -1px;" @change="taskChange">
                    <el-radio-button label="0">当前任务</el-radio-button>
                    <el-radio-button label="1">全部任务</el-radio-button>
                </el-radio-group>
                <el-radio-group v-model="upData.selectAll" style="margin-right: 40px;margin-top: -1px;" @change="rout">
                    <el-radio-button label="0">查看个人</el-radio-button>
                    <el-radio-button label="1">查看全部</el-radio-button>
                </el-radio-group>
                <router-link :to="{ path: '/index/NewBug', query: { taskId: upData.taskId }}"><el-button type="primary">新建BUG</el-button></router-link>
            </div>
        </div>
        <div class="con">
            <div class="bug-msg">
                <div>
                    <router-link :to="{ path: '/index/bug/list', query: { taskId: upData.taskId,listType:1,selectAll:upData.selectAll,taskName:taskName }}" ><span class="color-info">所有的BUG(共 {{num.totalNum}} 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: '/index/bug/list', query: { taskId: upData.taskId,listType:2,selectAll:upData.selectAll,taskName:taskName }}" ><span class="color-danger">待处理的BUG(共 {{num.notSolvedNum}} 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: '/index/bug/list', query: { taskId: upData.taskId,listType:3,selectAll:upData.selectAll,taskName:taskName }}" ><span class="color-warning">待确认的BUG(共 {{num.solvedNum}} 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: '/index/bug/list', query: { taskId: upData.taskId,listType:4,selectAll:upData.selectAll,taskName:taskName}}" ><span class="color-success">已关闭的BUG(共 {{num.closedNum}} 条)</span></router-link>
                </div>
            </div>
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    import http from '../../lib/Http'
    export default {
        name: "Index",
        data(){
            return {
                isTaskAll:0,
                taskName:'',
                upData:{
                    taskId:'',
                    selectAll:'0',
                },
                num:{
                    closedNum: 0,
                    notSolvedNum: 0,
                    solvedNum: 0,
                    totalNum: 0,
                }

            }
        },
        watch:{
            $route(to,from){
                this.getDefaultDatas();
            }
        },
        created() {
            this.getDefaultDatas();
            this.setSessionScreenData();
        },
        methods: {
            getDefaultDatas(){
                this.upData.taskId = this.$route.query.taskId;
                this.taskName = this.$route.query.taskName;

                http.zsyPostHttp('/task-bug/num', this.upData, (res) => {
                    this.num = res.data;
                    // console.log(99)
                },()=>{
                    console.log(11)
                })
                // console.log(66)
            },
            rout(){
                this.$router.push({ path: '/index/bug/list', query: { taskId: this.upData.taskId,listType:1,selectAll:this.upData.selectAll,taskName:this.taskName}});
            },
            // 缓存试卷列表请求数据
            setSessionScreenData() {
                let taskData = {
                    taskId:0,
                    taskName:''
                }
                taskData.taskId = this.$route.query.taskId;
                taskData.taskName = this.$route.query.taskName;
                sessionStorage.taskData = JSON.stringify(taskData);
            },
            taskChange(val){
                console.log(val);
                if(val===1){
                    this.upData.taskId = 0;
                    this.taskName = '全部任务';

                }else {
                    this.upData.taskId =  sessionStorage.taskData.taskId;
                    this.upData.taskName =  sessionStorage.taskData.taskName;
                }
                this.rout();
            }
        },
    }
</script>

<style scoped lang="less">
.bug-index{
    padding: 20px;
    background-color: #fff;
    .color-info{
        color: #909399;
    }
    .color-danger{
        color: #F56C6C;
    }
    .color-warning{
        color: #E6A23C;
    }
    .color-success{
        color: #67C23A;
    }
    .title{
        padding-bottom: 20px;
        border-bottom: 1px solid #eee;
        overflow: hidden;
        .name{
            font-size: 16px;
            line-height: 36px;

        }
    }
    .con{
        margin-top: 50px;
        .bug-msg{
            display: flex;
            margin-bottom: 30px;
            >div{
                flex: 1;
                font-size: 18px;
                line-height: 50px;
                text-align: center;
                background-color: #eee;
                border: 1px solid #ddd;
                &:hover span{
                    color: #20a0ff !important;
                    text-decoration: underline;
                }
                
            }
        }
        .bug-new-list{
            margin-top: 50px;
            .table-box{
                th{
                    text-align: center;
                }
            }
        }
    }
}
</style>
<template>
    <div class="bug-index">
        <div class="title">
            <div class="left fl">
                <span class="name">项目名称</span>
            </div>
            <div class="right fr">
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
                    <router-link :to="{ path: '/index/bug/list', query: { taskId: upData.taskId,listType:1,selectAll:upData.selectAll }}" ><span class="color-info">所有的BUG(共 {{num.totalNum}} 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: '/index/bug/list', query: { taskId: upData.taskId,listType:2,selectAll:upData.selectAll }}" ><span class="color-danger">待处理的BUG(共 {{num.notSolvedNum}} 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: '/index/bug/list', query: { taskId: upData.taskId,listType:3,selectAll:upData.selectAll }}" ><span class="color-warning">待确认的BUG(共 {{num.solvedNum}} 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: '/index/bug/list', query: { taskId: upData.taskId,listType:4,selectAll:upData.selectAll }}" ><span class="color-success">已关闭的BUG(共 {{num.closedNum}} 条)</span></router-link>
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
        created() {
            this.getDefaultDatas();
        },
        methods: {
            getDefaultDatas(){
                this.upData.taskId = this.$route.query.taskId;

                http.zsyPostHttp('/task-bug/num', this.upData, (res) => {
                    this.num = res.data;
                })
            },
            rout(){
                this.getDefaultDatas();
                this.$router.push({ path: '/index/bug/list', query: { taskId: this.upData.taskId,listType:1,selectAll:this.upData.selectAll}});
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
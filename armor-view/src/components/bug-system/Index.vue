<template>
    <div class="bug-index">
        <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane label="我的视图" name="my">
                <My v-if="activeName == 'my'"
                    v-on:edit-bug-id="goEditBug"
                ></My>
            </el-tab-pane>
            <el-tab-pane label="查看问题" name="list">
                <List v-if="activeName == 'list'"
                      v-on:edit-bug-id="goEditBug"></List>
            </el-tab-pane>
            <el-tab-pane label="提交问题" name="add">
                <Add v-if="activeName == 'add'"
                :edit-bug-data="editBugData"
                ></Add>
            </el-tab-pane>
        </el-tabs>
        <!--<div class="title">-->
            <!--<div class="left fl">-->
                <!--<span class="name">{{taskName}}</span>-->
            <!--</div>-->
            <!--<div class="right fr">-->
                <!--<el-radio-group v-model="isTaskAll" style="margin-right: 40px;margin-top: -1px;" @change="taskChange">-->
                    <!--<el-radio-button label="0">当前任务</el-radio-button>-->
                    <!--<el-radio-button label="1">全部任务</el-radio-button>-->
                <!--</el-radio-group>-->
                <!--<el-radio-group v-model="upData.selectAll" style="margin-right: 40px;margin-top: -1px;" @change="rout">-->
                    <!--<el-radio-button label="0">查看个人</el-radio-button>-->
                    <!--<el-radio-button label="1">查看全部</el-radio-button>-->
                <!--</el-radio-group>-->
                <!--<router-link :to="{ path: '/index/NewBug', query: { taskId: upData.taskId }}"><el-button type="primary">新建BUG</el-button></router-link>-->
            <!--</div>-->
        <!--</div>-->
        <!--<div class="con">-->
            <!--<div class="bug-msg">-->
                <!--<div>-->
                    <!--<router-link :to="{ path: '/index/bug/list', query: { taskId: upData.taskId,listType:1,selectAll:upData.selectAll,taskName:taskName }}" ><span class="color-info">所有的BUG(共 {{num.totalNum}} 条)</span></router-link>-->
                <!--</div>-->
                <!--<div>-->
                    <!--<router-link :to="{ path: '/index/bug/list', query: { taskId: upData.taskId,listType:2,selectAll:upData.selectAll,taskName:taskName }}" ><span class="color-danger">待处理的BUG(共 {{num.notSolvedNum}} 条)</span></router-link>-->
                <!--</div>-->
                <!--<div>-->
                    <!--<router-link :to="{ path: '/index/bug/list', query: { taskId: upData.taskId,listType:3,selectAll:upData.selectAll,taskName:taskName }}" ><span class="color-warning">待确认的BUG(共 {{num.solvedNum}} 条)</span></router-link>-->
                <!--</div>-->
                <!--<div>-->
                    <!--<router-link :to="{ path: '/index/bug/list', query: { taskId: upData.taskId,listType:4,selectAll:upData.selectAll,taskName:taskName}}" ><span class="color-success">已关闭的BUG(共 {{num.closedNum}} 条)</span></router-link>-->
                <!--</div>-->
            <!--</div>-->
            <!--<router-view></router-view>-->
        <!--</div>-->
    </div>
</template>

<script>
    import http from '../../lib/Http'
    import My from './My.vue'
    import List from './List.vue'
    import Add from './NewBug.vue'
    export default {
        name: "Index",
        components: {
            My,
            List,
            Add
        },
        data(){
            return {
                activeName: 'my',
                isTaskAll:0,
                taskName:'',
                upData:{
                    taskId:'',
                    selectAll:'0',
                },

                editBugData:{
                    tbId:0,
                    taskId:0,
                    isEdit:false
                },

            }
        },
        watch:{
            $route(to,from){
                this.getDefaultDatas();
            }
        },
        created() {
            this.getDefaultDatas();
            let _this = this;
            //选中组织tab
            _this.$root.eventBus.$emit("handleTabSelected", "bug");
            // this.setSessionScreenData();
        },
        methods: {
            getDefaultDatas(){
                // this.upData.taskId = this.$route.query.taskId;
                // this.taskName = this.$route.query.taskName;

                // http.zsyPostHttp('/task-bug/num', this.upData, (res) => {
                //     this.num = res.data;
                //     // console.log(99)
                // },()=>{
                //     console.log(11)
                // })
                // console.log(66)
            },
            handleClick(tab, event) {
                if(this.activeName == 'add'){
                    this.editBugData.tbId = '';
                    this.editBugData.isEdit = false;
                }
                // console.log(tab, event);
            },
            goEditBug(data){
                this.editBugData.tbId = data.tbId;
                this.editBugData.taskId = data.taskId;
                this.editBugData.isEdit = true;
                this.activeName = 'add';
            },
        },
    }
</script>

<style scoped lang="less">
.bug-index{
    width: 1100px;
    margin: 0 auto;
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
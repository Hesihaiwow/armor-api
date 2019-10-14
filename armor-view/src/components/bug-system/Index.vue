<template>
    <div class="bug-index">
        <div class="title">
            <div class="left fl">
                <span class="name">项目名称</span>
            </div>
            <div class="right fr">
                <el-radio-group v-model="radio1" style="margin-right: 40px;margin-top: -1px;">
                    <el-radio-button label="1">仅看自己</el-radio-button>
                    <el-radio-button label="2">看所有人</el-radio-button>
                </el-radio-group>
                <router-link :to="{ path: '/index/NewBug', query: { id: taskId }}"><el-button type="primary">新建BUG</el-button></router-link>
            </div>
        </div>
        <div class="con">
            <div class="bug-msg">
                <div>
                    <router-link :to="{ path: '/index/bug/list', query: { id: taskId,listType:0 }}" ><span class="color-info">所有的BUG(共 1 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: '/index/bug/list', query: { id: taskId,listType:1 }}" ><span class="color-danger">待处理的BUG(共 1 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: '/index/bug/list', query: { id: taskId,listType:2 }}" ><span class="color-warning">待确认的BUG(共 1 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: '/index/bug/list', query: { id: taskId,listType:3 }}" ><span class="color-success">已关闭的BUG(共 1 条)</span></router-link>
                </div>
            </div>
            <router-view></router-view>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        data(){
            return {
                taskId:'11',
                radio1:'1',
            }
        },
        methods: {

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
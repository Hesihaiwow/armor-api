<template>
    <div class="bug-index">
        <div class="title">
            <div class="left fl">
                <span class="name">项目名称</span>
            </div>
            <div class="right fr">
                <router-link :to="{ path: 'NewBug', query: { id: taskId }}" class="btn-bug"><el-button type="primary">新建BUG</el-button></router-link>
            </div>
        </div>
        <div class="con">
            <div class="bug-msg">
                <div>
                    <router-link :to="{ path: 'NewBug', query: { id: taskId }}" class="btn-bug"><span class="color-info">所有的BUG(共 1 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: 'NewBug', query: { id: taskId }}" class="btn-bug"><span class="color-danger">待处理的BUG(共 1 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: 'NewBug', query: { id: taskId }}" class="btn-bug"><span class="color-warning">待确认的BUG(共 1 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: 'NewBug', query: { id: taskId }}" class="btn-bug"><span class="color-success">已解决的BUG(共 1 条)</span></router-link>
                </div>
            </div>
            <div class="bug-new-list"></div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        data(){
            return {
                taskId:'',
            }
        }
    }
</script>

<style scoped lang="less">
.bug-index{
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
        padding: 20px;
        border-bottom: 1px solid #eee;
        overflow: hidden;
        .name{
            font-size: 16px;
            line-height: 36px;

        }
    }
}
</style>
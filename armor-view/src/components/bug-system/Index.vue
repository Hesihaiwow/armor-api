<template>
    <div class="bug-index">
        <div class="title">
            <div class="left fl">
                <span class="name">项目名称</span>
            </div>
            <div class="right fr">
                <router-link :to="{ path: 'NewBug', query: { id: taskId }}"><el-button type="primary">新建BUG</el-button></router-link>
            </div>
        </div>
        <div class="con">
            <div class="bug-msg">
                <div>
                    <router-link :to="{ path: 'NewBug', query: { id: taskId }}" ><span class="color-info">所有的BUG(共 1 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: 'NewBug', query: { id: taskId }}" ><span class="color-danger">待处理的BUG(共 1 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: 'NewBug', query: { id: taskId }}" ><span class="color-warning">待确认的BUG(共 1 条)</span></router-link>
                </div>
                <div>
                    <router-link :to="{ path: 'NewBug', query: { id: taskId }}" ><span class="color-success">已解决的BUG(共 1 条)</span></router-link>
                </div>
            </div>
            <div class="bug-new-list">
                <el-card class="box-card">
                    <div slot="header" class="clearfix">
                        <span>最新BUG</span>
                    </div>
                    <div class="table-box">
                        <el-table
                                :data="tableData"
                                style="width: 100%"
                                :row-class-name="tableRowClassName">
                            <el-table-column
                                    prop="name"
                                    label="标题">
                            </el-table-column>
                            <el-table-column
                                    prop="type"
                                    label="状态"
                                    width="80">
                            </el-table-column>
                            <el-table-column
                                    prop="give"
                                    label="分配人"
                                    width="120">
                            </el-table-column>
                            <el-table-column
                                    prop="date"
                                    label="时间"
                                    width="180">
                            </el-table-column>
                        </el-table>
                    </div>
                </el-card>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "Index",
        data(){
            return {
                taskId:'11',
                tableData:[
                    {
                        id:1234456,
                        name: 'bug标题',
                        type: 1,
                        give:'王小虎',
                        date: '2016-05-04 10:36:08',
                    },
                ]
            }
        },
        methods: {
            tableRowClassName({row, rowIndex}) {
                if (rowIndex === 1) {
                    return 'warning-row';
                } else if (rowIndex === 3) {
                    return 'success-row';
                }
                return '';
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
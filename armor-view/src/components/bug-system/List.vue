<template>
    <div class="bug-list">
        <el-card class="box-card">
            <!--<div slot="header" class="clearfix">-->
                <!--<span>{{listName}}</span>-->
            <!--</div>-->
            <div class="table-box">
                <el-table
                        :data="tableData.list"
                        style="width: 100%"
                        :row-class-name="tableRowClassName">
                    <el-table-column
                            type="index"
                            label="序号"
                            width="70">
                    </el-table-column>
                    <el-table-column
                            label="标题">
                        <template scope="scope">
                            <router-link :to="{ path: '/index/bug/details', query: { id: scope.row.tbId,taskId:upData.taskId,taskName:taskName }}">{{scope.row.title}}</router-link>

                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="statusName"
                            label="状态"
                            width="80">
                    </el-table-column>
                    <el-table-column
                            label="类型"
                            width="100">
                        <template scope="scope">
                            <span>{{ typeToWords(scope.row.status) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="createName"
                            label="提交人"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="handlerName"
                            label="处理人"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            label="时间"
                            width="180">
                        <template scope="scope">
                            <span>{{ scope.row.createTime | formatDate }}</span>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </el-card>
        <div class="pagination-box">
            <el-pagination
                    background
                    @current-change="handleCurrentChange"
                    layout="prev, pager, next"
                    :total="tableData.total"
                    :page-size="tableData.pageSize">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import http from '../../lib/Http'
    import moment from 'moment';
    moment.locale('zh-cn');
    export default {
        name: "List",
        data(){
            return {
                upData:{
                    taskId:'',
                    selectAll:0,
                    pageNum: 1,
                    status: 1
                },
                listName:'所有的BUG',
                tableData:{
                    total:0,
                    list:[],
                    pageSize:10,
                }
            }
        },
        filters:{
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY/MM/DD HH:mm:ss');
            },
        },
        watch:{
            $route(to,from){
                this.getDefaultDatas();
            }
        },
        created() {
            this.getDefaultDatas();
        },
        methods:{
            getDefaultDatas(){
                this.upData.taskId = this.$route.query.taskId;
                this.upData.status = this.$route.query.listType||1;
                this.upData.selectAll = this.$route.query.selectAll||0;
                this.taskName = this.$route.query.taskName;
                switch (this.upData.status) {
                    case 0:
                        this.listName = '所有的BUG'
                        break
                    case 1:
                        this.listName = '待处理的BUG'
                        break
                    case 2:
                        this.listName = '待确认的BUG'
                        break
                    case 3:
                        this.listName = '已关闭的BUG'
                        break
                }
                this.getList();
            },
            getList(){
                http.zsyPostHttp('/task-bug/page', this.upData, (res) => {
                    this.tableData = res.data;
                })
            },
            // 试卷列表分页
            handleCurrentChange (val) {
                this.upData.pageNum = val;
                this.getList();
            },
            tableRowClassName({row, rowIndex}) {
                if (rowIndex === 1) {
                    return 'warning-row';
                } else if (rowIndex === 3) {
                    return 'success-row';
                }
                return '';
            },
            stateToWords(val) {
                if (val === 1) {
                    return '已分配';
                } else if (val === 2) {
                    return '已解决';
                } else if (val === 3) {
                    return '已关闭';
                } else if (val === 4) {
                    return '打回';
                }
            },
            typeToWords(val) {
                if (val === 1) {
                    return '严重错误';
                } else if (val === 2) {
                    return '主要错误';
                } else if (val === 3) {
                    return '一般错误';
                } else if (val === 4) {
                    return '建议';
                }
            }
        },

    }
</script>

<style scoped>
.pagination-box{
    margin-top: 30px;
    text-align: center;
}
</style>
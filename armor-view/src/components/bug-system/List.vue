<template>
    <div class="bug-list">
        <div class="screen-box">
            <div class="screen-item">
                报告员
                <el-select v-model="upData.reporterId" placeholder="请选择" @change="getList" clearable filterable>
                    <el-option
                            v-for="item in reportUser"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </div>
            <div class="screen-item">
                分配给
                <el-select v-model="upData.handlerId" placeholder="请选择" @change="getList" clearable filterable>
                    <el-option
                            v-for="item in handleUser"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </div>

            <div class="screen-item">
                状态
                <el-select v-model="upData.status" placeholder="请选择" @change="getList" clearable filterable>
                    <el-option
                            v-for="item in selectData.statusName"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </div>
            <div class="screen-item">
                任务
                <el-select v-model="upData.taskId" placeholder="请选择" @change="getList" clearable filterable>
                    <el-option
                            v-for="item in taskList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </div>
            <div class="screen-item">
                严重性
                <el-select v-model="upData.severity" placeholder="请选择" @change="getList" clearable filterable>
                    <el-option
                            v-for="item in selectData.severity"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </div>

        </div>
        <div class="table-box" v-if="tableData.list">
            <el-table
                :data="tableData.list"
                style="width: 100%"
                :row-class-name="tableRowClassName"
                @row-click="goEdit">
                <el-table-column
                        type="index"
                        label="序号"
                        width="70">
                </el-table-column>
                <el-table-column
                        prop="severityName"
                        label="严重性"
                        width="110">
                </el-table-column>
                <el-table-column
                        prop="statusName"
                        label="状态"
                        width="80">
                </el-table-column>
                <el-table-column
                        prop="createName"
                        label="报告员"
                        width="110">
                </el-table-column>
                <el-table-column
                        prop="handlerName"
                        label="分配给"
                        width="110">
                </el-table-column>

                <el-table-column
                        prop="title"
                        label="摘要">
                    <!--<template scope="scope">-->
                        <!--&lt;!&ndash;<router-link :to="{ path: '/index/bug/details', query: { id: scope.row.tbId}}">{{scope.row.title}}</router-link>&ndash;&gt;-->
                    <!--</template>-->
                </el-table-column>
                <el-table-column
                        label="最后更新"
                        width="180">
                    <template scope="scope">
                        <span>{{ scope.row.createTime | formatDate }}</span>
                    </template>
                </el-table-column>

            </el-table>
        </div>
        <div class="pagination-box">
            <el-pagination
                    background
                    @current-change="handleCurrentChange"
                    :current-page.sync="currentPage"
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
                selectData:{
                    statusName:[
                        {
                            id:1,
                            name:'已分派',
                        },
                        {
                            id:2,
                            name:'已解决',
                        },
                        {
                            id:3,
                            name:'已关闭',
                        },
                        {
                            id:4,
                            name:'打回',
                        }
                    ],
                    severity:[
                        {
                            id:1,
                            name:'建议',
                        },
                        {
                            id:2,
                            name:'一般错误',
                        },
                        {
                            id:3,
                            name:'主要错误',
                        },
                        {
                            id:4,
                            name:'严重错误',
                        },
                    ]
                },
                taskList:[],
                reportUser:[],
                handleUser:[],
                upData:{
                    taskId:'',
                    reporterId:'',
                    selectAll:1,
                    pageNum: 1,
                    status: '',
                    severity:'',
                },
                listName:'所有的BUG',
                tableData:{
                    list:[],
                    total:0,
                    pageSize:10,
                },
                currentPage:1
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
                // this.upData.taskId = this.$route.query.taskId;
                // this.upData.status = this.$route.query.listType||1;
                // this.upData.selectAll = this.$route.query.selectAll||0;
                // this.taskName = this.$route.query.taskName;
                http.zsyGetHttp(`/task-bug/task/ready`, {}, (res) => {
                    this.taskList = res.data;
                    // this.upData.taskId = res.data[0].id;
                });
                http.zsyGetHttp(`/task-bug/users/report`, {}, (res) => {
                    this.reportUser = res.data;
                    // this.upData.reporterId = res.data[0].id;
                });
                http.zsyGetHttp(`/task-bug/users/handle`, {}, (res) => {
                    this.handleUser = res.data;
                    // this.upData.reporterId = res.data[0].id;
                });

                this.getList();
            },

            getList(){
                http.zsyPostHttp('/task-bug/page', this.upData, (res) => {
                    this.tableData = res.data;
                })
            },
            goEdit(data){
                // console.log(data,9999)
                this.$emit('edit-bug-id', data);
                // this.$router.push({ path: '/index/bug/list', query: { taskId: this.upData.taskId,listType:1,selectAll:this.upData.selectAll,taskName:this.taskName}});
            },
            // 试卷列表分页
            handleCurrentChange (val) {
                this.upData.pageNum = val;
                this.getList();
            },
            tableRowClassName(row, rowIndex) {
                if (row.status === 1) {
                    return 'bug-type-1';
                } else if (row.status === 2) {
                    return 'bug-type-2';
                }else if (row.status === 3) {
                    return 'bug-type-3';
                }else if (row.status === 4) {
                    return 'bug-type-4';
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

<style scoped lang="less">
    .screen-box{
        margin-bottom: 20px;
        display: flex;
        justify-content: space-between;
        .el-select{
            width: 160px;
        }
    }
.pagination-box{
    margin-top: 30px;
    text-align: center;
}




</style>
<style lang="less">
    .bug-list{
        .bug-type-1{
            background-color: #c2dfff;
            cursor: pointer;
        }
        .bug-type-2{
            background-color: #d2f5b0;
            cursor: pointer;
        }
        .bug-type-3{
            background-color: #c9ccc4;
            cursor: pointer;
        }
        .bug-type-4{
            background-color: #e3b7eb;
            cursor: pointer;
        }
    }
</style>
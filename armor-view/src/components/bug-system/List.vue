<template>
    <div class="bug-list">
        <div class="screen-box">
            <div class="screen-item">
                报告员
                <el-select v-model="upData.reporterId" placeholder="请选择" @change="changeReporter(upData.reporterId)" clearable filterable>
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
                <el-select v-model="upData.handlerId" placeholder="请选择" @change="changeHandler(upData.handlerId)" clearable filterable>
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
                <el-select v-model="upData.status" placeholder="请选择" @change="changeStatus(upData.status)" clearable>
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
                <el-select v-model="upData.taskId" placeholder="请选择" @change="changeTask(upData.taskId)" clearable filterable>
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
                <el-select v-model="upData.severity" placeholder="请选择" @change="changeSeverity(upData.severity)" clearable>
                    <el-option
                            v-for="item in selectData.severity"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </div>
            <div class="screen-item">
                问题类型
                <el-select v-model="upData.problemType" placeholder="请选择" @change="changeProblemType(upData.problemType)" clearable>
                    <el-option
                            v-for="item in selectData.problemTypeList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </div>

        </div>
        <div class="table-box" v-if="tableData.list">
            <el-table
                    :header-cell-style="{background:'#D9D9D9',color:'black'}"
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
                    <!--<template slot-scope="scope">-->
                        <!--&lt;!&ndash;<router-link :to="{ path: '/index/bug/details', query: { id: scope.row.tbId}}">{{scope.row.title}}</router-link>&ndash;&gt;-->
                    <!--</template>-->
                </el-table-column>
                <el-table-column
                        label="最后更新"
                        width="180">
                    <template slot-scope="scope">
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
                    ],
                    problemTypeList:[
                        {id:0,name:'代码问题'},
                        {id:1,name:'设计缺陷'},
                        {id:2,name:'标准规范'},
                        {id:3,name:'界面优化'},
                        {id:4,name:'其他'}
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
                    handlerId:'',
                    problemType:'',
                },
                listName:'所有的BUG',
                tableData:{
                    list:[],
                    total:0,
                    pageSize:10,
                },
                currentPage:1,
                userId:null,
                status:0
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
            this.userId = this.$route.query.userId;
            this.status = this.$route.query.status;
            // console.log('userId: '+this.userId);
            if (this.userId != null && this.userId !== undefined && this.userId !== ''){
                if (this.status != null && this.status !== undefined && this.status !== 0) {
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
                    this.getCustomizedPage(this.userId,this.status,1)
                }
            }else {
                let reporter = window.localStorage.getItem('reporter');
                if (reporter != null && reporter !== undefined && reporter !== ''){
                    this.upData.reporterId = reporter
                }
                let handler = window.localStorage.getItem('handler');
                if (handler != null && handler !== undefined && handler !== ''){
                    this.upData.handlerId = handler
                }
                let status = window.localStorage.getItem('status');
                if (status != null && status !== undefined && status !== ''){
                    this.upData.status = status
                }
                let taskId = window.localStorage.getItem('taskId');
                if (taskId != null && taskId !== undefined && taskId !== ''){
                    this.upData.taskId = taskId
                }
                let severity = window.localStorage.getItem('severity');
                if (severity != null && severity !== undefined && severity !== ''){
                    this.upData.severity = severity
                }
                let problemType = window.localStorage.getItem('problemType');
                if (problemType != null && problemType !== undefined && problemType !== ''){
                    this.upData.problemType = problemType
                }
                this.getDefaultDatas();
            }

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

            //页面跳转之后查询指定bug列表
            getCustomizedPage(userId,status,pageNum){
                http.zsyGetHttp('/task-bug/page/'+userId+'/'+status+'/'+pageNum,{},res=>{
                    this.tableData = res.data;
                })
            },
            changeReporter(reporter){
                window.localStorage.setItem('reporter',reporter);
                this.getList()
            },
            changeHandler(handler){
                window.localStorage.setItem('handler',handler);
                this.getList()
            },
            changeStatus(status){
                window.localStorage.setItem('status',status);
                this.getList()
            },
            changeTask(taskId){
                window.localStorage.setItem('taskId',taskId);
                this.getList()
            },
            changeSeverity(severity){
                window.localStorage.setItem('severity',severity);
                this.getList()
            },
            changeProblemType(type){
                window.localStorage.setItem('problemType',type);
                this.getList()
            },
            getList(){
                this.userId = null;
                this.status = 0;
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
                if (this.userId != null && this.userId !== undefined && this.userId !== ''){
                    if (this.status != null && this.status !== undefined && this.status !== 0) {
                        this.getCustomizedPage(this.userId,this.status,val)
                    }
                }else {
                    this.upData.pageNum = val;
                    this.getList();
                }

            },
            tableRowClassName(row, rowIndex) {
                if (row.row.status === 1) {
                    return 'bug-type-1';
                } else if (row.row.status === 2) {
                    return 'bug-type-2';
                }else if (row.row.status === 3) {
                    return 'bug-type-3';
                }else if (row.row.status === 4) {
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
        /*justify-content: space-between;*/
        flex-wrap: wrap;
        .screen-item{
            margin-bottom: 15px;
            margin-right: 15px;
        }
        .el-select{
            width: 250px;
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
        .table-box{
            tr{
                cursor: pointer;
            }
        }
    }
</style>
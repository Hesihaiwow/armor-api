<template>
    <div class="notice-con">
        <div class="notice-top clearfix">
            <div class="clearfix">
                <div class="notice-top-list fl">
                    <span class="ttl-name"  v-show="!showEveryoneVisible">读取状态:</span>
                    <el-select  v-show="!showEveryoneVisible" clearable filterable no-match-text=" " v-model="readStatus" placeholder="请选择"
                               size="small" style="width:100px;margin-left: -50px">
                        <el-option v-for="item in readStatuses" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                    <el-select v-show="showEveryoneVisible" v-model="userId" clearable filterable  filterable no-match-text=" " placeholder="筛选用户">
                        <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                    <el-date-picker
                            v-model="beginTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="请选择开始时间"
                    >
                    </el-date-picker>
                    <span style="font-size: 14px;color: #606266;">-</span>
                    <el-date-picker
                            v-model="endTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="请选择截止时间"
                    >
                    </el-date-picker>
                </div>
                <el-button type="primary" size="small" @click="select">查询</el-button>
                <el-button v-show="unreadNoticeNum > 0 && !showEveryoneVisible" type="primary" size="small" @click="readAll">标记全部已读</el-button>
                <el-button v-show="userRole == 0 && !showEveryoneVisible" type="primary" size="small" @click="selectEveryoneNotice">查看所有人通知</el-button>
                <el-button v-show="userRole == 0 && showEveryoneVisible" type="primary" size="small" @click="selectMyNotice">查看个人通知</el-button>
            </div>
        </div>
        <el-table :data="noticeData" border width="1200px" v-show="!showEveryoneVisible">
            <el-table-column align="center" label='序号' width="80px">
                <template slot-scope="scope">
                    {{(reqDTO.pageNum-1)*10 + scope.$index + 1}}
                </template>
            </el-table-column>
            <el-table-column property="content" label="内容" align="left">
                <template slot-scope="scope">
                        <span v-if="scope.row.status == 0" style="text-align: left">
                            {{scope.row.content}}
                        </span>
                    <span v-else style="color:gray;">{{scope.row.content}}</span>
                </template>
            </el-table-column>
            <el-table-column property="time" width="200" label="时间" align="center">
                <template slot-scope="scope">
                    <span>{{scope.row.createTime | formatTime}}</span>
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" width="70">
                <template slot-scope="scope">
                    <span v-if="scope.row.status == 0">未读</span>
                    <span v-else>已读</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="100">
                <template slot-scope="scope">
                    <a style="color:#20a0ff;cursor: pointer;"
                       @click="readNotice(scope.row.nid)" v-if="scope.row.status == 0">标记已阅</a>
                </template>
            </el-table-column>
        </el-table>
        <el-table :data="noticeData" border width="1200px" v-show="showEveryoneVisible">
            <el-table-column align="center" label='序号' width="80px">
                <template slot-scope="scope">
                    {{(reqDTO1.pageNum-1)*10 + scope.$index + 1}}
                </template>
            </el-table-column>
            <!--<el-table-column property="no" label="序号" align="center" width="70" type="index"></el-table-column>-->
            <el-table-column property="content" label="内容" align="left">
                <template slot-scope="scope">
                    <span>{{scope.row.content}}</span>
                </template>
            </el-table-column>
            <el-table-column property="time" width="200" label="时间" align="center">
                <template slot-scope="scope">
                    <span>{{scope.row.createTime | formatTime}}</span>
                </template>
            </el-table-column>
            <el-table-column property="noticeUser" width="200" label="通知人" align="center" v-show="showEveryoneVisible">
                <template slot-scope="scope">
                    <span>{{scope.row.noticeUser}}</span>
                </template>
            </el-table-column>
        </el-table>
        <div class="fr" v-show="showEveryoneVisible">
            <el-pagination
                    @current-change="handleCurrentChange1"
                    :current-page.sync="pageNum1"
                    :page-size="noticePage.pageSize"
                    :layout="pageLayout()"
                    :total="noticePage.total">
            </el-pagination>
        </div>
        <div class="fr" v-show="!showEveryoneVisible">
            <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page.sync="pageNum"
                    :page-size="noticePage.pageSize"
                    :layout="pageLayout()"
                    :total="noticePage.total">
            </el-pagination>
        </div>
    </div>
</template>

<script>
    import Http from "../lib/Http";
    import moment from 'moment';
    import helper from "../lib/Helper";
    export default {
        name: "Notice",
        data(){
            return{
                userList:[],
                taskDetail:{},
                taskLog: {
                    list: [],
                    hasNextPage: false,
                    pageNum: 1
                },
                //未读通知
                unreadNoticeData:[],
                //所有通知
                noticeData:[],
                //所有未读通知数量
                unreadNoticeNum:0,
                pageNum:1,
                pageNum1:1,
                reqDTO:{
                    pageNum:1,
                    beginTime:'',
                    endTime:'',
                    readStatus:''
                },
                reqDTO1:{
                    pageNum:1,
                    beginTime:'',
                    endTime:'',
                    userId:''
                },
                noticePage:{
                    pageSize:10,
                    total:0
                },
                readStatus:null,
                userId:null,
                readStatuses: [
                    {id: 0, name: '未读'},
                    {id: 1, name: '已读'},
                ],
                beginTime:'',
                endTime:'',
                showEveryoneVisible:false
            }
        },
        computed:{
            userRole(){
                let userRole = helper.decodeToken().userRole;
                return userRole;
            },
        },
        filters:{
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY/MM/DD');
            },
            formatTime: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD HH:mm:ss');
            }
        },
        created() {
            this.fetchUserList();
            this.fetchUnreadNoticeNum();
            this.fetchAllNotice();
        },
        beforeMount(){
            this.$root.eventBus.$emit("handleTabSelected", "notice");
        },
        methods:{
            fetchUserList() {
                let vm = this
                Http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.userList = resp.data
                })
            },
            showTaskDetails(taskId){
                Http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    this.taskDetail = resp.data
                });
                this.getTaskLog(taskId)
            },
            getTaskLog(taskId) {
                Http.zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, (resp) => {
                    this.taskLog.list = resp.data.list;
                    this.taskLog.hasNextPage = resp.data.hasNextPage;
                });
            },
            //查询最近5条未读通知
            fetchUnreadNotice(){
                Http.zsyGetHttp('/task/notification/un-read',{},(res)=>{
                    this.unreadNoticeData = res.data;
                })
            },
            //查询所有未读数量
            fetchUnreadNoticeNum(){
                Http.zsyGetHttp('/task/notification/un-read/num',{},(res)=>{
                    this.unreadNoticeNum = res.data.count
                    window.localStorage.setItem("unreadNum",res.data.count)
                })
            },
            //查询所有通知
            fetchAllNotice(){
                // this.allShow = true
                Http.zsyPostHttp('/task/notification/all',this.reqDTO,(res)=>{
                    this.noticeData = res.data.list;
                    this.noticePage.total = res.data.total
                })
            },
            //标记当前通知为已读
            readNotice(nid){
                Http.zsyPutHttp('/task/notification/read/'+nid,{},(res) => {
                    this.$message({showClose: true, message: '标记成功', type: 'success'});
                    this.fetchAllNotice()
                    this.fetchUnreadNoticeNum()
                })
            },
            clearReqDTO() {
                this.reqDTO.readStatus = null;
                this.reqDTO.beginTime = null;
                this.reqDTO.endTime = null;
            },
            clearReqDTO1() {
                this.reqDTO1.readStatus = null;
                this.reqDTO1.beginTime = null;
                this.reqDTO1.endTime = null;
                this.reqDTO1.userId = null;
            },
            clearCondition(){
                this.pageNum = 1
                this.pageNum1 = 1
                this.beginTime = null;
                this.endTime = null;
                this.readStatus = null;
                this.userId = null;
            },
            select(){
                //查询我的通知
                if (this.showEveryoneVisible == false){
                    this.clearReqDTO()
                    if (this.beginTime){
                        this.reqDTO.beginTime = moment(this.beginTime).format('YYYY-MM-DD 00:00:00')
                    }
                    if (this.endTime){
                        this.reqDTO.endTime = moment(this.endTime).format('YYYY-MM-DD 23:59:59')
                    }
                    if (this.readStatus != undefined && this.readStatus != null){
                        this.reqDTO.readStatus = this.readStatus
                    }
                    this.fetchAllNotice()
                } else {
                    this.clearReqDTO1();
                    if (this.beginTime){
                        this.reqDTO1.beginTime = moment(this.beginTime).format('YYYY-MM-DD 00:00:00')
                    }
                    if (this.endTime){
                        this.reqDTO1.endTime = moment(this.endTime).format('YYYY-MM-DD 23:59:59')
                    }
                    if (this.userId != undefined && this.userId != null){
                        this.reqDTO1.userId = this.userId;
                    }
                    this.fetchEveryoneNotice()
                }

            },
            //阅读全部
            readAll(){
                this.$confirm('此操作将标记全部已读?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    Http.zsyPutHttp(`/task/notification/read-all`, {}, (resp) => {
                        this.$message({showClose: true, message: '标记成功', type: 'success'});
                        this.fetchUnreadNoticeNum();
                        this.fetchAllNotice();
                    })
                    // window.history.go(0)
                }).catch(() => {
                });
            },
            selectEveryoneNotice(){
                this.showEveryoneVisible = true;
                this.clearCondition();
                this.clearReqDTO1();
                // if (this.beginTime){
                //     this.reqDTO1.beginTime = moment(this.beginTime).format('YYYY-MM-DD 00:00:00')
                // }
                // if (this.endTime){
                //     this.reqDTO1.endTime = moment(this.endTime).format('YYYY-MM-DD 23:59:59')
                // }
                this.fetchEveryoneNotice()
            },
            selectMyNotice(){
                this.showEveryoneVisible = false;
                this.clearCondition();
                this.clearReqDTO();
                // if (this.beginTime){
                //     this.reqDTO1.beginTime = moment(this.beginTime).format('YYYY-MM-DD 00:00:00')
                // }
                // if (this.endTime){
                //     this.reqDTO1.endTime = moment(this.endTime).format('YYYY-MM-DD 23:59:59')
                // }
                this.fetchAllNotice()
            },
            fetchEveryoneNotice(){
                Http.zsyPostHttp('/task/notification/everyone/all',this.reqDTO1,(res)=>{
                    this.noticeData = res.data.list
                    this.noticePage.total = res.data.total
                })
            },
            handleCurrentChange(currentPage) {
                this.reqDTO.pageNum = currentPage
                this.fetchAllNotice()
            },
            handleCurrentChange1(currentPage) {
                this.reqDTO1.pageNum = currentPage
                this.fetchEveryoneNotice()
            },
            pageLayout() {
                if (this.noticePage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
        }
    }
</script>

<style scoped>
    .notice-con {
        width: 1100px;
        margin: auto;
    }

    .notice-top {
        background: #fff;
        padding-top: 20px;
        border-radius: 4px;
        box-shadow: 0 0 10px #ccc;
        margin-bottom: 24px;
    }

    .notice-top-list {
        margin: 0 20px 5px 20px;
    }

    .ttl-name {
        margin-right: 50px;
        font-size: 14px;
    }
</style>

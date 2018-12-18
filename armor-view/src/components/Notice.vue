<template>
    <div class="notice-con">
        <div class="notice-top clearfix">
            <div class="clearfix">
                <div class="notice-top-list fl">
                    <span class="ttl-name">读取状态:</span>
                    <el-select clearable filterable no-match-text=" " v-model="readStatus" placeholder="请选择"
                               size="small" style="width:100px;margin-left: -50px">
                        <el-option v-for="item in readStatuses" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                    <!--<span class="ttl-name">通知时间:</span>-->
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
            </div>
        </div>
        <el-table :data="noticeData" border width="1200px">
            <el-table-column property="no" label="序号" align="center" width="70" type="index"></el-table-column>
            <el-table-column property="content" label="内容" align="center">
                <template scope="scope">
                        <span v-if="scope.row.status == 0" style="color:orangered;">
                            {{scope.row.content}}
                        </span>
                    <span v-else style="color:gray;">{{scope.row.content}}</span>
                </template>
            </el-table-column>
            <el-table-column property="time" width="200" label="时间" align="center">
                <template scope="scope">
                    <span>{{scope.row.createTime | formatTime}}</span>
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" width="70">
                <template scope="scope">
                    <span v-if="scope.row.status == 0">未读</span>
                    <span v-else>已读</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="100">
                <template scope="scope">
                    <a style="color:#20a0ff;cursor: pointer;"
                       @click="readNotice(scope.row.nid)" v-if="scope.row.status == 0">标记已阅</a>
                </template>
            </el-table-column>
        </el-table>
        <div class="fr">
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
    export default {
        name: "Notice",
        data(){
            return{
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
                reqDTO:{
                    pageNum:1
                },
                noticePage:{
                    pageSize:10,
                    total:0
                },
                readStatus:'',
                readStatuses: [
                    {id: 0, name: '未读'},
                    {id: 1, name: '已读'},
                ],
                beginTime:'',
                endTime:'',
            }
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
            this.fetchUnreadNoticeNum()
            this.fetchUnreadNotice()
            this.fetchAllNotice()
        },
        methods:{
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
                })
            },
            //查询所有通知
            fetchAllNotice(){
                this.allShow = true
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
            select(){
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
            },
            handleCurrentChange(currentPage) {
                this.reqDTO.pageNum = currentPage
                this.fetchAllNotice()
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

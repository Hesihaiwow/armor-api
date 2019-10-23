<template>
    <div class="base-page">
        <div style="float: right;margin-right: 20px;margin-bottom: 10px">
            <el-button type="primary" @click="goBack">返回</el-button>
        </div>
        <div style="float: right;margin-right: 20px">
            <el-button type="primary" @click="addRestHoursLog">手动新增调休记录</el-button>
        </div>

        <el-table :data="userRestHoursLogData" border>
            <el-table-column type="index" label="序号" align="center" width="80">
                <template scope="scope">
                    {{(userRestHoursLogPage.pageNum-1)*10 + scope.$index + 1}}
                </template>
            </el-table-column>
            <el-table-column prop="userName" label="用户" align="center" width="100"></el-table-column>
            <el-table-column prop="restHours" label="时长" align="center" width="100"></el-table-column>
            <el-table-column prop="content" label="事由" align="center"></el-table-column>
            <el-table-column  label="参考时间"  width="165"  align="center">
                <template scope="scope">
                    <div type="text" size="small" v-if="scope.row.type === 2">{{scope.row.leaveTimeStr}}</div>
                    <div type="text" size="small" v-if="scope.row.type === 3">{{scope.row.checkTimeStr}}</div>
                    <div type="text" size="small" v-if="scope.row.type === 4">{{scope.row.eworkTimeStr}}</div>
                </template>
            </el-table-column>
            <el-table-column prop="recordTime" label="记录日期"  width="120"  align="center">
                <template scope="scope">
                    <div type="text" size="small" >{{scope.row.recordTime | formatDate}}</div>
                </template>
            </el-table-column>
            <el-table-column prop="typeName" label="类型" width="120" align="center"></el-table-column>
            <el-table-column label="操作" width="150" align="center" v-if="userRole === 0">
                <template scope="scope">
                    <el-button type="text" size="small" @click="editLog(scope.row)">编辑</el-button>
                    <el-button type="text" size="small" @click="deleteLog(scope.row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagination">
            <el-pagination
                    @current-change="userRestHoursLogHandleCurrentChange"
                    :current-page.sync="userRestHoursLogPage.pageNum"
                    :page-size="userRestHoursLogPage.pageSize"
                    :layout="userRestHoursLogsPageLayout"
                    :total="userRestHoursLogPage.total">
            </el-pagination>
        </div>

        <el-dialog  title="新增个人加班/调休记录"  size="tiny"  :close-on-click-modal="false"
                    :close-on-press-escape="false" :visible.sync="addRestHoursVisible"
                    @close="cancelAddRestHoursLog">
            <el-form ref="userRestHoursLogForm" label-width="80px">
                <el-form-item label="用户 ">
                    <span>{{userRestHoursLogReqDTO.userName}}</span>
                </el-form-item>
                <el-form-item label="调休加减" prop="restHour">
                    <el-input v-model="userRestHoursLogForm.restHour" type="number" :maxlength="5"></el-input>
                </el-form-item>
                <el-form-item label="调休备注" prop="content">
                    <el-input type="textarea" v-model="userRestHoursLogForm.content" :rows="3"></el-input>
                </el-form-item>
                <el-form-item label="录入日期" prop="recordTime">
                    <el-date-picker
                            v-model="userRestHoursLogForm.recordTime"
                            type="date"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd HH:mm:00"
                            placeholder="选择录入日期">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" :loading="restHourLoading" @click="saveAddRestHoursLog">立即创建</el-button>
                <el-button @click="cancelAddRestHoursLog" type="error">取 消</el-button>
            </span>
        </el-dialog>
        <el-dialog  title="编辑个人加班/调休记录"  size="tiny"  :close-on-click-modal="false"
                    :close-on-press-escape="false" :visible.sync="editRestHoursVisible"
                    @close="cancelEditRestHoursLog">
            <el-form  ref="editRestHoursLogForm" label-width="80px">
                <el-form-item label="用户 ">
                    <span>{{userRestHoursLogReqDTO.userName}}</span>
                </el-form-item>
                <el-form-item label="调休加减" prop="restHour">
                    <el-input v-model="editRestHoursLogForm.restHour" type="number" :maxlength="5"></el-input>
                </el-form-item>
                <el-form-item label="调休备注" prop="content">
                    <el-input type="textarea" v-model="editRestHoursLogForm.content" :rows="3"></el-input>
                </el-form-item>
                <el-form-item label="录入日期" prop="recordTime">
                    <el-date-picker
                            v-model="editRestHoursLogForm.recordTime"
                            type="date"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd HH:mm:00"
                            placeholder="选择录入日期">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" :loading="restHourLoading" @click="saveEditRestHoursLog">立即创建</el-button>
                <el-button @click="cancelEditRestHoursLog" type="error">取 消</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    import Http from '../lib/Http';
    import moment from 'moment';
    import helper from '../lib/Helper'
    export default {
        name: "UserRestHoursPage",
        data() {
            return {
                userRestHoursLogData:[],
                userRestHoursLogPage:{
                    pageNum: 1,
                    pageSize: 10,
                    total: 0
                },
                userRestHoursLogReqDTO:{
                    userId:'',
                    userName:'',
                    pageNum:1
                },
                userRestHoursLogForm:{
                    restHour:0,
                    content:'',
                    userId:null,
                    recordTime:null
                },
                editRestHoursLogForm:{
                    logId:'',
                    restHour:0,
                    content:'',
                    recordTime:null
                },
                editRestHoursVisible: false,
                addRestHoursVisible: false,
                restHourLoading: false,
            }
        },
        created(){
            let userId = this.$route.query.userId;
            let userName = this.$route.query.userName;
            this.showUserRestHoursLog(userId,userName)
        },
        methods:{
            userRestHoursLogHandleCurrentChange(currentPage){
                this.userRestHoursLogReqDTO.pageNum = currentPage;
                this.showUserRestHoursLog(this.userRestHoursLogReqDTO.userId,this.userRestHoursLogReqDTO.userId);
            },
            showUserRestHoursLog(userId,userName){
                if (userId != null && userId !== undefined && userId !== '') {
                    this.userRestHoursLogReqDTO.userId = userId;
                    this.userRestHoursLogReqDTO.userName = userName;
                    Http.zsyPostHttp('/sign-in/rest-hours-log/page',this.userRestHoursLogReqDTO,res=>{
                        this.userRestHoursLogData = res.data.list;
                        this.userRestHoursLogPage.total = res.data.total;
                    })
                }else {
                    this.$message({
                        showClose: true,
                        message: '请选择用户',
                        type: 'warning'
                    });
                }
            },
            //手动 新增调休记录
            addRestHoursLog(){
                this.addRestHoursVisible = true;
            },
            //取消添加
            cancelAddRestHoursLog(){
                this.userRestHoursLogForm.userId = null;
                this.userRestHoursLogForm.recordTime = null;
                this.userRestHoursLogForm.restHour = 0;
                this.userRestHoursLogForm.content = '';
                this.addRestHoursVisible = false;
                this.restHourLoading = false;
            },
            cancelEditRestHoursLog(){
                this.editRestHoursLogForm.logId = '';
                this.editRestHoursLogForm.recordTime = null;
                this.editRestHoursLogForm.restHour = 0;
                this.editRestHoursLogForm.content = '';
                this.editRestHoursVisible = false;
                this.restHourLoading = false;
            },
            //保存修改调休日志
            saveAddRestHoursLog(){
                this.restHourLoading = true;
                this.userRestHoursLogForm.userId = this.userRestHoursLogReqDTO.userId;
                if (this.userRestHoursLogForm.userId == null || this.userRestHoursLogForm.userId === undefined
                    || this.userRestHoursLogForm.userId === ''){
                    this.$message({
                        showClose: true,
                        message: '请选择用户',
                        type: 'warning'
                    });
                    this.restHourLoading = false;
                    return false;
                }
                if (this.userRestHoursLogForm.restHour == null || this.userRestHoursLogForm.restHour === undefined
                    || this.userRestHoursLogForm.restHour === ''){
                    this.$message({
                        showClose: true,
                        message: '请填写调休时长',
                        type: 'warning'
                    });
                    this.restHourLoading = false;
                    return false;
                }
                if (this.userRestHoursLogForm.restHour > 999 || this.userRestHoursLogForm.restHour < -999) {
                    this.$message({showClose: true, message: '调休时长正确值应为-999~999', type: 'warning'});
                    this.restHourLoading = false;
                    return false;
                }
                if (this.userRestHoursLogForm.content == null || this.userRestHoursLogForm.content === undefined
                    || this.userRestHoursLogForm.content.trim() === ''){
                    this.$message({
                        showClose: true,
                        message: '请填写事由',
                        type: 'warning'
                    });
                    this.restHourLoading = false;
                    return false;
                }
                if (this.userRestHoursLogForm.recordTime == null || this.userRestHoursLogForm.recordTime === undefined) {
                    this.$message({
                        showClose: true,
                        message: '请选择录入时间',
                        type: 'warning'
                    });
                    this.restHourLoading = false;
                    return false;
                }
                this.userRestHoursLogForm.recordTime = moment(this.userRestHoursLogForm.recordTime).format("YYYY-MM-DD HH:mm:00");
                Http.zsyPostHttp('/sign-in/rest-hours-log/add',this.userRestHoursLogForm,res=>{
                    if (res.errMsg == "执行成功"){
                        this.$message({
                            showClose: true,
                            message: '修改成功',
                            type: 'success'
                        });
                        this.restHourLoading = false;
                        this.addRestHoursVisible = false;
                        this.showUserRestHoursLog(this.userRestHoursLogForm.userId,this.userRestHoursLogReqDTO.userName)
                    }else {
                        this.restHourLoading = false;
                        this.addRestHoursVisible = false;
                    }
                },err=>{
                    this.$message({
                        showClose: true,
                        message: err.errMsg,
                        type: 'error'
                    });
                    this.restHourLoading = false;
                },error=>{
                    this.restHourLoading = false;
                })
            },
            saveEditRestHoursLog(){
                this.restHourLoading = true;
                this.editRestHoursLogForm.userId = this.userRestHoursLogReqDTO.userId;

                if (this.editRestHoursLogForm.restHour == null || this.editRestHoursLogForm.restHour === undefined
                    || this.editRestHoursLogForm.restHour === ''){
                    this.$message({
                        showClose: true,
                        message: '请填写调休时长',
                        type: 'warning'
                    });
                    this.restHourLoading = false;
                    return false;
                }
                if (this.editRestHoursLogForm.restHour > 999 || this.editRestHoursLogForm.restHour < -999) {
                    this.$message({showClose: true, message: '调休时长正确值应为-999~999', type: 'warning'});
                    this.restHourLoading = false;
                    return false;
                }
                if (this.editRestHoursLogForm.content == null || this.editRestHoursLogForm.content === undefined
                    || this.editRestHoursLogForm.content.trim() === ''){
                    this.$message({
                        showClose: true,
                        message: '请填写事由',
                        type: 'warning'
                    });
                    this.restHourLoading = false;
                    return false;
                }
                if (this.editRestHoursLogForm.recordTime == null || this.editRestHoursLogForm.recordTime === undefined) {
                    this.$message({
                        showClose: true,
                        message: '请选择录入时间',
                        type: 'warning'
                    });
                    this.restHourLoading = false;
                    return false;
                }
                this.editRestHoursLogForm.recordTime = moment(this.editRestHoursLogForm.recordTime).format("YYYY-MM-DD HH:mm:00");
                Http.zsyPutHttp('/sign-in/rest-hours-log/update',this.editRestHoursLogForm,res=>{
                    if (res.errMsg == "执行成功"){
                        this.$message({
                            showClose: true,
                            message: '修改成功',
                            type: 'success'
                        });
                        this.restHourLoading = false;
                        this.editRestHoursVisible = false;
                        this.showUserRestHoursLog(this.editRestHoursLogForm.userId,this.userRestHoursLogReqDTO.userName)
                    }else {
                        this.restHourLoading = false;
                        this.editRestHoursVisible = false;
                    }
                },err=>{
                    this.$message({
                        showClose: true,
                        message: err.errMsg,
                        type: 'error'
                    });
                    this.restHourLoading = false;
                },error=>{
                    this.restHourLoading = false;
                })
            },
            goBack(){
                this.$router.go(-1)
            },
            deleteLog(id){
                this.$confirm('删除调休记录, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    Http.zsyDeleteHttp('sign-in/rest-hours-log/delete/'+id,{},(res)=>{
                        if (res.data){
                            this.$message({
                                showClose: true,
                                message: '删除成功',
                                type: 'success'
                            });
                            this.showUserRestHoursLog(this.userRestHoursLogReqDTO.userId,this.userRestHoursLogReqDTO.userName)
                            // this.$router.go(0)
                        }
                    })
                }).catch(() => {
                });
            },
            editLog(log){
                this.editRestHoursLogForm.logId = log.id;
                this.editRestHoursLogForm.content = log.content;
                this.editRestHoursLogForm.recordTime = log.recordTime;
                this.editRestHoursLogForm.restHour = log.restHours;
                this.editRestHoursVisible = true;
            }
        },
        computed:{
            userRestHoursLogsPageLayout() {
                if (this.userRestHoursLogPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            userRole() {
                let userRole = helper.decodeToken().userRole;
                return userRole;
            },
        },
        filters:{
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD');
            },
            formatDate1: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY年MM月DD日');
            },
            formatDate2: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY/MM/DD');
            },
            formatTime: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD HH:mm:ss');
            },
            formatTime2: function (value) {
                if (!value) return '';
                return moment(value).format('HH:mm:ss');
            },
        }
    }
</script>

<style scoped>
    .base-page {
        width: 1300px;
        font-size: 14px;
        background: #fff;
        padding: 30px 0;
        box-shadow: 0 0 10px #ccc;
        margin: auto;
    }
    .base-page .pagination {
        margin: 20px 0;
        text-align: right;
    }
    .right {
        margin-left: 20px;
        width: 60%;
    }
</style>
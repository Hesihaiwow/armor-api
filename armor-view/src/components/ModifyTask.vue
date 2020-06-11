<template>
    <div class="create-task-pop" v-show="showModifyTask">
        <div class="create-task-pop-con">
            <div class="ctpc-top clearfix">
                <span class="fl">编辑任务</span><span class="ctpc-top-close fr" @click="hide">×</span>
            </div>
            <div class="ctpc-con">
                <div class="ctpc-instruction-msg" v-show="!showDesc" @click="showInsChange">
                    {{insMsgShow}}
                </div>
                <div class="ctpc-ins-edit" v-show="showDesc">
                    <textarea class="ctpc-instruction" placeholder="添加备注" v-model="taskForm.description"></textarea>
                    <div class="ctpc-btns">
                        <input type="button" class="ctpc-cancel" @click="cancelEdit" value="取消">
                        <input type="button" class="ctpc-save" @click="saveEdit" value="确定">
                    </div>
                </div>
                <div class="ctpc-main-con">
                    <div class="ctpc-list clearfix">
                        <div class="ctpc-list-menu fl">任务名称</div>
                        <div class="ctpc-list-con fl">
                            <el-input type="text" v-model="taskForm.taskName" auto-complete="off"></el-input>
                        </div>
                    </div>
                    <div class="ctpc-list clearfix">
                        <div class="ctpc-list-menu fl">项目</div>
                        <div class="ctpc-list-con fl">
                            <el-select v-model="taskForm.projectId" placeholder="请选择">
                                <el-option v-for="item in projectList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                    </div>
                    <div class="ctpc-list clearfix">
                        <div class="ctpc-list-menu fl">截止日期</div>
                        <div class="ctpc-list-con fl">
                            <el-date-picker v-model="taskForm.endTime" type="datetime" placeholder="选择日期"
                                            :picker-options="pickerOptions0"></el-date-picker>
                        </div>
                    </div>
                    <div class="ctpc-list clearfix">
                        <div class="ctpc-list-menu fl">标签</div>
                        <div class="ctpc-list-con fl">
                            <ul class="ctpc-tags">
                                <li class="tag-lis-add">
                                    <span class="tag-lis-add-msg" @click="addTag">添加标签</span>
                                    <div class="tag-add-sel" v-show="showAddTag">
                                        <!--<div class="add-tag-btn" @click="addSelTag">添加</div>-->
                                        <el-select v-model="taskForm.tags" multiple filterable
                                                   default-first-option placeholder="添加标签">
                                            <el-option v-for="item in tagList" :key="item.id" :label="item.name"
                                                       :value="item.id"></el-option>
                                        </el-select>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="ctpc-member-con">
                        <div class="ctpc-member-list clearfix" v-for="(item,index) in taskUsers">
                            <span class="fl ctpc-member-head">{{item.userName}}</span>
                            <span class="fl ctpc-member-job ellipsis">{{item.stageName}}</span>
                            <span class="fl ctpc-member-job-time">工作量:{{item.taskHours}}工时</span>
                            <span class="fl ctpc-member-end-time">截止:{{item.endTime}}</span>
                            <!--<span class="fl ctpc-member-assess" v-show="showAssess">评价：{{list.jobLevel}}</span>-->
                            <span class="fl ctpc-member-delete" @click="deleteMember(index)">×</span>
                        </div>
                        <div class="bdl-line"></div>
                    </div>
                    <div class="ctpc-add-member-detail" v-if="showAddDetail">
                        <el-input type="textarea" placeholder="描述该成员的工作内容..." v-model="step.description"
                                  :rows="3"></el-input>
                        <!--<textarea class="add-member-remark" placeholder="备注"></textarea>-->
                        <div class="add-member-basic">
                            <div class="add-member-basic-list add-member-stage clearfix">
                                <div class="add-member-basic-menu fl">阶段：</div>
                                <!-- <div class="add-member-basic-msg add-stage-opt fl" @click="addStage">添加阶段</div> -->
                                <ul class="add-member-basic-msg add-stage-opt ctpc-tags fl">
                                    <!--<li class="ctpc-tag-lis" v-show="valueStage!==''">
                                        <span class="tag-lis-circle"></span>
                                        <span class="tag-lis-msg">{{valueStage}}</span>
                                        <span class="tag-lis-delete" @click="deleteStageTag">×</span>
                                    </li>-->
                                    <li class="tag-lis-add">
                                        <!--<span class="tag-lis-add-msg" @click="addStage">阶段</span>-->
                                        <el-select v-model="step.stageId" filterable :multiple-limit="1"
                                                   @change="stepStageChange"
                                                   default-first-option placeholder="请选择">
                                            <el-option v-for="item in stageList" :key="item.id"
                                                       :label="item.name" :value="item.id"></el-option>
                                        </el-select>
                                    </li>
                                </ul>
                            </div>
                            <div class="add-member-basic-list clearfix">
                                <div class="add-member-basic-menu fl">负责人：</div>
                                <div class="add-member-basic-msg fl">
                                    <el-select v-model="step.userId" placeholder="请选择" @change="stepUserChange">
                                        <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                                   :value="item.id"></el-option>
                                    </el-select>
                                </div>
                                <div class="add-member-basic-menu add-member-basic-time fl">工作量：</div>
                                <div class="add-member-basic-msg fl">
                                    <input class="member-time-count" v-model="step.taskHours">工时
                                </div>
                            </div>
                            <div class="add-member-basic-list clearfix">
                                <div class="add-member-basic-menu fl">开始日期：</div>
                                <div class="add-member-basic-msg fl">
                                    <el-date-picker v-model="step.beginTime" type="datetime" placeholder="选择日期"
                                                    :picker-options="pickerOptions0"></el-date-picker>
                                </div>
                                <div class="add-member-basic-menu add-member-basic-end fl">截止日期：</div>
                                <div class="add-member-basic-msg fl">
                                    <el-date-picker v-model="step.endTime" type="datetime" placeholder="选择日期"
                                                    :picker-options="pickerOptions0"></el-date-picker>
                                </div>
                            </div>
                        </div>
                        <div class="ctpc-btns">
                            <input type="button" class="ctpc-cancel" @click="cancelAddMember" value="取消">
                            <input type="button" class="ctpc-save" @click="saveAddMember" value="确定">
                        </div>
                    </div>
                    <div class="add-member-opt" v-show="!showAddDetail" @click="addMember">
                        <span class="add-member-icon">+</span>
                        <span class="add-member-msg">添加成员</span>
                    </div>
                </div>
            </div>
            <div class="ctpc-btns">
                <input type="button" class="ctpc-cancel" @click="hide" value="取消">
                <input type="button" class="ctpc-save" @click="saveTask" value="保存">
            </div>
        </div>
    </div>
</template>
<script>
    import http from '../lib/Http'
    import helper from '../lib/Helper'
    import moment from 'moment';

    moment.locale('zh-CN');
    export default {
        props: {
            taskId: String
        },
        data() {
            return {
                showModifyTask: false,
                projectList: [],
                stageList: [],
                tagList: [],
                userList: [],
                taskForm: {
                    taskType: 2,
                    description: '',
                    taskName: '',
                    endTime: '',
                    projectId: '',
                    priority: 1,
                    tags: []
                },
                taskUsers: [],
                step: {
                    stageId: '',
                    stageName: '',
                    userId: '',
                    userName: '',
                    taskHours: '',
                    beginTime: '',
                    endTime: '',
                    description: ''
                },
                showDesc: false,
                showAssess: true,
                stageTagShow: false,
                pickerOptions0: {
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7;
                    }
                },
                showAddTag: false,
                showAddDetail: false,
                showAddBtn: true,
                valueStage: '',
                showAddStageTag: false
            };
        },
        created() {
            this.fetchProjectList()
            this.fetchStageList()
            this.fetchTagList()
            this.fetchUserList()
        },
        computed: {
            insMsgShow() {
                if (this.taskForm.description == '') {
                    return '待添加'
                } else {
                    return this.taskForm.description
                }
            }
        },
        methods: {
            showInsChange() {
                this.showDesc = true;
            },
            cancelEdit() {
                this.showDesc = false;
                this.taskForm.description = '';
            },
            saveEdit() {
                this.showDesc = false;
            },
            deleteTag(index) {
                this.tagList.splice(index, 1);
            },
            addTag() {
                this.showAddTag = true;
            },
            addSelTag() {
                this.showAddTag = false;
                this.showAddStageTag = false;
                for (var i = 0; i < this.value10.length; i++) {
                    // debugger;
                    var tagLis = {};
                    tagLis.tagName = this.value10[i];
                    this.tagList.push(tagLis);
                    // tagLis.tagName = '';
                }
            },
            addMember() {
                this.showAddDetail = !this.showAddDetail;
            },
            cancelAddMember() {
                this.showAddDetail = !this.showAddDetail;
                this.step = {
                    stageId: '',
                    stageName: '',
                    userId: '',
                    userName: '',
                    taskHours: '',
                    beginTime: '',
                    endTime: '',
                    description: ''
                }
            },
            saveAddMember() {
                const valid = this.step.stageId == '' ||
                    this.step.userId == '' ||
                    this.step.taskHours == '' ||
                    this.step.beginTime == '' ||
                    this.step.endTime == '' ||
                    this.step.description == '';
                if (valid) {
                    this.warnMsg('请将阶段填写完整');
                    return
                }
                this.showAddDetail = !this.showAddDetail;
                let taskUser = {}
                taskUser.stageId = this.step.stageId
                taskUser.stageName = this.step.stageName
                taskUser.userId = this.step.userId
                taskUser.userName = this.step.userName
                taskUser.beginTime = moment(this.step.beginTime).format('YYYY-MM-DD HH:mm:ss')
                taskUser.endTime = moment(this.step.endTime).format('YYYY-MM-DD HH:mm:ss')
                taskUser.taskHours = this.step.taskHours
                taskUser.description = this.step.description
                this.taskUsers.push(taskUser)
                this.step = {
                    stageId: '',
                    stageName: '',
                    userId: '',
                    userName: '',
                    taskHours: '',
                    beginTime: '',
                    endTime: '',
                    description: ''
                }
            },
            stepUserChange(val) {
                let vm = this;
                this.userList.forEach((user) => {
                    if (user.id === val) {
                        vm.step.userName = user.name
                    }
                })
            },
            stepStageChange(val) {
                let vm = this;
                this.stageList.forEach((stage) => {
                    if (stage.id === val) {
                        vm.step.stageName = stage.name
                    }
                })
            },
            show() {
                this.fetchTaskDetail();
                this.showModifyTask = true;
            },
            hide() {
                this.showModifyTask = false;
            },
            deleteMember(index) {
                this.taskUsers.splice(index, 1);
            },
            addStage() {
                this.showAddStageTag = true;
            },
            deleteStageTag() {
            },
            fetchProjectList() {
                let vm = this
                http.zsyGetHttp('/project/list', {}, (resp) => {
                    vm.projectList = resp.data
                })
            },
            fetchStageList() {
                let vm = this
                http.zsyGetHttp('/stage/list', {}, (resp) => {
                    vm.stageList = resp.data
                })
            },
            fetchUserList() {
                let vm = this
                http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.userList = resp.data
                })
            },
            fetchTagList() {
                let vm = this
                http.zsyGetHttp('/tag/list', {}, (resp) => {
                    vm.tagList = resp.data
                })
            },
            fetchTaskDetail(){
                if (this.taskId=='') {
                    return
                }
                let vm = this;
                http.zsyGetHttp(`/task/detail/${this.taskId}`, {}, (resp) => {
                    vm.taskForm.description = resp.data.description;
                    vm.taskForm.taskName = resp.data.name;
                    vm.taskForm.endTime = resp.data.endTime;
                    vm.taskForm.projectId = resp.data.projectId;
                    vm.taskForm.priority = resp.data.priority;
                    vm.taskForm.tags = resp.data.tags.map((tag)=>{
                        tag.id
                    });
                    vm.taskUsers = resp.data.users;
                })
            },
            saveTask() {
                if (this.taskForm.description == '') {
                    this.warnMsg("请填写任务备注");
                    return;
                }
                if (this.taskForm.taskName == '') {
                    this.warnMsg("请填写任务名称");
                    return;
                }
                if (this.taskForm.projectId == '') {
                    this.warnMsg("请选择项目");
                    return;
                }
                if (this.taskForm.endTime == '') {
                    this.warnMsg("请选择结束时间");
                    return;
                }
                if (this.taskForm.tags.length == 0) {
                    this.warnMsg("请选择至少一项标签");
                    return;
                }
                if (this.taskUsers.length <2) {
                    this.warnMsg("任务阶段至少为2步");
                    return;
                }
                let param = this.taskForm;
                param.endTime = moment(param.endTime).format('YYYY-MM-DD HH:mm:ss');
                param['taskUsers'] = this.taskUsers;
                let vm = this;
                http.zsyPostHttp('/task/modify', param, (resp) => {
                    this.$message({
                        showClose: true,
                        message: '任务创建成功',
                        type: 'success'
                    });
                    vm.taskForm.description = '';
                    vm.taskForm.taskName = '';
                    vm.taskForm.endTime = '';
                    vm.taskForm.projectId = '';
                    vm.taskForm.priority = 1;
                    vm.taskForm.tags = [];
                    vm.taskUsers = [];
                    vm.$emit('handleFetchTaskList')
                })
                this.showModifyTask = false;
            },
            warnMsg(msg) {
                this.$message({
                    showClose: true,
                    message: msg,
                    type: 'warning'
                });
            },
        }
    }
</script>
<style scoped>
    .create-task-pop { /* display: none; */
        position: fixed;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        z-index: 100;
        background: rgba(0, 0, 0, 0.5);
    }

    .create-task-pop-con {
        position: absolute;
        background: #fff;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        width: 546px;
        padding: 16px 26px 44px;
        height: 566px;
    }

    .ctpc-top {
        font-size: 16px;
        line-height: 30px;
        font-weight: 700;
        color: #333;
        margin-bottom: 6px;
    }

    .ctpc-top-close {
        font-size: 30px;
        width: 30px;
        height: 30px;
        line-height: 27px;
        cursor: pointer;
        transition: 0.8s ease all;
        text-align: center;
    }

    .ctpc-top-close:hover {
        color: #36A8FF;
        transform: rotate(360deg);
    }

    .ctpc-instruction {
        background: #E4E4E4;
        border-radius: 3px;
        padding: 6px 10px;
        margin: 6px 0;
        line-height: 22px;
        font-size: 14px;
        color: #000;
        width: 504px;
    }

    .ctpc-main-con {
        border-top: 1px solid #ccc;
        padding: 10px 0;
        margin-top: 10px;
    }

    .ctpc-list-con .el-select {
        width: 188px;
    }

    .ctpc-list-menu {
        line-height: 30px;
        width: 80px;
        font-size: 14px;
    }

    .ctpc-list-con {
        width: 410px;
    }

    .ctpc-list {
        background: #fff;
        padding: 6px 10px; /* border: 1px solid #ccc; */
        margin-bottom: 10px;
    }

    .ctpc-tags {
        position: relative;
    }

    .ctpc-tags > li {
        display: inline-block;
        line-height: 30px;
    }

    .ctpc-tag-lis {
        background: #F2F2F2;
        padding: 0 16px;
        border-radius: 15px;
        margin-right: 14px;
        margin-bottom: 14px;
        box-shadow: 0 0 10px #ccc;
    }

    .ctpc-tag-lis > span {
        display: inline-block;
        vertical-align: middle;
        font-size: 14px;
    }

    .ctpc-tag-lis .tag-lis-delete:hover {
        text-shadow: 0 0 8px #ccc;
        font-size: 18px !important;
    }

    .tag-lis-circle {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        background: #FF9D6E;
        margin-right: 4px;
    }

    .tag-lis-delete {
        font-size: 18px !important;
        padding: 0 2px;
        cursor: pointer;
    }

    .tag-lis-add {
        position: relative;
        margin-left: 10px;
        cursor: pointer;
    }

    .tag-lis-add-msg {
        color: #36A8FF;
    }

    .ctpc-member-con {
        padding-left: 10px; /* border-left: 1px solid #ccc; */
        margin-left: 6px;
        position: relative;
    }

    .ctpc-member-list {
        height: 42px;
        background: #fff;
        border: 1px solid #ccc;
        line-height: 42px;
        color: #000;
        padding: 0 4px;
        position: relative;
        margin-bottom: 10px;
        box-shadow: 0 0 10px #ccc;
        display: -webkit-flex;
        display: -moz-flex;
        display: -ms-flex;
        display: -o-flex;
        display: flex;
    }

    .ctpc-member-list:before {
        content: '';
        position: absolute;
        left: -17px;
        top: 12px;
        width: 12px;
        height: 12px;
        border-radius: 50%;
        background: #008000;
        z-index: 110;
    }

    .ctpc-member-list > span {
        display: inline-block;
        vertical-align: middle;
    }

    .ctpc-member-job {
        width: 110px;
        /*  -webkit-flex: 1;
          -moz-flex: 1;
          -ms-flex: 1;
          -o-flex: 1;
          flex: 1;*/
    }

    .ctpc-member-job-time {
        width: 110px;
    }

    .ctpc-member-end-time {
        /*width: 110px;*/
    }

    .ctpc-member-assess {
        width: 70px;
    }

    .ctpc-member-head {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        background: #006699;
        color: #fff;
        font-size: 10px;
        text-align: center;
        line-height: 36px;
        margin-top: 3px;
        overflow: hidden;
        margin-right: 10px;
    }

    .bdl-line {
        position: absolute;
        left: 0;
        bottom: 20px;
        top: 14px;
        border-left: 1px solid #ccc;
    }

    .add-member-opt {
        cursor: pointer;
        margin: 20px 10px;
        width: 80px;
    }

    .add-member-opt > span {
        display: inline-block;
        vertical-align: middle;
    }

    .add-member-icon {
        background: #36A8FF;
        color: #fff;
        width: 20px;
        height: 20px;
        text-align: center;
        font-size: 20px;
        line-height: 16px;
        font-weight: bold;
        border-radius: 50%;
    }

    .add-member-msg {
        color: #36A8FF;
        margin-left: 6px;
    }

    .ctpc-btns {
        text-align: right;
        margin-top: 20px;
    }

    .ctpc-btns > input {
        display: inline-block;
        width: 80px;
        height: 28px;
        margin-left: 12px;
        cursor: pointer;
        border-radius: 4px;
    }

    .ctpc-cancel {
        background: #fff;
        border: 1px solid #ccc;
    }

    .ctpc-cancel:hover {
        background: #fff;
        border: 1px solid #36A8FF;
        color: #36A8FF;
        font-weight: 700;
    }

    .ctpc-save {
        background: #36A8FF;
        color: #fff;
    }

    .ctpc-con {
        height: 500px;
        overflow-y: scroll;
        padding-right: 10px;
    }

    .ctpc-con::-webkit-scrollbar {
        width: 10px;
        background-color: #fff;
    }

    .ctpc-con::-webkit-scrollbar-thumb {
        background-color: rgb(255, 255, 255);
        background-image: -webkit-gradient(linear, 40% 0%, 75% 84%, from(rgb(54, 168, 255)), color-stop(0.6, rgb(54, 229, 255)), to(rgb(54, 192, 255)));
        border-radius: 10px;
    }

    .ctpc-con::-webkit-scrollbar-track {
        /* -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.1); */
        background-color: #fff;
        border-radius: 10px;
    }

    .ctpc-instruction-msg {
        padding: 4px;
        cursor: pointer;
    }

    .ctpc-instruction-msg:hover {
        color: #3DA8F5;
        background: #F5F5F5;
    }

    .add-member-remark {
        border: 1px solid #ccc;
        display: block;
        padding: 6px;
        width: 512px;
    }

    .add-member-basic {
        background: #fff;
        border: 1px solid #ccc;
        margin-top: 10px;
        padding: 10px;
        font-size: 14px;
    }

    .add-member-basic-menu {
        width: 78px;
        text-align: right;
    }

    .add-member-basic-list { /* border-bottom: 1px solid #ccc; */
        padding: 5px 0;
        line-height: 34px;
    }

    .add-member-basic-list:last-child {
        border: none;
    }

    .add-stage-opt {
        color: #36A8FF;
        cursor: pointer;
        width: 400px;
    }

    .add-member-basic-msg .el-select {
        width: 140px;
    }

    .member-time-count {
        width: 40px;
        border: 1px solid #ccc;
        height: 26px;
        border-radius: 4px;
        margin-right: 4px;
        text-indent: 4px;
    }

    .add-member-basic-time {
        margin-left: 40px;
    }

    .add-member-basic-msg .el-date-editor.el-input {
        width: 140px;
    }

    .add-member-basic-end {
        margin-left: 40px;
    }

    .tag-add-sel .el-select {
        width: 188px;
    }

    .tag-add-sel {
        position: absolute;
        left: -10px;
        top: 0;
        z-index: 100;
    }

    .add-tag-btn {
        background: #36A8FF;
        color: #fff;
        position: absolute;
        right: 0;
        top: 50%;
        width: 40px;
        height: 30px;
        z-index: 200;
        text-align: center;
        transform: translateY(-50%);
        border-radius: 4px;
    }

    .ctpc-member-delete {
        font-size: 26px;
        cursor: pointer;
        margin-right: 4px;
    }

    .add-member-stage {
        position: relative;
    }

    .ctpc-ins-edit {
        margin-right: 10px;
    }

    .ctpc-ins-edit .ctpc-btns {
        margin-top: 0;
    }
</style>
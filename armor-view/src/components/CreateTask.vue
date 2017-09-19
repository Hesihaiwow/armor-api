<template>
    <div class="create-task-pop" v-show="showCreateTask">
        <div class="create-task-pop-con">
            <div class="ctpc-top clearfix">
                <span class="fl">创建多人任务</span>
                <span class="my-dialog-title-tool">
                    <el-button type="text" icon="check" @click="saveTask">保存</el-button>
                    <el-button type="text" icon="close" @click="hide"></el-button>
               </span>
            </div>
            <div class="ctpc-con">
                <div class="ctpc-instruction-msg" v-show="!showDesc" @click="showInsChange">
                    {{insMsgShow}}
                </div>
                <div class="ctpc-ins-edit" v-show="showDesc">
                    <textarea class="ctpc-instruction" placeholder="任务描述" v-model="taskForm.description"></textarea>
                    <div class="ctpc-btns">
                        <input type="button" class="ctpc-cancel" @click="cancelEdit" value="取消">
                        <input type="button" class="ctpc-save" @click="saveEdit" value="确定">
                    </div>
                </div>
                <div class="ctpc-main-con">
                    <div class="ctpc-list clearfix">
                        <div class="ctpc-list-menu fl"><span class="star">*</span>任务名称</div>
                        <div class="ctpc-list-con fl">
                            <el-input type="text" v-model="taskForm.taskName" auto-complete="off"></el-input>
                        </div>
                    </div>
                    <div class="ctpc-list clearfix">
                        <div class="ctpc-list-menu fl"><span class="star">*</span>项目</div>
                        <div class="ctpc-list-con fl">
                            <el-select v-model="taskForm.projectId" placeholder="请选择">
                                <el-option v-for="item in projectList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                    </div>
                    <div class="ctpc-list clearfix">
                        <div class="ctpc-list-menu fl"><span class="star">*</span>截止日期</div>
                        <div class="ctpc-list-con fl">
                            <el-date-picker v-model="taskForm.endTime" type="date" format="yyyy-MM-dd"
                                            placeholder="选择日期"></el-date-picker>
                        </div>
                    </div>
                    <div class="ctpc-list clearfix">
                        <div class="ctpc-list-menu fl"><span class="star">*</span>优先级</div>
                        <div class="ctpc-list-con fl">
                            <el-select v-model="taskForm.priority" clearable placeholder="请选择">
                                <el-option
                                        v-for="item in priorityList"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                        </div>
                    </div>
                    <div class="ctpc-list clearfix">
                        <div class="ctpc-list-menu fl"><span class="star">*</span>阶段</div>
                        <div class="ctpc-list-con fl">
                            <el-select v-model="taskForm.stageId" :multiple-limit="1"
                                       placeholder="请选择">
                                <el-option v-for="item in stageList" :key="item.id"
                                           :label="item.name" :value="item.id"></el-option>
                            </el-select>
                        </div>
                    </div>
                    <div class="ctpc-list clearfix">
                        <div class="ctpc-list-menu fl"><span class="star">*</span>标签</div>
                        <div class="ctpc-list-con fl">
                            <ul class="ctpc-tags">
                                <li class="tag-lis-add">
                                    <div class="tag-add-sel">
                                        <!--<div class="add-tag-btn" @click="addSelTag">添加</div>-->
                                        <el-select v-model="taskForm.tags"  multiple placeholder="添加标签">
                                            <el-option v-for="item in tagList" :key="item.id" :label="item.name"
                                                       :value="item.id"></el-option>
                                        </el-select>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                    <div class="ctpc-member-con">
                        <div class="ctpc-member-list clearfix in" v-for="(item,index) in taskUsers"
                             :class="item.cssClass">
                            <span class="fl ctpc-member-head">{{item.userName}}</span>
                            <span class="fl ctpc-member-job-time">工作量:{{item.taskHours}}工时</span>
                            <span class="fl ctpc-member-end-time">截止:{{item.endTime}}</span>
                            <span style="position: absolute;right: 10px;">
                                <el-button type="text" icon="edit" @click="modifyStep(index,taskUsers)"></el-button>
                            <el-button type="text" icon="close" @click="deleteMember(index)"></el-button>
                            </span>
                        </div>
                        <div class="bdl-line"></div>
                    </div>
                    <div class="ctpc-add-member-detail" v-if="showAddDetail">
                        <el-input type="textarea" placeholder="描述该成员的工作内容..." v-model="step.description"
                                  :rows="3"></el-input>
                        <!--<textarea class="add-member-remark" placeholder="备注"></textarea>-->
                        <div class="add-member-basic">
                            <div class="add-member-basic-list clearfix">
                                <div class="add-member-basic-menu fl"><span class="star">*</span>姓名：</div>
                                <div class="add-member-basic-msg fl">
                                    <el-select v-model="step.userId" filterable  placeholder="请选择" @change="stepUserChange">
                                        <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                                   :value="item.id"></el-option>
                                    </el-select>
                                </div>
                                <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>工作量：
                                </div>
                                <div class="add-member-basic-msg fl">
                                    <input class="member-time-count" v-model="step.taskHours" :maxlength="6" style="width:80px">工时
                                </div>
                            </div>
                            <div class="add-member-basic-list clearfix">
                                <div class="add-member-basic-menu fl"><span class="star">*</span>开始日期：</div>
                                <div class="add-member-basic-msg fl">
                                    <el-date-picker v-model="step.beginTime" type="date" format="yyyy-MM-dd"
                                                    placeholder="选择日期"></el-date-picker>
                                </div>
                                <div class="add-member-basic-menu add-member-basic-end fl"><span class="star">*</span>截止日期：
                                </div>
                                <div class="add-member-basic-msg fl">
                                    <el-date-picker v-model="step.endTime" type="date" format="yyyy-MM-dd"
                                                    placeholder="选择日期"></el-date-picker>
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
            <!--  <div class="ctpc-btns">
                  <input type="button" class="ctpc-cancel" @click="hide" value="取消">
                  <input type="button" class="ctpc-save" @click="saveTask" value="立即创建">
              </div>-->
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
            projectList: Array,
            stageList: Array,
            tagList: Array,
            userList: Array
        },
        data() {
            return {
                showCreateTask: false,

                priorityList: [
                    {label: '普通', value: 1},
                    {label: '紧急', value: 2},
                    {label: '非常紧急', value: 3},
                ],
                taskForm: {
                    taskType: 2,
                    description: '',
                    taskName: '',
                    endTime: '',
                    projectId: '',
                    stageId: '',
                    priority: '',
                    tags: []
                },
                taskUsers: [],
                step: {
                    index: '',
                    stageName: '',
                    userId: '',
                    userName: '',
                    taskHours: '',
                    beginTime: '',
                    endTime: '',
                    description: ''
                },
                stepTemp: {},
                showDesc: false,
                showAssess: true,
                stageTagShow: false,
                pickerOptions0: {
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7;
                    }
                },
                showAddDetail: false,
                showAddBtn: true,
                valueStage: '',
                showAddStageTag: false
            };
        },
        created() {
//            this.fetchProjectList()
//            this.fetchStageList()
//            this.fetchTagList()
//            this.fetchUserList()
        },
        computed: {
            insMsgShow() {
                if (this.taskForm.description == '') {
                    return '点击添加任务描述'
                } else {
                    return this.taskForm.description
                }
            },
            stepBeginTimeOptions() {
                let endTime = this.taskForm.endTime
                return {
                    disabledDate(time) {
                        return time > endTime;
                    }
                };
            },
            stepEndTimeOptions() {
                let endTime = this.taskForm.endTime
                let beginTime = this.step.beginTime
                return {
                    disabledDate(time) {
                        return time < beginTime || time > endTime;
                    }
                };
            }
        },
        filters: {
            getTagName(id, tagList) {
                tagList.forEach((tag) => {
                    if (tag.id === id) {
                        return tag.name
                    }
                })
            },
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD');
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
            },
            addSelTag() {
                this.showAddStageTag = false;
                for (var i = 0; i < this.value10.length; i++) {
                    // debugger;
                    var tagLis = {};
                    tagLis.tagName = this.value10[i];
                    this.tagList.push(tagLis);
                    // tagLis.tagName = '';
                }
            },
            // 修改阶段
            modifyStep(index, stages) {
                this.stepTemp = {
                    userId: stages[index].userId,
                    userName: stages[index].userName,
                    taskHours: stages[index].taskHours,
                    beginTime: stages[index].beginTime,
                    endTime: stages[index].endTime,
                    description: stages[index].description
                }
                this.taskUsers.forEach((item) => {
                    item.cssClass = ''
                })
                this.taskUsers[index].cssClass = 'stepActive'
                this.step = stages[index]
                this.step.index = index
                this.showAddDetail = true;
            },
            addMember() {
                this.showAddDetail = !this.showAddDetail;
            },
            cancelAddMember() {
                this.showAddDetail = !this.showAddDetail;
                if (this.step.index !== '') {
                    this.taskUsers[this.step.index] = this.stepTemp;
                    // 取消css
                    this.taskUsers[this.step.index].cssClass = ''
                }
                this.step = {
                    index: '',
                    userId: '',
                    userName: '',
                    taskHours: '',
                    beginTime: '',
                    endTime: '',
                    description: ''
                }
            },
            saveAddMember() {
                const valid =
                    this.step.userId == '' ||
                    this.step.taskHours == '' ||
                    this.step.beginTime == '' ||
                    this.step.endTime == ''
                if (valid) {
                    this.$message.error('请将阶段填写完整');
                    return
                }
                var isnum = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.step.taskHours);
                if(!isnum){
                    this.$message.error('工作量填写错误');
                    return false;
                }
                if(this.step.taskHours>99999.9||this.step.taskHours<0.1){
                    this.$message.error('工作量正确值应为0.1~99999.9');
                    return false;
                }

                this.showAddDetail = !this.showAddDetail;
                if (this.step.index === '') {
                    let taskUser = {}
                    taskUser.userId = this.step.userId
                    taskUser.userName = this.step.userName
                    taskUser.beginTime = moment(this.step.beginTime).format('YYYY-MM-DD')
                    taskUser.endTime = moment(this.step.endTime).format('YYYY-MM-DD')
                    taskUser.taskHours = this.step.taskHours
                    taskUser.description = this.step.description
                    this.taskUsers.push(taskUser)
                } else {
                    // 取消css
                    this.taskUsers[this.step.index].cssClass = ''
                }


                this.step = {
                    index: '',
                    userId: '',
                    userName: '',
                    taskHours: '',
                    beginTime: '',
                    endTime: '',
                    description: ''
                }
                this.stepTemp = {}
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
                this.showCreateTask = true;
            },
            hide() {
                this.showCreateTask = false;
            },
            deleteMember(index) {
                this.taskUsers.splice(index, 1);
                if (this.taskUsers.length == 0) {
                    this.showAddDetail = false
                    this.step = {
                        index: '',
                        userId: '',
                        userName: '',
                        taskHours: '',
                        beginTime: '',
                        endTime: '',
                        description: ''
                    }
                }
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
            saveTask() {
             /*   if (this.taskForm.description == '') {
                    this.$message.warning("请填写任务备注");
                    return;
                }*/
                if (this.taskForm.taskName == '') {
                    this.$message.warning("请填写任务名称");
                    return;
                }
                if (this.taskForm.projectId == '') {
                    this.$message.warning("请选择项目");
                    return;
                }
                if (this.taskForm.endTime == '') {
                    this.$message.warning("请选择结束时间");
                    return;
                }
                if (this.taskForm.stageId === '') {
                    this.$message.warning("请选择项目阶段");
                    return;
                }
                if (this.taskForm.tags.length == 0) {
                    this.$message.warning("请选择至少一项标签");
                    return;
                }
                /*  if (this.taskUsers.length < 2) {
                      this.$message.warning("至少添加2个成员");
                      return;
                  }*/
                let param = this.taskForm;
                param.taskName = param.taskName.trim()
                param.description = param.description.trim()
                param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:59');
                param['taskUsers'] = this.taskUsers;
                param.taskUsers.forEach((user) => {
                    user.description = user.description.trim();
                    user.beginTime = moment(user.beginTime).format('YYYY-MM-DD HH:mm:ss');
                    user.endTime = moment(user.endTime).format('YYYY-MM-DD 23:59:59');
                })
                let vm = this;
                http.zsyPostHttp('/task/create', param, (resp) => {
                    vm.$message.success('任务创建成功');
                    vm.taskForm.description = '';
                    vm.taskForm.taskName = '';
                    vm.taskForm.endTime = '';
                    vm.taskForm.projectId = '';
                    vm.taskForm.sta = '';
                    vm.taskForm.priority = 1;
                    vm.taskForm.tags = [];
                    vm.taskUsers = [];
                    // 刷新看板
                    this.$root.eventBus.$emit("reloadBoard");
                    // 刷新列表
                    vm.$emit('handleFetchTaskList')
                })
                this.showCreateTask = false;
            }
        }
    }
</script>
<style scoped>
    .stepActive {
        box-shadow: 0 0 10px #20A0FF !important;
    }

    .star {
        color: red;
        padding: 1px;
    }

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
        padding: 16px 26px 1px;
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
        /*margin-left: 10px;*/
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

    .ctpc-member-list.done:before {
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

    .ctpc-member-list.in:before {
        content: '';
        position: absolute;
        left: -17px;
        top: 12px;
        width: 12px;
        height: 12px;
        border-radius: 50%;
        background: #e4e8f1;
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
        width: 82px;
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
        /*position: absolute;*/
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
<template>
    <div class="nav-index-con">
        <div v-show="userRole == 3 || permit" style="float: left">
            <el-button v-loading.fullscreen.lock="fullscreenLoading"
                       element-loading-text="拼命导入中,请稍后"
                       element-loading-spinner="el-icon-loading"
                       element-loading-background="rgba(0, 0, 0, 0.8)"
                       type="primary" @click="uploadToMysqlVisible=true">导入考勤记录</el-button>
        </div>
        <div v-show="userRole == 3 || permit">
            <el-button v-loading.fullscreen.lock="fullscreenLoading"
                       element-loading-text="拼命导出中,请稍后"
                       element-loading-spinner="el-icon-loading"
                       element-loading-background="rgba(0, 0, 0, 0.8)"
                       type="primary" @click="excelSignInVisible=true"
                       style="margin-left: 20px">导出考勤记录</el-button>
        </div>
        <div class="my-integral-con" v-show="userRole>0 && userRole < 3">
            <div><p class="mic-title">我的积分</p>
                <div class="add-task" style="float: left;margin-top: -22px;margin-right: 420px;font-size: 14px"
                     @click="integralBasicVisible=true">
                    <span class="task-time-opt" style="font-size:14px"><i class="el-icon-edit"></i></span>计算基准积分
                </div>
            </div>
            <div class="mic-main clearfix">
                <div class="mic-item fl" v-for="(item,key) in integralItem" style="margin-left: 75px;">
                    <div class="mic-item-title"><img :src="`${require(`../assets/img/icon_${key+1}.png`)}`"
                                                     class="icon-score">{{item.label}}
                    </div>
                    <div class="mic-item-title" style="font-size: 12px">{{item.time}}</div>
                    <div class="mic-item-integral">{{item.score}}</div>
                </div>
            </div>
        </div>
        <div class="my-task-con" v-show="userRole == 3">

            <p class="mic-title" style="margin-top: 20px">考勤记录</p>
            <div class="my-task-detail" style="width: 1200px;">
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="signInReqDTO.userId" clearable filterable   placeholder="筛选用户">
                        <el-option v-for="item in checkInUsers" :key="item.userId" :label="item.userName"
                                   :value="item.userId"></el-option>
                    </el-select>
                </div>
                <div class="add-member-basic-msg fl">
                    <el-date-picker
                            v-model="signInReqDTO.beginTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="请选择开始时间"
                    >
                    </el-date-picker>
                    <span style="font-size: 14px;color: #606266;">-</span>
                    <el-date-picker
                            v-model="signInReqDTO.endTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="请选择截止时间"
                    >
                    </el-date-picker>
                </div>
                <el-button type="primary" @click="selectSignInData" style="margin-left: 10px" size="small">查询</el-button>
                <el-button type="primary" @click="showTotalEWrokTime = true" style="margin-left: 10px" size="small">加班总时长</el-button>
                <el-table :data="signInData" border>
                    <el-table-column prop="date" label="日期" align="center" width="120">
                        <template scope="scope">
                            {{scope.row.date | formatDate2}}{{scope.row.weekday}}
                            <span v-show="scope.row.isWeekend == 1" style="color: #3da7f5">(周末)</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="userName" label="用户" align="center" width="110">
                        <template scope="scope">
                            <span style="color: red" v-if="scope.row.isForget == 1">(漏)</span>{{scope.row.userName}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="checkTimeList" label="打卡记录" align="left">
                        <template scope="scope">
                            {{scope.row.checkTimeList}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="checkInTime" label="上班时间" align="center" width="120" >
                        <template scope="scope">
                            <span style="color: red" v-if="scope.row.isRecheckIn == 1">(补)</span>
                            <span v-if="scope.row.isCheckInAfterTen == 1" style="color: red">{{scope.row.checkInTime | formatTime2}}</span>
                            <span v-else>{{scope.row.checkInTime | formatTime2}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="checkOutTime" label="下班时间" align="center" width="120" >
                        <template scope="scope">
                            <span style="color: red" v-if="scope.row.isRecheckOut == 1">(补)</span>
                            <span style="color: green" v-if="scope.row.isWorkToNextDay == 1">(+1)</span>
                            <span v-if="scope.row.isCheckOutBeforeSix == 1" style="color: red">{{scope.row.checkOutTime | formatTime2}}</span>
                            <span v-else>{{scope.row.checkOutTime | formatTime2}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="workTime" label="上班时长" align="center" width="120" >
                        <template scope="scope">
                            <span v-if="scope.row.lessThanNine == 1" style="color: red">{{scope.row.workTime}}</span>
                            <span v-else>{{scope.row.workTime}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="eWorkTime" label="加班时长" align="center" width="120" >
                        <template scope="scope">
                            {{scope.row.eWorkTime}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="eWorkHours" label="加班申请" align="center" width="100" >
                        <template scope="scope">
                            {{scope.row.eWorkHours}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="leaveTime" label="请假时长" align="center" width="100" >
                        <template scope="scope">
                            {{scope.row.leaveTime}}
                        </template>
                    </el-table-column>

                </el-table>
                <div class="pagination">
                    <el-pagination
                            @current-change="signInHandleCurrentChange"
                            :current-page.sync="signInReqDTO.pageNum"
                            :page-size="signInPage.pageSize"
                            :layout="signInPageLayout"
                            :total="signInPage.total">
                    </el-pagination>
                </div>
            </div>
        </div>

        <div class="my-task-con">
            <div v-show="userRole>0 && userRole < 3">
                <div class="add-task" style="margin-right: 220px" @click="createExtraWork">
                    <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                    <span>加班申请</span>
                </div>
                <div class="add-task" @click="createQuestionClick">
                    <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                    <span>创建线上问题(数据)记录</span>
                </div>
                <div class="add-task  question" @click="createTaskClick">
                    <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                    <span>创建个人任务</span>
                </div>
                <div class="add-task help" @click="editHelpVisible=true">
                    <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                    <span>创建积分转移</span>
                </div>

                <div class="add-task leave" @click="editLeaveVisible=true,clearLeaveForm()">
                    <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                    <span>请假申请</span>
                </div>
                <p class="mic-title">我的任务</p>
                <div class="my-task-detail">
                    <el-tabs v-model="activeName" @tab-click="handleClick">
                        <el-tab-pane label="进行中" name="doing">
                            <task-item :taskItems="task.doing" :isPrivate="true"
                                       taskStatus="TaskDoing" @reload="reload"
                                       :projectList="projectList"
                                       :userList="userList"
                                       :stageList="stageList"
                                       :viewType="1"
                                       :tagList="tagList"></task-item>
                        </el-tab-pane>
                        <el-tab-pane label="已完成" name="completed">
                            <task-item :taskItems="task.finished" :isPrivate="true" taskStatus="finished"
                                       :projectList="projectList"
                                       :userList="userList"
                                       :stageList="stageList"
                                       :viewType="1"
                                       :tagList="tagList"></task-item>
                            <div class="pagination" v-show="this.task.finished.length>0">
                                <el-pagination
                                        @current-change="handleFinishedPage"
                                        :current-page.sync="finishedPage.pageNum"
                                        :page-size="finishedPage.pageSize"
                                        :layout="finishedPageLayout"
                                        :total="finishedPage.total">
                                </el-pagination>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="测试中" name="test" v-if="developVisible">
                            <div class="task-lis" v-for="test in task.test">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>任务名称：{{test.taskName}}：{{test.description}}</span>
                                    </div>
                                    <div class="task-state">
                                        <span class="task-end blue">截止日期{{test.endTime| formatDate }}</span>
                                    </div>
                                </div>
                                <div class="task-data-show">
                                    <span class="task-score">测试时间：{{test.hours}} 小时</span>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="test.avatarUrl && test.avatarUrl!=''"
                                         :src="test.avatarUrl">
                                    <span v-else="">{{test.userName}}</span>
                                </div>
                            </div>
                            <div v-show="task.test.length==0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <!--<el-tab-pane label="审核失败" name="applyFail">
                            <task-item :taskItems="task.applyFail" :isPrivate="true"
                                       taskStatus="ApplyFail" @reload="reload"></task-item>
                        </el-tab-pane>-->
                    </el-tabs>
                </div>
                <p class="mic-title">我的考勤</p>
                <div class="my-task-detail" style="width: 1200px">
                    <div class="add-member-basic-msg fl">
                        <el-date-picker
                                v-model="mySignInReqDTO.beginTime"
                                align="right"
                                type="date"
                                value-format="yyyy-MM-dd"
                                clearable
                                placeholder="请选择开始时间"
                        >
                        </el-date-picker>
                        <span style="font-size: 14px;color: #606266;">-</span>
                        <el-date-picker
                                v-model="mySignInReqDTO.endTime"
                                align="right"
                                type="date"
                                value-format="yyyy-MM-dd"
                                clearable
                                placeholder="请选择截止时间"
                        >
                        </el-date-picker>
                    </div>
                    <el-button type="primary" @click="selectMySignInData" style="margin-left: 10px" size="small">查询</el-button>
                    <el-button type="primary" @click="showPersonalTotalEWrokTime = true" style="margin-left: 10px" size="small">加班总时长</el-button>
                    <el-table :data="signInData" border>
                        <el-table-column prop="date" label="日期" align="center" width="120">
                            <template scope="scope">
                                {{scope.row.date | formatDate2}}{{scope.row.weekday}}
                                <span v-show="scope.row.isWeekend == 1" style="color: #3da7f5">(周末)</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="userName" label="用户" align="center" width="110">
                            <template scope="scope">
                                <span style="color: red" v-if="scope.row.isForget == 1">(漏)</span>{{scope.row.userName}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="checkTimeList" label="打卡记录" align="left">
                            <template scope="scope">
                                {{scope.row.checkTimeList}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="checkInTime" label="上班时间" align="center" width="120" >
                            <template scope="scope">
                                <span style="color: red" v-if="scope.row.isRecheckIn == 1">(补)</span>
                                <span v-if="scope.row.isCheckInAfterTen == 1" style="color: red">{{scope.row.checkInTime | formatTime2}}</span>
                                <span v-else>{{scope.row.checkInTime | formatTime2}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="checkOutTime" label="下班时间" align="center" width="120" >
                            <template scope="scope">
                                <span style="color: red" v-if="scope.row.isRecheckOut == 1">(补)</span>
                                <span style="color: green" v-if="scope.row.isWorkToNextDay == 1">(+1)</span>
                                <span v-if="scope.row.isCheckOutBeforeSix == 1" style="color: red">{{scope.row.checkOutTime | formatTime2}}</span>
                                <span v-else>{{scope.row.checkOutTime | formatTime2}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="workTime" label="上班时长" align="center" width="120" >
                            <template scope="scope">
                                <span v-if="scope.row.lessThanNine == 1" style="color: red">{{scope.row.workTime}}</span>
                                <span v-else>{{scope.row.workTime}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="eWorkTime" label="加班时长" align="center" width="120" >
                            <template scope="scope">
                                {{scope.row.eWorkTime}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="eWorkHours" label="加班申请" align="center" width="100" >
                            <template scope="scope">
                                {{scope.row.eWorkHours}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="leaveTime" label="请假时长" align="center" width="100" >
                            <template scope="scope">
                                {{scope.row.leaveTime}}
                            </template>
                        </el-table-column>
                        <el-table-column label="操作" align="center" width="80">
                            <template scope="scope">
                                <a v-show="scope.row.canReCheck == 1" style="color: blue;cursor: pointer" @click="recheck(scope.row.userId)">补签到</a>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                @current-change="mySignInHandleCurrentChange"
                                :current-page.sync="mySignInReqDTO.pageNum"
                                :page-size="mySignInPage.pageSize"
                                :layout="mySignInPageLayout"
                                :total="mySignInPage.total">
                        </el-pagination>
                    </div>
                </div>
                <p class="mic-title">我的线上问题</p>
                <div class="my-task-detail">
                    <el-tabs v-model="activeQuestionName" @tab-click="handleClick">
                        <el-tab-pane label="进行中" name="running">
                            <div class="task-lis" v-for="item in question.running" @click="item.reviewStatus == 1 ? editQuestion(item) : finishQuestion(item)">
                                <div class="head-img"><img src="../assets/img/doing.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name" style="width: 700px;">
                                        <span v-if="item.reviewStatus == 1" style="color: red">(待审核)</span><span>{{item.name}}:({{item.description}})</span>
                                    </div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人：{{item.userName}}</span>
                                        <span v-if="item.isToday == -1" class="task-end red">截止时间：{{item.endTime| formatDate }}</span>
                                        <span v-if="item.isToday == 0" class="task-end orange">截止时间：{{item.endTime| formatDate }}</span>
                                        <span v-if="item.isToday == 1" class="task-end blue">截止时间：{{item.endTime| formatDate }}</span>
                                    </div>
                                </div>
                                <div class="task-mark" style="position:relative; left:-10px">
                                    <img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;">
                                    <img v-else="" src="../assets/img/u431.png" alt="" >
                                    <span  class="mark-msg">{{item.projectName}}</span>
                                </div>
                                <div class="task-data-show">
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!=''"
                                         :src="item.avatarUrl" :alt="item.userName">
                                    <span v-else="">{{item.userName}}</span>
                                </div>
                            </div>
                            <div v-show="question.running.length==0 || question.running == null" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="已完成" name="completed">
                            <div class="task-lis" v-for="item in question.completed">
                                <div class="head-img"><img src="../assets/img/finished.jpg"></div>
                                <div class="main-task-detail">
                                    <div class="task-name" style="width: 700px;">
                                        <span>{{item.name}}:({{item.description}})</span>
                                    </div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人：{{item.userName}}</span>
                                        <span class="task-end green">完成时间：{{item.completeTime| formatDate }}</span>
                                    </div>
                                </div>
                                <div class="task-mark" style="position:relative; left:-10px">
                                    <img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;">
                                    <img v-else="" src="../assets/img/u431.png" alt="" >
                                    <!--<img src="../assets/img/u431.png" alt="">-->
                                    <span  class="mark-msg">{{item.projectName}}</span>
                                </div>
                                <div class="task-data-show">
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!=''"
                                         :src="item.avatarUrl" :alt="item.userName">
                                    <span v-else="">{{item.userName}}</span>
                                </div>
                            </div>
                            <div v-show="question.completed.length==0 || question.completed == null" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                            <div class="pagination" v-show="this.question.completed.length>0">
                                <el-pagination
                                        @current-change="handleFinishedPage1"
                                        :current-page.sync="finishedPage1.pageNum"
                                        :page-size="finishedPage1.pageSize"
                                        :layout="finishedPageLayout1"
                                        :total="finishedPage1.total">
                                </el-pagination>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
                <div>
                    <p class="mic-title">评价任务</p>
                    <el-tabs v-model="assessActiveName" @tab-click="handleClick">
                        <el-tab-pane label="待评价" name="waitAssess">
                            <task-item :taskItems="task.waitAssess" :isPrivate="true" @reload="reload"
                                       taskStatus="WaitAssess"
                                       :projectList="projectList"
                                       :userList="userList"
                                       :stageList="stageList"
                                       :viewType="1"
                                       :tagList="tagList"></task-item>
                        </el-tab-pane>

                        <el-tab-pane label="已评价" name="commented">
                            <task-item :taskItems="task.commented" :isPrivate="true" taskStatus="WaitAssess"
                                       :projectList="projectList"
                                       :userList="userList"
                                       :stageList="stageList"
                                       :viewType="1"
                                       :tagList="tagList"></task-item>
                            <div class="pagination" v-show="this.task.commented.length>0">
                                <el-pagination
                                        @current-change="handleCommentedPage"
                                        :current-page.sync="commentedPage.pageNum"
                                        :page-size="commentedPage.pageSize"
                                        :layout="commentedPageLayout"
                                        :total="commentedPage.total">
                                </el-pagination>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
                <div>
                    <p class="mic-title">任务延长申请</p>
                    <el-tabs v-model="taskExpandTabsActiveName" @tab-click="handleClick">
                        <el-tab-pane label="待审核" name="wait">
                            <div class="task-lis" v-for="expand in taskExpand.doing" @click="getExpandDetail(expand)">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>{{expand.taskName}}:{{expand.reason}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人：{{expand.userName}}</span>
                                        <span class="task-end blue">截止时间：{{expand.endTime| formatDate }}</span>
                                    </div>
                                </div>
                                <div class="task-data-show">
                                    <span class="task-score">延长时间：{{expand.hours}} 小时</span>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="expand.avatarUrl && expand.avatarUrl!=''"
                                         :src="expand.avatarUrl">
                                    <span v-else="">{{expand.userName}}</span>
                                </div>
                            </div>
                            <div v-show="taskExpand.doing.length==0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="审核通过" name="completed">
                            <div class="task-lis" v-for="expand in taskExpand.finished">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>{{expand.reason}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人：{{expand.userName}}</span>
                                        <span class="task-end blue">截止时间：{{expand.endTime| formatDate }}</span>
                                        <span class="task-time-opt"><i class="el-icon-circle-check"></i></span>
                                    </div>
                                </div>
                                <div class="task-data-show">
                                    <span class="task-score">延长时间：{{expand.hours}} 小时</span>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="expand.avatarUrl && expand.avatarUrl!=''"
                                         :src="expand.avatarUrl">
                                    <span v-else="">{{expand.userName}}</span>
                                </div>
                            </div>
                            <div class="pagination" v-show="this.taskExpand.finished.length>0">
                                <el-pagination
                                        @current-change="handleExpandPage"
                                        :current-page.sync="expandPage.pageNum"
                                        :page-size="expandPage.pageSize"
                                        :layout="expandPageLayout"
                                        :total="expandPage.total">
                                </el-pagination>
                            </div>
                            <div v-show="taskExpand.finished.length==0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
                <div>
                    <p class="mic-title">我的积分转移</p>
                    <div class="my-task-detail">
                        <el-tabs v-model="activeHelpName" @tab-click="handleClick">
                            <el-tab-pane label="审核中" name="wait">
                                <!--@click="reviewDetail(help)"-->
                                <div class="task-lis" v-for="help in review.wait">
                                    <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                    <div class="main-task-detail">
                                        <div class="task-name"><span>{{help.description}}</span></div>
                                        <div class="task-state">
                                            <span class="task-end blue">求助目标：{{help.name.split(",")[0]}}</span>
                                            <span class="task-end blue">{{help.time| formatDate }}</span>
                                            <span class="task-time-opt"><i class="el-icon-edit"
                                                                           @click="editHelpDetail(help)"></i></span>
                                        </div>
                                    </div>
                                    <div class="task-data-show">
                                        <span class="task-score">求助转移：{{help.integral}} 分</span>
                                    </div>
                                    <div class="task-username">
                                        <img class="task-avatar" v-if="help.avatarUrl && help.avatarUrl!=''"
                                             :src="help.avatarUrl">
                                        <span v-else="">{{help.name.split(",")[1]}}</span>
                                    </div>
                                </div>
                                <div class="pagination" v-show="this.review.wait.length>0">
                                    <el-pagination
                                            @current-change="handleWaitPage"
                                            :current-page.sync="waitPage.pageNum"
                                            :page-size="waitPage.pageSize"
                                            :layout="waitPageLayout"
                                            :total="waitPage.total">
                                    </el-pagination>
                                </div>
                                <div v-show="review.wait.length==0" class="empty">
                                    <h2>暂无数据</h2>
                                </div>
                            </el-tab-pane>
                            <el-tab-pane label="完成审核" name="review">
                                <div class="task-lis" v-for="help in review.review" @click="reviewDetail(help)">
                                    <div class="head-img"><img src="../assets/img/auditSuccess.png"></div>
                                    <div class="main-task-detail">
                                        <div class="task-name"><span>{{help.description}}</span></div>
                                        <div class="task-state">
                                            <span class="task-end blue">求助目标：{{help.name.split(",")[0]}}</span>
                                            <span class="task-end blue">{{help.time| formatDate }}</span>
                                            <span class="task-time-opt"><i class="el-icon-circle-check"></i></span>
                                        </div>
                                    </div>
                                    <div class="task-data-show">
                                        <span class="task-score">转移完成：-{{help.integral}} 分</span>
                                    </div>
                                    <div class="task-username">
                                        <img class="task-avatar" v-if="help.avatarUrl && help.avatarUrl!=''"
                                             :src="help.avatarUrl">
                                        <span v-else="">{{help.name.split(",")[1]}}</span>
                                    </div>
                                </div>
                                <div class="pagination" v-show="this.review.review.length>0">
                                    <el-pagination
                                            @current-change="handleReviewPage"
                                            :current-page.sync="reviewPage.pageNum"
                                            :page-size="reviewPage.pageSize"
                                            :layout="reviewPageLayout"
                                            :total="reviewPage.total">
                                    </el-pagination>
                                </div>
                                <div v-show="review.review.length==0" class="empty">
                                    <h2>暂无数据</h2>
                                </div>
                            </el-tab-pane>
                        </el-tabs>
                    </div>
                </div>
            </div>
            <div v-show="userRole===0">
                <p class="mic-title">个人任务审核</p>
                <div class="add-task" style="float: left;margin-top: -22px;margin-right: 400px;font-size: 14px"
                     @click="integralBasicVisible=true">
                    <span class="task-time-opt" style="font-size:14px"><i class="el-icon-edit"></i></span>计算基准积分
                </div>
                <el-tabs v-model="auditTabsActiveName" @tab-click="handleClick">
                    <el-tab-pane label="待审核" name="wait">
                        <task-item :taskItems="task.waitAudit" :isPrivate="true" @reload="reload"
                                   taskStatus="WaitAuditing"
                                   :projectList="projectList"
                                   :userList="userList"
                                   :stageList="stageList"
                                   :viewType="1"
                                   :tagList="tagList"></task-item>
                    </el-tab-pane>
                    <el-tab-pane label="审核通过" name="completed">
                        <task-item :taskItems="task.auditSuccess" :isPrivate="true" @reload="reload"
                                   taskStatus="auditSuccess"
                                   :projectList="projectList"
                                   :userList="userList"
                                   :stageList="stageList"
                                   :viewType="1"
                                   :tagList="tagList"></task-item>
                        <div class="pagination" v-show="this.task.auditSuccess.length>0">
                            <el-pagination
                                    @current-change="handleAuditSuccessPage"
                                    :current-page.sync="auditSuccessPage.pageNum"
                                    :page-size="auditSuccessPage.pageSize"
                                    :layout="auditSuccessPageLayout"
                                    :total="auditSuccessPage.total">
                            </el-pagination>
                        </div>
                    </el-tab-pane>
                </el-tabs>
                <p class="mic-title">任务延长申请审核</p>
                <el-tabs v-model="taskExpandTabsActiveName" @tab-click="handleClick">
                    <el-tab-pane label="待审核" name="wait">
                        <div class="task-lis" v-for="expand in taskExpand.doing" @click="getExpandDetail(expand)">
                            <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                            <div class="main-task-detail">
                                <div class="task-name"><span>{{expand.taskName}}:{{expand.reason}}</span></div>
                                <div class="task-state">
                                    <span class="task-end blue">申请人：{{expand.userName}}</span>
                                    <span class="task-end blue">{{expand.endTime| formatDate }}</span>
                                    <span class="task-time-opt"><i class="el-icon-edit" ></i></span>
                                </div>
                            </div>
                            <div class="task-data-show">
                                <span class="task-score">延长时间：{{expand.hours}} 小时</span>
                            </div>
                            <div class="task-username">
                                <img class="task-avatar" v-if="expand.avatarUrl && expand.avatarUrl!=''"
                                     :src="expand.avatarUrl">
                                <span v-else="">{{expand.userName}}</span>
                            </div>
                        </div>
                        <div v-show="taskExpand.doing.length==0" class="empty">
                            <h2>暂无数据</h2>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="审核通过" name="completed">
                        <div class="task-lis" v-for="expand in taskExpand.finished">
                            <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                            <div class="main-task-detail">
                                <div class="task-name"><span>{{expand.taskName}}:{{expand.reason}}</span></div>
                                <div class="task-state">
                                    <span class="task-end blue">申请人：{{expand.userName}}</span>
                                    <span class="task-end blue">截止时间：{{expand.endTime| formatDate }}</span>
                                    <span class="task-time-opt"><i class="el-icon-circle-check"></i></span>
                                </div>
                            </div>
                            <div class="task-data-show">
                                <span class="task-score">延长时间：{{expand.hours}} 小时</span>
                            </div>
                            <div class="task-username">
                                <img class="task-avatar" v-if="expand.avatarUrl && expand.avatarUrl!=''"
                                     :src="expand.avatarUrl">
                                <span v-else="">{{expand.userName}}</span>
                            </div>
                        </div>
                        <div class="pagination" v-show="this.taskExpand.finished.length>0">
                            <el-pagination
                                    @current-change="handleExpandPage"
                                    :current-page.sync="expandPage.pageNum"
                                    :page-size="expandPage.pageSize"
                                    :layout="expandPageLayout"
                                    :total="expandPage.total">
                            </el-pagination>
                        </div>
                        <div v-show="taskExpand.finished.length==0" class="empty">
                            <h2>暂无数据</h2>
                        </div>
                    </el-tab-pane>
                </el-tabs>
                <p class="mic-title">线上问题审核</p>
                <el-tabs v-model="questionActiveName" @tab-click="handleClick">
                    <el-tab-pane label="待审核" name="wait">
                        <div class="task-lis" v-for="item in question.wait" @click="checkQuestion(item)">
                            <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                            <div class="main-task-detail">
                                <div class="task-name" style="width: 700px"><span>{{item.name}}:({{item.description}})</span></div>
                                <div class="task-state">
                                    <span class="task-end blue">申请人：{{item.userName}}</span>
                                    <span v-if="item.isToday == -1" class="task-end red">截止时间：{{item.endTime| formatDate }}</span>
                                    <span v-if="item.isToday == 0" class="task-end orange">截止时间：{{item.endTime| formatDate }}</span>
                                    <span v-if="item.isToday == 1" class="task-end blue">截止时间：{{item.endTime| formatDate }}</span>
                                </div>
                            </div>
                            <div class="task-mark" style="position:relative; left:-10px">
                                <img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;">
                                <img v-else="" src="../assets/img/u431.png" alt="" >
                                <!--<img src="../assets/img/u431.png" alt="">-->
                                <span  class="mark-msg">{{item.projectName}}</span>
                            </div>
                            <div class="task-username">
                                <img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!=''"
                                     :src="item.avatarUrl" :alt="item.userName">
                                <span v-else="">{{item.userName}}</span>
                            </div>
                        </div>
                        <div class="pagination" v-show="this.question.wait.length>0">
                            <el-pagination
                                    @current-change="handleFinishedPage3"
                                    :current-page.sync="waitPage1.pageNum"
                                    :page-size="waitPage1.pageSize"
                                    :layout="finishedPageLayout3"
                                    :total="waitPage1.total">
                            </el-pagination>
                        </div>
                        <div v-show="question.wait.length==0" class="empty">
                            <h2>暂无数据</h2>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="审核通过" name="completed">
                        <div class="task-lis" v-for="item in question.accepted" @click="acceptedQuestion(item)">
                            <div class="head-img"><img src="../assets/img/finished.jpg"></div>
                            <div class="main-task-detail">
                                <div class="task-name" style="width: 700px;">
                                    <span>{{item.name}}:({{item.description}})</span>
                                </div>
                                <div class="task-state">
                                    <span class="task-end blue">申请人：{{item.userName}}</span>
                                    <span class="task-end green" v-if="item.completeTime">完成时间：{{item.completeTime| formatDate }}</span>
                                    <span class="task-end blue" v-else>截止时间: {{item.endTime| formatDate }}</span>
                                    <span class="task-time-opt"><i class="el-icon-circle-check"></i></span>
                                </div>
                            </div>
                            <div class="task-mark" style="position:relative; left:-10px">
                                <img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;">
                                <img v-else="" src="../assets/img/u431.png" alt="" >
                                <!--<img src="../assets/img/u431.png" alt="">-->
                                <span  class="mark-msg">{{item.projectName}}</span>
                            </div>
                            <div class="task-data-show">
                            </div>
                            <div class="task-username">
                                <img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!=''"
                                     :src="item.avatarUrl" :alt="item.userName">
                                <span v-else="">{{item.userName}}</span>
                            </div>
                        </div>
                        <div class="pagination" v-show="this.question.accepted.length>0">
                            <el-pagination
                                    @current-change="handleFinishedPage2"
                                    :current-page.sync="acceptedPage.pageNum"
                                    :page-size="acceptedPage.pageSize"
                                    :layout="finishedPageLayout2"
                                    :total="acceptedPage.total">
                            </el-pagination>
                        </div>
                        <div v-show="question.accepted.length==0" class="empty">
                            <h2>暂无数据</h2>
                        </div>
                    </el-tab-pane>
                </el-tabs>
                <p class="mic-title">积分求助转移</p>
                <el-tabs v-model="auditHelpTabsActiveName" @tab-click="handleClick">
                    <el-tab-pane label="待审核" name="wait">
                        <div class="task-lis" v-for="help in review.wait" @click="reviewDetail(help)">
                            <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                            <div class="main-task-detail">
                                <div class="task-name"><span>{{help.description}}</span></div>
                                <div class="task-state">
                                    <span class="task-end blue">求助目标：{{help.name.split(",")[0]}}</span>
                                    <span class="task-end blue">{{help.time| formatDate }}</span>
                                    <span class="task-time-opt"><i class="el-icon-edit"></i></span>
                                </div>
                            </div>
                            <div class="task-data-show">
                                <span class="task-score">求助转移：{{help.integral}} 分</span>
                            </div>
                            <div class="task-username">
                                <img class="task-avatar" v-if="help.avatarUrl && help.avatarUrl!=''"
                                     :src="help.avatarUrl">
                                <span v-else="">{{help.name.split(",")[1]}}</span>
                            </div>
                        </div>
                        <div class="pagination" v-show="this.review.wait.length>0">
                            <el-pagination
                                    @current-change="handleWaitPage"
                                    :current-page.sync="waitPage.pageNum"
                                    :page-size="waitPage.pageSize"
                                    :layout="waitPageLayout"
                                    :total="waitPage.total">
                            </el-pagination>
                        </div>
                        <div v-show="review.wait.length==0" class="empty">
                            <h2>暂无数据</h2>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="审核通过" name="completed">
                        <div class="task-lis" v-for="help in review.review" @click="reviewDetail(help)">
                            <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                            <div class="main-task-detail">
                                <div class="task-name"><span>{{help.description}}</span></div>
                                <div class="task-state">
                                    <span class="task-end blue">求助目标：{{help.name.split(",")[0]}}</span>
                                    <span class="task-end blue">{{help.time| formatDate }}</span>
                                    <span class="task-time-opt"><i class="el-icon-circle-check"></i></span>
                                </div>
                            </div>
                            <div class="task-data-show">
                                <span class="task-score">转移完成：-{{help.integral}} 分</span>
                            </div>
                            <div class="task-username">
                                <img class="task-avatar" v-if="help.avatarUrl && help.avatarUrl!=''"
                                     :src="help.avatarUrl">
                                <span v-else="">{{help.name.split(",")[1]}}</span>
                            </div>
                        </div>
                        <div class="pagination" v-show="this.review.review.length>0">
                            <el-pagination
                                    @current-change="handleReviewPage"
                                    :current-page.sync="reviewPage.pageNum"
                                    :page-size="reviewPage.pageSize"
                                    :layout="waitPageLayout"
                                    :total="reviewPage.total">
                            </el-pagination>
                        </div>
                        <div v-show="review.review.length==0" class="empty">
                            <h2>暂无数据</h2>
                        </div>
                    </el-tab-pane>
                </el-tabs>
            </div>
            <div v-show="userRole < 3">
                <p class="mic-title">{{userRole!=0?'请假申请':'请假申请审核'}}</p>
                <div class="my-task-detail">
                    <el-tabs v-model="activeLeaveName" @tab-click="handleClick">
                        <el-tab-pane :label="userRole!=0?'审核中':'待审核'" name="wait">
                            <!--@click="reviewDetail(help)"-->
                            <div class="task-lis" v-for="leave in leaveList.wait" @click="editLeaveDetail(leave,0)">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>{{leave.description}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">{{leave.createTime| formatDate }}</span>
                                        <span class="task-time-opt"><i class="el-icon-edit"></i></span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="leave.avatarUrl && leave.avatarUrl!=''"
                                         :src="leave.avatarUrl">
                                    <span v-else="">{{leave.userName}}</span>
                                </div>
                            </div>
                            <div class="pagination" v-show="this.leaveList.wait.length>0">
                                <el-pagination
                                        @current-change="handleLeaveWaitPage"
                                        :current-page.sync="leaveWaitPage.pageNum"
                                        :page-size="leaveWaitPage.pageSize"
                                        :layout="leaveWaitPageLayout"
                                        :total="leaveWaitPage.total">
                                </el-pagination>
                            </div>
                            <div v-show="leaveList.wait.length==0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane :label="userRole!=0?'完成审核':'审核通过'" name="review">
                            <div class="task-lis" v-for="leave in leaveList.pass" @click="leaveDetail(leave)">
                                <div class="head-img"><img src="../assets/img/auditSuccess.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>{{leave.description}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">{{leave.createTime| formatDate }}</span>
                                        <span class="task-time-opt"><i class="el-icon-circle-check"></i></span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="leave.avatarUrl && leave.avatarUrl!=''"
                                         :src="leave.avatarUrl">
                                    <span v-else="">{{leave.userName}}</span>
                                </div>
                            </div>
                            <div class="pagination" v-show="this.leaveList.pass.length>0">
                                <el-pagination
                                        @current-change="handleLeavePassPage"
                                        :current-page.sync="leavePassPage.pageNum"
                                        :page-size="leavePassPage.pageSize"
                                        :layout="leavePassPageLayout"
                                        :total="leavePassPage.total">
                                </el-pagination>
                            </div>
                            <div v-show="leaveList.pass.length==0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </div>
            <div v-show="userRole < 3">
                <p class="mic-title">{{userRole!=0?'加班申请':'加班申请审核'}}</p>
                <div class="my-task-detail">
                    <el-tabs v-model="activeEWorkName" @tab-click="handleClick">
                        <el-tab-pane :label="userRole!=0?'审核中':'待审核'" name="wait">
                            <div class="task-lis" v-for="eWork in eWorkList.wait" @click="showExtraWorkDetail(eWork.id)">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>{{eWork.reason}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">{{eWork.createTime| formatDate }}</span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="eWork.avatarUrl && eWork.avatarUrl!=''"
                                         :src="eWork.avatarUrl">
                                    <span v-else="">{{eWork.userName}}</span>
                                </div>
                            </div>
                            <div class="pagination" v-show="this.eWorkList.wait.length>0">
                                <el-pagination
                                        @current-change="handleEWorkWaitPage"
                                        :current-page.sync="eWorkWaitPage.pageNum"
                                        :page-size="eWorkWaitPage.pageSize"
                                        :layout="eWorkWaitPageLayout"
                                        :total="eWorkWaitPage.total">
                                </el-pagination>
                            </div>
                            <div v-show="eWorkList.wait.length==0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane :label="userRole!=0?'完成审核':'审核通过'" name="review">
                            <div class="task-lis" v-for="eWork in eWorkList.pass" @click="showExtraWorkDetail(eWork.id),isEWorkEdit = false">
                                <div class="head-img"><img src="../assets/img/auditSuccess.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>{{eWork.reason}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">{{eWork.createTime| formatDate }}</span>
                                        <span class="task-time-opt"><i class="el-icon-circle-check"></i></span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="eWork.avatarUrl && eWork.avatarUrl!=''"
                                         :src="eWork.avatarUrl">
                                    <span v-else="">{{eWork.userName}}</span>
                                </div>
                            </div>
                            <div class="pagination" v-show="this.eWorkList.pass.length>0">
                                <el-pagination
                                        @current-change="handleEWorkPassPage"
                                        :current-page.sync="eWorkPassPage.pageNum"
                                        :page-size="eWorkPassPage.pageSize"
                                        :layout="eWorkPassPageLayout"
                                        :total="eWorkPassPage.total">
                                </el-pagination>
                            </div>
                            <div v-show="eWorkList.pass.length==0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </div>
            <div v-show="userRole < 3">
                <p class="mic-title">{{userRole!=0?'补打卡申请':'补打卡申请审核'}}</p>
                <div class="my-task-detail">
                    <el-tabs v-model="activeRecheckName" @tab-click="handleClick">
                        <el-tab-pane :label="userRole!=0?'审核中':'待审核'" name="wait">
                            <div class="task-lis" v-for="recheck in recheckList.wait" @click="editRecheck(recheck)">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>补打卡原因: {{recheck.reason}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">补打卡时间: {{recheck.recheckTime| formatTime }}</span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="recheck.avatarUrl && recheck.avatarUrl!=''"
                                         :src="recheck.avatarUrl">
                                    <span v-else="">{{recheck.userName}}</span>
                                </div>
                            </div>
                            <div class="pagination" v-show="this.recheckList.wait.length>0">
                                <el-pagination
                                        @current-change="handleRecheckWaitPage"
                                        :current-page.sync="recheckWaitPage.pageNum"
                                        :page-size="recheckWaitPage.pageSize"
                                        :layout="recheckWaitPageLayout"
                                        :total="recheckWaitPage.total">
                                </el-pagination>
                            </div>
                            <div v-show="recheckList.wait.length==0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane :label="userRole!=0?'完成审核':'审核通过'" name="review">
                            <div class="task-lis" v-for="recheck in recheckList.pass" @click="showRecheck(recheck)">
                                <div class="head-img"><img src="../assets/img/auditSuccess.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>原因: {{recheck.reason}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">补打卡时间: {{recheck.recheckTime| formatTime }}</span>
                                        <span class="task-time-opt"><i class="el-icon-circle-check"></i></span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="recheck.avatarUrl && recheck.avatarUrl!=''"
                                         :src="recheck.avatarUrl">
                                    <span v-else="">{{recheck.userName}}</span>
                                </div>
                            </div>
                            <div class="pagination" v-show="this.recheckList.pass.length>0">
                                <el-pagination
                                        @current-change="handleRecheckPassPage"
                                        :current-page.sync="recheckPassPage.pageNum"
                                        :page-size="recheckPassPage.pageSize"
                                        :layout="recheckPassPageLayout"
                                        :total="recheckPassPage.total">
                                </el-pagination>
                            </div>
                            <div v-show="recheckList.pass.length==0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </div>
        </div>
        <el-dialog title="创建个人任务"
                size="tiny"
                custom-class="myDialog"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="createTaskVisible">
            <el-form :model="taskForm" :rules="rules" ref="taskForm" label-width="80px">
                <el-form-item label="项目" prop="projectId">
                    <el-select v-model="taskForm.projectId" placeholder="请选择">
                        <el-option
                                v-for="item in projectList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="开始日期" prop="beginTime">
                    <el-date-picker
                            v-model="taskForm.beginTime"
                            type="date"
                            format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="截止日期" prop="endTime">
                    <el-date-picker
                            v-model="taskForm.endTime"
                            type="date"
                            format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="工作量" prop="taskHours">
                    <el-input style="width:100px" v-model="taskForm.taskHours" :maxlength="6"></el-input>
                    小时
                </el-form-item>
                <el-form-item label="任务名称" prop="taskName">
                    <el-input v-model="taskForm.taskName"></el-input>
                </el-form-item>

                <el-form-item label="任务描述" prop="description">
                    <el-input type="textarea" v-model="taskForm.description" :rows="3"></el-input>
                </el-form-item>
                <el-form-item label="标签" prop="tags">
                    <el-select
                            v-model="taskForm.tags"
                            multiple
                            placeholder="请选择标签">
                        <el-option
                                v-for="item in tagList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="saveTaskInfo('taskForm')" :disabled="taskAble">立即创建</el-button>
            <el-button @click="createTaskVisible = false">取 消</el-button>
          </span>
        </el-dialog>
        <el-dialog title="创建线上问题(数据)记录"
                   @close="closeDialog1('questionForm')"
                size="tiny"
                custom-class="myDialog"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="createQuestionVisible">
            <el-form :model="questionForm" :rules="questionRules" ref="questionForm" label-width="80px">
                <el-form-item label="项目" prop="projectId">
                    <el-select v-model="questionForm.projectId" placeholder="请选择">
                        <el-option
                                v-for="item in projectList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="开始日期" prop="beginTime">
                    <el-date-picker
                            v-model="questionForm.beginTime"
                            type="date"
                            format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="截止日期" prop="endTime">
                    <el-date-picker
                            v-model="questionForm.endTime"
                            type="date"
                            format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="工作量" prop="workHour">
                    <el-input style="width:100px" v-model="questionForm.workHour" :maxlength="6"></el-input>
                    小时
                </el-form-item>
                <el-form-item label="主题" prop="name">
                    <el-input v-model="questionForm.name"></el-input>
                </el-form-item>

                <el-form-item label="描述" prop="description">
                    <el-input type="textarea" v-model="questionForm.description" :rows="3"></el-input>
                </el-form-item>
                <el-form-item label="上传图片">
                    <el-upload
                            class="upload-demo"
                            action=""
                            ref='uploadPic1'
                            :on-preview="handlePictureCardPreview"
                            :on-remove="handleRemove"
                            :http-request="upload"
                            :before-upload="beforeAvatarUpload"
                            list-type="picture-card">
                        <i class="el-icon-plus"></i>
                    </el-upload>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="saveQuestionInfo('questionForm')">立即创建</el-button>
            <el-button @click="createQuestionVisible = false">取 消</el-button>
          </span>
        </el-dialog>
        <el-dialog title="修改线上问题(数据)记录"
                @close="closeDialog('questionForm')"
                size="tiny"
                custom-class="myDialog"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="editQuestionVisible">
            <el-form :model="questionForm" :rules="questionRules" ref="questionForm" label-width="80px">
                <el-form-item label="项目" prop="projectId">
                    <el-select v-model="questionForm.projectId">
                        <el-option
                                v-for="item in projectList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="开始日期" prop="beginTime">
                    <el-date-picker
                            v-model="questionForm.beginTime"
                            type="date"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="截止日期" prop="endTime">
                    <el-date-picker
                            v-model="questionForm.endTime"
                            type="date"
                            format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="工作量" prop="workHour"  >
                    <el-input style="width:100px"  v-model="questionForm.workHour" :maxlength="6"></el-input>
                    小时
                </el-form-item>
                <el-form-item label="主题" prop="name">
                    <el-input v-model="questionForm.name"></el-input>
                </el-form-item>

                <el-form-item label="描述" prop="description">
                    <el-input type="textarea" v-model="questionForm.description" :rows="3"></el-input>
                </el-form-item>
                <el-form-item label="上传图片">
                    <el-upload
                            class="upload-demo"
                            action=""
                            ref='uploadPic'
                            :on-preview="handlePictureCardPreview"
                            :on-remove="handleRemove"
                            :http-request="upload"
                            :before-upload="beforeAvatarUpload"
                            list-type="picture-card">
                        <i class="el-icon-plus"></i>
                    </el-upload>
                </el-form-item>
                <el-form-item>
                    <span v-for="url in questionForm.urlList" v-show="questionForm.urlList.length != 0">
                        <img :src="url" v-if="url" style="margin-left: -50px;cursor: pointer" @click="showPic(url)">
                        <i class="el-icon-delete" style="margin-left: 10px" @click="removeUrl(url)"></i>
                        <!--<a style="color: blue;cursor: pointer" @click="showPic(url)">点击查看问题原件</a><i class="el-icon-delete" style="margin-left: 10px" @click="removeUrl(url)"></i><br/>-->
                    </span>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="editQuestionInfo('questionForm')">修改</el-button>
            <el-button @click="editQuestionVisible = false">取 消</el-button>
          </span>
        </el-dialog>
        <el-dialog title="线上问题(数据)记录"
                size="tiny"
                custom-class="myDialog"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="checkQuestionVisible">
            <el-form :model="questionForm" :rules="questionRules" ref="questionForm" label-width="100px">
                <el-form-item label="项目:" prop="projectId">
                    {{questionForm.projectName}}
                </el-form-item>
                <el-form-item label="开始日期:" prop="beginTime">
                    {{questionForm.beginTime}}
                </el-form-item>
                <el-form-item label="截止日期:" prop="endTime">
                    {{questionForm.endTime}}
                </el-form-item>
                <el-form-item label="工作量:" prop="workHour">
                    {{questionForm.workHour}} 小时
                </el-form-item>
                <el-form-item label="主题:" prop="name">
                    {{questionForm.name}}
                </el-form-item>
                <el-form-item label="描述:" prop="description">
                    {{questionForm.description}}
                </el-form-item>
                <el-form-item>
                    <span v-for="url in questionForm.urlList" v-show="questionForm.urlList.length != 0">
                        <img :src="url" v-if="url" style="margin-left: -50px;cursor: pointer" @click="showPic(url)">
                        <!--<a style="color: blue;cursor: pointer" @click="showPic(url)" class="fl">点击查看问题原件</a><br/>-->
                    </span>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button type="danger" @click="deleteQuestion(questionForm.oqrId)">删除</el-button>
            <el-button type="primary" icon="edit" @click="editQuestion(questionForm)"></el-button>
            <el-button type="primary" @click="acceptQuestion(questionForm.oqrId)">审核通过</el-button>
          </span>
        </el-dialog>
        <el-dialog title="线上问题(数据)记录"
                size="tiny"
                custom-class="myDialog"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="acceptedQuestionVisible">
            <el-form :model="questionForm" :rules="questionRules" ref="questionForm" label-width="100px">
                <el-form-item label="项目:" prop="projectId">
                    {{questionForm.projectName}}
                </el-form-item>
                <el-form-item label="开始日期:" prop="beginTime">
                    {{questionForm.beginTime}}
                </el-form-item>
                <el-form-item label="截止日期:" prop="endTime">
                    {{questionForm.endTime}}
                </el-form-item>
                <el-form-item label="工作量:" prop="workHour">
                    {{questionForm.workHour}} 小时
                </el-form-item>
                <el-form-item label="主题:" prop="name">
                    {{questionForm.name}}
                </el-form-item>
                <el-form-item label="描述:" prop="description">
                    {{questionForm.description}}
                </el-form-item>
                <el-form-item>
                    <span v-for="url in questionForm.urlList" v-show="questionForm.urlList.length != 0">
                        <img :src="url" v-if="url" style="margin-left: -50px;cursor: pointer" @click="showPic(url)">
                        <!--<a style="color: blue;cursor: pointer" @click="showPic(url)" class="fl">点击查看问题原件</a><br/>-->
                    </span>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button type="danger" @click="deleteQuestion(questionForm.oqrId)">删除</el-button>
            <el-button type="primary" icon="edit" @click="editQuestion(questionForm)"></el-button>
          </span>
        </el-dialog>
        <el-dialog title="线上问题(数据)记录"
                size="tiny"
                custom-class="myDialog"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="finishQuestionVisible">
            <el-form :model="questionForm" :rules="questionRules" ref="questionForm" label-width="100px">
                <el-form-item label="项目:" prop="projectId">
                    {{questionForm.projectName}}
                </el-form-item>
                <el-form-item label="开始日期:" prop="beginTime">
                    {{questionForm.beginTime}}
                </el-form-item>
                <el-form-item label="截止日期:" prop="endTime">
                    {{questionForm.endTime}}
                </el-form-item>
                <el-form-item label="工作量:" prop="workHour">
                    {{questionForm.workHour}} 小时
                </el-form-item>
                <el-form-item label="主题:" prop="name">
                    {{questionForm.name}}
                </el-form-item>
                <el-form-item label="描述:" prop="description">
                    {{questionForm.description}}
                </el-form-item>
                <el-form-item>
                    <span v-for="url in questionForm.urlList" v-show="questionForm.urlList.length != 0">
                        <img :src="url" v-if="url" style="margin-left: -50px;cursor: pointer" @click="showPic(url)">
                        <!--<a style="color: blue;cursor: pointer" @click="showPic(url)" class="fl">点击查看问题来源</a><br/>-->
                    </span>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button v-if="questionForm.reviewStatus == 1" type="warning">待审核</el-button>
                <el-button v-else type="success" @click="saveFinishQuestion(questionForm.oqrId)">完成</el-button>
            </span>
        </el-dialog>
        <el-dialog :visible.sync="dialogVisible" width="200px">
            <img width="100%" :src="dialogImageUrl" alt="">
        </el-dialog>
        <el-dialog title="积分求助转移" size="tiny" custom-class="myDialog" :close-on-click-modal="false"
                   :close-on-press-escape="false" :visible.sync="editHelpVisible" :show-close="false">
            <el-form :model="helpForm" ref="helpForm" :rules="helpRules" label-width="80px">
                <el-form-item label="任务详情" prop="description">
                    <el-input type="textarea" v-model="helpForm.description" :rows="3"></el-input>
                </el-form-item>
                <el-form-item label="求助目标" prop="userId">
                    <el-select v-model="helpForm.userId" placeholder="成员列表">
                        <el-option
                                v-for="item in userList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="发起者" prop="name" v-show="userRole==0">{{helpForm.name}}</el-form-item>
                <el-form-item label="原求助目标" prop="username" v-show="userRole==0">{{helpForm.username}}</el-form-item>
                <el-form-item label="任务积分" prop="integral">
                    <el-input type="input" v-model="helpForm.integral" style="width:100px"></el-input>
                </el-form-item>
                <el-form-item label="转移日期" prop="time" style="!important">
                    <el-date-picker
                            v-model="helpForm.time"
                            type="datetime"
                            format="yyyy-MM-dd HH:mm"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="saveHelpInfo('helpForm')" v-show="!editHelpDetailVisible"
                           :loading="isSaving">立即创建</el-button>
                <el-button type="primary" @click="saveHelpInfo('helpForm')" v-show="editHelpDetailVisible"
                           :loading="isSaving">立即更新</el-button>
                <el-button @click="editHelpVisible = false,clearHelpForm()">取 消</el-button>
              </span>
        </el-dialog>
        <el-dialog title="积分求助转移详情" custom-class="myDialog" :visible.sync="helpDetailVisible"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="10%" size="tiny">
            <el-form>
                <el-form-item class="task-form" label="任务描述：">{{helpDetail.description}}</el-form-item>
                <el-form-item class="task-form" label="发起者：">{{helpDetail.name}}</el-form-item>
                <el-form-item class="task-form" label="求助目标：">{{helpDetail.username}}</el-form-item>
                <el-form-item class="task-form" label="时间：">{{helpDetail.time | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="积分：">{{helpDetail.integral}}分</el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <div v-show="userRole==0&&auditHelpTabsActiveName=='wait'">
                    <el-tooltip content="删除该任务" placement="top">
                          <el-button type="danger" icon="delete" @click="deleteHelp(helpDetail.id)"></el-button>
                    </el-tooltip>
                    <el-tooltip content="编辑该任务" placement="top">
                     <el-button type="primary" icon="edit" @click="editHelpDetail(helpDetail)"></el-button>
                    </el-tooltip>
                    <el-button type="success" @click="acceptHelpChange(helpDetail.id)">审核通过</el-button>
                </div>
            </span>
        </el-dialog>
        <el-dialog title="任务延长申请详情" custom-class="myDialog" :visible.sync="expandDetailVisible"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="10%" size="tiny">
            <el-form v-if="userRole != 0">
                <el-form-item class="task-form" label="任务名称：">{{expandDetail.taskName}}</el-form-item>
                <el-form-item class="task-form" label="申请人：">{{expandDetail.userName}}</el-form-item>
                <el-form-item class="task-form" label="原因：">{{expandDetail.reason}}</el-form-item>
                <el-form-item class="task-form" label="截止时间：">{{expandDetail.endTime | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="延长时长：">{{expandDetail.hours}}小时</el-form-item>
            </el-form>
            <el-form v-if="userRole == 0">
                <el-form-item class="task-form" label="任务名称：">{{expandDetail.taskName}}
                </el-form-item>
                <el-form-item class="task-form" label="申请人：">{{expandDetail.userName}}</el-form-item>
                <el-form-item class="task-form" label="原因：">{{expandDetail.reason}}</el-form-item>
                <el-form-item class="task-form" label="开始时间：">{{expandDetail.beginTime | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="截止时间：">
                    <el-date-picker @change="changeExpandWeek"
                                    v-model="expandDetail.endTime"
                                    type="date"
                                    format="yyyy-MM-dd"
                                    placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item class="task-form" label="延长时长：">
                    <el-input v-model="expandDetail.hours" style="width: 30%"></el-input>
                    小时
                </el-form-item>
                <el-form-item>
                    <div v-for="item in expandWeekNumber">
                        <div class="add-member-basic-list add-member-basic-end clearfix">
                            <div class="fl" style="margin-left: 5px"><span class="star">*</span>第{{item.weekNumber}}周工作量({{item.range}})：
                            </div>
                            <input class="member-time-week" v-model="item.hours" :maxlength="6" style="width:80px">
                        </div>
                    </div>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <div v-show="userRole==0&&taskExpandTabsActiveName=='wait'">
                    <el-button type="danger" @click="deleteTaskExpand()">删除申请</el-button>
                    <el-button type="success" @click="acceptTaskExpand()">申请通过</el-button>
                    <!--<el-button type="success" @click="acceptTaskExpand()">申请驳回</el-button>-->
                </div>
            </span>
        </el-dialog>
        <el-dialog title="请假申请" size="tiny" custom-class="myDialog" :close-on-click-modal="false"
                   :close-on-press-escape="false" :visible.sync="editLeaveVisible" :show-close="false">
            <el-form :model="leaveForm" ref="leaveForm" :rules="leaveRules" label-width="110px">
                <el-form-item label="请假原因" prop="description">
                    <el-input type="textarea" v-model="leaveForm.description" :rows="3"></el-input>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="请假开始日期" prop="beginTime">
                    <el-date-picker
                            v-model="leaveForm.beginTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:00:00"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="请假结束日期" prop="endTime">
                    <el-date-picker
                            v-model="leaveForm.endTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:00:00"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="请假类型" prop="type">
                    <el-select
                            v-model="leaveForm.type"
                            placeholder="请选择类型">
                        <el-option v-for="item in leaveType" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="请假时长" prop="hours">
                    <el-input type="input" v-model="leaveForm.hours" style="width:100px"></el-input>
                    小时
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="saveLeaveInfo('leaveForm')" v-show="!editLeaveDetailVisible"
                           :loading="isSaving">立即创建</el-button>
                <el-button type="primary" @click="saveLeaveInfo('leaveForm')" v-show="editLeaveDetailVisible"
                           :loading="isSaving">立即更新</el-button>
                <el-button
                        @click="editLeaveVisible = false,editLeaveDetailVisible = false,clearLeaveForm()">取 消</el-button>
              </span>
        </el-dialog>

        <el-dialog title="加班申请详情" size="tiny" custom-class="myDialog" :close-on-click-modal="false"
                   :close-on-press-escape="false" :visible.sync="showEWorkDetailVisible" @close="closeEWorkDetail">
            <el-form>
                <el-form-item label="加班原因">{{eWorkDetail.reason}}</el-form-item>
                <el-form-item label="开始时间">{{eWorkDetail.beginTime | formatTime}}</el-form-item>
                <el-form-item label="截止时间">{{eWorkDetail.endTime | formatTime}}</el-form-item>
                <el-form-item label="加班时长">{{eWorkDetail.workHours}}小时</el-form-item>
                <el-form-item label="关联任务">

                </el-form-item>
                <div class="ctpc-member-con">
                    <div class="ctpc-member-list clearfix" v-for="(task,index) in eWorkDetail.tasks">
                        <span class="fl ctpc-member-head">{{eWorkDetail.userName}}</span>
                        <a class="fl ctpc-member-job-time" @click="toTask(task.id)"
                           style="color:black; cursor: pointer;">{{task.name|StringExtract}}</a>
                        <span class="fl ctpc-member-end-time">截止:{{task.endTime|formatDate}}</span>
                    </div>
                </div>
                <div style="margin-left: 430px" v-show="isEWorkEdit && !permit">
                    <el-tooltip content="编辑该申请" placement="top">
                        <el-button type="primary" icon="edit" @click="editEWorkForm(eWorkDetail)"></el-button>
                    </el-tooltip>
                    <el-tooltip content="删除该申请" placement="top">
                          <el-button type="danger" icon="delete" @click="deleteEWork(eWorkDetail.id)"></el-button>
                    </el-tooltip>
                </div>
                <div style="margin-left: 340px" v-show="isEWorkEdit && permit">
                    <el-tooltip content="编辑该申请" placement="top">
                        <el-button type="primary" icon="edit" @click="editEWorkForm(eWorkDetail)"></el-button>
                    </el-tooltip>
                    <el-tooltip content="删除该申请" placement="top">
                        <el-button type="danger" icon="delete" @click="deleteEWork(eWorkDetail.id)"></el-button>
                    </el-tooltip>
                    <el-tooltip content="审核通过" placement="top" v-show="permit">
                        <el-button type="success" @click="accessEWork(eWorkDetail.id)">审核通过</el-button>
                    </el-tooltip>
                </div>
                <div style="margin-left: 480px" v-show="!isEWorkEdit && permit">
                    <el-tooltip content="删除该申请" placement="top">
                        <el-button type="danger" icon="delete" @click="deleteEWork(eWorkDetail.id)"></el-button>
                    </el-tooltip>
                </div>
            </el-form>
        </el-dialog>
        <el-dialog title="编辑加班申请" size="tiny" custom-class="myDialog" :close-on-click-modal="false"
                   :close-on-press-escape="false" :visible.sync="editEWorkVisible"
                    @close="closeAddExtraWork">
            <el-form :model="extraWorkForm" ref="extraWorkForm" :rules="extraWorkRules" label-width="110px">
                <el-form-item label="加班原因" prop="reason">
                    <el-input type="textarea" v-model="extraWorkForm.reason" :rows="3"></el-input>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="加班开始时间" prop="beginTime">
                    <el-date-picker
                            v-model="extraWorkForm.beginTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:mm:00"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="加班结束时间" prop="endTime">
                    <el-date-picker
                            v-model="extraWorkForm.endTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:mm:00"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="加班时长" prop="hours">
                    <el-input type="input" v-model="extraWorkForm.workHours" style="width:100px"></el-input>
                    小时
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="关联任务" prop="taskIds">
                    <el-select v-model="extraWorkForm.taskIds"
                               multiple
                               filterable
                               allow-create
                               default-first-option
                               placeholder="请选关联任务">
                        <el-option v-for="task in myRunningTasks"
                                   :key="task.id"
                                   :label="task.name"
                                   :value="task.id"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="saveEditExtraWork('extraWorkForm',extraWorkForm.id)"
                           :loading="isSaving">立即修改</el-button>
                <el-button @click="editEWorkVisible = false">取 消</el-button>

            </span>
        </el-dialog>
        <el-dialog title="加班申请" size="tiny" custom-class="myDialog" :close-on-click-modal="false"
                   :close-on-press-escape="false" :visible.sync="createExtraWorkVisible" :show-close="false"
                    @close="closeAddExtraWork">
            <el-form :model="extraWorkForm" ref="extraWorkForm" :rules="extraWorkRules" label-width="110px">
                <el-form-item label="加班原因" prop="reason">
                    <el-input type="textarea" v-model="extraWorkForm.reason" :rows="3"></el-input>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="加班开始时间" prop="beginTime">
                    <el-date-picker
                            v-model="extraWorkForm.beginTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:mm:00"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="加班结束时间" prop="endTime">
                    <el-date-picker
                            v-model="extraWorkForm.endTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:mm:00"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="加班时长" prop="hours">
                    <el-input type="input" v-model="extraWorkForm.workHours" style="width:100px"></el-input>
                    小时
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="关联任务" prop="taskIds">
                    <el-select v-model="extraWorkForm.taskIds"
                               multiple
                               filterable
                               allow-create
                               default-first-option
                               placeholder="请选关联任务">
                        <el-option v-for="task in myRunningTasks"
                                   :key="task.id"
                                   :label="task.name"
                                   :value="task.id"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="saveExtraWork('extraWorkForm')"
                           :loading="isSaving">立即创建</el-button>
                <el-button
                        @click="createExtraWorkVisible = false">取 消</el-button>
              </span>
        </el-dialog>

        <el-dialog title="补打卡申请" size="tiny" custom-class="myDialog" :close-on-click-modal="false"
                   :close-on-press-escape="false" :visible.sync="reCheckVisible" :show-close="false"
                    @close="closeRecheckForm">
            <el-form :model="recheckForm" ref="recheckForm" :rules="recheckRules" label-width="110px">
                <el-form-item label="补打卡原因" prop="reason">
                    <el-input type="textarea" v-model="recheckForm.reason" :rows="3"></el-input>
                </el-form-item>
                <!--<span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>-->
                <el-form-item label="补打上班时间" prop="beginTime">
                    <el-date-picker
                            v-model="recheckForm.recheckInTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <!--<span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>-->
                <el-form-item label="补打下班时间" prop="beginTime">
                    <el-date-picker
                            v-model="recheckForm.recheckOutTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="saveRecheck('recheckForm')"
                           :loading="isSaving">立即创建</el-button>
                <el-button
                        @click="reCheckVisible = false">取 消</el-button>
              </span>
        </el-dialog>
        <el-dialog title="编辑补打卡申请" size="tiny" custom-class="myDialog" :close-on-click-modal="false"
                   :close-on-press-escape="false" :visible.sync="editRecheckVisible"
                   @close="closeRecheckForm">
            <el-form :model="recheckForm" ref="recheckForm" :rules="recheckRules" label-width="110px">
                <el-form-item label="补打卡原因" prop="reason">
                    <el-input type="textarea" v-model="recheckForm.reason" :rows="3"></el-input>
                </el-form-item>
                <!--<span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>-->
                <el-form-item label="补打上班时间" prop="beginTime" v-show="recheckForm.type == 0">
                    <el-date-picker
                            v-model="recheckForm.recheckInTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <!--<span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>-->
                <el-form-item label="补打下班时间" prop="beginTime" v-show="recheckForm.type == 1">
                    <el-date-picker
                            v-model="recheckForm.recheckOutTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:mm:ss"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="saveEditRecheck('recheckForm',recheckForm.id)"
                           :loading="isSaving">立即修改</el-button>
                <el-button type="danger" @click="deleteRecheck(recheckForm.id)"
                           :loading="isSaving">删除</el-button>
                <el-button v-show="permit" type="success" @click="accessRecheck(recheckForm.id)"
                           :loading="isSaving">审核</el-button>
                <el-button @click="editRecheckVisible = false">取 消</el-button>

            </span>
        </el-dialog>
        <el-dialog title="补打卡申请" size="tiny" custom-class="myDialog" :close-on-click-modal="false"
                   :close-on-press-escape="false" :visible.sync="showRecheckVisible"
                   @close="closeRecheckForm">
            <el-form>
                <el-form-item label="补打卡原因" prop="reason">
                    {{recheckForm.reason}}
                </el-form-item>
                <el-form-item label="补卡上班时间" prop="recheckTime" v-show="recheckForm.type == 0">
                    {{recheckForm.recheckTime | formatTime}}
                </el-form-item>
                <el-form-item label="补卡下班时间" prop="recheckTime" v-show="recheckForm.type == 1">
                    {{recheckForm.recheckTime | formatTime}}
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button v-show="permit" type="danger" @click="deleteRecheck(recheckForm.id)"
                           :loading="isSaving">删除</el-button>
                <el-button @click="showRecheckVisible = false">取 消</el-button>
            </span>
        </el-dialog>


        <el-dialog title="请假申请详情" :visible.sync="leaveDetailVisible" custom-class="myDialog"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="10%" size="tiny">
            <el-form>
                <el-form-item class="task-form" label="请假原因：">{{leaveForm.description}}</el-form-item>
                <el-form-item class="task-form" label="请假开始时间：">{{leaveForm.beginTime | formatTime}}</el-form-item>
                <el-form-item class="task-form" label="请假结束时间：">{{leaveForm.endTime | formatTime}}</el-form-item>
                <el-form-item class="task-form" label="类型：">{{leaveForm.typeName }}</el-form-item>
                <el-form-item class="task-form" label="请假时长：">{{leaveForm.hours}}小时</el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <div v-show="userRole==0">
                    <el-tooltip content="删除该申请" placement="top">
                          <el-button type="danger" icon="delete" @click="deleteLeave(leaveForm.id)"></el-button>
                    </el-tooltip>
                    <el-tooltip content="编辑该申请" placement="top">
                     <el-button type="primary" icon="edit" @click="editLeaveDetail(leaveForm,1)"></el-button>
                    </el-tooltip>
                    <el-button type="success" @click="acceptLeave(leaveForm.id)"
                               v-show="activeLeaveName=='wait'">审核通过</el-button>
                </div>
            </span>
        </el-dialog>
        <el-dialog title="月基准积分计算" :visible.sync="integralBasicVisible" custom-class="myDialog"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="tiny">
            工资：
            <el-input v-model="basicIntegral.salary" style="width: 100px"/>
            <div class="mic-item-title" style="font-size: 14px">基准积分 = 60 + （√(工资-5000)） *0.5</div>
            <div class="mic-item-title" style="font-size: 14px">月基准积分：<span>{{basicIntegral.integral}}</span></div>
            <span slot="footer" class="dialog-footer">
                <el-button type="success" @click="calculateIntegral" v-show="activeLeaveName=='wait'">计算基准积分</el-button>
            </span>
        </el-dialog>
        <el-dialog title="导入考勤记录到数据库" :visible.sync="uploadToMysqlVisible" custom-class="myDialog"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="tiny"
                    @close="closeSignInDialog">
            <el-upload
                    class="upload-demo"
                    ref="record"
                    action=""
                    :on-preview="handleRecordPreview"
                    :on-remove="handleRecordRemove"
                    :before-upload="beforeRecordUpload"
                    :http-request="uploadRecordToMysql">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                <div slot="tip" class="el-upload__tip">只能上传.dat文件，且不超过1MB</div>
            </el-upload>
        </el-dialog>

        <el-dialog title="导出考勤记录Excel" :visible.sync="excelSignInVisible" custom-class="myDialog"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="tiny"
                    @close="closeExcelDialog">
            <el-select clearable filterable no-match-text=" " v-model="workMonth3" placeholder="请选择月份"
                       size="small" style="width:200px">
                <el-option v-for="item in workMonths" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
            </el-select>
            <span slot="footer" class="dialog-footer">
                <el-button type="success" @click="signInExcel">导出</el-button>
            </span>
        </el-dialog>

        <el-dialog title="导入花名册到数据库" :visible.sync="uploadUserSortToMysqlVisible" custom-class="myDialog"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="tiny"
                   @close="closeUserSortDialog">
            <el-upload
                    class="upload-demo"
                    ref="usersort"
                    action=""
                    :on-preview="handleRecordPreview2"
                    :on-remove="handleRecordRemove2"
                    :before-upload="beforeUserSortUpload"
                    :http-request="uploadUserSortToMysql">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                <div slot="tip" class="el-upload__tip">只能上传.xls文件，且不超过1MB</div>
            </el-upload>
        </el-dialog>

        <el-dialog title="计算加班总时长" :visible.sync="showPersonalTotalEWrokTime" custom-class="myDialog"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="tiny"
        @close="closeMyEWorkCounter">
            <el-select clearable filterable no-match-text=" " v-model="workMonth1" placeholder="不选择及查询本年度"
                       size="small" style="width:200px">
                <el-option v-for="item in workMonths" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
            </el-select>
            <div class="mic-item-title" style="font-size: 14px;margin-top: 10px">加班总时长：<span>{{myTotalEWorkTime}}</span></div>
            <span slot="footer" class="dialog-footer">
                <el-button type="success" @click="fetchMyTotalEWorkTime">查询</el-button>
            </span>
        </el-dialog>
        <el-dialog title="计算加班总时长" :visible.sync="showTotalEWrokTime" custom-class="myDialog"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="tiny"
        @close="closeEWorkCounter">
            <el-select clearable filterable no-match-text=" " v-model="workMonth2" placeholder="不选择及查询本年度"
                        style="width:200px">
                <el-option v-for="item in workMonths" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
            </el-select>
                <el-select v-model="eWorkTimeUserId" clearable filterable   placeholder="筛选用户">
                    <el-option v-for="item in checkInUsers" :key="item.userId" :label="item.userName"
                               :value="item.userId"></el-option>
                </el-select>
            <div class="mic-item-title" style="font-size: 14px;margin-top: 10px">用户：<span>{{eWorkTimeUserName}}</span></div>
            <div class="mic-item-title" style="font-size: 14px;margin-top: 10px">加班总时长：<span>{{totalEWorkTime}}</span></div>
            <span slot="footer" class="dialog-footer">
                <el-button type="success" @click="fetchTotalEWorkTime">查询</el-button>
            </span>
        </el-dialog>

    </div>
</template>
<script>
    import TaskItem from './TaskItem'
    import QuestionItem from './QuestionItem'
    import Integral from './Intergral.vue'

    import http from '../lib/Http'
    import helper from '../lib/Helper'
    import moment from 'moment';
    import ElButton from "../../node_modules/element-ui/packages/button/src/button";
    import ElInput from "../../node_modules/element-ui/packages/input/src/input";
    import ElFormItem from "../../node_modules/element-ui/packages/form/src/form-item";

    moment.locale('zh-cn');

    export default {
        name: 'NavIndex',
        data() {
            var validateEmpty = (rule, value, callback) => {
                if (value.trim() == '') {
                    callback(new Error());
                } else {
                    callback();
                }
            };
            return {
                isSaving: false,
                activeName: 'doing',
                assessActiveName: 'waitAssess',
                integralBasicVisible: false,
                activeHelpName: 'wait',
                activeLeaveName: 'wait',
                activeEWorkName: 'wait',
                activeRecheckName: 'wait',
                auditTabsActiveName: 'wait',
                auditHelpTabsActiveName: 'wait',
                taskExpandTabsActiveName: 'wait',
                taskAble: false,
                editHelpDetailVisible: false,
                editHelpVisible: false,
                editLeaveVisible: false,
                leaveDetailVisible: false,
                helpDetailVisible: false,
                expandDetailVisible: false,
                createTaskVisible: false,
                editLeaveDetailVisible: false,
                developVisible: false,
                basicIntegral: {
                    integral: '',
                    salary: ''
                },
                commentedPage: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                finishedPage: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                finishedPage1: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                acceptedPage:{
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                waitPage:{
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                auditSuccessPage: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                expandPage: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                waitPage1: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                leaveWaitPage: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                reviewPage: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                leavePassPage: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                pickerOptions0: {
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7;
                    }
                },
                taskForm: {
                    taskName: '',
                    description: '',
                    projectId: '',
                    endTime: '',
                    beginTime: '',
                    priority: 1,
                    tags: [],
                    taskType: 1,
                    taskHours: '',
                    stageId: ''
                },
                helpForm: {
                    id: '',
                    description: '',
                    time: '',
                    userId: '',
                    integral: '',
                    name: '',
                    username: ''
                },
                leaveForm: {
                    id: '',
                    description: '',
                    beginTime: '',
                    endTime: '',
                    type: '',
                    hours: '',
                    typeName: ''
                },
                extraWorkForm:{
                    id:'',
                    reason:'',
                    beginTime: '',
                    endTime: '',
                    workHours: '',
                    taskIds:[]
                },
                eWorkWaitPage:{
                    pageNum: 1,
                    pageSize: 5,
                    total: 0
                },
                eWorkPassPage:{
                    pageNum: 1,
                    pageSize: 5,
                    total: 0
                },
                myRunningTasks:[],
                leaveType: [
                    {id: 1, name: '事假'},
                    {id: 2, name: '病假'},
                    {id: 3, name: '婚假'},
                    {id: 4, name: '产假'},
                    {id: 5, name: '丧假'},
                    {id: 6, name: '产检'},
                    {id: 7, name: '年假'},
                    {id: 8, name: '调休'},
                ],
                status: [
                    {id: 0, name: '审核中'},
                    {id: 1, name: '审核通过'},
                    {id: 2, name: '驳回'},
                ],
                userWeeks: [],
                weekTime: {
                    beginWeek: '',
                    endWeek: ''
                },
                leaveTimeRange: '',
                helpDetail: {
                    id: '',
                    description: '',
                    time: '',
                    userId: '',
                    integral: '',
                    name: '',
                    username: ''
                },
                expandDetail: {
                    teId: '',
                    userId: '',
                    userName: '',
                    hours: '',
                    taskName: '',
                    beginTime: '',
                    endTime: '',
                    userWeek: []
                },
                expandReview: {},
                expandWeekNumber: [],
                rules: {
                    projectId: [
                        {required: true, message: '项目不能为空', trigger: 'change'},
                    ],
                    endTime: [
                        {type: 'date', required: true, message: '截止时间不能为空', trigger: 'change'},
                    ],
                    beginTime: [
                        {type: 'date', required: true, message: '截止时间不能为空', trigger: 'change'},
                    ],
                    taskHours: [
                        {required: true, validator: validateEmpty, message: '工作量不能为空', trigger: 'blur'},
                    ],
                    taskName: [
                        {required: true, validator: validateEmpty, message: '任务名称不能为空', trigger: 'blur'},
                        {min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur'}
                    ],
                    description: [
                        {required: true, validator: validateEmpty, message: '描述不能为空', trigger: 'blur'},
                    ],
                    stageId: [
                        {required: true, message: '阶段不能为空', trigger: 'change'},
                    ],
                    tags: [
                        {type: 'array', required: true, message: '请至少选择一个标签', trigger: 'change'},
                    ]
                },
                helpRules: {
                    integral: [{required: true, message: '积分不能为空', trigger: 'blur'}],
                    description: [{required: true, message: '详情不能为空且不超过100字', trigger: 'blur', min: 1, max: 100}],
                    userId: [{required: true, message: '求助人不能为空', trigger: 'change'}],
                    time: [{type: 'date', required: true, message: '转移时间不能为空', trigger: 'change'}]
                },
                leaveRules: {
                    description: [{required: true, message: '请假原因不能为空并保证在6字以上', trigger: 'change', min: 6}],
                },
                extraWorkRules:{
                    reason:[{required: true, message: '加班原因不能为空', trigger: 'change'}]
                },
                recheckRules:{
                    reason:[{required: true, message: '补打卡原因不能为空', trigger: 'change'}]
                },
                task: {
                    doing: [],
                    test: [],
                    finished: [],
                    waitAssess: [],
                    commented: [],
                    waitAudit: [],
                    auditSuccess: [],
                    applyFail: []
                },
                taskExpand: {
                    doing: [],
                    finished: [],
                },
                review: {
                    wait: [],
                    review: []
                },
                leaveList: {
                    wait: [],
                    pass: []
                },
                eWorkList:{
                    wait:[],
                    pass:[]
                },
                projectList: [],
                stageList: [],
                tagList: [],
                userList: [],
                checkInUsers: [],
                integralItem: [
                    {
                        label: '本周',
                        score: ''
                    },
                    {
                        label: '本月',
                        score: ''
                    },
                    {
                        label: '年度总积分',
                        score: ''
                    }
                ],

                // sch --
                createQuestionVisible: false,
                createExtraWorkVisible: false,
                questionAble: false,
                questionForm: {
                    name: '',
                    description: '',
                    projectId: '',
                    endTime: '',
                    beginTime: '',
                    workHour: '',
                    urlList: []
                },
                questionRules: {
                    projectId: [
                        {required: true, message: '项目不能为空', trigger: 'change'},
                    ],
                    endTime: [
                        {type: 'date', required: true, message: '截止时间不能为空', trigger: 'blur'},
                    ],
                    beginTime: [
                        {type: 'date', required: true, message: '开始时间不能为空', trigger: 'blur'},
                    ],
                    workHour: [
                        {required: true,  validator: validateEmpty,message: '工作量不能为空', trigger: 'blur'},
                    ],
                    name: [
                        {required: true,  validator: validateEmpty,message: '任务名称不能为空', trigger: 'blur'},
                        {min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur'}
                    ],
                    description: [
                        {required: true,  validator: validateEmpty,message: '描述不能为空', trigger: 'blur'},
                        {min: 1, max: 200, message: '长度在 1 到 200 个字符', trigger: 'blur'}
                    ],

                },
                dialogImageUrl: '',
                dialogVisible: false,
                activeQuestionName: 'running',
                question: {
                    running: [],
                    completed: [],
                    wait:[],
                    accepted:[]
                },
                reqDTO: {},
                editQuestionVisible:false,
                checkQuestionVisible:false,
                acceptedQuestionVisible:false,
                finishQuestionVisible:false,
                fileList:[],
                questionActiveName:'wait',
                unreadNoticeNum:0,
                showEWorkDetailVisible:false,
                editEWorkVisible:false,
                isEWorkEdit:true,
                eWorkDetail:{},
                uploadToMysqlVisible: false,
                excelSignInVisible: false,
                fullscreenLoading: false,
                uploadUserSortToMysqlVisible: false,
                recordFileList:[],

                //考勤
                forgetSignIn:[],
                signInPage:{
                    pageNum:1,
                    total:0,
                    pageSize:20
                },
                mySignInPage:{
                    pageNum:1,
                    total:0,
                    pageSize:7
                },
                signInReqDTO:{
                    userId:'',
                    beginTime:'',
                    endTime:'',
                    pageNum:1,
                },
                mySignInReqDTO:{
                    userId:'',
                    beginTime:'',
                    endTime:'',
                    pageNum:1,
                },
                signInDaterange:'',
                mySignInDaterange:'',
                signInData:[],
                pickerOptions: {
                    shortcuts: [{
                        text: '本周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(moment().startOf('week').valueOf());
                            end.setTime(moment().endOf('week').valueOf());
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '本月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(moment().startOf('month').valueOf());
                            end.setTime(moment().endOf('month').valueOf());
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '本季度',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(moment().startOf('quarter').valueOf());
                            end.setTime(moment().endOf('quarter').valueOf());
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
                reCheckVisible:false,
                recheckForm:{
                    id:'',
                    reason:'',
                    recheckInTime: '',
                    recheckOutTime: '',
                    recheckTime: '',
                    userId:''
                },
                recheckWaitPage:{
                    pageNum: 1,
                    pageSize: 5,
                    total: 0
                },
                recheckPassPage:{
                    pageNum: 1,
                    pageSize: 5,
                    total: 0
                },
                recheckList:{
                    wait:[],
                    pass:[]
                },
                editRecheckVisible:false,
                showRecheckVisible:false,
                showPersonalTotalEWrokTime: false,
                showTotalEWrokTime: false,
                workMonth1:'',
                eWorkTimeReqDTO1:{
                    yearAndMonth:''
                },
                eWorkTimeReqDTO2:{
                    yearAndMonth:''
                },
                excelReqDTO:{
                    yearAndMonth:''
                },
                workMonth2:'',
                workMonth3:'',
                eWorkTimeUserId:'',
                eWorkTimeUserName:'',
                workMonths:[
                    {id:1,name:'一月'},
                    {id:2,name:'二月'},
                    {id:3,name:'三月'},
                    {id:4,name:'四月'},
                    {id:5,name:'五月'},
                    {id:6,name:'六月'},
                    {id:7,name:'七月'},
                    {id:8,name:'八月'},
                    {id:9,name:'九月'},
                    {id:10,name:'十月'},
                    {id:11,name:'十一月'},
                    {id:12,name:'十二月'}
                ],
                myTotalEWorkTime:'',
                totalEWorkTime:'',


                // -- sch
            };
        },
        created() {
            this.reload();
        },
        //数据初始化
        beforeMount() {
            let _this = this;
            //选中组织tab
            _this.$root.eventBus.$emit("handleTabSelected", "navIndex");
        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole <= 1;
            },
            userRole() {
                let userRole = helper.decodeToken().userRole;
                return userRole;
            },
            userId(){
                let userId = helper.decodeToken().userId;
                return userId;
            },
            finishedPageLayout() {
                if (this.finishedPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            finishedPageLayout1() {
                if (this.finishedPage1.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            finishedPageLayout2() {
                if (this.acceptedPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            finishedPageLayout3() {
                if (this.waitPage1.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            commentedPageLayout() {
                if (this.commentedPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            auditSuccessPageLayout() {
                if (this.auditSuccessPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            expandPageLayout() {
                if (this.expandPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            waitPageLayout() {
                if (this.waitPage.total > 5) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            leaveWaitPageLayout() {
                if (this.leaveWaitPage.total > 5) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            eWorkWaitPageLayout() {
                if (this.eWorkWaitPage.total > 5) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            recheckWaitPageLayout() {
                if (this.eWorkWaitPage.total > 5) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            recheckPassPageLayout() {
                if (this.eWorkWaitPage.total > 5) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            reviewPageLayout() {
                if (this.reviewPage.total > 5) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            leavePassPageLayout() {
                if (this.leavePassPage.total > 5) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            eWorkPassPageLayout() {
                if (this.eWorkPassPage.total > 5) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            signInPageLayout() {
                if (this.signInPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            mySignInPageLayout() {
                if (this.mySignInPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            }
        },
        filters: {
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY年MM月DD日');
            },
            formatDate2: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY/MM/DD');
            },
            formatTime: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY年MM月DD日 HH:mm:ss');
            },
            formatTime2: function (value) {
                if (!value) return '';
                return moment(value).format('HH:mm:ss');
            },
            StringExtract: function (name) {
                if (name.length > 18) {
                    return name.substring(0, 18) + '...';
                }
                return name;
            },
        },
        methods: {
            handleClick(tab, event) {
                // 点击进行中和已完成
                if (tab.label == '测试中') {
                    this.fetchTaskTesting();
                }
            },
            createTaskClick() {
                // 建个人任务
                this.createTaskVisible = true
            },
            reload() {
                this.initSignInTime()
                this.fetchIntegral()
                this.fetchTaskDoing()
                this.fetchTaskFinished()
                this.fetchTaskWaitAssess()
                this.fetchTaskCommented()
                this.fetchTaskWaitAudit()
                this.fetchProjectList()
                this.fetchStageList()
                this.fetchTagList()
                this.fetchUserList()
                this.fetchSignInUser()
                this.fetchUserLeaveList()
                this.fetchUserLeavePassList()
                this.fetchTaskExpandDoing()
                //this.fetchApplyFailTask();
                this.fetchTaskExpandSuccess()
                if (this.userRole === 0) {
                    // 所有审核通过的数据
                    this.fetchTaskAuditSuccess()
                    //待审核的积分转移，审核通过的积分转移
                    this.fetchHelpWaitAdmin()
                    this.fetchHelpReviewAdmin()
                } else {
                    this.fetchMyHelpWaitList();
                    this.fetchMyReviewSuccess();
                }
                this.fetchQuestionDoing();
                this.fetchQuestionCompleted();
                this.fetchQuestionAccepted();
                this.fetchQuestionWait();
                this.fetchUnreadNoticeNum();
                this.fetchMyRunningExtraWork();
                this.fetchMyCheckedExtraWork();
                this.fetchCheckingExtraWork();
                this.fetchCheckedExtraWork();
                this.fetchMyRecheckWait();
                this.fetchMyRecheckPass();
                this.fetchRecheckWait();
                this.fetchRecheckPass();
                this.fetchMySignInData();
                this.fetchSignInData();
            },
            saveTaskInfo(formName) {
                let vm = this;
                this.taskAble = true
                this.taskForm.endTime = moment(this.taskForm.endTime).toDate()
                this.taskForm.beginTime = moment(this.taskForm.beginTime).toDate()
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let userId = helper.decodeToken().userId;
                        var param = this.taskForm;
                        param.taskName = param.taskName.trim();
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD HH:00:00')
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD HH:00:00')
                        if (param.taskHours.length != parseFloat(param.taskHours).toString().length || parseFloat(param.taskHours) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            return false;
                        }
                        if (param.taskHours.trim() > 8 || param.taskHours.trim() < 0.1) {
                            this.$message({showClose: true, message: '工作量正确值应为0.1~8', type: 'error'});
                            return false;
                        }
                        if (moment(param.endTime).millisecond() < moment(param.beginTime).millisecond() || moment(param.endTime).week() != moment(param.beginTime).week()) {
                            this.$message({showClose: true, message: '请检查日期，个人任务请勿跨周进行', type: 'warning'});
                            return false;
                        }
                        var taskUsers = [{
                            userId: userId,
                            taskHours: param.taskHours.trim(),
                            beginTime: moment(param.beginTime).format('YYYY-MM-DD HH:00:00'),
                            endTime: moment(param.endTime).format('YYYY-MM-DD 23:59:59'),
                            description: param.description.trim()
                        }];
                        param['taskUsers'] = taskUsers;
                        http.zsyPostHttp('/task/create', param, (resp) => {
                            this.$message({showClose: true, message: '任务创建成功', type: 'success'});
                            this.$refs[formName].resetFields();
                            this.createTaskVisible = false
                            this.taskAble = false
                            vm.reload()
                        });
                    } else {
                        return false;
                    }
                }, err => {
                    this.taskAble = false
                });
            },
            makeUpItems(items) {
                const list = items;
                list.forEach((el) => {
                    let endTime = '', today = moment().format('YYYY-MM-DD')
                    if (el.status >= 2) {
                        endTime = el.completeTime
                    } else if ((el.reviewStatus == 1 || el.reviewStatus == 3) && el.taskUsers[0].status == 1) {
                        endTime = el.taskUsers[0].endTime
                    } else {
                        endTime = el.taskUsers[0].completeTime
                    }
                    endTime = moment(endTime).format('YYYY-MM-DD')
                    const diffDays = moment(today).diff(moment(endTime), 'days')
                    //const diffDays = Math.round(moment().diff(moment(endTime), 'days', true))
                    let endColor = '', endText = ''
                    endText = moment(endTime).calendar(null, {
                        sameDay: '[今天]',
                        nextDay: '[明天]',
                        nextWeek: 'L',
                        lastDay: '[昨天]',
                        lastWeek: 'L',
                        sameElse: 'L'
                    })
                    if (el.status < 3 && el.taskUsers[0].status == 1) {
                        if (diffDays == 0) {
                            endColor = 'orange'
                        } else if (diffDays > 0) {
                            endColor = 'red'
                        } else if (diffDays < 0) {
                            endColor = 'blue'
                        }
                        endText += ' 截止'
                    } else {
                        endColor = 'green'
                        endText += ' 完成'
                    }
                    el['endColor'] = endColor
                    el['endText'] = endText
                })
                return list
            },
            fetchIntegral() {
                http.zsyGetHttp(http.API_URI.USERINTEGRAL, null, (res) => {
                    let data = res.data;
                    let items = [];
                    items.push({label: '', score: data.week, time: this.getDateString('week')});
                    items.push({label: '', score: data.month, time: this.getDateString('month')});
                    items.push({label: '', score: data.year, time: this.getDateString('year')});
//                    items.push({label: '', score: 0, time:'' });
                    this.integralItem = items;
                    this.developVisible = data.developRole
                })
            },
            // 获取用户正在进行的任务
            fetchTaskDoing() {
                let vm = this
                http.zsyGetHttp('/task/doing', {}, (resp) => {
                    vm.task.doing = this.makeUpItems(resp.data)
                    vm.fetchMyTaskWaitAudit()
                })
            },
            // 获取用户测试中的任务
            fetchTaskTesting() {
                let vm = this
                http.zsyGetHttp('/task/testing', {}, (resp) => {
                    vm.task.test = resp.data
                })
            },
            // 获取用户已完成的任务
            fetchTaskFinished() {
                let vm = this
                http.zsyGetHttp(`/task/finished/${vm.finishedPage.pageNum}`, {}, (resp) => {
                    vm.finishedPage.pageNum = resp.data.pageNum
                    vm.finishedPage.total = resp.data.total
                    vm.task.finished = this.makeUpItems(resp.data.list)
                })
            },
            // 获取用户被打回任务
            fetchApplyFailTask() {
                let vm = this
                http.zsyGetHttp('/task/apply/fail', {}, (resp) => {
                    vm.task.applyFail = this.makeUpItems(resp.data)
                })
            },
            // 获取用户待评价的任务
            fetchTaskWaitAssess() {
                let vm = this
                http.zsyGetHttp('/task/waitAssess', {}, (resp) => {
                    vm.task.waitAssess = this.makeUpItems(resp.data)
                })
            },
            // 获取用户已评价的任务
            fetchTaskCommented() {
                let vm = this;
                http.zsyGetHttp(`/task/commented/${vm.commentedPage.pageNum}`, {}, (resp) => {
                    vm.commentedPage.total = resp.data.total
                    vm.task.commented = this.makeUpItems(resp.data.list)
                })
            },
            // 获取所有待审核的任务
            fetchTaskWaitAudit() {
                let vm = this
                http.zsyGetHttp('/task/pending/all', {}, (resp) => {
                    vm.task.waitAudit = this.makeUpItems(resp.data)
                })
            },
            fetchTaskExpandDoing() {
                let vm = this
                http.zsyGetHttp(`/task-expand/doing/0`, {}, (resp) => {
                    vm.taskExpand.doing = resp.data
                })
            },
            // 获取所有审核通过的任务
            fetchTaskAuditSuccess() {
                let vm = this
                http.zsyGetHttp(`/task/audit/success/all/${vm.auditSuccessPage.pageNum}`, {}, (resp) => {
                    vm.auditSuccessPage.pageNum = resp.data.pageNum
                    vm.auditSuccessPage.total = resp.data.total
                    vm.task.auditSuccess = this.makeUpItems(resp.data.list)
                })
            },
            // 获取所有审核通过的任务
            fetchTaskExpandSuccess() {
                let vm = this
                http.zsyPostHttp(`/task-expand/list`, {status: 1, pageNum: vm.expandPage.pageNum}, (resp) => {
                    vm.expandPage.pageNum = resp.data.pageNum
                    vm.expandPage.total = resp.data.total
                    vm.taskExpand.finished = resp.data.list
                })
            },
            // 获取我的待审核任务
            fetchMyTaskWaitAudit() {
                let vm = this
                http.zsyGetHttp('/task/pending', {}, (resp) => {
                    resp.data.forEach((task) => {
                        task.name += '(待审核)'
                    })
                    vm.task.doing = vm.task.doing.concat(this.makeUpItems(resp.data))
                })
            },
            //获取个人请假信息
            fetchUserLeaveList() {
                let vm = this
                http.zsyGetHttp('/userLeave/1/' + this.leaveWaitPage.pageNum, {}, (resp) => {
                    vm.leaveList.wait = resp.data.list
                    vm.leaveWaitPage.total = resp.data.total
                    vm.leaveWaitPage.pageNum = resp.data.pageNum
                })
            },
            //获取请假通过信息
            fetchUserLeavePassList() {
                let vm = this
                http.zsyGetHttp('/userLeave/3/' + this.leavePassPage.pageNum, {}, (resp) => {
                    vm.leaveList.pass = resp.data.list
                    vm.leavePassPage.total = resp.data.total
                    vm.leavePassPage.pageNum = resp.data.pageNum
                })
            },
            //保存用户转移积分
            saveHelpInfo(helpForm) {
                this.isSaving = true
                var param = this.helpForm;
                var help = {
                    userId: param.userId,
                    time: moment(param.time).format('YYYY-MM-DD HH:mm:ss'),
                    description: param.description.trim(),
                    integral: param.integral
                };
                this.$refs[helpForm].validate((valid) => {
                    if (valid) {
                        if (!this.isDecimal(param.integral)) {
                            this.$message({showClose: true, message: '积分格式错误', type: 'error'});
                            return false;
                        }
                        if (helper.decodeToken().userId == this.helpForm.userId) {
                            this.$message({showClose: true, message: '求助目标不能是自己，请重试', type: 'error'});
                            return false;
                        }
                        if (this.helpForm.id != '') {
                            http.zsyPostHttp('/integral/editHelpDetail/' + this.helpForm.id, help, (res) => {
                                this.$message({showClose: true, message: '转移积分更新成功，请等待审核', type: 'success'});
                                this.editHelpVisible = false;
                                this.clearHelpForm();
                                this.isSaving = false
                            });
                        } else {
                            http.zsyPostHttp('/integral/add', help, (res) => {
                                this.$message({showClose: true, message: '转移积分添加成功，请等待审核', type: 'success'});
                                this.editHelpVisible = false;
                                this.clearHelpForm();
                                this.isSaving = false
                            });
                        }
                        this.reload();
                    } else {
                        return false;
                    }
                });
            },
            //获取我的待审核的积分转移
            fetchMyHelpWaitList() {
                http.zsyGetHttp('/integral/getMyWaitList/' + this.waitPage.pageNum, {}, (resp) => {
                    this.review.wait = resp.data.list;
                    this.waitPage.total = resp.data.total;
                })
            },
            //所有待审核的积分转移
            fetchHelpWaitAdmin() {
                http.zsyGetHttp('/integral/getHelpWaitList/' + this.waitPage.pageNum, {}, (resp) => {
                    this.review.wait = resp.data.list;
                    this.waitPage.total = resp.data.total;
                })
            },
            fetchHelpReviewAdmin() {
                http.zsyGetHttp('/integral/getReviewList/' + this.reviewPage.pageNum, {}, (resp) => {
                    this.review.review = resp.data.list;
                    this.reviewPage.total = resp.data.total;
                })
            },
            // 获取所有我的审核通过的任务
            fetchMyReviewSuccess() {
                http.zsyGetHttp(`/integral/getMyReviewList/` + this.reviewPage.pageNum, {}, (resp) => {
                    this.review.review = resp.data.list;
                    this.reviewPage.total = resp.data.total;
                })
            },
            //删除积分求助
            deleteHelp(id) {
                this.$confirm('此操作将删除该任务, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyDeleteHttp(`/integral/delete/` + id, {}, (resp) => {
                        this.$message({showClose: true, message: '删除成功', type: 'success'});
                        this.reload();
                        this.helpDetailVisible = false;
                    })
                }).catch(() => {
                });
            },
            //审核通过积分求助
            acceptHelpChange(id) {
                this.$confirm('确认通过?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyGetHttp(`/integral/passReview/` + id, {}, (resp) => {
                        this.$message({showClose: true, message: '审核成功', type: 'success'});
                        this.reload();
                        this.helpDetailVisible = false;
                    })
                }).catch(() => {
                });
            },
            deleteTaskExpand() {
                this.$confirm('确认删除申请?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyDeleteHttp(`/task-expand/` + this.expandDetail.teId, {}, (resp) => {
                        this.$message({showClose: true, message: '删除成功', type: 'success'});
                        this.reload();
                        this.expandDetailVisible = false;
                    })
                }).catch(() => {
                });
            },
            acceptTaskExpand() {
                var sumHours = 0;
                for (var i = 0; i < this.expandWeekNumber.length; i++) {
                    if (this.expandWeekNumber[i].hours == '' || this.expandWeekNumber[i].hours === undefined) {
                        this.expandWeekNumber[i].hours = 0
                    }
                    var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.expandWeekNumber[i].hours);
                    if (!ishours && ishours != 0) {
                        this.$message({showClose: true, message: '工作量填写错误', type: 'error'});
                        return false;
                    }
                    if (this.expandWeekNumber[i].hours > 100 || this.expandWeekNumber[i].hours < 0) {
                        this.$message({showClose: true, message: '工作量正确值应为0~99.99', type: 'error'});
                        return false;
                    }
                    sumHours += parseFloat(this.expandWeekNumber[i].hours)
                }
                if (sumHours != this.expandDetail.hours) {
                    this.$message({showClose: true, message: '工作量总和应为延长时间，请检查', type: 'error'});
                    return false;
                }
                this.$confirm('确认审核通过?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    this.expandReview.endTime = moment(this.expandDetail.endTime).format('YYYY-MM-DD 23:59:59')
                    this.expandReview.teId = this.expandDetail.teId
                    this.expandReview.status = 1
                    this.expandReview.weeks = this.expandWeekNumber
                    this.expandReview.hours = sumHours
                    http.zsyPostHttp(`/task-expand/review`, this.expandReview, (resp) => {
                        this.$message({showClose: true, message: '审核成功', type: 'success'});
                        this.expandReview = {}
                        this.reload();
                        this.expandDetailVisible = false;
                    })
                }).catch(() => {
                });
            },
            getExpandDetail(expand) {
                this.expandDetailVisible = true;
                this.expandDetail.teId = expand.teId;
                this.expandDetail.userName = expand.userName;
                this.expandDetail.taskName = expand.taskName;
                this.expandDetail.userId = expand.userId;
                this.expandDetail.reason = expand.reason;
                this.expandDetail.hours = expand.hours
                this.expandDetail.taskId = expand.taskId
                this.expandDetail.endTime = expand.endTime
                this.expandDetail.beginTime = expand.beginTime
                this.changeExpandWeek()
            },
            //积分转移详情
            reviewDetail(help) {
                this.helpDetailVisible = true;
                this.helpDetail.time = moment(help.time).toDate();
                this.helpDetail.integral = help.integral;
                this.helpDetail.description = help.description;
                this.helpDetail.name = help.name.split(",")[1];
                this.helpDetail.username = help.name.split(",")[0];
                this.helpDetail.id = help.id;
                this.helpDetail.userId = help.userId;
            },
            editHelpDetail(help) {
                this.editHelpDetailVisible = true;
                this.helpForm.description = help.description;
                this.helpForm.integral = help.integral.toString();
                this.helpForm.time = moment(help.time).toDate();
                this.helpForm.id = help.id;
                this.helpForm.name = help.name;
                this.helpForm.userId = help.userId
                this.helpForm.username = help.username;
                this.editHelpVisible = true;
                this.helpDetailVisible = false;
            },
            leaveDetail(leave) {
                this.leaveDetailVisible = true
                this.leaveForm.id = leave.id
                this.leaveForm.description = leave.description
                this.leaveForm.beginTime = moment(leave.beginTime).toDate();
                this.leaveForm.endTime = moment(leave.endTime).toDate();
                this.leaveForm.typeName = leave.typeName
                this.leaveForm.type = leave.type
                this.leaveForm.hours = leave.hours
                this.userWeeks = leave.userWeeks
            },
            editLeaveDetail(leave, index) {
                if (this.userRole == 0 && index != 1) {
                    this.leaveDetail(leave)
                } else {
                    this.editLeaveDetailVisible = true;
                    this.leaveDetailVisible = false;
                    this.editLeaveVisible = true
                    this.leaveForm.id = leave.id
                    this.leaveForm.description = leave.description
                    this.leaveForm.beginTime = moment(leave.beginTime).toDate();
                    this.leaveForm.endTime = moment(leave.endTime).toDate();
                    this.leaveForm.type = leave.type
                    this.leaveForm.hours = leave.hours
                    this.userWeeks = leave.userWeeks
                }

            },
            clearHelpForm() {
                this.helpForm.integral = '';
                this.helpForm.time = '';
                this.helpForm.description = '';
                this.helpForm.userId = '';
                this.helpForm.id = '';
                this.helpForm.username = '';
                this.helpForm.name = '';
                this.editHelpDetailVisible = false;
            },
            clearLeaveForm() {
                this.leaveForm.id = this.leaveForm.endTime = this.leaveForm.beginTime = this.leaveForm.hours = this.leaveForm.type = this.leaveForm.description = ''
                this.userWeeks = []
            },
            clearExtraWorkForm(){
                this.extraWorkForm.reason = this.extraWorkForm.beginTime = this.extraWorkForm.endTime = this.extraWorkForm.workHours = '';
                this.extraWorkForm.taskIds = [];
                this.extraWorkForm.id = ''
            },
            //待审核积分转移页
            handleWaitPage(currentPage) {
                this.waitPage.pageNum = currentPage;
                if (this.userRole > 0) {
                    this.fetchMyHelpWaitList();
                } else {
                    this.fetchHelpWaitAdmin();
                }
            },
            handleLeaveWaitPage(currentPage) {
                this.leaveWaitPage.pageNum = currentPage;
                if (this.userRole > 0) {
                    this.fetchUserLeaveList();
                } else {
                    this.fetchUserLeaveList();
                }
            },
            handleEWorkWaitPage(currentPage) {
                this.eWorkWaitPage.pageNum = currentPage;
                if (this.userRole > 0) {
                    this.fetchMyRunningExtraWork();
                } else {
                    this.fetchCheckingExtraWork();
                }
            },
            handleLeavePassPage(currentPage) {
                this.leavePassPage.pageNum = currentPage;
                if (this.userRole > 0) {
                    this.fetchUserLeavePassList();
                } else {
                    this.fetchUserLeavePassList();
                }
            },
            handleEWorkPassPage(currentPage) {
                this.eWorkPassPage.pageNum = currentPage;
                if (this.userRole > 0) {
                    this.fetchMyCheckedExtraWork();
                } else {
                    this.fetchCheckedExtraWork();
                }
            },
            saveLeaveInfo(formName) {
                this.isSaving = true
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.leaveForm.type == '' || this.leaveForm.type == null) {
                            this.$message({showClose: true, message: '请选择请假类型', type: 'warning'});
                            return false;
                        }
                        var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.leaveForm.hours);
                        if (!ishours) {
                            this.$message({showClose: true, message: '请假时长填写错误', type: 'error'});
                            return false;
                        }
                        if (this.leaveForm.hours > 99999.9 || this.leaveForm.hours < 0) {
                            this.$message({showClose: true, message: '请假时长正确值应为0~99999.9', type: 'error'});
                            return false;
                        }
                        this.weekTime.beginWeek = moment(this.leaveForm.beginTime).week()
                        this.weekTime.endWeek = moment(this.leaveForm.endTime).week()
                        this.leaveForm.beginTime = moment(this.leaveForm.beginTime).toDate()
                        this.leaveForm.endTime = moment(this.leaveForm.endTime).toDate()

                        let form = this.leaveForm
                        form.beginTime = moment(form.beginTime).format('YYYY-MM-DD HH:00:00')
                        form.endTime = moment(form.endTime).format('YYYY-MM-DD HH:00:00')
                        if (this.weekTime.beginWeek != this.weekTime.endWeek || moment(form.beginTime).isAfter(moment(form.endTime)) || moment(form.beginTime).isSame(moment(form.endTime))) {
                            this.$message({showClose: true, message: '请假日期有误,请检查(请假时间不能跨周、相同)', type: 'warning'});
                            return false;
                        }
                        form['userWeeks'] = this.userWeeks
                        if (form.id != '') {
                            http.zsyPostHttp('/userLeave/editLeaveDetail/' + form.id, form, (resp) => {
                                this.$message({
                                    showClose: true,
                                    message: '请假申请修改成功',
                                    type: 'success'
                                });
                                this.fetchUserLeaveList();
                                this.clearLeaveForm();
                                this.editLeaveVisible = false
                                this.editLeaveDetailVisible = false
                                this.isSaving = false
                            }, err => {
                                this.isSaving = false
                            })
                        } else {
                            http.zsyPostHttp('/userLeave/add', form, (resp) => {
                                this.$message({
                                    showClose: true,
                                    message: '新建请假申请成功',
                                    type: 'success'
                                });
                                this.fetchUserLeaveList();
                                this.clearLeaveForm()
                                this.editLeaveVisible = false
                                this.editLeaveDetailVisible = false
                                this.isSaving = false
                            }, err => {
                                this.isSaving = false
                            })
                        }
                    }

                })
            },
            saveExtraWork(formName) {
                this.isSaving = true
                if (this.extraWorkForm.reason == null || this.extraWorkForm.reason == ''){
                    this.$message({showClose: true, message: '加班原因不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.beginTime == null || this.extraWorkForm.beginTime == ''){
                    this.$message({showClose: true, message: '加班开始时间不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.endTime == null || this.extraWorkForm.endTime == ''){
                    this.$message({showClose: true, message: '加班结束时间不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.endTime < this.extraWorkForm.beginTime){
                    this.$message({showClose: true, message: '加班结束时间不能在开始时间之前', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.workHours == null || this.extraWorkForm.workHours == ''){
                    this.$message({showClose: true, message: '加班时长不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.taskIds.length == 0){
                    this.$message({showClose: true, message: '关联任务不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.extraWorkForm.workHours);
                        if (!ishours) {
                            this.$message({showClose: true, message: '加班时长填写错误', type: 'error'});
                            return false;
                        }
                        if (this.extraWorkForm.workHours > 99999.9 || this.extraWorkForm.workHours < 0) {
                            this.$message({showClose: true, message: '加班时长正确值应为0~99999.9', type: 'error'});
                            return false;
                        }
                        this.extraWorkForm.beginTime = moment(this.extraWorkForm.beginTime).toDate()
                        this.extraWorkForm.endTime = moment(this.extraWorkForm.endTime).toDate()

                        let form = this.extraWorkForm
                        form.beginTime = moment(form.beginTime).format('YYYY-MM-DD HH:mm:00')
                        form.endTime = moment(form.endTime).format('YYYY-MM-DD HH:mm:00')
                            http.zsyPostHttp('/extra-work/add', form, (resp) => {
                                this.$message({
                                    showClose: true,
                                    message: '新建加班申请成功',
                                    type: 'success'
                                });
                                this.clearExtraWorkForm();
                                this.fetchMyRunningExtraWork();
                                this.createExtraWorkVisible = false
                                this.isSaving = false
                            }, err => {
                                this.isSaving = false
                            })
                        }

                })
            },
            saveEditExtraWork(formName,ewId) {
                this.isSaving = true
                if (this.extraWorkForm.reason == null || this.extraWorkForm.reason == ''){
                    this.$message({showClose: true, message: '加班原因不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.beginTime == null || this.extraWorkForm.beginTime == ''){
                    this.$message({showClose: true, message: '加班开始时间不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.endTime == null || this.extraWorkForm.endTime == ''){
                    this.$message({showClose: true, message: '加班结束时间不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.workHours == null || this.extraWorkForm.workHours == ''){
                    this.$message({showClose: true, message: '加班时长不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.taskIds.length == 0){
                    this.$message({showClose: true, message: '关联任务不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.extraWorkForm.workHours);
                        if (!ishours) {
                            this.$message({showClose: true, message: '加班时长填写错误', type: 'error'});
                            return false;
                        }
                        if (this.extraWorkForm.workHours > 99999.9 || this.extraWorkForm.workHours < 0) {
                            this.$message({showClose: true, message: '加班时长正确值应为0~99999.9', type: 'error'});
                            return false;
                        }
                        this.extraWorkForm.beginTime = moment(this.extraWorkForm.beginTime).toDate()
                        this.extraWorkForm.endTime = moment(this.extraWorkForm.endTime).toDate()

                        let form = this.extraWorkForm
                        form.beginTime = moment(form.beginTime).format('YYYY-MM-DD HH:00:00')
                        form.endTime = moment(form.endTime).format('YYYY-MM-DD HH:00:00')
                            http.zsyPutHttp('/extra-work/update/'+ewId, form, (resp) => {
                                this.$message({
                                    showClose: true,
                                    message: '修改加班申请成功',
                                    type: 'success'
                                });
                                this.clearExtraWorkForm();
                                this.fetchMyRunningExtraWork();
                                this.fetchCheckingExtraWork();
                                this.editEWorkVisible = false
                                this.isSaving = false
                            }, err => {
                                this.isSaving = false
                            })
                        }

                })
            },

            closeAddExtraWork(){
                this.clearExtraWorkForm()
                this.isSaving = false
            },
            deleteLeave(id) {
                http.zsyDeleteHttp('/userLeave/delete/' + id, null, (resp) => {
                    this.$message({
                        showClose: true,
                        message: '请假申请删除成功',
                        type: 'success'
                    });
                    this.fetchUserLeaveList();
                    this.clearLeaveForm()
                    this.leaveDetailVisible = false
                })
            },
            acceptLeave(id) {
                http.zsyGetHttp('/userLeave/passLeave/' + id, null, (resp) => {
                    this.$message({
                        showClose: true,
                        message: '请假申请审核完成',
                        type: 'success'
                    });
                    this.fetchUserLeaveList();
                    this.fetchUserLeavePassList();
                    this.clearLeaveForm()
                    this.leaveDetailVisible = false
                })
            },
            handleReviewPage(currentPage) {
                this.reviewPage.pageNum = currentPage;
                if (this.userRole > 0) {
                    this.fetchMyReviewSuccess();
                } else {
                    this.fetchHelpReviewAdmin();
                }
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
            handleFinishedPage(currentPage) {
                this.finishedPage.pageNum = currentPage
                this.fetchTaskFinished()
            },
            handleFinishedPage1(currentPage) {
                this.finishedPage1.pageNum = currentPage
                this.fetchQuestionCompleted()
            },
            handleFinishedPage2(currentPage) {
                this.acceptedPage.pageNum = currentPage
                this.fetchQuestionAccepted()
            },
            handleFinishedPage3(currentPage) {
                this.waitPage1.pageNum = currentPage
                this.fetchQuestionWait()
            },
            handleCommentedPage(currentPage) {
                this.commentedPage.pageNum = currentPage
                this.fetchTaskCommented()
            },
            handleAuditSuccessPage(currentPage) {
                this.auditSuccessPage.pageNum = currentPage
                this.fetchTaskAuditSuccess()
            },
            handleExpandPage(currentPage) {
                this.expandPage.pageNum = currentPage
//                this.fetchTaskExpandSuccess()
            },
            isDecimal(str) {
                var regu = /^[-]{0,1}[0-9]{1,}$/;
                if (regu.test(str)) {
                    return true;
                }
                var re = /^[-]{0,1}(\d+)[\.]+(\d+)$/;
                if (re.test(str)) {
                    if (RegExp.$1 == 0 && RegExp.$2 == 0) return false;
                    return true;
                } else {
                    return false;
                }

            },
            calculateIntegral() {
                if (this.basicIntegral.salary < 0) {
                    this.$message({
                        showClose: true,
                        message: '工资请选择正数',
                        type: 'warning'
                    });
                } else if (this.basicIntegral.salary < 5000) {
                    this.basicIntegral.integral = (60 - Math.sqrt(5000 - this.basicIntegral.salary) * 0.5).toFixed(2);
                } else {
                    this.basicIntegral.integral = (60 + Math.sqrt(this.basicIntegral.salary - 5000) * 0.5).toFixed(2);
                }
            },
            getDateString(date) {//时间期限
                let now = new Date();
                let curMonth = now.getMonth();
                let curYear = now.getFullYear();
                ;
                let startMonth = 0;
                let nowDayOfWeek = now.getDay();
                nowDayOfWeek = nowDayOfWeek == 0 ? 7 : nowDayOfWeek;
                if (date == "month") {//本月的开始结束时间
                    return moment(new Date(curYear, curMonth, 1)).format('YYYY-MM-DD') + "--" + moment(new Date(curYear, curMonth + 1, 1) - 1).format('YYYY-MM-DD');
                } else if (date == "week") {//本季度的开始结束时间
                    return moment(new Date(curYear, curMonth, now.getDate() - nowDayOfWeek + 1)).format('YYYY-MM-DD') + "--" + moment(new Date(curYear, curMonth, now.getDate() + (6 - nowDayOfWeek) + 1)).format('YYYY-MM-DD');
                } else if (date == "year") {//本年的开始结束时间
                    return moment(new Date(now.getFullYear(), 0, 1)).format('YYYY-MM-DD') + "--" + moment(new Date(now.getFullYear() + 1, 0, 1) - 1).format('YYYY-MM-DD');
                }
            },
            changeExpandWeek() {
                if (this.expandDetail.beginTime == null || this.expandDetail.endTime == null) {
                    return
                }
                this.expandWeekNumber = [];
                let weekData = '';
                let param = this.expandWeekNumber;
                this.expandDetail.beginWeek = moment(this.expandDetail.beginTime).week()
                this.expandDetail.endWeek = moment(this.expandDetail.endTime).week()
                let beginYear = moment(this.expandDetail.beginTime).year();
                let endYear = moment(this.expandDetail.endTime).year();
                if (beginYear != endYear) {
                    for (let i = this.expandDetail.beginWeek; i < moment(this.expandDetail.beginTime).weeksInYear() + 1; i++) {
                        weekData = {
                            'weekNumber': i,
                            'hours': '',
                            'year': beginYear,
                            'range': moment().year(beginYear).week(i).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(i).endOf('week').format('MM-DD')
                        };
                        param.push(weekData)
                    }
                    for (let i = 1; i < this.expandDetail.endWeek + 1; i++) {
                        weekData = {
                            'weekNumber': i,
                            'hours': '',
                            'year': endYear,
                            'range': moment().year(endYear).week(i).startOf('week').format('MM-DD') + '至' + moment().year(endYear).week(i).endOf('week').format('MM-DD')
                        };
                        param.push(weekData)
                    }
                }
                if (this.expandDetail.beginWeek == this.expandDetail.endWeek) {
                    weekData = {
                        'weekNumber': this.expandDetail.beginWeek,
                        'hours': this.expandDetail.hours,
                        'year': beginYear,
                        'range': moment().year(beginYear).week(this.expandDetail.beginWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.expandDetail.beginWeek).endOf('week').format('MM-DD')
                    };
                    param.push(weekData)
                } else if (this.expandDetail.endWeek - this.expandDetail.beginWeek > 1) {
                    for (let i = this.expandDetail.beginWeek; i < this.expandDetail.endWeek + 1; i++) {
                        weekData = {
                            'weekNumber': i,
                            'hours': '',
                            'year': beginYear,
                            'range': moment().week(i).year(beginYear).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(i).endOf('week').format('MM-DD')
                        };
                        param.push(weekData)
                    }
                } else if (this.expandDetail.endWeek - this.expandDetail.beginWeek == 1) {
                    param.push({
                        'weekNumber': this.expandDetail.beginWeek,
                        'hours': '',
                        'year': beginYear,
                        'range': moment().year(beginYear).week(this.expandDetail.beginWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.expandDetail.beginWeek).endOf('week').format('MM-DD')
                    })
                    param.push({
                        'weekNumber': this.expandDetail.endWeek,
                        'hours': '',
                        'year': beginYear,
                        'range': moment().year(beginYear).week(this.expandDetail.endWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.expandDetail.endWeek).endOf('week').format('MM-DD')
                    })
                }
                this.expandWeekNumber = param
            },
            // sch --
            //清空数据
            clearQuestionForm(){
              this.questionForm.name = null;
              this.questionForm.description = null;
              this.questionForm.beginTime = null;
              this.questionForm.endTime = null;
              this.questionForm.workHour = null;
              this.questionForm.urlList = [];
              this.questionForm.projectId = null;
            },
            //创建线上问题记录
            createQuestionClick() {
                this.createQuestionVisible = true
            },
            //完成线上问题
            finishQuestion(item){
                this.finishQuestionVisible = true
                this.questionForm.oqrId = item.oqrId
                this.questionForm.name = item.name;
                this.questionForm.beginTime = moment(item.beginTime).format('YYYY-MM-DD');
                this.questionForm.endTime = moment(item.endTime).format('YYYY-MM-DD');
                this.questionForm.projectId = item.projectId;
                this.questionForm.projectName = item.projectName;
                this.questionForm.description = item.description;
                this.questionForm.workHour = item.workHour;
                this.questionForm.urlList = item.urlList;
                this.questionForm.reviewStatus = item.reviewStatus;
            },
            //编辑问题
            editQuestion(item){
                this.fetchQuestionDoing()
                this.acceptedQuestionVisible = false;
                this.checkQuestionVisible = false;
                this.editQuestionVisible = true;
                this.questionForm.oqrId = item.oqrId;
                this.questionForm.name = item.name;
                this.questionForm.beginTime = item.beginTime;
                this.questionForm.endTime = item.endTime;
                this.questionForm.projectId = item.projectId;
                this.questionForm.description = item.description;
                this.questionForm.workHour = String(item.workHour);
                this.questionForm.urlList = item.urlList == null ? []:item.urlList;
            },
            //审核问题
            checkQuestion(item){
                this.fetchQuestionWait()
                this.checkQuestionVisible = true
                this.questionForm.oqrId = item.oqrId
                this.questionForm.name = item.name;
                this.questionForm.beginTime = moment(item.beginTime).format('YYYY-MM-DD');
                this.questionForm.endTime = moment(item.endTime).format('YYYY-MM-DD');
                this.questionForm.projectId = item.projectId;
                this.questionForm.projectName = item.projectName;
                this.questionForm.description = item.description;
                this.questionForm.workHour = item.workHour;
                this.questionForm.urlList = item.urlList;
            },
            //打开已完成审核的弹框
            acceptedQuestion(item){
                this.acceptedQuestionVisible = true
                this.questionForm.oqrId = item.oqrId
                this.questionForm.name = item.name;
                this.questionForm.beginTime = moment(item.beginTime).format('YYYY-MM-DD');
                this.questionForm.endTime = moment(item.endTime).format('YYYY-MM-DD');
                this.questionForm.projectId = item.projectId;
                this.questionForm.projectName = item.projectName;
                this.questionForm.description = item.description;
                this.questionForm.workHour = item.workHour;
                this.questionForm.urlList = item.urlList;
            },
            //新增线上问题
            saveQuestionInfo(formName) {
                let vm = this;
                this.questionAble = true
                this.questionForm.endTime = moment(this.questionForm.endTime).toDate()
                this.questionForm.beginTime = moment(this.questionForm.beginTime).toDate()
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        var param = this.questionForm;
                        param.name = param.name.trim();
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD HH:00:00')
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:59')
                        if (param.workHour.length != parseFloat(param.workHour).toString().length || parseFloat(param.workHour) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            return false;
                        }
                        http.zsyPostHttp('/question/add', param, (resp) => {
                            this.$message({showClose: true, message: '线上问题创建成功', type: 'success'});
                            this.$refs[formName].resetFields();
                            this.createQuestionVisible = false
                            this.questionAble = false
                            vm.reload()
                        });
                    } else {
                        return false;
                    }
                }, err => {
                    this.questionAble = false
                });
            },
            //修改线上问题
            editQuestionInfo(formName) {
                let vm = this;
                this.questionAble = true
                this.questionForm.endTime = moment(this.questionForm.endTime).toDate()
                this.questionForm.beginTime = moment(this.questionForm.beginTime).toDate()
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        var param = this.questionForm;
                        param.name = param.name.trim();
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD HH:00:00')
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:59')
                        param.oqrId = this.questionForm.oqrId;
                        if (param.workHour.length != parseFloat(param.workHour).toString().length || parseFloat(param.workHour) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            return false;
                        }
                        http.zsyPutHttp('/question/update', param, (resp) => {
                            this.$message({showClose: true, message: '线上问题修改成功', type: 'success'});
                            this.$refs[formName].resetFields();
                            this.editQuestionVisible = false
                            this.questionAble = false
                            vm.reload()
                        });
                    } else {
                        return false;
                    }
                }, err => {
                    this.questionAble = false
                });
            },
            handlePictureCardPreview(file) {
                this.dialogImageUrl = file.url;
                this.dialogVisible = true;
            },
            handleChange(file,fileList){
                this.questionForm.urlList = fileList.splice(-3)
            },
            handleRemove(file) {
                this.questionForm.urlList.splice(this.questionForm.urlList.findIndex(item => item.indexOf(file.name) > -1), 1)
            },
            beforeAvatarUpload(file) {
                const isJPG = file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/jpg';
                const isLt2M = file.size / 1024 / 1024 < 1;

                if (!isJPG) {
                    this.$message.error('上传图片只能是 JPG、JPEG、PNG 格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传图片大小不能超过 1MB!');
                }
                return isJPG && isLt2M;
            },
            upload(file) {
                var data = new FormData();
                data.append('uploadFile', file.file);
                http.zsyPostHttp('/upload/ucloud/image', data, (res) => {
                    this.questionForm.urlList.push(res.data.url)
                })
            },
            showPic(url){
              window.open(url)
            },
            removeUrl(url){
                this.questionForm.urlList.splice(this.questionForm.urlList.findIndex(item => item.indexOf(url) > -1), 1)
            },

            //查询待审核线上问题
            fetchQuestionWait(){
                let vm = this
                http.zsyGetHttp(`/question/wait/${vm.waitPage1.pageNum}`, {}, (res) => {
                    vm.question.wait = res.data.list
                    vm.waitPage1.total = res.data.total
                })
            },
            fetchQuestionAccepted(){
                let vm = this
                http.zsyGetHttp(`/question/accepted/${vm.acceptedPage.pageNum}`, {}, (res) => {
                    vm.question.accepted = res.data.list
                    vm.acceptedPage.total = res.data.total
                })
            },
            //查询进行中线上问题
            fetchQuestionDoing() {
                let vm = this
                http.zsyGetHttp('/question/running', {}, (res) => {
                    vm.question.running = res.data
                })
            },
            //查询已完成线上问题
            fetchQuestionCompleted(){
                let vm = this
                http.zsyGetHttp(`/question/completed/${vm.finishedPage1.pageNum}`, {}, (res) => {
                    vm.question.completed = res.data.list
                    vm.finishedPage1.total = res.data.total
                })
            },
            //关闭dialog
            closeDialog1(formName) {
                this.$refs[formName].resetFields();
                this.isSaving = false;
                this.$refs.uploadPic1.clearFiles();
            },
            closeDialog(formName) {
                this.$refs[formName].resetFields();
                this.isSaving = false;
                this.$refs.uploadPic.clearFiles();
            },
            //删除线上问题
            deleteQuestion(id){
                this.$confirm('删除线上问题, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    http.zsyPutHttp('question/delete/'+id,{},(res)=>{
                        if (res.data){
                            this.$message({
                                showClose: true,
                                message: '删除线上问题成功',
                                type: 'success'
                            });
                            this.checkQuestionVisible = false
                            this.fetchQuestionWait()
                            // this.$router.go(0)
                        }
                    })
                }).catch(() => {
                });

            },
            //审核通过线上问题
            acceptQuestion(id){
                this.$confirm('确认审核通过, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    http.zsyPutHttp('question/auditing/accept/'+id,{},(res)=>{
                        if (res.data){
                            this.$message({
                                showClose: true,
                                message: '审核成功',
                                type: 'success'
                            });
                            this.fetchQuestionWait()
                            this.fetchQuestionAccepted()
                        }
                    })
                    this.checkQuestionVisible = false;
                }).catch(() => {
                });
            },
            //完成线上问题
            saveFinishQuestion(id){
                this.$confirm('此操作将完成该任务, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                  http.zsyPutHttp('/question/finish/'+id,{},(res)=>{
                      if (res.data){
                          this.$message({
                              showClose: true,
                              message: '完成线上问题',
                              type: 'success'
                          });
                          this.fetchQuestionDoing();
                          this.fetchQuestionCompleted()
                          this.finishQuestionVisible = false
                      }
                  })
                }).catch(() => {
                });
            },
            //查询所有未读通知数量
            fetchUnreadNoticeNum(){
                http.zsyGetHttp('/task/notification/un-read/num',{},(res)=>{
                    this.unreadNoticeNum = res.data.count
                })
            },
            //跳转到通知页面
            toNotice(){
                this.$router.push({path:`/index/notice`,param:{activeName:'notice'}})
            },

            //跳转到我的年度总结
            toMySummary(){
                this.$router.push({path:`/index/SummaryNav`})
            },
            //创建加班申请
            createExtraWork(){
                this.createExtraWorkVisible = true;
                this.fetchMyRunningTasks(this.userId)
            },
            //查询我的未完成任务
            fetchMyRunningTasks(userId){
                http.zsyGetHttp('/extra-work/task-running/'+userId,{},(res)=>{
                    if (res){
                        this.myRunningTasks = res.data
                    }
                })
            },
            //查询我的加班申请(待审核)
            fetchMyRunningExtraWork(){
                if (this.userRole > 0){
                    http.zsyGetHttp('/extra-work/page/wait/'+this.eWorkWaitPage.pageNum,{},(res)=>{
                        if (res){
                            this.eWorkList.wait = res.data.list;
                            this.eWorkWaitPage.total = res.data.total;
                        }
                    })
                }
            },
            //查询我的加班申请(审核通过)
            fetchMyCheckedExtraWork(){
                if (this.userRole > 0){
                    http.zsyGetHttp('/extra-work/page/access/'+this.eWorkPassPage.pageNum,{},(res)=>{
                        if (res){
                            this.eWorkList.pass = res.data.list;
                            this.eWorkPassPage.total = res.data.total;
                        }
                    })
                }
            },
            //查询审核中的申请
            fetchCheckingExtraWork(){
                if (this.userRole == 0){
                    http.zsyGetHttp('/extra-work/page/checking/'+this.eWorkWaitPage.pageNum,{},(res)=>{
                        if(res){
                            this.eWorkList.wait = res.data.list;
                            this.eWorkWaitPage.total = res.data.total;
                        }
                    })
                }
            },
            //查询审核通过的申请
            fetchCheckedExtraWork(){
                if (this.userRole == 0){
                    http.zsyGetHttp('/extra-work/page/checked/'+this.eWorkPassPage.pageNum,{},(res)=>{
                        if (res){
                            this.eWorkList.pass = res.data.list;
                            this.eWorkPassPage.total = res.data.total;
                        }
                    })
                }
            },
            //打开加班申请详情卡片
            showExtraWorkDetail(ewId){
                this.showEWorkDetailVisible = true;
                http.zsyGetHttp('/extra-work/detail/'+ewId,{},(res)=>{
                    if (res){
                        this.eWorkDetail = res.data;
                    }
                })
            },
            //跳转到任务
            toTask(taskId){
                if (taskId != null && taskId != '') {
                    this.$router.push({name: 'taskListFormComments', params: {taskId: taskId}})
                }
            },
            //编辑加班申请
            editEWorkForm(extraWorkDetail){
                this.fetchMyRunningTasks(extraWorkDetail.userId)
                this.showEWorkDetailVisible = false;
                this.editEWorkVisible = true;
                this.extraWorkForm.id = extraWorkDetail.id;
                this.extraWorkForm.reason = extraWorkDetail.reason;
                this.extraWorkForm.beginTime = extraWorkDetail.beginTime;
                this.extraWorkForm.endTime = extraWorkDetail.endTime;
                this.extraWorkForm.workHours = extraWorkDetail.workHours;
                extraWorkDetail.tasks.forEach(task =>{
                    this.extraWorkForm.taskIds.push(task.id)
                })
            },
            //删除加班申请
            deleteEWork(ewId){
                this.$confirm('删除加班申请, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    http.zsyPutHttp('/extra-work/delete/'+ewId,{},(res)=>{
                            this.$message({
                                showClose: true,
                                message: '删除加班申请成功',
                                type: 'success'
                            });
                            this.showEWorkDetailVisible = false;
                            if (this.userRole > 0){
                                this.fetchMyRunningExtraWork();
                            } else {
                                this.fetchCheckingExtraWork();
                                this.fetchCheckedExtraWork();
                            }
                    })
                }).catch(() => {
                });
            },
            //审核通过加班申请
            accessEWork(ewId){
                this.$confirm('审核通过加班申请, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    http.zsyPutHttp('/extra-work/check/'+ewId,{},(res)=>{
                        this.$message({
                            showClose: true,
                            message: '审核通过加班申请成功',
                            type: 'success'
                        });
                        this.showEWorkDetailVisible = false;
                        this.fetchCheckingExtraWork();
                        this.fetchCheckedExtraWork();
                    })
                }).catch(() => {
                });
            },
            closeEWorkDetail(){
                this.isEWorkEdit = true
            },

            handleRecordPreview(){

            },
            handleRecordRemove(){

            },
            handleRecordPreview2(){

            },
            handleRecordRemove2(){

            },
            uploadRecordToMysql(file){
                this.fullscreenLoading = true;
                this.uploadToMysqlVisible = false;
                var data = new FormData();
                data.append('uploadFile', file.file);
                http.zsyPostHttp('/sign-in/upload/sign-in/repository',data,(res)=>{
                    this.$refs.record.clearFiles();
                    if (res.errMsg == "执行成功"){
                        this.fullscreenLoading = false;
                        this.$message({
                            showClose: true,
                            message: '导入成功',
                            type: 'success'
                        });
                    }else {
                        this.uploadToMysqlVisible = false;
                        this.fullscreenLoading = true;
                    }
                })
            },
            closeSignInDialog(){
                this.$refs.record.clearFiles();
            },
            uploadUserSortToMysql(file){
                var data = new FormData();
                data.append('uploadFile', file.file);
                http.zsyPostHttp('/sign-in/upload/user-sort/repository',data,(res)=>{
                    this.uploadUserSortToMysqlVisible = false;
                    this.$refs.usersort.clearFiles();
                    if (res.errMsg == "执行成功"){
                        this.$message({
                            showClose: true,
                            message: '导入成功',
                            type: 'success'
                        });
                    }else {
                        this.uploadUserSortToMysqlVisible = false;
                    }
                })
            },
            closeUserSortDialog(){
                this.$refs.usersort.clearFiles();
            },
            beforeRecordUpload(file) {
                var suffix = file.name.substring(file.name.lastIndexOf(".")+1)
                const isLt2M = file.size / 1024 / 1024 < 1;
                const isDat = file.name.substring(file.name.lastIndexOf(".")+1) == "dat"
                if (suffix != "dat"){
                    this.$message.error('上传文件只能是".dat"格式!');
                }
                if (!isLt2M) {
                    this.$message.error('上传文件大小不能超过 1MB!');
                }
                return isDat && isLt2M;
            },
            beforeUserSortUpload(file) {
                var suffix = file.name.substring(file.name.lastIndexOf(".")+1)
                const isXls = file.name.substring(file.name.lastIndexOf(".")+1) == "xls";
                const isExcel = file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
                const isLt2M = file.size / 1024 / 1024 < 1;
                if ("xls" != suffix) {
                    this.$message.error('上传文件只能是".xls"格式!');
                }
                if (!isExcel) {
                    this.$message.error('上传文件只能是excel 格式!');
                    return false
                }
                if (!isLt2M) {
                    this.$message.error('上传文件大小不能超过 1MB!');
                }
                return isExcel && isLt2M && isXls;
            },

            //查询我的漏打卡考勤
            fetchForgetSignIn(){
                http.zsyGetHttp('/sign-in/page/personal/forget/'+this.signInPage.pageNum,{},(res) =>{
                    if (res){
                        this.forgetSignIn = res.data.list;
                        this.signInPage.total = res.data.totalSize;
                    }
                })
            },
            signInTimeChange(time) {
                // 选择结束时间
                time = time.split(' - ')
                if (time && time.length == 2) {
                    this.signInReqDTO.beginTime = `${time[0]} 00:00:00`
                    this.signInReqDTO.endTime = `${time[1]} 23:59:59`
                } else {
                    this.signInReqDTO.beginTime = this.signInReqDTO.endTime = this.signInDaterange = ''
                }
            },
            mySignInTimeChange(time) {
                // 选择结束时间
                time = time.split(' - ')
                if (time && time.length == 2) {
                    this.mySignInReqDTO.beginTime = `${time[0]} 00:00:00`
                    this.mySignInReqDTO.endTime = `${time[1]} 23:59:59`
                } else {
                    this.mySignInReqDTO.beginTime = this.mySignInReqDTO.endTime = this.mySignInDaterange = ''
                }
            },
            //查询考勤情况
            fetchSignInData(){
                if (this.userRole == 3 ){
                    http.zsyPostHttp('/sign-in/page',this.signInReqDTO,(res)=>{
                        if (res){
                            this.signInData = res.data.list;
                            this.signInPage.total = res.data.total;
                            this.signInData.forEach(signIn =>{
                                if (signIn.workTime) {
                                    signIn.workTime = this.getTime2(signIn.workTime);
                                }
                                if(signIn.eWorkTime){
                                    signIn.eWorkTime = this.getTime2(signIn.eWorkTime);
                                }
                                var checkTimeStr = '';
                                signIn.checkTimeList.forEach(checkTime => {
                                    checkTimeStr = checkTimeStr + moment(checkTime).format("HH:mm:ss") + ', '
                                })
                                signIn.checkTimeList = checkTimeStr.substring(0,checkTimeStr.length-2)
                            })
                        }
                    })
                }
            },
            initSignInTime(){
                var date1 = new Date();
                var time1=date1.getFullYear()+"-"+(date1.getMonth()+1)+"-"+date1.getDate();//time1表示当前时间
                var date2 = new Date(date1);
                var date3 = new Date(date1);
                date3.setDate(date1.getDate() - 1);
                date2.setDate(date1.getDate() - 7);
                var time2 = date2.getFullYear()+"-"+(date2.getMonth()+1)+"-"+date2.getDate();
                var time3 = date3.getFullYear()+"-"+(date3.getMonth()+1)+"-"+date3.getDate();
                if (this.userRole == 3){
                    // this.signInReqDTO.beginTime = moment(time2).format('YYYY-MM-DD 00:00:00');
                    // this.signInReqDTO.endTime = moment(time3).format('YYYY-MM-DD 23:59:59');
                    this.signInReqDTO.beginTime = time2 + ' 00:00:00';
                    this.signInReqDTO.endTime = time3 + ' 23:59:59';
                }
                if(this.userRole > 0 && this.userRole < 3){
                    // this.mySignInReqDTO.beginTime = moment(time2).format('YYYY-MM-DD 00:00:00');
                    // this.mySignInReqDTO.endTime = moment(time3).format('YYYY-MM-DD 23:59:59');
                    this.mySignInReqDTO.beginTime = time2 + ' 00:00:00';
                    this.mySignInReqDTO.endTime = time3 + ' 23:59:59';
                }
            },
            selectMySignInData(){
                if (this.mySignInReqDTO.beginTime != null && this.mySignInReqDTO.beginTime != ''){
                    this.mySignInReqDTO.beginTime = moment(this.mySignInReqDTO.beginTime).format('YYYY-MM-DD 00:00:00');
                }
                if (this.mySignInReqDTO.endTime != null && this.mySignInReqDTO.endTime != '') {
                    this.mySignInReqDTO.endTime = moment(this.mySignInReqDTO.endTime).format('YYYY-MM-DD 23:59:59');
                }
                this.fetchMySignInData()
            },
            selectSignInData(){
                if (this.signInReqDTO.beginTime != null && this.signInReqDTO.beginTime != ''){
                    this.signInReqDTO.beginTime = moment(this.signInReqDTO.beginTime).format('YYYY-MM-DD 00:00:00');
                }
                if (this.signInReqDTO.endTime != null && this.signInReqDTO.endTime != '') {
                    this.signInReqDTO.endTime = moment(this.signInReqDTO.endTime).format('YYYY-MM-DD 23:59:59');
                }

                this.fetchSignInData()
            },
            fetchMyTotalEWorkTime(){
                if (this.workMonth1 != null && this.workMonth1 != ''){
                    const month = this.workMonth1;
                    const year = moment(new Date()).format('YYYY');
                    const yearAndMonth = year+"-"+this.addZero(month);
                    this.eWorkTimeReqDTO1.yearAndMonth = yearAndMonth;
                    http.zsyPostHttp('/sign-in/extra-hours/total/personal',this.eWorkTimeReqDTO1,(res)=>{
                        if (res) {
                            this.myTotalEWorkTime = this.getTime(res.data.extraTime);
                        }
                    })
                }else {
                    const year = moment(new Date()).format('YYYY');
                    this.eWorkTimeReqDTO1.yearAndMonth = year;
                    http.zsyPostHttp('/sign-in/extra-hours/total/personal',this.eWorkTimeReqDTO1,(res)=>{
                        if (res) {
                            this.myTotalEWorkTime = this.getTime(res.data.extraTime);
                        }
                    })
                }
            },
            closeExcelDialog(){
                this.workMonth3 = '';
                this.excelReqDTO.yearAndMonth = '';
            },
            //导出考勤记录Excel
            signInExcel(){
              if (this.workMonth3 == null || this.workMonth3 == ''){
                  this.$message({
                      showClose: true,
                      message: '请选择月份',
                      type: 'warning'
                  });
                  return false;
              }else {
                  const month = this.workMonth3;
                  const year = moment(new Date()).format('YYYY');
                  const yearAndMonth = year+"-"+this.addZero(month);
                  this.excelReqDTO.yearAndMonth = yearAndMonth;
                  this.fullscreenLoading = true;
                  http.zsyPostHttp('/sign-in/excel',this.excelReqDTO,(res)=>{
                      if (res.data) {
                          window.open(res.data)
                          this.$message({
                              showClose: true,
                              message: '导出成功',
                              type: 'success'
                          });
                          this.workMonth3 = '';
                          this.excelReqDTO.yearAndMonth = '';
                          this.fullscreenLoading = false;
                          this.excelSignInVisible = false;
                      }

                  },(fail)=>{
                      this.$message({
                          showClose: true,
                          message: fail.errMsg,
                          type: 'error'
                      });
                      this.workMonth3 = '';
                      this.excelReqDTO.yearAndMonth = '';
                      this.fullscreenLoading = false;
                      this.excelSignInVisible = false;
                  })
              }
            },
            fetchTotalEWorkTime(){
                if (this.eWorkTimeUserId == null || this.eWorkTimeUserId == ''){
                    this.$message({
                        showClose: true,
                        message: '请选择查询的用户',
                        type: 'warning'
                    });
                    return false;
                }
                if (this.workMonth2 != null && this.workMonth2 != ''){
                    const month = this.workMonth2;
                    const year = moment(new Date()).format('YYYY');
                    const yearAndMonth = year+"-"+this.addZero(month);
                    this.eWorkTimeReqDTO2.yearAndMonth = yearAndMonth;
                    http.zsyPostHttp('/sign-in/extra-hours/total/'+this.eWorkTimeUserId,this.eWorkTimeReqDTO2,(res)=>{
                        if (res) {
                            this.totalEWorkTime = this.getTime(res.data.extraTime);
                            this.eWorkTimeUserName = res.data.userName;
                        }
                    })
                }else {
                    const year = moment(new Date()).format('YYYY');
                    this.eWorkTimeReqDTO2.yearAndMonth = year;
                    http.zsyPostHttp('/sign-in/extra-hours/total/'+this.eWorkTimeUserId,this.eWorkTimeReqDTO2,(res)=>{
                        if (res) {
                            this.totalEWorkTime = this.getTime(res.data.extraTime);
                            this.eWorkTimeUserName = res.data.userName;
                        }
                    })
                }
            },
            closeMyEWorkCounter(){
                this.workMonth1 = '';
                this.myTotalEWorkTime = '';
                this.eWorkTimeReqDTO1.yearAndMonth = '';
            },
            closeEWorkCounter(){
                this.workMonth2 = '';
                this.totalEWorkTime = '';
                this.eWorkTimeUserId = '';
                this.eWorkTimeUserName = '';
                this.eWorkTimeReqDTO2.yearAndMonth = '';

            },
            fetchMySignInData(){
                if (this.userRole < 3 && this.userRole > 0){
                    http.zsyPostHttp('/sign-in/page/personal',this.mySignInReqDTO,(res)=>{
                        if (res){
                            this.signInData = res.data.list;
                            this.mySignInPage.total = res.data.total;
                            this.signInData.forEach(signIn =>{
                                if (signIn.workTime) {
                                    signIn.workTime = this.getTime2(signIn.workTime);
                                }
                                if(signIn.eWorkTime){
                                    signIn.eWorkTime = this.getTime2(signIn.eWorkTime);
                                }
                                var checkTimeStr = '';
                                signIn.checkTimeList.forEach(checkTime => {
                                    checkTimeStr = checkTimeStr + moment(checkTime).format("HH:mm:ss") + ', '
                                })
                                signIn.checkTimeList = checkTimeStr.substring(0,checkTimeStr.length-2)
                            })
                        }
                    })
                }
            },
            signInHandleCurrentChange(currentPage){
                this.signInPage.pageNum = currentPage;
                this.fetchSignInData();
            },
            mySignInHandleCurrentChange(currentPage){
                this.mySignInPage.pageNum = currentPage;
                this.fetchMySignInData();
            },
            //格式化时间
            getTime(time){
                const hours = this.addZero(parseInt(time/1000/60/60));
                const mins = this.addZero(parseInt(time/1000/60%60));
                const secs = this.addZero(parseInt(time/1000%60));
                return hours+'小时'+mins+'分钟'+secs+'秒'
            },
            getTime2(time){
                const hours = this.addZero(parseInt(time/1000/60/60));
                const mins = this.addZero(parseInt(time/1000/60%60));
                const secs = this.addZero(parseInt(time/1000%60));
                return hours+'h'+mins+'m'+secs+'s'
            },
            addZero(time){
                if (time < 10){
                    return "0"+time;
                } else {
                    return time+"";
                }
            },
            recheck(userId){
                this.recheckForm.userId = userId;
                this.reCheckVisible = true;
            },
            saveEditRecheck(formName,id){
                this.isSaving = true;
                if (this.recheckForm.reason == null || this.recheckForm.reason == ''){
                    this.$message({showClose: true, message: '补打卡原因不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.recheckForm.recheckInTime == null || this.recheckForm.recheckInTime == ''){
                    if(this.recheckForm.recheckOutTime == null || this.recheckForm.recheckOutTime == ''){
                        this.$message({showClose: true, message: '补打卡时间不能为空', type: 'error'});
                        this.isSaving = false;
                        return false;
                    }
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let form = this.recheckForm
                        if (form.recheckInTime != null && form.recheckInTime != ''){
                            form.recheckInTime = moment(form.recheckInTime).format('YYYY-MM-DD HH:mm:ss')
                        }
                        if(form.recheckOutTime != null && form.recheckOutTime != ''){
                            form.recheckOutTime = moment(form.recheckOutTime).format('YYYY-MM-DD HH:mm:ss')
                        }
                        http.zsyPostHttp('/sign-in/resign-in/update/'+id, form, (resp) => {
                            this.$message({
                                showClose: true,
                                message: '补打卡申请修改成功',
                                type: 'success'
                            });
                            this.clearRecheckForm();
                            this.fetchMyRecheckWait();
                            this.fetchRecheckWait();
                            this.editRecheckVisible = false
                            this.isSaving = false
                        }, err => {
                            this.isSaving = false
                        })
                    }
                })
            },
            saveRecheck(formName){
                this.isSaving = true;
                if (this.recheckForm.reason == null || this.recheckForm.reason == ''){
                    this.$message({showClose: true, message: '补打卡原因不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.recheckForm.recheckInTime == null || this.recheckForm.recheckInTime == ''){
                    if(this.recheckForm.recheckOutTime == null || this.recheckForm.recheckOutTime == ''){
                        this.$message({showClose: true, message: '补打卡时间不能为空', type: 'error'});
                        this.isSaving = false;
                        return false;
                    }
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let form = this.recheckForm
                        if (form.recheckInTime != null && form.recheckInTime != ''){
                            form.recheckInTime = moment(form.recheckInTime).format('YYYY-MM-DD HH:mm:ss')
                        }
                        if(form.recheckOutTime != null && form.recheckOutTime != ''){
                            form.recheckOutTime = moment(form.recheckOutTime).format('YYYY-MM-DD HH:mm:ss')
                        }

                        http.zsyPostHttp('/sign-in/resign-in/add', form, (resp) => {
                            this.$message({
                                showClose: true,
                                message: '补打卡申请成功',
                                type: 'success'
                            });
                            this.clearRecheckForm();
                            this.fetchMyRecheckWait();
                            this.reCheckVisible = false
                            this.isSaving = false
                        }, err => {
                            this.isSaving = false
                        })
                    }
                })
            },
            deleteRecheck(id){
                this.$confirm('删除补打卡申请, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    http.zsyDeleteHttp('/sign-in/resign-in/delete/'+id,{},(res)=>{
                        this.$message({
                            showClose: true,
                            message: '删除补打卡申请成功',
                            type: 'success'
                        });
                        this.editRecheckVisible = false;
                        this.showRecheckVisible = false;
                        if (this.userRole > 0){
                            this.fetchMyRecheckWait();
                        } else {
                            this.fetchRecheckWait();
                            this.fetchRecheckPass();
                        }
                    })
                }).catch(() => {
                });
            },
            accessRecheck(id){
                this.$confirm('同意补打卡申请, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    http.zsyPutHttp('/sign-in/resign-in/access/'+id,{},(res)=>{
                        this.$message({
                            showClose: true,
                            message: '同意补打卡申请成功',
                            type: 'success'
                        });
                        this.editRecheckVisible = false;
                        this.fetchRecheckWait();
                        this.fetchRecheckPass();
                    })
                }).catch(() => {
                });
            },
            clearRecheckForm(){
                this.recheckForm.reason = this.recheckForm.recheckInTime = this.recheckForm.recheckOutTime
                    = this.recheckForm.userId = this.recheckForm.id = ''
            },
            closeRecheckForm(){
                this.clearRecheckForm()
                this.isSaving = false
            },
            //查询我的补打卡申请(status=0 审核中  status=2 通过)
            fetchMyRecheckWait(){
                if (this.userRole > 0){
                    http.zsyGetHttp('/sign-in/resign-in/page/personal/0/'+this.recheckWaitPage.pageNum,{},(res)=>{
                        if (res){
                            this.recheckList.wait = res.data.list;
                            this.recheckWaitPage.total = res.data.total;
                        }
                    })
                }
            },
            fetchMyRecheckPass(){
                if(this.userRole > 0){
                    http.zsyGetHttp('/sign-in/resign-in/page/personal/2/'+this.recheckPassPage.pageNum,{},(res)=>{
                        if (res){
                            this.recheckList.pass = res.data.list;
                            this.recheckPassPage.total = res.data.total;
                        }
                    })
                }
            },
            fetchRecheckWait(){
                if(this.userRole == 0){
                    http.zsyGetHttp('/sign-in/resign-in/page/0/'+this.recheckWaitPage.pageNum,{},(res)=>{
                        if (res){
                            this.recheckList.wait = res.data.list;
                            this.recheckWaitPage.total = res.data.total;
                        }
                    })
                }
            },
            fetchRecheckPass(){
                if(this.userRole == 0){
                    http.zsyGetHttp('/sign-in/resign-in/page/2/'+this.recheckPassPage.pageNum,{},(res)=>{
                        if (res){
                            this.recheckList.pass = res.data.list;
                            this.recheckPassPage.total = res.data.total;
                        }
                    })
                }
            },
            handleRecheckWaitPage(currentPage) {
                this.recheckWaitPage.pageNum = currentPage;
                if (this.userRole > 0) {
                    this.fetchMyRecheckWait();
                } else {
                    this.fetchRecheckWait();
                }
            },
            handleRecheckPassPage(currentPage) {
                this.recheckPassPage.pageNum = currentPage;
                if (this.userRole > 0) {
                    this.fetchMyRecheckPass();
                } else {
                    this.fetchRecheckPass();
                }
            },
            editRecheck(recheck){
                this.editRecheckVisible = true;
                this.recheckForm.id = recheck.id;
                this.recheckForm.userId = recheck.userId;
                this.recheckForm.recheckTime = recheck.recheckTime;
                this.recheckForm.recheckInTime = recheck.recheckTime;
                this.recheckForm.recheckOutTime = recheck.recheckTime;
                this.recheckForm.reason = recheck.reason;
                this.recheckForm.type = recheck.type;
            },
            showRecheck(recheck){
                this.showRecheckVisible = true;
                this.recheckForm.id = recheck.id;
                this.recheckForm.userId = recheck.userId;
                this.recheckForm.recheckTime = recheck.recheckTime;
                this.recheckForm.reason = recheck.reason;
                this.recheckForm.type = recheck.type;
            },
            //获取考勤人员列表
            fetchSignInUser(){
                http.zsyGetHttp('/sign-in/users',{},(res)=>{
                    if (res){
                        this.checkInUsers = res.data
                    }
                })
            }

            // -- sch
        },
        components: {
            ElFormItem,
            ElInput,
            ElButton,
            TaskItem: TaskItem,
            QuestionItem: QuestionItem
        },
    }
</script>
<style>
    .el-dialog__wrapper .myDialog {
        width: 600px !important;
    }
</style>
<style scoped>

    .pagination {
        margin: 20px 0;
        text-align: right;
    }

    .nav-index-con {
        width: 1100px;
        margin: auto;
    }

    .mic-title {
        font-size: 18px;
        color: #000;
        position: relative;
    }

    .mic-item {
        margin: 18px 75px;
        width: 200px;
        text-align: center;
        box-shadow: 0 0 20px #ccc;
        background-color: #fff;
    }

    .mic-item-title {
        line-height: 40px;
        background: #fff;
        font-size: 20px;
    }

    .mic-item-title:first-child {
        margin-top: 10px;
    }

    .icon-score {
        margin-right: 10px;
        height: 60px;
        vertical-align: middle;
    }

    .mic-item-integral {
        font-size: 26px;
        color: #fff;
        background: #3dc999;
        line-height: 40px;
        /*padding: 14px 0;*/
        word-wrap: break-word;
    }

    .mic-item:nth-child(2) .mic-item-integral {
        background-color: #f4b548;
    }

    .mic-item:nth-child(3) .mic-item-integral {
        background-color: #ab93ed;
    }

    .mic-item:nth-child(4) .mic-item-integral {
        background-color: #609beb;
    }

    .mic-item:nth-child(5) .mic-item-integral {
        background-color: #f68350;
    }

    .mic-item:first-child {
        margin-left: 0;
    }

    .mic-item:last-child {
        margin-right: 0;
    }

    .star {
        color: red;
        padding: 1px;
    }

    .my-integral-con, .my-task-con {
        margin-top: 16px;
        position: relative;
    }

    .my-task-detail {
        margin-top: 6px;
    }

    .add-task {
        position: absolute;
        right: 430px;
        font-size: 16px;
        cursor: pointer;
        color: #36A8FF;
        z-index: 30;
    }

    .add-question {
        position: absolute;
        right: 430px;
        font-size: 16px;
        cursor: pointer;
        color: #36A8FF;
        z-index: 30;
    }

    .question {
        right: 280px;
    }

    .help {
        right: 135px;
    }

    .leave {
        right: 20px;
    }

    .add-task > span {
        display: inline-block;
        vertical-align: middle;
    }

    .add-task-icon {
        width: 22px;
        height: 22px;
        line-height: 22px;
        border-radius: 50%;
        background: #36A8FF;
        color: #fff;
        text-align: center;
        font-size: 14px;
        cursor: pointer;
    }

    /*积分转移审核样式*/
    .main-task-detail {
        flex: 1;
        -webkit-flex: 1;
        -moz-flex: 1;
        -ms-flex: 1;
        -o-flex: 1;
    }

    .task-lis {
        background: #fff;
        display: -webkit-flex;
        display: -moz-flex;
        display: -ms-flex;
        display: -o-flex;
        display: flex;
        margin-bottom: 20px;
        margin: 10px 0 20px;
        cursor: pointer;
    }

    .task-lis:hover {
        box-shadow: 0 0 10px #ccc;
    }

    .head-img {
        width: 60px;
        height: 60px;
        /*border-radius: 50%;*/
        overflow: hidden;
        margin: 16px;
    }

    .task-name {
        margin-top: 18px;
        font-size: 15px;
    }

    .task-end.blue {
        background: #36A8FF;
        padding: 2px 10px;
        color: #fff;
    }

    .task-end.orange {
        background: orange;
        padding: 2px 10px;
        color: #fff;
    }

    .task-end.red {
        background: red;
        padding: 2px 10px;
        color: #fff;
    }

    .task-end.green {
        background: green;
        padding: 2px 10px;
        color: #fff;
    }

    .task-state {
        margin-top: 10px;
    }

    .task-state .iconfont {
        margin-right: 5px;
    }

    .task-username {
        height: 40px;
        background: #69C8FA;
        border-radius: 50%;
        line-height: 40px;
        text-align: center;
        color: #fff;
        cursor: pointer;
        /*z-index: 100;*/
        margin-top: 24px;
        margin-right: 20px;
        width: 40px;
    }

    .task-time-opt {
        color: #36A8FF;
        font-size: 20px;
        margin-left: 16px;
        cursor: pointer;
    }

    .task-data-show > span {
        display: inline-block;
        vertical-align: middle;
    }

    .task-data-show {
        margin: 0 40px 0 20px;
    }

    .task-score {
        font-size: 18px;
        line-height: 92px;
    }

    .empty {
        text-align: center;
        font-size: 16px;
        padding: 20px;
    }

    .task-mark {
        line-height: 90px;
        font-size: 18px;
    }

    .iconfont {
        margin-right: 5px;
    }

    .message {
        background-color: #20a0ff;
        color: #fff;
        line-height: 30px;
        padding: 0 10px;
        width: 300px;
    }

    .ctpc-member-con {
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

    .ctpc-member-job-time {
        width: 280px;
    }
</style>

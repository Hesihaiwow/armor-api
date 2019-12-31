<template>
    <div class="nav-index-con">
        <div v-show="userRole === 3 || userRole === 0" style="float: left">
            <el-button v-loading.fullscreen.lock="fullscreenLoading"
                       element-loading-text="拼命导入中,请稍后"
                       element-loading-spinner="el-icon-loading"
                       element-loading-background="rgba(0, 0, 0, 0.8)"
                       type="primary" @click="uploadToMysqlVisible=true">导入考勤记录</el-button>
        </div>
        <div v-show="userRole === 3 || userRole === 0">
            <el-button v-loading.fullscreen.lock="fullscreenLoading"
                       element-loading-text="拼命导出中,请稍后"
                       element-loading-spinner="el-icon-loading"
                       element-loading-background="rgba(0, 0, 0, 0.8)"
                       type="primary" @click="excelSignInVisible=true"
                       style="margin-left: 20px">导出考勤记录</el-button>
        </div>
        <div v-show="userRole === 0" style="margin-top: 10px">
                <span style="font-size: 18px;color: black">项目管理者负责任务</span>
                <el-table :data="principalAllTaskList" border style="width: 1100px">
                    <el-table-column prop="userName" label="负责人" align="center" width="100"></el-table-column>
                    <el-table-column prop="chargeTaskNum" label="负责任务数(进行中)" align="center" sortable></el-table-column>
                    <el-table-column prop="reviewTaskNum" label="待评审任务数" align="center" sortable width="140"></el-table-column>
                    <el-table-column prop="summarizeTaskNum" label="待总结任务数" align="center" sortable width="140"></el-table-column>
                    <el-table-column prop="delayedTaskNum" label="已超时任务数" align="center" sortable width="140">
                        <template scope="scope"><font style="color:red">{{scope.row.delayedTaskNum}}</font></template>
                    </el-table-column>
                    <el-table-column prop="aboutDelayTaskNum" label="即将超时任务数" align="center" sortable>
                        <template scope="scope"><font style="color: orange">{{scope.row.aboutDelayTaskNum}}</font></template>
                    </el-table-column>
                    <el-table-column prop="messageFee" label="需缴纳短信费" align="center" sortable width="140">
                        <template scope="scope"><font style="color: red">{{scope.row.messageFee}}</font></template>
                    </el-table-column>
                </el-table>
        </div>
        <div v-show="userRole === 0" style="margin-top: 10px">
            <span style="font-size: 18px;color: black">员工周工作量</span>
            <div class="add-member-basic-msg" style="margin-top: 5px;">
                <el-select v-model="weekHourUserId" clearable filterable placeholder="筛选用户" @change="fetchUserWeekHourStats(weekHourUserId)" style="width: 150px">
                    <el-option v-for="item in checkInUsers" :key="item.userId" :label="item.userName"
                               :value="item.userId"></el-option>
                </el-select>
            </div>
            <div id="myChart2" :style="{width:'1100px',height:'250px',marginTop:'10px'}" v-show="weekHourStatsList.length>0"></div>
            <div v-show="weekHourStatsList.length===0" class="empty">
                <h2>暂无数据</h2>
            </div>
        </div>
        <div class="my-integral-con" v-show="userRole>0 && userRole < 3" style="width: 1300px;margin-bottom: 10px">
            <p class="mic-title">我的统计</p>
            <div class="my-task-detail hh">
                <el-tabs v-model="tabName" @tab-click="handleClickTab">
                    <el-tab-pane label="负责任务" name="task" v-if="userRole == 1">
                        <el-table :data="principalTaskList" border style="width: 1100px">
                            <el-table-column prop="chargeTaskNum" label="负责任务数(进行中)" align="center"></el-table-column>
                            <el-table-column prop="reviewTaskNum" label="待评审任务数" align="center"></el-table-column>
                            <el-table-column prop="summarizeTaskNum" label="待总结任务数" align="center"></el-table-column>
                            <el-table-column prop="delayedTaskNum" label="已超时任务数" align="center">
                                <template scope="scope"><font style="color:red">{{scope.row.delayedTaskNum}}</font></template>
                            </el-table-column>
                            <el-table-column prop="aboutDelayTaskNum" label="即将超时任务数" align="center">
                                <template scope="scope"><font style="color: orange">{{scope.row.aboutDelayTaskNum}}</font></template>
                            </el-table-column>
                            <el-table-column prop="messageFee" label="需缴纳短信费" align="center">
                                <template scope="scope"><font style="color: red">{{scope.row.messageFee}}</font></template>
                            </el-table-column>
                        </el-table>
                    </el-tab-pane>
                    <el-tab-pane label="任务积分" name="integral">
                        <div style="margin-top: 0px;margin-right: 420px;font-size: 14px"
                        @click="clickHistory">
                        <span style="font-size:14px;color: #36A8FF;cursor: pointer;text-decoration: underline">查看积分记录</span>
                        <!--<span class="task-time-opt" style="font-size:14px"><i class="el-icon-edit"></i></span>计算基准积分-->
                        </div>
                        <div class="mic-main clearfix">
                            <div class="mic-item fl" v-for="(item,key) in taskIntegralItem" style="margin-left: 75px;">
                                <div class="mic-item-title"><img :src="`${require(`../assets/img/icon_${key+6}.png`)}`"
                                                                 class="icon-score">
                                </div>
                                <div class="mic-item-title" style="font-size: 12px">{{item.time}}</div>
                                <div class="mic-item-integral">{{item.score}}</div>
                            </div>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="周工时" name="weekHours">
                        <div id="myChart1" :style="{width:'1100px',height:'250px'}"></div>
                    </el-tab-pane>
                    <el-tab-pane label="我的评价" name="evaluation">
                        <div class="mic-main clearfix" style="float: left;">
                            <div class="fl" style="margin-left: 0px;width: 430px">
                                <div style="font-size: 16px;">本周综合评价</div>
                                <div style="font-size: 15px;margin-bottom: 10px">{{personalEvaluation.weekTime}}</div>
                                <div  v-for="(item,index) in personalEvaluation.weekEvaluations"
                                      v-if="personalEvaluation.weekEvaluations.length > 0">
                                    <el-form label-position="left" inline class="demo-table-expand">
                                        <el-form-item class="task-form" :label="item.evaluationOptionName" style="margin-top: -10px">
                                            <el-rate v-model="item.avgScore"
                                                     :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                                     :allow-half=true
                                                     disabled
                                                     show-text
                                                     text-template="{value}"
                                                     style="float: left;margin-top: 7px">
                                            </el-rate>
                                        </el-form-item>
                                    </el-form>
                                </div>
                                <div v-show="personalEvaluation.weekEvaluations.length === 0" class="empty" style="margin-left: -200px">
                                    <h2>暂无评价</h2>
                                </div>
                            </div>
                        </div>
                        <div class="mic-main clearfix" style="float:left;">
                            <div class="fl" style="margin-left: 0px;width: 430px">
                                <div style="font-size: 16px;">本月综合评价</div>
                                <div style="font-size: 15px;margin-bottom: 10px">{{personalEvaluation.monthTime}}</div>
                                <div  v-for="(item,index) in personalEvaluation.monthEvaluations"
                                      v-if="personalEvaluation.monthEvaluations.length > 0">
                                    <el-form label-position="left" inline class="demo-table-expand">
                                        <el-form-item class="task-form" :label="item.evaluationOptionName" style="margin-top: -10px">
                                            <el-rate v-model="item.avgScore"
                                                     :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                                     :allow-half=true
                                                     disabled
                                                     show-text
                                                     text-template="{value}"
                                                     style="float: left;margin-top: 7px">
                                            </el-rate>
                                        </el-form-item>
                                    </el-form>
                                </div>
                                <div v-show="personalEvaluation.monthEvaluations.length === 0" class="empty" style="margin-left: -200px">
                                    <h2>暂无评价</h2>
                                </div>
                            </div>
                        </div>
                        <div class="mic-main clearfix">
                            <div class="fl" style="margin-left: 0px;width: 430px">
                                <div style="font-size: 16px">年度综合评价</div>
                                <div style="font-size: 15px;margin-bottom: 10px">{{personalEvaluation.yearTime}}</div>
                                <div  v-for="(item,index) in personalEvaluation.yearEvaluations"
                                      v-if="personalEvaluation.yearEvaluations.length > 0">
                                    <el-form label-position="left" inline class="demo-table-expand">
                                        <el-form-item class="task-form" :label="item.evaluationOptionName" style="margin-top: -10px">
                                            <el-rate v-model="item.avgScore"
                                                     :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                                     :allow-half=true
                                                     disabled
                                                     show-text
                                                     text-template="{value}"
                                                     style="float: left;margin-top: 7px">
                                            </el-rate>
                                        </el-form-item>
                                    </el-form>
                                </div>
                                <div v-show="personalEvaluation.yearEvaluations.length === 0" class="empty" style="margin-left: -200px;">
                                    <h2>暂无评价</h2>
                                </div>
                            </div>
                        </div>
                    </el-tab-pane>
                </el-tabs>
            </div>
            <!--<div class="steps-body">-->
                <!--<div id="myChart1" :style="{width:'1100px',height:'250px'}"></div>-->
            <!--</div>-->
            <!--<div><p class="mic-title">我的评价</p></div>-->

        </div>
        <div class="my-task-con" v-show="userRole === 3">

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
                <!--<el-button type="primary" @click="modifyUserRestHoursVisible = true" style="margin-left: 10px" size="small">修改调休时长</el-button>-->
                <el-table :data="signInData" border>
                    <el-table-column prop="date" label="日期" align="center" width="120">
                        <template scope="scope">
                            {{scope.row.date | formatDate2}}{{scope.row.weekday}}
                            <span v-show="scope.row.isWeekend === 1" style="color: #3da7f5">(周末)</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="userName" label="用户" align="center" width="110">
                        <template scope="scope">
                            <span style="color: red" v-if="scope.row.isForget === 1">(漏)</span>{{scope.row.userName}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="checkTimeList" label="打卡记录" align="left">
                        <template scope="scope">
                            {{scope.row.checkTimeList}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="checkInTime" label="上班时间" align="center" width="120" >
                        <template scope="scope">
                            <span style="color: red" v-if="scope.row.isRecheckIn === 1">(补)</span>
                            <span v-if="scope.row.isCheckInAfterTen === 1" style="color: red">{{scope.row.checkInTime | formatTime2}}</span>
                            <span v-else>{{scope.row.checkInTime | formatTime2}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="checkOutTime" label="下班时间" align="center" width="120" >
                        <template scope="scope">
                            <span style="color: red" v-if="scope.row.isRecheckOut === 1">(补)</span>
                            <span style="color: green" v-if="scope.row.isWorkToNextDay === 1">(+1)</span>
                            <span v-if="scope.row.isCheckOutBeforeSix === 1" style="color: red">{{scope.row.checkOutTime | formatTime2}}</span>
                            <span v-else>{{scope.row.checkOutTime | formatTime2}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="workTime" label="上班时长" align="center" width="120" >
                        <template scope="scope">
                            <span v-if="scope.row.lessThanNine === 1" style="color: red">{{scope.row.workTime}}</span>
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
        <div class="my-task-con" v-show="userRole === 3">

            <p class="mic-title" style="margin-top: 20px">调休统计</p>
            <div class="my-task-detail" style="width: 1200px;">
                <div class="add-member-basic-msg fl" >
                    <el-date-picker
                            v-model="restHourYear"
                            align="right"
                            type="year"
                            placeholder="选择年份">
                    </el-date-picker>
                </div>
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="restHourReqDTO.jobRole" clearable filterable   placeholder="筛选角色">
                        <el-option v-for="item in rolesList" :key="item.roleId" :label="item.roleName"
                                   :value="item.roleId"></el-option>
                    </el-select>
                </div>
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="restHourReqDTO.userId" clearable filterable   placeholder="筛选用户">
                        <el-option v-for="item in checkInUsers" :key="item.userId" :label="item.userName"
                                   :value="item.userId"></el-option>
                    </el-select>
                </div>
                <el-button type="primary" @click="fetchAllUsersRestHours" style="margin-left: 10px" size="small">查询</el-button>
                <el-table :data="restHoursData" border>
                    <el-table-column  type="index"  label="序号"  width="80"></el-table-column>
                    <el-table-column prop="userName" label="用户" align="center" width="100">
                        <template scope="sco">
                            <a style="color:#20a0ff;cursor: pointer;" @click="showUserRestHoursLog(sco.row.userId,sco.row.userName)" >{{sco.row.userName}}</a>
                        </template>
                    </el-table-column>
                    <el-table-column prop="totalRestHours" label="总调休" sortable align="center">
                        <template scope="sco">
                            <div style="white-space: pre-wrap;text-align: center">{{sco.row.totalRestHours}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="goneRestHours" label="已用调休"  sortable align="center">
                        <template scope="scope">
                            <span type="text">{{scope.row.goneRestHours}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="leftRestHours" label="剩余调休" sortable align="center"></el-table-column>
                    <el-table-column prop="endDate" label="截止日期"  width="200" align="center"></el-table-column>
                </el-table>
            </div>
        </div>
        <div class="my-task-con" v-show="userRole === 3">

            <p class="mic-title" style="margin-top: 20px">请假统计</p>
            <div class="my-task-detail" style="width: 1200px;">
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="leaveReqDTO.userId" clearable filterable   placeholder="筛选用户">
                        <el-option v-for="item in checkInUsers" :key="item.userId" :label="item.userName"
                                   :value="item.userId"></el-option>
                    </el-select>
                </div>
                <div class="add-member-basic-msg fl">
                    <el-date-picker
                            v-model="leaveReqDTO.beginTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="请选择开始时间"
                    >
                    </el-date-picker>
                    <span style="font-size: 14px;color: #606266;">-</span>
                    <el-date-picker
                            v-model="leaveReqDTO.endTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="请选择截止时间"
                    >
                    </el-date-picker>
                </div>
                <el-button type="primary" @click="getLeaveList" style="margin-left: 10px" size="small">查询</el-button>
                <el-table :data="leaveManage" border>
                    <el-table-column type="index" label="序号" width="80"></el-table-column>
                    <el-table-column prop="description" label="请假原因" align="center"></el-table-column>
                    <el-table-column prop="userName" label="请假人" align="center" width="130"></el-table-column>
                    <el-table-column prop="hours" label="时长" align="center" width="80"></el-table-column>
                    <el-table-column prop="typeName" label="类型" align="center" width="80"></el-table-column>
                    <el-table-column prop="beginTime" label="开始日期"  width="150"  align="center">
                        <template scope="scope">
                            <div type="text" size="small" >{{scope.row.beginTime | formatTime}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="endTime" label="结束日期"  width="150"  align="center">
                        <template scope="scope">
                            <div type="text" size="small" >{{scope.row.endTime | formatTime}}</div>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination
                            @current-change="leaveHandleCurrentChange"
                            :current-page.sync="leaveReqDTO.pageNum"
                            :page-size="leaveFormPage.pageSize"
                            :layout="leavePageLayout"
                            :total="leaveFormPage.total">
                    </el-pagination>
                </div>
            </div>
        </div>
        <div class="my-task-con" v-show="userRole === 3">
            <p class="mic-title" style="margin-top: 20px">加班统计</p>
            <div class="my-task-detail" style="width: 1200px;">
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="extraWorkReqDTO.userId" clearable filterable   placeholder="筛选用户">
                        <el-option v-for="item in checkInUsers" :key="item.userId" :label="item.userName"
                                   :value="item.userId"></el-option>
                    </el-select>
                </div>
                <!--<span class="fl" style="font-size: 15px;margin-top: 5px;margin-left: 10px;color: #1d90e6">加班时间:</span>-->
                <div class="add-member-basic-msg fl">
                    <el-date-picker
                            v-model="ewBeginTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="选择开始时间"
                    >
                    </el-date-picker>
                    <span style="font-size: 14px;color: #606266;">-</span>
                    <el-date-picker
                            v-model="ewEndTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="选择截止时间"
                    >
                    </el-date-picker>
                </div>
                <el-button type="primary" @click="searchEWorkStats" style="margin-left: 10px" size="small">查询</el-button>
                <el-table :data="extraWorkStatsList" border>
                    <el-table-column type="index" label="序号" align="center" width="80">
                        <template scope="scope">
                            {{(extraWorkReqDTO.pageNum-1)*10 + scope.$index + 1}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="reason" label="加班原因" align="center"></el-table-column>
                    <el-table-column prop="userName" label="申请人" align="center" width="130"></el-table-column>
                    <el-table-column prop="workHours" label="时长" align="center" width="80"></el-table-column>
                    <el-table-column prop="beginTime" label="开始日期"  width="150"  align="center">
                        <template scope="scope">
                            <div type="text" size="small" >{{scope.row.beginTime | formatTime}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="endTime" label="结束日期"  width="150"  align="center">
                        <template scope="scope">
                            <div type="text" size="small" >{{scope.row.endTime | formatTime}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="checkRecords" label="打卡记录" align="left">
                        <template scope="scope">
                            <span v-if="scope.row.checkRecords.length === 0">暂无</span>
                            <span v-else>{{scope.row.checkRecords}}</span>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination
                            @current-change="eWorkHandleCurrentChange"
                            :current-page.sync="extraWorkReqDTO.pageNum"
                            :page-size="extraWorkPage.pageSize"
                            :layout="leavePageLayout"
                            :total="extraWorkPage.total">
                    </el-pagination>
                </div>
            </div>

        </div>

        <div class="my-task-con">
            <div v-show="userRole>0 && userRole < 3">
                <!--<div class="add-task" style="margin-right: 320px" @click="createMultipleTask">-->
                    <!--<span class="add-task-icon"><i class="el-icon-plus"></i></span>-->
                    <!--<span>创建多人任务</span>-->
                <!--</div>-->
                <div class="add-task" style="margin-right: 220px" @click="createExtraWork">
                    <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                    <span>加班申请</span>
                </div>
                <div class="add-task" @click="createQuestionClick">
                    <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                    <span>创建线上问题(数据)记录</span>
                </div>
                <!--<div class="add-task  question" style="margin-right: -153px" @click="createTaskClick">-->
                    <!--<span class="add-task-icon"><i class="el-icon-plus"></i></span>-->
                    <!--<span>创建个人任务</span>-->
                <!--</div>-->
                <div class="add-task  question" style="margin-right: -153px" @click="createTaskClick">
                    <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                    <span>创建任务</span>
                </div>
                <!--<div class="add-task help" @click="editHelpVisible=true">-->
                    <!--<span class="add-task-icon"><i class="el-icon-plus"></i></span>-->
                    <!--<span>创建积分转移</span>-->
                <!--</div>-->

                <div class="add-task leave" @click="editLeaveVisible=true,clearLeaveForm()">
                    <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                    <span>请假申请</span>
                </div>
                <div v-show="showTaskReviewTabVisible">
                    <p class="mic-title">多人任务审核</p>
                    <el-tabs v-model="taskTempActiveName2" @tab-click="handleClickMultiTask">
                        <el-tab-pane label="待审核" name="wait">
                            <div class="task-lis" v-for="item in taskTemp.waitAssess2" @click="getTaskTempDetail(item)">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name" style="width: 700px">
                                        <span style="color: red">(待审核 多人任务)</span>
                                        <span>{{item.taskName}}:({{item.description}})</span>
                                    </div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人：{{item.userName}}</span>
                                        <span class="task-end blue">截止时间：{{item.endTime| formatDate }}</span>
                                    </div>
                                </div>
                                <div class="task-mark" style="position:relative; left:-10px">
                                    <img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;vertical-align: middle">
                                    <img v-else="" src="../assets/img/u431.png" alt="" style="vertical-align: middle">
                                    <!--<img src="../assets/img/u431.png" alt="">-->
                                    <span  class="mark-msg">{{item.projectName}}</span>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!==''"
                                         :src="item.avatarUrl" :alt="item.userName">
                                    <span v-else="">{{item.userName}}</span>
                                </div>
                            </div>
                            <div v-show="this.taskTemp.waitAssess2.length===0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="审核通过" name="access">
                            <div class="task-lis" v-for="item in taskTemp.auditSuccess2" @click="getTaskTempDetail(item)">
                                <div class="head-img"><img src="../assets/img/finished.jpg"></div>
                                <div class="main-task-detail">
                                    <div class="task-name" style="width: 700px;">
                                        <span>{{item.taskName}}:({{item.description}})</span>
                                    </div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人：{{item.userName}}</span>
                                        <span class="task-end green" v-if="item.completeTime">完成时间：{{item.completeTime| formatDate }}</span>
                                        <span class="task-end blue" v-else>截止时间: {{item.endTime| formatDate }}</span>
                                        <span class="task-time-opt"><i class="el-icon-circle-check"></i></span>
                                    </div>
                                </div>
                                <div class="task-mark" style="position:relative; left:-10px">
                                    <img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;vertical-align: middle">
                                    <img v-else="" src="../assets/img/u431.png" alt="" style="vertical-align: middle;" >
                                    <!--<img src="../assets/img/u431.png" alt="">-->
                                    <span  class="mark-msg">{{item.projectName}}</span>
                                </div>
                                <div class="task-data-show">
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!==''"
                                         :src="item.avatarUrl" :alt="item.userName">
                                    <span v-else="">{{item.userName}}</span>
                                </div>
                            </div>
                            <div class="pagination" v-show="this.taskTemp.auditSuccess2.length>0">
                                <el-pagination
                                        @current-change="handleAuditSuccessTaskTempPage2"
                                        :current-page.sync="taskTempPage4.pageNum"
                                        :page-size="taskTempPage4.pageSize"
                                        :layout="taskTempAuditSuccessPageLayout2"
                                        :total="taskTempPage4.total">
                                </el-pagination>
                            </div>
                            <div v-show="this.taskTemp.auditSuccess2.length===0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
                <p class="mic-title">我的任务</p>
                <div class="my-task-detail">
                    <el-tabs v-model="activeName" @tab-click="handleClick">
                        <el-tab-pane label="进行中" name="doing">
                            <task-item :taskItems="task.doing" :isPrivate="true"
                                       taskStatus="TaskDoing" @reload="reload"
                                       :projectList="projectList"
                                       :userList="checkInUsers"
                                       :stageList="stageList"
                                       :viewType="1"
                                       :tagList="tagList"></task-item>
                        </el-tab-pane>
                        <el-tab-pane label="已完成" name="completed">
                            <task-item :taskItems="task.finished" :isPrivate="true" taskStatus="finished"
                                       :projectList="projectList"
                                       :userList="checkInUsers"
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
                                    <img class="task-avatar" v-if="test.avatarUrl && test.avatarUrl!==''"
                                         :src="test.avatarUrl">
                                    <span v-else="">{{test.userName}}</span>
                                </div>
                            </div>
                            <div v-show="task.test.length===0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <!--<el-tab-pane label="审核失败" name="applyFail">
                            <task-item :taskItems="task.applyFail" :isPrivate="true"
                                       taskStatus="ApplyFail" @reload="reload"></task-item>
                        </el-tab-pane>-->
                    </el-tabs>
                </div>
                <!--<p class="mic-title">多人任务申请</p>-->
                <!--<div class="my-task-detail">-->
                    <!--<el-tabs v-model="taskTempActiveName" @tab-click="handleClick">-->
                        <!--<el-tab-pane label="待审核" name="wait">-->
                            <!--<div class="task-lis" v-for="item in taskTemp.waitAssess" @click="getTaskTempDetail(item)">-->
                                <!--<div class="head-img"><img src="../assets/img/doing.png"></div>-->
                                <!--<div class="main-task-detail">-->
                                    <!--<div class="task-name" style="width: 700px;">-->
                                        <!--<span>{{item.taskName}}:({{item.description}})</span>-->
                                    <!--</div>-->
                                    <!--<div class="task-state">-->
                                        <!--<span class="task-end blue">申请人：{{item.userName}}</span>-->
                                        <!--<span class="task-end blue">截止时间：{{item.endTime| formatDate }}</span>-->
                                    <!--</div>-->
                                <!--</div>-->
                                <!--<div class="task-mark" style="position:relative; left:-10px">-->
                                    <!--<img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;">-->
                                    <!--<img v-else="" src="../assets/img/u431.png" alt="" >-->
                                    <!--<span  class="mark-msg">{{item.projectName}}</span>-->
                                <!--</div>-->
                                <!--<div class="task-data-show">-->
                                <!--</div>-->
                                <!--<div class="task-username">-->
                                    <!--<img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!=''"-->
                                         <!--:src="item.avatarUrl" :alt="item.userName">-->
                                    <!--<span v-else="">{{item.userName}}</span>-->
                                <!--</div>-->
                            <!--</div>-->
                            <!--<div v-show="taskTemp.waitAssess.length==0 || taskTemp.waitAssess == null" class="empty">-->
                                <!--<h2>暂无数据</h2>-->
                            <!--</div>-->
                            <!--<div class="pagination" v-show="this.taskTemp.waitAssess.length>0">-->
                            <!--<el-pagination-->
                            <!--@current-change="handleWaitAuditTaskTempPage"-->
                            <!--:current-page.sync="taskTempPage.pageNum"-->
                            <!--:page-size="taskTempPage.pageSize"-->
                            <!--:layout="taskTempWaitAuditPageLayout"-->
                            <!--:total="taskTempPage.total">-->
                            <!--</el-pagination>-->
                            <!--</div>-->
                        <!--</el-tab-pane>-->
                        <!--<el-tab-pane label="审核通过" name="access">-->
                            <!--<div class="task-lis" v-for="item in taskTemp.auditSuccess" @click="getTaskTempDetail(item)">-->
                                <!--<div class="head-img"><img src="../assets/img/finished.jpg"></div>-->
                                <!--<div class="main-task-detail">-->
                                    <!--<div class="task-name" style="width: 700px;">-->
                                        <!--<span>{{item.taskName}}:({{item.description}})</span>-->
                                    <!--</div>-->
                                    <!--<div class="task-state">-->
                                        <!--<span class="task-end blue">申请人：{{item.userName}}</span>-->
                                        <!--<span class="task-end blue">截止时间：{{item.endTime| formatDate }}</span>-->
                                    <!--</div>-->
                                <!--</div>-->
                                <!--<div class="task-mark" style="position:relative; left:-10px">-->
                                    <!--<img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;">-->
                                    <!--<img v-else="" src="../assets/img/u431.png" alt="" >-->
                                    <!--&lt;!&ndash;<img src="../assets/img/u431.png" alt="">&ndash;&gt;-->
                                    <!--<span  class="mark-msg">{{item.projectName}}</span>-->
                                <!--</div>-->
                                <!--<div class="task-data-show">-->
                                <!--</div>-->
                                <!--<div class="task-username">-->
                                    <!--<img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!=''"-->
                                         <!--:src="item.avatarUrl" :alt="item.userName">-->
                                    <!--<span v-else="">{{item.userName}}</span>-->
                                <!--</div>-->
                            <!--</div>-->
                            <!--<div v-show="taskTemp.auditSuccess.length==0 || taskTemp.auditSuccess == null" class="empty">-->
                                <!--<h2>暂无数据</h2>-->
                            <!--</div>-->
                            <!--<div class="pagination" v-show="this.taskTemp.auditSuccess.length>0">-->
                                <!--<el-pagination-->
                                        <!--@current-change="handleAuditSuccessTaskTempPage"-->
                                        <!--:current-page.sync="taskTempPage2.pageNum"-->
                                        <!--:page-size="taskTempPage2.pageSize"-->
                                        <!--:layout="taskTempAuditSuccessPageLayout"-->
                                        <!--:total="taskTempPage2.total">-->
                                <!--</el-pagination>-->
                            <!--</div>-->
                        <!--</el-tab-pane>-->
                    <!--</el-tabs>-->
                <!--</div>-->
                <p class="mic-title">我的调休</p>
                <div class="my-task-detail" style="width: 1100px">
                    <el-table :data="myRestHours" border>
                        <el-table-column prop="totalRestHours" label="总调休" align="center" >
                        </el-table-column>
                        <el-table-column prop="goneRestHours" label="已用调休" align="center">
                        </el-table-column>
                        <el-table-column prop="leftRestHours" label="剩余调休" align="center">
                        </el-table-column>
                        <el-table-column prop="endDate" label="截止日期" align="center">
                        </el-table-column>
                        <el-table-column label="操作" align="center" width="150">
                            <template scope="scope">
                                <a style="color: blue;cursor: pointer"  @click="showRestHoursDetail">查看调休日志</a>
                            </template>
                        </el-table-column>
                    </el-table>
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
                    <!--<span @click="showRestHoursDetail" style="font-size: 15px;cursor: pointer;text-decoration: underline">剩余调休(截止上月底): {{myRestHours}}H</span>-->
                    <el-table :data="signInData" border>
                        <el-table-column prop="date" label="日期" align="center" width="120">
                            <template scope="scope">
                                {{scope.row.date | formatDate2}}{{scope.row.weekday}}
                                <span v-show="scope.row.isWeekend === 1" style="color: #3da7f5">(周末)</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="userName" label="用户" align="center" width="110">
                            <template scope="scope">
                                <span style="color: red" v-if="scope.row.isForget === 1">(漏)</span>{{scope.row.userName}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="checkTimeList" label="打卡记录" align="left">
                            <template scope="scope">
                                {{scope.row.checkTimeList}}
                            </template>
                        </el-table-column>
                        <el-table-column prop="checkInTime" label="上班时间" align="center" width="120" >
                            <template scope="scope">
                                <span style="color: red" v-if="scope.row.isRecheckIn === 1">(补)</span>
                                <span v-if="scope.row.isCheckInAfterTen === 1" style="color: red">{{scope.row.checkInTime | formatTime2}}</span>
                                <span v-else>{{scope.row.checkInTime | formatTime2}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="checkOutTime" label="下班时间" align="center" width="120" >
                            <template scope="scope">
                                <span style="color: red" v-if="scope.row.isRecheckOut === 1">(补)</span>
                                <span style="color: green" v-if="scope.row.isWorkToNextDay === 1">(+1)</span>
                                <span v-if="scope.row.isCheckOutBeforeSix === 1" style="color: red">{{scope.row.checkOutTime | formatTime2}}</span>
                                <span v-else>{{scope.row.checkOutTime | formatTime2}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="workTime" label="上班时长" align="center" width="120" >
                            <template scope="scope">
                                <span v-if="scope.row.lessThanNine === 1" style="color: red">{{scope.row.workTime}}</span>
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
                                <a v-show="scope.row.canReCheck === 1" style="color: blue;cursor: pointer" @click="recheck(scope.row.userId,scope.row.date)">补签到</a>
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
                    <el-tabs v-model="activeQuestionName" @tab-click="handleClickMyOnlineQuestion">
                        <el-tab-pane label="待审核" name="wait">
                            <div class="task-lis" v-for="item in question.running" @click="item.reviewStatus === 1 ? editQuestion(item) : finishQuestion(item)">
                                <div class="head-img"><img src="../assets/img/doing.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name" style="width: 700px;">
                                        <span v-if="item.reviewStatus === 1" style="color: red">(待审核)</span><span>{{item.name}}:({{item.description}})</span>
                                    </div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人：{{item.userName}}</span>
                                        <span v-if="item.isToday === -1" class="task-end red">截止时间：{{item.endTime| formatDate }}</span>
                                        <span v-if="item.isToday === 0" class="task-end orange">截止时间：{{item.endTime| formatDate }}</span>
                                        <span v-if="item.isToday === 1" class="task-end blue">截止时间：{{item.endTime| formatDate }}</span>
                                    </div>
                                </div>
                                <div class="task-mark" style="position:relative; left:-10px">
                                    <img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;vertical-align: middle">
                                    <img v-else="" src="../assets/img/u431.png" alt="" style="vertical-align: middle">
                                    <span  class="mark-msg">{{item.projectName}}</span>
                                </div>
                                <div class="task-data-show">
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!==''"
                                         :src="item.avatarUrl" :alt="item.userName">
                                    <span v-else="">{{item.userName}}</span>
                                </div>
                            </div>
                            <div v-show="question.running.length===0 || question.running == null" class="empty">
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
                                    <img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;vertical-align: middle">
                                    <img v-else="" src="../assets/img/u431.png" alt="" style="vertical-align: middle" >
                                    <!--<img src="../assets/img/u431.png" alt="">-->
                                    <span  class="mark-msg">{{item.projectName}}</span>
                                </div>
                                <div class="task-data-show">
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!==''"
                                         :src="item.avatarUrl" :alt="item.userName">
                                    <span v-else="">{{item.userName}}</span>
                                </div>
                            </div>
                            <div v-show="question.completed.length===0 || question.completed == null" class="empty">
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
                    <el-tabs v-model="assessActiveName" @tab-click="handleClickEvaluate">
                        <el-tab-pane label="待评价" name="waitAssess">
                            <task-item :taskItems="task.waitAssess" :isPrivate="true" @reload="reload"
                                       taskStatus="WaitAssess"
                                       :projectList="projectList"
                                       :userList="checkInUsers"
                                       :stageList="stageList"
                                       :viewType="1"
                                       :tagList="tagList"></task-item>
                        </el-tab-pane>

                        <el-tab-pane label="已评价" name="commented">
                            <task-item :taskItems="task.commented" :isPrivate="true" taskStatus="WaitAssess"
                                       :projectList="projectList"
                                       :userList="checkInUsers"
                                       :stageList="stageList"
                                       :viewType="1"
                                       :tagList="tagList"></task-item>
                            <div class="pagination" v-show="this.task.commented.length>0">
                                <el-pagination
                                        @current-change="handleEvaluatedPage"
                                        :current-page.sync="commentedPage.pageNum"
                                        :page-size="commentedPage.pageSize"
                                        :layout="commentedPageLayout"
                                        :total="commentedPage.total">
                                </el-pagination>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
                <!--<div>-->
                    <!--<p class="mic-title">任务延长申请</p>-->
                    <!--<el-tabs v-model="taskExpandTabsActiveName" @tab-click="handleClick">-->
                        <!--<el-tab-pane label="待审核" name="wait">-->
                            <!--<div class="task-lis" v-for="expand in taskExpand.doing" @click="getExpandDetail(expand)">-->
                                <!--<div class="head-img"><img src="../assets/img/waitAudit.png"></div>-->
                                <!--<div class="main-task-detail">-->
                                    <!--<div class="task-name"><span>{{expand.taskName}}:{{expand.reason}}</span></div>-->
                                    <!--<div class="task-state">-->
                                        <!--<span class="task-end blue">申请人：{{expand.userName}}</span>-->
                                        <!--<span class="task-end blue">截止时间：{{expand.endTime| formatDate }}</span>-->
                                    <!--</div>-->
                                <!--</div>-->
                                <!--<div class="task-data-show">-->
                                    <!--<span class="task-score">延长时间：{{expand.hours}} 小时</span>-->
                                <!--</div>-->
                                <!--<div class="task-username">-->
                                    <!--<img class="task-avatar" v-if="expand.avatarUrl && expand.avatarUrl!=''"-->
                                         <!--:src="expand.avatarUrl">-->
                                    <!--<span v-else="">{{expand.userName}}</span>-->
                                <!--</div>-->
                            <!--</div>-->
                            <!--<div v-show="taskExpand.doing.length==0" class="empty">-->
                                <!--<h2>暂无数据</h2>-->
                            <!--</div>-->
                        <!--</el-tab-pane>-->
                        <!--<el-tab-pane label="审核通过" name="completed">-->
                            <!--<div class="task-lis" v-for="expand in taskExpand.finished">-->
                                <!--<div class="head-img"><img src="../assets/img/waitAudit.png"></div>-->
                                <!--<div class="main-task-detail">-->
                                    <!--<div class="task-name"><span>{{expand.reason}}</span></div>-->
                                    <!--<div class="task-state">-->
                                        <!--<span class="task-end blue">申请人：{{expand.userName}}</span>-->
                                        <!--<span class="task-end blue">截止时间：{{expand.endTime| formatDate }}</span>-->
                                        <!--<span class="task-time-opt"><i class="el-icon-circle-check"></i></span>-->
                                    <!--</div>-->
                                <!--</div>-->
                                <!--<div class="task-data-show">-->
                                    <!--<span class="task-score">延长时间：{{expand.hours}} 小时</span>-->
                                <!--</div>-->
                                <!--<div class="task-username">-->
                                    <!--<img class="task-avatar" v-if="expand.avatarUrl && expand.avatarUrl!=''"-->
                                         <!--:src="expand.avatarUrl">-->
                                    <!--<span v-else="">{{expand.userName}}</span>-->
                                <!--</div>-->
                            <!--</div>-->
                            <!--<div class="pagination" v-show="this.taskExpand.finished.length>0">-->
                                <!--<el-pagination-->
                                        <!--@current-change="handleExpandPage"-->
                                        <!--:current-page.sync="expandPage.pageNum"-->
                                        <!--:page-size="expandPage.pageSize"-->
                                        <!--:layout="expandPageLayout"-->
                                        <!--:total="expandPage.total">-->
                                <!--</el-pagination>-->
                            <!--</div>-->
                            <!--<div v-show="taskExpand.finished.length==0" class="empty">-->
                                <!--<h2>暂无数据</h2>-->
                            <!--</div>-->
                        <!--</el-tab-pane>-->
                    <!--</el-tabs>-->
                <!--</div>-->
                <div>
                    <p class="mic-title">任务修改申请</p>
                    <el-tabs v-model="taskModifyTabsActiveName2" @tab-click="handleClickMyModify">
                        <el-tab-pane label="待审核" name="wait">
                            <div class="task-lis" v-for="modifyData in personalTaskModifyData.waitAssess" @click="getTaskModifyDetail(modifyData.id,modifyData.taskId,modifyData.userId)">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>{{modifyData.taskName}}:{{(modifyData.reason)}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人：{{modifyData.userName}}</span>
                                        <span class="task-end blue">截止时间：{{modifyData.endTime| formatDate }}</span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="modifyData.avatarUrl && modifyData.avatarUrl!==''"
                                         :src="modifyData.avatarUrl">
                                    <span v-else="">{{modifyData.userName}}</span>
                                </div>
                            </div>
                            <div v-show="this.personalTaskModifyData.waitAssess.length===0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="审核通过" name="accessed">
                            <div class="task-lis" v-for="modifyData in personalTaskModifyData.auditSuccess"
                                 @click="getTaskModifyDetail(modifyData.id,modifyData.taskId,modifyData.userId)">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>{{modifyData.reason}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人：{{modifyData.userName}}</span>
                                        <span class="task-end blue">截止时间：{{modifyData.endTime| formatDate }}</span>
                                        <span class="task-time-opt"><i class="el-icon-circle-check"></i></span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="modifyData.avatarUrl && modifyData.avatarUrl!==''"
                                         :src="modifyData.avatarUrl">
                                    <span v-else="">{{modifyData.userName}}</span>
                                </div>
                            </div>
                            <div class="pagination" v-show="this.personalTaskModifyData.auditSuccess.length>0">
                                <el-pagination
                                        @current-change="handlePersonalTaskModifyPage"
                                        :current-page.sync="personalTaskModifyPage.pageNum"
                                        :page-size="personalTaskModifyPage.pageSize"
                                        :layout="personalTaskModifyPageLayout"
                                        :total="personalTaskModifyPage.total">
                                </el-pagination>
                            </div>
                            <div v-show="personalTaskModifyData.auditSuccess.length===0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
                <!--<div>-->
                    <!--<p class="mic-title">我的积分转移</p>-->
                    <!--<div class="my-task-detail">-->
                        <!--<el-tabs v-model="activeHelpName" @tab-click="handleClick">-->
                            <!--<el-tab-pane label="审核中" name="wait">-->
                                <!--&lt;!&ndash;@click="reviewDetail(help)"&ndash;&gt;-->
                                <!--<div class="task-lis" v-for="help in review.wait">-->
                                    <!--<div class="head-img"><img src="../assets/img/waitAudit.png"></div>-->
                                    <!--<div class="main-task-detail">-->
                                        <!--<div class="task-name"><span>{{help.description}}</span></div>-->
                                        <!--<div class="task-state">-->
                                            <!--<span class="task-end blue">求助目标：{{help.name.split(",")[0]}}</span>-->
                                            <!--<span class="task-end blue">{{help.time| formatDate }}</span>-->
                                            <!--<span class="task-time-opt"><i class="el-icon-edit"-->
                                                                           <!--@click="editHelpDetail(help)"></i></span>-->
                                        <!--</div>-->
                                    <!--</div>-->
                                    <!--<div class="task-data-show">-->
                                        <!--<span class="task-score">求助转移：{{help.integral}} 分</span>-->
                                    <!--</div>-->
                                    <!--<div class="task-username">-->
                                        <!--<img class="task-avatar" v-if="help.avatarUrl && help.avatarUrl!=''"-->
                                             <!--:src="help.avatarUrl">-->
                                        <!--<span v-else="">{{help.name.split(",")[1]}}</span>-->
                                    <!--</div>-->
                                <!--</div>-->
                                <!--<div class="pagination" v-show="this.review.wait.length>0">-->
                                    <!--<el-pagination-->
                                            <!--@current-change="handleWaitPage"-->
                                            <!--:current-page.sync="waitPage.pageNum"-->
                                            <!--:page-size="waitPage.pageSize"-->
                                            <!--:layout="waitPageLayout"-->
                                            <!--:total="waitPage.total">-->
                                    <!--</el-pagination>-->
                                <!--</div>-->
                                <!--<div v-show="review.wait.length==0" class="empty">-->
                                    <!--<h2>暂无数据</h2>-->
                                <!--</div>-->
                            <!--</el-tab-pane>-->
                            <!--<el-tab-pane label="完成审核" name="review">-->
                                <!--<div class="task-lis" v-for="help in review.review" @click="reviewDetail(help)">-->
                                    <!--<div class="head-img"><img src="../assets/img/auditSuccess.png"></div>-->
                                    <!--<div class="main-task-detail">-->
                                        <!--<div class="task-name"><span>{{help.description}}</span></div>-->
                                        <!--<div class="task-state">-->
                                            <!--<span class="task-end blue">求助目标：{{help.name.split(",")[0]}}</span>-->
                                            <!--<span class="task-end blue">{{help.time| formatDate }}</span>-->
                                            <!--<span class="task-time-opt"><i class="el-icon-circle-check"></i></span>-->
                                        <!--</div>-->
                                    <!--</div>-->
                                    <!--<div class="task-data-show">-->
                                        <!--<span class="task-score">转移完成：-{{help.integral}} 分</span>-->
                                    <!--</div>-->
                                    <!--<div class="task-username">-->
                                        <!--<img class="task-avatar" v-if="help.avatarUrl && help.avatarUrl!=''"-->
                                             <!--:src="help.avatarUrl">-->
                                        <!--<span v-else="">{{help.name.split(",")[1]}}</span>-->
                                    <!--</div>-->
                                <!--</div>-->
                                <!--<div class="pagination" v-show="this.review.review.length>0">-->
                                    <!--<el-pagination-->
                                            <!--@current-change="handleReviewPage"-->
                                            <!--:current-page.sync="reviewPage.pageNum"-->
                                            <!--:page-size="reviewPage.pageSize"-->
                                            <!--:layout="reviewPageLayout"-->
                                            <!--:total="reviewPage.total">-->
                                    <!--</el-pagination>-->
                                <!--</div>-->
                                <!--<div v-show="review.review.length==0" class="empty">-->
                                    <!--<h2>暂无数据</h2>-->
                                <!--</div>-->
                            <!--</el-tab-pane>-->
                        <!--</el-tabs>-->
                    <!--</div>-->
                <!--</div>-->
            </div>
            <div v-show="userRole===0">
                <div v-show="showTaskReviewTabVisible">
                    <p class="mic-title">多人任务审核</p>
                    <el-tabs v-model="taskTempActiveName2" @tab-click="handleClickMultiTask">
                        <el-tab-pane label="待审核" name="wait">
                            <div class="task-lis" v-for="item in taskTemp.waitAssess2" @click="getTaskTempDetail(item)">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name" style="width: 700px">
                                        <span style="color: red">(待审核 多人任务)</span>
                                        <span>{{item.taskName}}:({{item.description}})</span>
                                    </div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人：{{item.userName}}</span>
                                        <span class="task-end blue">截止时间：{{item.endTime| formatDate }}</span>
                                    </div>
                                </div>
                                <div class="task-mark" style="position:relative; left:-10px">
                                    <img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;vertical-align: middle">
                                    <img v-else="" src="../assets/img/u431.png" alt="" style="vertical-align: middle">
                                    <!--<img src="../assets/img/u431.png" alt="">-->
                                    <span  class="mark-msg">{{item.projectName}}</span>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!==''"
                                         :src="item.avatarUrl" :alt="item.userName">
                                    <span v-else="">{{item.userName}}</span>
                                </div>
                            </div>
                            <div v-show="this.taskTemp.waitAssess2.length===0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane label="审核通过" name="access">
                            <div class="task-lis" v-for="item in taskTemp.auditSuccess2" @click="getTaskTempDetail(item)">
                                <div class="head-img"><img src="../assets/img/finished.jpg"></div>
                                <div class="main-task-detail">
                                    <div class="task-name" style="width: 700px;">
                                        <span>{{item.taskName}}:({{item.description}})</span>
                                    </div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人：{{item.userName}}</span>
                                        <span class="task-end green" v-if="item.completeTime">完成时间：{{item.completeTime| formatDate }}</span>
                                        <span class="task-end blue" v-else>截止时间: {{item.endTime| formatDate }}</span>
                                        <span class="task-time-opt"><i class="el-icon-circle-check"></i></span>
                                    </div>
                                </div>
                                <div class="task-mark" style="position:relative; left:-10px">
                                    <img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;vertical-align: middle">
                                    <img v-else="" src="../assets/img/u431.png" alt="" style="vertical-align: middle">
                                    <!--<img src="../assets/img/u431.png" alt="">-->
                                    <span  class="mark-msg">{{item.projectName}}</span>
                                </div>
                                <div class="task-data-show">
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!==''"
                                         :src="item.avatarUrl" :alt="item.userName">
                                    <span v-else="">{{item.userName}}</span>
                                </div>
                            </div>
                            <div class="pagination" v-show="this.taskTemp.auditSuccess2.length>0">
                                <el-pagination
                                        @current-change="handleAuditSuccessTaskTempPage2"
                                        :current-page.sync="taskTempPage4.pageNum"
                                        :page-size="taskTempPage4.pageSize"
                                        :layout="taskTempAuditSuccessPageLayout2"
                                        :total="taskTempPage4.total">
                                </el-pagination>
                            </div>
                            <div v-show="this.taskTemp.auditSuccess2.length===0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
                <p class="mic-title">个人任务审核</p>
                <div class="add-task" style="float: left;margin-top: -22px;margin-right: 400px;font-size: 14px"
                     @click="integralBasicVisible=true">
                    <!--<span class="task-time-opt" style="font-size:14px"><i class="el-icon-edit"></i></span>计算基准积分-->
                </div>
                <el-tabs v-model="auditTabsActiveName" @tab-click="handleClickPrivateTask">
                    <el-tab-pane label="待审核" name="wait">
                        <task-item :taskItems="task.waitAudit" :isPrivate="true" @reload="reload"
                                   taskStatus="WaitAuditing"
                                   :projectList="projectList"
                                   :userList="checkInUsers"
                                   :stageList="stageList"
                                   :viewType="1"
                                   :tagList="tagList"></task-item>
                        <div class="pagination" v-show="this.task.waitAudit.length>0">
                            <el-pagination
                                    @current-change="handleWaitAuditPage"
                                    :current-page.sync="waitAuditPage.pageNum"
                                    :page-size="waitAuditPage.pageSize"
                                    :layout="waitAuditPageLayout"
                                    :total="waitAuditPage.total">
                            </el-pagination>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="审核通过" name="completed">
                        <task-item :taskItems="task.auditSuccess" :isPrivate="true" @reload="reload"
                                   taskStatus="auditSuccess"
                                   :projectList="projectList"
                                   :userList="checkInUsers"
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
                <!--<p class="mic-title">任务延长申请审核</p>-->
                <!--<el-tabs v-model="taskExpandTabsActiveName" @tab-click="handleClick">-->
                    <!--<el-tab-pane label="待审核" name="wait">-->
                        <!--<div class="task-lis" v-for="expand in taskExpand.doing" @click="getExpandDetail(expand)">-->
                            <!--<div class="head-img"><img src="../assets/img/waitAudit.png"></div>-->
                            <!--<div class="main-task-detail">-->
                                <!--<div class="task-name"><span>{{expand.taskName}}:{{expand.reason}}</span></div>-->
                                <!--<div class="task-state">-->
                                    <!--<span class="task-end blue">申请人：{{expand.userName}}</span>-->
                                    <!--<span class="task-end blue">截止时间: {{expand.endTime| formatDate }}</span>-->
                                    <!--<span class="task-time-opt"><i class="el-icon-edit" ></i></span>-->
                                <!--</div>-->
                                <!--&lt;!&ndash;<div style="margin-top: 8px">&ndash;&gt;-->
                                    <!--&lt;!&ndash;<span class="task-end green">创建时间: {{expand.createTime| formatDate }}</span>&ndash;&gt;-->
                                <!--&lt;!&ndash;</div>&ndash;&gt;-->
                            <!--</div>-->
                            <!--<div class="task-data-show">-->
                                <!--<span class="task-score">延长时间：{{expand.hours}} 小时</span>-->
                            <!--</div>-->
                            <!--<div class="task-username">-->
                                <!--<img class="task-avatar" v-if="expand.avatarUrl && expand.avatarUrl!=''"-->
                                     <!--:src="expand.avatarUrl">-->
                                <!--<span v-else="">{{expand.userName}}</span>-->
                            <!--</div>-->
                        <!--</div>-->
                        <!--<div class="pagination" v-show="this.taskExpand.doing.length>0">-->
                            <!--<el-pagination-->
                                    <!--@current-change="handleExpandDoingPage"-->
                                    <!--:current-page.sync="expandDoingPage.pageNum"-->
                                    <!--:page-size="expandDoingPage.pageSize"-->
                                    <!--:layout="expandDoingPageLayout"-->
                                    <!--:total="expandDoingPage.total">-->
                            <!--</el-pagination>-->
                        <!--</div>-->
                        <!--<div v-show="taskExpand.doing.length==0" class="empty">-->
                            <!--<h2>暂无数据</h2>-->
                        <!--</div>-->
                    <!--</el-tab-pane>-->
                    <!--<el-tab-pane label="审核通过" name="completed">-->
                        <!--<div class="task-lis" v-for="expand in taskExpand.finished">-->
                            <!--<div class="head-img"><img src="../assets/img/waitAudit.png"></div>-->
                            <!--<div class="main-task-detail">-->
                                <!--<div class="task-name"><span>{{expand.taskName}}:{{expand.reason}}</span></div>-->
                                <!--<div class="task-state">-->
                                    <!--<span class="task-end blue">申请人：{{expand.userName}}</span>-->
                                    <!--<span class="task-end blue">截止时间：{{expand.endTime| formatDate }}</span>-->
                                    <!--<span class="task-time-opt"><i class="el-icon-circle-check"></i></span>-->
                                <!--</div>-->
                                <!--&lt;!&ndash;<div style="margin-top: 8px">&ndash;&gt;-->
                                    <!--&lt;!&ndash;<span class="task-end green">创建时间: {{expand.createTime| formatDate }}</span>&ndash;&gt;-->
                                <!--&lt;!&ndash;</div>&ndash;&gt;-->
                            <!--</div>-->
                            <!--<div class="task-data-show">-->
                                <!--<span class="task-score">延长时间：{{expand.hours}} 小时</span>-->
                            <!--</div>-->
                            <!--<div class="task-username">-->
                                <!--<img class="task-avatar" v-if="expand.avatarUrl && expand.avatarUrl!=''"-->
                                     <!--:src="expand.avatarUrl">-->
                                <!--<span v-else="">{{expand.userName}}</span>-->
                            <!--</div>-->
                        <!--</div>-->
                        <!--<div class="pagination" v-show="this.taskExpand.finished.length>0">-->
                            <!--<el-pagination-->
                                    <!--@current-change="handleExpandPage"-->
                                    <!--:current-page.sync="expandPage.pageNum"-->
                                    <!--:page-size="expandPage.pageSize"-->
                                    <!--:layout="expandPageLayout"-->
                                    <!--:total="expandPage.total">-->
                            <!--</el-pagination>-->
                        <!--</div>-->
                        <!--<div v-show="taskExpand.finished.length==0" class="empty">-->
                            <!--<h2>暂无数据</h2>-->
                        <!--</div>-->
                    <!--</el-tab-pane>-->
                <!--</el-tabs>-->
                <p class="mic-title">任务修改申请审核</p>
                <el-tabs v-model="taskModifyTabsActiveName" @tab-click="handleClickTaskModify">
                    <el-tab-pane label="待审核" name="wait">
                        <div class="task-lis" v-for="modifyData in taskModifyData.waitAssess"
                             @click="getTaskModifyDetail(modifyData.id,modifyData.taskId,modifyData.userId)">
                            <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                            <div class="main-task-detail">
                                <div class="task-name"><span>{{modifyData.taskName}}:{{(modifyData.reason)}}</span></div>
                                <div class="task-state">
                                    <span class="task-end blue">申请人：{{modifyData.userName}}</span>
                                    <span class="task-end blue">截止时间: {{modifyData.endTime| formatDate }}</span>
                                </div>
                            </div>
                            <div class="task-username">
                                <img class="task-avatar" v-if="modifyData.avatarUrl && modifyData.avatarUrl!==''"
                                     :src="modifyData.avatarUrl">
                                <span v-else="">{{modifyData.userName}}</span>
                            </div>
                        </div>
                        <div v-show="taskModifyData.waitAssess.length===0" class="empty">
                            <h2>暂无数据</h2>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="审核通过" name="accessed">
                        <div class="task-lis" v-for="modifyData in taskModifyData.auditSuccess"
                             @click="getTaskModifyDetail(modifyData.id,modifyData.taskId,modifyData.userId)">
                            <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                            <div class="main-task-detail">
                                <div class="task-name"><span>{{modifyData.taskName}}:{{modifyData.reason}}</span></div>
                                <div class="task-state">
                                    <span class="task-end blue">申请人：{{modifyData.userName}}</span>
                                    <span class="task-end blue">截止时间：{{modifyData.endTime| formatDate }}</span>
                                </div>
                            </div>
                            <div class="task-username">
                                <img class="task-avatar" v-if="modifyData.avatarUrl && modifyData.avatarUrl!==''"
                                     :src="modifyData.avatarUrl">
                                <span v-else="">{{modifyData.userName}}</span>
                            </div>
                        </div>
                        <div class="pagination" v-show="this.taskModifyData.auditSuccess.length>0">
                            <el-pagination
                                    @current-change="handleTaskModifyPage"
                                    :current-page.sync="taskModifyPage.pageNum"
                                    :page-size="taskModifyPage.pageSize"
                                    :layout="taskModifyPageLayout"
                                    :total="taskModifyPage.total">
                            </el-pagination>
                        </div>
                        <div v-show="taskModifyData.auditSuccess.length===0" class="empty">
                            <h2>暂无数据</h2>
                        </div>
                    </el-tab-pane>
                </el-tabs>
                <p class="mic-title">线上问题审核</p>
                <el-tabs v-model="questionActiveName" @tab-click="handleClickOnlineQuestion">
                    <el-tab-pane label="待审核" name="wait">
                        <div class="task-lis" v-for="item in question.wait" @click="checkQuestion(item)">
                            <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                            <div class="main-task-detail">
                                <div class="task-name" style="width: 700px"><span>{{item.name}}:({{item.description}})</span></div>
                                <div class="task-state">
                                    <span class="task-end blue">申请人：{{item.userName}}</span>
                                    <span v-if="item.isToday === -1" class="task-end red">截止时间：{{item.endTime| formatDate }}</span>
                                    <span v-if="item.isToday === 0" class="task-end orange">截止时间：{{item.endTime| formatDate }}</span>
                                    <span v-if="item.isToday === 1" class="task-end blue">截止时间：{{item.endTime| formatDate }}</span>
                                </div>
                            </div>
                            <div class="task-mark" style="position:relative; left:-10px">
                                <img v-if="item.projectImage" :src="item.projectImage" style="width: 40px;height: 40px;border-radius: 50%;vertical-align: middle">
                                <img v-else="" src="../assets/img/u431.png" alt="" style="vertical-align: middle">
                                <!--<img src="../assets/img/u431.png" alt="">-->
                                <span  class="mark-msg">{{item.projectName}}</span>
                            </div>
                            <div class="task-username">
                                <img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!==''"
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
                        <div v-show="question.wait.length===0" class="empty">
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
                                <img class="task-avatar" v-if="item.avatarUrl && item.avatarUrl!==''"
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
                        <div v-show="question.accepted.length===0" class="empty">
                            <h2>暂无数据</h2>
                        </div>
                    </el-tab-pane>
                </el-tabs>
                <!--<p class="mic-title">积分求助转移</p>-->
                <!--<el-tabs v-model="auditHelpTabsActiveName" @tab-click="handleClick">-->
                    <!--<el-tab-pane label="待审核" name="wait">-->
                        <!--<div class="task-lis" v-for="help in review.wait" @click="reviewDetail(help)">-->
                            <!--<div class="head-img"><img src="../assets/img/waitAudit.png"></div>-->
                            <!--<div class="main-task-detail">-->
                                <!--<div class="task-name"><span>{{help.description}}</span></div>-->
                                <!--<div class="task-state">-->
                                    <!--<span class="task-end blue">求助目标：{{help.name.split(",")[0]}}</span>-->
                                    <!--<span class="task-end blue">{{help.time| formatDate }}</span>-->
                                    <!--<span class="task-time-opt"><i class="el-icon-edit"></i></span>-->
                                <!--</div>-->
                            <!--</div>-->
                            <!--<div class="task-data-show">-->
                                <!--<span class="task-score">求助转移：{{help.integral}} 分</span>-->
                            <!--</div>-->
                            <!--<div class="task-username">-->
                                <!--<img class="task-avatar" v-if="help.avatarUrl && help.avatarUrl!=''"-->
                                     <!--:src="help.avatarUrl">-->
                                <!--<span v-else="">{{help.name.split(",")[1]}}</span>-->
                            <!--</div>-->
                        <!--</div>-->
                        <!--<div class="pagination" v-show="this.review.wait.length>0">-->
                            <!--<el-pagination-->
                                    <!--@current-change="handleWaitPage"-->
                                    <!--:current-page.sync="waitPage.pageNum"-->
                                    <!--:page-size="waitPage.pageSize"-->
                                    <!--:layout="waitPageLayout"-->
                                    <!--:total="waitPage.total">-->
                            <!--</el-pagination>-->
                        <!--</div>-->
                        <!--<div v-show="review.wait.length==0" class="empty">-->
                            <!--<h2>暂无数据</h2>-->
                        <!--</div>-->
                    <!--</el-tab-pane>-->
                    <!--<el-tab-pane label="审核通过" name="completed">-->
                        <!--<div class="task-lis" v-for="help in review.review" @click="reviewDetail(help)">-->
                            <!--<div class="head-img"><img src="../assets/img/waitAudit.png"></div>-->
                            <!--<div class="main-task-detail">-->
                                <!--<div class="task-name"><span>{{help.description}}</span></div>-->
                                <!--<div class="task-state">-->
                                    <!--<span class="task-end blue">求助目标：{{help.name.split(",")[0]}}</span>-->
                                    <!--<span class="task-end blue">{{help.time| formatDate }}</span>-->
                                    <!--<span class="task-time-opt"><i class="el-icon-circle-check"></i></span>-->
                                <!--</div>-->
                            <!--</div>-->
                            <!--<div class="task-data-show">-->
                                <!--<span class="task-score">转移完成：-{{help.integral}} 分</span>-->
                            <!--</div>-->
                            <!--<div class="task-username">-->
                                <!--<img class="task-avatar" v-if="help.avatarUrl && help.avatarUrl!=''"-->
                                     <!--:src="help.avatarUrl">-->
                                <!--<span v-else="">{{help.name.split(",")[1]}}</span>-->
                            <!--</div>-->
                        <!--</div>-->
                        <!--<div class="pagination" v-show="this.review.review.length>0">-->
                            <!--<el-pagination-->
                                    <!--@current-change="handleReviewPage"-->
                                    <!--:current-page.sync="reviewPage.pageNum"-->
                                    <!--:page-size="reviewPage.pageSize"-->
                                    <!--:layout="waitPageLayout"-->
                                    <!--:total="reviewPage.total">-->
                            <!--</el-pagination>-->
                        <!--</div>-->
                        <!--<div v-show="review.review.length==0" class="empty">-->
                            <!--<h2>暂无数据</h2>-->
                        <!--</div>-->
                    <!--</el-tab-pane>-->
                <!--</el-tabs>-->
            </div>
            <div v-show="userRole < 3">
                <p class="mic-title">{{userRole!==0?'请假申请':'请假申请审核'}}</p>
                <div class="my-task-detail">
                    <el-tabs v-model="activeLeaveName" @tab-click="handleClickLeave">
                        <el-tab-pane :label="userRole!==0?'审核中':'待审核'" name="wait">
                            <!--@click="reviewDetail(help)"-->
                            <div class="task-lis" v-for="leave in leaveList.wait" @click="editLeaveDetail(leave,0)">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>{{leave.description}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人: {{leave.userName}}</span>
                                        <span class="task-end blue">{{leave.createTime| formatDate }}</span>
                                        <span class="task-time-opt"><i class="el-icon-edit"></i></span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="leave.avatarUrl && leave.avatarUrl!==''"
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
                            <div v-show="leaveList.wait.length===0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane :label="userRole!==0?'完成审核':'审核通过'" name="review">
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
                                    <img class="task-avatar" v-if="leave.avatarUrl && leave.avatarUrl!==''"
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
                            <div v-show="leaveList.pass.length===0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </div>
            <div v-show="userRole < 3">
                <p class="mic-title">{{userRole!==0?'加班申请':'加班申请审核'}}</p>
                <div class="my-task-detail">
                    <el-tabs v-model="activeEWorkName" @tab-click="handleClickExtraWork">
                        <el-tab-pane :label="userRole!==0?'审核中':'待审核'" name="wait">
                            <div class="task-lis" v-for="eWork in eWorkList.wait" @click="showExtraWorkDetail(eWork.id)">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>{{eWork.reason}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人: {{eWork.userName}}</span>
                                        <span class="task-end blue">{{eWork.createTime| formatDate }}</span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="eWork.avatarUrl && eWork.avatarUrl!==''"
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
                            <div v-show="eWorkList.wait.length===0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane :label="userRole!==0?'完成审核':'审核通过'" name="review">
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
                                    <img class="task-avatar" v-if="eWork.avatarUrl && eWork.avatarUrl!==''"
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
                            <div v-show="eWorkList.pass.length===0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                    </el-tabs>
                </div>
            </div>
            <div v-show="userRole < 3">
                <p class="mic-title">{{userRole!==0?'补打卡申请':'补打卡申请审核'}}</p>
                <div class="my-task-detail">
                    <el-tabs v-model="activeRecheckName" @tab-click="handleClickRecheck">
                        <el-tab-pane :label="userRole!==0?'审核中':'待审核'" name="wait">
                            <div class="task-lis" v-for="recheck in recheckList.wait" @click="editRecheck(recheck)">
                                <div class="head-img"><img src="../assets/img/waitAudit.png"></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>补打卡原因: {{recheck.reason}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">申请人: {{recheck.userName}}</span>
                                        <span class="task-end blue">补打卡时间: {{recheck.recheckTime| formatTime }}</span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="recheck.avatarUrl && recheck.avatarUrl!==''"
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
                            <div v-show="recheckList.wait.length===0" class="empty">
                                <h2>暂无数据</h2>
                            </div>
                        </el-tab-pane>
                        <el-tab-pane :label="userRole!==0?'完成审核':'审核通过'" name="review">
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
                                    <img class="task-avatar" v-if="recheck.avatarUrl && recheck.avatarUrl!==''"
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
                            <div v-show="recheckList.pass.length===0" class="empty">
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
                   @close="clearPrivateTask"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="createPrivateTaskVisible">
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
                <el-form-item label="开始日期">
                    <el-date-picker
                            v-model="taskForm.beginTime"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="截止日期">
                    <el-date-picker
                            v-model="taskForm.endTime"
                            type="date"
                            value-format="yyyy-MM-dd"
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
            <el-button @click="createPrivateTaskVisible = false">取 消</el-button>
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
            <el-button type="primary" @click="saveQuestionInfo('questionForm')" :disabled="questionAble">立即创建</el-button>
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
                    <span v-for="url in questionForm.urlList" v-show="questionForm.urlList.length !== 0">
                        <img :src="url" v-if="url" style="margin-left: -50px;cursor: pointer" @click="showPic(url)">
                        <i class="el-icon-delete" style="margin-left: 10px" @click="removeUrl(url)"></i>
                        <!--<a style="color: blue;cursor: pointer" @click="showPic(url)">点击查看问题原件</a><i class="el-icon-delete" style="margin-left: 10px" @click="removeUrl(url)"></i><br/>-->
                    </span>
                </el-form-item>

            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="editQuestionInfo('questionForm')" :disabled="questionAble">修改</el-button>
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
                    <span v-for="url in questionForm.urlList" v-show="questionForm.urlList.length !== 0">
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
                    <span v-for="url in questionForm.urlList" v-show="questionForm.urlList.length !== 0">
                        <img :src="url" v-if="url" style="margin-left: -50px;cursor: pointer" @click="showPic(url)">
                        <!--<a style="color: blue;cursor: pointer" @click="showPic(url)" class="fl">点击查看问题原件</a><br/>-->
                    </span>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button type="danger" @click="deleteQuestion(questionForm.oqrId)">删除</el-button>
            <el-button type="primary" v-show="activeQuestionName === 'wait'" icon="edit" @click="editQuestion(questionForm)"></el-button>
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
                    <span v-for="url in questionForm.urlList" v-show="questionForm.urlList.length !== 0">
                        <img :src="url" v-if="url" style="margin-left: -50px;cursor: pointer" @click="showPic(url)">
                        <!--<a style="color: blue;cursor: pointer" @click="showPic(url)" class="fl">点击查看问题来源</a><br/>-->
                    </span>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button v-if="questionForm.reviewStatus === 1" type="warning">待审核</el-button>
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
                                v-for="item in checkInUsers"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="发起者" prop="name" v-show="userRole===0">{{helpForm.name}}</el-form-item>
                <el-form-item label="原求助目标" prop="username" v-show="userRole===0">{{helpForm.username}}</el-form-item>
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
                <div v-show="userRole===0&&auditHelpTabsActiveName==='wait'">
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
            <el-form v-if="userRole !== 0">
                <el-form-item class="task-form" label="任务名称：">{{expandDetail.taskName}}</el-form-item>
                <el-form-item class="task-form" label="申请人：">{{expandDetail.userName}}</el-form-item>
                <el-form-item class="task-form" label="原因：">{{expandDetail.reason}}</el-form-item>
                <el-form-item class="task-form" label="截止时间：">{{expandDetail.endTime | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="延长时长：">{{expandDetail.hours}}小时</el-form-item>
            </el-form>
            <el-form v-if="userRole === 0">
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
                <div v-show="userRole===0&&taskExpandTabsActiveName==='wait'">
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
                            format="yyyy-MM-dd HH:mm:00"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="请假结束日期" prop="endTime">
                    <el-date-picker
                            v-model="leaveForm.endTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:mm:00"
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
                <el-form-item style="margin-top: -20px" label="申请人">{{eWorkDetail.userName}}</el-form-item>
                <el-form-item style="margin-top: -20px" label="开始时间">{{eWorkDetail.beginTime | formatTime}}</el-form-item>
                <el-form-item style="margin-top: -20px" label="截止时间">{{eWorkDetail.endTime | formatTime}}</el-form-item>
                <el-form-item style="margin-top: -20px" label="加班时长">{{eWorkDetail.workHours}}小时</el-form-item>
                <el-form-item style="margin-top: -20px" label="关联任务">

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
                    <el-tooltip content="审核通过" placement="top" v-show="userRole===0">
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
                    <el-time-picker
                            v-model="recheckForm.recheckInTime"
                            type="time"
                            placeholder="选择时间">
                    </el-time-picker>
                </el-form-item>
                <!--<span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>-->
                <el-form-item label="补打下班时间" prop="endTime">
                    <el-time-picker
                            v-model="recheckForm.recheckOutTime"
                            type="time"
                            placeholder="选择时间">
                    </el-time-picker>
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
                <!--<span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>-->
                <el-form-item label="申请人:">{{recheckForm.userName}}</el-form-item>
                <el-form-item label="补打卡原因:" prop="reason">
                    <el-input type="textarea" v-model="recheckForm.reason" :rows="2"></el-input>
                </el-form-item>
                <span class="star" v-show="recheckForm.type === 0" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="补打上班时间:" prop="beginTime" v-show="recheckForm.type === 0">
                    <el-time-picker
                            v-model="recheckForm.recheckInTime"
                            type="time"
                            placeholder="选择时间">
                    </el-time-picker>
                </el-form-item>
                <span class="star" v-show="recheckForm.type === 1" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item label="补打下班时间" prop="beginTime" v-show="recheckForm.type === 1">
                    <el-time-picker
                            v-model="recheckForm.recheckOutTime"
                            type="time"
                            placeholder="选择时间">
                    </el-time-picker>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" v-show="userRole !== 0" @click="saveEditRecheck('recheckForm',recheckForm.id)"
                           :loading="isSaving">立即修改</el-button>
                <el-button type="danger" @click="deleteRecheck(recheckForm.id)"
                           :loading="isSaving">删除</el-button>
                <el-button v-show="userRole === 0" type="success" @click="accessRecheck('recheckForm',recheckForm.id)"
                           :loading="isSaving">审核</el-button>
            </span>
        </el-dialog>
        <el-dialog title="补打卡申请" size="tiny" custom-class="myDialog" :close-on-click-modal="false"
                   :close-on-press-escape="false" :visible.sync="showRecheckVisible"
                   @close="closeRecheckForm">
            <el-form>
                <el-form-item label="补打卡原因" prop="reason">
                    {{recheckForm.reason}}
                </el-form-item>
                <el-form-item label="补卡上班时间" prop="recheckTime" v-show="recheckForm.type === 0">
                    {{recheckForm.recheckTime | formatTime}}
                </el-form-item>
                <el-form-item label="补卡下班时间" prop="recheckTime" v-show="recheckForm.type === 1">
                    {{recheckForm.recheckTime | formatTime}}
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button v-show="userRole === 0" type="danger" @click="deleteRecheck(recheckForm.id)"
                           :loading="isSaving">删除</el-button>
            </span>
        </el-dialog>


        <el-dialog title="请假申请详情" :visible.sync="leaveDetailVisible" custom-class="myDialog"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="10%" size="tiny">
            <el-form>
                <el-form-item class="task-form" label="请假原因：">{{leaveForm.description}}</el-form-item>
                <el-form-item class="task-form" label="请假人：">{{leaveForm.userName}}</el-form-item>
                <el-form-item class="task-form" label="请假开始时间：">{{leaveForm.beginTime | formatTime}}</el-form-item>
                <el-form-item class="task-form" label="请假结束时间：">{{leaveForm.endTime | formatTime}}</el-form-item>
                <el-form-item class="task-form" label="类型：">{{leaveForm.typeName }}</el-form-item>
                <el-form-item class="task-form" label="请假时长：">{{leaveForm.hours}}小时</el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <div v-show="userRole===0">
                    <el-tooltip content="删除该申请" placement="top">
                          <el-button type="danger" icon="delete" @click="deleteLeave(leaveForm.id)"></el-button>
                    </el-tooltip>
                    <el-tooltip content="编辑该申请" placement="top">
                     <el-button type="primary" icon="edit" @click="editLeaveDetail(leaveForm,1)"></el-button>
                    </el-tooltip>
                    <el-button type="success" @click="acceptLeave(leaveForm.id)"
                               v-show="activeLeaveName==='wait'">审核通过</el-button>
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
                <el-button type="success" @click="calculateIntegral" v-show="activeLeaveName==='wait'">计算基准积分</el-button>
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
                <!--<div slot="tip" class="el-upload__tip">只能上传.dat文件，且不超过1MB</div>-->
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
            <el-select clearable no-match-text=" " v-model="workMonth1" placeholder="不选择及查询本年度"
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
            <el-select clearable no-match-text=" " v-model="workMonth2" placeholder="不选择及查询本年度"
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
        <el-dialog title="修改用户调休时长" :visible.sync="modifyUserRestHoursVisible" custom-class="myDialog"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="tiny"
                   @close="closeRestHoursDialog">
            <el-select v-model="restHoursUserId" clearable filterable   placeholder="筛选用户" @change="fetchUserRestHours">
                <el-option v-for="item in checkInUsers" :key="item.userId" :label="item.userName"
                           :value="item.userId"></el-option>
            </el-select>
            <div class="mic-item-title" style="font-size: 14px;margin-top: 10px">用户：<span>{{restHoursUserName}}</span></div>
            <div class="mic-item-title" style="font-size: 14px;margin-top: 10px">
                <span style="margin-left: 0px">截止上月底调休时长: </span>
                <el-input style="width:100px;margin-left: 22px" v-model="restHours" :maxlength="5" type="number"></el-input>
                <!--<span style="margin-left: 0px;cursor: pointer;text-decoration: underline"-->
                <!--@click="showUserRestHoursLog">查看详情</span>-->
            </div>
            <span slot="footer" class="dialog-footer">
                <!--<el-button type="primary" @click="fetchUserRestHours">查询</el-button>-->
                <el-button type="primary" @click="modifyUserRestHours" :loading="restHourLoading">修改</el-button>
            </span>
        </el-dialog>
        <el-dialog title="用户调休使用日志" :visible.sync="myRestHoursDetailVisible" class="rest-hour-log"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="small"
                   width="800px">
            <el-table :data="myRestHoursLogData" border>
                <el-table-column type="index" label="序号" align="center" width="80">
                    <template scope="scope">
                        {{(myRestHoursLogPage.pageNum-1)*10 + scope.$index + 1}}
                    </template>
                </el-table-column>
                <el-table-column prop="restHours" label="调整时长" align="center" width="100"></el-table-column>
                <el-table-column prop="content" label="事由" align="center"></el-table-column>
                <el-table-column prop="recordTime" label="记录日期"  width="150"  align="center">
                    <template scope="scope">
                        <div type="text" size="small" >{{scope.row.recordTime | formatTime}}</div>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        @current-change="myRestHoursLogHandleCurrentChange"
                        :current-page.sync="myRestHoursLogPage.pageNum"
                        :page-size="myRestHoursLogPage.pageSize"
                        :layout="myRestHoursLogsPageLayout"
                        :total="myRestHoursLogPage.total">
                </el-pagination>
            </div>
        </el-dialog>
        <el-dialog title="用户调休使用日志" :visible.sync="userRestHoursDetailVisible" class="rest-hour-log"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="small"
                   width="800px">
            <div>
                <el-button type="primary" @click="addRestHoursLog">手动新增调休记录</el-button>
            </div>
            <el-table :data="userRestHoursLogData" border>
                <el-table-column type="index" label="序号" align="center" width="80">
                    <template scope="scope">
                        {{(userRestHoursLogPage.pageNum-1)*10 + scope.$index + 1}}
                    </template>
                </el-table-column>
                <el-table-column prop="userName" label="用户" align="center" width="100"></el-table-column>
                <el-table-column prop="restHours" label="调整时长" align="center" width="100"></el-table-column>
                <el-table-column prop="content" label="事由" align="center"></el-table-column>
                <el-table-column prop="recordTime" label="记录日期"  width="150"  align="center">
                    <template scope="scope">
                        <div type="text" size="small" >{{scope.row.recordTime | formatTime}}</div>
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
        </el-dialog>
        <el-dialog title="新增多人任务" :visible.sync="createMultipleTaskVisible" custom-class="myDialog"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                    @close="clearMultipleTask">
            <el-form :model="taskTempForm"  ref="taskTempForm" label-width="80px">
                <el-form-item label="任务阶段" prop="stageId">
                    <el-select v-model="taskTempForm.stageId" placeholder="请选择阶段" clearable filterable
                               style="margin-left: 7px" @change="getTaskByStage(taskTempForm.stageId)">
                        <el-option
                                v-for="item in taskTempStageList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item class="task-form" label="关联任务" prop="taskId">
                    <el-select v-model="taskTempForm.taskId" placeholder="请选择" clearable filterable @change="getTaskDetail(taskTempForm.taskId)">
                        <el-option
                                v-for="item in taskList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                    <el-popover
                            v-show="showTaskDescriptionVisible"
                            placement="right"
                            title="任务描述"
                            width="400"
                            trigger="hover"
                            :content="taskDetail.description">
                        <el-button slot="reference">任务描述</el-button>
                    </el-popover>
                </el-form-item>
                <div v-show="showTaskDetailVisible" style="float: left;margin-top: 3px;margin-left: 20px">项目: {{taskDetail.projectName}}</div>
                <div v-show="showTaskDetailVisible" style="margin-top: 3px;margin-left: 250px">设计完成时间: {{taskDetail.beginTime | formatDate}}</div>
                <div v-show="showTaskDetailVisible" style="float: left;margin-top: 3px;margin-left: 20px">阶段: {{taskDetail.stageName}}</div>
                <div v-show="showTaskDetailVisible" style="margin-top: 3px;margin-left: 250px">开发完成时间: {{taskDetail.testTime | formatDate}}</div>
                <div v-show="showTaskDetailVisible" style="float: left;margin-top: 3px;margin-left: 20px">优先级: <span v-for="item in priorityList" v-if="item.value === taskDetail.priority">{{item.label}}</span></div>
                <div v-show="showTaskDetailVisible" style="margin-top: 3px;margin-left: 250px">任务截止时间: {{taskDetail.endTime | formatDate}}</div>
                <div v-show="showTaskDetailVisible" style="margin-left: 20px">标签:
                <!--<el-form-item class="task-form" v-show="showTaskDescriptionVisible" label="任务描述: ">-->
                    <!--<el-input type="textarea" disabled :rows="6" v-model="taskDetail.description"></el-input>-->
                <!--</el-form-item>-->
                    <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
                        {{item.name}}
                    </el-tag>
                </div>

                <!--<div>-->
                    <!--<span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>-->
                    <!--<el-button type="text" style="margin-left: 12px" @click="addFunctionVisible=true">点击新增任务功能点</el-button>-->
                <!--</div>-->

                <div v-show="this.taskFunctionData.length>0">
                    <span class="star" style="margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                    <span style="margin-left: 7px;color: blue;cursor: pointer" @click="showLevelReference">功能点(点击查看复杂度参考表)</span>
                    <div style="border: 1px solid #bfcbd9;border-radius: 4px; padding: 10px;">
                        <i style="margin-left: 0px" class="el-icon-plus" v-show="num>=1 && num<taskFunctionData.length" @click="plus"></i>
                        <i class="el-icon-minus" v-show="num>1"@click="minus(num-1)"></i>
                        <!--<el-button style="margin-left: 0px" v-show="num>=1" @click="plus(num-1)" type="text">加1</el-button>-->
                        <!--<el-button  type="text" v-show="num>1"@click="minus(num-1)">减1</el-button>-->
                        <div v-for="i in num" style="margin-top: 3px">
                            <el-select placeholder="功能点" v-model="taskFunctionList[i-1]" clearable
                            style="width: 400px">
                                <el-option
                                        v-for="item in taskFunctionData"
                                        :key="item.id"
                                        :label="item.functionStr"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                            <el-select placeholder="复杂度" v-model="functionLevelList[i-1]" clearable
                                       style="width: 120px">
                                <el-option
                                        v-for="item in taskLevelList"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </div>

                    </div>

                </div>
                <div style="margin-top: 5px">
                    <span class="star" style="margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>
                    <span style="margin-left: 7px;margin-top: 5px">任务描述:</span>
                    <el-input type="textarea" v-model="description" :rows="3" size="mini"></el-input>
                </div>
                <!--<span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span>-->
                <!--<el-form-item class="task-form" label="任务描述" prop="description">-->
                    <!--<el-input type="textarea" v-model="description" :rows="3" size="mini"></el-input>-->
                <!--</el-form-item>-->
                <span class="star" style="float: left;margin-top: 17px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item class="task-form" label="开始日期" prop="beginTime" style="float: left;margin-left: -8px;margin-top: 10px" label-width="90px">
                    <el-date-picker
                            v-model="taskTempForm.beginTime"
                            type="date"
                            format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 17px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item class="task-form" label="截止日期" prop="endTime" style="margin-left: 285px;margin-top: 10px" label-width="75px">
                    <el-date-picker
                            v-model="taskTempForm.endTime"
                            type="date"
                            format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 17px;margin-right: -8px;margin-left: 8px;">*</span>
                <el-form-item class="task-form" label="" prop="workHours" style="margin-top: 10px">
                    <span style="margin-left: -58px">工作量</span>
                    <el-input style="width:100px;margin-left: 22px" v-model="taskTempForm.workHours" :maxlength="6"></el-input>
                    小时
                </el-form-item>
                <div v-for="(item,index) in sortWeekNumber">
                    <div class="add-member-basic-list clearfix">
                        <div class="fl" style="margin-left: 5px"><span class="star">*</span>第{{item.weekNumber}}周工作量({{item.range}})：</div>
                        <input class="member-time-week" v-model="item.hours" :maxlength="6" style="width:80px" :placeholder="item.hoursTemp">&nbsp;&nbsp;&nbsp;&nbsp;已有工作量:
                        <div class="f1" v-show="parseFloat(item.weekHours===''?0:item.weekHours) + parseFloat(item.hours===''?0:item.hours) > 40" style="color:red;display:inline">
                            {{parseFloat(item.weekHours===''?0:item.weekHours) + parseFloat(item.hours===''?0:item.hours)}}</div>
                        <div class="f1" v-show="parseFloat(item.weekHours===''?0:item.weekHours) + parseFloat(item.hours===''?0:item.hours) <=40" style="display:inline">
                            {{parseFloat(item.weekHours===''?0:item.weekHours) + parseFloat(item.hours===''?0:item.hours)}}</div>
                    </div>
                </div>


            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="saveMultipleTask('taskTempForm')" :disabled="taskAble">立即创建</el-button>
                <el-button @click="createMultipleTaskVisible = false">取 消</el-button>
            </span>
        </el-dialog>

        <el-dialog title="添加任务功能点" :visible.sync="addFunctionVisible"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="10%" width="50%"
                    @close="closeFunctionDialog">

        </el-dialog>

        <el-dialog title="多人任务申请详情" custom-class="myDialog" :visible.sync="taskTempDetailVisible"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="10%" size="tiny"
                    @close="closeTaskTemp">
            <el-form  :model="taskTempDetail"  ref="editTaskTempForm">
                <div v-for="(item,index) in taskTempDetail.taskReviewLogResDTOList">
                    <div>审核阶段: {{item.level}};  审核人: {{item.checkUserName}};  审核时间: {{item.reviewTime | formatTime}}</div>
                    <div>审核意见: {{item.suggest}}</div>
                </div>
                <div class="ctpc-instruction-msg" v-show="!addSuggestVisible && taskTempDetail.isChecked === 0" @click="showInsChange" style="color:deepskyblue;">
                    {{insMsgShow}}
                </div>
                <div class="ctpc-ins-edit" v-show="addSuggestVisible">
                    <textarea class="ctpc-instruction" placeholder="审核建议" v-model="taskTempDetail.suggest"></textarea>
                    <div class="ctpc-btns">
                        <input type="button" class="ctpc-cancel" @click="cancelEdit" value="取消">
                        <input type="button" class="ctpc-save" @click="saveEdit" value="确定">
                    </div>
                </div>
                <el-form-item v-show="taskTempDetail.taskReviewLogResDTOList.length > 0"><span>-------------------------------------------------------------------------------------------</span></el-form-item>
                <div style="margin-top: 0px">申请人: {{taskTempDetail.userName}}</div>
                <div style="margin-top: 3px;">任务名称: {{taskTempDetail.taskName}}</div>
                <div style="margin-top: 3px;">关联文档:
                    <a v-if="taskDetail.doc !== undefined && taskDetail.doc !== null && taskDetail.doc !== ''" style="cursor: pointer;"
                                                       @click="toFile(taskDetail.doc)">{{taskDetail.doc.substring(0,50)}}...
                    </a>
                </div>
                <div style="float: left;margin-top: 3px">项目: {{taskDetail.projectName}}</div>
                <div style="margin-top: 3px;margin-left: 250px">设计完成时间: {{taskDetail.beginTime | formatDate}}</div>
                <div style="float: left;margin-top: 3px">阶段: {{taskDetail.stageName}}</div>
                <div style="margin-top: 3px;margin-left: 250px">开发完成时间: {{taskDetail.testTime | formatDate}}</div>
                <div style="float: left;margin-top: 3px">优先级: <span v-for="item in priorityList" v-if="item.value === taskDetail.priority">{{item.label}}</span></div>
                <div style="margin-top: 3px;margin-left: 250px">任务截止时间: {{taskDetail.endTime | formatDate}}</div>
                <div>标签:
                    <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
                        {{item.name}}
                    </el-tag>
                </div>
                <el-form-item><span>-------------------------------------------------------------------------------------------</span></el-form-item>
                <div v-show="!taskTempAble && taskTempDetail.functionResDTOList.length>0">
                    <span style="margin-left: 0px">功能点:</span>
                    <div style="border: 1px solid #bfcbd9;border-radius: 4px; padding: 10px;">
                        <!--<i style="margin-left: 0px" class="el-icon-plus" v-show="num>=taskTempDetail.functionResDTOList.length" @click="plus"></i>-->
                        <!--<i class="el-icon-minus" v-show="num>taskTempDetail.functionResDTOList.length" @click="minus(num-1)"></i>-->

                        <div v-for="i in num" style="margin-top: 3px">
                            <div  style="float: left">
                                <el-select placeholder="功能点" v-model="taskFunctionList[i-1]"  clearable
                                       disabled
                                       style="width: 400px">
                                <el-option
                                        v-for="item in taskFunctionData"
                                        :key="item.id"
                                        :label="item.functionStr"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                            </div>
                            <div>
                                <el-select placeholder="复杂度" v-model="functionLevelList[i-1]" clearable
                                           style="width: 120px">
                                    <el-option
                                            v-for="item in taskLevelList"
                                            :key="item.id"
                                            :label="item.name"
                                            :value="item.id">
                                    </el-option>
                                </el-select>
                            </div>
                            <!--<i type="text"  @mouseenter="mouseEnter(taskFunctionList[i-1])">参考</i>-->
                        </div>

                    </div>

                </div>
                <div v-show="taskTempAble && taskTempDetail.functionResDTOList !== undefined && taskTempDetail.functionResDTOList.length>0">
                    <span style="margin-left: 0px">功能点:</span>

                    <el-table class="hh" :data="taskTempDetail.functionResDTOList">
                        <el-table-column prop="moduleName" label="模块" align="center"></el-table-column>
                        <el-table-column prop="function" label="功能点" align="center" width="120"></el-table-column>
                        <el-table-column prop="actionName" label="动作" align="center" width="120"></el-table-column>
                        <el-table-column prop="levelName" label="复杂度" align="center" width="120"></el-table-column>
                    </el-table>
                </div>
                <el-form-item v-show="taskTempAble" label="任务描述: " prop="description" style="margin-top: 0px">
                    {{taskTempDetail.description}}
                </el-form-item>
                <el-form-item v-show="!taskTempAble" label="任务描述: " prop="description" style="margin-top: 0px">
                    <el-input type="textarea" :disabled="taskTempAble" v-model="description" :rows="3"></el-input>
                </el-form-item>

                <div v-show="taskTempAble" style="float: left;">开始时间: {{taskTempDetail.beginTime | formatDate}}</div>
                <div v-show="taskTempAble" style="float: left;margin-left: 20px;">截止: {{taskTempDetail.endTime | formatDate}}</div>
                <div v-show="taskTempAble" style="margin-left: 350px">任务时长: {{taskTempDetail.workHours}}小时</div>
                <div v-show="taskTempAble" style="margin-left: 0px">任务级别: {{taskTempDetail.taskLevelName}}</div>
                <el-form-item v-show="!taskTempAble" class="task-form" label="开始时间：" style="float: left;margin-left: -8px" label-width="90px">
                    <el-date-picker @change="changeTime"
                                    v-model="taskTempDetail.beginTime"
                                    type="date"
                                    format="yyyy-MM-dd"
                                    placeholder="选择日期时间"
                                    :disabled="taskTempAble">
                    </el-date-picker>
                </el-form-item>

                <el-form-item v-show="!taskTempAble" class="task-form" label="截止时间：" style="margin-left: 285px">
                    <el-date-picker @change="changeTime"
                                    v-model="taskTempDetail.endTime"
                                    type="date"
                                    format="yyyy-MM-dd"
                                    placeholder="选择日期时间"
                                    :disabled="taskTempAble">
                    </el-date-picker>
                </el-form-item>

                <el-form-item v-show="!taskTempAble" class="task-form" label="任务时长：" style="float: left">
                    <el-input v-model="taskTempDetail.workHours" :disabled="taskTempAble" style="width: 40%"></el-input>
                    小时
                </el-form-item>

                <el-form-item v-show="!taskTempAble" class="task-form" label="任务级别: " style="margin-left: 285px">
                    <el-select v-model="taskTempDetail.taskLevel" clearable filterable placeholder="请选择任务级别"  style="width: 150px;margin-left: 11px">
                        <el-option v-for="item in taskLevelList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <div v-for="(item,index) in sortWeekTempNumber" v-show="!taskTempAble">
                        <div class="add-member-basic-list clearfix">
                            <div class="fl" style="margin-left: 5px">第{{item.weekNumber}}周工作量({{item.range}})：</div>
                            <input class="member-time-week" :disabled="taskTempAble" v-model="item.hours" :maxlength="6" style="width:80px" :placeholder="item.hoursTemp">&nbsp;&nbsp;&nbsp;&nbsp;已有工作量:
                            <div class="f1" v-show="parseFloat(item.weekHours===''?0:item.weekHours) + parseFloat(item.hours===''?0:item.hours) > 40" style="color:red;display:inline">
                                {{parseFloat(item.weekHours===''?0:item.weekHours) + parseFloat(item.hours===''?0:item.hours)}}</div>
                            <div class="f1" v-show="parseFloat(item.weekHours===''?0:item.weekHours) + parseFloat(item.hours===''?0:item.hours) <=40" style="display:inline">
                                {{parseFloat(item.weekHours===''?0:item.weekHours) + parseFloat(item.hours===''?0:item.hours)}}</div>
                        </div>
                    </div>
                <div v-for="(item,index) in sortWeekTempNumber" v-show="taskTempAble" style="margin-top: 10px">
                    <div class="add-member-basic-list clearfix">
                        <div class="fl" style="margin-left: 5px">第{{item.weekNumber}}周工作量({{item.range}})：</div>
                        <input class="member-time-week" :disabled="taskTempAble" v-model="item.hours" :maxlength="6" style="width:80px" :placeholder="item.hoursTemp">&nbsp;&nbsp;&nbsp;&nbsp;已有工作量:
                        <div class="f1" v-show="parseFloat(item.weekHours===''?0:item.weekHours) > 40" style="color:red;display:inline">
                            {{parseFloat(item.weekHours===''?0:item.weekHours)}}</div>
                        <div class="f1" v-show="parseFloat(item.weekHours===''?0:item.weekHours) <=40" style="display:inline">
                            {{parseFloat(item.weekHours===''?0:item.weekHours)}}</div>
                    </div>
                </div>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <div v-if="userRole === 0">
                    <el-button type="danger" v-show="taskTempDetail.isChecked === 0" @click="deleteMultipleTask(taskTempDetail.id)">删除申请</el-button>
                    <el-button type="success" v-show="taskTempDetail.isChecked === 0" @click="acceptMultipleTask(taskTempDetail.id,'editTaskTempForm')" :loading="accessTaskTempLoading">申请通过</el-button>
                </div>
                <div v-else>
                     <el-button type="danger" v-show="taskTempDetail.isChecked === 0" @click="deleteMultipleTask(taskTempDetail.id)">删除申请</el-button>
                     <el-button type="success" v-show="taskTempDetail.isChecked === 0" @click="acceptMultipleTask(taskTempDetail.id,'editTaskTempForm')" :loading="accessTaskTempLoading">申请通过</el-button>
                </div>
            </span>
        </el-dialog>

        <el-dialog class="el-dialog--small" style="margin-left: 570px" title="创建任务" :visible.sync="createTaskVisible"
                   :close-on-click-modal="false" :close-on-press-escape="false"
                   top="10%" center="true">
            <span>请选择任务类型</span>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="createPrivateTask">个人任务</el-button>
                <el-button type="primary" @click="createMultipleTask">多人任务</el-button>
            </span>

        </el-dialog>

        <el-dialog title="任务修改申请详情" custom-class="myDialog" :visible.sync="showTaskModifyDetailVisible"
                   :close-on-click-modal="false" :close-on-press-escape="false"
        @close="closeModifyDialog">
            <div style="margin-top: -10px">申请人: {{taskUser.userName}}</div>
            <div style="margin-top: 3px;">任务名称: {{taskDetail.name}}</div>
            <div style="margin-top: 3px;">关联文档:
                <a v-if="taskDetail.doc !== undefined && taskDetail.doc !== undefined && taskDetail.doc !== null && taskDetail.doc !== ''" style="cursor: pointer;"
                   @click="toFile(taskDetail.doc)">{{taskDetail.doc.substring(0,50)}}...
                </a>
            </div>
            <div style="float: left;margin-top: 3px">项目: {{taskDetail.projectName}}</div>
            <div style="margin-top: 3px;margin-left: 250px">设计完成时间: {{taskDetail.beginTime | formatDate}}</div>
            <div style="float: left;margin-top: 3px">阶段: {{taskDetail.stageName}}</div>
            <div style="margin-top: 3px;margin-left: 250px">开发完成时间: {{taskDetail.testTime | formatDate}}</div>
            <div style="float: left;margin-top: 3px">优先级: <span v-for="item in priorityList" v-if="item.value === taskDetail.priority">{{item.label}}</span></div>
            <div style="margin-top: 3px;margin-left: 250px">任务截止时间: {{taskDetail.endTime | formatDate}}</div>
            <div>标签:
                <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
                    {{item.name}}
                </el-tag>
            </div>
            <div>-------------------------------------------------------------------------------------------</div>
            <div style="margin-top: 0px">任务描述: {{taskUser.description}}</div>
            <div v-show="taskModifyDetail.oldFunctionResDTOList !== undefined && taskModifyDetail.oldFunctionResDTOList.length>0">
                <span style="margin-left: 0px">功能点:</span>
                <el-table class="hh" :data="taskModifyDetail.oldFunctionResDTOList">
                    <el-table-column prop="moduleName" label="模块" align="center"></el-table-column>
                    <el-table-column prop="function" label="功能点" align="center" width="120"></el-table-column>
                    <el-table-column prop="actionName" label="动作" align="center" width="120"></el-table-column>
                    <el-table-column prop="levelName" label="复杂度" align="center" width="120"></el-table-column>
                </el-table>
            </div>
            <div style="float: left;margin-top: 3px">开始时间: {{taskUser.beginTime | formatDate}}</div>
            <div style="margin-top: 3px;margin-left: 250px;">截止时间: {{taskUser.endTime | formatDate}}</div>
            <div style="float: left;margin-top: 3px;">任务时长: {{taskUser.taskHours}}小时</div>
            <div style="margin-left: 250px;margin-top: 3px">任务级别: {{taskUser.taskLevelName}}</div>
            <div v-for="(item,index) in sortUserWeekNumber">
                <div class="add-member-basic-list clearfix">
                    <div class="fl" style="margin-left: 5px">第{{item.weekNumber}}周工作量({{item.range}})：</div>
                    <input class="member-time-week" disabled v-model="item.hours" :maxlength="6" style="width:80px" :placeholder="item.hoursTemp">&nbsp;&nbsp;&nbsp;&nbsp;已有工作量:
                    <div class="f1" v-show="parseFloat(item.weekHours===''?0:item.weekHours) > 40" style="color:red;display:inline">
                        {{parseFloat(item.weekHours===''?0:item.weekHours)}}</div>
                    <div class="f1" v-show="parseFloat(item.weekHours===''?0:item.weekHours) <=40" style="display:inline">
                        {{parseFloat(item.weekHours===''?0:item.weekHours)}}</div>
                </div>
            </div>
            <div>-------------------------------------------------------------------------------------------</div>
            <el-form :model="taskModifyDetail"  ref="editTaskModifyForm">
                <el-form-item v-show="taskModifyDetail.reviewStatus === 2" label="修改原因: " prop="description">
                    {{taskModifyDetail.reason}}
                </el-form-item>
                <el-form-item v-show="taskModifyDetail.reviewStatus === 1" label="修改原因: " prop="description" style="margin-top: 0px">
                    <el-input type="textarea" v-model="modifyMyTaskReason" :rows="2"></el-input>
                </el-form-item>

                <el-form-item v-show="taskModifyDetail.reviewStatus === 2" label="任务描述: " prop="description">
                    {{taskModifyDetail.description}}
                </el-form-item>
                <el-form-item v-show="taskModifyDetail.reviewStatus === 1" label="任务描述: " prop="description" style="margin-top: 0px">
                    <el-input type="textarea" v-model="modifyMyTaskDescription" :rows="3"></el-input>
                </el-form-item>
                <div v-show="taskModifyDetail.reviewStatus === 2 && taskModifyDetail.functionResDTOList !== undefined
                && taskModifyDetail.functionResDTOList.length>0">
                    <span style="margin-left: 0px">功能点:</span>
                    <el-table class="hh" :data="taskModifyDetail.functionResDTOList">
                        <el-table-column prop="moduleName" label="模块" align="center"></el-table-column>
                        <el-table-column prop="function" label="功能点" align="center" width="120"></el-table-column>
                        <el-table-column prop="actionName" label="动作" align="center" width="120"></el-table-column>
                        <el-table-column prop="levelName" label="复杂度" align="center" width="120"></el-table-column>
                    </el-table>
                </div>

                <div v-show="taskModifyDetail.reviewStatus === 1 && taskModifyDetail.functionResDTOList !== undefined
                && taskModifyDetail.functionResDTOList.length>0">
                    <span style="margin-left: 1px">功能点:</span>
                    <div style="border: 1px solid #bfcbd9;border-radius: 4px; padding: 10px;">
                        <i style="margin-left: 0px" class="el-icon-plus" v-show="num>=1&&num<taskFunctionData.length&&userRole>0" @click="plus(num-1)"></i>
                        <i class="el-icon-minus" v-show="num>1&&userRole>0"@click="minus(num-1)"></i>
                        <div v-for="i in num" style="margin-top: 3px">
                            <el-select placeholder="功能点" v-model="taskFunctionList[i-1]" clearable :disabled="userRole===0"
                                       style="width: 400px">
                                <el-option
                                        v-for="item in taskFunctionData"
                                        :key="item.id"
                                        :label="item.functionStr"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                            <el-select placeholder="复杂度" v-model="functionLevelList[i-1]" clearable
                                       style="width: 120px">
                                <el-option
                                        v-for="item in taskLevelList"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </div>
                    </div>
                </div>

                <el-form-item v-show="taskModifyDetail.reviewStatus === 2" class="task-form" label="开始时间：" style="float: left;margin-left: -10px" label-width="90px">
                    {{taskModifyDetail.beginTime | formatDate}}
                </el-form-item>
                <el-form-item v-show="taskModifyDetail.reviewStatus === 1" class="task-form" label="开始时间：" style="float: left;margin-left: -8px" label-width="90px">
                    <el-date-picker @change="changeTaskModifyTime()"
                                    v-model="taskModifyDetail.beginTime"
                                    type="date"
                                    format="yyyy-MM-dd"
                                    placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>

                <el-form-item v-show="taskModifyDetail.reviewStatus === 2" class="task-form" label="截止时间：" label-width="82px" style="margin-left: 250px">
                    {{taskModifyDetail.endTime | formatDate}}
                </el-form-item>
                <el-form-item v-show="taskModifyDetail.reviewStatus === 1" class="task-form" label="截止时间：" style="margin-left: 285px">
                    <el-date-picker @change="changeTaskModifyTime()"
                                    v-model="taskModifyDetail.endTime"
                                    type="date"
                                    format="yyyy-MM-dd"
                                    placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>

                <el-form-item v-show="taskModifyDetail.reviewStatus === 2" class="task-form" label="任务时长：" style="float: left;margin-left: -120px" label-width="200px">
                    {{taskModifyDetail.workHours}} 小时
                </el-form-item>
                <el-form-item v-show="taskModifyDetail.reviewStatus === 1" class="task-form" label="任务时长：" style="float: left">
                    <el-input v-model="taskModifyDetail.workHours" style="width: 20%"></el-input>
                    小时
                </el-form-item>
                <el-form-item  v-show="taskModifyDetail.reviewStatus === 1" class="task-form" label="任务级别:  " style="margin-left: 285px">
                    <el-select v-model="taskModifyDetail.taskLevel" clearable filterable placeholder="请选择任务级别"  style="width: 150px;margin-left: 11px">
                        <el-option v-for="item in taskLevelList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item  v-show="taskModifyDetail.reviewStatus === 2" class="task-form" label="任务级别:  " style="margin-left: 250px">
                    {{taskModifyDetail.taskLevelName}}
                </el-form-item>

                <div v-for="(item,index) in sortTaskModifyWeekNumber" v-show="taskModifyDetail.reviewStatus === 1">
                    <div class="add-member-basic-list clearfix">
                        <div class="fl" style="margin-left: 5px">第{{item.weekNumber}}周工作量({{item.range}})：</div>
                        <input class="member-time-week" :disabled="taskTempAble" v-model="item.hours" :maxlength="6" style="width:80px" :placeholder="item.hoursTemp">&nbsp;&nbsp;&nbsp;&nbsp;已有工作量:
                        <div class="f1" v-show="parseFloat(item.weekHours===''?0:item.weekHours) + parseFloat(item.hours===''?0:item.hours) > 40" style="color:red;display:inline">
                            {{parseFloat(item.weekHours===''?0:item.weekHours) + parseFloat(item.hours===''?0:item.hours)}}</div>
                        <div class="f1" v-show="parseFloat(item.weekHours===''?0:item.weekHours) + parseFloat(item.hours===''?0:item.hours) <=40" style="display:inline">
                            {{parseFloat(item.weekHours===''?0:item.weekHours) + parseFloat(item.hours===''?0:item.hours)}}</div>
                    </div>
                </div>
                <div v-for="(item,index) in sortTaskModifyWeekNumber" v-show="taskModifyDetail.reviewStatus === 2">
                    <div class="add-member-basic-list clearfix">
                        <div class="fl" style="margin-left: 5px">第{{item.weekNumber}}周工作量({{item.range}})：</div>
                        <input class="member-time-week" disabled v-model="item.hours" :maxlength="6" style="width:80px" :placeholder="item.hoursTemp">&nbsp;&nbsp;&nbsp;&nbsp;已有工作量:
                        <div class="f1" v-show="parseFloat(item.weekHours===''?0:item.weekHours) > 40" style="color:red;display:inline">
                            {{parseFloat(item.weekHours===''?0:item.weekHours)}}</div>
                        <div class="f1" v-show="parseFloat(item.weekHours===''?0:item.weekHours) <=40" style="display:inline">
                            {{parseFloat(item.weekHours===''?0:item.weekHours)}}</div>
                    </div>
                </div>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <div v-if="userRole === 0">
                    <el-button type="danger" @click="deleteTaskModify(taskModifyDetail.id)">删除申请</el-button>
                    <el-button type="success" v-show="taskModifyDetail.reviewStatus === 1" @click="accessTaskModify(taskModifyDetail.id,'editTaskModifyForm')"
                    :loading="isSaving">申请通过</el-button>
                </div>
                <div v-else>
                     <el-button type="danger" v-show="taskModifyDetail.reviewStatus === 1" @click="deleteTaskModify(taskModifyDetail.id)">删除申请</el-button>
                     <el-button type="primary" v-show="taskModifyDetail.reviewStatus === 1" @click="editTaskModify(taskModifyDetail.id,'editTaskModifyForm')"
                                :loading="isSaving">修改申请</el-button>
                </div>
            </span>
        </el-dialog>

        <el-dialog title="" style="margin-left: 30%;margin-right: 30%" :visible.sync="showUserAndLevelVisible"
                   top="10%" center="true">
            <el-table class="hh" :data="userAndLevelData">
                <el-table-column prop="userName"  align="center"></el-table-column>
                <el-table-column prop="jobName"  align="center"></el-table-column>
                <el-table-column prop="levelName"  align="center"></el-table-column>
            </el-table>
        </el-dialog>

        <el-dialog  title="新增个人/加班调休记录"  size="tiny"  :close-on-click-modal="false"
                    :close-on-press-escape="false" :visible.sync="editRestHoursVisible">
            <el-form :model="userRestHoursLogForm"  ref="userRestHoursLogForm" label-width="80px">
                <el-form-item label="用户 ">
                    <span>{{this.userRestHoursLogReqDTO.userName}}</span>
                </el-form-item>
                <el-form-item label="调休加减" prop="restHour">
                    <el-input v-model="userRestHoursLogForm.restHour" type="number" :maxlength="5"></el-input>
                </el-form-item>
                <el-form-item label="调休备注" prop="restHour">
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
            let validateEmpty = (rule, value, callback) => {
                if (value.trim() === '') {
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
                taskModifyTabsActiveName: 'wait',
                taskModifyTabsActiveName2: 'wait',
                taskAble: false,
                editHelpDetailVisible: false,
                editHelpVisible: false,
                editLeaveVisible: false,
                leaveDetailVisible: false,
                helpDetailVisible: false,
                expandDetailVisible: false,
                createTaskVisible: false,
                createPrivateTaskVisible: false,
                createMultipleTaskVisible: false,
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
                waitAuditPage: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                expandPage: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                expandDoingPage: {
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
                    userId: '',
                    userName: '',
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
                taskIntegralItem: [
                    {
                        label: '本月',
                        score: ''
                    },
                    {
                        label: '本季度',
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
                activeQuestionName: 'wait',
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
                    userId:'',
                    userName:'',
                    type:'',
                    date:''
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

                //修改调休时间
                modifyUserRestHoursVisible: false,
                restHoursUserId:'',
                restHoursUserName:'',
                restHours:0,
                restHourLoading: false,
                myRestHours:[],
                myRestHoursDetailVisible: false,
                myRestHoursLogData:[],
                myRestHoursLogReqDTO:{
                    userId:'',
                    pageNum:1
                },
                myRestHoursLogPage:{
                    pageNum: 1,
                    pageSize: 10,
                    total: 0
                },
                userRestHoursLogData:[],
                userRestHoursDetailVisible: false,
                userRestHoursLogReqDTO:{
                    userId:'',
                    userName:'',
                    pageNum:1
                },
                userRestHoursLogPage:{
                    pageNum: 1,
                    pageSize: 10,
                    total: 0
                },
                restHourReqDTO:{
                    jobRole:'',
                    userId:''
                },
                restHourYear:'',
                restHoursData:[],
                userRestHoursLogForm:{
                    restHour:0,
                    content:'',
                    recordTime:'',
                    userId:null
                },
                editRestHoursVisible: false,
                rolesList:[
                    {
                        roleId: 1,
                        roleName: '开发'
                    },
                    {
                        roleId: 0,
                        roleName: '测试'
                    },
                    {
                        roleId: 2,
                        roleName: '设计'
                    },
                    {
                        roleId: 3,
                        roleName: '产品'
                    },
                    {
                        roleId: 5,
                        roleName: '算法工程师'
                    },
                    {
                        roleId: 4,
                        roleName: '其他'
                    }
                ],
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

                //个人新建多人任务
                description:null,
                taskTempForm:{
                    id:null,
                    userId:null,
                    taskId:null,
                    stageId:null,
                    workHours:null,
                    beginTime:null,
                    endTime:null,
                    description:null,
                    createTime:null,
                    userWeeks:[],
                    taskTempFunctionList:[]
                },
                taskTempActiveName: 'wait',
                taskTempActiveName2: 'wait',
                taskTempStageList:[
                    {id:'212754785051344891',name:'待设计'},
                    {id:'212754785051344892',name:'设计中'},
                    {id:'212754785051344890',name:'待开发'},
                    {id:'212754785051344894',name:'开发中'},
                    {id:'212754785051344895',name:'待测试'},
                    {id:'212754785051344896',name:'测试中'}
                ],
                taskTempPage:{
                  pageNum:1,
                  total:0,
                  pageSize:5
                },
                taskTempPage2:{
                    pageNum:1,
                    total:0,
                    pageSize:5
                },
                taskTempPage3:{
                    pageNum:1,
                    total:0,
                    pageSize:5
                },
                taskTempPage4:{
                    pageNum:1,
                    total:0,
                    pageSize:5
                },
                taskList:[],
                weekNumber:[],
                weekNumberTemp:[],
                step: {
                    index: '',
                    stageId: '',
                    stageName: '',
                    userId: '',
                    userName: '',
                    beginTime: '',
                    endTime: '',
                    taskHours: '',
                    completeHours: '',
                    completeTime: '',
                    description: '',
                    status: ''
                },
                otherStep:{
                    description: '',
                    status:1,
                    taskHours:''
                },
                taskTempDetailVisible:false,
                taskTempAble:false,
                taskTempWeekNumber:[],
                taskWeekNumberTemp:[],
                accessTaskTempLoading:false,
                taskTempDetail:{
                    id:null,
                    taskId:null,
                    taskName:null,
                    userId:null,
                    userName:null,
                    description:null,
                    beginTime:null,
                    endTime:null,
                    createTime:null,
                    firstTime:null,
                    secondTime:null,
                    reviewStatus:null,
                    workHours:null,
                    beginWeek:null,
                    endWeek:null,
                    userWeeks:[],
                    taskReviewLogResDTOList:[],
                    functionResDTOList:[],
                    isChecked:'',
                    suggest:'',
                    taskLevel:'',
                    taskLevelName:'',
                },
                addFunctionVisible:false,
                num:1,
                taskTempModuleData:[],
                taskFunctionData:[],
                actionList:[
                    {id:0,name:'新增'},
                    {id:1,name:'修改'},
                    {id:2,name:'删除'}
                ],
                taskTempModuleList:[],
                taskFunctionList:[],
                functionList:[],
                functionLevelList:[],
                functionActionList:[],

                taskLevelList:[
                    {id:1,name:"一级"},
                    {id:2,name:"二级"},
                    {id:3,name:"三级"},
                    {id:4,name:"四级"},
                    {id:5,name:"五级"}
                ],
                taskTemp:{
                    waitAssess: [],
                    waitAssess2: [],
                    auditSuccess: [],
                    auditSuccess2: [],
                },
                taskDetail:{

                },
                showTaskDetailVisible:false,
                showTaskDescriptionVisible:false,
                priorityList: [
                    {label: '普通', value: 1},
                    {label: '紧急', value: 2},
                    {label: '非常紧急', value: 3},
                ],
                showTaskReviewTabVisible:false,
                controlledPeopleList:[],
                addSuggestVisible:false,
                personalTaskModifyData:{
                    waitAssess:[],
                    auditSuccess:[]
                },
                personalTaskModifyPage:{
                  pageNum:1,
                  pageSize:5,
                  total:0
                },
                taskModifyData:{
                    waitAssess:[],
                    auditSuccess:[]
                },
                taskModifyPage:{
                    pageNum:1,
                    pageSize:5,
                    total:0
                },
                taskUser:{
                    id:'',
                    taskId:'',
                    userId:'',
                    userName:'',
                    beginTime:'',
                    endTime:'',
                    taskHours:'',
                    description:'',
                    beginWeek:'',
                    endWeek:'',
                },
                taskUserWeekNumber:[],
                taskUserTempWeekNumber:[],
                modifyMyTaskReason:'',
                modifyMyTaskDescription:'',
                taskModifyDetail:{
                    id:'',
                    taskId:'',
                    taskName:'',
                    userId:'',
                    userName:'',
                    reason:'',
                    description:'',
                    workHours:'',
                    reviewStatus:'',
                    createTime:'',
                    firstTime:'',
                    secondTime:'',
                    beginTime:'',
                    endTime:'',
                    taskLevel:'',
                    userWeekResDTOList:[],
                    functionResDTOList:[],
                    oldFunctionResDTOList:[],
                    beginWeek:'',
                    endWeek:'',
                    userWeeks:[]
                },
                showTaskModifyDetailVisible:false,
                taskModifyWeekNumber:[],
                taskModifyWeekNumberTemp:[],
                taskModifyWeekTime:{
                    beginWeek:'',
                    endWeek:''
                },
                userLeaveReqDTO:{
                    pageNum:1,
                    userId:'',
                    beginTime:'',
                    endTime:''
                },
                leaveManage:[],
                leaveFormPage:{
                    pageSize: 10,
                    total: 0,
                },
                leaveReqDTO:{
                    userId:'',
                    beginTime:'',
                    endTime:'',
                    pageNum:1,
                },

                //个人综合评价
                personalEvaluation:{
                    weekTime:'',
                    monthTime:'',
                    yearTime:'',
                    weekEvaluations:[],
                    monthEvaluations:[],
                    yearEvaluations:[]
                },

                weekHourStatsList:[],
                weekHourList:[],
                avgWeekHourList:[],
                leaveHourList:[],
                weekNumberList:[],
                thisYear:'',
                weekHourUserId:'',

                //加班统计
                ewBeginTime:null,
                ewEndTime:null,
                extraWorkReqDTO:{
                    pageNum:1,
                    userId:null,
                    beginTime:null,
                    endTime:null
                },
                extraWorkStatsList:[],
                extraWorkPage:{
                    pageSize:10,
                    total:0
                },
                userInfo:{},
                tabName:'integral',
                personalTaskIntegralData:{
                    userId:'',
                    userName:'',
                    seasonIntegral:'',
                    monthIntegral:'',
                    yearIntegral:'',
                    monthBegin:'',
                    monthEnd:'',
                    seasonBegin:'',
                    seasonEnd:'',
                    yearBegin:'',
                    yearEnd:'',
                    developRole:'',
                },
                jobRoleName:'',
                userAndLevelData:[],
                showUserAndLevelVisible:false,
                principalTaskList:[],
                principalAllTaskList:[],
                principalId:'',
                managerList:[],
                // -- sch
            };
        },
        watch:{
            taskTempForm:{
                deep:true,
                handler:function (val, oldVal) {
                    this.weekNumber = [];
                    let weekData='';
                    let param = this.weekNumber;
                    if (this.userId != null && this.taskTempForm.taskId != null && this.taskTempForm.workHours != null && this.taskTempForm.beginTime != null
                        && this.taskTempForm.endTime != null && (moment(this.taskTempForm.beginTime).isBefore(moment(this.taskTempForm.endTime))
                            || moment(this.taskTempForm.beginTime).isSame(moment(this.taskTempForm.endTime)))) {
                        this.weekTime.beginWeek = moment(this.taskTempForm.beginTime).week();
                        this.weekTime.endWeek = moment(this.taskTempForm.endTime).week();
                        let beginYear = moment(this.taskTempForm.beginTime).year();
                        let endYear = moment(this.taskTempForm.endTime).year();
                        if(beginYear!==endYear){
                            //当前情况:  开始周是在上一年  截止周在下一年
                            if(this.weekTime.beginWeek > this.weekTime.endWeek){
                                for(let i=this.weekTime.beginWeek;i<moment(this.taskTempForm.beginTime).weeksInYear()+1;i++){
                                    http.zsyGetHttp('/userWeek/'+ this.taskTempForm.taskId +'/' + this.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                                        weekData = {'weekNumber':i, 'hours': '','year':beginYear ,'weekHours': resp.data,
                                            'range':moment().year(beginYear).week(i).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(i).endOf('week').format('MM-DD') };
                                        param.push(weekData)
                                    })
                                }
                                for(let i=1;i<this.weekTime.endWeek+1;i++){
                                    http.zsyGetHttp('/userWeek/'+ this.taskTempForm.taskId +'/' + this.userId + '/' + endYear + '/' + i, {}, (resp) => {
                                        weekData = {'weekNumber':i, 'hours': '','year':endYear ,'weekHours': resp.data,'range':moment().year(endYear).week(i).startOf('week').format('MM-DD')+'至'+moment().year(endYear).week(i).endOf('week').format('MM-DD') };
                                        param.push(weekData)
                                    })

                                }
                            }else {
                                for (let i = 0;i <= this.weekTime.endWeek - this.weekTime.beginWeek;i++){
                                    let thisWeek = i+this.weekTime.beginWeek;
                                    http.zsyGetHttp('/userWeek/'+ this.taskTempForm.taskId +'/' + this.userId + '/' + endYear + '/' + thisWeek, {}, (resp) => {
                                        weekData = {'weekNumber':thisWeek, 'hours': '','year':endYear ,'weekHours': resp.data,'range':moment().year(endYear).week(thisWeek).startOf('week').format('MM-DD')
                                                +'至'+moment().year(endYear).week(thisWeek).endOf('week').format('MM-DD') };
                                        param.push(weekData)
                                    })
                                }
                            }

                        }
                        else {
                            if(this.weekTime.beginWeek === this.weekTime.endWeek){
                                http.zsyGetHttp('/userWeek/'+ this.taskTempForm.taskId +'/' +  this.userId + '/' + beginYear + '/' + this.weekTime.beginWeek, {}, (resp) => {
                                    weekData = {'weekNumber':this.weekTime.beginWeek, 'hours': this.taskTempForm.workHours ,'weekHours': resp.data,'year':beginYear ,'range':moment().year(beginYear).week(this.weekTime.beginWeek).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(this.weekTime.beginWeek).endOf('week').format('MM-DD')};
                                    param.push(weekData)
                                })
                            }
                            else if(this.weekTime.endWeek - this.weekTime.beginWeek >1){
                                for(let i=this.weekTime.beginWeek;i<this.weekTime.endWeek+1;i++){
                                    http.zsyGetHttp('/userWeek/'+ this.taskTempForm.taskId +'/' +  this.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                                        weekData = {'weekNumber':i, 'hours': '','year':beginYear ,'weekHours': resp.data,'range':moment().week(i).year(beginYear).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(i).endOf('week').format('MM-DD')  };
                                        param.push(weekData)
                                    })
                                }
                            }
                            else if(this.weekTime.endWeek - this.weekTime.beginWeek === 1){
                                http.zsyGetHttp('/userWeek/'+ this.taskTempForm.taskId +'/' +  this.userId + '/' + beginYear + '/' + this.weekTime.beginWeek, {}, (resp) => {
                                    param.push( {'weekNumber':this.weekTime.beginWeek, 'hours': '' ,'year':beginYear  ,'weekHours': resp.data,'range':moment().year(beginYear).week(this.weekTime.beginWeek).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(this.weekTime.beginWeek).endOf('week').format('MM-DD')})
                                });
                                http.zsyGetHttp('/userWeek/'+ this.taskTempForm.taskId +'/' + this.userId + '/' + endYear + '/' + this.weekTime.endWeek, {}, (resp) => {
                                    param.push( {'weekNumber':this.weekTime.endWeek, 'hours': '' ,'year':beginYear ,'weekHours': resp.data,'range':moment().year(beginYear).week(this.weekTime.endWeek).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(this.weekTime.endWeek).endOf('week').format('MM-DD')})

                                })
                            }
                            else if (this.weekTime.endWeek === 1 && this.weekTime.beginWeek>1){

                                for(let i=this.weekTime.beginWeek;i<54;i++){
                                    let date = new Date(this.taskTempForm.beginTime);
                                    let date2 = new Date(date);
                                    date2.setDate(date.getDate() + 7*(i-this.weekTime.beginWeek));
                                    let year = date2.getFullYear();
                                    let week = moment(date2).week();
                                    if (week == 1){
                                        year +=1;
                                    }
                                    http.zsyGetHttp('/userWeek/'+ this.taskTempForm.taskId +'/' +  this.userId + '/' + year + '/' + week, {}, (resp) => {
                                        weekData = {'weekNumber':week, 'hours': '','year':year ,'weekHours': resp.data,'range':moment().week(week).year(year).startOf('week').format('MM-DD')+'至'+moment().year(year).week(week).endOf('week').format('MM-DD')  };
                                        param.push(weekData)
                                    })
                                }
                            }
                        }

                        this.weekNumber = param
                    }
                }
            },
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
            insMsgShow() {
                if (this.taskTempDetail.suggest === '') {
                    return '点击添加审核意见'
                } else {
                    return this.taskTempDetail.suggest
                }
            },
            sortWeekNumber(){
                // if (this.weekNumber.length != null) {
                //     for (let i = 0; i < this.weekNumber.length; i++) {
                //         for (let x = 0; x < this.weekNumberTemp.length; x++) {
                //             if (this.weekNumber[i] != null) {
                //                 if (this.weekNumber[i].weekNumber === this.weekNumberTemp[x].weekNumber) {
                //                     this.weekNumber[i].hoursTemp = this.weekNumberTemp[x].hours
                //                 }
                //             }
                //         }
                //     }
                // }
                // return _.orderBy(this.weekNumber, 'weekNumber')
                this.weekNumber=this.weekNumber.sort(function (a,b) {
                    if (a.weekNumber < b.weekNumber) {
                        return -1;
                    } else if (a.weekNumber == b.weekNumber) {
                        return 0;
                    } else {
                        return 1;
                    }
                });
                this.weekNumber=this.weekNumber.sort(function (a,b) {
                    if (a.year < b.year) {
                        return -1;
                    } else if (a.year == b.year) {
                        return 0;
                    } else {
                        return 1;
                    }
                });
                return this.weekNumber
            },
            sortWeekTempNumber(){
                // if (this.taskTempWeekNumber.length != null) {
                //     for (let i = 0; i < this.taskTempWeekNumber.length; i++) {
                //         for (let x = 0; x < this.taskWeekNumberTemp.length; x++) {
                //             if (this.taskTempWeekNumber[i] != null) {
                //                 if (this.taskTempWeekNumber[i].weekNumber === this.taskWeekNumberTemp[x].weekNumber) {
                //                     this.taskTempWeekNumber[i].hoursTemp = this.taskWeekNumberTemp[x].hours
                //                 }
                //             }
                //         }
                //     }
                // }
                this.taskTempWeekNumber=this.taskTempWeekNumber.sort(function (a,b) {
                    if (a.weekNumber < b.weekNumber) {
                        return -1;
                    } else if (a.weekNumber == b.weekNumber) {
                        return 0;
                    } else {
                        return 1;
                    }
                });
                this.taskTempWeekNumber=this.taskTempWeekNumber.sort(function (a,b) {
                    if (a.year < b.year) {
                        return -1;
                    } else if (a.year == b.year) {
                        return 0;
                    } else {
                        return 1;
                    }
                });
                return this.taskTempWeekNumber
                // return _.orderBy(this.taskTempWeekNumber, 'weekNumber')
            },
            sortUserWeekNumber(){
                // if (this.taskUserWeekNumber.length != null) {
                //     for (let i = 0; i < this.taskUserWeekNumber.length; i++) {
                //         for (let x = 0; x < this.taskUserTempWeekNumber.length; x++) {
                //             if (this.taskUserWeekNumber[i] != null) {
                //                 if (this.taskUserWeekNumber[i].weekNumber === this.taskUserTempWeekNumber[x].weekNumber) {
                //                     this.taskUserWeekNumber[i].hoursTemp = this.taskUserTempWeekNumber[x].hours
                //                 }
                //             }
                //         }
                //     }
                // }
                // return _.orderBy(this.taskUserWeekNumber, 'weekNumber')
                this.taskUserWeekNumber=this.taskUserWeekNumber.sort(function (a,b) {
                    if (a.weekNumber < b.weekNumber) {
                        return -1;
                    } else if (a.weekNumber == b.weekNumber) {
                        return 0;
                    } else {
                        return 1;
                    }
                });
                this.taskUserWeekNumber=this.taskUserWeekNumber.sort(function (a,b) {
                    if (a.year < b.year) {
                        return -1;
                    } else if (a.year == b.year) {
                        return 0;
                    } else {
                        return 1;
                    }
                });
                return this.taskUserWeekNumber
            },
            sortTaskModifyWeekNumber(){
                // if (this.taskModifyWeekNumber.length != null) {
                //     for (let i = 0; i < this.taskModifyWeekNumber.length; i++) {
                //         for (let x = 0; x < this.taskModifyWeekNumberTemp.length; x++) {
                //             if (this.taskModifyWeekNumber[i] != null) {
                //                 if (this.taskModifyWeekNumber[i].weekNumber === this.taskModifyWeekNumberTemp[x].weekNumber) {
                //                     this.taskModifyWeekNumber[i].hoursTemp = this.taskModifyWeekNumberTemp[x].hours
                //                 }
                //             }
                //         }
                //     }
                // }
                // return _.orderBy(this.taskModifyWeekNumber, 'weekNumber')
                this.taskModifyWeekNumber=this.taskModifyWeekNumber.sort(function (a,b) {
                    if (a.weekNumber < b.weekNumber) {
                        return -1;
                    } else if (a.weekNumber == b.weekNumber) {
                        return 0;
                    } else {
                        return 1;
                    }
                });
                this.taskModifyWeekNumber=this.taskModifyWeekNumber.sort(function (a,b) {
                    if (a.year < b.year) {
                        return -1;
                    } else if (a.year == b.year) {
                        return 0;
                    } else {
                        return 1;
                    }
                });
                return this.taskModifyWeekNumber
            },
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
            waitAuditPageLayout() {
                if (this.waitAuditPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            taskTempWaitAuditPageLayout() {
                if (this.taskTempPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            taskTempWaitAuditPageLayout2() {
                if (this.taskTempPage3.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            taskTempAuditSuccessPageLayout() {
                if (this.taskTempPage2.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            taskTempAuditSuccessPageLayout2() {
                if (this.taskTempPage4.total > 0) {
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
            personalTaskModifyPageLayout() {
                if (this.personalTaskModifyPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            taskModifyPageLayout() {
                if (this.taskModifyPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            expandDoingPageLayout() {
                if (this.expandDoingPage.total > 0) {
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
            myRestHoursLogsPageLayout() {
                if (this.myRestHoursLogPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            userRestHoursLogsPageLayout() {
                if (this.userRestHoursLogPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            leavePageLayout() {
                if (this.leaveFormPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            eWorkPageLayout() {
                if (this.extraWorkPage.total > 0) {
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
            sortList1(a,b){
                if (a.weekNumber < b.weekNumber) {
                    return -1;
                } else if (a.weekNumber == b.weekNumber) {
                    return 0;
                } else {
                    return 1;
                }
            },
            sortList2(a,b){
                if (a.year < b.year) {
                    return -1;
                } else if (a.year == b.year) {
                    return 0;
                } else {
                    return 1;
                }
            },
            handleClick(tab, event) {
                // 点击进行中和已完成
                if (tab.label === '测试中') {
                    this.fetchTaskTesting();
                }else if (this.activeName === 'completed'){
                    this.fetchTaskFinished();
                }
            },
            handleClickLeave(){
              if (this.activeLeaveName === 'wait'){
                  this.fetchUserLeaveList();
              }else {
                  this.fetchUserLeavePassList();
              }
            },
            handleClickExtraWork(){
              if (this.activeEWorkName === 'wait'){
                  if (this.userRole === 0){
                      this.fetchCheckingExtraWork();
                  } else {
                      this.fetchMyRunningExtraWork();
                  }
              }else {
                  if (this.userRole === 0){
                      this.fetchCheckedExtraWork();
                  }else {
                      this.fetchMyCheckedExtraWork();
                  }
              }
            },
            handleClickRecheck(){
                if (this.activeRecheckName === 'wait'){
                    if (this.userRole === 0){
                        this.fetchRecheckWait();
                    } else {
                        this.fetchMyRecheckWait();
                    }
                }else {
                    if (this.userRole === 0){
                        this.fetchRecheckPass();
                    } else {
                        this.fetchMyRecheckPass();
                    }
                }
            },
            handleClickMultiTask(){
               if (this.taskTempActiveName2 === 'wait'){
                   this.fetchPendingTaskTemp();
               }else {
                   this.fetchMultipleAccess();
               }
            },
            handleClickPrivateTask(){
                if (this.auditTabsActiveName === 'wait'){
                    this.fetchTaskWaitAudit();
                }else {
                    this.fetchTaskAuditSuccess();
                }
            },
            handleClickTaskModify(){
                if(this.taskModifyTabsActiveName === 'wait'){
                    this.fetchTaskModifyWait();
                }else {
                    this.fetchTaskModifyAccessed();
                }
            },
            handleClickOnlineQuestion(){
                if (this.questionActiveName === 'wait'){
                    this.fetchQuestionWait();
                }else {
                    this.fetchQuestionAccepted();
                }
            },
            handleClickMyOnlineQuestion(){
               if (this.activeQuestionName === 'wait'){
                 this.fetchQuestionDoing();
               } else {
                   this.fetchQuestionCompleted();
               }
            },
            handleClickEvaluate(){
              if (this.assessActiveName === 'waitAssess'){
                  this.fetchWaitEvaluate();
              }   else {
                  this.fetchEvaluated();
              }
            },
            handleClickMyModify(){
                if (this.taskModifyTabsActiveName2 === 'wait'){
                    this.fetchPersonalTaskModifyWait();
                } else {
                    this.fetchPersonalTaskModifyAccessed();
                }
            },
            createTaskClick() {
                // 建任务
                // this.clearPrivateTask()
                this.createTaskVisible = true
            },
            //创建多人任务
            createMultipleTask(){
                this.fetchAllMultipleTasks();
                this.createTaskVisible = false;
                this.clearMultipleTask();
                this.taskFunctionData = [];
                this.createMultipleTaskVisible = true;
                this.showTaskDescriptionVisible = false;
            },
            //创建个人任务
            createPrivateTask(){
                // this.clearMultipleTask();
                this.fetchProjectList();
                this.createTaskVisible = false;
                this.createPrivateTaskVisible = true;
            },
            reload() {
                this.task.doing = [];
                this.initSignInTime();
                // this.fetchIntegral()
                this.fetchProjectList();
                this.fetchStageList();
                this.fetchTagList();
                // this.fetchUserList();
                this.fetchSignInUser();
                //this.fetchApplyFailTask();
                // this.fetchTaskExpandDoing();
                // this.fetchTaskExpandSuccess();
                this.fetchUnreadNoticeNum();
                this.fetchControlledPeople();

                //人事查看相关统计
                this.fetchSignInData();
                this.getLeaveList();
                this.getExtraWorkStats();
                // this.fetchMultipleWait();
                // this.fetchTaskTempModuleList();
                if (this.userRole === 0) {
                    this.fetchAllPrincipalTask();
                    this.fetchTaskWaitAudit();
                    // this.fetchUserWeekHourStats()
                    // 所有审核通过的数据
                    // this.fetchTaskAuditSuccess();
                    //待审核的积分转移，审核通过的积分转移
                    // this.fetchHelpWaitAdmin()
                    // this.fetchHelpReviewAdmin()
                    // this.fetchQuestionAccepted();
                    this.fetchQuestionWait();
                    this.fetchCheckingExtraWork();
                    // this.fetchCheckedExtraWork();
                    this.fetchRecheckWait();
                    // this.fetchRecheckPass();
                    // this.fetchMultipleAccess();
                    this.fetchTaskModifyWait();
                    this.fetchTaskModifyAccessed();
                    this.fetchUserLeaveList();
                } else if (this.userRole>0&&this.userRole<3) {
                    // this.fetchMyHelpWaitList();
                    // this.fetchMyReviewSuccess();
                    this.fetchUserLeaveList();
                    // this.fetchUserLeavePassList();
                    this.fetchTaskDoing();
                    // this.fetchTaskFinished();
                    // this.fetchTaskWaitAssess();
                    // this.fetchTaskCommented();
                    this.fetchWaitEvaluate();
                    // this.fetchEvaluated();
                    this.fetchQuestionDoing();
                    // this.fetchQuestionCompleted();
                    this.fetchMyRunningExtraWork();
                    // this.fetchMyCheckedExtraWork();
                    this.fetchMyRecheckWait();
                    // this.fetchMyRecheckPass();
                    this.fetchMySignInData();
                    this.fetchMyRestHours();
                    // this.fetchMultipleAccess();
                    this.fetchPersonalMultipleWait();
                    // this.fetchPersonalMultipleAccess();
                    this.fetchPersonalTaskModifyWait();
                    // this.fetchPersonalTaskModifyAccessed();
                    // this.fetchPersonalEvaluation();
                    // this.fetchWeekHourStats();
                    if (this.userRole>1){
                        this.fetchPersonalTaskIntegral();
                    } else {
                        this.fetchPrincipalTask();
                        this.tabName = 'task';
                    }

                }else {
                    this.fetchAllUsersRestHours()
                }

            },
            errorMsg(msg) {
                this.$message({
                    showClose: true,
                    message: msg,
                    type: 'error'
                });
            },
            //清空新建个人任务
            clearPrivateTask(){
                this.taskAble = false;
                this.taskForm.taskType = 1;
                this.taskForm.priority = 1;
                this.taskForm.stageId = '';
              this.taskForm.taskName = this.taskForm.description = this.taskForm.beginTime = this.taskForm.endTime
                = this.taskForm.projectId = this.taskForm.taskHours = '';
                this.taskForm.tags = []
            },
            saveTaskInfo(formName) {
                let vm = this;
                this.taskAble = true;

                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let userId = helper.decodeToken().userId;
                        this.taskForm.endTime = moment(this.taskForm.endTime).toDate();
                        this.taskForm.beginTime = moment(this.taskForm.beginTime).toDate();
                        let param = this.taskForm;
                        param.taskName = param.taskName.trim();
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD HH:00:00');
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:59');
                        if ((moment(this.taskForm.endTime)).isBefore((moment(this.taskForm.beginTime)))){
                            this.$message({showClose: true, message: '开始时间不能再截止时间后面', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        if (param.taskHours.length !== parseFloat(param.taskHours).toString().length || parseFloat(param.taskHours) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        if (param.taskHours.trim() > 8 || param.taskHours.trim() < 0.1) {
                            this.$message({showClose: true, message: '工作量正确值应为0.1~8', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        if (moment(param.endTime).millisecond() < moment(param.beginTime).millisecond() || moment(param.endTime).week() !== moment(param.beginTime).week()) {
                            this.$message({showClose: true, message: '请检查日期，个人任务请勿跨周进行', type: 'warning'});
                            this.taskAble = false;
                            return false;
                        }
                        let taskUsers = [{
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
                            this.createPrivateTaskVisible = false;
                            this.taskAble = false;
                            vm.reload()
                        });
                    } else {
                        this.taskAble = false
                    }
                }, err => {
                    this.taskAble = false
                });
            },
            saveMultipleTask(formName) {
                if (this.taskFunctionData.length>0){
                    this.taskTempForm.taskTempFunctionList = [];
                    for(let i = 0;i<this.num;i++){
                        let taskTempFunction={
                            functionId:this.taskFunctionList[i],
                            level:this.functionLevelList[i]
                        };
                        this.taskTempForm.taskTempFunctionList.push(taskTempFunction);
                    }
                    for(let i = 0;i < this.taskTempForm.taskTempFunctionList.length;i++){
                        let taskTempFunction = this.taskTempForm.taskTempFunctionList[i];
                        let functionId = taskTempFunction.functionId;
                        let level = taskTempFunction.level;
                        if (functionId === undefined || functionId === null || functionId === ''){
                            this.$message({showClose: true, message: '关联任务功能点不能为空,请检查', type: 'error'});
                            return false;
                        }
                        if (level === undefined || level === null || level === ''){
                            this.$message({showClose: true, message: '功能点复杂度不能为空,请检查', type: 'error'});
                            return false;
                        }
                    }
                    let newArr = [this.taskTempForm.taskTempFunctionList[0].functionId];
                    for (let i = 1; i < this.taskTempForm.taskTempFunctionList.length; i++) {
                        let repeat = false;
                        for (let j = 0; j < newArr.length; j++) {
                            if (this.taskTempForm.taskTempFunctionList[i].functionId === newArr[j]) {
                                repeat = true;
                                break;
                            }else{

                            }
                        }
                        if (!repeat){
                            newArr.push(this.taskTempForm.taskTempFunctionList[i].functionId)
                        }
                    }
                    if (this.taskTempForm.taskTempFunctionList.length>newArr.length){
                        this.$message({showClose: true, message: '功能点不可重复选择', type: 'error'});
                        return false;
                    }
                }

                let sumHours=0;
                for(let i=0;i<this.weekNumber.length;i++){
                    if(this.weekNumber[i].hours===''|| this.weekNumber[i].hours=== undefined){
                        if(this.weekNumber[i].hoursTemp !== undefined &&this.weekNumber[i].hoursTemp!==''){
                            this.weekNumber[i].hours = this.weekNumber[i].hoursTemp
                        }else{
                            this.weekNumber[i].hours = 0
                        }
                    }
                    let ishours = /^(([0-9]+[\.]?[0-9]+)|[0-9])$/.test(this.weekNumber[i].hours);
                    if(!ishours){
                        this.errorMsg('工作量填写错误');
                        return false;
                    }
                    if(this.weekNumber[i].hours>99999.9||this.weekNumber[i].hours<0){
                        this.errorMsg('工作量正确值应为0~99999.9');
                        return false;
                    }
                    sumHours +=  parseFloat(this.weekNumber[i].hours)
                }
                if(sumHours!=this.taskTempForm.workHours){
                    this.errorMsg('周工作量与总工作量不符，请检查');
                    return
                }
                this.taskTempForm.userWeeks = this.weekNumber;


                let vm = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.taskAble = true;
                        let param = this.taskTempForm;
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD 00:00:00');
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:50');
                        if (param.taskId == null || param.taskId === ''){
                            this.$message({showClose: true, message: '关联任务不能为空', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        if (this.description == null || this.description.trim() === ''){
                            this.$message({showClose: true, message: '任务描述不能为空', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        if (param.beginTime == null || param.beginTime === ''){
                            this.$message({showClose: true, message: '开始时间不能为空', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        if (param.endTime == null || param.endTime === ''){
                            this.$message({showClose: true, message: '截止时间不能为空', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        //任务是设计相关阶段时
                        if (this.taskDetail.stageId === '212754785051344891' || this.taskDetail.stageId === '212754785051344892'){
                            if (!(moment(this.taskTempForm.endTime)).isBefore((moment(this.taskDetail.beginTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务设计完成时间', type: 'error'});
                                this.taskAble = false;
                                return false;
                            }
                        }
                        //任务是开发相关阶段时
                        if (this.taskDetail.stageId === '212754785051344890' || this.taskDetail.stageId === '212754785051344894'){
                            if (!(moment(this.taskTempForm.endTime)).isBefore((moment(this.taskDetail.testTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务开发完成时间', type: 'error'});
                                this.taskAble = false;
                                return false;
                            }
                        }
                        //任务是测试相关阶段时
                        if (this.taskDetail.stageId === '212754785051344895' || this.taskDetail.stageId === '212754785051344896'){
                            if (!(moment(this.taskTempForm.endTime)).isBefore((moment(this.taskDetail.endTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务截止时间', type: 'error'});
                                this.taskAble = false;
                                return false;
                            }
                        }
                        if (param.workHours.length !== parseFloat(param.workHours).toString().length || parseFloat(param.workHours) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        if (param.workHours.trim() < 0.1) {
                            this.$message({showClose: true, message: '工作量正确值应为0.1~8', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        // param.workHours = Number(param.workHours)
                        param.description = this.description;

                        param.taskTempFunctionList = this.taskTempForm.taskTempFunctionList;
                        http.zsyPostHttp('/task-temp/add', param, (resp) => {
                            this.$message({showClose: true, message: '任务创建成功', type: 'success'});
                            this.$refs[formName].resetFields();
                            this.description = '';
                            this.taskTempForm.taskTempFunctionList = [];
                            this.taskFunctionList = [];
                            this.functionLevelList = [];
                            this.num = 1;
                            this.createMultipleTaskVisible = false;
                            this.taskAble = false;
                            vm.reload()
                        });
                    } else {
                        this.taskAble = false;
                        return false;
                    }
                }, err => {
                    this.taskAble = false
                });
            },
            editMultipleTask(formName) {
                let sumHours=0;
                for(let i=0;i<this.taskTempWeekNumber.length;i++){
                    if(this.taskTempWeekNumber[i].hours===''|| this.taskTempWeekNumber[i].hours=== undefined){
                        if(this.taskTempWeekNumber[i].hoursTemp !== undefined &&this.taskTempWeekNumber[i].hoursTemp!==''){
                            this.taskTempWeekNumber[i].hours = this.taskTempWeekNumber[i].hoursTemp
                        }else{
                            this.taskTempWeekNumber[i].hours = 0
                        }
                    }
                    let ishours = /^(([0-9]+[\.]?[0-9]+)|[0-9])$/.test(this.taskTempWeekNumber[i].hours);
                    if(!ishours){
                        this.errorMsg('工作量填写错误');
                        return false;
                    }
                    if(this.taskTempWeekNumber[i].hours>99999.9||this.taskTempWeekNumber[i].hours<0){
                        this.errorMsg('工作量正确值应为0~99999.9');
                        return false;
                    }
                    sumHours +=  parseFloat(this.taskTempWeekNumber[i].hours)
                }
                if(sumHours!=this.taskTempDetail.workHours){
                    this.errorMsg('周工作量与总工作量不符，请检查');
                    return
                }
                this.taskTempDetail.userWeeks = this.taskTempWeekNumber;


                let vm = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.taskAble = true;
                        let param = this.taskTempDetail;
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD 00:00:00');
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:50');
                        if (param.taskId == null || param.taskId === ''){
                            this.$message({showClose: true, message: '关联任务不能为空', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        if (this.description == null || this.description.trim() === ''){
                            this.$message({showClose: true, message: '任务描述不能为空', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        if (param.beginTime == null || param.beginTime === ''){
                            this.$message({showClose: true, message: '开始时间不能为空', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        if (param.endTime == null || param.endTime === ''){
                            this.$message({showClose: true, message: '截止时间不能为空', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        //任务是设计相关阶段时
                        if (this.taskDetail.stageId === '212754785051344891' || this.taskDetail.stageId === '212754785051344892'){
                            if (!(moment(this.taskTempDetail.endTime)).isBefore((moment(this.taskDetail.beginTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务设计完成时间', type: 'error'});
                                this.taskAble = false;
                                return false;
                            }
                        }
                        //任务是开发相关阶段时
                        if (this.taskDetail.stageId === '212754785051344890' || this.taskDetail.stageId === '212754785051344894'){
                            if (!(moment(this.taskTempDetail.endTime)).isBefore((moment(this.taskDetail.testTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务开发完成时间', type: 'error'});
                                this.taskAble = false;
                                return false;
                            }
                        }
                        //任务是测试相关阶段时
                        if (this.taskDetail.stageId === '212754785051344895' || this.taskDetail.stageId === '212754785051344896'){
                            if (!(moment(this.taskTempDetail.endTime)).isBefore((moment(this.taskDetail.endTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务截止时间', type: 'error'});
                                this.taskAble = false;
                                return false;
                            }
                        }
                        param.workHours = String(param.workHours);
                        if (param.workHours.length !== parseFloat(param.workHours).toString().length || parseFloat(param.workHours) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        if (param.workHours < 0.1) {
                            this.$message({showClose: true, message: '工作量正确值应为0.1~8', type: 'error'});
                            this.taskAble = false;
                            return false;
                        }
                        // param.workHours = Number(param.workHours)
                        param.description = this.description;
                        http.zsyPutHttp('/task-temp/update', param, (resp) => {
                            this.taskTempDetailVisible = false;
                            this.$message({showClose: true, message: '任务修改成功', type: 'success'});
                            this.$refs[formName].resetFields();
                            this.description = '';
                            this.taskAble = false;
                            vm.reload()
                        });
                    } else {
                        this.taskAble = false;
                        return false;
                    }
                }, err => {
                    this.taskAble = false
                });
            },
            deleteMultipleTask(id){
                this.$confirm('此操作将删除该任务, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyDeleteHttp(`/task-temp/delete/` + id, {}, (resp) => {
                        this.$message({showClose: true, message: '删除成功', type: 'success'});
                        this.reload();
                        this.fetchPendingTaskTemp();
                        this.taskTempDetailVisible = false;
                    })
                }).catch(() => {
                });
            },
            closeTaskTemp(){
              this.taskFunctionList = [];
              this.functionLevelList = [];
            },
            acceptMultipleTask(id,formName){
                this.accessTaskTempLoading = true;
                let sumHours=0;
                for(let i=0;i<this.taskTempWeekNumber.length;i++){
                    if(this.taskTempWeekNumber[i].hours===''|| this.taskTempWeekNumber[i].hours=== undefined){
                        if(this.taskTempWeekNumber[i].hoursTemp !== undefined &&this.taskTempWeekNumber[i].hoursTemp!==''){
                            this.taskTempWeekNumber[i].hours = this.taskTempWeekNumber[i].hoursTemp
                        }else{
                            this.taskTempWeekNumber[i].hours = 0
                        }
                    }
                    let ishours = /^(([0-9]+[\.]?[0-9]+)|[0-9])$/.test(this.taskTempWeekNumber[i].hours);
                    if(!ishours){
                        this.errorMsg('工作量填写错误');
                        this.accessTaskTempLoading = false;
                        return false;
                    }
                    if(this.taskTempWeekNumber[i].hours>99999.9||this.taskTempWeekNumber[i].hours<0){
                        this.errorMsg('工作量正确值应为0~99999.9');
                        this.accessTaskTempLoading = false;
                        return false;
                    }
                    sumHours +=  parseFloat(this.taskTempWeekNumber[i].hours)
                }
                if(sumHours!=this.taskTempDetail.workHours){
                    this.errorMsg('周工作量与总工作量不符，请检查');
                    this.accessTaskTempLoading = false;
                    return
                }
                this.taskTempDetail.userWeeks = this.taskTempWeekNumber;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        // this.taskAble = true;
                        let param = this.taskTempDetail;
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD 00:00:00');
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:50');
                        if (param.taskId == null || param.taskId === ''){
                            this.$message({showClose: true, message: '关联任务不能为空', type: 'error'});
                            this.accessTaskTempLoading = false;
                            return false;
                        }
                        if (this.description == null || this.description.trim() === ''){
                            this.$message({showClose: true, message: '任务描述不能为空', type: 'error'});
                            this.accessTaskTempLoading = false;
                            return false;
                        }
                        if (param.beginTime == null || param.beginTime === ''){
                            this.$message({showClose: true, message: '开始时间不能为空', type: 'error'});
                            this.accessTaskTempLoading = false;
                            return false;
                        }
                        if (param.endTime == null || param.endTime === ''){
                            this.$message({showClose: true, message: '截止时间不能为空', type: 'error'});
                            this.accessTaskTempLoading = false;
                            return false;
                        }
                        //任务是设计相关阶段时
                        if (this.taskDetail.stageId === '212754785051344891' || this.taskDetail.stageId === '212754785051344892'){
                            if (!(moment(this.taskTempDetail.endTime)).isBefore((moment(this.taskDetail.beginTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务设计完成时间', type: 'error'});
                                this.accessTaskTempLoading = false;
                                return false;
                            }
                        }
                        //任务是开发相关阶段时
                        if (this.taskDetail.stageId === '212754785051344890' || this.taskDetail.stageId === '212754785051344894'){
                            if (!(moment(this.taskTempDetail.endTime)).isBefore((moment(this.taskDetail.testTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务开发完成时间', type: 'error'});
                                this.accessTaskTempLoading = false;
                                return false;
                            }
                        }
                        //任务是测试相关阶段时
                        if (this.taskDetail.stageId === '212754785051344895' || this.taskDetail.stageId === '212754785051344896'){
                            if (!(moment(this.taskTempDetail.endTime)).isBefore((moment(this.taskDetail.endTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务截止时间', type: 'error'});
                                this.accessTaskTempLoading = false;
                                return false;
                            }
                        }
                        if (param.taskLevel === undefined || param.taskLevel === null || param.taskLevel === '') {
                            this.$message({showClose: true, message: '请选择任务级别', type: 'error'});
                            this.accessTaskTempLoading = false;
                            return false;
                        }
                        param.workHours = String(param.workHours);
                        if (param.workHours.length !== parseFloat(param.workHours).toString().length || parseFloat(param.workHours) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            this.accessTaskTempLoading = false;
                            return false;
                        }
                        if (param.workHours < 0.1) {
                            this.$message({showClose: true, message: '工作量正确值应为0.1~8', type: 'error'});
                            this.accessTaskTempLoading = false;
                            return false;
                        }
                        param.description = this.description;

                        if (param.functionResDTOList !== undefined && param.functionResDTOList.length>0){
                            let functionList = [];
                            for(let i = 0;i<this.num;i++){
                                let taskTempFunction={
                                    functionId:this.taskFunctionList[i],
                                    level:this.functionLevelList[i],
                                };
                                functionList.push(taskTempFunction);
                            }
                            param.taskTempFunctionList = functionList;
                            if (param.taskTempFunctionList.length>0){
                                for (let i = 0;i<param.taskTempFunctionList.length;i++){
                                    let taskTempFunction = param.taskTempFunctionList[i];
                                    let functionId = taskTempFunction.functionId;
                                    let level = taskTempFunction.level;
                                    if (functionId === undefined || functionId === null || functionId ===''){
                                        this.$message({showClose: true, message: '关联任务功能点不能为空,请检查', type: 'error'});
                                        this.accessTaskTempLoading = false;
                                        return false;
                                    }
                                    if (level === undefined || level === null || level === ''){
                                        this.$message({showClose: true, message: '功能点复杂度不能为空,请检查', type: 'error'});
                                        this.accessTaskTempLoading = false;
                                        return false;
                                    }
                                }
                            }
                            let newArr = [param.taskTempFunctionList[0].functionId];
                            for (let i = 1; i < param.taskTempFunctionList.length; i++) {
                                let repeat = false;
                                for (let j = 0; j < newArr.length; j++) {
                                    if (param.taskTempFunctionList[i].functionId === newArr[j]) {
                                        repeat = true;
                                        break;
                                    }else{

                                    }
                                }
                                if (!repeat){
                                    newArr.push(param.taskTempFunctionList[i].functionId)
                                }
                            }
                            if (param.taskTempFunctionList.length>newArr.length){
                                this.$message({showClose: true, message: '功能点不可重复选择', type: 'error'});
                                this.accessTaskTempLoading = false;
                                return false;
                            }
                        }

                        http.zsyPutHttp(`/task-temp/access`, param, (resp) => {
                            this.$message({showClose: true, message: '审核成功', type: 'success'});
                            this.accessTaskTempLoading = false;
                            this.taskTempDetailVisible = false;
                            this.$refs[formName].resetFields();
                            param.taskTempFunctionList = [];
                            this.taskFunctionList = [];
                            this.functionLevelList = [];
                            this.description = '';
                            this.taskAble = false;
                            this.reload();
                            this.fetchPendingTaskTemp();
                        },(err)=>{
                            param.taskTempFunctionList = [];
                            this.taskFunctionList = [];
                            this.functionLevelList = [];
                        },error=>{
                            this.accessTaskTempLoading = false;
                            this.taskAble = false;
                            param.taskTempFunctionList = [];
                            this.taskFunctionList = [];
                            this.functionLevelList = [];
                        })
                    } else {
                        return false;
                    }
                }, err => {
                    this.taskAble = false
                });

            },
            //获取任务详情
            getTaskDetail(id){
                this.fetchTaskFunction(id);
                if(id != null && id !== ''){
                  http.zsyGetHttp('task/detail/'+id,{},(res =>{
                      this.taskDetail = res.data;
                      this.showTaskDetailVisible = true;
                      this.showTaskDescriptionVisible = true;
                  }))
              }else {
                  this.taskDetail = {};
                  this.showTaskDetailVisible = false;
                  this.showTaskDescriptionVisible = false;
                  this.clearFunctionForm();
              }
            },
            //按阶段查询任务
            getTaskByStage(stageId){
                if (stageId != null && stageId !== '') {
                    http.zsyGetHttp('task-temp/task/'+stageId,{},(res=>{
                        this.taskList = res.data;
                    }))
                }else {
                    this.fetchAllMultipleTasks()
                }
            },
            makeUpItems(items) {
                const list = items;
                list.forEach((el) => {
                    let endTime = '', today = moment().format('YYYY-MM-DD');
                    if (el.status >= 2) {
                        endTime = el.completeTime
                    } else if ((el.reviewStatus === 1 || el.reviewStatus === 3) && el.taskUsers[0].status === 1) {
                        endTime = el.taskUsers[0].endTime
                    } else {
                        endTime = el.taskUsers[0].completeTime
                    }
                    endTime = moment(endTime).format('YYYY-MM-DD');
                    const diffDays = moment(today).diff(moment(endTime), 'days');
                    //const diffDays = Math.round(moment().diff(moment(endTime), 'days', true))
                    let endColor = '', endText = '';
                    endText = moment(endTime).calendar(null, {
                        sameDay: '[今天]',
                        nextDay: '[明天]',
                        nextWeek: 'L',
                        lastDay: '[昨天]',
                        lastWeek: 'L',
                        sameElse: 'L'
                    });
                    if (el.status < 3 && el.taskUsers[0].status === 1) {
                        if (diffDays === 0) {
                            endColor = 'orange'
                        } else if (diffDays > 0) {
                            endColor = 'red'
                        } else if (diffDays < 0) {
                            endColor = 'blue'
                        }
                        endText += ' 截止'
                    } else {
                        endColor = 'green';
                        endText += ' 完成'
                    }
                    el['endColor'] = endColor;
                    el['endText'] = endText
                });
                return list
            },
            makeUpItems2(items) {
                const list = items;
                list.forEach((el) => {
                    let endTime = '', today = moment().format('YYYY-MM-DD');
                    if (el.status >= 2) {
                        endTime = el.completeTime
                    } else if ((el.reviewStatus === 1 || el.reviewStatus === 3) && el.taskUsers[0].status === 1) {
                        endTime = el.taskUsers[0].endTime
                    } else {
                        endTime = el.taskUsers[0].completeTime
                    }
                    endTime = moment(endTime).format('YYYY-MM-DD');
                    const diffDays = moment(today).diff(moment(endTime), 'days');
                    //const diffDays = Math.round(moment().diff(moment(endTime), 'days', true))
                    let endColor = '', endText = '';
                    endText = moment(endTime).calendar(null, {
                        sameDay: '[今天]',
                        nextDay: '[明天]',
                        nextWeek: 'L',
                        lastDay: '[昨天]',
                        lastWeek: 'L',
                        sameElse: 'L'
                    });
                    if (el.status < 3 && el.taskUsers[0].status === 1) {
                        if (diffDays === 0) {
                            endColor = 'orange'
                        } else if (diffDays > 0) {
                            endColor = 'red'
                        } else if (diffDays < 0) {
                            endColor = 'blue'
                        }
                        endText += ' 截止'
                    } else {
                        endColor = 'green';
                        endText += ' 完成'
                    }
                    el['endColor'] = endColor;
                    el['endText'] = endText
                });
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
                let vm = this;
                http.zsyGetHttp('/task/doing', {}, (resp) => {
                    vm.task.doing = this.makeUpItems(resp.data);
                    vm.fetchMyTaskWaitAudit()
                })
            },
            // 获取用户测试中的任务
            fetchTaskTesting() {
                let vm = this;
                http.zsyGetHttp('/task/testing', {}, (resp) => {
                    vm.task.test = resp.data
                })
            },
            // 获取用户已完成的任务
            fetchTaskFinished() {
                let vm = this;
                http.zsyGetHttp(`/task/finished/${vm.finishedPage.pageNum}`, {}, (resp) => {
                    vm.finishedPage.pageNum = resp.data.pageNum;
                    vm.finishedPage.total = resp.data.total;
                    vm.task.finished = this.makeUpItems(resp.data.list)
                })
            },
            // 获取用户被打回任务
            fetchApplyFailTask() {
                let vm = this;
                http.zsyGetHttp('/task/apply/fail', {}, (resp) => {
                    vm.task.applyFail = this.makeUpItems(resp.data)
                })
            },
            // 获取用户待评价的任务
            fetchTaskWaitAssess() {
                let vm = this;
                http.zsyGetHttp('/task/waitAssess', {}, (resp) => {
                    vm.task.waitAssess = this.makeUpItems(resp.data)
                })
            },
            // 获取用户已评价的任务
            fetchTaskCommented() {
                let vm = this;
                http.zsyGetHttp(`/task/commented/${vm.commentedPage.pageNum}`, {}, (resp) => {
                    vm.commentedPage.total = resp.data.total;
                    vm.task.commented = this.makeUpItems(resp.data.list)
                })
            },
            //查看评价
            fetchWaitEvaluate(){
                http.zsyGetHttp('/evaluation/task/wait',{},(res)=>{
                    this.task.waitAssess = this.makeUpItems(res.data)
                })
            },
            fetchEvaluated(){
                http.zsyGetHttp('/evaluation/task/evaluated/'+this.commentedPage.pageNum,{},(res)=>{
                    this.commentedPage.total = res.data.total;
                    this.task.commented = this.makeUpItems(res.data.list)
                })
            },
            // 获取所有待审核的任务
            fetchTaskWaitAudit() {
                let vm = this;
                http.zsyGetHttp(`/task/pending/all/`+this.waitAuditPage.pageNum, {}, (resp) => {
                    vm.task.waitAudit = this.makeUpItems(resp.data.list);
                    vm.waitAuditPage.pageNum = resp.data.pageNum;
                    vm.waitAuditPage.total = resp.data.total;
                })
            },
            fetchTaskExpandDoing() {
                let vm = this;
                http.zsyGetHttp(`/task-expand/doing/0/`+this.expandDoingPage.pageNum, {}, (resp) => {
                    vm.taskExpand.doing = resp.data.list;
                    // vm.expandDoingPage.pageNum = resp.data.pageNum;
                    vm.expandDoingPage.total = resp.data.total;
                })
            },
            // 获取所有审核通过的任务
            fetchTaskAuditSuccess() {
                let vm = this;
                http.zsyGetHttp(`/task/audit/success/all/${vm.auditSuccessPage.pageNum}`, {}, (resp) => {
                    vm.auditSuccessPage.pageNum = resp.data.pageNum;
                    vm.auditSuccessPage.total = resp.data.total;
                    vm.task.auditSuccess = this.makeUpItems(resp.data.list)
                })
            },
            // 获取所有审核通过的任务
            fetchTaskExpandSuccess() {
                let vm = this;
                http.zsyPostHttp(`/task-expand/list`, {status: 1, pageNum: vm.expandPage.pageNum}, (resp) => {
                    vm.expandPage.pageNum = resp.data.pageNum;
                    vm.expandPage.total = resp.data.total;
                    vm.taskExpand.finished = resp.data.list
                })
            },
            // 获取我的待审核任务
            fetchMyTaskWaitAudit() {
                let vm = this;
                http.zsyGetHttp('/task/pending', {}, (resp) => {
                    resp.data.forEach((task) => {
                        // task.name = '(待审核 个人任务)' + task.name;
                    });
                    vm.task.doing = vm.task.doing.concat(this.makeUpItems(resp.data))
                })
            },
            //获取个人请假信息
            fetchUserLeaveList() {
                let vm = this;
                http.zsyGetHttp('/userLeave/1/' + this.leaveWaitPage.pageNum, {}, (resp) => {
                    vm.leaveList.wait = resp.data.list;
                    vm.leaveWaitPage.total = resp.data.total;
                    vm.leaveWaitPage.pageNum = resp.data.pageNum
                })
            },
            //获取请假通过信息
            fetchUserLeavePassList() {
                let vm = this;
                http.zsyGetHttp('/userLeave/3/' + this.leavePassPage.pageNum, {}, (resp) => {
                    vm.leaveList.pass = resp.data.list;
                    vm.leavePassPage.total = resp.data.total;
                    vm.leavePassPage.pageNum = resp.data.pageNum
                })
            },
            //保存用户转移积分
            saveHelpInfo(helpForm) {
                let param = this.helpForm;
                let help = {
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
                        if (helper.decodeToken().userId === this.helpForm.userId) {
                            this.$message({showClose: true, message: '求助目标不能是自己，请重试', type: 'error'});
                            return false;
                        }
                        this.isSaving = true;
                        if (this.helpForm.id !== '') {
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
                let sumHours = 0;
                for (let i = 0; i < this.expandWeekNumber.length; i++) {
                    if (this.expandWeekNumber[i].hours === '' || this.expandWeekNumber[i].hours === undefined) {
                        this.expandWeekNumber[i].hours = 0
                    }
                    let ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.expandWeekNumber[i].hours);
                    if (!ishours) {
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
                    this.expandReview.endTime = moment(this.expandDetail.endTime).format('YYYY-MM-DD 23:59:59');
                    this.expandReview.teId = this.expandDetail.teId;
                    this.expandReview.status = 1;
                    this.expandReview.weeks = this.expandWeekNumber;
                    this.expandReview.hours = sumHours;
                    http.zsyPostHttp(`/task-expand/review`, this.expandReview, (resp) => {
                        this.$message({showClose: true, message: '审核成功', type: 'success'});
                        this.expandReview = {};
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
                this.expandDetail.hours = expand.hours;
                this.expandDetail.taskId = expand.taskId;
                this.expandDetail.endTime = expand.endTime;
                this.expandDetail.beginTime = expand.beginTime;
                this.changeExpandWeek()
            },
            getTaskTempDetail(taskTemp){
                this.taskTempDetail.suggest = '';
                this.addSuggestVisible = false;
                this.getTaskDetail(taskTemp.taskId);

              if (this.taskTempActiveName === 'access' || this.taskTempActiveName2 === 'access' || taskTemp.isChecked === 1) {
                  this.taskTempAble = true
              }else {
                  this.taskTempAble = false
              }
              var first = taskTemp.beginTime;
              var second = taskTemp.endTime;
                this.taskTempDetail.firstTime = first;
                this.taskTempDetail.secondTime = second;

              this.taskTempDetail.id = taskTemp.id;
              this.taskTempDetail.taskId = taskTemp.taskId;
              this.taskTempDetail.taskName = taskTemp.taskName;
              this.taskTempDetail.userName = taskTemp.userName;
              this.taskTempDetail.userId = taskTemp.userId;
              this.taskTempDetail.beginTime = taskTemp.beginTime;
              this.taskTempDetail.endTime = taskTemp.endTime;
              this.taskTempDetail.createTime = taskTemp.createTime;
              this.taskTempDetail.description = taskTemp.description;
              this.taskTempDetail.isChecked = taskTemp.isChecked;
              this.taskTempDetail.taskLevel = taskTemp.taskLevel;
              this.taskTempDetail.taskLevelName = taskTemp.taskLevelName;
              this.taskTempDetail.functionResDTOList = taskTemp.functionResDTOList;
              this.description = taskTemp.description;
              // this.taskTempDetail.reviewStatus = taskTemp.reviewStatus;
              this.taskTempDetail.workHours = taskTemp.workHours;
              this.taskTempDetail.reviewStatus = taskTemp.reviewStatus;
              this.taskTempDetail.userWeeks = taskTemp.userWeekTempList;
              this.taskTempDetail.taskReviewLogResDTOList = taskTemp.taskReviewLogResDTOList;
                this.taskFunctionList = [];
                this.functionLevelList = [];
              if (this.taskTempDetail.functionResDTOList !== undefined && this.taskTempDetail.functionResDTOList.length>0){
                  this.taskTempDetail.functionResDTOList.forEach(resDTO=>{
                      this.taskFunctionList.push(resDTO.functionId);
                      this.functionLevelList.push(resDTO.level);
                      this.num = this.taskTempDetail.functionResDTOList.length;
                  })
              }
                this.changeTaskTempWeek();
                if (this.taskDetail){
                    this.taskTempDetailVisible = true;
                }
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
                this.helpForm.userId = help.userId;
                this.helpForm.username = help.username;
                this.editHelpVisible = true;
                this.helpDetailVisible = false;
            },
            leaveDetail(leave) {
                this.leaveDetailVisible = true;
                this.leaveForm.id = leave.id;
                this.leaveForm.userId = leave.userId;
                this.leaveForm.userName = leave.userName;
                this.leaveForm.description = leave.description;
                this.leaveForm.beginTime = moment(leave.beginTime).toDate();
                this.leaveForm.endTime = moment(leave.endTime).toDate();
                this.leaveForm.typeName = leave.typeName;
                this.leaveForm.type = leave.type;
                this.leaveForm.hours = leave.hours;
                this.userWeeks = leave.userWeeks
            },
            editLeaveDetail(leave, index) {
                if (this.userRole === 0 && index !== 1) {
                    this.leaveDetail(leave)
                } else {
                    this.editLeaveDetailVisible = true;
                    this.leaveDetailVisible = false;
                    this.editLeaveVisible = true;
                    this.leaveForm.id = leave.id;
                    this.leaveForm.userId = leave.userId;
                    this.leaveForm.userName = leave.userName;
                    this.leaveForm.description = leave.description;
                    this.leaveForm.beginTime = moment(leave.beginTime).toDate();
                    this.leaveForm.endTime = moment(leave.endTime).toDate();
                    this.leaveForm.type = leave.type;
                    this.leaveForm.hours = leave.hours;
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
                this.leaveForm.id = this.leaveForm.endTime = this.leaveForm.beginTime = this.leaveForm.hours = this.leaveForm.type = this.leaveForm.description = '';
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
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (this.leaveForm.type === '' || this.leaveForm.type == null) {
                            this.$message({showClose: true, message: '请选择请假类型', type: 'warning'});
                            return false;
                        }
                        // let ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.leaveForm.hours);
                        let ishours = /^[1-9]\d*$/.test(this.leaveForm.hours);
                        if (!ishours) {
                            this.$message({showClose: true, message: '请假时长为正整数', type: 'error'});
                            return false;
                        }
                        if (this.leaveForm.hours > 99999 || this.leaveForm.hours < 1) {
                            this.$message({showClose: true, message: '请假时长正确值应为1~99999', type: 'error'});
                            return false;
                        }
                        this.weekTime.beginWeek = moment(this.leaveForm.beginTime).week();
                        this.weekTime.endWeek = moment(this.leaveForm.endTime).week();
                        this.leaveForm.beginTime = moment(this.leaveForm.beginTime).toDate();
                        this.leaveForm.endTime = moment(this.leaveForm.endTime).toDate();

                        let form = this.leaveForm;
                        form.beginTime = moment(form.beginTime).format('YYYY-MM-DD HH:mm:00');
                        form.endTime = moment(form.endTime).format('YYYY-MM-DD HH:mm:00');
                        if (this.weekTime.beginWeek !== this.weekTime.endWeek || moment(form.beginTime).isAfter(moment(form.endTime)) || moment(form.beginTime).isSame(moment(form.endTime))) {
                            this.$message({showClose: true, message: '请假日期有误,请检查(请假时间不能跨周、相同)', type: 'warning'});
                            return false;
                        }
                        form['userWeeks'] = this.userWeeks;
                        this.isSaving = true;
                        if (form.id !== '') {
                            http.zsyPostHttp('/userLeave/editLeaveDetail/' + form.id, form, (resp) => {
                                this.$message({
                                    showClose: true,
                                    message: '请假申请修改成功',
                                    type: 'success'
                                });
                                this.fetchUserLeaveList();
                                this.clearLeaveForm();
                                this.editLeaveVisible = false;
                                this.editLeaveDetailVisible = false;
                                this.isSaving = false
                            }, err => {
                                this.isSaving = false;
                                this.$message({
                                    showClose: true,
                                    message: err.errMsg,
                                    type: 'error'
                                });
                            })
                        } else {
                            http.zsyPostHttp('/userLeave/add', form, (resp) => {
                                this.$message({
                                    showClose: true,
                                    message: '新建请假申请成功',
                                    type: 'success'
                                });
                                this.fetchUserLeaveList();
                                this.clearLeaveForm();
                                this.editLeaveVisible = false;
                                this.editLeaveDetailVisible = false;
                                this.isSaving = false
                            }, err => {
                                this.$message({
                                    showClose: true,
                                    message: err.errMsg,
                                    type: 'error'
                                });
                                this.isSaving = false
                            })
                        }
                    }

                })
            },
            //获取两个日期间隔天数
            getDiff(s1,s2) {
                var days = s2.getTime() - s1.getTime();
                var time = parseInt(days / (1000 * 60 * 60 * 24));
                return time;
            },
            saveExtraWork(formName) {
                this.isSaving = true;
                if (this.extraWorkForm.reason == null || this.extraWorkForm.reason === ''){
                    this.$message({showClose: true, message: '加班原因不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.beginTime == null || this.extraWorkForm.beginTime === ''){
                    this.$message({showClose: true, message: '加班开始时间不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.endTime == null || this.extraWorkForm.endTime === ''){
                    this.$message({showClose: true, message: '加班结束时间不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (moment(this.extraWorkForm.endTime).isBefore(moment(this.extraWorkForm.beginTime))){
                    this.$message({showClose: true, message: '加班结束时间不能在开始时间之前', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                let beginDayStr = String(moment(this.extraWorkForm.beginTime).format('YYYY-MM-DD HH:mm:ss'))
                let endDayStr = String(moment(this.extraWorkForm.endTime).format('YYYY-MM-DD HH:mm:ss'))
                const days = this.getDiff(new Date(beginDayStr.substring(0,10)+" 00:00:00")
                    ,new Date(endDayStr.substring(0,10)+" 23:59:59"));
                if (days > 0){
                    this.$message({showClose: true, message: '加班申请不能跨天', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.workHours == null || this.extraWorkForm.workHours === ''){
                    this.$message({showClose: true, message: '加班时长不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.taskIds.length === 0){
                    this.$message({showClose: true, message: '关联任务不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.extraWorkForm.workHours);
                        if (!ishours) {
                            this.$message({showClose: true, message: '加班时长填写错误', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        if (this.extraWorkForm.workHours > 99999.9 || this.extraWorkForm.workHours < 0) {
                            this.$message({showClose: true, message: '加班时长正确值应为0~99999.9', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        this.extraWorkForm.beginTime = moment(this.extraWorkForm.beginTime).toDate();
                        this.extraWorkForm.endTime = moment(this.extraWorkForm.endTime).toDate();

                        let form = this.extraWorkForm;
                        form.beginTime = moment(form.beginTime).format('YYYY-MM-DD HH:mm:00');
                        form.endTime = moment(form.endTime).format('YYYY-MM-DD HH:mm:00');
                            http.zsyPostHttp('/extra-work/add', form, (resp) => {
                                this.$message({
                                    showClose: true,
                                    message: '新建加班申请成功',
                                    type: 'success'
                                });
                                this.clearExtraWorkForm();
                                this.fetchMyRunningExtraWork();
                                this.createExtraWorkVisible = false;
                                this.isSaving = false
                            }, err => {
                                this.isSaving = false
                            })
                        }

                })
            },
            saveEditExtraWork(formName,ewId) {
                this.isSaving = true;
                if (this.extraWorkForm.reason == null || this.extraWorkForm.reason === ''){
                    this.$message({showClose: true, message: '加班原因不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.beginTime == null || this.extraWorkForm.beginTime === ''){
                    this.$message({showClose: true, message: '加班开始时间不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.endTime == null || this.extraWorkForm.endTime === ''){
                    this.$message({showClose: true, message: '加班结束时间不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (moment(this.extraWorkForm.endTime).isBefore(moment(this.extraWorkForm.beginTime))){
                    this.$message({showClose: true, message: '加班结束时间不能在开始时间之前', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                let day1 = new Date(this.extraWorkForm.beginTime).getDay();
                let day2 = new Date(this.extraWorkForm.endTime).getDay();
                if (day2 - day1 > 0){
                    this.$message({showClose: true, message: '加班申请不能跨天', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.workHours == null || this.extraWorkForm.workHours === ''){
                    this.$message({showClose: true, message: '加班时长不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.extraWorkForm.taskIds.length === 0){
                    this.$message({showClose: true, message: '关联任务不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.extraWorkForm.workHours);
                        if (!ishours) {
                            this.$message({showClose: true, message: '加班时长填写错误', type: 'error'});
                            return false;
                        }
                        if (this.extraWorkForm.workHours > 99999.9 || this.extraWorkForm.workHours < 0) {
                            this.$message({showClose: true, message: '加班时长正确值应为0~99999.9', type: 'error'});
                            return false;
                        }
                        this.extraWorkForm.beginTime = moment(this.extraWorkForm.beginTime).toDate();
                        this.extraWorkForm.endTime = moment(this.extraWorkForm.endTime).toDate();

                        let form = this.extraWorkForm;
                        form.beginTime = moment(form.beginTime).format('YYYY-MM-DD HH:00:00');
                        form.endTime = moment(form.endTime).format('YYYY-MM-DD HH:00:00');
                            http.zsyPutHttp('/extra-work/update/'+ewId, form, (resp) => {
                                this.$message({
                                    showClose: true,
                                    message: '修改加班申请成功',
                                    type: 'success'
                                });
                                this.clearExtraWorkForm();
                                this.fetchMyRunningExtraWork();
                                this.fetchCheckingExtraWork();
                                this.editEWorkVisible = false;
                                this.isSaving = false
                            }, err => {
                                this.isSaving = false
                            })
                        }

                })
            },

            closeAddExtraWork(){
                this.clearExtraWorkForm();
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
                    this.clearLeaveForm();
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
                    this.clearLeaveForm();
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
                if (this.projectList == null || this.projectList === undefined || this.projectList.length === 0){
                    let vm = this;
                    http.zsyGetHttp('/project/list', {}, (resp) => {
                        vm.projectList = resp.data
                    })
                }
            },
            fetchStageList() {
                if (this.stageList == null || this.stageList === undefined || this.stageList.length === 0){
                    let vm = this;
                    http.zsyGetHttp('/stage/list', {}, (resp) => {
                        vm.stageList = resp.data
                    })
                }
            },
            fetchUserList() {
                let vm = this;
                http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.userList = resp.data
                })
            },
            fetchTagList() {
                if (this.tagList == null || this.tagList === undefined || this.tagList.length === 0){
                    let vm = this;
                    http.zsyGetHttp('/tag/list', {}, (resp) => {
                        vm.tagList = resp.data
                    })
                }
            },
            handleFinishedPage(currentPage) {
                this.finishedPage.pageNum = currentPage;
                this.fetchTaskFinished()
            },
            handleFinishedPage1(currentPage) {
                this.finishedPage1.pageNum = currentPage;
                this.fetchQuestionCompleted()
            },
            handleFinishedPage2(currentPage) {
                this.acceptedPage.pageNum = currentPage;
                this.fetchQuestionAccepted()
            },
            handleFinishedPage3(currentPage) {
                this.waitPage1.pageNum = currentPage;
                this.fetchQuestionWait()
            },
            handleCommentedPage(currentPage) {
                this.commentedPage.pageNum = currentPage;
                this.fetchTaskCommented()
            },
            handleEvaluatedPage(currentPage) {
                this.commentedPage.pageNum = currentPage;
                this.fetchEvaluated()
            },
            handleAuditSuccessPage(currentPage) {
                this.auditSuccessPage.pageNum = currentPage;
                this.fetchTaskAuditSuccess()
            },
            handleWaitAuditPage(currentPage) {
                this.waitAuditPage.pageNum = currentPage;
                this.fetchTaskWaitAudit()
            },
            handleWaitAuditTaskTempPage(currentPage) {
                this.taskTempPage.pageNum = currentPage;
                this.fetchPersonalMultipleWait()
            },
            handleWaitAuditTaskTempPage2(currentPage) {
                this.taskTempPage3.pageNum = currentPage;
                this.fetchMultipleWait()
            },
            handleAuditSuccessTaskTempPage(currentPage) {
                this.taskTempPage2.pageNum = currentPage;
                this.fetchPersonalMultipleAccess()
            },
            handleAuditSuccessTaskTempPage2(currentPage) {
                this.taskTempPage4.pageNum = currentPage;
                this.fetchMultipleAccess()
            },
            handleExpandPage(currentPage) {
                this.expandPage.pageNum = currentPage;
               this.fetchTaskExpandSuccess()
            },
            handlePersonalTaskModifyPage(currentPage) {
                this.personalTaskModifyPage.pageNum = currentPage;
               this.fetchPersonalTaskModifyAccessed()
            },
            handleTaskModifyPage(currentPage) {
                this.taskModifyPage.pageNum = currentPage;
                this.fetchTaskModifyAccessed()
            },
            handleExpandDoingPage(currentPage) {
                this.expandDoingPage.pageNum = currentPage;
                this.fetchTaskExpandDoing()
            },
            isDecimal(str) {
                let regu = /^[-]{0,1}[0-9]{1,}$/;
                if (regu.test(str)) {
                    return true;
                }
                let re = /^[-]{0,1}(\d+)[\.]+(\d+)$/;
                if (re.test(str)) {
                    if (RegExp.$1 === 0 && RegExp.$2 === 0) return false;
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

                let startMonth = 0;
                let nowDayOfWeek = now.getDay();
                nowDayOfWeek = nowDayOfWeek === 0 ? 7 : nowDayOfWeek;
                if (date === "month") {//本月的开始结束时间
                    return moment(new Date(curYear, curMonth, 1)).format('YYYY-MM-DD') + "--" + moment(new Date(curYear, curMonth + 1, 1) - 1).format('YYYY-MM-DD');
                } else if (date === "week") {//本季度的开始结束时间
                    return moment(new Date(curYear, curMonth, now.getDate() - nowDayOfWeek + 1)).format('YYYY-MM-DD') + "--" + moment(new Date(curYear, curMonth, now.getDate() + (6 - nowDayOfWeek) + 1)).format('YYYY-MM-DD');
                } else if (date === "year") {//本年的开始结束时间
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
                this.expandDetail.beginWeek = moment(this.expandDetail.beginTime).week();
                this.expandDetail.endWeek = moment(this.expandDetail.endTime).week();
                let beginYear = moment(this.expandDetail.beginTime).year();
                let endYear = moment(this.expandDetail.endTime).year();
                if (beginYear !== endYear) {
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
                if (this.expandDetail.beginWeek === this.expandDetail.endWeek) {
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
                } else if (this.expandDetail.endWeek - this.expandDetail.beginWeek === 1) {
                    param.push({
                        'weekNumber': this.expandDetail.beginWeek,
                        'hours': '',
                        'year': beginYear,
                        'range': moment().year(beginYear).week(this.expandDetail.beginWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.expandDetail.beginWeek).endOf('week').format('MM-DD')
                    });
                    param.push({
                        'weekNumber': this.expandDetail.endWeek,
                        'hours': '',
                        'year': beginYear,
                        'range': moment().year(beginYear).week(this.expandDetail.endWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.expandDetail.endWeek).endOf('week').format('MM-DD')
                    })
                }
                this.expandWeekNumber = param
            },

            changeTime(){
                if(this.taskTempDetail.beginTime instanceof Date){
                    this.taskTempDetail.beginTime = this.taskTempDetail.beginTime.getTime()
                }
                if(this.taskTempDetail.endTime instanceof Date){
                    this.taskTempDetail.endTime = this.taskTempDetail.endTime.getTime()
                }
                // console.log("first: "+ this.taskTempDetail.firstTime);
                // console.log("begin: "+ this.taskTempDetail.beginTime);
                // console.log(this.taskTempDetail.firstTime === this.taskTempDetail.beginTime &&
                //     this.taskTempDetail.secondTime === this.taskTempDetail.endTime);

                if (this.taskTempDetail.firstTime === this.taskTempDetail.beginTime &&
                    this.taskTempDetail.secondTime === this.taskTempDetail.endTime) {
                    return ;
                }
                this.taskTempDetail.firstTime = this.taskTempDetail.beginTime;
                this.taskTempDetail.secondTime = this.taskTempDetail.endTime;

                if(this.taskTempDetail.beginTime != null && this.taskTempDetail.beginTime !== ''
                    && this.taskTempDetail.endTime != null&& this.taskTempDetail.endTime !== ''){
                    let userWeeks = [];
                    let beginYear = moment(this.taskTempDetail.beginTime).year();
                    let endYear = moment(this.taskTempDetail.endTime).year();
                    this.taskTempDetail.beginWeek = moment(this.taskTempDetail.beginTime).week();
                    this.taskTempDetail.endWeek = moment(this.taskTempDetail.endTime).week();
                    if ((moment(this.taskTempDetail.beginTime).isAfter(moment(this.taskTempDetail.endTime)))){
                        this.$message({ showClose: true,message: '开始时间不可在截止时间后面',type: 'error'});
                    }
                    if (beginYear !== endYear){
                        if (this.taskTempDetail.beginWeek > this.taskTempDetail.endWeek){
                            for(let i=this.taskTempDetail.beginWeek;i<moment(this.taskTempDetail.beginTime).weeksInYear()+1;i++){
                                let weekData = {
                                    'weekNumber':i,
                                    'hours': '',
                                    'year':beginYear ,
                                    'weekHours': 0,
                                    'range':moment().year(beginYear).week(i).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(i).endOf('week').format('MM-DD') };
                                userWeeks.push(weekData)
                            }
                            for(let i=1;i<this.taskTempDetail.endWeek+1;i++){
                                let weekData = {
                                    'weekNumber':i,
                                    'hours': '',
                                    'year':endYear ,
                                    'weekHours': 0,
                                    'range':moment().year(endYear).week(i).startOf('week').format('MM-DD')+'至'+moment().year(endYear).week(i).endOf('week').format('MM-DD') };
                                userWeeks.push(weekData)

                            }
                        } else {
                            for(let i = 0;i<=this.taskTempDetail.endWeek - this.taskTempDetail.beginWeek;i++){
                                let weekData = {
                                    'weekHours':0,
                                    'weekNumber': i + this.taskTempDetail.beginWeek,
                                    'hours': 0,
                                    'year': endYear,
                                    'range': moment().year(beginYear).week(i + this.taskTempDetail.beginWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(i + this.taskTempDetail.beginWeek).endOf('week').format('MM-DD')
                                };
                                userWeeks.push(weekData)

                            }
                        }
                    }
                    else {
                        //同年份,且周也是属于同年份
                        if (this.taskTempDetail.endWeek >= this.taskTempDetail.beginWeek) {
                            for(let i = 0;i<=this.taskTempDetail.endWeek - this.taskTempDetail.beginWeek;i++){
                                let weekData = {
                                    'weekHours':0,
                                    'weekNumber': i + this.taskTempDetail.beginWeek,
                                    'hours': 0,
                                    'year': endYear,
                                    'range': moment().year(beginYear).week(i + this.taskTempDetail.beginWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(i + this.taskTempDetail.beginWeek).endOf('week').format('MM-DD')
                                };
                                userWeeks.push(weekData)

                            }
                        }
                        //同年份,但是有的周属于上一年,有的属于下一年
                        else {
                            for(let i=this.taskTempDetail.beginWeek;i<moment(this.taskTempDetail.beginTime).weeksInYear()+1;i++){
                                let weekData = {
                                    'weekNumber':i,
                                    'hours': '',
                                    'year':beginYear ,
                                    'weekHours': 0,
                                    'range':moment().year(beginYear).week(i).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(i).endOf('week').format('MM-DD') };
                                userWeeks.push(weekData)
                            }
                            for(let i=1;i<this.taskTempDetail.endWeek+1;i++){
                                let year = endYear + 1;
                                let weekData = {
                                    'weekNumber':i,
                                    'hours': '',
                                    'year':year ,
                                    'weekHours': 0,
                                    'range':moment().year(year).week(i).startOf('week').format('MM-DD')+'至'+moment().year(year).week(i).endOf('week').format('MM-DD') };
                                userWeeks.push(weekData)

                            }
                        }
                    }
                        this.taskTempDetail.userWeeks = userWeeks;
                        this.changeTaskTempWeek();
                    }




            },
            changeTaskTempWeek(){
                if (this.taskTempDetail.beginTime == null || this.taskTempDetail.endTime == null) {
                    return
                }
                this.taskTempWeekNumber = [];
                let weekData = '';
                let param = this.taskTempWeekNumber;
                this.taskTempDetail.beginWeek = moment(this.taskTempDetail.beginTime).week();
                this.taskTempDetail.endWeek = moment(this.taskTempDetail.endTime).week();
                let beginYear = moment(this.taskTempDetail.beginTime).year();
                let endYear = moment(this.taskTempDetail.endTime).year();
                let userWeeks = this.taskTempDetail.userWeeks;
                if (beginYear !== endYear) {
                    // for (let i = this.taskTempDetail.beginWeek; i < moment(this.taskTempDetail.beginTime).weeksInYear() + 1; i++) {
                    for (let i = 0; i < userWeeks.length; i++) {
                        let year = userWeeks[i].year;
                        let week = userWeeks[i].weekNumber;
                        http.zsyGetHttp('/userWeek/'+ this.taskTempDetail.taskId +'/' + this.taskTempDetail.userId + '/' + year + '/' + week, {}, (resp) => {
                            weekData = {
                                'weekHours':resp.data,
                                'weekNumber': userWeeks[i].weekNumber,
                                // 'weekNumber': i,
                                // 'hours': userWeeks[i-this.taskTempDetail.beginWeek].hours,
                                'hours': userWeeks[i].hours,
                                'year': year,
                                'range': moment().year(year).week(week).startOf('week').format('MM-DD') + '至' + moment().year(year).week(week).endOf('week').format('MM-DD')
                            };
                            param.push(weekData)
                        })
                    }
                    // for (let i = 1; i < this.taskTempDetail.endWeek + 1; i++) {
                    //     http.zsyGetHttp('/userWeek/'+ this.taskTempDetail.taskId +'/' + this.taskTempDetail.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                    //         weekData = {
                    //             'weekHours':resp.data,
                    //             'weekNumber': i,
                    //             'hours': userWeeks[i-1].hours,
                    //             'year': endYear,
                    //             'range': moment().year(endYear).week(i).startOf('week').format('MM-DD') + '至' + moment().year(endYear).week(i).endOf('week').format('MM-DD')
                    //         };
                    //         param.push(weekData)
                    //     })
                    //
                    // }
                }
                else {
                    if (this.taskTempDetail.beginWeek === this.taskTempDetail.endWeek) {
                        http.zsyGetHttp('/userWeek/'+ this.taskTempDetail.taskId +'/' + this.taskTempDetail.userId + '/' + beginYear + '/' +this.taskTempDetail.beginWeek , {}, (resp) => {
                            weekData = {
                                'weekHours':resp.data,
                                'weekNumber': this.taskTempDetail.beginWeek,
                                'hours': this.taskTempDetail.workHours,
                                'year': beginYear,
                                'range': moment().year(beginYear).week(this.taskTempDetail.beginWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.taskTempDetail.beginWeek).endOf('week').format('MM-DD')
                            };
                            param.push(weekData)
                        })

                    }
                    else if (this.taskTempDetail.endWeek - this.taskTempDetail.beginWeek > 1) {
                        for (let i = this.taskTempDetail.beginWeek; i < this.taskTempDetail.endWeek + 1; i++) {
                            http.zsyGetHttp('/userWeek/'+ this.taskTempDetail.taskId +'/' + this.taskTempDetail.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                                weekData = {
                                    'weekHours':resp.data,
                                    'weekNumber': i,
                                    'hours': userWeeks[i-this.taskTempDetail.beginWeek].hours,
                                    'year': beginYear,
                                    'range': moment().week(i).year(beginYear).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(i).endOf('week').format('MM-DD')
                                };
                                param.push(weekData)
                            })
                        }
                    }
                    else if (this.taskTempDetail.endWeek - this.taskTempDetail.beginWeek === 1) {

                        http.zsyGetHttp('/userWeek/'+ this.taskTempDetail.taskId +'/' + this.taskTempDetail.userId + '/' + beginYear + '/' + this.taskTempDetail.beginWeek, {}, (resp) => {
                            param.push({
                                'weekHours':resp.data,
                                'weekNumber': this.taskTempDetail.beginWeek,
                                'hours': userWeeks[0].hours,
                                'year': beginYear,
                                'range': moment().year(beginYear).week(this.taskTempDetail.beginWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.taskTempDetail.beginWeek).endOf('week').format('MM-DD')
                            })
                        });
                        http.zsyGetHttp('/userWeek/'+ this.taskTempDetail.taskId +'/' + this.taskTempDetail.userId + '/' + beginYear + '/' + this.taskTempDetail.endWeek, {}, (resp) => {
                            param.push({
                                'weekHours':resp.data,
                                'weekNumber': this.taskTempDetail.endWeek,
                                'hours': userWeeks[1].hours,
                                'year': beginYear,
                                'range': moment().year(beginYear).week(this.taskTempDetail.endWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.taskTempDetail.endWeek).endOf('week').format('MM-DD')
                            })
                        })

                    }
                    else if (this.taskTempDetail.endWeek ===1 && this.taskTempDetail.beginWeek > 1){
                        for (let i = 0; i < userWeeks.length; i++) {
                            let year = userWeeks[i].year;
                            let week = userWeeks[i].weekNumber;
                            http.zsyGetHttp('/userWeek/'+ this.taskTempDetail.taskId +'/' + this.taskTempDetail.userId + '/' + year + '/' + week, {}, (resp) => {
                                weekData = {
                                    'weekHours':resp.data,
                                    'weekNumber': userWeeks[i].weekNumber,
                                    // 'weekNumber': i,
                                    // 'hours': userWeeks[i-this.taskTempDetail.beginWeek].hours,
                                    'hours': userWeeks[i].hours,
                                    'year': year,
                                    'range': moment().year(year).week(week).startOf('week').format('MM-DD') + '至' + moment().year(year).week(week).endOf('week').format('MM-DD')
                                };
                                param.push(weekData)
                            })
                        }
                    }
                }

                this.taskTempWeekNumber = param
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
                this.clearQuestionForm();
                this.createQuestionVisible = true
            },
            //完成线上问题
            finishQuestion(item){
                this.finishQuestionVisible = true;
                this.questionForm.oqrId = item.oqrId;
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
                // this.fetchQuestionDoing()
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
                this.acceptedQuestionVisible = false;
            },
            //审核问题
            checkQuestion(item){
                this.fetchQuestionWait();
                this.checkQuestionVisible = true;
                this.questionForm.oqrId = item.oqrId;
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
                this.acceptedQuestionVisible = true;
                this.questionForm.oqrId = item.oqrId;
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
                this.questionAble = true;
                this.questionForm.endTime = moment(this.questionForm.endTime).toDate();
                this.questionForm.beginTime = moment(this.questionForm.beginTime).toDate();
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let param = this.questionForm;
                        param.name = param.name.trim();
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD HH:00:00');
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:59');
                        if (param.workHour.length !== parseFloat(param.workHour).toString().length || parseFloat(param.workHour) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            this.questionAble = false;
                            return false;
                        }
                        http.zsyPostHttp('/question/add', param, (resp) => {
                            this.$message({showClose: true, message: '线上问题创建成功', type: 'success'});
                            this.$refs[formName].resetFields();
                            this.createQuestionVisible = false;
                            this.questionAble = false;
                            vm.reload()
                        });
                    } else {
                        this.questionAble = false;
                        return false;
                    }
                }, err => {
                    this.questionAble = false
                },error=>{
                    this.questionAble = false
                });
            },
            //修改线上问题
            editQuestionInfo(formName) {
                let vm = this;
                this.questionAble = true;
                this.questionForm.endTime = moment(this.questionForm.endTime).toDate();
                this.questionForm.beginTime = moment(this.questionForm.beginTime).toDate();
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let param = this.questionForm;
                        param.name = param.name.trim();
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD HH:00:00');
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:59');
                        param.oqrId = this.questionForm.oqrId;
                        if (param.workHour.length !== parseFloat(param.workHour).toString().length || parseFloat(param.workHour) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            this.questionAble = false;
                            return false;
                        }
                        http.zsyPutHttp('/question/update', param, (resp) => {
                            this.$message({showClose: true, message: '线上问题修改成功', type: 'success'});
                            this.$refs[formName].resetFields();
                            this.editQuestionVisible = false;
                            this.questionAble = false;
                            vm.reload()
                        });
                    } else {
                        this.questionAble = false;
                        return false;
                    }
                }, err => {
                    this.questionAble = false
                },error=>{
                    this.questionAble = false;
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
                let data = new FormData();
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
                let vm = this;
                http.zsyGetHttp(`/question/wait/${vm.waitPage1.pageNum}`, {}, (res) => {
                    vm.question.wait = res.data.list;
                    vm.waitPage1.total = res.data.total
                })
            },
            fetchQuestionAccepted(){
                let vm = this;
                http.zsyGetHttp(`/question/accepted/${vm.acceptedPage.pageNum}`, {}, (res) => {
                    vm.question.accepted = res.data.list;
                    vm.acceptedPage.total = res.data.total
                })
            },
            //查询进行中线上问题
            fetchQuestionDoing() {
                let vm = this;
                http.zsyGetHttp('/question/running', {}, (res) => {
                    vm.question.running = res.data
                })
            },
            //查询已完成线上问题
            fetchQuestionCompleted(){
                let vm = this;
                http.zsyGetHttp(`/question/completed/${vm.finishedPage1.pageNum}`, {}, (res) => {
                    vm.question.completed = res.data.list;
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
                            this.checkQuestionVisible = false;
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
                            this.fetchQuestionWait();
                            this.fetchQuestionAccepted()
                        }
                    });
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
                          this.fetchQuestionCompleted();
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
                this.clearExtraWorkForm();
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
                if (this.userRole === 0){
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
                if (this.userRole === 0){
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
                if (taskId != null && taskId !== '') {
                    this.$router.push({name: 'taskListFormComments', params: {taskId: taskId}})
                }
            },
            //编辑加班申请
            editEWorkForm(extraWorkDetail){
                this.fetchMyRunningTasks(extraWorkDetail.userId);
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
                let data = new FormData();
                data.append('uploadFile', file.file);
                http.zsyPostHttp('/sign-in/upload/sign-in/repository',data,(res)=>{
                    this.$refs.record.clearFiles();
                    if (res.errMsg === "执行成功"){
                        this.fullscreenLoading = false;
                        this.$message({
                            showClose: true,
                            message: '导入成功',
                            type: 'success'
                        });
                    }else {
                        this.uploadToMysqlVisible = false;
                        this.fullscreenLoading = false;
                    }
                },(fail)=>{
                    this.$message({
                        showClose: true,
                        message: fail.errMsg,
                        type: 'error'
                    });
                    this.fullscreenLoading = false;
                })
            },
            closeSignInDialog(){
                this.$refs.record.clearFiles();
            },
            uploadUserSortToMysql(file){
                let data = new FormData();
                data.append('uploadFile', file.file);
                http.zsyPostHttp('/sign-in/upload/user-sort/repository',data,(res)=>{
                    this.uploadUserSortToMysqlVisible = false;
                    this.$refs.usersort.clearFiles();
                    if (res.errMsg === "执行成功"){
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
                // let suffix = file.name.substring(file.name.lastIndexOf(".")+1);
                const isLt2M = file.size / 1024 / 1024 < 2;
                // const isDat = file.name.substring(file.name.lastIndexOf(".")+1) === "dat";
                // if (suffix !== "dat"){
                //     this.$message.error('上传文件只能是".dat"格式!');
                // }
                if (!isLt2M) {
                    this.$message.error('上传文件大小不能超过 2MB!');
                }
                return isLt2M;
                // return isDat && isLt2M;
            },
            beforeUserSortUpload(file) {
                let suffix = file.name.substring(file.name.lastIndexOf(".")+1);
                const isXls = file.name.substring(file.name.lastIndexOf(".")+1) === "xls";
                const isExcel = file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
                const isLt2M = file.size / 1024 / 1024 < 1;
                if ("xls" !== suffix) {
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
                time = time.split(' - ');
                if (time && time.length === 2) {
                    this.signInReqDTO.beginTime = `${time[0]} 00:00:00`;
                    this.signInReqDTO.endTime = `${time[1]} 23:59:59`
                } else {
                    this.signInReqDTO.beginTime = this.signInReqDTO.endTime = this.signInDaterange = ''
                }
            },
            mySignInTimeChange(time) {
                // 选择结束时间
                time = time.split(' - ');
                if (time && time.length === 2) {
                    this.mySignInReqDTO.beginTime = `${time[0]} 00:00:00`;
                    this.mySignInReqDTO.endTime = `${time[1]} 23:59:59`
                } else {
                    this.mySignInReqDTO.beginTime = this.mySignInReqDTO.endTime = this.mySignInDaterange = ''
                }
            },
            //查询考勤情况
            fetchSignInData(){
                if (this.userRole === 3 ){
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
                                let checkTimeStr = '';
                                signIn.checkTimeList.forEach(checkTime => {
                                    checkTimeStr = checkTimeStr + moment(checkTime).format("HH:mm:ss") + ', '
                                });
                                signIn.checkTimeList = checkTimeStr.substring(0,checkTimeStr.length-2)
                            })
                        }
                    })
                }
            },
            //查询任务
            fetchAllMultipleTasks(){
              http.zsyGetHttp('task/multiple/all',{},(res=>{
                  if (res){
                      this.taskList = res.data;
                  }
              }))
            },
            initSignInTime(){
                let date1 = new Date();
                let time1=date1.getFullYear()+"-"+(date1.getMonth()+1)+"-"+date1.getDate();//time1表示当前时间
                let date2 = new Date(date1);
                let date3 = new Date(date1);
                date3.setDate(date1.getDate() - 1);
                date2.setDate(date1.getDate() - 7);
                let time2 = date2.getFullYear()+"-"+(date2.getMonth()+1)+"-"+date2.getDate();
                let time3 = date3.getFullYear()+"-"+(date3.getMonth()+1)+"-"+date3.getDate();
                if (this.userRole === 3){
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
                if (this.mySignInReqDTO.beginTime != null && this.mySignInReqDTO.beginTime !== ''){
                    this.mySignInReqDTO.beginTime = moment(this.mySignInReqDTO.beginTime).format('YYYY-MM-DD 00:00:00');
                }
                if (this.mySignInReqDTO.endTime != null && this.mySignInReqDTO.endTime !== '') {
                    this.mySignInReqDTO.endTime = moment(this.mySignInReqDTO.endTime).format('YYYY-MM-DD 23:59:59');
                }
                this.fetchMySignInData()
            },
            selectSignInData(){
                if (this.signInReqDTO.beginTime != null && this.signInReqDTO.beginTime !== ''){
                    this.signInReqDTO.beginTime = moment(this.signInReqDTO.beginTime).format('YYYY-MM-DD 00:00:00');
                }
                if (this.signInReqDTO.endTime != null && this.signInReqDTO.endTime !== '') {
                    this.signInReqDTO.endTime = moment(this.signInReqDTO.endTime).format('YYYY-MM-DD 23:59:59');
                }

                this.fetchSignInData()
            },
            fetchMyTotalEWorkTime(){
                if (this.workMonth1 != null && this.workMonth1 !== ''){
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
              if (this.workMonth3 == null || this.workMonth3 === ''){
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
                          window.open(res.data);
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
                if (this.eWorkTimeUserId == null || this.eWorkTimeUserId === ''){
                    this.$message({
                        showClose: true,
                        message: '请选择查询的用户',
                        type: 'warning'
                    });
                    return false;
                }
                if (this.workMonth2 != null && this.workMonth2 !== ''){
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
            closeRestHoursDialog(){
                this.restHours = 0;
                this.restHoursUserId = '';
                this.restHoursUserName = '';
            },
            fetchUserRestHours(){
              if (this.restHoursUserId != null && this.restHoursUserId !== undefined && this.restHoursUserId !==  ''){
                  http.zsyGetHttp('/user/'+this.restHoursUserId,{},(res)=>{
                     this.restHoursUserName = res.data.name;
                     this.restHours = res.data.restHours;
                  });
              }
            },
            //管理员重置用户调休时间
            modifyUserRestHours(){
                this.restHourLoading = true;
              if (this.restHoursUserId == null || this.restHoursUserId === undefined || this.restHoursUserId === ''){
                  this.$message({
                      showClose: true,
                      message: '请选择要修改的用户',
                      type: 'warning'
                  });
                  this.restHourLoading = false;
                  return false;
              }
              if (this.restHours == null || this.restHours === undefined){
                  this.$message({
                      showClose: true,
                      message: '请填写调休时长',
                      type: 'warning'
                  });
                  this.restHourLoading = false;
                  return false;
              }
                if (this.restHours > 9999 || this.restHours < 1) {
                    this.$message({showClose: true, message: '调休时长正确值应为1~9999', type: 'error'});
                    this.restHourLoading = false;
                    return false;
                }

                let reqDTO = {
                    userId:this.restHoursUserId,
                    restHours:this.restHours
                };
                http.zsyPostHttp('/user/rest-hours/update',reqDTO,res=>{
                    if (res.errMsg == "执行成功"){
                        this.$message({
                            showClose: true,
                            message: '修改成功',
                            type: 'success'
                        });
                        this.restHourLoading = false;
                        // this.modifyUserRestHoursVisible = false;
                    }else {
                        this.restHourLoading = false;
                        // this.modifyUserRestHoursVisible = false;
                    }
                },err=>{

                },errrr=>{
                    this.restHourLoading = false;
                })
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
                                let checkTimeStr = '';
                                signIn.checkTimeList.forEach(checkTime => {
                                    checkTimeStr = checkTimeStr + moment(checkTime).format("HH:mm:ss") + ', '
                                });
                                signIn.checkTimeList = checkTimeStr.substring(0,checkTimeStr.length-2)
                            })
                        }
                    })
                }
            },
            //用户查看剩余调休时间
            fetchMyRestHours(){
                if (this.userRole < 3 && this.userRole > 0){
                    http.zsyGetHttp('/sign-in/rest-hours/'+this.userId,{},res=>{
                        this.myRestHours = res.data;
                    })
                }
            },
            //用户查看调休时间修改日志
            showRestHoursDetail(){
                http.zsyPostHttp('/sign-in/rest-hours-log/personal/page',this.myRestHoursLogReqDTO,res=>{
                    this.myRestHoursLogData = res.data.list;
                    this.myRestHoursLogPage.total = res.data.total;
                    this.myRestHoursDetailVisible = true;
                })
            },
            showUserRestHoursLog(userId,userName){
                if (userId != null && userId !== undefined && userId !==  '') {
                    // this.userRestHoursLogReqDTO.userId = userId;
                    // this.userRestHoursLogReqDTO.userName = userName;
                    // http.zsyPostHttp('/sign-in/rest-hours-log/page',this.userRestHoursLogReqDTO,res=>{
                    //     this.userRestHoursLogData = res.data.list;
                    //     this.userRestHoursLogPage.total = res.data.total;
                    //     this.userRestHoursDetailVisible = true;
                    // })
                    this.$router.push({ path: '/index/UserRestHoursPage', query: { userId: userId,userName:userName }});
                }else {
                    this.$message({
                        showClose: true,
                        message: '请选择用户',
                        type: 'warning'
                    });
                }
            },
            myRestHoursLogHandleCurrentChange(currentPage){
                this.myRestHoursLogReqDTO.pageNum = currentPage;
                this.showRestHoursDetail();
            },
            userRestHoursLogHandleCurrentChange(currentPage){
                this.userRestHoursLogReqDTO.pageNum = currentPage;
                this.showUserRestHoursLog(this.userRestHoursLogReqDTO.userId);
            },
            signInHandleCurrentChange(currentPage){
                this.signInPage.pageNum = currentPage;
                this.fetchSignInData();
            },
            mySignInHandleCurrentChange(currentPage){
                this.mySignInPage.pageNum = currentPage;
                this.fetchMySignInData();
            },
            eWorkHandleCurrentChange(currentPage){
                this.extraWorkReqDTO.pageNum = currentPage;
                this.getExtraWorkStats();
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
            recheck(userId,date){
                this.recheckForm.userId = userId;
                let dateStr = moment(date).format('YYYY-MM-DD');
                this.recheckForm.date = dateStr;
                this.reCheckVisible = true;
            },
            saveEditRecheck(formName,id){
                this.isSaving = true;
                if (this.recheckForm.reason == null || this.recheckForm.reason === ''){
                    this.$message({showClose: true, message: '补打卡原因不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                //上班打卡
                if (this.recheckForm.type === 0){
                    if(this.recheckForm.recheckInTime == null || this.recheckForm.recheckInTime === ''){
                        this.$message({showClose: true, message: '补打上班卡时间不能为空', type: 'error'});
                        this.isSaving = false;
                        return false;
                    }
                }
                //下班打卡
                else {
                    if(this.recheckForm.recheckOutTime == null || this.recheckForm.recheckOutTime === ''){
                        this.$message({showClose: true, message: '补打下班卡时间不能为空', type: 'error'});
                        this.isSaving = false;
                        return false;
                    }
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let form = this.recheckForm;
                        if (form.recheckInTime != null && form.recheckInTime !== ''){
                            form.recheckInTime = moment(form.recheckInTime).format('YYYY-MM-DD HH:mm:ss');
                            let inTime =this.recheckForm.date+' '+form.recheckInTime.substring(11);
                            form.recheckInTime = inTime;
                        }
                        if(form.recheckOutTime != null && form.recheckOutTime !== ''){
                            form.recheckOutTime = moment(form.recheckOutTime).format('YYYY-MM-DD HH:mm:ss');
                            let outTime =this.recheckForm.date+' '+form.recheckOutTime.substring(11);
                            form.recheckOutTime = outTime;
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
                            this.editRecheckVisible = false;
                            this.isSaving = false
                        }, err => {
                            this.isSaving = false
                        })
                    }
                })
            },
            saveRecheck(formName){
                this.isSaving = true;
                if (this.recheckForm.reason == null || this.recheckForm.reason.trim() === ''){
                    this.$message({showClose: true, message: '补打卡原因不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                if (this.recheckForm.recheckInTime == null || this.recheckForm.recheckInTime === ''){
                    if(this.recheckForm.recheckOutTime == null || this.recheckForm.recheckOutTime === ''){
                        this.$message({showClose: true, message: '补打卡时间不能为空', type: 'error'});
                        this.isSaving = false;
                        return false;
                    }
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let form = this.recheckForm;
                        if (form.recheckInTime != null && form.recheckInTime !== ''){
                            form.recheckInTime = moment(form.recheckInTime).format('YYYY-MM-DD HH:mm:ss');
                            let inTime =this.recheckForm.date+' '+form.recheckInTime.substring(11);
                            form.recheckInTime = inTime;
                        }
                        if(form.recheckOutTime != null && form.recheckOutTime !== ''){
                            form.recheckOutTime = moment(form.recheckOutTime).format('YYYY-MM-DD HH:mm:ss');
                            let outTime =this.recheckForm.date+' '+form.recheckOutTime.substring(11);
                            form.recheckOutTime = outTime;
                        }
                        http.zsyPostHttp('/sign-in/resign-in/add', form, (resp) => {
                            this.$message({
                                showClose: true,
                                message: '补打卡申请成功',
                                type: 'success'
                            });
                            this.clearRecheckForm();
                            this.fetchMyRecheckWait();
                            this.reCheckVisible = false;
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
            accessRecheck(formName,id){
                this.isSaving = true;
                if (this.recheckForm.reason == null || this.recheckForm.reason === ''){
                    this.$message({showClose: true, message: '补打卡原因不能为空', type: 'error'});
                    this.isSaving = false;
                    return false;
                }
                //上班打卡
                if (this.recheckForm.type === 0){
                    if(this.recheckForm.recheckInTime == null || this.recheckForm.recheckInTime === ''){
                        this.$message({showClose: true, message: '补打上班卡时间不能为空', type: 'error'});
                        this.isSaving = false;
                        return false;
                    }
                }
                //下班打卡
                else {
                    if(this.recheckForm.recheckOutTime == null || this.recheckForm.recheckOutTime === ''){
                        this.$message({showClose: true, message: '补打下班卡时间不能为空', type: 'error'});
                        this.isSaving = false;
                        return false;
                    }
                }
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let form = this.recheckForm;
                        if (form.recheckInTime != null && form.recheckInTime !== ''){
                            form.recheckInTime = moment(form.recheckInTime).format('YYYY-MM-DD HH:mm:ss');
                            let inTime =this.recheckForm.date+' '+form.recheckInTime.substring(11);
                            form.recheckInTime = inTime;
                        }
                        if(form.recheckOutTime != null && form.recheckOutTime !== ''){
                            form.recheckOutTime = moment(form.recheckOutTime).format('YYYY-MM-DD HH:mm:ss');
                            let outTime =this.recheckForm.date+' '+form.recheckOutTime.substring(11);
                            form.recheckOutTime = outTime;
                        }
                        http.zsyPutHttp('/sign-in/resign-in/access/'+id, form, (resp) => {
                            this.$message({
                                showClose: true,
                                message: '审核通过',
                                type: 'success'
                            });
                            this.clearRecheckForm();
                            this.fetchMyRecheckWait();
                            this.fetchRecheckWait();
                            this.editRecheckVisible = false;
                            this.isSaving = false
                        }, err => {
                            this.isSaving = false
                        })
                    }
                })
            },
            clearRecheckForm(){
                this.recheckForm.reason = this.recheckForm.recheckInTime = this.recheckForm.recheckOutTime
                    = this.recheckForm.userId = this.recheckForm.id = '';
                this.$refs.recheckForm.resetFields();
            },
            closeRecheckForm(){
                this.clearRecheckForm();
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
                if(this.userRole === 0){
                    http.zsyGetHttp('/sign-in/resign-in/page/0/'+this.recheckWaitPage.pageNum,{},(res)=>{
                        if (res){
                            this.recheckList.wait = res.data.list;
                            this.recheckWaitPage.total = res.data.total;
                        }
                    })
                }
            },
            fetchRecheckPass(){
                if(this.userRole === 0){
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

                this.recheckForm.id = recheck.id;
                this.recheckForm.userId = recheck.userId;
                this.recheckForm.userName = recheck.userName;
                this.recheckForm.recheckTime = recheck.recheckTime;
                this.recheckForm.recheckInTime = recheck.recheckTime;
                this.recheckForm.recheckOutTime = recheck.recheckTime;
                this.recheckForm.reason = recheck.reason;
                this.recheckForm.type = recheck.type;
                let dateStr = moment(recheck.recheckTime).format('YYYY-MM-DD');
                this.recheckForm.date = dateStr;
                this.editRecheckVisible = true;
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
            },
            //查询多人任务列表
            // fetchPersonalMultipleWait(){
            //         http.zsyGetHttp('/task-temp/personal/page/'+this.taskTempPage.pageNum+'/1',{},(res=>{
            //             this.taskTemp.waitAssess = res.data.list;
            //             this.taskTempPage.total = res.data.total;
            //         }))
            //
            // },
            fetchPersonalMultipleWait(){
                http.zsyGetHttp('/task-temp/personal/pending',{},(res=>{
                    res.data.forEach((task) => {
                        // task.name = '(待审核 多人任务)' + task.name;
                    });
                    this.task.doing = this.task.doing.concat(this.makeUpItems2(res.data))
                }))

            },
            fetchPersonalMultipleAccess(){
                http.zsyGetHttp('/task-temp/personal/page/'+this.taskTempPage2.pageNum+'/2',{},(res=>{
                    this.taskTemp.auditSuccess = res.data.list;
                    this.taskTempPage2.total = res.data.total;
                }))
            },
            fetchMultipleWait(){
                http.zsyGetHttp('/task-temp/page/'+this.taskTempPage3.pageNum+'/1',{},(res=>{
                    this.taskTemp.waitAssess2 = res.data.list;
                    this.taskTempPage3.total = res.data.total;
                }))
            },
            fetchMultipleAccess(){
                http.zsyGetHttp('/task-temp/accessed/page/'+this.taskTempPage4.pageNum+'/'+this.userId,{},(res=>{
                    this.taskTemp.auditSuccess2 = res.data.list;
                    this.taskTempPage4.total = res.data.total;
                }))
            },
            clearMultipleTask(){
                this.taskTempForm.taskId = this.taskTempForm.description = this.taskTempForm.beginTime
                = this.taskTempForm.endTime = this.taskTempForm.workHours = this.taskTempForm.createTime
                = this.taskTempForm.id = this.taskTempForm.userId = null;
                this.taskTempForm.userWeeks = [];
                this.description = null;
                this.taskDetail = {};
                this.showTaskDetailVisible = false;
                this.clearFunctionForm();
            },
            //查询当前用户是否其他用户的审核人
            fetchControlledPeople(){
                http.zsyGetHttp('/user/controlled-people/'+this.userId,{},(res=>{
                    this.controlledPeopleList = res.data;
                    if(this.controlledPeopleList.length > 0){
                        this.showTaskReviewTabVisible = true;
                        this.fetchPendingTaskTemp()
                    }
                }))
            },
            fetchPendingTaskTemp(){
                http.zsyGetHttp(`/task-temp/pending/list/`+this.userId,{},(res=>{
                    this.taskTemp.waitAssess2 = res.data;
                }))
            },
            cancelEdit() {
                this.addSuggestVisible = false;
                this.taskTempDetail.suggest = '';
            },
            saveEdit() {
                this.addSuggestVisible = false;
            },
            showInsChange() {
                this.addSuggestVisible = true;
            },
            fetchPersonalTaskModifyWait(){
                http.zsyGetHttp('/task-modify/personal/list',{},(res=>{
                    this.personalTaskModifyData.waitAssess = res.data;
                }))
            },
            fetchPersonalTaskModifyAccessed(){
                http.zsyGetHttp('/task-modify/personal/page/'+this.personalTaskModifyPage.pageNum,{},(res=>{
                    this.personalTaskModifyData.auditSuccess = res.data.list;
                    this.personalTaskModifyPage.total = res.data.total;
                }))
            },
            fetchTaskModifyWait(){
                http.zsyGetHttp('/task-modify/list',{},(res=>{
                    this.taskModifyData.waitAssess = res.data;
                }))
            },
            fetchTaskModifyAccessed(){
                http.zsyGetHttp('/task-modify/page/'+this.taskModifyPage.pageNum,{},(res=>{
                    this.taskModifyData.auditSuccess = res.data.list;
                    this.taskModifyPage.total = res.data.total
                }))
            },
            getTaskModifyDetail(tmId,taskId,userId){
                this.fetchTaskFunction(taskId);
                //查询任务信息
                http.zsyGetHttp('task/detail/'+taskId,{},(res =>{
                    this.taskDetail = res.data;
                }));

                //查询原来的周工时分配
                http.zsyGetHttp('/task/task-user/'+taskId+'/'+userId,{},(res=>{
                    this.taskUser = res.data;
                    this.changeTaskUserWeek()
                }));

                //查询修改任务申请详情
                http.zsyGetHttp('/task-modify/detail/'+tmId,{},(res=>{
                    this.taskModifyDetail = res.data;
                    this.taskModifyDetail.firstTime = this.taskModifyDetail.beginTime;
                    this.taskModifyDetail.secondTime = this.taskModifyDetail.endTime;
                    this.taskFunctionList=[];
                    this.functionLevelList=[];
                    if (this.taskModifyDetail.functionResDTOList !== undefined && this.taskModifyDetail.functionResDTOList.length>0){
                        this.taskModifyDetail.functionResDTOList.forEach(resDTO=>{
                            this.taskFunctionList.push(resDTO.functionId);
                            this.functionLevelList.push(resDTO.level);
                        });
                        this.num = this.taskModifyDetail.functionResDTOList.length;
                    }
                    this.modifyMyTaskReason = res.data.reason;
                    this.modifyMyTaskDescription = res.data.description;
                    this.changeTaskModifyUserWeek();
                    this.showTaskModifyDetailVisible = true;
                }))
            },
            changeTaskUserWeek(){
                if (this.taskUser.beginTime == null || this.taskUser.endTime == null) {
                    return
                }
                this.taskUserWeekNumber = [];
                let weekData = '';
                let param = this.taskUserWeekNumber;
                this.taskUser.beginWeek = moment(this.taskUser.beginTime).week();
                this.taskUser.endWeek = moment(this.taskUser.endTime).week();
                let beginYear = moment(this.taskUser.beginTime).year();
                let endYear = moment(this.taskUser.endTime).year();
                let userWeeks = this.taskUser.userWeeks;
                if (beginYear !== endYear) {
                    //当前情况:  开始周是在上一年  截止周在下一年
                    if(this.taskUser.beginWeek > this.taskUser.endWeek){
                        for (let i = this.taskUser.beginWeek; i < moment(this.taskUser.beginTime).weeksInYear() + 1; i++) {
                            http.zsyGetHttp('/userWeek/'+ this.taskUser.taskId +'/' + this.taskUser.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                                weekData = {
                                    'weekHours':resp.data,
                                    'weekNumber': i,
                                    'hours': userWeeks[i-this.taskUser.beginWeek].hours,
                                    'year': beginYear,
                                    'range': moment().year(beginYear).week(i).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(i).endOf('week').format('MM-DD')
                                };
                                param.push(weekData)
                            })
                        }
                        for (let i = 1; i < this.taskUser.endWeek + 1; i++) {
                            let range = moment(this.taskUser.beginTime).weeksInYear()-this.taskUser.beginWeek+1;
                            http.zsyGetHttp('/userWeek/'+ this.taskUser.taskId +'/' + this.taskUser.userId + '/' + endYear + '/' + i, {}, (resp) => {
                                weekData = {
                                    'weekHours':resp.data,
                                    'weekNumber': i,
                                    'hours': userWeeks[i-1+range].hours,
                                    'year': endYear,
                                    'range': moment().year(endYear).week(i).startOf('week').format('MM-DD') + '至' + moment().year(endYear).week(i).endOf('week').format('MM-DD')
                                };
                                param.push(weekData)
                            })

                        }
                    }else {
                        for (let i = 0;i <= this.taskUser.endWeek - this.taskUser.beginWeek;i++){
                            let thisWeek = i+this.taskUser.beginWeek;
                            http.zsyGetHttp('/userWeek/'+ this.taskUser.taskId +'/' + this.taskUser.userId + '/' + endYear + '/' + thisWeek, {}, (resp) => {
                                weekData = {
                                    'weekNumber':thisWeek,
                                    'hours': userWeeks[i].hours,
                                    'year':endYear ,
                                    'weekHours': resp.data,
                                    'range':moment().year(endYear).week(thisWeek).startOf('week').format('MM-DD')
                                        +'至'+moment().year(endYear).week(thisWeek).endOf('week').format('MM-DD') };
                                param.push(weekData)
                            })
                        }
                    }

                }
                else {
                    if (this.taskUser.beginWeek === this.taskUser.endWeek) {
                        http.zsyGetHttp('/userWeek/'+ this.taskUser.taskId +'/' + this.taskUser.userId + '/' + beginYear + '/' +this.taskUser.beginWeek , {}, (resp) => {
                            weekData = {
                                'weekHours':resp.data,
                                'weekNumber': this.taskUser.beginWeek,
                                'hours': this.taskUser.taskHours,
                                'year': beginYear,
                                'range': moment().year(beginYear).week(this.taskUser.beginWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.taskUser.beginWeek).endOf('week').format('MM-DD')
                            };
                            param.push(weekData)
                        })

                    }
                    else if (this.taskUser.endWeek - this.taskUser.beginWeek > 1) {
                        for (let i = this.taskUser.beginWeek; i < this.taskUser.endWeek + 1; i++) {
                            http.zsyGetHttp('/userWeek/'+ this.taskUser.taskId +'/' + this.taskUser.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                                weekData = {
                                    'weekHours':resp.data,
                                    'weekNumber': i,
                                    'hours': userWeeks[i-this.taskUser.beginWeek].hours,
                                    'year': beginYear,
                                    'range': moment().week(i).year(beginYear).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(i).endOf('week').format('MM-DD')
                                };
                                param.push(weekData)
                            })
                        }
                    }
                    else if (this.taskUser.endWeek - this.taskUser.beginWeek === 1) {

                        http.zsyGetHttp('/userWeek/'+ this.taskUser.taskId +'/' + this.taskUser.userId + '/' + beginYear + '/' + this.taskUser.beginWeek, {}, (resp) => {
                            param.push({
                                'weekHours':resp.data,
                                'weekNumber': this.taskUser.beginWeek,
                                'hours': userWeeks[0].hours,
                                'year': beginYear,
                                'range': moment().year(beginYear).week(this.taskUser.beginWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.taskUser.beginWeek).endOf('week').format('MM-DD')
                            })
                        });
                        http.zsyGetHttp('/userWeek/'+ this.taskUser.taskId +'/' + this.taskUser.userId + '/' + beginYear + '/' + this.taskUser.endWeek, {}, (resp) => {
                            param.push({
                                'weekHours':resp.data,
                                'weekNumber': this.taskUser.endWeek,
                                'hours': userWeeks[1].hours,
                                'year': beginYear,
                                'range': moment().year(beginYear).week(this.taskUser.endWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.taskUser.endWeek).endOf('week').format('MM-DD')
                            })
                        })

                    }
                    else if (this.taskUser.endWeek === 1 && this.taskUser.beginWeek > 1){
                        for(let i=this.taskUser.beginWeek;i<54;i++){
                            let date = new Date(this.taskUser.beginTime);
                            let date2 = new Date(date);
                            date2.setDate(date.getDate() + 7*(i-this.taskUser.beginWeek));
                            let year = date2.getFullYear();
                            let week = moment(date2).week();
                            if (week == 1){
                                year += 1;
                            }
                            http.zsyGetHttp('/userWeek/'+ this.taskUser.taskId +'/' +  this.taskUser.userId + '/' + year + '/' + week, {}, (resp) => {
                                weekData = {'weekNumber':week, 'hours': userWeeks[i-this.taskUser.beginWeek].hours,'year':year ,'weekHours': resp.data,'range':moment().week(week).year(year).startOf('week').format('MM-DD')+'至'+moment().year(year).week(week).endOf('week').format('MM-DD')  };
                                param.push(weekData)
                            })
                        }
                    }
                }

                this.taskUserWeekNumber = param
            },
            changeTaskModifyUserWeek(){
                if (this.taskModifyDetail.beginTime == null || this.taskModifyDetail.endTime == null) {
                    return
                }
                this.taskModifyWeekNumber = [];
                let weekData = '';
                let param = this.taskModifyWeekNumber;
                this.taskModifyDetail.beginWeek = moment(this.taskModifyDetail.beginTime).week();
                this.taskModifyDetail.endWeek = moment(this.taskModifyDetail.endTime).week();
                let beginYear = moment(this.taskModifyDetail.beginTime).year();
                let endYear = moment(this.taskModifyDetail.endTime).year();
                // console.log("beginYear: "+beginYear)
                // console.log("endYear: "+endYear)
                // console.log("beginWeek: "+this.taskModifyDetail.beginWeek)
                // console.log("endWeek: "+this.taskModifyDetail.endWeek)
                let userWeeks = this.taskModifyDetail.userWeekResDTOList;
                if (beginYear !== endYear) {
                    //当前情况:  开始周是在上一年  截止周在下一年
                    if(this.taskModifyDetail.beginWeek > this.taskModifyDetail.endWeek){
                        let range = moment(this.taskModifyDetail.beginTime).weeksInYear()-this.taskModifyDetail.beginWeek+1;
                        for (let i = this.taskModifyDetail.beginWeek; i < moment(this.taskModifyDetail.beginTime).weeksInYear() + 1; i++) {
                            http.zsyGetHttp('/userWeek/without/'+ this.taskModifyDetail.taskId +'/' + this.taskModifyDetail.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                                weekData = {
                                    'weekHours':resp.data,
                                    'weekNumber': i,
                                    'hours': userWeeks[i-this.taskModifyDetail.beginWeek].hours,
                                    'year': beginYear,
                                    'range': moment().year(beginYear).week(i)
                                        .startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(i)
                                        .endOf('week').format('MM-DD')
                                };
                                param.push(weekData)
                            })
                        }
                        for (let i = 1; i < this.taskModifyDetail.endWeek + 1; i++) {
                            http.zsyGetHttp('/userWeek/without/'+ this.taskModifyDetail.taskId +'/' + this.taskModifyDetail.userId + '/' + endYear + '/' + i, {}, (resp) => {
                                weekData = {
                                    'weekHours':resp.data,
                                    'weekNumber': i,
                                    'hours': userWeeks[i-1+range].hours,
                                    'year': endYear,
                                    'range': moment().year(endYear).week(i)
                                        .startOf('week').format('MM-DD') + '至' + moment().year(endYear).week(i)
                                        .endOf('week').format('MM-DD')
                                };
                                param.push(weekData)
                            })

                        }
                    }else {
                        for (let i = 0;i <= this.taskModifyDetail.endWeek - this.taskModifyDetail.beginWeek;i++){
                            let thisWeek = i+this.taskModifyDetail.beginWeek;
                            http.zsyGetHttp('/userWeek/'+ this.taskModifyDetail.taskId +'/' + this.taskModifyDetail.userId + '/' + endYear + '/' + thisWeek, {}, (resp) => {
                                weekData = {
                                    'weekNumber':thisWeek,
                                    'hours': userWeeks[i].hours,
                                    'year':endYear ,
                                    'weekHours': resp.data,
                                    'range':moment().year(endYear).week(thisWeek).startOf('week').format('MM-DD')
                                        +'至'+moment().year(endYear).week(thisWeek).endOf('week').format('MM-DD') };
                                param.push(weekData)
                            })
                        }
                    }

                }
                else {
                    if (this.taskModifyDetail.beginWeek === this.taskModifyDetail.endWeek) {
                        http.zsyGetHttp('/userWeek/without/'+ this.taskModifyDetail.taskId +'/' + this.taskModifyDetail.userId + '/' + beginYear + '/' +this.taskModifyDetail.beginWeek , {}, (resp) => {
                            weekData = {
                                'weekHours':resp.data,
                                'weekNumber': this.taskModifyDetail.beginWeek,
                                'hours': this.taskModifyDetail.workHours,
                                'year': beginYear,
                                'range': moment().year(beginYear).week(this.taskModifyDetail.beginWeek)
                                    .startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.taskModifyDetail.beginWeek)
                                    .endOf('week').format('MM-DD')
                            };
                            param.push(weekData)
                        })

                    }
                    else if (this.taskModifyDetail.endWeek - this.taskModifyDetail.beginWeek > 1) {
                        for (let i = this.taskModifyDetail.beginWeek; i < this.taskModifyDetail.endWeek + 1; i++) {
                            http.zsyGetHttp('/userWeek/without/'+ this.taskModifyDetail.taskId +'/' + this.taskModifyDetail.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                                weekData = {
                                    'weekHours':resp.data,
                                    'weekNumber': i,
                                    'hours': userWeeks[i-this.taskModifyDetail.beginWeek].hours,
                                    'year': beginYear,
                                    'range': moment().week(i).year(beginYear)
                                        .startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(i)
                                        .endOf('week').format('MM-DD')
                                };
                                param.push(weekData)
                            })
                        }
                    }
                    else if (this.taskModifyDetail.endWeek - this.taskModifyDetail.beginWeek === 1) {

                        http.zsyGetHttp('/userWeek/without/'+ this.taskModifyDetail.taskId +'/' + this.taskModifyDetail.userId + '/' + beginYear + '/' + this.taskModifyDetail.beginWeek, {}, (resp) => {
                            param.push({
                                'weekHours':resp.data,
                                'weekNumber': this.taskModifyDetail.beginWeek,
                                'hours': userWeeks[0].hours,
                                'year': beginYear,
                                'range': moment().year(beginYear).week(this.taskModifyDetail.beginWeek)
                                    .startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.taskModifyDetail.beginWeek)
                                    .endOf('week').format('MM-DD')
                            })
                        });
                        http.zsyGetHttp('/userWeek/without/'+ this.taskModifyDetail.taskId +'/' + this.taskModifyDetail.userId + '/' + beginYear + '/' + this.taskModifyDetail.endWeek, {}, (resp) => {
                            param.push({
                                'weekHours':resp.data,
                                'weekNumber': this.taskModifyDetail.endWeek,
                                'hours': userWeeks[1].hours,
                                'year': beginYear,
                                'range': moment().year(beginYear).week(this.taskModifyDetail.endWeek)
                                    .startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.taskModifyDetail.endWeek)
                                    .endOf('week').format('MM-DD')
                            })
                        })

                    }
                    else if (this.taskModifyDetail.endWeek === 1 && this.taskModifyDetail.beginWeek > 1){
                        for(let i=this.taskModifyDetail.beginWeek;i<54;i++){
                            let date = new Date(this.taskModifyDetail.beginTime);
                            let date2 = new Date(date);
                            date2.setDate(date.getDate() + 7*(i-this.taskModifyDetail.beginWeek));
                            let year = date2.getFullYear();
                            let week = moment(date2).week();
                            if (week == 1){
                                year += 1;
                            }
                            http.zsyGetHttp('/userWeek/without/'+ this.taskModifyDetail.taskId +'/' +  this.taskModifyDetail.userId + '/' + year + '/' + week, {}, (resp) => {
                                weekData = {'weekNumber':week, 'hours': userWeeks[i-this.taskModifyDetail.beginWeek].hours,'year':year ,'weekHours': resp.data,'range':moment().week(week).year(year).startOf('week').format('MM-DD')+'至'+moment().year(year).week(week).endOf('week').format('MM-DD')  };
                                param.push(weekData)
                            })
                        }
                    }
                }

                this.taskModifyWeekNumber = param
            },
            changeTaskModifyTime(){
                if(this.taskModifyDetail.beginTime instanceof Date){
                    this.taskModifyDetail.beginTime = this.taskModifyDetail.beginTime.getTime()
                }
                if(this.taskModifyDetail.endTime instanceof Date){
                    this.taskModifyDetail.endTime = this.taskModifyDetail.endTime.getTime()
                }

                if (this.taskModifyDetail.firstTime === this.taskModifyDetail.beginTime &&
                    this.taskModifyDetail.secondTime === this.taskModifyDetail.endTime) {
                    return ;
                }
                this.taskTempDetail.firstTime = this.taskTempDetail.beginTime;
                this.taskTempDetail.secondTime = this.taskTempDetail.endTime;

                if(this.taskModifyDetail.beginTime != null && this.taskModifyDetail.beginTime !== ''
                    && this.taskModifyDetail.endTime != null&& this.taskModifyDetail.endTime !== ''){
                    var userWeeks = [];
                    let beginYear = moment(this.taskModifyDetail.beginTime).year();
                    let endYear = moment(this.taskModifyDetail.endTime).year();
                    this.taskModifyDetail.beginWeek = moment(this.taskModifyDetail.beginTime).week();
                    this.taskModifyDetail.endWeek = moment(this.taskModifyDetail.endTime).week();
                    if (moment(this.taskModifyDetail.beginTime).isAfter(moment(this.taskModifyDetail.endTime))){
                        this.$message({ showClose: true,message: '开始时间不可在截止时间后面',type: 'error'});
                    }
                    if (beginYear !== endYear){
                        if (this.taskModifyDetail.beginWeek > this.taskModifyDetail.endWeek){
                            for(let i=this.taskModifyDetail.beginWeek;i<moment(this.taskModifyDetail.beginTime).weeksInYear()+1;i++){
                                http.zsyGetHttp('/userWeek/without/'+ this.taskModifyDetail.taskId +'/' + this.taskModifyDetail.userId + '/' + beginYear + '/'
                                    + (this.taskModifyDetail.beginWeek+i), {}, (resp) => {
                                    let weekData = {
                                        'weekNumber':i,
                                        'hours': '',
                                        'year':beginYear ,
                                        'weekHours': resp.data,
                                        'range':moment().year(beginYear).week(i).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(i).endOf('week').format('MM-DD') };
                                    userWeeks.push(weekData)
                                })
                            }
                            for(let i=1;i<this.taskModifyDetail.endWeek+1;i++){
                                http.zsyGetHttp('/userWeek/without/'+ this.taskModifyDetail.taskId +'/' + this.taskModifyDetail.userId + '/' + beginYear + '/'
                                    + (this.taskModifyDetail.beginWeek+i), {}, (resp) => {
                                    let weekData = {
                                        'weekNumber':i,
                                        'hours': '',
                                        'year':endYear ,
                                        'weekHours': resp.data,
                                        'range':moment().year(endYear).week(i).startOf('week').format('MM-DD')+'至'+moment().year(endYear).week(i).endOf('week').format('MM-DD') };
                                    userWeeks.push(weekData)
                                })

                            }
                        } else {
                            for(let i = 0;i<=this.taskModifyDetail.endWeek - this.taskModifyDetail.beginWeek;i++){
                                http.zsyGetHttp('/userWeek/without/'+ this.taskModifyDetail.taskId +'/' + this.taskModifyDetail.userId + '/' + beginYear + '/'
                                    + (this.taskModifyDetail.beginWeek+i), {}, (resp) => {
                                    let weekData = {
                                        'weekHours':resp.data,
                                        'weekNumber': i + this.taskModifyDetail.beginWeek,
                                        'hours': 0,
                                        'year': endYear,
                                        'range': moment().year(beginYear).week(i + this.taskModifyDetail.beginWeek).startOf('week').format('MM-DD')
                                            + '至' + moment().year(beginYear).week(i + this.taskModifyDetail.beginWeek).endOf('week').format('MM-DD')
                                    };
                                    userWeeks.push(weekData)
                                })


                            }
                        }
                    }
                    else {
                        //同年份,且周也是属于同年份
                        if (this.taskModifyDetail.endWeek >= this.taskModifyDetail.beginWeek) {
                            for(let i = 0;i<=this.taskModifyDetail.endWeek - this.taskModifyDetail.beginWeek;i++){
                                let weekData = {
                                    'weekHours':0,
                                    'weekNumber': i + this.taskModifyDetail.beginWeek,
                                    'hours': 0,
                                    'year': endYear,
                                    'range': moment().year(beginYear).week(i + this.taskModifyDetail.beginWeek).startOf('week').format('MM-DD')
                                        + '至' + moment().year(beginYear).week(i + this.taskModifyDetail.beginWeek).endOf('week').format('MM-DD')
                                };
                                userWeeks.push(weekData)

                            }
                        }
                        //同年份,但是有的周属于上一年,有的属于下一年
                        else {
                            for(let i=this.taskModifyDetail.beginWeek;i<moment(this.taskModifyDetail.beginTime).weeksInYear()+1;i++){
                                let weekData = {
                                    'weekNumber':i,
                                    'hours': '',
                                    'year':beginYear ,
                                    'weekHours': 0,
                                    'range':moment().year(beginYear).week(i).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(i).endOf('week').format('MM-DD') };
                                userWeeks.push(weekData)
                            }
                            for(let i=1;i<this.taskModifyDetail.endWeek+1;i++){
                                let year = endYear + 1;
                                let weekData = {
                                    'weekNumber':i,
                                    'hours': '',
                                    'year':year ,
                                    'weekHours': 0,
                                    'range':moment().year(year).week(i).startOf('week').format('MM-DD')+'至'+moment().year(year).week(i).endOf('week').format('MM-DD') };
                                userWeeks.push(weekData)

                            }
                        }
                    }
                    // this.taskModifyDetail.userWeekResDTOList = userWeeks;
                    this.taskModifyWeekNumber = userWeeks
                    // this.changeTaskModifyUserWeek();


                }


            },
            deleteTaskModify(id){
                this.$confirm('删除任务修改申请, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(()=>{
                    http.zsyDeleteHttp('/task-modify/delete/'+id,{},(res)=>{
                        this.showTaskModifyDetailVisible = false;
                        this.$message({
                            showClose: true,
                            message: '删除任务修改申请成功',
                            type: 'success'
                        });
                        if (this.userRole > 0){
                            this.reload();
                            // this.fetchPersonalTaskModifyWait();
                            // this.fetchTaskDoing();
                            // this.fetchPersonalMultipleWait();
                            // window.history.go(0)
                        } else {
                            this.fetchTaskModifyWait();
                            this.fetchTaskModifyAccessed();
                        }
                    })
                }).catch(() => {
                });

            },
            editTaskModify(id,formName){
                let sumHours=0;
                for(let i=0;i<this.taskModifyWeekNumber.length;i++){
                    if(this.taskModifyWeekNumber[i].hours===''|| this.taskModifyWeekNumber[i].hours=== undefined){
                        if(this.taskModifyWeekNumber[i].hoursTemp !== undefined &&this.taskModifyWeekNumber[i].hoursTemp!==''){
                            this.taskModifyWeekNumber[i].hours = this.taskModifyWeekNumber[i].hoursTemp
                        }else{
                            this.taskModifyWeekNumber[i].hours = 0
                        }
                    }
                    let ishours = /^(([0-9]+[\.]?[0-9]+)|[0-9])$/.test(this.taskModifyWeekNumber[i].hours);
                    if(!ishours){
                        this.errorMsg('工作量填写错误');
                        return false;
                    }
                    if(this.taskModifyWeekNumber[i].hours>99999.9||this.taskModifyWeekNumber[i].hours<0){
                        this.errorMsg('工作量正确值应为0~99999.9');
                        return false;
                    }
                    sumHours +=  parseFloat(this.taskModifyWeekNumber[i].hours)
                }

                if(sumHours!=this.taskModifyDetail.workHours){
                    this.errorMsg('周工作量与总工作量不符，请检查');
                    return
                }
                this.taskModifyDetail.userWeeks = this.taskModifyWeekNumber;


                let vm = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.isSaving = true;
                        let param = this.taskModifyDetail;
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD 00:00:00');
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:50');
                        if (this.modifyMyTaskReason == null || this.modifyMyTaskReason.trim() === ''){
                            this.$message({showClose: true, message: '修改原因不能为空', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        if (this.modifyMyTaskDescription == null || this.modifyMyTaskDescription.trim() === ''){
                            this.$message({showClose: true, message: '新的任务描述不能为空', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        if (param.beginTime == null || param.beginTime === ''){
                            this.$message({showClose: true, message: '开始时间不能为空', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        if (param.endTime == null || param.endTime === ''){
                            this.$message({showClose: true, message: '截止时间不能为空', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        if((moment(param.beginTime).isAfter(moment(param.endTime)))){
                            this.$message({showClose: true, message: '开始时间不能在截止时间后面', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        //任务是设计相关阶段时
                        if (this.taskDetail.stageId === '212754785051344891' || this.taskDetail.stageId === '212754785051344892'){
                            if (!(moment(this.taskModifyDetail.endTime)).isBefore((moment(this.taskDetail.beginTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务设计完成时间', type: 'error'});
                                this.isSaving = false;
                                return false;
                            }
                        }
                        //任务是开发相关阶段时
                        if (this.taskDetail.stageId === '212754785051344890' || this.taskDetail.stageId === '212754785051344894'){
                            if (!(moment(this.taskModifyDetail.endTime)).isBefore((moment(this.taskDetail.testTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务开发完成时间', type: 'error'});
                                this.isSaving = false;
                                return false;
                            }
                        }
                        //任务是测试相关阶段时
                        if (this.taskDetail.stageId === '212754785051344895' || this.taskDetail.stageId === '212754785051344896'){
                            if (!(moment(this.taskModifyDetail.endTime)).isBefore((moment(this.taskDetail.endTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务截止时间', type: 'error'});
                                this.isSaving = false;
                                return false;
                            }
                        }
                        param.workHours = String(param.workHours);
                        if (param.workHours.length !== parseFloat(param.workHours).toString().length || parseFloat(param.workHours) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        if (param.workHours < 0.1) {
                            this.$message({showClose: true, message: '工作量正确值应为0.1~8', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        param.reason = this.modifyMyTaskReason;
                        param.description = this.modifyMyTaskDescription;
                        param.id = this.taskModifyDetail.id;

                        if (this.taskModifyDetail.functionResDTOList !== undefined
                            && this.taskModifyDetail.functionResDTOList.length>0) {
                            let taskModifyFunctionList = [];
                            for(let i = 0;i<this.num;i++){
                                let taskModifyFunction={
                                    functionId:this.taskFunctionList[i],
                                    level:this.functionLevelList[i],
                                };
                                taskModifyFunctionList.push(taskModifyFunction);
                            }
                            for(let i = 0;i < taskModifyFunctionList.length;i++){
                                let taskModifyFunction = taskModifyFunctionList[i];
                                let functionId = taskModifyFunction.functionId;
                                let level = taskModifyFunction.level;
                                if (functionId === undefined || functionId === null || functionId === ''){
                                    this.$message({showClose: true, message: '关联任务功能点不能为空,请检查', type: 'error'});
                                    this.isSaving = false;
                                    return false;
                                }
                                if (level === undefined || level === null || level === ''){
                                    this.$message({showClose: true, message: '功能点复杂度不能为空,请检查', type: 'error'});
                                    this.isSaving = false;
                                    return false;
                                }
                            }
                            param.taskModifyFunctionList = taskModifyFunctionList;
                            let newArr = [param.taskModifyFunctionList[0].functionId];
                            for (let i = 1; i < param.taskModifyFunctionList.length; i++) {
                                let repeat = false;
                                for (let j = 0; j < newArr.length; j++) {
                                    if (param.taskModifyFunctionList[i].functionId === newArr[j]) {
                                        repeat = true;
                                        break;
                                    }else{

                                    }
                                }
                                if (!repeat){
                                    newArr.push(param.taskModifyFunctionList[i].functionId)
                                }
                            }
                            if (param.taskModifyFunctionList.length>newArr.length){
                                this.$message({showClose: true, message: '功能点不可重复选择', type: 'error'});
                                this.isSaving = false;
                                return false;
                            }

                        }

                        http.zsyPutHttp('/task-modify/update', param, (resp) => {
                            this.showTaskModifyDetailVisible = false;
                            this.$message({showClose: true, message: '更新任务修改申请成功', type: 'success'});
                            this.isSaving = false;
                            this.$refs[formName].resetFields();
                            this.clearEditTaskModifyForm();
                            this.taskFunctionList = [];
                            this.functionLevelList = [];
                            param.taskModifyFunctionList = [];
                            this.fetchPersonalTaskModifyWait();
                            this.fetchPersonalTaskModifyAccessed();
                        },err=>{
                            this.isSaving = false;
                            this.taskFunctionList = [];
                            this.functionLevelList = [];
                            param.taskModifyFunctionList = [];
                        });
                    } else {
                        this.isSaving = false;
                        return false;
                    }
                }, err => {
                    this.isSaving = false;
                });
            },
            accessTaskModify(id,formName){
                let sumHours=0;
                for(let i=0;i<this.taskModifyWeekNumber.length;i++){
                    if(this.taskModifyWeekNumber[i].hours===''|| this.taskModifyWeekNumber[i].hours=== undefined){
                        if(this.taskModifyWeekNumber[i].hoursTemp !== undefined &&this.taskModifyWeekNumber[i].hoursTemp!==''){
                            this.taskModifyWeekNumber[i].hours = this.taskModifyWeekNumber[i].hoursTemp
                        }else{
                            this.taskModifyWeekNumber[i].hours = 0
                        }
                    }
                    let ishours = /^(([0-9]+[\.]?[0-9]+)|[0-9])$/.test(this.taskModifyWeekNumber[i].hours);
                    if(!ishours){
                        this.errorMsg('工作量填写错误');
                        return false;
                    }
                    if(this.taskModifyWeekNumber[i].hours>99999.9||this.taskModifyWeekNumber[i].hours<0){
                        this.errorMsg('工作量正确值应为0~99999.9');
                        return false;
                    }
                    sumHours +=  parseFloat(this.taskModifyWeekNumber[i].hours)
                }

                if(sumHours!=this.taskModifyDetail.workHours){
                    this.errorMsg('周工作量与总工作量不符，请检查');
                    return
                }
                this.taskModifyDetail.userWeeks = this.taskModifyWeekNumber;


                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.isSaving = true;
                        let param = this.taskModifyDetail;
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD 00:00:00');
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:50');
                        if (this.modifyMyTaskReason == null || this.modifyMyTaskReason.trim() === ''){
                            this.$message({showClose: true, message: '修改原因不能为空', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        if (param.beginTime == null || param.beginTime === ''){
                            this.$message({showClose: true, message: '开始时间不能为空', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        if (param.endTime == null || param.endTime === ''){
                            this.$message({showClose: true, message: '截止时间不能为空', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        if((moment(param.beginTime).isAfter(moment(param.endTime)))){
                            this.$message({showClose: true, message: '开始时间不能在截止时间后面', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        //任务是设计相关阶段时
                        if (this.taskDetail.stageId === '212754785051344891' || this.taskDetail.stageId === '212754785051344892'){
                            if (!(moment(this.taskModifyDetail.endTime)).isBefore((moment(this.taskDetail.beginTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务设计完成时间', type: 'error'});
                                this.isSaving = false;
                                return false;
                            }
                        }
                        //任务是开发相关阶段时
                        if (this.taskDetail.stageId === '212754785051344890' || this.taskDetail.stageId === '212754785051344894'){
                            if (!(moment(this.taskModifyDetail.endTime)).isBefore((moment(this.taskDetail.testTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务开发完成时间', type: 'error'});
                                this.isSaving = false;
                                return false;
                            }
                        }
                        //任务是测试相关阶段时
                        if (this.taskDetail.stageId === '212754785051344895' || this.taskDetail.stageId === '212754785051344896'){
                            if (!(moment(this.taskModifyDetail.endTime)).isBefore((moment(this.taskDetail.endTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务截止时间', type: 'error'});
                                this.isSaving = false;
                                return false;
                            }
                        }
                        param.workHours = String(param.workHours);
                        if (param.workHours.length !== parseFloat(param.workHours).toString().length || parseFloat(param.workHours) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        if (param.workHours < 0.1) {
                            this.$message({showClose: true, message: '工作量正确值应为0.1~8', type: 'error'});
                            this.isSaving = false;
                            return false;
                        }
                        param.reason = this.modifyMyTaskReason;
                        param.id = this.taskModifyDetail.id;

                        if (this.taskModifyDetail.functionResDTOList !== undefined
                            && this.taskModifyDetail.functionResDTOList.length>0) {
                            let taskModifyFunctionList = [];
                            for(let i = 0;i<this.num;i++){
                                let taskModifyFunction={
                                    functionId:this.taskFunctionList[i],
                                    level:this.functionLevelList[i],
                                };
                                taskModifyFunctionList.push(taskModifyFunction);
                            }
                            for(let i = 0;i < taskModifyFunctionList.length;i++){
                                let taskModifyFunction = taskModifyFunctionList[i];
                                let functionId = taskModifyFunction.functionId;
                                let level = taskModifyFunction.level;
                                if (functionId === undefined || functionId === null || functionId === ''){
                                    this.$message({showClose: true, message: '关联任务功能点不能为空,请检查', type: 'error'});
                                    this.isSaving = false;
                                    return false;
                                }
                                if (level === undefined || level === null || level === ''){
                                    this.$message({showClose: true, message: '功能点复杂度不能为空,请检查', type: 'error'});
                                    this.isSaving = false;
                                    return false;
                                }
                            }
                            param.taskModifyFunctionList = taskModifyFunctionList;
                            let newArr = [param.taskModifyFunctionList[0].functionId];
                            for (let i = 1; i < param.taskModifyFunctionList.length; i++) {
                                let repeat = false;
                                for (let j = 0; j < newArr.length; j++) {
                                    if (param.taskModifyFunctionList[i].functionId === newArr[j]) {
                                        repeat = true;
                                        break;
                                    }else{

                                    }
                                }
                                if (!repeat){
                                    newArr.push(param.taskModifyFunctionList[i].functionId)
                                }
                            }
                            if (param.taskModifyFunctionList.length>newArr.length){
                                this.$message({showClose: true, message: '功能点不可重复选择', type: 'error'});
                                this.isSaving = false;
                                return false;
                            }
                        }
                        http.zsyPutHttp('/task-modify/review', param, (resp) => {
                            this.showTaskModifyDetailVisible = false;
                            this.$message({showClose: true, message: '审核通过', type: 'success'});
                            this.isSaving = false;
                            this.$refs[formName].resetFields();
                            this.clearEditTaskModifyForm();
                            this.taskFunctionList = [];
                            this.functionLevelList = [];
                            param.taskModifyFunctionList = [];
                            this.fetchTaskModifyWait();
                            this.fetchTaskModifyAccessed();
                        },err=>{
                            this.isSaving = false;
                            this.taskFunctionList = [];
                            this.functionLevelList = [];
                            param.taskModifyFunctionList = [];
                        });
                    } else {
                        this.isSaving = false;
                        return false;
                    }
                }, err => {
                    this.isSaving = false;
                });
            },
            clearEditTaskModifyForm(){
                this.modifyMyTaskReason = '';
                this.modifyMyTaskDescription = '';
                this.taskModifyWeekNumber = [];
                this.taskModifyDetail.reason = '';
                this.taskModifyDetail.description = '';
                this.taskModifyDetail.taskId = null;
                this.taskModifyDetail.userId = null;
                this.taskModifyDetail.id = null;
                this.taskModifyDetail.beginTime = '';
                this.taskModifyDetail.endTime = '';
                this.taskModifyDetail.userWeeks = [];
                this.taskModifyDetail.userWeekResDTOList = [];
            },
            getLeaveList(){
                if (this.userRole === 3){
                    if (this.leaveReqDTO.beginTime != null && this.leaveReqDTO.beginTime !== ''){
                        this.leaveReqDTO.beginTime = moment(this.leaveReqDTO.beginTime).format('YYYY-MM-DD 00:00:00')
                    }
                    if (this.leaveReqDTO.endTime != null && this.leaveReqDTO.endTime !== ''){
                        this.leaveReqDTO.endTime = moment(this.leaveReqDTO.beginTime).format('YYYY-MM-DD 23:59:59')
                    }
                    http.zsyPostHttp(`/userLeave/list`, this.leaveReqDTO, (resp) => {
                        this.leaveManage =  resp.data.list;
                        this.leaveFormPage.total = resp.data.total;
                    });
                }

            },
            leaveHandleCurrentChange(currentPage){
                this.leaveReqDTO.pageNum = currentPage;
                this.getLeaveList();
            },

            fetchPersonalEvaluation(){
                http.zsyGetHttp('/evaluation/average/personal',{},(res)=>{
                    this.personalEvaluation = res.data;
                    this.personalEvaluation.weekTime = this.getDateString('week');
                    this.personalEvaluation.monthTime = this.getDateString('month');
                    this.personalEvaluation.yearTime = this.getDateString('year')
                })
            },

            //查询个人近12周工作量
            fetchWeekHourStats(){
                let today = new Date();
                this.thisYear = today.getFullYear()+'年';
                this.weekHourStatsList = [];
                this.weekHourList = [];
                this.weekNumberList = [];
                this.avgWeekHourList = [];
                this.leaveHourList = [];
                this.userInfo = {};
                this.jobRoleName = '';
                let userId = helper.decodeToken().userId;
                this.fetchUserInfo(userId);
                http.zsyGetHttp('/data/personal/week-hour-stats',{},(res)=>{
                    this.weekHourStatsList = res.data;
                    this.weekHourStatsList = this.weekHourStatsList.reverse();
                    this.weekHourStatsList.forEach(weekHourStat=>{
                        this.weekHourList.push(weekHourStat.weekHours);
                        this.weekNumberList.push(weekHourStat.week);
                        this.avgWeekHourList.push(weekHourStat.avgWeekHours);
                        this.leaveHourList.push(weekHourStat.leaveHours);
                    });
                    this.drawLine1()
                })
            },
            fetchUserWeekHourStats(userId){
                if (userId != null && userId !== ''){
                    let today = new Date();
                    this.thisYear = today.getFullYear()+'年';
                    this.weekHourStatsList = [];
                    this.weekHourList = [];
                    this.weekNumberList = [];
                    this.avgWeekHourList = [];
                    this.leaveHourList = [];
                    this.userInfo = {};
                    this.jobRoleName = '';
                    this.fetchUserInfo(userId);
                    http.zsyGetHttp('/data/week-hour-stats/'+userId,{},(res)=>{
                        this.weekHourStatsList = res.data;
                        this.weekHourStatsList = this.weekHourStatsList.reverse();
                        this.weekHourStatsList.forEach(weekHourStat=>{
                            this.weekHourList.push(weekHourStat.weekHours);
                            this.weekNumberList.push(weekHourStat.week);
                            this.avgWeekHourList.push(weekHourStat.avgWeekHours);
                            this.leaveHourList.push(weekHourStat.leaveHours);
                        });
                        this.drawLine2()
                    })
                }else {
                    this.weekHourStatsList = [];
                    this.weekHourList = [];
                    this.weekNumberList = [];
                }
            },
            //查询用户
            fetchUserInfo(userId){
                http.zsyGetHttp('/user/'+userId,{},(res)=>{
                    this.userInfo = res.data;
                    let jobRole = this.userInfo.jobRole;
                    let jobRoleName = '';
                    if (jobRole !== undefined && jobRole !== null && jobRole !== ''){
                        if (jobRole === 0){
                            jobRoleName = '测试';
                        } else if(jobRole === 1){
                            jobRoleName = '开发';
                        }else if (jobRole === 2){
                            jobRoleName = '设计';
                        } else if (jobRole === 3){
                            jobRoleName = '产品';
                        } else if (jobRole === 5){
                            jobRoleName = '算法工程师';
                        } else if (jobRole === 4){
                            jobRoleName = '其他';
                        }
                    }
                    this.jobRoleName = jobRoleName;
                })
            },
            drawLine1(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart1'));
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '周工时'
                    },
                    tooltip : {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['个人周工时','平均周工时('+this.jobRoleName+')','周请假']
                    },
                    xAxis: {
                        type: 'category',
                        data: this.weekNumberList
                    },
                    yAxis: [
                        {
                            type: 'value',
                            name:'时长',
                            interval: 10,
                            axisLabel: {
                                formatter: '{value} h'
                            },
                            splitLine: {
                                show: false
                            }
                        },
                        // {
                        //     type: 'value',
                        //     name: '时长',
                        //     min: 0,
                        //     interval: 10,
                        //     axisLabel: {
                        //         formatter: '{value} h'
                        //     },
                        //     splitLine:{
                        //         show:false
                        //     },
                        // },

                    ],
                    // label: {
                    //     show: true,//是否展示
                    //     position: 'inside',//在右端
                    //     textStyle: {
                    //         fontWeight: 'bolder',
                    //         fontSize: '12',
                    //         fontFamily: '微软雅黑',
                    //         // color: 'black'
                    //     }
                    // },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    series : [
                        {
                            name:'个人周工时',
                            type:'bar',
                            stack:'总量',
                            data:this.weekHourList,
                            barWidth:32,
                            barGap:0,
                            label:{
                                normal: {
                                    show: true,
                                    position: 'inside'
                                }
                            }
                        },
                        {
                            name:'平均周工时('+this.jobRoleName+')',
                            type:'bar',
                            data:this.avgWeekHourList,
                            barWidth:32,
                            // barGap:10
                            label:{
                                normal: {
                                    show: true,
                                    position: 'inside',
                                    // verticalAlign:'middle'
                                }
                            }
                        },
                        {
                            name:'周请假',
                            type:'bar',
                            stack:'总量',
                            data:this.leaveHourList,
                            barWidth:32,
                            // barGap:10
                            label:{
                                normal: {
                                    show: true,
                                    position: 'top'
                                }
                            }
                        },
                    ]
                });
            },
            drawLine2(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart2'));
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '周工时'
                    },
                    tooltip : {
                        trigger: 'axis'
                    },
                    legend: {
                        data: ['个人周工时','平均周工时('+this.jobRoleName+')','周请假']
                    },
                    xAxis: {
                        type: 'category',
                        data: this.weekNumberList
                    },
                    yAxis: [
                        {
                            type: 'value',
                            name:'时长',
                            interval: 10,
                            axisLabel: {
                                formatter: '{value} h'
                            },
                            splitLine: {
                                show: false
                            }
                        },
                        // {
                        //     type: 'value',
                        //     name: '时长',
                        //     min: 0,
                        //     interval: 10,
                        //     axisLabel: {
                        //         formatter: '{value} h'
                        //     },
                        //     splitLine:{
                        //         show:false
                        //     },
                        // },

                    ],
                    // label: {
                    //     show: true,//是否展示
                    //     position: 'inside',//在右端
                    //     textStyle: {
                    //         fontWeight: 'bolder',
                    //         fontSize: '12',
                    //         fontFamily: '微软雅黑',
                    //         // color: 'black'
                    //     }
                    // },
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    series : [
                        {
                            name:'个人周工时',
                            type:'bar',
                            stack:'总量',
                            data:this.weekHourList,
                            barWidth:32,
                            barGap:0,
                            label:{
                                normal: {
                                    show: true,
                                    position: 'inside'
                                }
                            }
                        },
                        {
                            name:'平均周工时('+this.jobRoleName+')',
                            type:'bar',
                            data:this.avgWeekHourList,
                            barWidth:32,
                            // barGap:10
                            label:{
                                normal: {
                                    show: true,
                                    position: 'inside',
                                    // verticalAlign:'middle'
                                }
                            }
                        },
                        {
                            name:'周请假',
                            type:'bar',
                            stack:'总量',
                            data:this.leaveHourList,
                            barWidth:32,
                            // barGap:10
                            label:{
                                normal: {
                                    show: true,
                                    position: 'top'
                                }
                            }
                        },
                    ]
                });
            },
            //跳转到任务关联文档URL
            toFile(url){
                if (url !== null && url !== ''){
                    window.open(url,'_blank')
                }
            },

            getExtraWorkStats(){
                if (this.userRole === 3 ){
                    http.zsyPostHttp('/stats/extra-work/page',this.extraWorkReqDTO,(res)=>{
                        this.extraWorkStatsList = res.data.list;
                        this.extraWorkPage.total = res.data.total;
                        this.extraWorkStatsList.forEach(extraWork=>{
                            if (extraWork.checkRecords.length > 0) {
                                let checkTimeStr = '';
                                extraWork.checkRecords.forEach(checkTime => {
                                    checkTimeStr = checkTimeStr + moment(checkTime).format("HH:mm:ss") + ', '
                                })
                                extraWork.checkRecords = checkTimeStr.substring(0,checkTimeStr.length-2)
                            }
                        });
                        // this.extraWorkReqDTO.beginTime = null;
                        // this.extraWorkReqDTO.endTime = null;
                    })
                }
            },
            searchEWorkStats(){
                this.extraWorkReqDTO.beginTime = null;
                this.extraWorkReqDTO.endTime = null;
                if (this.ewBeginTime !== undefined && this.ewBeginTime !== null && this.ewBeginTime !== ''){
                    this.extraWorkReqDTO.beginTime = moment(this.ewBeginTime).format('YYYY-MM-DD 00:00:00');
                }
                if (this.ewEndTime !== undefined && this.ewEndTime !== null && this.ewEndTime !== ''){
                    this.extraWorkReqDTO.endTime = moment(this.ewEndTime).format('YYYY-MM-DD 23:59:59');
                }
                this.getExtraWorkStats()
            },

            closeFunctionDialog(){
                this.createMultipleTaskVisible = true;
            },
            plus(i){
                this.num = this.num+1;
            },
            minus(i){
                this.num = this.num-1;
                this.taskFunctionList.splice(i);
                this.functionLevelList.splice(i);
            },
            //查看复杂度参考表
            showLevelReference(){
              let url = "http://zxhx-test.cn-bj.ufileos.com/zsy-ufile-service/功能点复杂度参考表.pdf";
              // let url = "http://zxhx-test.cn-bj.ufileos.com/zsy-ufile-service/df71d0bf-6a42-4a84-a55d-4e21288fa073.png";
              window.open(url,'_blank')
            },
            //鼠标移入
            mouseEnter(functionId){
                this.userAndLevelData = [];
                if (functionId !== undefined && functionId !== null && functionId !== ''){
                    http.zsyGetHttp('task-function/function-level/'+functionId,{},(res)=>{
                        this.userAndLevelData = res.data;
                        this.showUserAndLevelVisible = true;
                    })
                }
            },
            //鼠标移出
            mouseLeave(){
                this.userAndLevelData = [];
                // this.showUserAndLevelVisible = false;
            },
            //查询 临时任务涉及项目
            fetchTaskTempModuleList(){
                http.zsyGetHttp('/task-temp-module/list',{},(res)=>{
                    this.taskTempModuleData = res.data;
                })
            },
            //查询任务功能点
            fetchTaskFunction(taskId){
                if (taskId !== undefined && taskId != null && taskId !== ''){
                    http.zsyGetHttp(`/task-function/list/`+taskId,{},(res)=>{
                        this.taskFunctionData = res.data;
                    })
                }else {
                    this.taskFunctionData = [];
                }
            },
            clearFunctionForm(){
                this.taskFunctionList = [];
                this.functionLevelList = [];
                this.num = 1;
            },
            closeModifyDialog(){
                this.taskFunctionList = [];
                this.functionLevelList = [];
                this.num = 0;
            },
            //切换tab
            handleClickTab(){
                if (this.tabName === 'integral'){
                    this.fetchPersonalTaskIntegral();
                } else if (this.tabName === 'weekHours'){
                    this.fetchWeekHourStats();
                } else if (this.tabName === 'evaluation'){
                    this.fetchPersonalEvaluation();
                }else if (this.tabName === 'task'){
                    this.fetchPrincipalTask()
                }
            },
            //查看任务
            fetchPersonalTaskIntegral(){
                http.zsyGetHttp('/user-task-integral/personal',{},(res)=>{
                    this.personalTaskIntegralData = res.data;
                    let items = [];
                    items.push({label: '', score: this.personalTaskIntegralData.monthIntegral, time: this.getDateString('month')});
                    let seasonRange = moment(this.personalTaskIntegralData.seasonBegin).format("YYYY-MM-DD")+'--'+moment(this.personalTaskIntegralData.seasonEnd).format('YYYY-MM-DD');
                    items.push({label: '', score: this.personalTaskIntegralData.seasonIntegral, time: seasonRange});
                    items.push({label: '', score: this.personalTaskIntegralData.yearIntegral, time: this.getDateString('year')});
                    this.taskIntegralItem = items;
                    this.developVisible = this.personalTaskIntegralData.developRole;
                })
            },
            fetchPrincipalTask(){
                http.zsyGetHttp('/data/principal-task-stats',{},(res)=>{
                    this.principalTaskList = res.data;
                })
            },
            fetchAllPrincipalTask(){
                http.zsyGetHttp('/data/principal-task-stats/all',{},(res)=>{
                    this.principalAllTaskList = res.data;
                })
            },
            //查看用户调休
            fetchAllUsersRestHours(){
                if (this.restHourYear != null && this.restHourYear  !== undefined && this.restHourYear !== ''){
                    this.restHourReqDTO.year = moment(this.restHourYear).format("YYYY-MM-DD");
                    this.restHourReqDTO.year = this.restHourReqDTO.year.substring(0,4);
                }else {
                    this.restHourReqDTO.year = null
                }
                http.zsyPostHttp('sign-in/rest-hours/list',this.restHourReqDTO,res=>{
                    this.restHoursData = res.data;
                })
            },
            //手动 新增调休记录
            addRestHoursLog(){
                this.userRestHoursDetailVisible = false;
                this.editRestHoursVisible = true;
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

                http.zsyPostHttp('/sign-in/rest-hours-log/add',this.userRestHoursLogForm,res=>{
                    if (res.errMsg == "执行成功"){
                        this.$message({
                            showClose: true,
                            message: '修改成功',
                            type: 'success'
                        });
                        this.restHourLoading = false;
                        this.editRestHoursVisible = false;
                        this.fetchAllUsersRestHours()
                    }else {
                        this.restHourLoading = false;
                        this.editRestHoursVisible = false;
                    }
                },err=>{
                    this.restHourLoading = false;
                },error=>{
                    this.restHourLoading = false;
                })
            },
            //取消添加
            cancelAddRestHoursLog(){
                this.userRestHoursLogForm.userId = null;
                this.userRestHoursLogForm.recordTime = null;
                this.userRestHoursLogForm.restHour = 0;
                this.userRestHoursLogForm.content = '';
                this.editRestHoursVisible = false;
                this.restHourLoading = false;
            },

            //查看积分记录
            clickHistory() {
                this.$router.push({path:'/index/IntegralHistory',query:{userId:this.userId}})
            },
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
    .hh.el-table td, .hh.el-table th {
        height: 25px;
        min-width: 0;
        text-overflow: ellipsis;
        vertical-align: middle;
    }
    .hh .el-tabs__header {
        border-bottom: 0;
    }
</style>
<style scoped>
    .el-dialog--small {
        width: 40%;
    }
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
        right: 240px;
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

    .task-form {
        margin-bottom: 0;
    }

    .ctpc-instruction-msg {
        padding: 4px;
        cursor: pointer;
    }

    .ctpc-instruction-msg:hover {
        color: #3DA8F5;
        background: #F5F5F5;
    }

    .ctpc-ins-edit {
        margin-right: 10px;
    }

    .ctpc-ins-edit .ctpc-btns {
        margin-top: 0;
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

    .trends {
        /*background-color: #f2f2f2; */
        /*padding-left: 10px;*/
        line-height: 30px;
        border: 1px solid #e4e8f1;

    }

    .trends-title {
        padding: 0 10px;
        line-height: 30px;
        background-color: #e4e8f1;
    }

    .steps-body {
        display: flex;
        flex-direction: row;
    }
    .rest-hour-log .el-dialog--tiny {
        width: 45%;
    }
</style>

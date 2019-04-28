<template>
    <div class="stats-con">
        <el-tabs v-model="activeName" @tab-click="" style="position:relative;margin-bottom: 20px;">
            <el-tab-pane label="任务统计" name="stat"  style="">
                <el-table :data="statsData" >
                    <el-table-column prop="name" label="成员" align="center" ></el-table-column>
                    <el-table-column prop="inProcess" label="我的任务/进行中任务" align="center" >
                        <template scope="sco">
                            <el-button type="text" @click="getTask(sco.$index)">{{sco.row.inProcess}} / {{sco.row.multiTask}}</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="hours" label="进行中任务耗时（小时）" align="center"></el-table-column>
                    <el-table-column prop="delay" label="超时任务" align="center" >
                        <template scope="scope">
                            <span type="text" style="color: red;">{{scope.row.delay}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="complete" label="已完成任务" align="center" ></el-table-column>
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="线上问题统计" name="bug"  style="">
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="bugReqDTO.userId" clearable filterable   placeholder="筛选用户">
                        <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </div>
                <div class="add-member-basic-msg fl"><el-date-picker
                        v-model="bugDaterange"
                        type="daterange"
                        placeholder="选择日期范围"
                        unlink-panels
                        @change="bugTimeChange1"
                        :picker-options="pickerOptions">
                </el-date-picker></div>
                <div class="add-member-basic-msg fl" ><img src="../assets/img/u1221.png" alt="" @click="fetchBugPage()" class="search-btn"></div>
                <el-button type="primary" style="margin-left: 300px;margin-bottom: 10px;" class="add-member-basic-msg fl" @click="openBugDialog" v-show="permit">创建bug处理</el-button>
                <div class="fr">
                    <span>bug: {{BugNumData.bugNum}}</span>
                    <span>优化: {{BugNumData.optimizationNum}}</span>
                    <span style="margin-right: 10px">协助: {{BugNumData.assistanceNum}}</span>
                </div>
                <el-table :data="bugPage" border>
                    <el-table-column type="index" label="序号" align="center" width="80">
                        <template scope="scope">
                            {{(bugReqDTO.pageNum-1)*10 + scope.$index + 1}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="origin" label="反馈人" align="center" width="130"></el-table-column>
                    <el-table-column prop="createTime" label="反馈日期"  width="130">
                        <template scope="scope">
                            <span>{{scope.row.discoverTime | formatDate1}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="processTime" label="解决日期"  width="130">
                        <template scope="scope">
                            <span>{{scope.row.processTime | formatDate1}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="demandSystemName" label="反馈系统"  width="130"></el-table-column>
                    <!--<el-table-column prop="accountInfo" label="账号信息"  width="130"></el-table-column>-->
                    <el-table-column prop="description" label="问题描述" align="center"></el-table-column>
                    <!--<el-table-column prop="developers" label="开发人员" align="center" width="130"></el-table-column>-->
                    <!--<el-table-column prop="testers" label="测试人员" align="center" width="130"></el-table-column>-->
                    <el-table-column prop="type" label="问题类型" align="center" width="110">
                        <template scope="scope">
                            <span v-if="scope.row.type == 0">bug</span>
                            <span v-if="scope.row.type == 1">优化</span>
                            <span v-if="scope.row.type == 2">协助</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="isSolved" label="是否解决" align="center" width="110">
                        <template scope="scope">
                            <span v-if="scope.row.isSolved == 0">未解决</span>
                            <span v-if="scope.row.isSolved == 1">已解决</span>
                        </template>
                    </el-table-column>
                    <!--<el-table-column prop="remark" label="备注" align="center" width="200"></el-table-column>-->
                    <el-table-column label="操作" width="110" fixed="right" align="center">
                        <template scope="scope">
                            <el-button @click="bugDetail(scope.row)" type="text" size="small" >查看</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination
                            @current-change="handleCurrentChange"
                            :current-page.sync="bugReqDTO.pageNum"
                            :page-size="bugFormPage.pageSize"
                            :layout="pageLayout"
                            :total="bugFormPage.total">
                    </el-pagination>
                </div>
            </el-tab-pane>
            <el-tab-pane label="测试问题统计" name="mantisBug" v-if="permit" style="">
                <div class="bug-stats-con">
                    <h1 style="font-size: 20px;margin-left: 10px;margin-top: -35px;margin-bottom: 10px;font-weight: bold;">任务bug统计</h1>
                    <div class="add-member-basic-msg fl"><el-date-picker
                            v-model="yearMonth3"
                            type="month"
                            placeholder="选择月份"
                            @change="changeMonth3"
                    size="medium">
                    </el-date-picker></div>
                    <el-button type="primary" style="margin-left: 850px;margin-top: -5px;" @click="selectMantisProject">导入bug信息</el-button>
                    <el-table :data="taskBugStatsList" border>
                        <el-table-column prop="taskName" label="任务名称" align="center"></el-table-column>
                        <el-table-column prop="totalBugNum" label="bug数量" width="120"></el-table-column>
                        <el-table-column label="测试提交数量"  width="200">
                            <template scope="scope">
                                <div v-for="testerData in scope.row.mantisBugTesterNumResDTOList">
                                    <span>{{testerData.userName}} {{testerData.bugNum}}个</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="开发解决数量" width="200">
                            <template scope="scope">
                                <div v-for="developerData in scope.row.mantisBugDeveloperNumResDTOList">
                                    <span>{{developerData.userName}} {{developerData.bugNum}}个</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="按严重程度划分" width="200">
                            <template scope="scope">
                                <div v-for="severity in scope.row.mantisBugSeverityNumResDTOList">
                                    <span>{{severity.severityName}} {{severity.bugNum}}个</span>
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
                    <div class="pagination">
                        <el-pagination
                                @current-change="handleTaskBugChange"
                                :current-page.sync="mantisBugReqDTO4.pageNum"
                                :page-size="taskBugPage.pageSize"
                                :layout="taskPageLayout"
                                :total="taskBugPage.total">
                        </el-pagination>
                    </div>
                </div>
                <div class="bug-stats-con">
                    <h1 style="font-size: 20px;margin-left: 10px;margin-top: -25px;margin-bottom: 10px;font-weight: bold;">测试bug统计</h1>
                    <div class="add-member-basic-msg fl"><el-date-picker
                            v-model="yearMonth1"
                            type="month"
                            placeholder="选择月份"
                            @change="changeMonth1"
                    size="medium">
                    </el-date-picker></div>
                    <el-table :data="mantisUserBugStatsList" border>
                        <el-table-column prop="realName" label="负责人" align="center" width="100"></el-table-column>
                        <el-table-column label="负责平台"  width="240">
                            <template scope="scope">
                                <div v-for="category in scope.row.categoryResDTOList">
                                    <span>{{category.name}}</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="mantisBugTotalNum" label="bug数量" width="100"></el-table-column>
                        <el-table-column label="按严重程度划分" width="140">
                            <template scope="scope">
                                <div v-for="severity in scope.row.mantisBugSeverityNumResDTOList">
                                    <span>{{severity.severityName}} {{severity.bugNum}}个</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="按状态划分" width="130">
                            <template scope="scope">
                                <div v-for="status in scope.row.mantisBugStatusNumResDTOList">
                                    <span>{{status.statusName}} {{status.bugNum}}个</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="按分类划分">
                            <template scope="scope">
                                <div v-for="category in scope.row.mantisBugCategoryNumResDTOList">
                                    <span v-if="category.isInCharge == 1" style="color: red">{{category.categoryName}} {{category.bugNum}}个</span>
                                    <span v-else>{{category.categoryName}} {{category.bugNum}}个</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="线上bug统计">
                            <template scope="scope">
                                <div v-for="demandSystem in scope.row.onlineBugCategoryNumResDTOList">
                                    <span v-if="demandSystem.isInCharge == 1" style="color: red">{{demandSystem.demandSystemName}} {{demandSystem.bugNum}}个</span>
                                    <span v-else>{{demandSystem.demandSystemName}} {{demandSystem.bugNum}}个</span>
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
                <div class="bug-stats-con" style="height: 500px;">
                    <div class="add-member-basic-msg fl"><el-date-picker
                            v-model="yearMonth4"
                            type="month"
                            placeholder="选择月份"
                            @change="changeMonth4"
                    >
                    </el-date-picker></div>
                    <div id="myChart8" :style="{width:'1000px',height:'400px',left:'40px',float:'left',marginTop:'20px'}"></div>
                </div>
                <div class="bug-stats-con" style="height: 500px;">
                    <div class="add-member-basic-msg fl"><el-date-picker
                            v-model="yearMonth2"
                            type="month"
                            placeholder="选择月份"
                            @change="changeMonth2"
                            size="medium">
                    </el-date-picker></div>
                    <div id="myChart9" :style="{width:'1000px',height:'400px',left:'40px',float:'left',marginTop:'20px'}"></div>
                </div>
            </el-tab-pane>
            <el-tab-pane label="个人任务" name="personal">
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="persanalForm.userId" clearable filterable   placeholder="筛选用户">
                        <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </div>
                <div class="add-member-basic-msg fl"><el-date-picker
                        v-model="daterange"
                        type="daterange"
                        placeholder="选择日期范围"
                        range-separator=" 至 "
                        @change="timeChange"
                        :picker-options="pickerOptions">
                </el-date-picker></div>
                <div class="add-member-basic-msg fl" ><img src="../assets/img/u1221.png" alt="" @click="getPersonalData()" class="search-btn"></div>
                <el-table :data="pesonalTaskData" border :summary-method="getSummaries" show-summary>
                    <el-table-column prop="id" label="序号" align="center" width="80"></el-table-column>
                    <el-table-column prop="taskName" label="任务名称" align="center" width="150">
                        <template scope="sco">
                            <a style="color:#20a0ff;cursor: pointer;"  @click="getPesonTask(sco.row.taskId)">{{sco.row.taskName}}</a>
                        </template>
                    </el-table-column>
                    <el-table-column prop="taskDescription" label="任务描述" align="center" ></el-table-column>
                    <el-table-column prop="description" label="工作内容" align="center"></el-table-column>
                    <el-table-column prop="createTime" label="开始日期"  width="120"></el-table-column>
                    <el-table-column prop="endTime" label="截止日期"  width="120"></el-table-column>
                    <el-table-column prop="taskHours" label="工作量"  width="80"></el-table-column>
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="周任务统计" name="week">
                <div class="add-member-basic-msg fl" >
                    <el-date-picker
                            v-model="userWeekForm.date"
                            :picker-options="pickerWeek"
                            type="week"
                            format="yyyy 第 WW 周"
                            placeholder="选择周">
                    </el-date-picker>
                </div>
                <div class="add-member-basic-msg fl"><el-button type="text" @click="getCurrentWeek()">当前第{{currentWeek}}周</el-button></div>
                <div class="add-member-basic-msg fl" ><img src="../assets/img/u1221.png" alt="" @click="getUserWeekStats()" class="search-btn"></div>
                <el-table :data="userWeekData" border  >
                    <el-table-column  type="index"  label="序号"  width="80"></el-table-column>
                    <el-table-column prop="userName" label="用户" align="center" width="80" >
                        <template scope="sco">
                            <a style="color:#20a0ff;cursor: pointer;"  @click="getPesonStats(sco.row.userId)">{{sco.row.userName}}</a>
                        </template>
                    </el-table-column>
                    <el-table-column prop="taskName" label="任务名称" align="center">
                        <template scope="sco">
                            <div style="white-space: pre-wrap;text-align: left">{{sco.row.taskName}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="hours" label="周工作量"  sortable  width="120">
                        <template scope="scope">
                            <span type="text" v-show="scope.row.hours<=40">{{scope.row.hours}}</span>
                            <span type="text" style="color: red;" v-show="scope.row.hours>40">{{scope.row.hours}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="leaveHours" label="请假时间"  width="100"></el-table-column>
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="请假统计" name="leave"  style="" v-if="admin" >
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="leaveList.userId" clearable filterable   placeholder="筛选用户">
                        <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </div>
                <div class="add-member-basic-msg fl"><el-date-picker
                        v-model="leaveDaterange"
                        type="daterange"
                        placeholder="选择日期范围"
                        unlink-panels
                        @change="leaveTimeChange"
                        :picker-options="pickerOptions">
                </el-date-picker></div>
                <div class="add-member-basic-msg fl" ><img src="../assets/img/u1221.png" alt="" @click="getLeaveList()" class="search-btn"></div>
                <el-table :data="leaveManage" border>
                    <el-table-column type="index" label="序号" width="80"></el-table-column>
                    <el-table-column prop="description" label="请假原因" align="center"></el-table-column>
                    <el-table-column prop="userName" label="请假人" align="center" width="130"></el-table-column>
                    <el-table-column prop="hours" label="时长" align="center" width="80"></el-table-column>
                    <el-table-column prop="typeName" label="类型" align="center" width="80"></el-table-column>
                    <el-table-column prop="beginTime" label="开始日期"  width="150"  align="center">
                        <template scope="scope">
                            <div type="text" size="small" >{{scope.row.beginTime | formatDate}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="endTime" label="结束日期"  width="150"  align="center">
                        <template scope="scope">
                            <div type="text" size="small" >{{scope.row.endTime | formatDate}}</div>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination
                            @current-change="leaveHandleCurrentChange"
                            :current-page.sync="leaveList.pageNum"
                            :page-size="leaveFormPage.pageSize"
                            :layout="leavePageLayout"
                            :total="leaveFormPage.total">
                    </el-pagination>
                </div>
            </el-tab-pane>
            <el-tab-pane label="年度总结" name="task"  style="" v-if="admin" >
                <div class="stats-con">
                    <div class="steps" style="width: 1000px">
                        <div class="card-title-con" style="font-size: 20px;margin-top: -30px;">
                            年度任务统计
                            <el-date-picker
                                    style="margin-left: 20px"
                                    v-model="whichYear1"
                                    align="right"
                                    type="year"
                                    placeholder="选择年">
                            </el-date-picker>
                            <el-button type="primary" @click="selectTaskByYear">查询</el-button>
                        </div>
                        <div class="steps-body">
                            <div id="myChart1" :style="{width:'500px',height:'400px',left:'40px',float:'left'}"></div>
                            <div id="myChart2" :style="{width:'500px',height:'400px',left:'40px',float:'left'}"></div>
                        </div>
                    </div>
                </div>
                <div class="stats-con">
                    <div class="steps" style="width: 1000px">
                        <div class="card-title-con" style="font-size: 20px;margin-top: -20px;">
                            年度需求统计
                            <el-date-picker
                                    style="margin-left: 20px"
                                    v-model="whichYear2"
                                    align="right"
                                    type="year"
                                    placeholder="选择年">
                            </el-date-picker>
                            <el-button type="primary" @click="selectFeedbackByYear">查询</el-button>
                        </div>
                        <div class="steps-body">
                            <div id="myChart3" :style="{width:'500px',height:'400px',left:'80px',float:'left',marginTop:'50px'}"></div>
                            <div id="myChart6" :style="{width:'500px',height:'400px',left:'80px',float:'left',marginTop:'50px'}"></div>
                            <!--<div v-else class="steps-body" style="height: 300px">
                                <h1 style="text-align: center;width: 100%;font-size: 50px">暂无数据</h1>
                            </div>-->
                        </div>
                    </div>
                </div>
                <div class="stats-con">
                    <div class="steps" style="width: 1000px">
                        <div class="card-title-con" style="font-size: 20px;margin-top: -20px;">
                            每月任务统计
                            <el-date-picker
                                    style="margin-left: 20px"
                                    v-model="whichYear4"
                                    align="right"
                                    type="year"
                                    placeholder="选择年">
                            </el-date-picker>
                            <el-button type="primary" @click="selectTaskByYear4">查询</el-button>
                        </div>
                        <div class="steps-body">
                            <div id="myChart4" :style="{width:'600px',height:'400px',left:'230px',float:'left',marginTop:'50px'}"></div>
                            <!--<div v-else class="steps-body" style="height: 300px">
                                <h1 style="text-align: center;width: 100%;font-size: 50px">暂无数据</h1>
                            </div>-->
                        </div>
                    </div>
                </div>
                <div class="stats-con">
                    <div class="steps" style="width: 1000px">
                        <div class="card-title-con" style="font-size: 20px;margin-top: -20px;margin-left: 20px;">
                            年度请假分析
                            <el-date-picker
                                    style="margin-left: 20px"
                                    v-model="whichYear5"
                                    align="right"
                                    type="year"
                                    placeholder="选择年">
                            </el-date-picker>
                            <el-button type="primary" @click="selectVacationByYear">查询</el-button>
                        </div>
                        <div class="steps-body" style="height: 600px;margin-left: 120px">
                            <div id="myChart5" :style="{width:'600px',height:'400px',left:'80px',float:'left',marginTop:'50px'}"></div>
                            <div style="width: 300px;margin-left: 100px">
                                <ol type="A" style="margin-left: -250px">
                                    <li><span class="task-span">年度请假总数:</span><span style="margin-left: 71px">{{vacationData.totalCount}}次</span></li>
                                    <li><span class="task-span">年度请假时长:</span><span style="margin-left: 71px">{{vacationData.totalTime}}小时</span></li>
                                </ol>
                            </div>
                        </div>
                        <!--<div v-else class="steps-body" style="height: 300px">
                            <h1 style="text-align: center;width: 100%;font-size: 50px">暂无数据</h1>
                        </div>-->
                    </div>
                </div>
                <div class="stats-con">
                    <div class="steps" style="width: 1000px">
                        <div class="card-title-con" style="font-size: 20px;margin-top: -30px;">
                            年度多人任务耗时统计
                            <el-date-picker
                                    style="margin-left: 20px"
                                    v-model="whichYear7"
                                    align="right"
                                    type="year"
                                    placeholder="选择年">
                            </el-date-picker>
                            <el-button type="primary" @click="selectTaskTimeByYear">查询</el-button>
                        </div>
                        <div class="steps-body">
                            <div id="myChart7" :style="{width:'600px',height:'400px',left:'40px',float:'left',marginTop:'30px'}"></div>
                            <div style="width: 300px;margin-top: 100px;margin-left: 100px">
                                <div style="font-size: 16px;margin-bottom: 20px;color: black">最耗时的任务Top10</div>
                                <ol v-for="task in top10TaskList">
                                    <a style="cursor: pointer;text-decoration: underline" @click="fetchTaskDetail(task.id)"><li>{{task.name}}</li></a>
                                </ol>
                            </div>
                        </div>
                    </div>
                </div>
            </el-tab-pane>
            <el-tab-pane label="个人请假统计" name="personalLeave" v-if="admin">
                <span class="fl" style="font-size: 15px;margin-top: 5px;margin-left: 10px;color: #1d90e6">请假时间:</span>
                <div class="add-member-basic-msg fl">
                    <el-date-picker
                            v-model="beginTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="选择日期"
                    >
                    </el-date-picker>
                    <span style="font-size: 14px;color: #606266;">-</span>
                    <el-date-picker
                            v-model="endTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="选择日期"
                    >
                    </el-date-picker>
                </div>
                <el-button type="primary" @click="selectVacationByDate" style="margin-left: 10px" size="small">查询</el-button>
                <el-table :data="personVacationData" border>
                    <el-table-column type="index" label="序号" width="80"></el-table-column>
                    <el-table-column prop="userName" label="用户" align="center" width="80">
                        <template scope="scope">
                            {{scope.row.userName}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="remark" label="备注" align="left">
                        <template scope="scope">
                            {{scope.row.remarkList}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="vacationNum" label="请假次数" align="center" width="120" sortable></el-table-column>
                    <el-table-column prop="vacationTime" label="请假时长" align="center" width="120" sortable></el-table-column>
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="考勤统计" name="signIn" v-if="admin">
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
            </el-tab-pane>
        </el-tabs>
        <el-dialog
                title="创建Bug处理结果"
                style="width:auto;"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="createBugSolvingVisible">
                <div class="ctpc-con">
                    <div  style="display: inline"><span class="star">*</span>问题项目</div>
                    <div style="display: inline;margin-left: 30px">
                        <el-select v-model="bugForm.projectId" placeholder="请选择">
                            <el-option  v-for="item in projectForm" :key="item.id" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </div>
                    <div style="margin-top: 20px"><span class="star">*</span>问题描述</div>
                        <el-input type="textarea" style="position: relative;margin-left: 100px;margin-top:-20px;width: 80%" v-model="bugForm.description" :rows="3"></el-input>
                    <div style="margin-top: 20px;margin-bottom: -20px"><span class="star">*</span>	发现日期</div>
                        <el-date-picker v-model="bugForm.createTime" type="date" placeholder="选择发现日期" style="position: relative;margin-left: 100px"></el-date-picker>
                    <div style="margin-top: 20px;margin-bottom: -20px"><span class="star">*</span>	处理日期</div>
                        <el-date-picker v-model="bugForm.processTime" type="date" placeholder="选择处理日期" style="position: relative;margin-left: 100px"></el-date-picker>
                </div>

                <div class="ctpc-member-con">
                    <div class="ctpc-member-list clearfix in" v-for="(item,index) in bugUsers"  :class="item.cssClass">
                        <span class="fl ctpc-member-head">{{item.userName}}</span>
                        <span class="fl ctpc-member-job-time">积分:{{item.integral}}</span>
                        <span style="position: absolute;right: 10px;">
                                <el-button type="text" icon="edit" @click="modifyMember(index,bugUsers)"></el-button>
                            <el-button type="text" icon="close" @click="deleteMember(index)"></el-button>
                        </span>
                    </div>
                </div>
                <div class="ctpc-add-member-detail" v-if="showAddDetail">
                    <div class="add-member-basic">
                        <div class="add-member-basic-list clearfix">
                            <div class="add-member-basic-menu fl"><span class="star">*</span>姓名：</div>
                            <div class="add-member-basic-msg fl">
                                <el-select v-model="addMemberIndex.userId" filterable  placeholder="请选择" @change="stepUserChange">
                                    <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>积分：
                            </div>
                            <div class="add-member-basic-msg fl">
                                <input class="member-time-count" v-model="addMemberIndex.integral" :maxlength="6" style="width:80px">
                            </div>
                        </div>
                    </div>
                    <div class="ctpc-btns">
                        <input type="button" class="ctpc-cancel" @click="cancelAddMember" value="取消">
                        <input type="button" class="ctpc-save" @click="saveAddMember" value="确定">
                    </div>
                </div>
                <div class="add-member-opt" v-show="!showAddDetail" @click="showAddDetail = !showAddDetail;">
                    <span class="add-member-icon">+</span>
                    <span class="add-member-msg" style="">添加成员</span>
                </div>
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="saveBugForm('bugForm')" :loading="isSaving">立即创建</el-button>
            <el-button @click="createBugSolvingVisible = false">取 消</el-button>
          </span>
        </el-dialog>


        <el-dialog
                @close="closeDialog()"
                title="创建Bug处理结果"
                style="width:auto;"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="createBugSolvingVisible1">
                <div class="ctpc-con">

                    <div style="margin-top: 10px;float: left">
                        <div  style="display: inline"><span class="star">*</span>反馈人</div>
                        <div style="display: inline;margin-left: 44px">
                            <el-select v-model="onlineBugForm.origin" placeholder="请选择">
                                <el-option  v-for="item in originList" :key="item.id" :label="item.name" :value="item.name"></el-option>
                            </el-select>
                        </div>
                    </div>
                    <div style="margin-top: 10px;float: left;margin-left: 10px">
                        <div  style="display: inline"><span class="star">*</span>问题类型</div>
                        <div style="display: inline;margin-left: 30px">
                            <el-select v-model="onlineBugForm.type" placeholder="请选择">
                                <el-option  v-for="item in typeList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                            </el-select>
                        </div>
                    </div>

                    <div style="margin-top: 10px;float: left">
                        <div  style="display: inline"><span class="star">*</span>是否解决</div>
                        <div style="display: inline;margin-left: 30px">
                            <el-select v-model="onlineBugForm.isSolved" placeholder="请选择">
                                <el-option  v-for="item in solveList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                            </el-select>
                        </div>
                    </div>
                    <div style="margin-top: 10px;float: left;margin-left: 10px">
                        <div  style="display: inline">反馈系统</div>
                        <div style="display: inline;margin-left: 40px">
                            <el-select v-model="onlineBugForm.demandSystemId" placeholder="请选择">
                                <el-option  v-for="item in demandSystemList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                            </el-select>
                        </div>
                    </div>
                    <div style="margin-top: -10px;float: left">
                        <div style="margin-top: 20px;margin-bottom: -20px"><span class="star">*</span>发现日期</div>
                        <el-date-picker
                                v-model="onlineBugForm.discoverTime"
                                type="date"
                                placeholder="选择发现日期"
                                style="position: relative;margin-left: 100px">
                        </el-date-picker>
                    </div>
                    <div style="margin-top: -10px;float: left;margin-left: 33px">
                        <div style="margin-top: 20px;margin-bottom: -20px">	处理日期</div>
                        <el-date-picker
                                v-model="onlineBugForm.processTime"
                                type="date"
                                placeholder="未处理无需填写"
                                style="position: relative;margin-left: 100px">
                        </el-date-picker>
                    </div>
                    <div>
                        <div style="margin-top: 150px"><span class="star">*</span>问题描述</div>
                        <el-input type="textarea" style="position: relative;margin-left: 100px;margin-top:-20px;width: 80%" v-model="onlineBugForm.description" :rows="2"></el-input>
                    </div>
                    <div>
                        <div style="margin-top: 10px">账号信息</div>
                        <el-input type="textarea" style="position: relative;margin-left: 100px;margin-top:-20px;width: 80%" v-model="onlineBugForm.accountInfo" :rows="2"></el-input>
                    </div>
                    <div>
                        <div style="margin-top: 10px">备注</div>
                        <el-input type="textarea" style="position: relative;margin-left: 100px;margin-top:-20px;width: 80%" v-model="onlineBugForm.remark" :rows="2"></el-input>
                    </div>
                </div>

                <div class="ctpc-member-con">
                    <div class="ctpc-member-list clearfix in" v-for="(item,index) in bugUsers"  :class="item.cssClass">
                        <span class="fl ctpc-member-head">{{item.userName}}</span>
                        <span class="fl ctpc-member-job-time">积分:{{item.integral}}</span>
                        <span style="position: absolute;right: 10px;">
                                <el-button type="text" icon="edit" @click="modifyMember(index,bugUsers)"></el-button>
                            <el-button type="text" icon="close" @click="deleteMember(index)"></el-button>
                        </span>
                    </div>
                </div>
                <div class="ctpc-add-member-detail" v-if="showAddDetail">
                    <div class="add-member-basic">
                        <div class="add-member-basic-list clearfix">
                            <div class="add-member-basic-menu fl"><span class="star">*</span>姓名：</div>
                            <div class="add-member-basic-msg fl">
                                <el-select v-model="addMemberIndex.userId" filterable  placeholder="请选择" @change="stepUserChange">
                                    <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                               :value="item.id"></el-option>
                                </el-select>
                            </div>
                            <div class="add-member-basic-menu add-member-basic-time fl">积分：
                            </div>
                            <div class="add-member-basic-msg fl">
                                <input class="member-time-count" v-model="addMemberIndex.integral" :maxlength="6" style="width:80px">
                            </div>
                        </div>
                    </div>
                    <div class="ctpc-btns">
                        <input type="button" class="ctpc-cancel" @click="cancelAddMember" value="取消">
                        <input type="button" class="ctpc-save" @click="saveAddMember" value="确定">
                    </div>
                </div>
                <div class="add-member-opt" v-show="!showAddDetail" @click="showAddDetail = !showAddDetail;">
                    <span class="add-member-icon">+</span>
                    <span class="add-member-msg" style="">添加成员</span>
                </div>
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="saveOnlineBugForm()" :loading="isSaving">立即创建</el-button>
            <el-button @click="createBugSolvingVisible1 = false">取 消</el-button>
          </span>
        </el-dialog>
        <el-dialog
                title="更新Bug处理"
                style="width:auto;"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="updateBugSolvingVisible">
            <div class="ctpc-con">
                <div  style="display: inline"><span class="star">*</span>问题项目</div>
                <div style="display: inline;margin-left: 30px">
                    <el-select v-model="bugForm.projectId" placeholder="请选择">
                        <el-option  v-for="item in projectForm" :key="item.id" :label="item.name" :value="item.id"></el-option>
                    </el-select>
                </div>
                <div style="margin-top: 20px"><span class="star">*</span>问题描述</div>
                <el-input type="textarea" style="position: relative;margin-left: 100px;margin-top:-20px;width: 80%" v-model="bugForm.description" :rows="3"></el-input>
                <div style="margin-top: 20px;margin-bottom: -20px"><span class="star">*</span>	发现日期</div>
                <el-date-picker v-model="bugForm.createTime" type="date" placeholder="选择发现日期" style="position: relative;margin-left: 100px"></el-date-picker>
                <div style="margin-top: 20px;margin-bottom: -20px"><span class="star">*</span>	处理日期</div>
                <el-date-picker v-model="bugForm.processTime" type="date" placeholder="选择处理日期" style="position: relative;margin-left: 100px"></el-date-picker>
            </div>

            <div class="ctpc-member-con">
                <div class="ctpc-member-list clearfix in" v-for="(item,index) in bugUsers"  :class="item.cssClass">
                    <span class="fl ctpc-member-head">{{item.userName}}</span>
                    <span class="fl ctpc-member-job-time">积分:{{item.integral}}</span>
                    <span style="position: absolute;right: 10px;">
                                <el-button type="text" icon="edit" @click="modifyMember(index,bugUsers)"></el-button>
                            <el-button type="text" icon="close" @click="deleteMember(index)"></el-button>
                        </span>
                </div>
            </div>
            <div class="ctpc-add-member-detail" v-if="showAddDetail">
                <div class="add-member-basic">
                    <div class="add-member-basic-list clearfix">
                        <div class="add-member-basic-menu fl"><span class="star">*</span>姓名：</div>
                        <div class="add-member-basic-msg fl">
                            <el-select v-model="addMemberIndex.userId" filterable  placeholder="请选择" @change="stepUserChange">
                                <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                        <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>积分：
                        </div>
                        <div class="add-member-basic-msg fl">
                            <input class="member-time-count" v-model="addMemberIndex.integral" :maxlength="6" style="width:80px">
                        </div>
                    </div>
                </div>
                <div class="ctpc-btns">
                    <input type="button" class="ctpc-cancel" @click="cancelAddMember" value="取消">
                    <input type="button" class="ctpc-save" @click="saveAddMember" value="确定">
                </div>
            </div>
            <div class="add-member-opt" v-show="!showAddDetail" @click="showAddDetail = !showAddDetail;">
                <span class="add-member-icon">+</span>
                <span class="add-member-msg" style="">添加成员</span>
            </div>
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="editBugForm('bugForm')">立即更新</el-button>
            <el-button @click="updateBugSolvingVisible = false">取 消</el-button>
          </span>
        </el-dialog>
        <el-dialog
                @close="closeDialog()"
                title="更新Bug处理"
                style="width:auto;"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="updateBugSolvingVisible1">
            <div class="ctpc-con">
                <div style="margin-top: 10px;float: left">
                    <div  style="display: inline"><span class="star">*</span>反馈人</div>
                    <div style="display: inline;margin-left: 44px">
                        <el-select v-model="onlineBugForm.origin" placeholder="请选择">
                            <el-option  v-for="item in originList" :key="item.id" :label="item.name" :value="item.name"></el-option>
                        </el-select>
                    </div>
                </div>
                <div style="margin-top: 10px;float: left;margin-left: 10px">
                    <div  style="display: inline"><span class="star">*</span>问题类型</div>
                    <div style="display: inline;margin-left: 30px">
                        <el-select v-model="onlineBugForm.type" placeholder="请选择">
                            <el-option  v-for="item in typeList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </div>
                </div>

                <div style="margin-top: 10px;float: left">
                    <div  style="display: inline"><span class="star">*</span>是否解决</div>
                    <div style="display: inline;margin-left: 30px">
                        <el-select v-model="onlineBugForm.isSolved" placeholder="请选择">
                            <el-option  v-for="item in solveList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </div>
                </div>
                <div style="margin-top: 10px;float: left;margin-left: 10px">
                    <div  style="display: inline">反馈系统</div>
                    <div style="display: inline;margin-left: 40px">
                        <el-select v-model="onlineBugForm.demandSystemId" placeholder="请选择">
                            <el-option  v-for="item in demandSystemList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </div>
                </div>
                <div style="margin-top: -10px;float: left">
                    <div style="margin-top: 20px;margin-bottom: -20px"><span class="star">*</span>发现日期</div>
                    <el-date-picker
                            v-model="onlineBugForm.discoverTime"
                            type="date"
                            placeholder="选择发现日期"
                            style="position: relative;margin-left: 100px">
                    </el-date-picker>
                </div>
                <div style="margin-top: -10px;float: left;margin-left: 33px">
                    <div style="margin-top: 20px;margin-bottom: -20px">	处理日期</div>
                    <el-date-picker
                            v-model="onlineBugForm.processTime"
                            type="date"
                            placeholder="未处理无需填写"
                            style="position: relative;margin-left: 100px">
                    </el-date-picker>
                </div>
                <div>
                    <div style="margin-top: 150px"><span class="star">*</span>问题描述</div>
                    <el-input type="textarea" style="position: relative;margin-left: 100px;margin-top:-20px;width: 80%" v-model="onlineBugForm.description" :rows="2"></el-input>
                </div>
                <div>
                    <div style="margin-top: 10px">账号信息</div>
                    <el-input type="textarea" style="position: relative;margin-left: 100px;margin-top:-20px;width: 80%" v-model="onlineBugForm.accountInfo" :rows="2"></el-input>
                </div>
                <div>
                    <div style="margin-top: 10px">备注</div>
                    <el-input type="textarea" style="position: relative;margin-left: 100px;margin-top:-20px;width: 80%" v-model="onlineBugForm.remark" :rows="2"></el-input>
                </div>
            </div>

            <div class="ctpc-member-con">
                <div class="ctpc-member-list clearfix in" v-for="(item,index) in bugUsers"  :class="item.cssClass">
                    <span class="fl ctpc-member-head">{{item.userName}}</span>
                    <span class="fl ctpc-member-job-time">积分:{{item.integral}}</span>
                    <span style="position: absolute;right: 10px;">
                                <el-button type="text" icon="edit" @click="modifyMember(index,bugUsers)"></el-button>
                            <el-button type="text" icon="close" @click="deleteMember(index)"></el-button>
                        </span>
                </div>
            </div>
            <div class="ctpc-add-member-detail" v-if="showAddDetail">
                <div class="add-member-basic">
                    <div class="add-member-basic-list clearfix">
                        <div class="add-member-basic-menu fl"><span class="star">*</span>姓名：</div>
                        <div class="add-member-basic-msg fl">
                            <el-select v-model="addMemberIndex.userId" filterable  placeholder="请选择" @change="stepUserChange">
                                <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                        <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>积分：
                        </div>
                        <div class="add-member-basic-msg fl">
                            <input class="member-time-count" v-model="addMemberIndex.integral" :maxlength="6" style="width:80px">
                        </div>
                    </div>
                </div>
                <div class="ctpc-btns">
                    <input type="button" class="ctpc-cancel" @click="cancelAddMember" value="取消">
                    <input type="button" class="ctpc-save" @click="saveAddMember" value="确定">
                </div>
            </div>
            <div class="add-member-opt" v-show="!showAddDetail" @click="showAddDetail = !showAddDetail;">
                <span class="add-member-icon">+</span>
                <span class="add-member-msg" style="">添加成员</span>
            </div>
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="editBugForm1()">立即更新</el-button>
            <el-button @click="updateBugSolvingVisible1 = false,showAddDetail = false">取 消</el-button>
          </span>
        </el-dialog>

        <el-dialog title="Bug处理结果详情" size="tiny" :close-on-click-modal="false"
            :close-on-press-escape="false"
            :visible.sync="bugDetailVisible">
            <el-form>
                <el-form-item class="task-form" label="反馈人：">{{bugDetailForm.origin}}</el-form-item>
                <el-form-item class="task-form" label="反馈日期：">{{bugDetailForm.discoverTime | formatDate1}}</el-form-item>
                <el-form-item class="task-form" label="解决日期：">{{bugDetailForm.processTime | formatDate1}}</el-form-item>
                <el-form-item class="task-form" label="反馈系统：">{{bugDetailForm.demandSystemName}}</el-form-item>
                <el-form-item class="task-form" label="账号信息：">{{bugDetailForm.accountInfo}}</el-form-item>
                <el-form-item class="task-form" label="问题描述：">{{bugDetailForm.description}}</el-form-item>
                <el-form-item class="task-form" label="问题类型：">
                    <template scope="scope">
                        <span v-if="bugDetailForm.type == 0">bug</span>
                        <span v-if="bugDetailForm.type == 1">优化</span>
                        <span v-if="bugDetailForm.type == 2">协助</span>
                    </template>
                </el-form-item>
                <el-form-item class="task-form" label="是否解决：">
                    <template scope="scope">
                        <span v-if="bugDetailForm.isSolved == 0">未解决</span>
                        <span v-if="bugDetailForm.isSolved == 1">已解决</span>
                    </template>
                </el-form-item>
                <el-form-item class="task-form" label="备注：">
                    {{bugDetailForm.remark}}
                </el-form-item>
                <div class="ctpc-member-list clearfix"  v-for="(item,index) in bugDetailForm.bugUsers">
                    <span class="fl ctpc-member-head">{{item.userName}}</span>
                    <span class="fl ctpc-member-job-time">积分:{{item.integral}}</span>
                </div>
            </el-form>
            <span slot="footer" class="dialog-footer" >
                <div v-show="permit">
                    <el-tooltip content="编辑该任务" placement="top">
                     <el-button type="primary" icon="edit" @click="editBugDetail1(bugDetailForm)"></el-button>
                    </el-tooltip>
                    <el-tooltip content="删除该任务" placement="top">
                          <el-button type="danger" icon="delete" @click="deleteBug()"></el-button>
                    </el-tooltip>
                </div>
            </span>

        </el-dialog>

        <el-dialog
                title="任务详情"
                top="10%"
                :visible.sync="showTaskVisible"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                custom-class="myDialog"
                size="tiny">
            <el-form>
                <el-form-item class="task-form" label="任务名称：">{{taskDetail.name}}</el-form-item>
                <!--<el-form-item class="task-form" label="任务描述：">{{taskDetail.description}}</el-form-item>-->
                <el-form-item class="task-form" label="项目：">{{taskDetail.projectName}}</el-form-item>
                <el-form-item class="task-form" label="阶段：">{{taskDetail.stageName}}</el-form-item>
                <el-form-item class="task-form"  label="优先级："><span v-for="item in priorityList"
                                                                    v-if="item.value == taskDetail.priority">{{item.label}}</span>
                </el-form-item>
                <el-form-item class="task-form" label="开始时间：">{{taskDetail.createTime | formatDate2}}</el-form-item>
                <el-form-item class="task-form" label="截止时间：">{{taskDetail.endTime | formatDate2}}</el-form-item>
                <el-form-item class="task-form" label="完成时间：">{{taskDetail.completeTime | formatDate2}}</el-form-item>
                <el-form-item label="任务总时长" >{{taskTotalTime}}小时</el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="toTask(taskDetail.id)">跳转到任务</el-button>
            </span>
        </el-dialog>

        <el-dialog title="计算加班总时长" :visible.sync="showTotalEWrokTime" custom-class="myDialog"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="tiny"
                   @close="closeEWorkCounter">
            <el-select clearable filterable no-match-text=" " v-model="workMonth1" placeholder="不选择及查询本年度"
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

        <el-dialog title="选择mantis系统项目" :visible.sync="selectMantisProjectVisible" custom-class="myDialog"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="tiny"
                   @close="closeMantisVisible">
            <el-select clearable filterable no-match-text=" " v-model="mantisProject" placeholder="请选择"
                       style="width:200px">
                <el-option v-for="item in mantisProjectList" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
            </el-select>
            <span slot="footer" class="dialog-footer">
                <el-button type="success" @click="importBugInfo">导入</el-button>
            </span>
        </el-dialog>

    </div>
</template>
<script>
    import Http from '../lib/Http'
    import Helper from '../lib/Helper'
    import ElButton from "../../node_modules/element-ui/packages/button/src/button";
    import ElDialog from "../../node_modules/element-ui/packages/dialog/src/component";
    import moment from 'moment';
    import helper from '../lib/Helper'
    import ElTabPane from "../../node_modules/element-ui/packages/tabs/src/tab-pane";
    import Task from "./Task"

    export default {
        components: {
            ElButton,
            ElTabPane},
        name: 'IntegralHistory',
        data() {
            return {
                isSaving:false,
                activeName:'stat',
                createBugSolvingVisible:false,
                createBugSolvingVisible1:false,
                updateBugSolvingVisible:false,
                updateBugSolvingVisible1:false,
                statsData:[],
                pesonalTaskData:[],
                modifyId:'',
                bugList:{
                  userId:'',
                  startTime:'',
                  endTime:'',
                  pageNum:1,
                },
                leaveList:{
                    userId:'',
                    beginTime:'',
                    endTime:'',
                    pageNum:1,
                },
                bugFormPage:{
                    pageSize: 10,
                    total: 0,
                },
                leaveFormPage:{
                    pageSize: 10,
                    total: 0,
                },
                bugForm:{
                    projectId:'',
                    description:'',
                    createTime:'',
                    processTime:''
                },
                persanalForm:{
                    userId:'',
                    startTime:'',
                    endTime:'',
                },
                daterange:'',
                bugDaterange:'',
                leaveDaterange:'',
                bugDetailForm:{},
                bugManage:[],
                leaveManage:[],
                addMemberIndex:{
                    index:'',
                    userId:'',
                    integral:0,
                    userName:'',
                },
                stepTemp: {},
                bugDetailVisible:false,
                projectForm:[],
                userList:[],
                checkInUsers:[],
                bugUsers:[],
                showAddDetail:false,
                statsPage: {
                    layout: "total, pager",
                    currentPage: 1,
                    pageSize: 10,
                    totals: 0,
                    pageNum: 0
                },
                userWeekForm:{
                    weekNumber:'',
                    date:''
                },
                userWeekData:[],
                currentWeek:moment().week(),
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
                pickerWeek:{
                    firstDayOfWeek:1
                },

                // sch --
                whichYear1:'2018',
                whichYear2:'2018',
                whichYear4:'2018',
                whichYear5:'2018',
                whichYear7:'2018',
                taskReqDTO:{
                    whichYear:'2018'
                },
                fbReqDTO:{
                    whichYear:'2018'
                },
                taskMonthReqDTO:{
                    whichYear:'2018'
                },
                taskTimeReqDTO:{
                    whichYear:'2018'
                },
                taskData1:[],
                taskData2:[],
                taskLegend:[],
                projectTaskNum:0,
                priorityTask:{
                    totalNum:0,
                    normalNum:0,
                    urgentNum:0,
                    veryUrgentNum:0
                },
                projectTaskList:[],
                feedbackMonthList:[],
                feedbackTotal:0,
                fbTotalCount:0,
                fromCoach:0,
                other:0,
                feedbackData:[],
                taskMonthList:[],
                taskTotal:0,

                vacationReqDTO:{
                    whichYear:'2018'
                },
                vacationData:{
                    totalCount:0,
                    totalTime:0
                },
                vacationCountList:[],
                vacationTimeList:[],
                beginTime:null,
                endTime:null,
                personVacationData:[],
                personVacationReqDTO:{
                    beginTime:null,
                    endTime:null
                },

                //新增bug相关
                originList:[
                    {id:1,name:'冯丽芸'},
                    {id:2,name:'方迪智'},
                    {id:3,name:'蔡雁'},
                    {id:4,name:'刘利利'},
                    {id:5,name:'冷毅剑'},
                    {id:6,name:'张怡硕'},
                    {id:7,name:'杜仲'}
                ],
                typeList:[
                    {id:0,name:'bug'},
                    {id:1,name:'优化'},
                    {id:2,name:'协助'}
                ],
                solveList:[
                    {id:0,name:'未解决'},
                    {id:1,name:'已解决'}
                ],
                demandSystemList:[
                    // {id:1,name:'知心慧学'},
                    // {id:2,name:'教师端'},
                    // {id:3,name:'安卓教师端'},
                    // {id:4,name:'IOS教师端'},
                    // {id:5,name:'公众号教师端'},
                    // {id:6,name:'助教端'},
                    // {id:7,name:'客户端'},
                    // {id:8,name:'产品'},
                    // {id:9,name:'数学题库'},
                    // {id:10,name:'英语题库'},
                    // {id:11,name:'学生端'},
                    // {id:12,name:'公众号学生端'},
                    // {id:13,name:'安卓学生端'},
                    // {id:14,name:'IOS学生端'},
                    // {id:15,name:'IMS'},
                    // {id:16,name:'IMS安卓端'},
                    // {id:17,name:'直播课'},
                    // {id:18,name:'联考'},
                    // {id:19,name:'家长端'},
                    // {id:10,name:'安卓家长端'},
                    // {id:21,name:'IOS家长端'},
                    // {id:22,name:'校长端'},
                ],
                onlineBugForm:{
                    projectId:'',
                    discoverTime:'',
                    processTime:'',
                    description:'',
                    origin:'',
                    accountInfo:'',
                    type:0,
                    demandSystemId:'',
                    demandSystemName:'',
                    remark:'',
                    isSolved:0
                },
                bugReqDTO:{
                    userId:null,
                    beginTime:null,
                    endTime:null,
                    pageNum:1,
                    departmentId:null
                },
                bugPage:[],
                BugNumData:{},
                taskTimeData:{
                    taskNum:0,
                    totalTaskTime:0,
                    designTime:0,
                    productTime:0,
                    developTime:0,
                    testTime:0,
                    designTaskNum:0,
                    productTaskNum:0,
                    developTaskNum:0,
                    testTaskNum:0,
                    avgDesignTime:0,
                    avgProductTime:0,
                    avgDevelopTime:0,
                    avgTestTime:0
                },
                diffStageTime:[],
                diffStageAvgTime:[],
                top10TaskList:[],
                showTaskVisible:false,
                taskDetail:{},
                priorityList: [
                    {label: '普通', value: 0},
                    {label: '紧急', value: 1},
                    {label: '非常紧急', value: 2},
                ],
                taskTotalTime:0,

                //考勤
                signInReqDTO:{
                    userId:'',
                    beginTime:'',
                    endTime:'',
                    pageNum:1,
                },
                signInDaterange:'',
                signInData:[],
                signInPage:{
                    pageSize: 20,
                    total: 0,
                },
                showTotalEWrokTime: false,
                workMonth1:'',
                eWorkTimeReqDTO1:{
                    yearAndMonth:''
                },
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
                totalEWorkTime:'',

                //mantis-bug统计
                mantisBugReqDTO1:{
                    beginTime:'',
                    endTime:'',
                },
                mantisBugReqDTO2:{
                    beginTime:'',
                    endTime:'',
                },
                mantisBugReqDTO3:{
                    beginTime:'',
                    endTime:'',
                },
                mantisBugReqDTO4:{
                    beginTime:'',
                    endTime:'',
                    pageNum:1
                },
                mantisUserBugStatsList:[],
                taskBugStatsList:[],
                mantisUserBugWeekList:[],
                mantisUserBugMonthList:[],
                onlineBugUserList:[],
                seriesDataList:[],
                seriesDataList2:[],
                onlineBugTotalNum:0,
                dateList:[],
                weekdayList:[],
                userNameList:[],
                yearMonth1:'',
                yearMonth2:'',
                yearMonth3:'',
                yearMonth4:'',
                yearWeek:'',
                taskBugPage:{
                    pageSize: 10,
                    total: 0,
                },
                mantisProjectList:[
                    {id:53,name:'知心慧学2018'},
                    {id:57,name:'知心慧学2019'}
                ],
                selectMantisProjectVisible:false,
                mantisProject:''
                // -- sch
            }
        },
        beforeMount:function () {
            this.getStats(this.statsPage.currentPage);
            //选中任务tab
            this.$root.eventBus.$emit("handleTabSelected", "stats");
            this.fetchUserList();
            this.fetchSignInUser();
            this.fetchProjectList();
            this.fetchProjectList();
            // this.getBugList();
            this.fetchBugPage();
            this.getLeaveList();
            this.fetchAnnualTaskByPriority();
            this.fetchAnnualProjectTaskNum();
            this.fetchEveryMonthFeedback();
            this.fetchEveryMonthTask();
            this.fetchEveryMonthVacation();
            this.fetchAnnualVacation();
            this.fetchAnnualFeedback();
            this.fetchPersonVacation();
            this.fetchDiffStageTaskTime();
            this.fetchTop10MostTimeTask();
            this.initSignInTime();
            this.fetchSignInData();
            this.fetchDiffTypeBugNum();
            this.fetchDemandSystem();
            this.fetchMantisBugStatsGroupByUser();
            this.fetchBugWeekGroupByUser();
            this.fetchOnlineBugGroupByUser();
            this.fetchBugStatsGroupByTask();
        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole < 2;
            },
            admin() {
                let userRole = helper.decodeToken().userRole;
                return userRole < 1;
            },
            pageLayout() {
                if (this.bugFormPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            taskPageLayout() {
                if (this.taskBugPage.total > 0) {
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
            signInPageLayout() {
                if (this.signInPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            }

        },
        filters: {
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY年MM月DD日 HH:00:00');
            },
            formatDate1: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY/MM/DD');
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
        },
        methods: {
            getStats(currentPage){
                Http.zsyGetHttp(`/stats/list/`, {}, (resp) => {
                    this.statsData =  resp.data;
                });
            },
            getCurrentWeek(){
                this.userWeekForm.date=moment().toDate()
            },
            getTask(index){
                this.$router.push({name:'taskList', params:{ userId:this.statsData[index].id }})
            },
            getPesonTask(taskId){
                this.$router.push({name:'taskListFormComments', params:{ taskId:taskId }})
            },
            getBugList(){
                Http.zsyGetHttp(`/bug/list/`, this.bugList, (resp) => {
                    this.bugManage =  resp.data.list;
                    this.bugFormPage.total = resp.data.total;
                });
            },
            getLeaveList(){
                Http.zsyPostHttp(`/userLeave/list/`, this.leaveList, (resp) => {
                    this.leaveManage =  resp.data.list;
                    this.leaveFormPage.total = resp.data.total;
                });
            },
            bugTimeChange(time) {
                // 选择结束时间
                time = time.split(' - ')
                if (time && time.length == 2) {
                    this.bugList.startTime = `${time[0]} 00:00:00`
                    this.bugList.endTime = `${time[1]} 23:59:59`
                } else {
                    this.bugList.startTime = this.bugList.endTime = this.bugDaterange = ''
                }
            },
            bugTimeChange1(time) {
                // 选择结束时间
                time = time.split(' - ')
                if (time && time.length == 2) {
                    this.bugReqDTO.startTime = `${time[0]} 00:00:00`
                    this.bugReqDTO.endTime = `${time[1]} 23:59:59`
                } else {
                    this.bugReqDTO.startTime = this.bugReqDTO.endTime = this.bugDaterange = ''
                }
            },
            leaveTimeChange(time) {
                // 选择结束时间
                time = time.split(' - ')
                if (time && time.length == 2) {
                    this.leaveList.beginTime = `${time[0]} 00:00:00`
                    this.leaveList.endTime = `${time[1]} 23:59:59`
                } else {
                    this.leaveList.startTime = this.leaveList.endTime = this.leaveDaterange = ''
                }
            },
            timeChange(time) {
                // 选择结束时间
                time = time.split(' 至 ')
                if (time && time.length == 2) {
                    this.persanalForm.startTime = `${time[0]} 00:00:00`
                    this.persanalForm.endTime = `${time[1]} 23:59:59`
                } else {
                    this.persanalForm.startTime = this.persanalForm.endTime = this.daterange =  ''
                }
            },
            getPersonalData(){
                if(this.persanalForm.userId == null||this.persanalForm.userId===''){
                    this.errorMsg("请选择你想查询的用户")
                }
                Http.zsyGetHttp(`/stats/personTaskList`, this.persanalForm, (resp) => {
                    this.pesonalTaskData =  resp.data;
                });
            },
            saveBugForm(){
                this.isSaving = true
                if (this.bugForm.projectId == ''||this.bugForm.description == ''||this.bugForm.createTime == ''||this.bugForm.processTime == '') {
                    this.errorMsg('请将问题信息填写完整');
                    return
                }
                this.bugForm.createTime  = moment(this.bugForm.createTime ).format('YYYY-MM-DD HH:mm:ss')
                this.bugForm.processTime  = moment(this.bugForm.processTime ).format('YYYY-MM-DD HH:mm:ss')
                let param = this.bugForm;
                param.projectId = param.projectId.trim()
                param.description = param.description.trim()
                param['bugUsers'] = this.bugUsers;
                Http.zsyPostHttp('/bug/add', param, (resp) => {
                    this.$message({
                        showClose: true,
                        message: 'Bug处理结果创建成功',
                        type: 'success'
                    });
                    this.bugForm.projectId = this.bugForm.description = '';
                    this.bugForm.createTime = this.bugForm.processTime = '';
                    this.createBugSolvingVisible = false
                    this.bugUsers = [];
                    this.fetchDiffTypeBugNum();
                    this.getBugList();
                    this.isSaving = false
                })

            },
            editBugForm(id){
                if (this.bugForm.projectId == ''||this.bugForm.description == ''||this.bugForm.createTime == ''||this.bugForm.processTime == '') {
                    this.errorMsg('请将问题信息填写完整');
                    return
                }
                this.bugForm.createTime  = moment(this.bugForm.createTime ).format('YYYY-MM-DD HH:mm:ss')
                this.bugForm.processTime  = moment(this.bugForm.processTime ).format('YYYY-MM-DD HH:mm:ss')
                let param = this.bugForm;
                param.projectId = param.projectId.trim()
                param.description = param.description.trim()
                param['bugUsers'] = this.bugUsers;
                Http.zsyPutHttp('/bug/update/'+this.modifyId, param, (resp) => {
                    this.$message({
                        showClose: true,
                        message: 'Bug处理结果更新成功',
                        type: 'success'
                    });
                    this.bugForm.projectId = this.bugForm.description = '';
                    this.bugForm.createTime = this.bugForm.processTime = '';
                    this.updateBugSolvingVisible = false
                    this.bugUsers = [];
                    this.getBugList();
                    this.fetchDiffTypeBugNum();
                })
            },

            createBugSolve(){
                this.createBugSolvingVisible = true;
                this.bugForm.description = this.bugForm.projectId = this.bugForm.createTime = this.bugForm.processTime = '';
                this.bugUsers = [];
                this.showAddDetail = false
                this.addMemberIndex = {
                    index: '',
                    userId: '',
                    userName: '',
                    integral: '',
                }
            },
            saveAddMember(){
                if (this.addMemberIndex.userId == ''||this.addMemberIndex.integral == '') {
                    this.errorMsg('请将积分信息填写完整');
                    return
                }
                this.showAddDetail = !this.showAddDetail;
                if (this.addMemberIndex.index === '') {
                    let bugUser = {}
                    bugUser.userId = this.addMemberIndex.userId
                    bugUser.userName = this.addMemberIndex.userName
                    bugUser.integral = this.addMemberIndex.integral
                    this.bugUsers.push(bugUser)
                } else {
                    // 取消css
                    this.bugUsers[this.addMemberIndex.index].cssClass = ''
                }

                this.addMemberIndex = {
                    index: '',
                    userId: '',
                    userName: '',
                    integral: '',
                }
                this.stepTemp = {}

            },
            modifyMember(index, stages) {
                this.stepTemp = {
                    userId: stages[index].userId,
                    userName: stages[index].userName,
                    integral: stages[index].integral,
                }
                this.bugUsers.forEach((item) => {
                    item.cssClass = ''
                })
                this.bugUsers[index].cssClass = 'stepActive'
                this.addMemberIndex = stages[index]
                this.addMemberIndex.index = index
                this.showAddDetail = true;
            },
            deleteMember(index) {
                this.bugUsers.splice(index, 1);
                if (this.bugUsers.length == 0) {
                    this.showAddDetail = false
                    this.addMemberIndex = {
                        index: '',
                        userId: '',
                        userName: '',
                        integral: '',
                    }
                }
            },
            cancelAddMember(){
                this.showAddDetail = !this.showAddDetail;
            },
            editBugDetail(bugDetailForm){
                this.updateBugSolvingVisible = true;
                this.bugDetailVisible = false;
                this.bugForm.projectId = bugDetailForm.projectId;
                this.bugForm.createTime = bugDetailForm.createTime;
                this.bugForm.processTime = bugDetailForm.processTime;
                this.bugForm.description = bugDetailForm.description;
                this.bugUsers = bugDetailForm.bugUsers;
            },
            editBugDetail1(bugDetailForm){
                this.updateBugSolvingVisible1 = true;
                this.bugDetailVisible = false;
                this.onlineBugForm.projectId = bugDetailForm.projectId;
                this.onlineBugForm.createTime = bugDetailForm.createTime;
                this.onlineBugForm.discoverTime = bugDetailForm.discoverTime;
                this.onlineBugForm.processTime = bugDetailForm.processTime;
                this.onlineBugForm.description = bugDetailForm.description;
                this.onlineBugForm.origin = bugDetailForm.origin;
                this.onlineBugForm.demandSystemId = bugDetailForm.demandSystemId;
                this.onlineBugForm.demandSystemName = bugDetailForm.demandSystemName;
                this.onlineBugForm.type = bugDetailForm.type;
                this.onlineBugForm.remark = bugDetailForm.remark;
                this.onlineBugForm.isSolved = bugDetailForm.isSolved;
                this.onlineBugForm.accountInfo = bugDetailForm.accountInfo;
                this.bugUsers = bugDetailForm.bugUsers;
            },
            deleteBug(){
                this.$confirm('确认删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    Http.zsyDeleteHttp('/bug/'+this.modifyId, null, (resp) => {
                        this.$message({
                            showClose: true,
                            message: 'Bug处理结果删除成功',
                            type: 'success'
                        });
                    })
                    this.bugDetailVisible = false;
                    this.fetchBugPage();
                    this.fetchDiffTypeBugNum();
                }).catch(() => {
                });
            },
            fetchUserList() {
                let vm = this
                Http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.userList = resp.data
                })
            },
            fetchProjectList() {
                let vm = this
                Http.zsyGetHttp('/project/list', {}, (resp) => {
                    vm.projectForm = resp.data
                })
            },
            stepUserChange(val) {
                let vm = this;
                this.userList.forEach((user) => {
                    if (user.id === val) {
                        vm.addMemberIndex.userName = user.name
                    }
                })
            },
            bugDetail(row){
                this.bugDetailVisible = true;
                Http.zsyGetHttp('/bug/detail/'+row.id, null, (resp) => {
                    this.bugDetailForm = resp.data
                })
                this.modifyId = row.id;
            },
            handleCurrentChange(currentPage){
                this.bugList.pageNum = currentPage;
                this.fetchBugPage();
            },
            handleTaskBugChange(currentPage){
                this.mantisBugReqDTO4.pageNum = currentPage;
                this.fetchBugStatsGroupByTask();
            },
            leaveHandleCurrentChange(currentPage){
                this.leaveList.pageNum = currentPage;
                this.getLeaveList();
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
            errorMsg(msg) {
                this.$message({
                    showClose: true,
                    message: msg,
                    type: 'error'
                });
            },
            getSummaries(param) {
                const { columns, data } = param;
                const sums = [];
                columns.forEach((column, index) => {
                    if (index === 0) {
                        sums[index] = '总计';
                        return;
                    }
                    if (index < 4) {
                        sums[index] = '';
                        return;
                    }
                    const values = data.map(item => Number(item[column.property]));
                    if (!values.every(value => isNaN(value))) {
                        sums[index] = values.reduce((prev, curr) => {
                            const value = Number(curr);
                            if (!isNaN(value)) {
                                return prev + curr;
                            } else {
                                return prev;
                            }
                        }, 0);
                    }
                });

                return sums;
            },
            getUserWeekStats(){
                if(this.userWeekForm.date!=''){
                    this.userWeekForm.date = moment(this.userWeekForm.date).format('YYYY-MM-DD HH:mm:ss')
                    this.userWeekForm.weekNumber = moment(this.userWeekForm.date).week()
                    if(this.userWeekForm.date!=''){
                        Http.zsyPostHttp('/stats/weekStats',this.userWeekForm , (resp) => {
                            this.userWeekData = resp.data
                        })
                    }else{
                        this.errorMsg('请选择统计信息')
                    }
                }else{
                    this.errorMsg("请选择时间")
                }


            },
            getPesonStats(id){
                this.activeName='personal'
                this.persanalForm.userId = id;
                this.persanalForm.startTime = moment(this.userWeekForm.date).startOf('week').format("YYYY-MM-DD 00:00:00");
                this.persanalForm.endTime =  moment(this.userWeekForm.date).endOf('week').format("YYYY-MM-DD 23:59:59");
                Http.zsyGetHttp(`/stats/personTaskList`, this.persanalForm, (resp) => {
                    this.pesonalTaskData =  resp.data;
                });
            },

            // sch --
            drawLine1(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart1'))
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '年度任务(优先级)',
                        x:'center',
                        subtext:'任务总数: '+this.priorityTask.totalNum ,
                        subtextStyle:{
                            fontSize:16
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['普通','紧急','非常紧急']
                    },
                    series : [
                        {
                            name: '优先级',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:this.taskData1,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
            },
            drawLine2(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart2'))
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '年度任务(项目)',
                        x:'center',
                        subtext: '任务总数: '+this.projectTaskNum,
                        subtextStyle: {
                            fontSize: 16
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: this.taskLegend
                    },
                    series : [
                        {
                            name: '项目',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:this.taskData2,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
            },
            drawLine3(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart3'))
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '每月需求',
                        x:'center',
                        subtext:'需求总数: ' + this.feedbackTotal,
                        subtextStyle:{
                            fontSize: 16
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    legend: {
                        left: 'left',
                        data: ['个数']
                    },
                    xAxis: {
                        data: ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"]
                    },
                    yAxis: [
                        {
                            type: 'value',
                            min: 0,
                            splitLine:{
                                show:false
                            },
                        }
                    ],
                    series: [
                        {
                            name: '个数',
                            type: 'bar',
                            barGap: 0,
                            data: this.feedbackMonthList
                        }
                    ]
                });
            },
            drawLine6(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart6'))
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '年度需求 ',
                        x:'center' ,
                        subtext:'需求总数: ' + this.fbTotalCount,
                        subtextStyle:{
                            fontSize: 16
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['学管端','其他']
                    },
                    series : [
                        {
                            name: '需求来源',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:this.feedbackData,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
            },
            drawLine4(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart4'))
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '每月任务',
                        x:'center',
                        subtext:'任务总数: ' + this.taskTotal,
                        subtextStyle:{
                            fontSize: 16
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    legend: {
                        left: 'left',
                        data: ['个数']
                    },
                    xAxis: {
                        data: ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"]
                    },
                    yAxis: [
                        {
                            type: 'value',
                            min: 0,
                            splitLine:{
                                show:false
                            },
                        }
                    ],
                    series: [
                        {
                            name: '个数',
                            type: 'bar',
                            barGap: 0,
                            data: this.taskMonthList
                        }
                    ]
                });
            },
            drawLine5(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart5'))
                // 绘制图表
                myChart.setOption({
                    title: { text: '年度请假分布',x:'center' },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    legend: {
                        left: 'left',
                        data: ['次数', '时长']
                    },
                    xAxis: {
                        data: ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"]
                    },
                    yAxis: [
                        {
                            type: 'value',
                            name: '次数',
                            min: 0,
                            interval: 5,
                            splitLine:{
                                show:false
                            },
                        },
                        {
                            type: 'value',
                            name: '时长',
                            min: 0,
                            interval: 50,
                            axisLabel: {
                                formatter: '{value} h'
                            },
                            splitLine:{
                                show:false
                            },
                        }
                    ],
                    series: [
                        {
                            name: '次数',
                            type: 'bar',
                            barGap: 0,
                            data: this.vacationCountList
                        },
                        {
                            name: '时长',
                            type: 'bar',
                            data: this.vacationTimeList,
                            yAxisIndex:1
                        }
                    ]
                });
            },
            drawLine7(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart7'))
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '各个阶段任务时长',
                        x:'center',
                        subtext:'任务总耗时: ' + this.taskTimeData.totalTaskTime,
                        subtextStyle:{
                            fontSize: 16
                        }
                    },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    legend: {
                        left: 'left',
                        data: ['总耗时', '平均耗时']
                    },
                    xAxis: {
                        data: ["设计","产品","开发","测试"]
                    },
                    yAxis: [
                        {
                            type: 'value',
                            name: '总耗时',
                            min: 0,
                            splitLine:{
                                show:false
                            },
                        },
                        {
                            type: 'value',
                            name: '平均耗时',
                            min: 0,
                            interval: 5,
                            axisLabel: {
                                formatter: '{value} h'
                            },
                            splitLine:{
                                show:false
                            },
                        }
                    ],
                    series: [
                        {
                            name: '总耗时',
                            type: 'bar',
                            barGap: 0,
                            data: this.diffStageTime
                        },
                        {
                            name: '平均耗时',
                            type: 'bar',
                            data: this.diffStageAvgTime,
                            yAxisIndex:1
                        }
                    ]
                });
            },
            drawLine8(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart8'))
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '测试bug月统计折线图',
                        x:'left'
                    },
                    tooltip: {
                        trigger: 'axis'
                    },
                    legend: {
                        x:'right',
                        data: this.userNameList
                    },
                    xAxis: {
                        data: this.dateList
                    },
                    yAxis: [
                        {
                            type: 'value',
                            name: '数量',
                            min: 0,
                            splitLine:{
                                show:false
                            },
                        }
                    ],
                    series: this.seriesDataList
                });
            },
            drawLine9(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart9'))
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '线上bug统计图',
                        x:'center',
                        subtext:'总计: '+this.onlineBugTotalNum+ ' 个'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: this.onlineBugUserList
                    },
                    series: [
                        {
                            name: '线上bug',
                            type: 'pie',
                            radius : '80%',
                            center: ['50%', '60%'],
                            data:this.seriesDataList2,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
            },
            selectTaskByYear(){
                if(this.whichYear1){
                    this.taskReqDTO.whichYear = moment(this.whichYear1).format("YYYY-MM-DD")
                    this.taskReqDTO.whichYear = this.taskReqDTO.whichYear.substring(0,4)
                }
                // if (Number(this.taskReqDTO.whichYear) < )
                this.taskData1 = [];
                this.taskData2 = [];
                this.projectTaskNum = 0;
                this.fetchAnnualProjectTaskNum();
                this.fetchAnnualTaskByPriority()
            },
            selectTaskTimeByYear(){
              if (this.whichYear7){
                  this.taskTimeReqDTO.whichYear = moment(this.whichYear7).format("YYYY-MM-DD")
                  this.taskTimeReqDTO.whichYear = this.taskTimeReqDTO.whichYear.substring(0,4)
                  // this.taskTimeData = {};
                  this.taskTimeData.totalTaskTime = 0;
                  this.diffStageTime = [];
                  this.diffStageAvgTime = [];
                  this.top10TaskList = [];
                  this.fetchDiffStageTaskTime();
                  this.fetchTop10MostTimeTask();
              }
            },
            selectTaskByYear4(){
                if(this.whichYear4){
                    this.taskMonthReqDTO.whichYear = moment(this.whichYear4).format("YYYY-MM-DD")
                    this.taskMonthReqDTO.whichYear = this.taskMonthReqDTO.whichYear.substring(0,4)
                }
                this.taskTotal = 0;
                this.taskMonthList = [];
                this.fetchEveryMonthTask()
            },
            selectVacationByYear(){
                if (this.whichYear5){
                    this.vacationReqDTO.whichYear = moment(this.whichYear5).format("YYYY-MM-DD")
                    this.vacationReqDTO.whichYear = this.vacationReqDTO.whichYear.substring(0,4)
                }
                this.vacationCountList = [];
                this.vacationTimeList = [];
                this.fetchEveryMonthVacation();
                this.fetchAnnualVacation();
            },
            selectFeedbackByYear(){
              if (this.whichYear2){
                  this.fbReqDTO.whichYear = moment(this.whichYear2).format("YYYY-MM-DD")
                  this.fbReqDTO.whichYear = this.fbReqDTO.whichYear.substring(0,4)
              }
              this.feedbackMonthList = [];
              this.feedbackTotal = 0;
                this.feedbackData = [];
              this.fetchEveryMonthFeedback();
              this.fetchAnnualFeedback();
            },
            //查询年度需求
            fetchAnnualFeedback(){
                if (this.admin){
                    Http.zsyPostHttp('/data/annual/feedback-num',this.fbReqDTO,(res)=>{
                        if (res){
                            this.fbTotalCount = res.data.totalNum;
                            this.fromCoach = res.data.fromCoachNum;
                            this.other = res.data.otherNum;
                            const fbData = {};
                            fbData.value = this.fromCoach;
                            fbData.name = '学管端';
                            this.feedbackData.push(fbData)
                            const fbData2 = {};
                            fbData2.value = this.other;
                            fbData2.name = '其他';
                            this.feedbackData.push(fbData2)
                            this.drawLine6()
                        }
                    })
                }
            },
            //查询年度每个月的需求数
            fetchEveryMonthFeedback(){
                if (this.admin){
                    Http.zsyPostHttp('/data/annual/feedback/month',this.fbReqDTO,(res)=>{
                        if (res){
                            this.feedbackMonthList = res.data;
                            this.feedbackMonthList.forEach(feedbackNum=>{
                                this.feedbackTotal = this.feedbackTotal + feedbackNum
                            })
                            this.drawLine3()
                            this.drawLine6()
                        }
                    })
                }
            },
            //查询年度每月完成的任务数
            fetchEveryMonthTask(){
                if (this.admin){
                    Http.zsyPostHttp('/data/annual/task/month',this.taskMonthReqDTO,(res)=>{
                        if (res){
                            this.taskMonthList = res.data;
                            this.taskMonthList.forEach(taskNum=>{
                                this.taskTotal = this.taskTotal + taskNum
                            })
                            this.drawLine4()
                        }
                    })
                }
            },
            //年度 每月请假次数和时长集合
            fetchEveryMonthVacation(){
                if (this.admin){
                    Http.zsyPostHttp('/data/annual/vacation/month',this.vacationReqDTO,(res)=>{
                        if (res){
                            this.vacationCountList = res.data.vacationCountList;
                            this.vacationTimeList = res.data.vacationTimeList;
                            this.drawLine5()
                        }
                    })
                }
            },
            //年度请假总次数,总时长
            fetchAnnualVacation(){
                if (this.admin){
                    Http.zsyPostHttp('/data/annual/vacation',this.vacationReqDTO,(res)=>{
                        if (res){
                            this.vacationData.totalCount = res.data.vacationCount;
                            this.vacationData.totalTime = res.data.vacationTime;
                        }
                    })
                }
            },
            //查询年度各个项目对应的任务数
            fetchAnnualProjectTaskNum(){
                if (this.admin){
                    Http.zsyPostHttp('/data/annual/task/project-task',this.taskReqDTO,(res) =>{
                        if (res){
                            this.projectTaskList = res.data
                            this.projectTaskList.forEach(project =>{
                                const tData = {};
                                tData.value = project.taskNum;
                                tData.name = project.projectName;
                                this.taskData2.push(tData)
                                this.taskLegend.push(project.projectName)
                                this.projectTaskNum = this.projectTaskNum + project.taskNum
                            })
                            this.drawLine2()
                        }
                    })
                }
            },
            //根据优先级查询年度任务完成数
            fetchAnnualTaskByPriority(){
                if (this.admin){
                    Http.zsyPostHttp('data/annual/task/priority',this.taskReqDTO,(res)=>{
                        if (res){
                            this.priorityTask = res.data
                            const tData3 = {};
                            tData3.value = this.priorityTask.veryUrgentNum;
                            tData3.name = '非常紧急';
                            this.taskData1.push(tData3);

                            const tData2 = {};
                            tData2.value = this.priorityTask.urgentNum;
                            tData2.name = '紧急';
                            this.taskData1.push(tData2);
                            const tData = {};
                            tData.value = this.priorityTask.normalNum;
                            tData.name = '普通';
                            this.taskData1.push(tData);
                            this.priorityTask.totalNum = this.priorityTask.normalNum + this.priorityTask.urgentNum + this.priorityTask.veryUrgentNum
                            this.drawLine1()
                        }
                    })
                }
            },

            //查询个人请假情况
            fetchPersonVacation(){
              Http.zsyPostHttp('data/annual/person-vacation',this.personVacationReqDTO,(res)=>{
                  if (res){
                      this.personVacationData = res.data;
                  }
              })
            },

            //按条件查询个人请假情况
            selectVacationByDate(){
                this.personVacationReqDTO.beginTime = null;
                this.personVacationReqDTO.endTime = null;
                if (this.beginTime){
                    this.personVacationReqDTO.beginTime = moment(this.beginTime).format('YYYY-MM-DD 00:00:00')
                }
                if (this.endTime){
                    this.personVacationReqDTO.endTime = moment(this.endTime).format('YYYY-MM-DD 23:59:59')
                }
                this.fetchPersonVacation()
            },

            //打开新增bug窗口
            openBugDialog(){
                this.createBugSolvingVisible1 = true;
                this.onlineBugForm.description
                    = this.onlineBugForm.projectId
                    = this.onlineBugForm.createTime
                    = this.onlineBugForm.processTime
                    = this.onlineBugForm.origin
                    = this.onlineBugForm.accountInfo
                    = this.onlineBugForm.remark
                    = this.onlineBugForm.demandSystem
                    = '';
                this.onlineBugForm.type = 0;
                this.onlineBugForm.isSolved = 0;
                this.bugUsers = [];
                this.showAddDetail = false
                this.addMemberIndex = {
                    index: '',
                    userId: '',
                    userName: '',
                    integral: '',
                }
            },

            //保存新增bug
            saveOnlineBugForm(){
                this.isSaving = true
                if (this.onlineBugForm.origin == null || this.onlineBugForm.origin == ''){
                    this.errorMsg("反馈人不能为空");
                    this.isSaving = false;
                    return
                }
                if (this.onlineBugForm.description == null || this.onlineBugForm.description == ''){
                    this.errorMsg("问题描述不能为空");
                    this.isSaving = false;
                    return
                }
                if (this.onlineBugForm.discoverTime == null || this.onlineBugForm.discoverTime == ''){
                    this.errorMsg("反馈时间不能为空");
                    this.isSaving = false;
                    return
                }
                this.onlineBugForm.discoverTime  = moment(this.onlineBugForm.discoverTime ).format('YYYY-MM-DD HH:mm:ss')
                if (this.onlineBugForm.processTime) {
                    this.onlineBugForm.processTime  = moment(this.onlineBugForm.processTime ).format('YYYY-MM-DD HH:mm:ss')
                }
                let param = this.onlineBugForm;
                if (param.demandSystemId != null && param.demandSystemId != ''){
                    var demandSystems = this.demandSystemList.filter(demandSystem1=>
                        demandSystem1.id === param.demandSystemId
                    )
                    param.demandSystemName = demandSystems[0].name
                }
                param.projectId = param.projectId.trim()
                param.description = param.description.trim()
                param['bugUsers'] = this.bugUsers;
                Http.zsyPostHttp('/bug/add-bug', param, (resp) => {
                    this.$message({
                        showClose: true,
                        message: 'Bug处理创建成功',
                        type: 'success'
                    });
                    this.onlineBugForm.projectId = this.onlineBugForm.description = this.onlineBugForm.demandSystemId = this.onlineBugForm.demandSystemName = '';
                    this.onlineBugForm.discoverTime = this.onlineBugForm.processTime = null;
                    this.createBugSolvingVisible1 = false
                    this.bugUsers = [];
                    this.fetchBugPage();
                    this.fetchDiffTypeBugNum();
                    this.isSaving = false
                },(fail)=>{
                    this.$message({
                        showClose: true,
                        message: fail.errMsg,
                        type: 'error'
                    });
                    this.isSaving = false;
                    this.bugUsers = [];
                })
            },
            editBugForm1(){
                if (this.onlineBugForm.origin == null || this.onlineBugForm.origin == ''){
                    this.errorMsg("反馈人不能为空");
                    return
                }
                if (this.onlineBugForm.description == null || this.onlineBugForm.description == ''){
                    this.errorMsg("问题描述不能为空");
                    return
                }
                if (this.onlineBugForm.discoverTime == null || this.onlineBugForm.discoverTime == ''){
                    this.errorMsg("反馈时间不能为空");
                    return
                }
                this.onlineBugForm.discoverTime  = moment(this.onlineBugForm.discoverTime ).format('YYYY-MM-DD HH:mm:ss')
                this.onlineBugForm.processTime  = moment(this.onlineBugForm.processTime ).format('YYYY-MM-DD HH:mm:ss')
                let param = this.onlineBugForm;
                param.description = param.description.trim()
                param['bugUsers'] = this.bugUsers;
                if (param.demandSystemId != null && param.demandSystemId != ''){
                    var demandSystems = this.demandSystemList.filter(demandSystem1=>
                        demandSystem1.id === param.demandSystemId
                    )
                    param.demandSystemName = demandSystems[0].name
                }
                Http.zsyPutHttp('/bug/update-bug/'+this.modifyId, param, (resp) => {
                    this.$message({
                        showClose: true,
                        message: 'Bug处理更新成功',
                        type: 'success'
                    });
                    this.onlineBugForm.projectId = this.onlineBugForm.description = this.onlineBugForm.demandSystemId = this.onlineBugForm.demandSystemName = '';
                    this.onlineBugForm.discoverTime = this.onlineBugForm.processTime = null;
                    this.updateBugSolvingVisible1 = false
                    this.bugUsers = [];
                    this.fetchBugPage();
                },(fail)=>{
                    this.$message({
                        showClose: true,
                        message: fail.errMsg,
                        type: 'error'
                    });
                    this.isSaving = false;
                    this.bugUsers = [];
                })
            },
            //查询各个分类bug数量
            fetchDiffTypeBugNum(){
                Http.zsyPostHttp('/bug/num/type',this.bugReqDTO,(res)=>{
                    if (res){
                        this.BugNumData = res.data;
                    }
                })
            },
            //分页查询bug
            fetchBugPage(){
              Http.zsyPostHttp('/bug/page',this.bugReqDTO,(res)=>{
                  if (res){
                      this.bugPage = res.data.list;
                      this.bugFormPage.total = res.data.total
                  }
              })
                this.fetchDiffTypeBugNum()
            },

            closeDialog(){
                this.onlineBugForm.projectId = null;
                this.onlineBugForm.origin = null;
                this.onlineBugForm.discoverTime = null;
                this.onlineBugForm.processTime = null;
                this.onlineBugForm.description = null;
                this.onlineBugForm.accountInfo = null;
                this.onlineBugForm.demandSystem = null;
                this.onlineBugForm.remark = null;
                this.onlineBugForm.type = 0;
                this.onlineBugForm.isSolved = 0;
                this.isSaving = false;
            },

            //查询年度已完成任务总耗时(设计,产品,开发,测试)
            fetchDiffStageTaskTime(){
                if (this.admin){
                    Http.zsyPostHttp('/data/annual/diff-stage/task-time',this.taskTimeReqDTO,(res)=>{
                        if (res){
                            this.taskTimeData = res.data;
                            this.taskTimeData.totalTaskTime = res.data.totalTaskTime;
                            this.diffStageTime.push(res.data.designTime);
                            this.diffStageTime.push(res.data.productTime);
                            this.diffStageTime.push(res.data.developTime);
                            this.diffStageTime.push(res.data.testTime);

                            this.diffStageAvgTime.push(res.data.avgDesignTime);
                            this.diffStageAvgTime.push(res.data.avgProductTime);
                            this.diffStageAvgTime.push(res.data.avgDevelopTime);
                            this.diffStageAvgTime.push(res.data.avgTestTime);
                            this.drawLine7()
                        }
                    })
                }
            },

            //查询耗时前十名的多人任务
            fetchTop10MostTimeTask(){
                Http.zsyPostHttp('/data/annual/most-time-task/top10',this.taskTimeReqDTO,(res)=>{
                    if (res){
                        this.top10TaskList = res.data.taskList;
                    }
                })
            },

            //查询任务总耗时
            fetchTaskTotalTime(taskId){
              Http.zsyGetHttp(`/data/annual/task/time/${taskId}`,{},(res)=>{
                  if (res){
                      this.taskTotalTime = res.data.taskHours;
                  }
              })
            },

            //跳转到任务
            toTask(taskId){
                if (taskId != null && taskId != '') {
                    this.$router.push({name: 'taskListFormComments', params: {taskId: taskId}})
                }
            },

            //查询任务详情
            fetchTaskDetail(id){
                Http.zsyGetHttp(`/task/detail/${id}`, {},(res)=>{
                    if (res){
                        this.taskDetail = res.data;
                        this.fetchTaskTotalTime(id);
                        this.showTaskVisible = true;
                    }
                })
            },

            //考勤
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
            initSignInTime(){
                var date1 = new Date();
                var time1=date1.getFullYear()+"-"+(date1.getMonth()+1)+"-"+date1.getDate();//time1表示当前时间
                var date2 = new Date(date1);
                var date3 = new Date(date1);
                date3.setDate(date1.getDate() - 1);
                date2.setDate(date1.getDate() - 7);
                var time2 = date2.getFullYear()+"-"+(date2.getMonth()+1)+"-"+date2.getDate();
                var time3 = date3.getFullYear()+"-"+(date3.getMonth()+1)+"-"+date3.getDate();
                // this.signInReqDTO.beginTime = moment(time2).format('YYYY-MM-DD 00:00:00');
                // this.signInReqDTO.endTime = moment(time3).format('YYYY-MM-DD 23:59:59');
                this.signInReqDTO.beginTime = time2 + ' 00:00:00';
                this.signInReqDTO.endTime = time3 + ' 23:59:59';

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

            //查询考勤情况
            fetchSignInData(){
                if (this.admin){
                    Http.zsyPostHttp('/sign-in/page',this.signInReqDTO,(res)=>{
                        if (res){
                            this.signInData = res.data.list;
                            this.signInPage.total = res.data.total;
                            this.signInData.forEach(signIn =>{
                                if (signIn.workTime) {
                                    signIn.workTime = this.getTime(signIn.workTime);
                                }
                                if(signIn.eWorkTime){
                                    signIn.eWorkTime = this.getTime(signIn.eWorkTime);
                                }
                                var checkTimeStr = '';
                                signIn.checkTimeList.forEach(checkTime => {
                                    checkTimeStr = checkTimeStr + moment(checkTime).format("HH:mm:ss") + ', '
                                })
                                signIn.checkTimeList = checkTimeStr.substring(0,checkTimeStr.length-2)
                            })
                        }
                    })
                } else {
                    Http.zsyPostHttp('/sign-in/page/personal',this.signInReqDTO,(res)=>{
                        if (res){
                            this.signInData = res.data.list;
                            this.signInPage.total = res.data.total;
                            this.signInData.forEach(signIn =>{
                                if (signIn.workTime) {
                                    signIn.workTime = this.getTime(signIn.workTime);
                                }
                                if(signIn.eWorkTime){
                                    signIn.eWorkTime = this.getTime(signIn.eWorkTime);
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
            //格式化时间
            getTime(time){
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
            closeEWorkCounter(){
                this.workMonth1 = '';
                this.totalEWorkTime = '';
                this.eWorkTimeUserId = '';
                this.eWorkTimeUserName = '';
                this.eWorkTimeReqDTO1.yearAndMonth = '';
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
                if (this.workMonth1 != null && this.workMonth1 != ''){
                    const month = this.workMonth1;
                    const year = moment(new Date()).format('YYYY');
                    const yearAndMonth = year+"-"+this.addZero(month);
                    this.eWorkTimeReqDTO1.yearAndMonth = yearAndMonth;
                    Http.zsyPostHttp('/sign-in/extra-hours/total/'+this.eWorkTimeUserId,this.eWorkTimeReqDTO1,(res)=>{
                        if (res) {
                            this.totalEWorkTime = this.getTime(res.data.extraTime);
                            this.eWorkTimeUserName = res.data.userName;
                        }
                    })
                }else {
                    const year = moment(new Date()).format('YYYY');
                    this.eWorkTimeReqDTO1.yearAndMonth = year;
                    Http.zsyPostHttp('/sign-in/extra-hours/total/'+this.eWorkTimeUserId,this.eWorkTimeReqDTO1,(res)=>{
                        if (res) {
                            this.totalEWorkTime = this.getTime(res.data.extraTime);
                            this.eWorkTimeUserName = res.data.userName;
                        }
                    })
                }
            },
            //获取考勤人员列表
            fetchSignInUser(){
                Http.zsyGetHttp('/sign-in/users',{},(res)=>{
                    if (res){
                        this.checkInUsers = res.data
                    }
                })
            },

            //查询反馈系统
            fetchDemandSystem(){
                Http.zsyGetHttp('/mantis-bug/category/list',{},(res=>{
                    this.demandSystemList = res.data;
                }))
            },
            //按年月查询测试人员bug统计情况
            fetchMantisBugStatsGroupByUser(){
                Http.zsyPostHttp('/mantis-bug/stats/user',this.mantisBugReqDTO1,(res=>{
                    this.mantisUserBugStatsList = res.data
                }))
            },
            changeMonth1(){
                if (this.yearMonth1 != null && this.yearMonth1 != ''){
                    let today = new Date(this.yearMonth1)
                    var month=today.getMonth();
                    var nextMonth=++month;
                    var nextMonthFirstDay=new Date(today.getFullYear(),nextMonth,1);
                    var oneDay=1000*60*60*24;
                    var dateString=new Date(nextMonthFirstDay-oneDay);
                     //Wed Oct 31 2018 00:00:00 GMT+0800 (中国标准时间)
                    let beginTime = moment(this.yearMonth1).format("YYYY-MM-DD 00:00:00");
                    let endTime = moment(dateString).format("YYYY-MM-DD 23:59:59");
                    this.mantisBugReqDTO1.beginTime = beginTime;
                    this.mantisBugReqDTO1.endTime = endTime;
                    this.fetchMantisBugStatsGroupByUser();
                }else {
                    this.mantisBugReqDTO1.beginTime = '';
                    this.mantisBugReqDTO1.endTime = '';
                    this.fetchMantisBugStatsGroupByUser();
                }
            },
            changeMonth2(){
                if (this.yearMonth2 != null && this.yearMonth2 != ''){
                    let today = new Date(this.yearMonth2)
                    var month=today.getMonth();
                    var nextMonth=++month;
                    var nextMonthFirstDay=new Date(today.getFullYear(),nextMonth,1);
                    var oneDay=1000*60*60*24;
                    var dateString=new Date(nextMonthFirstDay-oneDay);
                    //Wed Oct 31 2018 00:00:00 GMT+0800 (中国标准时间)
                    let beginTime = moment(this.yearMonth2).format("YYYY-MM-DD 00:00:00");
                    let endTime = moment(dateString).format("YYYY-MM-DD 23:59:59");
                    this.mantisBugReqDTO3.beginTime = beginTime;
                    this.mantisBugReqDTO3.endTime = endTime;
                    this.mantisUserBugMonthList = [];
                    this.seriesDataList2 = [];
                    this.onlineBugUserList = [];
                    this.onlineBugTotalNum = 0;
                    this.fetchOnlineBugGroupByUser();
                }else {
                    this.onlineBugTotalNum = 0;
                    this.mantisUserBugMonthList = [];
                    this.seriesDataList2 = [];
                    this.onlineBugUserList = [];
                    this.mantisBugReqDTO3.beginTime = '';
                    this.mantisBugReqDTO3.endTime = '';
                    this.fetchOnlineBugGroupByUser();
                }
            },
            changeMonth3(){
                if (this.yearMonth3 != null && this.yearMonth3 != ''){
                    let today = new Date(this.yearMonth3)
                    var month=today.getMonth();
                    var nextMonth=++month;
                    var nextMonthFirstDay=new Date(today.getFullYear(),nextMonth,1);
                    var oneDay=1000*60*60*24;
                    var dateString=new Date(nextMonthFirstDay-oneDay);
                    //Wed Oct 31 2018 00:00:00 GMT+0800 (中国标准时间)
                    let beginTime = moment(this.yearMonth3).format("YYYY-MM-DD 00:00:00");
                    let endTime = moment(dateString).format("YYYY-MM-DD 23:59:59");
                    this.mantisBugReqDTO4.beginTime = beginTime;
                    this.mantisBugReqDTO4.endTime = endTime;
                    this.fetchBugStatsGroupByTask();
                }else {
                    this.mantisBugReqDTO4.beginTime = '';
                    this.mantisBugReqDTO4.endTime = '';
                    this.fetchBugStatsGroupByTask();
                }
            },
            changeMonth4(){
                if (this.yearMonth4 != null && this.yearMonth4 != ''){
                    let today = new Date(this.yearMonth4)
                    var month=today.getMonth();
                    var nextMonth=++month;
                    var nextMonthFirstDay=new Date(today.getFullYear(),nextMonth,1);
                    var oneDay=1000*60*60*24;
                    var dateString=new Date(nextMonthFirstDay-oneDay);
                    //Wed Oct 31 2018 00:00:00 GMT+0800 (中国标准时间)
                    let beginTime = moment(this.yearMonth4).format("YYYY-MM-DD 00:00:00");
                    let endTime = moment(dateString).format("YYYY-MM-DD 23:59:59");
                    this.mantisUserBugWeekList = [];
                    this.seriesDataList = [];
                    this.mantisBugReqDTO2.beginTime = beginTime;
                    this.mantisBugReqDTO2.endTime = endTime;
                    this.fetchBugWeekGroupByUser();
                }else {
                    this.mantisUserBugWeekList = [];
                    this.seriesDataList = [];
                    this.mantisBugReqDTO2.beginTime = '';
                    this.mantisBugReqDTO2.endTime = '';
                    this.fetchBugWeekGroupByUser();
                }
            },
            changeWeek(){
              if (this.yearWeek != null && this.yearWeek != ''){
                  var weekFisrt = new Date(this.yearWeek);
                  var time1=weekFisrt.getFullYear()+"-"+(weekFisrt.getMonth()+1)+"-"+weekFisrt.getDate();//time1表示当前时间
                  var date2 = new Date(weekFisrt);
                  date2.setDate(weekFisrt.getDate() + 6);
                  var time2 = date2.getFullYear()+"-"+(date2.getMonth()+1)+"-"+date2.getDate();
                  var beginTime = time1+' 00:00:00';
                  var endTime = time2+' 23:59:59';
                  this.mantisUserBugWeekList = [];
                  this.seriesDataList = [];
                  this.mantisBugReqDTO2.beginTime = beginTime;
                  this.mantisBugReqDTO2.endTime = endTime;
                  this.fetchBugWeekGroupByUser();
              }else {
                  this.mantisUserBugWeekList = [];
                  this.seriesDataList = [];
                  this.mantisBugReqDTO2.beginTime = beginTime;
                  this.mantisBugReqDTO2.endTime = endTime;
                  this.fetchBugWeekGroupByUser();
              }
            },
            selectMantisProject(){
                this.selectMantisProjectVisible = true;
            },
            closeMantisVisible(){
                this.selectMantisProjectVisible = false;
                this.mantisProject = '';
            },
            importBugInfo(){
                if (this.mantisProject != null &&  this.mantisProject != ''){
                    var project = this.mantisProjectList.filter(mantisProject=>(mantisProject.id === this.mantisProject))[0]
                    this.$confirm('确认导入 <'+project.name+'> 的bug信息?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.selectMantisProjectVisible = false;
                        Http.zsyPostHttp('/mantis-bug/import/'+this.mantisProject,{},(res=>{
                            if (res.errMsg == '执行成功'){
                                this.$message({
                                    showClose: true,
                                    message: '导入成功',
                                    type: 'success'
                                });
                                this.mantisUserBugWeekList = [];
                                this.seriesDataList = [];
                                this.fetchBugWeekGroupByUser();
                                this.mantisUserBugMonthList = [];
                                this.seriesDataList2 = [];
                                this.onlineBugUserList = [];
                                this.onlineBugTotalNum = 0;
                                this.fetchOnlineBugGroupByUser();
                                this.fetchBugStatsGroupByTask();
                                this.fetchMantisBugStatsGroupByUser();

                            }
                        }))
                    }).catch(() => {
                    });
                } else {
                    this.$message({
                        showClose: true,
                        message: '请选择项目',
                        type: 'warning'
                    });
                }
            },
            fetchBugWeekGroupByUser(){
                Http.zsyPostHttp('/mantis-bug/bug-week/user',this.mantisBugReqDTO2,(res=>{
                    this.mantisUserBugWeekList = res.data;

                    this.mantisUserBugWeekList.forEach(bugWeek=>{
                        this.dateList = bugWeek.dateStrList;
                        this.weekdayList = bugWeek.weekdayList;
                        this.userNameList.push(bugWeek.userName)
                        var seriesData = {
                            'name':bugWeek.userName,
                            'type':'line',
                            'data':bugWeek.bugNumList
                        }
                        this.seriesDataList.push(seriesData)
                    })
                    if (this.permit){
                        this.drawLine8()
                    }

                }))
            },
            fetchOnlineBugGroupByUser(){
                Http.zsyPostHttp('/mantis-bug/online-bug/user',this.mantisBugReqDTO3,(res=>{
                    this.mantisUserBugMonthList = res.data;

                    this.mantisUserBugMonthList.forEach(bugUserMonth=>{
                        var bugUserMonthData = {
                            'name': bugUserMonth.userName,
                            'value': bugUserMonth.bugNum
                        }
                        this.onlineBugUserList.push(bugUserMonth.userName);
                        this.seriesDataList2.push(bugUserMonthData);
                        this.onlineBugTotalNum += bugUserMonth.bugNum;
                    })
                    if (this.permit){
                        this.drawLine9()
                    }
                }))
            },
            fetchBugStatsGroupByTask(){
                Http.zsyPostHttp('/mantis-bug/stats/task',this.mantisBugReqDTO4,(res=>{
                    this.taskBugStatsList = res.data.list;
                    this.taskBugPage.total = res.data.total;
                }))
            }
            // -- sch
        }
    }


</script>
<style scoped>

    .stats-con {
        width: 1100px;
        font-size: 14px;
        background: #fff;
        padding: 30px 0;
        box-shadow: 0 0 10px #ccc;
        margin: auto;
    }

    .star {
        color: red;
        padding: 1px;
    }

    .ctpc-tags > li {
        display: inline-block;
        line-height: 30px;
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

    .ctpc-member-list > span {
        display: inline-block;
        vertical-align: middle;
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
        margin-left: 24px;
        margin-top: -45px;
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

    .add-member-basic-msg .el-select {
        width: 140px;
        margin-left: 10px;
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

    .add-member-basic-msg .el-date-picker.el-input {
        width: 300px;
    }

    .add-member-basic-msg {
        margin-left: 10px;
        margin-bottom: 10px;
    }

    .tag-add-sel .el-select {
        width: 188px;
    }

    .ctpc-ins-edit .ctpc-btns {
        margin-top: 0;
    }

    .ctpc-member-con {
        padding-left: 10px; /* border-left: 1px solid #ccc; */
        margin-left: 6px;
        margin-top: 10px;
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


    .ctpc-member-list > span {
        display: inline-block;
        vertical-align: middle;
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
        width: 110px;
    }

    .stepActive {
        box-shadow: 0 0 10px #20A0FF !important;
    }

    .search-btn {
        vertical-align: middle;
        cursor: pointer;
        margin-left: 100px;
        margin-top: 5px;
    }

    .pagination .el-pagination {
        text-align: right;
        padding-right: 0;
        margin-top: 20px;
        margin-right: 10px;
    }

    .ctpc-con {
        height: 340px;
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

    .steps {
        display: inline;
        flex-direction: column;
        margin-bottom: 20px;
        padding-bottom: 10px;
        background: #fff;
    }

    .card-title-con {
        position: relative;
        padding-left: 10px;
        margin: 0 0 16px;
        font-weight: normal;
        line-height: 40px;
        font-size: 16px;
        border-bottom: 1px solid #eee;
        color: #304156;
    }

    .task-span{
        font-size: 18px;
        width: 200px;
        color: black;
    }

    .stats-con {
        width: 1200px;
        font-size: 14px;
        background: #fff;
        padding: 30px 0;
        box-shadow: 0 0 10px #ccc;
        margin: auto;
        height: 500px;
    }
    .bug-stats-con {
        width: 1200px;
        font-size: 14px;
        background: #fff;
        padding: 30px 0;
        box-shadow: 0 0 10px #ccc;
        margin: auto;
    }

    .steps-body {
        display: flex;
        flex-direction: row;
    }

    .task-form {
        margin-bottom: 0;
    }


</style>


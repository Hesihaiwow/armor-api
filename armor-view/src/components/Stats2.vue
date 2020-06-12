<template>
    <div class="stats-con">
        <el-tabs v-model="activeName" @tab-click="handleClick" style="position:relative;margin-bottom: 20px;">
            <el-tab-pane label="任务统计" name="stat"  style="">
                <el-table :data="statsData" >
                    <el-table-column prop="name" label="成员" align="center" ></el-table-column>
                    <el-table-column prop="inProcess" label="我的任务/进行中任务" align="center" >
                        <template slot-scope="sco">
                            <el-button type="text" @click="getTask(sco.$index)">{{sco.row.inProcess}} / {{sco.row.multiTask}}</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column prop="hours" label="进行中任务耗时（小时）" align="center"></el-table-column>
                    <el-table-column prop="delay" label="超时任务" align="center" >
                        <template slot-scope="scope">
                            <span type="text" style="color: red;">{{scope.row.delay}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="complete" label="已完成任务" align="center" ></el-table-column>
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="周发版计划" name="weekPublish"  style="">
                <div class="add-member-basic-msg fl">
                    <el-date-picker
                            v-model="weekPublishReqDTO.beginTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="请选择开始时间"
                    >
                    </el-date-picker>
                    <span style="font-size: 14px;color: #606266;">-</span>
                    <el-date-picker
                            v-model="weekPublishReqDTO.endTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="请选择截止时间"
                    >
                    </el-date-picker>
                </div>
                <!--<div class="add-member-basic-msg fl" >-->
                    <!--<el-date-picker-->
                            <!--v-model="weekPublishReqDTO.date"-->
                            <!--:picker-options="pickerWeek"-->
                            <!--type="week"-->
                            <!--format="yyyy 第 WW 周"-->
                            <!--placeholder="选择周">-->
                    <!--</el-date-picker>-->
                <!--</div>-->
                <!--<div class="add-member-basic-msg fl"><el-button type="text" @click="getWPLastWeek()">第{{lastWeek}}周</el-button></div>-->
                <!--<div class="add-member-basic-msg fl"><el-button type="text" @click="getWPCurrentWeek()">当前第{{currentWeek}}周</el-button></div>-->
                <!--<div class="add-member-basic-msg fl"><el-button type="text" @click="getWPNextWeek()">第{{nextWeek}}周</el-button></div>-->
                <div class="add-member-basic-msg fl" style="margin-left: -90px"><img src="../assets/img/u1221.png" alt="" @click="fetchWeekPublishPlan()" class="search-btn"></div>
                <el-checkbox v-model="weekPublishReqDTO.isTesting" style="margin-top: 5px;margin-left: 10px" @change="fetchWeekPublishPlan">测试中</el-checkbox>

                <el-table :data="weekPublishData" border>
                    <el-table-column type="index" label="序号" align="center" width="80" fixed>
                        <template slot-scope="scope">
                            {{(weekPublishReqDTO.pageNum-1)*20 + scope.$index + 1}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="createByName" label="负责人" align="center" width="90" fixed></el-table-column>
                    <el-table-column prop="taskName" label="任务名称" align="center" width="200" fixed></el-table-column>
                    <el-table-column prop="specialTestTime" label="专项测试时间" align="center" width="130"></el-table-column>
                    <el-table-column prop="zujuan" label="组卷" align="center" width="120"></el-table-column>
                    <el-table-column prop="yuejuan" label="阅卷" align="center" width="120"></el-table-column>
                    <el-table-column prop="saomiao" label="扫描上传" align="center" width="120"></el-table-column>
                    <el-table-column prop="xueyebaogao" label="学业报告" align="center" width="120"></el-table-column>
                    <!--<el-table-column prop="chanpin" label="产品" align="center" width="120"></el-table-column>-->
                    <!--<el-table-column label="设计截止时间" align="center" width="175">-->
                        <!--<template slot-scope="scope">-->
                            <!--<span>{{scope.row.beginTime | formatDate1}}</span><span> ({{scope.row.designDays}})</span>-->
                        <!--</template>-->
                    <!--</el-table-column>-->
                    <el-table-column prop="developers" label="开发" align="center" width="110"></el-table-column>
                    <!--<el-table-column label="开发截止时间" align="center" width="175">-->
                        <!--<template slot-scope="scope">-->
                            <!--<span v-if="scope.row.testTimeColor == 1" style="color: orange">{{scope.row.testTime | formatDate1}}</span>-->
                            <!--<span v-else-if="scope.row.testTimeColor == 2" style="color: red">{{scope.row.testTime | formatDate1}}</span>-->
                            <!--<span v-else>{{scope.row.testTime | formatDate1}}</span>-->
                            <!--<span>({{scope.row.developDays}})</span>-->
                        <!--</template>-->
                    <!--</el-table-column>-->
                    <el-table-column prop="testers" label="测试" align="center" width="110"></el-table-column>
                    <!--<el-table-column label="截止时间" align="center" width="175">-->
                        <!--<template slot-scope="scope">-->
                            <!--<span>{{scope.row.endTime | formatDate1}}</span><span> ({{scope.row.testDays}})</span>-->
                        <!--</template>-->
                    <!--</el-table-column>-->
                    <!--<el-table-column prop="platforms" label="需要发布平台" align="center" width="130"></el-table-column>-->
                    <!--<el-table-column label="是否可以发布上线" align="center" width="100">-->
                        <!--<template slot-scope="scope">-->
                            <!--<el-tooltip content="点击更改状态" placement="top">-->
                                <!--<i class="el-icon-circle-check" v-if="scope.row.canOnline===1"-->
                                   <!--@click="updateCanOnline(scope.row.canOnline,scope.row.wppId)" style="cursor: pointer;color: lawngreen"></i>-->
                                <!--<i class="el-icon-circle-close" v-else style="cursor: pointer;color: red;"-->
                                   <!--@click="updateCanOnline(scope.row.canOnline,scope.row.wppId)"></i>-->
                            <!--</el-tooltip>-->
                        <!--</template>-->
                    <!--</el-table-column>-->
                    <!--<el-table-column prop="condition" label="任务发布情况" align="center" width="200"></el-table-column>-->
                    <el-table-column prop="realTestTime" label="实际测试时间" align="center" width="130"></el-table-column>
                    <el-table-column prop="onlineTime" label="预估上线时间" align="center" width="130"></el-table-column>
                    <el-table-column prop="realOnlineTime" label="实际上线时间" align="center" width="130"></el-table-column>
                    <el-table-column label="操作" width="80" align="center" fixed="right">
                        <template slot-scope="scope">
                            <el-button @click="editWeekPublish(scope.row)" type="text" size="small" >编辑</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="pagination">
                    <el-pagination
                            @current-change="handleWeekPublishCurrentChange"
                            :current-page.sync="weekPublishReqDTO.pageNum"
                            :page-size="weekPublishPage.pageSize"
                            :layout="weekPublishPageLayout"
                            :total="weekPublishPage.total">
                    </el-pagination>
                </div>
            </el-tab-pane>
            <el-tab-pane label="线上问题统计" name="bug">
                <div class="stats-con" style="height: auto">
                    <div class="add-member-basic-msg fl" >
                        <el-select v-model="bugReqDTO.userId" clearable filterable   placeholder="筛选用户">
                            <el-option v-for="item in checkInUsers" :key="item.userId" :label="item.userName"
                                       :value="item.userId"></el-option>
                        </el-select>
                    </div>
                    <div class="add-member-basic-msg fl" >
                        <el-select v-model="bugReqDTO.type" clearable filterable   placeholder="类型">
                            <el-option v-for="item in typeList" :key="item.id" :label="item.name"
                                       :value="item.id"></el-option>
                        </el-select>
                    </div>
                    <div class="add-member-basic-msg fl" >
                        <el-select v-model="bugReqDTO.isSolved" clearable filterable   placeholder="是否解决">
                            <el-option v-for="item in isSolvedList" :key="item.id" :label="item.name"
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
                    <el-button type="primary" @click="fetchBugPage()" style="margin-left: 10px">搜索</el-button>
                    <el-button type="primary" style="margin-left: 10px;margin-bottom: 10px;" @click="openBugDialog" v-show="permit">创建bug处理</el-button>
                    <div class="fr">
                        <span>bug: {{BugNumData.bugNum}}</span>
                        <span>优化: {{BugNumData.optimizationNum}}</span>
                        <span style="margin-right: 10px">协助: {{BugNumData.assistanceNum}}</span>
                    </div>
                    <el-table :data="bugPage" border>
                        <el-table-column type="index" label="序号" align="center" width="60">
                            <template slot-scope="scope">
                                {{(bugReqDTO.pageNum-1)*10 + scope.$index + 1}}
                            </template>
                        </el-table-column>
                        <!--<el-table-column prop="origin" label="反馈人" align="center" width="130"></el-table-column>-->
                        <!--<el-table-column prop="bugNoStr" label="bug编号" align="center" width="110"></el-table-column>-->
                        <el-table-column prop="createTime" label="反馈日期"  width="115">
                            <template slot-scope="scope">
                                <span>{{scope.row.discoverTime | formatDate1}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="processTime" label="解决日期"  width="115">
                            <template slot-scope="scope">
                                <span>{{scope.row.processTime | formatDate1}}</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="demandSystemName" label="反馈系统"  width="130"></el-table-column>
                        <!--<el-table-column prop="accountInfo" label="账号信息"  width="130"></el-table-column>-->
                        <el-table-column prop="description" label="问题描述" align="center">
                            <template slot-scope="scope">
                                <span v-show="scope.row.description.length<20">{{scope.row.description}}</span>
                                <el-popover v-show="scope.row.description.length>=20" :content="scope.row.description" placement="top-start"
                                            width="250"
                                            trigger="hover">
                                    <span slot="reference" style="cursor: pointer;">{{scope.row.description.substring(0,30)}}......</span>
                                </el-popover>
                            </template>
                        </el-table-column>

                        <el-table-column prop="remark" label="备注" align="center" width="130">
                            <template slot-scope="scope">
                                <span v-show="scope.row.remark.length<20">{{scope.row.remark}}</span>
                                <el-popover v-show="scope.row.remark.length>=20" :content="scope.row.remark" placement="top-start"
                                            width="250"
                                            trigger="hover">
                                    <span slot="reference" style="cursor: pointer;">{{scope.row.remark.substring(0,21)}}......</span>
                                </el-popover>
                            </template>
                        </el-table-column>
                        <!--<el-table-column prop="testers" label="测试人员" align="center" width="130"></el-table-column>-->
                        <el-table-column prop="type" label="问题类型" align="center" width="100">
                            <template slot-scope="scope">
                                <span v-if="scope.row.type === 0">bug</span>
                                <span v-if="scope.row.type === 1">优化</span>
                                <span v-if="scope.row.type === 2">协助</span>
                            </template>
                        </el-table-column>
                        <el-table-column prop="testers" label="测试人员" align="center" width="100"></el-table-column>
                        <el-table-column prop="developers" label="开发人员" align="center" width="100"></el-table-column>
                        <el-table-column prop="others" label="其他人员" align="center" width="100"></el-table-column>
                        <el-table-column prop="isSolved" label="是否解决" align="center" width="100">
                            <template slot-scope="scope">
                                <span v-if="scope.row.isSolved === 0">未解决
                                    <span v-if="scope.row.taskId !== undefined"><i class="el-icon-information" @click="toTask(scope.row.taskId)" style="cursor: pointer;color: orangered"></i></span>
                                </span>
                                <span v-if="scope.row.isSolved === 1">已解决
                                    <span v-if="scope.row.taskId !== undefined"><i class="el-icon-information" @click="toTask(scope.row.taskId)" style="cursor: pointer;color: orangered"></i></span>
                                </span>
                                <span v-if="scope.row.isSolved === 2">暂搁置
                                    <span v-if="scope.row.taskId !== undefined"><i class="el-icon-information" @click="toTask(scope.row.taskId)" style="cursor: pointer;color: orangered"></i></span>
                                </span>
                            </template>
                        </el-table-column>
                        <!--<el-table-column prop="remark" label="备注" align="center" width="200"></el-table-column>-->
                        <el-table-column label="操作" width="80" align="center">
                            <template slot-scope="scope">
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
                </div>
                <div class="stats-con" style="height: auto">
                    <div class="add-member-basic-msg fl"><el-date-picker
                            v-model="bugHistogramDateRange"
                            type="daterange"
                            placeholder="选择日期范围"
                            unlink-panels
                            @change="bugTimeChange3"
                            :picker-options="pickerOptions">
                    </el-date-picker></div>
                    <el-button type="primary" style="margin-left: 10px;" @click="fetchHistogram()">搜索</el-button>
                    <div>
                        <div id="myChart11" :style="{width:'1100px',height:'400px',marginTop:'70px',left:'80px'}"></div>
                        <div id="myChart12" :style="{width:'1100px',height:'400px',marginTop:'70px',left:'80px'}"></div>
                    </div>
                </div>

            </el-tab-pane>
            <el-tab-pane label="测试问题统计" name="mantisBug" v-if="permit" style="">
                <div class="bug-stats-con">
                    <h1 style="font-size: 20px;margin-left: 10px;margin-top: -35px;margin-bottom: 10px;font-weight: bold;">任务bug统计</h1>
                    <div class="add-member-basic-msg" style="float: left"><el-date-picker
                            v-model="yearMonth3"
                            type="month"
                            placeholder="选择月份"
                            @change="changeMonth3"
                    size="medium">
                    </el-date-picker></div>
                    <div>
                        <el-button v-loading.fullscreen.lock="fullscreenLoading" type="primary" style="margin-left: 700px;margin-top: -5px;" @click="importBugVisible = true">导入bug信息</el-button>
                        <el-button type="primary" style="margin-top: -5px" v-show="environment === 'dev' || environment === 'test'" @click="selectMantisProject">导出bug信息</el-button>
                    </div>
                    <el-table :data="taskBugStatsList" border>
                        <el-table-column prop="taskName" label="任务名称" align="center">
                            <template slot-scope="scope">
                                <a style="color: #1c8de0;cursor: pointer" @click="toTask(scope.row.taskId)">{{scope.row.taskName}}</a>
                            </template>
                        </el-table-column>
                        <el-table-column prop="totalBugNum" label="bug数量" width="120"></el-table-column>
                        <el-table-column label="测试提交数量"  width="200">
                            <template slot-scope="scope">
                                <div v-for="testerData in scope.row.mantisBugTesterNumResDTOList">
                                    <span>{{testerData.userName}} {{testerData.bugNum}}个</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="开发解决数量" width="200">
                            <template slot-scope="scope">
                                <div v-for="developerData in scope.row.mantisBugDeveloperNumResDTOList">
                                    <span>{{developerData.userName}} {{developerData.bugNum}}个</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="按严重程度划分" width="200">
                            <template slot-scope="scope">
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
                            <template slot-scope="scope">
                                <div v-for="category in scope.row.categoryResDTOList">
                                    <span>{{category.name}}</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column prop="mantisBugTotalNum" label="bug数量" width="100"></el-table-column>
                        <el-table-column label="按严重程度划分" width="140">
                            <template slot-scope="scope">
                                <div v-for="severity in scope.row.mantisBugSeverityNumResDTOList">
                                    <span>{{severity.severityName}} {{severity.bugNum}}个</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="按状态划分" width="130">
                            <template slot-scope="scope">
                                <div v-for="status in scope.row.mantisBugStatusNumResDTOList">
                                    <span>{{status.statusName}} {{status.bugNum}}个</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="按分类划分">
                            <template slot-scope="scope">
                                <div v-for="category in scope.row.mantisBugCategoryNumResDTOList">
                                    <span v-if="category.isInCharge == 1" style="color: red">{{category.categoryName}} {{category.bugNum}}个</span>
                                    <span v-else>{{category.categoryName}} {{category.bugNum}}个</span>
                                </div>
                            </template>
                        </el-table-column>
                        <el-table-column label="线上bug统计">
                            <template slot-scope="scope">
                                <div v-for="demandSystem in scope.row.onlineBugCategoryNumResDTOList">
                                    <span v-if="demandSystem.isInCharge === 1" style="color: red">{{demandSystem.demandSystemName}} {{demandSystem.bugNum}}个</span>
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
                    <div class="steps-body">
                        <div id="myChart9" :style="{width:'600px',height:'400px',left:'-120px',float:'left',marginTop:'70px'}"></div>
                        <div id="myChart10" :style="{width:'600px',height:'400px',left:'-220px',float:'left',marginTop:'70px'}"></div>
                    </div>
                </div>
            </el-tab-pane>
            <el-tab-pane label="个人任务" name="personal">
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="persanalForm.userId" clearable filterable   placeholder="筛选用户">
                        <el-option v-for="item in checkInUsers" :key="item.userId" :label="item.userName"
                                   :value="item.userId"></el-option>
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
                        <template slot-scope="sco">
                            <a style="color:#20a0ff;cursor: pointer;"  @click="getPersonTask(sco.row.taskId)">{{sco.row.taskName}}</a>
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
                    <el-select v-model="userWeekForm.jobRole" clearable filterable   placeholder="筛选角色">
                        <el-option v-for="item in rolesList" :key="item.roleId" :label="item.roleName"
                                   :value="item.roleId"></el-option>
                    </el-select>
                </div>
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
                    <el-table-column  type="index"  label="序号" align="center" width="80"></el-table-column>
                    <el-table-column prop="userName" label="用户" align="center" width="80" >
                        <template slot-scope="sco">
                            <a style="color:#20a0ff;cursor: pointer;"  @click="getPersonStats(sco.row.userId)">{{sco.row.userName}}</a>
                        </template>
                    </el-table-column>
                    <el-table-column prop="taskName" label="任务名称" align="center">
                        <template slot-scope="sco">
                            <div style="white-space: pre-wrap;text-align: left">{{sco.row.taskName}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="hours" label="周工作量"  sortable  width="120">
                        <template slot-scope="scope">
                            <span type="text" v-show="scope.row.hours<=40">{{scope.row.hours}}</span>
                            <span type="text" style="color: red;" v-show="scope.row.hours>40">{{scope.row.hours}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="leaveHours" label="请假时间"  width="100"></el-table-column>
                </el-table>
            </el-tab-pane>
            <!-- <el-tab-pane label="周人员投入表" name="weekUserCost">
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="userCostReqDTO.groupId" clearable filterable  placeholder="筛选团队">
                        <el-option v-for="item in groupList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </div>
                <div class="add-member-basic-msg fl" >
                    <el-date-picker
                            v-model="userCostReqDTO.date"
                            :picker-options="userCostPickerWeek"
                            type="week"
                            format="yyyy 第 WW 周"
                            placeholder="选择周">
                    </el-date-picker>
                </div>
                <div class="add-member-basic-msg fl"><el-button type="text" @click="getUserCostCurrentWeek()">当前第{{currentWeek}}周</el-button></div>

                <div class="add-member-basic-msg fl" ><img src="../assets/img/u1221.png" alt="" @click="fetchWeekUserCost()" class="search-btn"></div>
                <el-table :data="weekUserCostList" border  >
                    <el-table-column prop="jobRoleName" label="岗位" align="center" width="130" >
                        <template slot-scope="sco">{{sco.row.jobRoleName}}</template>
                    </el-table-column>
                    <el-table-column prop="userName" label="人员" align="center" width="90" >
                        <template slot-scope="sco">
                            <el-form>
                                <div v-for="(item,key) in sco.row.userTaskHoursResDTOS">
                                    {{item.userName}}<br/><br/>
                                    <div style="border: 0.5px solid gray;width:90px;height:0;margin-left: -18px"></div>
                                </div>

                            </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column prop="taskName" label="任务" align="center">
                        <template slot-scope="sco">
                            <el-form>
                                <div v-for="item in sco.row.userTaskHoursResDTOS">
                                    <div v-for="task in item.taskHoursResDTOS">
                                        {{task.taskName}}
                                    </div><br/>
                                </div>
                            </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column prop="workHours" label="任务工作量" align="center" width="130" >
                        <template slot-scope="sco">
                            <el-form>
                                <div v-for="item in sco.row.userTaskHoursResDTOS">
                                    <div v-for="task in item.taskHoursResDTOS">
                                        {{task.workHours}}
                                    </div><br/>
                                </div>
                            </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column prop="totalHours" label="周工作量" align="center" width="130" >
                        <template slot-scope="sco">
                            <el-form>
                                <div v-for="item in sco.row.userTaskHoursResDTOS">
                                    {{item.totalHours}}<br/><br/>
                                </div>
                            </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column prop="leaveHours" label="周请假时长" align="center" width="130" >
                        <template slot-scope="sco">
                            <el-form>
                                <div v-for="item in sco.row.userTaskHoursResDTOS">
                                    {{item.leaveHours}}<br/><br/>
                                </div>
                            </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column prop="hourPercent" label="成员工作量饱和度" align="center" width="150" >
                        <template slot-scope="sco">
                            <el-form>
                                <div v-for="item in sco.row.userTaskHoursResDTOS">
                                    <div v-if="item.color === 0">{{item.hourPercent}}<br/><br/></div>
                                    <div v-if="item.color === 1" style="color: orangered">{{item.hourPercent}}<br/><br/></div>
                                    <div v-if="item.color === 2" style="color: deepskyblue;">{{item.hourPercent}}<br/><br/></div>
                                </div>
                            </el-form>
                        </template>
                    </el-table-column>
                    <el-table-column prop="positionHourPercent" label="岗位工作量饱和度" align="center" width="150"  >
                        <template slot-scope="sco">{{sco.row.positionHourPercent}}</template>
                    </el-table-column>
                </el-table>
            </el-tab-pane> -->
            <el-tab-pane label="周人员投入表" name="weekUserCost">
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="userCostReqDTO.groupId" clearable filterable  placeholder="筛选团队">
                        <el-option v-for="item in groupList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </div>
                <div class="add-member-basic-msg fl" >
                    <el-date-picker
                            v-model="userCostReqDTO.date"
                            :picker-options="userCostPickerWeek"
                            type="week"
                            format="yyyy 第 WW 周"
                            placeholder="选择周">
                    </el-date-picker>
                </div>
                <div class="add-member-basic-msg fl"><el-button type="text" @click="getUserCostCurrentWeek()">当前第{{currentWeek}}周</el-button></div>

                <div class="add-member-basic-msg fl" ><img src="../assets/img/u1221.png" alt="" @click="fetchWeekUserCost()" class="search-btn"></div>
                <el-table :data="weekUserCostList" border
                          :span-method="objectSpanMethod">
                    <el-table-column prop="jobRoleName" label="岗位" align="center" width="130" >
                        <template slot-scope="sco">{{sco.row.jobRoleName}}</template>
                    </el-table-column>
                    <el-table-column prop="userName" label="人员" align="center" width="90" >
                        <template slot-scope="sco">
                            {{sco.row.userName}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="taskName" label="任务" align="center">
                        <template slot-scope="sco">
                            {{sco.row.taskName}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="workHours" label="任务工作量" align="center" width="130" >
                        <template slot-scope="sco">
                            {{sco.row.workHours}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="totalHours" label="周工作量" align="center" width="130" >
                        <template slot-scope="sco">
                            {{sco.row.totalHours}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="leaveHours" label="周请假时长" align="center" width="130" >
                        <template slot-scope="sco">
                            {{sco.row.leaveHours}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="hourPercent" label="成员工作量饱和度" align="center" width="150" >
                        <template slot-scope="sco">
                            <div v-if="sco.row.upColor === 0">{{sco.row.hourPercent}}</div>
                            <div v-if="sco.row.upColor === 1" style="color: orangered">{{sco.row.hourPercent}}</div>
                            <div v-if="sco.row.upColor === 2" style="background-color: #FFF129;color: blue">{{sco.row.hourPercent}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="positionHourPercent" label="岗位工作量饱和度" align="center" width="150"  >
                        <template slot-scope="sco">
                            <div v-if="sco.row.ppColor === 0">{{sco.row.positionHourPercent}}</div>
                            <div v-if="sco.row.ppColor === 1" style="color: orangered">{{sco.row.positionHourPercent}}</div>
                            <div v-if="sco.row.ppColor === 2" style="background-color: #FFF129;color: blue;">{{sco.row.positionHourPercent}}</div>
                        </template>
                    </el-table-column>
                </el-table>
            </el-tab-pane>
            <el-tab-pane label="请假统计" name="leave"  style="" v-if="admin" >
                <div class="add-member-basic-msg fl" >
                    <el-select v-model="leaveList.userId" clearable filterable   placeholder="筛选用户">
                        <el-option v-for="item in checkInUsers" :key="item.userId" :label="item.userName"
                                   :value="item.userId"></el-option>
                    </el-select>
                </div>
                <div class="add-member-basic-msg fl">
                    <el-date-picker
                            v-model="leaveBeginTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="请选择开始时间"
                    >
                    </el-date-picker>
                    <span style="font-size: 14px;color: #606266;">-</span>
                    <el-date-picker
                            v-model="leaveEndTime"
                            align="right"
                            type="date"
                            value-format="yyyy-MM-dd"
                            clearable
                            placeholder="请选择截止时间"
                    >
                    </el-date-picker>
                </div>
                <div class="add-member-basic-msg fl" ><img src="../assets/img/u1221.png" alt="" @click="fetchLeaveList()" class="search-btn"></div>
                <el-table :data="leaveManage" border>
                    <el-table-column type="index" label="序号" width="80" align="center"></el-table-column>
                    <el-table-column prop="description" label="请假原因" align="center"></el-table-column>
                    <el-table-column prop="userName" label="请假人" align="center" width="130"></el-table-column>
                    <el-table-column prop="hours" label="时长" align="center" width="80"></el-table-column>
                    <el-table-column prop="typeName" label="类型" align="center" width="80"></el-table-column>
                    <el-table-column prop="beginTime" label="开始日期"  width="150"  align="center">
                        <template slot-scope="scope">
                            <div type="text" size="small" >{{scope.row.beginTime | formatDate}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="endTime" label="结束日期"  width="150"  align="center">
                        <template slot-scope="scope">
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
                        <template slot-scope="scope">
                            {{scope.row.userName}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="remark" label="备注" align="left">
                        <template slot-scope="scope">
                            <pre class="pre">{{scope.row.remarkList}}</pre>
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
                <!--<el-button type="primary" @click="modifyUserRestHoursVisible = true" style="margin-left: 10px" size="small">修改调休时长</el-button>-->
                <el-table :data="signInData" border>
                    <el-table-column prop="date" label="日期" align="center" width="120">
                        <template slot-scope="scope">
                            {{scope.row.date | formatDate2}}{{scope.row.weekday}}
                            <span v-show="scope.row.isWeekend === 1" style="color: #3da7f5">(周末)</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="userName" label="用户" align="center" width="110">
                        <template slot-scope="scope">
                            <span style="color: red" v-if="scope.row.isForget === 1">(漏)</span>{{scope.row.userName}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="checkTimeList" label="打卡记录" align="left">
                        <template slot-scope="scope">
                            {{scope.row.checkTimeList}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="checkInTime" label="上班时间" align="center" width="120" >
                        <template slot-scope="scope">
                            <span style="color: red" v-if="scope.row.isRecheckIn === 1">(补)</span>
                            <span v-if="scope.row.isCheckInAfterTen === 1" style="color: red">{{scope.row.checkInTime | formatTime2}}</span>
                            <span v-else>{{scope.row.checkInTime | formatTime2}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="checkOutTime" label="下班时间" align="center" width="120" >
                        <template slot-scope="scope">
                            <span style="color: red" v-if="scope.row.isRecheckOut === 1">(补)</span>
                            <span style="color: green" v-if="scope.row.isWorkToNextDay === 1">(+1)</span>
                            <span v-if="scope.row.isCheckOutBeforeSix === 1" style="color: red">{{scope.row.checkOutTime | formatTime2}}</span>
                            <span v-else>{{scope.row.checkOutTime | formatTime2}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="workTime" label="上班时长" align="center" width="120" >
                        <template slot-scope="scope">
                            <span v-if="scope.row.lessThanNine === 1" style="color: red">{{scope.row.workTime}}</span>
                            <span v-else>{{scope.row.workTime}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="eWorkTime" label="加班时长" align="center" width="120" >
                        <template slot-scope="scope">
                            {{scope.row.eWorkTime}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="eWorkHours" label="加班申请" align="center" width="100" >
                        <template slot-scope="scope">
                            {{scope.row.eWorkHours}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="leaveTime" label="请假时长" align="center" width="100" >
                        <template slot-scope="scope">
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
            <el-tab-pane label="加班申请统计" name="eWork"  style="" v-if="admin" >
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
                <div class="add-member-basic-msg fl" ><img src="../assets/img/u1221.png" alt="" @click="searchEWorkStats()" class="search-btn"></div>
                <el-table :data="extraWorkStatsList" border>
                    <el-table-column type="index" label="序号" align="center" width="80">
                        <template slot-scope="scope">
                            {{(extraWorkReqDTO.pageNum-1)*10 + scope.$index + 1}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="reason" label="加班原因" align="center"></el-table-column>
                    <el-table-column prop="userName" label="申请人" align="center" width="130"></el-table-column>
                    <el-table-column prop="workHours" label="时长" align="center" width="80"></el-table-column>
                    <el-table-column prop="beginTime" label="开始日期"  width="150"  align="center">
                        <template slot-scope="scope">
                            <div type="text" size="small" >{{scope.row.beginTime | formatDate}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="endTime" label="结束日期"  width="150"  align="center">
                        <template slot-scope="scope">
                            <div type="text" size="small" >{{scope.row.endTime | formatDate}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="checkRecords" label="打卡记录" align="left">
                        <template slot-scope="scope">
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
                            :layout="eWorkPageLayout"
                            :total="extraWorkPage.total">
                    </el-pagination>
                </div>
            </el-tab-pane>
            <el-tab-pane label="加班调休统计" name="restHours" v-if="admin">
                <!--<div class="add-member-basic-msg fl" >-->
                    <!--<el-date-picker-->
                            <!--v-model="restHourYear"-->
                            <!--align="right"-->
                            <!--type="year"-->
                            <!--placeholder="选择年份">-->
                    <!--</el-date-picker>-->
                <!--</div>-->
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
                <div class="add-member-basic-msg fl" ><img src="../assets/img/u1221.png" alt="" @click="fetchAllUsersRestHours" class="search-btn"></div>
                <el-table :data="restHoursData" border  >
                    <el-table-column  type="index"  label="序号"  width="80"></el-table-column>
                    <el-table-column prop="userName" label="用户" align="center" width="100">
                        <template slot-scope="sco">
                            <a style="color:#20a0ff;cursor: pointer;" @click="showUserRestHoursLog2(sco.row.userId,sco.row.userName)" >{{sco.row.userName}}</a>
                        </template>
                    </el-table-column>
                    <el-table-column prop="totalRestHours" label="总调休" sortable align="center">
                        <template slot-scope="sco">
                            <div style="white-space: pre-wrap;text-align: center">{{sco.row.totalRestHours}}</div>
                        </template>
                    </el-table-column>
                    <el-table-column prop="goneRestHours" label="已用调休"  sortable  align="center">
                        <template slot-scope="scope">
                            <span type="text">{{scope.row.goneRestHours}}</span>
                        </template>
                    </el-table-column>
                    <el-table-column prop="leftRestHours" label="剩余调休" sortable align="center"></el-table-column>
                    <el-table-column prop="endDate" label="截止日期"  width="200" align="center"></el-table-column>
                </el-table>
            </el-tab-pane>
        </el-tabs>
        <el-dialog title="创建Bug处理结果"
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


        <el-dialog title="创建Bug处理结果"
                @close="closeDialog()"
                class="aaa"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="createBugSolvingVisible1">
                <div class="ctpc-con" style="height: 400px">

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
                        <div  style="display: inline"><span class="star">*</span>反馈系统</div>
                        <div style="display: inline;margin-left: 30px">
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
                    <div style="margin-top: 150px;margin-left: 10px">
                        <div  style="display: inline">关联任务</div>
                        <div style="display: inline;margin-left: 30px">
                            <el-select v-model="onlineBugForm.taskId" placeholder="请选择关联任务" style="width: 555px" filterable clearable>
                                <el-option  v-for="item in doingTaskList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                            </el-select>
                        </div>
                    </div>
                    <div>
                        <div style="margin-top: 10px"><span class="star">*</span>问题描述</div>
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
                                    <el-option v-for="item in checkInUsers" :key="item.userId" :label="item.userName"
                                               :value="item.userId"></el-option>
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
        <el-dialog title="更新Bug处理"
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
        <el-dialog title="更新Bug处理"
                @close="closeDialog()"
                class="aaa"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="updateBugSolvingVisible1">
            <div class="ctpc-con" style="height: 400px;">
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
                <div style="margin-top: 150px;margin-left: 10px">
                    <div  style="display: inline">关联任务</div>
                    <div style="display: inline;margin-left: 30px">
                        <el-select v-model="onlineBugForm.taskId" placeholder="请选择关联任务" style="width: 555px" filterable clearable>
                            <el-option  v-for="item in doingTaskList" :key="item.id" :label="item.name" :value="item.id"></el-option>
                        </el-select>
                    </div>
                </div>
                <div>
                    <div style="margin-top: 10px"><span class="star">*</span>问题描述</div>
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
                                <el-option v-for="item in checkInUsers" :key="item.userId" :label="item.userName"
                                           :value="item.userId"></el-option>
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
                <el-form-item class="task-form" label="bug编号：">{{bugDetailForm.bugNoStr}}</el-form-item>
                <el-form-item class="task-form" label="关联任务：">{{bugDetailForm.taskName}}</el-form-item>
                <el-form-item class="task-form" label="反馈人：">{{bugDetailForm.origin}}</el-form-item>
                <el-form-item class="task-form" label="反馈日期：">{{bugDetailForm.discoverTime | formatDate1}}</el-form-item>
                <el-form-item class="task-form" label="解决日期：">{{bugDetailForm.processTime | formatDate1}}</el-form-item>
                <el-form-item class="task-form" label="反馈系统：">{{bugDetailForm.demandSystemName}}</el-form-item>
                <el-form-item class="task-form" label="账号信息：">{{bugDetailForm.accountInfo}}</el-form-item>
                <el-form-item class="task-form" label="问题描述：">{{bugDetailForm.description}}</el-form-item>
                <el-form-item class="task-form" label="问题类型：">
                    <template slot-scope="scope">
                        <span v-if="bugDetailForm.type === 0">bug</span>
                        <span v-if="bugDetailForm.type === 1">优化</span>
                        <span v-if="bugDetailForm.type === 2">协助</span>
                    </template>
                </el-form-item>
                <el-form-item class="task-form" label="是否解决：">
                    <template slot-scope="scope">
                        <span v-if="bugDetailForm.isSolved === 0">未解决</span>
                        <span v-if="bugDetailForm.isSolved === 1">已解决</span>
                        <span v-if="bugDetailForm.isSolved === 2">暂搁置</span>
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

        <el-dialog title="任务详情"
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
                                                                    v-if="item.value === taskDetail.priority">{{item.label}}</span>
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

        <el-dialog title="导入bug信息" :visible.sync="importBugVisible" custom-class="myDialog"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="tiny"
                   @close="closeMantisVisible">
            <el-upload
                    ref="bugData"
                    action=""
                    :on-preview="handleRecordPreview"
                    :on-remove="handleRecordRemove"
                    :before-upload="beforeUpload"
                    :http-request="importBugInfo">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                <!--<div slot="tip" class="el-upload__tip">只能上传.dat文件，且不超过1MB</div>-->
            </el-upload>
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
                <el-button v-loading.fullscreen.lock="fullscreenLoading"
                           element-loading-text="拼命导出中,请稍后"
                           element-loading-spinner="el-icon-loading"
                           element-loading-background="rgba(0, 0, 0, 0.8)"
                           type="primary" @click="exportBugInfo">导出</el-button>
            </span>
        </el-dialog>

        <el-dialog title="编辑周发版计划" :visible.sync="editWeekPublishVisible"
                   :close-on-click-modal="false" :close-on-press-escape="false"
                   class="wpp"
        @close="closeEditWeekPublish">
                <el-form :model="weekPublish"  ref="weekPublishForm">
                    <!--<el-form-item label="平台: ">-->
                        <!--<el-checkbox-group-->
                                <!--v-model="weekPublish.platforms">-->
                            <!--<el-checkbox v-for="platform in platformList"-->
                                         <!--:label="platform.id" :key="platform.id">{{platform.name}}</el-checkbox>-->
                        <!--</el-checkbox-group>-->
                    <!--</el-form-item>-->
                    <el-form-item  label="专项测试时间: ">
                        <el-input type="input" style="width: 300px" v-model="weekPublish.specialTestTime"></el-input>
                    </el-form-item>
                    <el-form-item  label="是否涉及组卷: ">
                        <el-input type="input" style="width: 300px"  v-model="weekPublish.zujuan"></el-input>
                    </el-form-item>
                    <el-form-item  label="是否涉及阅卷: ">
                        <el-input type="input" style="width: 300px"  v-model="weekPublish.yuejuan"></el-input>
                    </el-form-item>
                    <el-form-item  label="是否涉及扫描上传: ">
                        <el-input type="input" style="width: 280px"  v-model="weekPublish.saomiao"></el-input>
                    </el-form-item>
                    <el-form-item  label="是否涉及学业报告: ">
                        <el-input type="input" style="width: 280px"  v-model="weekPublish.xueyebaogao"></el-input>
                    </el-form-item>
                    <!--<el-form-item  label="是否涉及产品: ">-->
                        <!--<el-input type="input" style="width: 300px"  v-model="weekPublish.chanpin"></el-input>-->
                    <!--</el-form-item>-->
                    <el-form-item  label="实际测试时间: ">
                        <el-input type="input" style="width: 300px"  v-model="weekPublish.realTestTime"></el-input>
                    </el-form-item>
                    <el-form-item  label="预期上线时间: ">
                        <el-input type="input"  style="width: 300px" v-model="weekPublish.onlineTime"></el-input>
                    </el-form-item>
                    <el-form-item  label="实际上线时间: ">
                        <el-input type="input"  style="width: 300px" v-model="weekPublish.realOnlineTime"></el-input>
                    </el-form-item>
                    <!--<el-form-item  label="任务发布情况: ">-->
                        <!--<el-input type="textarea"  v-model="weekPublish.condition" :rows="3"></el-input>-->
                    <!--</el-form-item>-->
                </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="saveEditWeekPublish(weekPublish.id)">确认修改</el-button>
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
        <el-dialog title="用户调休使用日志" :visible.sync="userRestHoursDetailVisible" class="rest-hour-log"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="small"
                   width="800px">
            <el-table :data="userRestHoursLogData" border>
                <el-table-column type="index" label="序号" align="center" width="80">
                    <template slot-scope="scope">
                        {{(userRestHoursLogPage.pageNum-1)*10 + scope.$index + 1}}
                    </template>
                </el-table-column>
                <el-table-column prop="userName" label="用户" align="center" width="100"></el-table-column>
                <el-table-column prop="restHours" label="调整时长" align="center" width="100"></el-table-column>
                <el-table-column prop="content" label="事由" align="center"></el-table-column>
                <el-table-column prop="recordTime" label="记录日期"  width="200"  align="center">
                    <template slot-scope="scope">
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
        <el-dialog title="用户调休使用日志" :visible.sync="userRestHoursDetailVisible2" class="rest-hour-log"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="25%" size="small"
                   width="800px">
            <div style="margin-bottom: 10px">
                <el-button type="primary" @click="addRestHoursLog">手动新增调休记录</el-button>
            </div>
            <el-table :data="userRestHoursLogData2" border>
                <el-table-column type="index" label="序号" align="center" width="80">
                    <template slot-scope="scope">
                        {{(userRestHoursLogPage2.pageNum-1)*10 + scope.$index + 1}}
                    </template>
                </el-table-column>
                <el-table-column prop="userName" label="用户" align="center" width="100"></el-table-column>
                <el-table-column prop="restHours" label="调整时长" align="center" width="100"></el-table-column>
                <el-table-column prop="content" label="事由" align="center"></el-table-column>
                <el-table-column prop="recordTime" label="记录日期"  width="200"  align="center">
                    <template slot-scope="scope">
                        <div type="text" size="small" >{{scope.row.recordTime | formatTime}}</div>
                    </template>
                </el-table-column>
            </el-table>
            <div class="pagination">
                <el-pagination
                        @current-change="userRestHoursLogHandleCurrentChange2"
                        :current-page.sync="userRestHoursLogPage2.pageNum"
                        :page-size="userRestHoursLogPage2.pageSize"
                        :layout="userRestHoursLogsPageLayout2"
                        :total="userRestHoursLogPage2.total">
                </el-pagination>
            </div>
        </el-dialog>
        <el-dialog  title="新增个人加班/调休记录"  size="tiny"  :close-on-click-modal="false"
                    :close-on-press-escape="false" :visible.sync="editRestHoursVisible"
        @close="cancelAddRestHoursLog">
            <el-form :model="userRestHoursLogForm"  ref="userRestHoursLogForm" label-width="80px">
                <el-form-item label="用户 ">
                    <span>{{this.userRestHoursLogReqDTO2.userName}}</span>
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
    </div>
</template>
<script>
    import Http from '../lib/Http'
    import ElButton from "../../node_modules/element-ui/packages/button/src/button";
    import moment from 'moment';
    import helper from '../lib/Helper'
    import ElTabPane from "../../node_modules/element-ui/packages/tabs/src/tab-pane";

    export default {
        components: {
            ElButton,
            ElTabPane
        },
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
                oldBugList:{
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
                oldBugFormPage:{
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
                bugDaterange2:'',
                leaveDaterange:'',
                leaveBeginTime:null,
                leaveEndTime:null,
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
                    date:'',
                    jobRole:''
                },
                userWeekData:[],
                currentWeek:moment().week(),
                lastWeek:moment().week()-1,
                nextWeek:moment().week()+1,
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
                whichYear1:'2019',
                whichYear2:'2019',
                whichYear4:'2019',
                whichYear5:'2019',
                whichYear7:'2019',
                taskReqDTO:{
                    whichYear:'2019'
                },
                fbReqDTO:{
                    whichYear:'2019'
                },
                taskMonthReqDTO:{
                    whichYear:'2019'
                },
                taskTimeReqDTO:{
                    whichYear:'2019'
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
                    whichYear:'2019'
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
                    {id:1,name:'已解决'},
                    {id:2,name:'暂搁置'}
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
                    taskId:'',
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
                    type:null,
                    isSolved:null,
                    pageNum:1,
                    departmentId:null
                },
                bugTypeList:[
                    {id:0,name:'bug'},
                    {id:1,name:'优化'},
                    {id:2,name:'协助'}
                ],
                isSolvedList:[
                    {id:0,name:'未解决'},
                    {id:1,name:'已解决'},
                    {id:2,name:'暂搁置'}
                ],
                oldBugReqDTO:{
                    userId:null,
                    beginTime:null,
                    endTime:null,
                    type:null,
                    isSolved:null,
                    pageNum:1,
                    departmentId:null
                },
                bugPage:[],
                oldBugPage:[],
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
                mantisDeveloperBugMonthList:[],
                onlineBugUserList:[],
                onlineBugDeveloperList:[],
                seriesDataList:[],
                seriesDataList2:[],
                seriesDataList3:[],
                onlineBugTotalNum:0,
                onlineBugTotalNum3:0,
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
                mantisProject:'',
                fullscreenLoading:false,
                rolesList:[
                    {
                        roleId: 3,
                        roleName: '产品经理'
                    },
                    {
                        roleId: 0,
                        roleName: '测试'
                    },
                    {
                        roleId: 2,
                        roleName: 'UI设计'
                    },
                    {
                        roleId: 1,
                        roleName: 'JAVA开发'
                    },
                    {
                        roleId: 4,
                        roleName: 'C++开发'
                    },
                    {
                        roleId: 5,
                        roleName: 'PHP开发'
                    },
                    {
                        roleId: 6,
                        roleName: '前端开发'
                    },
                    {
                        roleId: 7,
                        roleName: 'IOS开发'
                    },
                    {
                        roleId: 8,
                        roleName: 'Android开发'
                    },
                    {
                        roleId: 9,
                        roleName: '人工智能'
                    },
                    {
                        roleId: 10,
                        roleName: '其他'
                    }
                ],
                urlList:[],
                environment:'',
                importBugVisible:false,

                showWeekPublishPlanVisible: false,
                editWeekPublishVisible: false,
                weekPublishDate:'',
                weekPublishReqDTO:{
                    pageNum:1,
                    date:'',
                    beginTime:'',
                    endTime:'',
                    isTesting:false
                },
                weekPublishPage:{
                    pageSize: 20,
                    total: 0,
                },
                weekPublish:{
                    id:'',
                    taskId:'',
                    canOnline:null,
                    condition:'',
                    specialTestTime:'',
                    zujuan:'',
                    yuejuan:'',
                    saomiao:'',
                    xueyebaogao:'',
                    chanpin:'',
                    realTestTime:'',
                    onlineTime:'',
                    realOnlineTime:'',
                    platforms:[]
                },
                weekPublishData:[],
                weekPublishByChargeData:[],
                platformList:[],

                //加班统计
                ewBeginTime:null,
                ewEndTime:null,
                extraWorkReqDTO:{
                    pageNum:1,
                    userId:null,
                },
                extraWorkStatsList:[],
                extraWorkPage:{
                    pageSize:10,
                    total:0
                },
                //修改调休时间
                modifyUserRestHoursVisible: false,
                restHoursUserId:'',
                restHoursUserName:'',
                restHours:0,
                restHourLoading: false,

                userRestHoursLogData:[],
                userRestHoursDetailVisible: false,
                userRestHoursLogReqDTO:{
                    userId:'',
                    pageNum:1
                },
                userRestHoursLogPage:{
                    pageNum: 1,
                    pageSize: 10,
                    total: 0
                },
                restHourYear:'',
                restHourReqDTO:{
                    year:'',
                    jobRole:'',
                    userId:''
                },
                restHoursData:[],
                userRestHoursLogData2:[],
                userRestHoursDetailVisible2: false,
                userRestHoursLogPage2:{
                    pageNum: 1,
                    pageSize: 10,
                    total: 0
                },
                userRestHoursLogReqDTO2:{
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
                editRestHoursVisible: false,
                bugHistogramReqDTO:{
                    startTime:null,
                    endTime:null
                },
                doingTaskList:[],
                bugHistogramDateRange:'',
                systemBugNumList:[],
                systemX:[],
                systemBugList:[],
                systemOpsList:[],
                systemAssList:[],
                userBugList:[],
                userOpsList:[],
                userAssList:[],
                userX:[],
                userBugNumList:[],

                //周人员投入表相关
                weekUserCostList:[],
                groupList:[],
                userCostReqDTO:{
                    date:null,
                    weekNumber:null,
                    groupId:null
                },
                userCostPickerWeek:{
                    firstDayOfWeek:1
                },
                spanArr:[],
                userSpanArr:[],
                weekHourSpanArr:[],
                leaveHourSpanArr:[],
                userPercentSpanArr:[],
                positionPercentSpanArr:[],
                pos:0,
                userPos:0,
                weekHourPos:0,
                leaveHourPos:0,
                userPercentPos:0,
                positionPercentPos:0,
                // -- sch
            }
        },
        beforeMount:function () {
            let sss = this.$route.query.activeName;
            if (sss != null && sss!== undefined && sss != ''){
                this.activeName = this.$route.query.activeName;
            }
            this.handleClick()
            // this.getStats(this.statsPage.currentPage);
            //选中任务tab
            this.$root.eventBus.$emit("handleTabSelected", "stats");
            // this.fetchUserList();
            this.fetchSignInUser();
            this.fetchDemandSystem();
            this.fetchPlatformList();
            this.fetchAllDoingTasks();
            // this.fetchProjectList();
            // this.getBugList();
            // this.fetchBugPage();
            // this.fetchOldBugPage();
            // this.getLeaveList();
            // this.fetchAnnualTaskByPriority();
            // this.fetchAnnualProjectTaskNum();
            // this.fetchEveryMonthFeedback();
            // this.fetchEveryMonthTask();
            // this.fetchEveryMonthVacation();
            // this.fetchAnnualVacation();
            // this.fetchAnnualFeedback();
            // this.fetchPersonVacation();
            // this.fetchDiffStageTaskTime();
            // this.fetchTop10MostTimeTask();
            // this.initSignInTime();
            // this.fetchSignInData();
            // this.fetchDiffTypeBugNum();
            // this.fetchMantisBugStatsGroupByUser();
            // this.fetchBugWeekGroupByUser();
            // this.fetchOnlineBugGroupByUser();
            // this.fetchBugStatsGroupByTask();
            // this.fetchOnlineBugGroupByDeveloper();
            // this.initTime();
            // this.getExtraWorkStats();
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
            weekPublishPageLayout() {
                if (this.weekPublishPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            oldBugPageLayout() {
                if (this.oldBugFormPage.total > 0) {
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
            eWorkPageLayout() {
                if (this.extraWorkPage.total > 0) {
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
            userRestHoursLogsPageLayout() {
                if (this.userRestHoursLogPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            userRestHoursLogsPageLayout2() {
                if (this.userRestHoursLogPage2.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
        },
        filters: {
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY年MM月DD日 HH:00:00');
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
        },
        methods: {
            handleClick(){
                this.$router.push({query:{activeName:this.activeName}})
              if (this.activeName === 'stat'){
                  this.getStats();
              }else if (this.activeName === 'weekPublish'){
                  this.initTime();
                  // this.initSignInTime2();
              } else if (this.activeName === 'bug'){
                  // this.fetchSignInUser();
                  this.fetchBugPage();
                  this.getSystemHistogram();
                  this.getUserBugHistogram();
                  // this.fetchOldBugPage();
              } else if (this.activeName === 'mantisBug'){
                  this.getEnv();
                  this.fetchMantisBugStatsGroupByUser();
                  this.fetchBugWeekGroupByUser();
                  this.fetchOnlineBugGroupByUser();
                  this.fetchBugStatsGroupByTask();
                  this.fetchOnlineBugGroupByDeveloper();
              } else if (this.activeName === 'personal'){
                    // this.fetchSignInUser();
              } else if (this.activeName === 'week'){

              } else if (this.activeName === 'leave'){
                    // this.getLeaveList();
                   this.initTime3()
              } else if (this.activeName === 'task'){
                  this.fetchAnnualTaskByPriority();
                  this.fetchAnnualProjectTaskNum();
                  this.fetchEveryMonthFeedback();
                  this.fetchEveryMonthTask();
                  this.fetchEveryMonthVacation();
                  this.fetchAnnualVacation();
                  this.fetchAnnualFeedback();
                  this.fetchDiffStageTaskTime();
                  this.fetchTop10MostTimeTask();
              } else if (this.activeName === 'personalLeave'){
                  this.initTime2();
                  // this.fetchPersonVacation();
              } else if (this.activeName === 'signIn'){
                  this.initSignInTime();
                  this.fetchSignInData();
              } else if (this.activeName === 'eWork'){
                  this.initTime4();
                  // this.getExtraWorkStats();
              }else if (this.activeName === 'restHours'){
                  this.fetchAllUsersRestHours();
              }else if (this.activeName === 'weekUserCost'){
                  // this.fetchWeekUserCost()
                  this.fetchGroupList()
              }
            },
            //查询所有可用团队
            fetchGroupList(){
                Http.zsyGetHttp('/work-group/list',{},res=>{
                    this.groupList= res.data;
                })
            },
            getStats(currentPage){
                Http.zsyGetHttp(`/stats/list`, {}, (resp) => {
                    this.statsData =  resp.data;
                });
            },
            getCurrentWeek(){
                this.userWeekForm.date=moment().toDate()
            },
            getUserCostCurrentWeek(){
                this.userCostReqDTO.date=moment().toDate()
            },
            getTask(index){
                this.$router.push({name:'taskList', params:{ userId:this.statsData[index].id }})
            },
            getPersonTask(taskId){
                this.$router.push({name:'taskListFormComments', params:{ taskId:taskId }})
            },
            getBugList(){
                Http.zsyGetHttp(`/bug/list/`, this.bugList, (resp) => {
                    this.bugManage =  resp.data.list;
                    this.bugFormPage.total = resp.data.total;
                });
            },
            fetchLeaveList(){
                this.leaveList.pageNum = 1;
                this.leaveList.beginTime = this.leaveList.endTime = null;
                if (this.leaveBeginTime != null
                && this.leaveBeginTime !== undefined
                && this.leaveBeginTime !== ''){
                    this.leaveList.beginTime = moment(this.leaveBeginTime).format('YYYY-MM-DD 00:00:00');
                }
                if (this.leaveEndTime != null
                    && this.leaveEndTime !== undefined
                    && this.leaveEndTime !== ''){
                    this.leaveList.endTime = moment(this.leaveEndTime).format('YYYY-MM-DD 23:59:59');
                }
                this.getLeaveList()
            },
            getLeaveList(){
                Http.zsyPostHttp(`/userLeave/list`, this.leaveList, (resp) => {
                    this.leaveManage =  resp.data.list;
                    this.leaveFormPage.total = resp.data.total;
                });
            },
            bugTimeChange(time) {
                // 选择结束时间
                if (time && time.length === 2) {
                    this.bugList.startTime = moment(time[0]).format('YYYY-MM-DD 00:00:00');
                    this.bugList.endTime = moment(time[0]).format('YYYY-MM-DD 23:59:59');
                } else {
                    this.bugList.startTime = this.bugList.endTime = this.bugDaterange = ''
                }
            },
            bugTimeChange1(time) {
                // 选择结束时间
                if (time && time.length === 2) {
                    this.bugReqDTO.startTime = moment(time[0]).format('YYYY-MM-DD 00:00:00');
                    this.bugReqDTO.endTime = moment(time[1]).format('YYYY-MM-DD 23:59:59')
                } else {
                    this.bugReqDTO.startTime = this.bugReqDTO.endTime = this.bugDaterange = ''
                }
            },
            bugTimeChange2(time) {
                // 选择结束时间
                if (time && time.length === 2) {
                    this.oldBugReqDTO.startTime = moment(time[0]).format('YYYY-MM-DD 00:00:00');
                    this.oldBugReqDTO.endTime = moment(time[1]).format('YYYY-MM-DD 23:59:59')
                } else {
                    this.oldBugReqDTO.startTime = this.oldBugReqDTO.endTime = this.bugDaterange2 = ''
                }
            },
            bugTimeChange3(time) {
                // 选择结束时间
                if (time && time.length === 2) {
                    this.bugHistogramReqDTO.startTime = moment(time[0]).format('YYYY-MM-DD 00:00:00');
                    this.bugHistogramReqDTO.endTime = moment(time[1]).format('YYYY-MM-DD 23:59:59')
                } else {
                    this.bugHistogramReqDTO.startTime = this.bugHistogramReqDTO.endTime = this.bugHistogramDateRange = ''
                }
            },
            leaveTimeChange(time) {
                // 选择结束时间
                // time = time.split(' - ');
                if (time && time.length === 2) {
                    this.leaveList.beginTime = moment(time[0]).format('YYYY-MM-DD 00:00:00');
                    this.leaveList.endTime = moment(time[1]).format('YYYY-MM-DD 23:59:59')
                } else {
                    this.leaveList.beginTime = this.leaveList.endTime = this.leaveDaterange = ''
                }
            },
            timeChange(time) {
                // 选择结束时间
                if (time && time.length === 2) {
                    this.persanalForm.startTime = moment(time[0]).format('YYYY-MM-DD HH:mm:ss');
                    this.persanalForm.endTime = moment(time[1]).format('YYYY-MM-DD HH:mm:ss');
                } else {
                    this.persanalForm.startTime = this.persanalForm.endTime = this.daterange =  ''
                }
            },
            getPersonalData(){
                if(this.persanalForm.userId == null||this.persanalForm.userId===''){
                    this.errorMsg("请选择你想查询的用户")
                    return;
                }
                Http.zsyGetHttp(`/stats/personTaskList`, this.persanalForm, (resp) => {
                    this.pesonalTaskData =  resp.data;
                });
            },
            saveBugForm(){
                this.isSaving = true
                if (this.bugForm.projectId === ''||this.bugForm.description === ''
                    ||this.bugForm.createTime === ''||this.bugForm.processTime === '') {
                    this.errorMsg('请将问题信息填写完整');
                    return
                }
                this.bugForm.createTime  = moment(this.bugForm.createTime ).format('YYYY-MM-DD HH:mm:ss');
                this.bugForm.processTime  = moment(this.bugForm.processTime ).format('YYYY-MM-DD HH:mm:ss');
                let param = this.bugForm;
                param.projectId = param.projectId.trim();
                param.description = param.description.trim();
                param['bugUsers'] = this.bugUsers;
                Http.zsyPostHttp('/bug/add', param, (resp) => {
                    this.$message({
                        showClose: true,
                        message: 'Bug处理结果创建成功',
                        type: 'success'
                    });
                    this.bugForm.projectId = this.bugForm.description = '';
                    this.bugForm.createTime = this.bugForm.processTime = '';
                    this.createBugSolvingVisible = false;
                    this.bugUsers = [];
                    this.fetchDiffTypeBugNum();
                    this.getBugList();
                    this.isSaving = false
                })

            },
            editBugForm(id){
                if (this.bugForm.projectId === ''||this.bugForm.description === ''
                    ||this.bugForm.createTime === ''||this.bugForm.processTime === '') {
                    this.errorMsg('请将问题信息填写完整');
                    return
                }
                this.bugForm.createTime  = moment(this.bugForm.createTime ).format('YYYY-MM-DD HH:mm:ss');
                this.bugForm.processTime  = moment(this.bugForm.processTime ).format('YYYY-MM-DD HH:mm:ss');
                let param = this.bugForm;
                param.projectId = param.projectId.trim();
                param.description = param.description.trim();
                param['bugUsers'] = this.bugUsers;
                Http.zsyPutHttp('/bug/update/'+this.modifyId, param, (resp) => {
                    this.$message({
                        showClose: true,
                        message: 'Bug处理结果更新成功',
                        type: 'success'
                    });
                    this.bugForm.projectId = this.bugForm.description = '';
                    this.bugForm.createTime = this.bugForm.processTime = '';
                    this.updateBugSolvingVisible = false;
                    this.bugUsers = [];
                    this.getBugList();
                    this.fetchDiffTypeBugNum();
                })
            },

            createBugSolve(){
                this.createBugSolvingVisible = true;
                this.bugForm.description = this.bugForm.projectId = this.bugForm.createTime = this.bugForm.processTime = '';
                this.bugUsers = [];
                this.showAddDetail = false;
                this.addMemberIndex = {
                    index: '',
                    userId: '',
                    userName: '',
                    integral: '',
                }
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
            saveAddMember(){
                // this.addMemberIndex.integral = 0;
                if (this.addMemberIndex.userId === ''||this.addMemberIndex.integral === '') {
                    this.errorMsg('请将积分信息填写完整');
                    return
                }
                if (!this.isDecimal(this.addMemberIndex.integral)) {
                    this.$message({showClose: true, message: '积分填写不正确,请填写成数字类型', type: 'error'});
                    return false;
                }
                if (this.addMemberIndex.integral >= 108 || this.addMemberIndex.integral <= -108) {
                    this.$message({showClose: true, message: '积分正确值应为-108~108', type: 'error'});
                    return false;
                }
                this.showAddDetail = !this.showAddDetail;
                if (this.addMemberIndex.index === '') {
                    let bugUser = {};
                    bugUser.userId = this.addMemberIndex.userId;
                    bugUser.userName = this.addMemberIndex.userName;
                    bugUser.integral = this.addMemberIndex.integral;
                    this.bugUsers.push(bugUser)
                } else {
                    // 取消css
                    this.bugUsers[this.addMemberIndex.index].cssClass = ''
                }

                this.addMemberIndex = {
                    index: '',
                    userId: '',
                    userName: '',
                    integral: null,
                };
                this.stepTemp = {}

            },
            modifyMember(index, stages) {
                this.stepTemp = {
                    userId: stages[index].userId,
                    userName: stages[index].userName,
                    integral: stages[index].integral,
                };
                this.bugUsers.forEach((item) => {
                    item.cssClass = ''
                });
                this.bugUsers[index].cssClass = 'stepActive';
                this.addMemberIndex = stages[index];
                this.addMemberIndex.index = index;
                this.showAddDetail = true;
            },
            deleteMember(index) {
                this.bugUsers.splice(index, 1);
                if (this.bugUsers.length === 0) {
                    this.showAddDetail = false;
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
                this.onlineBugForm.taskId = bugDetailForm.taskId;
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
                        this.bugDetailVisible = false;
                        this.fetchBugPage();
                        this.changeMonth2();
                        this.changeMonth1();
                    });

                }).catch(() => {
                });
            },
            fetchUserList() {
                let vm = this;
                Http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.userList = resp.data
                })
            },
            fetchProjectList() {
                let vm = this;
                Http.zsyGetHttp('/project/list', {}, (resp) => {
                    vm.projectForm = resp.data
                })
            },
            // stepUserChange(val) {
            //     let vm = this;
            //     this.userList.forEach((user) => {
            //         if (user.id === val) {
            //             vm.addMemberIndex.userName = user.name
            //         }
            //     })
            // },
            stepUserChange(val) {
                let vm = this;
                this.checkInUsers.forEach((user) => {
                    if (user.userId === val) {
                        vm.addMemberIndex.userName = user.userName
                    }
                })
            },
            bugDetail(row){
                Http.zsyGetHttp('/bug/detail/'+row.id, null, (resp) => {
                    this.bugDetailForm = resp.data
                });
                this.bugDetailVisible = true;
                this.modifyId = row.id;
            },
            handleCurrentChange(currentPage){
                this.bugList.pageNum = currentPage;
                this.fetchBugPage();
            },
            handleWeekPublishCurrentChange(currentPage){
                this.weekPublishReqDTO.pageNum = currentPage;
                this.fetchWeekPublishPlan();
            },
            handleOldBugChange(currentPage){
                this.oldBugList.pageNum = currentPage;
                this.fetchOldBugPage();
            },
            handleTaskBugChange(currentPage){
                this.mantisBugReqDTO4.pageNum = currentPage;
                this.fetchBugStatsGroupByTask();
            },
            leaveHandleCurrentChange(currentPage){
                this.leaveList.pageNum = currentPage;
                this.getLeaveList();
            },
            eWorkHandleCurrentChange(currentPage){
                this.extraWorkReqDTO.pageNum = currentPage;
                this.getExtraWorkStats();
            },
            isDecimal(str) {
                let regu = /^[-]{0,1}[0-9]{1,}$/;
                if (regu.test(str)) {
                    return true;
                }
                let re = /^[-]{0,1}(\d+)[\.]+(\d+)$/;
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
                if(this.userWeekForm.date == null
                || this.userWeekForm.date === undefined
                || this.userWeekForm.date === ''){
                    this.$message({
                        showClose: true,
                        message: '请选择时间',
                        type: 'warning'
                    });
                    return;
                }
                this.userWeekForm.date = moment(this.userWeekForm.date).format('YYYY-MM-DD 00:00:00');
                this.userWeekForm.weekNumber = moment(this.userWeekForm.date).week();
                Http.zsyPostHttp('/stats/weekStats',this.userWeekForm , (resp) => {
                    this.userWeekData = resp.data;
                })
            },
            getPersonStats(id){
                this.activeName='personal';
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
                let dom = document.getElementById('myChart1');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
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
                let dom = document.getElementById('myChart2');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
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
                let dom = document.getElementById('myChart3');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
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
                let dom = document.getElementById('myChart6');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
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
                let dom = document.getElementById('myChart4');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
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
                let dom = document.getElementById('myChart5');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
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
                let dom = document.getElementById('myChart7');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
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
                let dom = document.getElementById('myChart8');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
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
                let dom = document.getElementById('myChart9');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
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
                            radius : '60%',
                            center: ['50%', '50%'],
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
            drawLine10(){
                // 基于准备好的dom，初始化echarts实例
                let dom = document.getElementById('myChart10');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '开发解决bug统计图',
                        x:'center',
                        subtext:'总计: '+this.onlineBugTotalNum3+ ' 个'
                    },
                    tooltip: {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'right',
                        data: this.onlineBugDeveloperList
                    },
                    series: [
                        {
                            name: '线上bug',
                            type: 'pie',
                            radius : '60%',
                            center: ['50%', '50%'],
                            data:this.seriesDataList3,
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
            drawLine11(){
                // 基于准备好的dom，初始化echarts实例
                let dom = document.getElementById('myChart11');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '反馈系统分类柱状图',
                        // x:'center',
                        // subtext:'总计: '+this.onlineBugTotalNum3+ ' 个'
                    },
                    tooltip: {
                        trigger: 'item',
                        // formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {

                    },
                    xAxis: {
                        type: 'category',
                        data: this.systemX,
                        axisLabel:{
                            interval:0,
                            rotate:-45,//倾斜度 -90 至 90 默认为0
                            margin:2,
                            textStyle:{
                                color:"#000000",
                                fontSize:15
                            }
                        }
                    },
                    yAxis: [
                        {
                            type: 'value',
                            name:'个数',
                            interval: 5,
                            axisLabel: {
                                formatter: '{value} 个'
                            },
                            splitLine: {
                                show: false
                            }
                        },

                    ],
                    dataZoom: [{
                        type: 'slider',
                        show: true, //flase直接隐藏图形
                        xAxisIndex: [0],
                        left: '9%', //滚动条靠左侧的百分比
                        bottom: -5,
                        start: 0,//滚动条的起始位置
                        end: 50 //滚动条的截止位置（按比例分割你的柱状图x轴长度）
                    }],
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    series : [
                        {
                            name:'bug',
                            type:'bar',
                            stack:'总量',
                            data:this.systemBugList,
                            barWidth:30,
                            barGap:0,
                            label:{
                                normal: {
                                    show: true,
                                    position: 'inside'
                                }
                            }
                        },
                        {
                            name:'优化',
                            type:'bar',
                            stack:'总量',
                            data:this.systemOpsList,
                            barWidth:30,
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
                            name:'协助',
                            type:'bar',
                            stack:'总量',
                            data:this.systemAssList,
                            barWidth:30,
                            // barGap:10
                            label:{
                                normal: {
                                    show: true,
                                    position: 'inside'
                                }
                            }
                        },
                    ]
                });
            },
            drawLine12(){
                // 基于准备好的dom，初始化echarts实例
                let dom = document.getElementById('myChart12');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '用户分类柱状图',
                        // x:'center',
                        // subtext:'总计: '+this.onlineBugTotalNum3+ ' 个'
                    },
                    tooltip: {
                        trigger: 'item',
                        // formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {

                    },
                    dataZoom: [{
                        type: 'slider',
                        show: true, //flase直接隐藏图形
                        xAxisIndex: [0],
                        left: '9%', //滚动条靠左侧的百分比
                        bottom: -5,
                        start: 0,//滚动条的起始位置
                        end: 50 //滚动条的截止位置（按比例分割你的柱状图x轴长度）
                    }],

                    xAxis: {
                        type: 'category',
                        data: this.userX,
                        axisLabel:{
                            interval:0,
                            rotate:-45,//倾斜度 -90 至 90 默认为0
                            margin:2,
                            textStyle:{
                                color:"#000000",
                                fontSize:15
                            }
                        }
                    },
                    yAxis: [
                        {
                            type: 'value',
                            name:'个数',
                            interval: 5,
                            axisLabel: {
                                formatter: '{value} 个'
                            },
                            splitLine: {
                                show: false
                            }
                        },

                    ],
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    series : [
                        {
                            name:'bug',
                            type:'bar',
                            stack:'总量',
                            data:this.userBugList,
                            barWidth:30,
                            barGap:0,
                            label:{
                                normal: {
                                    show: true,
                                    position: 'inside'
                                }
                            }
                        },
                        {
                            name:'优化',
                            type:'bar',
                            stack:'总量',
                            data:this.userOpsList,
                            barWidth:30,
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
                            name:'协助',
                            type:'bar',
                            stack:'总量',
                            data:this.userAssList,
                            barWidth:30,
                            // barGap:10
                            label:{
                                normal: {
                                    show: true,
                                    position: 'inside'
                                }
                            }
                        },
                    ]
                });
            },
            selectTaskByYear(){
                if(this.whichYear1 == null
                || this.whichYear1 === undefined
                || this.whichYear1 === ''){
                    this.$message({
                        showClose: true,
                        message: '请选择年份',
                        type: 'warning'
                    });
                    return;
                }
                if(this.whichYear1){
                    this.taskReqDTO.whichYear = moment(this.whichYear1).format("YYYY-MM-DD");
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
                  this.taskTimeReqDTO.whichYear = moment(this.whichYear7).format("YYYY-MM-DD");
                  this.taskTimeReqDTO.whichYear = this.taskTimeReqDTO.whichYear.substring(0,4);
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
                    this.taskMonthReqDTO.whichYear = moment(this.whichYear4).format("YYYY-MM-DD");
                    this.taskMonthReqDTO.whichYear = this.taskMonthReqDTO.whichYear.substring(0,4)
                }
                this.taskTotal = 0;
                this.taskMonthList = [];
                this.fetchEveryMonthTask()
            },
            selectVacationByYear(){
                if (this.whichYear5){
                    this.vacationReqDTO.whichYear = moment(this.whichYear5).format("YYYY-MM-DD");
                    this.vacationReqDTO.whichYear = this.vacationReqDTO.whichYear.substring(0,4)
                }
                this.vacationCountList = [];
                this.vacationTimeList = [];
                this.fetchEveryMonthVacation();
                this.fetchAnnualVacation();
            },
            selectFeedbackByYear(){
                if(this.whichYear2 == null
                    || this.whichYear2 === undefined
                    || this.whichYear2 === ''){
                    this.$message({
                        showClose: true,
                        message: '请选择年份',
                        type: 'warning'
                    });
                    return;
                }
              if (this.whichYear2){
                  this.fbReqDTO.whichYear = moment(this.whichYear2).format("YYYY-MM-DD");
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
                    this.feedbackTotal = 0;
                    this.feedbackData = [];
                    Http.zsyPostHttp('/data/annual/feedback-num',this.fbReqDTO,(res)=>{
                        if (res){
                            this.fbTotalCount = res.data.totalNum;
                            this.fromCoach = res.data.fromCoachNum;
                            this.other = res.data.otherNum;
                            const fbData = {};
                            fbData.value = this.fromCoach;
                            fbData.name = '学管端';
                            this.feedbackData.push(fbData);
                            const fbData2 = {};
                            fbData2.value = this.other;
                            fbData2.name = '其他';
                            this.feedbackData.push(fbData2);
                            this.$nextTick(()=>{
                                this.drawLine6()
                            })
                        }
                    })
                }
            },
            //查询年度每个月的需求数
            fetchEveryMonthFeedback(){
                if (this.admin){
                    this.feedbackMonthList = [];
                    Http.zsyPostHttp('/data/annual/feedback/month',this.fbReqDTO,(res)=>{
                        if (res){
                            this.feedbackMonthList = res.data;
                            this.feedbackMonthList.forEach(feedbackNum=>{
                                this.feedbackTotal = this.feedbackTotal + feedbackNum
                            });
                            this.$nextTick(()=>{
                                this.drawLine3();
                                this.drawLine6()
                            })
                        }
                    })
                }
            },
            //查询年度每月完成的任务数
            fetchEveryMonthTask(){
                if (this.admin){
                    this.taskTotal = 0;
                    this.taskMonthList = [];
                    Http.zsyPostHttp('/data/annual/task/month',this.taskMonthReqDTO,(res)=>{
                        if (res){
                            this.taskMonthList = res.data;
                            this.taskMonthList.forEach(taskNum=>{
                                this.taskTotal = this.taskTotal + taskNum
                            });
                            this.$nextTick(()=>{
                                this.drawLine4()
                            })
                        }
                    })
                }
            },
            //年度 每月请假次数和时长集合
            fetchEveryMonthVacation(){
                if (this.admin){
                    this.vacationCountList = [];
                    this.vacationTimeList = [];
                    Http.zsyPostHttp('/data/annual/vacation/month',this.vacationReqDTO,(res)=>{
                        if (res){
                            this.vacationCountList = res.data.vacationCountList;
                            this.vacationTimeList = res.data.vacationTimeList;

                            this.$nextTick(()=>{
                                this.drawLine5()
                            })
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
                    this.taskData2 = [];
                    this.projectTaskNum = 0;
                    Http.zsyPostHttp('/data/annual/task/project-task',this.taskReqDTO,(res) =>{
                        if (res){
                            this.projectTaskList = res.data;
                            this.projectTaskList.forEach(project =>{
                                const tData = {};
                                tData.value = project.taskNum;
                                tData.name = project.projectName;
                                this.taskData2.push(tData);
                                this.taskLegend.push(project.projectName);
                                this.projectTaskNum = this.projectTaskNum + project.taskNum
                            });
                            this.$nextTick(()=>{
                                this.drawLine2()
                            })
                        }
                    })
                }
            },
            //根据优先级查询年度任务完成数
            fetchAnnualTaskByPriority(){
                if (this.admin){
                    this.taskData1 = [];

                    Http.zsyPostHttp('data/annual/task/priority',this.taskReqDTO,(res)=>{
                        if (res){
                            this.priorityTask = res.data;
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
                            this.priorityTask.totalNum = this.priorityTask.normalNum + this.priorityTask.urgentNum + this.priorityTask.veryUrgentNum;

                            this.$nextTick(()=>{
                                this.drawLine1()
                            })
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
                this.showAddDetail = false;
                this.addMemberIndex = {
                    index: '',
                    userId: '',
                    userName: '',
                    integral: '',
                }
            },

            //保存新增bug
            saveOnlineBugForm(){
                this.isSaving = true;
                if (this.onlineBugForm.origin == null || this.onlineBugForm.origin === ''){
                    this.errorMsg("反馈人不能为空");
                    this.isSaving = false;
                    return
                }
                if (this.onlineBugForm.type == null || this.onlineBugForm.type === ''){
                    this.errorMsg("问题类型不能为空");
                    this.isSaving = false;
                    return
                }
                if (this.onlineBugForm.isSolved == null || this.onlineBugForm.isSolved === ''){
                    this.errorMsg("是否解决不能为空");
                    this.isSaving = false;
                    return
                }
                if (this.onlineBugForm.demandSystemId == null || this.onlineBugForm.demandSystemId === ''){
                    this.errorMsg("反馈系统不能为空");
                    this.isSaving = false;
                    return
                }
                if (this.onlineBugForm.description == null || this.onlineBugForm.description === ''){
                    this.errorMsg("问题描述不能为空");
                    this.isSaving = false;
                    return
                }
                if (this.onlineBugForm.discoverTime == null || this.onlineBugForm.discoverTime === ''){
                    this.errorMsg("反馈时间不能为空");
                    this.isSaving = false;
                    return
                }
                this.onlineBugForm.discoverTime  = moment(this.onlineBugForm.discoverTime ).format('YYYY-MM-DD HH:mm:ss');
                if (this.onlineBugForm.processTime) {
                    this.onlineBugForm.processTime  = moment(this.onlineBugForm.processTime ).format('YYYY-MM-DD HH:mm:ss')
                }
                let param = this.onlineBugForm;
                if (param.demandSystemId != null && param.demandSystemId !== ''){
                    let demandSystems = this.demandSystemList.filter(demandSystem1=>
                        demandSystem1.id === param.demandSystemId
                    );
                    param.demandSystemName = demandSystems[0].name
                }
                param.projectId = param.projectId.trim();
                param.taskId = param.taskId;
                param.description = param.description.trim();
                param['bugUsers'] = this.bugUsers;
                Http.zsyPostHttp('/bug/add-bug', param, (resp) => {
                    this.$message({
                        showClose: true,
                        message: 'Bug处理创建成功',
                        type: 'success'
                    });
                    this.onlineBugForm.projectId = this.onlineBugForm.description = this.onlineBugForm.demandSystemId = this.onlineBugForm.demandSystemName = '';
                    this.onlineBugForm.discoverTime = this.onlineBugForm.processTime = this.onlineBugForm.taskId = null;
                    this.createBugSolvingVisible1 = false;
                    this.bugUsers = [];
                    this.fetchBugPage();
                    this.changeMonth1();
                    this.changeMonth2();
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
                if (this.onlineBugForm.origin == null || this.onlineBugForm.origin === ''){
                    this.errorMsg("反馈人不能为空");
                    return
                }
                if (this.onlineBugForm.description == null || this.onlineBugForm.description === ''){
                    this.errorMsg("问题描述不能为空");
                    return
                }
                if (this.onlineBugForm.discoverTime == null || this.onlineBugForm.discoverTime === ''){
                    this.errorMsg("反馈时间不能为空");
                    return
                }
                this.onlineBugForm.discoverTime  = moment(this.onlineBugForm.discoverTime ).format('YYYY-MM-DD HH:mm:ss');
                if (this.onlineBugForm.processTime != null && this.onlineBugForm.processTime !== '') {
                    this.onlineBugForm.processTime  = moment(this.onlineBugForm.processTime ).format('YYYY-MM-DD HH:mm:ss')
                }
                let param = this.onlineBugForm;
                param.description = param.description.trim();
                param['bugUsers'] = this.bugUsers;
                if (param.demandSystemId != null && param.demandSystemId !== ''){
                    let demandSystems = this.demandSystemList.filter(demandSystem1=>
                        demandSystem1.id === param.demandSystemId
                    );
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
                    this.updateBugSolvingVisible1 = false;
                    this.bugUsers = [];
                    this.fetchBugPage();
                    this.changeMonth1();
                    this.changeMonth2();
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
            //按时间查询
            fetchHistogram(){
                this.systemX = [];
                this.systemBugList= [];
                this.systemOpsList= [];
                this.systemAssList= [];
                this.getSystemHistogram();

                this.userX = [];
                this.userBugList= [];
                this.userOpsList= [];
                this.userAssList= [];
                this.getUserBugHistogram();
            },
            //查询系统bug柱状图
            getSystemHistogram(){
              Http.zsyPostHttp('/bug/system/histogram',this.bugHistogramReqDTO,res=>{
                    this.systemBugNumList = res.data;
                    if (this.systemBugNumList.length>0){
                        this.systemBugNumList.forEach(systemBugNum=>{
                            this.systemX.push(systemBugNum.demandSystemName);
                            this.systemBugList.push(systemBugNum.bugNum);
                            this.systemOpsList.push(systemBugNum.optimizationNum);
                            this.systemAssList.push(systemBugNum.assistanceNum);
                        });

                    }
                  this.drawLine11()

              })
            },
            //查询用户bug分类柱状图
            getUserBugHistogram(){
                Http.zsyPostHttp('/bug/user/histogram',this.bugHistogramReqDTO,res=>{
                    this.userBugNumList = res.data;
                    if (this.userBugNumList.length>0){
                        this.userBugNumList.forEach(userBugNum=>{
                            this.userX.push(userBugNum.userName);
                            this.userBugList.push(userBugNum.bugNum);
                            this.userOpsList.push(userBugNum.optimizationNum);
                            this.userAssList.push(userBugNum.assistanceNum);
                        });

                    }
                    this.drawLine12()
                })
            },
            //分页查询bug
            fetchBugPage(){
              Http.zsyPostHttp('/bug/new/page',this.bugReqDTO,(res)=>{
                  if (res){
                      this.bugPage = res.data.list;
                      this.bugFormPage.total = res.data.total
                  }
              });
                this.fetchDiffTypeBugNum()
            },
            //分页查询bug(老数据)
            fetchOldBugPage(){
                Http.zsyPostHttp('/bug/old/page',this.oldBugReqDTO,(res)=>{
                    if (res){
                        this.oldBugPage = res.data.list;
                        this.oldBugFormPage.total = res.data.total
                    }
                })
            },
            closeDialog(){
                this.onlineBugForm.projectId = null;
                this.onlineBugForm.taskId = null;
                this.onlineBugForm.origin = null;
                this.onlineBugForm.discoverTime = null;
                this.onlineBugForm.processTime = null;
                this.onlineBugForm.description = null;
                this.onlineBugForm.accountInfo = null;
                this.onlineBugForm.demandSystemId = null;
                this.onlineBugForm.remark = null;
                this.onlineBugForm.type = 0;
                this.onlineBugForm.isSolved = 0;
                this.isSaving = false;
            },

            //查询年度已完成任务总耗时(设计,产品,开发,测试)
            fetchDiffStageTaskTime(){
                if (this.admin){
                    this.taskTimeData.totalTaskTime = 0;
                    this.diffStageTime = [];
                    this.diffStageAvgTime = [];
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
                            this.$nextTick(()=>{
                                this.drawLine7()
                            })
                        }
                    })
                }
            },

            //查询耗时前十名的多人任务
            fetchTop10MostTimeTask(){
                this.top10TaskList = [];
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
                if (taskId != null && taskId !== '') {
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
                if (time && time.length === 2) {
                    this.signInReqDTO.beginTime = moment(time[0]).format('YYYY-MM-DD 00:00:00');
                    this.signInReqDTO.endTime = moment(time[1]).format('YYYY-MM-DD 23:59:59')
                } else {
                    this.signInReqDTO.beginTime = this.signInReqDTO.endTime = this.signInDaterange = ''
                }
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
                // this.signInReqDTO.beginTime = moment(time2).format('YYYY-MM-DD 00:00:00');
                // this.signInReqDTO.endTime = moment(time3).format('YYYY-MM-DD 23:59:59');
                this.signInReqDTO.beginTime = time2 + ' 00:00:00';
                this.signInReqDTO.endTime = time3 + ' 23:59:59';

            },
            selectSignInData(){
                this.signInReqDTO.pageNum = 1;
                if (this.signInReqDTO.beginTime != null && this.signInReqDTO.beginTime !== ''){
                    this.signInReqDTO.beginTime = moment(this.signInReqDTO.beginTime).format('YYYY-MM-DD 00:00:00');
                }
                if (this.signInReqDTO.endTime != null && this.signInReqDTO.endTime !== '') {
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
                                let checkTimeStr = '';
                                signIn.checkTimeList.forEach(checkTime => {
                                    checkTimeStr = checkTimeStr + moment(checkTime).format("HH:mm:ss") + ', '
                                });
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
                if (this.eWorkTimeUserId == null || this.eWorkTimeUserId === ''){
                    this.$message({
                        showClose: true,
                        message: '请选择查询的用户',
                        type: 'warning'
                    });
                    return false;
                }
                if (this.workMonth1 != null && this.workMonth1 !== ''){
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
                if (this.yearMonth1 != null && this.yearMonth1 !== ''){
                    let today = new Date(this.yearMonth1);
                    let month=today.getMonth();
                    let nextMonth=++month;
                    let nextMonthFirstDay=new Date(today.getFullYear(),nextMonth,1);
                    let oneDay=1000*60*60*24;
                    let dateString=new Date(nextMonthFirstDay-oneDay);
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
                if (this.yearMonth2 != null && this.yearMonth2 !== ''){
                    let today = new Date(this.yearMonth2);
                    let month=today.getMonth();
                    let nextMonth=++month;
                    let nextMonthFirstDay=new Date(today.getFullYear(),nextMonth,1);
                    let oneDay=1000*60*60*24;
                    let dateString=new Date(nextMonthFirstDay-oneDay);
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

                    this.mantisDeveloperBugMonthList = [];
                    this.seriesDataList3 = [];
                    this.onlineBugDeveloperList = [];
                    this.onlineBugTotalNum3 = 0;
                    this.fetchOnlineBugGroupByDeveloper();
                }else {
                    this.onlineBugTotalNum = 0;
                    this.mantisUserBugMonthList = [];
                    this.seriesDataList2 = [];
                    this.onlineBugUserList = [];

                    this.mantisDeveloperBugMonthList = [];
                    this.seriesDataList3 = [];
                    this.onlineBugDeveloperList = [];
                    this.onlineBugTotalNum3 = 0;

                    this.mantisBugReqDTO3.beginTime = '';
                    this.mantisBugReqDTO3.endTime = '';
                    this.fetchOnlineBugGroupByUser();
                    this.fetchOnlineBugGroupByDeveloper();
                }
            },
            changeMonth3(){
                if (this.yearMonth3 != null && this.yearMonth3 !== ''){
                    let today = new Date(this.yearMonth3);
                    let month=today.getMonth();
                    let nextMonth=++month;
                    let nextMonthFirstDay=new Date(today.getFullYear(),nextMonth,1);
                    let oneDay=1000*60*60*24;
                    let dateString=new Date(nextMonthFirstDay-oneDay);
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
                if (this.yearMonth4 != null && this.yearMonth4 !== ''){
                    let today = new Date(this.yearMonth4);
                    let month=today.getMonth();
                    let nextMonth=++month;
                    let nextMonthFirstDay=new Date(today.getFullYear(),nextMonth,1);
                    let oneDay=1000*60*60*24;
                    let dateString=new Date(nextMonthFirstDay-oneDay);
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
              if (this.yearWeek != null && this.yearWeek !== ''){
                  let weekFisrt = new Date(this.yearWeek);
                  let time1=weekFisrt.getFullYear()+"-"+(weekFisrt.getMonth()+1)+"-"+weekFisrt.getDate();//time1表示当前时间
                  let date2 = new Date(weekFisrt);
                  date2.setDate(weekFisrt.getDate() + 6);
                  let time2 = date2.getFullYear()+"-"+(date2.getMonth()+1)+"-"+date2.getDate();
                  let beginTime = time1+' 00:00:00';
                  let endTime = time2+' 23:59:59';
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
                this.mantisProject = '';
            },
            closeMantisVisible(){
                this.selectMantisProjectVisible = false;
            },
            importBugInfo(file){
                this.fullscreenLoading = true;
                this.importBugVisible = false;
                let data = new FormData();
                data.append('uploadFile', file.file);
                Http.zsyPostHttp('/mantis-bug/import',data,(res)=> {
                    if (res.errMsg === "执行成功") {
                        this.$refs.bugData.clearFiles();
                        this.$message({
                            showClose: true,
                            message: '导入成功',
                            type: 'success'
                        });
                        this.fullscreenLoading = false;
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
                    }else {
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
            exportBugInfo(){
                if (this.mantisProject != null &&  this.mantisProject !== ''){
                    this.selectMantisProjectVisible = false;
                    let project = this.mantisProjectList.filter(mantisProject=>(mantisProject.id === this.mantisProject))[0];
                    this.$confirm('确认导出 <'+project.name+'> 的bug信息?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.fullscreenLoading = true;
                        Http.zsyGetHttp('/mantis-bug/export/'+this.mantisProject,{},(res)=>{
                            if (res.errMsg === '执行成功'){
                                this.$message({
                                    showClose: true,
                                    message: '导出成功',
                                    type: 'success'
                                });
                                this.fullscreenLoading = false;
                                this.urlList = res.data;
                                this.urlList.forEach(url=>{
                                    window.open (url)
                                })
                            }
                        },err=>{
                            this.fullscreenLoading = false;
                            this.$message({
                                showClose: true,
                                message: err.errMsg,
                                type: 'error'
                            });
                        })
                    }).catch(() => {
                    });
                } else {
                    this.$message({
                        showClose: true,
                        message: '请选择项目',
                        type: 'warning'
                    });
                    this.fullscreenLoading = false;
                }
            },
            fetchBugWeekGroupByUser(){
                this.mantisUserBugWeekList = [];
                this.seriesDataList = [];
                Http.zsyPostHttp('/mantis-bug/bug-week/user',this.mantisBugReqDTO2,(res)=>{
                    this.mantisUserBugWeekList = res.data;

                    this.mantisUserBugWeekList.forEach(bugWeek=>{
                        this.dateList = bugWeek.dateStrList;
                        this.weekdayList = bugWeek.weekdayList;
                        this.userNameList.push(bugWeek.userName);
                        let seriesData = {
                            'name':bugWeek.userName,
                            'type':'line',
                            'data':bugWeek.bugNumList
                        };
                        this.seriesDataList.push(seriesData)
                    });
                    if (this.permit){
                        this.$nextTick(()=>{
                            this.drawLine8()
                        })
                    }

                })
            },
            fetchOnlineBugGroupByUser(){
                this.onlineBugTotalNum = 0;
                this.mantisUserBugMonthList = [];
                this.seriesDataList2 = [];
                this.onlineBugUserList = [];
                Http.zsyPostHttp('/mantis-bug/online-bug/user',this.mantisBugReqDTO3,(res)=>{
                    this.mantisUserBugMonthList = res.data;

                    this.mantisUserBugMonthList.forEach(bugUserMonth=>{
                        let bugUserMonthData = {
                            'name': bugUserMonth.userName+'('+ bugUserMonth.bugNum+')',
                            'value': bugUserMonth.bugNum
                        };
                        this.onlineBugUserList.push(bugUserMonth.userName+'('+ bugUserMonth.bugNum+')');
                        this.seriesDataList2.push(bugUserMonthData);
                        this.onlineBugTotalNum += bugUserMonth.bugNum;
                    });
                    if (this.permit){
                        this.$nextTick(()=>{
                            this.drawLine9()
                        })
                    }
                })
            },
            fetchOnlineBugGroupByDeveloper(){
                this.mantisDeveloperBugMonthList = [];
                this.seriesDataList3 = [];
                this.onlineBugDeveloperList = [];
                this.onlineBugTotalNum3 = 0;
                Http.zsyPostHttp('/mantis-bug/online-bug/develop',this.mantisBugReqDTO3,(res)=>{
                    this.mantisDeveloperBugMonthList = res.data;

                    this.mantisDeveloperBugMonthList.forEach(bugUserMonth=>{
                        let bugUserMonthData = {
                            'name': bugUserMonth.userName+'('+ bugUserMonth.bugNum+')',
                            'value': bugUserMonth.bugNum
                        };
                        this.onlineBugDeveloperList.push(bugUserMonth.userName+'('+ bugUserMonth.bugNum+')');
                        this.seriesDataList3.push(bugUserMonthData);
                        this.onlineBugTotalNum3 += bugUserMonth.bugNum;
                    });
                    if (this.permit){
                        this.$nextTick(()=>{
                            this.drawLine10()
                        })
                    }
                })
            },
            fetchBugStatsGroupByTask(){
                Http.zsyPostHttp('/mantis-bug/stats/task',this.mantisBugReqDTO4,(res)=>{
                    this.taskBugStatsList = res.data.list;
                    this.taskBugPage.total = res.data.total;
                })
            },
            //查看当前环境,用于显示  导出mantisbug按钮
            getEnv(){
                Http.zsyGetHttp('/mantis-bug/env',{},(res)=>{
                    this.environment = res.data;
                })
            },
            handleRecordPreview(){

            },
            handleRecordRemove(){

            },
            beforeUpload(file) {
                let suffix = file.name.substring(file.name.lastIndexOf(".")+1);
                const isXls = file.name.substring(file.name.lastIndexOf(".")+1) === "xls";
                const isExcel = file.type === 'application/vnd.ms-excel' || file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
                const isLt2M = file.size / 1024 / 1024 < 2;
                // if ("xls" != suffix) {
                //     this.$message.error('上传文件只能是".xls"格式!');
                // }
                if (!isExcel) {
                    this.$message.error('上传文件只能是excel 格式!');
                    return false
                }
                if (!isLt2M) {
                    this.$message.error('上传文件大小不能超过 2MB!');
                }
                return isExcel && isLt2M && isXls;
            },
            //查询周发版计划
            fetchWeekPublishPlan(){
                this.weekPublishReqDTO.beginTime = moment(this.weekPublishReqDTO.beginTime).format('YYYY-MM-DD 00:00:00');
                this.weekPublishReqDTO.endTime = moment(this.weekPublishReqDTO.endTime).format('YYYY-MM-DD 23:59:59');
                Http.zsyPostHttp('week-publish/list',this.weekPublishReqDTO,(res)=>{
                    this.weekPublishData = res.data.list;
                    this.weekPublishPage.total = res.data.total;
                })
            },
            //根据任务负责人分组查询周发版计划
            fetchWeekPublishPlanByCharge(){
                this.weekPublishReqDTO.beginTime = moment(this.weekPublishReqDTO.beginTime).format('YYYY-MM-DD 00:00:00');
                this.weekPublishReqDTO.endTime = moment(this.weekPublishReqDTO.endTime).format('YYYY-MM-DD 23:59:59');
                Http.zsyPostHttp('week-publish/list/by-charge',this.weekPublishReqDTO,(res)=>{
                    this.weekPublishByChargeData = res.data;
                })
            },

            //查询平台
            fetchPlatformList() {
                Http.zsyGetHttp('/platform/list', {}, (resp) => {
                    this.platformList = resp.data;
                })
            },
            //查询进行中的任务
            fetchAllDoingTasks(){
                Http.zsyGetHttp('/task/doing/all',{},res=>{
                    this.doingTaskList = res.data;
                })
            },
            initTime(){
                let date = new Date();
                // 本周一的日期
                date.setDate(date.getDate() - date.getDay() + 1);
                let begin = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 00:00:00";

                // 本周日的日期
                date.setDate(date.getDate() + 6);
                let end = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 23:59:59";
                this.weekPublishReqDTO.beginTime = begin;
                this.weekPublishReqDTO.endTime = end;

                // this.fetchWeekPublishPlanByCharge();
                this.fetchWeekPublishPlan()
            },
            initTime2(){
                let date = new Date();
                // 本周一的日期
                date.setDate(date.getDate() - date.getDay() + 1);
                let begin = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 00:00:00";

                // 本周日的日期
                date.setDate(date.getDate() + 6);
                let end = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 23:59:59";
                this.beginTime = begin;
                this.endTime = end;
                this.personVacationReqDTO.beginTime = begin;
                this.personVacationReqDTO.endTime = end;

                this.fetchPersonVacation()
            },
            initTime3(){
                let date = new Date();
                // 本周一的日期
                date.setDate(date.getDate() - date.getDay() + 1);
                let begin = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 00:00:00";

                // 本周日的日期
                date.setDate(date.getDate() + 6);
                let end = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 23:59:59";
                this.leaveBeginTime = begin;
                this.leaveEndTime = end;
                this.leaveList.beginTime = begin;
                this.leaveList.endTime = end;

                this.getLeaveList()
            },
            initTime4(){
                let date = new Date();
                // 本周一的日期
                date.setDate(date.getDate() - date.getDay() + 1);
                let begin = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 00:00:00";

                // 本周日的日期
                date.setDate(date.getDate() + 6);
                let end = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 23:59:59";
                this.ewBeginTime = begin;
                this.ewEndTime = end;
                this.extraWorkReqDTO.beginTime = begin;
                this.extraWorkReqDTO.endTime = end;

                this.getExtraWorkStats()
            },
            editWeekPublish(weekPublish){
                this.weekPublish.id = weekPublish.wppId;
                this.weekPublish.taskId = weekPublish.taskId;
                this.weekPublish.condition = weekPublish.condition;
                this.weekPublish.specialTestTime = weekPublish.specialTestTime;
                this.weekPublish.zujuan = weekPublish.zujuan;
                this.weekPublish.yuejuan = weekPublish.yuejuan;
                this.weekPublish.saomiao = weekPublish.saomiao;
                this.weekPublish.xueyebaogao = weekPublish.xueyebaogao;
                this.weekPublish.chanpin = weekPublish.chanpin;
                this.weekPublish.realTestTime = weekPublish.realTestTime;
                this.weekPublish.onlineTime = weekPublish.onlineTime;
                this.weekPublish.realOnlineTime = weekPublish.realOnlineTime;
                // weekPublish.platformResDTOS.forEach(platform=>{
                //     this.weekPublish.platforms.push(platform.id)
                // });
                this.editWeekPublishVisible = true;
            },
            saveEditWeekPublish(){
                Http.zsyPostHttp('/week-publish/edit',this.weekPublish,(res)=>{
                    if (res){
                        this.$message({
                            showClose: true,
                            message: '修改成功',
                            type: 'success'
                        });
                        // this.fetchWeekPublishPlanByCharge();
                        this.fetchWeekPublishPlan();
                        this.clearWeekPublish();
                        this.editWeekPublishVisible = false;
                    }
                })
            },
            updateCanOnline(canOnline,id){
                  if (canOnline === 0){
                      this.weekPublish.canOnline = 1;
                      this.weekPublish.id = id;
                  } else if (canOnline === 1){
                      this.weekPublish.canOnline = 0;
                      this.weekPublish.id = id;
                  }
                  this.saveEditWeekPublish()
            },
            changeWeekPublishTime(){
                if (this.weekPublishReqDTO.date != null && this.weekPublishReqDTO.date !== '') {
                    let date = this.weekPublishReqDTO.date;
                    // 本周一的日期
                    date.setDate(date.getDate() - date.getDay() + 1);
                    let begin = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 00:00:00";

                    // 本周日的日期
                    date.setDate(date.getDate() + 6);
                    let end = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 23:59:59";

                    this.weekPublishReqDTO.beginTime = begin;
                    this.weekPublishReqDTO.endTime = end;
                    this.fetchWeekPublishPlan()
                }

            },
            closeEditWeekPublish(){
                this.clearWeekPublish();
            },
            clearWeekPublish(){
                this.weekPublish.id = '';
                this.weekPublish.condition = '';
                this.weekPublish.specialTestTime = '';
                this.weekPublish.zujuan = '';
                this.weekPublish.yuejuan = '';
                this.weekPublish.saomiao = '';
                this.weekPublish.xueyebaogao = '';
                this.weekPublish.chanpin = '';
                this.weekPublish.realTestTime = '';
                this.weekPublish.onlineTime = '';
                this.weekPublish.realOnlineTime = '';
                this.weekPublish.canOnline = null;
                this.weekPublish.platforms = [];
                // this.weekPublishReqDTO.date = ''
            },
            addFormTagId(platformId, num, $event) {
                if (this.hasClass($event.currentTarget, 'active')) {
                    this.removeClass($event.currentTarget, 'active');
                    if (num === 1) {
                        this.weekPublish.platforms.splice(this.findIndex(this.weekPublish.platforms, platformId), 1);
                    }
                } else {
                    this.addClass($event.currentTarget, 'active');
                    if (num === 1) {
                        this.weekPublish.platforms.push(platformId);
                    }
                }
            },
            hasClass(obj, cls) {
                return obj.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
            },
            addClass(obj, cls) {
                if (!this.hasClass(obj, cls)) obj.className += " " + cls;
            },
            removeClass(obj, cls) {
                if (this.hasClass(obj, cls)) {
                    let reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
                    obj.className = obj.className.replace(reg, ' ');
                }
            },
            findIndex(arr, val) {
                for (let i = 0; i < arr.length; i++) {
                    if (arr[i] === val) {
                        return i;
                    }
                }
            },
            getExtraWorkStats(){
                Http.zsyPostHttp('/stats/extra-work/page',this.extraWorkReqDTO,(res)=>{
                    this.extraWorkStatsList = res.data.list;
                    this.extraWorkPage.total = res.data.total;
                    this.extraWorkStatsList.forEach(extraWork=>{
                        if (extraWork.checkRecords.length > 0) {
                            let checkTimeStr = '';
                            extraWork.checkRecords.forEach(checkTime => {
                                checkTimeStr = checkTimeStr + moment(checkTime).format("HH:mm:ss") + ', '
                            });
                            extraWork.checkRecords = checkTimeStr.substring(0,checkTimeStr.length-2)
                        }
                    })
                })
            },
            searchEWorkStats(){
                this.extraWorkReqDTO.pageNum = 1;
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
            getWPCurrentWeek(){
                this.weekPublishReqDTO.date=moment().toDate();
                let date = new Date();
                // 本周一的日期
                date.setDate(date.getDate() - date.getDay() + 1);
                let begin = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 00:00:00";

                // 本周日的日期
                date.setDate(date.getDate() + 6);
                let end = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 23:59:59";
                this.weekPublishReqDTO.beginTime = begin;
                this.weekPublishReqDTO.endTime = end;
                // this.fetchWeekPublishPlan();
            },
            getWPLastWeek(){
                let date = new Date();
                // 本周一的日期
                date.setDate(date.getDate() - date.getDay() - 6);
                let begin = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 00:00:00";

                // 本周日的日期
                date.setDate(date.getDate() + 6);
                let end = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 23:59:59";
                this.weekPublishReqDTO.beginTime = begin;
                this.weekPublishReqDTO.endTime = end;
                this.weekPublishReqDTO.date = date;
            },
            getWPNextWeek(){
                let date = new Date();
                // 本周一的日期
                date.setDate(date.getDate() - date.getDay() + 8);
                let begin = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 00:00:00";

                // 本周日的日期
                date.setDate(date.getDate() + 6);
                let end = date.getFullYear() + "-" + (date.getMonth() + 1) + "-" + date.getDate() + " 23:59:59";
                this.weekPublishReqDTO.beginTime = begin;
                this.weekPublishReqDTO.endTime = end;
                this.weekPublishReqDTO.date = date;
            },

            closeRestHoursDialog(){
                this.restHours = 0;
                this.restHoursUserId = '';
                this.restHoursUserName = '';
            },
            fetchUserRestHours(){
                if (this.restHoursUserId != null && this.restHoursUserId !== undefined && this.restHoursUserId !==  ''){
                    Http.zsyGetHttp('/user/'+this.restHoursUserId,{},(res)=>{
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
                if (this.restHours > 9999 || this.restHours < 0) {
                    this.$message({showClose: true, message: '调休时长正确值应为0.1~9999', type: 'error'});
                    this.restHourLoading = false;
                    return false;
                }

                let reqDTO = {
                    userId:this.restHoursUserId,
                    restHours:this.restHours
                };
                Http.zsyPostHttp('/user/rest-hours/update',reqDTO,res=>{
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

            showUserRestHoursLog(){
                if (this.restHoursUserId != null && this.restHoursUserId !== undefined && this.restHoursUserId !==  '') {
                    this.userRestHoursLogReqDTO.userId = this.restHoursUserId;
                    Http.zsyPostHttp('/sign-in/rest-hours-log/page',this.userRestHoursLogReqDTO,res=>{
                        this.userRestHoursLogData = res.data.list;
                        this.userRestHoursLogPage.total = res.data.total;
                        this.userRestHoursDetailVisible = true;
                    })
                }else {
                    this.$message({
                        showClose: true,
                        message: '请选择用户',
                        type: 'warning'
                    });
                }
            },
            showUserRestHoursLog2(userId,userName){
                if (userId != null && userId !== undefined && userId !== '') {
                    // this.userRestHoursLogReqDTO2.userId = userId;
                    // this.userRestHoursLogReqDTO2.userName = userName;
                    // Http.zsyPostHttp('/sign-in/rest-hours-log/page',this.userRestHoursLogReqDTO2,res=>{
                    //     this.userRestHoursLogData2 = res.data.list;
                    //     this.userRestHoursLogPage2.total = res.data.total;
                    //     this.userRestHoursDetailVisible2 = true;
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
            userRestHoursLogHandleCurrentChange(currentPage){
                this.userRestHoursLogReqDTO.pageNum = currentPage;
                this.showUserRestHoursLog();
            },
            userRestHoursLogHandleCurrentChange2(currentPage){
                this.userRestHoursLogReqDTO2.pageNum = currentPage;
                this.showUserRestHoursLog2(this.userRestHoursLogReqDTO2.userId);
            },
            //查看周人员投入表
            fetchWeekUserCost(){
                if (this.userCostReqDTO.groupId == null
                || this.userCostReqDTO.groupId === undefined
                || this.userCostReqDTO.groupId === ''){
                    this.$message({
                        showClose: true,
                        message: '请选择团队',
                        type: 'warning'
                    });
                    return;
                }
                if (this.userCostReqDTO.date == null
                    || this.userCostReqDTO.date === undefined
                    || this.userCostReqDTO.date === ''){
                    this.$message({
                        showClose: true,
                        message: '请选择时间',
                        type: 'warning'
                    });
                    return;
                }
                this.userCostReqDTO.date = moment(this.userCostReqDTO.date).format('YYYY-MM-DD HH:mm:ss');
                this.userCostReqDTO.weekNumber = moment(this.userCostReqDTO.date).week();
                Http.zsyPostHttp('/stats/week-user-cost/v2',this.userCostReqDTO , (resp) => {
                    this.weekUserCostList = resp.data;
                    this.getSpanArr(this.weekUserCostList)
                })

            },
            getSpanArr(data) {

                let that = this;
                //页面展示的数据，不一定是全部的数据，所以每次都清空之前存储的 保证遍历的数据是最新的数据。以免造成数据渲染混乱
                that.spanArr = [];
                that.pos = 0;
                that.userSpanArr = [];
                that.userPos = 0;
                that.weekHourSpanArr = [];
                that.weekHourPos = 0;
                that.leaveHourSpanArr = [];
                that.leaveHourPos = 0;
                that.userPercentSpanArr = [];
                that.userPercentPos = 0;
                that.positionPercentSpanArr = [];
                that.positionPercentPos = 0;
                //遍历数据
                data.forEach((item, index) => {
                    //判断是否是第一项
                    if (index === 0) {
                        this.spanArr.push(1);
                        this.pos = 0;
                        this.userSpanArr.push(1);
                        this.userPos = 0;
                        this.weekHourSpanArr.push(1);
                        this.weekHourPos = 0;
                        this.leaveHourSpanArr.push(1);
                        this.leaveHourPos = 0;
                        this.userPercentSpanArr.push(1);
                        this.userPercentPos = 0;
                        this.positionPercentSpanArr.push(1);
                        this.positionPercentPos = 0;
                    } else {
                        //不是第一项时，就根据标识去存储
                        if (data[index].jobRoleName === data[index - 1].jobRoleName) {
                            // 查找到符合条件的数据时每次要把之前存储的数据+1
                            this.spanArr[this.pos] += 1;
                            this.spanArr.push(0)
                        } else {
                            // 没有符合的数据时，要记住当前的index
                            this.spanArr.push(1);
                            this.pos = index
                        }

                        //不是第一项时，就根据标识去存储
                        if (data[index].userName === data[index - 1].userName) {
                            // 查找到符合条件的数据时每次要把之前存储的数据+1
                            this.userSpanArr[this.userPos] += 1;
                            this.userSpanArr.push(0)
                        } else {
                            // 没有符合的数据时，要记住当前的index
                            this.userSpanArr.push(1);
                            this.userPos = index
                        }

                        //不是第一项时，就根据标识去存储
                        if (data[index].totalHours === data[index - 1].totalHours
                        && data[index].userName === data[index - 1].userName) {
                            // 查找到符合条件的数据时每次要把之前存储的数据+1
                            this.weekHourSpanArr[this.weekHourPos] += 1;
                            this.weekHourSpanArr.push(0)
                        } else {
                            // 没有符合的数据时，要记住当前的index
                            this.weekHourSpanArr.push(1);
                            this.weekHourPos = index
                        }

                        //不是第一项时，就根据标识去存储
                        if (data[index].leaveHours === data[index - 1].leaveHours
                            && data[index].userName === data[index - 1].userName) {
                            // 查找到符合条件的数据时每次要把之前存储的数据+1
                            this.leaveHourSpanArr[this.leaveHourPos] += 1;
                            this.leaveHourSpanArr.push(0)
                        } else {
                            // 没有符合的数据时，要记住当前的index
                            this.leaveHourSpanArr.push(1);
                            this.leaveHourPos = index
                        }

                        //不是第一项时，就根据标识去存储
                        if (data[index].hourPercent === data[index - 1].hourPercent
                            && data[index].userName === data[index - 1].userName) {
                            // 查找到符合条件的数据时每次要把之前存储的数据+1
                            this.userPercentSpanArr[this.userPercentPos] += 1;
                            this.userPercentSpanArr.push(0)
                        } else {
                            // 没有符合的数据时，要记住当前的index
                            this.userPercentSpanArr.push(1);
                            this.userPercentPos = index
                        }

                        //不是第一项时，就根据标识去存储
                        if (data[index].positionHourPercent === data[index - 1].positionHourPercent
                        && data[index].jobRoleName === data[index - 1].jobRoleName) {
                            // 查找到符合条件的数据时每次要把之前存储的数据+1
                            this.positionPercentSpanArr[this.positionPercentPos] += 1;
                            this.positionPercentSpanArr.push(0)
                        } else {
                            // 没有符合的数据时，要记住当前的index
                            this.positionPercentSpanArr.push(1);
                            this.positionPercentPos = index
                        }
                    }
                })
            },
            // 列表方法
            objectSpanMethod({row, column,rowIndex, columnIndex}) {

            // 页面列表上 表格合并行 -> 第几列(从0开始)
            // 需要合并多个单元格时 依次增加判断条件即可
                if (columnIndex === 0) {
                    // 二维数组存储的数据 取出
                    const _row = this.spanArr[rowIndex]
                    const _col = _row > 0 ? 1 : 0
                    return {
                        rowspan: _row,
                        colspan: _col
                    }
                    //不可以return {rowspan：0， colspan: 0} 会造成数据不渲染， 也可以不写else，eslint过不了的话就返回false
                } else if(columnIndex === 1){
                    const _row = this.userSpanArr[rowIndex]
                    const _col = _row > 0 ? 1 : 0
                    return {
                        rowspan: _row,
                        colspan: _col
                    }
                } else if(columnIndex === 4){
                    const _row = this.weekHourSpanArr[rowIndex]
                    const _col = _row > 0 ? 1 : 0
                    return {
                        rowspan: _row,
                        colspan: _col
                    }
                } else if(columnIndex === 5){
                    const _row = this.leaveHourSpanArr[rowIndex]
                    const _col = _row > 0 ? 1 : 0
                    return {
                        rowspan: _row,
                        colspan: _col
                    }
                } else if(columnIndex === 6){
                    const _row = this.userPercentSpanArr[rowIndex]
                    const _col = _row > 0 ? 1 : 0
                    return {
                        rowspan: _row,
                        colspan: _col
                    }
                } else if(columnIndex === 7){
                    const _row = this.positionPercentSpanArr[rowIndex]
                    const _col = _row > 0 ? 1 : 0
                    return {
                        rowspan: _row,
                        colspan: _col
                    }
                }

                else {
                    return false
                }
            },

            //查看用户调休
            fetchAllUsersRestHours(){
                if (this.restHourYear != null && this.restHourYear  !== undefined && this.restHourYear !== ''){
                    this.restHourReqDTO.year = moment(this.restHourYear).format("YYYY-MM-DD");
                    this.restHourReqDTO.year = this.restHourReqDTO.year.substring(0,4);
                }else {
                    this.restHourReqDTO.year = null
                }
                Http.zsyPostHttp('sign-in/rest-hours/list',this.restHourReqDTO,res=>{
                    this.restHoursData = res.data;
                })
            },
            //手动 新增调休记录
            addRestHoursLog(){
                this.userRestHoursDetailVisible2 = false;
                this.editRestHoursVisible = true;
            },
            //保存修改调休日志
            saveAddRestHoursLog(){
                this.restHourLoading = true;
                this.userRestHoursLogForm.userId = this.userRestHoursLogReqDTO2.userId;
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
                        this.editRestHoursVisible = false;
                        this.fetchAllUsersRestHours()
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
            //取消添加
            cancelAddRestHoursLog(){
                this.userRestHoursLogForm.userId = null;
                this.userRestHoursLogForm.recordTime = null;
                this.userRestHoursLogForm.restHour = 0;
                this.userRestHoursLogForm.content = '';
                this.editRestHoursVisible = false;
                this.restHourLoading = false;
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
        margin-left: 10px;
        margin-top: 10px;
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
      padding-bottom: 5px;
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
        width: 1300px;
        font-size: 14px;
        background: #fff;
        padding: 30px 0;
        box-shadow: 0 0 10px #ccc;
        margin: auto;
        height: 500px;
    }
    .bug-stats-con {
        width: 1300px;
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
<style>
    .aaa .el-dialog--small {
        width: 755px;
    }

    .wpp .el-dialog--small {
        width: 500px;
    }
    .pre{
        white-space:pre-wrap;
        white-space:-moz-pre-wrap;
        white-space:-pre-wrap;
        white-space:-o-pre-wrap;
        word-wrap:break-word;
    }
</style>

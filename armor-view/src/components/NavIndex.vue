<template>
    <div class="nav-index-con">
        <div class="my-integral-con" v-show="userRole>0">
            <p class="mic-title">我的积分</p>
            <div class="mic-main clearfix">
                <div class="mic-item fl" v-for="(item,key) in integralItem">
                    <div class="mic-item-title" ><img :src="`${require(`../assets/img/icon_${key+1}.png`)}`" class="icon-score">{{item.label}}</div>
                    <div class="mic-item-title" style="font-size: 12px">({{item.time}})</div>
                    <div class="mic-item-integral">{{item.score}}</div>
                </div>
            </div>
        </div>

        <div class="my-task-con">
            <div v-show="userRole>0">
                <div class="add-task" @click="createTaskClick">
                    <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                    <span>创建个人任务</span>
                </div>
                <div class="add-task help" @click="editHelpVisible=true">
                    <span class="add-task-icon"><i class="el-icon-plus"></i></span>
                    <span>创建积分转移</span>
                </div>

                <div class="add-task leave" @click="editLeaveVisible=true">
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
                        <!--<el-tab-pane label="审核失败" name="applyFail">
                            <task-item :taskItems="task.applyFail" :isPrivate="true"
                                       taskStatus="ApplyFail" @reload="reload"></task-item>
                        </el-tab-pane>-->
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
                        </el-tab-pane>
                    </el-tabs>
                </div>
                <div>
                    <p class="mic-title">我的积分转移</p>
                    <div class="my-task-detail">
                        <el-tabs v-model="activeHelpName" @tab-click="handleClick">
                            <el-tab-pane label="审核中" name="wait">
                                <!--@click="reviewDetail(help)"-->
                                <div class="task-lis" v-for="help in review.wait" >
                                    <div class="head-img" ><img src="../assets/img/waitAudit.png" ></div>
                                    <div class="main-task-detail">
                                        <div class="task-name"><span>{{help.description}}</span></div>
                                        <div class="task-state">
                                            <span class="task-end blue">求助目标：{{help.name.split(",")[0]}}</span>
                                            <span class="task-end blue">{{help.time| formatDate }}</span>
                                            <span class="task-time-opt"><i class="el-icon-edit" @click="editHelpDetail(help)"></i></span>
                                        </div>
                                    </div>
                                    <div class="task-data-show">
                                        <span class="task-score">求助转移：{{help.integral}} 分</span>
                                    </div>
                                    <div class="task-username">
                                        <img class="task-avatar" v-if="help.avatarUrl && help.avatarUrl!=''" :src="help.avatarUrl" >
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
                                    <div class="head-img" ><img src="../assets/img/auditSuccess.png" ></div>
                                    <div class="main-task-detail">
                                        <div class="task-name"><span>{{help.description}}</span></div>
                                        <div class="task-state">
                                            <span class="task-end blue">求助目标：{{help.name.split(",")[0]}}</span>
                                            <span class="task-end blue">{{help.time| formatDate }}</span>
                                            <span class="task-time-opt"><i class="el-icon-circle-check" ></i></span>
                                        </div>
                                    </div>
                                    <div class="task-data-show">
                                        <span class="task-score">转移完成：-{{help.integral}} 分</span>
                                    </div>
                                    <div class="task-username">
                                        <img class="task-avatar" v-if="help.avatarUrl && help.avatarUrl!=''" :src="help.avatarUrl" >
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
                <p class="mic-title">积分求助转移</p>
                <el-tabs v-model="auditHelpTabsActiveName" @tab-click="handleClick">
                    <el-tab-pane label="待审核" name="wait">
                        <div class="task-lis" v-for="help in review.wait" @click="reviewDetail(help)">
                            <div class="head-img" ><img src="../assets/img/waitAudit.png" ></div>
                            <div class="main-task-detail">
                                <div class="task-name"><span>{{help.description}}</span></div>
                                <div class="task-state">
                                    <span class="task-end blue">求助目标：{{help.name.split(",")[0]}}</span>
                                    <span class="task-end blue">{{help.time| formatDate }}</span>
                                    <span class="task-time-opt"><i class="el-icon-edit" ></i></span>
                                </div>
                            </div>
                            <div class="task-data-show">
                                <span class="task-score">求助转移：{{help.integral}} 分</span>
                            </div>
                            <div class="task-username">
                                <img class="task-avatar" v-if="help.avatarUrl && help.avatarUrl!=''" :src="help.avatarUrl" >
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
                            <div class="head-img" ><img src="../assets/img/waitAudit.png" ></div>
                            <div class="main-task-detail">
                                <div class="task-name"><span>{{help.description}}</span></div>
                                <div class="task-state">
                                    <span class="task-end blue">求助目标：{{help.name.split(",")[0]}}</span>
                                    <span class="task-end blue">{{help.time| formatDate }}</span>
                                    <span class="task-time-opt"><i class="el-icon-circle-check" ></i></span>
                                </div>
                            </div>
                            <div class="task-data-show">
                                <span class="task-score">转移完成：-{{help.integral}} 分</span>
                            </div>
                            <div class="task-username">
                                <img class="task-avatar" v-if="help.avatarUrl && help.avatarUrl!=''" :src="help.avatarUrl" >
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
            <div>
                <p class="mic-title">{{userRole!=0?'请假申请':'请假申请审核'}}</p>
                <div class="my-task-detail">
                    <el-tabs v-model="activeLeaveName" @tab-click="handleClick">
                        <el-tab-pane :label="userRole!=0?'审核中':'待审核'" name="wait">
                            <!--@click="reviewDetail(help)"-->
                            <div class="task-lis" v-for="leave in leaveList.wait" >
                                <div class="head-img" ><img src="../assets/img/waitAudit.png" ></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>{{leave.description}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">{{leave.createTime| formatDate }}</span>
                                        <span class="task-time-opt"><i class="el-icon-edit" @click="editLeaveDetail(leave,0)"></i></span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="leave.avatarUrl && leave.avatarUrl!=''" :src="leave.avatarUrl" >
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
                                <div class="head-img" ><img src="../assets/img/auditSuccess.png" ></div>
                                <div class="main-task-detail">
                                    <div class="task-name"><span>{{leave.description}}</span></div>
                                    <div class="task-state">
                                        <span class="task-end blue">{{leave.createTime| formatDate }}</span>
                                        <span class="task-time-opt"><i class="el-icon-circle-check" ></i></span>
                                    </div>
                                </div>
                                <div class="task-username">
                                    <img class="task-avatar" v-if="leave.avatarUrl && leave.avatarUrl!=''" :src="leave.avatarUrl" >
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
        </div>
        <el-dialog
                title="创建个人任务"
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
                <el-form-item label="阶段" prop="stageId">
                    <el-select
                            v-model="taskForm.stageId"
                            filterable
                            default-first-option
                            placeholder="请选择阶段">
                        <el-option
                                v-for="item in stageList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
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
            <el-button type="primary" @click="saveTaskInfo('taskForm')">立即创建</el-button>
            <el-button @click="createTaskVisible = false">取 消</el-button>
          </span>
        </el-dialog>
        <el-dialog  title="积分求助转移"  size="tiny" custom-class="myDialog"  :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="editHelpVisible"  :show-close="false">
            <el-form :model="helpForm" ref="helpForm" :rules="helpRules" label-width="80px">
                <el-form-item label="任务详情" prop="description">
                    <el-input type="textarea" v-model="helpForm.description" :rows="3"></el-input>
                </el-form-item>
                <el-form-item label="求助目标" prop="userId">
                    <el-select v-model="helpForm.userId" placeholder="成员列表" >
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
                <el-button type="primary" @click="saveHelpInfo('helpForm')" v-show="!editHelpDetailVisible">立即创建</el-button>
                <el-button type="primary" @click="saveHelpInfo('helpForm')" v-show="editHelpDetailVisible">立即更新</el-button>
                <el-button @click="editHelpVisible = false,clearHelpForm()">取 消</el-button>
              </span>
        </el-dialog>
        <el-dialog title="积分求助转移详情" custom-class="myDialog" :visible.sync="helpDetailVisible" :close-on-click-modal="false" :close-on-press-escape="false" top="10%" size="tiny">
            <el-form>
                <el-form-item class="task-form" label="任务描述：">{{helpDetail.description}}</el-form-item>
                <el-form-item class="task-form" label="发起者：">{{helpDetail.name}}</el-form-item>
                <el-form-item class="task-form" label="求助目标：">{{helpDetail.username}}</el-form-item>
                <el-form-item class="task-form" label="时间：">{{helpDetail.time | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="积分：">{{helpDetail.integral}}分</el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer" >
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

        <el-dialog  title="请假申请"  size="tiny"  custom-class="myDialog"  :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="editLeaveVisible"  :show-close="false">
            <el-form :model="leaveForm" ref="leaveForm" :rules="leaveRules" label-width="110px">
                <el-form-item label="请假原因" prop="description">
                    <el-input type="textarea" v-model="leaveForm.description" :rows="3"></el-input>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span><el-form-item label="请假开始日期" prop="beginTime">
                    <el-date-picker
                            v-model="leaveForm.beginTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:00:00"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span><el-form-item label="请假结束日期" prop="endTime">
                    <el-date-picker
                            v-model="leaveForm.endTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:00:00"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span><el-form-item label="请假类型" prop="type">
                    <el-select
                            v-model="leaveForm.type"
                            placeholder="请选择类型">
                        <el-option v-for="item in leaveType" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <span class="star" style="float: left;margin-top: 7px;margin-right: -8px;margin-left: 8px;">*</span><el-form-item label="请假时长" prop="hours">
                    <el-input type="input" v-model="leaveForm.hours" style="width:100px"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button type="primary" @click="saveLeaveInfo('leaveForm')" v-show="!editLeaveDetailVisible">立即创建</el-button>
                <el-button type="primary" @click="saveLeaveInfo('leaveForm')" v-show="editLeaveDetailVisible">立即更新</el-button>
                <el-button @click="editLeaveVisible = false,editLeaveDetailVisible = false,clearLeaveForm()">取 消</el-button>
              </span>
        </el-dialog>
        <el-dialog title="请假申请详情" :visible.sync="leaveDetailVisible" custom-class="myDialog" :close-on-click-modal="false" :close-on-press-escape="false" top="10%" size="tiny">
            <el-form>
                <el-form-item class="task-form" label="请假原因：">{{leaveForm.description}}</el-form-item>
                <el-form-item class="task-form" label="请假开始时间：">{{leaveForm.beginTime | formatTime}}</el-form-item>
                <el-form-item class="task-form" label="请假结束时间：">{{leaveForm.endTime | formatTime}}</el-form-item>
                <el-form-item class="task-form" label="类型：">{{leaveForm.typeName }}</el-form-item>
                <el-form-item class="task-form" label="请假时长：">{{leaveForm.hours}}小时</el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer" >
                <div v-show="userRole==0&&activeLeaveName=='wait'">
                    <el-tooltip content="删除该任务" placement="top">
                          <el-button type="danger" icon="delete" @click="deleteLeave(leaveForm.id)"></el-button>
                    </el-tooltip>
                    <el-tooltip content="编辑该任务" placement="top">
                     <el-button type="primary" icon="edit" @click="editLeaveDetail(leaveForm,1)"></el-button>
                    </el-tooltip>
                    <el-button type="success" @click="acceptLeave(leaveForm.id)">审核通过</el-button>
                </div>
            </span>
        </el-dialog>




    </div>
</template>
<script>
    import TaskItem from './TaskItem'
    import Integral from './Intergral.vue'

    import http from '../lib/Http'
    import helper from '../lib/Helper'
    import moment from 'moment';

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
                activeName: 'doing',
                assessActiveName: 'waitAssess',
                activeHelpName: 'wait',
                activeLeaveName:'wait',
                auditTabsActiveName: 'wait',
                auditHelpTabsActiveName:'wait',
                editHelpDetailVisible:false,
                editHelpVisible: false,
                editLeaveVisible:false,
                leaveDetailVisible:false,
                helpDetailVisible:false,
                createTaskVisible: false,
                editLeaveDetailVisible:false,
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
                auditSuccessPage: {
                    pageNum: 1,
                    pageSize: 5,
                    total: 0,
                },
                waitPage: {
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
                    beginTime:'',
                    priority: 1,
                    tags: [],
                    taskType: 1,
                    taskHours: '',
                    stageId: ''
                },
                helpForm: {
                    id :'',
                    description: '',
                    time: '',
                    userId: '',
                    integral: '',
                    name: '',
                    username: ''
                },
                leaveForm:{
                    id:'',
                    description:'',
                    beginTime:'',
                    endTime:'',
                    type:'',
                    hours:'',
                    typeName:''
                },
                leaveType:[
                    {id:1,name:'事假'},
                    {id:2,name:'病假'},
                    {id:3,name:'婚假'},
                    {id:4,name:'产假'},
                    {id:5,name:'丧假'},
                    {id:6,name:'产检'},
                    {id:7,name:'年假'},
                    {id:8,name:'调休'},
                ] ,
                userWeeks:[],
                weekTime:{
                    beginWeek:'',
                    endWeek:''
                },
                leaveTimeRange:'',
                helpDetail: {
                    id:'',
                    description: '',
                    time: '',
                    userId: '',
                    integral: '',
                    name: '',
                    username: ''
                },
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
                        { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }
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
                    integral: [ {required: true, message: '积分不能为空', trigger: 'blur'}],
                    description: [{required: true, message: '详情不能为空且不超过100字', trigger: 'blur', min: 1, max: 100}],
                    userId: [ {required: true, message: '求助人不能为空', trigger: 'change'}],
                    time: [ {type: 'date', required: true, message: '转移时间不能为空', trigger: 'change'}]
                },
                leaveRules:{
                    description: [{required: true, message: '请假原因不能为空', trigger: 'change', min: 1}],
                },
                task: {
                    doing: [],
                    finished: [],
                    waitAssess: [],
                    commented:[],
                    waitAudit: [],
                    auditSuccess: [],
                    applyFail: []
                },
                review:{
                    wait: [],
                    review: []
                },
                leaveList:{
                    wait:[],
                    pass:[]
                },
                projectList: [],
                stageList: [],
                tagList: [],
                userList:[],
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
                    },
                    {
                        label: '季度积分排名',
                        score: ''
                    },
                    {
                        label: '年度积分排名',
                        score: ''
                    }
                ]
            };
        },
        created() {
            this.reload();
        },
        //数据初始化
        beforeMount(){
            let _this = this;
            //选中组织tab
            _this.$root.eventBus.$emit("handleTabSelected", "navIndex");
        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole <= 1;
            },
            userRole(){
                let userRole = helper.decodeToken().userRole;
                return userRole;
            },
            finishedPageLayout() {
                if (this.finishedPage.total>0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            commentedPageLayout() {
                if (this.commentedPage.total>0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            auditSuccessPageLayout() {
                if (this.auditSuccessPage.total>0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            waitPageLayout() {
                if (this.waitPage.total>5) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            leaveWaitPageLayout() {
                if (this.leaveWaitPage.total>5) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            reviewPageLayout() {
                if (this.reviewPage.total>5) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            leavePassPageLayout() {
                if (this.leavePassPage.total>5) {
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
            formatTime: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY年MM月DD日 HH:00:00');
            },
        },
        methods: {
            handleClick(tab, event) {
                // 点击进行中和已完成
                console.log(tab, event);
            },
            createTaskClick() {
                // 建个人任务
                this.createTaskVisible = true
            },
            reload() {
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
                this.fetchUserLeaveList()
                this.fetchUserLeavePassList()
                //this.fetchApplyFailTask();
                if (this.userRole === 0) {
                    // 所有审核通过的数据
                    this.fetchTaskAuditSuccess()
                    //待审核的积分转移，审核通过的积分转移
                    this.fetchHelpWaitAdmin()
                    this.fetchHelpReviewAdmin()
                }else{
                    this.fetchMyHelpWaitList();
                    this.fetchMyReviewSuccess();
                }
            },
            saveTaskInfo(formName) {
                let vm = this;
                this.taskForm.endTime = moment(this.taskForm.endTime).toDate()
                this.taskForm.beiginTime = moment(this.taskForm.beiginTime).toDate()
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        let userId = helper.decodeToken().userId;
                        var param = this.taskForm;
                        param.taskName = param.taskName.trim();
                        param.beiginTime = moment(param.beiginTime).format('YYYY-MM-DD HH:00:00')
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD HH:00:00')
                        if(param.taskHours.length!=parseFloat(param.taskHours).toString().length||parseFloat(param.taskHours)=="NaN"){
                            this.$message({ showClose: true,message: '工作量只能为数字或者小数',type: 'error'});
                            return false;
                        }
                        if(param.taskHours.trim()>1000||param.taskHours.trim()<1){
                            this.$message({ showClose: true,message: '工作量请保持在1至1000范围',type: 'error'});
                            return false;
                        }
                        if(moment(param.endTime).millisecond()<moment(param.beginTime).millisecond()||moment(param.endTime).week()!=moment(param.beginTime).week()){
                            this.$message({ showClose: true,message: '请检查日期，个人任务请勿跨周进行',type: 'warning'});
                            return false;
                        }
                        var taskUsers = [{
                            userId: userId,
                            taskHours: param.taskHours.trim(),
                            beginTime: moment().format('YYYY-MM-DD HH:mm:ss'),
                            endTime:  moment(param.endTime).format('YYYY-MM-DD 23:59:59'),
                            description: param.description.trim()
                        }];
                        param['taskUsers'] = taskUsers;
                        http.zsyPostHttp('/task/create', param, (resp) => {
                            this.$message({ showClose: true,message: '任务创建成功',type: 'success'});
                            this.$refs[formName].resetFields();
                            this.createTaskVisible = false
                            vm.reload()
                        });
                    } else {
                        return false;
                    }
                });
            },
            makeUpItems(items) {
                const list = items;
                list.forEach((el) => {
                    let endTime = '',today = moment().format('YYYY-MM-DD')
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
                    items.push({label: ' ', score: data.week,time:this.getDateString('week')});
                    items.push({label: '', score: data.month,time:this.getDateString('month')});
                    items.push({label: '', score: data.year,time:this.getDateString('year')});
                    items.push({label: '', score: data.quarterRank,time:this.getDateString('quarter')});
                    items.push({label: '', score: data.yearRank,time:this.getDateString('year')});
                    this.integralItem = items;
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
                http.zsyGetHttp(`/task/commented`, {}, (resp) => {
                    vm.task.commented = this.makeUpItems(resp.data)
                })
            },
            // 获取所有待审核的任务
            fetchTaskWaitAudit() {
                let vm = this
                http.zsyGetHttp('/task/pending/all', {}, (resp) => {
                    vm.task.waitAudit = this.makeUpItems(resp.data)
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
            // 获取我的待审核任务
            fetchMyTaskWaitAudit() {
                let vm = this
                http.zsyGetHttp('/task/pending', {}, (resp) => {
                    resp.data.forEach((task)=>{
                        task.name +='(待审核)'
                    })
                    vm.task.doing = vm.task.doing.concat(this.makeUpItems(resp.data))
                })
            },
            //获取个人请假信息
            fetchUserLeaveList(){
                let vm = this
                http.zsyGetHttp('/userLeave/1/'+this.leaveWaitPage.pageNum, {}, (resp) => {
                    vm.leaveList.wait = resp.data.list
                    vm.leaveWaitPage.total = resp.data.total
                    vm.leaveWaitPage.pageNum = resp.data.pageNum
                })
            },
            //获取请假通过信息
            fetchUserLeavePassList(){
                let vm = this
                http.zsyGetHttp('/userLeave/3/'+this.leavePassPage.pageNum, {}, (resp) => {
                    vm.leaveList.pass = resp.data.list
                    vm.leavePassPage.total = resp.data.total
                    vm.leavePassPage.pageNum = resp.data.pageNum
                })
            },
            //保存用户转移积分
            saveHelpInfo(helpForm){
                var param = this.helpForm;
                var help = {
                    userId: param.userId,
                    time:  moment(param.time).format('YYYY-MM-DD HH:mm:ss'),
                    description: param.description.trim(),
                    integral: param.integral
                };
                this.$refs[helpForm].validate((valid) => {
                    if (valid) {
                        if (!this.isDecimal(param.integral)) {
                            this.$message({ showClose: true,message: '积分格式错误',type: 'error'});
                            return false;
                        }
                        if (helper.decodeToken().userId==this.helpForm.userId) {
                            this.$message({ showClose: true,message: '求助目标不能是自己，请重试',type: 'error'});
                            return false;
                        }
                        if(this.helpForm.id!=''){
                            http.zsyPostHttp('/integral/editHelpDetail/'+this.helpForm.id, help, (res) => {
                                this.$message({ showClose: true,message: '转移积分更新成功，请等待审核',type: 'success'});
                                this.editHelpVisible = false;
                                this.clearHelpForm();
                            });
                        }else{
                            http.zsyPostHttp('/integral/add', help, (res) => {
                                this.$message({ showClose: true,message: '转移积分添加成功，请等待审核',type: 'success'});
                                this.editHelpVisible = false;
                                this.clearHelpForm();
                            });
                        }
                        this.reload();
                    } else {
                        return false;
                    }
                });
            },
            //获取我的待审核的积分转移
            fetchMyHelpWaitList(){
                http.zsyGetHttp('/integral/getMyWaitList/'+this.waitPage.pageNum, {}, (resp) => {
                    this.review.wait = resp.data.list;
                    this.waitPage.total = resp.data.total;
                })
            },
            //所有待审核的积分转移
            fetchHelpWaitAdmin(){
                http.zsyGetHttp('/integral/getHelpWaitList/'+this.waitPage.pageNum, {}, (resp) => {
                    this.review.wait = resp.data.list;
                    this.waitPage.total = resp.data.total;
                })
            },
            fetchHelpReviewAdmin(){
                http.zsyGetHttp('/integral/getReviewList/'+this.reviewPage.pageNum, {}, (resp) => {
                    this.review.review = resp.data.list;
                    this.reviewPage.total = resp.data.total;
                })
            },
            // 获取所有我的审核通过的任务
            fetchMyReviewSuccess() {
                http.zsyGetHttp(`/integral/getMyReviewList/`+this.reviewPage.pageNum, {}, (resp) => {
                    this.review.review = resp.data.list;
                    this.reviewPage.total = resp.data.total;
                })
            },
            //删除积分求助
            deleteHelp(id){
                this.$confirm('此操作将删除该任务, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyDeleteHttp(`/integral/delete/`+id, {}, (resp) => {
                        this.$message({ showClose: true,message: '删除成功',type: 'success'});
                        this.reload();
                        this.helpDetailVisible = false;
                    })
                }).catch(() => {
                });
            },
            //审核通过积分求助
            acceptHelpChange(id){
                this.$confirm('确认通过?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyGetHttp(`/integral/passReview/`+id, {}, (resp) => {
                        this.$message({ showClose: true,message: '审核成功',type: 'success'});
                        this.reload();
                        this.helpDetailVisible = false;
                    })
                }).catch(() => {
                });
            },
            //积分转移详情
            reviewDetail(help){
                this.helpDetailVisible=true ;
                this.helpDetail.time = moment(help.time).toDate();
                this.helpDetail.integral = help.integral;
                this.helpDetail.description = help.description;
                this.helpDetail.name = help.name.split(",")[1];
                this.helpDetail.username = help.name.split(",")[0];
                this.helpDetail.id = help.id;
                this.helpDetail.userId = help.userId;
            },
            editHelpDetail(help){
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
            leaveDetail(leave){
                this.leaveDetailVisible =true
                this.leaveForm.id = leave.id
                this.leaveForm.description = leave.description
                this.leaveForm.beginTime = moment(leave.beginTime).toDate();
                this.leaveForm.endTime = moment(leave.endTime).toDate();
                this.leaveForm.typeName = leave.typeName
                this.leaveForm.hours = leave.hours
                this.userWeeks = leave.userWeeks
            },
            editLeaveDetail(leave,index){
                if(this.userRole == 0&&index!=1){
                    this.leaveDetail(leave)
                }else{
                    this.editLeaveDetailVisible = true ;
                    this.leaveDetailVisible = false ;
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
            clearHelpForm(){
                this.helpForm.integral = '';
                this.helpForm.time = '';
                this.helpForm.description='';
                this.helpForm.userId='';
                this.helpForm.id='';
                this.helpForm.username='';
                this.helpForm.name='';
                this.editHelpDetailVisible = false;
            },
            clearLeaveForm(){
                this.leaveForm.id = this.leaveForm.endTime = this.leaveForm.beginTime = this.leaveForm.hours = this.leaveForm.type = this.leaveForm.description  = ''
                this.userWeeks = []
            },
            //待审核积分转移页
            handleWaitPage(currentPage){
                this.waitPage.pageNum = currentPage;
                if(this.userRole>0){
                    this.fetchMyHelpWaitList();
                }else{
                    this.fetchHelpWaitAdmin();
                }
            },
            handleLeaveWaitPage(currentPage){
                this.leaveWaitPage.pageNum = currentPage;
                if(this.userRole>0){
                    this.fetchUserLeaveList();
                }else{
                    this.fetchUserLeaveList();
                }
            },
            handleLeavePassPage(currentPage){
                this.leavePassPage.pageNum = currentPage;
                if(this.userRole>0){
                    this.fetchUserLeavePassList();
                }else{
                    this.fetchUserLeavePassList();
                }
            },
            saveLeaveInfo(formName){
                this.$refs[formName].validate((valid) =>{
                    if (valid) {
                        if(this.leaveForm.type==''||this.leaveForm.type==null){
                            this.$message({ showClose: true,message: '请选择请假类型',type: 'warning'});
                            return false;
                        }
                        var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.leaveForm.hours);
                        if(!ishours){
                            this.$message({ showClose: true,message: '请假时长填写错误',type: 'error'});
                            return false;
                        }
                        if(this.leaveForm.hours>99999.9||this.leaveForm.hours<0){
                            this.$message({ showClose: true,message: '请假时长正确值应为0~99999.9',type: 'error'});
                            return false;
                        }
                        this.weekTime.beginWeek = moment(this.leaveForm.beginTime).week()
                        this.weekTime.endWeek = moment(this.leaveForm.endTime).week()
                        this.leaveForm.beginTime =  moment(this.leaveForm.beginTime).toDate()
                        this.leaveForm.endTime =  moment(this.leaveForm.endTime).toDate()

                        let form = this.leaveForm
                        form.beginTime = moment(form.beginTime).format('YYYY-MM-DD HH:00:00')
                        form.endTime = moment(form.endTime).format('YYYY-MM-DD HH:00:00')
                        if(this.weekTime.beginWeek!=this.weekTime.endWeek||moment(form.beginTime).isAfter(moment(form.endTime))|| moment(form.beginTime).isSame(moment(form.endTime))){
                            this.$message({ showClose: true,message: '请假日期有误,请检查(请假时间不能跨周、相同)',type: 'warning'});
                            return false;
                        }
                        form['userWeeks'] = this.userWeeks
                        if(form.id!=''){
                            http.zsyPostHttp('/userLeave/editLeaveDetail/'+form.id, form, (resp) => {
                                this.$message({
                                    showClose: true,
                                    message: '新建请假申请成功',
                                    type: 'success'
                                });
                                this.fetchUserLeaveList();
                                this.clearLeaveForm();
                                this.editLeaveVisible = false
                                this.editLeaveDetailVisible = false
                            })
                        }else{
                            http.zsyPostHttp('/userLeave/add', form, (resp) => {
                                this.$message({
                                    showClose: true,
                                    message: '请假申请修改成功',
                                    type: 'success'
                                });
                                this.fetchUserLeaveList();
                                this.clearLeaveForm()
                                this.editLeaveVisible = false
                                this.editLeaveDetailVisible = false
                            })
                        }
                    }

                })
            },
            deleteLeave(id){
                http.zsyDeleteHttp('/userLeave/'+id, null, (resp) => {
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
            acceptLeave(id){
                http.zsyGetHttp('/userLeave/passLeave/'+id, null, (resp) => {
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
            handleReviewPage(currentPage){
                this.reviewPage.pageNum = currentPage;
                if(this.userRole>0){
                    this.fetchMyReviewSuccess();
                }else{
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
            handleFinishedPage(currentPage){
                this.finishedPage.pageNum = currentPage
                this.fetchTaskFinished()
            },
            handleCommentedPage(currentPage){
                this.commentedPage.pageNum = currentPage
                this.fetchTaskCommented()
            },
            handleAuditSuccessPage(currentPage){
                this.auditSuccessPage.pageNum = currentPage
                this.fetchTaskAuditSuccess()
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
            getDateString(date){//时间期限
                let now = new Date();
                let curMonth = now.getMonth();
                let curYear =  now.getFullYear();;
                let startMonth = 0 ;
                if(date=="month"){//本月的开始结束时间
                    return  moment(new Date(curYear, curMonth, 1)).format('YYYY-MM-DD')+"--"+moment(new Date(curYear,curMonth+1,1)-1).format('YYYY-MM-DD');
                }else if(date=="week"){//本季度的开始结束时间
                    return  moment(new Date(curYear, curMonth, now.getDate() - now.getDay()+1)).format('YYYY-MM-DD')+"--"+moment(new Date(curYear, curMonth, now.getDate()+( 6 - now.getDay())+1)).format('YYYY-MM-DD');
                }else if(date=="year"){//本年的开始结束时间
                    return  moment(new Date(now.getFullYear(),0,1)).format('YYYY-MM-DD')+"--"+moment(new Date(now.getFullYear()+1,0,1)-1).format('YYYY-MM-DD');
                }else if(date=="quarter"){
                    if (curMonth >= 0 && curMonth <= 2){
                        startMonth = 0;
                    }else if (curMonth >= 3 && curMonth <= 5){
                        startMonth = 3;
                    }else if (curMonth >= 6 && curMonth <= 8){
                        startMonth = 6;
                    }else if (curMonth >= 9 && curMonth <= 11){
                        startMonth = 9;
                    }
                    return  moment(new Date(curYear, startMonth, 1)).format('YYYY-MM-DD')+"--"+moment(new Date(curYear, startMonth+3,1)-1).format('YYYY-MM-DD');
                }
            }
        },
        components: {
            TaskItem: TaskItem
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
        margin: 18px 12px;
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
    .mic-item-title:first-child{
         margin-top: 10px;
    }
    .icon-score{
        margin-right: 10px;
        height:  60px;
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
    .mic-item:nth-child(2) .mic-item-integral{
        background-color: #f4b548;
    }
    .mic-item:nth-child(3) .mic-item-integral{
        background-color: #ab93ed;
    }
    .mic-item:nth-child(4) .mic-item-integral{
        background-color: #609beb;
    }
    .mic-item:nth-child(5) .mic-item-integral{
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
        right: 280px;
        font-size: 16px;
        cursor: pointer;
        color: #36A8FF;
        z-index: 30;
    }

    .help{
        right: 135px;
    }

    .leave{
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
</style>

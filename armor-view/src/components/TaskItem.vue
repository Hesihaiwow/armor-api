<template>
    <div>
        <div class="task-lis" v-for="task in taskItems" @click="taskItemClick(task.id,task.type)" v-show="viewType==1"
             :class="task.borderClass">
            <div class="trans" v-show="task.examine==1"></div>
            <div class="head-img">
                <!-- 待审核 -->
                <img v-if="task.reviewStatus ==1" src="../assets/img/waitAudit.png">
                <!-- 审核通过 -->
                <img v-else-if="taskStatus=='auditSuccess'" src="../assets/img/auditSuccess.png">
                <!-- 进行中 -->
                <img v-else-if="(taskStatus=='TaskDoing' && task.reviewStatus ==3)||(taskStatus=='taskList' && task.status<3)"
                     src="../assets/img/doing.png">
                <!-- 已完成 -->
                <img v-else-if="(taskStatus=='finished')||task.status==3" src="../assets/img/finished.jpg">
                <!-- 待评价 -->
                <img v-else-if="taskStatus=='WaitAssess'" src="../assets/img/waitAssess.png">
            </div>
            <div class="main-task-detail">
                <div class="task-name">
                    <span v-if="isPrivate">
                        <span v-show="task.reviewStatus == 1 && task.type == 2" style="color: red">(待审核 多人任务)</span>
                        <span v-show="task.reviewStatus == 1 && task.type == 1" style="color: red">(待审核 个人任务)</span>
                        {{task.name}}
                        <span v-for="(item,index) in task.taskUsers" v-if="task.type==2">
                          <span v-if="item.userId == loginUserId && item.description!=''">({{item.description}})</span>
                      </span>
                    </span>
                    <span v-else>{{task.name}}</span>
                </div>
                <div class="task-state">
                    <i class="iconfont icon-person" v-show="task.type==1"></i>
                    <i class="iconfont icon-people" v-show="task.type==2"></i>
                    <span class="task-end purple" v-if="task.status==3&&task.stageName=='已发布'">{{task.endText}}</span>
                    <span class="task-end" :class="task.endColor" v-else="">{{task.endText}}</span>
                    <span v-if="task.status==1&&task.delayNo!=0&&task.delayNo!=null" class="task-end orange">超时人数:{{task.delayNo}}</span>
                    <span class="task-time-opt">
                    <i v-show="taskStatus=='TaskDoing'  && task.reviewStatus ==3" class="el-icon-circle-check"
                       @click="showFinishedPop(task.id,task.taskUsers[0].id,task.type,task.expand)"></i>
                        <i v-show="taskStatus=='TaskDoing' && task.type==1 && task.reviewStatus===1 "
                           @click="modifyPrivateTask(task.id)" class="el-icon-edit"></i>
                        <i v-show="taskStatus=='TaskDoing' && task.type==2 && task.reviewStatus==1 "
                           @click="getTaskTempDetail(task.id,task.ttId)" class="el-icon-edit"></i>
                        <!-- 待评价 -->
                        <i v-show="taskStatus=='WaitAssess'" class="el-icon-star-off"></i>
                        <!-- 待审核 -->
                        <i v-show="taskStatus=='WaitAuditing'" class="el-icon-edit"
                           @click="showAuditPop(task.id,task.taskUsers[0].id)"></i>
                        <!-- 审核通过 -->
                      <i v-show="taskStatus=='auditSuccess' " @click.stop="modifyPrivateTask(task.id)"
                         class="el-icon-edit"></i>
                    </span>
                    <ul class="task-key-tag">
                        <li class="task-key-lis" v-for="tag in task.tags">
                            <span class="circle" :style="{background:tag.colorValue}"></span>
                            <span class="task-key-msg">{{tag.name}}</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="task-mark"  v-show="isPrivate && task.status==1 && !task.expand && taskStatus=='TaskDoing'  && task.reviewStatus == 3 && task.type == 2" style="margin-right: 20px;">
                <el-button @click="applyModifyMyTask(task)">申请修改任务</el-button>
                <!--<el-button @click="applyExpandTime(task)">申请延长时间</el-button>-->
            </div>
            <!--<div class="task-data-show" v-show="isPrivate && task.status==3 && taskStatus!='WaitAssess'">-->
                <!--<span class="task-score">+{{task.userIntegral}}</span>-->
                <!--<span class="task-level first" v-show="task.type==2">{{task.integralGrade}}</span>-->
            <!--</div>-->
            <div class="" v-show="!isPrivate && task.status==1&& taskStatus!='WaitAssess'">
                <span class="mark-stage">{{task.stageName}}</span>
            </div>
            <div class="task-mark" style="position:relative; left:-10px">
                <img v-if="task.projectImage" :src="task.projectImage" style="width: 40px;height: 40px;border-radius: 50%;">
                <img v-else="" src="../assets/img/u431.png" alt="" >
                <!--<img src="../assets/img/u431.png" alt="">-->
                <span  class="mark-msg">{{task.projectName}}</span>
            </div>
            <div class="task-username">
                <img class="task-avatar" v-if="task.avatarUrl && task.avatarUrl!=''" :src="task.avatarUrl" :alt="task.userName">
                <span v-else="">{{task.userName}}</span>
            </div>
        </div>
        <div v-show="taskItems.length==0" class="empty">
            <h2>暂无数据</h2>
        </div>
        <el-dialog
                title="任务信息"
                top="10%"
                :visible.sync="showFinishedTask"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                size="tiny"
                :before-close="hideFinishedPop">
            <el-form>
                <el-form-item class="task-form" label="任务名称：">{{taskDetail.name}}</el-form-item>
                <el-form-item class="task-form" label="任务ID：">{{taskDetail.id}}</el-form-item>
                <el-form-item class="task-form" label="任务描述：">{{taskDetail.description}}</el-form-item>
                <el-form-item class="task-form" label="关联文档：" v-show="taskDetail.type === 2">
                    <a id="text" v-if="taskDetail.doc !== undefined && taskDetail.doc !== null && taskDetail.doc !== ''" style="cursor: pointer;"
                       @click="toFile(taskDetail.doc)">{{taskDetail.doc.substring(0,50)}}...
                    </a>
                </el-form-item>
                <el-form-item class="task-form" label="项目：" style="float: left;width: 150px">{{taskDetail.projectName}}</el-form-item>
                <el-form-item class="task-form" label="阶段：">{{taskDetail.stageName}}</el-form-item>
                <el-form-item class="task-form" label="优先级：" style="float: left;width: 150px">
                    <span v-for="item in priorityList" v-if="item.value == taskDetail.priority">{{item.label}}</span>
                </el-form-item>
                <el-form-item class="task-form" label="截止时间：" style="">{{taskDetail.endTime | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="标签：">
                    <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
                        {{item.name}}
                    </el-tag>
                </el-form-item>
                <div v-for="(item,index) in taskDetail.users">
                    <el-card class="box-card" v-if="item.userId==loginUserId">
                        <div class="text item">
                            工作量：{{item.taskHours}} 工时
                        </div>
                        <div class="text item" v-show="taskDetail.type==2">
                            截止：{{item.endTime | formatDate}}
                        </div>
                        <div class="text item">
                            任务级别：{{item.taskLevelName}}
                        </div>
                        <div class="text item" v-show="taskDetail.type==2">
                            描述：{{item.description}}
                        </div>
                    </el-card>
                </div>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button type="success" @click="finishTask">已完成</el-button>
                <!--<el-button @click="hideFinishedPop">取 消</el-button>-->
          </span>
        </el-dialog>
        <el-dialog
                title="任务信息"
                top="10%"
                :visible.sync="showAuditTask"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                custom-class="myDialog"
                size="tiny"
                :before-close="hideAuditPop">
            <el-form>
                <el-form-item class="task-form" label="任务名称：">{{taskDetail.name}}</el-form-item>
                <el-form-item class="task-form" label="任务描述：">{{taskDetail.description}}</el-form-item>
                <el-form-item class="task-form" label="项目：">{{taskDetail.projectName}}</el-form-item>
                <el-form-item class="task-form" label="截止时间：">{{taskDetail.endTime | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="标签：">
                    <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
                        {{item.name}}
                    </el-tag>
                </el-form-item>
                <div v-for="(item,index) in taskDetail.users">
                    <el-form-item class="task-form" label="工作量：">{{item.taskHours}} 工时</el-form-item>
                    <el-form-item class="task-form" label="负责人：">{{item.userName}}</el-form-item>
                </div>
                <el-form-item class="task-form" label="任务级别: ">
                    <el-select v-model="privateTaskLevel" clearable filterable placeholder="请选择任务级别"  style="width: 150px">
                        <el-option v-for="item in taskLevelList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                 <el-tooltip content="删除该任务" placement="top">
                      <el-button type="danger" icon="delete" @click="deleteTask"></el-button>
                </el-tooltip>
                 <el-tooltip content="编辑该任务" placement="top">
                 <el-button type="primary" icon="edit" @click="modifyPrivateTask(taskDetail.id)"></el-button>
               </el-tooltip>
            <el-button type="success" @click="acceptTask(privateTaskLevel,taskDetail.createBy)">审核通过</el-button>
          </span>
        </el-dialog>
        <!--<el-dialog-->
                <!--title="任务详情"-->
                <!--top="10%"-->
                <!--:visible.sync="showTaskDetail"-->
                <!--:close-on-click-modal="false"-->
                <!--:close-on-press-escape="false"-->
                <!--custom-class="myDialog"-->
                <!--size="tiny"-->
                <!--:before-close="hideTaskDetail">-->
            <!--<el-form>-->
                <!--<el-form-item class="task-form" label="任务名称：">{{taskDetail.name}}</el-form-item>-->
                <!--<el-form-item class="task-form" label="关联文档：">-->
                    <!--<a  v-if="taskDetail.doc !== undefined && taskDetail.doc !== null && taskDetail.doc !== ''" style="cursor: pointer;"-->
                        <!--@click="toFile(taskDetail.doc)">{{taskDetail.doc.substring(0,50)}}...-->
                    <!--</a>-->
                <!--</el-form-item>-->
                <!--<el-form-item class="task-form" style="white-space: pre-wrap" label="任务描述：">{{taskDetail.description}}</el-form-item>-->
                <!--<el-form-item class="task-form" label="项目：">{{taskDetail.projectName}}</el-form-item>-->
                <!--<el-form-item class="task-form" label="阶段：" style="margin-bottom: -36px;">{{taskDetail.stageName}}</el-form-item>-->
                <!--<el-form-item class="task-form" label="优先级：" style="margin-left: 200px;"><span v-for="item in priorityList"-->
                                                                   <!--v-if="item.value == taskDetail.priority">{{item.label}}</span>-->
                <!--</el-form-item>-->
                <!--<el-form-item class="task-form" label="难易度："  style="margin-bottom: -36px;"><span v-for="item in facilityList"-->
                                                                   <!--v-if="item.value == taskDetail.facility">{{item.label}}</span>-->
                <!--</el-form-item>-->
                <!--<el-form-item class="task-form" label="设计完成时间：" style="margin-left: 200px;">{{taskDetail.beginTime | formatDate}}</el-form-item>-->
                <!--<el-form-item class="task-form" label="开发完成时间：" style="margin-bottom: -36px;">{{taskDetail.testTime | formatDate}}</el-form-item>-->
                <!--<el-form-item class="task-form" label="截止时间：" style="margin-left: 200px;">{{taskDetail.endTime | formatDate}}</el-form-item>-->
                <!--<el-form-item class="task-form" label="标签：">-->
                    <!--<el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">-->
                        <!--{{item.name}}-->
                    <!--</el-tag>-->
                <!--</el-form-item>-->
                <!--<div class="ctpc-member-con" v-if="taskDetail.type==2">-->
                    <!--<div class="ctpc-member-list clearfix" :class="taskStepStatus(item, taskDetail.users.length)"-->
                         <!--v-for="(item,index) in taskDetail.users">-->
                        <!--<el-tooltip  placement="top">-->
                            <!--<div slot="content">-->
                                <!--<span>进行中任务:</span>-->
                                <!--<div v-for="(userTask,userIndex) in item.userTask">-->
                                    <!--<div class="fl" style="margin-left: 20px;">{{userIndex+1}}:任务名称:{{userTask.taskName}}</div>-->
                                    <!--<div class="fl" style="margin-left: 20px;">工作量:{{userTask.taskHours}}</div>-->
                                    <!--<div>&nbsp;&nbsp;开始时间:{{userTask.beginTime | formatDate}}</div>-->
                                    <!--<div>&nbsp;&nbsp;结束时间:{{userTask.endTime | formatDate}}</div>-->
                                    <!--&lt;!&ndash;<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 任务描述:{{userTask.description}}</div>&ndash;&gt;-->
                                <!--</div>-->
                                <!--<div v-if="item.userTask&&item.userTask.length==0">无</div>-->
                            <!--</div>-->
                            <!--<span class="fl ctpc-member-head" >{{item.userName}}</span>-->
                        <!--</el-tooltip>-->
                        <!--<span class="fl ctpc-member-job-time">工作量:{{item.taskHours}}工时</span>-->
                        <!--<span class="fl ctpc-member-end-time">截止:{{item.endTime | formatDate}}</span>-->
                        <!--&lt;!&ndash;<span class="fl ctpc-member-assess" v-show="item.status==3 && item.commentGrade">评价：{{item.commentGrade}}</span>&ndash;&gt;-->
                        <!--<span class="fl ctpc-member-assess" v-show="item.status==3 && item.avgScore">评分：{{item.avgScore}}</span>-->
                        <!--<a href="javascript:;" v-show="taskDetail.status>1 && userRole===0 && item.status==3"-->
                           <!--@click="evaluateDetail(item.id,item.jobRole,item.userName)">查看评价</a>-->
                        <!--<el-tooltip placement="top">-->
                            <!--<div slot="content">{{item.description}}<br/>开始时间:{{item.beginTime | formatDate}}</div>-->
                            <!--<span class="fl" style="margin-left: 25px"><i class="el-icon-information"></i></span>-->
                        <!--</el-tooltip>-->
                        <!--<span v-if="item.proTest && !taskDetail.testing" class="fl ctpc-member-end-time" style="margin-left:20px;color: #66ccff">测试中</span>-->
                    <!--</div>-->
                    <!--<div class="bdl-line"></div>-->
                <!--</div>-->
                <!--<div v-else="taskDetail.type==1" v-for="(item,index) in taskDetail.users">-->
                    <!--<el-form-item class="task-form" label="工作量：">{{item.taskHours}} 工时</el-form-item>-->
                    <!--<el-form-item class="task-form" label="负责人：">{{item.userName}}</el-form-item>-->
                <!--</div>-->

            <!--</el-form>-->

            <!--<div class="trends" v-show="taskLog.list.length>0">-->
                <!--<div class="trends-title clearfix">-->
                    <!--<b class="fl">动态</b>-->
                    <!--<a class="fr" href="javascript:;" @click="taskLogMore(taskDetail.id)" v-show="taskLog.hasNextPage">显示较早的动态</a>-->
                <!--</div>-->
                <!--<ul style="height: 100px; overflow: auto">-->
                    <!--<li v-for="(item,index) in taskLog.list" :key="index" class="clearfix">-->
                        <!--<div style="float: left;width: 350px;"> {{item.title}} <div class="task-title-detail" v-show="item.content!==''" ><em></em>{{item.content}}</div></div>-->
                        <!--<span style="float: right;font-size: 13px;padding-right: 10px"> {{item.createTime | formatTime}}</span>-->
                    <!--</li>-->
                <!--</ul>-->
            <!--</div>-->
            <!--<span slot="footer" class="dialog-footer" v-show="permit && (taskDetail.status==1 || taskDetail.status==0)">-->
                <!--<el-tooltip content="添加Bug修复时间" placement="top" v-if="taskDetail.testing">-->
                    <!--<el-button type="primary"  @click="testTask(taskDetail.id,taskDetail.name,taskDetail.proNames)"  style="text-align: right">添加Bug修复时间</el-button>-->
                <!--</el-tooltip>-->
                <!--<el-tooltip content="启用任务" placement="top" >-->
                    <!--<el-button type="primary"  @click="stopTask(taskDetail.id,1)" v-show="userRole===0&&taskDetail.status===0" style="text-align: left">启用任务</el-button>-->
                <!--</el-tooltip>-->
                <!--<el-tooltip content="暂停任务" placement="top" >-->
                    <!--<el-button type="danger"  @click="modifyStopVisible=true" v-show="userRole===0&&taskDetail.status!=0" style="text-align: left">暂停任务</el-button>-->
                <!--</el-tooltip>-->
                <!--&lt;!&ndash;<el-tooltip content="评审任务" placement="top" >-->
                    <!--<el-button type="primary"  @click="examineTask(taskDetail.id,1)" v-show="userRole===0&&taskDetail.examine===0" style="text-align: left">已评审</el-button>-->
                <!--</el-tooltip>&ndash;&gt;-->
                <!--&lt;!&ndash;<el-tooltip content="评审任务" placement="top" >-->
                    <!--<el-button type="danger"  @click="examineTask(taskDetail.id,0)" v-show="userRole===0&&taskDetail.examine===1" style="text-align: left">未评审</el-button>-->
                <!--</el-tooltip>&ndash;&gt;-->
                <!--<el-tooltip content="删除该任务" placement="top">-->
                      <!--<el-button type="danger" icon="delete" @click="deleteTask" v-show="showDelete"></el-button>-->
                <!--</el-tooltip>-->
                 <!--<el-tooltip content="编辑该任务" placement="top">-->
                 <!--<el-button type="primary" icon="edit" @click="showModifyDescription"-->
                            <!--v-show="((taskDetail.createBy==loginUserId&&userRole===1)  || userRole===0 )&& taskDetail.type==2"></el-button>-->
               <!--</el-tooltip>-->
                <!--<el-button type="primary" icon="check" @click="completeTask(taskDetail.examine)"-->
                           <!--v-show="taskDetail.status!=3 && userRole==0 && taskDetail.type==2">完成</el-button>-->
                <!--<el-button type="primary" @click="showTaskDetail = false" v-show="taskDetail.status>1">确定</el-button>-->
          <!--</span>-->
        <!--</el-dialog>-->
        <el-dialog
                title="任务详情"
                top="10%"
                :visible.sync="showTaskDetail"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                custom-class="myDialog"
                size="tiny"
                :before-close="hideTaskDetail">
            <el-form>
                <div>任务名称: {{taskDetail.name}}</div>
                <div style="margin-top: 5px">关联文档:
                    <a  v-if="taskDetail.doc !== undefined && taskDetail.doc !== null && taskDetail.doc !== ''" style="cursor: pointer;"
                        @click="toFile(taskDetail.doc)">{{taskDetail.doc.substring(0,50)}}...
                    </a>
                </div>
                <div style="margin-top: 5px">任务描述: {{taskDetail.description}}</div>
                <div style="margin-top: 5px;float: left">项目: {{taskDetail.projectName}}</div>
                <div style="margin-top: 5px;margin-left: 250px">阶段: {{taskDetail.stageName}}</div>
                <div style="margin-top: 5px;float: left">优先级:
                    <span v-for="item in priorityList"
                          v-if="item.value == taskDetail.priority">{{item.label}}</span>
                </div>
                <div style="margin-top: 5px;margin-left: 250px">难易度：
                    <span v-for="item in facilityList"
                          v-if="item.value == taskDetail.facility">{{item.label}}</span>
                </div>
                <div style="margin-top: 5px;float: left">设计完成时间：{{taskDetail.beginTime | formatDate}}</div>
                <div style="margin-top: 5px;margin-left: 250px">开发完成时间：{{taskDetail.testTime | formatDate}}</div>
                <div style="margin-top: 5px;float: left">截止时间：{{taskDetail.endTime | formatDate}}</div>
                <div style="margin-top: 5px;margin-left: 250px" v-show="taskDetail">我的任务级别: {{taskDetail.myTaskLevelName}}</div>
                <div>标签：
                    <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
                        {{item.name}}
                    </el-tag>
                </div>
                <div class="ctpc-member-con" v-if="taskDetail.type==2">
                    <div class="ctpc-member-list clearfix" :class="taskStepStatus(item, taskDetail.users.length)"
                         v-for="(item,index) in taskDetail.users">
                        <el-tooltip  placement="top">
                            <div slot="content">
                                <span>进行中任务:</span>
                                <div v-for="(userTask,userIndex) in item.userTask">
                                    <div class="fl" style="margin-left: 20px;">{{userIndex+1}}:任务名称:{{userTask.taskName}}</div>
                                    <div class="fl" style="margin-left: 20px;">工作量:{{userTask.taskHours}}</div>
                                    <div>&nbsp;&nbsp;开始时间:{{userTask.beginTime | formatDate}}</div>
                                    <div>&nbsp;&nbsp;结束时间:{{userTask.endTime | formatDate}}</div>
                                    <!--<div>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 任务描述:{{userTask.description}}</div>-->
                                </div>
                                <div v-if="item.userTask&&item.userTask.length==0">无</div>
                            </div>
                            <span class="fl ctpc-member-head" >{{item.userName}}</span>
                        </el-tooltip>
                        <span class="fl ctpc-member-job-time">工作量:{{item.taskHours}}工时</span>
                        <span class="fl ctpc-member-end-time">截止:{{item.endTime | formatDate}}</span>
                        <!--<span class="fl ctpc-member-assess" v-show="item.status==3 && item.commentGrade">评价：{{item.commentGrade}}</span>-->
                        <span class="fl ctpc-member-assess" v-show="item.status==3 && item.avgScore">评分：{{item.avgScore}}</span>
                        <a href="javascript:;" v-show="taskDetail.status>1 && userRole===0 && item.status==3"
                           @click="evaluateDetail(item.id,item.jobRole,item.userName)">查看评价</a>
                        <el-tooltip placement="top">
                            <div slot="content">{{item.description}}<br/>开始时间:{{item.beginTime | formatDate}}</div>
                            <span class="fl" style="margin-left: 25px"><i class="el-icon-information"></i></span>
                        </el-tooltip>
                        <span v-if="item.proTest && !taskDetail.testing" class="fl ctpc-member-end-time" style="margin-left:20px;color: #66ccff">测试中</span>
                    </div>
                    <div class="bdl-line"></div>
                </div>
                <div v-else="taskDetail.type==1" v-for="(item,index) in taskDetail.users">
                    <div style="float: left">工作量：{{item.taskHours}} 工时</div>
                    <div style="margin-left: 100px;float: left">负责人：{{item.userName}}</div>
                    <div style="margin-left: 360px">任务级别：{{item.taskLevelName}}</div>
                </div>

            </el-form>

            <div class="trends" v-show="taskLog.list.length>0">
                <div class="trends-title clearfix">
                    <b class="fl">动态</b>
                    <a class="fr" href="javascript:;" @click="taskLogMore(taskDetail.id)" v-show="taskLog.hasNextPage">显示较早的动态</a>
                </div>
                <ul style="height: 100px; overflow: auto">
                    <li v-for="(item,index) in taskLog.list" :key="index" class="clearfix">
                        <div style="float: left;width: 350px;"> {{item.title}} <div class="task-title-detail" v-show="item.content!==''" ><em></em>{{item.content}}</div></div>
                        <span style="float: right;font-size: 13px;padding-right: 10px"> {{item.createTime | formatTime}}</span>
                    </li>
                </ul>
            </div>
            <span slot="footer" class="dialog-footer" v-show="permit && (taskDetail.status==1 || taskDetail.status==0)">
                <el-tooltip content="添加Bug修复时间" placement="top" v-if="taskDetail.testing">
                    <el-button type="primary"  @click="testTask(taskDetail.id,taskDetail.name,taskDetail.proNames)"  style="text-align: right">添加Bug修复时间</el-button>
                </el-tooltip>
                <el-tooltip content="启用任务" placement="top" >
                    <el-button type="primary"  @click="stopTask(taskDetail.id,1)" v-show="userRole===0&&taskDetail.status===0" style="text-align: left">启用任务</el-button>
                </el-tooltip>
                <el-tooltip content="暂停任务" placement="top" >
                    <el-button type="danger"  @click="modifyStopVisible=true" v-show="userRole===0&&taskDetail.status!=0" style="text-align: left">暂停任务</el-button>
                </el-tooltip>
                <!--<el-tooltip content="评审任务" placement="top" >
                    <el-button type="primary"  @click="examineTask(taskDetail.id,1)" v-show="userRole===0&&taskDetail.examine===0" style="text-align: left">已评审</el-button>
                </el-tooltip>-->
                <!--<el-tooltip content="评审任务" placement="top" >
                    <el-button type="danger"  @click="examineTask(taskDetail.id,0)" v-show="userRole===0&&taskDetail.examine===1" style="text-align: left">未评审</el-button>
                </el-tooltip>-->
                <el-tooltip content="删除该任务" placement="top">
                      <el-button type="danger" icon="delete" @click="deleteTask" v-show="showDelete"></el-button>
                </el-tooltip>
                 <el-tooltip content="编辑该任务" placement="top">
                 <el-button type="primary" icon="edit" @click="showModifyDescription"
                            v-show="((taskDetail.createBy==loginUserId&&userRole===1)  || userRole===0 )&& taskDetail.type==2"></el-button>
               </el-tooltip>
                <el-button type="primary" icon="check" @click="completeTask(taskDetail.examine)"
                           v-show="taskDetail.status!=3 && userRole==0 && taskDetail.type==2">完成</el-button>
                <el-button type="primary" @click="showTaskDetail = false" v-show="taskDetail.status>1">确定</el-button>
          </span>
        </el-dialog>

        <el-dialog title="填写修改备注" top="10%"
                   :visible.sync="modifyDescriptionVisible"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   custom-class="myDialog"
                   @close="closeDialog"
                   size="tiny">
            <el-button type="primary" @click="addRemark('design',taskDetail.id)">新增设计任务</el-button>
            <el-button type="primary" @click="addRemark('product',taskDetail.id)" >新增产品任务</el-button>
            <el-button type="primary" @click="addRemark('develop',taskDetail.id)">新增开发任务</el-button>
            <el-button type="primary" @click="addRemark('test',taskDetail.id)">新增测试任务</el-button>
            <el-form >
                <el-form-item label="其他">
                    <el-input type="textarea" placeholder="请填写本次修改备注" :maxlength="500" v-model="modifyTaskForm.modifyDescription" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="modifyTask(taskDetail.id)">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog title="填写修改原因" top="10%"
                   :visible.sync="modifyStopVisible"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   custom-class="myDialog"
                   size="tiny">
            <el-form >
                <el-form-item label="原因">
                    <el-input type="textarea" placeholder="请填写暂停原因" :maxlength="500" v-model="modifyTaskForm.modifyDescription" auto-complete="off"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="stopTask(taskDetail.id,0)">确 定</el-button>
            </div>
        </el-dialog>
        <el-dialog
                title="评价"
                top="10%"
                :visible.sync="showTaskComment"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                custom-class="myDialog"
                size="tiny"
                :before-close="hideWaitAssess">
            <div class="assess-task">
                <div class="assess-task-list" v-for="(stage,index) in commentStages">
                    <div class="assess-man-detail">
                        <span class="amd-job">{{stage.stageName}}</span>
                        <span class="amd-job-time">工作量：{{stage.taskHours}}小时</span>
                        <span class="amd-during-time">截止：{{stage.completeTime | formatDate}}</span>
                        <span class="amd-name">{{stage.userName}}</span>
                    </div>
                    <div v-if="!stage.myComment">
                        <el-form>
                            <el-form-item class="task-form" label="请评价">
                                <el-radio-group v-model="assessForm.comments[index].grade">
                                    <el-radio class="radio" label="A">A</el-radio>
                                    <el-radio class="radio" label="B">B</el-radio>
                                    <el-radio class="radio" label="C">C</el-radio>
                                </el-radio-group>
                                <el-input type="textarea" v-model="assessForm.comments[index].description"
                                          placeholder="请输入你的评价"></el-input>
                            </el-form-item>
                        </el-form>
                    </div>
                    <div v-else="stage.myComment">
                        <p>我的评价</p>
                        <div>
                            <p>等级：{{stage.myComment.grade}}</p>
                            <p>内容:{{stage.myComment.description}}</p>
                        </div>
                    </div>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                 <el-button @click="hideWaitAssess">取消</el-button>
                <el-button type="primary" @click="taskAssess" v-show="!allComment" :loading="isSaving">完成</el-button>
          </span>
        </el-dialog>
        <el-dialog
                title="评价"
                top="10%"
                :visible.sync="addTaskEvaluationVisible"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                custom-class="myDialog"
                size="tiny"
                :before-close="hideWaitEvaluation">
            <div class="assess-task">
                <div class="assess-task-list" v-for="(stage,index) in evaluationStages">
                    <div class="assess-man-detail">
                        <!--<span class="amd-job">{{stage.stageName}}</span>-->
                        <!--<i class="el-icon-plus" v-show="!isEvaluated" style="cursor: pointer;margin-left: -40px" @click="plus(index,stage.jobRole,stage.userId)"></i>-->
                        <!--<i class="el-icon-minus" v-show="!isEvaluated" style="cursor: pointer" @click="minus(index,stage.jobRole,stage.userId)"></i>-->
                        <el-checkbox v-show="!isEvaluated"  v-model="checkBox[`${index}`]" @change="hasIntersection(index,stage.userId)" style="margin-right: 20px">任务有交集</el-checkbox>
                        <span class="amd-job-time">工作量：{{stage.taskHours}}小时</span>
                        <span class="amd-during-time">截止：{{stage.completeTime | formatDate}}</span>
                        <span class="amd-name">{{stage.userName}}</span>
                        <span>{{stage.jobRoleName}}</span>
                    </div>
                    <div v-if="!isEvaluated">
                        <el-form class="person-evaluate">
                            <div v-if="stage.jobRole == 0 && showEvaluate[`${index}`]">
                                <el-form-item label="沟通" class="person-evaluate el-form-item">
                                    <el-rate
                                        v-model="evaluation[`${index}_0`]"
                                        :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                        :allow-half=true
                                        style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_0`]}}</span>
                                </el-form-item>
                                <el-form-item label="态度" class="person-evaluate el-form-item">
                                    <el-rate
                                            v-model="evaluation[`${index}_1`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_1`]}}</span>
                                </el-form-item>
                            </div>
                            <div v-if="stage.jobRole == 1 && showEvaluate[`${index}`]" >
                                <el-form-item class="person-evaluate el-form-item" label="沟通">
                                    <el-rate
                                            v-model="evaluation[`${index}_0`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_0`]}}</span>
                                </el-form-item>
                                <el-form-item class="person-evaluate el-form-item" label="态度">
                                    <el-rate
                                            v-model="evaluation[`${index}_1`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_1`]}}</span>
                                </el-form-item>
                                <el-form-item class="person-evaluate el-form-item" label="质量">
                                    <el-rate
                                            v-model="evaluation[`${index}_3`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_3`]}}</span>
                                </el-form-item>
                                <el-form-item class="person-evaluate el-form-item" label="效率">
                                    <el-rate
                                            v-model="evaluation[`${index}_2`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_2`]}}</span>
                                </el-form-item>
                            </div>
                            <div v-if="stage.jobRole == 5 && showEvaluate[`${index}`]" >
                                <el-form-item class="person-evaluate el-form-item" label="沟通">
                                    <el-rate
                                            v-model="evaluation[`${index}_0`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_0`]}}</span>
                                </el-form-item>
                                <el-form-item class="person-evaluate el-form-item" label="态度">
                                    <el-rate
                                            v-model="evaluation[`${index}_1`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_1`]}}</span>
                                </el-form-item>
                                <el-form-item class="person-evaluate el-form-item" label="质量">
                                    <el-rate
                                            v-model="evaluation[`${index}_3`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_3`]}}</span>
                                </el-form-item>
                                <el-form-item class="person-evaluate el-form-item" label="效率">
                                    <el-rate
                                            v-model="evaluation[`${index}_2`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_2`]}}</span>
                                </el-form-item>
                            </div>
                            <div v-if="stage.jobRole == 2 && showEvaluate[`${index}`]" >
                                <el-form-item class="person-evaluate el-form-item" label="沟通">
                                    <el-rate
                                            v-model="evaluation[`${index}_0`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_0`]}}</span>
                                </el-form-item>
                                <el-form-item class="person-evaluate el-form-item" label="态度">
                                    <el-rate
                                            v-model="evaluation[`${index}_1`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_1`]}}</span>
                                </el-form-item>
                                <el-form-item class="person-evaluate el-form-item" label="美感">
                                    <el-rate
                                            v-model="evaluation[`${index}_2`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_2`]}}</span>
                                </el-form-item>
                            </div>
                            <div v-if="stage.jobRole == 3 && showEvaluate[`${index}`]">
                                <el-form-item class="person-evaluate el-form-item" label="沟通">
                                    <el-rate
                                            v-model="evaluation[`${index}_0`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_0`]}}</span>
                                </el-form-item>
                                <el-form-item class="person-evaluate el-form-item" label="态度">
                                    <el-rate
                                            v-model="evaluation[`${index}_1`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_1`]}}</span>
                                </el-form-item>
                                <el-form-item class="person-evaluate el-form-item" label="文档">
                                    <el-rate
                                            v-model="evaluation[`${index}_2`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_2`]}}</span>
                                </el-form-item>
                                <el-form-item class="person-evaluate el-form-item" label="效率">
                                    <el-rate
                                            v-model="evaluation[`${index}_3`]"
                                            :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                            :allow-half=true
                                            style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <span>{{evaluation[`${index}_3`]}}</span>
                                </el-form-item>
                            </div>
                        </el-form>
                    </div>
                    <div v-else style="margin-top: 10px">
                        <div v-for="item in stage.evaluationResDTOS[0].evaluationScoreResDTOS">
                            <el-form>
                                <el-form-item  :label="item.evaluationOptionName">
                                    <el-rate v-model="item.score"
                                             :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                             :allow-half=true
                                             disabled
                                             show-text
                                             text-template="{value}"
                                             style="float: left;margin-top: 7px">
                                    </el-rate>
                                    <!--<span>{{item.score}}</span>-->
                                </el-form-item>
                            </el-form>

                        </div>
                    </div>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                 <el-button @click="hideWaitEvaluation">取消</el-button>
                <el-button type="primary" @click="taskEvaluate" v-show="!isEvaluated" :loading="isSaving">完成</el-button>
          </span>
        </el-dialog>
        <el-dialog
                title="编辑多人任务"
                top="10%"
                :visible.sync="showTaskModify"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                size="tiny"
                :show-close="false"
                custom-class="myDialog"
                :before-close="hideTaskModify">
            <span slot="title">
                <span class="my-dialog-title">编辑任务</span>
               <span class="my-dialog-title-tool">
                    <el-button type="text" icon="check" @click="saveTaskInfo">保存</el-button>
                    <el-button type="text" icon="close" @click="hideTaskModify"></el-button>
               </span>
            </span>
            <el-form label-width="80px">
                <el-form-item class="task-form-edit" label="">
                    <span slot="label"><span class="star">*</span>任务名称</span>
                    <el-input v-model="modifyTaskForm.taskName" style="width: 440px"></el-input>
                </el-form-item>
                <el-form-item class="task-form-edit" label="">
                    <span slot="label">关联文档</span>
                    <el-input v-model="modifyTaskForm.doc" style="width: 440px"></el-input>
                </el-form-item>
                <el-form-item class="task-form-edit" label="" style="float: left">
                    <span slot="label"><span class="star">*</span>项目</span>
                    <el-select v-model="modifyTaskForm.projectId" placeholder="请选择" style="width: 180px">
                        <el-option
                                v-for="item in projectList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item class="task-form-edit" label="">
                    <span slot="label"><span class="star">*</span>负责人</span>
                    <el-select v-model="modifyTaskForm.createBy" placeholder="请选择" style="width: 180px">
                        <el-option
                                v-for="item in userList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item class="task-form-edit" label="" style="float: left">
                    <span slot="label"><span class="star">*</span>优先级</span>
                    <el-select v-model="modifyTaskForm.priority" placeholder="请选择" style="width: 180px">
                        <el-option
                                v-for="item in priorityList"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item class="task-form-edit" label="">
                    <span slot="label"><span class="star">*</span>难易度</span>
                    <el-select v-model="modifyTaskForm.facility" placeholder="请选择" style="width: 180px">
                        <el-option
                                v-for="item in facilityList"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item class="task-form-edit" label="" style="float: left">
                    <span slot="label"><span class="star">*</span>设计完成日期</span>
                    <el-date-picker style="width: 180px"
                            v-model="modifyTaskForm.beginTime"
                            type="date"
                            format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item class="task-form-edit" label="">
                    <span slot="label"><span class="star">*</span>开发完成日期</span>
                    <el-date-picker style="width: 180px"
                            v-model="modifyTaskForm.testTime"
                            type="date"
                            format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item class="task-form-edit" label="">
                    <span slot="label"><span class="star">*</span>截止日期</span>
                    <el-date-picker style="width: 180px"
                            v-model="modifyTaskForm.endTime"
                            type="date"
                            format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item class="task-form-edit" label="" style="float: left">
                    <span slot="label"><span class="star">*</span>阶段</span>
                    <el-select v-model="modifyTaskForm.stageId" :multiple-limit="1" placeholder="请选择" style="width: 180px">
                        <el-option v-for="item in stageList" :key="item.id"
                                   :label="item.name" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item class="task-form-edit" label="">
                    <span slot="label"><span class="star">*</span>标签</span>
                    <!--  <div class="fl tag-name clearfix">
                         <el-button class="fl" size="small" v-for="item in tagList" @click="addFormTagId(item.id,2,$event)">{{item.name}}</el-button>
                      </div> -->
                    <el-select style="width: 180px"
                            v-model="modifyTaskForm.tags"
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
                <el-form-item class="task-form-edit" label="">
                    <span slot="label"><span class="star">*</span>任务描述</span>
                    <el-input type="textarea" v-model="modifyTaskForm.description" :rows="3" style="width: 440px;"></el-input>
                </el-form-item>
            </el-form>
            <div class="ctpc-member-con">
                <div class="ctpc-member-list clearfix" :class="[item.status>1?'done':'in',item.cssClass]"
                     v-for="(item,index) in modifyTaskForm.taskUsers">
                    <span class="fl ctpc-member-head">{{item.userName}}</span>
                    <span class="fl ctpc-member-job-time">工作量:{{item.taskHours}}工时</span>
                    <span class="fl ctpc-member-end-time">截止:{{item.endTime | formatDate}}</span>
                    <span style="position: absolute;right: 10px;">
                        <el-button type="text" icon="edit" v-show="userRole == 0"
                                   @click="modifyStep(index,modifyTaskForm.taskUsers)"></el-button>
                    <el-button v-show="userRole == 0" type="text" icon="close" @click="deleteMember(index)"></el-button>
                    </span>
                </div>
                <div class="bdl-line"></div>
            </div>
            <div class="ctpc-add-member-detail" v-if="showAddDetail">
                <el-input type="textarea" placeholder="描述该成员的工作内容..." v-model="otherStep.description"
                          :rows="3"></el-input>
                <div class="add-member-basic">
                    <div class="add-member-basic-list clearfix">
                        <div class="add-member-basic-menu fl"><span class="star">*</span>姓名：</div>
                        <div class="add-member-basic-msg fl">
                            <el-select v-model="step.userId" filterable placeholder="请选择" @change="stepUserChange">
                                <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                        <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>工作量：
                        </div>
                        <div class="add-member-basic-msg fl">
                            <!--<input class="member-time-count" v-model="step.taskHours">工时-->
                            <el-input v-model="otherStep.taskHours" style="width: 70px"></el-input>
                            工时
                        </div>
                    </div>
                    <div class="add-member-basic-list clearfix">
                        <div class="add-member-basic-menu fl"><span class="star">*</span>开始日期：</div>
                        <div class="add-member-basic-msg fl">
                            <el-date-picker v-model="step.beginTime" format="yyyy-MM-dd" type="date"
                                            placeholder="选择日期"></el-date-picker>
                        </div>
                        <div class="add-member-basic-menu add-member-basic-end fl"><span class="star">*</span>截止日期：
                        </div>
                        <div class="add-member-basic-msg fl">
                            <el-date-picker v-model="step.endTime" type="date" format="yyyy-MM-dd"
                                            placeholder="选择日期"></el-date-picker>
                        </div>
                    </div>
                        <div v-for="(item,index) in sortWeekNumber">
                            <div class="add-member-basic-list clearfix">
                                <div class="fl" style="margin-left: 5px"><span class="star">*</span>第{{item.weekNumber}}周工作量({{item.range}})：</div>
                                <input class="member-time-week" v-model="item.hours" :maxlength="6" style="width:80px" :placeholder="item.hoursTemp">&nbsp;&nbsp;&nbsp;&nbsp;已有工作量:
                                <div class="f1" v-show="parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours) > 40" style="color:red;display:inline">
                                    {{parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours)}}</div>
                                <div class="f1" v-show="parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours) <=40" style="display:inline">
                                    {{parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours)}}</div>
                            </div>
                        </div>
                    <div class="add-member-basic-list clearfix">
                        <div class="add-member-basic-menu fl"><span class="star">*</span>状态：</div>
                        <div class="add-member-basic-msg fl">
                            <el-select v-model="otherStep.status" filterable placeholder="请选择">
                                <el-option v-for="item in statusOptions" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                    </div>

                </div>
                <div class="ctpc-btns">
                    <input type="button" class="ctpc-cancel" @click="cancelAddMember" value="取消">
                    <input type="button" class="ctpc-save" @click="saveAddMember" value="确定">
                </div>
            </div>
            <div class="add-member-opt" v-show="!showAddDetail && userRole == 0" @click="addMember">
                <span class="add-member-icon">+</span>
                <span class="add-member-msg">添加成员</span>
            </div>
            <!--   <span slot="footer" class="dialog-footer">
               <el-button @click="hideTaskModify">取 消</el-button>
               <el-button type="primary" @click="saveTaskInfo">保 存</el-button>
             </span>-->

        </el-dialog>

        <el-dialog
                title="评价详情"
                top="10%"
                :visible.sync="showTaskEvaluationDetail"
                size="tiny"
                :before-close="hideTaskEvaluationDetail">
            <div style="border: gray;border-style: solid;border-width: 1px;padding: 10px;">
                <span style="font-size: 20px;margin-bottom: 20px" v-if="hasAvgEvalution">被评价人：{{taskUserName}}</span>
                <h2 style="font-size: 20px;margin-bottom: 20px" v-if="hasAvgEvalution">总体评价：</h2>
                <div  v-for="(item,index) in avgEvaluation" v-if="hasAvgEvalution">
                    <el-form label-position="left" inline class="demo-table-expand">
                        <el-form-item class="task-form" :label="item.option" style="margin-top: -10px">
                            <el-rate v-model="item.score"
                                     :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                     :allow-half=true
                                     disabled
                                     style="float: left;margin-top: 7px">
                            </el-rate>
                            <span>{{item.score}}</span>
                        </el-form-item>
                    </el-form>
                </div>
                <div v-show="!hasAvgEvalution" class="empty">
                    <h2>暂无数据</h2>
                </div>
            </div>
            <div style="border: gray;border-style: solid;border-width: 1px;padding: 10px;margin-top: 10px">
                <h2 style="font-size: 20px;margin-bottom: 20px" v-show="hasAvgEvalution">用户评价：</h2>
                <div v-for="(item,index) in taskEvaluation" v-show="hasAvgEvalution">
                    <el-form label-position="left" inline class="demo-table-expand">
                        <el-form-item class="task-form" label="评价人">
                            <span>{{ item.evaluateUserName }}</span>
                        </el-form-item>
                        <div v-for="evaluation in item.evaluationScoreResDTOS">
                            <el-form-item class="task-form" :label="evaluation.evaluationOptionName" style="margin-top: -10px">
                                <el-rate v-model="evaluation.score"
                                         :colors="['#99A9BF', '#F7BA2A', '#FF9900']"
                                         :allow-half=true
                                         disabled
                                style="float: left;margin-top: 7px">
                                </el-rate>
                                <span>{{evaluation.score}}</span>
                            </el-form-item>
                        </div>

                    </el-form>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="hideTaskEvaluationDetail" type="primary">确 定</el-button>
            </span>
        </el-dialog>
        <el-dialog
                title="评价详情"
                top="10%"
                :visible.sync="showTaskCommentDetail"
                size="tiny"
                :before-close="hideTaskCommentDetail">
            <h2 style="font-size: 20px;margin-bottom: 20px">总体评价：  <span>{{ taskCommentDetail.commentGrade }}</span>
            </h2>
            <div v-for="(item,index) in taskCommentDetail.comments">
                <el-form label-position="left" inline class="demo-table-expand">
                    <el-form-item class="task-form" label="姓名">
                        <span>{{ item.commentUserName }}</span>
                    </el-form-item>
                    <el-form-item class="task-form" label="评价">
                        <span>{{ item.grade }}</span>
                    </el-form-item>
                    <el-form-item class="task-form" label="描述">
                        <span>{{ item.description }}</span>
                    </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
    <el-button @click="hideTaskCommentDetail" type="primary">确 定</el-button>
  </span>
        </el-dialog>

        <el-dialog
                title="编辑个人任务"
                size="tiny"
                custom-class="myDialog"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="showModifyPrivateTask">
            <el-form :model="modifyPrivateTaskForm" label-width="80px">
                <el-form-item class="task-form-edit" label="项目">
                    <el-select v-model="modifyPrivateTaskForm.projectId" placeholder="请选择">
                        <el-option
                                v-for="item in projectList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item class="task-form-edit" label="开始日期">
                    <el-date-picker
                            v-model="modifyPrivateTaskForm.beginTime"
                            type="date"
                            format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item class="task-form-edit" label="截止日期">
                    <el-date-picker
                            v-model="modifyPrivateTaskForm.endTime"
                            type="date"
                            format="yyyy-MM-dd"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item class="task-form-edit" label="工作量">
                    <el-input style="width:100px" v-model="modifyPrivateTaskForm.taskHours" :maxlength="6"></el-input>
                    小时
                </el-form-item>
                <el-form-item class="task-form-edit" label="任务名称">
                    <el-input v-model="modifyPrivateTaskForm.taskName"></el-input>
                </el-form-item>

                <el-form-item class="task-form-edit" label="任务描述">
                    <el-input type="textarea" v-model="modifyPrivateTaskForm.description" :rows="3"></el-input>
                </el-form-item>
                <el-form-item class="task-form-edit" label="阶段">
                    <el-select
                            v-model="modifyPrivateTaskForm.stageId"
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
                <el-form-item class="task-form-edit" label="标签">
                    <el-select
                            v-model="modifyPrivateTaskForm.tags"
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
            <el-button type="primary" @click="saveModifyPrivateTaskForm">保存</el-button>
            <el-button @click="hideModifyPrivateTaskDialog">取 消</el-button>
          </span>
        </el-dialog>

        <el-dialog title="填写Bug修复时间" top="10%"
                   :visible.sync="testingVisible"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   custom-class="myDialog"
                   size="tiny">
            <div class="add-member-basic-list clearfix">
                <div class="add-member-basic-menu fl"><span class="star">*</span>任务名称：
                </div>
                <div class="add-member-basic-msg ">
                    {{testing.name}}
                </div>
            </div>
            <div class="add-member-basic-list clearfix" >
                <div class="add-member-basic-menu  fl"><span class="star">*</span>开发人员：</div>
                <div class="add-member-basic-msg ">
                    {{testing.proName}}
                </div>
            </div>
            <div class="add-member-basic-list clearfix" >
                <div class="add-member-basic-menu  fl" style="width: 110px;"><span class="star">*</span>测试开始日期：</div>
                <div class="add-member-basic-msg ">
                    <el-date-picker v-model="testing.beginTime" format="yyyy-MM-dd" type="date"
                                    placeholder="选择日期" @change="changeTestWeek()"></el-date-picker>
                </div>
            </div>
            <div class="add-member-basic-list clearfix">
                <div class="add-member-basic-menu  fl" style="width: 110px;"><span class="star">*</span>测试截止日期：
                </div>
                <div class="add-member-basic-msg ">
                    <el-date-picker v-model="testing.endTime" type="date" format="yyyy-MM-dd"
                                    placeholder="选择日期" @change="changeTestWeek()"></el-date-picker>
                </div>
            </div>
            <div class="add-member-basic-list clearfix">
                <div class="add-member-basic-menu fl"><span class="star">*</span>时间比例：
                </div>
                <div class="add-member-basic-msg ">
                    <input class="member-time-week" v-model="testing.percent" :maxlength="6" style="width:80px">%
                </div>
            </div>
            <div v-for="item in testWeekNumber">
                <div class="add-member-basic-list add-member-basic-end clearfix">
                    <div class="fl" style="margin-left: 5px"><span class="star">*</span>第{{item.weekNumber}}周工作量({{item.range}})：</div>
                    <input class="member-time-week" v-model="item.hours" :maxlength="6" style="width:80px">%
                </div>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="testingVisible= false">取消</el-button>
                <el-button type="primary" @click="modifyTestTask()">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="申请延长任务时间" top="10%"
                   :visible.sync="expandTimeVisible"
                   :close-on-click-modal="false"
                   :close-on-press-escape="false"
                   custom-class="myDialog"
                   size="tiny">
            <div class="add-member-basic-list clearfix">
                <div class="add-member-basic-menu fl"><span class="star">*</span>任务名称：
                </div>
                <div class="add-member-basic-msg ">
                    {{expandTime.name}}
                </div>
            </div>
            <div class="add-member-basic-list clearfix">
                <div class="add-member-basic-menu  fl"><span class="star">*</span>开始日期：
                </div>
                <div class="add-member-basic-msg ">
                    <el-date-picker v-model="expandTime.beginTime" type="date" :disabled=true format="yyyy-MM-dd"
                                    placeholder="选择日期"></el-date-picker>
                </div>
            </div>
            <div class="add-member-basic-list clearfix">
                <div class="add-member-basic-menu  fl"><span class="star">*</span>截止日期：
                </div>
                <div class="add-member-basic-msg ">
                    <el-date-picker v-model="expandTime.endTime" type="date" format="yyyy-MM-dd"
                                    placeholder="选择日期"></el-date-picker>
                </div>
            </div>
            <div class="add-member-basic-list clearfix">
                <div class="add-member-basic-menu  fl"><span class="star">*</span>延长时间：
                </div>
                <div class="add-member-basic-msg ">
                    <el-input v-model="expandTime.hours" style="width: 70px"></el-input>
                </div>
            </div>
            <div class="add-member-basic-list clearfix">
                <div class="add-member-basic-menu  fl"><span class="star">*</span>原因：
                </div>
                <div class="add-member-basic-msg ">
                    <el-input v-model="expandTime.reason" style="width:80%" type="textarea" :rows="3" ></el-input>
                </div>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="expandTimeVisible= false">取消</el-button>
                <el-button type="primary" @click="addTaskExpand()">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog title="多人任务申请详情" custom-class="myDialog" :visible.sync="taskTempDetailVisible"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="10%" size="tiny"
                    @close="closeMultipleTask">
            <el-form  :model="taskTempDetail"  ref="editTaskTempForm">
                <div v-for="(item,index) in taskTempDetail.taskReviewLogResDTOList">
                    <div>审核阶段: {{item.level}};  审核人: {{item.checkUserName}};  审核时间: {{item.reviewTime | formatTime}}</div>
                    <div>审核意见: {{item.suggest}}</div>
                </div>
                <el-form-item v-show="taskTempDetail.taskReviewLogResDTOList.length > 0"><span>-------------------------------------------------------------------------------------------</span></el-form-item>
                <div style="margin-top: -10px">申请人: {{taskTempDetail.userName}}</div>
                <div style="margin-top: 3px;">任务名称: {{taskTempDetail.taskName}}</div>
                <div style="margin-top: 3px;">关联文档:
                    <a  v-if="taskDetail.doc !== undefined && taskDetail.doc !== null && taskDetail.doc !== ''" style="cursor: pointer;"
                                                       @click="toFile(taskDetail.doc)">{{taskDetail.doc.substring(0,50)}}...
                    </a>
                </div>
                <div style="float: left;margin-top: 3px">项目: {{taskDetail.projectName}}</div>
                <div style="margin-top: 3px;margin-left: 250px">设计完成时间: {{taskDetail.beginTime | formatDate}}</div>
                <div style="float: left;margin-top: 3px">阶段: {{taskDetail.stageName}}</div>
                <div style="margin-top: 3px;margin-left: 250px">开发完成时间: {{taskDetail.testTime | formatDate}}</div>
                <div style="float: left;margin-top: 3px">优先级: <span v-for="item in priorityList" v-if="item.value == taskDetail.priority">{{item.label}}</span></div>
                <div style="margin-top: 3px;margin-left: 250px">任务截止时间: {{taskDetail.endTime | formatDate}}</div>
                <div>标签:
                    <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
                        {{item.name}}
                    </el-tag>
                </div>
                <el-form-item><span>-------------------------------------------------------------------------------------------</span></el-form-item>
                <el-form-item v-show="taskTempDetail.isChecked == 1" label="任务描述: " prop="description">
                    {{taskTempDetail.description}}
                </el-form-item>
                <el-form-item v-show="taskTempDetail.isChecked == 0" label="任务描述: " prop="description" style="margin-top: -10px">
                    <el-input type="textarea"  v-model="description" :rows="3"></el-input>
                </el-form-item>

                <el-form-item v-show="taskTempDetail.isChecked == 1" class="task-form" label="开始时间：" style="float: left;margin-left: -10px" label-width="90px">
                    {{taskTempDetail.beginTime | formatDate}}
                </el-form-item>
                <el-form-item v-show="taskTempDetail.isChecked == 0" class="task-form" label="开始时间：" style="float: left;margin-left: -8px" label-width="90px">
                    <el-date-picker @change="changeTime()"
                                    v-model="taskTempDetail.beginTime"
                                    type="date"
                                    format="yyyy-MM-dd"
                                    placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>

                <el-form-item v-show="taskTempDetail.isChecked == 1" class="task-form" label="截止时间：" label-width="82px" style="float: left;margin-left: 30px">
                    {{taskTempDetail.endTime | formatDate}}
                </el-form-item>
                <el-form-item v-show="taskTempDetail.isChecked == 0" class="task-form" label="截止时间：" style="margin-left: 285px">
                    <el-date-picker @change="changeTime()"
                                    v-model="taskTempDetail.endTime"
                                    type="date"
                                    format="yyyy-MM-dd"
                                    placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>

                <el-form-item v-show="taskTempDetail.isChecked == 1" class="task-form" label="任务时长：" style="margin-left: 405px">
                    {{taskTempDetail.workHours}} 小时
                </el-form-item>
                <el-form-item v-show="taskTempDetail.isChecked == 0" class="task-form" label="任务时长：">
                    <el-input v-model="taskTempDetail.workHours" style="width: 20%"></el-input>
                    小时
                </el-form-item>

                <div v-for="(item,index) in sortWeekTempNumber" v-show="taskTempDetail.reviewStatus == 1">
                    <div class="add-member-basic-list clearfix">
                        <div class="fl" style="margin-left: 5px">第{{item.weekNumber}}周工作量({{item.range}})：</div>
                        <input class="member-time-week" :disabled="taskTempDetail.isChecked == 1" v-model="item.hours" :maxlength="6" style="width:80px" :placeholder="item.hoursTemp">&nbsp;&nbsp;&nbsp;&nbsp;已有工作量:
                        <div class="f1" v-show="parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours) > 40" style="color:red;display:inline">
                            {{parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours)}}</div>
                        <div class="f1" v-show="parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours) <=40" style="display:inline">
                            {{parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours)}}</div>
                    </div>
                </div>
                <div v-for="(item,index) in sortWeekTempNumber" v-show="taskTempDetail.reviewStatus == 2">
                    <div class="add-member-basic-list clearfix">
                        <div class="fl" style="margin-left: 5px">第{{item.weekNumber}}周工作量({{item.range}})：</div>
                        <input class="member-time-week" :disabled="taskTempAble" v-model="item.hours" :maxlength="6" style="width:80px" :placeholder="item.hoursTemp">&nbsp;&nbsp;&nbsp;&nbsp;已有工作量:
                        <div class="f1" v-show="parseFloat(item.weekHours==''?0:item.weekHours) > 40" style="color:red;display:inline">
                            {{parseFloat(item.weekHours==''?0:item.weekHours)}}</div>
                        <div class="f1" v-show="parseFloat(item.weekHours==''?0:item.weekHours) <=40" style="display:inline">
                            {{parseFloat(item.weekHours==''?0:item.weekHours)}}</div>
                    </div>
                </div>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <div>
                    <el-button type="danger" v-show="taskTempDetail.isChecked == 0" @click="deleteMultipleTask(taskTempDetail.id)">删除申请</el-button>
                    <el-button type="primary" v-show="taskTempDetail.isChecked == 0" @click="editMultipleTask('editTaskTempForm')">修改申请</el-button>
                </div>
            </span>
        </el-dialog>
        <el-dialog title="修改任务申请" custom-class="myDialog" :visible.sync="modifyMyTaskVisible"
                   :close-on-click-modal="false" :close-on-press-escape="false" top="10%" size="tiny">
            <el-form  :model="modifyMyTaskForm"  ref="modifyMyTaskForm">

                <div style="margin-top: -10px">申请人: {{taskUser.userName}}</div>
                <div style="margin-top: 3px;">任务名称: {{taskDetail.name}}</div>
                <div style="float: left;margin-top: 3px">项目: {{taskDetail.projectName}}</div>
                <div style="margin-top: 3px;margin-left: 250px">设计完成时间: {{taskDetail.beginTime | formatDate}}</div>
                <div style="float: left;margin-top: 3px">阶段: {{taskDetail.stageName}}</div>
                <div style="margin-top: 3px;margin-left: 250px">开发完成时间: {{taskDetail.testTime | formatDate}}</div>
                <div style="float: left;margin-top: 3px">优先级: <span v-for="item in priorityList" v-if="item.value == taskDetail.priority">{{item.label}}</span></div>
                <div style="margin-top: 3px;margin-left: 250px">任务截止时间: {{taskDetail.endTime | formatDate}}</div>
                <div>标签:
                    <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
                        {{item.name}}
                    </el-tag>
                </div>
                <el-form-item><span>-------------------------------------------------------------------------------------------</span></el-form-item>
                <!--<el-form-item class="task-form" label="申请人：">{{taskUser.userName}}</el-form-item>-->
                <div style="margin-top: -10px">任务描述: {{taskUser.description}}</div>
                <div style="float: left;margin-top: 3px">开始时间: {{taskUser.beginTime | formatDate}}</div>
                <div style="float: left;margin-top: 3px;margin-left: 10px;">截止时间: {{taskUser.endTime | formatDate}}</div>
                <div style="margin-top: 3px;margin-left: 310px">任务时长: {{taskUser.taskHours}}小时</div>
                <div v-for="(item,index) in sortUserWeekNumber">
                    <div class="add-member-basic-list clearfix">
                        <div class="fl" style="margin-left: 5px">第{{item.weekNumber}}周工作量({{item.range}})：</div>
                        <input class="member-time-week" disabled v-model="item.hours" :maxlength="6" style="width:80px" :placeholder="item.hoursTemp">&nbsp;&nbsp;&nbsp;&nbsp;已有工作量:
                        <div class="f1" v-show="parseFloat(item.weekHours==''?0:item.weekHours) > 40" style="color:red;display:inline">
                            {{parseFloat(item.weekHours==''?0:item.weekHours)}}</div>
                        <div class="f1" v-show="parseFloat(item.weekHours==''?0:item.weekHours) <=40" style="display:inline">
                            {{parseFloat(item.weekHours==''?0:item.weekHours)}}</div>
                    </div>
                </div>

                <el-form-item><span>-------------------------------------------------------------------------------------------</span></el-form-item>
                <el-form-item label="修改原因: ">
                    <el-input type="textarea"  v-model="modifyMyTaskReason" :rows="2"></el-input>
                </el-form-item>
                <el-form-item label="任务描述: ">
                    <el-input type="textarea"  v-model="modifyMyTaskDescription" :rows="3"></el-input>
                </el-form-item>
                <el-form-item class="task-form" label="开始时间：" style="float: left;margin-left: -8px" label-width="90px">
                    <el-date-picker @change="changeTime()"
                                    v-model="modifyMyTaskForm.beginTime"
                                    type="date"
                                    format="yyyy-MM-dd"
                                    placeholder="选择日期时间"
                                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item class="task-form" label="截止时间：" style="margin-left: 285px">
                    <el-date-picker @change="changeTime()"
                                    v-model="modifyMyTaskForm.endTime"
                                    type="date"
                                    format="yyyy-MM-dd"
                                    placeholder="选择日期时间"
                                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item class="task-form" label="任务时长：">
                    <el-input v-model="modifyMyTaskForm.workHours"  style="width: 20%"></el-input>
                    小时
                </el-form-item>
                <div v-for="(item,index) in sortTaskModifyWeekNumber">
                    <div class="add-member-basic-list clearfix">
                        <div class="fl" style="margin-left: 5px">第{{item.weekNumber}}周工作量({{item.range}})：</div>
                        <input class="member-time-week" v-model="item.hours" :maxlength="6" style="width:80px" :placeholder="item.hoursTemp">&nbsp;&nbsp;&nbsp;&nbsp;已有工作量:
                        <div class="f1" v-show="parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours)  > 40" style="color:red;display:inline">
                            {{parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours) }}</div>
                        <div class="f1" v-show="parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours)  <=40" style="display:inline">
                            {{parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours) }}</div>
                    </div>
                </div>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <div>
                    <el-button type="primary" @click="saveApplyModifyMyTask('modifyMyTaskForm')">确认修改</el-button>
                </div>
            </span>
        </el-dialog>
    </div>
</template>
<script>
    import http from '../lib/Http'
    import moment from 'moment';
    import helper from '../lib/Helper'
    import _ from 'lodash'
    import ElButton from "../../node_modules/element-ui/packages/button/src/button";
    import ElInput from "../../node_modules/element-ui/packages/input/src/input";

    moment.locale('zh-cn');
    export default {
        components: {
            ElInput,
            ElButton},
        name: 'TaskItem',
        props: {
            taskItems: Array,
            taskStatus: String,
            isPrivate: Boolean,
            projectList: Array,
            stageList: Array,
            tagList: Array,
            userList: Array,
            viewType:'',
        },
        data() {
            var validateEmpty = (rule, value, callback) => {
                if (value.trim() == '') {
                    callback(new Error());
                } else {
                    callback();
                }
            };
            return {
                loginUserId: '',
                isSaving:false,
                modifyDescriptionVisible:false,
                modifyStopVisible:false,
                showFinishedTask: false,
                showAuditTask: false,
                showTaskDetail: false,
                showTaskComment: false,
                addTaskEvaluationVisible: false,
                isEvaluated: false,
                allowHalf: false,
                taskDetailBO: {},
                showTaskModify: false,
                showAddDetail: false,
                showTaskCommentDetail: false,
                showTaskEvaluationDetail: false,
                taskCommentDetail: {},
                avgEvaluation: [],
                taskUserName: '',
                hasAvgEvalution:false,
                taskEvaluation: [],
                value:3.7,
                showModifyPrivateTask: false,
                testingVisible:false,
                expandTimeVisible:false,
                modifyPrivateTaskForm: {
                    id: '',
                    userId: '',
                    taskType: 1,
                    priority: 1,
                    taskName: '',
                    description: '',
                    projectId: '',
                    beginTime:'',
                    endTime: '',
                    tags: [],
                    taskHours: '',
                    stageId: ''
                },
                pickerOptions0: {
                    disabledDate(time) {
                        return time.getTime() < Date.now() - 8.64e7;
                    }
                },
                beforeNow: {
                    disabledDate(time) {
                        return time.getTime() > Date.now() - 8.64e7;
                    }
                },
                finishForm: {
                    taskId: '',
                    taskUserId: '',
                    taskType: '',
                    completeHours: '',
                    completeTime: ''
                },
                auditForm: {},
                assessForm: {
                    taskId: '',
                    comments: [],
                    testerEvaluations:[],
                    developerEvaluations:[],
                    designerEvaluations:[],
                    producterEvaluations:[],
                },
                evaluationForm:{
                    taskId:'',
                    evaluationUserReqDTOS:[],
                    testerEvaluations:[],
                    developerEvaluations:[],
                    designerEvaluations:[],
                    producterEvaluations:[],
                },
                commentStages: [],
                evaluationStages: [],
                evaluation:{},
                showEvaluate:{},
                checkBox:{},
                allComment: false,
                allEvaluation: false,
                productShow: true,
                developShow: true,
                designShow: true,
                testShow: true,
                taskDetail: {},
                privateTaskLevel:'',
                taskLevelList:[
                    {id:1,name:"一级"},
                    {id:2,name:"二级"},
                    {id:3,name:"三级"},
                    {id:4,name:"四级"},
                    {id:5,name:"五级"}
                ],
                taskLog: {
                    list: [],
                    hasNextPage: false,
                    pageNum: 1
                },
                modifyTaskForm: {
                    taskName: '',
                    doc: '',
                    description: '',
                    projectId: '',
                    beginTime:'',
                    testTime:'',
                    endTime: '',
                    priority: '',
                    facility:'',
                    tags: [],
                    taskType: 2,
                    stageId: '',
                    taskUsers: [],
                    modifyDescription:'',
                    createBy:''
                },
                priorityList: [
                    {label: '普通', value: 1},
                    {label: '紧急', value: 2},
                    {label: '非常紧急', value: 3},
                ],
                facilityList: [
                    {label: '容易', value: 1},
                    {label: '简单', value: 2},
                    {label: '一般', value: 3},
                    {label: '较难', value: 4},
                    {label: '困难', value: 5},
                ],
                statusOptions:[
                    {name: '进行中', id: 1},
                    {name: '已完成', id: 2},
                ],
                step: {
                    index: '',
                    stageId: '',
                    stageName: '',
                    userId: '',
                    userName: '',
                    beginTime: '',
                    endTime: '',
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
                testing:{
                    beginTime: null,
                    endTime: null,
                    percent:25,
                    taskId:'',
                    proName:'',
                    name:''
                },
                expandTime:{
                    name:'',
                    beginTime:'',
                    endTime:'',
                    reason:'',
                    hours:0,
                },
                stepTemp: {},
                weekTime:{
                    beginWeek:'',
                    endWeek:''
                },
                testWeekNumber:[],
                testWeekNumberTemp:[],
                weekNumber:[],
                weekNumberTemp:[],/*
                 projectList: [],
                 stageList: [],
                 tagList: [],*/
                beginTime:'',
                endTime:'',
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
                    reviewStatus:null,
                    workHours:null,
                    beginWeek:null,
                    endWeek:null,
                    userWeekTempList:[],
                    taskReviewLogResDTOList:[],
                    isChecked:''
                },
                showTaskDetailVisible:false,
                taskTempDetailVisible:false,
                taskTempWeekNumber:[],
                taskWeekNumberTemp:[],
                taskTempAble : false,
                description:'',
                modifyMyTaskVisible:false,
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
                modifyMyTaskForm:{
                    taskId:'',
                    userId:'',
                    reason:'',
                    description:'',
                    beginTime:'',
                    endTime:'',
                    workHours:'',
                    userWeeks:[]
                },
                taskModifyWeekNumber:[],
                taskModifyWeekNumberTemp:[],
                taskModifyWeekTime:{
                    beginWeek:'',
                    endWeek:''
                },

            };
        },
        beforeMount() {
//            this.fetchProjectList()
//            this.fetchStageList()
//            this.fetchTagList()
//            this.fetchUserList()
            this.loginUserId = helper.decodeToken().userId;
        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole < 2;
            },
            userRole() {
                let userRole = helper.decodeToken().userRole;
                return userRole;
            },
            showDelete() {
                // 不包含完成的阶段才显示删除
                if (this.taskDetail.users) {
                    let done = this.taskDetail.users.filter((user) => {
                        return user.status == 2
                    })
                    return done.length == 0 && this.taskDetail.status != 3;
                }
            },
            stepBeginTimeOptions() {
                let endTime = this.modifyTaskForm.endTime
                return {
                    disabledDate(time) {
                        const fTime = time.getTime()
                        return fTime > endTime;
                    }
                };
            },
            stepEndTimeOptions() {
                let endTime = this.modifyTaskForm.endTime
                let beginTime = this.step.beginTime
                return {
                    disabledDate(time) {
                        const fTime = time.getTime()
                        return fTime < beginTime || fTime > endTime;
                    }
                };
            },
            sortWeekNumber(){
                if (this.weekNumber.length != null) {
                    for (let i = 0; i < this.weekNumber.length; i++) {
                        for (let x = 0; x < this.weekNumberTemp.length; x++) {
                            if (this.weekNumber[i] != null) {
                                if (this.weekNumber[i].weekNumber == this.weekNumberTemp[x].weekNumber) {
                                    this.weekNumber[i].hoursTemp = this.weekNumberTemp[x].hours
                                }
                            }
                        }
                    }
                }
                return _.orderBy(this.weekNumber, 'weekNumber')
            },
            sortTestWeekNumber(){
                if (this.testWeekNumber.length != 0) {
                    for (let i = 0; i < this.testWeekNumber.length; i++) {
                        for (let x = 0; x < this.testWeekNumberTemp.length; x++) {
                            if (this.testWeekNumber[i] != null) {
                                if (this.testWeekNumber[i].weekNumber == this.testWeekNumberTemp[x].weekNumber) {
                                    this.testWeekNumber[i].hoursTemp = this.testWeekNumberTemp[x].hours
                                }
                            }
                        }
                    }
                }
                return _.orderBy(this.weekNumber, 'weekNumber')
            },
            sortWeekTempNumber(){
                if (this.taskTempWeekNumber.length != null) {
                    for (let i = 0; i < this.taskTempWeekNumber.length; i++) {
                        for (let x = 0; x < this.taskWeekNumberTemp.length; x++) {
                            if (this.taskTempWeekNumber[i] != null) {
                                if (this.taskTempWeekNumber[i].weekNumber == this.taskWeekNumberTemp[x].weekNumber) {
                                    this.taskTempWeekNumber[i].hoursTemp = this.taskWeekNumberTemp[x].hours
                                }
                            }
                        }
                    }
                }
                return _.orderBy(this.taskTempWeekNumber, 'weekNumber')
            },
            sortUserWeekNumber(){
                if (this.taskUserWeekNumber.length != null) {
                    for (let i = 0; i < this.taskUserWeekNumber.length; i++) {
                        for (let x = 0; x < this.taskUserTempWeekNumber.length; x++) {
                            if (this.taskUserWeekNumber[i] != null) {
                                if (this.taskUserWeekNumber[i].weekNumber == this.taskUserTempWeekNumber[x].weekNumber) {
                                    this.taskUserWeekNumber[i].hoursTemp = this.taskUserTempWeekNumber[x].hours
                                }
                            }
                        }
                    }
                }
                return _.orderBy(this.taskUserWeekNumber, 'weekNumber')
            },
            sortTaskModifyWeekNumber(){
                if (this.taskModifyWeekNumber.length != null) {
                    for (let i = 0; i < this.taskModifyWeekNumber.length; i++) {
                        for (let x = 0; x < this.taskModifyWeekNumberTemp.length; x++) {
                            if (this.taskModifyWeekNumber[i] != null) {
                                if (this.taskModifyWeekNumber[i].weekNumber == this.taskModifyWeekNumberTemp[x].weekNumber) {
                                    this.taskModifyWeekNumber[i].hoursTemp = this.taskModifyWeekNumberTemp[x].hours
                                }
                            }
                        }
                    }
                }
                return _.orderBy(this.taskModifyWeekNumber, 'weekNumber')
            },
        },
        filters: {
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD');
            },
            formatTime: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD HH:mm:ss');
            }
        },
        methods: {
            fetchProjectList() {
                let vm = this;
                http.zsyGetHttp('/project/list', {}, (resp) => {
                    vm.projectList = resp.data;
                })
            },
            fetchStageList() {
                let vm = this;
                http.zsyGetHttp('/stage/list', {}, (resp) => {
                    vm.stageList = resp.data;
                })
            },
            fetchUserList() {
                let vm = this;
                http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.userList = resp.data;
                })
            },
            fetchTagList() {
                let vm = this;
                http.zsyGetHttp('/tag/list', {}, (resp) => {
                    vm.tagList = resp.data;
                })
            },
            showFinishedPop(taskId, userId, taskType) {
                this.finishForm.taskId = taskId;
                this.finishForm.taskUserId = userId;
                this.finishForm.taskType = taskType;
                this.showFinishedTask = true;
                http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    this.taskDetail = resp.data;
                    // let aStr = this.taskDetail.doc;
                    // // let reg = /(http:\/\/|https:\/\/)((\w|=|\?|\.|\/|&|-)+)/g;
                    // let reg = /(http:\/\/|https:\/\/|HTTP:\/\/|HTTPS:\/\/)(\S+\.)+\S{2,}/;
                    // // let reg = /[hH][tT][tT][pP]([sS]?):\/\/(\S+\.)+\S{2,}/;
                    // aStr = aStr.replace(reg,"<a href='$1$2' target='_blank'>$1$2</a>");
                    // this.taskDetail.doc = aStr;
                    // document.getElementById('text').innerHTML=aStr;
                    // console.log(reg.test(aStr));
                    // console.log(aStr);
                })
            },
            //跳转到任务关联文档URL
            toFile(url){
                if (url !== null && url !== ''){
                    window.open(url,'_blank')
                }
            },
            hideFinishedPop() {
                this.resetFinishForm();
                this.showFinishedTask = false;
            },
            showAuditPop(taskId, userId) {
                this.auditForm.taskId = taskId;
                this.auditForm.taskUserId = userId;
                http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    this.taskDetail = resp.data;
                });
                this.showAuditTask = true;
            },
            hideAuditPop() {
                this.auditForm.taskId = '';
                this.auditForm.taskUserId = '';
                this.showAuditTask = false;
                this.taskDetail = {};
            },
            // 审核通过任务
            acceptTask(privateTaskLevel,userId) {
                if (privateTaskLevel !== undefined && privateTaskLevel !== null && privateTaskLevel !== ''){
                    http.zsyPutHttp(`task/auditing/accept/${this.auditForm.taskId}/${privateTaskLevel}/${userId}`, {}, (resp) => {
                        this.$message({ showClose: true,message: '任务审核成功',type: 'success'});
                        this.$emit('reload');
                        this.auditForm.taskId = '';
                        this.auditForm.taskUserId = '';
                        this.privateTaskLevel = '';
                    })
                    this.showAuditTask = false;
                    this.taskDetail = {};
                } else {
                    this.$message({ showClose: true,message: '请选择任务级别',type: 'error'});
                }

            },
            // 打回任务
            rejectTask() {
                http.zsyPutHttp(`task/auditing/reject/${this.auditForm.taskId}`, {}, (resp) => {
                    this.$message({ showClose: true,message: '任务打回成功',type: 'success'});
                    this.$emit('reload');
                    this.auditForm.taskId = '';
                    this.auditForm.taskUserId = '';
                })
                this.showAuditTask = false;
                this.taskDetail = {};
            },
            // 完成任务
            finishTask() {
                this.$confirm('此操作将完成该任务, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    var param = this.finishForm
                    param.completeTime = moment().format('YYYY-MM-DD HH:mm:ss')
                    http.zsyPutHttp('/task/complete', param, (resp) => {
                        this.resetFinishForm()
                        this.showFinishedTask = false;
                        this.taskDetail = {};
                        this.$message({ showClose: true,message: '操作成功',type: 'success'});
                        this.$emit('reload');
                    })
                }).catch(() => {
                });
            },
            resetFinishForm() {
                this.finishForm.taskId = '';
                this.finishForm.taskUserId = '';
                this.finishForm.taskType = '';
                this.finishForm.completeTime = '';
                this.finishForm.completeHours = '';
            },
            hideTaskDetail() {
                this.showTaskDetail = false;
                this.taskDetail = {};
                this.taskLog.list = [];
                this.taskLog.hasNextPage = false;
                this.taskLog.pageNum = 1;
            },
            taskItemClick(taskId, taskType) {
                if (!this.isPrivate || this.taskStatus == 'finished' || this.taskStatus == 'auditSuccess') {
                    this.showTaskDetail = true;
                    http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                        this.taskDetail = resp.data
                    });
                    this.getTaskLog(taskId)
                }

                // 个人点击完成任务
                if (this.taskStatus == 'TaskDoing' && this.isPrivate) {
                    this.taskItems.forEach((task) => {
                        if (task.id === taskId && task.reviewStatus==3 && !this.expandTimeVisible) {
                            this.showFinishedPop(taskId, task.taskUsers[0].id, taskType)
                        }
                    });
                }
                // 个人任务点击评价
                if(this.taskStatus == 'WaitAssess' && this.isPrivate){
                    this.showWaitAssess(taskId)
                }
                var vm = this;
                // 待审核点击
                if(vm.taskStatus == 'WaitAuditing'){
                    vm.taskItems.forEach((task)=>{
                        if (task.id === taskId) {
                            vm.showAuditPop(task.id,task.taskUsers[0].id)
                        }
                    })
                }
            },
            getTaskLog(taskId) {
                http.zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, (resp) => {
                    this.taskLog.list = resp.data.list;
                    this.taskLog.hasNextPage = resp.data.hasNextPage;
                });
            },
            taskLogMore(taskId) {
                this.taskLog.pageNum += 1;
                http.zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, (resp) => {
                    let logs = resp.data.list;
                    this.taskLog.list = this.taskLog.list.concat(logs);
                    this.taskLog.hasNextPage = resp.data.hasNextPage;
                });
            },
            completeTask(examine) {
//                if(examine==0){
//                    this.errorMsg('任务未评审，请在评审后完成任务')
//                    return
//                }
                this.$confirm('此操作将完成该任务, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyPutHttp(`/task/complete/master/${this.taskDetail.id}`, {}, (resp) => {
                        this.$emit('reload');
                        this.$root.eventBus.$emit('reloadBoard');
                        this.$message({ showClose: true,message: '操作成功',type: 'success'});
                        this.hideTaskDetail();
                    })
                }).catch(() => {
                });
            },
            deleteTask() {
                this.$confirm('此操作将删除该任务, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyDeleteHttp(`/task/delete/${this.taskDetail.id}`, {}, (resp) => {
                        this.$emit('reload');
                        this.$root.eventBus.$emit('reloadBoard');
                        this.$message({ showClose: true,message: '删除成功',type: 'success'});
                        this.hideTaskDetail();
                        this.showAuditTask = false;
                        this.taskDetail = {};
                    })
                }).catch(() => {
                });
            },
            // showWaitAssess(taskId) {
            //     let vm = this
            //     http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
            //         vm.assessForm.taskId = taskId;
            //         let users = [];
            //         // 过滤我的任务
            //         resp.data.users.forEach((user) => {
            //             if (user.userId != vm.loginUserId) {
            //                 users.push(user);
            //             }
            //         });
            //         // 查询我的评价
            //         users.forEach((user) => {
            //             user.comments.forEach((comment) => {
            //                 if (comment.createBy == vm.loginUserId) {
            //                     user.myComment = comment;
            //                     return;
            //                 }
            //             });
            //         });
            //         for (let i = 0; i < users.length; i++) {
            //             if (!users[i].myComment) {
            //                 vm.assessForm.comments.push({
            //                     'taskUserId': users[i].id,
            //                     'description': '',
            //                     'grade': ''
            //                 })
            //             }
            //         }
            //         let myComments = 0;
            //         users.forEach((stage) => {
            //             if (stage.myComment) {
            //                 myComments++
            //             }
            //         })
            //         vm.allComment = (myComments == users.length);
            //         vm.commentStages = users;
            //     });
            //
            //     this.showTaskComment = true;
            // },
            showWaitAssess(taskId) {
                let vm = this
                http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    this.taskDetail = resp.data;
                    vm.evaluationForm.taskId = taskId;
                    let users = [];
                    // 过滤我的任务
                    resp.data.users.forEach((user) => {
                        if (user.userId != vm.loginUserId) {
                            users.push(user);
                        }
                    });
                    for (let i = 0; i < users.length; i++) {
                        var jobRole = users[i].jobRole;
                        users[i].evaluationList = []
                        //测试
                        if (jobRole == 0){
                            var evaluationList = [];
                            var testCommunication = {
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '1'
                            };
                            var testAttitude = {
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '2'
                            }
                            evaluationList.push(testCommunication);
                            evaluationList.push(testAttitude);
                            users[i].evaluationList = evaluationList;
                        }
                        else if (jobRole == 1){
                            //开发
                            var evaluationList = [];
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '1'
                            });
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '2'
                            });
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '3'
                            });
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '4'
                            });
                            users[i].evaluationList = evaluationList;
                            // stage.evaluationList.splice(attitude, 1, score)
                            // this.$set(users[i],users[i].evaluationList,evaluationList)
                        }
                        else if (jobRole == 5){
                            //算法
                            var evaluationList = [];
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '1'
                            });
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '2'
                            });
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '3'
                            });
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '4'
                            });
                            users[i].evaluationList = evaluationList;
                            // stage.evaluationList.splice(attitude, 1, score)
                            // this.$set(users[i],users[i].evaluationList,evaluationList)
                        }
                        else if (jobRole == 2){
                            //设计
                            var evaluationList = [];
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '1'
                            });
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '2'
                            });
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '6'
                            });
                            users[i].evaluationList = evaluationList;
                            // this.$set(users[i],users[i].evaluationList,evaluationList)
                        }
                        else if (jobRole == 3){
                            //产品
                            var evaluationList = [];
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '1'
                            });
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '2'
                            });
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '5'
                            });
                            evaluationList.push({
                                'taskUserId': users[i].userId,
                                'score': 0,
                                'evaluationOption': '3'
                            });
                            users[i].evaluationList = evaluationList;
                            // this.$set(users[i],users[i].evaluationList,evaluationList)
                        }
                    }
                    let myEvaluations = 0;
                    this.evaluation = []
                    for (let i = 0; i < users.length; i++){
                        var user = users[i];
                        if (user.evaluationResDTOS.length > 0) {
                            myEvaluations += 1;
                        }
                        this.$set(this.showEvaluate,`${i}`,true)
                        this.$set(this.checkBox,`${i}`,true)
                        user.showEvaluate = true;
                        user.evaluationList.forEach((item,index)=>{
                            this.$set(this.evaluation,`${i}_${index}`,item.score);

                        })
                    }
                    vm.evaluationStages = users;
                    if (myEvaluations>0) {
                        this.isEvaluated = true
                        vm.evaluationStages = vm.evaluationStages.filter(user=>{return user.evaluationResDTOS.length > 0})
                    }
                    this.addTaskEvaluationVisible = true;
                });

            },
            changeDevAttitude(evaluationList,attitude){
                // console.log(1111)
                // this.$forceUpdate()
                // this.$set(evaluationList, 1, attitude)
                // console.log(evaluationList,attitude)
            },
            hideWaitAssess() {
                this.showTaskComment = false;
                this.commentStages = [];
                this.assessForm = {
                    taskId: '',
                    comments: []
                };
            },
            hideWaitEvaluation() {
                this.addTaskEvaluationVisible = false;
                this.isEvaluated = false;
                this.productShow = true;
                this.testShow = true;
                this.developShow = true;
                this.designShow = true;
            },
            hasIntersection(index,taskUserId){
                var checked = this.checkBox[`${index}`]
                if (checked){
                    this.evaluationStages.forEach(user=>{
                        if (taskUserId === user.userId){
                            user.showEvaluate = true
                        }
                    })
                    this.showEvaluate[`${index}`] = true;
                } else {
                    this.evaluationStages.forEach(user=>{
                        if (taskUserId === user.userId){
                            user.showEvaluate = false
                        }
                    })
                    this.showEvaluate[`${index}`] = false;
                }
            },
            plus(index,jobRole,taskUserId){
                this.evaluationStages.forEach(user=>{
                    if (taskUserId === user.userId){
                        user.showEvaluate = true
                    }
                })
                this.showEvaluate[`${index}`] = true;
            },
            minus(index,jobRole,taskUserId){

                this.evaluationStages.forEach(user=>{
                    if (taskUserId === user.userId){
                        user.showEvaluate = false
                    }
                })
                this.showEvaluate[`${index}`] = false;
            },
            // 评价任务
            taskAssess() {
                this.isSaving=true
                http.zsyPostHttp('/task/comment', this.assessForm, (resp) => {
                    this.$message({ showClose: true,message: '评价成功',type: 'success'});
                    this.showTaskComment = false
                    this.commentStages = []
                    this.assessForm = {
                        taskId: '',
                        comments: []
                    };
                    this.$emit('reload');
                    this.isSaving=false
                })
            },
            taskEvaluate(){
                // this.isSaving=true
                Object.keys(this.evaluation).forEach(key=>{
                    let indexArr = key.split('_')
                    this.evaluationStages[indexArr[0]].evaluationList[indexArr[1]].score = this.evaluation[key]
                })
                var param = {};
                param.evaluationUserReqDTOS = [];
                param.taskId = this.evaluationForm.taskId;
                var flag = true;
                for (var j = 0;j<this.evaluationStages.length;j++){
                    var user = this.evaluationStages[j]
                    if (user.showEvaluate){
                        for (var i = 0;i < user.evaluationList.length;i++){
                            var evaluation = user.evaluationList[i]
                            if (evaluation.score === 0){
                                this.$message({ showClose: true,message: '请评价全部评分项',type: 'error'});
                                this.isSaving=false;
                                flag = false;
                                return false;
                            }
                            param.evaluationUserReqDTOS.push(evaluation)
                        }
                    }
                }
                var isSerious = true;
                param.evaluationUserReqDTOS.forEach(evaluation=>{
                    if (evaluation.score != 5){
                        isSerious = false;
                    }
                })
                if (param.evaluationUserReqDTOS.length >0 && isSerious){
                    this.$message({ showClose: true,message: '请认真评价(不要随意全5星!)',type: 'error'});
                    return false;
                }
                if (flag){
                    http.zsyPostHttp('/evaluation/add', param, (resp) => {
                        this.$message({ showClose: true,message: '评价成功',type: 'success'});
                        this.addTaskEvaluationVisible = false
                        this.commentStages = []
                        this.assessForm = {
                            taskId: '',
                            comments: [],
                            testerEvaluations:[],
                            developerEvaluations:[],
                            designerEvaluations:[],
                            producterEvaluations:[],
                        };
                        this.$emit('reload');
                        this.isSaving=false
                    },(fail)=>{
                        this.$message({
                            showClose: true,
                            message: fail.errMsg,
                            type: 'error'
                        });
                        this.isSaving = false;
                    })
                }

            },
            // 修改单人任务
            modifyPrivateTask(taskId) {
                this.showModifyPrivateTaskDialog(taskId);
            },
            // 保存修改单人任务
            saveModifyPrivateTaskForm() {
                if (this.modifyPrivateTaskForm.projectId == '') {
                    this.warnMsg("请选择项目");
                    return;
                }
                if (this.modifyPrivateTaskForm.endTime == '') {
                    this.warnMsg("请选择结束时间");
                    return;
                }
                if (this.modifyPrivateTaskForm.taskHours == '') {
                    this.warnMsg("请输入工作量");
                    return;
                }
                if (this.modifyPrivateTaskForm.taskName.trim() == '') {
                    this.warnMsg("请填写任务名称");
                    return;
                }
                if (this.modifyPrivateTaskForm.description.trim() == '') {
                    this.warnMsg("请填写任务备注");
                    return;
                }

                if (this.modifyPrivateTaskForm.stageId === '') {
                    this.warnMsg("请选择项目阶段");
                    return;
                }
                if (this.modifyPrivateTaskForm.tags.length == 0) {
                    this.warnMsg("请选择至少一项标签");
                    return;
                }

                var isNum = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.modifyPrivateTaskForm.taskHours);
                if(!isNum){
                    this.$message({ showClose: true,message: '工作量填写错误',type: 'error'});
                    return false;
                }
                if(this.modifyPrivateTaskForm.taskHours>8||this.modifyPrivateTaskForm.taskHours<0.1){
                    this.$message({ showClose: true,message: '工作量正确值应为0.1~8',type: 'error'});
                    return false;
                }
                if(moment(this.modifyPrivateTaskForm.endTime).millisecond()<moment(this.modifyPrivateTaskForm.beginTime).millisecond()||moment(this.modifyPrivateTaskForm.endTime).week()!=moment(this.modifyPrivateTaskForm.beginTime).week()){
                    this.$message({ showClose: true,message: '请检查日期，个人任务请勿跨周进行',type: 'error'});
                    return false;
                }

                this.modifyPrivateTaskForm.taskName = this.modifyPrivateTaskForm.taskName.trim();
                this.modifyPrivateTaskForm.beginTime = moment(this.modifyPrivateTaskForm.beginTime).format('YYYY-MM-DD 23:00:00')
                this.modifyPrivateTaskForm.endTime = moment(this.modifyPrivateTaskForm.endTime).format('YYYY-MM-DD 23:59:59')
                this.modifyPrivateTaskForm.taskUsers = [{
                    userId: this.modifyPrivateTaskForm.userId,
                    taskHours: this.modifyPrivateTaskForm.taskHours,
                    beginTime: moment(this.modifyPrivateTaskForm.beginTime).format('YYYY-MM-DD HH:00:00'),
                    endTime: moment(this.modifyPrivateTaskForm.endTime).format('YYYY-MM-DD 23:59:59'),
                    description: this.modifyPrivateTaskForm.description.trim()
                }]
                let vm = this;
                http.zsyPutHttp(`/task/modify/${this.modifyPrivateTaskForm.id}`, this.modifyPrivateTaskForm, (resp) => {
                    this.$message({showClose: true, message: '任务修改成功',type: 'success'});
                    vm.hideModifyPrivateTaskDialog();
                    vm.$emit('reload');
                })
            },
            // 显示修改单人任务弹出层
            showModifyPrivateTaskDialog(taskId) {
                this.showAuditTask = false;
                this.taskDetail = {};
                http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    this.modifyPrivateTaskForm.id = resp.data.id;
                    this.modifyPrivateTaskForm.taskName = resp.data.name;
                    this.modifyPrivateTaskForm.description = resp.data.description;
                    this.modifyPrivateTaskForm.projectId = resp.data.projectId;
                    this.modifyPrivateTaskForm.endTime = resp.data.endTime;
                    this.modifyPrivateTaskForm.beginTime = resp.data.users[0].beginTime;
                    this.modifyPrivateTaskForm.taskHours = resp.data.users[0].taskHours;
                    this.modifyPrivateTaskForm.userId = resp.data.users[0].userId;
                    this.modifyPrivateTaskForm.stageId = resp.data.stageId;
                    for (let i = 0; i < resp.data.tags.length; i++) {
                        this.modifyPrivateTaskForm.tags.push(resp.data.tags[i].id)
                    }
                });
                this.showModifyPrivateTask = true;

            },// 隐藏修改单人任务弹出层
            hideModifyPrivateTaskDialog() {
                this.showModifyPrivateTask = false;
                this.modifyPrivateTaskForm = {
                    id: '',
                    userId: '',
                    taskType: 1,
                    priority: 1,
                    facility: 1,
                    taskName: '',
                    description: '',
                    projectId: '',
                    endTime: '',
                    tags: [],
                    taskHours: '',
                    stageId: ''
                };
            },

            //快捷备注
            addRemark(type,taskId){
                if (type == 'design'){
                    this.modifyTaskForm.modifyDescription = '新增设计任务'
                } else if (type == 'product'){
                    this.modifyTaskForm.modifyDescription = '新增产品任务'
                } else if (type == 'develop'){
                    this.modifyTaskForm.modifyDescription = '新增开发任务'
                } else if (type == 'test'){
                    this.modifyTaskForm.modifyDescription = '新增测试任务'
                }
                this.modifyTask(taskId)
            },
            //关闭弹窗,清空备注
            closeDialog(){
                this.modifyTaskForm.modifyDescription = ''
            },

            // 修改任务
            modifyTask(taskId) {
                if (this.modifyTaskForm.modifyDescription == null || this.modifyTaskForm.modifyDescription.trim() == ''){
                    this.errorMsg('请填写修改任务备注');
                    return;
                }
                this.hideTaskDetail();
                this.showTaskModify = true
                http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    this.modifyTaskForm.id = resp.data.id;
                    this.modifyTaskForm.taskName = resp.data.name;
                    this.modifyTaskForm.doc = resp.data.doc;
                    this.modifyTaskForm.description = resp.data.description;
                    this.modifyTaskForm.endTime = resp.data.endTime;
                    this.modifyTaskForm.beginTime = resp.data.beginTime;
                    this.modifyTaskForm.testTime = resp.data.testTime;
                    this.modifyTaskForm.projectId = resp.data.projectId;
                    this.modifyTaskForm.stageId = resp.data.stageId;
                    this.modifyTaskForm.priority = resp.data.priority;
                    this.modifyTaskForm.facility = resp.data.facility;
                    this.modifyTaskForm.createBy = resp.data.createBy;
                    for (let i = 0; i < resp.data.tags.length; i++) {
                        this.modifyTaskForm.tags.push(resp.data.tags[i].id)
                    }
                    this.modifyTaskForm.taskUsers = resp.data.users
                });
            },
            // 修改阶段
            modifyStep(index, stages) {
                this.stepTemp = {
                    stageId: stages[index].stageId,
                    stageName: stages[index].stageName,
                    userId: stages[index].userId,
                    userName: stages[index].userName,
                    taskHours: stages[index].taskHours,
                    beginTime: stages[index].beginTime,
                    endTime: stages[index].endTime,
                    description: stages[index].description,
                    completeHours: stages[index].completeHours,
                    completeTime: stages[index].completeTime,
                    status: stages[index].status,
                    userWeeks: stages[index].userWeeks,
                }
                this.step = stages[index];
                this.step.index = index;
                this.otherStep.description = stages[index].description;
                this.otherStep.status = stages[index].status;
                this.otherStep.taskHours = stages[index].taskHours;
                this.modifyTaskForm.taskUsers.forEach((item) => {
                    item.cssClass = ''
                })
                this.modifyTaskForm.taskUsers[index].cssClass = 'stepActive'
                this.showAddDetail = true;
                this.weekNumberTemp = stages[index].userWeeks
            },
            hideTaskModify() {
                this.modifyTaskForm.taskName = '';
                this.modifyTaskForm.doc = '';
                this.modifyTaskForm.description = '';
                this.modifyTaskForm.taskName = '';
                this.modifyTaskForm.endTime = '';
                this.modifyTaskForm.beginTime = '';
                this.modifyTaskForm.testTime = '';
                this.modifyTaskForm.projectId = '';
                this.modifyTaskForm.stageId = '';
                this.modifyTaskForm.createBy = '';
                this.modifyTaskForm.priority = 1;
                this.modifyTaskForm.tags = [];
                this.modifyTaskForm.taskUsers = [];
                this.modifyTaskForm.facility = 1 ;
                this.modifyTaskForm.modifyDescription = '';
                this.showTaskModify = false;
                this.step = {
                    index: '',
                    stageId: '',
                    stageName: '',
                    userId: '',
                    userName: '',
                    taskHours: '',
                    beginTime: '',
                    endTime: '',
                    description: '',
                    completeHours: '',
                    completeTime: '',
                    status: ''
                }
                this.hideModifyDescription()
            },
            deleteMember(index) {
                this.modifyTaskForm.taskUsers.splice(index, 1);
                if (this.modifyTaskForm.taskUsers.length == 0) {
                    this.showAddDetail = false
                    this.step = {
                        index: '',
                        stageId: '',
                        stageName: '',
                        userId: '',
                        userName: '',
                        taskHours: '',
                        beginTime: '',
                        endTime: '',
                        description: '',
                        completeHours: '',
                        completeTime: '',
                        status: ''
                    }
                }
            },
            addMember() {
                this.showAddDetail = !this.showAddDetail;
            },
            cancelAddMember() {
                this.showAddDetail = !this.showAddDetail;
                if (this.step.index !== '') {
                    this.modifyTaskForm.taskUsers[this.step.index] = this.stepTemp;
                    // 取消css
                    this.modifyTaskForm.taskUsers[this.step.index].cssClass = ''
                }
                this.step = {
                    index: '',
                    userId: '',
                    userName: '',
                    beginTime: '',
                    endTime: '',
                    description: '',
                    completeHours: '',
                    completeTime: '',
                    status: ''
                }
                this.otherStep.description = ''
                this.otherStep.status=''
                this.otherStep.taskHours=''
                this.weekNumberTemp = []
            },
            saveAddMember() {
                var sumHours=0;
                for(var i=0;i<this.weekNumber.length;i++){
                    if(this.weekNumber[i].hours==''|| this.weekNumber[i].hours=== undefined){
                        if(this.weekNumber[i].hoursTemp !== undefined &&this.weekNumber[i].hoursTemp!=''){
                            this.weekNumber[i].hours = this.weekNumber[i].hoursTemp
                        }else{
                            this.weekNumber[i].hours = 0
                        }
                    }
                    var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.weekNumber[i].hours);
                    if(!ishours &&ishours!=0){
                        this.errorMsg('工作量填写错误');
                        return false;
                    }
                    if(this.weekNumber[i].hours>99999.9||this.weekNumber[i].hours<0){
                        this.errorMsg('工作量正确值应为0~99999.9');
                        return false;
                    }
                    sumHours +=  parseFloat(this.weekNumber[i].hours)
                }
                if(sumHours!=this.otherStep.taskHours){
                    this.errorMsg('周工作量与总工作量不符，请检查');
                    return
                }
                const valid =
                    this.step.userId == '' ||
                    this.otherStep.taskHours == '' ||
                    this.step.beginTime == '' ||
                    this.step.endTime == '';
                if (valid) {
                    this.warnMsg('请将阶段填写完整');
                    return
                }
                if (this.step.index === '') {
                    let taskUser = {};
                    taskUser.userId = this.step.userId;
                    taskUser.userName = this.step.userName;
                    taskUser.beginTime = this.step.beginTime;
                    taskUser.endTime = this.step.endTime;
                    taskUser.taskHours = this.otherStep.taskHours;
                    taskUser.description = this.otherStep.description;
                    taskUser.userWeeks = this.weekNumber;
                    taskUser.status = this.otherStep.status;
                    this.modifyTaskForm.taskUsers.push(taskUser);
                } else {
                    // 取消css
                    this.modifyTaskForm.taskUsers[this.step.index].cssClass = '';
                    this.modifyTaskForm.taskUsers[this.step.index].description = this.otherStep.description
                    this.modifyTaskForm.taskUsers[this.step.index].status = this.otherStep.status
                    this.modifyTaskForm.taskUsers[this.step.index].taskHours = this.otherStep.taskHours
                    this.modifyTaskForm.taskUsers[this.step.index].userWeeks = this.weekNumber;
                }

                this.showAddDetail = !this.showAddDetail;
                this.step = {
                    index: '',
                    userId: '',
                    userName: '',
                    taskHours: '',
                    beginTime: '',
                    endTime: '',
                    description: '',
                    completeHours: '',
                    completeTime: '',
                    status: ''
                };
                this.otherStep.description=''
                this.otherStep.status =''
                this.otherStep.taskHours =''
                this.stepTemp = {};
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
            saveTaskInfo() {
                /* if (this.modifyTaskForm.description == '') {
                 this.$message.warning("请填写任务备注");
                 return;
                 }*/
                if (this.modifyTaskForm.taskName == '') {
                    this.warnMsg("请填写任务名称");
                    return;
                }
                if (this.modifyTaskForm.doc !== undefined && this.modifyTaskForm.doc !== null && this.modifyTaskForm.doc !== '') {
                    let reg = /(http:\/\/|https:\/\/|HTTP:\/\/|HTTPS:\/\/)(\S+\.)+\S{2,}/;
                    let flag = reg.test(this.modifyTaskForm.doc.trim());
                    if (!flag){
                        this.warnMsg("请输入HTTP://或HTTPS://开头的正确URL");
                        return;
                    }
                }
                if (this.modifyTaskForm.projectId == '') {
                    this.warnMsg("请选择项目");
                    return;
                }
                if (this.modifyTaskForm.createBy == '') {
                    this.warnMsg("请选择负责人");
                    return;
                }
                if (this.modifyTaskForm.beginTime==null || this.modifyTaskForm.beginTime == '') {
                    this.warnMsg("请选择开发日期");
                    return;
                }
                if (this.modifyTaskForm.testTime==null || this.modifyTaskForm.testTime == '') {
                    this.warnMsg("请选择提测日期");
                    return;
                }
                if (this.modifyTaskForm.endTime==null || this.modifyTaskForm.endTime == '') {
                    this.warnMsg("请选择截止日期");
                    return;
                }
                if (this.modifyTaskForm.stageId === '') {
                    this.warnMsg("请选择项目阶段");
                    return;
                }
                if (this.modifyTaskForm.tags.length == 0) {
                    this.warnMsg("请选择至少一项标签");
                    return;
                }
                let param = this.modifyTaskForm;
                param.taskName = param.taskName.trim()
                if (this.modifyTaskForm.doc !== undefined && this.modifyTaskForm.doc !== null && this.modifyTaskForm.doc !== '') {
                    param.doc = param.doc.trim()
                }
                param.description = param.description.trim()
                param.modifyDescription = param.modifyDescription.trim()
                param.taskUsers.forEach((user) => {
                    user.description = user.description.trim()
                    user.beginTime = moment(user.beginTime).format('YYYY-MM-DD HH:mm:ss')
                    user.endTime = moment(user.endTime).format('YYYY-MM-DD 23:59:59')
                    if(user.userWeeks==null){
                        this.errorMsg('请检查周工作量是否填写完整');//判断不可用
                        return false;
                    }
                })
                param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:59');
                param.beginTime = moment(param.beginTime).format('YYYY-MM-DD 23:59:59');
                param.testTime = moment(param.testTime).format('YYYY-MM-DD 23:59:59');
                let vm = this;
                http.zsyPutHttp(`/task/modify/${this.modifyTaskForm.id}`, param, (resp) => {
                    this.$message({ showClose: true,message: '任务修改成功',type: 'success'});
                    this.hideTaskModify()
                    // 刷新看板
                    //this.$root.eventBus.$emit("reloadBoard");
                    // 刷新列表
                    vm.$emit('reload')
                    // 刷新看板
                    this.$root.eventBus.$emit('reloadBoard');
                    this.fetchUnreadNoticeNum();
                })
                this.showCreateTask = false;

            },
            hideTaskCommentDetail() {
                this.showTaskCommentDetail = false
            },
            hideTaskEvaluationDetail() {
                this.showTaskEvaluationDetail = false;
                this.hasAvgEvalution = false;
            },
            commentDetail(taskUserId) {
                let vm = this;
                this.taskDetail.users.forEach((user) => {
                    if (user.id == taskUserId) {
                        vm.taskCommentDetail = user
                        return
                    }
                })
                this.showTaskEvaluationDetail = true
            },
            evaluateDetail(taskUserId,jobRole,userName) {
                this.taskUserName = userName;
                let vm = this;
                this.taskDetail.users.forEach((user) => {
                    if (user.id == taskUserId) {
                        vm.taskEvaluation = user.evaluationResDTOS;
                        this.avgEvaluation = []
                        var length = vm.taskEvaluation.length
                        //测试
                        if (vm.taskEvaluation.length>0){
                            this.hasAvgEvalution = true
                            if (jobRole == 0){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                            }
                            else if (jobRole == 1){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalQualityScore = 0;
                                var totalEfficiencyScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalQualityScore += evaluation.evaluationScoreResDTOS[3].score
                                    totalEfficiencyScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'效率',
                                    'score':Number((totalEfficiencyScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'质量',
                                    'score':Number((totalQualityScore/length).toFixed(2))
                                })
                            }
                            else if (jobRole == 5){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalQualityScore = 0;
                                var totalEfficiencyScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalQualityScore += evaluation.evaluationScoreResDTOS[3].score
                                    totalEfficiencyScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'效率',
                                    'score':Number((totalEfficiencyScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'质量',
                                    'score':Number((totalQualityScore/length).toFixed(2))
                                })
                            }
                            else if (jobRole == 2){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalDesignScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalDesignScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'美感',
                                    'score':Number((totalDesignScore/length).toFixed(2))
                                })
                            }
                            else if (jobRole == 3){
                                var totalCommunicateScore = 0;
                                var totalAttitudeScore = 0;
                                var totalEfficiencyScore = 0;
                                var totalDocumentScore = 0;
                                vm.taskEvaluation.forEach(evaluation=>{
                                    totalCommunicateScore += evaluation.evaluationScoreResDTOS[0].score
                                    totalAttitudeScore += evaluation.evaluationScoreResDTOS[1].score
                                    totalEfficiencyScore += evaluation.evaluationScoreResDTOS[3].score
                                    totalDocumentScore += evaluation.evaluationScoreResDTOS[2].score
                                })
                                this.avgEvaluation.push({
                                    'option':'沟通',
                                    'score':Number((totalCommunicateScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'态度',
                                    'score':Number((totalAttitudeScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'文档',
                                    'score':Number((totalDocumentScore/length).toFixed(2))
                                })
                                this.avgEvaluation.push({
                                    'option':'效率',
                                    'score':Number((totalEfficiencyScore/length).toFixed(2))
                                })
                            }
                        }
                        return
                    }
                })
                this.showTaskEvaluationDetail = true
            },
            /** 编辑任务填写备注 **/
            showModifyDescription(){
                this.modifyDescriptionVisible = true;
            },
            hideModifyDescription(){
                this.modifyDescriptionVisible = false;
                this.hideTaskDetail()
            },
            taskStepStatus(item, taskUserNum){
                // const commented = item.commentNum > 0 && item.commentNum == taskUserNum - 1;
                const commented = item.isEvaluated == 1;
                let className = 'in';
                if (item.status == 1) {
                    // 进行中
                    className = "in"
                }else if(item.status>1 && !commented){
                    // 已完成未评级
                    className = "done"
                }else {
                    // 已评价
                    className = "finished"
                }
                return className;
            },
            //评审任务
            examineTask(id,exam){
                var examText
                if(exam!='0'){
                    examText='确定评审完成?'
                }else{
                    examText='确定取消评审?'
                }
                this.$confirm(examText, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let vm = this;
                    http.zsyPutHttp('/task/examine/'+exam+'/'+id, {}, (resp) => {
                        if(exam!='0'){
                            this.$message({ showClose: true,message: '评审成功',type: 'success'});
                        }else{
                            this.$message({ showClose: true,message: '取消评审成功',type: 'success'});
                        }
                        vm.$emit('reload')
                        // 刷新看板
                        this.$root.eventBus.$emit('reloadBoard');
                        this.showTaskDetail = false;
                    })
                }).catch(() => {
                });
            },
            //测试弹窗
            testTask(id, name, proNames){
                this.showTaskDetail = false
                this.testingVisible = true
                this.testing.taskId = id
                if(proNames && proNames != ""){
                    this.testing.proName = proNames.toString()
                }

                this.testing.name = name
            },
            modifyTestTask(){
                let sumPercent = 0
                for(var i=0;i<this.testWeekNumber.length;i++){
                    if(this.testWeekNumber[i].hours==''|| this.testWeekNumber[i].hours=== undefined){
                        if(this.testWeekNumber[i].hoursTemp !== undefined &&this.testWeekNumber[i].hoursTemp!=''){
                            this.testWeekNumber[i].hours = this.testWeekNumber[i].hoursTemp
                        }else{
                            this.testWeekNumber[i].hours = 0
                        }
                    }
                    var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.testWeekNumber[i].hours);
                    if(!ishours &&ishours!=0){
                        this.errorMsg('百分比填写错误');
                        return false;
                    }
                    if(this.testWeekNumber[i].hours>100 ||this.testWeekNumber[i].hours<0){
                        this.errorMsg('百分比正确值应为0~100');
                        return false;
                    }
                    sumPercent +=  parseFloat(this.testWeekNumber[i].hours)
                }
                if(sumPercent!=100){
                    this.errorMsg('百分比总和应该为100%，请检查');
                    return
                }
                this.testing.beginTime = moment(this.testing.beginTime).format('YYYY-MM-DD HH:00:00')
                this.testing.endTime = moment(this.testing.endTime).format('YYYY-MM-DD HH:00:00')
                this.testing.weeks = this.testWeekNumber
                http.zsyPostHttp('/task/test/add', this.testing, (resp) => {
                    this.$message({ showClose: true,message: '测试时间添加成功',type: 'success'});
                    this.testing.weeks = []
                    this.testing.beginTime = this.testing.endTime =this.testing.taskId =this.testing.name =this.testing.percent =''
                    this.testingVisible = false;
                })
            },
            //暂停任务
            stopTask(id,status){
                var examText
                if(status!='0'){
                    examText='确定启用任务?'
                }else{
                    examText='确定暂停任务?'
                }
                this.$confirm(examText, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    let vm = this;
                    http.zsyPutHttp('/task/stop/'+status+'/'+id+"?desc="+ vm.modifyTaskForm.modifyDescription,{}, (resp) => {
                        if(status!='0'){
                            this.$message({ showClose: true,message: '启用成功',type: 'success'});
                        }else{
                            this.$message({ showClose: true,message: '暂停成功',type: 'success'});
                        }
                        vm.$emit('reload')
                        // 刷新看板
                        this.$root.eventBus.$emit('reloadBoard');
                        this.showTaskDetail = false;
                        this.modifyStopVisible =false;
                        this.fetchUnreadNoticeNum()
                    })
                }).catch(() => {
                });
            },
            changeTestWeek(){
                if(this.testing.beginTime == null || this.testing.endTime ==null){
                    return
                }
                this.testWeekNumber = [];
                let weekData='';
                let param = this.testWeekNumber;
                this.testing.beginWeek = moment(this.testing.beginTime).week()
                this.testing.endWeek = moment(this.testing.endTime).week()
                let beginYear = moment(this.testing.beginTime).year();
                let endYear = moment(this.testing.endTime).year();
                if(beginYear!=endYear){
                    for(let i=this.testing.beginWeek;i<moment(this.testing.beginTime).weeksInYear()+1;i++){
                        weekData = {'weekNumber':i, 'hours': '','year':beginYear ,'range':moment().year(beginYear).week(i).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(i).endOf('week').format('MM-DD') };
                        param.push(weekData)
                    }
                    for(let i=1;i<this.testing.endWeek+1;i++){
                        weekData = {'weekNumber':i, 'hours': '','year':endYear ,'range':moment().year(endYear).week(i).startOf('week').format('MM-DD')+'至'+moment().year(endYear).week(i).endOf('week').format('MM-DD') };
                        param.push(weekData)
                    }
                }
                if(this.testing.beginWeek == this.testing.endWeek){
                    weekData = {'weekNumber':this.testing.beginWeek, 'hours': 100+'' ,'year':beginYear ,'range':moment().year(beginYear).week(this.testing.beginWeek).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(this.testing.beginWeek).endOf('week').format('MM-DD')};
                    param.push(weekData)
                }else if(this.testing.endWeek - this.testing.beginWeek >1){
                    for(let i=this.testing.beginWeek;i<this.testing.endWeek+1;i++){
                        weekData = {'weekNumber':i, 'hours': '','year':beginYear ,'range':moment().week(i).year(beginYear).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(i).endOf('week').format('MM-DD')  };
                        param.push(weekData)
                    }
                }else if(this.testing.endWeek - this.testing.beginWeek == 1){
                    param.push( {'weekNumber':this.testing.beginWeek, 'hours': '' ,'year':beginYear ,'range':moment().year(beginYear).week(this.testing.beginWeek).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(this.testing.beginWeek).endOf('week').format('MM-DD')})
                    param.push( {'weekNumber':this.testing.endWeek, 'hours': '' ,'year':beginYear ,'range':moment().year(beginYear).week(this.testing.endWeek).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(this.testing.endWeek).endOf('week').format('MM-DD')})
                }
                this.testWeekNumber = param
            },
            warnMsg(msg) {
                this.$message({
                    showClose: true,
                    message: msg,
                    type: 'warning'
                });
            },
            errorMsg(msg) {
                this.$message({
                    showClose: true,
                    message: msg,
                    type: 'error'
                });
            },
            //申请修改任务
            applyModifyMyTask(task){
                var taskId = task.taskUsers[0].taskId;
                var userId = task.taskUsers[0].userId;
                this.modifyMyTaskForm.userId = userId;
                this.modifyMyTaskForm.taskId = taskId;
                this.getTaskDetail(taskId)
                http.zsyGetHttp('/task/task-user/'+taskId+'/'+userId,{},(res=>{
                    this.taskUser = res.data;
                    this.changeTaskUserWeek()
                    this.showFinishedTask = false;
                }))
                this.modifyMyTaskVisible = true;
            },
            applyExpandTime(task){
                this.expandTime.name = task.name
                this.expandTime.beginTime= task.taskUsers[0].beginTime
                this.expandTime.endTime= task.taskUsers[0].endTime
                this.expandTime.taskId= task.id
                this.expandTime.reason = null
                this.expandTime.hours = 0
                this.expandTimeVisible = true
                this.showFinishedTask = false
            },
            addTaskExpand(){
                var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.expandTime.hours);
                if(!ishours){
                    this.errorMsg('时间填写错误');
                    return false;
                }

                this.expandTime.endTime= moment(this.expandTime.endTime).format('YYYY-MM-DD HH:00:00')
                http.zsyPostHttp('/task-expand/add', this.expandTime, (resp) => {
                    this.$message({ showClose: true,message: '任务时间延长申请成功',type: 'success'});
                    this.expandTimeVisible = false;
                    this.clearExpand();
                    this.$emit('reload');
                    this.$root.eventBus.$emit('reloadBoard');
                })
            },
            clearExpand(){
                this.expandTime.name = ''
                this.expandTime.beginTime= null
                this.expandTime.endTime= null
                this.expandTime.taskId= ''
                this.expandTime.hours = 0
            },
            //查询所有未读数量
            fetchUnreadNoticeNum(){
                http.zsyGetHttp('/task/notification/un-read/num',{},(res)=>{
                    window.localStorage.setItem("unreadNum",res.data.count)
                })
            },

            //查看多人待审核任务详情
            getTaskTempDetail(taskId,ttId){
                http.zsyGetHttp('/task-temp/personal/'+ttId,{},(res => {
                    this.taskTempDetail = res.data;
                    this.description = this.taskTempDetail.description;
                    this.beginTime = this.taskTempDetail.beginTime;
                    this.endTime = this.taskTempDetail.endTime;
                    this.getTaskDetail(taskId);
                    this.changeTaskTempWeek();
                    this.taskTempDetailVisible = true;
                }))

            },

            //获取任务详情
            getTaskDetail(id){
                if(id != null && id != ''){
                    http.zsyGetHttp('task/detail/'+id,{},(res =>{
                        this.taskDetail = res.data;
                        this.showTaskDetailVisible = true
                    }))
                }else {
                    this.taskDetail = {};
                    this.showTaskDetailVisible = false;
                }
            },

            changeTime(){
                if(this.taskTempDetail.beginTime != null && this.taskTempDetail.beginTime != null
                   && this.taskTempDetail.endTime != null&& this.taskTempDetail.endTime != null){
                    let userWeeks = [];
                    let beginYear = moment(this.taskTempDetail.beginTime).year();
                    let endYear = moment(this.taskTempDetail.endTime).year();
                    this.taskTempDetail.beginWeek = moment(this.taskTempDetail.beginTime).week();
                    this.taskTempDetail.endWeek = moment(this.taskTempDetail.endTime).week();

                    if (this.taskTempDetail.endWeek - this.taskTempDetail.beginWeek + 1 != this.taskTempDetail.userWeekTempList.length){
                        if ((endYear >= beginYear) && (this.taskTempDetail.endWeek >= this.taskTempDetail.beginWeek)){
                            for(var i = 0;i<=this.taskTempDetail.endWeek - this.taskTempDetail.beginWeek;i++){
                                let weekData = {
                                    'weekHours':0,
                                    'weekNumber': i + this.taskTempDetail.beginWeek,
                                    'hours': 0,
                                    'year': beginYear,
                                    'range': moment().year(beginYear).week(i + this.taskTempDetail.beginWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(i + this.taskTempDetail.beginWeek).endOf('week').format('MM-DD')
                                };
                                userWeeks.push(weekData)

                            }
                        }
                        this.taskTempDetail.userWeekTempList = userWeeks;
                        this.changeTaskTempWeek();
                    }
                    if (this.taskTempDetail.endWeek < this.taskTempDetail.beginWeek){
                        this.$message({ showClose: true,message: '开始时间不可在截止时间后面',type: 'error'});
                    }
                }


            },

            changeTaskTempWeek(){
                if (this.taskTempDetail.beginTime == null || this.taskTempDetail.endTime == null) {
                    return
                }
                this.taskTempWeekNumber = [];
                let weekData = '';
                let param = this.taskTempWeekNumber;
                this.taskTempDetail.beginWeek = moment(this.taskTempDetail.beginTime).week()
                this.taskTempDetail.endWeek = moment(this.taskTempDetail.endTime).week()
                let beginYear = moment(this.taskTempDetail.beginTime).year();
                let endYear = moment(this.taskTempDetail.endTime).year();
                let userWeeks = this.taskTempDetail.userWeekTempList;
                if (beginYear != endYear) {
                    for (let i = this.taskTempDetail.beginWeek; i < moment(this.taskTempDetail.beginTime).weeksInYear() + 1; i++) {
                        http.zsyGetHttp('/userWeek/'+ this.taskTempDetail.taskId +'/' + this.taskTempDetail.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                            weekData = {
                                'weekHours':resp.data,
                                'weekNumber': i,
                                'hours': userWeeks[i-this.taskTempDetail.beginWeek].hours,
                                'year': beginYear,
                                'range': moment().year(beginYear).week(i).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(i).endOf('week').format('MM-DD')
                            };
                            param.push(weekData)
                        })
                    }
                    for (let i = 1; i < this.taskTempDetail.endWeek + 1; i++) {
                        http.zsyGetHttp('/userWeek/'+ this.taskTempDetail.taskId +'/' + this.taskTempDetail.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                            weekData = {
                                'weekHours':resp.data,
                                'weekNumber': i,
                                'hours': userWeeks[i-1].hours,
                                'year': endYear,
                                'range': moment().year(endYear).week(i).startOf('week').format('MM-DD') + '至' + moment().year(endYear).week(i).endOf('week').format('MM-DD')
                            };
                            param.push(weekData)
                        })

                    }
                }
                if (this.taskTempDetail.beginWeek == this.taskTempDetail.endWeek) {
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

                } else if (this.taskTempDetail.endWeek - this.taskTempDetail.beginWeek > 1) {
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
                } else if (this.taskTempDetail.endWeek - this.taskTempDetail.beginWeek == 1) {

                    http.zsyGetHttp('/userWeek/'+ this.taskTempDetail.taskId +'/' + this.taskTempDetail.userId + '/' + beginYear + '/' + this.taskTempDetail.beginWeek, {}, (resp) => {
                        param.push({
                            'weekHours':resp.data,
                            'weekNumber': this.taskTempDetail.beginWeek,
                            'hours': userWeeks[0].hours,
                            'year': beginYear,
                            'range': moment().year(beginYear).week(this.taskTempDetail.beginWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.taskTempDetail.beginWeek).endOf('week').format('MM-DD')
                        })
                    })
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
                this.taskTempWeekNumber = param
            },
            changeTaskUserWeek(){
                if (this.taskUser.beginTime == null || this.taskUser.endTime == null) {
                    return
                }
                this.taskUserWeekNumber = [];
                let weekData = '';
                let param = this.taskUserWeekNumber;
                this.taskUser.beginWeek = moment(this.taskUser.beginTime).week()
                this.taskUser.endWeek = moment(this.taskUser.endTime).week()
                let beginYear = moment(this.taskUser.beginTime).year();
                let endYear = moment(this.taskUser.endTime).year();
                let userWeeks = this.taskUser.userWeeks;
                if (beginYear != endYear) {
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
                        http.zsyGetHttp('/userWeek/'+ this.taskUser.taskId +'/' + this.taskUser.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                            weekData = {
                                'weekHours':resp.data,
                                'weekNumber': i,
                                'hours': userWeeks[i-1].hours,
                                'year': endYear,
                                'range': moment().year(endYear).week(i).startOf('week').format('MM-DD') + '至' + moment().year(endYear).week(i).endOf('week').format('MM-DD')
                            };
                            param.push(weekData)
                        })

                    }
                }
                if (this.taskUser.beginWeek == this.taskUser.endWeek) {
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

                } else if (this.taskUser.endWeek - this.taskUser.beginWeek > 1) {
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
                } else if (this.taskUser.endWeek - this.taskUser.beginWeek == 1) {

                    http.zsyGetHttp('/userWeek/'+ this.taskUser.taskId +'/' + this.taskUser.userId + '/' + beginYear + '/' + this.taskUser.beginWeek, {}, (resp) => {
                        param.push({
                            'weekHours':resp.data,
                            'weekNumber': this.taskUser.beginWeek,
                            'hours': userWeeks[0].hours,
                            'year': beginYear,
                            'range': moment().year(beginYear).week(this.taskUser.beginWeek).startOf('week').format('MM-DD') + '至' + moment().year(beginYear).week(this.taskUser.beginWeek).endOf('week').format('MM-DD')
                        })
                    })
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
                this.taskUserWeekNumber = param
            },
            editMultipleTask(formName) {
                var sumHours=0;
                if((moment(this.taskTempDetail.beginTime).isAfter(moment(this.taskTempDetail.endTime)))){
                    this.$message({showClose: true, message: '开始时间不能在截止时间后面', type: 'error'});
                    return false;
                }
                for(var i=0;i<this.taskTempWeekNumber.length;i++){
                    if(this.taskTempWeekNumber[i].hours==''|| this.taskTempWeekNumber[i].hours=== undefined){
                        if(this.taskTempWeekNumber[i].hoursTemp !== undefined &&this.taskTempWeekNumber[i].hoursTemp!=''){
                            this.taskTempWeekNumber[i].hours = this.taskTempWeekNumber[i].hoursTemp
                        }else{
                            this.taskTempWeekNumber[i].hours = 0
                        }
                    }
                    var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.taskTempWeekNumber[i].hours);
                    if(!ishours &&ishours!=0){
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
                this.taskTempDetail.userWeeks = this.taskTempWeekNumber


                let vm = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        // this.taskAble = true;
                        var param = this.taskTempDetail;
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD 00:00:00')
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:50')
                        if (param.taskId == null || param.taskId == ''){
                            this.$message({showClose: true, message: '关联任务不能为空', type: 'error'});
                            return false;
                        }
                        if (this.description == null || this.description.trim() == ''){
                            this.$message({showClose: true, message: '任务描述不能为空', type: 'error'});
                            return false;
                        }
                        if (param.beginTime == null || param.beginTime == ''){
                            this.$message({showClose: true, message: '开始时间不能为空', type: 'error'});
                            return false;
                        }
                        if (param.endTime == null || param.endTime == ''){
                            this.$message({showClose: true, message: '截止时间不能为空', type: 'error'});
                            return false;
                        }
                        if((moment(param.beginTime).isAfter(moment(param.endTime)))){
                            this.$message({showClose: true, message: '开始时间不能在截止时间后面', type: 'error'});
                            return false;
                        }
                        //任务是设计相关阶段时
                        if (this.taskDetail.stageId == '212754785051344891' || this.taskDetail.stageId == '212754785051344892'){
                            if (!(moment(this.taskTempDetail.endTime)).isBefore((moment(this.taskDetail.beginTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务设计完成时间', type: 'error'});
                                return false;
                            }
                        }
                        //任务是开发相关阶段时
                        if (this.taskDetail.stageId == '212754785051344890' || this.taskDetail.stageId == '212754785051344894'){
                            if (!(moment(this.taskTempDetail.endTime)).isBefore((moment(this.taskDetail.testTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务开发完成时间', type: 'error'});
                                return false;
                            }
                        }
                        //任务是测试相关阶段时
                        if (this.taskDetail.stageId == '212754785051344895' || this.taskDetail.stageId == '212754785051344896'){
                            if (!(moment(this.taskTempDetail.endTime)).isBefore((moment(this.taskDetail.endTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务截止时间', type: 'error'});
                                return false;
                            }
                        }
                        param.workHours = String(param.workHours)
                        if (param.workHours.length != parseFloat(param.workHours).toString().length || parseFloat(param.workHours) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            return false;
                        }
                        if (param.workHours < 0.1) {
                            this.$message({showClose: true, message: '工作量正确值应为0.1~8', type: 'error'});
                            return false;
                        }
                        // param.workHours = Number(param.workHours)
                        param.description = this.description
                        http.zsyPutHttp('/task-temp/update', param, (resp) => {
                            this.taskTempDetailVisible = false;
                            this.$message({showClose: true, message: '任务修改成功', type: 'success'});
                            this.$refs[formName].resetFields();
                            this.description = ''
                            this.taskAble = false;
                            vm.$emit('reload');
                            // window.history.go(0)
                        });
                    } else {
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
                        this.$emit('reload');
                        this.taskTempDetailVisible = false;
                    })
                }).catch(() => {
                });
            },
            saveApplyModifyMyTask(formName) {
                var sumHours=0;
                if((moment(this.modifyMyTaskForm.beginTime).isAfter(moment(this.modifyMyTaskForm.endTime)))){
                    this.$message({showClose: true, message: '开始时间不能在截止时间后面', type: 'error'});
                    return false;
                }
                for(var i=0;i<this.taskModifyWeekNumber.length;i++){
                    if(this.taskModifyWeekNumber[i].hours==''|| this.taskModifyWeekNumber[i].hours=== undefined){
                        if(this.taskModifyWeekNumber[i].hoursTemp !== undefined &&this.taskModifyWeekNumber[i].hoursTemp!=''){
                            this.taskModifyWeekNumber[i].hours = this.taskModifyWeekNumber[i].hoursTemp
                        }else{
                            this.taskModifyWeekNumber[i].hours = 0
                        }
                    }
                    var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.taskModifyWeekNumber[i].hours);
                    if(!ishours &&ishours!=0){
                        this.errorMsg('工作量填写错误');
                        return false;
                    }
                    if(this.taskModifyWeekNumber[i].hours>99999.9||this.taskModifyWeekNumber[i].hours<0){
                        this.errorMsg('工作量正确值应为0~99999.9');
                        return false;
                    }
                    sumHours +=  parseFloat(this.taskModifyWeekNumber[i].hours)
                }

                if(sumHours!=this.modifyMyTaskForm.workHours){
                    this.errorMsg('周工作量与总工作量不符，请检查');
                    return
                }
                this.modifyMyTaskForm.userWeeks = this.taskModifyWeekNumber


                let vm = this;
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        // this.taskAble = true;
                        var param = this.modifyMyTaskForm;
                        param.beginTime = moment(param.beginTime).format('YYYY-MM-DD 00:00:00')
                        param.endTime = moment(param.endTime).format('YYYY-MM-DD 23:59:50')
                        if (this.modifyMyTaskReason == null || this.modifyMyTaskReason.trim() == ''){
                            this.$message({showClose: true, message: '修改原因不能为空', type: 'error'});
                            return false;
                        }
                        if (this.modifyMyTaskDescription == null || this.modifyMyTaskDescription.trim() == ''){
                            this.$message({showClose: true, message: '新的任务描述不能为空', type: 'error'});
                            return false;
                        }
                        if (param.beginTime == null || param.beginTime == ''){
                            this.$message({showClose: true, message: '开始时间不能为空', type: 'error'});
                            return false;
                        }
                        if (param.endTime == null || param.endTime == ''){
                            this.$message({showClose: true, message: '截止时间不能为空', type: 'error'});
                            return false;
                        }
                        if((moment(param.beginTime).isAfter(moment(param.endTime)))){
                            this.$message({showClose: true, message: '开始时间不能在截止时间后面', type: 'error'});
                            return false;
                        }
                        //任务是设计相关阶段时
                        if (this.taskDetail.stageId == '212754785051344891' || this.taskDetail.stageId == '212754785051344892'){
                            if (!(moment(this.modifyMyTaskForm.endTime)).isBefore((moment(this.taskDetail.beginTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务设计完成时间', type: 'error'});
                                return false;
                            }
                        }
                        //任务是开发相关阶段时
                        if (this.taskDetail.stageId == '212754785051344890' || this.taskDetail.stageId == '212754785051344894'){
                            if (!(moment(this.modifyMyTaskForm.endTime)).isBefore((moment(this.taskDetail.testTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务开发完成时间', type: 'error'});
                                return false;
                            }
                        }
                        //任务是测试相关阶段时
                        if (this.taskDetail.stageId == '212754785051344895' || this.taskDetail.stageId == '212754785051344896'){
                            if (!(moment(this.modifyMyTaskForm.endTime)).isBefore((moment(this.taskDetail.endTime)))) {
                                this.$message({showClose: true, message: '截止时间不能超过任务截止时间', type: 'error'});
                                return false;
                            }
                        }
                        param.workHours = String(param.workHours)
                        if (param.workHours.length != parseFloat(param.workHours).toString().length || parseFloat(param.workHours) == "NaN") {
                            this.$message({showClose: true, message: '工作量只能为数字或者小数', type: 'error'});
                            return false;
                        }
                        if (param.workHours < 0.1) {
                            this.$message({showClose: true, message: '工作量正确值应为0.1~8', type: 'error'});
                            return false;
                        }
                        param.reason = this.modifyMyTaskReason;
                        param.description = this.modifyMyTaskDescription;
                        http.zsyPostHttp('/task-modify/add', param, (resp) => {
                            this.modifyMyTaskVisible = false;
                            this.$message({showClose: true, message: '添加任务修改申请成功', type: 'success'});
                            this.$refs[formName].resetFields();
                            this.clearModifyMyTaskForm()
                            // this.taskAble = false;
                            vm.$emit('reload')
                            // window.history.go(0)
                        });
                    } else {
                        return false;
                    }
                }, err => {
                    // this.taskAble = false
                });
            },
            clearModifyMyTaskForm(){
                this.modifyMyTaskReason = '';
                this.modifyMyTaskDescription = '';
                this.taskModifyWeekNumber = [];
                this.modifyMyTaskForm.reason = '';
                this.modifyMyTaskForm.workHours = '';
                this.modifyMyTaskForm.description = '';
                this.modifyMyTaskForm.taskId = null;
                this.modifyMyTaskForm.userId = null;
                this.modifyMyTaskForm.beginTime = '';
                this.modifyMyTaskForm.endTime = '';
                this.modifyMyTaskForm.userWeeks = [];
            },
            closeMultipleTask(){
                // this.$emit('reload')
                // this.taskTempDetail = {};
            },

        },
        created() {
            // 监听看板任务点击事件
            var vm = this;
            vm.$root.eventBus.$off('handleBoardClick')
            vm.$root.eventBus.$on("handleBoardClick", (taskId) => {
                vm.showTaskDetail = true;
                http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    vm.taskDetail = resp.data
                });
                vm.getTaskLog(taskId)
            });
        },
        watch:{
            step:{
                deep:true,
                handler:function (val, oldVal) {
                    this.weekNumber = [];
                    let weekData='';
                    let param = this.weekNumber;
                    if (this.step.userId != '' && this.otherStep.taskHours != '' && this.step.beginTime != '' && this.step.endTime != ''&& (moment(this.step.beginTime).isBefore(this.step.endTime)|| moment(this.step.beginTime).isSame(this.step.endTime))) {
                        this.weekTime.beginWeek = moment(this.step.beginTime).week()
                        this.weekTime.endWeek = moment(this.step.endTime).week()
                        let beginYear = moment(this.step.beginTime).year();
                        let endYear = moment(this.step.endTime).year();
                        if(beginYear!=endYear){
                            for(let i=this.weekTime.beginWeek;i<moment(this.step.beginTime).weeksInYear()+1;i++){
                                http.zsyGetHttp('/userWeek/'+ this.modifyTaskForm.id +'/' + this.step.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                                    weekData = {'weekNumber':i, 'hours': '','year':beginYear ,'weekHours': resp.data,'range':moment().year(beginYear).week(i).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(i).endOf('week').format('MM-DD') };
                                    param.push(weekData)
                                })
                            }
                            for(let i=1;i<this.weekTime.endWeek+1;i++){
                                http.zsyGetHttp('/userWeek/'+ this.modifyTaskForm.id +'/' + this.step.userId + '/' + endYear + '/' + i, {}, (resp) => {
                                    weekData = {'weekNumber':i, 'hours': '','year':endYear ,'weekHours': resp.data,'range':moment().year(endYear).week(i).startOf('week').format('MM-DD')+'至'+moment().year(endYear).week(i).endOf('week').format('MM-DD') };
                                    param.push(weekData)
                                })

                            }
                        }
                        if(this.weekTime.beginWeek == this.weekTime.endWeek){
                            http.zsyGetHttp('/userWeek/'+ this.modifyTaskForm.id +'/' +  this.step.userId + '/' + beginYear + '/' + this.weekTime.beginWeek, {}, (resp) => {
                                weekData = {'weekNumber':this.weekTime.beginWeek, 'hours': this.otherStep.taskHours ,'weekHours': resp.data,'year':beginYear ,'range':moment().year(beginYear).week(this.weekTime.beginWeek).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(this.weekTime.beginWeek).endOf('week').format('MM-DD')};
                                param.push(weekData)
                            })
                        }else if(this.weekTime.endWeek - this.weekTime.beginWeek >1){
                            for(let i=this.weekTime.beginWeek;i<this.weekTime.endWeek+1;i++){
                                http.zsyGetHttp('/userWeek/'+ this.modifyTaskForm.id +'/' +  this.step.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                                    weekData = {'weekNumber':i, 'hours': '','year':beginYear ,'weekHours': resp.data,'range':moment().week(i).year(beginYear).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(i).endOf('week').format('MM-DD')  };
                                    param.push(weekData)
                                })
                            }
                        }else if(this.weekTime.endWeek - this.weekTime.beginWeek == 1){
                            http.zsyGetHttp('/userWeek/'+ this.modifyTaskForm.id +'/' +  this.step.userId + '/' + beginYear + '/' + this.weekTime.beginWeek, {}, (resp) => {
                                param.push( {'weekNumber':this.weekTime.beginWeek, 'hours': '' ,'year':beginYear  ,'weekHours': resp.data,'range':moment().year(beginYear).week(this.weekTime.beginWeek).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(this.weekTime.beginWeek).endOf('week').format('MM-DD')})
                            })
                            http.zsyGetHttp('/userWeek/'+ this.modifyTaskForm.id +'/' + this.step.userId + '/' + endYear + '/' + this.weekTime.endWeek, {}, (resp) => {
                                param.push( {'weekNumber':this.weekTime.endWeek, 'hours': '' ,'year':beginYear ,'weekHours': resp.data,'range':moment().year(beginYear).week(this.weekTime.endWeek).startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(this.weekTime.endWeek).endOf('week').format('MM-DD')})

                            })
                        }
                        this.weekNumber = param
                    }
                }
            },
            modifyMyTaskForm:{
                deep:true,
                handler:function (val, oldVal) {
                    this.taskModifyWeekNumber = [];
                    let weekData='';
                    let param = this.taskModifyWeekNumber;
                    if (this.modifyMyTaskForm.userId != '' && this.modifyMyTaskForm.workHours != ''
                        && this.modifyMyTaskForm.beginTime != '' && this.modifyMyTaskForm.endTime != ''
                        && (moment(this.modifyMyTaskForm.beginTime).isBefore(this.modifyMyTaskForm.endTime)
                            || moment(this.modifyMyTaskForm.beginTime).isSame(this.modifyMyTaskForm.endTime))) {
                        this.taskModifyWeekTime.beginWeek = moment(this.modifyMyTaskForm.beginTime).week()
                        this.taskModifyWeekTime.endWeek = moment(this.modifyMyTaskForm.endTime).week()
                        let beginYear = moment(this.modifyMyTaskForm.beginTime).year();
                        let endYear = moment(this.modifyMyTaskForm.endTime).year();
                        if(beginYear!=endYear){
                            for(let i=this.taskModifyWeekTime.beginWeek;i<moment(this.taskModifyWeekTime.beginTime).weeksInYear()+1;i++){
                                http.zsyGetHttp('/userWeek/without/'+ this.modifyMyTaskForm.taskId +'/' + this.modifyMyTaskForm.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                                    weekData = {
                                        'weekNumber':i,
                                        'hours': '',
                                        'year':beginYear ,
                                        'weekHours': resp.data,
                                        'range':moment().year(beginYear).week(i)
                                            .startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(i)
                                            .endOf('week').format('MM-DD') };
                                    param.push(weekData)
                                })
                            }
                            for(let i=1;i<this.taskModifyWeekTime.endWeek+1;i++){
                                http.zsyGetHttp('/userWeek/without/'+ this.modifyMyTaskForm.taskId +'/' + this.modifyMyTaskForm.userId + '/' + endYear + '/' + i, {}, (resp) => {
                                    weekData = {
                                        'weekNumber':i,
                                        'hours': '',
                                        'year':endYear ,
                                        'weekHours': resp.data,
                                        'range':moment().year(endYear).week(i).startOf('week')
                                            .format('MM-DD')+'至'+moment().year(endYear)
                                            .week(i).endOf('week').format('MM-DD') };
                                    param.push(weekData)
                                })

                            }
                        }
                        if(this.taskModifyWeekTime.beginWeek == this.taskModifyWeekTime.endWeek){
                            http.zsyGetHttp('/userWeek/without/'+ this.modifyMyTaskForm.taskId +'/' +  this.modifyMyTaskForm.userId + '/' + beginYear + '/' + this.taskModifyWeekTime.beginWeek, {}, (resp) => {
                                weekData = {
                                    'weekNumber':this.taskModifyWeekTime.beginWeek,
                                    'hours': this.modifyMyTaskForm.workHours ,
                                    'weekHours': resp.data,
                                    'year':beginYear ,
                                    'range':moment().year(beginYear).week(this.taskModifyWeekTime.beginWeek)
                                        .startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(this.taskModifyWeekTime.beginWeek)
                                        .endOf('week').format('MM-DD')};
                                param.push(weekData)
                            })
                        }else if(this.taskModifyWeekTime.endWeek - this.taskModifyWeekTime.beginWeek >1){
                            for(let i=this.taskModifyWeekTime.beginWeek;i<this.taskModifyWeekTime.endWeek+1;i++){
                                http.zsyGetHttp('/userWeek/without/'+ this.modifyMyTaskForm.taskId +'/' +  this.modifyMyTaskForm.userId + '/' + beginYear + '/' + i, {}, (resp) => {
                                    weekData = {
                                        'weekNumber':i,
                                        'hours': '',
                                        'year':beginYear ,
                                        'weekHours': resp.data,
                                        'range':moment().week(i).year(beginYear)
                                            .startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(i)
                                            .endOf('week').format('MM-DD')  };
                                    param.push(weekData)
                                })
                            }
                        }else if(this.taskModifyWeekTime.endWeek - this.taskModifyWeekTime.beginWeek == 1){
                            http.zsyGetHttp('/userWeek/without/'+ this.modifyMyTaskForm.taskId +'/' +  this.modifyMyTaskForm.userId + '/' + beginYear + '/' + this.taskModifyWeekTime.beginWeek, {}, (resp) => {
                                param.push( {
                                    'weekNumber':this.taskModifyWeekTime.beginWeek,
                                    'hours': '' ,
                                    'year':beginYear  ,
                                    'weekHours': resp.data,
                                    'range':moment().year(beginYear).week(this.taskModifyWeekTime.beginWeek)
                                        .startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(this.taskModifyWeekTime.beginWeek)
                                        .endOf('week').format('MM-DD')})
                            })
                            http.zsyGetHttp('/userWeek/without/'+ this.modifyMyTaskForm.taskId +'/' + this.modifyMyTaskForm.userId + '/' + endYear + '/' + this.taskModifyWeekTime.endWeek, {}, (resp) => {
                                param.push( {
                                    'weekNumber':this.taskModifyWeekTime.endWeek,
                                    'hours': '' ,
                                    'year':beginYear ,
                                    'weekHours': resp.data,
                                    'range':moment().year(beginYear).week(this.taskModifyWeekTime.endWeek)
                                        .startOf('week').format('MM-DD')+'至'+moment().year(beginYear).week(this.taskModifyWeekTime.endWeek)
                                        .endOf('week').format('MM-DD')})

                            })
                        }
                        this.taskModifyWeekNumber = param
                    }
                }
            },
            evaluation:{
                deep:true,
                handler(oldVal,newVal){
                }
            }
        }

    }
</script>
<style>
    .task-avatar{
        height: 40px;
        border-radius: 50%;
        width: 40px;
    }
    .task-title-detail{
        margin-top:-5px;
        line-height: 20px;
        font-size:12px;
        color: #ccc;
    }
    .task-title-detail em{
        margin-right: 5px;
        border-left:3px solid #ccc;
    }
    .el-dialog__wrapper .myDialog {
        width: 600px !important;
    }

    .my-dialog-title {
        font-size: 16px;
        font-weight: bold;
    }

    .my-dialog-title-tool {
        float: right;
    }

    .red-border {
        border-left: red 3px solid;
    }

    .orange-border {
        border-left: orange 3px solid;
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


</style>
<style scoped>
    .stepActive {
        box-shadow: 0 0 10px #20A0FF !important;
    }

    .mark-stage {
        line-height: 90px;
        margin-right: 20px;
        font-size: 15px;
    }

    .star {
        color: red;
        padding: 1px;
    }

    .empty {
        text-align: center;
        font-size: 16px;
        padding: 20px;
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

    .main-task-detail {
        flex: 1;
        -webkit-flex: 1;
        -moz-flex: 1;
        -ms-flex: 1;
        -o-flex: 1;
    }

    .task-mark {
        line-height: 90px;
        font-size: 18px;
    }

    .task-mark > img, .task-mark > span {
        vertical-align: middle;
    }

    .task-mark > span {
        margin-right: 20px;
        margin-left: 10px;
    }

    .task-name {
        margin-top: 18px;
        font-size: 16px;
    }

    .task-state {
        margin-top: 10px;
    }

    .task-state .iconfont {
        margin-right: 5px;
    }

    .task-state > span, .task-data-show > span {
        display: inline-block;
        vertical-align: middle;
    }

    .task-end {
        padding: 2px 10px;
        color: #fff;
    }

    .task-end.red {
        background: #FF4515;
    }

    .task-end.orange {
        background: #FF9900;
    }

    .task-end.blue {
        background: #36A8FF;
    }

    .task-end.green {
        background: #339933;
    }

    .task-end.purple {
        background: #da70d6;
    }

    .task-time-opt {
        color: #36A8FF;
        font-size: 20px;
        margin-left: 16px;
        cursor: pointer;
    }

    .task-data-show {
        margin: 0 40px 0 20px;
    }

    .task-score {
        font-size: 18px;
        line-height: 92px;
    }

    .task-level {
        width: 44px;
        height: 44px;
        line-height: 44px;
        text-align: center;
        border-radius: 50%;
        color: #fff;
        font-size: 20px;
        margin-left: 16px;
    }

    .task-level.first {
        background: #FF9900;
    }

    .task-level.second {
        background: #99CC66;
    }

    .task-level.third {
        background: #999900;
    }

    .task-level.forth {
        background: #9993F1;
    }

    .task-key-tag {
        margin-left: 10px;
        display: inline-block;
        vertical-align: middle;
    }

    .task-key-tag li {
        display: inline-block;
        margin-right: 20px;
    }

    .task-key-lis > span {
        display: inline-block;
        vertical-align: middle;
    }

    .task-key-lis .circle {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        margin-right: 4px;
    }

    .task-key-lis .circle.pink {
        background: #FF9966;
    }

    .task-key-lis .circle.purple {
        background: #D863B0;
    }

    .task-key-lis .circle.red {
        background: #CC0000;
    }

    .task-key-lis .circle.gray {
        background: #999999;
    }

    .task-key-lis .circle.blue {
        background: #0E0E9D;
    }
</style>
<style scoped>
    .ctpc-member-con {
        margin: 15px 0;
        padding-left: 10px; /* border-left: 1px solid #ccc; */
        margin-left: 6px;
        position: relative;
    }

    .trends {
        /*background-color: #f2f2f2; */
        /*padding-left: 10px;*/
        line-height: 30px;
        border: 1px solid #e4e8f1;

    }

    .trends ul{
        padding-left: 10px;
        list-style: circle;
    }
    .trends li{
        /*list-style: circle!important;*/
    }
    .trends li:before{
        content:"*";
        float: left;
        margin-right: 5px;
        color: #f40;
    }

    .trends-title {
        padding: 0 10px;
        line-height: 30px;
        background-color: #e4e8f1;
    }

    .trends-title a {
        color: #20a0ff;

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

    .ctpc-member-list.finished:before {
        content: '';
        position: absolute;
        left: -17px;
        top: 12px;
        width: 12px;
        height: 12px;
        border-radius: 50%;
        background: #006699;
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
        margin-left: 30px;
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

</style>
<style scoped>
    .assess-man-detail {
        background: #fff;
        border: 1px solid #ccc;
        padding: 2px 6px;
        line-height: 36px;
        margin-right: 10px;
    }

    .assess-man-detail > span {
        display: inline-block;
    }

    .amd-job {
        width: 100px;
    }

    .amd-job-time {
        width: 120px;
    }

    .amd-during-time {
        width: 200px;
    }

    .amd-name {
        width: 36px;
        height: 36px;
        text-align: center;
        line-height: 36px;
        border-radius: 50%;
        background: #006699;
        color: #fff;
        font-size: 10px;
    }

    .assess-level-opt > span {
        display: inline-block;
        vertical-align: middle;
        line-height: 36px;
    }

    .assess-msg-inp {
        border: 1px solid #ccc;
        padding: 10px;
        width: 496px;
    }

    .alo-radio > input, .alo-radio > label {
        cursor: pointer;
    }

    /*.assess-task-list{margin-top: 16px;}*/
    /*.assess-task{height: 500px;overflow-y: scroll;}*/
    .assess-task::-webkit-scrollbar {
        width: 10px;
        background-color: #fff;
    }

    .assess-task::-webkit-scrollbar-thumb {
        background-color: rgb(255, 255, 255);
        background-image: -webkit-gradient(linear, 40% 0%, 75% 84%, from(rgb(54, 168, 255)), color-stop(0.6, rgb(54, 229, 255)), to(rgb(54, 192, 255)));
        border-radius: 10px;
    }

    .assess-task::-webkit-scrollbar-track {
        /* -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.1); */
        background-color: #fff;
        border-radius: 10px;
    }
</style>
<style scoped>


    .ctpc-list-con .el-select {
        width: 188px;
    }

    .ctpc-tags {
        position: relative;
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

    .tag-lis-add {
        position: relative;
        margin-left: 10px;
        cursor: pointer;
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
        /*width: 80px;*/
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

    .ctpc-ins-edit .ctpc-btns {
        margin-top: 0;
    }
    .person-evaluate {

    }
    .person-evaluate.el-form-item {
        margin-bottom: 0px;
    }
    .el-form-item {
        margin-bottom: 0px;
    }
</style>
<style>
    .demo-table-expand {
        font-size: 0;
    }

    .demo-table-expand label {
        width: 60px;
        color: #99a9bf;
    }

    .demo-table-expand .el-form-item {
        margin-right: 0;
        margin-bottom: 0 !important;
        width: 50%;
    }

    .task-form {
        margin-bottom: 0;
    }

    .task-form-edit {
        margin-bottom: 10px;
    }

    .el-dialog__body {
        padding: 20px !important;
    }

    .member-time-week{
        width: 40px;
        border: 1px solid #ccc;
        height: 26px;
        border-radius: 4px;
        text-indent: 4px;
    }

    .trans {
        width: 0px;
        height: 0px;
        border-top: 15px solid rgb(32, 163, 191);
        border-left: 0px solid transparent;border-right: 15px solid transparent;
    }

    .el-tooltip__popper {
        max-width: 50%
    }
</style>

<template>
    <div>
        <div class="task-lis" v-for="task in taskItems" @click="taskItemClick(task.id)" :class="task.borderClass">
            <div class="head-img"><img src="../assets/img/project.png" alt="" class=""></div>
            <div class="main-task-detail">
                <div class="task-name">
                    <span v-if="isPrivate">
                        {{task.name}}
                        <span v-for="(item,index) in task.taskUsers" v-if="task.type==2">
                          <span v-if="item.userId == loginUserId">({{item.description}})</span>
                      </span>
                    </span>
                    <span v-else>{{task.name}}</span>
                </div>
                <div class="task-state">
                    <span class="task-end" :class="task.endColor">{{task.endText}}</span>
                    <span class="task-time-opt">
                    <i v-show="taskStatus=='TaskDoing'  && task.reviewStatus ==3" class="el-icon-circle-check" @click="showFinishedPop(task.id,task.taskUsers[0].id,task.type)"></i>
                    <i v-show="taskStatus=='TaskDoing' && task.type==1 && task.reviewStatus==1 " @click="modifyPrivateTask(task.id)" class="el-icon-edit" ></i>
                    <i v-show="taskStatus=='WaitAssess'" class="el-icon-star-off" @click="showWaitAssess(task.id)"></i>
                    <i v-show="taskStatus=='WaitAuditing'" class="el-icon-edit" @click="showAuditPop(task.id,task.taskUsers[0].id)"></i>
                        <!-- 审核通过 -->
                      <i v-show="taskStatus=='auditSuccess' " @click.stop="modifyPrivateTask(task.id)" class="el-icon-edit" ></i>

                  </span>
                    <ul class="task-key-tag">
                        <li class="task-key-lis" v-for="tag in task.tags">
                            <span class="circle" :style="{background:tag.colorValue}"></span>
                            <span class="task-key-msg">{{tag.name}}</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="task-data-show" v-show="isPrivate && task.status==3">
                <span class="task-score">+{{task.userIntegral}}</span>
                <span class="task-level first" v-show="task.type==2">{{task.integralGrade}}</span>
            </div>
            <div class="" v-show="!isPrivate && task.status==1">
                <span class="mark-stage">{{task.stageName}}</span>
            </div>
            <div class="task-mark">
                <img src="../assets/img/u431.png" alt="">
                <span class="mark-msg">{{task.projectName}}</span>
            </div>

            <div class="task-username" >{{task.userName}}</div>
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
                <el-form-item  class="task-form" label="任务名称：">{{taskDetail.name}}</el-form-item>
                <el-form-item  class="task-form" label="任务描述：">{{taskDetail.description}}</el-form-item>
                <el-form-item  class="task-form" label="项目：">{{taskDetail.projectName}}</el-form-item>
                <el-form-item  class="task-form" label="阶段：">{{taskDetail.stageName}}</el-form-item>
                <el-form-item  class="task-form" label="优先级："><span v-for="item in priorityList" v-if="item.value == taskDetail.priority">{{item.label}}</span>
                </el-form-item>
                <el-form-item  class="task-form" label="截止时间：">{{taskDetail.endTime | formatTime}}</el-form-item>
                <el-form-item  class="task-form" label="标签：">
                    <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
                        {{item.name}}
                    </el-tag>
                </el-form-item>
                <div  v-for="(item,index) in taskDetail.users">
                    <el-card class="box-card" v-if="item.userId==loginUserId">
                        <div class="text item">
                            工作量：{{item.taskHours}} 工时
                        </div>
                        <div class="text item" v-show="taskDetail.type==2">
                            截止：{{item.endTime | formatDate}}
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
                <el-form-item  class="task-form" label="任务名称：">{{taskDetail.name}}</el-form-item>
                <el-form-item  class="task-form" label="任务描述：">{{taskDetail.description}}</el-form-item>
                <el-form-item  class="task-form" label="项目：">{{taskDetail.projectName}}</el-form-item>
                <el-form-item  class="task-form" label="截止时间：">{{taskDetail.endTime | formatTime}}</el-form-item>
                <el-form-item  class="task-form" label="标签：">
                    <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
                        {{item.name}}
                    </el-tag>
                </el-form-item>
                <div v-for="(item,index) in taskDetail.users">
                    <el-form-item  class="task-form" label="工作量：">{{item.taskHours}} 工时</el-form-item>
                    <el-form-item  class="task-form" label="负责人：">{{item.userName}}</el-form-item>
                </div>
            </el-form>
            <span slot="footer" class="dialog-footer">
                 <el-tooltip content="删除该任务" placement="top">
                      <el-button type="danger" icon="delete" @click="deleteTask"></el-button>
                </el-tooltip>
                 <el-tooltip content="编辑该任务" placement="top">
                 <el-button type="primary" icon="edit" @click="modifyPrivateTask(taskDetail.id)"></el-button>
               </el-tooltip>
            <el-button type="success" @click="acceptTask">审核通过</el-button>
          </span>
        </el-dialog>
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
                <el-form-item  class="task-form" label="任务名称：">{{taskDetail.name}}</el-form-item>
                <el-form-item  class="task-form" label="任务描述：">{{taskDetail.description}}</el-form-item>
                <el-form-item  class="task-form" label="项目：">{{taskDetail.projectName}}</el-form-item>
                <el-form-item  class="task-form" label="阶段：">{{taskDetail.stageName}}</el-form-item>
                <el-form-item  class="task-form" label="优先级："><span v-for="item in priorityList" v-if="item.value == taskDetail.priority">{{item.label}}</span>
                </el-form-item>
                <el-form-item  class="task-form" label="截止时间：">{{taskDetail.endTime | formatTime}}</el-form-item>
                <el-form-item  class="task-form" label="标签：">
                    <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
                        {{item.name}}
                    </el-tag>
                </el-form-item>
                <div class="ctpc-member-con" v-if="taskDetail.type==2">
                    <div class="ctpc-member-list clearfix" :class="item.status>1?'done':'in'"
                         v-for="(item,index) in taskDetail.users">
                        <span class="fl ctpc-member-head">{{item.userName}}</span>
                        <span class="fl ctpc-member-job-time">工作量:{{item.taskHours}}工时</span>
                        <span class="fl ctpc-member-end-time">截止:{{item.endTime | formatDate}}</span>
                        <span class="fl ctpc-member-assess" v-show="item.commentGrade">评价：{{item.commentGrade}}</span>
                        <a href="javascript:;" v-show="taskDetail.status>1 && userRole===0 && item.status==3"
                           @click="commentDetail(item.id)">查看评价</a>
                    </div>
                    <div class="bdl-line"></div>
                </div>
                <div v-else="taskDetail.type==1" v-for="(item,index) in taskDetail.users">
                    <el-form-item  class="task-form" label="工作量：">{{item.taskHours}} 工时</el-form-item>
                    <el-form-item  class="task-form" label="负责人：">{{item.userName}}</el-form-item>
                </div>
            </el-form>

            <div class="trends" v-show="taskLog.list.length>0">
                <div class="trends-title clearfix">
                    <b class="fl">动态</b>
                    <a class="fr" href="javascript:;" @click="taskLogMore(taskDetail.id)" v-show="taskLog.hasNextPage">显示较早的动态</a>
                </div>
                <ul style="height: 100px; overflow: auto">
                    <li v-for="(item,index) in taskLog.list" :key="index">
                        {{item.title}}
                    </li>
                </ul>
            </div>
            <span slot="footer" class="dialog-footer" v-show="permit && taskDetail.status==1">
                <el-tooltip content="删除该任务" placement="top">
                      <el-button type="danger" icon="delete" @click="deleteTask" v-show="showDelete"></el-button>
                </el-tooltip>
                 <el-tooltip content="编辑该任务" placement="top">
                 <el-button type="primary" icon="edit" @click="modifyTask(taskDetail.id)"
                            v-show="taskDetail.createBy==loginUserId && taskDetail.type==2"></el-button>
               </el-tooltip>
                <el-button type="primary" icon="check" @click="completeTask"
                           v-show="taskDetail.status!=3 && userRole==0 && taskDetail.type==2">完成</el-button>
                <el-button type="primary" @click="showTaskDetail = false" v-show="taskDetail.status>1">确定</el-button>
          </span>
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
                            <el-form-item  class="task-form" label="请评价">
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
                <el-button type="primary" @click="taskAssess" v-show="!allComment">完成</el-button>
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
                <el-form-item  class="task-form" label="">
                    <span slot="label"><span class="star">*</span>任务名称</span>
                    <el-input v-model="modifyTaskForm.taskName" style="width: 300px"></el-input>
                </el-form-item>
                <el-form-item  class="task-form" label="">
                    <span slot="label"><span class="star">*</span>项目</span>
                    <el-select v-model="modifyTaskForm.projectId" placeholder="请选择">
                        <el-option
                                v-for="item in projectList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item  class="task-form" label="">
                    <span slot="label"><span class="star">*</span>优先级</span>
                    <el-select v-model="modifyTaskForm.priority" placeholder="请选择">
                        <el-option
                                v-for="item in priorityList"
                                :key="item.value"
                                :label="item.label"
                                :value="item.value">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item  class="task-form" label="">
                    <span slot="label"><span class="star">*</span>截止日期</span>
                    <el-date-picker
                            v-model="modifyTaskForm.endTime"
                            type="datetime"
                            format="yyyy-MM-dd HH:mm"
                            :picker-options="pickerOptions0"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item  class="task-form" label="">
                    <span slot="label"><span class="star">*</span>阶段</span>
                    <el-select v-model="modifyTaskForm.stageId" :multiple-limit="1" placeholder="请选择">
                        <el-option v-for="item in stageList" :key="item.id"
                                   :label="item.name" :value="item.id"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item  class="task-form" label="">
                    <span slot="label"><span class="star">*</span>标签</span>
                   <!--  <div class="fl tag-name clearfix">
                        <el-button class="fl" size="small" v-for="item in tagList" @click="addFormTagId(item.id,2,$event)">{{item.name}}</el-button>
                     </div> -->
                    <el-select
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
                <el-form-item  class="task-form" label="">
                    <span slot="label"><span class="star">*</span>任务描述</span>
                    <el-input type="textarea" v-model="modifyTaskForm.description" :rows="3"></el-input>
                </el-form-item>
            </el-form>
            <div class="ctpc-member-con">
                <div class="ctpc-member-list clearfix" :class="[item.status>1?'done':'in',item.cssClass]"
                     v-for="(item,index) in modifyTaskForm.taskUsers">
                    <span class="fl ctpc-member-head">{{item.userName}}</span>
                    <span class="fl ctpc-member-job-time">工作量:{{item.taskHours}}工时</span>
                    <span class="fl ctpc-member-end-time">截止:{{item.endTime | formatDate}}</span>
                    <span style="position: absolute;right: 10px;">
                        <el-button type="text" icon="edit"
                                   @click="modifyStep(index,modifyTaskForm.taskUsers)"></el-button>
                    <el-button type="text" icon="close" @click="deleteMember(index)"></el-button>
                    </span>
                </div>
                <div class="bdl-line"></div>
            </div>
            <div class="ctpc-add-member-detail" v-if="showAddDetail">
                <el-input type="textarea" placeholder="描述该成员的工作内容..." v-model="step.description"
                          :rows="3"></el-input>
                <div class="add-member-basic">
                    <div class="add-member-basic-list clearfix">
                        <div class="add-member-basic-menu fl"><span class="star">*</span>姓名：</div>
                        <div class="add-member-basic-msg fl">
                            <el-select v-model="step.userId" placeholder="请选择" @change="stepUserChange">
                                <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                           :value="item.id"></el-option>
                            </el-select>
                        </div>
                        <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>工作量：
                        </div>
                        <div class="add-member-basic-msg fl">
                            <!--<input class="member-time-count" v-model="step.taskHours">工时-->
                            <el-input v-model="step.taskHours" style="width: 70px"></el-input>
                            工时
                        </div>
                    </div>
                    <div class="add-member-basic-list clearfix">
                        <div class="add-member-basic-menu fl"><span class="star">*</span>开始日期：</div>
                        <div class="add-member-basic-msg fl">
                            <el-date-picker v-model="step.beginTime" format="yyyy-MM-dd HH:mm" type="datetime"
                                            placeholder="选择日期"
                                            :picker-options="pickerOptions0"></el-date-picker>
                        </div>
                        <div class="add-member-basic-menu add-member-basic-end fl"><span class="star">*</span>截止日期：
                        </div>
                        <div class="add-member-basic-msg fl">
                            <el-date-picker v-model="step.endTime" type="datetime" format="yyyy-MM-dd HH:mm"
                                            placeholder="选择日期"
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
            <!--   <span slot="footer" class="dialog-footer">
               <el-button @click="hideTaskModify">取 消</el-button>
               <el-button type="primary" @click="saveTaskInfo">保 存</el-button>
             </span>-->

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
                    <el-form-item  class="task-form" label="姓名">
                        <span>{{ item.commentUserName }}</span>
                    </el-form-item>
                    <el-form-item  class="task-form" label="评价">
                        <span>{{ item.grade }}</span>
                    </el-form-item>
                    <el-form-item  class="task-form" label="描述">
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
                <el-form-item  class="task-form" label="项目">
                    <el-select v-model="modifyPrivateTaskForm.projectId" placeholder="请选择">
                        <el-option
                                v-for="item in projectList"
                                :key="item.id"
                                :label="item.name"
                                :value="item.id">
                        </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item  class="task-form" label="截止日期">
                    <el-date-picker
                            v-model="modifyPrivateTaskForm.endTime"
                            type="datetime"
                            :picker-options="pickerOptions0"
                            format="yyyy-MM-dd HH:mm"
                            placeholder="选择日期时间">
                    </el-date-picker>
                </el-form-item>
                <el-form-item  class="task-form" label="工作量">
                    <el-input style="width:100px" v-model="modifyPrivateTaskForm.taskHours" :maxlength="2"></el-input>
                    小时
                </el-form-item>
                <el-form-item  class="task-form" label="任务名称">
                    <el-input v-model="modifyPrivateTaskForm.taskName"></el-input>
                </el-form-item>

                <el-form-item  class="task-form" label="任务描述">
                    <el-input type="textarea" v-model="modifyPrivateTaskForm.description" :rows="3"></el-input>
                </el-form-item>
                <el-form-item  class="task-form" label="阶段">
                    <el-select
                            v-model="modifyPrivateTaskForm.stageId"
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
                <el-form-item  class="task-form" label="标签">
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
    </div>
</template>
<script>
    import http from '../lib/Http'
    import moment from 'moment';
    import helper from '../lib/Helper'

    moment.locale('zh-cn');
    export default {
        name: 'TaskItem',
        props: {
            taskItems: Array,
            taskStatus: String,
            isPrivate: Boolean
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
                showFinishedTask: false,
                showAuditTask: false,
                showTaskDetail: false,
                showTaskComment: false,
                showTaskModify: false,
                showAddDetail: false,
                showTaskCommentDetail: false,
                taskCommentDetail: {},
                showModifyPrivateTask: false,
                modifyPrivateTaskForm: {
                    id: '',
                    userId:'',
                    taskType: 1,
                    priority: 1,
                    taskName: '',
                    description: '',
                    projectId: '',
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
                    comments: []
                },
                commentStages: [],
                allComment: false,
                taskDetail: {},
                taskLog: {
                    list: [],
                    hasNextPage: false,
                    pageNum: 1
                },
                modifyTaskForm: {
                    taskName: '',
                    description: '',
                    projectId: '',
                    endTime: '',
                    priority: '',
                    tags: [],
                    taskType: 2,
                    stageId: '',
                    taskUsers: []
                },
                priorityList: [
                    {label: '普通', value: 1},
                    {label: '紧急', value: 2},
                    {label: '非常紧急', value: 3},
                ],
                step: {
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
                },
                stepTemp: {},
                projectList: [],
                stageList: [],
                tagList: [],
            };
        },
        created() {
            this.fetchProjectList()
            this.fetchStageList()
            this.fetchTagList()
            this.fetchUserList()
            this.loginUserId = helper.decodeToken().userId
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
            }
        },
        filters: {
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD');
            },
            formatTime: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD HH:mm');
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
                })
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
            acceptTask() {
                http.zsyPutHttp(`task/auditing/accept/${this.auditForm.taskId}`, {}, (resp) => {
                    this.$message.success("任务审核成功");
                    this.$emit('reload');
                    this.auditForm.taskId = '';
                    this.auditForm.taskUserId = '';
                })
                this.showAuditTask = false;
                this.taskDetail = {};
            },
            // 打回任务
            rejectTask() {
                http.zsyPutHttp(`task/auditing/reject/${this.auditForm.taskId}`, {}, (resp) => {
                    this.$message.success("任务打回成功");
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
                        this.$message.success("操作成功");
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
            taskItemClick(taskId) {
              if(!this.isPrivate || this.taskStatus=='finished' || this.taskStatus=='auditSuccess'){
                    this.showTaskDetail = true;
                    http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                        this.taskDetail = resp.data
                    });
                    this.getTaskLog(taskId)
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
            completeTask() {
                this.$confirm('此操作将完成该任务, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyPutHttp(`/task/complete/master/${this.taskDetail.id}`, {}, (resp) => {
                        this.$emit('reload');
                        this.$message.success("操作成功");
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
                        this.$message.success("删除成功");
                        this.hideTaskDetail();
                        this.showAuditTask = false;
                        this.taskDetail = {};
                    })
                }).catch(() => {
                });
            },
            showWaitAssess(taskId) {
                let vm = this
                http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    vm.assessForm.taskId = taskId;
                    let users = [];
                    // 过滤我的任务
                    resp.data.users.forEach((user) => {
                        if (user.userId != vm.loginUserId) {
                            users.push(user);
                        }
                    })
                    // 查询我的评价
                    users.forEach((user) => {
                        user.comments.forEach((comment) => {
                            if (comment.createBy == vm.loginUserId) {
                                user.myComment = comment;
                                return;
                            }
                        });
                    })
                    for (let i = 0; i < users.length; i++) {
                        if (!users[i].myComment) {
                            vm.assessForm.comments.push({
                                'taskUserId': users[i].id,
                                'description': '',
                                'grade': ''
                            })
                        }
                    }
                    let myComments = 0;
                    users.forEach((stage) => {
                        if (stage.myComment) {
                            myComments++
                        }
                    })
                    vm.allComment = (myComments == users.length);
                    vm.commentStages = users;
                })

                this.showTaskComment = true;
            },
            hideWaitAssess() {
                this.showTaskComment = false;
                this.commentStages = [];
                this.assessForm = {
                    taskId: '',
                    comments: []
                };
            },
            // 评价任务
            taskAssess() {
                http.zsyPostHttp('/task/comment', this.assessForm, (resp) => {
                    this.$message.success("评价成功");
                    this.showTaskComment = false
                    this.commentStages = []
                    this.assessForm = {
                        taskId: '',
                        comments: []
                    };
                    this.$emit('reload');
                })
            },
            // 修改单人任务
            modifyPrivateTask(taskId) {
                this.showModifyPrivateTaskDialog(taskId);
            },
            // 保存修改单人任务
            saveModifyPrivateTaskForm() {
                if (this.modifyPrivateTaskForm.projectId == '') {
                    this.$message.warning("请选择项目");
                    return;
                }
                if (this.modifyPrivateTaskForm.endTime == '') {
                    this.$message.warning("请选择结束时间");
                    return;
                }
                if (this.modifyPrivateTaskForm.taskHours== '') {
                    this.$message.warning("请输入工作量");
                    return;
                }
                if (this.modifyPrivateTaskForm.taskName.trim() == '') {
                    this.$message.warning("请填写任务名称");
                    return;
                }
                if (this.modifyPrivateTaskForm.description.trim() == '') {
                    this.$message.warning("请填写任务备注");
                    return;
                }

                if (this.modifyPrivateTaskForm.stageId === '') {
                    this.$message.warning("请选择项目阶段");
                    return;
                }
                if (this.modifyPrivateTaskForm.tags.length == 0) {
                    this.$message.warning("请选择至少一项标签");
                    return;
                }
                this.modifyPrivateTaskForm.taskName = this.modifyPrivateTaskForm.taskName.trim();
                this.modifyPrivateTaskForm.endTime = moment(this.modifyPrivateTaskForm.endTime).format('YYYY-MM-DD HH:mm:ss')
                this.modifyPrivateTaskForm.taskUsers = [{
                    userId: this.modifyPrivateTaskForm.userId,
                    taskHours: this.modifyPrivateTaskForm.taskHours,
                    beginTime: moment().format('YYYY-MM-DD HH:mm:ss'),
                    endTime: this.modifyPrivateTaskForm.endTime,
                    description: this.modifyPrivateTaskForm.description.trim()
                }]
                let vm = this;
                http.zsyPutHttp(`/task/modify/${this.modifyPrivateTaskForm.id}`, this.modifyPrivateTaskForm, (resp) => {
                    vm.$message.success('任务修改成功');
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
                    userId:'',
                    taskType: 1,
                    priority: 1,
                    taskName: '',
                    description: '',
                    projectId: '',
                    endTime: '',
                    tags: [],
                    taskHours: '',
                    stageId: ''
                };
            },
            // 修改任务
            modifyTask(taskId) {
                this.hideTaskDetail();
                this.showTaskModify = true
                http.zsyGetHttp(`/task/detail/${taskId}`, {}, (resp) => {
                    this.modifyTaskForm.id = resp.data.id;
                    this.modifyTaskForm.taskName = resp.data.name;
                    this.modifyTaskForm.description = resp.data.description;
                    this.modifyTaskForm.endTime = resp.data.endTime;
                    this.modifyTaskForm.projectId = resp.data.projectId;
                    this.modifyTaskForm.stageId = resp.data.stageId;
                    this.modifyTaskForm.priority = resp.data.priority;
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
                    status: stages[index].status
                }
                this.step = stages[index];
                this.step.index = index;
                this.modifyTaskForm.taskUsers.forEach((item) => {
                    item.cssClass = ''
                })
                this.modifyTaskForm.taskUsers[index].cssClass = 'stepActive'
                this.showAddDetail = true;
            },
            hideTaskModify() {
                this.modifyTaskForm.taskName = '';
                this.modifyTaskForm.description = '';
                this.modifyTaskForm.taskName = '';
                this.modifyTaskForm.endTime = '';
                this.modifyTaskForm.projectId = '';
                this.modifyTaskForm.stageId = '';
                this.modifyTaskForm.priority = 1;
                this.modifyTaskForm.tags = [];
                this.modifyTaskForm.taskUsers = [];
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
                    taskHours: '',
                    beginTime: '',
                    endTime: '',
                    description: '',
                    completeHours: '',
                    completeTime: '',
                    status: ''
                }
            },
            saveAddMember() {
                const valid =
                    this.step.userId == '' ||
                    this.step.taskHours == '' ||
                    this.step.beginTime == '' ||
                    this.step.endTime == '' ||
                    this.step.description == '';
                if (valid) {
                    this.$message.warning('请将阶段填写完整');
                    return
                }
                if (this.step.index === '') {
                    let taskUser = {};
                    taskUser.userId = this.step.userId;
                    taskUser.userName = this.step.userName;
                    taskUser.beginTime = this.step.beginTime;
                    taskUser.endTime = this.step.endTime;
                    taskUser.taskHours = this.step.taskHours;
                    taskUser.description = this.step.description;
                    this.modifyTaskForm.taskUsers.push(taskUser);
                } else {
                    // 取消css
                    this.modifyTaskForm.taskUsers[this.step.index].cssClass = '';
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
                if (this.modifyTaskForm.description == '') {
                    this.$message.warning("请填写任务备注");
                    return;
                }
                if (this.modifyTaskForm.taskName == '') {
                    this.$message.warning("请填写任务名称");
                    return;
                }
                if (this.modifyTaskForm.projectId == '') {
                    this.$message.warning("请选择项目");
                    return;
                }
                if (this.modifyTaskForm.endTime == '') {
                    this.$message.warning("请选择结束时间");
                    return;
                }
                if (this.modifyTaskForm.stageId === '') {
                    this.$message.warning("请选择项目阶段");
                    return;
                }
                if (this.modifyTaskForm.tags.length == 0) {
                    this.$message.warning("请选择至少一项标签");
                    return;
                }
                let param = this.modifyTaskForm;
                param.taskName = param.taskName.trim()
                param.description = param.description.trim()
                param.taskUsers.forEach((user) => {
                    user.description = user.description.trim()
                    user.beginTime = moment(user.beginTime).format('YYYY-MM-DD HH:mm:ss')
                    user.endTime = moment(user.endTime).format('YYYY-MM-DD HH:mm:ss')
                })
                param.endTime = moment(param.endTime).format('YYYY-MM-DD HH:mm:ss');
                let vm = this;
                http.zsyPutHttp(`/task/modify/${this.modifyTaskForm.id}`, param, (resp) => {
                    vm.$message.success('任务修改成功');
                    this.hideTaskModify()
                    vm.$emit('reload')
                })
                this.showCreateTask = false;
            },
            hideTaskCommentDetail() {
                this.showTaskCommentDetail = false
            },
            commentDetail(taskUserId) {
                let vm = this;
                this.taskDetail.users.forEach((user) => {
                    if (user.id == taskUserId) {
                        vm.taskCommentDetail = user
                        return
                    }
                })
                this.showTaskCommentDetail = true
            }
        }
    }
</script>
<style>
    .myDialog {
        width: 600px;
    }

    .my-dialog-title {
        font-size: 16px;
        font-weight: bold;
    }

    .my-dialog-title-tool {
        float: right;
    }
    .red-border{
        border-left: red 3px solid;
    }
    .orange-border{
        border-left: orange 3px solid;
    }
    .task-username{
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
        margin:15px 0;
        padding-left: 10px; /* border-left: 1px solid #ccc; */
        margin-left: 6px;
        position: relative;
    }
    .trends{
        /*background-color: #f2f2f2; */
       /*padding-left: 10px;*/
       line-height: 30px;
       border:1px solid #e4e8f1;
        
    }
    .trends ul{
        padding-left: 30px;
        list-style: circle;
    }
    .trends li{
        list-style: circle!important;
    }
    .trends-title{
        padding:0 10px;
        line-height: 30px;
       background-color: #e4e8f1;
    }
   .trends-title a{
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
        margin-left:30px;
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

    .add-stage-opt {
        color: #36A8FF;
        cursor: pointer;
        width: 400px;
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

    .ctpc-member-delete {
        font-size: 26px;
        cursor: pointer;
        margin-right: 4px;
        position: absolute;
        right: 1px;
    }

    .add-member-stage {
        position: relative;
    }

    .ctpc-ins-edit .ctpc-btns {
        margin-top: 0;
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
        margin-bottom: 0!important;
        width: 50%;
    }
    .task-form{
        margin-bottom: 0;
    }
    .el-dialog__body{
        padding:20px!important;
    }
</style>

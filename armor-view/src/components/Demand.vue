<template>
    <div class="demand-con">
      <div>
        <div class="demand-top clearfix">
          <div class="clearfix">
            <div class="demand-top-list fl">
                <span class="ttl-name">负责人:</span>
              <el-select clearable filterable @change="fetchFeedbackList" v-model="feedbackList.user" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                <el-option v-for="item in adminList" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
              </el-select>
                       <!--    @click="addFormTagId(item.id,1,$event)"-->
            </div>
            <div class="demand-top-list fl">
              <span class="ttl-name" style="margin-left: -15px;">需求来源:</span>
              <el-select clearable filterable @change="fetchFeedbackList" v-model="feedbackList.origin" placeholder="请选择" size="small" style="width:100px;margin-left: -50px">
                <el-option v-for="item in originList" :key="item" :label="item"
                           :value="item"></el-option>
              </el-select>
            </div>
            <div class="demand-top-list fl">
              <span class="ttl-name" style="margin-left: -15px;">状态:</span>
              <div class="fr tag-name" style="margin-left: -45px;">
                <el-button class="fl" type="" size="small" v-for="item in statusList" :key="item.id"
                           @click="addFormTerm(item.id,1,$event)" :class="feedbackList.status==item.id?'active':''">{{item.name}}
                </el-button>
              </div>
            </div>
            <div class="demand-top-list fl">
              <span class="ttl-name">优先级:</span>
              <div class="fr tag-name" style="margin-left: -45px;">
                <el-button class="fl" type="" size="small" v-for="item in priorityListDesk" :key="item.id"
                           @click="addFormTerm(item.id,2,$event)" :class="feedbackList.priority==item.id?'active':''">{{item.name}}
                </el-button>
              </div>
            </div>
            <el-button type="primary" size="small" @click="feedbackVisible=true,clearFeedbackForm()" v-show="permit">提需求</el-button>
          </div>

        </div>

        <el-table :data="demandData" border>
          <el-table-column prop="no" label="序号" align="center" width="80"></el-table-column>
          <el-table-column prop="title" label="需求名称" align="center" width="250">
            <template scope="scope">
              <a style="color:#20a0ff;cursor: pointer;" @click="feedbackEdit(scope.row)">{{scope.row.title}}</a>
            </template>
          </el-table-column>
          <el-table-column prop="origin" label="需求来源" align="center" width="100"></el-table-column>
          <el-table-column prop="priority" label="优先级" align="center" width="100">
            <template scope="scope">
              <div type="text" v-for="item in priorityList" v-if="item.id == scope.row.priority">{{item.name}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="feedbackTime" label="提出日期" align="center" width="150">
            <template scope="scope">
              <div type="text" >{{scope.row.feedbackTime | formatDate}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" align="center" >
            <template scope="scope">
              <div type="text" v-for="item in statusList" v-if="item.id == scope.row.status">{{item.name}}</div>
            </template>
          </el-table-column>
          <el-table-column prop="taskNo" label="任务" align="center" width="80"></el-table-column>
          <el-table-column prop="users" label="负责人" align="center" width="150"></el-table-column>
          <el-table-column  label="操作" align="center" >
            <template scope="scope">
              <el-button @click="feedbackPlan(scope.row)" type="text" size="small" v-show="permit || scope.row.taskNo!=0">计划</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="pagination">
          <el-pagination
                  @current-change="handleCurrentChange"
                  :current-page.sync="feedbackPage.pageNum"
                  :page-size="feedbackPage.pageSize"
                  :layout="feedbackPageLayout()"
                  :total="feedbackPage.total">
          </el-pagination>
        </div>

      </div>

      <el-dialog :visible.sync="feedbackVisible" title="需求描述" size="tiny" custom-class="myDialog" :close-on-click-modal="false" :close-on-press-escape="false">
        <el-form :model="feedbackForm" ref="feedbackForm" :rules="rules" label-width="110px" :inline="true" >
          <el-form-item label="需求名称:" prop="title"  >
            <el-input type="text" v-model="feedbackForm.title" ></el-input>
          </el-form-item>
          <el-form-item label="需求来源:" prop="origin">
            <el-input v-model="feedbackForm.origin"></el-input>
          </el-form-item>
          <el-form-item label="提出日期:" prop="feedbackTime"  >
            <el-date-picker
                    v-model="feedbackForm.feedbackTime"
                    type="date"
                    format="yyyy-MM-dd"
                    placeholder="选择日期时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="项目:" prop="projectId" >
            <el-select v-model="feedbackForm.projectId" placeholder="请选择">
              <el-option
                      v-for="item in projectList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="优先级:" prop="priority">
            <el-select v-model="feedbackForm.priority" placeholder="请选择">
              <el-option
                      v-for="item in priorityList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="原始需求:" prop="content">
              <quill-editor v-model="feedbackForm.content" ref="myQuillEditor" :options="editorOption" style="width:440px;height: 140px;padding-bottom:30px;">
              </quill-editor>
              <el-upload
                      class="avatar-uploader"
                      action=""
                      :show-file-list="false"
                      :http-request="upload"
                      :before-upload="beforeAvatarUpload">
              </el-upload>
          </el-form-item>
          <div class="el-dialog__footer" style=" margin-top: 30px;margin-right: 20px;">
            <el-button type="danger" @click="deleteFeedback()"v-show="feedbackDeleteIcon">删除需求</el-button>
            <el-button @click="saveFeedback('feedbackForm')" type="primary" :loading="isSaving">保存</el-button>
          </div >
        </el-form>
      </el-dialog>

      <el-dialog :visible.sync="feedbackDetailVisible" title="需求描述" size="tiny" custom-class="myDialog" :close-on-click-modal="false" :close-on-press-escape="false">
        <div class="ctpc-list clearfix">
          <div class="ctpc-list-menu fl"><span class="star">*</span>名称：</div>
          <div class="ctpc-list-con fl" >{{feedbackForm.title}}</div>
        </div>
        <div class="ctpc-list clearfix">
          <div class="ctpc-list-menu fl"><span class="star">*</span>项目：</div>
          <div class="ctpc-list-con fl" v-for="item in projectList" v-if="item.id == feedbackForm.projectId">{{item.name}}
          </div>
        </div>
        <div class="ctpc-list clearfix">
          <div class="ctpc-list-menu fl"><span class="star">*</span>原始需求：</div>
          <div class="ctpc-list-con fl">
            <quill-editor v-model="feedbackForm.content" ref="myQuillEditor" :options="editorOption1" style="width:350px;height: 120px;margin-bottom: 40px;">
            </quill-editor>
          </div>
        </div>
      </el-dialog>

      <el-dialog :visible.sync="planVisible" custom-class="myDialog"  title="计划" size="tiny"
                 :close-on-click-modal="false" :close-on-press-escape="false">
        <div class="ctpc-list clearfix">
          <div class="ctpc-list-menu fl"><span class="star">*</span>项目：</div>
          <div class="ctpc-list-con fl" v-for="item in projectList" v-if="item.id == feedbackForm.projectId">{{item.name}}</div>
        </div>
        <div class="ctpc-list clearfix">
          <div class="ctpc-list-menu fl"><span class="star">*</span>优先级</div>
          <div class="ctpc-list-con fl" v-for="item in priorityList" v-if="item.id == feedbackForm.priority">{{item.name}}
          </div>
        </div>
        <div class="ctpc-list clearfix">
          <div class="ctpc-list-menu fl"><span class="star">*</span>预计开始时间:</div>
          <div class="ctpc-list-con fl">
            <div v-if="!permit || feedbackForm.status==2" >{{feedbackPlanForm.expectStartTime|formatDate}}</div>
            <el-date-picker v-model="feedbackPlanForm.expectStartTime" type="date" format="yyyy-MM-dd" v-else=" "
                            placeholder="选择日期"></el-date-picker>
          </div>
        </div>
        <div class="ctpc-list clearfix">
          <div class="ctpc-list-menu fl"><span class="star">*</span>预计上线时间:</div>
          <div class="ctpc-list-con fl">
            <div v-if="!permit || feedbackForm.status==2" >{{feedbackPlanForm.expectOfficialTime|formatDate}}</div>
            <el-date-picker v-model="feedbackPlanForm.expectOfficialTime" type="date" format="yyyy-MM-dd" v-else=""
                            placeholder="选择日期"></el-date-picker>
          </div>
        </div>
        <div class="ctpc-member-con">
          <div class="ctpc-member-list clearfix" v-for="(item,index) in planTask"
               :class="[item.status>1?'done':'in',item.cssClass]">
            <span class="fl ctpc-member-head" >{{item.userName}}</span>
            <a class="fl ctpc-member-job-time" @click="directPlanTask(item.id)" style="color:black; cursor: pointer;">{{item.taskName|StringExtract}}</a>
            <span class="fl ctpc-member-end-time">截止:{{item.endTime|formatDate}}</span>
            <!--<span style="position: absolute;right: 10px;">-->
                                <!--<el-button type="text" icon="edit" @click="modifyStep(index,planTask)"></el-button>-->
                            <!--<el-button type="text" icon="close" @click="deleteTaskMember(index)"></el-button>-->
                            <!--</span>-->
          </div>
          <div class="bdl-line"></div>
        </div>
        <div class="ctpc-add-member-detail" v-if="showTaskDetail">
          <div class="add-member-basic">
            <div class="add-member-basic-list clearfix">
              <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>任务名称：</div>
              <div class="add-member-basic-msg">
                <input class="member-time-count" v-model="taskStep.taskName" @change=""  style="width:300px">
              </div>
              <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>任务描述：
              </div>
              <div class="add-member-basic-msg ">
                <el-input type="textarea" placeholder="添加任务描述" v-model="taskStep.description"
                          :rows="2" style="width: 300px"></el-input>
              </div>
              <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>负责人：
              </div>
              <div class="add-member-basic-msg fl" >
                <el-select v-model="taskStep.createBy" clearable placeholder="请选择">
                  <el-option v-for="item in adminList" :key="item.id" :label="item.name"
                             :value="item.id"></el-option>
                </el-select>
              </div>
              <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>截止日期：
              </div>
              <div class="add-member-basic-msg fl">
                <el-date-picker v-model="taskStep.endTime" type="date" format="yyyy-MM-dd"
                                placeholder="选择日期"></el-date-picker>
              </div>
              <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>难易度：
              </div>
              <div class="add-member-basic-msg fl">
                <el-select v-model="taskStep.facility" clearable placeholder="请选择">
                  <el-option
                          v-for="item in facilityList"
                          :key="item.value"
                          :label="item.label"
                          :value="item.value">
                  </el-option>
                </el-select>
              </div>
              <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>阶段：
              </div>
              <div class="add-member-basic-msg fl">
                <el-select v-model="taskStep.stageId" :multiple-limit="1"
                           placeholder="请选择">
                  <el-option v-for="item in stageList" :key="item.id"
                             :label="item.name" :value="item.id"></el-option>
                </el-select>
              </div>
              <div class="add-member-basic-menu add-member-basic-time fl"><span class="star">*</span>标签：
              </div>
              <div class="add-member-basic-msg fl">
                <ul class="ctpc-tags">
                  <li class="tag-lis-add">
                    <div class="tag-add-sel">
                      <el-select v-model="taskStep.tags"  multiple placeholder="添加标签">
                        <el-option v-for="item in tagList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                      </el-select>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div class="ctpc-btns">
            <input type="button" class="ctpc-cancel" @click="cancelAddTask" value="取消">
            <input type="button" class="ctpc-save" @click="saveAddTask" value="确定" >
          </div>
        </div>
        <div v-if="!permit ||feedbackForm.status==2 "></div>
        <div v-else="">
          <div class="add-member-opt" v-show="!showTaskDetail" @click="addTask">
            <span class="add-member-icon" >+</span>
            <span class="add-member-msg" style="margin-top: -40px;margin-left: 24px;">添加任务</span>
          </div>
          <el-button type="text" icon="check" @click="savePlan" style="margin-left: 400px;" :loading="isSaving">保存计划</el-button>
        </div>
      </el-dialog>

    </div>
</template>
<script>
    import http from '../lib/Http'
    import moment from 'moment';
    import ElButton from "../../node_modules/element-ui/packages/button/src/button";
    import ElDialog from "../../node_modules/element-ui/packages/dialog/src/component";
    import helper from '../lib/Helper'
    import 'quill/dist/quill.core.css'
    import 'quill/dist/quill.snow.css'
    import 'quill/dist/quill.bubble.css'

    import { quillEditor } from 'vue-quill-editor'

    moment.locale('zh-cn');
    export default {
        components: {
            ElDialog,
            quillEditor,
            ElButton},
        name: 'Demand',
        data() {
            var validateEmpty = (rule, value, callback) => {
                if (value.trim() == '') {
                    callback(new Error());
                } else {
                    callback();
                }
            };
            return {
                isSaving:false,
                demandData:[],
                feedbackVisible:false,
                feedbackDeleteIcon:false,
                feedbackDetailVisible:false,
                planVisible:false,
                showTaskDetail:false,
                feedbackForm: {
                    id:'',
                    title: '',
                    origin:'',
                    projectId:'',
                    priority:1,
                    feedbackTime:'',
                    createBy:'',
                    content:'',
                    status:''
                },
                taskStep:{
                    index: '',
                    taskType:2,
                    name: '',
                    description: '',
                    projectId:'',
                    createBy:'',
                    endTime: '',
                    stageId: '',
                    priority: '',
                    facility:'',
                    tags: []
                },
                stepTemp:{},
                planTask:[],
                feedbackPlanForm:{
                    id:'',
                    projectId:'',
                    feedbackId:'',
                    expectStartTime:'',
                    expectOfficialTime:''
                },
                feedbackList:{
                    user:'',
                    origin:'',
                    status:-1,
                    priority:0,
                    pageNum:1
                },
                feedbackPage:{
                    pageSize:10,
                    total:0
                },
                adminList:[],
                adminId:'',
                rules: {
                    origin: [
                        {required: true, validator: validateEmpty, message: '需求来源不能为空', trigger: 'blur'},
                    ],
                    title: [
                        {required: true, validator: validateEmpty, message: '需求名称不能为空', trigger: 'blur'},
                        {min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur'}
                    ],
                    content: [
                        {required: true, validator: validateEmpty, message: '原始需求不能为空', trigger: 'blur'},
                    ],
                },
                projectList:[],
                statusList:[
                    {id:-1,name:'全部'},
                    {id:0,name:'待处理'},
                    {id:1,name:'已计划'},
                    {id:2,name:'已完成'},
                ],
                priorityList:[
                    {id:1,name:'普通'},
                    {id:2,name:'紧急'},
                    {id:3,name:'非常紧急'},
                ],
                priorityListDesk:[
                    {id:0,name:'全部'},
                    {id:1,name:'普通'},
                    {id:2,name:'紧急'},
                    {id:3,name:'非常紧急'},
                ],
                facilityList: [
                    {label: '容易', value: 1},
                    {label: '简单', value: 2},
                    {label: '一般', value: 3},
                    {label: '较难', value: 4},
                    {label: '困难', value: 5},
                ],
                stageList:[],
                tagList:[],
                originList:[],
                content :'',    // 文章内容
                editorOption:{
                    placeholder: '',
                    theme: 'snow',  // or 'bubble'
                    modules: {
                        toolbar: {
                            container: [['bold', 'italic', 'underline', 'strike'],        // toggled buttons
                                                    ['blockquote', 'code-block'],
                                        [{'list': 'ordered'}, {'list': 'bullet'}],
                                        [{'script': 'sub'}, {'script': 'super'}],      // superscript/subscript
                                        [{'indent': '-1'}, {'indent': '+1'}],
                                         [{'color': []}, {'background': []}],          // dropdown with defaults from theme
                                        [{'size': ['small', false, 'large', 'huge']}],  // custom dropdown
                                        [{'header': [1, 2, 3, 4, 5, 6, false]}],
                                        [{'align': []}],
                                        [ 'image'] ], // 工具栏
                            handlers: {
                                'image': function (value) {
                                    if (value) {
                                        document.querySelector('.avatar-uploader input').click()
                                    } else {
                                        this.quill.format('image', false);
                                    }
                                }
                            }
                        }
                    }
                },    // 编辑器选项
                editorOption1:{
                    placeholder: '',
                    theme: 'snow',  // or 'bubble'
                    modules: {
                        toolbar: {
                            container: [['bold', 'italic', 'underline', 'strike'],] // 工具栏
                        }
                    },
                    readOnly:true
                },
                addRange: {},
                uploadData:'',
                photoUrl :'',         // 上传图片地址
                uploadType: '',    // 上传的文件类型（图片、视频）
                fullscreenLoading : false,
                $refs: {
                    myQuillEditor: '',
                    imgInput: ''
                }
            }
        },
        beforeMount:function () {
            //选中任务tab
            this.$root.eventBus.$emit("handleTabSelected", "demand");
            this.fetchProjectList();
            this.fetchUserList();
            this.fetchStageList();
            this.fetchTagList();
            this.fetchFeedbackList();
            this.fetchOriginList();
        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole < 2;
            },
            getUserName(){
                let userName = helper.decodeToken().userName;
                return userName;
            }
        },
        filters: {
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY年MM月DD日');
            },
            StringExtract:function (name) {
                if(name.length>20){
                  return name.substring(0,20)+'...';
                }
                return name;
            },
        },
        methods: {
            clearFeedbackForm(){
                this.feedbackForm.id = ''
                this.feedbackForm.feedbackTime=''
                this.feedbackForm.projectId=''
                this.feedbackForm.priority=''
                this.feedbackForm.origin=''
                this.feedbackForm.title=''
                this.feedbackForm.content=''
                this.feedbackForm.createBy=''
                this.feedbackForm.status=''
            },
            feedbackPlan(feedback){
                this.clearPlan();
                this.planVisible=true
                this.feedbackPlanForm.feedbackId = feedback.id
                this.feedbackPlanForm.projectId = feedback.projectId
                this.feedbackForm.projectId =feedback.projectId
                this.feedbackForm.priority =feedback.priority
                this.feedbackForm.status = feedback.status
                http.zsyGetHttp('/feedback/getPlan/'+feedback.id, {}, (resp) => {
                    if(resp.data.planTask.length!=0){
                        this.feedbackPlanForm.id = resp.data.id
                        this.feedbackPlanForm.expectOfficialTime =resp.data.expectOfficialTime
                        this.feedbackPlanForm.expectStartTime =resp.data.expectStartTime
                        this.planTask = resp.data.planTask
                    }
                })
            },
            addTask(){
                this.showTaskDetail = true
            },
            fetchUserList() {
                let vm = this
                http.zsyGetHttp('/user/manager', {}, (resp) => {
                    vm.adminList = resp.data
                })
            },
            fetchProjectList() {
                http.zsyGetHttp('/project/list', {}, (resp) => {
                    this.projectList = resp.data
                })
            },
            fetchOriginList() {
                http.zsyGetHttp('/feedback/originList', {}, (resp) => {
                    this.originList = resp.data
                })
            },
            addFormTerm(id, num, $event) {
                  if (num == 1) {
                      if(id !=this.feedbackList.status){
                          this.feedbackList.status=id;
                          this.fetchFeedbackList();
                      }
                  } else if (num == 2) {
                      if(id !=this.feedbackList.priority){
                          this.feedbackList.priority=id;
                          this.fetchFeedbackList();
                      }
                  }
            },
            feedbackEdit(feedback){
                if(this.permit){
                    this.clearFeedbackForm();
                    this.feedbackVisible = true;
                    this.feedbackDeleteIcon = true
                    this.feedbackForm.id = feedback.id
                    this.feedbackForm.priority = feedback.priority;
                    this.feedbackForm.title = feedback.title
                    this.feedbackForm.origin = feedback.origin
                    this.feedbackForm.feedbackTime = feedback.feedbackTime
                    this.feedbackForm.projectId = feedback.projectId
                    this.feedbackForm.content = feedback.content
                }else{
                    this.feedbackDetailVisible = true;
                    this.clearFeedbackForm();
                    this.feedbackForm.priority = feedback.priority;
                    this.feedbackForm.title = feedback.title
                    this.feedbackForm.origin = feedback.origin
                    this.feedbackForm.projectId = feedback.projectId
                    this.feedbackForm.content = feedback.content
                }
            },
            saveFeedback(formName){
                this.isSaving = true
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if(this.feedbackForm.projectId==''){
                            this.$message({ showClose: true,message: '请选择项目',type: 'warning'});
                            return ;
                        }
                        if (this.feedbackForm.feedbackTime == '') {
                            this.$message({ showClose: true,message: '请选择提出时间',type: 'warning'});
                            return;
                        }
                        if(this.feedbackForm.priority==''){
                            this.$message({ showClose: true,message: '请选择优先级',type: 'warning'});
                            return ;
                        }
                        var param = this.feedbackForm;
                        param.origin = this.feedbackForm.origin.trim()
                        param.content = this.feedbackForm.content.trim()
                        param.feedbackTime = moment(param.feedbackTime).format('YYYY-MM-DD HH:00:00')
                        if(this.feedbackForm.id!=''){
                            http.zsyPutHttp('/feedback/edit/'+this.feedbackForm.id, param, (resp) => {
                                this.$message({ showClose: true,message: '需求修改成功',type: 'success'});
                                this.$refs[formName].resetFields();
                                this.feedbackVisible = false
                                this.fetchFeedbackList();
                                this.isSaving =false
                            });
                        }else{
                            http.zsyPostHttp('/feedback/add', param, (resp) => {
                                this.$message({ showClose: true,message: '需求创建成功',type: 'success'});
                                this.$refs[formName].resetFields();
                                this.feedbackVisible = false
                                this.fetchFeedbackList();
                                this.isSaving =false
                            });
                        }
                    } else {
                        this.$message({ showClose: true,message: '请确认信息填写完整',type: 'warning'});
                        return false;
                    }
                });
            },
            deleteFeedback(){
                this.$confirm('此操作将删除该需求, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(() => {
                    http.zsyDeleteHttp('/feedback/delete/'+this.feedbackForm.id, null, (resp) => {
                        this.$message({ showClose: true,message: '需求删除成功',type: 'success'});
                        this.feedbackVisible = false
                        this.fetchFeedbackList();
                    });
                }).catch(() => {
                });
            },
            fetchFeedbackList(){
                http.zsyPostHttp(`/feedback/list`, this.feedbackList, (resp) => {
                    this.demandData =  resp.data.list;
                    this.feedbackPage.total = resp.data.total
                });
            },
            feedbackPageLayout() {
                if (this.feedbackPage.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            },
            fetchStageList() {
                let vm = this
                http.zsyGetHttp('/stage/list', {}, (resp) => {
                    vm.stageList = resp.data
                })
            },
            fetchTagList() {
                let vm = this
                http.zsyGetHttp('/tag/list', {}, (resp) => {
                    vm.tagList = resp.data
                })
            },
            handleCurrentChange(currentPage){
                this.feedbackList.pageNum = currentPage;
                this.fetchFeedbackList();
            },
            saveAddTask() {
                if (this.taskStep.taskName == '') {
                    this.warnMsg("请填写任务名称");
                    return;
                }
                if (this.taskStep.taskName.length >100  ) {
                    this.warnMsg("任务名称请勿超过100字");
                    return;
                }
                if (this.taskStep.createBy == '') {
                    this.warnMsg("请填写任务负责人");
                    return;
                }
                if (this.taskStep.endTime == '') {
                    this.warnMsg("请选择结束时间");
                    return;
                }
                if (this.taskStep.stageId === '') {
                    this.warnMsg("请选择项目阶段");
                    return;
                }
                if (this.taskStep.facility === '') {
                    this.warnMsg("请选择任务难易度");
                    return;
                }
                if (this.taskStep.tags.length == 0) {
                    this.warnMsg("请选择至少一项标签");
                    return;
                }
                for(var i =0;i< this.adminList.length;i++){
                    if(this.taskStep.createBy == this.adminList[i].id){
                        this.taskStep.userName = this.adminList[i].name
                    }
                }

                this.showTaskDetail = !this.showTaskDetail;
                if (this.taskStep.index === '') {
                    let task = {}
                    task.taskName = this.taskStep.taskName.trim()
                    task.description = this.taskStep.description.trim()
                    task.createBy = this.taskStep.createBy
                    task.endTime = moment(this.taskStep.endTime).format('YYYY-MM-DD 23:59:59')
                    task.taskType = this.taskStep.taskType
                    task.projectId = 0
                    task.priority = this.feedbackForm.priority
                    task.facility = this.taskStep.facility
                    task.stageId = this.taskStep.stageId
                    task.tags = this.taskStep.tags
                    task.userName = this.taskStep.userName
                    this.planTask.push(task)
                } else {
                    // 取消css
                    this.planTask[this.taskStep.index].cssClass = ''
                }

                this.taskStep = {
                    index: '',
                    taskName:'',
                    createBy:'',
                    taskType:2,
                    description: '',
                    userName:'',
                    endTime: '',
                    projectId: '',
                    priority: '',
                    facility: '',
                    stageId:'',
                    tags:''
                }
                this.stepTemp = {}
            },
            cancelAddTask(){
                this.showTaskDetail = !this.showTaskDetail;
                if (this.taskStep.index !== '') {
                    this.planTask[this.taskStep.index] = this.stepTemp;
                    // 取消css
                    this.planTask[this.taskStep.index].cssClass = ''
                }
                this.taskStep = {
                    index: '',
                    taskName:'',
                    createBy:'',
                    taskType:2,
                    description: '',
                    userName:'',
                    endTime: '',
                    projectId: '',
                    priority: '',
                    facility: '',
                    stageId:'',
                    tags:''
                }
            },
            savePlan(){
                this.isSaving = true
                if(this.planTask.length<1){
                    this.warnMsg("请添加计划任务");
                    return;
                }
                if (this.feedbackPlanForm.expectStartTime == '') {
                    this.warnMsg("请选择预计开始时间");
                    return;
                }
                if (this.feedbackPlanForm.expectOfficialTime == '') {
                    this.warnMsg("请填写预计上线时间");
                    return;
                }
                let param = this.feedbackPlanForm;
                param.expectOfficialTime = moment(param.expectOfficialTime).format('YYYY-MM-DD 23:59:59');
                param.expectStartTime = moment(param.expectStartTime).format('YYYY-MM-DD 00:00:00');
                param['planTask'] = this.planTask;
                http.zsyPostHttp('/feedback/addPlan', param, (resp) => {
                    this.$message({
                        showClose: true,
                        message: '计划创建成功',
                        type: 'success'
                    });
                    this.fetchFeedbackList();
                    this.planVisible = false;
                    this.isSaving = false
                })
                },
            modifyStep(index, stages) {
                this.stepTemp = {
                    name: stages[index].name,
                    createBy:stages[index].createBy,
                    description: stages[index].description,
                    endTime: stages[index].endTime,
                    projectId: stages[index].projectId,
                    priority: stages[index].priority,
                    facility: stages[index].facility,
                    stageId: stages[index].stageId,
                    taskType:2,
                    tags: stages[index].tags
                }
                this.planTask.forEach((item) => {
                    item.cssClass = ''
                })
                this.planTask[index].cssClass = 'stepActive'
                this.taskStep = stages[index]
                this.taskStep.index = index
                this.showTaskDetail = true;
            },
            deleteTaskMember(index) {
                this.planTask.splice(index, 1);
                if (this.planTask.length == 0) {
                    this.showTaskDetail = false
                    this.taskStep = {
                        index: '',
                        taskName:'',
                        createBy:'',
                        taskType:2,
                        description: '',
                        userName:'',
                        endTime: '',
                        projectId: '',
                        priority: '',
                        facility: '',
                        stageId:'',
                        tags:''
                    }
                }
            },
            clearPlan(){
                this.feedbackPlanForm.feedbackId=''
                this.feedbackPlanForm.id=''
                this.feedbackPlanForm.expectOfficialTime = ''
                this.feedbackPlanForm.expectStartTime = ''
                this.planTask = []
            },
            upload(file) {
                var data = new FormData();
                data.append('uploadFile', file.file);
                http.zsyPostHttp('/upload/image', data, (res) => {
                    this.imageUrl = res.data.url;

                    let quill = this.$refs.myQuillEditor.quill
                    // 如果上传成功
                    if (res.errCode === '00' && this.imageUrl !== null) {
                        // 获取光标所在位置
                        let length = quill.getSelection().index;
                        // 插入图片  res.info为服务器返回的图片地址
                        quill.insertEmbed(length, 'image', this.imageUrl)
                        // 调整光标到最后
                        quill.setSelection(length + 1)
                    } else {
                        this.$message.error('图片插入失败')
                    }
                })
            },
            beforeAvatarUpload(file){
                const isImage = file.type.indexOf('image') === 0;
                const isLt2M = file.size / 1024 / 1024 < 2;

                if (!isImage) {
                    this.$message({ showClose: true,message: '请选择正确的图片文件',type: 'error'});
                }
                if (!isLt2M) {
                    this.$message({ showClose: true,message: '上传头像图片大小不能超过 2MB',type: 'error'});
                }
                return isImage && isLt2M;
            },
            directPlanTask(taskId){
                if(taskId!=null||taskId!=''){
                    this.$router.push({name:'taskListFormComments', params:{ taskId:taskId }})
                }
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

  .demand-con {
    width: 1100px;
    margin: auto;
  }

  .demand-top {
    background: #fff;
    padding-top: 20px;
    border-radius: 4px;
    box-shadow: 0 0 10px #ccc;
    margin-bottom: 24px;
  }

  .demand-top-list {
    margin: 0 20px 5px 20px;
  }


  .tag-name button {
    margin: 0 10px 10px 0;
  }

  .tag-name button:focus {
    color: #545454;
    border-color: #c4c4c4;
  }

  .tag-name button.active {
    color: #36A8FF;
    border-color: #36A8FF;
  }

  .ttl-name {
    margin-right: 50px;
    font-size: 14px;
  }

  .pagination .el-pagination {
    text-align: right;
    padding-right: 0;
    margin-top: 20px;
    margin-right: 10px;
  }
</style>
<style>

  .stepActive {
    box-shadow: 0 0 10px #20A0FF !important;
  }

  .star {
    color: red;
    padding: 1px;
  }

  .ctpc-list-con .el-select {
    width: 188px;
  }

  .ctpc-list-menu {
    line-height: 30px;
    width: 170px;
    font-size: 14px;
  }

  .ctpc-list-con {
    width: 220px;
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
    /*margin-left: 10px;*/
    cursor: pointer;
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

  .ctpc-member-list > span {
    display: inline-block;
    vertical-align: middle;
  }

  .ctpc-member-job-time {
    width: 300px;
  }

  .ctpc-member-end-time {
    /*width: 110px;*/
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
    width: 120px;
  }

  .member-time-count {
    width: 40px;
    border: 1px solid #ccc;
    height: 26px;
    border-radius: 4px;
    margin-right: 4px;
    text-indent: 4px;
  }

  .member-time-week{
    width: 40px;
    border: 1px solid #ccc;
    height: 26px;
    border-radius: 4px;
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
    width: 180px;
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

<style>
  .quill-editor {
    height: 745px;

  .ql-container {
    height: 680px;
  }
  }

  .limit {
    height: 30px;
    border: 1px solid #ccc;
    line-height: 30px;
    text-align: right;

  span {
    color: #ee2a7b;
  }
  }

  .ql-snow .ql-editor img {
    max-width: 480px;
  }

  .ql-editor .ql-video {
    max-width: 480px;
  }


</style>


<style>
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


</style>

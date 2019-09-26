<template>
  <div class="intergralHistory-con">
    <div class="intergralHistory-con-top clearfix">
      <div class="fl menu-list" v-for="(list,index) in menuList" @click="togTable(index)"
      :style="tabActive(index)">{{list.name}}
      </div>
      <div class="fl menu-list" v-show="diyStyle">
      <el-date-picker v-model="queryForm.beginTime" type="date" placeholder="选择日期"></el-date-picker>
      <span class="div-line">-</span>
      <el-date-picker v-model="queryForm.endTime" type="date" placeholder="选择日期"></el-date-picker>
      <img src="../assets/img/u1221.png" alt="" @click="integralDate()" class="serch-btn">
      </div>
      <el-button @click="editIntegralVisible = true" size="large" v-show="permit" style="float: right;position: relative;bottom: 12PX; right: 100PX;">添加积分记录</el-button>
      <el-button @click="backButton()" size="large" style="float: right;position: relative;bottom: 12PX; right: 200PX;">返回上页</el-button>
    </div>
    <el-table :data="historyData" stripe style="width: 100%;bottom:20px">
      <el-table-column prop="userName" label="成员" align="center" width="100px" ></el-table-column>
      <el-table-column prop="integral" label="积分" align="center" >
        <template scope="scope">
          <el-tag
            :type="scope.row.integral<0 ? 'danger' : 'gray'"
            close-transition>{{scope.row.integral}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="taskHours" label="工作量" align="center" width="100px" ></el-table-column>
      <el-table-column prop="origin" label="来源" align="center" width="200px" >
        <template scope="scope">
          <el-tag
            :type="scope.row.origin === '任务系统-单人任务' ? 'warning' : 'success'&&scope.row.origin === '手动录入' ? 'primary' : 'success'&&scope.row.origin === '转移求助' ? 'danger' : 'success'&&scope.row.origin === 'Bug处理结果' ? 'info' : 'success'"
            close-transition>{{scope.row.origin}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="score" label="评价" align="center" width="100px" >
        <template scope="scope">
          <!--<el-button type="text" @click="scope.row.type =='2'?commentDetail(scope.$index,historyData):''" v-show="permit">{{scope.row.grade}}</el-button>-->
          <div type="text">{{scope.row.score}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="完成时间" align="center" width="200px"></el-table-column>
      <el-table-column prop="description" label="备注" align="center" width="400px">
          <template scope="scope">
              <span @click="showTaskDetail(scope.row.taskId)" style="cursor: pointer;text-decoration: underline">{{scope.row.description}}</span>
          </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @current-change="integralHistory"
      :page-size="historyPage.pageSize"
      :current-page="historyPage.currentPage"
      :layout="historyPage.layout"
      :total="historyPage.totals">
    </el-pagination>
    <el-dialog  title="新增个人积分记录"  size="tiny"  :close-on-click-modal="false"
                :close-on-press-escape="false" :visible.sync="editIntegralVisible">
      <el-form :model="integralForm" :rules="rules" ref="integralForm" label-width="80px">
        <el-form-item label="积分加减" prop="integral">
        <el-input v-model="integralForm.integral"></el-input>
        </el-form-item>
        <el-form-item label="积分备注" prop="description">
        <el-input type="textarea" v-model="integralForm.description" :rows="3"></el-input>
      </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="saveIntegralInfo('integralForm')">立即创建</el-button>
        <el-button @click="cancelIntegral()">取 消</el-button>
      </span>
    </el-dialog>
    <el-dialog
      title="评价详情"
      top="10%"
      :visible.sync="showTaskCommentDetail"
      size="small">
          <h2 style="font-size: 20px;margin-bottom: 20px">总体评价：  <span>{{ taskCommentDetail.commentGrade }}</span>
          </h2>
          <div v-for="(item,index) in taskCommentDetail.comments">
            <el-form label-position="left" inline class="demo-table-expand">
              <el-form-item label="姓名">
                <span>{{ item.commentUserName }}</span>
              </el-form-item>
              <el-form-item label="评价">
                <span>{{ item.grade }}</span>
              </el-form-item>
              <el-form-item label="描述">
                <span>{{ item.description }}</span>
              </el-form-item>
            </el-form>
          </div>
          <span slot="footer" class="dialog-footer">
        <el-button @click="showTaskCommentDetail = false" type="primary">确 定</el-button>
      </span>
    </el-dialog>

    <el-dialog
            title="评价详情"
            top="10%"
            :visible.sync="showTaskEvaluationVisible"
            size="tiny"
            :before-close="hideTaskEvaluationDetail"
            :close-on-click-modal="false"
            :close-on-press-escape="false">
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
                       show-text
                       text-template="{value}"
                       style="float: left;margin-top: 7px">
              </el-rate>
              <!--<span>{{item.score}}</span>-->
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
                         show-text
                         text-template="{value}"
                         style="float: left;margin-top: 7px">
                </el-rate>
                <!--<span>{{evaluation.score}}</span>-->
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
            title="任务详情"
            top="10%"
            :visible.sync="showTaskDetailVisible"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            custom-class="myDialog"
            size="tiny"
            :before-close="hideTaskDetail">
      <el-form>
        <el-form-item class="task-form" label="任务名称：">{{taskDetail.name}}</el-form-item>
        <el-form-item class="task-form" label="关联文档：">
          <a v-if="taskDetail.doc !== undefined && taskDetail.doc !== null && taskDetail.doc !== ''" style="cursor: pointer;"
             @click="toFile(taskDetail.doc)">{{taskDetail.doc.substring(0,50)}}...
          </a>
        </el-form-item>
        <el-form-item class="task-form" style="white-space: pre-wrap" label="任务描述：">{{taskDetail.description}}</el-form-item>
        <el-form-item class="task-form" label="项目：">{{taskDetail.projectName}}</el-form-item>
        <el-form-item class="task-form" label="阶段：" style="margin-bottom: -36px;">{{taskDetail.stageName}}</el-form-item>
        <el-form-item class="task-form" label="优先级：" style="margin-left: 200px;"><span v-for="item in priorityList"
                                                                                       v-if="item.value == taskDetail.priority">{{item.label}}</span>
        </el-form-item>
        <el-form-item class="task-form" label="难易度："  style="margin-bottom: -36px;"><span v-for="item in facilityList"
                                                                                          v-if="item.value == taskDetail.facility">{{item.label}}</span>
        </el-form-item>
        <el-form-item class="task-form" label="设计完成时间：" style="margin-left: 200px;">{{taskDetail.beginTime | formatDate}}</el-form-item>
        <el-form-item class="task-form" label="开发完成时间：" style="margin-bottom: -36px;">{{taskDetail.testTime | formatDate}}</el-form-item>
        <el-form-item class="task-form" label="截止时间：" style="margin-left: 200px;">{{taskDetail.endTime | formatDate}}</el-form-item>
        <el-form-item class="task-form" label="标签：">
          <el-tag style="margin: 5px;" type="gray" v-for="(item, key) in taskDetail.tags" :key="key">
            {{item.name}}
          </el-tag>
        </el-form-item>
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
            <span class="fl ctpc-member-assess" v-show="item.status>1 && item.avgScore">评分：{{item.avgScore}}</span>
            <a href="javascript:;" v-show="taskDetail.status>1 && userRole===0 && item.status > 1"
               @click="evaluateDetail(item.id,item.jobRole,item.userName)">查看评价</a>
            <el-tooltip placement="top">
              <div slot="content">
                {{item.description}}<br/>开始时间:{{item.beginTime | formatDate}}
                <div v-if="item.functionStrs !== undefined && item.functionStrs.length > 0">
                  <div>功能点:</div>
                  <div v-for="functionStr in item.functionStrs">{{functionStr}}</div>
                </div>
              </div>
              <span class="fl" style="margin-left: 25px"><i class="el-icon-information"></i></span>
            </el-tooltip>
            <span v-if="item.proTest && !taskDetail.testing" class="fl ctpc-member-end-time" style="margin-left:20px;color: #66ccff">测试中</span>
          </div>
          <div class="bdl-line"></div>
        </div>
        <div v-else="taskDetail.type==1" v-for="(item,index) in taskDetail.users">
          <el-form-item class="task-form" label="工作量：">{{item.taskHours}} 工时</el-form-item>
          <el-form-item class="task-form" label="负责人：">{{item.userName}}</el-form-item>
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
    </el-dialog>
  </div>
</template>
<script>
  import Http from '../lib/Http'
  import Helper from '../lib/Helper'
  import moment from 'moment';

  export default {
    name: 'IntegralHistory',
    data() {
      return {
        TableData:'',
        historyData: [],
        queryForm: {
          startTime: '',
          endTime: '',
          userId: '',
          pageNum: 1,
        },
        editIntegralVisible:false,
        showTaskCommentDetail:false,
        taskCommentDetail: {},
        taskDetail:{},
        historyPage: {
          layout: "total, pager",
          currentPage: 1,
          pageSize: 10,
          totals: 0,
          pageNum: 0
        },
        integralForm: {//添加积分记录
          userId: '',
          description: '',
          integral: ''
        },
        activeIdx:0,
        diyStyle: false,
        menuList: [
          {
            name: '本月'
          },
          {
            name: '本季度'
          },
          {
            name: '本年'
          },
          {
            name: '自定义'
          }
        ],
        rules: {
          integral: [{required: true, message: '积分不能为空', trigger: 'change', decimal: 10}],
          description: [{message: '备注不能超过100字', trigger: 'change', min: 0, max: 100}],
        },
          showTaskDetailVisible:false,
          showTaskEvaluationVisible:false,
          hasAvgEvalution:false,
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
          taskLog: {
              list: [],
              hasNextPage: false,
              pageNum: 1
          },
          taskEvaluation:[],
          avgEvaluation:[],
          taskUserName:'',
      };
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
    computed: {
      permit() {
        let userRole = Helper.decodeToken().userRole;
        return userRole == 0;
      },
        userRole() {
            let userRole = Helper.decodeToken().userRole;
            return userRole;
        },
    },
    beforeMount: function (){
        this.integralHistory(1);
        //选中任务tab
        this.$root.eventBus.$emit("handleTabSelected", "intergral");
    },
    methods:{
      commentDetail(index,rows){
        var taskId= rows[index].taskId;
        Http.zsyGetHttp(`/task/detail/`+taskId, {}, (resp) => {
          this.taskDetail = resp.data

          this.taskDetail.users.forEach((user) => {
            if (user.id == rows[index].id) {
              this.taskCommentDetail = user
              return
            }
          })
          this.showTaskCommentDetail = true
        });
      },
      togTable(index) {
        // 点击菜单
        this.activeIdx = index;

        switch (index) {
          case 0:
            this.getDateString("month");
            this.integralHistory(1);
            this.display(index);
            break;
          case 1:
            this.getDateString("quarter");
            this.integralHistory(1);
            this.display(index);
            break;
          case 2:
            this.getDateString("year");
            this.integralHistory(1);
            this.display(index);
            break;
          case 3:
            this.display(index);
            break;
        }
      },
      backButton(){
        this.$router.go(-1);
      },
      tabActive(index) {
        // 颜色变化
        if (index == this.activeIdx) {
          return {
            color: '#36A8FF'
          }
        }
      },
      display(index) {
        if (index != 3) {
          this.diyStyle = false;
        } else {
          if (this.diyStyle == false) {
            this.diyStyle = true;
          } else {
            this.diyStyle = false;
          }
        }
      },
      integralHistory(currentPage) {//查询积分历史记录
        this.integralForm.userId = this.$route.query.userId;
        this.queryForm.userId = this.$route.query.userId;
        this.queryForm.pageNum = currentPage;
        Http.zsyPostHttp('/user-task-integral/history', this.queryForm, (res) => {
          let list = res.data.list;
          // for (var i = 0; i < list.length; i++) {
          //   if (list[i].origin == 2) {
          //     list[i].origin = "手动录入";
          //     list[i].createTime = this.localeTimeString(list[i].createTime);
          //   } else if(list[i].origin == 3) {
          //       list[i].origin = "转移求助";
          //       list[i].createTime = this.localeTimeString(list[i].createTime);
          //   }else if(list[i].origin == 4) {
          //       list[i].origin = "Bug处理结果";
          //       list[i].createTime = this.localeTimeString(list[i].createTime);
          //   }else{
          //           list[i].createTime = this.localeTimeString(list[i].createTime);
          //           if(list[i].type!=1){
          //               list[i].origin = "任务系统-多人任务";
          //           }else{
          //               list[i].origin = "任务系统-单人任务";
          //               list[i].grade = "";
          //           }
          //   }
          // }
          for (var i = 0; i < list.length; i++) {
            if (list[i].origin == 3) {
              list[i].origin = "手动录入";
              list[i].createTime = this.localeTimeString(list[i].createTime);
            } else if(list[i].origin == 1) {
                list[i].origin = "任务系统-多人任务";
                list[i].createTime = this.localeTimeString(list[i].createTime);
            }else if(list[i].origin == 4) {
                list[i].origin = "Bug处理结果";
                list[i].createTime = this.localeTimeString(list[i].createTime);
            }else{
                list[i].origin = "任务系统-单人任务";
                list[i].createTime = this.localeTimeString(list[i].createTime);
            }
          }
          if (this.queryForm.endTime != null && this.queryForm.endTime != "") {
            this.queryForm.endTime = this.localeTimeString(new Date(this.queryForm.endTime).getTime() - 86399000);//之前加入的结束时间减回
          }
          this.historyPage.totals = res.data.total;
          this.historyPage.pageNum = res.data.pages;
          this.historyPage.pageSize = res.data.pagesize;
          this.historyData = res.data.list;
          this.pagingLayout();
        });
      },
      saveIntegralInfo(integralForm) {
        this.$refs[integralForm].validate((valid) => {
          if (valid) {
            if (!this.isDecimal(this.integralForm.integral)) {
                this.$message({
                    showClose: true,
                    message: '积分格式错误',
                    type: 'error'
                });
                return false;
            }
            // Http.zsyPostHttp('/integral/add', this.integralForm, (res) => {
            Http.zsyPostHttp('/user-task-integral/add', this.integralForm, (res) => {
              this.$message({
                  showClose: true,
                  message: '积分添加成功',
                  type: 'success'
              });
              this.editIntegralVisible = false;
              this.integralHistory(1);
              this.cancelIntegral();
            });
          } else {
            return false;
          }
        });
      },
      cancelIntegral() {
        this.editIntegralVisible = false;
        this.integralForm.description = '';
        this.integralForm.integral = '';
      },
      pagingLayout() {
        if (this.historyPage.pageNum > 1) {
          this.historyPage.layout = 'total,prev,pager,next';
        } else {
          this.historyPage.layout = 'total,pager';
        }
      },
      //根据自定义时间进行查询
      integralDate() {
        // this.queryForm.startTime = this.localeTimeString(this.queryForm.startTime);
        this.queryForm.beginTime = this.localeTimeString(this.queryForm.beginTime);
        if (this.queryForm.endTime != null && this.queryForm.endTime != "") {
          this.queryForm.endTime = this.localeTimeString(new Date(this.queryForm.endTime).getTime() + 86399000);//结束时间加入23:59:59
        }
        this.integralHistory(1)
      },
      //获取本年,季度,月的开始结束日期
      getDateString(date) {
        let now = new Date();
        let curMonth = now.getMonth();
        let curYear = now.getFullYear();
        let startMonth = 0;
        if (date == "month") {//本月的开始结束时间
          // this.queryForm.startTime = this.localeTimeString(new Date(curYear, curMonth, 1));
          this.queryForm.beginTime = this.localeTimeString(new Date(curYear, curMonth, 1));
          this.queryForm.endTime = this.localeTimeString(new Date(curYear, curMonth + 1, 1));//下月第一天0点
        } else if (date == "quarter") {//本季度的开始结束时间
          if (curMonth >= 0 && curMonth <= 2) {
            startMonth = 0;
          } else if (curMonth >= 3 && curMonth <= 5) {
            startMonth = 3;
          } else if (curMonth >= 6 && curMonth <= 8) {
            startMonth = 6;
          } else if (curMonth >= 9 && curMonth <= 11) {
            startMonth = 9;
          }
          // this.queryForm.startTime = this.localeTimeString(new Date(curYear, startMonth, 1));
          this.queryForm.beginTime = this.localeTimeString(new Date(curYear, startMonth, 1));
          this.queryForm.endTime = this.localeTimeString(new Date(curYear, startMonth + 3, 1));//下季度第一天0点
        } else if (date == "year") {//本年的开始结束时间
          // this.queryForm.startTime = this.localeTimeString(new Date(now.getFullYear(), 0, 1));
          this.queryForm.beginTime = this.localeTimeString(new Date(now.getFullYear(), 0, 1));
          this.queryForm.endTime = this.localeTimeString(new Date(now.getFullYear() + 1, 0, 1));
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
      localeTimeString(time) {
        if (time != null && time != "") {
          time = moment(time).format('YYYY-MM-DD HH:mm:ss');
          return time;
        } else {
          return "";
        }
      },
        showTaskDetail(taskId){
          if (taskId != null && taskId !== undefined && taskId !== ''){
              this.getTaskDetail(taskId)
          }
        },

        //查看任务详情
        getTaskDetail(id){
            if(id != null && id !== ''){
                Http.zsyGetHttp('task/detail/'+id,{},(res =>{
                    this.taskDetail = res.data;
                    this.showTaskDetailVisible = true;
                    // if (this.taskDetail.myFunctionResDTOS !== undefined && this.taskDetail.myFunctionResDTOS.length>0){
                    //     this.taskDetail.myFunctionResDTOS.forEach(resDTO=>{
                    //         this.modifyTaskFunctionList.push(resDTO.functionId);
                    //         this.modifyFunctionLevelList.push(resDTO.level);
                    //     });
                    //     this.num = this.taskDetail.myFunctionResDTOS.length;
                    // }
                    this.getTaskLog(id)
                }))
            }else {
                this.taskDetail = {};
                this.showTaskDetailVisible = false;
            }
        },
        hideTaskDetail() {
            this.showTaskDetailVisible = false;
            this.taskDetail = {};
            this.taskLog.list = [];
            this.taskLog.hasNextPage = false;
            this.taskLog.pageNum = 1;
        },
        hideTaskEvaluationDetail() {
            this.showTaskEvaluationVisible = false;
            this.hasAvgEvalution = false;
            this.showTaskDetailVisible = true;
        },
        taskStepStatus(item, taskUserNum){
            // const commented = item.commentNum > 0 && item.commentNum == taskUserNum - 1;
            const commented = item.isEvaluated === 1;
            let className = 'in';
            if (item.status === 1) {
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
        getTaskLog(taskId) {
            Http.zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, (resp) => {
                this.taskLog.list = resp.data.list;
                this.taskLog.hasNextPage = resp.data.hasNextPage;
            });
        },
        taskLogMore(taskId) {
            this.taskLog.pageNum += 1;
            Http.zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, (resp) => {
                let logs = resp.data.list;
                this.taskLog.list = this.taskLog.list.concat(logs);
                this.taskLog.hasNextPage = resp.data.hasNextPage;
            });
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
            this.showTaskEvaluationVisible = true;
            this.showTaskDetailVisible = false;
        },
    }
  }

</script>
<style scoped>

  .intergralHistory-con-top {
    font-size: 14px;
    background: #fff;
    padding: 30px 0;
    text-indent: 10px;
    box-shadow: 0 0 10px #ccc;
    /*margin: 0 10px;*/
    width: 1300px;
    margin: auto;
  }

  .intergralHistory-con{
    width: 1300px;
    margin: auto;
  }

  .menu-list {
    line-height: 36px;
    padding: 0 4px;
    margin-right: 10px;
    cursor: pointer;
  }

  .div-line {
    margin: 0 10px 0 12px;
    color: #000;
  }

  .serch-btn {
    vertical-align: middle;
    margin-left: 10px;
    cursor: pointer;
  }

  .intergralHistory-con .task-form {
    margin-bottom: 0;
  }

  .intergralHistory-con .ctpc-member-con {
    margin: 15px 0;
    padding-left: 10px; /* border-left: 1px solid #ccc; */
    margin-left: 6px;
    position: relative;
  }

  .intergralHistory-con .ctpc-member-list {
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

  .intergralHistory-con .ctpc-member-head {
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

  .intergralHistory-con .ctpc-member-job-time {
    width: 110px;
  }

  .intergralHistory-con .ctpc-member-end-time {
    /*width: 110px;*/
  }

  .intergralHistory-con .ctpc-member-assess {
    width: 70px;
    margin-left: 30px;
  }

  .intergralHistory-con .bdl-line {
    position: absolute;
    left: 0;
    bottom: 20px;
    top: 14px;
    border-left: 1px solid #ccc;
  }

  .intergralHistory-con .trends {
    /*background-color: #f2f2f2; */
    /*padding-left: 10px;*/
    line-height: 30px;
    border: 1px solid #e4e8f1;

  }

  .intergralHistory-con .trends-title {
    padding: 0 10px;
    line-height: 30px;
    background-color: #e4e8f1;
  }

  .intergralHistory-con .trends li:before{
    content:"*";
    float: left;
    margin-right: 5px;
    color: #f40;
  }

  .intergralHistory-con .task-title-detail{
    margin-top:-5px;
    line-height: 20px;
    font-size:12px;
    color: #ccc;
  }

  .intergralHistory-con .task-title-detail em{
    margin-right: 5px;
    border-left:3px solid #ccc;
  }

  .intergralHistory-con .ctpc-member-list.done:before {
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

  .intergralHistory-con .ctpc-member-list.finished:before {
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

  .intergralHistory-con .ctpc-member-list.in:before {
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
</style>

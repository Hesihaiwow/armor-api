<template>
  <div class="intergralHistory-con">
    <div class="intergralHistory-con-top clearfix">
      <div class="fl menu-list" v-for="(list,index) in menuList" @click="togTable(index)"
      :style="tabActive(index)">{{list.name}}
      </div>
      <div class="fl menu-list" v-show="diyStyle">
      <el-date-picker v-model="queryForm.startTime" type="date" placeholder="选择日期"></el-date-picker>
      <span class="div-line">-</span>
      <el-date-picker v-model="queryForm.endTime" type="date" placeholder="选择日期"></el-date-picker>
      <img src="../assets/img/u1221.png" alt="" @click="integralDate()" class="serch-btn">
      </div>
      <el-button @click="editIntegralVisible = true" size="large" v-show="permit" style="float: right;position: relative;bottom: 12PX; right: 100PX;">添加积分记录</el-button>
      <el-button @click="backButton()" size="large" style="float: right;position: relative;bottom: 12PX; right: 200PX;">返回上页</el-button>
    </div>
    <el-table :data="historyData" stripe style="width: 100%;bottom:20px">
      <el-table-column prop="name" label="成员" align="center" ></el-table-column>
      <el-table-column prop="integral" label="积分" align="center" >
        <template scope="scope">
          <el-tag
            :type="scope.row.integral<0 ? 'danger' : 'gray'"
            close-transition>{{scope.row.integral}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="taskHours" label="工作量" align="center"></el-table-column>
      <el-table-column prop="origin" label="来源" align="center">
        <template scope="scope">
          <el-tag
            :type="scope.row.origin === '任务系统-单人任务' ? 'warning' : 'success'&&scope.row.origin === '手动录入' ? 'primary' : 'success'"
            close-transition>{{scope.row.origin}}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="grade" label="评价" align="center" >
        <template scope="scope">
          <el-button type="text" @click="scope.row.type =='2'?commentDetail(scope.$index,historyData):''" v-show="permit">{{scope.row.grade}}</el-button>
          <div type="text" v-show="!permit">{{scope.row.grade}}</div>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="完成时间" align="center"></el-table-column>
      <el-table-column prop="description" label="备注" align="center" ></el-table-column>
    </el-table>
    <el-pagination
      @current-change="integralHistory"
      :page-size="historyPage.pageSize"
      :current-page="historyPage.currentPage"
      :layout="historyPage.layout"
      :total="historyPage.totals">
    </el-pagination>
    <el-dialog  title="新增个人积分记录"  size="tiny"  :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="editIntegralVisible">
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
  </div>
</template>
<script>
  import Http from '../lib/Http'
  import Helper from '../lib/Helper'
  import { Message } from 'element-ui';
  import moment from 'moment';
  import ElButton from "../../node_modules/element-ui/packages/button/src/button";

  export default {
    components: {ElButton},
    name: 'IntegralHistory',
    data() {
      return {
        TableData:'',
        historyData: [],
        queryForm: {
          startTime: '',
          endTime: ''
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
        }
      };
    },
    computed: {
      permit() {
        let userRole = Helper.decodeToken().userRole;
        return userRole == 0;
      }
    },
    beforeMount: function (){
        this.integralHistory(1)
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
        Http.zsyGetHttp('/integral/history/' + this.integralForm.userId + '/' + currentPage, this.queryForm, (res) => {
          let list = res.data.list;
          for (var i = 0; i < list.length; i++) {
            if (list[i].origin != 1) {
              list[i].origin = "手动录入";
              list[i].createTime = this.localeTimeString(list[i].createTime);
            } else {
                list[i].createTime = this.localeTimeString(list[i].createTime);
                if(list[i].type!=1){
                  list[i].origin = "任务系统-多人任务";
                }else{
                  list[i].origin = "任务系统-单人任务";
                  list[i].grade = "";
                }
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
                Message.error("积分格式错误");
                return false;
            }
            Http.zsyPostHttp('/integral/add', this.integralForm, (res) => {
              Message.success("积分添加成功");
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
        this.editIntegralVisible = false
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
        this.queryForm.startTime = this.localeTimeString(this.queryForm.startTime);
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
        ;
        let startMonth = 0;
        if (date == "month") {//本月的开始结束时间
          this.queryForm.startTime = this.localeTimeString(new Date(curYear, curMonth, 1));
          this.queryForm.endTime = this.localeTimeString(new Date(curYear, curMonth + 1, 1));//下月第一天0点
        } else if (date == "quarter") {//本季度的开始结束时间
          if (curMonth >= 1 && curMonth <= 3) {
            startMonth = 0;
          } else if (curMonth >= 4 && curMonth <= 6) {
            startMonth = 3;
          } else if (curMonth >= 7 && curMonth <= 9) {
            startMonth = 6;
          } else if (curMonth >= 10 && curMonth <= 12) {
            startMonth = 9;
          }
          this.queryForm.startTime = this.localeTimeString(new Date(curYear, startMonth, 1));
          this.queryForm.endTime = this.localeTimeString(new Date(curYear, startMonth + 3, 1));//下季度第一天0点
        } else if (date == "year") {//本年的开始结束时间
          this.queryForm.startTime = this.localeTimeString(new Date(now.getFullYear(), 0, 1));
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
      }
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
</style>

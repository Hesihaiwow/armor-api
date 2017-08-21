<template>
	<div class="intergral-con">
		<div class="intergral-con-top clearfix">
      <div class="fl menu-list" v-for="(list,index) in menuList" @click="togTable(index)" :style="tabActive(index)">{{list.name}}</div>
      <!--<div class="fl menu-list" @click="togTable(4)" :style="tabActive(4)">自定义</div>-->
      <div class="fl menu-list"  v-show="diyStyle">
        <el-date-picker v-model="queryForm.startTime" type="date" placeholder="选择日期"></el-date-picker><span class="div-line">-</span>
        <el-date-picker v-model="queryForm.endTime" type="date" placeholder="选择日期"  ></el-date-picker>
        <img src="../assets/img/u1221.png" alt="" @click="integralDate()" class="serch-btn">
      </div>
    </div>
    <div class="intergral-data-detail">
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="id" label="排名" align="center"></el-table-column>
        <el-table-column prop="name" label="成员" align="center"></el-table-column>
        <el-table-column prop="integral" label="积分" align="center"></el-table-column>
      </el-table>
    </div>
    <el-pagination
      @current-change="integralPage"
      :page-size="pageInfo.pageSize"
      :current-page="pageInfo.currentPage"
      :layout="pageInfo.layout"
      :total="pageInfo.totals">
    </el-pagination>
	</div>
</template>
<script>
  import Http from '../lib/Http'
  import { Message } from 'element-ui';
  import moment from 'moment';

  moment.locale('zh-cn');
  export default {
    name: 'Intergral',
    data() {
      return {
        queryForm:{
          startTime:'',
          endTime:''
        },
        dateForm:{
          startTime:'0'
        },
        pageInfo:{
          layout:"total, pager",
          currentPage:1,
          pageSize:10,
          totals:0,
          pageNum:0
        },
        diyStyle:false,
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
            name:'自定义'
          }
        ],
        activeIdx: 0,
        startValue: '',
        endValue: '',
        tableData:[]
      }
    },
    beforeMount:function () {
      this.togTable(0);
    },
    methods: {
      togTable(index) {
        // 点击菜单
        this.activeIdx = index;

        switch (index) {
          case 0:
            this.getDateString("month");
            this.integralPage();
            this.display(index);
            break;
          case 1:
            this.getDateString("quarter");
            this.integralPage();
            this.display(index);
            break;
          case 2:
            this.getDateString("year");
            this.integralPage();
            this.display(index);
            break;
          case 3:
            this.display(index);
            break;
        }
      },
      tabActive (index) {
        // 颜色变化
        if (index == this.activeIdx) {
          return {
            color: '#36A8FF'
          }
        }
      },
      //根据自定义时间进行查询
      integralDate(){
        this.queryForm.startTime = this.localeTimeString(this.queryForm.startTime);
        if(this.queryForm.endTime!=null&&this.queryForm.endTime !=""){
          this.queryForm.endTime = this.localeTimeString(new Date(this.queryForm.endTime).getTime()+86399000);//结束时间加入23:59:59
        }
        this.integralPage()
      },
      //查询积分列表
      integralPage(currentPage){
        if(currentPage==null||currentPage.length==0){
          currentPage=1;
        }
        Http.zsyGetHttp(Http.API_URI.INTEGRAL+currentPage,this.queryForm,(res)=>{
          this.tableData = res.data.list;
          this.pageInfo.totals=res.data.total;
          this.pageInfo.pageNum = res.data.pages;
          this.pageInfo.pageSize =res.data.pagesize;
          this.pagingLayout();
          if(this.queryForm.endTime!=null&&this.queryForm.endTime !=""){
            this.queryForm.endTime = this.localeTimeString(new Date(this.queryForm.endTime).getTime()-86399000);//之前加入的结束时间减回
          }
        })
      },
      display(index){
        if(index!=3){
            this.diyStyle = false;
        }else{
          if(this.diyStyle==false){
            this.diyStyle=true;
          }else{
            this.diyStyle=false;
          }
        }
      },
      pagingLayout() {
        if (this.pageInfo.pageNum>1){
          this.pageInfo.layout = 'total,prev,pager,next';
        }else{
          this.pageInfo.layout = 'total,pager';
        }
      },
      //获取本年,季度,月的开始结束日期
      getDateString(date){
        let now = new Date();
        let curMonth = now.getMonth();
        let curYear =  now.getFullYear();;
        let startMonth = 0 ;
        if(date=="month"){//本月的开始结束时间
          this.queryForm.startTime = this.localeTimeString(new Date(curYear, curMonth, 1));
          this.queryForm.endTime = this.localeTimeString(new Date(curYear,curMonth+1,1));//下月第一天0点
        }else if(date=="quarter"){//本季度的开始结束时间
          if (curMonth >= 1 && curMonth <= 3){
            startMonth = 0;
          }else if (curMonth >= 4 && curMonth <= 6){
            startMonth = 3;
          }else if (curMonth >= 7 && curMonth <= 9){
            startMonth = 6;
          }else if (curMonth >= 10 && curMonth <= 12){
            startMonth = 9;
          }
          this.queryForm.startTime = this.localeTimeString(new Date(curYear, startMonth, 1));
          this.queryForm.endTime = this.localeTimeString(new Date(curYear, startMonth+3,1));//下季度第一天0点
        }else if(date=="year"){//本年的开始结束时间
          this.queryForm.startTime = this.localeTimeString(new Date(now.getFullYear(),0,1));
          this.queryForm.endTime = this.localeTimeString(new Date(now.getFullYear()+1,0,1));
        }
      },
      //时间字符串格式化
      localeTimeString(time){
        if(time!=null&&time!=""){
          time = moment(time).format('YYYY-MM-DD HH:mm:ss');
          return time;
        }else{
          return "";
        }
      }

    }
  }
</script>
<style scoped>
.intergral-con{width: 1100px;margin: auto;}
.intergral-con-top{font-size: 14px;background: #fff;padding: 16px 0;text-indent: 10px;box-shadow: 0 0 10px #ccc;margin: 0 10px;}
.menu-list{line-height: 36px;padding: 0 4px;margin-right: 10px;cursor: pointer;}
.div-line{margin: 0 10px 0 12px;color: #000;}
.self-define{margin-right: 14px;}
.serch-btn{vertical-align: middle;margin-left: 10px;cursor: pointer;}
.intergral-data-detail{margin: 20px 8px;}
</style>

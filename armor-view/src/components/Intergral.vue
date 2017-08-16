<template>
	<div class="intergral-con">
		<div class="intergral-con-top clearfix">
      <div class="fl menu-list" v-for="(list,index) in menuList" @click="togTable(index)" :style="tabActive(index)">{{list.name}}</div>
      <div class="fl menu-list" @click="togTable(4)" :style="tabActive(4)">自定义</div>
      <div class="fl menu-list" id="diy" style="display: none">
        <el-date-picker v-model="queryForm.startTime" type="datetime" placeholder="选择日期" ></el-date-picker><span class="div-line">-</span>
        <el-date-picker v-model="queryForm.endTime" type="datetime" placeholder="选择日期" ></el-date-picker>
        <img src="../assets/img/u1221.png" alt="" @click="integralPage('0')" class="serch-btn">
      </div>
    </div>
    <div class="intergral-data-detail">
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="name" label="成员" align="center"></el-table-column>
        <el-table-column prop="integral" label="积分" align="center"></el-table-column>
      </el-table>
    </div>
    <el-pagination
      @size-change=""
      @current-change="integralPage"
      :page-size="pageSize"
      :current-page="currentPage"
      layout="total, prev, pager, next"
      :total="totals">
    </el-pagination>
	</div>
</template>
<script>
  import Http from '../lib/Http.js'
  import { Message } from 'element-ui';

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
        currentPage:1,
        pageSize:10,
        totals:0,
        menuList: [
          {
            name: '本月'
          },
          {
            name: '本季度'
          },
          {
            name: '本年'
          }
        ],
        activeIdx: 0,
        startValue: '',
        endValue: '',
        tableData:''
      }
    },
    beforeMount:function () {
      Http.zsyGetHttp(Http.API_URI.INTEGRAL+this.currentPage,this.queryForm,(res)=>{
        this.tableData = res.data.list;
        this.totals=res.data.total;
        this.pageSize =res.data.pagesize;
      })
    },

    methods: {
      togTable(index) {
        // 点击菜单
        this.activeIdx = index;

        switch (index) {
          case 0:
            this.integralDate("month");
            this.displayTest();
            break;
          case 1:
            this.integralDate("quarter");
            this.displayTest();
            break;
          case 2:
            this.integralDate("year");
            this.displayTest();
            break;
          case 4:
            this.displayTest(4);
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
      integralDate(time){
        this.dateForm.startTime = time ;
        Http.zsyGetHttp(Http.API_URI.INTEGRAL+this.currentPage,this.dateForm,(res)=>{
          this.tableData = res.data.list;
          this.totals=res.data.total;
          this.pageSize =res.data.pagesize;
        })

      },
      integralPage(currentPage){
        Http.zsyGetHttp(Http.API_URI.INTEGRAL+currentPage,this.queryForm,(res)=>{
          this.tableData = res.data.list;
          this.totals=res.data.total;
          this.pageSize =res.data.pagesize;
        })
      },
      displayTest(id){
        let diy = document.getElementById("diy");
        if(id!=4){
            diy.style.display='none';
        }else{
          if(diy.style.display=="block"){
            diy.style.display='none';
          }else{
            diy.style.display='block';
          }
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

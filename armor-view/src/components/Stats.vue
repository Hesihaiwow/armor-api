<template>
  <div class="stats-con">
      <el-table :data="statsData" >
        <el-table-column prop="name" label="成员" align="center" ></el-table-column>
        <el-table-column prop="quantity" label="任务数量" align="center" >
          <template scope ="scope">
            <el-button type="text" @click="getTask(scope.$index)">{{scope.row.quantity}}</el-button>
          </template>
        </el-table-column>
        <el-table-column prop="inProcess" label="进行中任务" align="center" ></el-table-column>
        <el-table-column prop="complete" label="已完成任务" align="center" ></el-table-column>
        <el-table-column prop="delay" label="超时任务" align="center" ></el-table-column>
        <el-table-column prop="sum" label="总积分" align="center" ></el-table-column>
      </el-table>
      <el-pagination
        @current-change="getStats"
        :page-size="statsPage.pageSize"
        :current-page="statsPage.currentPage"
        :layout="statsPage.layout"
        :total="statsPage.totals">
      </el-pagination>
  </div>
</template>
<script>
  import Http from '../lib/Http'
  import Helper from '../lib/Helper'
  import { Message } from 'element-ui';
  import ElButton from "../../node_modules/element-ui/packages/button/src/button";
  import Task from "./Task"

  export default {
    components: {ElButton},
    name: 'IntegralHistory',
    data() {
      return {
          statsData:[],
          statsPage: {
            layout: "total, pager",
            currentPage: 1,
            pageSize: 10,
            totals: 0,
            pageNum: 0
          }
      }
    },
    beforeMount:function () {
        this.getStats(this.statsPage.currentPage);
        //选中任务tab
        this.$root.eventBus.$emit("handleTabSelected", "stats");
    },
    methods: {
        getStats(currentPage){
          Http.zsyGetHttp(`/stats/list/`+currentPage, {}, (resp) => {
              this.statsData =  resp.data.list;
              this.statsPage.totals = resp.data.total;
              this.statsPage.pageNum = resp.data.pages;
              this.statsPage.pageSize = resp.data.pagesize;
              this.pagingLayout();
          });
        },
        pagingLayout() {
          if (this.statsPage.pageNum > 1) {
            this.statsPage.layout = 'total,prev,pager,next';
          } else {
            this.statsPage.layout = 'total,pager';
          }
        },
        getTask(index){
          this.$router.push({path:'/index/task', query:{ userId:this.statsData[index].id }})
        }
    }
  }


</script>
<style scoped>

  .stats-con {
    width: 1100px;
    font-size: 14px;
    background: #fff;
    padding: 30px 0;
    text-indent: 10px;
    box-shadow: 0 0 10px #ccc;
    margin: auto;
  }

  .el-pagination{
    margin: 20px 0;
    text-align: right;
    margin-right: 10px;
  }

</style>


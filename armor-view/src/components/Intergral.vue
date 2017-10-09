<template>
    <div class="intergral-con">

        <div class="intergral-data-detail">
            <el-table :data="quarter" stripe style="width: 50%;float:left">
              <el-table-column label="季度排名" align="center">
                <el-table-column prop="id" label="排名" align="center">
                  <template scope="scope">
                    <div v-if="scope.row.id=='1'"><img  src="../assets/img/jin.png" :alt="scope.row.id"></div>
                    <div v-if="scope.row.id=='2'"><img  src="../assets/img/yin.png" :alt="scope.row.id"></div>
                    <div v-if="scope.row.id=='3'"><img  src="../assets/img/tong.png" :alt="scope.row.id"></div>
                    <div v-if="scope.row.id>'3'">{{scope.row.id}}</div>
                  </template>
                </el-table-column>
                <el-table-column prop="name" label="成员" align="center">
                  <template scope="scope">
                    <el-button @click.native.prevent="clicklHistory(scope.$index, quarter)" type="text" size="small">{{ scope.row.name }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column prop="integral" label="积分" align="center"></el-table-column>
              </el-table-column>
            </el-table>
        </div>
      <div class="intergral-data-detail">
          <el-table :data="year" stripe style="width: 48%;float:right">
            <el-table-column label="年度排名" align="center">
              <el-table-column prop="id" label="排名" align="center">
                <template scope="scope">
                  <div v-if="scope.row.id=='1'"><img  src="../assets/img/jin.png" :alt="scope.row.id"></div>
                  <div v-if="scope.row.id=='2'"><img  src="../assets/img/yin.png" :alt="scope.row.id"></div>
                  <div v-if="scope.row.id=='3'"><img  src="../assets/img/tong.png" :alt="scope.row.id"></div>
                  <div v-if="scope.row.id>'3'">{{scope.row.id}}</div>
                </template>
              </el-table-column>
              <el-table-column prop="name" label="成员" align="center">
                <template scope="scope">
                  <el-button @click.native.prevent="clicklHistory(scope.$index, year)" type="text" size="small">{{ scope.row.name }}</el-button>
                </template>
              </el-table-column>
              <el-table-column prop="integral" label="积分" align="center" type="danger">
                <!--<template scope="scope">-->
                  <!--<el-button @click.native.prevent="clicklHistory(scope.$index, year)" type="text" size="small">{{ scope.row.name }}</el-button>-->
                <!--</template>-->
              </el-table-column>
            </el-table-column>
          </el-table>
      </div>
    </div>

</template>
<script>
    import Http from '../lib/Http'
    import moment from 'moment';
    import Helper from '../lib/Helper'
    import IntegralHistory from "./IntegralHistory";

    moment.locale('zh-cn');
    export default {
        name: 'Intergral',
        data() {
            return {
                queryForm: {
                    startTime: '',
                    endTime: ''
                },
                yearForm: {
                  startTime: '',
                  endTime: ''
                },
                quarter: [],
                year: [],
                imgUrl:'',
                imgGUrl: '../assets/img/jin.png',
                imgYUrl: '../assets/img/yin.png',
                imgTUrl: '../assets/img/tong.png'
            }
        },
        beforeMount: function () {
            //选中积分tab
            this.$root.eventBus.$emit("handleTabSelected", "intergral");
            this.integralPage("quarter");
            this.integralPage("year");
        },
        methods: {
            clicklHistory(index, rows) {//查看积分记录
                this.$router.push({path:'/index/IntegralHistory',query:{userId:rows[index].userId}})
            },
            //查询积分列表
            integralPage(date) {
              this.getDateString("quarter");
              this.getDateString("year");
              if(date!="year"){
                Http.zsyGetHttp(Http.API_URI.INTEGRAL,this.queryForm, (res) => {
                  this.quarter= res.data;
                })
              }else{
                Http.zsyGetHttp(Http.API_URI.INTEGRAL,this.yearForm, (res) => {
                  this.year= res.data;
                })
              }
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
                    if (curMonth >= 0 && curMonth <= 2) {
                        startMonth = 0;
                    } else if (curMonth >= 3 && curMonth <= 5) {
                        startMonth = 3;
                    } else if (curMonth >= 6 && curMonth <= 8) {
                        startMonth = 6;
                    } else if (curMonth >= 9 && curMonth <= 11) {
                        startMonth = 9;
                    }
                    this.queryForm.startTime = this.localeTimeString(new Date(curYear, startMonth, 1));
                    this.queryForm.endTime = this.localeTimeString(new Date(curYear, startMonth + 3, 1));//下季度第一天0点
                } else if (date == "year") {//本年的开始结束时间
                    this.yearForm.startTime = this.localeTimeString(new Date(now.getFullYear(), 0, 1));
                    this.yearForm.endTime = this.localeTimeString(new Date(now.getFullYear() + 1, 0, 1));
                }
            },
            //时间字符串格式化
            localeTimeString(time) {
                if (time != null && time != "") {
                    time = moment(time).format('YYYY-MM-DD HH:mm:ss');
                    return time;
                } else {
                    return "";
                }
            },
        }
    }
</script>
<style scoped>
    .intergral-con {
        width: 1100px;
        margin: auto;
    }

    .intergral-con-top {
        font-size: 14px;
        background: #fff;
        padding: 16px 0;
        text-indent: 10px;
        box-shadow: 0 0 10px #ccc;
        margin: 0 10px;
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

    .self-define {
        margin-right: 14px;
    }

    .serch-btn {
        vertical-align: middle;
        margin-left: 10px;
        cursor: pointer;
    }

    .intergral-data-detail {
        margin: 20px 8px;
    }
</style>

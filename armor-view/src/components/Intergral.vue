<template>
    <div class="intergral-con">

        <div class="intergral-data-detail">
            <el-table :data="quarter" stripe style="width: 50%;float:left">
              <el-table-column label="季度排名" align="center" :render-header="createQuarterHistory">
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
            <el-table-column label="年度排名" align="center" :render-header="createYearHistory">
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
                yearList:{
                    curYear:'',
                    prevYear:'',
                    nextYear:''
                },
                quarterShow:{
                    prev:'',
                    next:''
                },
                yearShow:{
                    prev:'',
                    next:''
                },
                quarterTime:'',
                yearTime:'',
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
              if(date=="quarter"){//本季度
                this.getDateString("quarter");
                Http.zsyGetHttp(Http.API_URI.INTEGRAL,this.queryForm, (res) => {
                  this.quarter= res.data;
                  this.countQuarterHistory(this.queryForm);
                  if(this.quarter.length<1){
                    console.log(11)
                  }
                })
              }else if(date=="year"){//今年
                this.getDateString("year");
                Http.zsyGetHttp(Http.API_URI.INTEGRAL,this.yearForm, (res) => {
                  this.year= res.data;
                  this.countYearHistory(this.yearForm);
                })
              }
            },
            //获取本年,季度,月的开始结束日期
            getDateString(date) {
                let now = new Date();
                let curMonth = now.getMonth();
                let curYear = now.getFullYear();
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
                    this.quarterTime = this.localeStringTime(new Date(curYear, startMonth, 1))+'—'+ this.localeStringTime(new Date(curYear, startMonth + 3, 1)-1);
                } else if (date == "year") {//本年的开始结束时间
                    this.yearForm.startTime = this.localeTimeString(new Date(now.getFullYear(), 0, 1));
                    this.yearForm.endTime = this.localeTimeString(new Date(now.getFullYear() + 1, 0, 1));
                    this.yearTime = this.localeStringTime(new Date(curYear, 0, 1))+'—'+ this.localeStringTime(new Date(curYear+1,0,1)-1);
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
            //时间字符串格式化
            localeDayString(time) {
                if (time != null && time != "") {
                    time = moment(time).format('YYYY-MM-DD');
                    return time;
                } else {
                    return "";
                }
            },
            //时间字符串格式化
            localeStringTime(time) {
                if (time != null && time != "") {
                    time = moment(time).format('YYYY.MM.DD');
                    return time;
                } else {
                    return "";
                }
            },
            countYearHistory(time){
                Http.zsyGetHttp('/integral/count',time, (res) => {
                    if(res.data.prev==0){
                        this.yearShow.prev = 'none'
                    }else{
                        this.yearShow.prev = ''
                    }
                    if(res.data.next==0){
                        this.yearShow.next = 'none'
                    }else{
                        this.yearShow.next = ''
                    }
                })
            },
            countQuarterHistory(time){
                Http.zsyGetHttp('/integral/count',time, (res) => {
                    if(res.data.prev==0){
                        this.quarterShow.prev = 'none'
                    }else{
                        this.quarterShow.prev = ''
                    }
                    if(res.data.next==0){
                        this.quarterShow.next = 'none'
                    }else{
                        this.quarterShow.next = ''
                    }
                })
            },
            prevQuarterHistory(){
                var currQuarter = new Date(this.quarterTime.split('—')[0])
                this.queryForm.endTime = new Date(currQuarter.getFullYear(),currQuarter.getMonth(),1);
                this.queryForm.startTime = new Date(currQuarter.getFullYear(),currQuarter.getMonth()-3,1);
                this.quarterTime = this.localeStringTime(this.queryForm.startTime)+'—'+ this.localeStringTime(new Date(this.queryForm.endTime)-1);
                Http.zsyGetHttp(Http.API_URI.INTEGRAL,this.queryForm, (res) => {
                    this.quarter= res.data;
                    this.countQuarterHistory(this.queryForm);
                })
            },
            nextQuarterHistory(){
                var currQuarter = new Date(this.quarterTime.split('—')[1])
                this.queryForm.startTime = new Date(currQuarter.getFullYear(),currQuarter.getMonth()+1,1);
                this.queryForm.endTime = new Date(currQuarter.getFullYear(),currQuarter.getMonth()+4,1);
                this.quarterTime = this.localeStringTime(this.queryForm.startTime)+'—'+ this.localeStringTime(new Date(this.queryForm.endTime)-1);
                Http.zsyGetHttp(Http.API_URI.INTEGRAL,this.queryForm, (res) => {
                    this.quarter= res.data;
                    this.countQuarterHistory(this.queryForm);
                })
            },
            prevYearHistory(){
                var currQuarter = new Date(this.yearTime.split('—')[0])
                this.queryForm.endTime = new Date(currQuarter.getFullYear(),0,1);
                this.queryForm.startTime = new Date(currQuarter.getFullYear()-1,0,1);
                this.yearTime = this.localeStringTime(this.queryForm.startTime)+'—'+ this.localeStringTime(new Date(this.queryForm.endTime)-1);
                Http.zsyGetHttp(Http.API_URI.INTEGRAL,this.queryForm, (res) => {
                    this.year= res.data;
                    this.countYearHistory(this.queryForm);
                })
            },
            nextYearHistory(){
                var currQuarter = new Date(this.yearTime.split('—')[1])
                this.queryForm.startTime = new Date(currQuarter.getFullYear()+1,0,1);
                this.queryForm.endTime = new Date(currQuarter.getFullYear()+2,0,1);
                this.yearTime = this.localeStringTime(this.queryForm.startTime)+'—'+ this.localeStringTime(new Date(this.queryForm.endTime)-1);

                Http.zsyGetHttp(Http.API_URI.INTEGRAL,this.queryForm, (res) => {
                    this.year= res.data;
                    this.countYearHistory(this.queryForm);
                })
            },
            createQuarterHistory(createElement){
                return createElement(
                    'div', {
                        'class': 'renderTableHead'
                    }, [
                        createElement(
                            'el-button', {
                                attrs: {
                                    type: 'text',
                                    style:'color:#ffffff;float: left;font-size:10px;display:'+this.quarterShow.prev,
                                },
                                on: {
                                    click: this.prevQuarterHistory,
                                }
                            }, [
                                '上一季度'
                            ]
                        ),
                        createElement(
                            'el-button' , {
                                attrs: {
                                    type: 'text',
                                    style:'color:#ffffff;cursor:default',
                                }
                            }, [
                                '季度排名('+this.quarterTime+')'
                            ]
                        ),
                        createElement(
                            'el-button', {
                                attrs: {
                                    type: 'text',
                                    style:'color:#ffffff;float: right;font-size:10px;display:'+this.quarterShow.next,
                                },
                                on: {
                                    click: this.nextQuarterHistory,
                                }
                            }, [
                                '下一季度'
                            ]
                        ),
                    ]
                );

            },
            createYearHistory(createElement){
                return createElement(
                    'div', {
                        'class': 'renderTableHead'
                    }, [
                        createElement(
                            'el-button', {
                                attrs: {
                                    type: 'text',
                                    style:'color:#ffffff;float: left;font-size:10px;display:'+this.yearShow.prev,
                                },
                                on: {
                                    click: this.prevYearHistory,
                                }
                            }, [
                                '上一年度'
                            ]
                        ),
                        createElement(
                            'el-button' , {
                                attrs: {
                                    type: 'text',
                                    style:'color:#ffffff;cursor:default',
                                }
                            }, [
                                '年度排名('+this.yearTime+')'
                            ]
                        ),
                        createElement(
                            'el-button', {
                                attrs: {
                                    type: 'text',
                                    style:'color:#ffffff;float: right;font-size:10px;display:'+this.yearShow.next,
                                },
                                on: {
                                    click: this.nextYearHistory,
                                }
                            }, [
                                '下一年度'
                            ]
                        ),
                    ]
                );

            },

        }
    }
</script>
<style scoped>
    .intergral-con {
        width: 1100px;
        margin: auto;
    }

    .intergral-data-detail {
        margin: 20px 8px;
    }
</style>

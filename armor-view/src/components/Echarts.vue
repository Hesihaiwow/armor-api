<template>
    <div>
        <div class="stats-con">
            <div class="steps">
                <div class="card-title-con" style="font-size: 20px;margin-top: -20px;margin-left: 20px;">
                    年度需求分析
                    <el-date-picker
                            style="margin-left: 20px"
                            v-model="whichYear"
                            align="right"
                            type="year"
                            placeholder="选择年">
                    </el-date-picker>
                    <el-button type="primary" @click="selectFbByYear">查询</el-button>
                </div>
                <div class="steps-body">
                    <div id="myChart1" :style="{width:'600px',height:'400px',float:'left'}"></div>
                    <div style="width: 300px;">
                        <ol type="A">
                            <li>年度需求总数:    {{fbTotalCount}}</li>
                            <li>来自学管端需求:  {{fromCoach}}</li>
                            <li>其他需求:        {{other}}</li>
                        </ol>
                    </div>
                </div>
            </div>
            <div class="vertical"></div>

        </div>
        <div class="stats-con">
            <div class="steps" style="width: 1000px">
            <div class="card-title-con" style="font-size: 20px;margin-top: -20px;margin-left: 20px;">
                年度任务分析
                <el-date-picker
                        style="margin-left: 20px"
                        v-model="whichYear2"
                        align="right"
                        type="year"
                        placeholder="选择年">
                </el-date-picker>
                <el-button type="primary" @click="selectTaskByYear">查询</el-button>
            </div>
            <div class="steps-body">
                <div id="myChart2" :style="{width:'600px',height:'400px',left:'240px'}"></div>
            </div>
        </div>
        </div>
        <div class="stats-con">
            <div class="steps" style="width: 1000px">
                <div  style="float: left;width: 33%;color: red;font-size: 20px;text-align: center;border: black">
                    得分率变化趋势对比分析
                </div>
                <div style="float: left;width: 33%;color: blue;font-size: 20px;text-align: center">第二个</div>
                <div style="float: left;width: 34%;color: green;font-size: 20px;text-align: center">第三个</div>
                <!--<div class="steps-body">
                    <div id="myChart3" :style="{width:'600px',height:'400px',left:'80px'}"></div>
                </div>-->
            </div>
        </div>
    </div>
</template>

<script>
    import Http from '../lib/Http'
    import moment from 'moment';
    import helper from "../lib/Helper";
    export default {
        name: "Echarts",
        data(){
            return{
                // 年度需求
                whichYear:'2018',
                fbTotalCount:0,
                fromCoach:0,
                other:0,
                feedbackData:[],
                fbReqDTO:{
                    whichYear:'2018'
                },

                //年度任务
                whichYear2:'2018',
                taskTotalCount:0,
                multiCount:0,
                singleCount:0,
                taskData:[],
                taskReqDTO:{
                    whichYear:'2018'
                }
            }
        },
        created(){
          this.fetchAnnualFeedback();
          this.fetchAnnualTaskData();
        },
        beforeMount(){
            this.$root.eventBus.$emit("handleTabSelected", "echarts");
        },
        mounted(){
            // this.drawLine3();
        },
        methods:{
            drawLine1(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart1'))
                // 绘制图表
                myChart.setOption({
                    title: { text: '年度需求总数: '+ this.fbTotalCount,x:'center' },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['学管端','其他']
                    },
                    series : [
                        {
                            name: '需求来源',
                            type: 'pie',
                            radius : '75%',
                            center: ['50%', '60%'],
                            data:this.feedbackData,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
            },
            drawLine2(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart2'))
                // 绘制图表
                myChart.setOption({
                    title: { text: '年度任务总数: '+ this.taskTotalCount,x:'center' },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['单人任务','多人任务']
                    },
                    series : [
                        {
                            name: '需求来源',
                            type: 'pie',
                            radius : '75%',
                            center: ['50%', '60%'],
                            /*data:[
                                {value:50,name:'单人任务'},
                                {value:234,name:'多人任务'}
                            ],*/
                            data:this.taskData,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
            },
            drawLine3(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart3'))
                // 绘制图表
                myChart.setOption({
                    title: { text: '在Vue中使用echarts' },
                    tooltip: {},
                    xAxis: {
                        data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
                    },
                    yAxis: {},
                    series: [{
                        name: '销量',
                        type: 'bar',
                        data: [5, 20, 36, 10, 10, 20]
                    }]
                });
            },
            //查询年度需求
            fetchAnnualFeedback(){
                Http.zsyPostHttp('/data/annual/feedback-num',this.fbReqDTO,(res)=>{
                    if (res){
                        this.fbTotalCount = res.data.totalNum;
                        this.fromCoach = res.data.fromCoachNum;
                        this.other = res.data.otherNum;
                        const fbData = {};
                        fbData.value = this.fromCoach;
                        fbData.name = '学管端';
                        this.feedbackData.push(fbData)
                        const fbData2 = {};
                        fbData2.value = this.other;
                        fbData2.name = '其他';
                        this.feedbackData.push(fbData2)
                        this.drawLine1()
                    }
                })
            },
            selectFbByYear(){
                if (this.whichYear){
                    this.fbReqDTO.whichYear = moment(this.whichYear).format("YYYY-MM-DD")
                    this.fbReqDTO.whichYear = this.fbReqDTO.whichYear.substring(0,4)
                }
                this.feedbackData = [];
                this.fetchAnnualFeedback()
            },

            //查询年度任务数据
            fetchAnnualTaskData(){
                Http.zsyPostHttp('/data/annual/task-num',this.taskReqDTO,(res) =>{
                    if (res){
                        this.taskTotalCount = res.data.totalNum;
                        this.multiCount = res.data.multiTaskNum;
                        this.singleCount = res.data.singleTaskNum;
                        const tData = {};
                        tData.value = this.multiCount;
                        tData.name = '多人任务';
                        this.taskData.push(tData);
                        const tData2 = {};
                        tData2.value = this.singleCount;
                        tData2.name = '单人任务';
                        this.taskData.push(tData2);
                        this.drawLine2()
                    }
                })
            },
            selectTaskByYear(){
                if(this.whichYear2){
                    this.taskReqDTO.whichYear = moment(this.whichYear2).format("YYYY-MM-DD")
                    this.taskReqDTO.whichYear = this.taskReqDTO.whichYear.substring(0,4)
                }
                this.taskData = [];
                this.fetchAnnualTaskData()
            }
        }
    }
</script>

<style scoped>
    .steps {
        display: inline;
        flex-direction: column;
        margin-bottom: 20px;
        padding-bottom: 10px;
        background: #fff;
    }

    .card-title-con {
        position: relative;
        padding-left: 10px;
        margin: 0 0 16px;
        font-weight: normal;
        line-height: 40px;
        font-size: 16px;
        border-bottom: 1px solid #eee;
        color: #304156;
    }

    .steps-body {
        display: flex;
        flex-direction: row;
    }

    .stats-con {
        width: 1100px;
        font-size: 14px;
        background: #fff;
        padding: 30px 0;
        box-shadow: 0 0 10px #ccc;
        margin: auto;
        height: 500px;
    }

    .vertical {
        margin-top: 15px;
        margin-bottom: 15px;
        width: 1px;
        background: rgb(144, 147, 153);
    }

</style>

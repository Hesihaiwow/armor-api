<template>
    <div>
        <div class="stats-con" v-show="userRole>0">
            <div class="steps" style="width: 1000px">
                <div class="card-title-con" style="font-size: 20px;margin-top: -20px;margin-left: 20px;">
                    年度个人数据
                    <el-date-picker
                            style="margin-left: 20px"
                            v-model="whichYear1"
                            align="right"
                            type="year"
                            placeholder="选择年">
                    </el-date-picker>
                    <el-button type="primary" @click="selectPersonDataByYear">查询</el-button>
                </div>
                <div class="steps-body">
                    <div id="myChart1" :style="{width:'600px',height:'400px',left:'40px',float:'left'}"></div>
                    <div style="width: 300px;margin-top: 100px" v-if="personData">
                        <ol>
                            <li class="my-li"><span class="fb-span">个人完成任务总数:</span><span>{{personData.taskNum}}个</span></li>
                            <li class="my-li"><span class="fb-span">个人完成任务总时长:</span><span>{{personData.taskTime}}小时</span></li>
                            <!--<li class="my-li"><span class="fb-span">个人请假次数:</span><span>{{personData.vacationCount}}次</span></li>-->
                            <!--<li class="my-li"><span class="fb-span">个人请假时长:</span><span>{{personData.vacationTime}}小时</span></li>-->
                            <li class="my-li"><span class="fb-span">个人任务数:</span><span>{{personData.singleTaskNum}}个</span></li>
                            <li class="my-li"><span class="fb-span">多人任务数:</span><span>{{personData.multiTaskNum}}个</span></li>
                            <li><span class="fb-span">耗时最长任务:</span><br/>
                                <a style="cursor: pointer;color: #69C8FA;text-decoration: underline" @click="fetchTaskDetail(personData.mostTimeTaskId,1)">{{personData.mostTimeTaskName}}</a></li>
                            <li><span class="fb-span">耗时最短任务:</span><br/>
                                <a style="cursor: pointer;color: #69C8FA;text-decoration: underline" @click="fetchTaskDetail(personData.lessTimeTaskId,0)">{{personData.lessTimeTaskName}}</a></li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
        <div class="stats-con" v-show="userRole>0">
            <div class="steps" style="width: 1000px">
                <div class="card-title-con" style="font-size: 20px;margin-top: -20px;margin-left: 20px;">
                    年度请假分析
                    <el-date-picker
                            style="margin-left: 20px"
                            v-model="whichYear2"
                            align="right"
                            type="year"
                            placeholder="选择年">
                    </el-date-picker>
                    <el-button type="primary" @click="selectVacationByYear">查询</el-button>
                </div>
                <div class="steps-body" style="height: 600px;margin-left: 120px">
                    <div id="myChart2" :style="{width:'600px',height:'400px',left:'80px',float:'left',marginTop:'50px'}"></div>
                    <div style="width: 300px;margin-left: 100px">
                        <ol type="A" style="margin-left: -250px">
                            <li><span class="task-span">年度请假总数:</span><span style="margin-left: 71px">{{vacationData.totalCount}}次</span></li>
                            <li><span class="task-span">年度请假时长:</span><span style="margin-left: 71px">{{vacationData.totalTime}}小时</span></li>
                        </ol>
                    </div>
                </div>
                <!--<div v-else class="steps-body" style="height: 300px">
                    <h1 style="text-align: center;width: 100%;font-size: 50px">暂无数据</h1>
                </div>-->
            </div>
        </div>

        <el-dialog
                title="任务详情"
                top="10%"
                :visible.sync="showTaskVisible"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                custom-class="myDialog"
                size="tiny">
            <el-form>
                <el-form-item class="task-form" label="任务名称：">{{taskDetail.name}}</el-form-item>
                <!--<el-form-item class="task-form" label="任务描述：">{{taskDetail.description}}</el-form-item>-->
                <el-form-item class="task-form" label="项目：">{{taskDetail.projectName}}</el-form-item>
                <el-form-item class="task-form" label="阶段：">{{taskDetail.stageName}}</el-form-item>
                <el-form-item class="task-form"  label="优先级："><span v-for="item in priorityList"
                                                                    v-if="item.value == taskDetail.priority">{{item.label}}</span>
                </el-form-item>
                <el-form-item class="task-form" label="开始时间：">{{taskDetail.createTime | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="截止时间：">{{taskDetail.endTime | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="完成时间：">{{taskDetail.completeTime | formatDate}}</el-form-item>
                <el-form-item label="任务总时长" v-if="showTaskTime && personData">{{personData.mostTime}}小时</el-form-item>
                <el-form-item label="任务总时长" v-if="!showTaskTime && personData">{{personData.lessTime}}小时</el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button v-show="showTaskTime" type="primary" @click="toTask(personData.mostTimeTaskId)">查看任务</el-button>
                <el-button v-show="!showTaskTime" type="primary" @click="toTask(personData.lessTimeTaskId)">查看任务</el-button>
            </span>
        </el-dialog>

    </div>
</template>

<script>
    import Http from '../lib/Http'
    import moment from 'moment';
    import helper from "../lib/Helper";
    export default {
        name: "SummaryNav",
        data(){
            return{

                projectTaskList:[],
                taskDetail:{},
                showTaskVisible:false,
                priorityList: [
                    {label: '普通', value: 0},
                    {label: '紧急', value: 1},
                    {label: '非常紧急', value: 2},
                ],
                showTaskTime:false,

                //年度个人数据
                whichYear1:'2019',
                personDataReqDTO:{
                    whichYear:'2019'
                },
                personData:{
                    taskNum:0,
                    taskTime:0,
                    vacationCount:0,
                    vacationTime:0,
                    singleTaskNum:0,
                    multiTaskNum:0,
                    mostTimeTaskId:'',
                    mostTimeTaskName:'',
                    mostTime:0,
                    lessTimeTaskId:'',
                    lessTimeTaskName:'',
                    lessTime:0
                },
                personTaskData:[],

                //年度请假
                // showVacationData:false,
                whichYear2:'2019',
                vacationReqDTO:{
                    whichYear:'2019'
                },
                vacationData:{
                    totalCount:0,
                    totalTime:0
                },
                vacationCountList:[],
                vacationTimeList:[],

            }
        },
        computed:{
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole < 2;
            },
            userRole() {
                let userRole = helper.decodeToken().userRole;
                return userRole;
            }
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
        created(){
          this.fetchPersonData();
          this.fetchPersonEveryMonthVacation();
        },
        beforeMount(){
            this.$root.eventBus.$emit("handleTabSelected", "summaryNav");
        },
        methods:{
            //查询负责人
            fetchUserList() {
                let vm = this
                Http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.chargeManList = resp.data
                })
            },
            drawLine2(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart2'))
                // 绘制图表
                myChart.setOption({
                    title: { text: '年度请假分布',x:'center' },
                    tooltip: {
                        trigger: 'axis',
                        axisPointer: {
                            type: 'shadow'
                        }
                    },
                    legend: {
                        left: 'left',
                        data: ['次数', '时长']
                    },
                    xAxis: {
                        data: ["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"]
                    },
                    yAxis: [
                        {
                            type: 'value',
                            name: '次数',
                            min: 0,
                            interval: 5,
                            splitLine:{
                                show:false
                            },
                        },
                        {
                            type: 'value',
                            name: '时长',
                            min: 0,
                            interval: 50,
                            axisLabel: {
                                formatter: '{value} h'
                            },
                            splitLine:{
                                show:false
                            },
                        }
                    ],
                    series: [
                        {
                            name: '次数',
                            type: 'bar',
                            barGap: 0,
                            data: this.vacationCountList
                        },
                        {
                            name: '时长',
                            type: 'bar',
                            data: this.vacationTimeList,
                            yAxisIndex:1
                        }
                    ]
                });
            },
            drawLine1(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart1'))
                // 绘制图表
                myChart.setOption({
                    title: { text: '个人年度任务',x:'center' },
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
                            name: '任务类型',
                            type: 'pie',
                            radius : '75%',
                            center: ['50%', '60%'],
                            data:this.personTaskData,
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

            //查询任务详情
            fetchTaskDetail(id,num){
                Http.zsyGetHttp(`/task/detail/${id}`, {},(res)=>{
                    if (res){
                        this.taskDetail = res.data;
                        this.showTaskVisible = true;
                        if (num == 1){
                            this.showTaskTime = true;
                        } else {
                            this.showTaskTime = false;
                        }
                    }
                })
            },


            //跳转到任务
            toTask(taskId){
                if (taskId != null && taskId != '') {
                    this.$router.push({name: 'taskListFormComments', params: {taskId: taskId}})
                }
            },


            //查询个人年度数据
            fetchPersonData(){
                Http.zsyPostHttp('/data/annual/person-data',this.personDataReqDTO,(res)=>{
                    if (res){
                        this.personData = res.data
                        const tData = {};
                        tData.value = this.personData.multiTaskNum;
                        tData.name = '多人任务';
                        this.personTaskData.push(tData);
                        const tData2 = {};
                        tData2.value = this.personData.singleTaskNum;
                        tData2.name = '单人任务';
                        this.personTaskData.push(tData2);
                        this.drawLine1()
                    }
                })
            },
            selectPersonDataByYear(){
                if (this.whichYear1){
                    this.personDataReqDTO.whichYear = moment(this.whichYear1).format("YYYY-MM-DD")
                    this.personDataReqDTO.whichYear = this.personDataReqDTO.whichYear.substring(0,4)
                }
                this.personData = null;
                this.personTaskData = []
                this.fetchPersonData()
            },

            //查看年度个人请假情况
            fetchPersonEveryMonthVacation(){
                Http.zsyPostHttp('/data/annual/person-vacation/month',this.vacationReqDTO,(res)=>{
                    if (res){
                        this.vacationCountList = res.data.vacationCountList;
                        this.vacationTimeList = res.data.vacationTimeList;
                        this.vacationCountList.forEach(count =>{
                            this.vacationData.totalCount = this.vacationData.totalCount + count;
                        })
                        this.vacationTimeList.forEach(time => {
                            this.vacationData.totalTime = this.vacationData.totalTime + time;
                        })
                        this.drawLine2();
                    }
                })
            },

            selectVacationByYear(){
                if (this.whichYear2){
                    this.vacationReqDTO.whichYear = moment(this.whichYear2).format("YYYY-MM-DD")
                    this.vacationReqDTO.whichYear = this.vacationReqDTO.whichYear.substring(0,4)
                }
                this.vacationData.totalTime = 0;
                this.vacationData.totalCount = 0;
                this.vacationCountList = [];
                this.vacationTimeList = [];
                this.fetchPersonEveryMonthVacation();
            },
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

    .fb-span{
        width: 200px;
        color: black;
        font-size: 20px;
    }

    .task-span{
        font-size: 18px;
        width: 200px;
        color: black;
    }

    .my-li{
        display: flex;
        justify-content:space-between;
    }

</style>

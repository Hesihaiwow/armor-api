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
                    <div id="myChart1" :style="{width:'600px',height:'400px',left:'40px',float:'left'}"></div>
                    <div style="width: 300px;height: 300px;margin-top: 100px">
                        <ol type="A">
                            <li class="my-li"><span class="fb-span">年度需求总数:</span><span>{{fbTotalCount}}个</span></li>
                            <li class="my-li"><span class="fb-span">来自学管端需求:</span><span>{{fromCoach}}个</span></li>
                            <li class="my-li"><span class="fb-span">其他需求:</span><span>{{other}}个</span></li>
                            <hr>
                            <li class="my-li"><span class="fb-span">个人建议:</span><span>{{diffTypeFeedback.personSuggestion}}个</span></li>
                            <li class="my-li"><span class="fb-span">市场反馈:</span><span>{{diffTypeFeedback.marketDemand}}个</span></li>
                            <li class="my-li"><span class="fb-span">公司决策:</span><span>{{diffTypeFeedback.companyDecision}}个</span></li>
                            <hr>
                            <li class="my-li"><span class="fb-span">单月最多:</span><span>{{maxAndMinFbData.maxMonth}}月;	&nbsp;	&nbsp;共:</span><span>{{maxAndMinFbData.maxFbNum}}个</span></li>
                            <li class="my-li"><span class="fb-span">单月最少:</span><span>{{maxAndMinFbData.minMonth}}月;	&nbsp;	&nbsp;共:</span><span>{{maxAndMinFbData.minFbNum}}个</span></li>
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
                <div id="myChart2" :style="{width:'600px',height:'400px',left:'40px',float:'left'}"></div>
                <div style="width: 300px;">
                    <ol type="A">
                        <li><span class="task-span">年度任务总数:</span><span style="margin-left: 71px">{{taskTotalCount}}个</span></li>
                        <li><span class="task-span">多人任务:</span><span style="margin-left: 108px">{{multiCount}}个</span></li>
                        <li><span class="task-span">个人任务:</span><span style="margin-left: 108px">{{singleCount}}个</span></li>
                        <li><span class="task-span">任务总时长:</span><span style="margin-left: 94px">{{taskTotalTime}}小时</span></li>
                        <li v-if="mostTimeTask.id != null"><span class="task-span">耗时最长任务:</span><br/>
                            <a style="cursor: pointer;color:#69C8FA;text-decoration: underline" @click="fetchTaskDetail(mostTimeTask.id,1)">{{mostTimeTask.name}}</a></li>
                        <li v-if="lessTimeTask.id != null"><span class="task-span">耗时最短任务:</span>
                            <br/><a style="cursor: pointer;color: #69C8FA;text-decoration: underline" @click="fetchTaskDetail(lessTimeTask.id,0)">{{lessTimeTask.name}}</a></li>
                        <hr>
                    </ol>
                    <ol v-for="item in projectTaskList" v-show="projectTaskList != null && projectTaskList.length > 0">
                        <li>
                            <div class="task-span" style="float: left;text-align: left">{{item.projectName}}:</div>
                            <div style="float: left;">{{item.taskNum}}个</div>
                        </li>
                    </ol>
                </div>
            </div>
        </div>
        </div>
        <div class="stats-con">
            <div class="steps" style="width: 1000px">
                <div class="card-title-con" style="font-size: 20px;margin-top: -20px;margin-left: 20px;">
                    年度请假分析
                    <el-date-picker
                            style="margin-left: 20px"
                            v-model="whichYear3"
                            align="right"
                            type="year"
                            placeholder="选择年">
                    </el-date-picker>
                    <el-button type="primary" @click="selectVacationByYear">查询</el-button>
                </div>
                <div class="steps-body" style="height: 600px;margin-left: 120px">
                    <div id="myChart3" :style="{width:'600px',height:'400px',left:'80px',float:'left',marginTop:'50px'}"></div>
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
        <div class="stats-con" v-show="userRole>0">
            <div class="steps" style="width: 1000px">
                <div class="card-title-con" style="font-size: 20px;margin-top: -20px;margin-left: 20px;">
                    年度个人数据
                    <el-date-picker
                            style="margin-left: 20px"
                            v-model="whichYear4"
                            align="right"
                            type="year"
                            placeholder="选择年">
                    </el-date-picker>
                    <el-button type="primary" @click="selectPersonDataByYear">查询</el-button>
                </div>
                <div class="steps-body">
                    <div id="myChart4" :style="{width:'600px',height:'400px',left:'40px',float:'left'}"></div>
                    <div style="width: 300px;margin-top: 100px" v-if="personData">
                        <ol>
                            <li class="my-li"><span class="fb-span">个人完成任务总数:</span><span>{{personData.taskNum}}个</span></li>
                            <li class="my-li"><span class="fb-span">个人完成任务总时长:</span><span>{{personData.taskTime}}小时</span></li>
                            <li class="my-li"><span class="fb-span">个人请假次数:</span><span>{{personData.vacationCount}}次</span></li>
                            <li class="my-li"><span class="fb-span">个人请假时长:</span><span>{{personData.vacationTime}}小时</span></li>
                            <hr>
                            <li class="my-li"><span class="fb-span">个人任务数:</span><span>{{personData.singleTaskNum}}个</span></li>
                            <li class="my-li"><span class="fb-span">多人任务数:</span><span>{{personData.multiTaskNum}}个</span></li>
                            <li><span class="fb-span">耗时最长任务:</span><br/>
                                <a style="cursor: pointer;color: #69C8FA;text-decoration: underline" @click="fetchTaskDetail2(personData.mostTimeTaskId,1)">{{personData.mostTimeTaskName}}</a></li>
                            <li><span class="fb-span">耗时最短任务:</span><br/>
                                <a style="cursor: pointer;color: #69C8FA;text-decoration: underline" @click="fetchTaskDetail2(personData.lessTimeTaskId,0)">{{personData.lessTimeTaskName}}</a></li>
                        </ol>
                    </div>
                </div>
            </div>
        </div>
        <div class="stats-con" v-show="userRole===0">
            <div class="steps" style="width: 1000px">
                <div class="card-title-con" style="font-size: 20px;margin-top: -20px;margin-left: 20px;">
                    年度个人数据
                    <el-date-picker
                            style="margin-left: 20px"
                            v-model="whichYear5"
                            align="right"
                            type="year"
                            placeholder="选择年">
                    </el-date-picker>
                    <span>姓名:</span>
                    <el-select clearable filterable no-match-text=" " v-model="chargeMan" placeholder="请选择"
                               size="small" style="width:100px;">
                        <el-option v-for="item in chargeManList" :key="item.id" :label="item.name"
                                   :value="item.id"></el-option>
                    </el-select>
                    <el-button type="primary" @click="selectAdminPersonData">查询</el-button>
                </div>
                <div class="steps-body" v-if="personDataVisible">
                    <div id="myChart5" :style="{width:'600px',height:'400px',left:'40px',float:'left'}"></div>
                    <div style="width: 300px;margin-top: 100px" v-if="personData1">
                        <ol>
                            <li class="my-li"><span class="fb-span">个人完成任务总数:</span><span>{{personData1.taskNum}}个</span></li>
                            <li class="my-li"><span class="fb-span">个人完成任务总时长:</span><span>{{personData1.taskTime}}小时</span></li>
                            <li class="my-li"><span class="fb-span">个人请假次数:</span><span>{{personData1.vacationCount}}次</span></li>
                            <li class="my-li"><span class="fb-span">个人请假时长:</span><span>{{personData1.vacationTime}}小时</span></li>
                            <hr>
                            <li class="my-li"><span class="fb-span">个人任务数:</span><span>{{personData1.singleTaskNum}}个</span></li>
                            <li class="my-li"><span class="fb-span">多人任务数:</span><span>{{personData1.multiTaskNum}}个</span></li>
                            <li><span class="fb-span">耗时最长任务:</span><br/>
                                <a style="cursor: pointer;color: #69C8FA;text-decoration: underline" @click="fetchTaskDetail3(personData1.mostTimeTaskId,1)">{{personData1.mostTimeTaskName}}</a></li>
                            <li><span class="fb-span">耗时最短任务:</span><br/>
                                <a style="cursor: pointer;color: #69C8FA;text-decoration: underline" @click="fetchTaskDetail3(personData1.lessTimeTaskId,0)">{{personData1.lessTimeTaskName}}</a></li>
                        </ol>
                    </div>
                </div>
                <div v-else class="steps-body" style="height: 300px">
                    <h1 style="text-align: center;width: 100%;font-size: 50px">暂无数据</h1>
                </div>
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
                <el-form-item class="task-form" label="项目：">{{taskDetail.projectName}}</el-form-item>
                <el-form-item class="task-form" label="阶段：">{{taskDetail.stageName}}</el-form-item>
                <el-form-item class="task-form"  label="优先级："><span v-for="item in priorityList"
                                                                    v-if="item.value == taskDetail.priority">{{item.label}}</span>
                </el-form-item>
                <el-form-item class="task-form" label="开始时间：">{{taskDetail.createTime | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="截止时间：">{{taskDetail.endTime | formatDate}}</el-form-item>
                <el-form-item class="task-form" label="完成时间：">{{taskDetail.completeTime | formatDate}}</el-form-item>
                <el-form-item label="任务总时长" v-show="showTaskTime">{{mostTimeTask.hours}}小时</el-form-item>
                <el-form-item label="任务总时长" v-show="!showTaskTime">{{lessTimeTask.hours}}小时</el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button v-show="showTaskTime" type="primary" @click="toTask(mostTimeTask.id)">查看任务</el-button>
                <el-button v-show="!showTaskTime" type="primary" @click="toTask(lessTimeTask.id)">查看任务</el-button>
            </span>
        </el-dialog>

        <el-dialog
                title="任务详情"
                top="10%"
                :visible.sync="showTaskVisible2"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                custom-class="myDialog"
                size="tiny">
            <el-form>
                <el-form-item class="task-form" label="任务名称：">{{taskDetail.name}}</el-form-item>
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
        <el-dialog
                title="任务详情"
                top="10%"
                :visible.sync="showTaskVisible3"
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
                <el-form-item label="任务总时长" v-show="showTaskTime">{{personData1.mostTime}}小时</el-form-item>
                <el-form-item label="任务总时长" v-show="!showTaskTime">{{personData1.lessTime}}小时</el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button v-show="showTaskTime" type="primary" @click="toTask(personData1.mostTimeTaskId)">查看任务</el-button>
                <el-button v-show="!showTaskTime" type="primary" @click="toTask(personData1.lessTimeTaskId)">查看任务</el-button>
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
                // 年度需求
                whichYear:'2018',
                fbTotalCount:0,
                fromCoach:0,
                other:0,
                feedbackData:[],
                fbReqDTO:{
                    whichYear:'2018'
                },
                maxAndMinFbData:{
                    maxMonth:1,
                    maxFbNum:0,
                    minMonth:12,
                    minFbNum:0
                },
                diffTypeFeedback:{
                    personSuggestion:0,
                    marketDemand:0,
                    companyDecision:0
                },

                //年度任务
                whichYear2:'2018',
                taskTotalCount:0,
                multiCount:0,
                singleCount:0,
                taskData:[],
                taskReqDTO:{
                    whichYear:'2018'
                },
                taskTotalTime:0,
                mostTimeTask:{
                    id:'',
                    name:'',
                    hours:0
                },
                lessTimeTask:{
                    id:'',
                    name:'',
                    hours:0
                },
                projectTaskList:[],
                taskDetail:{},
                showTaskVisible:false,
                showTaskVisible2:false,
                showTaskVisible3:false,
                priorityList: [
                    {label: '普通', value: 0},
                    {label: '紧急', value: 1},
                    {label: '非常紧急', value: 2},
                ],
                showTaskTime:false,

                //年度请假
                whichYear3:'2018',
                vacationReqDTO:{
                    whichYear:'2018'
                },
                vacationData:{
                    totalCount:0,
                    totalTime:0
                },
                vacationCountList:[],
                vacationTimeList:[],

                //年度个人数据
                whichYear4:'2018',
                personDataReqDTO:{
                    whichYear:'2018'
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

                //管理员查看
                personDataVisible:false,
                chargeManList:[],
                chargeMan:'',
                whichYear5:'2018',
                adminReqDTO:{
                    whichYear:'2018',
                },
                personData1:{
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
                personTaskData1:[],

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
          // this.fetchUserList()
          // this.fetchAnnualFeedback();
          // this.fetchAnnualTaskData();
          // this.fetchMaxAndMinFbMonth();
          // this.fetchDiffTypeFbNum();
          // this.fetchAnnualProjectTaskNum();
          // this.fetchAnnualTaskTotalHours();
          // this.fetchAnnualVacation();
          // this.fetchEveryMonthVacation();
          this.fetchPersonData();
        },
        beforeMount(){
            this.$root.eventBus.$emit("handleTabSelected", "summaryNav");
        },
        mounted(){
        },
        methods:{
            //查询负责人
            fetchUserList() {
                let vm = this
                Http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.chargeManList = resp.data
                    // vm.chargeManList = vm.chargeManList.splice(3,(vm.chargeManList.size-2))
                })
            },
            drawLine1(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart1'))
                // 绘制图表
                myChart.setOption({
                    title: { text: '年度需求图 ',x:'center' },
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
                    title: { text: '年度任务图',x:'center' },
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
            drawLine4(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart4'))
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
                            /*data:[
                                {value:50,name:'单人任务'},
                                {value:234,name:'多人任务'}
                            ],*/
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
            drawLine5(){
                // 基于准备好的dom，初始化echarts实例
                let myChart = this.$echarts.init(document.getElementById('myChart5'))
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
                            data:this.personTaskData1,
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
                this.fetchAnnualFeedback();
                this.fetchDiffTypeFbNum();
                this.fetchMaxAndMinFbMonth();
            },

            //查询需求最多(最少)月份及数量
            fetchMaxAndMinFbMonth(){
              Http.zsyPostHttp('/data/annual/feedback/max-min',this.fbReqDTO,(res)=>{
                  if (res){
                      this.maxAndMinFbData.maxFbNum = res.data.maxFbNum;
                      this.maxAndMinFbData.maxMonth = res.data.maxFbMonth;
                      this.maxAndMinFbData.minFbNum = res.data.minFbNum;
                      this.maxAndMinFbData.minMonth = res.data.minFbMonth
                  }
              })
            },

            //查询不同type的需求数量
            fetchDiffTypeFbNum(){
              Http.zsyPostHttp('/data/annual/feedback-type',this.fbReqDTO,(res)=>{
                  if (res){
                      this.diffTypeFeedback.personSuggestion = res.data.personSuggestion;
                      this.diffTypeFeedback.marketDemand = res.data.marketDemand;
                      this.diffTypeFeedback.companyDecision = res.data.companyDecision
                  }
              })
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
                // if (Number(this.taskReqDTO.whichYear) < )
                this.taskData = [];
                this.fetchAnnualTaskData();
                this.fetchAnnualTaskTotalHours();
                this.fetchAnnualProjectTaskNum();
            },

            //查询年度任务总耗时,耗时最多(最少)任务
            fetchAnnualTaskTotalHours(){
              Http.zsyPostHttp('/data/annual/task/total-hours',this.taskReqDTO,(res) =>{
                  if (res){
                      this.taskTotalTime = res.data.totalHours;
                      this.mostTimeTask.id = res.data.mostTimeTaskId;
                      this.mostTimeTask.name = res.data.mostTimeTaskName;
                      this.mostTimeTask.hours = res.data.mostTime;
                      this.lessTimeTask.id = res.data.lessTimeTaskId;
                      this.lessTimeTask.name = res.data.lessTimeTaskName;
                      this.lessTimeTask.hours = res.data.lessTime;
                  }
              })
            },

            //查询年度各个项目对应的任务数
            fetchAnnualProjectTaskNum(){
              Http.zsyPostHttp('/data/annual/task/project-task',this.taskReqDTO,(res) =>{
                  if (res){
                      this.projectTaskList = res.data
                  }
              })
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
            fetchTaskDetail2(id,num){
                Http.zsyGetHttp(`/task/detail/${id}`, {},(res)=>{
                    if (res){
                        this.taskDetail = res.data;
                        this.showTaskVisible2 = true;
                        if (num == 1){
                            this.showTaskTime = true;
                        } else {
                            this.showTaskTime = false;
                        }
                    }

                })
            },
            fetchTaskDetail3(id,num){
                Http.zsyGetHttp(`/task/detail/${id}`, {},(res)=>{
                    if (res){
                        this.taskDetail = res.data;
                        this.showTaskVisible3 = true;
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

            //年度请假总次数,总时长
            fetchAnnualVacation(){
                Http.zsyPostHttp('/data/annual/vacation',this.vacationReqDTO,(res)=>{
                    if (res){
                        this.vacationData.totalCount = res.data.vacationCount;
                        this.vacationData.totalTime = res.data.vacationTime;
                    }
                })
            },

            //年度 每月请假次数和时长集合
            fetchEveryMonthVacation(){
              Http.zsyPostHttp('/data/annual/vacation/month',this.vacationReqDTO,(res)=>{
                  if (res){
                      this.vacationCountList = res.data.vacationCountList;
                      this.vacationTimeList = res.data.vacationTimeList;
                      this.drawLine3()
                  }
              })
            },

            selectVacationByYear(){
                if (this.whichYear3){
                    this.vacationReqDTO.whichYear = moment(this.whichYear3).format("YYYY-MM-DD")
                    this.vacationReqDTO.whichYear = this.vacationReqDTO.whichYear.substring(0,4)
                }
                this.vacationCountList = [];
                this.vacationTimeList = [];
                this.fetchEveryMonthVacation();
                this.fetchAnnualVacation();
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
                        this.drawLine4()
                    }
                })
            },
            selectPersonDataByYear(){
                if (this.whichYear4){
                    this.personDataReqDTO.whichYear = moment(this.whichYear4).format("YYYY-MM-DD")
                    this.personDataReqDTO.whichYear = this.personDataReqDTO.whichYear.substring(0,4)
                }
                this.personData = null;
                this.personTaskData = []
                this.fetchPersonData()
            },
            //查询个人年度数据
            fetchPersonData1(userId){
                Http.zsyPostHttp(`/data/annual/person-data/${userId}`,this.adminReqDTO,(res)=>{
                    if (res){

                        this.personData1 = res.data
                        const tData = {};
                        tData.value = this.personData1.multiTaskNum;
                        tData.name = '多人任务';
                        this.personTaskData1.push(tData);
                        const tData2 = {};
                        tData2.value = this.personData1.singleTaskNum;
                        tData2.name = '单人任务';
                        this.personTaskData1.push(tData2);
                        this.drawLine5()
                    }
                })
            },
            selectAdminPersonData(){
                if (this.whichYear5){
                    this.adminReqDTO.whichYear = moment(this.whichYear5).format("YYYY-MM-DD")
                    this.adminReqDTO.whichYear = this.adminReqDTO.whichYear.substring(0,4)
                }
                this.personTaskData1 = []
                if (this.chargeMan != null && this.chargeMan != ''){
                    this.personDataVisible = true
                    this.fetchPersonData1(this.chargeMan)
                }else {
                    this.personDataVisible = false
                }
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

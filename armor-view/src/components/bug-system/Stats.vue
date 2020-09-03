<template>
    <div>
        <el-card style="height: 500px">
            <div class="fl">
                <el-date-picker
                        style="width: 140px;"
                        v-model="bugStatusStat.queryDTO.yearAndMonth"
                        align="right"
                        type="month"
                        placeholder="选择年月">
                </el-date-picker>
            </div>
            <el-button type="primary" style="margin-left: 10px;" @click="fetchBugStatusStat()">搜索</el-button>
            <div id="bugStatusPie" style="height: 400px;width: 100%;text-align: center"></div>
        </el-card>
        <el-card style="height: 500px">
            <div class="fl">
                <el-date-picker
                        style="width: 140px;"
                        v-model="bugTypeStat.queryDTO.yearAndMonth"
                        align="right"
                        type="month"
                        placeholder="选择年月">
                </el-date-picker>
            </div>
            <el-button type="primary" style="margin-left: 10px;" @click="fetchBugTypeStat()">搜索</el-button>
            <div id="bugTypePie" style="height: 400px;width: 100%;text-align: center"></div>
        </el-card>
        <el-card style="height: 500px">
            <div class="fl">
                <el-date-picker
                        style="width: 140px;"
                        v-model="userBugStat.queryDTO.yearAndMonth"
                        align="right"
                        type="month"
                        placeholder="选择年月">
                </el-date-picker>
            </div>
            <div class="fl">
                <el-select v-model="userBugStat.queryDTO.isTester" filterable placeholder="筛选用户">
                    <el-option v-for="item in userBugStat.isTesterList" :key="item.value" :label="item.name"
                               :value="item.value"></el-option>
                </el-select>
            </div>
            <el-button type="primary" style="margin-left: 10px;" @click="fetchUserBugStat()">搜索</el-button>
            <div id="userBugHistogram" style="height: 400px;width: 100%;text-align: center"></div>
        </el-card>
        <el-card>
            <div class="fl">
                <el-date-picker
                        style="width: 140px;"
                        v-model="taskBugStat.queryDTO.yearAndMonth"
                        align="right"
                        type="month"
                        placeholder="选择年月">
                </el-date-picker>
            </div>
            <el-button type="primary" style="margin-left: 10px;" @click="selectTaskBug()">搜索</el-button>
            <div>
                <el-table :data="taskBugStat.taskBugData" border
                          :header-cell-style="{background:'#D9D9D9',color:'black'}">
                    <el-table-column type="index" label="序号" align="center" width="60">
                        <template slot-scope="scope">
                            {{(taskBugStat.queryDTO.pageNum-1)*10 + scope.$index + 1}}
                        </template>
                    </el-table-column>
                    <el-table-column prop="taskName" label="任务名称" align="center" ></el-table-column>
                    <el-table-column label="问题状态" align="center"  width="150">
                        <template slot-scope="scope">
                            <div v-for="item in scope.row.bugStatusNumList">
                                <span>{{item.statusName}}</span>
                                <span>{{item.number}}</span>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="严重程度" align="center"  width="150">
                        <template slot-scope="scope">
                            <div v-for="item in scope.row.bugSeverityNumList">
                                <span>{{item.severityName}}</span>
                                <span>{{item.number}}</span>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="问题类型" align="center"  width="150">
                        <template slot-scope="scope">
                            <div v-for="item in scope.row.bugTypeNumList">
                                <span>{{item.typeName}}</span>
                                <span>{{item.number}}</span>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="测试" align="center"  width="150">
                        <template slot-scope="scope">
                            <div v-for="item in scope.row.creatorNumList">
                                <span>{{item.userName}}</span>
                                <span>{{item.number}}</span>
                            </div>
                        </template>
                    </el-table-column>
                    <el-table-column label="开发" align="center"  width="150">
                        <template slot-scope="scope">
                            <div v-for="item in scope.row.handlerNumList">
                                <span>{{item.userName}}</span>
                                <span>{{item.number}}</span>
                            </div>
                        </template>
                    </el-table-column>
                </el-table>
                <div style="text-align: center;margin: 20px auto 40px;">
                    <el-pagination
                            @current-change="taskBugPageChange"
                            :current-page.sync="taskBugStat.queryDTO.pageNum"
                            :page-size="taskBugStat.queryDTO.pageSize"
                            layout="total, prev, pager, next"
                            :total="taskBugStat.queryDTO.total">
                    </el-pagination>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
    import http from '../../lib/Http'
    import moment from 'moment';
    export default {
        name: "Stats",
        data(){
            return{
                //bug状态分类相关
                bugStatusStat:{
                    queryDTO:{
                        yearAndMonth:null
                    },
                    totalNum:0,
                    legendData:[],
                    bugData:[]
                },
                //bug类型分类相关
                bugTypeStat:{
                    queryDTO:{
                        yearAndMonth:null
                    },
                    totalNum:0,
                    legendData:[],
                    bugData:[]
                },
                //用户bug统计相关
                userBugStat:{
                    queryDTO:{
                        yearAndMonth:null,
                        isTester:1
                    },
                    totalNum:0,
                    xData:[],
                    bugData:[],
                    isTesterList:[
                        {name:'分配人',value:0},
                        {name:'创建人',value:1}
                    ]
                },
                //任务bug数量统计表
                taskBugStat:{
                    queryDTO:{
                        yearAndMonth:null,
                        pageNum:1,
                        pageSize:10,
                        total:0
                    },
                    taskBugData:[],
                }
            }
        },
        created(){
          this.initTime();
          this.fetchBugStatusStat();
          this.fetchBugTypeStat();
          this.fetchUserBugStat();
          this.fetchTaskBugStat();
        },
        methods:{

            //初始化时间条件
            initTime(){
                let curDate = new Date();
                curDate = moment(curDate).format('YYYY-MM-DD 23:59:59');
                this.bugStatusStat.queryDTO.yearAndMonth = curDate;
                this.bugTypeStat.queryDTO.yearAndMonth = curDate;
                this.userBugStat.queryDTO.yearAndMonth = curDate;
                this.taskBugStat.queryDTO.yearAndMonth = curDate;
            },
            //查询bug状态分类图
            fetchBugStatusStat(){
                this.bugStatusStat.bugData = [];
                this.bugStatusStat.legendData = [];
                this.bugStatusStat.totalNum = 0;
                if (this.bugStatusStat.queryDTO.yearAndMonth != null
                && this.bugStatusStat.queryDTO.yearAndMonth !== undefined
                && this.bugStatusStat.queryDTO.yearAndMonth !== ''){
                    this.bugStatusStat.queryDTO.yearAndMonth = moment(this.bugStatusStat.queryDTO.yearAndMonth).format('YYYY-MM-DD 23:59:59');
                }
                console.log(this.bugStatusStat.queryDTO)
                http.zsyPostHttp('/task-bug/status-pie',this.bugStatusStat.queryDTO,res=>{
                    let data = res.data;
                    if (data != null && data !== undefined && data.length >0){
                        let totalBugNum = 0;
                        data.forEach(item=>{
                            let pieData = {
                                value: item.number,
                                name:item.statusName
                            };
                            this.bugStatusStat.bugData.push(pieData);
                            this.bugStatusStat.legendData.push(item.statusName);
                            totalBugNum = totalBugNum + item.number
                        });
                        this.bugStatusStat.totalNum = totalBugNum;
                    }
                    this.$nextTick(()=>{
                        this.drawBugStatusPie()
                    })
                })
            },
            //查询bug类型分类图
            fetchBugTypeStat(){
                this.bugTypeStat.bugData = [];
                this.bugTypeStat.legendData = [];
                this.bugTypeStat.totalNum = 0;
                if (this.bugTypeStat.queryDTO.yearAndMonth != null
                    && this.bugTypeStat.queryDTO.yearAndMonth !== undefined
                    && this.bugTypeStat.queryDTO.yearAndMonth !== ''){
                    this.bugTypeStat.queryDTO.yearAndMonth = moment(this.bugTypeStat.queryDTO.yearAndMonth).format('YYYY-MM-DD 23:59:59');
                }
                http.zsyPostHttp('/task-bug/type-pie',this.bugTypeStat.queryDTO,res=>{
                    let data = res.data;
                    if (data != null && data !== undefined && data.length >0){
                        let totalBugNum = 0;
                        data.forEach(item=>{
                            let pieData = {
                                value: item.number,
                                name:item.typeName
                            };
                            this.bugTypeStat.bugData.push(pieData);
                            this.bugTypeStat.legendData.push(item.typeName);
                            totalBugNum = totalBugNum + item.number
                        });
                        this.bugTypeStat.totalNum = totalBugNum;
                    }
                    this.$nextTick(()=>{
                        this.drawBugTypePie()
                    })
                })
            },
            //查询用户bug数量图
            fetchUserBugStat(){
                this.userBugStat.bugData = [];
                this.userBugStat.xData = [];
                this.userBugStat.totalNum = 0;
                if (this.userBugStat.queryDTO.yearAndMonth != null
                    && this.userBugStat.queryDTO.yearAndMonth !== undefined
                    && this.userBugStat.queryDTO.yearAndMonth !== ''){
                    this.userBugStat.queryDTO.yearAndMonth = moment(this.userBugStat.queryDTO.yearAndMonth).format('YYYY-MM-DD 23:59:59');
                }
                http.zsyPostHttp('/task-bug/user-histogram',this.userBugStat.queryDTO,res=>{
                    let data = res.data;
                    if (data != null && data !== undefined && data.length >0){
                        let totalBugNum = 0;
                        data.forEach(item=>{
                            this.userBugStat.bugData.push(item.number);
                            this.userBugStat.xData.push(item.userName);
                            totalBugNum = totalBugNum + item.number
                        });
                        this.userBugStat.totalNum = totalBugNum;
                    }
                    this.$nextTick(()=>{
                        this.drawUserBugHistogram()
                    })
                })
            },
            //查询任务bug统计表
            fetchTaskBugStat(){
                if (this.taskBugStat.queryDTO.yearAndMonth != null
                    && this.taskBugStat.queryDTO.yearAndMonth !== undefined
                    && this.taskBugStat.queryDTO.yearAndMonth !== ''){
                    this.taskBugStat.queryDTO.yearAndMonth = moment(this.taskBugStat.queryDTO.yearAndMonth).format('YYYY-MM-DD 23:59:59');
                }
                http.zsyPostHttp('/task-bug/task-stat',this.taskBugStat.queryDTO,res=>{
                    this.taskBugStat.taskBugData = res.data.list;
                    this.taskBugStat.queryDTO.total = res.data.totalSize;
                })
            },
            //页码切换
            taskBugPageChange(){
                this.fetchTaskBugStat()
            },
            //搜索
            selectTaskBug(){
              this.taskBugStat.queryDTO.pageNum = 1;
              this.fetchTaskBugStat();
            },
            //绘制bug状态分类饼形图
            drawBugStatusPie(){
                // 基于准备好的dom，初始化echarts实例
                let dom = document.getElementById('bugStatusPie');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: 'bug状态分类图',
                        x:'center',
                        subtext: 'bug总数: '+this.bugStatusStat.totalNum,
                        subtextStyle: {
                            fontSize: 16
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: this.bugStatusStat.legendData
                    },
                    series : [
                        {
                            name: '状态',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:this.bugStatusStat.bugData,
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
            //绘制bug类型分类饼形图
            drawBugTypePie(){
                // 基于准备好的dom，初始化echarts实例
                let dom = document.getElementById('bugTypePie');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: 'bug类型分类图',
                        x:'center',
                        subtext: 'bug总数: '+this.bugTypeStat.totalNum,
                        subtextStyle: {
                            fontSize: 16
                        }
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: this.bugTypeStat.legendData
                    },
                    series : [
                        {
                            name: '类型',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:this.bugTypeStat.bugData,
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
            //绘制用户bug数量柱状图
            drawUserBugHistogram(){
                // 基于准备好的dom，初始化echarts实例
                let dom = document.getElementById('userBugHistogram');
                if(!dom)return;
                let myChart = this.$echarts.init(dom);
                // 绘制图表
                myChart.setOption({
                    title: {
                        text: '用户bug柱状图',
                        x:'center',
                        subtext: 'bug总数: '+this.userBugStat.totalNum,
                        subtextStyle: {
                            fontSize: 16
                        }
                    },
                    tooltip: {
                        trigger: 'item',
                        // formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        left: 'left',
                    },
                    xAxis: {
                        type: 'category',
                        data: this.userBugStat.xData,
                        axisLabel:{
                            interval:0,
                            rotate:-45,//倾斜度 -90 至 90 默认为0
                            margin:2,
                            textStyle:{
                                color:"#000000",
                                fontSize:15
                            }
                        }
                    },
                    yAxis: [
                        {
                            type: 'value',
                            name:'个数',
                            // interval: 5,
                            axisLabel: {
                                formatter: '{value} 个'
                            },
                            splitLine: {
                                show: false
                            }
                        },

                    ],
                    // dataZoom: [{
                    //     type: 'slider',
                    //     show: true, //flase直接隐藏图形
                    //     xAxisIndex: [0],
                    //     // left: '9%', //滚动条靠左侧的百分比
                    //     bottom: -5,
                    //     start: 0,//滚动条的起始位置
                    //     end: 50 //滚动条的截止位置（按比例分割你的柱状图x轴长度）
                    // }],
                    grid: {
                        left: '3%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    series : [
                        {
                            name:'bug',
                            type:'bar',
                            data:this.userBugStat.bugData,
                            barWidth:30,
                            barGap:0,
                            label:{
                                normal: {
                                    show: true,
                                    position: 'top'
                                }
                            }
                        }
                    ]
                });
            },
        }
    }
</script>

<style scoped>

</style>
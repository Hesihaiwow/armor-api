<template>
    <div class="plan clearfix">
        <div class="fl plan-control">
                <el-date-picker
                        v-model="planYear"
                        align="right"
                        type="year"
                        placeholder="选择年">
                </el-date-picker>
            <ul class="plan-date">
                <li v-for="date in dateTree"  style="cursor: pointer" >
                    <div v-bind:class="{'activeMonth':date.id == curMonth}">
                        <span class="month" @click="monthToggle(date)">{{date.name}}</span><i class="iconfont icon-circle"></i>
                    </div>
                    <ul class="plan-date-week" v-show="date.id == curMonth" v-for="week in dateTreeBuild(date.id,curMonth)">
                        <li  v-bind:class="{ 'cur' : curWeekClass(week),  'active':week == activeWeek}" @click="toggleWeek(week)">
                            <div ><span>第{{week}}周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        <div class="plan-content fl">
            <div class="plan-filter clearfix">
                <span class="fl">排序：</span>
                <a href="javascript:;" class="fl" v-bind:class="{ 'active' : activeSort == 'com'}" @click="toggleSort('com')">完成进度
                    <i class="icon-arrow-top iconfont" v-bind:class="{ 'active' : activeSort == 'com'&&activeArrow}" ></i>
                    <i class="icon-arrow-bottom iconfont" v-bind:class="{ 'active' : activeSort == 'com'&&!activeArrow}"></i>
                    <i class="el-icon-sort-down"></i></a>
                <a href="javascript:;" class="fl" v-bind:class="{ 'active' : activeSort == 'time'}"   @click="toggleSort('time')">截止日期
                    <i class="icon-arrow-top iconfont" v-bind:class="{'active' : activeSort == 'time'&&activeTimeArrow}" ></i>
                    <i class="icon-arrow-bottom iconfont" v-bind:class="{ 'active' : activeSort == 'time'&&!activeTimeArrow}"></i>
                    <i class="el-icon-sort-down"></i></a>
                <p class="fl">需求来源：<input type="text" placeholder="请输入姓名" v-model="treeList.origin">
                    <el-button type="primary" @click="fetchTreeJson()"><i class="el-icon-search"></i>查询</el-button>
                </p>
            </div>
            <div class="plan-list">
                <ul>
                    <plan-tree @parentMethodInit="init" :model="item" :key="idx" :modelKey="idx" v-for="(item,idx) in tree"></plan-tree>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
    import http from '../lib/Http'
    import moment from 'moment';
    import helper from '../lib/Helper'
    import PlanTree from "./PlanTree.vue"


    moment.locale('zh-cn');
    export default {
        name: 'Plan',
        components: {
            PlanTree:PlanTree
        },
        data() {
            var validateEmpty = (rule, value, callback) => {
                if (value.trim() == '') {
                    callback(new Error());
                } else {
                    callback();
                }
            };
            return {
                tree:[],
                activeWeek:'',
                activeSort:'',
                activeArrow:'',
                activeTimeArrow:'',
                curWeek:false,
                planYear:moment().format('YYYY'),
                curMonth:moment().month(),
                value:'选项2',
                dateTree:[{id:0,name:'一月',key:[]},{id:1,name:'二月',key:[]},{id:2,name:'三月',key:[]},{id:101,name:'第一季度',key:[]},
                        {id:3,name:'四月',key:[]},{id:4,name:'五月',key:[]},{id:5,name:'六月',key:[]},{id:102,name:'第二季度',key:[]},
                        {id:6,name:'七月',key:[]},{id:7,name:'八月',key:[]},{id:8,name:'九月',key:[]},{id:103,name:'第三季度',key:[]},
                        {id:9,name:'十月',key:[]},{id:10,name:'十一月',key:[]},{id:11,name:'十二月',key:[]},{id:104,name:'第四季度',key:[]},
                        ],
                monthWeeks:[],
                treeList:{
                    startTime:moment().startOf('week').week(moment().week()).format('YYYY-MM-DD 00:00:00'),
                    endTime:moment().endOf('week').week(moment().week()).format('YYYY-MM-DD 23:59:59'),
                    sort:'',
                    origin:''
                }
            }
        },
        beforeMount:function () {
            //选中任务tab
            this.$root.eventBus.$emit("handleTabSelected", "plan");
            this.fetchTreeJson();
            this.dateTreeBuild();
        },
        methods:{
            init(){
            },
            fetchTreeJson(){
                if(this.activeSort=='com'){
                    if(this.activeArrow){
                        this.treeList.sort = '0'//降序
                    }else{
                        this.treeList.sort = '1'//升序
                    }
                }else if(this.activeSort=='time'){
                    if(this.activeTimeArrow){
                        this.treeList.sort = '2'
                    }else{
                        this.treeList.sort = '3'
                    }
                }
                http.zsyPostHttp(`/feedbackPlan/list/`, this.treeList, (resp) => {
                    this.tree =  resp.data;
                });
            },
            dateTreeBuild(id,month){
                if(id == month && month <100 ){
                    let startWeek = moment(moment().startOf('month').month(month)).week();
                    let endWeek = moment(moment().endOf('month').month(month)).week();
                    let weeks = []
                    for(let i=startWeek;i<endWeek+1;i++){
                        weeks.push(i)
                    }
                    return weeks
                }
            },
            curWeekClass(week){
                if(week ==moment().week()){
                    return true
                }else{
                    return false
                }
            },
            toggleWeek(week){
                this.activeWeek =  week
                this.treeList.startTime =moment().startOf('week').week(week).format('YYYY-MM-DD 00:00:00');
                this.treeList.endTime =moment().endOf('week').week(week).format('YYYY-MM-DD 23:59:59');
                this.fetchTreeJson()
            },
            monthToggle(date){
                this.curMonth = date.id
                if(date.id == '101' ){//季度时间
                    this.treeList.startTime = moment().startOf('quarter').quarter(1).format('YYYY-MM-DD 00:00:00');
                    this.treeList.endTime = moment().endOf('quarter').quarter(1).format('YYYY-MM-DD 23:59:59');
                }else if(date.id == '102' ){//季度时间
                    this.treeList.startTime = moment().startOf('quarter').quarter(2).format('YYYY-MM-DD 00:00:00');
                    this.treeList.endTime = moment().endOf('quarter').quarter(2).format('YYYY-MM-DD 23:59:59');
                }else if(date.id == '103' ){//季度时间
                    this.treeList.startTime = moment().startOf('quarter').quarter(3).format('YYYY-MM-DD 00:00:00');
                    this.treeList.endTime = moment().endOf('quarter').quarter(3).format('YYYY-MM-DD 23:59:59');
                }else if(date.id == '104' ){//季度时间
                    this.treeList.startTime = moment().startOf('quarter').quarter(4).format('YYYY-MM-DD 00:00:00');
                    this.treeList.endTime = moment().endOf('quarter').quarter(4).format('YYYY-MM-DD 23:59:59');
                }else{
                    this.treeList.startTime = moment().startOf('month').month(date.id).format('YYYY-MM-DD 00:00:00');
                    this.treeList.endTime = moment().endOf('month').month(date.id).format('YYYY-MM-DD 23:59:59');
                }
                this.fetchTreeJson()
            },
            toggleSort(name){
                if(name =='com'){
                    this.activeSort = name
                    this.activeArrow = !this.activeArrow
                }else{
                    this.activeSort = name
                    this.activeTimeArrow = !this.activeTimeArrow
                }
                this.fetchTreeJson()
            }


        }
    }
</script>
<style lang="less" scoped>
    .plan{
        margin:0 auto;
        width: 1100px;
        .plan-control{
            padding-top: 40px;
            width: 200px;
            .control-select{
                width: 180px;
            }
            .plan-date{
                margin-top: 20px;
                width: 50px;
                .month{
                    margin-left: -7px;
                    font-weight: bold;
                }
                .activeMonth{
                    i{
                        color: #4298fc;
                    }
                }
                li{
                    &>div{
                        position: relative;
                        height: 40px;
                        border-right:1px solid #84bd2b;
                        i{
                            position: absolute;
                            right: 0;
                            font-size: 12px;
                            width: 12px;
                            height: 12px;
                            transform: translateX(50%);
                            background: #f5f5f5;
                            color: #84bd2b;
                            cursor: pointer;
                        }
                    }
                    &:last-child{
                        ul{
                            li:last-child{
                                &>div{
                                    border:0;
                                }
                            }
                        }
                    }
                }
                .plan-date-week{
                    li{
                        span{
                            
                            width: 80px;
                            height: 30px;
                            line-height: 30px;
                            text-align: center;
                            position: absolute;
                            right: 0;
                            bottom:0;
                            transform:translate(120%,-50%);
                            
                        }
                        &.cur{
                            i{
                                color: #e05f03
                            }
                        }
                        &.active{
                            i{
                                color: #4298fc;
                            }
                            span{
                                border:1px solid #4298fc;
                                background-color: #fff;
                                border-radius: 3px;
                                &:before{
                                    content: "";
                                    position: absolute;
                                    width: 8px;
                                    height: 8px;
                                    left:-5px;
                                    top:30%;
                                    border-top:1px solid #4298fc;
                                    border-right:1px solid #4298fc;
                                    transform: rotate(-135deg);
                                    background-color: #fff;
                                }  
                            }
                        }
                    }

                }
            }
            
        }
        .plan-content{
            width: 800px;
            .plan-filter{
                line-height: 40px;
                font-size: 14px;
                a{
                    margin-right: 50px;
                    color: #333;
                    &.active{
                        color: #4298fc;
                    }
                    i{
                        margin-right: -4px;
                        font-size: 12px;
                        color: #333;
                        &.active{
                            color: #4298fc;
                        }
                        // width: 4px;
                    }
                }
                p{
                    input{
                        margin-right: 10px;
                        height: 30px;
                        text-align: center;
                        background-color: #fff;
                        border-radius: 18px;
                    }
                    button{
                        padding:0 15px;
                        height: 30px;
                        // line-height: 30px;
                        i{
                            margin-right: 5px;
                        }
                    }
                }
            }
            .plan-list{
                min-height: 550px;
                padding:20px;
                padding-bottom: 100px;
                background-color: #fff;
            }
        }
    }
</style>


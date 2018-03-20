<template>
    <div class="plan clearfix">
        <div class="fl plan-control">
            <el-select v-model="value" placeholder="请选择" class="control-select">
                <el-option
                  v-for="item in options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
            </el-select>
            <ul class="plan-date">
                <li>
                    <div><span class="month">一月</span><i class="iconfont icon-circle"></i></div>
                    <ul class="plan-date-week">
                        <li class="active">
                            <div><span>第一周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                        <li>
                            <div><span>第二周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                        <li class="cur">
                            <div><span>第三周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                        <li>
                            <div><span>第四周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                        <li>
                            <div><span>第五周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                    </ul>
                </li>
                <li>
                    <div><span class="month">二月</span><i class="iconfont icon-circle"></i></div>
                    <ul class="plan-date-week">
                        <li>
                            <div><span>第一周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                        <li>
                            <div><span>第二周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                        <li>
                            <div><span>第三周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                        <li>
                            <div><span>第四周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                        <li>
                            <div><span>第五周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                    </ul>
                </li>
                <li>
                    <div><span class="month">三月</span><i class="iconfont icon-circle"></i></div>
                    <ul class="plan-date-week">
                        <li>
                            <div><span>第一周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                        <li>
                            <div><span>第二周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                        <li>
                            <div><span>第三周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                        <li>
                            <div><span>第四周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                        <li>
                            <div><span>第五周</span><i class="iconfont icon-circle"></i></div>
                        </li>
                    </ul>
                </li>
            </ul>
        </div>
        <div class="plan-content fl">
            <div class="plan-filter clearfix">
                <span class="fl">排序：</span>
                <a href="javascript:;" class="fl active">完成进度<i class="icon-arrow-top iconfont active"></i><i class="icon-arrow-bottom iconfont"></i><i class="el-icon-sort-down"></i></a>
                <a href="javascript:;" class="fl">截止日期<i class="icon-arrow-top iconfont"></i><i class="icon-arrow-bottom iconfont"></i><i class="el-icon-sort-down"></i></a>
                <p class="fl">需求来源：<input type="text" placeholder="请输入姓名"> <el-button type="primary"><i class="el-icon-search"></i>查询</el-button></p>
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
    import treeJson from './../constants/treeJson.js'


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
                options: [{
                  value: '选项1',
                  label: '2017'
                }, {
                  value: '选项2',
                  label: '2018'
                }],
                value:'选项2',
            }
        },
        mounted(){
            this.tree = treeJson.data
        },
        methods:{
            init(){
                console.log(111)
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
                    margin-left: 10px;
                    font-weight: bold;
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


<template>
    <div class="bug-details">
            <div class="title">
                <div class="left fl">
                    <span class="name">BUG详情</span>
                </div>
                <div class="right fr">
                    <el-button type="primary" @click="goBack">返回</el-button>
                </div>
            </div>
            <div class="con">
                <div class="left">
                    <div class="item">
                        <p class="name">标题</p>
                        <div class="content">
                            <p class="text" v-show="bugData.isCreater===0">{{bugData.title}}</p>
                            <el-input v-show="bugData.isCreater===1" v-model="upData.title" placeholder="请输入内容"></el-input>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">创建时间</p>
                        <div class="content">
                            <p class="text">{{bugData.createTime | formatDate }}</p>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">出现频率</p>
                        <div class="content" v-show="bugData.isCreater===0">
                            <p class="text">{{frequencyToWords(bugData.frequency)}}</p>
                        </div>
                        <div class="content" v-show="bugData.isCreater===1">
                            <el-radio v-model="bugData.frequency" :label="1" border>固定重现</el-radio>
                            <el-radio v-model="bugData.frequency" :label="2" border>测试随机</el-radio>
                            <el-radio v-model="bugData.frequency" :label="3" border>无法重现</el-radio>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">类型</p>
                        <div class="content" v-show="bugData.isCreater===0">
                            <p class="text">{{typeToWords(bugData.status)}}</p>
                        </div>
                        <div class="content" v-show="bugData.isCreater===1">
                            <el-radio v-model="upData.severity" :label="4" border>严重错误</el-radio>
                            <el-radio v-model="upData.severity" :label="3" border>主要错误</el-radio>
                            <el-radio v-model="upData.severity" :label="2" border>一般错误</el-radio>
                            <el-radio v-model="upData.severity" :label="1" border>建议</el-radio>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">状态</p>
                        <div class="content">
                            <el-radio v-model="upData.status" :label="1" border>已分配</el-radio>
                            <el-radio v-model="upData.status" :label="2" border>已解决</el-radio>
                            <el-radio v-model="upData.status" :label="3" border>已关闭</el-radio>
                            <el-radio v-model="upData.status" :label="4" border>打回</el-radio>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">提交人</p>
                        <div class="content">
                            <p class="text">{{bugData.createName}}</p>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">处理人</p>
                        <div class="content">
                            <el-select  v-model="upData.handlerId" filterable placeholder="请选择">
                                <el-option
                                        v-for="item in handlerEr"
                                        :key="item.id"
                                        :label="item.name"
                                        :value="item.id">
                                </el-option>
                            </el-select>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">处理历史</p>
                        <div class="content">
                            <el-table
                                    :data="bugData.logResDTOList"
                                    stripe
                                    style="width: 100%">
                                <el-table-column
                                        label="日期"
                                        width="180">
                                    <template scope="scope">
                                        <span>{{ scope.row.createTime | formatDate }}</span>
                                    </template>
                                </el-table-column>
                                <!--<el-table-column-->
                                        <!--prop="name"-->
                                        <!--label="账号">-->
                                <!--</el-table-column>-->
                                <el-table-column
                                        prop="content"
                                        label="事件">
                                </el-table-column>
                                <!--<el-table-column-->
                                        <!--prop="handle"-->
                                        <!--label="处理人">-->
                                <!--</el-table-column>-->
                            </el-table>
                        </div>
                    </div>

                </div>
                <div class="right">
                    <div class="text-box">
                        <p class="name">描述与截图</p>
                        <div v-show="bugData.isCreater===0" class="text" v-html="bugData.description"></div>
                        <editor v-show="bugData.isCreater===1" :content="bugData.description" v-on:change="getEditordescription"  />
                    </div>
                    <div class="text-box" v-for="item in bugData.remarkResDTOS">
                        <p class="name">{{item.createName}}  {{item.createTime | formatDate}}</p>
                        <div class="text" v-html="item.remark"></div>
                    </div>
                    <div class="text-box">
                        <p class="name">添加备注</p>
                        <editor :content="upData.remark" v-on:change="getEditorremark" />
                    </div>
                </div>
            </div>
            <div class="btn-box">
                <el-button type="primary" @click="save">提交</el-button>
                <el-button @click="goBack">取消</el-button>

            </div>
    </div>
</template>

<script>
    import http from '../../lib/Http'
    import editor from "./Editor.vue";
    import moment from 'moment';
    moment.locale('zh-cn');
    export default {
        name: "Details",
        components: { editor },
        data(){
            return {
                upData:{
                    tbId:0,
                    handlerId:0,
                    frequency:1,
                    severity:1,
                    status:1,
                    remark:''
                },
                bugData:{
                    status:'1',
                    description:''
                },
                handlerEr: [],
                HistoryData:[
                    {
                        Event:'ddddd',
                        name:'uuu',
                        handle:'王小虎->666',
                        date: '2016-05-04 10:36:08',
                    }
                ],
                input:'',
                radio1:'',
                value: '',
                content:'',
            }
        },
        created() {
            this.getDefaultDatas();
        },
        filters:{
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY/MM/DD HH:mm:ss');
            },
        },
        methods:{
            getDefaultDatas(){
                this.upData.tbId = this.$route.query.id;
                this.upData.taskId = this.$route.query.taskId;
                http.zsyGetHttp(`/task-bug/detail/${this.upData.tbId}`, {}, (res) => {
                    this.bugData = res.data;
                    this.upData.handlerId = res.data.handlerId;
                    this.upData.frequency = res.data.frequency;
                    this.upData.severity = res.data.severity;
                    this.upData.status = res.data.status;
                    this.upData.title =  res.data.title;
                });
                http.zsyGetHttp(`/task-bug/users/${this.upData.taskId}`, {}, (res) => {
                    this.handlerEr = res.data;
                })

            },
            goBack(){
                this.$router.go(-1)
            },
            frequencyToWords(val) {
                if (val === 1) {
                    return '固定重现';
                } else if (val === 2) {
                    return '测试随机';
                } else if (val === 3) {
                    return '无法重现';
                }
            },
            typeToWords(val) {
                if (val === 1) {
                    return '严重错误';
                } else if (val === 2) {
                    return '主要错误';
                } else if (val === 3) {
                    return '一般错误';
                } else if (val === 4) {
                    return '建议';
                }
            },
            getEditordescription(val){
                this.upData.description = val
            },
            getEditorremark(val){
                this.upData.remark = val
            },
            save(){

                http.zsyPutHttp(`/task-bug/edit`, this.upData, (res) => {
                    this.$message({
                        message: '修改成功',
                        type: 'success'
                    });
                    this.goBack();
                })
            }

        }
    }
</script>

<style scoped lang="less">
    .bug-details{
        padding: 20px;
        background-color: #fff;
        .title{
            padding-bottom: 20px;
            border-bottom: 1px solid #eee;
            overflow: hidden;
            .name{
                font-size: 16px;
                line-height: 36px;

            }
        }
        .con{
            margin-top: 20px;
            display: flex;
            border: 1px solid #eee;
            border-radius: 5px;
            padding: 20px;
            .left{
                width: 40%;
                .item{
                    margin-bottom: 20px;
                    font-size: 14px;
                    line-height: 30px;
                    .name{
                        font-size: 16px;
                        font-weight: 700;
                    }
                    .el-select{
                        width: 100%;
                    }
                    .text{
                        min-height: 20px;
                        line-height: 36px;
                        padding-left: 13px;
                        padding-right: 13px;
                        margin-bottom: 10px;
                        background-color: #f5f5f5;
                        border: 1px solid #e3e3e3;
                        border-radius: 4px;
                        box-shadow: inset 0 1px 1px rgba(0,0,0,.05);
                    }
                }
            }
            .right{
                margin-left: 20px;
                width: 60%;
                .name{
                    font-size: 16px;
                    line-height: 30px;
                    font-weight: 700;
                }
                .text-box{
                    margin-bottom: 20px;
                    .text{
                        min-height: 36px;
                        padding: 10px;
                        font-size: 14px;
                        background-color: #f5f5f5;
                        border: 1px solid #e3e3e3;
                        border-radius: 4px;
                        box-shadow: inset 0 1px 1px rgba(0,0,0,.05);
                    }
                }

            }

        }
        .btn-box{
            margin-top: 20px;
            text-align: center;
        }

    }
</style>
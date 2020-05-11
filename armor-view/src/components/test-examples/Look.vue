<template>
    <div class="test-examples-edit">
        <div class="btn-box">
            <el-row>
                <el-button v-if="lookData.status!=1" @click="setStatus(1)">通过</el-button>
                <el-button type="success" v-if="lookData.status===1" @click="setStatus(1)">通过</el-button>
                <el-button v-if="lookData.status!=2" @click="setStatus(2)">失败</el-button>
                <el-button type="danger" v-if="lookData.status===2" @click="setStatus(2)">失败</el-button>
                <el-button v-if="lookData.status!=3" @click="setStatus(3)">阻塞</el-button>
                <el-button type="warning" v-if="lookData.status===3" @click="setStatus(3)">阻塞</el-button>
            </el-row>
        </div>
        <div class="test-examples-msg block">
            <h2 class="title">信息</h2>
            <div class="content">
                <p>
                    <span>用例名称:</span>
                    <span>{{lookData.name}}</span>
                </p>
                <p>
                    <span>用例类型:</span>
                    <span v-if="lookData.type==1">正用例</span>
                    <span v-if="lookData.type==2">反用例</span>
                    <!--<span>-->
                        <!--<el-radio-group disabled v-model="lookData.type">-->
                            <!--<el-radio :label="1">正用例</el-radio>-->
                            <!--<el-radio :label="2">反用例</el-radio>-->
                        <!--</el-radio-group>-->
                    <!--</span>-->
                </p>
                <p>
                    <span>评审状态:</span>
                    <span v-if="lookData.examStatus===0">未评审</span>
                    <span v-if="lookData.examStatus===1">评审通过</span>
                    <span v-if="lookData.examStatus===2">评审失败</span>
                </p>
            </div>
        </div>
        <div class="test-step block">
            <h2 class="title">测试步骤</h2>
            <div class="content">
                <table class="table-test-step">
                    <tr>
                        <th>检查项</th>
                        <th>预期结果</th>
                    </tr>
                    <tr>
                        <td class="border-blue">
                            <p>{{lookData.checkPoint}}</p>
                            <!--<el-input-->
                                    <!--type="textarea"-->
                                    <!--:autosize="{ minRows: 8, maxRows: 20}"-->
                                    <!--placeholder="请输入内容"-->
                                    <!--:disabled="true"-->
                                    <!--v-model="lookData.checkPoint">-->
                            <!--</el-input>-->
                        </td>
                        <td class="border-blue">
                            <p>{{lookData.expectResult}}</p>
                            <!--<el-input-->
                                    <!--type="textarea"-->
                                    <!--:autosize="{ minRows: 8, maxRows: 20}"-->
                                    <!--placeholder="请输入内容"-->
                                    <!--:disabled="true"-->
                                    <!--v-model="lookData.expectResult">-->
                            <!--</el-input>-->
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="remarks block">
            <h2 class="title">备注</h2>
            <div class="content">
                <p class="remarks-msg">{{lookData.remark}}</p>
                <!--<el-input-->
                        <!--type="textarea"-->
                        <!--:autosize="{ minRows: 4, maxRows: 20}"-->
                        <!--placeholder="请输入内容"-->
                        <!--:disabled="true"-->
                        <!--v-model="lookData.remark">-->
                <!--</el-input>-->
            </div>
        </div>
        <div class="historical-records block">
            <h2 class="title">最后修改</h2>
            <div class="content">
                <ul>
                    <li>
                        <span>{{lookData.updateName}}</span>
                        <span>{{lookData.updateTime | formatDate}}</span>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
    import http from '../../lib/Http'
    import moment from 'moment';
    moment.locale('zh-cn');
    export default {
        name: "Look",
        data(){
            return {
                upData:{
                    taskId:'',
                    functionId:'',
                    exampleId:'',
                    name:'',
                    type:0,
                    checkPoint:'',
                    expectResult:'',
                    remark:'',
                    examStatus:0,
                },
                lookData:{},
            }
        },
        filters:{
        formatDate: function (value) {
            if (!value) return '';
            return moment(value).format('YYYY/MM/DD HH:mm:ss');
            },
        },
        watch:{
             '$route': 'getDefaultData'
        },
        methods:{
            getDefaultData(){
                this.upData.taskId=this.$route.query.id;
                this.upData.exampleId=this.$route.query.exampleId;
                http.zsyGetHttp(`test-example/detail/${this.upData.exampleId}`, {}, (res) => {
                    this.lookData = res.data;
                })
            },
            setStatus(val){
                this.lookData.status=val;
                http.zsyPutHttp(`test-example/status/${this.upData.exampleId}/${this.lookData.status}`, {}, (res) => {
                    this.$message({
                        message: '修改成功',
                        type: 'success'
                    });
                })
            }
        },
        created () {
            this.getDefaultData();
        }

    }
</script>

<style scoped lang="less">
    .test-examples-edit{
        >div{
            margin-top: 20px;
        }
        .test-examples-msg{
            p{
                font-size: 14px;
                line-height: 30px;
                span{
                    margin-right: 20px;

                    &:first-child{
                        display: inline-block;
                        width: 60px;
                        text-align: right;
                    }
                }
            }
        }
        .table-test-step{
            width: 80%;
            .border-blue{
                width: 40%;
                padding: 10px;
                border: 1px solid #257bd2;
                border-radius: 5px;
                p{
                    min-height: 100px;
                }

            }
        }
        .block{
            .title{
                font-size: 20px;
                font-weight: bold;
            }
            .content{
                margin-top: 20px;
                padding-left: 10px;
            }
        }
        .remarks{
            .content{
                width: 80%;
            }
            .remarks-msg{
                min-height: 100px;
                padding: 10px;
                border: 1px solid #257bd2;
                border-radius: 5px;
            }
        }
        .historical-records{
            li{
                padding: 10px;
                border-bottom: 1px solid #999;
                span{
                    margin-left: 20px;
                }
            }
        }
    }
</style>
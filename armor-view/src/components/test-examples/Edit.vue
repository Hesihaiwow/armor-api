<template>
    <div class="test-examples-edit">
        <div class="btn-box">
            <el-row>
                <el-button type="primary" @click="save">保存</el-button>
                <el-button v-if="upData.examStatus!=1" @click="upData.examStatus=1">评审通过</el-button>
                <el-button type="success" v-if="upData.examStatus===1" @click="upData.examStatus=1">评审通过</el-button>
                <el-button v-if="upData.examStatus!=2" @click="upData.examStatus=2">评审失败</el-button>
                <el-button type="warning" v-if="upData.examStatus===2" @click="upData.examStatus=2">评审失败</el-button>
            </el-row>
        </div>
        <div class="test-examples-name">
           <el-input v-model="upData.name" placeholder="请输入实例名称"></el-input>
        </div>
        <div>
            <el-radio-group v-model="upData.type">
                <el-radio :label="1">正用例</el-radio>
                <el-radio :label="2">反用例</el-radio>
            </el-radio-group>
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
                        <td>
                            <el-input
                                    type="textarea"
                                    :autosize="{ minRows: 8, maxRows: 20}"
                                    placeholder="请输入内容"
                                    v-model="upData.checkPoint">
                            </el-input>
                        </td>
                        <td>
                            <el-input
                                    type="textarea"
                                    :autosize="{ minRows: 8, maxRows: 20}"
                                    placeholder="请输入内容"
                                    v-model="upData.exceptResult">
                            </el-input>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="remarks block">
            <h2 class="title">备注</h2>
            <div class="content">
                <el-input
                        type="textarea"
                        :autosize="{ minRows: 4, maxRows: 20}"
                        placeholder="请输入内容"
                        v-model="upData.remark">
                </el-input>
            </div>
        </div>
        <!--<div class="historical-records block">-->
            <!--<h2 class="title">修改记录</h2>-->
            <!--<div class="content">-->
                <!--<ul>-->
                    <!--<li>-->
                        <!--<span>王陆</span>-->
                        <!--<span>2019-08-09 17:05:21</span>-->
                    <!--</li>-->
                    <!--<li>-->
                        <!--<span>王舞</span>-->
                        <!--<span>2019-08-09 17:02:20</span>-->
                    <!--</li>-->
                <!--</ul>-->
            <!--</div>-->
        <!--</div>-->
    </div>
</template>

<script>
    import http from '../../lib/Http'
    export default {
        name: "Edit",
        data(){
            return {
                upData:{
                    taskId:'',
                    functionId:'',
                    name:'',
                    type:0,
                    checkPoint:'',
                    exceptResult:'',
                    remark:'',
                    examStatus:0,
                },
                isAdd:true,
                lookData:'',
            }
        },
        watch:{
            '$route': 'getDefaultData'
        },
        methods:{
            getDefaultData(){
                this.upData.taskId=this.$route.query.id;
                this.upData.functionId=this.$route.query.functional;
                this.upData.exampleId=this.$route.query.exampleId;
                this.isAdd = this.$route.query.isAdd;
                if(this.isAdd===false){
                    http.zsyGetHttp(`test-example/detail/${this.upData.exampleId}`, {}, (res) => {
                        this.lookData = res.data;
                        this.upData.name =  res.data.name;
                        this.upData.type =  res.data.type;
                        this.upData.checkPoint =  res.data.checkPoint;
                        this.upData.exceptResult =  res.data.exceptResult;
                        this.upData.remark =  res.data.remark;
                        this.upData.examStatus =  res.data.examStatus;
                    })
                }

            },
            save(){
                if(this.upData.name==''){
                    this.$message({
                        message: '名字不能为空',
                        type: 'warning'
                    });
                    return false;
                }
                this.getDefaultData();

                if(this.isAdd===true){
                    http.zsyPostHttp(`test-example/add`, this.upData, (res) => {
                        this.$message({
                            message: '保存成功',
                            type: 'success'
                        });
                    })
                }else {
                    http.zsyPutHttp(`test-example/edit`, this.upData, (res) => {
                        this.$message({
                            message: '修改成功',
                            type: 'success'
                        });
                    })
                }
                this.$router.push({ path: '/index/testExamples', query: {id:this.upData.taskId}});
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
    .table-test-step{
        width: 80%;
    }
    .block{
        .title{
            font-size: 20px;
            font-weight: bold;
        }
        .content{
            margin-top: 20px;
        }
    }
    .remarks{
        .content{
            width: 80%;
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
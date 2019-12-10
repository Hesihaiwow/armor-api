<template>
    <div class="new-bug">
        <div class="con">
            <div class="select-box">
                <div class="item">
                    <p class="name">任务:</p>
                    <div class="content">
                        <el-select v-model="upBugData.handlerId" filterable placeholder="请选择">
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
                    <p class="name">出现频率:</p>
                    <div class="content">
                        <el-select v-model="upBugData.frequency" filterable placeholder="请选择">
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
                    <p class="name">严重性:</p>
                    <div class="content">
                        <el-select v-model="upBugData.handlerId" filterable placeholder="请选择">
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
                    <p class="name">分派给:</p>
                    <div class="content">
                        <el-select v-model="upBugData.handlerId" filterable placeholder="请选择">
                            <el-option
                                    v-for="item in handlerEr"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </div>
                </div>
            </div>
            <div class="input-box">
                <div class="item">
                    <p class="name">摘要</p>
                    <div class="content">
                        <el-input v-model="upBugData.title" placeholder="请输入内容"></el-input>
                    </div>
                </div>
                <div class="item">
                    <p class="name">描述与截图</p>
                    <editor :content="content" v-on:change="getEditorVal" />
                </div>
                <!--<div class="item">-->
                    <!--<p class="name">出现频率</p>-->
                    <!--<div class="content">-->
                        <!--<el-radio v-model="upBugData.frequency" label="1" border>固定重现</el-radio>-->
                        <!--<el-radio v-model="upBugData.frequency" label="2" border>测试随机</el-radio>-->
                        <!--<el-radio v-model="upBugData.frequency" label="3" border>无法重现</el-radio>-->
                    <!--</div>-->
                <!--</div>-->
                <!--<div class="item">-->
                    <!--<p class="name">BUG类型</p>-->
                    <!--<div class="content">-->
                        <!--<el-radio v-model="upBugData.severity" label="1" border>严重错误</el-radio>-->
                        <!--<el-radio v-model="upBugData.severity" label="2" border>主要错误</el-radio>-->
                        <!--<el-radio v-model="upBugData.severity" label="3" border>一般错误</el-radio>-->
                        <!--<el-radio v-model="upBugData.severity" label="4" border>建议</el-radio>-->
                    <!--</div>-->
                <!--</div>-->
                <!--<div class="item">-->
                    <!--<p class="name">处理人</p>-->
                    <!--<div class="content">-->
                        <!--<el-select v-model="upBugData.handlerId" filterable placeholder="请选择">-->
                            <!--<el-option-->
                                    <!--v-for="item in handlerEr"-->
                                    <!--:key="item.id"-->
                                    <!--:label="item.name"-->
                                    <!--:value="item.id">-->
                            <!--</el-option>-->
                        <!--</el-select>-->
                    <!--</div>-->
                <!--</div>-->

            </div>
        </div>
        <div class="btn-box">
            <el-button type="primary" @click="save">保存</el-button>
            <el-button @click="goBack">取消</el-button>

        </div>
    </div>
</template>

<script>
    import http from '../../lib/Http'
    import editor from "./Editor.vue";
    export default {
        name: "NewBug",
        components: { editor },
        data(){
          return {
              handlerEr: [],
              content:'',
              upBugData:{
                  description: "",
                  frequency: 0,
                  handlerId: '',
                  severity: 0,
                  taskId: 0,
                  title: ""
              },

          }
        },
        created() {
            this.getDefaultDatas();
        },
        methods:{
            getDefaultDatas(){
                this.upBugData.taskId = this.$route.query.taskId;
                http.zsyGetHttp(`/task-bug/users/${this.upBugData.taskId}`, {}, (res) => {
                    this.handlerEr = res.data;
                })

            },
            goBack(){
                this.$router.go(-1)
            },
            getEditorVal(val){
                this.upBugData.description = val
            },
            save(){
                // console.log(this.upBugData)

                http.zsyPostHttp(`/task-bug/add`, this.upBugData, (res) => {
                    this.$message({
                        message: '创建成功',
                        type: 'success'
                    });
                    this.goBack();
                })
            }

        }
    }
</script>

<style scoped lang="less">
.new-bug{
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
        /*display: flex;*/
        border: 1px solid #eee;
        border-radius: 5px;
        padding: 20px;
        .select-box{
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            .el-select{
                width: 160px;
            }
            .item{
                display: flex;
                .name{
                    display: inline-block;
                    width: 60px;
                    line-height: 36px;
                    font-weight: 700;
                }
            }
        }
        .input-box{
            /*width: 40%;*/
            .item{
                margin-bottom: 20px;
                font-size: 14px;
                line-height: 30px;
                .name{
                    font-weight: 700;
                }
                .el-select{
                    width: 100%;
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
        }

    }
    .btn-box{
        margin-top: 20px;
        text-align: center;
    }

}
</style>
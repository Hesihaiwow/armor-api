<template>
    <div class="new-bug">
        <div class="title">
            <div class="left fl">
                <span class="name">新建BUG</span>
            </div>
            <div class="right fr">
                <el-button type="primary" @click="goBack">返回</el-button>
            </div>
        </div>
        <div class="con">
            <div class="left">
                <div class="item">
                    <p class="name">BUG标题</p>
                    <div class="content">
                        <el-input v-model="upBugData.title" placeholder="请输入内容"></el-input>
                    </div>
                </div>
                <div class="item">
                    <p class="name">出现频率</p>
                    <div class="content">
                        <el-radio v-model="upBugData.frequency" label="1" border>固定重现</el-radio>
                        <el-radio v-model="upBugData.frequency" label="2" border>测试随机</el-radio>
                        <el-radio v-model="upBugData.frequency" label="3" border>无法重现</el-radio>
                    </div>
                </div>
                <div class="item">
                    <p class="name">BUG类型</p>
                    <div class="content">
                        <el-radio v-model="upBugData.severity" label="1" border>严重错误</el-radio>
                        <el-radio v-model="upBugData.severity" label="2" border>主要错误</el-radio>
                        <el-radio v-model="upBugData.severity" label="3" border>一般错误</el-radio>
                        <el-radio v-model="upBugData.severity" label="4" border>建议</el-radio>
                    </div>
                </div>
                <div class="item">
                    <p class="name">处理人</p>
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
            <div class="right">
                <p class="name">描述与截图</p>
                <editor :content="content" v-on:change="getEditorVal" />
            </div>
        </div>
        <div class="btn-box">
            <el-button type="primary" @click="save">创建</el-button>
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
        display: flex;
        border: 1px solid #eee;
        border-radius: 5px;
        padding: 20px;
        .left{
            width: 40%;
            .item{
                margin-bottom: 20px;
                font-size: 16px;
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
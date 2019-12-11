<template>
    <div class="new-bug">
        <div class="con">
            <div class="select-box">
                <div class="item">
                    <p class="name">任务:</p>
                    <div class="content">
                        <el-select v-model="upBugData.taskId" filterable placeholder="请选择" @change="getUser">
                            <el-option
                                    v-for="item in taskList"
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
                                    v-for="item in selectData.frequency"
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
                        <el-select v-model="upBugData.severity" filterable placeholder="请选择">
                            <el-option
                                    v-for="item in selectData.severity"
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
                <div class="item" v-if="editBugData.isEdit">
                    <p class="name">状态:</p>
                    <div class="content">
                        <el-select v-model="upBugData.status" filterable placeholder="请选择">
                            <el-option
                                    v-for="item in selectData.statusName"
                                    :key="item.id"
                                    :label="item.name"
                                    :value="item.id">
                            </el-option>
                        </el-select>
                    </div>
                </div>
            </div>
            <div class="input-box">
                <div class="item" v-if="editBugData.isEdit">
                    <p class="name">任务名称</p>
                    <div class="content">
                        <p>{{editData.taskName}}</p>
                        <!--<el-input v-model="upBugData.title" placeholder="请输入内容"></el-input>-->
                    </div>
                </div>
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
            </div>
        </div>
        <div class="btn-box">
            <el-button type="primary" @click="save">保存</el-button>
            <!--<el-button @click="goBack">取消</el-button>-->

        </div>
    </div>
</template>

<script>
    import http from '../../lib/Http'
    import editor from "./Editor.vue";
    export default {
        name: "NewBug",
        props: {
            editBugData: Object,
        },
        components: { editor },
        data(){
          return {
              handlerEr: [],
              content:'',
              upBugData:{
                  description: "",
                  frequency: 1,
                  handlerId: '',
                  severity: 3,
                  taskId: '',
                  title: "",
                  status:'',
                  tbId:0,
              },
              selectData:{
                  frequency:[
                      {
                          id:1,
                          name:'固定重现',
                      },
                      {
                          id:2,
                          name:'测试随机',
                      },
                      {
                          id:3,
                          name:'无法重现',
                      }
                  ],
                  severity:[
                      {
                          id:1,
                          name:'建议',
                      },
                      {
                          id:2,
                          name:'一般错误',
                      },
                      {
                          id:3,
                          name:'主要错误',
                      },
                      {
                          id:4,
                          name:'严重错误',
                      },
                  ],
                  statusName:[
                      {
                          id:1,
                          name:'已分派',
                      },
                      {
                          id:2,
                          name:'已解决',
                      },
                      {
                          id:3,
                          name:'已关闭',
                      },
                      {
                          id:4,
                          name:'打回',
                      }
                  ],
              },
              taskList:[],
              editData:{}

          }
        },
        watch:{
            editBugData:{
                handler(n,o){
                    this.getDefaultDatas();
                },
                // immediate: true,  //刷新加载 立马触发一次handler
                deep: true  // 可以深度检测对象的属性值的变化
            }
        },
        created() {
            this.getDefaultDatas();
        },
        methods:{
            getDefaultDatas(){
                if(this.editBugData.isEdit){
                    this.getEditData();
                }else {
                    this.upBugData.description = '';
                    this.content = '';
                    this.upBugData.status = 0;
                    this.upBugData.severity = 3;
                    this.upBugData.handlerId = '';
                    this.upBugData.frequency = 1;
                    this.upBugData.title = '';
                    this.upBugData.tbId = 0;
                }
                http.zsyGetHttp(`/task-bug/task/testing`, {}, (res) => {
                    this.taskList = res.data;
                });
            },
            getUser(){
                http.zsyGetHttp(`/task-bug/users/${this.upBugData.taskId}`, {}, (res) => {
                    this.handlerEr = res.data;
                })
            },
            getEditData(){
                this.upBugData.taskId = this.editBugData.taskId;
                http.zsyGetHttp(`/task-bug/detail/${this.editBugData.tbId}`, {}, (res) => {
                    this.editData = res.data;
                    this.upBugData.description = res.data.description;
                    this.content = res.data.description;
                    this.upBugData.status = res.data.status;
                    this.upBugData.severity = res.data.severity;
                    this.upBugData.handlerId = res.data.handlerId;
                    this.upBugData.frequency = res.data.frequency;
                    this.upBugData.title = res.data.title;
                    this.upBugData.tbId = res.data.tbId;
                });
                this.getUser();
            },
            goBack(){
                this.$router.go(-1)
            },
            getEditorVal(val){
                this.upBugData.description = val
            },
            save(){
                // console.log(this.upBugData)
                if(this.editBugData.isEdit){
                    http.zsyPutHttp(`/task-bug/edit`, this.upBugData, (res) => {
                        this.$message({
                            message: '编辑成功',
                            type: 'success'
                        });
                        // this.goBack();
                    })
                }else {
                    http.zsyPostHttp(`/task-bug/add`, this.upBugData, (res) => {
                        this.$message({
                            message: '创建成功',
                            type: 'success'
                        });
                        // this.goBack();
                    })
                }


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
                width: 140px;
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
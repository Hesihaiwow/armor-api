<template>
    <div class="test-examples">
        <div class="aside">
            <Tree :task-id="taskId"></Tree>
        </div>
        <div class="main">
            <div class="upload-box" v-if="isIndex">
                <el-upload
                        class="upload-demo"
                        ref="record"
                        action=""
                        :http-request="uploadRecordToMysql"
                        accept="application/vnd.ms-excel ,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/msexcel,">
                    <el-button slot="trigger" size="small" type="primary">上传文件</el-button>
                    <div slot="tip" class="el-upload__tip">只能上传Excel文件</div>
                </el-upload>
            </div>
            <router-view></router-view>
        </div>

    </div>
</template>

<script>
    import http from '../../lib/Http'
    import Tree from './Tree'

    export default {
        name: "Index",
        components: {
            Tree
        },
        data(){
          return{
              taskId:'',
              isIndex:true,
          }
        },
        watch:{
            '$route': 'getDefaultData'
        },
        methods:{
            getDefaultData(){
                this.taskId=this.$route.query.id;
                // console.log( this.$router.history.current.path,111);
               let e =  this.$router.history.current.path.indexOf('/edit');
                let l =  this.$router.history.current.path.indexOf('/look');
                if(e>-1||l>-1){
                    this.isIndex = false;
                }else {
                    this.isIndex = true;
                }
                console.log(this.isIndex,1)
            },
            uploadRecordToMysql(file){
                let _this = this;
                var data = new FormData();
                data.append('uploadFile', file.file);
                http.zsyPostHttp(`/test-example/import/${this.taskId}`,data,(res)=>{
                    this.$refs.record.clearFiles();
                    if (res.errMsg == "执行成功"){
                        this.fullscreenLoading = false;
                        this.$message({
                            showClose: true,
                            message: '导入成功',
                            type: 'success'
                        });
                        setTimeout(function(){
                            _this.getDefaultData();
                        }, 1000);

                    }else {
                        // this.uploadToMysqlVisible = false;
                        // this.fullscreenLoading = false;
                    }
                },(fail)=>{
                    this.$message({
                        showClose: true,
                        message: fail.errMsg,
                        type: 'error'
                    });
                    // this.fullscreenLoading = false;
                })
            },
        },
        created () {
            this.getDefaultData();
        }

    }
</script>

<style scoped lang="less">
    .test-examples {
        width: 100%;
        display: flex;
        flex-direction:row;
        flex: 1;
        .aside {
            flex-shrink: 0;
            width: 300px;

        }
        .main {
            flex: 1;
            flex-basis: auto;
            padding: 20px;
            background-color: #fff;
            border-left: 2px solid #cccccc;
            .upload-box{
                padding: 20px 0;
                text-align: right;
                background-color: #fff;
            }
        }

    }
</style>
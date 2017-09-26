<template>
  <div class="project-con">
    <el-tabs v-model="activeName" @tab-click="">
        <div v-if="isAdmin">
            <el-tab-pane label="标签管理" name="tag"  style="line-height:50px">
                <el-button  :key="tag.id" hit="true" close-transition="true" :type="color[tag.color]" size="large"
                            v-for="tag in tagList"
                            :closable="true"
                            :close-transition="false" >
                    {{tag.name}}
                </el-button>
                <el-collapse v-model="activeNames" style="position: relative;margin-top:50px;width: 550px" accordion>
                    <el-collapse-item title="添加标签" name="1">
                        <el-input style="width: 200px" placeholder="请输入标签内容" v-model="addName" ></el-input>
                        <el-button @click="saveTag">确认</el-button>
                    </el-collapse-item>
                    <el-collapse-item title="删除标签" name="2">
                        <el-select v-model="TagName"  filterable placeholder="请选择">
                            <el-option
                                    filterable
                                    v-for="tag in tagList"
                                    :key="tag.id"
                                    :label="tag.name"
                                    :value="tag.name">
                            </el-option>
                        </el-select>
                        <el-button @click="deleteTag()">删除</el-button>
                    </el-collapse-item>
                </el-collapse>
            </el-tab-pane>
            <el-tab-pane label="阶段管理" name="stage" >
                <el-table :data="stageList" style="width: 40%" >
                    <el-table-column
                            label="阶段优先级"
                            prop="sort" align="center"></el-table-column>
                    <el-table-column
                            label="阶段名称"
                            prop="name"></el-table-column>
                    <el-table-column  label="编辑">
                        <template scope="scope">
                            <el-button
                                    size="small"
                                    type="danger"
                                    @click="deleteStage(scope.$index, stageList)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <div class="stage">
                    <div class="stage-box">
                        <!--<span style="display:block;font-size: 15px"></span>-->
                        <el-tag  style="display: block;width:70px;font-size: 15px;margin-top: 5px">添加阶段</el-tag>
                        <el-input style="width: 250px;margin-bottom:20px;margin-top:20px;margin-left: 15px;" placeholder="请输入阶段优先级" v-model="stage.sort">
                            <template slot="prepend">阶段优先级:</template>
                        </el-input>
                        <el-input style="width: 200px;margin-bottom:20px;margin-left: 15px;" placeholder="请输入阶段名称" v-model="stage.name" >
                            <template slot="prepend">阶段名称:</template>
                        </el-input>
                        <div><el-button @click="saveStage" type="primary" style="position: relative;left: 285px">确认</el-button></div>
                    </div>
                </div>
            </el-tab-pane>
        </div>
        <div v-else=""></div>
      <el-tab-pane label="项目管理" name="project">
        <div class="task-item" v-for="list in TaskItem">
          <img v-if="list.imageUrl" :src="list.imageUrl" class="task-logo" style="width: 40px;height: 40px;border-radius: 50%;">
          <img v-else="" src="../assets/img/u431.png" class="task-logo">
          <div class="task-info"  @click="editProject(list.id,list.name,list.description,list.imageUrl)">
            <div class="task-name">{{list.name}}</div>
            <div class="task-sub-name">{{list.description}}</div>
          </div>
        </div>
        <div class="add-task-item" v-show="hasPermission" @click="addTask">
          <div class="add-task-btn">＋</div>
          <div class="add-task-msg">创建新项目</div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <div class="add-task-pop" v-show="showAddTask">
      <div class="add-task-pop-con">
        <div class="add-task-top">
          创建项目<span class="close" @click="hidePop">×</span>
        </div>
        <el-upload
                class="avatar-uploader"
                action=""
                :show-file-list="false"
                :http-request="upload"
                :before-upload="beforeAvatarUpload">
          <img v-if="project.imageUrl" :src="project.imageUrl" class="avatar" >
          <img v-else="" src="../assets/img/u1285.png" class="avatar" >
          <!--<i v-else><img src="../assets/img/u1284.png" alt="" class="att-img"></i>-->
          <p class="att-msg">点击图片为你的项目上传项目图</p>
        </el-upload>
        <input type="text" class="project-name" placeholder="项目名称" v-model="project.name">
        <textarea class="project-intro" placeholder="项目简介（选填）" v-model="project.description"></textarea>
        <div class="att-bents">
          <span class="cancel" @click="hidePop">取消</span>
          <span class="save" @click="saveProject">保存</span>
        </div>
      </div>
    </div>
    <div class="add-task-pop" v-show="editProjectVisible">
      <div class="add-task-pop-con">
        <div class="add-task-top">
          编辑项目<span class="close" @click="hideEdit">×</span>
        </div>
        <el-upload
              class="avatar-uploader"
              action=""
              :show-file-list="false"
              :http-request="upload"
              :before-upload="beforeAvatarUpload">
          <img v-if="project.imageUrl" :src="project.imageUrl" style="height: 180px;width: 200px">
          <img v-else="" src="../assets/img/u1285.png" style="height: 180px;width: 200px">
        </el-upload>
        <p class="att-msg">点击图片为你的项目上传项目图</p>
      <input type="text" class="project-name" :placeholder="project.name" v-model="project.name">
      <textarea class="project-intro" placeholder="项目简介（选填）" v-model="project.description"></textarea>
      <div class="att-bents">
        <span class="delete" @click="deleteWindow">删除项目</span>
        <span class="cancel" @click="hideEdit">取消</span>
        <span class="save" @click="updateProject">保存</span>
      </div>
      </div>
    </div>
  </div>
</template>
<script>
  import Http from '../lib/Http'
  import Helper from '../lib/Helper'
  import { Message } from 'element-ui';
  import ElButton from "../../node_modules/element-ui/packages/button/src/button";
  import ElTabPane from "../../node_modules/element-ui/packages/tabs/src/tab-pane";
  import ElInput from "../../node_modules/element-ui/packages/input/src/input";
  import ElForm from "../../node_modules/element-ui/packages/form/src/form";

  export default {
    components: {
        ElForm,
        ElInput,
        ElTabPane,
        ElButton},
    name: 'Project',
    data() {
      return {
        activeName:"project",
        activeNames: 1,
        editProjectVisible:false,
        inputVisible:true,
        TaskItem:'',
        showAddTask: false,
        tagList:[],
        stageList:[],
        addName:'',
        TagName:'',
        TagId:'',
        stage:{
            name: '',
            sort: '',
        },
        project:{
          imageUrl:'',
          name: '',
          description: ''
        },//待编辑项目ID
        editProjectId:'',
        color:['','primary','success','warning','danger','info','primary','success',],
      };
    },
    beforeMount:function () {
      //选中项目tab
      this.$root.eventBus.$emit("handleTabSelected", "project");
      this.projectList();
      this.fetchTagList();
      this.fetchStageList();
    },
    computed: {
        //是否有权限
        hasPermission () {
            return Helper.decodeToken().userRole<=1;
        },
        isAdmin(){
            return Helper.decodeToken().userRole==0;
        }
    },
    methods: {
      deleteWindow(){//删除项目弹出框
          this.$confirm('此操作将永久删除该项目, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            Http.zsyDeleteHttp("/project/delete/"+this.editProjectId,null,(res)=>{
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
            this.project.name = this.project.description = this.project.imageUrl = this.editProjectId= '';
            this.editProjectVisible = false;
            this.projectList();
            })
          }).catch(() => {
          });
      },
      editProject(id,name,description,url){
        if(this.hasPermission){
          this.editProjectId=id;
          this.project.name =name;
          this.project.description = description;
          this.project.imageUrl =url ;
          this.editProjectVisible = true;
        }
      },
      hideEdit(){
        this.project.name = this.project.description = this.editProjectId= '';
        this.editProjectVisible = false;
      },
      projectList(){
        Http.zsyGetHttp(Http.API_URI.PROJECT,null,(res)=>{
          this.TaskItem = res.data;
        })
      },
      updateProject(){
          if(this.saveAdd()){
            Http.zsyPutHttp("/project/update/"+this.editProjectId,this.project,(res)=>{
              Message.success("项目更新成功");
              this.project.name = this.project.description = this.editProjectId= '';
              this.editProjectVisible = false;
              this.projectList();
            });
          }
      },
      addTask () {
        this.showAddTask = true;
      },
      hidePop () {
        this.project.name = this.project.description = this.editProjectId= '';
        this.showAddTask = false;
      },
      saveAdd () {
        // 保存
        if (this.project.name!= ''&&this.project.name.trim().length!=0&&this.project.name.trim().length<15){
          return true;
        }else{
            Message.error("项目名称不能为空且不超过15字");
            return false;
        }
      },
      saveProject(){
          if(this.saveAdd()){
            Http.zsyPostHttp(Http.API_URI.ADDPROJECT,this.project,(res)=>{
              Message.success("项目添加成功");
              this.projectList();
              this.project.name = this.project.description = this.editProjectId= '';
              this.showAddTask = false;
            });
          }
      },
        beforeAvatarUpload(file) {
            const isImage = file.type.indexOf('image') === 0;
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isImage) {
                this.$message.error('请选择正确的图片文件');
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB');
            }
            return isImage && isLt2M;
        },
        upload(file) {
            var data = new FormData();
            data.append('uploadFile', file.file);
            Http.zsyPostHttp('/upload/image', data, (res) => {
                this.project.imageUrl = res.data.url;
            })
        },
        fetchTagList() {
            Http.zsyGetHttp('/tag/list', {}, (resp) => {
                this.tagList = resp.data;
            })
        },
        saveTag(){
            if (this.addName!=''&&this.addName.trim().length>0&&this.addName.length<10) {
                Http.zsyPostHttp('/tag/add?name='+this.addName, null, (res) => {
                    this.$message.success('标签添加成功');
                    this.fetchTagList;
                    this.tagList.push({color: "7", id: res.data, name:this.addName});
                    this.addName='';
                })
            }else{
                this.$message.error('标签名不为空且不超过10个字');
            }
        },
        deleteTag(){
            var num = 0;
            this.tagList.forEach((tag)=>{
                if (this.TagName == tag.name) {
                    this.TagId = tag.id;
                }
            })
            Http.zsyDeleteHttp('/tag/'+this.TagId, null, (res) => {
                this.$message.success('标签删除成功');
                this.$router.go(0)
                this.fetchTagList;
                this.TagName='';
            })
        },
        fetchStageList(){
            Http.zsyGetHttp('/stage/list', {}, (resp) => {
                this.stageList = resp.data
            })
        },
        saveStage(){
            console.log(this.stage.sort)
            if(isNaN(this.stage.sort)||this.stage.sort.trim()<1||this.stage.sort==''){
                this.$message.error('阶段优先级必须为大于1的数字');
                return false;
            }else if (this.stage.name!=''&&this.stage.name.trim().length>0&&this.stage.name.length<10) {
                Http.zsyPostHttp('/stage/add', this.stage, (res) => {
                    this.$message.success('阶段添加成功');
                    this.stage.name= this.stage.sort = '';
                    this.fetchStageList();
                })
            }else{
                this.$message.error('阶段名称不为空且不超过10个字');
            }
        },
        deleteStage(index,rows){
            Http.zsyDeleteHttp(`/stage/`+rows[index].id, {}, (resp) => {
                this.$message.success('阶段删除成功');
                rows.splice(index, 1);
            })
        },
    }
  }
</script>
<style scoped>
.project-con{width: 1100px;margin: auto;}
.mic-title{font-size: 18px;color: #000;position: relative;margin: 0 10px;}
.task-item,.add-task-item{background: #fff;display: -webkit-flex;display: -moz-flex;display: -ms-flex;display: -o-flex;display: flex;padding: 14px 16px;margin: 16px 10px;cursor: pointer;}
.task-item:hover,.add-task-item:hover{box-shadow: 0 0 20px #ccc;}
.task-info{flex: 1;-webkit-flex:1;-moz-flex:1;-ms-flex:1;-o-flex:1;}
.task-name{font-size: 18px;line-height: 26px;}
.task-sub-name{font-size: 12px;color: #999999;line-height: 20px;}
.task-logo{margin-right: 20px;height: 30px;margin-top: 6px;}
.add-task-btn{width: 40px;height: 40px;background: #F2F2F2;color: #CCCCCC;line-height: 34px;text-align: center;font-size: 36px;}
.add-task-msg{line-height: 40px;margin-left: 26px;font-size: 14px;color: #999999;}
.add-task-pop{position: fixed;top: 0;left: 0;bottom: 0;right: 0;background: rgba(0,0,0,0.5);z-index: 110;}
.add-task-pop-con{position: absolute;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 350px;height: 450px;background: #fff;}
.add-task-top{text-align: center;font-size: 16px;font-weight: bold;margin: 10px;border-bottom: 1px solid #ccc;line-height: 30px;padding-bottom: 10px;position: relative;}
.add-task-top .close{position: absolute;right: 0;font-size: 28px;font-weight: normal;color: #999;cursor: pointer;transition:0.8s ease all;width: 30px;height: 30px;text-align: center;line-height: 24px;}
.add-task-top .close:hover{color:#36A8FF;transform:rotate(360deg);}
.att-img{margin: 16px 0;}
.att-msg{text-align: center;color: #A7A7A7;margin-bottom: 24px;}
.project-name,.project-intro{width: 90%;border: 1px solid #ccc;height: 36px;line-height: 36px;display: block;margin: 16px auto;border-radius: 3px;text-indent: 10px;font-size: inherit;}
.att-bents{text-align: right;margin: 20px;}
.att-bents > span{display: inline-block;width: 60px;height: 26px;text-align: center;line-height: 26px;font-size: 14px;cursor: pointer;border-radius: 4px;}
.cancel{margin-right: 10px;border: 1px solid #D0D3D3;}
.cancel:hover{background: #fff;border: 1px solid #36A8FF;color: #36A8FF;font-weight: 700;}
.save{background: #36A8FF;color: #fff;}
.delete{background: #FF4949;color: #fff;float:left;}
.stage{float:right; display:inline; margin-bottom: auto;position: absolute; right: 200px; top: 45px;}
.stage-box{padding:10px; width:350px; height:200px; border: 2px solid #999999; -moz-border-radius: 15px;-webkit-border-radius: 15px;border-radius:10px;}


</style>

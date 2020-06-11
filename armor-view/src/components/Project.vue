<template>
  <div class="project-con">
    <el-tabs v-model="activeName" @tab-click="handleClick">
        <div v-if="isAdmin">
            <el-tab-pane label="标签管理" name="tag"  style="line-height:50px">
                <el-tag
                        v-for="tag in tagList"
                        :key="tag.name"
                        :closable="true"
                        :close-transition="false"
                        @close="deleteTagButton(tag)"
                        :type="color[tag.color]" style="width: 100px;height:40px;line-height:40px;text-align: center;font-size: 15px;margin-left:10px"
                        :style="'width:'+(tag.name.length)*40+'px'">{{tag.name}}</el-tag>
                <el-input
                        style="width: 100px;height:40px;line-height:40px;font-size: 15px;"
                        class="input-new-tag"
                        v-if="inputVisible"
                        v-model="TagName"
                        ref="saveTagInput"
                        size="large"
                        @blur="handleInputTagConfirm">
                </el-input>
                <el-button v-else class="button-new-tag" size="large" @click="showTag">+ 新增标签</el-button>
            </el-tab-pane>
            <el-tab-pane label="阶段管理" name="stage" >
            <el-button type="primary" size="middle" style="margin-bottom: 10px;margin-left: 1000px" @click="addStageVisible=true;clearStage()">添加阶段</el-button>
            <el-table :data="stageList" style="width: 100%;margin: auto" >
                <el-table-column
                        label="序号"
                        prop="num" style="text-align: center"></el-table-column>
                <el-table-column
                        label="阶段名称"
                        prop="name"></el-table-column>
                <!--<el-table-column-->
                <!--label="阶段优先级"-->
                <!--prop="sort" align="center"></el-table-column>-->
                <el-table-column  label="操作" align="center">
                    <template slot-scope="scope">
                        <el-button type="text" v-if="scope.row.id != scope.row.firstId" @click="moveStage(scope.row.id,0)">上移</el-button>
                        <el-button type="text" v-if="scope.row.id != scope.row.lastId" @click="moveStage(scope.row.id,1)">下移</el-button>
                        <el-button
                                size="small"
                                type="primary"
                                @click="editStageDialog(scope.$index, stageList)">编辑</el-button>
                        <el-button
                                size="small"
                                type="danger"
                                @click="deleteStage(scope.$index, stageList)">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
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

        <el-tab-pane label="模块管理" name="taskTempModule">
        <div class="task-item" v-for="list in taskTempModuleData">
          <img v-if="list.imageUrl" :src="list.imageUrl" class="task-logo" style="width: 40px;height: 40px;border-radius: 50%;">
          <img v-else="" src="../assets/img/u431.png" class="task-logo">
          <div class="task-info"  @click="editTaskTempModule(list.id,list.name,list.description,list.imageUrl)">
            <div class="task-name">{{list.name}}</div>
            <div class="task-sub-name">{{list.description}}</div>
          </div>
        </div>
        <div class="add-task-item" v-show="hasPermission" @click="addTaskTempModule">
          <div class="add-task-btn">＋</div>
          <div class="add-task-msg">创建新模块</div>
        </div>
      </el-tab-pane>
        <el-tab-pane label="发布平台管理" name="platform"  style="line-height:50px">
            <el-tag
                    v-for="platform in platformList"
                    :key="Number(platform.id)"
                    :close-transition="false"
                    :type="color[platform.color]" style="width: 100px;height:40px;line-height:40px;text-align: center;font-size: 15px;margin-left:10px"
                    :style="'width:'+(platform.name.length)*40+'px'">{{platform.name}}</el-tag>
            <el-input
                    style="width: 100px;height:40px;line-height:40px;font-size: 15px;"
                    class="input-new-tag"
                    v-if="addPlatformVisible"
                    v-model="platformName"
                    ref="platformName"
                    size="large"
                    @blur="handleInputPlatformConfirm">
            </el-input>
            <el-button v-else class="button-new-tag" size="large" @click="showPlatform">+ 新增平台</el-button>
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
          <span class="save" @click="saveProject" :loading="isSaving">保存</span>
        </div>
      </div>
    </div>
    <div class="add-task-pop" v-show="showAddTaskTempModule">
      <div class="add-task-pop-con">
        <div class="add-task-top">
          创建模块<span class="close" @click="hidePop">×</span>
        </div>
        <el-upload
                class="avatar-uploader"
                action=""
                :show-file-list="false"
                :http-request="upload2"
                ref='uploadPic'
                :before-upload="beforeAvatarUpload">
          <img v-if="taskTempModule.imageUrl" :src="taskTempModule.imageUrl" class="avatar" >
          <img v-else="" src="../assets/img/u1285.png" class="avatar" >
          <!--<i v-else><img src="../assets/img/u1284.png" alt="" class="att-img"></i>-->
          <p class="att-msg">点击图片为你的模块上传模块图</p>
        </el-upload>
        <input type="text" class="project-name" placeholder="模块名称" v-model="taskTempModule.name">
        <textarea class="project-intro" placeholder="模块简介（选填）" v-model="taskTempModule.description"></textarea>
        <div class="att-bents">
          <span class="cancel" @click="hidePop">取消</span>
          <span class="save" @click="saveTaskTempModule" :loading="isSaving">保存</span>
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
    <div class="add-task-pop" v-show="editTaskTempModuleVisible">
      <div class="add-task-pop-con">
        <div class="add-task-top">
          编辑模块<span class="close" @click="hideEdit">×</span>
        </div>
        <el-upload
              class="avatar-uploader"
              action=""
              :show-file-list="false"
              ref='uploadPic2'
              :http-request="upload2"
              :before-upload="beforeAvatarUpload">
          <img v-if="taskTempModule.imageUrl" :src="taskTempModule.imageUrl" style="height: 180px;width: 200px">
          <img v-else="" src="../assets/img/u1285.png" style="height: 180px;width: 200px">
        </el-upload>
        <p class="att-msg">点击图片为你的模块上传模块图</p>
      <input type="text" class="project-name" :placeholder="taskTempModule.name" v-model="taskTempModule.name">
      <textarea class="project-intro" placeholder="模块简介（选填）" v-model="taskTempModule.description"></textarea>
      <div class="att-bents">
        <span class="delete" @click="deleteTaskTempModule">删除模块</span>
        <span class="cancel" @click="hideEdit">取消</span>
        <span class="save" @click="updateTaskTempModule">保存</span>
      </div>
      </div>
    </div>
    <el-dialog  title="添加阶段"
                :visible.sync="addStageVisible"
                size="tiny" >
            <!--<el-input style="text-align:center;margin-bottom: 10px" placeholder="请输入阶段优先级（请勿重复）" v-model="stage.sort">-->
            <!--<template slot="prepend">阶段优先级:</template>-->
            <!--</el-input>-->
            <el-input style="text-align:center;" placeholder="请输入阶段名称" v-model="stage.name" >
            <template slot="prepend">阶段名称:</template>
            </el-input>
            <div slot="footer"  class="dialog-footer">
                <el-button @click="saveStage" type="primary" :loading="isSaving">确认</el-button>
            </div>
    </el-dialog>
      <el-dialog  title="编辑阶段"
                  :visible.sync="editStageVisible"
                  size="tiny">
              <!--<el-input style="margin-bottom: 10px" :placeholder="stage.sort+''" v-model="stage.sort">-->
                  <!--<template slot="prepend">阶段优先级:</template>-->
              <!--</el-input>-->
              <el-input style="" :placeholder="stage.name" v-model="stage.name" >
                  <template slot="prepend">阶段名称:</template>
              </el-input>
              <div slot="footer" class="dialog-footer">
                  <el-button @click="editStage" type="primary" >确认</el-button>
              </div>

      </el-dialog>
  </div>
</template>
<script>
  import Http from '../lib/Http'
  import Helper from '../lib/Helper'
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
          isSaving:false,
        activeName:"project",
        activeNames: 1,
        editProjectVisible:false,
        // inputVisible:true,
        addPlatformVisible:false,
        addStageVisible:false,
        editStageVisible:false,
        TaskItem:'',
        showAddTask: false,
        tagList:[],
        platformList:[],
        stageList:[],
        TagName:'',
        platformName:'',
        TagId:'',
        inputVisible:false,
        stage:{
            id:'',
            name: '',
        },
        project:{
          imageUrl:'',
          name: '',
          description: ''
        },//待编辑项目ID
        editProjectId:'',
        color:['','primary','success','warning','danger','info','primary','success',],

          taskTempModuleData:[],
          taskTempModule:{
              id:'',
              name:'',
              description:'',
              imageUrl:''
          },
          showAddTaskTempModule: false,
          editTaskTempModuleVisible: false,
          editTaskTempModuleId:'',

      };
    },
    beforeMount:function () {
      //选中项目tab
      this.$root.eventBus.$emit("handleTabSelected", "project");
      this.projectList();
      // this.fetchTagList();
      // this.fetchStageList();
      // this.fetchPlatformList();
      // this.fetchTaskTempModuleList();
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
        handleClick(){
          if (this.activeName === 'project'){
              this.projectList();
          }  else if (this.activeName === 'taskTempModule') {
              this.fetchTaskTempModuleList();
          }else if (this.activeName === 'platform') {
              this.fetchPlatformList();
          }else if (this.activeName === 'stage') {
              this.fetchStageList()
          }else if (this.activeName === 'tag') {
              this.fetchTagList();
          }
        },
      deleteWindow(){//删除项目弹出框
          this.$confirm('此操作将永久删除该项目, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            Http.zsyDeleteHttp("/project/delete/"+this.editProjectId,null,(res)=>{
            this.successMsg('删除成功')
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
        this.taskTempModule.name = this.taskTempModule.description = this.editTaskTempModuleId = this.taskTempModule.imageUrl =  '';
        this.editTaskTempModuleVisible = false;
          this.$refs.uploadPic2.clearFiles();

      },
      projectList(){
        Http.zsyGetHttp(Http.API_URI.PROJECT,null,(res)=>{
          this.TaskItem = res.data;
        })
      },
      updateProject(){
          if(this.saveAdd()){
            Http.zsyPutHttp("/project/update/"+this.editProjectId,this.project,(res)=>{
              this.successMsg('项目更新成功');
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
        this.taskTempModule.name = this.taskTempModule.description  = this.taskTempModule.imageUrl = '';
        this.showAddTaskTempModule = false;
        this.$refs.uploadPic.clearFiles();
      },
      saveAdd () {
        // 保存
        if (this.project.name!= ''&&this.project.name.trim().length!=0&&this.project.name.trim().length<15){
          return true;
        }else{
            this.warnMsg("项目名称不能为空且不超过15字");
            return false;
        }
      },
      saveProject(){
          this.isSaving = true;
          if(this.saveAdd()){
            Http.zsyPostHttp(Http.API_URI.ADDPROJECT,this.project,(res)=>{
              this.successMsg("项目添加成功");
              this.projectList();
              this.project.name = this.project.description = this.editProjectId= '';
              this.showAddTask = false;
                this.isSaving = false
            });
          }
      },
        beforeAvatarUpload(file) {
            const isImage = file.type.indexOf('image') === 0;
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isImage) {
                this.errorMsg("请选择正确的图片文件");
            }
            if (!isLt2M) {
                this.errorMsg("上传头像图片大小不能超过 2MB");
            }
            return isImage && isLt2M;
        },
        upload(file) {
            var data = new FormData();
            data.append('uploadFile', file.file);
            Http.zsyPostHttp('/upload/ucloud/image', data, (res) => {
                this.project.imageUrl = res.data.url;
            })
        },
        fetchTagList() {
            Http.zsyGetHttp('/tag/list', {}, (resp) => {
                this.tagList = resp.data;
            })
        },
        fetchPlatformList() {
            Http.zsyGetHttp('/platform/list', {}, (resp) => {
                this.platformList = resp.data;
            })
        },
        deleteTagButton(tag){
            this.$confirm('确认删除?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                Http.zsyDeleteHttp('/tag/'+tag.id, null, (res) => {
                    this.successMsg("标签删除成功");
                    this.tagList.splice(this.tagList.indexOf(tag), 1);
                })
            }).catch(() => {
            });
        },
        fetchStageList(){
            Http.zsyGetHttp('/stage/list', {}, (resp) => {
                this.stageList = resp.data
            })
        },
        saveStage(){
            this.isSaving = true
            var reg = /^\+?[1-9][0-9]*$/;
//            this.stage.sort = this.stage.sort.trim();
            this.stage.name = this.stage.name+"".trim();
//            if(!reg.test(this.stage.sort)||this.stage.sort<1||this.stage.sort==''||this.stage.sort>100){
//                this.warnMsg("阶段优先级须为大于0并小于100的整数");
//                return false;
//            }else
            if (this.stage.name!=''&&this.stage.name.length>0&&this.stage.name.length<=10) {
                Http.zsyPostHttp('/stage/add', this.stage, (res) => {
                    this.successMsg("阶段添加成功");
                    this.stage.name= this.stage.sort = '';
                    this.fetchStageList();
                    this.addStageVisible = false;
                    this.isSaving = false
                })
            }else{
                this.warnMsg("阶段名称不为空且不超过10个字");
            }
        },
        moveStage(id,sortType){
            Http.zsyPutHttp(`/stage/move/${id}/${sortType}`, null, (res) => {
                this.fetchStageList();
            })
        },
        clearStage(){
            this.stage.name = '';
        },
        deleteStage(index,rows){
            this.$confirm('确认删除?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                Http.zsyDeleteHttp(`/stage/`+rows[index].id, {}, (resp) => {
                    this.successMsg("阶段删除成功");
                    this.fetchStageList();
                })
            }).catch(() => {
            });
        },
        editStageDialog(index,rows){
            this.editStageVisible = true;
            this.stage.id= rows[index].id;
            this.stage.sort = rows[index].sort;
            this.stage.name = rows[index].name;
        },
        editStage(){
            var reg = /^\+?[1-9][0-9]*$/;
//            if(!reg.test(this.stage.sort)||this.stage.sort<1||this.stage.sort==''||this.stage.sort>100){
//                this.warnMsg("阶段优先级须为大于0并小于100的整数");
//                return false;
//            }else
            if (this.stage.name!=''&&this.stage.name.trim().length>0&&this.stage.name.trim().length<=10) {
                Http.zsyPostHttp(`/stage/edit`, this.stage, (resp) => {
                    this.successMsg("阶段更新成功");
                    this.fetchStageList()
                    this.editStageVisible = false;
                })
            }else{
                this.warnMsg("阶段名称不为空且不超过10个字");
            }
        },
        showTag() {
            this.inputVisible = true;
            this.$nextTick(_ => {
                this.$refs.saveTagInput.$refs.input.focus();
            });
        },
        showPlatform() {
            this.addPlatformVisible = true;
            this.$nextTick(_ => {
                this.$refs.platformName.$refs.input.focus();
            });
        },
        handleInputTagConfirm() {
            if (this.TagName!=''&&this.TagName.trim().length>0&&this.TagName.length<=10) {
                Http.zsyPostHttp('/tag/add?name='+this.TagName, null, (res) => {
                    this.successMsg("标签添加成功");
                    this.fetchTagList;
                    this.tagList.push({color: "7", id: res.data, name:this.TagName});
                    this.inputVisible = false;
                    this.TagName = '';
                })
            }else{
                this.inputVisible = false;
                this.TagName = '';
                this.warnMsg("标签名不为空且不超过10个字");
            }
        },
        handleInputPlatformConfirm() {
            if (this.platformName!=''&&this.platformName.trim().length>0&&this.platformName.length<=20) {
                Http.zsyPostHttp('/platform/add?name='+this.platformName, null, (res) => {
                    this.successMsg("平台添加成功");
                    this.fetchPlatformList();
                    this.platformList.push({color: "7", id: res.data, name:this.platformName});
                    this.addPlatformVisible = false;
                    this.platformName = '';
                })
            }else{
                this.addPlatformVisible = false;
                this.platformName = '';
                this.warnMsg("平台名不为空且不超过20个字");
            }
        },
        msg(msg) {
            this.$message({
                showClose: true,
                message: msg
            });
        },
        successMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'success'
            });
        },

        warnMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'warning'
            });
        },
        errorMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'error'
            });
        },

        //查询 临时任务涉及项目
        fetchTaskTempModuleList(){
            Http.zsyGetHttp('/task-temp-module/list',{},(res)=>{
                this.taskTempModuleData = res.data;
            })
        },
        addTaskTempModule(){
            this.showAddTaskTempModule = true;
        },
        upload2(file) {
            var data = new FormData();
            data.append('uploadFile', file.file);
            Http.zsyPostHttp('/upload/ucloud/image', data, (res) => {
                this.taskTempModule.imageUrl = res.data.url;
            })
        },
        saveTaskTempModule(){
            this.isSaving = true
            if(this.taskTempModule.name.trim() === undefined
                || this.taskTempModule.name.trim() === null
                || this.taskTempModule.name.trim() ===''
                || this.taskTempModule.name.trim().length>15){
                this.warnMsg("模块或子系统名称不能为空且不超过15字");
                return;
            }
            Http.zsyPostHttp('/task-temp-module/add',this.taskTempModule,(res)=>{
                this.successMsg("模块添加成功");
                this.fetchTaskTempModuleList();
                this.taskTempModule.name = this.taskTempModule.description = this.taskTempModule.imageUrl= '';
                this.showAddTaskTempModule = false;
                this.isSaving = false;
            });

        },
        editTaskTempModule(id,name,description,url){
            if(this.hasPermission){
                this.editTaskTempModuleId=id;
                this.taskTempModule.name =name;
                this.taskTempModule.description = description;
                this.taskTempModule.imageUrl =url ;
                this.editTaskTempModuleVisible = true;
            }
        },
        updateTaskTempModule(){
            if(this.taskTempModule.name.trim() === undefined
                || this.taskTempModule.name.trim() === null
                || this.taskTempModule.name.trim() ===''
                || this.taskTempModule.name.trim().length>15){
                this.warnMsg("项目名称不能为空且不超过15字");
                return false;
            }
            Http.zsyPutHttp("/task-temp-module/update/"+this.editTaskTempModuleId,this.taskTempModule,(res)=>{
                this.successMsg('模块更新成功');
                this.taskTempModule.name = this.taskTempModule.description = this.editTaskTempModuleId = this.taskTempModule.imageUrl = '';
                this.editTaskTempModuleVisible = false;
                this.fetchTaskTempModuleList();
            });
        },
        deleteTaskTempModule(){//删除临时任务涉及项目弹出框
            this.$confirm('此操作将永久删除该模块, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                Http.zsyDeleteHttp("/task-temp-module/delete/"+this.editTaskTempModuleId,null,(res)=>{
                    this.successMsg('删除成功')
                    this.taskTempModule.name = this.taskTempModule.description = this.taskTempModule.imageUrl = this.editTaskTempModuleId= '';
                    this.editTaskTempModuleVisible = false;
                    this.fetchTaskTempModuleList();
                })
            }).catch(() => {
            });
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
.att-msg{text-align: center;color: #A7A7A7;margin-bottom: 24px;}
.project-name,.project-intro{width: 90%;border: 1px solid #ccc;height: 36px;line-height: 36px;display: block;margin: 16px auto;border-radius: 3px;text-indent: 10px;font-size: inherit;}
.att-bents{text-align: right;margin: 20px;}
.att-bents > span{display: inline-block;width: 60px;height: 26px;text-align: center;line-height: 26px;font-size: 14px;cursor: pointer;border-radius: 4px;}
.cancel{margin-right: 10px;border: 1px solid #D0D3D3;}
.cancel:hover{background: #fff;border: 1px solid #36A8FF;color: #36A8FF;font-weight: 700;}
.save{background: #36A8FF;color: #fff;}
.delete{background: #FF4949;color: #fff;float:left;}


</style>

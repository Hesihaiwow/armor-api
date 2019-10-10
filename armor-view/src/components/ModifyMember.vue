<template>
	<div class="add-member-pop" v-show="showAddPop">
    <div class="add-member-pop-con">
      <div class="ctpc-top clearfix">
        <span class="fl">修改成员</span><span class="ctpc-top-close fr" @click="hide">×</span>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">姓名</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="modifyForm.name" placeholder="请输入姓名"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">账号</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="modifyForm.account" placeholder="请输入账号"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">角色</div>
        <div class="ftp-msg fl">
          <el-select class="w280" v-model="modifyForm.jobRole" placeholder="请选择角色">
            <el-option
                    v-for="item in rolesList"
                    :key="item.roleId"
                    :label="item.roleName"
                    :value="item.roleId">
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">级别</div>
        <div class="ftp-msg fl">
          <el-select class="w280" v-model="modifyForm.level" placeholder="请选择级别">
            <el-option
                    v-for="item in levelList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">职位</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="modifyForm.jobName" placeholder="请输入职位"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">手机号</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="modifyForm.phone" placeholder="请输入手机号"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">邮箱地址</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="modifyForm.email" placeholder="请输入邮箱地址"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">所属部门</div>
        <div class="ftp-msg fl">
          <el-select class="w280" v-model="modifyForm.departmentId" placeholder="请选择部门">
            <el-option
                    v-for="item in deptOptions"
                    :key="item.id"
                    :label="item.label"
                    :value="item.id">
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">用户权限</div>
        <div class="ftp-msg fl">
          <el-select class="w280" v-model="modifyForm.userRole" placeholder="请选择权限">
            <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">考勤序号</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="modifyForm.checkSort" placeholder="请输入考勤序号"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">工号</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="modifyForm.jobNumber" placeholder="请输入工号"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">用户状态</div>
        <div class="ftp-msg fl">
          <el-select class="w280" v-model="modifyForm.status" placeholder="请选择状态">
            <el-option
                    v-for="item in statusOptions"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">审核人</div>
        <div class="ftp-msg fl">
          <i class="el-icon-plus" v-show="num>=1 && num < 3" @click="plus"></i>
          <i class="el-icon-minus" v-show="num>1"@click="minus(num-1)"></i>
          <div v-for="i in num"><span style="margin-right: 20px">{{i}}</span>
            <el-select placeholder="请选择审核人" @change="addCheckUser(i)" v-model="checkUserIdList[i-1]" clearable>
              <el-option
              v-for="item in userList"
              :key="item.id"
              :label="item.name"
              :value="item.id">
              </el-option>
            </el-select>
          </div>

        </div>
      </div>
      <!--<div class="am-warn">{{amWarn}}</div>-->
      <div class="ctpc-btns">
        <input type="button" class="ctpc-cancel" value="取消" @click="hide">
        <input type="button" class="ctpc-save" value="保存" @click="handleModifyUserClick">
      </div>
    </div>
	</div>
</template>
<script>
  import Http from '../lib/Http'
  import Helper from '../lib/Helper'

  export default {
    data() {
      return {
        //添加用户表单
        modifyForm:{
          userId:'',
          name:'',
          roleId:'',
          account:'',
          jobName:'',
          jobRole:'',
          level:'',
          phone:'',
          userRole:'',
          checkSort:'',
          departmentId:'',
          status:'',
            jobNumber:'',
          email:'',
            checkUserList:[],
        },
        rolesList:[{
            roleId: 1,
            roleName: '开发'
        }, {
            roleId: 0,
            roleName: '测试'
        }, {
            roleId: 2,
            roleName: '设计'
        }, {
            roleId: 3,
            roleName: '产品'
        }, {
            roleId: 5,
            roleName: '算法工程师'
        }, {
            roleId: 4,
            roleName: '其他'
        }],
          levelList:[
              {id:1,name:'一级'},
              {id:2,name:'二级'},
              {id:3,name:'三级'},
              {id:4,name:'四级'},
              {id:5,name:'五级'},
              {id:6,name:'六级'},
              {id:7,name:'七级'},
              {id:8,name:'八级'},
              {id:9,name:'九级'}
          ],
        //用户权限手
        options: [{
              value: 0,
              label: '超级管理员'
          }, {
              value: 1,
              label: '项目管理者'
          }, {
              value: 2,
              label: '普通成员'
        },{
            value:3,
            label:'人事行政'
        }],
        statusOptions:[{
              value:0,
              label:'正常使用'
        },{
              value:1,
              label:'冻结使用'
        }],
          deptOptions:[],
          showAddPop: false,
          checkUserIdList:[],
          num:1,
          userList:[]
      };
    },
      created(){
        this.fetchUserList()
      },
    methods: {
      //显示弹框
      show () {
        Http.zsyGetHttp(`/user/${this.modifyForm.userId}`,null,(res)=>{
            this.modifyForm.userId=res.data.id;
            this.modifyForm.name=res.data.name;
            this.modifyForm.account=res.data.account;
            this.modifyForm.jobName=res.data.jobName;
            this.modifyForm.phone=res.data.phone;
            this.modifyForm.userRole=res.data.userRole;
            this.modifyForm.jobRole=res.data.jobRole;
            this.modifyForm.level=res.data.level;
            this.modifyForm.status=res.data.status;
            this.modifyForm.email=res.data.email;
            this.modifyForm.departmentId=res.data.departmentId;
            this.modifyForm.checkSort=res.data.checkSort;
            this.modifyForm.jobNumber=res.data.jobNumber;
            this.modifyForm.checkUserList = res.data.checkUsers;
            if(this.modifyForm.checkUserList != null && this.modifyForm.checkUserList != [] && this.modifyForm.checkUserList.length > 0){
                this.num = this.modifyForm.checkUserList.length
                this.modifyForm.checkUserList.forEach(checkUser=>{
                    this.checkUserIdList.push(checkUser.id)
                })
            }
            this.showAddPop = true;
        });
          Http.zsyGetHttp(`/dept/tree`,null,(res)=>{
              this.deptOptions=res.data.children;
              this.deptOptions.unshift({id:res.data.id,label:res.data.label});
          });
      },
      //隐藏弹框
      hide () {
        this.showAddPop = false;
        this.modifyForm.userId='';
        this.modifyForm.name='';
        this.modifyForm.account='';
        this.modifyForm.jobName='';
        this.modifyForm.phone='';
        this.modifyForm.userRole='';
        this.modifyForm.email='';
        this.modifyForm.jobRole='';
        this.modifyForm.level='';
        this.modifyForm.checkSort='';
        this.modifyForm.jobNumber='';
        this.num = 1;
        this.checkUserIdList = [];
      },
      //部门ID
      setUserId(userId){
          this.modifyForm.userId=userId;
      },
      //修改用户
      handleModifyUserClick () {
        if (Helper.trim(this.modifyForm.name)==''){
            this.warnMsg("请填写用户名称");
            return;
        }
        if (Helper.trim(this.modifyForm.account)==''){
              this.warnMsg("请填写用户账号");
              return;
        }
        if (Helper.trim(this.modifyForm.jobName)==''){
              this.warnMsg("请填写用户职位");
              return;
        }
        if (Helper.trim(this.modifyForm.phone)==''){
              this.warnMsg("请填写用户手机号");
              return;
        }
        if (Helper.trim(this.modifyForm.userRole)==''){
              this.warnMsg("请选择用户权限");
            return;
        }
        if (Helper.trim(this.modifyForm.status)==''){
            this.warnMsg("请选择用户状态");
            return;
        }
        if (Helper.trim(this.modifyForm.jobRole)==''){
            this.warnMsg("请选择用户角色");
            return;
        }
          if (Helper.trim(this.modifyForm.level)==''){
              this.warnMsg("请选择用户级别");
              return;
          }
          if (Helper.trim(this.modifyForm.checkSort)==''){
              this.warnMsg("请选择用户考勤序号");
              return;
          }
          if (Helper.trim(this.modifyForm.jobNumber)==''){
              this.warnMsg("请输入用户工号");
              return;
          }
          var checkUsers = this.modifyForm.checkUserList;
          var checkUserIds = [];
          checkUsers.forEach(checkUser =>{
              if (checkUser.id != null && checkUser.id != ''){
                  checkUserIds.push(checkUser.id)
              }
          })
          if (checkUserIds == null || checkUserIds == [] || checkUserIds.length == 0 || checkUserIds.length != checkUsers.length){
              this.warnMsg("请选择审核人");
              return;
          }
          var nary = checkUserIds.sort();
          for (var i = 0; i < checkUserIds.length; i++) {
              if (nary[i] == nary[i + 1]) {
                  this.warnMsg("多级审核人重复,请检查");
                  return;
              }
          }

        Http.zsyPutHttp(`/user/${this.modifyForm.userId}`,this.modifyForm,(res)=>{
            this.hide();
            this.$message({
                showClose: true,
                message: '用户修改成功',
                type: 'success'
            });
            this.$emit("handleUserDataRefresh");
        });
      },
      warnMsg(msg) {
          this.$message({
              showClose: true,
              message: msg,
              type: 'warning'
          });
      },
        addCheckUser(i){
          var checkUsers = this.modifyForm.checkUserList;
          var flag = true;
          checkUsers.forEach(user=>{
              if(user.level === i){
                  user.id = this.checkUserIdList[i-1];
                  flag = false;
              }
          })
          this.modifyForm.checkUserList = checkUsers;
          if (flag){
              let checkUser={
                  'level':i,
                  'id':this.checkUserIdList[i-1]
              }
              this.modifyForm.checkUserList.push(checkUser);
          }
        },
        plus(){
            this.num = this.num+1;
        },
        minus(i){
            this.num = this.num-1;
            this.checkUserIdList.splice(i);
            this.modifyForm.checkUserList.splice(i);
        },
        fetchUserList() {
            let vm = this
            Http.zsyGetHttp('/user/effective', {}, (resp) => {
                vm.userList = resp.data
            })
        },
    }
  }
</script>
<style scoped>
.add-member-pop{position: fixed;top: 0;left: 0;bottom: 0;right: 0;z-index: 100;background: rgba(0,0,0,0.5);}
.add-member-pop-con{position: absolute;background: #fff;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 536px;padding: 16px;padding-bottom: 30px;}
.ctpc-top{font-size: 16px;line-height: 30px;font-weight: 700;color: #333;}
.ctpc-top-close{font-size: 30px;width: 30px;height: 30px;line-height: 27px;cursor: pointer;transition:0.8s ease all;text-align: center;}
.ctpc-top-close:hover{color:#36A8FF;transform:rotate(360deg);}
.ftp-list{margin: 20px 0;font-size: 14px;}
.ftp-menus{width: 130px;text-align: right;margin-right: 16px;line-height: 30px;}
.ftp-msg{line-height: 30px;}
.ftp-msg > input{height: 28px;width: 280px;border: 1px solid #ccc;border-radius: 3px;text-indent: 4px;}
.am-warn{height: 20px;line-height: 20px;text-indent: 84px;color: red;}



.ctpc-btns{text-align: right;}
.ctpc-btns > input{display: inline-block;width: 80px;height: 28px;margin-left: 12px;cursor: pointer;border-radius: 4px;}
.ctpc-cancel{background: #fff;border: 1px solid #ccc;}
.ctpc-cancel:hover{background: #fff;border: 1px solid #36A8FF;color: #36A8FF;font-weight: 700;}
.ctpc-save{background: #36A8FF;color: #fff;}



</style>

<template>
	<div class="add-member-pop" v-show="showAddPop">
    <div class="add-member-pop-con">
      <div class="ctpc-top clearfix">
        <span class="fl">添加成员</span><span class="ctpc-top-close fr" @click="hide">×</span>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">姓名</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="addForm.name" placeholder="请输入姓名"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">账号</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="addForm.account" placeholder="请输入账号"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">职位</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="addForm.jobName" placeholder="请输入职位"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">角色</div>
        <div class="ftp-msg fl">
          <el-select class="w280" v-model="addForm.jobRole" placeholder="请选择角色">
            <el-option
                    v-for="item in rolesList"
                    :key="item.roleId"
                    :label="item.roleName"
                    :value="item.roleId">
            </el-option>
          </el-select>
        </div>
      </div>
      <!--<div class="ftp-list clearfix">-->
        <!--<div class="ftp-menus fl">团队</div>-->
        <!--<div class="ftp-msg fl">-->
          <!--<el-select class="w280" v-model="addForm.groupId" placeholder="请选择团队">-->
            <!--<el-option-->
                    <!--v-for="item in groupList"-->
                    <!--:key="item.id"-->
                    <!--:label="item.name"-->
                    <!--:value="item.id">-->
            <!--</el-option>-->
          <!--</el-select>-->
        <!--</div>-->
      <!--</div>-->
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">级别</div>
        <div class="ftp-msg fl">
          <el-select class="w280" v-model="addForm.level" placeholder="请选择级别">
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
        <div class="ftp-menus fl">手机号</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="addForm.phone" placeholder="请输入手机号"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">邮箱地址</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="addForm.email" placeholder="请输入邮箱地址"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">用户权限</div>
        <div class="ftp-msg fl">
          <el-select class="w280" v-model="addForm.userRole" placeholder="请选择权限">
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
          <el-input class="w280" v-model="addForm.checkSort" placeholder="请输入考勤序号"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">工号</div>
        <div class="ftp-msg fl">
          <el-input class="w280" v-model="addForm.jobNumber" placeholder="请输入员工工号"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">审核人</div>
        <div class="ftp-msg fl">
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
          <i style="margin-left: 325px" class="el-icon-plus" v-show="num>=1 && num < 3" @click="plus"></i>
          <i class="el-icon-minus" v-show="num>1"@click="minus(num-1)"></i>
        </div>
      </div>

      <!--<div class="am-warn">{{amWarn}}</div>-->
      <div class="ctpc-btns">
        <input type="button" class="ctpc-cancel" value="取消" @click="hide">
        <input type="button" class="ctpc-save" value="保存" @click="handleAddUserClick">
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
        addForm:{
          name:'',
          account:'',
          jobName:'',
          phone:'',
          userRole:'',
          groupId:null,
          jobRole:'',
          level:'',
          departmentId:'',
          checkSort:'',
            jobNumber:'',
          email:'',
            checkUserList:[]
        },
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
        },
            {
                value: 3,
                label: '人事行政'
            }
        ],
          rolesList:[
              {
                  roleId: 3,
                  roleName: '产品经理'
              },
              {
                  roleId: 0,
                  roleName: '测试'
              },
              {
                  roleId: 2,
                  roleName: 'UI设计'
              },
              {
                  roleId: 1,
                  roleName: 'JAVA开发'
              },
              {
                  roleId: 4,
                  roleName: 'C++开发'
              },
              {
                  roleId: 5,
                  roleName: 'PHP开发'
              },
              {
                  roleId: 6,
                  roleName: '前端开发'
              },
              {
                  roleId: 7,
                  roleName: 'IOS开发'
              },
              {
                  roleId: 8,
                  roleName: 'Android开发'
              },
              {
                  roleId: 9,
                  roleName: '人工智能'
              },
              {
                  roleId: 10,
                  roleName: '其他'
              }
          ],

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
        showAddPop: false,
          checkUserIdList:[],
          num:1,
          userList:[],
          groupList:[]

      };
    },
      created(){
        this.fetchUserList();
        // this.fetchGroupList()
      },
    methods: {
      //显示弹框
      show () {
        this.showAddPop = true;
      },
      //隐藏弹框
      hide () {
        this.showAddPop = false;
        this.addForm.name='';
        this.addForm.account='';
        this.addForm.jobName='';
        this.addForm.phone='';
        this.addForm.userRole='';
        this.addForm.email='';
        this.addForm.jobRole='';
        this.addForm.level='';
        this.addForm.checkSort='';
        this.addForm.jobNumber='';
        this.addForm.groupId=null;
        this.num = 1;
        this.checkUserIdList = [];
      },
      //部门ID
      setDeptId(deptId){
          this.addForm.departmentId=deptId;
      },
      //添加用户
      handleAddUserClick () {
        if (Helper.trim(this.addForm.name)==''){
            this.warnMsg("请填写用户名称");
            return;
        }
        if (Helper.trim(this.addForm.account)==''){
              this.warnMsg("请填写用户账号");
              return;
        }
        if (Helper.trim(this.addForm.jobName)==''){
              this.warnMsg("请填写用户职位");
              return;
        }
        if (Helper.trim(this.addForm.phone)==''){
              this.warnMsg("请填写用户手机号");
              return;
        }
        if (Helper.trim(this.addForm.userRole)==''){
              this.warnMsg("请选择用户权限");
            return;
        }
        if (Helper.trim(this.addForm.jobRole)==''){
            this.warnMsg("请选择用户角色");
            return;
        }
        if (Helper.trim(this.addForm.level)==''){
            this.warnMsg("请选择用户级别");
            return;
        }
          if (Helper.trim(this.addForm.checkSort)==''){
              this.warnMsg("请选择用户考勤序号");
              return;
          }
          if (Helper.trim(this.addForm.jobNumber)==''){
              this.warnMsg("请输入用户工号");
              return;
          }
          var checkUsers = this.addForm.checkUserList;
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
        Http.zsyPostHttp('/user/add',this.addForm,(res)=>{
            this.hide();
            this.$message({
                showClose: true,
                message: '用户添加成功',
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
        fetchUserList() {
            let vm = this
            Http.zsyGetHttp('/user/effective', {}, (resp) => {
                vm.userList = resp.data
            })
        },
        addCheckUser(i){
            var checkUsers = this.addForm.checkUserList;
            var flag = true;
            checkUsers.forEach(user=>{
                if(user.level === i){
                    user.id = this.checkUserIdList[i-1];
                    flag = false;
                }
            })
            this.addForm.checkUserList = checkUsers;
            if (flag){
                let checkUser={
                    'level':i,
                    'id':this.checkUserIdList[i-1]
                }
                this.addForm.checkUserList.push(checkUser);
            }
        },
        plus(){
            this.num = this.num+1;
        },
        minus(i){
            this.num = this.num-1;
            this.checkUserIdList.splice(i);
            this.addForm.checkUserList.splice(i);
        },
        //查询所有可用团队
        fetchGroupList(){
            Http.zsyGetHttp('/work-group/list',{},res=>{
                this.groupList = res.data;
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

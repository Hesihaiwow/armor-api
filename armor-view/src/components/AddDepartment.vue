<template>
	<div class="add-member-pop" v-show="showAddPop">
    <div class="add-member-pop-con">
      <div class="ctpc-top clearfix">
        <span class="fl">添加部门</span><span class="ctpc-top-close fr" @click="hide">×</span>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">部门名称</div>
        <div class="ftp-msg fl">
          <el-input v-model="addForm.name" placeholder="请输入部门名称"></el-input>
        </div>
      </div>
      <div class="ftp-list clearfix">
        <div class="ftp-menus fl">上级部门</div>
        <div class="ftp-msg fl">
          <el-select v-model="addForm.parentId" placeholder="请选择部门">
            <el-option
                    v-for="item in departmentLevel"
                    :key="item.id"
                    :label="item.label"
                    :value="item.id">
            </el-option>
          </el-select>
        </div>
      </div>
      <div class="am-warn">{{amWarn}}</div>
      <div class="ctpc-btns">
        <input type="button" class="ctpc-cancel" value="取消" @click="hide">
        <input type="button" class="ctpc-save" value="保存" @click="handleAddDeptBtnClick">
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
        //部门层级结构数据
        departmentLevel:[],
        addForm:{
            name:'',
            parentId:''
        },
        showAddPop: false,
        amWarn: ''
      };
    },
    beforeMount(){
        this.initDeptLevelData();
    },
    methods: {
        //初始化部门层级结构数据
      initDeptLevelData(){
          let _this = this;
          Http.zsyGetHttp('/dept/level',null,(res)=>{
              _this.departmentLevel=res.data;
          });
      },
      //提示框显示
      show () {
        this.showAddPop = true;
      },
      //提示框隐藏
      hide () {
        this.showAddPop = false;
        this.addForm.name='';
        this.addForm.parentId='';
        this.amWarn = '';
      },
      //添加部门
      handleAddDeptBtnClick () {
        if (Helper.trim(this.addForm.name)=='') {
          this.amWarn = '部门名称不能为空';
          return;
        }
        if (Helper.trim(this.addForm.parentId)=='') {
            this.amWarn = '请选择上级部门';
            return;
        }
        Http.zsyPostHttp('/dept/add',this.addForm,(res)=>{
            this.hide();
            this.initDeptLevelData();
            this.$emit("handleDeptDataRefresh");
            this.$message.success("添加部门成功");
        })
      }
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
<template>
	<div class="create-task-pop" v-show="showCreateTask">
    <div class="create-task-pop-con">
      <div class="ctpc-top clearfix">
        <span class="fl">建任务</span><span class="ctpc-top-close fr" @click="hide">×</span>
      </div>
      <div class="ctpc-con">
        <textarea class="ctpc-instruction" name="">业务后台和CRM合并：期望订单管理和学管管理在一个平台完成</textarea>
  		  <div class="ctpc-main-con">
            <div class="ctpc-list clearfix">
              <div class="ctpc-list-menu fl">项目</div>
              <div class="ctpc-list-con fl">
                <el-select v-model="projectValue" placeholder="请选择">
                  <el-option v-for="item in projectItem" :key="item.value" :label="item.label" :value="item.value"></el-option>
                </el-select>
              </div>
            </div>
            <div class="ctpc-list clearfix">
              <div class="ctpc-list-menu fl">截止日期</div>
              <div class="ctpc-list-con fl">
                <el-date-picker v-model="value1" type="date" placeholder="选择日期" :picker-options="pickerOptions0"></el-date-picker>
              </div>
            </div>
            <div class="ctpc-list clearfix">
              <div class="ctpc-list-menu fl">标签</div>
              <div class="ctpc-list-con fl">
                <ul class="ctpc-tags">
                  <li class="ctpc-tag-lis" v-for="(list,index) in tagList">
                    <span class="tag-lis-circle"></span>
                    <span class="tag-lis-msg">{{list.tagName}}</span>
                    <span class="tag-lis-delete" @click="deleteTag(index)">×</span>
                  </li>
                  <li class="tag-lis-add">
                    <span class="tag-lis-add-msg" @click="addTag">添加标签</span>
                    <div class="tag-add-sel" v-show="showAddTag">
                      <div class="add-tag-btn" @click="addSelTag">添加</div>
                      <el-select v-model="value10" multiple filterable allow-create placeholder="添加标签">
                        <el-option v-for="item in options5" :key="item.value" :label="item.label" :value="item.value"></el-option>
                      </el-select>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
            <div class="ctpc-member-con">
              <div class="ctpc-member-list clearfix" v-for="(list,index) in memberList">
                <span class="fl ctpc-member-head">{{list.name}}</span>
                <span class="fl ctpc-member-job ellipsis">{{list.job}}</span>
                <span class="fl ctpc-member-job-time">工作量：{{list.jobTime}}工时</span>
                <span class="fl ctpc-member-end-time">截止：{{list.endTime}}</span>
                <span class="fl ctpc-member-assess">评价：{{list.jobLevel}}</span>
                <span class="fl ctpc-member-delete" @click="deleteMember(index)">×</span>
              </div>
              <div class="bdl-line"></div>
            </div>
            <div class="ctpc-add-member-detail" v-if="showAddDetail">
              <textarea class="add-member-remark" placeholder="备注"></textarea>
              <div class="add-member-basic">
                <div class="add-member-basic-list add-member-stage clearfix">
                  <div class="add-member-basic-menu fl">阶段：</div>
                  <div class="add-member-basic-msg add-stage-opt fl" @click="addStage">添加阶段</div>
                  <el-select v-model="valueStage" multiple filterable allow-create placeholder="添加标签">
                    <el-option v-for="item in optionsStage" :key="item.value" :label="item.label" :value="item.value"></el-option>
                  </el-select>
                </div>
                <div class="add-member-basic-list clearfix">
                  <div class="add-member-basic-menu fl">负责人：</div>
                  <div class="add-member-basic-msg fl">
                    <el-select v-model="value" placeholder="请选择">
                      <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value"></el-option>
                    </el-select>
                  </div>
                  <div class="add-member-basic-menu add-member-basic-time fl">工作量：</div>
                  <div class="add-member-basic-msg fl">
                    <input type="text" class="member-time-count">工时
                  </div>
                </div>
                <div class="add-member-basic-list clearfix">
                  <div class="add-member-basic-menu fl">开始日期：</div>
                  <div class="add-member-basic-msg fl">
                    <el-date-picker v-model="valueStart" type="date" placeholder="选择日期" :picker-options="pickerOptions0"></el-date-picker>
                  </div>
                  <div class="add-member-basic-menu add-member-basic-end fl">截止日期：</div>
                  <div class="add-member-basic-msg fl">
                    <el-date-picker v-model="valueEnd" type="date" placeholder="选择日期" :picker-options="pickerOptions0"></el-date-picker>
                  </div>
                 
                </div>



              </div>
            </div>
            <div class="add-member-opt" v-show="showAddBtn" @click="addMember">
              <span class="add-member-icon">+</span>
              <span class="add-member-msg">添加成员</span>
            </div>
        </div>
      </div>
      <div class="ctpc-btns">
        <input type="button" class="ctpc-cancel" value="取消">
        <input type="button" class="ctpc-save" value="保存">
      </div>
    </div>
	</div>
</template>
<script>
  export default {
    data() {
      return {
        showCreateTask: false,
        projectItem: [
          {
            value: '选项1',
            label: '知心慧学'
          }, 
          {
            value: '选项2',
            label: '会学本'
          }
        ],
        projectValue: '',
        pickerOptions0: {
          disabledDate(time) {
            return time.getTime() < Date.now() - 8.64e7;
          }
        },
        value1: '',
        memberList: [
          {
            job: '产品设计',
            jobTime: 22,
            endTime: '8月20日',
            jobLevel: 'C',
            name: '小花'
          },
          {
            job: 'UI设计',
            jobTime: 11,
            endTime: '8月22日',
            jobLevel: 'B',
            name: '晓晓'
          },
          {
            job: '前端开发',
            jobTime: 22,
            endTime: '8月20日',
            jobLevel: 'A',
            name: '小希'
          },
          {
            job: 'Java开发',
            jobTime: 22,
            endTime: '8月20日',
            jobLevel: 'A',
            name: '大小花'
          }
        ],
        tagList: [
          {
            tagName: '教师端1'
          },
          {
            tagName: 'pad端2'
          },
          {
            tagName: '教师端3'
          },
          {
            tagName: 'pad端4'
          },
          {
            tagName: '教师端5'
          },
          {
            tagName: 'pad端6'
          },
          {
            tagName: '教师端7'
          },
          {
            tagName: 'pad端8'
          }
        ],
        options: [
          {
            value: '选项1',
            label: '黄金糕'
          }, 
          {
            value: '选项2',
            label: '双皮奶'
          }
        ],
        value: '',
        pickerOptions0: {
          disabledDate(time) {
            return time.getTime() < Date.now() - 8.64e7;
          }
        },
        valueStart: '',
        valueEnd: '',
        options5: [{
          value: 'HTML',
          label: 'HTML'
        }, {
          value: 'CSS',
          label: 'CSS'
        }, {
          value: 'JavaScript',
          label: 'JavaScript'
        }],
        value10: [],
        showAddTag: false,
        showAddDetail: false,
        showAddBtn: true,
        valueStage: [],
        optionsStage: [{
          value: '产品设计',
          label: '产品设计'
        }, {
          value: 'UI设计',
          label: 'UI设计'
        }, {
          value: '前端开发',
          label: '前端开发'
        }],
      };
    },
    methods: {
      deleteTag (index) {
        this.tagList.splice(index,1);
      },
      addTag () {
        this.showAddTag = true;
      },
      addSelTag () {
        this.showAddTag = false;
        for (var i = 0; i < this.value10.length; i++){
          // debugger;
          var tagLis = {};
          tagLis.tagName = this.value10[i];
          this.tagList.push(tagLis);
          console.log(this.tagList);
          // tagLis.tagName = '';
        }
      },
      addMember () {
        this.showAddDetail = true;
        this.showAddBtn = false;
      },
      show () {
        this.showCreateTask = true;
      },
      hide () {
        this.showCreateTask = false;
      },
      deleteMember (index) {
        this.memberList.splice(index,1);
      },
      addStage () {
        
      }
    }
  }
</script>
<style scoped>
.create-task-pop{/* display: none; */position: fixed;top: 0;left: 0;bottom: 0;right: 0;z-index: 100;background: rgba(0,0,0,0.5);}
.create-task-pop-con{position: absolute;background: #fff;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 546px;padding: 16px 26px 44px;height: 566px;box-shadow: 0 0 10px #fff;}
.ctpc-top{font-size: 16px;line-height: 30px;font-weight: 700;color: #333;margin-bottom: 10px;}
.ctpc-top-close{font-size: 30px;width: 30px;height: 30px;line-height: 22px;cursor: pointer;transition:0.8s ease all;text-align: center;}
.ctpc-top-close:hover{color:#36A8FF;transform:rotate(360deg);}
.ctpc-instruction{background: #E4E4E4;border-radius: 3px;padding: 6px 10px;margin: 6px 0;line-height: 22px;font-size: 14px;color: #000;width: 496px;}
.ctpc-main-con{border-top: 1px solid #ccc;padding: 10px 0;margin-top: 10px;}
.ctpc-list-con .el-select{width: 188px;}
.ctpc-list-menu{line-height: 30px;width: 80px;font-size: 14px;}
.ctpc-list-con{width: 410px;}
.ctpc-list{background: #fff;padding: 6px 10px;border: 1px solid #ccc;margin-bottom: 10px;}
.ctpc-tags{position: relative;}
.ctpc-tags > li{display: inline-block;line-height: 30px;}
.ctpc-tag-lis{background: #F2F2F2;padding: 0 16px;border-radius: 15px;margin-right: 14px;margin-bottom: 14px;box-shadow: 0 0 10px #ccc;}
.ctpc-tag-lis > span{display: inline-block;vertical-align: middle;font-size: 14px;}
.ctpc-tag-lis .tag-lis-delete:hover{text-shadow: 0 0 8px #ccc;font-size: 18px !important;}
.tag-lis-circle{width: 8px;height: 8px;border-radius: 50%;background: #FF9D6E;margin-right: 4px;}
.tag-lis-delete{font-size: 18px !important;padding: 0 2px;cursor: pointer;}
.tag-lis-add{position: relative;margin-left: 10px;cursor: pointer;}
.tag-lis-add-msg{color: #36A8FF;}
.ctpc-member-con{padding-left: 10px;/* border-left: 1px solid #ccc; */margin-left: 6px;position: relative;}
.ctpc-member-list{height: 42px;background: #fff;border: 1px solid #ccc;line-height: 42px;color: #000;padding: 0 4px;position: relative;margin-bottom: 10px;}
.ctpc-member-list:before{
  content: '';
  position: absolute;
  left: -17px;
  top: 12px;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #008000;
  z-index: 110;
}
.ctpc-member-list > span{display: inline-block;vertical-align: middle;}
.ctpc-member-job{width: 136px;}
.ctpc-member-job-time{width: 110px;}
.ctpc-member-end-time{width: 110px;}
.ctpc-member-assess{width: 70px;}
.ctpc-member-head{width: 36px;height: 36px;border-radius: 50%;background: #006699;color: #fff;font-size: 10px;text-align: center;line-height: 36px;margin-top: 3px;overflow: hidden;margin-right: 10px;}
.bdl-line{position: absolute;left: 0;bottom: 14px;top: 14px;border-left: 1px solid #ccc;}
.add-member-opt{cursor: pointer;margin: 20px 10px;width: 80px;}
.add-member-opt > span{display: inline-block;vertical-align: middle;}
.add-member-icon{background: #36A8FF;color: #fff;width: 20px;height: 20px;text-align: center;font-size: 20px;line-height: 16px;font-weight: bold;border-radius: 50%;}
.add-member-msg{color: #36A8FF;margin-left: 6px;}


.ctpc-btns{text-align: right;margin-top: 20px;}
.ctpc-btns > input{display: inline-block;width: 80px;height: 28px;margin-left: 12px;cursor: pointer;border-radius: 4px;}
.ctpc-cancel{background: #fff;border: 1px solid #ccc;}
.ctpc-cancel:hover{background: #fff;border: 1px solid #36A8FF;color: #36A8FF;font-weight: 700;}
.ctpc-save{background: #36A8FF;color: #fff;}

.ctpc-con{height: 500px;overflow-y: auto;}

.add-member-remark{border: 1px solid #ccc;display: block;padding: 6px;width: 499px;}
.add-member-basic{background: #fff;border: 1px solid #ccc;margin-top: 10px;padding: 10px;font-size: 14px;}
.add-member-basic-menu{width: 78px;text-align: right;}
.add-member-basic-list{border-bottom: 1px solid #ccc;padding: 5px 0;line-height: 34px;}
.add-member-basic-list:last-child{border: none;}
.add-stage-opt{color: #36A8FF;cursor: pointer;}
.add-member-basic-msg .el-select{width: 140px;}
.member-time-count{width: 40px;border: 1px solid #ccc;height: 26px;border-radius: 4px;margin-right: 4px;text-indent: 4px;box-shadow: 0 0 10px #ccc;}
.add-member-basic-time{margin-left: 40px;}

.add-member-basic-msg .el-date-editor.el-input{width: 140px;}
.add-member-basic-end{margin-left: 40px;}

.tag-add-sel .el-select{width: 130px;}

.tag-add-sel{position: absolute;left: -10px;top: 10px;z-index: 100;}
.add-tag-btn{background: #36A8FF;color: #fff;position: absolute;right: 0;top: 50%;width: 40px;height: 30px;z-index: 200;text-align: center;transform: translateY(-50%);border-radius: 4px;}

.ctpc-member-delete{font-size: 26px;cursor: pointer;}
.add-member-stage{position: relative;}

</style>
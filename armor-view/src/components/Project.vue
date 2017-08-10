<template>
  <div class="project-con">
    <p class="mic-title">我参与的项目</p>
    <div class="task-item" v-for="list in TaskItem">
      <img src="../assets/img/u431.png" alt="" class="task-logo">
      <div class="task-info">
        <div class="task-name">{{list.taskName}}</div>
        <div class="task-sub-name">{{list.subTaskName}}</div>
      </div>
    </div>
    <div class="add-task-item" @click="addTask">
      <div class="add-task-btn">＋</div>
      <div class="add-task-msg">创建新项目</div>
    </div>
    <div class="add-task-pop" v-show="showAddTask">
      <div class="add-task-pop-con">
        <div class="add-task-top">
          创建项目<span class="close" @click="hidePop">×</span>
        </div>
        <img src="../assets/img/u1284.png" alt="" class="att-img">
        <p class="att-msg">为不同的事物建立各自的项目</p>
        <input type="text" class="project-name" placeholder="项目名称" v-model="projrctName">
        <textarea class="project-intro" placeholder="项目简介（选填）" v-model="projectIntro"></textarea>
        <div class="att-bents">
          <span class="cancel" @click="hidePop">取消</span>
          <span class="save" @click="saveAdd">保存</span>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
  export default {
    name: 'Project',
    data() {
      return {
        TaskItem: [
          {
            taskName: '知心慧学',
            subTaskName: '知心慧学'
          },
          {
            taskName: '慧学本',
            subTaskName: '知心慧学'
          }
        ],
        showAddTask: false,
        projrctName: '',
        projectIntro: ''
      };
    },
    methods: {
      addTask () {
        this.showAddTask = true;
      },
      hidePop () {
        this.showAddTask = false;
      },
      saveAdd () {
        console.log(this.projrctName+'~~~'+this.projectIntro);
        if (this.projrctName !== ''){
          let addPro = {};
          addPro.taskName = this.projrctName;
          addPro.subTaskName = this.projectIntro;

          this.TaskItem.push(addPro);
          this.showAddTask = false;
          this.projrctName = this.projectIntro = '';
        }

      }
    }
  }
</script>
<style scoped>
.project-con{width: 1100px;margin: auto;}
.mic-title{font-size: 18px;color: #000;position: relative;}
.task-item,.add-task-item{background: #fff;display: -webkit-flex;display: -moz-flex;display: -ms-flex;display: -o-flex;display: flex;padding: 14px 16px;margin: 16px 0;cursor: pointer;}
.task-item:hover,.add-task-item:hover{box-shadow: 0 0 20px #ccc;}
.task-info{flex: 1;-webkit-flex:1;-moz-flex:1;-ms-flex:1;-o-flex:1;}
.task-name{font-size: 18px;line-height: 26px;}
.task-sub-name{font-size: 12px;color: #999999;line-height: 20px;}
.task-logo{margin-right: 20px;height: 30px;margin-top: 6px;}
.add-task-btn{width: 40px;height: 40px;background: #F2F2F2;color: #CCCCCC;line-height: 40px;text-align: center;font-size: 36px;}
.add-task-msg{line-height: 40px;margin-left: 26px;font-size: 14px;color: #999999;}
.add-task-pop{position: fixed;top: 0;left: 0;bottom: 0;right: 0;background: rgba(0,0,0,0.5);z-index: 20;}
.add-task-pop-con{position: absolute;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 350px;height: 400px;background: #fff;}
.add-task-top{text-align: center;font-size: 16px;font-weight: bold;margin: 10px;border-bottom: 1px solid #ccc;line-height: 30px;padding-bottom: 10px;position: relative;}
.add-task-top .close{position: absolute;right: 0;font-size: 28px;font-weight: normal;color: #999;cursor: pointer;}
.att-img{margin: 16px 0;}
.att-msg{text-align: center;color: #A7A7A7;margin-bottom: 24px;}
.project-name,.project-intro{width: 90%;border: 1px solid #ccc;height: 36px;line-height: 36px;display: block;margin: 16px auto;border-radius: 3px;text-indent: 10px;font-size: inherit;}
.att-bents{text-align: right;margin: 20px;}
.att-bents > span{display: inline-block;width: 60px;height: 26px;text-align: center;line-height: 26px;font-size: 14px;cursor: pointer;}
.cancel{margin-right: 10px;border: 1px solid #D0D3D3;}
.save{background: #36A8FF;color: #fff;}





</style>
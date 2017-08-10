<template>
	<div class="intergral-con">
		<div class="intergral-con-top clearfix">
      <div class="fl menu-list" v-for="(list,index) in menuList" @click="togTable(index)" :style="tabActive(index)">{{list.name}}</div>
      
      <div class="fl menu-list" @click="togTable(4)" :style="tabActive(4)"><span class="self-define">自定义</span>
        <el-date-picker v-model="value1" type="date" placeholder="选择日期" :picker-options="pickerOptions0"></el-date-picker><span class="div-line">-</span>
        <el-date-picker v-model="value2" type="date" placeholder="选择日期" :picker-options="pickerOptions0"></el-date-picker>
        <img src="../assets/img/u1221.png" alt="" class="serch-btn">
      </div>
    </div>
    <div class="intergral-data-detail">
      <el-table :data="tableData" stripe style="width: 100%">
        <el-table-column prop="member" label="成员" align="center"></el-table-column>
        <el-table-column prop="intergral" label="积分" align="center"></el-table-column>
      </el-table>
    </div>
	</div>
</template>
<script>
  export default {
    name: 'Intergral',
    data() {
      return {
        menuList: [
          {
            name: '本月'
          },
          {
            name: '本季度'
          },
          {
            name: '本年'
          }
        ],
        activeIdx: 0,
        pickerOptions0: {
          disabledDate(time) {
            return time.getTime() < Date.now() - 8.64e7;
          }
        },
        value1: '',
        value2: '',
        tableData: [{
          member: '小花',
          intergral: '122'
        }, {
          member: '大花',
          intergral: '22'
        }, {
          member: '小希',
          intergral: '111'
        }]
      };
    },
    methods: {
      togTable(index) {
        this.activeIdx = index;
      },
      tabActive (index) {
        if (index == this.activeIdx) {
          return {
            color: '#36A8FF'
          }
        }
      }




    }
  }
</script>
<style scoped>
.intergral-con{width: 1100px;margin: auto;}
.intergral-con-top{font-size: 14px;}
.menu-list{line-height: 36px;padding: 0 4px;margin-right: 10px;cursor: pointer;}
.div-line{margin: 0 10px 0 12px;color: #000;}
.self-define{margin-right: 14px;}
.serch-btn{vertical-align: middle;margin-left: 10px;cursor: pointer;}
.intergral-data-detail{margin: 20px 0;}
</style>
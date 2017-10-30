<template>
    <div  class="calculate-con">
      <div class="calculate-con-top clearfix">
        <div class="clearfix select-box">
          <div class="calculate-top-list fl">
            <el-input  v-model="queryForm.datumIntegral"><template slot="prepend">基准积分:</template></el-input>
          </div>
          <div class="calculate-top-list fl">
            <el-input v-model="queryForm.bonus"><template slot="prepend">绩效奖金池:</template></el-input>
          </div>
          <div class="calculate-top-list fl">
            <el-date-picker v-model="queryForm.startTime" type="date" placeholder="选择开始日期"></el-date-picker>
          </div>
          <div class="calculate-top-list fl">
            <el-date-picker v-model="queryForm.endTime" type="date" placeholder="选择结束日期"></el-date-picker>
          </div>
          <div class="calculate-top-list fl" style="width: 50px">
            <img src="../assets/img/u1221.png" alt="" @click="calculateIntegral()" class="search-btn">
          </div>
        </div>

      </div>
      <div class="calculate-data-detail">
        <el-table :data="calculate" :summary-method="getSummaries" show-summary>
          <el-table-column prop="id" label="序号" align="center"></el-table-column>
          <el-table-column prop="name" label="成员" align="center"></el-table-column>
          <el-table-column prop="integral" label="绩效积分" align="center"></el-table-column>
          <el-table-column prop="bonus" label="奖金" align="center"></el-table-column>
        </el-table>
      </div>
    </div>
</template>
<script>
    import Http from '../lib/Http'
    import moment from 'moment';
    import Helper from '../lib/Helper'

    moment.locale('zh-cn');
    export default {
        name: 'Calculate',
        data() {
            return {
                calculate:[],
                queryForm:{
                    startTime:'',
                    endTime:'',
                    datumIntegral:'',
                    bonus:''
                },
            }
        },
        methods: {
            calculateIntegral(){
                if(this.isDecimal(this.queryForm.datumIntegral)&&this.isDecimal(this.queryForm.bonus)){
                    Http.zsyPostHttp("/stats/calculate",this.queryForm, (res) => {
                        this.calculate =res.data;
                    })
                }else{
                    this.$message({
                        showClose: true,
                        message: '基准积分与奖金为必填项，请检查并确认格式正确',
                        type: 'error'
                    });
                }

            },
            isDecimal(str) {
                var regu = /^[-]{0,1}[0-9]{1,}$/;
                if (regu.test(str)) {
                    return true;
                }
                var re = /^[-]{0,1}(\d+)[\.]+(\d+)$/;
                if (re.test(str)) {
                    if (RegExp.$1 == 0 && RegExp.$2 == 0) return false;
                    return true;
                } else {
                    return false;
                }

            },
            getSummaries(param) {
                const { columns, data } = param;
                const sums = [];
                columns.forEach((column, index) => {
                    if (index === 0) {
                        sums[index] = '总计';
                        return;
                    }
                    const values = data.map(item => Number(item[column.property]));
                    if (!values.every(value => isNaN(value))) {
                        sums[index] = values.reduce((prev, curr) => {
                            const value = Number(curr);
                            if (!isNaN(value)) {
                                return prev + curr;
                            } else {
                                return prev;
                            }
                        }, 0).toFixed(2);
                        sums[index] += ' ';
                    } else {
                        sums[index] = '';
                    }
                });

                return sums;
            }
        }
    }
</script>
<style scoped>
    .calculate-con {
        width: 1100px;
        margin: auto;
    }

    .calculate-con-top {
        font-size: 14px;
        background: #fff;
        padding: 16px 0;
        text-indent: 10px;
        box-shadow: 0 0 10px #ccc;
        margin: 0 10px;
    }

    .calculate-top-list {
      margin: 0 10px 20px 10px;
      width: 180px;
    }

    .select-box > div:nth-child(3n) {
      margin-right: 50px;
    }

    .search-btn {
        vertical-align: middle;
        margin-left: 50px;
        cursor: pointer;
        margin-top: 8px;
    }

    .calculate-data-detail {
        margin: 20px 8px;
    }
</style>

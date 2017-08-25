<template>
    <div class="intergral-con">
        <div class="intergral-con-top clearfix">
            <div class="fl menu-list" v-for="(list,index) in menuList" @click="togTable(index)"
                 :style="tabActive(index)">{{list.name}}
            </div>
            <!--<div class="fl menu-list" @click="togTable(4)" :style="tabActive(4)">自定义</div>-->
            <div class="fl menu-list" v-show="diyStyle">
                <el-date-picker v-model="queryForm.startTime" type="date" placeholder="选择日期"></el-date-picker>
                <span class="div-line">-</span>
                <el-date-picker v-model="queryForm.endTime" type="date" placeholder="选择日期"></el-date-picker>
                <img src="../assets/img/u1221.png" alt="" @click="integralDate()" class="serch-btn">
            </div>
        </div>
        <div class="intergral-data-detail">
            <el-table :data="tableData" stripe style="width: 100%">
                <el-table-column prop="id" label="排名" align="center"></el-table-column>
                <el-table-column prop="name" label="成员" align="center"></el-table-column>
                <el-table-column prop="integral" label="积分" align="center"></el-table-column>
                <el-table-column prop="userId" label="编辑" align="center">
                    <template scope="scope">
                        <el-button @click.native.prevent="clicklHistory(scope.$index, tableData)" type="text"
                                   size="small">查看记录
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <el-pagination
                @current-change="integralPage"
                :page-size="pageInfo.pageSize"
                :current-page="pageInfo.currentPage"
                :layout="pageInfo.layout"
                :total="pageInfo.totals">
        </el-pagination>
        <el-dialog
                title="修改个人积分"
                size="tiny"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="editIntegralVisible">
            <el-form :model="integralForm" :rules="rules" ref="integralForm" label-width="80px">
                <el-form-item label="积分加减" prop="integral">
                    <el-input v-model="integralForm.integral"></el-input>
                </el-form-item>
                <el-form-item label="积分备注" prop="description">
                    <el-input type="textarea" v-model="integralForm.description" :rows="3"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="saveIntegralInfo('integralForm')">立即创建</el-button>
            <el-button @click="cancelIntegral()">取 消</el-button>
          </span>
        </el-dialog>
        <el-dialog
                title="查看积分历史记录"
                size="small"
                :close-on-click-modal="false"
                :close-on-press-escape="false"
                :visible.sync="integralHistoryVisible">
            <el-button @click="editIntegral()" size="large" v-show="permit"
                       style="float: right;position: relative;bottom: 40PX; right: 100PX;">添加积分记录
            </el-button>
            <el-table :data="historyData" stripe style="width: 100%;bottom:20px">
                <el-table-column prop="name" label="成员" align="center" width="100px"></el-table-column>
                <el-table-column prop="integral" label="积分" align="center"></el-table-column>
                <el-table-column prop="origin" label="来源" align="center" width="100px">
                    <template scope="scope">
                        <el-tag
                                :type="scope.row.origin === '手动录入' ? 'primary' : 'success'"
                                close-transition>{{scope.row.origin}}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" align="center" width="200px"></el-table-column>
                <el-table-column prop="description" label="备注" align="center" width="400px"></el-table-column>
            </el-table>
            <el-pagination
                    @current-change="integralHistory"
                    :page-size="historyPage.pageSize"
                    :current-page="historyPage.currentPage"
                    :layout="historyPage.layout"
                    :total="historyPage.totals">
            </el-pagination>
        </el-dialog>
    </div>
</template>
<script>
    import Http from '../lib/Http'
    import {Message} from 'element-ui';
    import moment from 'moment';
    import Helper from '../lib/Helper'
    import ElButton from "../../node_modules/element-ui/packages/button/src/button";

    moment.locale('zh-cn');
    export default {
        components: {ElButton},
        name: 'Intergral',
        data() {
            return {
                integralHistoryVisible: false,
                editIntegralVisible: false,
                queryForm: {
                    startTime: '',
                    endTime: ''
                },
                dateForm: {
                    startTime: '0'
                },
                integralForm: {//添加积分记录
                    userId: '',
                    description: '',
                    integral: ''
                },
                pageInfo: {
                    layout: "total, pager",
                    currentPage: 1,
                    pageSize: 10,
                    totals: 0,
                    pageNum: 0
                },
                historyPage: {
                    layout: "total, pager",
                    currentPage: 1,
                    pageSize: 10,
                    totals: 0,
                    pageNum: 0
                },
                diyStyle: false,
                menuList: [
                    {
                        name: '本月'
                    },
                    {
                        name: '本季度'
                    },
                    {
                        name: '本年'
                    },
                    {
                        name: '自定义'
                    }
                ],
                activeIdx: 0,
                startValue: '',
                endValue: '',
                tableData: [],
                historyData: [],
                rules: {
                    integral: [{required: true, message: '积分不能为空', trigger: 'change', decimal: 10}],
                    description: [{message: '备注不能超过100字', trigger: 'change', min: 0, max: 100}],
                }
            }
        },
        computed: {
            permit() {
                let userRole = Helper.decodeToken().userRole;
                return userRole == 0;
            }
        },
        beforeMount: function () {
            //选中积分tab
            this.$root.eventBus.$emit("handleTabSelected", "intergral");
            this.togTable(0);
        },
        methods: {
            editIntegral() {
                this.editIntegralVisible = true;
            },
            clicklHistory(index, rows) {//点击积分历史
                this.integralHistoryVisible = true;
                this.integralForm.userId = rows[index].userId;
                this.integralHistory(1);
            },
            integralHistory(currentPage) {//查询积分历史记录
                Http.zsyGetHttp('/integral/history/' + this.integralForm.userId + '/' + currentPage, this.queryForm, (res) => {
                    let list = res.data.list;
                    for (var i = 0; i < list.length; i++) {
                        if (list[i].origin != 1) {
                            list[i].origin = "手动录入";
                            list[i].createTime = this.localeTimeString(list[i].createTime);
                        } else {
                            list[i].origin = "任务系统";
                            list[i].createTime = this.localeTimeString(list[i].createTime);
                        }
                    }
                    this.historyPage.totals = res.data.total;
                    this.historyPage.pageNum = res.data.pages;
                    this.historyPage.pageSize = res.data.pagesize;
                    this.historyData = res.data.list;
                    this.pagingLayout();
                });
            },
            saveIntegralInfo(integralForm) {
                if (!this.isDecimal(this.integralForm.integral)) {
                    Message.error("积分格式错误");
                    return false;
                }
                this.$refs[integralForm].validate((valid) => {
                    if (valid) {
                        Http.zsyPostHttp('/integral/add', this.integralForm, (res) => {
                            Message.success("积分添加成功");
                            this.editIntegralVisible = false;
                            this.integralHistory(1);
                            this.integralPage(1);
                            this.cancelIntegral();
                        });
                    } else {
                        return false;
                    }
                });
            },
            cancelIntegral() {
                this.editIntegralVisible = false
                this.integralForm.description = '';
                this.integralForm.integral = '';
            },
            togTable(index) {
                // 点击菜单
                this.activeIdx = index;

                switch (index) {
                    case 0:
                        this.getDateString("month");
                        this.integralPage();
                        this.display(index);
                        break;
                    case 1:
                        this.getDateString("quarter");
                        this.integralPage();
                        this.display(index);
                        break;
                    case 2:
                        this.getDateString("year");
                        this.integralPage();
                        this.display(index);
                        break;
                    case 3:
                        this.display(index);
                        break;
                }
            },
            tabActive(index) {
                // 颜色变化
                if (index == this.activeIdx) {
                    return {
                        color: '#36A8FF'
                    }
                }
            },
            //根据自定义时间进行查询
            integralDate() {
                this.queryForm.startTime = this.localeTimeString(this.queryForm.startTime);
                if (this.queryForm.endTime != null && this.queryForm.endTime != "") {
                    this.queryForm.endTime = this.localeTimeString(new Date(this.queryForm.endTime).getTime() + 86399000);//结束时间加入23:59:59
                }
                this.integralPage()
            },
            //查询积分列表
            integralPage(currentPage) {
                if (currentPage == null || currentPage.length == 0) {
                    currentPage = 1;
                }
                Http.zsyGetHttp(Http.API_URI.INTEGRAL + currentPage, this.queryForm, (res) => {
                    this.tableData = res.data.list;
                    this.pageInfo.totals = res.data.total;
                    this.pageInfo.pageNum = res.data.pages;
                    this.pageInfo.pageSize = res.data.pagesize;
                    this.pagingLayout();
                    if (this.queryForm.endTime != null && this.queryForm.endTime != "") {
                        this.queryForm.endTime = this.localeTimeString(new Date(this.queryForm.endTime).getTime() - 86399000);//之前加入的结束时间减回
                    }
                })
            },
            display(index) {
                if (index != 3) {
                    this.diyStyle = false;
                } else {
                    if (this.diyStyle == false) {
                        this.diyStyle = true;
                    } else {
                        this.diyStyle = false;
                    }
                }
            },
            pagingLayout() {
                if (this.pageInfo.pageNum > 1) {
                    this.pageInfo.layout = 'total,prev,pager,next';
                } else {
                    this.pageInfo.layout = 'total,pager';
                }
                if (this.historyPage.pageNum > 1) {
                    this.historyPage.layout = 'total,prev,pager,next';
                } else {
                    this.historyPage.layout = 'total,pager';
                }
            },
            //获取本年,季度,月的开始结束日期
            getDateString(date) {
                let now = new Date();
                let curMonth = now.getMonth();
                let curYear = now.getFullYear();
                ;
                let startMonth = 0;
                if (date == "month") {//本月的开始结束时间
                    this.queryForm.startTime = this.localeTimeString(new Date(curYear, curMonth, 1));
                    this.queryForm.endTime = this.localeTimeString(new Date(curYear, curMonth + 1, 1));//下月第一天0点
                } else if (date == "quarter") {//本季度的开始结束时间
                    if (curMonth >= 1 && curMonth <= 3) {
                        startMonth = 0;
                    } else if (curMonth >= 4 && curMonth <= 6) {
                        startMonth = 3;
                    } else if (curMonth >= 7 && curMonth <= 9) {
                        startMonth = 6;
                    } else if (curMonth >= 10 && curMonth <= 12) {
                        startMonth = 9;
                    }
                    this.queryForm.startTime = this.localeTimeString(new Date(curYear, startMonth, 1));
                    this.queryForm.endTime = this.localeTimeString(new Date(curYear, startMonth + 3, 1));//下季度第一天0点
                } else if (date == "year") {//本年的开始结束时间
                    this.queryForm.startTime = this.localeTimeString(new Date(now.getFullYear(), 0, 1));
                    this.queryForm.endTime = this.localeTimeString(new Date(now.getFullYear() + 1, 0, 1));
                }
            },
            //时间字符串格式化
            localeTimeString(time) {
                if (time != null && time != "") {
                    time = moment(time).format('YYYY-MM-DD HH:mm:ss');
                    return time;
                } else {
                    return "";
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

            }
        }
    }
</script>
<style scoped>
    .intergral-con {
        width: 1100px;
        margin: auto;
    }

    .intergral-con-top {
        font-size: 14px;
        background: #fff;
        padding: 16px 0;
        text-indent: 10px;
        box-shadow: 0 0 10px #ccc;
        margin: 0 10px;
    }

    .menu-list {
        line-height: 36px;
        padding: 0 4px;
        margin-right: 10px;
        cursor: pointer;
    }

    .div-line {
        margin: 0 10px 0 12px;
        color: #000;
    }

    .self-define {
        margin-right: 14px;
    }

    .serch-btn {
        vertical-align: middle;
        margin-left: 10px;
        cursor: pointer;
    }

    .intergral-data-detail {
        margin: 20px 8px;
    }
</style>

<template>
    <div class="bug-list">
        <el-card class="box-card">
            <div slot="header" class="clearfix">
                <span>{{listName}}</span>
            </div>
            <div class="table-box">
                <el-table
                        :data="tableData"
                        style="width: 100%"
                        :row-class-name="tableRowClassName">
                    <el-table-column
                            label="标题">
                        <template scope="scope">
                            <router-link :to="{ path: '/index/bug/details', query: { id: scope.row.id }}">{{scope.row.name}}</router-link>

                        </template>
                    </el-table-column>
                    <el-table-column
                            label="状态"
                            width="80">
                        <template scope="scope">
                            <span>{{ stateToWords(scope.row.state) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            label="类型"
                            width="100">
                        <template scope="scope">
                            <span>{{ typeToWords(scope.row.type) }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column
                            prop="give"
                            label="分配人"
                            width="120">
                    </el-table-column>
                    <el-table-column
                            prop="date"
                            label="时间"
                            width="180">
                    </el-table-column>
                </el-table>
            </div>
        </el-card>
    </div>
</template>

<script>
    export default {
        name: "List",
        data(){
            return {
                listName:'所有的BUG',
                getData:{
                    id:'222',
                    listType:1,
            },
                tableData:[
                    {
                        id:1234456,
                        name: 'bug标题',
                        state: 1,
                        type:1,
                        give:'王小虎',
                        date: '2016-05-04 10:36:08',
                    },
                    {
                        id:2234456,
                        name: 'bug标题',
                        state: 1,
                        type:1,
                        give:'王小虎',
                        date: '2016-05-04 10:36:08',
                    },
                ]
            }
        },
        watch:{
            $route(to,from){
                this.getDefaultDatas();
            }
        },
        created() {
            this.getDefaultDatas();
        },
        methods:{
            getDefaultDatas(){
                this.getData.id = this.$route.query.id;
                this.getData.listType = this.$route.query.listType;
                switch (this.getData.listType) {
                    case 0:
                        this.listName = '所有的BUG'
                        break
                    case 1:
                        this.listName = '待处理的BUG'
                        break
                    case 2:
                        this.listName = '待确认的BUG'
                        break
                    case 3:
                        this.listName = '已解决的BUG'
                        break
                    default:
                        this.listName = '所有的BUG'
                }
            },
            tableRowClassName({row, rowIndex}) {
                if (rowIndex === 1) {
                    return 'warning-row';
                } else if (rowIndex === 3) {
                    return 'success-row';
                }
                return '';
            },
            stateToWords(val) {
                if (val === 1) {
                    return '已分配';
                } else if (val === 2) {
                    return '已解决';
                } else if (val === 3) {
                    return '已关闭';
                } else if (val === 4) {
                    return '打回';
                }
            },
            typeToWords(val) {
                if (val === 1) {
                    return '严重错误';
                } else if (val === 2) {
                    return '主要错误';
                } else if (val === 3) {
                    return '一般错误';
                } else if (val === 4) {
                    return '建议';
                }
            }
        },

    }
</script>

<style scoped>

</style>
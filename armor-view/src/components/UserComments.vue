<template>
<div class="userCommentsBlock">
    <el-form :inline="true" >
        <el-form-item label="">
            <el-select filterable clearable v-model="form.userId" placeholder="评价人">
                <el-option v-for="item in userList" :key="item.id" :label="item.name"
                           :value="item.id"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="">
            <el-select clearable v-model="form.grade" placeholder="等级">
                <el-option v-for="item in gradeList" :key="item" :label="item"
                           :value="item"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item>
        <el-button type="primary" @click="onSubmit">查询</el-button>
    </el-form-item>
    </el-form>
    <el-table
            :data="data.list"
            stripe
            style="width: 100%">
        <el-table-column
                label="任务名称">
            <template scope="scope">
                <el-button type="text" @click="toTaskList(scope.row.taskId)"> {{scope.row.taskName}}</el-button>
            </template>
        </el-table-column>
        <el-table-column
                prop="userName"
                label="评价人">
        </el-table-column>
        <el-table-column
                prop="grade"
                label="评价等级">
            <template scope="scope">
                <span :style="scope.row.grade==='C'?'color: #ff4949;':''">{{scope.row.grade}}</span>
            </template>
        </el-table-column>
        <el-table-column
                prop="description"
                label="评价描述">
        </el-table-column>
        <el-table-column
                label="任务负责人">
            <template scope="scope">
                <el-popover trigger="hover" placement="top">
                    <p>{{ scope.row.taskUserDesc }}</p>
                    <div slot="reference" class="name-wrapper">
                        <el-tag>{{ scope.row.taskUserName }}</el-tag>
                    </div>
                </el-popover>
            </template>
        </el-table-column>
        <el-table-column
                label="评价时间">
            <template scope="scope">
                <span>{{scope.row.createTime|_formatDateTime}}</span>
            </template>
        </el-table-column>
    </el-table>
    <div class="pagination">
        <el-pagination
                @current-change="handleCurrentChange"
                :current-page.sync="data.pageNum"
                :page-size="data.pageSize"
                :layout="pageLayout"
                :total="data.total">
        </el-pagination>
    </div>
</div>
</template>
<script>
    import Http from '../lib/Http'
    import Helper from '../lib/Helper'

    export default {
        name: 'UserComments',
        data() {
            return {
                data:{},
                form:{
                    pageNum: 1,
                    userId:'',
                    grade:''
                },
                userList: [],
                gradeList:['A','B','C']
            }
        },
        created(){
            this.fetchUserComments();
            this.fetchUserList();
        },
        beforeMount:function () {
            //选中任务tab
            this.$root.eventBus.$emit("handleTabSelected", "comments");
        },
        computed: {
            pageLayout() {
                if (this.data.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            }
        },
        methods: {
            fetchUserComments(){
                let vue = this;
                Http.zsyGetHttp('/stats/userComments', this.form, (resp)=>{
                    vue.data = resp.data
                })
            },
            handleCurrentChange(currentPage){
                this.form.pageNum = currentPage
                this.fetchUserComments();
            },
            fetchUserList() {
                let vm = this
                Http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.userList = resp.data
                })
            },
            onSubmit(){
                this.form.pageNum  =1;
                this.fetchUserComments();
            },
            toTaskList(taskId){
                console.log(taskId)
                this.$router.push({name:'taskListFormComments', params:{ taskId:taskId }})
            }

        }
    }


</script>
<style scoped>
    .userCommentsBlock{
        width: 1100px;
        margin: auto;
    }
    .pagination .el-pagination {
        text-align: right;
        padding-right: 0;
        margin-top: 20px;
        margin-right: 10px;
    }
    .tb-taskname .cell{
        width: 100px;
        text-overflow: ellipsis; overflow: hidden; white-space: nowrap;
    }
</style>


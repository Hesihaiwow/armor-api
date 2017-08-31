<template>
    <div class="task-con">
        <div class="task-top clearfix">
            <div class="clearfix">
                <div class="clearfix select-box">
                    <div class="task-top-list fl">
                        <span class="ttl-name">项目</span>
                        <el-select clearable v-model="form.projectId" placeholder="请选择">
                            <el-option v-for="item in projectList" :key="item.id" :label="item.name"
                                       :value="item.id"></el-option>
                        </el-select>
                    </div>
                    <div class="task-top-list fl">
                        <span class="ttl-name">成员</span>
                        <el-select clearable v-model="form.userId" placeholder="请选择">
                            <el-option v-for="item in userList" :key="item.id" :label="item.name"
                                       :value="item.id"></el-option>
                        </el-select>
                    </div>
                    
                    <div class="task-top-list fl">
                        <span class="ttl-name">优先级</span>
                        <el-select clearable v-model="form.priority" placeholder="请选择">
                            <el-option
                                    v-for="item in priorityList"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </div>
                    <div class="task-top-list fl">
                        <span class="ttl-name">类型</span>
                        <el-select clearable v-model="form.type" placeholder="请选择">
                            <el-option v-for="item in typeList" :key="item.value" :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </div>
                     
                     
                    <div class="task-top-list fl">
                        <span class="ttl-name">状态</span>
                        <el-select clearable v-model="form.status" placeholder="请选择">
                            <el-option v-for="item in status" :key="item.value" :label="item.name"
                                       :value="item.value"></el-option>
                        </el-select>
                    </div>

                    <div class="task-top-list fl">
                        <span class="ttl-name">截止日期</span>
                        <el-date-picker v-model="timeRange" type="daterange" :picker-options="pickerOptions"
                                        placeholder="选择日期"
                                        @change="timeChange"></el-date-picker>
                    </div>
                </div>
                
            </div>
            <transition name="filter">
               <div v-show="open">
                    <div class="task-top-list clearfix">
                         <span class="ttl-name fl">标签&nbsp;</span>
                         <div class="fl tag-name clearfix">
                             <el-button class="fl" type="" size="small" v-for="item in tagList" @click="addFormTagId(item.id,1,$event)">{{item.name}}</el-button>
                         </div>
                    </div>
                     <div class="task-top-list  clearfix">
                        <span class="ttl-name fl">阶段&nbsp;</span>
                        <div class="fl tag-name clearfix">
                            <el-button class="fl" size="small" v-for="item in stageList" @click="addFormTagId(item.id,2,$event)">{{item.name}}</el-button>
                         </div>
                     </div>
               </div>
            </transition>
            <div class="clearfix">
                <div class="task-top-list fl search-button">
                    <el-button type="primary" icon="search" size="small" @click="fetchTaskList()" :loading="loading">查询
                    </el-button>
                </div>
                <div class="task-top-list fl creat-task" @click="createTaskClick" v-show="permit">
                    <span class="ttl-add-icon">+</span>
                    <span class="ttl-add-msg">创建多人任务</span>
                </div>
            </div>
            <div class="filter-btn">
                <span @click="openFun($event)">收起筛选</span>
            </div>
        </div>

        <div class="task-lis-con">

            <task-item :taskItems="taskItems" :isPrivate="false" @reload="fetchTaskList"
                       :projectList="projectList"
                       :userList="userList"
                       :stageList="stageList"
                       :tagList="tagList"></task-item>
            <create-task ref="createTaskPop" @handleFetchTaskList="fetchTaskList"></create-task>
        </div>
        <div class="pagination">
            <el-pagination
                    @current-change="handleCurrentChange"
                    :current-page.sync="page.pageNum"
                    :page-size="page.pageSize"
                    :layout="pageLayout"
                    :total="page.total">
            </el-pagination>
        </div>
    </div>
</template>
<script>
    import CreateTask from './CreateTask'
    import TaskItem from './TaskItem'
    import http from '../lib/Http'
    import helper from '../lib/Helper'
    import moment from 'moment';

    moment.locale('zh-cn');
    export default {
        name: 'Task',
        data() {
            return {
                open:true,
                loading: true,
                timeRange: '',
                projectList: [],
                userList: [],
                stageList: [],
                tagList: [],
                priorityList: [
                    {label: '普通', value: 1},
                    {label: '紧急', value: 2},
                    {label: '非常紧急', value: 3},
                ],
                typeList: [{value: 1, name: '个人任务'}, {value: 2, name: '多人任务'}],
                status: [{value: 1, name: '进行中'}, {value: 3, name: '已完成'}],
                taskItems: [],
                page: {
                    pageNum: 0,
                    pageSize: 5,
                    total: 0,
                },
                form: {
                    projectId: '',
                    userId: '',
                    stageId: [],
                    tagId: [],
                    type: '',
                    status: '',
                    priority: '',
                    beginTime: '',
                    endTime: ''
                }, pickerOptions: {
                    shortcuts: [{
                        text: '本周',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(moment().startOf('week').valueOf());
                            end.setTime(moment().endOf('week').valueOf());
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '本月',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(moment().startOf('month').valueOf());
                            end.setTime(moment().endOf('month').valueOf());
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '本季度',
                        onClick(picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(moment().startOf('quarter').valueOf());
                            end.setTime(moment().endOf('quarter').valueOf());
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                }
            };
        },
        created() {
            this.fetchProjectList()
            this.fetchUserList()
            this.fetchStageList()
            this.fetchTagList()
            this.fetchTaskList()
            //选中任务tab
            this.$root.eventBus.$emit("handleTabSelected", "task");
        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole < 2;
            },
            pageLayout() {
                if (this.page.total > 0) {
                    return 'total, prev, pager, next'
                }
                return 'total, pager'
            }
        },
        methods: {
            openFun($event){
                this.open = !this.open;  
                if(this.open){
                    $event.currentTarget.innerHTML = "收起筛选"
                    $event.currentTarget.className = "";
                }else{
                    $event.currentTarget.innerHTML = "展开筛选";
                    $event.currentTarget.className = "open";
                }
            },
            addFormTagId(tagId,num,$event){
                
                if(this.hasClass($event.currentTarget,'active')){  
                    this.removeClass($event.currentTarget,'active');  
                    if(num == 1){
                        this.form.tagId.splice(this.findIndex(this.form.tagId,tagId),1);
                    }else if(num ==2){
                        this.form.stageId.splice(this.findIndex(this.form.stageId,tagId),1);
                    }
                }else{  
                    this.addClass($event.currentTarget,'active'); 
                    if(num == 1){
                         this.form.tagId.push(tagId) ;
                    }else if(num ==2){
                         this.form.stageId.push(tagId) ;
                    }
                }  
                console.log(this.form.tagId);
                console.log(this.form.stageId);
            },
            hasClass(obj, cls) {  
                return obj.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));  
            },
            addClass(obj, cls) {  
                if (!this.hasClass(obj, cls)) obj.className += " " + cls;  
            },
            removeClass(obj, cls) {  
                if (this.hasClass(obj, cls)) {  
                    var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');  
                    obj.className = obj.className.replace(reg, ' ');  
                }  
            },
            findIndex(arr,val){
                for(var i=0;i<arr.length;i++){
                    if(arr[i] == val){
                        return i;
                    }
                }
            },  
            handleCurrentChange(currentPage) {
                this.page.pageNum = currentPage
                this.fetchTaskList()
            },
            createTaskClick() {
                // 点击建任务
                this.$refs.createTaskPop.show();
            }, timeChange(time) {
                // 选择结束时间
                time = time.split(' - ')
                if (time && time.length == 2) {
                    this.form.beginTime = `${time[0]} 00:00:00`
                    this.form.endTime = `${time[1]} 23:59:59`
                } else {
                    this.form.beginTime = this.form.endTime = ''
                }
            },
            fetchProjectList() {
                let vm = this
                http.zsyGetHttp('/project/list', {}, (resp) => {
                    vm.projectList = resp.data
                })
            },
            fetchUserList() {
                let vm = this
                http.zsyGetHttp('/user/effective', {}, (resp) => {
                    vm.userList = resp.data
                })
            },
            fetchStageList() {
                let vm = this
                http.zsyGetHttp('/stage/list', {}, (resp) => {
                    vm.stageList = resp.data
                })
            },
            fetchTagList() {
                let vm = this
                http.zsyGetHttp('/tag/list', {}, (resp) => {
                    vm.tagList = resp.data
                })
            },
            fetchTaskList() {
                this.loading = true
                this.taskItems = []
                let vm = this
                let param = {}
                param['pageNum'] = this.page.pageNum || 1;
                param['pageSize'] = this.page.pageSize;
                if (this.form.projectId !== '') {
                    param['projectId'] = this.form.projectId
                }
                if (this.form.userId !== '') {
                    param['userId'] = this.form.userId
                }
                if (this.form.status !== '') {
                    param['status'] = this.form.status
                }
                if (this.form.priority !== '') {
                    param['priority'] = this.form.priority
                }
                if (this.form.beginTime !== '') {
                    param['beginTime'] = this.form.beginTime
                }
                if (this.form.endTime !== '') {
                    param['endTime'] = this.form.endTime
                }
                if (this.form.stageId.length > 0) {
                    param['stageId'] = this.form.stageId
                }
                if (this.form.tagId.length > 0) {
                    param['tagId'] = this.form.tagId
                }
                param['type'] = this.form.type
                http.zsyPostHttp('/task/public/master/all', param, (resp) => {
                    const list = resp.data.list
                    list.forEach((el) => {
                        let endTime = '';
                        if (el.status == 1) {
                            endTime = el.endTime
                        } else {
                            endTime = el.completeTime
                        }
                        const diffDays = moment().diff(moment(endTime), 'days')
                        let endColor = '', endText = ''
                        endText = moment(endTime).calendar(null, {
                            sameDay: '[今天]LT',
                            nextDay: '[明天]LT',
                            nextWeek: 'L',
                            lastDay: '[昨天]LT',
                            lastWeek: 'L',
                            sameElse: 'L'
                        })
                        if (el.status == 1) {

                            if (diffDays == 0) {
                                endColor = 'orange'
                            } else if (diffDays > 0) {
                                endColor = 'red'
                            } else if (diffDays < 0) {
                                endColor = 'blue'
                            }
                            endText += ' 截止'
                        } else {
                            endColor = 'green'
                            endText += ' 完成'
                        }
                        el['endColor'] = endColor
                        el['endText'] = endText

                        // 优先级样式
                        if (el.priority == 2) {
                            el.borderClass = 'orange-border'
                        } else if (el.priority == 3) {
                            el.borderClass = 'red-border'
                        }

                    })
                    vm.loading = false
                    vm.taskItems = resp.data.list
                    vm.page.pageNum = resp.data.pageNum
                    vm.page.pageSize = 5
                    vm.page.total = resp.data.total
                })
            }
        },
        components: {
            TaskItem: TaskItem,
            CreateTask: CreateTask
        }
    }
</script>
<style scoped>
    .tag-name{
        width:980px;

    }
    .tag-name button{
        margin: 0 10px 10px 0;
    }
   
    .tag-name button:focus{
       color: #545454;
       border-color: #c4c4c4;
    }
     .tag-name button.active{
       color: #36A8FF;
       border-color: #36A8FF;
    }
    .select-box>div:nth-child(3n){
        margin-right: 200px;
    }
    .filter-btn{
        text-align: center;
        background-color: #f2f2f2;
    }
    .filter-btn span{
        /*position: relative;*/
        display: inline-block;
        font-size: 14px;
        line-height: 36px;
        color: #36A8FF;
        cursor: pointer;
    }

    .filter-btn span:after{
        content: "";
        display: inline-block;
        margin-left:10px;
        margin-bottom: -1px;
        width: 8px;
        height: 8px;
        border-left: 1px solid #36A8FF;
        border-top:1px solid #36A8FF;
        transform:rotate(45deg);
        /*position: absolute;*/
    }
    .filter-btn span.open:after{
        margin-bottom:2px;
        transform:rotate(-135deg);
    }
    .filter-enter,.filter-leave-active{
        opacity: 0;
        /*transform: translate3d(0,20%,0);*/
    }
    .filter-enter-active,.filter-leave-active{
        transition:all .2s ease;
    }
    .pagination {
        margin: 20px 0;
        text-align: right;
    }

    .task-con {
        width: 1100px;
        margin: auto;
    }

    .task-top {
        /*position: fixed;*/
        /*transform: translateX(-50%);*/
        /*top: 80px;*/
        /*width: 1080px;*/
        /*left: 50%;*/
        background: #fff;
        padding-top: 20px;
        /*padding-bottom: 50px;*/
        border-radius: 4px;
        box-shadow: 0 0 10px #ccc;
        margin-bottom: 24px;
    }

    .task-top-list {
        margin: 0 20px 20px 20px;
    }

    .task-top-list:last-child {
        /*margin-top: 16px;*/
    }

    .task-top-list .el-select {
        width: 148px;
    }

    .ttl-name {
        margin-right: 10px;
        font-size: 14px;
    }

    .div-line {
        margin-left: 10px;
        margin-right: 6px;
    }

    .serch-btn {
        vertical-align: middle;
        margin-left: 10px;
    }

    .creat-task,.search-button {
        margin-left:63px;
        cursor: pointer;
        /* position: absolute;
        right: 0;
        bottom: 0; */
    }

     .creat-task {
        margin-left: 30px;
      /*  position: absolute;
      right: 160px;
      bottom: 0; */
   } 

    .creat-task > span {
        display: inline-block;
        vertical-align: middle;
    }

    .ttl-add-msg {
        font-size: 16px;
        color: #36A8FF;
        margin-left: 10px;
    }

    .ttl-add-icon {
        width: 24px;
        height: 24px;
        line-height: 22px;
        text-align: center;
        font-size: 24px;
        border-radius: 50%;
        color: #fff;
        background: #36A8FF;
    }

    .task-lis-con {
        /*max-height: 150px;*/
        /*overflow-y: auto;*/
        /*margin-top: 200px;*/
    }
</style>

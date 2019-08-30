<template>
    <div class="task-board">
        <div class="task-board-list clearfix" id="task-board-list" :style="{'width':taskBoxWidth}">
            <div class="fl task-list" v-for="(item,key) in stageList" @drop='drop($event)'
                 @dragover='allowDrop($event)'  >
                <header :data-id="item.id" :style="'cursor: '+cursor" >{{item.name}} · {{item.tasks ? item.tasks.length : 0}}</header>
                <ul class="task-item" :stageId="item.id">
                    <li class="clearfix" :class="task.borderClass" draggable='true' @dragstart='drag($event,task.canDrag)'
                        v-for="(task,keyTask) in item.tasks" @click="handleTaskItemClick(task.id)" :taskId="task.id"
                        :createBy="task.createBy" >
                        <div class="trans" v-show="task.examine===1"></div>
                        <div class="fl complate" data-title="" >
                        </div>
                        <div class="fl task-name">
                            <div style="font-size: 16px;padding: 12px 0">{{task.name}}</div>
                            <div v-if="item.name==='已发布'&&task.status===3">
                                <span class="tips purple" >{{task.endText}}</span>
                            </div>
                            <div v-else-if="task.status===0">
                                <span class="tips grey">任务暂停</span>
                            </div>
                            <div v-else="">
                                <span class="tips" :class="task.endColor">{{task.endText}}</span>
                            </div>
                            <div style="border-top: 5px;padding: 2px"><span v-if="task.status===1&&task.delayNo!==0" class="tips orange">超时人数:{{task.delayNo}}</span></div>
                        </div>
                        <div class="master-info fr ellipsis">
                            <img v-if="task.avatarUrl && task.avatarUrl!==''" :src="task.avatarUrl" :alt="task.userName">
                            <span v-else="">{{task.userName}}</span>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>
<script>
    let dom;
    import moment from 'moment';
    import helper from '../../lib/Helper'
    import move from '../../lib/move'
    import ElButton from "../../../node_modules/element-ui/packages/button/src/button";

    moment.locale('zh-cn');
    export default {
        components: {ElButton},
        name: "task-board",
        data() {
            return {
                stageList: [],
                taskBoxWidth: '1200px',
                cursor:''
            }
        },
        computed: {
            loginUserRole() {
                let userRole = helper.decodeToken().userRole;
                return userRole;
            },
            loginUserId() {
                let userId = helper.decodeToken().userId;
                return userId;
            },
        },
        methods: {
            drag: function (event,canDrag) {
                console.log(canDrag);
                // 自己创建的任务及超管才可以拖动任务
                let taskCreateBy = event.currentTarget.getAttribute('createby');
                if((this.loginUserId === taskCreateBy || this.loginUserRole===0) && canDrag ){
                    dom = event.currentTarget;
                }else{
                    event.preventDefault();
                    return;
                }
            },
            drop: function (event) {
                let stageId = '', originId = dom.getAttribute('taskid'), targetId = '', vm = this;
                event.preventDefault();
                if (event.target.tagName.toLowerCase() === "li") {
                    targetId = event.target.getAttribute('taskid');
                } else if (event.target.tagName.toLowerCase() === "ul") {
                    stageId = event.target.getAttribute('stageid');
                } else {
                    let li = this.findParent(event.target);
                    targetId = li.getAttribute('taskid');
                }

                this.http.zsyPutHttp('/task/move', {
                    originId: originId,
                    targetId: targetId,
                    targetStageId: stageId
                }, (res) => {
                    if (event.target.tagName.toLowerCase() === "li") {
                        event.target.parentNode.insertBefore(dom, event.target);
                    } else if (event.target.tagName.toLowerCase() === "ul") {
                        event.target.appendChild(dom);
                    } else {
                        let li = vm.findParent(event.target);
                        li.parentNode.insertBefore(dom, li);
                    }
                    vm.getData2();
                    this.fetchUnreadNoticeNum();
                });

            },
            //查询所有未读数量
            fetchUnreadNoticeNum(){
                this.http.zsyGetHttp('/task/notification/un-read/num',{},(res)=>{
                    window.localStorage.setItem("unreadNum",res.data.count)
                })
            },
            allowDrop: function (event) {
                event.preventDefault();
            },
            findParent(obj) {
                while (obj.parentNode && obj.tagName.toLowerCase() !== "li") {
                    obj = obj.parentNode;
                }
                return obj;
            },
            handleTaskItemClick(taskId) {
                this.$root.eventBus.$emit("handleBoardClick", taskId);
            },
            getData() {
                const publishType = window.localStorage.getItem("publishType");
                // 获取阶段
                let that = this;
                that.http.zsyGetHttp('/stage/list', {}, (res) => {
                    that.stageList = res.data;
                    that.taskBoxWidth = that.stageList.length * 270 + "px";
                    that.stageList.forEach((stage) => {
                        if(publishType===1){
                            // 获取任务
                            that.http.zsyGetHttp(`/task/tasksByStageTime/${stage.id}`, {}, (res) => {
                                let list = res.data;
                                list.forEach((el) => {
                                    let endTime = '', today = moment().format('YYYY-MM-DD');
                                    if (el.status === 1) {
                                        endTime = el.endTime
                                    } else {
                                        endTime = el.completeTime
                                    }
                                    endTime = moment(endTime).format('YYYY-MM-DD');
                                    const diffDays = moment(today).diff(moment(endTime), 'days');
                                    let endColor = '', endText = '';
                                    endText = moment(endTime).calendar(null, {
                                        sameDay: '[今天]',
                                        nextDay: '[明天]',
                                        nextWeek: 'L',
                                        lastDay: '[昨天]',
                                        lastWeek: 'L',
                                        sameElse: 'L'
                                    });
                                    if (el.status === 1) {
                                        if (diffDays === 0) {
                                            endColor = 'orange'
                                        } else if (diffDays > 0) {
                                            endColor = 'red'
                                        } else if (diffDays < 0) {
                                            endColor = 'blue'
                                        }
                                        endText += ' 截止'
                                    } else {
                                        endColor = 'green';
                                        endText += ' 完成'
                                    }
                                    el['endColor'] = endColor;
                                    el['endText'] = endText;

                                    // 优先级样式
                                    if (el.priority === 2) {
                                        el.borderClass = 'orange-border'
                                    } else if (el.priority === 3) {
                                        el.borderClass = 'red-border'
                                    }
                                });
                                that.$set(stage, 'tasks', list)
                            })
                        }else{
                            that.http.zsyGetHttp(`/task/tasksByStage/${stage.id}`, {}, (res) => {
                                let list = res.data;
                                list.forEach((el) => {
                                    let endTime = '', today = moment().format('YYYY-MM-DD');
                                    if (el.status === 1) {
                                        endTime = el.endTime
                                    } else {
                                        endTime = el.completeTime
                                    }
                                    endTime = moment(endTime).format('YYYY-MM-DD');
                                    const diffDays = moment(today).diff(moment(endTime), 'days');
                                    let endColor = '', endText = '';
                                    endText = moment(endTime).calendar(null, {
                                        sameDay: '[今天]',
                                        nextDay: '[明天]',
                                        nextWeek: 'L',
                                        lastDay: '[昨天]',
                                        lastWeek: 'L',
                                        sameElse: 'L'
                                    });
                                    if (el.status === 1) {
                                        if (diffDays === 0) {
                                            endColor = 'orange'
                                        } else if (diffDays > 0) {
                                            endColor = 'red'
                                        } else if (diffDays < 0) {
                                            endColor = 'blue'
                                        }
                                        endText += ' 截止'
                                    } else {
                                        endColor = 'green';
                                        endText += ' 完成'
                                    }
                                    el['endColor'] = endColor;
                                    el['endText'] = endText;

                                    // 优先级样式
                                    if (el.priority === 2) {
                                        el.borderClass = 'orange-border'
                                    } else if (el.priority === 3) {
                                        el.borderClass = 'red-border'
                                    }
                                });
                                that.$set(stage, 'tasks', list)
                            })
                        }

                    });
                });
            },
            getData2() {
                const justMine = window.localStorage.getItem("justMine");
                // 获取阶段
                let that = this;
                that.http.zsyGetHttp('/stage/list', {}, (res) => {
                    that.stageList = res.data;
                    that.taskBoxWidth = that.stageList.length * 270 + "px";
                    that.stageList.forEach((stage) => {
                        if(justMine==1){
                            // 获取任务
                            that.http.zsyGetHttp(`/task/tasksByStage/mine/${stage.id}`, {}, (res) => {
                                let list = res.data;
                                list.forEach((el) => {
                                    let endTime = '', today = moment().format('YYYY-MM-DD');
                                    if (el.status === 1) {
                                        endTime = el.endTime
                                    } else {
                                        endTime = el.completeTime
                                    }
                                    endTime = moment(endTime).format('YYYY-MM-DD');
                                    const diffDays = moment(today).diff(moment(endTime), 'days');
                                    let endColor = '', endText = '';
                                    endText = moment(endTime).calendar(null, {
                                        sameDay: '[今天]',
                                        nextDay: '[明天]',
                                        nextWeek: 'L',
                                        lastDay: '[昨天]',
                                        lastWeek: 'L',
                                        sameElse: 'L'
                                    });
                                    if (el.status === 1) {
                                        if (diffDays === 0) {
                                            endColor = 'orange'
                                        } else if (diffDays > 0) {
                                            endColor = 'red'
                                        } else if (diffDays < 0) {
                                            endColor = 'blue'
                                        }
                                        endText += ' 截止'
                                    } else {
                                        endColor = 'green';
                                        endText += ' 完成'
                                    }
                                    el['endColor'] = endColor;
                                    el['endText'] = endText;

                                    // 优先级样式
                                    if (el.priority === 2) {
                                        el.borderClass = 'orange-border'
                                    } else if (el.priority === 3) {
                                        el.borderClass = 'red-border'
                                    }
                                });
                                that.$set(stage, 'tasks', list)
                            })
                        }else{
                            that.http.zsyGetHttp(`/task/tasksByStage/${stage.id}`, {}, (res) => {
                                let list = res.data;
                                list.forEach((el) => {
                                    let endTime = '', today = moment().format('YYYY-MM-DD');
                                    if (el.status === 1) {
                                        endTime = el.endTime
                                    } else {
                                        endTime = el.completeTime
                                    }
                                    endTime = moment(endTime).format('YYYY-MM-DD');
                                    const diffDays = moment(today).diff(moment(endTime), 'days');
                                    let endColor = '', endText = '';
                                    endText = moment(endTime).calendar(null, {
                                        sameDay: '[今天]',
                                        nextDay: '[明天]',
                                        nextWeek: 'L',
                                        lastDay: '[昨天]',
                                        lastWeek: 'L',
                                        sameElse: 'L'
                                    });
                                    if (el.status === 1) {
                                        if (diffDays === 0) {
                                            endColor = 'orange'
                                        } else if (diffDays > 0) {
                                            endColor = 'red'
                                        } else if (diffDays < 0) {
                                            endColor = 'blue'
                                        }
                                        endText += ' 截止'
                                    } else {
                                        endColor = 'green';
                                        endText += ' 完成'
                                    }
                                    el['endColor'] = endColor
                                    el['endText'] = endText

                                    // 优先级样式
                                    if (el.priority === 2) {
                                        el.borderClass = 'orange-border'
                                    } else if (el.priority === 3) {
                                        el.borderClass = 'red-border'
                                    }
                                });
                                that.$set(stage, 'tasks', list)
                            })
                        }

                    });
                });
            },
            drugList(){
                let vm = this;
                if(this.$parent.btnValStatus === 2){
                    let oUl=document.getElementById('task-board-list');
                    /*let oUl = document.queryselector(".task-board-list");
                     let aLi = document.queryselectorAll(".task-list");*/
                    let aLi=oUl.children;
                    let zIndex=2;
                    //0.布局转换
                    let aPos=[];

                    for(let i=0;i<aLi.length;i++){
                        aPos.push({left:aLi[i].offsetLeft,top:aLi[i].offsetTop});
                        aLi[i].style.left=aPos[i].left+'px';
                        aLi[i].style.top=aPos[i].top+'px';
                    }
                    for(let i=0;i<aLi.length;i++){
                        aLi[i].style.position='absolute';
                        aLi[i].style.margin=0;
                        aLi[i].index=i;
                    }

                    //1.拖拽
                    for(let i=0;i<aLi.length;i++){
                        drag(aLi[i].children[0],aLi[i]);//拖拽每一个图标
                    }
                }else{
                    console.log("列表模式")
                }


                function drag(obj,objBox){
                    if(vm.loginUserRole>0){
                        return
                    }
                    obj.onmousedown=function(ev){
                        let oEvt=ev||event;
                        let disX=oEvt.clientX-objBox.offsetLeft;
                        let disY=oEvt.clientY-objBox.offsetTop;
                        objBox.style.zIndex=zIndex++;
                        clearInterval(objBox.timer);
                        document.onmousemove=function(ev){
                            let oEvt=ev||event;
                            objBox.style.left=oEvt.clientX-disX+'px';
                            objBox.style.top=oEvt.clientY-disY+'px';

                            //2.move时碰撞
                            let nearObj=findNearest(objBox);
                            if(nearObj && nearObj!==objBox){//撞到了
                                //动所有的房客
                                //交换索引，所有房客
                                //'所有'有条件的
                                let n=objBox.index;
                                let m=nearObj.index;
                                for(let i=0;i<aLi.length;i++){
                                    //n<aLi[i].index<=m
                                    //m<=aLi[i].index<n
                                    if(aLi[i].index>n && aLi[i].index<=m){
                                        //←
                                        aLi[i].index--;
                                        move.move(aLi[i],aPos[aLi[i].index]);
                                    }else if(aLi[i].index>=m && aLi[i].index<n){
                                        //→
                                        aLi[i].index++;
                                        move.move(aLi[i],aPos[aLi[i].index]);
                                    }
                                }
                                objBox.index=m;
                            }
                        };
                        document.onmouseup=function(){
                            document.onmousemove=document.onmouseup=null;

                            //抓着的对象回自个位置
                            move.move(objBox,aPos[objBox.index]);
                            vm.dragStage(objBox.getElementsByTagName('header')[0].getAttribute("data-id"),objBox.index);
                        };
                        return false;
                    };
                }
                function findNearest(obj){
                    let minDis=99999999;
                    let minIndex=-1;
                    for(let i=0;i<aLi.length;i++){
                        //if(obj==aLi[i]) continue;
                        if(collTest(obj,aLi[i])){   //撞到的房子
                            //还要找最近

                            let dis=getDis(obj,aLi[i]);//取出来的也是obj1到房子的距离
                            if(dis<minDis){
                                minDis=dis;
                                minIndex=i;
                            }
                        }
                    }
                    if(minIndex===-1){
                        return null;
                    }else{
                        return aLi[minIndex];   //就返房客出去
                    }
                }
                function getDis(obj1,obj2){
                    let a=aPos[obj2.index].left-obj1.offsetLeft;
                    let b=aPos[obj2.index].top-obj1.offsetTop;
                    return Math.sqrt(a*a+b*b);
                }
                function collTest(obj1,obj2){//obj1对象和obj2的位置（房子)撞
                    let l1=obj1.offsetLeft;
                    let t1=obj1.offsetTop;
                    let r1=obj1.offsetLeft+obj1.offsetWidth;
                    let b1=obj1.offsetTop+obj1.offsetHeight;

                    let l2=aPos[obj2.index].left;
                    let t2=aPos[obj2.index].top;
                    let r2=aPos[obj2.index].left+obj2.offsetWidth;
                    let b2=aPos[obj2.index].top+obj2.offsetHeight;

                    if(l1>r2||t1>b2||r1<l2||b1<t2){
                        return  false;
                    }else{
                        return true;
                    }
                }
            },
            dragStage(stage,index) {
                let num = 0;
                if(index ===this.stageList.length){//移到最后
                    num = 1;
                }
                if(index === 0){//移到第一个
                    num = 2;
                }
                this.http.zsyPutHttp('/stage/move', {
                    id: stage,
                    name: '移动专用'+Math.floor(Math.random()*1000),
                    sort: index,
                    num: num,
                }, (res) => {
                });
            }
        },
        created() {
            this.getData2();
        },
        beforeMount() {
            let vm = this;
            if(this.loginUserRole===0 ){
                this.cursor = 'move';
            }
            this.$root.eventBus.$on("reloadBoard", () => {
                vm.getData2();
            });
        },
        mounted(){
            setTimeout(()=>{
                this.drugList()
            },400)
        },
        beforeDestroy(){
        }
    }
</script>
<style lang="less" scoped>
    .task-board {
        position: fixed;
        top: 120px;
        right: 0;
        bottom: 0;
        left: 0;
        padding: 10px;
        /*width: 1080px;*/
        margin: 0 auto;
        background-color: #fff;
        overflow-x: auto;
        .task-board-list {
            position: relative;
            width: 400%;
            height: 100%;
            .task-list {
                position: relative;
                width: 260px;
                margin-right: 10px;
                height: 100%;
                background-color: #eee;
                padding-bottom: 10px;
                box-sizing: border-box;
                header {
                    position: absolute;
                    width: 100%;
                    top: 0;
                    left: 0;
                    padding-left: 10px;
                    line-height: 50px;
                    font-size: 16px;
                    font-weight: bold;
                    background-color: #eee;
                    box-sizing: border-box;
                    -webkit-touch-callout: none;
                    -webkit-user-select: none;
                    -khtml-user-select: none;
                    -moz-user-select: none;
                    -ms-user-select: none;
                    user-select: none;
                }
                .task-item {

                    padding: 50px 10px 0;
                    height: 100%;
                    box-sizing: border-box;
                    overflow-y: auto;

                    li {
                        border-bottom: 10px solid #eee;
                        padding: 5px 10px 15px;
                        background-color: #fff;
                        border-radius: 3px;
                        cursor: pointer;
                        .complate {
                            margin-right: 10px;
                            span {
                                display: inline-block;
                                margin-top: 4px;
                                width: 20px;
                                height: 20px;
                                text-align: justify;
                                background-color: #eee;
                                border-radius: 3px;
                            }
                        }
                        .task-name {
                            width: 150px;
                            font-size: 12px;
                            line-height: 18px;
                            word-break: break-all;
                            .tips {
                                padding: 0 8px 2px;
                                /*   background-color: #FFAF38;*/
                                color: #fff;
                                border-radius: 3px;
                            }
                            .tips.red {
                                background: #FF4515;
                            }
                            .tips.orange {
                                background: #FF9900;
                            }
                            .tips.blue {
                                background: #36A8FF;
                            }
                            .tips.grey {
                                background: #7A7A7A;
                            }
                            .tips.green {
                                background: #339933;
                            }
                            .tips.purple {
                                background: #da70d6;
                            }
                        }
                        .master-info {
                             img {
                                 width: 40px;
                                 height: 40px;
                                 border-radius: 50%;
                             }
                            height: 40px;
                            background: #69C8FA;
                            border-radius: 50%;
                            line-height: 40px;
                            text-align: center;
                            color: #fff;
                            cursor: pointer;
                            width: 40px;
                        }
                    }
                    li:last-child {
                        border-bottom: 0;
                    }
                }
            }
            .task-list:last-child {
                margin-right: 0;

            }

            .triangle-topright {
            }

            .trans {
                margin-left: -10px;
                margin-top: -5px;
                width: 0px;
                height: 0px;
                border-top: 15px solid rgb(32, 163, 191);
                border-left: 0px solid transparent;border-right: 15px solid transparent;
            }
        }

    }
</style>

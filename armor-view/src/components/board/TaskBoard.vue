<template>
    <div class="task-board">
        <div class="task-board-list clearfix" :style="{'width':taskBoxWidth}">
            <div class="fl task-list" v-for="(item,key) in list" @drop='drop($event)' @dragover='allowDrop($event)'>
                <header :data-id="item.id">{{item.name}}</header>
                <ul class="task-item">
                    <li class="clearfix" draggable='true' @dragstart='drag($event)' v-for="(val,keyTask) in item.task.list">
                        <div class="fl complate" data-title="由执行者阿敏设置任务是否完成">
                            <span></span>
                        </div>
                        <div class="fl task-name">
                            <div>{{val.description}}</div>
                            <span class="tips">截止时间</span>
                        </div>
                        <div class="master-info fr">
                            <!--<img src="../../assets/img/man.png" alt="">-->
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>
<script>
    var dom;
    export default{
        name:"task-board",
        data(){
            return{
                list:[
                    {name:'待开发',id:"212754785051344890",task:{}},
                    {name:'待设计需求',id:"212754785051344891",task:{}},
                    {name:'设计中需求',id:"212754785051344892",task:{}},
                    {name:'开发中',id:"212754785051344894",task:{}},
                    {name:'待测试',id:"212754785051344895",task:{}},
                    {name:'测试中',id:"212754785051344896",task:{}},
                    {name:'待发布',id:"212754785051344897",task:{}},
                    {name:'已发布',id:"212754785051344898",task:{}},
                ],
                taskBoxWidth:'1200px',
                httpStageId:[
                    {name:'待开发',id:"212754785051344890"},
                    {name:'待设计需求',id:"212754785051344891"},
                    {name:'设计中需求',id:"212754785051344892"},
                    {name:'开发中',id:"212754785051344894"},
                    {name:'待测试',id:"212754785051344895"},
                    {name:'测试中',id:"212754785051344896"},
                    {name:'待发布',id:"212754785051344897"},
                    {name:'已发布',id:"212754785051344898"}
                ]
            }
        },
        methods: {
            drag:function(event){
                dom = event.currentTarget
                console.log(dom)
            },
            drop:function(event){
                event.preventDefault();


                if(event.target.tagName.toLowerCase() == "li"){
                    event.target.parentNode.insertBefore(dom,event.target);
                }else if(event.target.tagName.toLowerCase() == "ul"){

                    event.target.appendChild(dom);
                }
                console.log(event.target.parentNode)
            },
            allowDrop:function(event){
                event.preventDefault();
            }
        },
        beforeMount(){
            /*212754785051344890 待开发*/
            this.http.zsyPostHttp('/task/public/master/all',{"stageId":['212754785051344890']}, (res) => {
                this.list[0].task = res.data;
            })

            /*212754785051344891 待设计需求*/
            this.http.zsyPostHttp('/task/public/master/all',{"stageId":['212754785051344891']}, (res) => {
                this.list[1].task = res.data;
            })
            /*212754785051344892 设计中需求*/
            this.http.zsyPostHttp('/task/public/master/all',{"stageId":['212754785051344892']}, (res) => {
                this.list[2].task = res.data;
            })
            /*212754785051344894 开发中*/
            this.http.zsyPostHttp('/task/public/master/all',{"stageId":['212754785051344894']}, (res) => {
                this.list[3].task = res.data;
            })
            /*212754785051344895 待测试*/
            this.http.zsyPostHttp('/task/public/master/all',{"stageId":['212754785051344895']}, (res) => {
                this.list[4].task = res.data;
            })
            /*212754785051344896 测试中*/
            this.http.zsyPostHttp('/task/public/master/all',{"stageId":['212754785051344896']}, (res) => {
                this.list[5].task = res.data;
            })
            /*212754785051344897 待发布*/
            this.http.zsyPostHttp('/task/public/master/all',{"stageId":['212754785051344897']}, (res) => {
                this.list[6].task = res.data;
            })
            /*212754785051344898 已发布*/
            this.http.zsyPostHttp('/task/public/master/all',{"stageId":['212754785051344898']}, (res) => {
                this.list[7].task = res.data;
            })

            console.log(this.list)
        },
        mounted(){
            this.taskBoxWidth = this.list.length*270+"px"
        }
    }
</script>
<style lang="less" scoped>
    .task-board{
        padding:10px;
        width:1080px;
        margin:0 auto;
        background-color: #fff;
        overflow-x: auto;
        .task-board-list{
            width:400%;
            .task-list{
                width:260px;
                margin-right: 10px;

                background-color: #eee;
                header{
                    padding-left: 10px;
                    line-height: 30px;
                    font-size: 14px;
                    font-weight: bold;
                }
                .task-item{
                    padding:10px;
                    padding-top: 0;
                    height: 500px;
                    overflow-y: auto;
                    li{
                        border-bottom: 10px solid #eee;
                        padding:5px 10px;
                        background-color: #fff;
                        border-radius: 3px;
                        cursor: pointer;
                        .complate{
                            margin-right: 10px;
                            span{
                                display: inline-block;
                                margin-top: 4px;
                                width:20px;
                                height: 20px;
                                text-align: justify;
                                background-color: #eee;
                                border-radius:3px;
                            }
                        }
                        .task-name{
                            width:150px;
                            font-size:12px;
                            line-height: 18px;
                            word-break:break-all;
                            .tips{
                                padding:0 8px 2px;
                                background-color: #FFAF38;
                                color:#fff;
                                border-radius: 3px;
                            }
                        }
                        .master-info{
                            width:40px;
                            img{
                                width: 40px;
                                height: 40px;
                                border-radius: 50%;
                            }
                        }
                    }
                }
            }
            .task-list:last-child{
                margin-right: 0;
            }
        }

    }
</style>
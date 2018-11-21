<template>
    <div class="demandDetail">
        <div class="btn-box">
            <el-button onclick="javascript:history.go(-1);" class="btn-go_back">返回</el-button>
            <el-button class="fr" v-if="permit && !agreeVisible && notRunning" @click="rejectDemand">不采纳</el-button>
            <el-button class="fr" type="primary" v-if="permit && agreeVisible && notRunning" @click="agreeDemand">采纳</el-button>
            <el-button v-if="isLikeVisible" class="fr" type="primary" @click="like">点赞</el-button>
            <el-button v-if="!isLikeVisible" class="fr" type="primary" @click="dislike">取消点赞</el-button>
            <el-button v-if="permit && compleltedVisible && isOnlieVisible" class="fr" type="primary" @click="online">上线</el-button>
        </div>
        <div class="main">
            <div class="title-box">
                <h2 class="title--name">{{demandDetail.title}}</h2>
                <div class="title-msg">
                    <span>来源：{{demandDetail.origin}}</span>
                    <!--<span>类型：个人建议</span>-->
                    <span v-for="item in typeList" v-if="item.id == demandDetail.type">类型：{{item.name}}</span>
                    <span>提出日期：{{demandDetail.createTime|formatDate}}</span>
                    <span>期待上线时间：{{demandDetail.releaseTime|formatDate}}</span>
                    <span>点赞数：{{demandDetail.likesNum}}</span>
                </div>
            </div>
            <div class="content">
                <table class="table">
                    <tbody>
                    <tr>
                        <td><span>问题</span></td>
                        <td>
                            <ul>
                                <li>{{demandDetail.question}}</li>
                                <!--<li>1、使用知心慧学答题卡，因扫描异常改用第三方扫描，第三方需要上传试卷，导致后台重复标注</li>-->
                                <!--<li>2、第三方考试，文理卷大部分试题相同但因为是截图，需要在题库重复标注</li>-->
                            </ul>
                        </td>
                    </tr>
                    <tr>
                        <td><span>建议</span></td>
                        <td>
                            <ul>
                                <li>{{demandDetail.target}}</li>
                                <!--<li>1、使用知心慧学答题卡，因扫描异常改用第三方扫描，第三方需要上传试卷，导致后台重复标注</li>-->
                                <!--<li>2、第三方考试，文理卷大部分试题相同但因为是截图，需要在题库重复标注</li>-->
                            </ul>
                        </td>
                    </tr>
                    <tr v-if="downloadVisible">
                        <td><span>附件</span></td>
                        <td>
                            <a href="#" class="btn-down" @click.prevent="download">下载附件</a>
                        </td>
                    </tr>
                    </tbody>
                </table>
                    <table class="table">
                        <tbody>
                    <tr>

                        <td>
                            <div>
                                <img src="../assets/img/icon-good.png" alt="">
                                <span>点赞{{demandDetail.likesNum}}</span>
                            </div>
                            <div class="likes-box">
                                <span v-for="item in demandDetail.likesPeople" :class="{'likes-username' : getByteLen(item) > 4 }">{{item}}</span>
                            </div>
                        </td>
                        <td></td>


                    </tr>
                    <tr>

                        <td>
                            <div>
                                <img src="../assets/img/icon-user.png" alt="">
                                <span>已读{{demandDetail.readNum}}</span>
                            </div>
                            <div class="likes-box" >
                                <span v-for="item in demandDetail.readPeople" :class="{'likes-username' : getByteLen(item) > 4 }">{{item}}</span>
                                <!--<span :class="{'likes-username' : getByteLen(testval2) > 4 }">李四王二</span>-->
                                <!--<span :class="{'likes-username' : getByteLen(testval) > 4 }">李四</span>-->
                                <!--<span :class="{'likes-username' : getByteLen(testval) > 4 }">李四</span>-->
                            </div>
                        </td>
                        <td></td>

                    </tr>

                    </tbody>
                </table>
                <div class="comment-box">
                    <div class="comment-tiele">
                        <img src="../assets/img/icon-msg.png" alt="">
                        <span>回复:{{replyNum}}</span>
                    </div>
                    <div class="comment-text_box">
                        <div class="comment-lis" v-for="item in replyList">
                            <div class="comment-lis-title">
                                <span class="comment-lis-title-name">{{item.replyPeople}}</span>
                                <span class="comment-lis-title-date">{{item.replyTime | formatDate2}}</span>
                            </div>
                            <div class="comment-lis-msg">
                                <p>{{item.content}}</p>
                            </div>
                        </div>
                    </div>
                    <div class="comment-input_box">
                        <div>
                            <el-input
                                    v-model="replyContent"
                                    type="textarea"
                                    :rows="2"
                                    placeholder="请输入评论内容"
                                    >
                            </el-input>
                        </div>

                        <div><el-button type="primary" class="btn-reply" @click="reply">回复</el-button></div>

                    </div>

                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import http from '../lib/Http'
    import moment from 'moment'
    import helper from '../lib/Helper'
    export default {
        name: "demandDetail",
        data() {
            return {
                id: 0,
                status:'',
                textarea: '',
                testval:'张三',
                testval2:'张三王二',
                isReadVisible:true,
                isLikeVisible:false,
                demandDetail:{
                    id:'',
                    title:'',
                    origin:'',
                    type:'',
                    createTime:'',
                    releaseTime:'',
                    likesNum:'',
                    question:'',
                    target:'',
                    likesPeople:[],
                    readPeople:[],
                    readNum:''
                },
                replyList:[],
                replyNum:0,
                urlList:[],
                typeList:[
                    {id:-1,name:'全部'},
                    {id:0,name:'个人建议'},
                    {id:1,name:'市场反馈'},
                    {id:2,name:'公司决策'},
                ],
                replyContent:'',
                replyDTO:{
                    demandId:'',
                    content:''
                },
                agreeVisible:false,
                rejectVisible:false,
                notRunning:false,
                compleltedVisible:false,
                isOnlieVisible:true,
                downloadVisible:false,
                demandDetailReqDTO:{}

            }
        },
        beforeMount:function () {
            // this.isRead()
            this.isLike()
            this.isAgree()
            this.isReject()
            this.fetchDetail()
            this.fetchDemandReply()
            this.isRunning()
            this.isCompleleted()
            this.isOnline()
            this.fetchUrls()

        },
        filters: {
            formatDate: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD');
            },
            formatDate2: function (value) {
                if (!value) return '';
                return moment(value).format('YYYY-MM-DD HH:mm:ss');
            },

        },
        computed: {
            permit() {
                let userRole = helper.decodeToken().userRole;
                return userRole < 2;
            },
            getUserName(){
                let userName = helper.decodeToken().userName;
                return userName;
            }
        },
        methods: {
            get() {
                this.id = this.$route.query.id;
                this.status = this.$route.query.status;
            },

            //获取需求附件url
            fetchUrls(){
              http.zsyGetHttp('/feedback/demand/accessory/'+this.id,{},(res)=>{
                  if (res.data != null && res.data.length != 0) {
                      this.downloadVisible = true
                      this.urlList = res.data
                  }
              })
            },
            download(){
                this.urlList.forEach(url =>{
                    /*let frame = document.createElement('iframe')
                          frame.style.display = 'none'
                          document.body.appendChild(frame)
                          frame.src = url*/
                    window.open(url)
                })
            },
            //是否是进行中  进行中需求无法采纳和驳回
            isRunning(){
                if (this.status != 1 && this.status != 2){
                    this.notRunning = true
                }
            },
            //判断是否完成  完成需求可以点击上线
            isCompleleted(){
              if (this.status == 2){
                  this.compleltedVisible = true
              }
            },
            //是否已上线
            isOnline(){
              http.zsyGetHttp('/feedback/demand/is-online/'+this.id,{},(res)=>{
                  if (res.data.onlineTime != null){
                      this.isOnlieVisible = false
                  }
              })
            },
            //点击上线  添加上线时间
            online(){
                http.zsyPostHttp('/feedback/demand/online/'+this.id,{},(res)=>{
                    this.$message({
                        showClose: true,
                        message: '上线成功',
                        type: 'success'
                    });
                })
            },
            //查看当前需求是否已读
            isRead(){
                http.zsyGetHttp('/feedback/demand/is-read/'+this.id,{},(res)=>{
                    if (res.data.count == 0){
                        this.isReadVisible = false
                        this.readDemand()
                    }

                })
            },
            //标记需求已读
            readDemand(){
                http.zsyPostHttp('/feedback/demand/read/'+this.id,{},(res)=> {

                })
            },
            //查看需求是否点赞
            isLike(){
              http.zsyGetHttp('/feedback/demand/is-like/'+this.id,{},(res)=> {
                    if (res.data.count == 0){
                        this.isLikeVisible = true
                    }
              })
            },
            //点赞
            like(){
              http.zsyPostHttp('/feedback/demand/like/'+this.id,{},(res)=> {
                  this.isLikeVisible = false
                  this.$message({
                      showClose: true,
                      message: '点赞成功',
                      type: 'success'
                  });
                  this.fetchDetail()
              })
            },
            //取消点赞
            dislike(){
              http.zsyPostHttp('/feedback/demand/dislike/'+this.id,{},(res)=> {
                  this.isLikeVisible = true
                  this.fetchDetail()
              })
            },
            //获取需求详情
            fetchDetail(){
                this.demandDetailReqDTO.demandId = this.id
                this.demandDetailReqDTO.status = this.status
                console.log(this.demandDetailReqDTO.status)
                http.zsyPostHttp('/feedback/demand/detail',this.demandDetailReqDTO,(res)=> {
                    this.demandDetail = res.data
                })
            },
            //获取需求读取详情
            fetchDemandReply(){
              http.zsyGetHttp('/feedback/demand/reply/'+this.id,{},(res)=> {
                  if (res.data != null && res.data.length != 0){
                      this.replyList = res.data
                      this.replyNum = this.replyList.length
                  }
              })
            },
            //回复
            reply(){
                if (this.replyContent){
                    this.replyDTO.demandId = this.id
                    this.replyDTO.content = this.replyContent
                    http.zsyPostHttp('feedback/demand/reply',this.replyDTO,(res)=>{
                        this.$message({
                            showClose: true,
                            message: '回复成功',
                            type: 'success'
                        });
                        this.replyContent = null
                        this.replyDTO.demandId = null
                        this.replyDTO.content = null
                        this.fetchDemandReply()
                    })


                }
            },
            //查看是否已采纳
            isAgree(){
              http.zsyGetHttp('/feedback/demand/is-agree/'+this.id,{},(res)=>{
                  if (res.data.count == 0){
                      this.agreeVisible = true
                  }else {
                      this.agreeVisible = false
                  }
                })
            },
            //采纳需求
            agreeDemand(){
              http.zsyPostHttp('/feedback/demand/agree/'+this.id,{},(res)=>{
                  this.$message({
                      showClose: true,
                      message: '采纳成功',
                      type: 'success'
                  });
                  this.isAgree()
              })
            },
            //查看需求是否驳回
            isReject(){
              http.zsyGetHttp('/feedback/demand/is-reject/'+this.id,{},(res)=>{
                  if (res.data.count == 0){
                      this.agreeVisible = false
                  }else {
                      this.agreeVisible = true
                  }
              })
            },
            //驳回需求
            rejectDemand(){
              http.zsyPostHttp('/feedback/demand/reject/'+this.id,{},(res)=>{
                  this.$message({
                      showClose: true,
                      message: '已驳回',
                      type: 'success'
                  });
                  this.isReject()
              })
            },

            //获取字符串长度（汉字算两个字符，字母数字算一个）

            getByteLen(val) {
                var len = 0;
                for (var i = 0; i < val.length; i++) {
                    var a = val.charAt(i);
                    if (a.match(/[^\x00-\xff]/ig) != null) {//\x00-\xff→GBK双字节编码范围
                        len += 2;
                    }
                    else {
                        len += 1;
                    }
                }
                return len;
            }
        },
        created() {
            this.get();
        }
    }
</script>

<style scoped>
    .demandDetail {
        width: 1080px;
        margin: 20px auto;
    }

    .btn-go_back {
        color: #34a7fffc;
    }

    .main {
        width: 1080px;
        margin: 20px auto;
        padding: 15px 8px;
        background-color: #fff;
        box-sizing: border-box;
    }

    .title-box {
        padding: 15px;
        background-color: #eff5fb;
    }

    .title--name {
        color: #333;
        font-weight: bold;
        line-height: 28px;
    }

    .title-msg {
        font-size: 0;
    }

    .title-msg > span {
        display: inline-block;
        width: 20%;
        font-size: 13px;
        color: #999;
    }

    .content > table {
        border-collapse: collapse;
        width: 100%;
        background-color: #fff;
        font-size: 14px;
        margin-bottom: 15px;
        line-height: 1.5em;
    }

    .content > table td, .content > table th {
        border-bottom: 1px solid #d8d8d8;
        padding: 15px;
        max-width: 900px;
    }

    .content > table td:first-child {
        color: #999;
    }

    .btn-down {
        color: #009933fc;
    }

    .likes-box span {
        margin-top: 10px;
        margin-right: 10px;
        padding: 2px;
        display: inline-block;
        width: 36px;
        height: 36px;
        line-height: 36px;
        font-size: 13px;
        border-radius: 50%;
        color: #fff;
        background-color: #03cd9a;
        text-align: center;
        overflow: hidden;
    }

    .likes-box .likes-username {
        line-height: 16px;
    }
    .comment-box .comment-lis-title-name{
        font-size: 16px;
        font-weight: bold;
        margin-right: 10px;
    }
    .comment-box .comment-lis-msg{
        color: #999;
    }
    .comment-box .comment-lis{
        margin-top: 20px;
    }
    .comment-input_box{
        margin-top: 15px;
    }
    .btn-reply{
        margin-top: 15px;
    }
    .likes-box{
        height: 95px;
        overflow: auto;
    }
</style>

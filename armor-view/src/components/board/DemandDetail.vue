<template>
    <div>
        <div>
            <el-button type="primary" round="true">返回</el-button>
            <el-button type="primary" round="true" disabled="agreeButtonDisable" @click="agreeDemand(String(demandDatail.id))">采纳</el-button>
            <el-button type="primary" round="true" disabled="rejectButtonDisable" plain>不采纳</el-button>
        </div>
        <div>
            <span>{{demandDatail.title}}</span><br>
            <span>来源:{{demandDatail.origin}}</span>
            <span v-if="demandDatail.type == 0">类型:个人建议</span>
            <span v-if="demandDatail.type == 1">类型:市场反馈</span>
            <span v-if="demandDatail.type == 2">类型:公司决策</span>
            <span>提出日期:{{demandDatail.createTime}}</span>
            <span>期待上传日期:{{demandDatail.releaseTime}}</span>
            <span>点赞数:{{demandDatail.likesNum}}</span>
        </div>
        <div>
            <span>问题</span><span>{{demandDatail.question}}</span>
        </div>
        <div>
            <span>目标</span><span>{{demandDatail.target}}</span>
        </div>
        <div>
            <i class="my-icon-zan"></i>
            <span>点赞</span><span>{{demandDatail.likesNum}}</span>
            <div v-for="item in demandDatail.likesPeople">
                <div style=" width:50px; height:50px; background-color:lightgreen; border-radius:25px;">
                    <span style="height:50px; line-height:50px; display:block; color:#FFF; text-align:center">{{item}}</span>
                </div>
            </div>
        </div>
        <div>
            <i class="my-icon-gerenfill"></i>
            <span>已读</span><span>{{demandDatail.readNum}}</span>
            <div v-for="item in demandDatail.readPeople">
                <div style=" width:50px; height:50px; background-color:lightgreen; border-radius:25px;">
                    <span style="height:50px; line-height:50px; display:block; color:#FFF; text-align:center">{{item}}</span>
                </div>
            </div>
        </div>
        <div>
            <i class="my-icon-xinxi"></i>
            <span>回复</span><span>{{replyList.length}}</span>
            <div v-for="item in replyList">
                <span>{{item.replyPeople}}</span><span>{{item.replyTime}}</span>
                <p>{{item.content}}</p>
            </div>
        </div>
        <div>
            <el-input type="textarea" v-model="replyConment" placeholder="请输入评论内容" clearable
                      resize="horizontal" ></el-input>
        </div>
        <div>
            <el-button type="primary" round="true" disabled="likeDisable" @click="like">点赞</el-button>
            <el-button type="primary" round="true" @click="reply">回复</el-button>
        </div>
    </div>
</template>

<script>
    export default {
        props:{
            demandId:{
                required:true,
                type:String
            }
        },
        name: "DemandDetail",
        data() {
            return{
                demandDatail:{},
                agreeButtonDisable:false,
                rejectButtonDisable:false,
                likeDisable:false,
                replyList:[],
                replyConment:'',
                replyDTO:{
                    demandId:'',
                    conment:''
                }

            }
        },
        beforeMount:function () {
            this.fetchDemandDetail()
            this.fetchDemandReply()
        },

        methods:{
            //查看需求详情
            fetchDemandDetail(){
                http.zsyGetHttp('/feedback/demand/detail/'+this.demandId, {}, (resp) => {
                    this.demandDetail = resp.data
                })
            },

            //采纳需求
            agreeDemand(id){
                http.zsyPostHttp('/feedback/demand/agree/'+id, {}, (resp) => {
                    this.$message({ showClose: true,message: '需求采纳',type: 'success'});
                    this.agreeButtonDisable = true
                    this.rejectButtonDisable = false
                })
            },

            //驳回需求
            rejectDemand(id){
                http.zsyPostHttp('/feedback/demand/reject/'+id, {}, (resp) => {
                    this.$message({ showClose: true,message: '需求驳回',type: 'success'});
                    this.agreeButtonDisable = false
                    this.rejectButtonDisable = true
                })
            },

            //查询回复
            fetchDemandReply(){
                http.zsyGetHttp('/feedback/demand/reply/'+this.demandId, {}, (resp) => {
                    this.replyList = resp.data.list
                })
            },

            //点赞
            like(){
                http.zsyPostHttp('/feedback/demand/like/'+this.demandId, {}, (resp) => {
                    this.$message({ showClose: true,message: '点赞成功',type: 'success'});
                    this.likeDisable = false
                })
            },

            //回复
            reply(){
                if (this.replyConment){
                    this.replyDTO.conment = this.replyConment
                    this.replyDTO.demandId = this.demandId
                }
                http.zsyPostHttp('/feedback/demand/reply', this.replyDTO, (resp) => {
                    this.$message({ showClose: true,message: '点赞成功',type: 'success'});
                })
            },



        }
    }
</script>

<style scoped>

</style>

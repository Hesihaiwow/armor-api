<template>
    <div class="demandDetail">
        <div class="btn-box">
            <el-button onclick="javascript:history.go(-1);" class="btn-go_back">返回</el-button>
            <el-button class="fr">不采纳</el-button>
            <el-button class="fr" type="primary">采纳</el-button>
        </div>
        <div class="main">
            <div class="title-box">
                <h2 class="title--name">第三方试卷上传、标注流程优化</h2>
                <div class="title-msg">
                    <span>来源：上海技术</span>
                    <span>类型：个人建议</span>
                    <span>提出日期：2018-07-31</span>
                    <span>期待上线时间：2018-07-31</span>
                    <span>点赞数：5</span>
                </div>
            </div>
            <div class="content">
                <table class="table">
                    <tbody>
                    <tr>
                        <td><span>问题</span></td>
                        <td>
                            <ul>
                                <li>1、使用知心慧学答题卡，因扫描异常改用第三方扫描，第三方需要上传试卷，导致后台重复标注</li>
                                <li>2、第三方考试，文理卷大部分试题相同但因为是截图，需要在题库重复标注</li>
                            </ul>
                        </td>
                    </tr>
                    <tr>
                        <td><span>建议</span></td>
                        <td>
                            <ul>
                                <li>1、使用知心慧学答题卡，因扫描异常改用第三方扫描，第三方需要上传试卷，导致后台重复标注</li>
                                <li>2、第三方考试，文理卷大部分试题相同但因为是截图，需要在题库重复标注</li>
                            </ul>
                        </td>
                    </tr>
                    <tr>
                        <td><span>附件</span></td>
                        <td>
                            <a href="#" class="btn-down">下载附件</a>
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
                                <span>点赞10</span>
                            </div>
                            <div class="likes-box">
                                <span :class="{'likes-username' : getByteLen(testval) > 4 }">李四</span>
                                <span :class="{'likes-username' : getByteLen(testval2) > 4 }">李四王二</span>
                                <span :class="{'likes-username' : getByteLen(testval) > 4 }">李四</span>
                                <span :class="{'likes-username' : getByteLen(testval) > 4 }">李四</span>
                            </div>
                        </td>
                        <td></td>


                    </tr>
                    <tr>

                        <td>
                            <div>
                                <img src="../assets/img/icon-user.png" alt="">
                                <span>已读109</span>
                            </div>
                            <div class="likes-box">
                                <span :class="{'likes-username' : getByteLen(testval) > 4 }">李四</span>
                                <span :class="{'likes-username' : getByteLen(testval2) > 4 }">李四王二</span>
                                <span :class="{'likes-username' : getByteLen(testval) > 4 }">李四</span>
                                <span :class="{'likes-username' : getByteLen(testval) > 4 }">李四</span>
                            </div>
                        </td>
                        <td></td>

                    </tr>

                    </tbody>
                </table>
                <div class="comment-box">
                    <div class="comment-tiele">
                        <img src="../assets/img/icon-msg.png" alt="">
                        <span>回复2</span>
                    </div>
                    <div class="comment-text_box">
                        <div class="comment-lis">
                            <div class="comment-lis-title">
                                <span class="comment-lis-title-name">冯宝宝</span>
                                <span class="comment-lis-title-date">2017-10-26 18:59:20</span>
                            </div>
                            <div class="comment-lis-msg">
                                <p>这个很重要，我们学校也遇到这种问题</p>
                            </div>
                        </div>
                    </div>
                    <div class="comment-input_box">
                        <div>
                            <el-input
                                    type="textarea"
                                    :rows="2"
                                    placeholder="请输入评论内容"
                                    v-model="textarea">
                            </el-input>
                        </div>

                        <div><el-button type="primary" class="btn-reply">回复</el-button></div>

                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "demandDetail",
        data() {
            return {
                id: 0,
                textarea: '',
                testval:'张三',
                testval2:'张三王二',

            }
        },
        computed: {

        },
        methods: {
            get() {
                this.id = this.$route.query.id;
                console.log(this.id)
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
</style>
<template>
    <div class="bug-details">
            <div class="title">
                <div class="left fl">
                    <span class="name">BUG详情</span>
                </div>
                <div class="right fr">
                    <el-button type="primary" @click="goBack">返回</el-button>
                </div>
            </div>
            <div class="con">
                <div class="left">
                    <div class="item">
                        <p class="name">标题</p>
                        <div class="content">
                            <p class="text" v-show="!bugData.is">标题</p>
                            <el-input v-show="bugData.is" v-model="input" placeholder="请输入内容"></el-input>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">创建时间</p>
                        <div class="content">
                            <p class="text">创建时间</p>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">出现频率</p>
                        <div class="content" v-show="!bugData.is">
                            <p class="text">{{frequencyToWords(1)}}</p>
                        </div>
                        <div class="content" v-show="bugData.is">
                            <el-radio v-model="radio1" label="1" border>固定重现</el-radio>
                            <el-radio v-model="radio1" label="2" border>测试随机</el-radio>
                            <el-radio v-model="radio1" label="3" border>无法重现</el-radio>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">类型</p>
                        <div class="content" v-show="!bugData.is">
                            <p class="text">{{typeToWords(1)}}</p>
                        </div>
                        <div class="content" v-show="bugData.is">
                            <el-radio v-model="radio1" label="1" border>严重错误</el-radio>
                            <el-radio v-model="radio1" label="2" border>主要错误</el-radio>
                            <el-radio v-model="radio1" label="3" border>一般错误</el-radio>
                            <el-radio v-model="radio1" label="4" border>建议</el-radio>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">状态</p>
                        <div class="content">
                            <el-radio v-model="radio1" label="1" border>已分配</el-radio>
                            <el-radio v-model="radio1" label="2" border>已解决</el-radio>
                            <el-radio v-model="radio1" label="3" border>已关闭</el-radio>
                            <el-radio v-model="radio1" label="4" border>打回</el-radio>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">提交人</p>
                        <div class="content">
                            <p class="text">提交人</p>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">处理人</p>
                        <div class="content">
                            <el-select  v-model="value" filterable placeholder="请选择">
                                <el-option
                                        v-for="item in options"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                        </div>
                    </div>
                    <div class="item">
                        <p class="name">处理历史</p>
                        <div class="content">

                        </div>
                    </div>

                </div>
                <div class="right">
                    <div class="text-box">
                        <p class="name">描述与截图</p>
                        <div v-show="!bugData.is" class="text">描述与截图</div>
                        <editor v-show="bugData.is" />
                    </div>
                    <div class="text-box">
                        <p class="name">张三备注</p>
                        <div class="text">55555555</div>
                    </div>
                    <div class="text-box">
                        <p class="name">李四备注</p>
                        <div class="text">6666666666</div>
                    </div>
                    <div class="text-box">
                        <p class="name">添加备注</p>
                        <editor />
                    </div>
                </div>
            </div>
            <div class="btn-box">
                <el-button type="primary">提交</el-button>
                <el-button @click="goBack">取消</el-button>

            </div>
    </div>
</template>

<script>
    import editor from "./Editor.vue";
    export default {
        name: "Details",
        components: { editor },
        data(){
            return {
                bugData:{
                  is:false,
                },
                input:'',
                radio1:'',
                options: [{
                    value: '选项1',
                    label: '黄金糕'
                }, {
                    value: '选项2',
                    label: '双皮奶'
                }, {
                    value: '选项3',
                    label: '蚵仔煎'
                }, {
                    value: '选项4',
                    label: '龙须面'
                }, {
                    value: '选项5',
                    label: '北京烤鸭'
                }],
                value: '',
                content:'',
            }
        },
        methods:{
            goBack(){
                this.$router.go(-1)
            },
            frequencyToWords(val) {
                if (val === 1) {
                    return '固定重现';
                } else if (val === 2) {
                    return '测试随机';
                } else if (val === 3) {
                    return '无法重现';
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
            },

        }
    }
</script>

<style scoped lang="less">
    .bug-details{
        padding: 20px;
        background-color: #fff;
        .title{
            padding-bottom: 20px;
            border-bottom: 1px solid #eee;
            overflow: hidden;
            .name{
                font-size: 16px;
                line-height: 36px;

            }
        }
        .con{
            margin-top: 20px;
            display: flex;
            border: 1px solid #eee;
            border-radius: 5px;
            padding: 20px;
            .left{
                width: 40%;
                .item{
                    margin-bottom: 20px;
                    font-size: 14px;
                    line-height: 30px;
                    .name{
                        font-size: 16px;
                        font-weight: 700;
                    }
                    .el-select{
                        width: 100%;
                    }
                    .text{
                        min-height: 20px;
                        line-height: 36px;
                        padding-left: 13px;
                        padding-right: 13px;
                        margin-bottom: 10px;
                        background-color: #f5f5f5;
                        border: 1px solid #e3e3e3;
                        border-radius: 4px;
                        box-shadow: inset 0 1px 1px rgba(0,0,0,.05);
                    }
                }
            }
            .right{
                margin-left: 20px;
                width: 60%;
                .name{
                    font-size: 16px;
                    line-height: 30px;
                    font-weight: 700;
                }
                .text-box{
                    margin-top: 20px;
                    .text{
                        min-height: 36px;
                        padding: 10px;
                        font-size: 14px;
                        background-color: #f5f5f5;
                        border: 1px solid #e3e3e3;
                        border-radius: 4px;
                        box-shadow: inset 0 1px 1px rgba(0,0,0,.05);
                    }
                }

            }

        }
        .btn-box{
            margin-top: 20px;
            text-align: center;
        }

    }
</style>
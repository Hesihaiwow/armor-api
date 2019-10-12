<template>
    <div class="new-bug">
        <div class="title">
            <div class="left fl">
                <span class="name">新建BUG</span>
            </div>
            <div class="right fr">
                <el-button type="primary" @click="goBack">返回</el-button>
            </div>
        </div>
        <div class="con">
            <div class="left">
                <div class="item">
                    <p class="name">BUG标题</p>
                    <div class="content">
                        <el-input v-model="input" placeholder="请输入内容"></el-input>
                    </div>
                </div>
                <div class="item">
                    <p class="name">出现频率</p>
                    <div class="content">
                        <el-radio v-model="radio1" label="1" border>固定重现</el-radio>
                        <el-radio v-model="radio1" label="2" border>测试随机</el-radio>
                        <el-radio v-model="radio1" label="3" border>无法重现</el-radio>
                    </div>
                </div>
                <div class="item">
                    <p class="name">BUG类型</p>
                    <div class="content">
                        <el-radio v-model="radio1" label="1" border>严重错误</el-radio>
                        <el-radio v-model="radio1" label="2" border>主要错误</el-radio>
                        <el-radio v-model="radio1" label="3" border>一般错误</el-radio>
                        <el-radio v-model="radio1" label="4" border>建议</el-radio>
                    </div>
                </div>
                <div class="item">
                    <p class="name">处理人</p>
                    <div class="content">
                        <el-select v-model="value" filterable placeholder="请选择">
                            <el-option
                                    v-for="item in options"
                                    :key="item.value"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </div>
                </div>

            </div>
            <div class="right">
                <p class="name">描述与截图</p>
                <editor />
            </div>
        </div>
    </div>
</template>

<script>
    import editor from "./Editor.vue";
    export default {
        name: "NewBug",
        components: { editor },
        data(){
          return {
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
            }
        }
    }
</script>

<style scoped lang="less">
.new-bug{
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
                font-size: 16px;
                line-height: 30px;
                .name{
                    font-weight: 700;
                }
                .el-select{
                    width: 100%;
                }
            }
        }
        .right{
            margin-left: 20px;
            .name{
                font-size: 16px;
                line-height: 30px;
                font-weight: 700;
            }
        }

    }
}
</style>
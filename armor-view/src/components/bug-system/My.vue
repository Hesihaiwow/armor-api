<template>
    <div class="my-bugs">
        <div class="list-box">
            <p class="list-title"><span class="high-light-color">未解决的</span> (99)</p>
            <ul class="list">
                <li class="bug-type-1" @click="goList(1)">
                    <span class="bug-id">1234567</span>
                    <span class="bug-title">
                        <p>【学业报告教师端】下载报告设置 默认报表全选</p>
                        <p>PC教师端（阅卷+学业报告） - 2019-12-05 07:36</p>
                    </span>
                </li>
                <li class="bug-type-1">
                    <span class="bug-id">1234567</span>
                    <span class="bug-title">
                        <p>【学业报告教师端】下载报告设置 默认报表全选</p>
                        <p>PC教师端（阅卷+学业报告） - 2019-12-05 07:36</p>
                    </span>
                </li>
                <li class="bug-type-1">
                    <span class="bug-id">1234567</span>
                    <span class="bug-title">
                        <p>【学业报告教师端】下载报告设置 默认报表全选</p>
                        <p>PC教师端（阅卷+学业报告） - 2019-12-05 07:36</p>
                    </span>
                </li>

            </ul>
        </div>
        <div class="list-box">
            <p class="list-title"><span class="high-light-color">已解决的</span> (99)</p>
            <ul class="list">
                <li class="bug-type-1" @click="goList(1)">
                    <span class="bug-id">1234567</span>
                    <span class="bug-title">
                        <p>【学业报告教师端】下载报告设置 默认报表全选</p>
                        <p>PC教师端（阅卷+学业报告） - 2019-12-05 07:36</p>
                    </span>
                </li>
                <li class="bug-type-1">
                    <span class="bug-id">1234567</span>
                    <span class="bug-title">
                        <p>【学业报告教师端】下载报告设置 默认报表全选</p>
                        <p>PC教师端（阅卷+学业报告） - 2019-12-05 07:36</p>
                    </span>
                </li>
                <li class="bug-type-1">
                    <span class="bug-id">1234567</span>
                    <span class="bug-title">
                        <p>【学业报告教师端】下载报告设置 默认报表全选</p>
                        <p>PC教师端（阅卷+学业报告） - 2019-12-05 07:36</p>
                    </span>
                </li>

            </ul>
        </div>
        <div class="list-box">
            <p class="list-title"><span class="high-light-color">打回的</span> (99)</p>
            <ul class="list">
                <li class="bug-type-1" @click="goList(1)">
                    <span class="bug-id">1234567</span>
                    <span class="bug-title">
                        <p>【学业报告教师端】下载报告设置 默认报表全选</p>
                        <p>PC教师端（阅卷+学业报告） - 2019-12-05 07:36</p>
                    </span>
                </li>
                <li class="bug-type-1">
                    <span class="bug-id">1234567</span>
                    <span class="bug-title">
                        <p>【学业报告教师端】下载报告设置 默认报表全选</p>
                        <p>PC教师端（阅卷+学业报告） - 2019-12-05 07:36</p>
                    </span>
                </li>
                <li class="bug-type-1">
                    <span class="bug-id">1234567</span>
                    <span class="bug-title">
                        <p>【学业报告教师端】下载报告设置 默认报表全选</p>
                        <p>PC教师端（阅卷+学业报告） - 2019-12-05 07:36</p>
                    </span>
                </li>

            </ul>
        </div>
        <div class="list-box">
            <p class="list-title"><span class="high-light-color">关闭的</span> (99)</p>
            <ul class="list">
                <li class="bug-type-1" @click="goList(1)">
                    <span class="bug-id">1234567</span>
                    <span class="bug-title">
                        <p>【学业报告教师端】下载报告设置 默认报表全选</p>
                        <p>PC教师端（阅卷+学业报告） - 2019-12-05 07:36</p>
                    </span>
                </li>
                <li class="bug-type-1">
                    <span class="bug-id">1234567</span>
                    <span class="bug-title">
                        <p>【学业报告教师端】下载报告设置 默认报表全选</p>
                        <p>PC教师端（阅卷+学业报告） - 2019-12-05 07:36</p>
                    </span>
                </li>
                <li class="bug-type-1">
                    <span class="bug-id">1234567</span>
                    <span class="bug-title">
                        <p>【学业报告教师端】下载报告设置 默认报表全选</p>
                        <p>PC教师端（阅卷+学业报告） - 2019-12-05 07:36</p>
                    </span>
                </li>

            </ul>
        </div>

    </div>
</template>

<script>
    export default {
        name: "My",
        data(){
            return {

            }
        },
        methods:{
            goList(data){
                console.log(data)
                // this.$router.push({ path: '/index/bug/list', query: { taskId: this.upData.taskId,listType:1,selectAll:this.upData.selectAll,taskName:this.taskName}});
            }
        }
    }
</script>

<style scoped lang="less">
    .high-light-color{
        color: #20a0ff;
    }
    .my-bugs{
        display: flex;
        flex-wrap: wrap;
        justify-content: space-between;
        .list-box{
            width: 540px;
            border: 1px solid #333;
            margin-bottom: 15px;
            .list-title{
                padding-left: 5px;
                line-height: 30px;
                font-size: 16px;
                font-weight: bold;
            }
        }
        .list{
            border: 1px solid #d1dbe5;
            li{
                display: flex;
                line-height: 40px;
                border-bottom: 1px solid #fff;
                cursor: pointer;
                .bug-id{
                    display: inline-block;
                    width: 60px;
                    text-align: center;
                    border-right: 1px solid #fff;
                }
                .bug-title{
                    width: 476px;
                    padding: 0 5px;
                    white-space: nowrap;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    p{
                        font-size: 14px;
                        line-height: 20px;
                    }
                }
            }
        }
    }
    .bug-type-1{
        background-color: #c2dfff;
    }
    .bug-type-2{
        background-color: #c2dfff;
    }
    .bug-type-3{
        background-color: #c2dfff;
    }
    .bug-type-4{
        background-color: #c2dfff;
    }
</style>
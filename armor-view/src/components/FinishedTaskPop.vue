<template>
    <div class="finished-task-pop" v-show="showFinishedTask">
        <div class="finished-task-pop-con">
            <div class="ctpc-top clearfix">
                <span class="fl">完成</span><span class="ctpc-top-close fr" @click="hide">×</span>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">实际消耗</div>
                <div class="ftp-msg fl">
                    <input type="text" class="actual-expend-time" max="2" v-model="form.completeHours">
                    <!--<span class="star-warn">*</span>-->小时
                </div>
            </div>
            <div class="ftp-list clearfix">
                <div class="ftp-menus fl">实际完成时间</div>
                <div class="ftp-msg fl">
                    <el-date-picker v-model="form.completeTime" type="datetime" placeholder="选择日期时间">
                    </el-date-picker>
                </div>
            </div>
            <div class="ctpc-btns">
                <input type="button" class="ctpc-cancel" @click="hide" value="取消">
                <input type="button" class="ctpc-save" @click="finish" value="保存">
            </div>
        </div>
    </div>
</template>
<script>
    import http from '../lib/Http'
    import moment from 'moment';

    moment.locale('zh-cn');
    export default {
        name: 'FinishedTaskPop',
        data() {
            return {
                showFinishedTask: false,
                form: {
                    taskId: '',
                    taskUserId: '',
                    completeHours: '',
                    completeTime: ''
                }
            };
        },
        methods: {
            show(taskId, userId) {
                console.log(`FinishedTaskPop:${taskId}  ${userId}`)
                this.form.taskId = taskId;
                this.form.taskUserId = userId
                this.showFinishedTask = true;
            },
            hide() {
                this.reset()
                this.showFinishedTask = false;
            },
            finish(){
                this.$emit("hi");
                /*if (this.form.completeHours == '') {
                    this.$message.warning("请输入实际消耗");
                    return
                }
                if (this.form.completeTime == '') {
                    this.$message.warning("请选择实际完成时间");
                    return
                }
                var param = this.form
                param.completeTime = moment(param.completeTime).format('YYYY-MM-DD HH:mm:ss')
                http.zsyPutHttp('/task/complete/public', this.form, (resp)=>{
                    this.reset()
                    this.showFinishedTask = false;
                    this.$message.success("操作成功");
                    this.$emit("hi");
                })
*/
            },
            reset(){
                this.form.taskId = '';
                this.form.taskUserId = ''
                this.form.completeTime = ''
                this.form.completeHours = ''
            }
        },
        components: {},
    }
</script>
<style scoped>
    .finished-task-pop {
        position: fixed;
        top: 0;
        left: 0;
        bottom: 0;
        right: 0;
        z-index: 100;
        background: rgba(0, 0, 0, 0.5);
    }

    .finished-task-pop-con {
        position: absolute;
        background: #fff;
        left: 50%;
        top: 50%;
        transform: translate(-50%, -50%);
        width: 536px;
        padding: 16px;
        height: 196px;
    }

    .ctpc-top {
        font-size: 16px;
        line-height: 30px;
        font-weight: 700;
        color: #333;
        margin-bottom: 10px;
    }

    .ctpc-top-close {
        font-size: 30px;
        width: 30px;
        height: 30px;
        line-height: 27px;
        cursor: pointer;
        transition: 0.8s ease all;
        text-align: center;
    }

    .ctpc-top-close:hover {
        color: #36A8FF;
        transform: rotate(360deg);
    }

    .ftp-list {
        margin: 20px 0;
        font-size: 14px;
    }

    .ftp-menus {
        width: 90px;
        text-align: right;
        margin-right: 16px;
        line-height: 30px;
    }

    .actual-expend-time {
        width: 200px;
        height: 30px;
        border: 1px solid #bfcbd9;
        border-radius: 4px;
        text-indent: 6px;
    }

    .star-warn {
        color: red;
        font-size: 18px;
        margin: 0 6px;
    }

    .ctpc-btns {
        text-align: right;
    }

    .ctpc-btns > input {
        display: inline-block;
        width: 80px;
        height: 28px;
        margin-left: 12px;
        cursor: pointer;
        border-radius: 4px;
    }

    .ctpc-cancel {
        background: #fff;
        border: 1px solid #ccc;
    }

    .ctpc-cancel:hover {
        background: #fff;
        border: 1px solid #36A8FF;
        color: #36A8FF;
        font-weight: 700;
    }

    .ctpc-save {
        background: #36A8FF;
        color: #fff;
    }

    .ftp-msg .el-date-editor.el-input {
        width: 200px;
    }


</style>
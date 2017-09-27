<template>
    <el-dialog
            title="上传头像"
            :visible.sync="showUploadAvatarDialog"
            size="tiny"
            :close-on-click-modal="false"
            :close-on-press-escape="false"
            :before-close="hide">
        <el-upload
                class="avatar-uploader"
                action=""
                :show-file-list="false"
                :http-request="upload"
                :before-upload="beforeAvatarUpload">
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>

        <span slot="footer" class="dialog-footer">
    <el-button @click="hide">取 消</el-button>
    <el-button type="primary" @click="saveAvatar">确 定</el-button>
  </span>
    </el-dialog>

</template>
<script>
    import Helper from '../lib/Helper'
    import Http from '../lib/Http'

    export default {
        data() {
            return {
                showUploadAvatarDialog: false,
                imageUrl: ''
            };
        },
        methods: {
            show() {
                this.showUploadAvatarDialog = true
            },
            hide() {
                this.showUploadAvatarDialog = false;
                this.imageUrl = ''
            },
            beforeAvatarUpload(file) {

                const isImage = file.type.indexOf('image') === 0;
                const isLt2M = file.size / 1024 / 1024 < 2;

                if (!isImage) {
                    this.$message({ showClose: true,message: '请选择正确的图片文件',type: 'error'});
                }
                if (!isLt2M) {
                    this.$message({ showClose: true,message: '上传头像图片大小不能超过 2MB',type: 'error'});
                }
                return isImage && isLt2M;
            },
            /**
             * 覆盖默认的上传行为
             */
            upload(file) {
                var data = new FormData();
                data.append('uploadFile', file.file);
                Http.zsyPostHttp('/upload/image', data, (res) => {
                    this.imageUrl = res.data.url;
                })
            },
            saveAvatar() {
                if (this.imageUrl == '') {
                    this.$message({ showClose: true,message: '请选择图片文件',type: 'error'});
                    return false
                }
                let userId = Helper.decodeToken().userId;
                Http.zsyPutHttp(`/user/modifyAvatar`, {userId: userId, url: this.imageUrl}, (res) => {
                    this.$message({ showClose: true,message: '更换头像成功',type: 'success'});
                    this.$router.go(0);
                })
            }
        }
    }
</script>
<style>
    .avatar-uploader {
        text-align: center;
    }

    .avatar-uploader .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
    }

    .avatar-uploader .el-upload:hover {
        border-color: #20a0ff;
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        line-height: 178px;
        text-align: center;
    }

    .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
</style>
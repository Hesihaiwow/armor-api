<template>
    <div class="editor">
        <vue-editor :id='id' :editor-toolbar="customToolbar" useCustomImageHandler @image-added="handleImageAdded" v-model="strHtml"></vue-editor>
    </div>
</template>

<script>
    import { VueEditor } from "vue2-editor";
    import http from '../../lib/Http'
    export default {
        name: "Editor",
        components: { VueEditor },
        props:{
            content:String
        },
        data(){
            return {
                customToolbar:[
                    [{"header": [false,1,2,3,4,5,6]},"bold","underline"],
                    [{"align": "center"}, {"align": "justify"}, {"align": "right"}],
                    [{ list: "ordered" },{indent:"+1"},{indent:"-1"},{"color":[]},{"background":[]}],
                    ["link","image", "code-block"]
                ],
                strHtml: this.content,
                id: this.uniqueId(),
            }
        },
        watch: {
            strHtml(newval) {
                //实时监控编辑器内容变化，使父组件能够实时获取输入内容
                this.$emit('change', newval);
            },
            content(newval) {
                //父组件实时更新数据流向子组件
                this.strHtml = newval
            }
        },
        methods:{
            uniqueId() {
                let rdNum = ('' + Date.now()).slice(-8);
                let str = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_';
                let len = str.length;
                let res = '';
                for (let i = 0; i < 8; i++) {
                    res += str[Math.floor(Math.random() * len)];
                }
                return res + rdNum;
            },
            handleImageAdded(file, Editor, cursorLocation, resetUploader) {

                 // 上传图片操作
                let formData = new FormData();
                formData.append('uploadFile', file);

                http.zsyPostHttp('/upload/ucloud/image', formData, (res) => {
                    Editor.insertEmbed(cursorLocation, "image", res.data.url);
                    resetUploader();
                })

            },
        }

    }
</script>

<style >
   .editor strong{
        font-weight: bold;
    }
</style>
<template>
    <div class="editor">
        <vue-editor v-model="content" :editor-toolbar="customToolbar" />
    </div>
</template>

<script>
    import { VueEditor } from "vue2-editor";
    export default {
        name: "Editor",
        components: { VueEditor },
        data(){
            return {
                customToolbar:[
                    [{'header': [false,1,2,3,4,5,6]},"bold","underline"],
                    [{'align': 'center'}, {'align': 'justify'}, {'align': 'right'}],
                    [{ list: "ordered" },{indent:"+1"},{indent:"-1"},{'color':[]},{'background':[]}],
                    ["link","image", "code-block"]
                ],
                content:'',
            }
        }
    }
</script>

<style >
   .editor strong{
        font-weight: bold;
    }
</style>
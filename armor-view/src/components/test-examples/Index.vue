<template>
    <div class="test-examples">
        <div class="aside">
            <Tree :task-id="taskId"></Tree>
        </div>
        <div class="main">
            <router-view></router-view>
        </div>

    </div>
</template>

<script>

    import Tree from './Tree'

    export default {
        name: "Index",
        components: {
            Tree
        },
        data(){
          return{
              taskId:'',
          }
        },
        watch:{
            '$route': 'getDefaultData'
        },
        methods:{
            getDefaultData(){
                this.taskId=this.$route.query.id;
                // http.zsyGetHttp(`test-example/tree/${this.taskId}`, {}, (res) => {
                //     this.setTree = [];
                //     this.setTree.push( res.data);
                // })
            },
        },
        created () {
            this.getDefaultData();
        }

    }
</script>

<style scoped lang="less">
    .test-examples {
        width: 100%;
        display: flex;
        flex-direction:row;
        flex: 1;
        .aside {
            flex-shrink: 0;
            width: 300px;
            border-right: 2px solid #cccccc;
        }
        .main {
            flex: 1;
            flex-basis: auto;
            padding: 20px;
            background-color: #fff;
        }

    }
</style>
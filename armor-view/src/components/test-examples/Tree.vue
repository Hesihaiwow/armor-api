<template>
    <div class="expand">
        <div>
            <div class="upload-box">
                <el-upload
                        class="upload-demo"
                        ref="record"
                        action=""
                        :http-request="uploadRecordToMysql"
                        accept="application/vnd.ms-excel ,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/msexcel,">
                    <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                    <div slot="tip" class="el-upload__tip">只能上传Excel文件</div>
                </el-upload>
            </div>

            <el-tree ref="expandMenuList" class="expand-tree"
                     :data="setTree"
                     node-key="id"
                     highlight-current
                     :props="defaultProps"
                     :expand-on-click-node="false"
                     :render-content="renderContent"
                     :default-expanded-keys="defaultExpandKeys"
                     :default-expand-all="true"
                     @node-click="handleNodeClick"></el-tree>
        </div>
    </div>
</template>
<!-- VUE饿了么树形控件添加增删改功能按钮 -->
<script>
    import http from '../../lib/Http'
    import TreeRender from './tree_render'

    export default{
        name: 'tree',
        props:{
            taskId:''
        },
        data(){
            return{
                maxexpandId:100 ,//新增节点开始id
                non_maxexpandId: 100,//新增节点开始id(不更改)
                isLoadingTree: false,//是否加载节点树
                setTree: [],//节点树数据
                defaultProps: {
                    children: 'functionTreeResDTOS',
                    label: 'treeName'
                },
                defaultExpandKeys: [],//默认展开节点列表
                // taskId:'',
                fileList:[],
            }
        },
        mounted(){
            // this.initExpand()
        },
        // watch:{
        //     taskId:{
        //         handler(val,oval){
        //             this.getTree();
        //         },
        //         deep:true,
        //     }
        // },
        methods: {
            getDefaultData(){
                http.zsyGetHttp(`test-example/tree/${this.taskId}`, {}, (res) => {
                    this.setTree = [];
                    this.setTree.push( res.data);
                })
            },
            getTree(){
                http.zsyGetHttp(`test-example/tree/${this.taskId}`, {}, (res) => {
                    this.setTree = [];
                    this.setTree.push( res.data);
                })
            },
            setFunctional (name){
                http.zsyPostHttp(`test-example/function/add`, {taskId:this.taskId,name:name}, (res) => {
                    this.getTree();
                })
            },

            initExpand(){
                this.setTree.map((a) => {
                    this.defaultExpandKeys.push(a.id)
                });
                this.isLoadingTree = true;
            },
            handleNodeClick(d,n,s){//点击节点
                d.isEdit = false;//放弃编辑状态
               if(n.level<3){
                   this.$router.push({ path: '/index/testExamples', query: {id:this.taskId}});
                   return false;
               }
                this.$router.push({ path: '/index/testExamples/look', query: {id:this.taskId,exampleId:n.key}});

            },
            renderContent(h,{node,data,store}){//加载节点
                let that = this;
                return h(TreeRender,{
                    props: {
                        DATA: data,
                        NODE: node,
                        STORE: store,
                        maxexpandId: that.non_maxexpandId
                    },
                    on: {
                        nodeAdd: ((s,d,n) => that.handleAdd(s,d,n)),
                        nodeEditPass: ((s,d,n) => that.handleEditPass(s,d,n)),
                        nodeAdde: ((s,d,n) => that.handleAdde(s,d,n)),
                        nodeEdit: ((s,d,n) => that.handleEdit(s,d,n)),
                        nodeDel: ((s,d,n) => that.handleDelete(s,d,n))
                    }
                });
            },
            handleAddTop(){
                this.setTree.push({
                    id: ++this.maxexpandId,
                    name: '新增节点',
                    pid: '',
                    isEdit: false,
                    children: []
                })
            },
            handleAdd(s,d,n){//增加节点
                // console.log(s,d,n)
                if(n.level >=2){
                    this.$message.error("最多只支持3级！")
                    return false;
                }
                //添加数据
                d.functionTreeResDTOS.push({
                    id: ++this.maxexpandId,
                    name: '新增节点',
                    pid: d.id,
                    isEdit: true,
                    functionTreeResDTOS: []
                });
                //展开节点
                if(!n.expanded){
                    n.expanded = true;
                }

            },
            handleEditPass(s,d,n){
                console.log(n.label,3333);
                this.setFunctional(n.label);
            },
            handleAdde(s,d,n){//增加实例
                console.log(n);
                this.$router.push({ path: '/index/testExamples/edit', query: {id:this.taskId, functional: n.key,isAdd:true }});
                // console.log(s,d,n)
                // if(n.level >=3){
                //     this.$message.error("最多只支持3级！")
                //     return false;
                // }
                // //添加数据
                // d.children.push({
                //     id: ++this.maxexpandId,
                //     name: '',
                //     pid: d.id,
                //     isEdit: true,
                //     children: []
                // });
                // //展开节点
                // if(!n.expanded){
                //     n.expanded = true;
                // }
            },
            handleEdit(s,d,n){//编辑节点
                // console.log(s,d,n)
                this.$router.push({ path: '/index/testExamples/edit', query: {id:this.taskId, exampleId: n.key,isAdd:false }});
            },

            handleDelete(s,d,n){//删除节点
                console.log(s,d,n)
                let that = this;
                //有子级不删除
                if(d.functionTreeResDTOS && d.functionTreeResDTOS.length !== 0){
                    this.$message.error("此节点有子级，不可删除！")
                    return false;
                }else{
                    //新增节点直接删除，否则要询问是否删除
                    let delNode = () => {
                        let list = n.parent.data.functionTreeResDTOS || n.parent.data,//节点同级数据
                            _index = 99999;//要删除的index
                        /*if(!n.parent.data.children){//删除顶级节点，无children
                          list = n.parent.data
                        }*/
                        list.map((c,i) => {
                            if(d.id == c.id){
                                _index = i;
                            }
                        })
                        let k = list.splice(_index,1);
                        http.zsyDeleteHttp(`test-example/delete/${n.key}`, {}, (res) => {
                            this.getDefaultData();
                        })


                        //console.log(_index,k)
                        this.$message.success("删除成功！")
                    }
                    let isDel = () => {
                        that.$confirm("是否删除此节点？","提示",{
                            confirmButtonText: "确认",
                            cancelButtonText: "取消",
                            type: "warning"
                        }).then(() => {
                            delNode()
                        }).catch(() => {
                            return false;
                        })
                    }
                    //判断是否新增
                    d.id > this.non_maxexpandId ? delNode() : isDel()

                }
            },
            uploadRecordToMysql(file){
                var data = new FormData();
                data.append('uploadFile', file.file);
                http.zsyPostHttp(`/test-example/import/${this.taskId}`,data,(res)=>{
                    this.$refs.record.clearFiles();
                    if (res.errMsg == "执行成功"){
                        this.fullscreenLoading = false;
                        this.$message({
                            showClose: true,
                            message: '导入成功',
                            type: 'success'
                        });
                        this.getDefaultData();
                    }else {
                        // this.uploadToMysqlVisible = false;
                        // this.fullscreenLoading = false;
                    }
                },(fail)=>{
                    this.$message({
                        showClose: true,
                        message: fail.errMsg,
                        type: 'error'
                    });
                    // this.fullscreenLoading = false;
                })
            },
        },
        created () {
            this.getDefaultData();
        }

    }
</script>

<style>
    .expand{
        width:100%;
        /*height:80%;*/
        overflow:hidden;
    }
    .expand>div{
        height:100%;
        /*padding-top:20px;*/
        width:100%;
        /*margin:20px auto;*/
        /*max-width:400px;*/
        overflow-y:auto;
    }
    .expand>div::-webkit-scrollbar-track{
        box-shadow: inset 0 0 6px rgba(0,0,0,.3);
        border-radius:5px;
    }
    .expand>div::-webkit-scrollbar-thumb{
        background-color:rgba(50, 65, 87, 0.5);
        outline:1px solid slategrey;
        border-radius:5px;
    }
    .expand>div::-webkit-scrollbar{
        width:10px;
    }
    .expand-tree{
        border:none;
        /*margin-top:10px;*/
    }
    .expand-tree .el-tree-node.is-current,
    .expand-tree .el-tree-node:hover{
        overflow:hidden;
    }
    .expand-tree .is-current>.el-tree-node__content .tree-btn,
    .expand-tree .el-tree-node__content:hover .tree-btn{
        display:inline-block;
    }
    .expand-tree .is-current>.el-tree-node__content .tree-label{
        font-weight:600;
        white-space:normal;
    }
    .upload-box{
        padding: 20px 0;
        text-align: center;
        background-color: #fff;
    }

</style>
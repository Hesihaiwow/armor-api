<template>
	<span class="tree-expand">
		<span class="tree-label" v-show="DATA.isEdit">
			<el-input class="edit" size="mini" autofocus
			v-model="DATA.treeName"
			:ref="'treeInput'+DATA.id"
			@click.stop.native="nodeEditFocus"
			@blur.stop="nodeEditPass(STORE,DATA,NODE)"
			></el-input>
		</span>
		<span v-show="!DATA.isEdit" 
		:class="[DATA.id > maxexpandId ? 'tree-new tree-label' : 'tree-label']">
			<span>{{DATA.treeName}}</span>
		</span>
		<span class="tree-btn" v-show="!DATA.isEdit">
			<i v-if="NODE.level == 1" class="el-icon-plus" @click.stop="nodeAdd(STORE,DATA,NODE)" title="新增目录"></i>
			<i v-if="NODE.level==2" class="el-icon-document" @click.stop="nodeAdde(STORE,DATA,NODE)" title="新增实例"></i>
			<i v-if="NODE.level==3" class="el-icon-edit" @click.stop="nodeEdit(STORE,DATA,NODE)" title="修改"></i>
			<i class="el-icon-delete" @click.stop="nodeDel(STORE,DATA,NODE)" title="删除"></i>
		</span>
	</span>
</template>

<script>
	export default{
		name: 'treeExpand',
		props: ['NODE', 'DATA', 'STORE', 'maxexpandId'],
		methods: {
			nodeAdd(s,d,n){//新增子级
				this.$emit('nodeAdd',s,d,n)
			},
            nodeAdde(s,d,n){//新增实例
                this.$emit('nodeAdde',s,d,n);
            },
			nodeEdit(s,d,n){//编辑
				// d.isEdit = true;
				// this.$nextTick(() => {
				// 	this.$refs['treeInput'+d.id].$refs.input.focus()
				// })
				this.$emit('nodeEdit',s,d,n)
			},
			nodeDel(s,d,n){//删除
				this.$emit('nodeDel',s,d,n)
			},
			nodeEditPass(s,d,n){//编辑完成
				d.isEdit = false;
				console.log(n,9)
				if(n.label!=undefined){
                    this.$emit('nodeEditPass',s,d,n)
				}else {
                    this.$emit('nodeDelk',s,d,n)
				}

			},
			nodeEditFocus(){
				//阻止点击节点的事件冒泡
			},
		}
	}
</script>

<style>
	.tree-expand{
		overflow:hidden;
	}
	.tree-expand .tree-label.tree-new{
		font-weight:600;
	}
	.tree-expand .tree-label{
		font-size:0.9em;
	}
	.tree-expand .tree-label .edit{
		width:80%;
	}
	.tree-expand .tree-btn{
		display:none;
		float:right;
		margin-right:20px;
	}
	.tree-expand .tree-btn i{
		color:#8492a6;
		font-size:0.9em;
		margin-right:3px;
	}
</style>
<template>
  <li class="kl-graph-item-wrap">
  	<div class="kl-graph-item clearfix" :class="depthStyle(model.title)">
  		<p class="fl" @click="toggleItem(model.depth)">
  			<i class="iconfont fl" :class="model.childs ? 'icon-triangle-bottom' : 'icon-dian'"></i>
  			<span v-html="deleteBr(model.title==null?model.taskName:model.title)" class="fl" @click="directPlanTask(model)"></span>
  		</p>
  		<div class="fr disc-info" v-show="model.origin!= null">
  			<span class="fl source">需求来源：{{model.origin}}</span>
  			<span class="date fl">截止{{model.endTime}}</span>
  			<span class="fl progress"><em class="fl" :style="'width:'+model.percent+'%'">{{model.percent}}%</em></span>
  		</div>
  		<div class="fr disc-status" v-show="model.origin== null"  v-on="stageIcon(model)">
  			<a href="javascript:;" :class="stage.design">设计</a>
  			<i class="iconfont fl icon-arrow-right"></i>
  			<a href="javascript:;" :class="stage.dev">开发</a>
  			<i class="iconfont fl icon-arrow-right"></i>
  			<a href="javascript:;" :class="stage.test">测试</a>
  			<i class="iconfont fl icon-arrow-right"></i>
  			<a href="javascript:;" :class="stage.publish">发布</a>
  		</div>
  	</div>
  	<ul class="sub-container" v-show="open">
  		<access-tree :model="item" :key="idx"  v-for="(item,idx) in model.childs"></access-tree>
  	</ul>
   
  </li>
</template>
<script>
	export default{
		name:"access-tree",
		props:{
			model: {
		      type: Object,
		      default:{}
		    },
		},
		data(){
			return {
    			open:true,
				stage:{
    			    design:'',
					dev:'',
					test:'',
                    publish:''
				},
    			httpForm:{},
    			emitFlag:true
    		}
		},
		methods:{
			depthStyle(depth) {
				/*class添加*/
		      let klass = "";
		      if(depth!=null){
                  klass = "kl-graph-item-first";
			  }else{
                  klass = "kl-graph-item-second";
				}
//		      switch (depth) {
//		        case "3":
//		          klass = "kl-graph-item-first";
//		          break;
//		        case "4":
//		          klass = "kl-graph-item-second";
//		          break;
//		        case "5":
//		          klass = "kl-graph-item-third";
//		          break;
//		        case "4":
//		          klass = "kl-graph-item-fourth";
//		          break;
//		        default:
//		          klass = "kl-graph-item-first";
//		          break;
//		      }
		      return klass;
		    },
		    deleteBr(name) {
		    	/*去除标签*/
		      return name && name.replace(/<br\/>/g, "");
		    },
			stageIcon(model){
			    var stageName = model.stageName
			    if(model.stageName!=null){
                    if(stageName.indexOf("设计")!=-1){
                        this.stage.design =  "fl playing";
                        this.stage.dev = this.stage.test = this.stage.publish = "fl"
                    }else if(stageName.indexOf("开发")!=-1){
                        this.stage.design = "fl complete"
                        this.stage.dev = "fl playing"
                        this.stage.test = this.stage.publish = "fl"
                    }else if(stageName.indexOf("测试")!=-1){
                        this.stage.design = this.stage.dev = "fl complete"
                        this.stage.test = "fl playing"
                        this.stage.publish = "fl"
                    }else if(stageName.indexOf("发布")!=-1){
                        this.stage.design = this.stage.dev = this.stage.test = "fl complete"
                        this.stage.publish = "fl playing"
                    }
				}

			},
		    toggleItem(depth){
		    	/*展开关闭*/
		        this.open = !this.open;
		    },
            directPlanTask(model){
		        if(model.taskName!=null){
					this.$router.push({name:'taskListFormComments', params:{ taskId:model.id }})
				}
            },
		}
	}
</script>
<style scoped lang="less">
	.kl-graph-item{
		padding:0 10px;
		line-height: 36px;
		&:hover{
			background: #eee;
		}
		&>p{
			cursor: pointer;
		}
		.iconfont{
			margin-right: 5px;
			color: #333;
			&.icon-triangle-bottom{
				margin-top: 3px;
				font-size: 12px;
				line-height: 30px;
			}
		}
		.disc-info{
			span{
				margin-left: 20px;
				&.date{
					margin-top: 8px;
					width: 110px;
					height: 20px;
					text-align: center;
					line-height: 18px;
					border:1px solid #ddd;
					box-sizing: border-box;
				}
				&.progress{
					margin-top: 8px;
					width: 100px;
					height: 20px;
					line-height: 18px;
					background-color: #fff;
					border:1px solid #ddd;
					box-sizing: border-box;
					overflow: hidden;
					em{
						text-align: center;
						background-color: #4298fc;
						color: #fff;
					}
				}
			}
		}
		.disc-status{
			a{
				margin-top: 8px;
				padding:0 10px;
				height: 20px;
				line-height: 20px;
				background-color: #ccc;
				border-radius: 10px;
				color: #333;
				&.complete{
					background-color: #008000;
					color: #fff;
				}
				&.playing{
					background-color: #ff9900;
					color: #fff;
				}
			}
		}
	}

	.kl-graph-item-first {
		background-color: #f2f2f2;
	}

	.kl-graph-item-second {
		padding-left: 30px;
	}
</style>
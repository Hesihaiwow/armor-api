export default {
    //去空格
    getStyle: function( obj,attr ) {
        return obj.currentStyle?obj.currentStyle[attr]:getComputedStyle(obj,false)[attr];
    },
    //解Token
    move: function (obj,json,optional) {


        optional=optional||{};
        optional.time=optional.time||300;
        optional.fn=optional.fn||null;
        optional.type=optional.type||'ease-out';
        
        var start={};
        var dis={};
        for(var key in json){
            start[key]=parseFloat(this.getStyle(obj,key));//初始
            dis[key]=json[key]-start[key]//运动距离{width:100,height:450}
        }
        var count=Math.round(optional.time/30); //总次数
        var n=0;//第几次
        
        clearInterval(obj.timer);
        obj.timer=setInterval(function(){
            n++;//递增
            for(var key in json){//计算每个属性并修改
                switch(optional.type){
                    case 'linear':
                        var a=n/count;
                        var cur=start[key]+dis[key]*a;
                        break;
                    case 'ease-in'://加速
                        var a=n/count;
                        var cur=start[key]+dis[key]*a*a*a;
                        break;
                    case 'ease-out':
                        var a=1-n/count;
                        var cur=start[key]+dis[key]*(1-a*a*a);
                        break;  
                }
                if(key=='opacity'){
                    obj.style.opacity=cur;
                    obj.style.filter='alpha(opacity='+cur*100+')';
                }else{
                    obj.style[key]=cur+'px';
                }   
            }
            if(n==count){//停止条件
                clearInterval(obj.timer);
                optional.fn && optional.fn();
            }
        },30);  
    }
    
}
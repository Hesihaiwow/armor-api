<template>
	<div class="login-con">
		<div class="login-detail">
      <div class="login-title">任务管理系统</div>
      <div class="username-list">
        <img src="../assets/img/account.png" alt="">
        <input type="text" class="username-inp" placeholder="用户名">
      </div> 
      <div class="pwd-list">
        <img src="../assets/img/password.png" alt="">
        <input type="password" class="pwd-inp" placeholder="密码">
      </div> 
      <!-- <div class="forget-pwd"><span @click="goForgetPwd">忘记密码？</span></div>   -->
		  <input type="button" class="login-btn" value="登录" @click="goIndex">
    </div>
	</div>
</template>
<script>
  export default {
    data() {
      return {
        
      };
    },
    methods: {
      goIndex () {
      	this.$router.push('/navTop');
      }
    }
  }
</script>
<style scoped>
.login-con{position: fixed;left: 0;top: 0;bottom: 0;right: 0;background: url(../assets/img/login2.jpg) no-repeat;-webkit-background-size: 100% 100%;
background-size: 100% 100%;}

.login-detail{position: absolute;left: 50%;top: 50%;width: 500px;height: 280px;transform: translate(-50%,-50%);background: hsla(0,0%,100%,.6) border-box;overflow: hidden;border-radius: 1px;box-shadow: 0 0 0 1px hsla(0,0%,100%,.3) inset,  0 1px 10px rgba(0, 0, 0, 0.4);text-shadow: 0 1px 1px hsla(0,0%,100%,.3); 
}
/*.login-detail::before{content: '';position: absolute;top: 0; rightright: 0; bottombottom: 0; left: 0; margin: -30px;z-index: -1; -webkit-filter: blur(20px);filter: blur(20px);} */
.login-title{font-size: 26px;text-align: center;color:#36A8FF;margin: 20px auto;/* text-shadow: 0px 1px 0px #999, 0px 2px 0px #888, 0px 2px 0px #777, 0px 2px 0px #666, 0px 2px 0px #555, 0px 2px 0px #444, 0px 2px 0px #333, 0px 2px 7px #001135; */ }
.username-list,.pwd-list{line-height: 40px;width: 300px;/* border: 1px solid #000; */margin: 20px auto;}
.username-list > img,.username-list > input,.pwd-list > img,.pwd-list > input{display: inline-block;vertical-align: middle;}
.forget-pwd > span{cursor: pointer;}
.username-list > input,.pwd-list > input{
  width: 260px;height: 36px;text-indent: 10px;
}
.username-list > img,.pwd-list > img{margin-right: 12px;}
.login-btn{cursor: pointer;width: 300px;height: 34px;display: block;margin: 30px auto;border-radius: 4px;font-size: 16px;color: #fff;background: #36A8FF;}

</style>
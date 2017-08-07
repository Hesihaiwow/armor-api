<template>
	<div>
		login html
		<input type="button" class="login" value="登录" @click="goIndex">
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
.login{cursor: pointer;}
</style>
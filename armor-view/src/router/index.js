import Vue from 'vue'
import Router from 'vue-router'
import Hello from '@/components/Hello'
import NavTop from '@/components/NavTop'
import Login from '@/components/Login'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    }
  ]
})

import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index'
import Login from '@/components/Login'
import Task from '@/components/Task'
import Project from '@/components/Project'
import Intergral from '@/components/Intergral'
import Organization from '@/components/Organization'
import NavIndex from '@/components/NavIndex'
import IntegralHistory from '@/components/IntegralHistory'


Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login
    },
    {
      path: '/index',
      name: 'Index',
      component: Index,
      children: [
        {
          path: 'task',
          component: Task
        },
        {
          path: 'project',
          component: Project
        },
        {
          path: 'intergral',
          component: Intergral
        },
        {
          path: 'organization',
          component: Organization
        },
        {
          path: 'navIndex',
          component: NavIndex
        },
        {
          path: 'integralHistory',
          component: IntegralHistory
        }
      ]
    }
  ]
})

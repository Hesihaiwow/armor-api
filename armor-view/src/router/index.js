import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index2'
import Login from '@/components/Login'
import Task from '@/components/Task'
import Project from '@/components/Project'
import Demand from '@/components/Demand3'
import Intergral from '@/components/Intergral'
import Organization from '@/components/Organization'
import NavIndex from '@/components/NavIndex2'
import IntegralHistory from '@/components/IntegralHistory'
import Stats from '@/components/Stats2'
import UserComments from '@/components/UserComments'
import Calculate from '@/components/Calculate'
import Plan from '@/components/Plan2'
import demandDetail from '@/components/demandDetail'
import Notice from '@/components/Notice'
import SummaryNav from '@/components/SummaryNav'
import Evaluation from '@/components/Evaluation'


Vue.use(Router)

export default new Router({
    routes: [
        {
            path: '/',
            name: 'Login',
            component: Login
        },
        {
            path: '*', redirect: '/index/navIndex'
        },
        {
            path: '/index',
            name: 'Index',
            component: Index,
            children: [
                {
                    name:'taskList',
                    path: 'task/:userId',
                    component: Task
                },
                {
                    name:'taskListFormComments',
                    path: 'task/id/:taskId',
                    component: Task
                },
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
                },
                {
                  path: 'stats',
                  component: Stats
                },
                {
                    path: 'demand',
                    component: Demand
                },
                {
                    path: 'plan',
                    component: Plan
                },
                {
                    path: 'comments',
                    component: UserComments
                },
                {
                    path: 'calculate',
                    component: Calculate
                },
                {
                    path: 'demandDetail',
                    component: demandDetail
                },
                {
                    path: 'notice',
                    component: Notice
                },
                {
                    path:'summaryNav',
                    component:SummaryNav
                },
                {
                    path:'evaluation',
                    component:Evaluation
                }
            ]
        }
    ]
})

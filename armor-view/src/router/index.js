

import Vue from 'vue'
import Router from 'vue-router'
import Index from '@/components/Index2'
import Login from '@/components/Login'
//manage
import Task from '@/components/Task'
import Project from '@/components/Project'
//manage
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
import UserRestHoursPage from '@/components/UserRestHoursPage'
import TestExamples from '@/components/test-examples'
import TestExamplesEdit from '@/components/test-examples/Edit'
import TestExamplesLook from '@/components/test-examples/Look'
import { cancelArr } from '../lib/Http'

import bug from '@/components/bug-system/Index.vue'

import IndexAdmin from '@/components/IndexAdmin'
import LoginAdmin from '@/components/LoginAdmin'
import NavIndexAdmin from '@/components/NavIndexAdmin'


Vue.use(Router)

const router = new Router({
    routes: [
        {
            path: '/',
            name: 'Login',
            component: Login
        },
        {
            path: '/admin',
            // name: 'LoginAdmin',
            component: LoginAdmin
        },
        {
            path: '/indexAdmin',
            name: 'IndexAdmin',
            component: IndexAdmin,
            children: [
                {
                    name:'NavIndexAdmin',
                    path: 'navIndexAdmin',
                    component: NavIndexAdmin
                },
            ]
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
                },
                {
                    path:'testExamples',
                    component:TestExamples,
                    children:[
                        {
                            path: 'edit',
                            component: TestExamplesEdit
                        },
                        {
                            path: 'look',
                            component: TestExamplesLook
                        }
                    ]
                },
                {
                    path:'UserRestHoursPage',
                    component: UserRestHoursPage
                },
                {
                    path:'bug',
                    component:bug,
                },

            ]
        },
        {
            path: '*', redirect: '/index/navIndex'
        },
    ]
})

router.beforeEach((to, from, next) => {
	cancelArr.forEach((ele,index)=>{
		ele.cancel()
		cancelArr.splice(index,1)
	})
	next()
})

export default router;
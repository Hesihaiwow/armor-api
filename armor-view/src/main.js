// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
// element-ui
import ElementUI from 'element-ui'
import echarts  from 'echarts'
import 'element-ui/lib/theme-default/index.css'
// common css
import './assets/css/base.css'
import './assets/css/iconfont.css'

Vue.use(ElementUI);
Vue.prototype.$echarts = echarts;
Vue.config.productionTip = false;

import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

Vue.use(VueQuillEditor,);

import request from './lib/Http'
Vue.prototype.http = request;

import * as filters from './lib/Filter'
Object.keys(filters).forEach(k => Vue.filter(k, filters[k])) // 注册过滤器


/* eslint-disable no-new */
window.vue = new Vue({
  el: '#app',
  router,
  template: '<App/>',
  data: {
    eventBus: new Vue(),
  },
  components: { App }
});

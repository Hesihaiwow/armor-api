// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
// element-ui
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-default/index.css'
// common css
import './assets/css/base.css'

Vue.use(ElementUI);
Vue.config.productionTip = false;

import request from './lib/Http'
Vue.prototype.http = request;

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

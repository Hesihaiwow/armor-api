webpackJsonp([1],[
/* 0 */,
/* 1 */,
/* 2 */,
/* 3 */,
/* 4 */,
/* 5 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_axios__ = __webpack_require__(192);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_axios___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_axios__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_element_ui__ = __webpack_require__(10);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_element_ui___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1_element_ui__);
// http



/* harmony default export */ __webpack_exports__["a"] = ({
    //请求基础路径
    API_ROOT: function () {
        if (false) {
            return 'http://zsy.dev.xueping.com/armor/api';
        } else if (true) {
            return 'http://zsy.dev.xueping.com/armor2/api';
        } else {
            return 'http://localhost:10012/armor/api';
        }
    },
    //所有请求URI
    API_URI: {
        //用户登陆
        LOGIN: '/user/login',
        //积分列表
        INTEGRAL: '/integral/',
        //项目列表
        PROJECT: '/project/list',
        //添加项目
        ADDPROJECT: '/project/add',
        //个人积分记录
        USERINTEGRAL: '/integral/user'
    },
    //请求方法
    HTTP_METHOD: {
        GET: "get",
        POST: "post",
        PUT: "put",
        DELETE: "delete"
    },
    //请求超时时间15s
    TIME_OUT: 15000,
    //返回数据类型JSON
    RESPONSE_TYPE: 'json',

    //AXIOS请求
    zsyHttpBase(_url, _method, _data = {}, success, fail, _catch) {
        let _this = this;
        //请求全局配置
        let config = {};
        config.baseURL = _this.API_ROOT();
        config.url = _url;
        if (_method && _method == _this.HTTP_METHOD.GET) {
            config.params = _data;
        } else if (_method && (_method == _this.HTTP_METHOD.POST || _method == _this.HTTP_METHOD.PUT)) {
            config.data = _data;
        }
        config.method = _method;
        config.timeout = _this.TIME_OUT;
        config.responseType = _this.RESPONSE_TYPE;
        config.headers = { 'Authorization': "Bearer " + window.localStorage.getItem("token") };
        __WEBPACK_IMPORTED_MODULE_0_axios___default()(config).then(response => {
            let res = response.data;
            if (res.errCode != "00") {
                if (res.errCode == "02") {
                    //重新登录
                    __WEBPACK_IMPORTED_MODULE_1_element_ui__["Message"].warning(res.errMsg);
                    setTimeout(function () {
                        window.vue.$router.push("/");
                        window.localStorage.clear();
                    }, 1000);
                } else {
                    if (typeof fail == "function") {
                        fail(res);
                    } else {
                        if (res.errMsg != "") {
                            __WEBPACK_IMPORTED_MODULE_1_element_ui__["Message"].error(res.errMsg);
                        }
                    }
                }
            } else {
                success && success(res);
            };
        }).catch(err => {
            _catch && _catch(err);
            if (err.message.indexOf("Network Error") != -1) {
                __WEBPACK_IMPORTED_MODULE_1_element_ui__["Message"].error('网络异常,请检查您的网络连接,请稍后重试.');
            } else if (err.message.indexOf("timeout") != -1) {
                __WEBPACK_IMPORTED_MODULE_1_element_ui__["Message"].error('当前网络连接不给力，请稍后重试.');
            } else if (err.message.indexOf("code 503") != -1) {
                __WEBPACK_IMPORTED_MODULE_1_element_ui__["Message"].error('当前服务不可用');
            } else {
                console.debug(err);
                __WEBPACK_IMPORTED_MODULE_1_element_ui__["Message"].error('系统异常');
            }
        });
    },
    // GET请求
    zsyGetHttp(_url, _data = {}, success, fail, _catch) {
        let _this = this;
        _this.zsyHttpBase(_url, _this.HTTP_METHOD.GET, _data, success, fail, _catch);
    },
    // POST请求
    zsyPostHttp(_url, _data = {}, success, fail, _catch) {
        let _this = this;
        _this.zsyHttpBase(_url, _this.HTTP_METHOD.POST, _data, success, fail, _catch);
    },
    // PUT请求
    zsyPutHttp(_url, _data = {}, success, fail, _catch) {
        let _this = this;
        _this.zsyHttpBase(_url, _this.HTTP_METHOD.PUT, _data, success, fail, _catch);
    },
    // DELETE请求
    zsyDeleteHttp(_url, _data = {}, success, fail, _catch) {
        let _this = this;
        _this.zsyHttpBase(_url, _this.HTTP_METHOD.DELETE, _data, success, fail, _catch);
    }
});

/***/ }),
/* 6 */,
/* 7 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony default export */ __webpack_exports__["a"] = ({
    //去空格
    trim: function (text) {
        let rTrim = /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g;
        return text == null ? "" : (text + "").replace(rTrim, "");
    },
    //解Token
    decodeToken: function () {
        let token = window.localStorage.getItem("token");
        if (token == null || token == '') {
            var tmp = {};
            tmp.userName = null;
            tmp.userId = null;
            tmp.userRole = null;
            return tmp;
        }
        let tokens = token.split(".");
        var output = tokens[1].replace(/-/g, '+').replace(/_/g, '/');
        switch (output.length % 4) {
            case 0:
                break;
            case 2:
                output += '==';
                break;
            case 3:
                output += '=';
                break;
            default:
                throw 'Illegal base64url string!';
        }
        let Base64 = __webpack_require__(278).Base64;
        return eval('(' + Base64.decode(output) + ')');
    }

});

/***/ }),
/* 8 */,
/* 9 */,
/* 10 */,
/* 11 */,
/* 12 */,
/* 13 */,
/* 14 */,
/* 15 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(211),
  /* template */
  __webpack_require__(306),
  /* styles */
  null,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\node_modules\\._element-ui@1.4.1@element-ui\\packages\\button\\src\\button.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] button.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-5ce48d69", Component.options)
  } else {
    hotAPI.reload("data-v-5ce48d69", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 16 */,
/* 17 */,
/* 18 */,
/* 19 */,
/* 20 */,
/* 21 */,
/* 22 */,
/* 23 */,
/* 24 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(327)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(219),
  /* template */
  __webpack_require__(307),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-5f256490",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\IntegralHistory.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] IntegralHistory.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-5f256490", Component.options)
  } else {
    hotAPI.reload("data-v-5f256490", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 25 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(325)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(220),
  /* template */
  __webpack_require__(304),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-42ed7ea2",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\Intergral.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] Intergral.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-42ed7ea2", Component.options)
  } else {
    hotAPI.reload("data-v-42ed7ea2", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 26 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(329)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(227),
  /* template */
  __webpack_require__(309),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-65c5fa76",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\Task.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] Task.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-65c5fa76", Component.options)
  } else {
    hotAPI.reload("data-v-65c5fa76", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 27 */,
/* 28 */,
/* 29 */,
/* 30 */,
/* 31 */,
/* 32 */,
/* 33 */,
/* 34 */,
/* 35 */,
/* 36 */,
/* 37 */,
/* 38 */,
/* 39 */,
/* 40 */,
/* 41 */,
/* 42 */,
/* 43 */,
/* 44 */,
/* 45 */,
/* 46 */,
/* 47 */,
/* 48 */,
/* 49 */,
/* 50 */,
/* 51 */,
/* 52 */,
/* 53 */,
/* 54 */,
/* 55 */,
/* 56 */,
/* 57 */,
/* 58 */,
/* 59 */,
/* 60 */,
/* 61 */,
/* 62 */,
/* 63 */,
/* 64 */,
/* 65 */,
/* 66 */,
/* 67 */,
/* 68 */,
/* 69 */,
/* 70 */,
/* 71 */,
/* 72 */,
/* 73 */,
/* 74 */,
/* 75 */,
/* 76 */,
/* 77 */,
/* 78 */,
/* 79 */,
/* 80 */,
/* 81 */,
/* 82 */,
/* 83 */,
/* 84 */,
/* 85 */,
/* 86 */,
/* 87 */,
/* 88 */,
/* 89 */,
/* 90 */,
/* 91 */,
/* 92 */,
/* 93 */,
/* 94 */,
/* 95 */,
/* 96 */,
/* 97 */,
/* 98 */,
/* 99 */,
/* 100 */,
/* 101 */,
/* 102 */,
/* 103 */,
/* 104 */,
/* 105 */,
/* 106 */,
/* 107 */,
/* 108 */,
/* 109 */,
/* 110 */,
/* 111 */,
/* 112 */,
/* 113 */,
/* 114 */,
/* 115 */,
/* 116 */,
/* 117 */,
/* 118 */,
/* 119 */,
/* 120 */,
/* 121 */,
/* 122 */,
/* 123 */,
/* 124 */,
/* 125 */,
/* 126 */,
/* 127 */,
/* 128 */,
/* 129 */,
/* 130 */,
/* 131 */,
/* 132 */,
/* 133 */,
/* 134 */,
/* 135 */,
/* 136 */,
/* 137 */,
/* 138 */,
/* 139 */,
/* 140 */,
/* 141 */,
/* 142 */,
/* 143 */,
/* 144 */,
/* 145 */,
/* 146 */,
/* 147 */,
/* 148 */,
/* 149 */,
/* 150 */,
/* 151 */,
/* 152 */,
/* 153 */,
/* 154 */,
/* 155 */,
/* 156 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkJBODVBMUJBOEQ1NzExRTc5Njg5RjA1MDBFQTQ0QzQ1IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkJBODVBMUJCOEQ1NzExRTc5Njg5RjA1MDBFQTQ0QzQ1Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6QkE4NUExQjg4RDU3MTFFNzk2ODlGMDUwMEVBNDRDNDUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6QkE4NUExQjk4RDU3MTFFNzk2ODlGMDUwMEVBNDRDNDUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz6rtpSkAAADHUlEQVR42rxYzWsTURCfbZpNE1JKmkvxIhU04kVEMBWqoTkUxIsUxIueelCsB/8BT/oPiKCWHnsqfuBFD1r6QYoQ0IOgovUDBVFBk1oTkibZzTqTjLKfyXu7MQM/dt/um5lf5s2bNxvFMAywyHMF+iqHrf4HfZrZhziFmEYcQCT5eQHxGvEY8QCxKWtYkYxQBnEVcUzQfg5xBbEuGqEBQcNDiNuIVQkywHNXWXdIREGE0ChiBXGeIupjeRXWXWFbHaVbDsUQjxBp+4t6A2CnDlDDa1PnXxcCiIQxFCqAGnbYOsq2soiK3whdt5PR0HnxdxuVHQAdx5QFBLqnZ3/fa7rDXppt+loySuBZe1QK2+1rN+kwd5ZtSxO6Zs4Z+rVbJYyEIZ48NJd0bJFS2LYUoYOISfOD7bIcGTMp0rXJJPsQJjRjD39Dc58YimVg9MhLGJs2Wlc3IV2XpZuR2WWWWkO7yU3iexchPn5WKFJkw7bzjstEaL95UPNIYjVxCLTyJ6j9yHcl5GIjJUMoaR40dfdJ5Q834OfTcWiU33Ul5GIj6bdSe9bnemG+54e/F6GCJXEHeuAo1NlHN0JvzIPwYHBCqtPGWxlCOctRHwlOyMVGTobQffOADswgUSLdiPOwvSdD6AViw/xgJI657ZHcevUL1H+9Aq303rkflLauTTbYh1THmOHmSjFXbJnzjMgkhh0F0eAWZE22Y6S2c8GSmGg4OSK2fDSH5rr0RQv/yPjoqWPc6aXdKm+NGzTqgygaVB6IQER1zRmSvKNBk/zqIMWTiId2UuTQw6mX5NlWJWhPTQVsCjHP6y/dgbBu1qsY+vnqqCIuELHmt6QwE56bZd2KiI5sdVmvLU2BMlqC0J6vENr9vXWvRNv9iVFVwSgOg/55DPSPu1r30ct312Qc+Cp35EgrpkB7lur54ercZV5rdu60OSekfEQX7wQ+7WWEmttLiDm+DyRBz/EtxBnEEx5Tp7aESPS6HxIRcj5hIgN8P8Hv+kpomQul298tm/xuuV+EbiFO8HJ1Wkqac7MfOXRRItnn/tu275f8EWAA+9wBIsXQj44AAAAASUVORK5CYII="

/***/ }),
/* 157 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkMwMTM1RkRCOEQ1NzExRTdCRUFCRjdBODFEMjM0RjU4IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkMwMTM1RkRDOEQ1NzExRTdCRUFCRjdBODFEMjM0RjU4Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6QzAxMzVGRDk4RDU3MTFFN0JFQUJGN0E4MUQyMzRGNTgiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6QzAxMzVGREE4RDU3MTFFN0JFQUJGN0E4MUQyMzRGNTgiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz5afCihAAAD8klEQVR42rxYX0hTYRQ/d2vqrptmilmZRUJJEVEEs8gioyKCikgKsl4kioyg3oIiIiF77KWSXoJ6iSzsoR76H0okROCD4Z/MIVardP7ZdnWb2zrn7tPun+/O3U33gx/bd+93zvfjO+c739mEWCwGKjzeDBlFzWfVcEGKblYjDyH3INciC9nzYeRX5EtkC7LHrGOzgnYgryOrDN4vYdyFvIlsRV5Bfkh2AUuS83KQd5HvEojhoYrZ3GU+5kTQIuRb5GmkkEJ4BWb7lvlKK2Qi8gXSpX0hBXwwPuYFye+DcDgkP7PZskB0OMGZtwhy8VODLcxXNZmnKuiWVkwoOAmenwOyIC1CoSCEvEEY9Q6BmOuEkqVlkJWtipSL+TyVSsgogeu0u+Lu6+KK4e2gwdw65tu0oAZlztDODA70QTQa0Wf88iNcBzSXbMhWk1MNZkO2AblN+eDXDzdEI2oxiw+2QP663WDJFuWxv68dfjXXQkT69l8U2pDtilUVStNtbI2OZHfosHIQ8I/DhBRQTVjougEFmw7K3388vQRjnW/AUe6CZcdbdM7IlnwkWmO2HVLVGt/YiD6BR9ww9PEhBD2d4OtoBKm3GXerF8TSdVyH5CPXkad8tN2MINX+BjhJLPU0yZxGQdXVmbDxwPGxxoygQuVgitUZHor2PoCirbVxkYOdcg7xwPFRmPLVIQjG06T+Nhj58gyiQUkOl3N9XVqXv9FKw6pttNmMBWHYfj87BIPNF+Vx8a7z/OOMVTzRGrMJ6lIO7PZc3YTSk5+g4lpMPvqEsDduMl0CdHeQ6NA+6jYjqFU5yFuovxP93e/kT6pDlEelx+7LYwofDxwfrWYEPVUO6LjaRfUujbZfAs+LRpj82w9i2UaIBAPymMKnBdlqjjzhCTdfE7SwrcpqTeXf/b1LV61nTVKrFVZildZcsm0ztU7TwiY6ZZeRM2rJYWlZOVgs1uTF4Fyy0YiJsS7S9OVKbec9VWJiS7GyvEIXPh5oDs0lGw3I5/tU+6EL7BJ0KXeKLkq6m+g6oApMRU8QBPlo02ly5hfwcobQznym3DFSZ7cf+VzbqNGCBosaoZ35ktLtqamA7UQ2KXPKBGLMttqoGKbyq2MCeYaERb05SSthc6uZrTQfv8s+BNuWgOAIg7VEAmuxBIIzDIItGt+KsAViPhtE/ogQ8YgQ89vAfqD//Xz+UIwvjAtNfcuXOdfQF0ajmJ2oUeaEqTXsDx6nfdubwRTyHLKefU8LC9K0p972KPIVG/ciH1EDOdf9UDKgxSsVYoB9r2TvMiroNSuUvL9beti715kSdAe5j4UrUShpzu1M5NBZE8leP2/HPlP4J8AAmYBWic7Fp5IAAAAASUVORK5CYII="

/***/ }),
/* 158 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/u1284.cd2bb65.png";

/***/ }),
/* 159 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAcCAYAAADr9QYhAAAD4UlEQVRIDcVXW2xMURRde2b6oIafSpGID6ESj8aMR0gkRUJDQgSTaqtCJBJfBH805ccP8QwhfHi1NRIhEuItfhBGEQQp4kM0fKBtPOfebZ2py9xrzKOKndw5Z++zH2vvs8+5dwT/icY0aSUUaxn+qfix7GZEWn3/A0u4QTcRyGHGHsZnplrYanCI+flXFD6k/SWALYw3LzmmgtD8GBxIFnZ1PjyqvQo/IxjPQzBPUWQpCpllD/orEh9K1MYQEYwjP5GP3xuHumLbqMgZDEtcDB+q6WAqg4yi4wFiUUJPBJIgf3K9KSOQzGQjlDWY0Ud0kE+wnhsbEUW+8Z5VkMwwEhr0VZYZc1T94TjqCGI1lQuy9J2zGgvYmhbM2EYdSKWj9Gz2+++S4utvt6msSYfairNEO/Dvouj0zqS/pKzM2Kj20zhucmv6/wsgJgbBvPy1MvXqsy00sqFyAkJnH9nY11Vwh0k84/yNxWx5q85hxgszJqV4+guYUClqaTwpo3FnNnHqniCQQ+/74ELLDPnstRt1UC/m52UGw+QfusCUX9ZA+yusY2ZpicFVFQf5bGiulhde5dKTGixqQ4nlR0FAUOldT8WzP6+6wLS3YjoRDkql7MgIoMUGFjdXyTVHZnrMsjCXWzKNsvH4gGJzCWb74mNyVjyOKy4wFFakKwr74ULAQuRGjbQZICMatKRAUM8eW8TAiYvQAZjjeOZerbx2gSGQsjRO9sd8WI4aXv6kUJMuFhubadM7jU12SxZ2GUUXGPL9Ullza7bFqmSVWQvvUb4OsY+npTpTb6Xy5ZVxNy7FauSckbvAMEuuuYmCvQ6QCVHt8cXCSepNcWt1jWOSn+LASsfa1WMM/MZZMCP5i7EFstzMw6e051cLp7oLiPHJDl9xt0oeJOYJ1plxJNL7SazhnxvefK9IB05zWm747iAe5c2xStmX7IuJ/qTwEZ3Mj6HzjoRgbB7146yQeb0PceR/OtLfFlZ8jdePq2fYkOae+EEEYrZxngvxj9XcJwRhMcYKVmR3KusEmO837y4GX5JKqTtkrHLnZbng52Xp9SuJo9obR5n9LO9it/CKD7wst+b7sfFaRD6m8+nTIHZ4gbC5DsQLUcxrfylL+ySdg9+t0a6D9tv5N6SU/VGXCYjxI/zAfsvtCTpO6eRlR0+MeDxb2hMyVQk1Yj51annWyzkWOrrekVvxiT1xhc8JdtuxWETee3XS8RJq0EZ+aM83Sub0EEzF7Sq5lMrI9FZHK0ZybTBL35f/Dgpo28a/Ge9o+6i5BY9Qz5dEF0lGR7Wvz0KUWZfxWNfdqpSdXfT1x2bfAJlGQYx0YpZ9AAAAAElFTkSuQmCC"

/***/ }),
/* 160 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkJFMDVBREQ5OEQ1NzExRTc4Qzg5RDc2QUYwOTFCOEI0IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkJFMDVBRERBOEQ1NzExRTc4Qzg5RDc2QUYwOTFCOEI0Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6QkUwNUFERDc4RDU3MTFFNzhDODlENzZBRjA5MUI4QjQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6QkUwNUFERDg4RDU3MTFFNzhDODlENzZBRjA5MUI4QjQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz5x8QlwAAADkElEQVR42rxYzWsTURCffDQfK4U0MR89WCwWLUX02ApVsQdBvEhBPHkqolj/BD3Zowh6qIpHTyKKFz3U0g/qpUdFSq0gpqbSJqbxkGzSko/OrG/j7tu3zb5NzcCP7L6dmTf75vfmzcbTaDTAKLlcDjop8XjcdO936ec44griImIIEWPjecQKYgbxFrEm69gjuULnEfcRZx36X0LcQyw6XSGvQ8chxFPEvEQwwHTnmW3IiYGTgKKIOcRNWlEX6fUw2znma19pxSEF8R4xzD/Y2dmBsqpqv7VaTRvz+XwQDAYhrCjaLydnmK8xhOo2oEd8MNVqFf4UClogvNAzQqlU0gKK9PSA32+aYpj5vOEmZUTgCX5VsltbwmBEK2ijO8F8Swc0ZeQMvfl2Pg/8riQJ4GpQunghXbIhW45TU7IpO40YNQ4UtrehXq+blHp7e+Ewbls9mHK5DD/X10FV/1GEbMg2nkgYTUfZHJ+crtA4v/y7u7smhe7ubkimUto1BfEb61c4HIaj/f0WZ2QrSN24TMpMtaasWjdFIBCAUrGovX0e05LJZJrjiqJY9AU+zsmkbJBfIV4oCIIukUhE+6USoApeQODjhMwKxYw3ep2xLVa4Ikf6+rTrXxsbQh2Bj5jbSt0ymGMDAxqx9fS1I3YBmbyKtrSeJj0YInU6nbadSOAjL8OhVdrVRgJztQRisVgzTbTLWq0M+eDkq0xA1DZc0G/obOKJGo1Gm9yga/2eZAN5xOsLdt6STMremHqPUMjyhlQEadtX8LeVkG0wZOk+Xss2aEvGak0py2WzlmrdkqRer9aE+bu6jMMf9Von06DdpeOomVs8taPIG4/HeUtEumTDBdNgXaT04Upt53PjALUUiWRSRFBhmkhX0BeRzwW3PbXCOj1rg1apaDwSNmh4pgk4Q7LMN2iyXx1keBnxjg+KJrSZ1E6WmS+13Z46z0rAMyOnJKTBbMfsiqGbo4P29i0KzPfju+NImO4Ys1Wd2Mh+KC6GHz+AeiIF1ZOnoDY4BPVkChrKob+EVEvg3doE3+oK+L98Bm92E4oPpxdkJnD15UoTBeY2ke4zB/5p7RH1yMKcXb9q5ITUHOEXr9o+7WWETt07iEl23Zb427QvIK4hPrD7b4iXiJ6D7oecCE0+YggG2PUIe9bRgGZZoRT93bLGns12KqAniEssXfulknSmO8Gh2xJkn/xv275TsifAANzfXHQ3ifizAAAAAElFTkSuQmCC"

/***/ }),
/* 161 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(323)
  __webpack_require__(324)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(223),
  /* template */
  __webpack_require__(303),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-412fc1af",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\NavIndex.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] NavIndex.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-412fc1af", Component.options)
  } else {
    hotAPI.reload("data-v-412fc1af", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 162 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(318)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(224),
  /* template */
  __webpack_require__(298),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-03088b73",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\Organization.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] Organization.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-03088b73", Component.options)
  } else {
    hotAPI.reload("data-v-03088b73", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 163 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(333)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(225),
  /* template */
  __webpack_require__(313),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-a77519ee",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\Project.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] Project.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-a77519ee", Component.options)
  } else {
    hotAPI.reload("data-v-a77519ee", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 164 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(326)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(226),
  /* template */
  __webpack_require__(305),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-52cc4f8f",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\Stats.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] Stats.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-52cc4f8f", Component.options)
  } else {
    hotAPI.reload("data-v-52cc4f8f", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 165 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(335)
  __webpack_require__(337)
  __webpack_require__(338)
  __webpack_require__(339)
  __webpack_require__(340)
  __webpack_require__(336)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(228),
  /* template */
  __webpack_require__(315),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-e45d7c10",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\TaskItem.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] TaskItem.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-e45d7c10", Component.options)
  } else {
    hotAPI.reload("data-v-e45d7c10", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 166 */,
/* 167 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_vue__ = __webpack_require__(8);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_vue_router__ = __webpack_require__(317);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__components_Index__ = __webpack_require__(294);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__components_Index___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__components_Index__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_Login__ = __webpack_require__(295);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_Login___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__components_Login__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__components_Task__ = __webpack_require__(26);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__components_Task___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4__components_Task__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__components_Project__ = __webpack_require__(163);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__components_Project___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5__components_Project__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__components_Intergral__ = __webpack_require__(25);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__components_Intergral___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_6__components_Intergral__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__components_Organization__ = __webpack_require__(162);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__components_Organization___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_7__components_Organization__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__components_NavIndex__ = __webpack_require__(161);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__components_NavIndex___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_8__components_NavIndex__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__components_IntegralHistory__ = __webpack_require__(24);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__components_IntegralHistory___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_9__components_IntegralHistory__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__components_Stats__ = __webpack_require__(164);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__components_Stats___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_10__components_Stats__);












__WEBPACK_IMPORTED_MODULE_0_vue__["default"].use(__WEBPACK_IMPORTED_MODULE_1_vue_router__["a" /* default */]);

/* harmony default export */ __webpack_exports__["a"] = (new __WEBPACK_IMPORTED_MODULE_1_vue_router__["a" /* default */]({
    routes: [{
        path: '/',
        name: 'Login',
        component: __WEBPACK_IMPORTED_MODULE_3__components_Login___default.a
    }, {
        path: '*', redirect: '/index/navIndex'
    }, {
        path: '/index',
        name: 'Index',
        component: __WEBPACK_IMPORTED_MODULE_2__components_Index___default.a,
        children: [{
            path: 'task',
            component: __WEBPACK_IMPORTED_MODULE_4__components_Task___default.a
        }, {
            path: 'project',
            component: __WEBPACK_IMPORTED_MODULE_5__components_Project___default.a
        }, {
            path: 'intergral',
            component: __WEBPACK_IMPORTED_MODULE_6__components_Intergral___default.a
        }, {
            path: 'organization',
            component: __WEBPACK_IMPORTED_MODULE_7__components_Organization___default.a
        }, {
            path: 'navIndex',
            component: __WEBPACK_IMPORTED_MODULE_8__components_NavIndex___default.a
        }, {
            path: 'integralHistory',
            component: __WEBPACK_IMPORTED_MODULE_9__components_IntegralHistory___default.a
        }, {
            path: 'stats',
            component: __WEBPACK_IMPORTED_MODULE_10__components_Stats___default.a
        }]
    }]
}));

/***/ }),
/* 168 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 169 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 170 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(332)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(213),
  /* template */
  __webpack_require__(312),
  /* styles */
  injectStyle,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\App.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] App.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-a2833670", Component.options)
  } else {
    hotAPI.reload("data-v-a2833670", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 171 */,
/* 172 */,
/* 173 */,
/* 174 */,
/* 175 */,
/* 176 */,
/* 177 */,
/* 178 */,
/* 179 */,
/* 180 */,
/* 181 */,
/* 182 */,
/* 183 */,
/* 184 */,
/* 185 */,
/* 186 */,
/* 187 */,
/* 188 */,
/* 189 */,
/* 190 */,
/* 191 */,
/* 192 */,
/* 193 */,
/* 194 */,
/* 195 */,
/* 196 */,
/* 197 */,
/* 198 */,
/* 199 */,
/* 200 */,
/* 201 */,
/* 202 */,
/* 203 */,
/* 204 */,
/* 205 */,
/* 206 */,
/* 207 */,
/* 208 */,
/* 209 */,
/* 210 */,
/* 211 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

/* harmony default export */ __webpack_exports__["default"] = ({
  name: 'ElButton',

  props: {
    type: {
      type: String,
      default: 'default'
    },
    size: String,
    icon: {
      type: String,
      default: ''
    },
    nativeType: {
      type: String,
      default: 'button'
    },
    loading: Boolean,
    disabled: Boolean,
    plain: Boolean,
    autofocus: Boolean
  },

  methods: {
    handleClick(evt) {
      this.$emit('click', evt);
    }
  }
});

/***/ }),
/* 212 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
//
//
//
//
//

/* harmony default export */ __webpack_exports__["default"] = ({
  name: 'ElTabPane',

  componentName: 'ElTabPane',

  props: {
    label: String,
    labelContent: Function,
    name: String,
    closable: Boolean,
    disabled: Boolean
  },

  data() {
    return {
      index: null
    };
  },

  computed: {
    isClosable() {
      return this.closable || this.$parent.closable;
    },
    active() {
      return this.$parent.currentName === (this.name || this.index);
    }
  },

  mounted() {
    this.$parent.addPanes(this);
  },

  destroyed() {
    if (this.$el && this.$el.parentNode) {
      this.$el.parentNode.removeChild(this.$el);
    }
    this.$parent.removePanes(this);
  },

  watch: {
    label() {
      this.$parent.$forceUpdate();
    }
  }
});

/***/ }),
/* 213 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
//
//
//
//
//
//

/* harmony default export */ __webpack_exports__["default"] = ({
  name: 'app'
});

/***/ }),
/* 214 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(7);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//




/* harmony default export */ __webpack_exports__["default"] = ({
  data() {
    return {
      //部门层级结构数据
      departmentLevel: [],
      addForm: {
        name: '',
        parentId: ''
      },
      showAddPop: false,
      amWarn: ''
    };
  },
  beforeMount() {
    this.initDeptLevelData();
  },
  methods: {
    //初始化部门层级结构数据
    initDeptLevelData() {
      let _this = this;
      __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/dept/level', null, res => {
        _this.departmentLevel = res.data;
      });
    },
    //提示框显示
    show() {
      this.showAddPop = true;
    },
    //提示框隐藏
    hide() {
      this.showAddPop = false;
      this.addForm.name = '';
      this.addForm.parentId = '';
      this.amWarn = '';
    },
    //添加部门
    handleAddDeptBtnClick() {
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.addForm.name) == '') {
        this.amWarn = '部门名称不能为空';
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.addForm.parentId) == '') {
        this.amWarn = '请选择上级部门';
        return;
      }
      __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/dept/add', this.addForm, res => {
        this.hide();
        this.initDeptLevelData();
        this.$emit("handleDeptDataRefresh");
        this.$message.success("添加部门成功");
      });
    }
  }
});

/***/ }),
/* 215 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(7);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//




/* harmony default export */ __webpack_exports__["default"] = ({
  data() {
    return {
      //添加用户表单
      addForm: {
        name: '',
        account: '',
        jobName: '',
        phone: '',
        userRole: '',
        departmentId: ''
      },
      //用户权限手
      options: [{
        value: 0,
        label: '超级管理员'
      }, {
        value: 1,
        label: '项目管理者'
      }, {
        value: 2,
        label: '普通成员'
      }],
      showAddPop: false
    };
  },
  methods: {
    //显示弹框
    show() {
      this.showAddPop = true;
    },
    //隐藏弹框
    hide() {
      this.showAddPop = false;
      this.addForm.name = '';
      this.addForm.account = '';
      this.addForm.jobName = '';
      this.addForm.phone = '';
      this.addForm.userRole = '';
    },
    //部门ID
    setDeptId(deptId) {
      this.addForm.departmentId = deptId;
    },
    //添加用户
    handleAddUserClick() {
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.addForm.name) == '') {
        this.$message.warning("请填写用户名称");
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.addForm.account) == '') {
        this.$message.warning("请填写用户账号");
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.addForm.jobName) == '') {
        this.$message.warning("请填写用户职位");
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.addForm.phone) == '') {
        this.$message.warning("请填写用户手机号");
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.addForm.userRole) == '') {
        this.$message.warning("请选择用户权限");
        return;
      }
      __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/user/add', this.addForm, res => {
        this.hide();
        this.$message.success("用户添加成功");
        this.$emit("handleUserDataRefresh");
      });
    }
  }
});

/***/ }),
/* 216 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Helper__ = __webpack_require__(7);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Http__ = __webpack_require__(5);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//



/* harmony default export */ __webpack_exports__["default"] = ({
    props: {
        ftpList: {
            type: Array
        }
    },
    data() {
        return {
            showAlterPwd: false,
            amWarn: '',
            modifyForm: {
                originalPassword: '',
                newPassword: '',
                sureNewPwd: ''
            },
            button: {
                loading: false,
                btnName: '保存'
            }
        };
    },
    methods: {
        show() {
            this.showAlterPwd = true;
        },
        hide() {
            this.showAlterPwd = false;
            this.amWarn = '';
        },
        //修改用户密码
        handleModifyPwd() {
            let _this = this;
            let oldPwd = __WEBPACK_IMPORTED_MODULE_0__lib_Helper__["a" /* default */].trim(this.modifyForm.originalPassword);
            let newPwd = __WEBPACK_IMPORTED_MODULE_0__lib_Helper__["a" /* default */].trim(this.modifyForm.newPassword);
            let sureNewPwd = __WEBPACK_IMPORTED_MODULE_0__lib_Helper__["a" /* default */].trim(this.modifyForm.sureNewPwd);
            if (oldPwd == '') {
                this.$message.warning("请输入旧密码");
                return;
            }
            if (newPwd == '') {
                this.$message.warning("请输入新密码");
                return;
            }
            if (sureNewPwd == '') {
                this.$message.warning("请再次输入新密码");
                return;
            }
            if (newPwd != sureNewPwd) {
                this.$message.warning("两次输入的新密码不一致");
                return;
            }
            this.button.loading = true;
            this.button.btnName = '保存中';
            __WEBPACK_IMPORTED_MODULE_1__lib_Http__["a" /* default */].zsyPutHttp('/user/password', this.modifyForm, res => {
                this.$message.success("修改成功");
                setTimeout(function () {
                    _this.$router.push("/");
                }, 1000);
            }, fail => {
                this.$message.error(fail.errMsg);
                this.button.loading = false;
                this.button.btnName = '保存';
            }, err => {
                this.button.loading = false;
                this.button.btnName = '保存';
            });
        },
        saveMember() {
            if (this.newDept == '') {
                this.amWarn = '新加部门不能为空';
                return;
            }
            var newDept = {};
            newDept.addNewDept = this.newDept;
            newDept.addFatherDept = this.fatherDept;
            this.$emit("addNewDept", newDept);
            this.showAlterPwd = false;
            this.amWarn = '';
        }
    }
});

/***/ }),
/* 217 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(7);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_moment__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//





__WEBPACK_IMPORTED_MODULE_2_moment___default.a.locale('zh-CN');
/* harmony default export */ __webpack_exports__["default"] = ({
    props: {
        projectList: Array,
        stageList: Array,
        tagList: Array,
        userList: Array
    },
    data() {
        return {
            showCreateTask: false,

            priorityList: [{ label: '普通', value: 1 }, { label: '紧急', value: 2 }, { label: '非常紧急', value: 3 }],
            taskForm: {
                taskType: 2,
                description: '',
                taskName: '',
                endTime: '',
                projectId: '',
                stageId: '',
                priority: '',
                tags: []
            },
            taskUsers: [],
            step: {
                index: '',
                stageName: '',
                userId: '',
                userName: '',
                taskHours: '',
                beginTime: '',
                endTime: '',
                description: ''
            },
            stepTemp: {},
            showDesc: false,
            showAssess: true,
            stageTagShow: false,
            pickerOptions0: {
                disabledDate(time) {
                    return time.getTime() < Date.now() - 8.64e7;
                }
            },
            showAddDetail: false,
            showAddBtn: true,
            valueStage: '',
            showAddStageTag: false
        };
    },
    created() {
        //            this.fetchProjectList()
        //            this.fetchStageList()
        //            this.fetchTagList()
        //            this.fetchUserList()
    },
    computed: {
        insMsgShow() {
            if (this.taskForm.description == '') {
                return '点击添加任务描述';
            } else {
                return this.taskForm.description;
            }
        },
        stepBeginTimeOptions() {
            let endTime = this.taskForm.endTime;
            return {
                disabledDate(time) {
                    return time > endTime;
                }
            };
        },
        stepEndTimeOptions() {
            let endTime = this.taskForm.endTime;
            let beginTime = this.step.beginTime;
            return {
                disabledDate(time) {
                    return time < beginTime || time > endTime;
                }
            };
        }
    },
    filters: {
        getTagName(id, tagList) {
            tagList.forEach(tag => {
                if (tag.id === id) {
                    return tag.name;
                }
            });
        },
        formatDate: function (value) {
            if (!value) return '';
            return __WEBPACK_IMPORTED_MODULE_2_moment___default()(value).format('YYYY-MM-DD');
        }
    },
    methods: {
        showInsChange() {
            this.showDesc = true;
        },
        cancelEdit() {
            this.showDesc = false;
            this.taskForm.description = '';
        },
        saveEdit() {
            this.showDesc = false;
        },
        deleteTag(index) {
            this.tagList.splice(index, 1);
        },
        addTag() {},
        addSelTag() {
            this.showAddStageTag = false;
            for (var i = 0; i < this.value10.length; i++) {
                // debugger;
                var tagLis = {};
                tagLis.tagName = this.value10[i];
                this.tagList.push(tagLis);
                console.log(this.tagList);
                // tagLis.tagName = '';
            }
        },
        // 修改阶段
        modifyStep(index, stages) {
            this.stepTemp = {
                userId: stages[index].userId,
                userName: stages[index].userName,
                taskHours: stages[index].taskHours,
                beginTime: stages[index].beginTime,
                endTime: stages[index].endTime,
                description: stages[index].description
            };
            this.taskUsers.forEach(item => {
                item.cssClass = '';
            });
            this.taskUsers[index].cssClass = 'stepActive';
            this.step = stages[index];
            this.step.index = index;
            this.showAddDetail = true;
        },
        addMember() {
            this.showAddDetail = !this.showAddDetail;
        },
        cancelAddMember() {
            this.showAddDetail = !this.showAddDetail;
            if (this.step.index !== '') {
                this.taskUsers[this.step.index] = this.stepTemp;
                // 取消css
                this.taskUsers[this.step.index].cssClass = '';
            }
            this.step = {
                index: '',
                userId: '',
                userName: '',
                taskHours: '',
                beginTime: '',
                endTime: '',
                description: ''
            };
        },
        saveAddMember() {
            const valid = this.step.userId == '' || this.step.taskHours == '' || this.step.beginTime == '' || this.step.endTime == '';
            if (valid) {
                this.$message.error('请将阶段填写完整');
                return;
            }
            this.showAddDetail = !this.showAddDetail;
            if (this.step.index === '') {
                let taskUser = {};
                taskUser.userId = this.step.userId;
                taskUser.userName = this.step.userName;
                taskUser.beginTime = __WEBPACK_IMPORTED_MODULE_2_moment___default()(this.step.beginTime).format('YYYY-MM-DD');
                taskUser.endTime = __WEBPACK_IMPORTED_MODULE_2_moment___default()(this.step.endTime).format('YYYY-MM-DD');
                taskUser.taskHours = this.step.taskHours;
                taskUser.description = this.step.description;
                this.taskUsers.push(taskUser);
            } else {
                // 取消css
                this.taskUsers[this.step.index].cssClass = '';
            }

            this.step = {
                index: '',
                userId: '',
                userName: '',
                taskHours: '',
                beginTime: '',
                endTime: '',
                description: ''
            };
            this.stepTemp = {};
        },
        stepUserChange(val) {
            let vm = this;
            this.userList.forEach(user => {
                if (user.id === val) {
                    vm.step.userName = user.name;
                }
            });
        },
        stepStageChange(val) {
            let vm = this;
            this.stageList.forEach(stage => {
                if (stage.id === val) {
                    vm.step.stageName = stage.name;
                }
            });
        },
        show() {
            this.showCreateTask = true;
        },
        hide() {
            this.showCreateTask = false;
        },
        deleteMember(index) {
            this.taskUsers.splice(index, 1);
            if (this.taskUsers.length == 0) {
                this.showAddDetail = false;
                this.step = {
                    index: '',
                    userId: '',
                    userName: '',
                    taskHours: '',
                    beginTime: '',
                    endTime: '',
                    description: ''
                };
            }
        },
        addStage() {
            this.showAddStageTag = true;
        },
        deleteStageTag() {},
        fetchProjectList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/project/list', {}, resp => {
                vm.projectList = resp.data;
            });
        },
        fetchStageList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/stage/list', {}, resp => {
                vm.stageList = resp.data;
            });
        },
        fetchUserList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/user/effective', {}, resp => {
                vm.userList = resp.data;
            });
        },
        fetchTagList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/tag/list', {}, resp => {
                vm.tagList = resp.data;
            });
        },
        saveTask() {
            /*   if (this.taskForm.description == '') {
                   this.$message.warning("请填写任务备注");
                   return;
               }*/
            if (this.taskForm.taskName == '') {
                this.$message.warning("请填写任务名称");
                return;
            }
            if (this.taskForm.projectId == '') {
                this.$message.warning("请选择项目");
                return;
            }
            if (this.taskForm.endTime == '') {
                this.$message.warning("请选择结束时间");
                return;
            }
            if (this.taskForm.stageId === '') {
                this.$message.warning("请选择项目阶段");
                return;
            }
            if (this.taskForm.tags.length == 0) {
                this.$message.warning("请选择至少一项标签");
                return;
            }
            /*  if (this.taskUsers.length < 2) {
                  this.$message.warning("至少添加2个成员");
                  return;
              }*/
            let param = this.taskForm;
            param.taskName = param.taskName.trim();
            param.description = param.description.trim();
            param.endTime = __WEBPACK_IMPORTED_MODULE_2_moment___default()(param.endTime).format('YYYY-MM-DD 23:59:59');
            param['taskUsers'] = this.taskUsers;
            param.taskUsers.forEach(user => {
                user.description = user.description.trim();
                user.beginTime = __WEBPACK_IMPORTED_MODULE_2_moment___default()(user.beginTime).format('YYYY-MM-DD HH:mm:ss');
                user.endTime = __WEBPACK_IMPORTED_MODULE_2_moment___default()(user.endTime).format('YYYY-MM-DD 23:59:59');
            });
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/task/create', param, resp => {
                vm.$message.success('任务创建成功');
                vm.taskForm.description = '';
                vm.taskForm.taskName = '';
                vm.taskForm.endTime = '';
                vm.taskForm.projectId = '';
                vm.taskForm.sta = '';
                vm.taskForm.priority = 1;
                vm.taskForm.tags = [];
                vm.taskUsers = [];
                // 刷新看板
                //this.$root.eventBus.$emit("reloadBoard");
                // 刷新列表
                vm.$emit('handleFetchTaskList');
            });
            this.showCreateTask = false;
        }
    }
});

/***/ }),
/* 218 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__Task__ = __webpack_require__(26);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__Task___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__Task__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__Project__ = __webpack_require__(163);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__Project___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__Project__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__Intergral__ = __webpack_require__(25);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__Intergral___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__Intergral__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__Organization__ = __webpack_require__(162);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__Organization___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__Organization__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__NavIndex__ = __webpack_require__(161);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__NavIndex___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4__NavIndex__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__AlterPassword__ = __webpack_require__(292);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__AlterPassword___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5__AlterPassword__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__lib_Helper__ = __webpack_require__(7);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__IntegralHistory__ = __webpack_require__(24);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__IntegralHistory___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_7__IntegralHistory__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__Stats__ = __webpack_require__(164);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__Stats___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_8__Stats__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//











/* harmony default export */ __webpack_exports__["default"] = ({
  data() {
    return {
      activeName: '',
      tabs: [{
        label: '主页',
        name: 'navIndex'
      }, {
        label: '任务',
        name: 'task'
      }, {
        label: '项目',
        name: 'project'
      }, {
        label: '积分',
        name: 'intergral'
      }, {
        label: '统计',
        name: 'stats'
      }],
      showIndex: true,
      showPerOpt: false,
      oldPwd: '',
      newPwd: '',
      sureNewPwd: '',
      showAlterPwd: false
    };
  },
  created() {
    this.activeName = 'navIndex';
    //this.$router.push(`/index/navIndex`);
  },
  beforeMount() {
    //监听子组件传过来的tab选中事件
    this.$root.eventBus.$on("handleTabSelected", val => {
      this.activeName = val;
    });
    if (__WEBPACK_IMPORTED_MODULE_6__lib_Helper__["a" /* default */].decodeToken().userRole < 1) {
      this.tabs.push({
        label: '组织',
        name: 'organization'
      });
    }
  },
  watch: {
    activeName(curVal, oldVal) {
      if (oldVal == null) return;
    }
  },
  computed: {
    //获取用户名称
    getUserName() {
      return __WEBPACK_IMPORTED_MODULE_6__lib_Helper__["a" /* default */].decodeToken().userName;
    }
  },
  methods: {
    handleClick(path) {
      this.showIndex = false;
      /*tab切换*/
      this.$router.push(`/index/${path}`);
    },
    showIndexEvent() {
      // 显示首页
      // this.showIndex = true;
      this.activeName = '';
      this.$router.push(`/index/navIndex`);
    },
    personalOpt() {
      // 个人
      this.showPerOpt = true;
    },
    hidePerOpt() {
      this.showPerOpt = false;
    },
    alterPwd() {
      // 修改密码
      this.showPerOpt = false;
      this.$refs.alterPwdPop.show();
    },
    // 退出登录
    handleLogout() {
      this.showPerOpt = false;
      window.localStorage.clear();
      this.$router.push('/');
    }
  },
  components: {
    Task: __WEBPACK_IMPORTED_MODULE_0__Task___default.a,
    Project: __WEBPACK_IMPORTED_MODULE_1__Project___default.a,
    Intergral: __WEBPACK_IMPORTED_MODULE_2__Intergral___default.a,
    Organization: __WEBPACK_IMPORTED_MODULE_3__Organization___default.a,
    NavIndex: __WEBPACK_IMPORTED_MODULE_4__NavIndex___default.a,
    AlterPassword: __WEBPACK_IMPORTED_MODULE_5__AlterPassword___default.a,
    IntegralHistory: __WEBPACK_IMPORTED_MODULE_7__IntegralHistory___default.a,
    Stats: __WEBPACK_IMPORTED_MODULE_8__Stats___default.a
  }
});

/***/ }),
/* 219 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(7);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_element_ui__ = __webpack_require__(10);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_element_ui___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_element_ui__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_moment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__node_modules_element_ui_packages_button_src_button__ = __webpack_require__(15);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__node_modules_element_ui_packages_button_src_button___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4__node_modules_element_ui_packages_button_src_button__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//







/* harmony default export */ __webpack_exports__["default"] = ({
  components: { ElButton: __WEBPACK_IMPORTED_MODULE_4__node_modules_element_ui_packages_button_src_button___default.a },
  name: 'IntegralHistory',
  data() {
    return {
      TableData: '',
      historyData: [],
      queryForm: {
        startTime: '',
        endTime: ''
      },
      editIntegralVisible: false,
      showTaskCommentDetail: false,
      taskCommentDetail: {},
      taskDetail: {},
      historyPage: {
        layout: "total, pager",
        currentPage: 1,
        pageSize: 10,
        totals: 0,
        pageNum: 0
      },
      integralForm: { //添加积分记录
        userId: '',
        description: '',
        integral: ''
      },
      activeIdx: 0,
      diyStyle: false,
      menuList: [{
        name: '本月'
      }, {
        name: '本季度'
      }, {
        name: '本年'
      }, {
        name: '自定义'
      }],
      rules: {
        integral: [{ required: true, message: '积分不能为空', trigger: 'change', decimal: 10 }],
        description: [{ message: '备注不能超过100字', trigger: 'change', min: 0, max: 100 }]
      }
    };
  },
  computed: {
    permit() {
      let userRole = __WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].decodeToken().userRole;
      return userRole == 0;
    }
  },
  beforeMount: function () {
    this.integralHistory(1);
  },
  methods: {
    commentDetail(index, rows) {
      var taskId = rows[index].taskId;
      __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/task/detail/` + taskId, {}, resp => {
        this.taskDetail = resp.data;

        this.taskDetail.users.forEach(user => {
          if (user.id == rows[index].id) {
            this.taskCommentDetail = user;
            return;
          }
        });
        this.showTaskCommentDetail = true;
      });
    },
    togTable(index) {
      // 点击菜单
      this.activeIdx = index;

      switch (index) {
        case 0:
          this.getDateString("month");
          this.integralHistory(1);
          this.display(index);
          break;
        case 1:
          this.getDateString("quarter");
          this.integralHistory(1);
          this.display(index);
          break;
        case 2:
          this.getDateString("year");
          this.integralHistory(1);
          this.display(index);
          break;
        case 3:
          this.display(index);
          break;
      }
    },
    backButton() {
      this.$router.go(-1);
    },
    tabActive(index) {
      // 颜色变化
      if (index == this.activeIdx) {
        return {
          color: '#36A8FF'
        };
      }
    },
    display(index) {
      if (index != 3) {
        this.diyStyle = false;
      } else {
        if (this.diyStyle == false) {
          this.diyStyle = true;
        } else {
          this.diyStyle = false;
        }
      }
    },
    integralHistory(currentPage) {
      //查询积分历史记录
      this.integralForm.userId = this.$route.query.userId;
      __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/integral/history/' + this.integralForm.userId + '/' + currentPage, this.queryForm, res => {
        let list = res.data.list;
        for (var i = 0; i < list.length; i++) {
          if (list[i].origin != 1) {
            list[i].origin = "手动录入";
            list[i].createTime = this.localeTimeString(list[i].createTime);
          } else {
            list[i].createTime = this.localeTimeString(list[i].createTime);
            if (list[i].type != 1) {
              list[i].origin = "任务系统-多人任务";
            } else {
              list[i].origin = "任务系统-单人任务";
              list[i].grade = "";
            }
          }
        }
        if (this.queryForm.endTime != null && this.queryForm.endTime != "") {
          this.queryForm.endTime = this.localeTimeString(new Date(this.queryForm.endTime).getTime() - 86399000); //之前加入的结束时间减回
        }
        this.historyPage.totals = res.data.total;
        this.historyPage.pageNum = res.data.pages;
        this.historyPage.pageSize = res.data.pagesize;
        this.historyData = res.data.list;
        this.pagingLayout();
      });
    },
    saveIntegralInfo(integralForm) {
      this.$refs[integralForm].validate(valid => {
        if (valid) {
          if (!this.isDecimal(this.integralForm.integral)) {
            __WEBPACK_IMPORTED_MODULE_2_element_ui__["Message"].error("积分格式错误");
            return false;
          }
          __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/integral/add', this.integralForm, res => {
            __WEBPACK_IMPORTED_MODULE_2_element_ui__["Message"].success("积分添加成功");
            this.editIntegralVisible = false;
            this.integralHistory(1);
            this.cancelIntegral();
          });
        } else {
          return false;
        }
      });
    },
    cancelIntegral() {
      this.editIntegralVisible = false;
      this.integralForm.description = '';
      this.integralForm.integral = '';
    },
    pagingLayout() {
      if (this.historyPage.pageNum > 1) {
        this.historyPage.layout = 'total,prev,pager,next';
      } else {
        this.historyPage.layout = 'total,pager';
      }
    },
    //根据自定义时间进行查询
    integralDate() {
      this.queryForm.startTime = this.localeTimeString(this.queryForm.startTime);
      if (this.queryForm.endTime != null && this.queryForm.endTime != "") {
        this.queryForm.endTime = this.localeTimeString(new Date(this.queryForm.endTime).getTime() + 86399000); //结束时间加入23:59:59
      }
      this.integralHistory(1);
    },
    //获取本年,季度,月的开始结束日期
    getDateString(date) {
      let now = new Date();
      let curMonth = now.getMonth();
      let curYear = now.getFullYear();
      ;
      let startMonth = 0;
      if (date == "month") {
        //本月的开始结束时间
        this.queryForm.startTime = this.localeTimeString(new Date(curYear, curMonth, 1));
        this.queryForm.endTime = this.localeTimeString(new Date(curYear, curMonth + 1, 1)); //下月第一天0点
      } else if (date == "quarter") {
        //本季度的开始结束时间
        if (curMonth >= 1 && curMonth <= 3) {
          startMonth = 0;
        } else if (curMonth >= 4 && curMonth <= 6) {
          startMonth = 3;
        } else if (curMonth >= 7 && curMonth <= 9) {
          startMonth = 6;
        } else if (curMonth >= 10 && curMonth <= 12) {
          startMonth = 9;
        }
        this.queryForm.startTime = this.localeTimeString(new Date(curYear, startMonth, 1));
        this.queryForm.endTime = this.localeTimeString(new Date(curYear, startMonth + 3, 1)); //下季度第一天0点
      } else if (date == "year") {
        //本年的开始结束时间
        this.queryForm.startTime = this.localeTimeString(new Date(now.getFullYear(), 0, 1));
        this.queryForm.endTime = this.localeTimeString(new Date(now.getFullYear() + 1, 0, 1));
      }
    },
    isDecimal(str) {
      var regu = /^[-]{0,1}[0-9]{1,}$/;
      if (regu.test(str)) {
        return true;
      }
      var re = /^[-]{0,1}(\d+)[\.]+(\d+)$/;
      if (re.test(str)) {
        if (RegExp.$1 == 0 && RegExp.$2 == 0) return false;
        return true;
      } else {
        return false;
      }
    },
    localeTimeString(time) {
      if (time != null && time != "") {
        time = __WEBPACK_IMPORTED_MODULE_3_moment___default()(time).format('YYYY-MM-DD HH:mm:ss');
        return time;
      } else {
        return "";
      }
    }
  }
});

/***/ }),
/* 220 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_element_ui__ = __webpack_require__(10);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_element_ui___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1_element_ui__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_moment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__lib_Helper__ = __webpack_require__(7);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__node_modules_element_ui_packages_button_src_button__ = __webpack_require__(15);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__node_modules_element_ui_packages_button_src_button___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4__node_modules_element_ui_packages_button_src_button__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__IntegralHistory__ = __webpack_require__(24);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__IntegralHistory___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5__IntegralHistory__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//








__WEBPACK_IMPORTED_MODULE_2_moment___default.a.locale('zh-cn');
/* harmony default export */ __webpack_exports__["default"] = ({
    components: { ElButton: __WEBPACK_IMPORTED_MODULE_4__node_modules_element_ui_packages_button_src_button___default.a },
    name: 'Intergral',
    data() {
        return {
            queryForm: {
                startTime: '',
                endTime: ''
            },
            yearForm: {
                startTime: '',
                endTime: ''
            },
            quarter: [],
            year: [],
            imgUrl: '',
            imgGUrl: '../assets/img/jin.png',
            imgYUrl: '../assets/img/yin.png',
            imgTUrl: '../assets/img/tong.png'
        };
    },
    beforeMount: function () {
        //选中积分tab
        this.$root.eventBus.$emit("handleTabSelected", "intergral");
        this.integralPage("quarter");
        this.integralPage("year");
    },
    methods: {
        clicklHistory(index, rows) {
            //查看积分记录
            this.$router.push({ path: '/index/IntegralHistory', query: { userId: rows[index].userId } });
        },
        //查询积分列表
        integralPage(date) {
            this.getDateString("quarter");
            this.getDateString("year");
            if (date != "year") {
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(__WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].API_URI.INTEGRAL, this.queryForm, res => {
                    this.quarter = res.data;
                });
            } else {
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(__WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].API_URI.INTEGRAL, this.yearForm, res => {
                    this.year = res.data;
                });
            }
        },
        //获取本年,季度,月的开始结束日期
        getDateString(date) {
            let now = new Date();
            let curMonth = now.getMonth();
            let curYear = now.getFullYear();
            ;
            let startMonth = 0;
            if (date == "month") {
                //本月的开始结束时间
                this.queryForm.startTime = this.localeTimeString(new Date(curYear, curMonth, 1));
                this.queryForm.endTime = this.localeTimeString(new Date(curYear, curMonth + 1, 1)); //下月第一天0点
            } else if (date == "quarter") {
                //本季度的开始结束时间
                if (curMonth >= 1 && curMonth <= 3) {
                    startMonth = 0;
                } else if (curMonth >= 4 && curMonth <= 6) {
                    startMonth = 3;
                } else if (curMonth >= 7 && curMonth <= 9) {
                    startMonth = 6;
                } else if (curMonth >= 10 && curMonth <= 12) {
                    startMonth = 9;
                }
                this.queryForm.startTime = this.localeTimeString(new Date(curYear, startMonth, 1));
                this.queryForm.endTime = this.localeTimeString(new Date(curYear, startMonth + 3, 1)); //下季度第一天0点
            } else if (date == "year") {
                //本年的开始结束时间
                this.yearForm.startTime = this.localeTimeString(new Date(now.getFullYear(), 0, 1));
                this.yearForm.endTime = this.localeTimeString(new Date(now.getFullYear() + 1, 0, 1));
            }
        },
        //时间字符串格式化
        localeTimeString(time) {
            if (time != null && time != "") {
                time = __WEBPACK_IMPORTED_MODULE_2_moment___default()(time).format('YYYY-MM-DD HH:mm:ss');
                return time;
            } else {
                return "";
            }
        }
    }
});

/***/ }),
/* 221 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_element_ui__ = __webpack_require__(10);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_element_ui___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_element_ui__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(7);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__lib_Http__ = __webpack_require__(5);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//





/* harmony default export */ __webpack_exports__["default"] = ({
    data() {
        return {
            loginForm: {
                account: '',
                password: ''
            },
            button: {
                loading: false
            }
        };
    },
    methods: {
        login() {
            let _this = this;
            if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(_this.loginForm.account) == '') {
                __WEBPACK_IMPORTED_MODULE_0_element_ui__["Message"].warning('请输入用户名');
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(_this.loginForm.password) == '') {
                __WEBPACK_IMPORTED_MODULE_0_element_ui__["Message"].warning('请输入密码');
                return;
            }
            _this.button.loading = true;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyPostHttp(__WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].API_URI.LOGIN, _this.loginForm, res => {
                if (res.errCode != '00') {
                    _this.button.loading = false;
                    __WEBPACK_IMPORTED_MODULE_0_element_ui__["Message"].error(res.errMsg);
                } else {
                    _this.button.btnName = '登录成功,跳转中...';
                    window.localStorage.setItem("token", res.data);
                    _this.$router.push('/index/navIndex');
                }
            }, res => {
                _this.button.loading = false;
                __WEBPACK_IMPORTED_MODULE_0_element_ui__["Message"].error(res.errMsg);
            }, e => {
                _this.button.loading = false;
            });
        }
    },
    created() {
        if (window.localStorage.getItem("token") != null && window.localStorage.getItem("token") !== '') {
            this.$router.push('/index/navIndex');
        }
    },
    mounted() {}
});

/***/ }),
/* 222 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(7);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//




/* harmony default export */ __webpack_exports__["default"] = ({
  data() {
    return {
      //添加用户表单
      modifyForm: {
        userId: '',
        name: '',
        account: '',
        jobName: '',
        phone: '',
        userRole: '',
        departmentId: ''
      },
      //用户权限手
      options: [{
        value: 0,
        label: '超级管理员'
      }, {
        value: 1,
        label: '项目管理者'
      }, {
        value: 2,
        label: '普通成员'
      }],
      showAddPop: false
    };
  },
  methods: {
    //显示弹框
    show() {
      __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/user/${this.modifyForm.userId}`, null, res => {
        this.modifyForm.userId = res.data.id;
        this.modifyForm.name = res.data.name;
        this.modifyForm.account = res.data.account;
        this.modifyForm.jobName = res.data.jobName;
        this.modifyForm.phone = res.data.phone;
        this.modifyForm.userRole = res.data.userRole;
        this.modifyForm.departmentId = res.data.departmentId;
        this.showAddPop = true;
      });
    },
    //隐藏弹框
    hide() {
      this.showAddPop = false;
      this.modifyForm.userId = '';
      this.modifyForm.name = '';
      this.modifyForm.account = '';
      this.modifyForm.jobName = '';
      this.modifyForm.phone = '';
      this.modifyForm.userRole = '';
    },
    //部门ID
    setUserId(userId) {
      this.modifyForm.userId = userId;
    },
    //修改用户
    handleModifyUserClick() {
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.modifyForm.name) == '') {
        this.$message.warning("请填写用户名称");
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.modifyForm.account) == '') {
        this.$message.warning("请填写用户账号");
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.modifyForm.jobName) == '') {
        this.$message.warning("请填写用户职位");
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.modifyForm.phone) == '') {
        this.$message.warning("请填写用户手机号");
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.modifyForm.userRole) == '') {
        this.$message.warning("请选择用户权限");
        return;
      }
      __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp(`/user/${this.modifyForm.userId}`, this.modifyForm, res => {
        this.hide();
        this.$message.success("用户添加成功");
        this.$emit("handleUserDataRefresh");
      });
    }
  }
});

/***/ }),
/* 223 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__TaskItem__ = __webpack_require__(165);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__TaskItem___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__TaskItem__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__Intergral_vue__ = __webpack_require__(25);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__Intergral_vue___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__Intergral_vue__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__lib_Http__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__lib_Helper__ = __webpack_require__(7);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_moment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__node_modules_element_ui_packages_tabs_src_tab_pane_vue__ = __webpack_require__(289);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__node_modules_element_ui_packages_tabs_src_tab_pane_vue___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5__node_modules_element_ui_packages_tabs_src_tab_pane_vue__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//









__WEBPACK_IMPORTED_MODULE_4_moment___default.a.locale('zh-cn');

/* harmony default export */ __webpack_exports__["default"] = ({
    name: 'NavIndex',
    data() {
        var validateEmpty = (rule, value, callback) => {
            if (value.trim() == '') {
                callback(new Error());
            } else {
                callback();
            }
        };
        return {
            activeName: 'doing',
            assessActiveName: 'waitAssess',
            auditTabsActiveName: 'wait',
            createTaskVisible: false,
            commentedPage: {
                pageNum: 1,
                pageSize: 5,
                total: 0
            },
            finishedPage: {
                pageNum: 1,
                pageSize: 5,
                total: 0
            },
            auditSuccessPage: {
                pageNum: 1,
                pageSize: 5,
                total: 0
            },
            pickerOptions0: {
                disabledDate(time) {
                    return time.getTime() < Date.now() - 8.64e7;
                }
            },
            taskForm: {
                taskName: '',
                description: '',
                projectId: '',
                endTime: '',
                priority: 1,
                tags: [],
                taskType: 1,
                taskHours: '',
                stageId: ''
            },
            rules: {
                projectId: [{ required: true, message: '项目不能为空', trigger: 'change' }],
                endTime: [{ type: 'date', required: true, message: '截止时间不能为空', trigger: 'change' }],
                taskHours: [{ required: true, validator: validateEmpty, message: '工作量不能为空', trigger: 'blur' }],
                taskName: [{ required: true, validator: validateEmpty, message: '任务名称不能为空', trigger: 'blur' }],
                description: [{ required: true, validator: validateEmpty, message: '描述不能为空', trigger: 'blur' }],
                stageId: [{ required: true, message: '阶段不能为空', trigger: 'change' }],
                tags: [{ type: 'array', required: true, message: '请至少选择一个标签', trigger: 'change' }]
            }, task: {
                doing: [],
                finished: [],
                waitAssess: [],
                commented: [],
                waitAudit: [],
                auditSuccess: [],
                applyFail: []
            },
            projectList: [],
            stageList: [],
            tagList: [],
            userList: [],
            integralItem: [{
                label: '本周',
                score: ''
            }, {
                label: '本月',
                score: ''
            }, {
                label: '年度总积分',
                score: ''
            }, {
                label: '季度积分排名',
                score: ''
            }, {
                label: '年度积分排名',
                score: ''
            }]
        };
    },
    created() {
        this.reload();
    },
    //数据初始化
    beforeMount() {
        let _this = this;
        //选中组织tab
        _this.$root.eventBus.$emit("handleTabSelected", "navIndex");
    },
    computed: {
        permit() {
            let userRole = __WEBPACK_IMPORTED_MODULE_3__lib_Helper__["a" /* default */].decodeToken().userRole;
            return userRole <= 1;
        },
        userRole() {
            let userRole = __WEBPACK_IMPORTED_MODULE_3__lib_Helper__["a" /* default */].decodeToken().userRole;
            return userRole;
        },
        finishedPageLayout() {
            if (this.finishedPage.total > 0) {
                return 'total, prev, pager, next';
            }
            return 'total, pager';
        },
        commentedPageLayout() {
            if (this.commentedPage.total > 0) {
                return 'total, prev, pager, next';
            }
            return 'total, pager';
        },
        auditSuccessPageLayout() {
            if (this.auditSuccessPage.total > 0) {
                return 'total, prev, pager, next';
            }
            return 'total, pager';
        }
    },
    methods: {
        handleClick(tab, event) {
            // 点击进行中和已完成
            console.log(tab, event);
        },
        createTaskClick() {
            // 建个人任务
            this.createTaskVisible = true;
        },
        reload() {
            this.fetchIntegral();
            this.fetchTaskDoing();
            this.fetchTaskFinished();
            this.fetchTaskWaitAssess();
            this.fetchTaskCommented();
            this.fetchTaskWaitAudit();
            this.fetchProjectList();
            this.fetchStageList();
            this.fetchTagList();
            this.fetchUserList();
            //this.fetchApplyFailTask();
            if (this.userRole === 0) {
                // 所有审核通过的数据
                this.fetchTaskAuditSuccess();
            }
        },
        saveTaskInfo(formName) {
            let vm = this;
            this.$refs[formName].validate(valid => {
                if (valid) {
                    let userId = __WEBPACK_IMPORTED_MODULE_3__lib_Helper__["a" /* default */].decodeToken().userId;
                    var param = this.taskForm;
                    param.taskName = param.taskName.trim();
                    param.endTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(param.beginTime).format('YYYY-MM-DD 23:59:59');
                    var taskUsers = [{
                        userId: userId,
                        taskHours: param.taskHours.trim(),
                        beginTime: __WEBPACK_IMPORTED_MODULE_4_moment___default()().format('YYYY-MM-DD HH:mm:ss'),
                        endTime: __WEBPACK_IMPORTED_MODULE_4_moment___default()(param.endTime).format('YYYY-MM-DD 23:59:59'),
                        description: param.description.trim()
                    }];
                    param['taskUsers'] = taskUsers;
                    __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyPostHttp('/task/create', param, resp => {
                        vm.$message.success('任务创建成功');
                        this.$refs[formName].resetFields();
                        this.createTaskVisible = false;
                        vm.reload();
                    });
                } else {
                    return false;
                }
            });
        },
        makeUpItems(items) {
            const list = items;
            list.forEach(el => {
                let endTime = '',
                    today = __WEBPACK_IMPORTED_MODULE_4_moment___default()().format('YYYY-MM-DD');
                if (el.status >= 2) {
                    endTime = el.completeTime;
                } else if ((el.reviewStatus == 1 || el.reviewStatus == 3) && el.taskUsers[0].status == 1) {
                    endTime = el.endTime;
                } else {
                    endTime = el.taskUsers[0].completeTime;
                }
                endTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(endTime).format('YYYY-MM-DD');
                const diffDays = __WEBPACK_IMPORTED_MODULE_4_moment___default()(today).diff(__WEBPACK_IMPORTED_MODULE_4_moment___default()(endTime), 'days');
                //const diffDays = Math.round(moment().diff(moment(endTime), 'days', true))
                let endColor = '',
                    endText = '';
                endText = __WEBPACK_IMPORTED_MODULE_4_moment___default()(endTime).calendar(null, {
                    sameDay: '[今天]',
                    nextDay: '[明天]',
                    nextWeek: 'L',
                    lastDay: '[昨天]',
                    lastWeek: 'L',
                    sameElse: 'L'
                });
                if (el.status < 3 && el.taskUsers[0].status == 1) {
                    if (diffDays == 0) {
                        endColor = 'orange';
                    } else if (diffDays > 0) {
                        endColor = 'red';
                    } else if (diffDays < 0) {
                        endColor = 'blue';
                    }
                    endText += ' 截止';
                } else {
                    endColor = 'green';
                    endText += ' 完成';
                }
                el['endColor'] = endColor;
                el['endText'] = endText;
            });
            return list;
        },
        fetchIntegral() {
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp(__WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].API_URI.USERINTEGRAL, null, res => {
                let data = res.data;
                let items = [];
                items.push({ label: '本周 ', score: data.week, time: this.getDateString('week') });
                items.push({ label: '本月', score: data.month, time: this.getDateString('month') });
                items.push({ label: '本年', score: data.year, time: this.getDateString('year') });
                items.push({ label: '季度积分排名', score: data.quarterRank, time: this.getDateString('quarter') });
                items.push({ label: '年度积分排名', score: data.yearRank, time: this.getDateString('year') });
                this.integralItem = items;
            });
        },
        // 获取用户正在进行的任务
        fetchTaskDoing() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/task/doing', {}, resp => {
                vm.task.doing = this.makeUpItems(resp.data);
                vm.fetchMyTaskWaitAudit();
            });
        },
        // 获取用户已完成的任务
        fetchTaskFinished() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp(`/task/finished/${vm.finishedPage.pageNum}`, {}, resp => {
                vm.finishedPage.pageNum = resp.data.pageNum;
                vm.finishedPage.total = resp.data.total;
                vm.task.finished = this.makeUpItems(resp.data.list);
            });
        },
        // 获取用户被打回任务
        fetchApplyFailTask() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/task/apply/fail', {}, resp => {
                vm.task.applyFail = this.makeUpItems(resp.data);
            });
        },
        // 获取用户待评价的任务
        fetchTaskWaitAssess() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/task/waitAssess', {}, resp => {
                vm.task.waitAssess = this.makeUpItems(resp.data);
            });
        },
        // 获取用户已评价的任务
        fetchTaskCommented() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp(`/task/commented`, {}, resp => {
                vm.task.commented = this.makeUpItems(resp.data);
            });
        },
        // 获取所有待审核的任务
        fetchTaskWaitAudit() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/task/pending/all', {}, resp => {
                vm.task.waitAudit = this.makeUpItems(resp.data);
            });
        },
        // 获取所有审核通过的任务
        fetchTaskAuditSuccess() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp(`/task/audit/success/all/${vm.auditSuccessPage.pageNum}`, {}, resp => {
                vm.auditSuccessPage.pageNum = resp.data.pageNum;
                vm.auditSuccessPage.total = resp.data.total;
                vm.task.auditSuccess = this.makeUpItems(resp.data.list);
            });
        },
        // 获取我的待审核任务
        fetchMyTaskWaitAudit() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/task/pending', {}, resp => {
                resp.data.forEach(task => {
                    task.name += '(待审核)';
                });
                vm.task.doing = vm.task.doing.concat(this.makeUpItems(resp.data));
            });
        },
        fetchProjectList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/project/list', {}, resp => {
                vm.projectList = resp.data;
            });
        },
        fetchStageList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/stage/list', {}, resp => {
                vm.stageList = resp.data;
            });
        },
        fetchUserList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/user/effective', {}, resp => {
                vm.userList = resp.data;
            });
        },
        fetchTagList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/tag/list', {}, resp => {
                vm.tagList = resp.data;
            });
        },
        handleFinishedPage(currentPage) {
            this.finishedPage.pageNum = currentPage;
            this.fetchTaskFinished();
        },
        handleCommentedPage(currentPage) {
            this.commentedPage.pageNum = currentPage;
            this.fetchTaskCommented();
        },
        handleAuditSuccessPage(currentPage) {
            this.auditSuccessPage.pageNum = currentPage;
            this.fetchTaskAuditSuccess();
        },
        getDateString(date) {
            //时间期限
            let now = new Date();
            let curMonth = now.getMonth();
            let curYear = now.getFullYear();;
            let startMonth = 0;
            if (date == "month") {
                //本月的开始结束时间
                return __WEBPACK_IMPORTED_MODULE_4_moment___default()(new Date(curYear, curMonth, 1)).format('YYYY-MM-DD') + "--" + __WEBPACK_IMPORTED_MODULE_4_moment___default()(new Date(curYear, curMonth + 1, 1) - 1).format('YYYY-MM-DD');
            } else if (date == "week") {
                //本季度的开始结束时间
                return __WEBPACK_IMPORTED_MODULE_4_moment___default()(new Date(curYear, curMonth, now.getDate() - now.getDay() + 1)).format('YYYY-MM-DD') + "--" + __WEBPACK_IMPORTED_MODULE_4_moment___default()(new Date(curYear, curMonth, now.getDate() + (6 - now.getDay()) + 1)).format('YYYY-MM-DD');
            } else if (date == "year") {
                //本年的开始结束时间
                return __WEBPACK_IMPORTED_MODULE_4_moment___default()(new Date(now.getFullYear(), 0, 1)).format('YYYY-MM-DD') + "--" + __WEBPACK_IMPORTED_MODULE_4_moment___default()(new Date(now.getFullYear() + 1, 0, 1) - 1).format('YYYY-MM-DD');
            } else if (date == "quarter") {
                if (curMonth >= 1 && curMonth <= 3) {
                    startMonth = 0;
                } else if (curMonth >= 4 && curMonth <= 6) {
                    startMonth = 3;
                } else if (curMonth >= 7 && curMonth <= 9) {
                    startMonth = 6;
                } else if (curMonth >= 10 && curMonth <= 12) {
                    startMonth = 9;
                }
                return __WEBPACK_IMPORTED_MODULE_4_moment___default()(new Date(curYear, startMonth, 1)).format('YYYY-MM-DD') + "--" + __WEBPACK_IMPORTED_MODULE_4_moment___default()(new Date(curYear, startMonth + 3, 1) - 1).format('YYYY-MM-DD');
            }
        }
    },
    components: {
        ElTabPane: __WEBPACK_IMPORTED_MODULE_5__node_modules_element_ui_packages_tabs_src_tab_pane_vue___default.a,
        TaskItem: __WEBPACK_IMPORTED_MODULE_0__TaskItem___default.a
    }
});

/***/ }),
/* 224 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__AddMember__ = __webpack_require__(291);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__AddMember___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__AddMember__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__ModifyMember__ = __webpack_require__(296);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__ModifyMember___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__ModifyMember__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__AddDepartment__ = __webpack_require__(290);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__AddDepartment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__AddDepartment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__lib_Http__ = __webpack_require__(5);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//







/* harmony default export */ __webpack_exports__["default"] = ({
    name: 'Organization',
    data() {
        return {
            //查询表单
            queryForm: {
                pageIndex: 1,
                deptId: ''
            },
            //分页信息
            pagination: {
                total: 0,
                pageSize: 10,
                layout: 'total, pager'
            },
            //部门树结构数据
            departmentTree: [],
            //部门树结构父子传递字段
            defaultProps: {
                children: 'children',
                label: 'label'
            },
            //分页表数据
            tableData: [],
            editStatusIndex: '',
            nowCreateDate: '',
            newLastLogin: '',
            currentPage1: 5,
            defaultProps: {
                id: 'id',
                label: 'label',
                children: 'children'
            }
        };
    },
    //数据初始化
    beforeMount() {
        let _this = this;
        //选中组织tab
        _this.$root.eventBus.$emit("handleTabSelected", "organization");
        //初始化部门树结构数据
        __WEBPACK_IMPORTED_MODULE_3__lib_Http__["a" /* default */].zsyGetHttp('/dept/tree', null, res => {
            _this.departmentTree.push(res.data);
            _this.userPaging(_this.departmentTree[0].id, _this.queryForm.pageIndex);
        });
    },
    methods: {
        //分页上一页和下一页样式
        pagingLayout(total) {
            if (total > 0) {
                this.pagination.layout = 'total,prev,pager,next';
            } else {
                this.pagination.layout = 'total,pager';
            }
        },
        //用户按部门分页查询
        userPaging(deptId, pageIndex) {
            __WEBPACK_IMPORTED_MODULE_3__lib_Http__["a" /* default */].zsyGetHttp(`/user/page/${deptId}/${pageIndex}`, null, res => {
                this.queryForm.pageIndex = pageIndex;
                this.queryForm.deptId = deptId;
                this.tableData = res.data.list;
                this.pagination.pageSize = res.data.pageSize;
                this.pagination.total = res.data.total;
                this.pagingLayout(this.pagination.total);
            });
        },
        //点击页码
        handleCurrentChange(val) {
            this.userPaging(this.queryForm.deptId, val);
        },
        // 部门结构树点击
        handleNodeClick(data) {
            //最小子节点,可点击
            this.userPaging(data.id, 1);
        },
        // 重置用户密码确认框
        resetUserPwdDlgShow(index) {
            let userId = this.tableData[index].id;
            this.$confirm('此操作将重置密码, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.resetUserPwd(userId);
            }).catch(() => {});
        },
        // 重置用户密码
        resetUserPwd(userId) {
            __WEBPACK_IMPORTED_MODULE_3__lib_Http__["a" /* default */].zsyPutHttp(`/user/repwd/${userId}`, null, res => {
                this.$message.success('重置密码成功');
            });
        },
        //删除用户确认框
        deleteUserDlgShow(index) {
            let userId = this.tableData[index].id;
            // 删除
            this.$confirm('此操作将删除该条数据, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.deleteUser(userId);
            }).catch(() => {});
        },
        //删除用户
        deleteUser(userId) {
            __WEBPACK_IMPORTED_MODULE_3__lib_Http__["a" /* default */].zsyDeleteHttp(`/user/${userId}`, null, res => {
                this.$message.success('删除用户成功');
                this.userPaging(this.queryForm.deptId, this.queryForm.pageIndex);
            });
        },
        //添加部门弹窗
        addDepartDlgShow() {
            // 添加部门
            this.$refs.showAddDepartment.show();
        },
        //添加部门成功,刷新部门数据
        handleDeptDataRefresh() {
            // 部门结构数据
            __WEBPACK_IMPORTED_MODULE_3__lib_Http__["a" /* default */].zsyGetHttp('/dept/tree', null, res => {
                this.departmentTree = [];
                this.departmentTree.push(res.data);
            });
        },
        //添加用户弹窗
        addUserDlgShow() {
            this.$refs.showAddMember.setDeptId(this.queryForm.deptId);
            this.$refs.showAddMember.show();
        },
        //修改用户弹窗
        modifyUserDlgShow(index) {
            let userId = this.tableData[index].id;
            this.$refs.showModifyMember.setUserId(userId);
            this.$refs.showModifyMember.show();
        },
        //添加用户成功,刷新用户数据
        handleUserDataRefresh() {
            this.userPaging(this.queryForm.deptId, this.queryForm.pageIndex);
        }
    },
    components: {
        AddMember: __WEBPACK_IMPORTED_MODULE_0__AddMember___default.a,
        ModifyMember: __WEBPACK_IMPORTED_MODULE_1__ModifyMember___default.a,
        AddDepartment: __WEBPACK_IMPORTED_MODULE_2__AddDepartment___default.a
    }
});

/***/ }),
/* 225 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(7);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_element_ui__ = __webpack_require__(10);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_element_ui___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_element_ui__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_button_src_button__ = __webpack_require__(15);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_button_src_button___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_button_src_button__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//






/* harmony default export */ __webpack_exports__["default"] = ({
  components: { ElButton: __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_button_src_button___default.a },
  name: 'Project',
  data() {
    return {
      editProjectVisible: false,
      TaskItem: '',
      showAddTask: false,
      project: {
        name: '',
        description: ''
      }, //待编辑项目ID
      editProjectId: ''
    };
  },
  beforeMount: function () {
    //选中项目tab
    this.$root.eventBus.$emit("handleTabSelected", "project");
    this.projectList();
  },
  computed: {
    //是否有权限
    hasPermission() {
      return __WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].decodeToken().userRole <= 1;
    }
  },
  methods: {
    deleteWindow() {
      //删除项目弹出框
      this.$confirm('此操作将永久删除该项目, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyDeleteHttp("/project/delete/" + this.editProjectId, null, res => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          this.project.name = this.project.description = this.editProjectId = '';
          this.editProjectVisible = false;
          this.projectList();
        });
      }).catch(() => {});
    },
    editProject(id, name, description) {
      if (this.hasPermission) {
        this.editProjectId = id;
        this.project.name = name;
        this.project.description = description;
        this.editProjectVisible = true;
      }
    },
    hideEdit() {
      this.project.name = this.project.description = this.editProjectId = '';
      this.editProjectVisible = false;
    },
    projectList() {
      __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(__WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].API_URI.PROJECT, null, res => {
        this.TaskItem = res.data;
      });
    },
    updateProject() {
      if (this.saveAdd()) {
        __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp("/project/update/" + this.editProjectId, this.project, res => {
          __WEBPACK_IMPORTED_MODULE_2_element_ui__["Message"].success("项目更新成功");
          this.project.name = this.project.description = this.editProjectId = '';
          this.editProjectVisible = false;
          this.projectList();
        });
      }
    },
    addTask() {
      this.showAddTask = true;
    },
    hidePop() {
      this.project.name = this.project.description = this.editProjectId = '';
      this.showAddTask = false;
    },
    saveAdd() {
      // 保存
      if (this.project.name != '' && this.project.name.trim().length != 0 && this.project.name.trim().length < 15) {
        return true;
      } else {
        __WEBPACK_IMPORTED_MODULE_2_element_ui__["Message"].error("项目名称不能为空且不超过15字");
        return false;
      }
    },
    saveProject() {
      if (this.saveAdd()) {
        __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp(__WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].API_URI.ADDPROJECT, this.project, res => {
          __WEBPACK_IMPORTED_MODULE_2_element_ui__["Message"].success("项目添加成功");
          this.projectList();
          this.project.name = this.project.description = this.editProjectId = '';
          this.showAddTask = false;
        });
      }
    }
  }
});

/***/ }),
/* 226 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(7);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_element_ui__ = __webpack_require__(10);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_element_ui___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_element_ui__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_button_src_button__ = __webpack_require__(15);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_button_src_button___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_button_src_button__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__Task__ = __webpack_require__(26);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__Task___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4__Task__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//







/* harmony default export */ __webpack_exports__["default"] = ({
    components: { ElButton: __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_button_src_button___default.a },
    name: 'IntegralHistory',
    data() {
        return {
            statsData: [],
            statsPage: {
                layout: "total, pager",
                currentPage: 1,
                pageSize: 10,
                totals: 0,
                pageNum: 0
            }
        };
    },
    beforeMount: function () {
        this.getStats(this.statsPage.currentPage);
        //选中任务tab
        this.$root.eventBus.$emit("handleTabSelected", "stats");
    },
    methods: {
        getStats(currentPage) {
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/stats/list/`, {}, resp => {
                this.statsData = resp.data;
            });
        },
        getTask(index) {
            this.$router.push({ path: '/index/task', query: { userId: this.statsData[index].id } });
        }
    }
});

/***/ }),
/* 227 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__CreateTask__ = __webpack_require__(293);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__CreateTask___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__CreateTask__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__TaskItem__ = __webpack_require__(165);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__TaskItem___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__TaskItem__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__board_TaskBoard__ = __webpack_require__(297);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__board_TaskBoard___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__board_TaskBoard__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__lib_Http__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__lib_Helper__ = __webpack_require__(7);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5_moment__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//








__WEBPACK_IMPORTED_MODULE_5_moment___default.a.locale('zh-cn');
/* harmony default export */ __webpack_exports__["default"] = ({
    name: 'Task',
    data() {
        return {
            open: false,
            btnVal: "点击切换到看板模式",
            btnValStatus: 1, /*1是列表模式，2是看板模式*/
            loading: true,
            timeRange: '',
            projectList: [],
            userList: [],
            stageList: [],
            tagList: [],
            priorityList: [{ label: '普通', value: 1 }, { label: '紧急', value: 2 }, { label: '非常紧急', value: 3 }],
            typeList: [{ value: 1, name: '个人任务' }, { value: 2, name: '多人任务' }],
            status: [{ value: 1, name: '进行中' }, { value: 3, name: '已完成' }],
            taskItems: [],
            page: {
                pageNum: 0,
                pageSize: 5,
                total: 0
            },
            form: {
                projectId: '',
                userId: '',
                stageId: [],
                tagId: [],
                type: 2,
                status: 1,
                priority: '',
                beginTime: '',
                endTime: ''
            }, pickerOptions: {
                shortcuts: [{
                    text: '本周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(__WEBPACK_IMPORTED_MODULE_5_moment___default()().startOf('week').valueOf());
                        end.setTime(__WEBPACK_IMPORTED_MODULE_5_moment___default()().endOf('week').valueOf());
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '本月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(__WEBPACK_IMPORTED_MODULE_5_moment___default()().startOf('month').valueOf());
                        end.setTime(__WEBPACK_IMPORTED_MODULE_5_moment___default()().endOf('month').valueOf());
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '本季度',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(__WEBPACK_IMPORTED_MODULE_5_moment___default()().startOf('quarter').valueOf());
                        end.setTime(__WEBPACK_IMPORTED_MODULE_5_moment___default()().endOf('quarter').valueOf());
                        picker.$emit('pick', [start, end]);
                    }
                }]
            }
        };
    },
    created() {
        if (typeof this.$route.query.userId != "undefined") {
            console.log(this.$route.query.userId);
            this.form.userId = this.$route.query.userId;
            this.form.type = '';
        }
        this.fetchProjectList();
        this.fetchUserList();
        this.fetchStageList();
        this.fetchTagList();
        this.fetchTaskList();
        //选中任务tab
        this.$root.eventBus.$emit("handleTabSelected", "task");
    },
    computed: {
        permit() {
            let userRole = __WEBPACK_IMPORTED_MODULE_4__lib_Helper__["a" /* default */].decodeToken().userRole;
            return userRole < 2;
        },
        pageLayout() {
            if (this.page.total > 0) {
                return 'total, prev, pager, next';
            }
            return 'total, pager';
        }
    },
    methods: {
        btnValFun() {
            if (this.btnValStatus == 1) {
                // 刷新看板
                this.$root.eventBus.$emit("reloadBoard");
                this.btnValStatus = 2;
                this.btnVal = "点击切换到列表模式";
                document.getElementById('app').style.overflowY = 'hidden';
            } else {
                // 刷新列表
                this.fetchTaskList();
                this.btnValStatus = 1;
                this.btnVal = "点击切换到看板模式";
                document.getElementById('app').style.overflowY = 'auto';
            }
        },
        openFun() {
            this.open = !this.open;
        },
        addFormTagId(tagId, num, $event) {

            if (this.hasClass($event.currentTarget, 'active')) {
                this.removeClass($event.currentTarget, 'active');
                if (num == 1) {
                    this.form.tagId.splice(this.findIndex(this.form.tagId, tagId), 1);
                } else if (num == 2) {
                    this.form.stageId.splice(this.findIndex(this.form.stageId, tagId), 1);
                }
            } else {
                this.addClass($event.currentTarget, 'active');
                if (num == 1) {
                    this.form.tagId.push(tagId);
                } else if (num == 2) {
                    this.form.stageId.push(tagId);
                }
            }
            console.log(this.form.tagId);
            console.log(this.form.stageId);
        },
        hasClass(obj, cls) {
            return obj.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
        },
        addClass(obj, cls) {
            if (!this.hasClass(obj, cls)) obj.className += " " + cls;
        },
        removeClass(obj, cls) {
            if (this.hasClass(obj, cls)) {
                var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
                obj.className = obj.className.replace(reg, ' ');
            }
        },
        findIndex(arr, val) {
            for (var i = 0; i < arr.length; i++) {
                if (arr[i] == val) {
                    return i;
                }
            }
        },
        handleCurrentChange(currentPage) {
            this.page.pageNum = currentPage;
            this.fetchTaskList();
        },
        createTaskClick() {
            // 点击建任务
            this.$refs.createTaskPop.show();
        }, timeChange(time) {
            // 选择结束时间
            time = time.split(' - ');
            if (time && time.length == 2) {
                this.form.beginTime = `${time[0]} 00:00:00`;
                this.form.endTime = `${time[1]} 23:59:59`;
            } else {
                this.form.beginTime = this.form.endTime = '';
            }
        },
        fetchProjectList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_3__lib_Http__["a" /* default */].zsyGetHttp('/project/list', {}, resp => {
                vm.projectList = resp.data;
            });
        },
        fetchUserList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_3__lib_Http__["a" /* default */].zsyGetHttp('/user/effective', {}, resp => {
                vm.userList = resp.data;
            });
        },
        fetchStageList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_3__lib_Http__["a" /* default */].zsyGetHttp('/stage/list', {}, resp => {
                vm.stageList = resp.data;
            });
        },
        fetchTagList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_3__lib_Http__["a" /* default */].zsyGetHttp('/tag/list', {}, resp => {
                vm.tagList = resp.data;
            });
        },
        searchTask() {
            this.page.pageNum = 1;
            this.fetchTaskList();
        },
        fetchTaskList() {
            this.loading = true;
            this.taskItems = [];
            let vm = this;
            let param = {};
            param['pageNum'] = this.page.pageNum || 1;
            param['pageSize'] = this.page.pageSize;
            if (this.form.projectId !== '') {
                param['projectId'] = this.form.projectId;
            }
            if (this.form.userId !== '') {
                param['userId'] = this.form.userId;
            }
            if (this.form.status !== '') {
                param['status'] = this.form.status;
            }
            if (this.form.priority !== '') {
                param['priority'] = this.form.priority;
            }
            if (this.form.beginTime !== '') {
                param['beginTime'] = this.form.beginTime;
            }
            if (this.form.endTime !== '') {
                param['endTime'] = this.form.endTime;
            }
            if (this.form.stageId.length > 0) {
                param['stageId'] = this.form.stageId;
            }
            if (this.form.tagId.length > 0) {
                param['tagId'] = this.form.tagId;
            }
            param['type'] = this.form.type;
            __WEBPACK_IMPORTED_MODULE_3__lib_Http__["a" /* default */].zsyPostHttp('/task/public/master/all', param, resp => {
                const list = resp.data.list;
                list.forEach(el => {
                    let endTime = '',
                        today = __WEBPACK_IMPORTED_MODULE_5_moment___default()().format('YYYY-MM-DD');
                    if (el.status == 1) {
                        endTime = el.endTime;
                    } else {
                        endTime = el.completeTime;
                    }
                    endTime = __WEBPACK_IMPORTED_MODULE_5_moment___default()(endTime).format('YYYY-MM-DD');
                    const diffDays = __WEBPACK_IMPORTED_MODULE_5_moment___default()(today).diff(__WEBPACK_IMPORTED_MODULE_5_moment___default()(endTime), 'days');
                    let endColor = '',
                        endText = '';
                    endText = __WEBPACK_IMPORTED_MODULE_5_moment___default()(endTime).calendar(null, {
                        sameDay: '[今天]',
                        nextDay: '[明天]',
                        nextWeek: 'L',
                        lastDay: '[昨天]',
                        lastWeek: 'L',
                        sameElse: 'L'
                    });
                    if (el.status == 1) {

                        if (diffDays == 0) {
                            endColor = 'orange';
                        } else if (diffDays > 0) {
                            endColor = 'red';
                        } else if (diffDays < 0) {
                            endColor = 'blue';
                        }
                        endText += ' 截止';
                    } else {
                        endColor = 'green';
                        endText += ' 完成';
                    }
                    el['endColor'] = endColor;
                    el['endText'] = endText;

                    // 优先级样式
                    if (el.priority == 2) {
                        el.borderClass = 'orange-border';
                    } else if (el.priority == 3) {
                        el.borderClass = 'red-border';
                    }
                });
                vm.loading = false;
                vm.taskItems = resp.data.list;
                vm.page.pageNum = resp.data.pageNum;
                vm.page.pageSize = 5;
                vm.page.total = resp.data.total;
            });
        }
    },
    components: {
        TaskItem: __WEBPACK_IMPORTED_MODULE_1__TaskItem___default.a,
        CreateTask: __WEBPACK_IMPORTED_MODULE_0__CreateTask___default.a,
        TaskBoard: __WEBPACK_IMPORTED_MODULE_2__board_TaskBoard___default.a
    }
});

/***/ }),
/* 228 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1_moment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__lib_Helper__ = __webpack_require__(7);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//





__WEBPACK_IMPORTED_MODULE_1_moment___default.a.locale('zh-cn');
/* harmony default export */ __webpack_exports__["default"] = ({
    name: 'TaskItem',
    props: {
        taskItems: Array,
        taskStatus: String,
        isPrivate: Boolean,
        projectList: Array,
        stageList: Array,
        tagList: Array,
        userList: Array

    },
    data() {
        var validateEmpty = (rule, value, callback) => {
            if (value.trim() == '') {
                callback(new Error());
            } else {
                callback();
            }
        };
        return {
            loginUserId: '',
            modifyDescriptionVisible: false,
            showFinishedTask: false,
            showAuditTask: false,
            showTaskDetail: false,
            showTaskComment: false,
            showTaskModify: false,
            showAddDetail: false,
            showTaskCommentDetail: false,
            taskCommentDetail: {},
            showModifyPrivateTask: false,
            modifyPrivateTaskForm: {
                id: '',
                userId: '',
                taskType: 1,
                priority: 1,
                taskName: '',
                description: '',
                projectId: '',
                endTime: '',
                tags: [],
                taskHours: '',
                stageId: ''
            },
            pickerOptions0: {
                disabledDate(time) {
                    return time.getTime() < Date.now() - 8.64e7;
                }
            },
            beforeNow: {
                disabledDate(time) {
                    return time.getTime() > Date.now() - 8.64e7;
                }
            },
            finishForm: {
                taskId: '',
                taskUserId: '',
                taskType: '',
                completeHours: '',
                completeTime: ''
            },
            auditForm: {},
            assessForm: {
                taskId: '',
                comments: []
            },
            commentStages: [],
            allComment: false,
            taskDetail: {},
            taskLog: {
                list: [],
                hasNextPage: false,
                pageNum: 1
            },
            modifyTaskForm: {
                taskName: '',
                description: '',
                projectId: '',
                endTime: '',
                priority: '',
                tags: [],
                taskType: 2,
                stageId: '',
                taskUsers: [],
                modifyDescription: ''
            },
            priorityList: [{ label: '普通', value: 1 }, { label: '紧急', value: 2 }, { label: '非常紧急', value: 3 }],
            step: {
                index: '',
                stageId: '',
                stageName: '',
                userId: '',
                userName: '',
                taskHours: '',
                beginTime: '',
                endTime: '',
                description: '',
                completeHours: '',
                completeTime: '',
                status: ''
            },
            stepTemp: {} /*
                         projectList: [],
                         stageList: [],
                         tagList: [],*/
        };
    },
    created() {
        //            this.fetchProjectList()
        //            this.fetchStageList()
        //            this.fetchTagList()
        //            this.fetchUserList()
        this.loginUserId = __WEBPACK_IMPORTED_MODULE_2__lib_Helper__["a" /* default */].decodeToken().userId;
    },
    computed: {
        permit() {
            let userRole = __WEBPACK_IMPORTED_MODULE_2__lib_Helper__["a" /* default */].decodeToken().userRole;
            return userRole < 2;
        },
        userRole() {
            let userRole = __WEBPACK_IMPORTED_MODULE_2__lib_Helper__["a" /* default */].decodeToken().userRole;
            return userRole;
        },
        showDelete() {
            // 不包含完成的阶段才显示删除
            if (this.taskDetail.users) {
                let done = this.taskDetail.users.filter(user => {
                    return user.status == 2;
                });
                return done.length == 0 && this.taskDetail.status != 3;
            }
        },
        stepBeginTimeOptions() {
            let endTime = this.modifyTaskForm.endTime;
            return {
                disabledDate(time) {
                    const fTime = time.getTime();
                    return fTime > endTime;
                }
            };
        },
        stepEndTimeOptions() {
            let endTime = this.modifyTaskForm.endTime;
            let beginTime = this.step.beginTime;
            return {
                disabledDate(time) {
                    const fTime = time.getTime();
                    return fTime < beginTime || fTime > endTime;
                }
            };
        }
    },
    filters: {
        formatDate: function (value) {
            if (!value) return '';
            return __WEBPACK_IMPORTED_MODULE_1_moment___default()(value).format('YYYY-MM-DD');
        },
        formatTime: function (value) {
            if (!value) return '';
            return __WEBPACK_IMPORTED_MODULE_1_moment___default()(value).format('YYYY-MM-DD HH:mm:ss');
        }
    },
    methods: {
        fetchProjectList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/project/list', {}, resp => {
                vm.projectList = resp.data;
            });
        },
        fetchStageList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/stage/list', {}, resp => {
                vm.stageList = resp.data;
            });
        },
        fetchUserList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/user/effective', {}, resp => {
                vm.userList = resp.data;
            });
        },
        fetchTagList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/tag/list', {}, resp => {
                vm.tagList = resp.data;
            });
        },
        showFinishedPop(taskId, userId, taskType) {
            this.finishForm.taskId = taskId;
            this.finishForm.taskUserId = userId;
            this.finishForm.taskType = taskType;
            this.showFinishedTask = true;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/task/detail/${taskId}`, {}, resp => {
                this.taskDetail = resp.data;
            });
        },
        hideFinishedPop() {
            this.resetFinishForm();
            this.showFinishedTask = false;
        },
        showAuditPop(taskId, userId) {
            this.auditForm.taskId = taskId;
            this.auditForm.taskUserId = userId;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/task/detail/${taskId}`, {}, resp => {
                this.taskDetail = resp.data;
            });
            this.showAuditTask = true;
        },
        hideAuditPop() {
            this.auditForm.taskId = '';
            this.auditForm.taskUserId = '';
            this.showAuditTask = false;
            this.taskDetail = {};
        },
        // 审核通过任务
        acceptTask() {
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp(`task/auditing/accept/${this.auditForm.taskId}`, {}, resp => {
                this.$message.success("任务审核成功");
                this.$emit('reload');
                this.auditForm.taskId = '';
                this.auditForm.taskUserId = '';
            });
            this.showAuditTask = false;
            this.taskDetail = {};
        },
        // 打回任务
        rejectTask() {
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp(`task/auditing/reject/${this.auditForm.taskId}`, {}, resp => {
                this.$message.success("任务打回成功");
                this.$emit('reload');
                this.auditForm.taskId = '';
                this.auditForm.taskUserId = '';
            });
            this.showAuditTask = false;
            this.taskDetail = {};
        },
        // 完成任务
        finishTask() {
            this.$confirm('此操作将完成该任务, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                var param = this.finishForm;
                param.completeTime = __WEBPACK_IMPORTED_MODULE_1_moment___default()().format('YYYY-MM-DD HH:mm:ss');
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp('/task/complete', param, resp => {
                    this.resetFinishForm();
                    this.showFinishedTask = false;
                    this.taskDetail = {};
                    this.$message.success("操作成功");
                    this.$emit('reload');
                });
            }).catch(() => {});
        },
        resetFinishForm() {
            this.finishForm.taskId = '';
            this.finishForm.taskUserId = '';
            this.finishForm.taskType = '';
            this.finishForm.completeTime = '';
            this.finishForm.completeHours = '';
        },
        hideTaskDetail() {
            this.showTaskDetail = false;
            this.taskDetail = {};
            this.taskLog.list = [];
            this.taskLog.hasNextPage = false;
            this.taskLog.pageNum = 1;
        },
        taskItemClick(taskId, taskType) {
            if (!this.isPrivate || this.taskStatus == 'finished' || this.taskStatus == 'auditSuccess') {
                this.showTaskDetail = true;
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/task/detail/${taskId}`, {}, resp => {
                    this.taskDetail = resp.data;
                });
                this.getTaskLog(taskId);
            }
            // 个人点击完成任务
            if (this.taskStatus == 'TaskDoing' && this.isPrivate) {
                this.taskItems.forEach(task => {
                    if (task.id === taskId && task.reviewStatus == 3) {
                        this.showFinishedPop(taskId, task.taskUsers[0].id, taskType);
                    }
                });
            }
            // 个人任务点击评价
            if (this.taskStatus == 'WaitAssess' && this.isPrivate) {
                this.showWaitAssess(taskId);
            }
        },
        getTaskLog(taskId) {
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, resp => {
                this.taskLog.list = resp.data.list;
                this.taskLog.hasNextPage = resp.data.hasNextPage;
            });
        },
        taskLogMore(taskId) {
            this.taskLog.pageNum += 1;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/task/log/${taskId}/${this.taskLog.pageNum}`, {}, resp => {
                let logs = resp.data.list;
                this.taskLog.list = this.taskLog.list.concat(logs);
                this.taskLog.hasNextPage = resp.data.hasNextPage;
            });
        },
        completeTask() {
            this.$confirm('此操作将完成该任务, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp(`/task/complete/master/${this.taskDetail.id}`, {}, resp => {
                    this.$emit('reload');
                    this.$message.success("操作成功");
                    this.hideTaskDetail();
                });
            }).catch(() => {});
        },
        deleteTask() {
            this.$confirm('此操作将删除该任务, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyDeleteHttp(`/task/delete/${this.taskDetail.id}`, {}, resp => {
                    this.$emit('reload');
                    this.$message.success("删除成功");
                    this.hideTaskDetail();
                    this.showAuditTask = false;
                    this.taskDetail = {};
                });
            }).catch(() => {});
        },
        showWaitAssess(taskId) {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/task/detail/${taskId}`, {}, resp => {
                vm.assessForm.taskId = taskId;
                let users = [];
                // 过滤我的任务
                resp.data.users.forEach(user => {
                    if (user.userId != vm.loginUserId) {
                        users.push(user);
                    }
                });
                // 查询我的评价
                users.forEach(user => {
                    user.comments.forEach(comment => {
                        if (comment.createBy == vm.loginUserId) {
                            user.myComment = comment;
                            return;
                        }
                    });
                });
                for (let i = 0; i < users.length; i++) {
                    if (!users[i].myComment) {
                        vm.assessForm.comments.push({
                            'taskUserId': users[i].id,
                            'description': '',
                            'grade': ''
                        });
                    }
                }
                let myComments = 0;
                users.forEach(stage => {
                    if (stage.myComment) {
                        myComments++;
                    }
                });
                vm.allComment = myComments == users.length;
                vm.commentStages = users;
            });

            this.showTaskComment = true;
        },
        hideWaitAssess() {
            this.showTaskComment = false;
            this.commentStages = [];
            this.assessForm = {
                taskId: '',
                comments: []
            };
        },
        // 评价任务
        taskAssess() {
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/task/comment', this.assessForm, resp => {
                this.$message.success("评价成功");
                this.showTaskComment = false;
                this.commentStages = [];
                this.assessForm = {
                    taskId: '',
                    comments: []
                };
                this.$emit('reload');
            });
        },
        // 修改单人任务
        modifyPrivateTask(taskId) {
            this.showModifyPrivateTaskDialog(taskId);
        },
        // 保存修改单人任务
        saveModifyPrivateTaskForm() {
            if (this.modifyPrivateTaskForm.projectId == '') {
                this.$message.warning("请选择项目");
                return;
            }
            if (this.modifyPrivateTaskForm.endTime == '') {
                this.$message.warning("请选择结束时间");
                return;
            }
            if (this.modifyPrivateTaskForm.taskHours == '') {
                this.$message.warning("请输入工作量");
                return;
            }
            if (this.modifyPrivateTaskForm.taskName.trim() == '') {
                this.$message.warning("请填写任务名称");
                return;
            }
            if (this.modifyPrivateTaskForm.description.trim() == '') {
                this.$message.warning("请填写任务备注");
                return;
            }

            if (this.modifyPrivateTaskForm.stageId === '') {
                this.$message.warning("请选择项目阶段");
                return;
            }
            if (this.modifyPrivateTaskForm.tags.length == 0) {
                this.$message.warning("请选择至少一项标签");
                return;
            }
            this.modifyPrivateTaskForm.taskName = this.modifyPrivateTaskForm.taskName.trim();
            this.modifyPrivateTaskForm.endTime = __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.modifyPrivateTaskForm.endTime).format('YYYY-MM-DD 23:59:59');
            this.modifyPrivateTaskForm.taskUsers = [{
                userId: this.modifyPrivateTaskForm.userId,
                taskHours: this.modifyPrivateTaskForm.taskHours,
                beginTime: __WEBPACK_IMPORTED_MODULE_1_moment___default()().format('YYYY-MM-DD HH:mm:ss'),
                endTime: __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.modifyPrivateTaskForm.endTime).format('YYYY-MM-DD 23:59:59'),
                description: this.modifyPrivateTaskForm.description.trim()
            }];
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp(`/task/modify/${this.modifyPrivateTaskForm.id}`, this.modifyPrivateTaskForm, resp => {
                vm.$message.success('任务修改成功');
                vm.hideModifyPrivateTaskDialog();
                vm.$emit('reload');
            });
        },
        // 显示修改单人任务弹出层
        showModifyPrivateTaskDialog(taskId) {
            this.showAuditTask = false;
            this.taskDetail = {};
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/task/detail/${taskId}`, {}, resp => {
                this.modifyPrivateTaskForm.id = resp.data.id;
                this.modifyPrivateTaskForm.taskName = resp.data.name;
                this.modifyPrivateTaskForm.description = resp.data.description;
                this.modifyPrivateTaskForm.projectId = resp.data.projectId;
                this.modifyPrivateTaskForm.endTime = resp.data.endTime;
                this.modifyPrivateTaskForm.taskHours = resp.data.users[0].taskHours;
                this.modifyPrivateTaskForm.userId = resp.data.users[0].userId;
                this.modifyPrivateTaskForm.stageId = resp.data.stageId;
                for (let i = 0; i < resp.data.tags.length; i++) {
                    this.modifyPrivateTaskForm.tags.push(resp.data.tags[i].id);
                }
            });
            this.showModifyPrivateTask = true;
        }, // 隐藏修改单人任务弹出层
        hideModifyPrivateTaskDialog() {
            this.showModifyPrivateTask = false;
            this.modifyPrivateTaskForm = {
                id: '',
                userId: '',
                taskType: 1,
                priority: 1,
                taskName: '',
                description: '',
                projectId: '',
                endTime: '',
                tags: [],
                taskHours: '',
                stageId: ''
            };
        },
        // 修改任务
        modifyTask(taskId) {
            this.hideTaskDetail();
            this.showTaskModify = true;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/task/detail/${taskId}`, {}, resp => {
                this.modifyTaskForm.id = resp.data.id;
                this.modifyTaskForm.taskName = resp.data.name;
                this.modifyTaskForm.description = resp.data.description;
                this.modifyTaskForm.endTime = resp.data.endTime;
                this.modifyTaskForm.projectId = resp.data.projectId;
                this.modifyTaskForm.stageId = resp.data.stageId;
                this.modifyTaskForm.priority = resp.data.priority;
                for (let i = 0; i < resp.data.tags.length; i++) {
                    this.modifyTaskForm.tags.push(resp.data.tags[i].id);
                }
                this.modifyTaskForm.taskUsers = resp.data.users;
            });
        },
        // 修改阶段
        modifyStep(index, stages) {
            this.stepTemp = {
                stageId: stages[index].stageId,
                stageName: stages[index].stageName,
                userId: stages[index].userId,
                userName: stages[index].userName,
                taskHours: stages[index].taskHours,
                beginTime: stages[index].beginTime,
                endTime: stages[index].endTime,
                description: stages[index].description,
                completeHours: stages[index].completeHours,
                completeTime: stages[index].completeTime,
                status: stages[index].status
            };
            this.step = stages[index];
            this.step.index = index;
            this.modifyTaskForm.taskUsers.forEach(item => {
                item.cssClass = '';
            });
            this.modifyTaskForm.taskUsers[index].cssClass = 'stepActive';
            this.showAddDetail = true;
        },
        hideTaskModify() {
            this.modifyTaskForm.taskName = '';
            this.modifyTaskForm.description = '';
            this.modifyTaskForm.taskName = '';
            this.modifyTaskForm.endTime = '';
            this.modifyTaskForm.projectId = '';
            this.modifyTaskForm.stageId = '';
            this.modifyTaskForm.priority = 1;
            this.modifyTaskForm.tags = [];
            this.modifyTaskForm.taskUsers = [];
            this.modifyTaskForm.modifyDescription = '';
            this.showTaskModify = false;
            this.step = {
                index: '',
                stageId: '',
                stageName: '',
                userId: '',
                userName: '',
                taskHours: '',
                beginTime: '',
                endTime: '',
                description: '',
                completeHours: '',
                completeTime: '',
                status: ''
            };
            this.hideModifyDescription();
        },
        deleteMember(index) {
            this.modifyTaskForm.taskUsers.splice(index, 1);
            if (this.modifyTaskForm.taskUsers.length == 0) {
                this.showAddDetail = false;
                this.step = {
                    index: '',
                    stageId: '',
                    stageName: '',
                    userId: '',
                    userName: '',
                    taskHours: '',
                    beginTime: '',
                    endTime: '',
                    description: '',
                    completeHours: '',
                    completeTime: '',
                    status: ''
                };
            }
        },
        addMember() {
            this.showAddDetail = !this.showAddDetail;
        },
        cancelAddMember() {
            this.showAddDetail = !this.showAddDetail;
            if (this.step.index !== '') {
                this.modifyTaskForm.taskUsers[this.step.index] = this.stepTemp;
                // 取消css
                this.modifyTaskForm.taskUsers[this.step.index].cssClass = '';
            }
            this.step = {
                index: '',
                userId: '',
                userName: '',
                taskHours: '',
                beginTime: '',
                endTime: '',
                description: '',
                completeHours: '',
                completeTime: '',
                status: ''
            };
        },
        saveAddMember() {
            const valid = this.step.userId == '' || this.step.taskHours == '' || this.step.beginTime == '' || this.step.endTime == '';
            if (valid) {
                this.$message.warning('请将阶段填写完整');
                return;
            }
            if (this.step.index === '') {
                let taskUser = {};
                taskUser.userId = this.step.userId;
                taskUser.userName = this.step.userName;
                taskUser.beginTime = this.step.beginTime;
                taskUser.endTime = this.step.endTime;
                taskUser.taskHours = this.step.taskHours;
                taskUser.description = this.step.description;
                this.modifyTaskForm.taskUsers.push(taskUser);
            } else {
                // 取消css
                this.modifyTaskForm.taskUsers[this.step.index].cssClass = '';
            }

            this.showAddDetail = !this.showAddDetail;
            this.step = {
                index: '',
                userId: '',
                userName: '',
                taskHours: '',
                beginTime: '',
                endTime: '',
                description: '',
                completeHours: '',
                completeTime: '',
                status: ''
            };
            this.stepTemp = {};
        },
        stepUserChange(val) {
            let vm = this;
            this.userList.forEach(user => {
                if (user.id === val) {
                    vm.step.userName = user.name;
                }
            });
        },
        stepStageChange(val) {
            let vm = this;
            this.stageList.forEach(stage => {
                if (stage.id === val) {
                    vm.step.stageName = stage.name;
                }
            });
        },
        saveTaskInfo() {
            /* if (this.modifyTaskForm.description == '') {
                 this.$message.warning("请填写任务备注");
                 return;
             }*/
            if (this.modifyTaskForm.taskName == '') {
                this.$message.warning("请填写任务名称");
                return;
            }
            if (this.modifyTaskForm.projectId == '') {
                this.$message.warning("请选择项目");
                return;
            }
            if (this.modifyTaskForm.endTime == '') {
                this.$message.warning("请选择结束时间");
                return;
            }
            if (this.modifyTaskForm.stageId === '') {
                this.$message.warning("请选择项目阶段");
                return;
            }
            if (this.modifyTaskForm.tags.length == 0) {
                this.$message.warning("请选择至少一项标签");
                return;
            }
            let param = this.modifyTaskForm;
            param.taskName = param.taskName.trim();
            param.description = param.description.trim();
            param.modifyDescription = param.modifyDescription.trim();
            param.taskUsers.forEach(user => {
                user.description = user.description.trim();
                user.beginTime = __WEBPACK_IMPORTED_MODULE_1_moment___default()(user.beginTime).format('YYYY-MM-DD HH:mm:ss');
                user.endTime = __WEBPACK_IMPORTED_MODULE_1_moment___default()(user.endTime).format('YYYY-MM-DD 23:59:59');
            });
            param.endTime = __WEBPACK_IMPORTED_MODULE_1_moment___default()(param.endTime).format('YYYY-MM-DD 23:59:59');
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp(`/task/modify/${this.modifyTaskForm.id}`, param, resp => {
                vm.$message.success('任务修改成功');
                this.hideTaskModify();
                // 刷新看板
                //this.$root.eventBus.$emit("reloadBoard");
                // 刷新列表
                vm.$emit('reload');
            });
            this.showCreateTask = false;
        },
        hideTaskCommentDetail() {
            this.showTaskCommentDetail = false;
        },
        commentDetail(taskUserId) {
            let vm = this;
            this.taskDetail.users.forEach(user => {
                if (user.id == taskUserId) {
                    vm.taskCommentDetail = user;
                    return;
                }
            });
            this.showTaskCommentDetail = true;
        },
        /** 编辑任务填写备注 **/
        showModifyDescription() {
            this.modifyDescriptionVisible = true;
        },
        hideModifyDescription() {
            this.modifyDescriptionVisible = false;
            this.hideTaskDetail();
        }

    },
    created() {
        // 监听看板任务点击事件
        var vm = this;
        vm.$root.eventBus.$on("handleBoardClick2", taskId => {
            console.log('on');
            vm.showTaskDetail = true;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/task/detail/${taskId}`, {}, resp => {
                //vm.taskDetail = resp.data
            });
            vm.getTaskLog(taskId);
        });
    }
});

/***/ }),
/* 229 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_moment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(7);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//

var dom;



__WEBPACK_IMPORTED_MODULE_0_moment___default.a.locale('zh-cn');
/* harmony default export */ __webpack_exports__["default"] = ({
    name: "task-board",
    data() {
        return {
            stageList: [],
            taskBoxWidth: '1200px'
        };
    },
    computed: {
        loginUserRole() {
            let userRole = __WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].decodeToken().userRole;
            return userRole;
        },
        loginUserId() {
            let userId = __WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].decodeToken().userId;
            return userId;
        }
    },
    methods: {
        drag: function (event) {
            // 自己创建的任务及超管才可以拖动任务
            let taskCreateBy = event.currentTarget.getAttribute('createby');
            if (this.loginUserId === taskCreateBy || this.loginUserRole === 0) {
                dom = event.currentTarget;
            } else {
                event.preventDefault();
                return;
            }
        },
        drop: function (event) {
            let stageId = '',
                originId = dom.getAttribute('taskid'),
                targetId = '',
                vm = this;
            event.preventDefault();
            if (event.target.tagName.toLowerCase() == "li") {
                targetId = event.target.getAttribute('taskid');
            } else if (event.target.tagName.toLowerCase() == "ul") {
                stageId = event.target.getAttribute('stageid');
            } else {
                let li = this.findParent(event.target);
                targetId = li.getAttribute('taskid');
            }
            this.http.zsyPutHttp('/task/move', {
                originId: originId,
                targetId: targetId,
                targetStageId: stageId
            }, res => {

                if (event.target.tagName.toLowerCase() == "li") {
                    event.target.parentNode.insertBefore(dom, event.target);
                } else if (event.target.tagName.toLowerCase() == "ul") {
                    event.target.appendChild(dom);
                } else {
                    let li = vm.findParent(event.target);
                    li.parentNode.insertBefore(dom, li);
                }
                vm.getData();
            });
        },
        allowDrop: function (event) {
            event.preventDefault();
        },
        findParent(obj) {
            while (obj.parentNode && obj.tagName.toLowerCase() != "li") {
                obj = obj.parentNode;
            }
            return obj;
        },
        handleTaskItemClick(taskId) {
            console.log('emit');
            this.$root.eventBus.$emit("handleBoardClick2", taskId);
        },
        getData() {
            // 获取阶段
            let that = this;
            that.http.zsyGetHttp('/stage/list', {}, res => {
                that.stageList = res.data;
                that.taskBoxWidth = that.stageList.length * 270 + "px";
                that.stageList.forEach(stage => {
                    // 获取任务
                    that.http.zsyGetHttp(`/task/tasksByStage/${stage.id}`, {}, res => {
                        let list = res.data;
                        list.forEach(el => {
                            let endTime = '',
                                today = __WEBPACK_IMPORTED_MODULE_0_moment___default()().format('YYYY-MM-DD');
                            if (el.status == 1) {
                                endTime = el.endTime;
                            } else {
                                endTime = el.completeTime;
                            }
                            endTime = __WEBPACK_IMPORTED_MODULE_0_moment___default()(endTime).format('YYYY-MM-DD');
                            const diffDays = __WEBPACK_IMPORTED_MODULE_0_moment___default()(today).diff(__WEBPACK_IMPORTED_MODULE_0_moment___default()(endTime), 'days');
                            let endColor = '',
                                endText = '';
                            endText = __WEBPACK_IMPORTED_MODULE_0_moment___default()(endTime).calendar(null, {
                                sameDay: '[今天]',
                                nextDay: '[明天]',
                                nextWeek: 'L',
                                lastDay: '[昨天]',
                                lastWeek: 'L',
                                sameElse: 'L'
                            });
                            if (el.status == 1) {
                                if (diffDays == 0) {
                                    endColor = 'orange';
                                } else if (diffDays > 0) {
                                    endColor = 'red';
                                } else if (diffDays < 0) {
                                    endColor = 'blue';
                                }
                                endText += ' 截止';
                            } else {
                                endColor = 'green';
                                endText += ' 完成';
                            }
                            el['endColor'] = endColor;
                            el['endText'] = endText;

                            // 优先级样式
                            if (el.priority == 2) {
                                el.borderClass = 'orange-border';
                            } else if (el.priority == 3) {
                                el.borderClass = 'red-border';
                            }
                        });
                        that.$set(stage, 'tasks', list);
                    });
                });
            });
        }
    },
    created() {
        this.getData();
    },
    beforeMount() {
        let vm = this;
        this.$root.eventBus.$on("reloadBoard", () => {
            vm.getData();
        });
    },
    mounted() {}
});

/***/ }),
/* 230 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_vue__ = __webpack_require__(8);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__App__ = __webpack_require__(170);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__App___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__App__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__router__ = __webpack_require__(167);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_element_ui__ = __webpack_require__(10);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_element_ui___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_element_ui__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_element_ui_lib_theme_default_index_css__ = __webpack_require__(168);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_element_ui_lib_theme_default_index_css___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_element_ui_lib_theme_default_index_css__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__assets_css_base_css__ = __webpack_require__(169);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__assets_css_base_css___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5__assets_css_base_css__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__lib_Http__ = __webpack_require__(5);
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.



// element-ui


// common css


__WEBPACK_IMPORTED_MODULE_0_vue__["default"].use(__WEBPACK_IMPORTED_MODULE_3_element_ui___default.a);
__WEBPACK_IMPORTED_MODULE_0_vue__["default"].config.productionTip = false;


__WEBPACK_IMPORTED_MODULE_0_vue__["default"].prototype.http = __WEBPACK_IMPORTED_MODULE_6__lib_Http__["a" /* default */];

/* eslint-disable no-new */
window.vue = new __WEBPACK_IMPORTED_MODULE_0_vue__["default"]({
  el: '#app',
  router: __WEBPACK_IMPORTED_MODULE_2__router__["a" /* default */],
  template: '<App/>',
  data: {
    eventBus: new __WEBPACK_IMPORTED_MODULE_0_vue__["default"]()
  },
  components: { App: __WEBPACK_IMPORTED_MODULE_1__App___default.a }
});

/***/ }),
/* 231 */,
/* 232 */,
/* 233 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.organization-con[data-v-03088b73]{width: 1100px;margin: auto;\n}\n.department-structure[data-v-03088b73]{width: 260px;background: #fff;padding-bottom: 30px;height: 564px;overflow-y: scroll;\n}\n.department-structure[data-v-03088b73]::-webkit-scrollbar {\r\n    width: 3px;\r\n    background-color: #F5F5F5;\n}\n.department-structure[data-v-03088b73]::-webkit-scrollbar-thumb {\r\n    background-color: rgb(255, 255, 255);\r\n    background-image: -webkit-gradient(linear, 40% 0%, 75% 84%, from(rgb(54, 168, 255)), color-stop(0.6, rgb(54, 229, 255)), to(rgb(54, 192, 255)));\r\n    border-radius: 10px;\n}\n.department-structure[data-v-03088b73]::-webkit-scrollbar-track {\r\n    background-color: #f5f5f5;\r\n    border-radius: 10px;\n}\n.ds-title[data-v-03088b73],.dm-title[data-v-03088b73]{position: relative;line-height: 34px;background: #36A8FF;color: #fff;font-size: 14px;padding: 0 10px;margin-bottom: 10px;\n}\n.add-department[data-v-03088b73]{position: absolute;display: block;border: 1px solid #fff;width: 22px;height: 22px;border-radius: 50%;text-align: center;line-height: 20px;right: 10px;top: 5px;font-size: 24px;cursor: pointer;\n}\n.department-member[data-v-03088b73]{width: 820px;height: 594px;margin-left: 20px;background: #fff;\n}\n.dm-title[data-v-03088b73]{margin-bottom: 0;\n}\n.white-bg[data-v-03088b73]{background: #fff;\n}\r\n", ""]);

// exports


/***/ }),
/* 234 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.add-member-pop[data-v-0f1dfbc3]{position: fixed;top: 0;left: 0;bottom: 0;right: 0;z-index: 100;background: rgba(0,0,0,0.5);\n}\n.add-member-pop-con[data-v-0f1dfbc3]{position: absolute;background: #fff;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 536px;padding: 16px;padding-bottom: 30px;\n}\n.ctpc-top[data-v-0f1dfbc3]{font-size: 16px;line-height: 30px;font-weight: 700;color: #333;\n}\n.ctpc-top-close[data-v-0f1dfbc3]{font-size: 30px;width: 30px;height: 30px;line-height: 27px;cursor: pointer;transition:0.8s ease all;text-align: center;\n}\n.ctpc-top-close[data-v-0f1dfbc3]:hover{color:#36A8FF;transform:rotate(360deg);\n}\n.ftp-list[data-v-0f1dfbc3]{margin: 20px 0;font-size: 14px;\n}\n.ftp-menus[data-v-0f1dfbc3]{width: 130px;text-align: right;margin-right: 16px;line-height: 30px;\n}\n.ftp-msg[data-v-0f1dfbc3]{line-height: 30px;\n}\n.ftp-msg > input[data-v-0f1dfbc3]{height: 28px;width: 280px;border: 1px solid #ccc;border-radius: 3px;text-indent: 4px;\n}\n.am-warn[data-v-0f1dfbc3]{height: 20px;line-height: 20px;text-indent: 84px;color: red;\n}\n.ctpc-btns[data-v-0f1dfbc3]{text-align: right;\n}\n.ctpc-btns > input[data-v-0f1dfbc3]{display: inline-block;width: 80px;height: 28px;margin-left: 12px;cursor: pointer;border-radius: 4px;\n}\n.ctpc-cancel[data-v-0f1dfbc3]{background: #fff;border: 1px solid #ccc;\n}\n.ctpc-cancel[data-v-0f1dfbc3]:hover{background: #fff;border: 1px solid #36A8FF;color: #36A8FF;font-weight: 700;\n}\n.ctpc-save[data-v-0f1dfbc3]{background: #36A8FF;color: #fff;\n}\r\n\r\n", ""]);

// exports


/***/ }),
/* 235 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.add-member-pop[data-v-176c51b4]{position: fixed;top: 0;left: 0;bottom: 0;right: 0;z-index: 100;background: rgba(0,0,0,0.5);\n}\n.add-member-pop-con[data-v-176c51b4]{position: absolute;background: #fff;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 536px;padding: 16px;padding-bottom: 30px;\n}\n.ctpc-top[data-v-176c51b4]{font-size: 16px;line-height: 30px;font-weight: 700;color: #333;\n}\n.ctpc-top-close[data-v-176c51b4]{font-size: 30px;width: 30px;height: 30px;line-height: 27px;cursor: pointer;transition:0.8s ease all;text-align: center;\n}\n.ctpc-top-close[data-v-176c51b4]:hover{color:#36A8FF;transform:rotate(360deg);\n}\n.ftp-list[data-v-176c51b4]{margin: 20px 0;font-size: 14px;\n}\n.ftp-menus[data-v-176c51b4]{width: 130px;text-align: right;margin-right: 16px;line-height: 30px;\n}\n.ftp-msg[data-v-176c51b4]{line-height: 30px;\n}\n.ftp-msg > input[data-v-176c51b4]{height: 28px;width: 280px;border: 1px solid #ccc;border-radius: 3px;text-indent: 4px;\n}\n.am-warn[data-v-176c51b4]{height: 20px;line-height: 20px;text-indent: 84px;color: red;\n}\n.ctpc-btns[data-v-176c51b4]{text-align: right;\n}\n.ctpc-btns > input[data-v-176c51b4]{display: inline-block;width: 80px;height: 28px;margin-left: 12px;cursor: pointer;border-radius: 4px;\n}\n.ctpc-cancel[data-v-176c51b4]{background: #fff;border: 1px solid #ccc;\n}\n.ctpc-cancel[data-v-176c51b4]:hover{background: #fff;border: 1px solid #36A8FF;color: #36A8FF;font-weight: 700;\n}\n.ctpc-save[data-v-176c51b4]{background: #36A8FF;color: #fff;\n}\r\n\r\n\r\n\r\n", ""]);

// exports


/***/ }),
/* 236 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.task-board[data-v-2346b0e8] {\n  position: fixed;\n  top: 120px;\n  right: 0;\n  bottom: 0;\n  left: 0;\n  padding: 10px;\n  /*width: 1080px;*/\n  margin: 0 auto;\n  background-color: #fff;\n  overflow-x: auto;\n}\n.task-board .task-board-list[data-v-2346b0e8] {\n  width: 400%;\n  height: 100%;\n}\n.task-board .task-board-list .task-list[data-v-2346b0e8] {\n  position: relative;\n  width: 260px;\n  margin-right: 10px;\n  height: 100%;\n  background-color: #eee;\n  padding-bottom: 10px;\n  box-sizing: border-box;\n}\n.task-board .task-board-list .task-list header[data-v-2346b0e8] {\n  position: absolute;\n  width: 100%;\n  top: 0;\n  left: 0;\n  padding-left: 10px;\n  line-height: 30px;\n  font-size: 14px;\n  font-weight: bold;\n  background-color: #eee;\n  box-sizing: border-box;\n}\n.task-board .task-board-list .task-list .task-item[data-v-2346b0e8] {\n  padding: 30px 10px 0;\n  height: 100%;\n  box-sizing: border-box;\n  overflow-y: auto;\n}\n.task-board .task-board-list .task-list .task-item li[data-v-2346b0e8] {\n  border-bottom: 10px solid #eee;\n  padding: 5px 10px;\n  background-color: #fff;\n  border-radius: 3px;\n  cursor: pointer;\n}\n.task-board .task-board-list .task-list .task-item li .complate[data-v-2346b0e8] {\n  margin-right: 10px;\n}\n.task-board .task-board-list .task-list .task-item li .complate span[data-v-2346b0e8] {\n  display: inline-block;\n  margin-top: 4px;\n  width: 20px;\n  height: 20px;\n  text-align: justify;\n  background-color: #eee;\n  border-radius: 3px;\n}\n.task-board .task-board-list .task-list .task-item li .task-name[data-v-2346b0e8] {\n  width: 150px;\n  font-size: 12px;\n  line-height: 18px;\n  word-break: break-all;\n}\n.task-board .task-board-list .task-list .task-item li .task-name .tips[data-v-2346b0e8] {\n  padding: 0 8px 2px;\n  /*   background-color: #FFAF38;*/\n  color: #fff;\n  border-radius: 3px;\n}\n.task-board .task-board-list .task-list .task-item li .task-name .tips.red[data-v-2346b0e8] {\n  background: #FF4515;\n}\n.task-board .task-board-list .task-list .task-item li .task-name .tips.orange[data-v-2346b0e8] {\n  background: #FF9900;\n}\n.task-board .task-board-list .task-list .task-item li .task-name .tips.blue[data-v-2346b0e8] {\n  background: #36A8FF;\n}\n.task-board .task-board-list .task-list .task-item li .task-name .tips.green[data-v-2346b0e8] {\n  background: #339933;\n}\n.task-board .task-board-list .task-list .task-item li .master-info[data-v-2346b0e8] {\n  /* img {\n                             width: 40px;\n                             height: 40px;\n                             border-radius: 50%;\n                         }*/\n  height: 40px;\n  background: #69C8FA;\n  border-radius: 50%;\n  line-height: 40px;\n  text-align: center;\n  color: #fff;\n  cursor: pointer;\n  width: 40px;\n}\n.task-board .task-board-list .task-list .task-item li[data-v-2346b0e8]:last-child {\n  border-bottom: 0;\n}\n.task-board .task-board-list .task-list[data-v-2346b0e8]:last-child {\n  margin-right: 0;\n}\n", ""]);

// exports


/***/ }),
/* 237 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.add-member-pop[data-v-256e98ea]{position: fixed;top: 0;left: 0;bottom: 0;right: 0;z-index: 100;background: rgba(0,0,0,0.5);\n}\n.add-member-pop-con[data-v-256e98ea]{position: absolute;background: #fff;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 536px;padding: 16px;padding-bottom: 30px;\n}\n.ctpc-top[data-v-256e98ea]{font-size: 16px;line-height: 30px;font-weight: 700;color: #333;\n}\n.ctpc-top-close[data-v-256e98ea]{font-size: 30px;width: 30px;height: 30px;line-height: 27px;cursor: pointer;transition:0.8s ease all;text-align: center;\n}\n.ctpc-top-close[data-v-256e98ea]:hover{color:#36A8FF;transform:rotate(360deg);\n}\n.ftp-list[data-v-256e98ea]{margin: 20px 0;font-size: 14px;\n}\n.ftp-menus[data-v-256e98ea]{width: 130px;text-align: right;margin-right: 16px;line-height: 30px;\n}\n.ftp-msg[data-v-256e98ea]{line-height: 30px;\n}\n.ftp-msg > input[data-v-256e98ea]{height: 28px;width: 280px;border: 1px solid #ccc;border-radius: 3px;text-indent: 4px;\n}\n.am-warn[data-v-256e98ea]{height: 20px;line-height: 20px;text-indent: 84px;color: red;\n}\n.ctpc-btns[data-v-256e98ea]{text-align: right;\n}\n.ctpc-btns > input[data-v-256e98ea]{display: inline-block;width: 80px;height: 28px;margin-left: 12px;cursor: pointer;border-radius: 4px;\n}\n.ctpc-cancel[data-v-256e98ea]{background: #fff;border: 1px solid #ccc;\n}\n.ctpc-cancel[data-v-256e98ea]:hover{background: #fff;border: 1px solid #36A8FF;color: #36A8FF;font-weight: 700;\n}\n.ctpc-save[data-v-256e98ea]{background: #36A8FF;color: #fff;\n}\r\n\r\n\r\n\r\n", ""]);

// exports


/***/ }),
/* 238 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.myDialog {\n    width: 600px;\n}\n", ""]);

// exports


/***/ }),
/* 239 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.pagination[data-v-412fc1af] {\n    margin: 20px 0;\n    text-align: right;\n}\n.nav-index-con[data-v-412fc1af] {\n    width: 1100px;\n    margin: auto;\n}\n.mic-title[data-v-412fc1af] {\n    font-size: 18px;\n    color: #000;\n    position: relative;\n}\n.mic-item[data-v-412fc1af] {\n    margin: 18px 12px;\n    width: 200px;\n    text-align: center;\n    box-shadow: 0 0 20px #ccc;\n}\n.mic-item-title[data-v-412fc1af] {\n    line-height: 40px;\n    background: #D7D7D7;\n    font-size: 20px;\n}\n.mic-item-integral[data-v-412fc1af] {\n    font-size: 26px;\n    color: #000;\n    background: #fff;\n    line-height: 30px;\n    padding: 14px 0;\n    word-wrap: break-word;\n}\n.mic-item[data-v-412fc1af]:first-child {\n    margin-left: 0;\n}\n.mic-item[data-v-412fc1af]:last-child {\n    margin-right: 0;\n}\n.my-integral-con[data-v-412fc1af], .my-task-con[data-v-412fc1af] {\n    margin-top: 16px;\n    position: relative;\n}\n.my-task-detail[data-v-412fc1af] {\n    margin-top: 6px;\n}\n.add-task[data-v-412fc1af] {\n    position: absolute;\n    right: 20px;\n    font-size: 16px;\n    cursor: pointer;\n    color: #36A8FF;\n    z-index: 30;\n}\n.add-task > span[data-v-412fc1af] {\n    display: inline-block;\n    vertical-align: middle;\n}\n.add-task-icon[data-v-412fc1af] {\n    width: 22px;\n    height: 22px;\n    line-height: 22px;\n    border-radius: 50%;\n    background: #36A8FF;\n    color: #fff;\n    text-align: center;\n    font-size: 14px;\n    cursor: pointer;\n}\n", ""]);

// exports


/***/ }),
/* 240 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.intergral-con[data-v-42ed7ea2] {\n    width: 1100px;\n    margin: auto;\n}\n.intergral-con-top[data-v-42ed7ea2] {\n    font-size: 14px;\n    background: #fff;\n    padding: 16px 0;\n    text-indent: 10px;\n    box-shadow: 0 0 10px #ccc;\n    margin: 0 10px;\n}\n.menu-list[data-v-42ed7ea2] {\n    line-height: 36px;\n    padding: 0 4px;\n    margin-right: 10px;\n    cursor: pointer;\n}\n.div-line[data-v-42ed7ea2] {\n    margin: 0 10px 0 12px;\n    color: #000;\n}\n.self-define[data-v-42ed7ea2] {\n    margin-right: 14px;\n}\n.serch-btn[data-v-42ed7ea2] {\n    vertical-align: middle;\n    margin-left: 10px;\n    cursor: pointer;\n}\n.intergral-data-detail[data-v-42ed7ea2] {\n    margin: 20px 8px;\n}\n", ""]);

// exports


/***/ }),
/* 241 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.stats-con[data-v-52cc4f8f] {\n    width: 1100px;\n    font-size: 14px;\n    background: #fff;\n    padding: 30px 0;\n    text-indent: 10px;\n    box-shadow: 0 0 10px #ccc;\n    margin: auto;\n}\n.el-tag[data-v-52cc4f8f]{\n    margin-left: 16px;\n}\n\n\n", ""]);

// exports


/***/ }),
/* 242 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.intergralHistory-con-top[data-v-5f256490] {\n  font-size: 14px;\n  background: #fff;\n  padding: 30px 0;\n  text-indent: 10px;\n  box-shadow: 0 0 10px #ccc;\n  /*margin: 0 10px;*/\n  width: 1300px;\n  margin: auto;\n}\n.intergralHistory-con[data-v-5f256490]{\n  width: 1300px;\n  margin: auto;\n}\n.menu-list[data-v-5f256490] {\n  line-height: 36px;\n  padding: 0 4px;\n  margin-right: 10px;\n  cursor: pointer;\n}\n.div-line[data-v-5f256490] {\n  margin: 0 10px 0 12px;\n  color: #000;\n}\n.serch-btn[data-v-5f256490] {\n  vertical-align: middle;\n  margin-left: 10px;\n  cursor: pointer;\n}\n", ""]);

// exports


/***/ }),
/* 243 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.stepActive[data-v-6573cde1] {\n    box-shadow: 0 0 10px #20A0FF !important;\n}\n.star[data-v-6573cde1] {\n    color: red;\n    padding: 1px;\n}\n.create-task-pop[data-v-6573cde1] { /* display: none; */\n    position: fixed;\n    top: 0;\n    left: 0;\n    bottom: 0;\n    right: 0;\n    z-index: 100;\n    background: rgba(0, 0, 0, 0.5);\n}\n.create-task-pop-con[data-v-6573cde1] {\n    position: absolute;\n    background: #fff;\n    left: 50%;\n    top: 50%;\n    transform: translate(-50%, -50%);\n    width: 546px;\n    padding: 16px 26px 1px;\n    height: 566px;\n}\n.ctpc-top[data-v-6573cde1] {\n    font-size: 16px;\n    line-height: 30px;\n    font-weight: 700;\n    color: #333;\n    margin-bottom: 6px;\n}\n.ctpc-top-close[data-v-6573cde1] {\n    font-size: 30px;\n    width: 30px;\n    height: 30px;\n    line-height: 27px;\n    cursor: pointer;\n    transition: 0.8s ease all;\n    text-align: center;\n}\n.ctpc-top-close[data-v-6573cde1]:hover {\n    color: #36A8FF;\n    transform: rotate(360deg);\n}\n.ctpc-instruction[data-v-6573cde1] {\n    background: #E4E4E4;\n    border-radius: 3px;\n    padding: 6px 10px;\n    margin: 6px 0;\n    line-height: 22px;\n    font-size: 14px;\n    color: #000;\n    width: 504px;\n}\n.ctpc-main-con[data-v-6573cde1] {\n    border-top: 1px solid #ccc;\n    padding: 10px 0;\n    margin-top: 10px;\n}\n.ctpc-list-con .el-select[data-v-6573cde1] {\n    width: 188px;\n}\n.ctpc-list-menu[data-v-6573cde1] {\n    line-height: 30px;\n    width: 80px;\n    font-size: 14px;\n}\n.ctpc-list-con[data-v-6573cde1] {\n    width: 410px;\n}\n.ctpc-list[data-v-6573cde1] {\n    background: #fff;\n    padding: 6px 10px; /* border: 1px solid #ccc; */\n    margin-bottom: 10px;\n}\n.ctpc-tags[data-v-6573cde1] {\n    position: relative;\n}\n.ctpc-tags > li[data-v-6573cde1] {\n    display: inline-block;\n    line-height: 30px;\n}\n.ctpc-tag-lis[data-v-6573cde1] {\n    background: #F2F2F2;\n    padding: 0 16px;\n    border-radius: 15px;\n    margin-right: 14px;\n    margin-bottom: 14px;\n    box-shadow: 0 0 10px #ccc;\n}\n.ctpc-tag-lis > span[data-v-6573cde1] {\n    display: inline-block;\n    vertical-align: middle;\n    font-size: 14px;\n}\n.ctpc-tag-lis .tag-lis-delete[data-v-6573cde1]:hover {\n    text-shadow: 0 0 8px #ccc;\n    font-size: 18px !important;\n}\n.tag-lis-circle[data-v-6573cde1] {\n    width: 8px;\n    height: 8px;\n    border-radius: 50%;\n    background: #FF9D6E;\n    margin-right: 4px;\n}\n.tag-lis-delete[data-v-6573cde1] {\n    font-size: 18px !important;\n    padding: 0 2px;\n    cursor: pointer;\n}\n.tag-lis-add[data-v-6573cde1] {\n    position: relative;\n    /*margin-left: 10px;*/\n    cursor: pointer;\n}\n.tag-lis-add-msg[data-v-6573cde1] {\n    color: #36A8FF;\n}\n.ctpc-member-con[data-v-6573cde1] {\n    padding-left: 10px; /* border-left: 1px solid #ccc; */\n    margin-left: 6px;\n    position: relative;\n}\n.ctpc-member-list[data-v-6573cde1] {\n    height: 42px;\n    background: #fff;\n    border: 1px solid #ccc;\n    line-height: 42px;\n    color: #000;\n    padding: 0 4px;\n    position: relative;\n    margin-bottom: 10px;\n    box-shadow: 0 0 10px #ccc;\n    display: -webkit-flex;\n    display: -moz-flex;\n    display: -ms-flex;\n    display: -o-flex;\n    display: flex;\n}\n.ctpc-member-list.done[data-v-6573cde1]:before {\n    content: '';\n    position: absolute;\n    left: -17px;\n    top: 12px;\n    width: 12px;\n    height: 12px;\n    border-radius: 50%;\n    background: #008000;\n    z-index: 110;\n}\n.ctpc-member-list.in[data-v-6573cde1]:before {\n    content: '';\n    position: absolute;\n    left: -17px;\n    top: 12px;\n    width: 12px;\n    height: 12px;\n    border-radius: 50%;\n    background: #e4e8f1;\n    z-index: 110;\n}\n.ctpc-member-list > span[data-v-6573cde1] {\n    display: inline-block;\n    vertical-align: middle;\n}\n.ctpc-member-job[data-v-6573cde1] {\n    width: 110px;\n    /*  -webkit-flex: 1;\n      -moz-flex: 1;\n      -ms-flex: 1;\n      -o-flex: 1;\n      flex: 1;*/\n}\n.ctpc-member-job-time[data-v-6573cde1] {\n    width: 110px;\n}\n.ctpc-member-end-time[data-v-6573cde1] {\n    /*width: 110px;*/\n}\n.ctpc-member-assess[data-v-6573cde1] {\n    width: 70px;\n}\n.ctpc-member-head[data-v-6573cde1] {\n    width: 36px;\n    height: 36px;\n    border-radius: 50%;\n    background: #006699;\n    color: #fff;\n    font-size: 10px;\n    text-align: center;\n    line-height: 36px;\n    margin-top: 3px;\n    overflow: hidden;\n    margin-right: 10px;\n}\n.bdl-line[data-v-6573cde1] {\n    position: absolute;\n    left: 0;\n    bottom: 20px;\n    top: 14px;\n    border-left: 1px solid #ccc;\n}\n.add-member-opt[data-v-6573cde1] {\n    cursor: pointer;\n    margin: 20px 10px;\n    width: 80px;\n}\n.add-member-opt > span[data-v-6573cde1] {\n    display: inline-block;\n    vertical-align: middle;\n}\n.add-member-icon[data-v-6573cde1] {\n    background: #36A8FF;\n    color: #fff;\n    width: 20px;\n    height: 20px;\n    text-align: center;\n    font-size: 20px;\n    line-height: 16px;\n    font-weight: bold;\n    border-radius: 50%;\n}\n.add-member-msg[data-v-6573cde1] {\n    color: #36A8FF;\n    margin-left: 6px;\n}\n.ctpc-btns[data-v-6573cde1] {\n    text-align: right;\n    margin-top: 20px;\n}\n.ctpc-btns > input[data-v-6573cde1] {\n    display: inline-block;\n    width: 80px;\n    height: 28px;\n    margin-left: 12px;\n    cursor: pointer;\n    border-radius: 4px;\n}\n.ctpc-cancel[data-v-6573cde1] {\n    background: #fff;\n    border: 1px solid #ccc;\n}\n.ctpc-cancel[data-v-6573cde1]:hover {\n    background: #fff;\n    border: 1px solid #36A8FF;\n    color: #36A8FF;\n    font-weight: 700;\n}\n.ctpc-save[data-v-6573cde1] {\n    background: #36A8FF;\n    color: #fff;\n}\n.ctpc-con[data-v-6573cde1] {\n    height: 500px;\n    overflow-y: scroll;\n    padding-right: 10px;\n}\n.ctpc-con[data-v-6573cde1]::-webkit-scrollbar {\n    width: 10px;\n    background-color: #fff;\n}\n.ctpc-con[data-v-6573cde1]::-webkit-scrollbar-thumb {\n    background-color: rgb(255, 255, 255);\n    background-image: -webkit-gradient(linear, 40% 0%, 75% 84%, from(rgb(54, 168, 255)), color-stop(0.6, rgb(54, 229, 255)), to(rgb(54, 192, 255)));\n    border-radius: 10px;\n}\n.ctpc-con[data-v-6573cde1]::-webkit-scrollbar-track {\n    /* -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.1); */\n    background-color: #fff;\n    border-radius: 10px;\n}\n.ctpc-instruction-msg[data-v-6573cde1] {\n    padding: 4px;\n    cursor: pointer;\n}\n.ctpc-instruction-msg[data-v-6573cde1]:hover {\n    color: #3DA8F5;\n    background: #F5F5F5;\n}\n.add-member-remark[data-v-6573cde1] {\n    border: 1px solid #ccc;\n    display: block;\n    padding: 6px;\n    width: 512px;\n}\n.add-member-basic[data-v-6573cde1] {\n    background: #fff;\n    border: 1px solid #ccc;\n    margin-top: 10px;\n    padding: 10px;\n    font-size: 14px;\n}\n.add-member-basic-menu[data-v-6573cde1] {\n    width: 82px;\n    text-align: right;\n}\n.add-member-basic-list[data-v-6573cde1] { /* border-bottom: 1px solid #ccc; */\n    padding: 5px 0;\n    line-height: 34px;\n}\n.add-member-basic-list[data-v-6573cde1]:last-child {\n    border: none;\n}\n.add-stage-opt[data-v-6573cde1] {\n    color: #36A8FF;\n    cursor: pointer;\n    width: 400px;\n}\n.add-member-basic-msg .el-select[data-v-6573cde1] {\n    width: 140px;\n}\n.member-time-count[data-v-6573cde1] {\n    width: 40px;\n    border: 1px solid #ccc;\n    height: 26px;\n    border-radius: 4px;\n    margin-right: 4px;\n    text-indent: 4px;\n}\n.add-member-basic-time[data-v-6573cde1] {\n    margin-left: 40px;\n}\n.add-member-basic-msg .el-date-editor.el-input[data-v-6573cde1] {\n    width: 140px;\n}\n.add-member-basic-end[data-v-6573cde1] {\n    margin-left: 40px;\n}\n.tag-add-sel .el-select[data-v-6573cde1] {\n    width: 188px;\n}\n.tag-add-sel[data-v-6573cde1] {\n    /*position: absolute;*/\n    left: -10px;\n    top: 0;\n    z-index: 100;\n}\n.add-tag-btn[data-v-6573cde1] {\n    background: #36A8FF;\n    color: #fff;\n    position: absolute;\n    right: 0;\n    top: 50%;\n    width: 40px;\n    height: 30px;\n    z-index: 200;\n    text-align: center;\n    transform: translateY(-50%);\n    border-radius: 4px;\n}\n.ctpc-member-delete[data-v-6573cde1] {\n    font-size: 26px;\n    cursor: pointer;\n    margin-right: 4px;\n}\n.add-member-stage[data-v-6573cde1] {\n    position: relative;\n}\n.ctpc-ins-edit[data-v-6573cde1] {\n    margin-right: 10px;\n}\n.ctpc-ins-edit .ctpc-btns[data-v-6573cde1] {\n    margin-top: 0;\n}\n", ""]);

// exports


/***/ }),
/* 244 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.toggle-view[data-v-65c5fa76] {\n    width:1080px;\n    margin: 0 auto 10px;\n}\n.toggle-view input[data-v-65c5fa76] {\n    display: block;\n    background-color: #20a0ff;\n    color: #fff;\n    line-height: 30px;\n    padding: 0 10px;\n    cursor: pointer;\n}\n.tag-name[data-v-65c5fa76] {\n    width: 980px;\n}\n.tag-name button[data-v-65c5fa76] {\n    margin: 0 10px 10px 0;\n}\n.tag-name button[data-v-65c5fa76]:focus {\n    color: #545454;\n    border-color: #c4c4c4;\n}\n.tag-name button.active[data-v-65c5fa76] {\n    color: #36A8FF;\n    border-color: #36A8FF;\n}\n.select-box > div[data-v-65c5fa76]:nth-child(3n) {\n    margin-right: 200px;\n}\n.filter-btn[data-v-65c5fa76] {\n    text-align: center;\n    background-color: #f2f2f2;\n}\n.filter-btn span[data-v-65c5fa76] {\n    /*position: relative;*/\n    display: inline-block;\n    font-size: 14px;\n    line-height: 36px;\n    color: #36A8FF;\n    cursor: pointer;\n}\n.filter-btn span[data-v-65c5fa76]:after {\n    content: \"\";\n    display: inline-block;\n    margin-left: 10px;\n    margin-bottom: -1px;\n    width: 8px;\n    height: 8px;\n    border-left: 1px solid #36A8FF;\n    border-top: 1px solid #36A8FF;\n    transform: rotate(45deg);\n    /*position: absolute;*/\n}\n.filter-btn span.open[data-v-65c5fa76]:after {\n    margin-bottom: 2px;\n    transform: rotate(-135deg);\n}\n.filter-enter[data-v-65c5fa76], .filter-leave-active[data-v-65c5fa76] {\n    opacity: 0;\n    /*transform: translate3d(0,20%,0);*/\n}\n.filter-enter-active[data-v-65c5fa76], .filter-leave-active[data-v-65c5fa76] {\n    transition: all .2s ease;\n}\n.pagination[data-v-65c5fa76] {\n    margin: 20px 0;\n    text-align: right;\n}\n.task-con[data-v-65c5fa76] {\n    width: 1100px;\n    margin: auto;\n}\n.task-top[data-v-65c5fa76] {\n    /*position: fixed;*/\n    /*transform: translateX(-50%);*/\n    /*top: 80px;*/\n    /*width: 1080px;*/\n    /*left: 50%;*/\n    background: #fff;\n    padding-top: 20px;\n    /*padding-bottom: 50px;*/\n    border-radius: 4px;\n    box-shadow: 0 0 10px #ccc;\n    margin-bottom: 24px;\n}\n.task-top-list[data-v-65c5fa76] {\n    margin: 0 20px 20px 20px;\n}\n.task-top-list[data-v-65c5fa76]:last-child {\n    /*margin-top: 16px;*/\n}\n.task-top-list .el-select[data-v-65c5fa76] {\n    width: 148px;\n}\n.ttl-name[data-v-65c5fa76] {\n    margin-right: 10px;\n    font-size: 14px;\n}\n.div-line[data-v-65c5fa76] {\n    margin-left: 10px;\n    margin-right: 6px;\n}\n.serch-btn[data-v-65c5fa76] {\n    vertical-align: middle;\n    margin-left: 10px;\n}\n.creat-task[data-v-65c5fa76], .search-button[data-v-65c5fa76] {\n    margin-left: 63px;\n    cursor: pointer;\n    /* position: absolute;\n    right: 0;\n    bottom: 0; */\n}\n.creat-task[data-v-65c5fa76] {\n    margin-left: 30px;\n    /*  position: absolute;\n    right: 160px;\n    bottom: 0; */\n}\n.creat-task > span[data-v-65c5fa76] {\n    display: inline-block;\n    vertical-align: middle;\n}\n.ttl-add-msg[data-v-65c5fa76] {\n    font-size: 16px;\n    color: #36A8FF;\n    margin-left: 10px;\n}\n.ttl-add-icon[data-v-65c5fa76] {\n    width: 24px;\n    height: 24px;\n    line-height: 22px;\n    text-align: center;\n    font-size: 24px;\n    border-radius: 50%;\n    color: #fff;\n    background: #36A8FF;\n}\n.task-lis-con[data-v-65c5fa76] {\n    /*max-height: 150px;*/\n    /*overflow-y: auto;*/\n    /*margin-top: 200px;*/\n}\n", ""]);

// exports


/***/ }),
/* 245 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.form-input .el-input__inner{height: 56px!important;\n}\ninput:-webkit-autofill {  \n    -webkit-box-shadow: 0 0 0px 1000px white inset !important;\n}\ninput{outline: none;\n}\n", ""]);

// exports


/***/ }),
/* 246 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.login[data-v-67e10559]{\n    height: 100%;\n    background: url(" + __webpack_require__(284) + ") no-repeat;\n    -webkit-background-size: 100% 100%;\n    background-size: 100% 100%;\n}\n/*  .login-title{\n   padding-left: 40px;\n   height: 70px;\n   line-height: 70px;\n   font-size: 22px;\n   background-color: #fff;\n   background-color: rgba(255,255,255,.9);\n   border-bottom:5px solid #20a0ff;\n   color: #20a0ff;\n   font-family: \"黑体\"\n    }  */\n.login-container[data-v-67e10559] {\n    position: absolute;\n    right: 12%;\n    top:50%;\n    transform: translateY(-50%);\n    box-shadow: 0 0px 8px 0 rgba(0, 0, 0, 0.06), 0 1px 0px 0 rgba(0, 0, 0, 0.02);\n    /*-webkit-border-radius: 5px;*/\n    /*border-radius: 5px;*/\n    /*-moz-border-radius: 5px;*/\n    background-clip: padding-box;\n    /*margin: 200px auto;*/\n    width: 385px;\n    /*padding: 0 35px 10px 35px;*/\n    background: rgba(255,255,255,.6);\n    border: 1px solid #eaeaea;\n    box-shadow: 0 0 25px #cac6c6;\n    box-sizing:border-box;\n}\n.login-container .title[data-v-67e10559] {/*  margin: 0px auto 40px auto;*/ font-family: \"\\9ED1\\4F53\";padding:0 20px; font-size: 22px;line-height:58px;  text-align: left;color: #000; border-bottom: 1px solid #a0a0a0;\n}\n.login-container .title em[data-v-67e10559]{font-size: 14px;\n}\n.login-container .remember[data-v-67e10559] {  margin: 0px 0px 35px 0px;\n}\n.form-items[data-v-67e10559]{ padding:42px;border-top:1px solid #fff;\n}\n.form-input[data-v-67e10559]{height: 56px;\n}\n\n\n", ""]);

// exports


/***/ }),
/* 247 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n#app{\r\n\t height: 100vh;\n}\r\n", ""]);

// exports


/***/ }),
/* 248 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.project-con[data-v-a77519ee]{width: 1100px;margin: auto;\n}\n.mic-title[data-v-a77519ee]{font-size: 18px;color: #000;position: relative;margin: 0 10px;\n}\n.task-item[data-v-a77519ee],.add-task-item[data-v-a77519ee]{background: #fff;display: -webkit-flex;display: -moz-flex;display: -ms-flex;display: -o-flex;display: flex;padding: 14px 16px;margin: 16px 10px;cursor: pointer;\n}\n.task-item[data-v-a77519ee]:hover,.add-task-item[data-v-a77519ee]:hover{box-shadow: 0 0 20px #ccc;\n}\n.task-info[data-v-a77519ee]{flex: 1;-webkit-flex:1;-moz-flex:1;-ms-flex:1;-o-flex:1;\n}\n.task-name[data-v-a77519ee]{font-size: 18px;line-height: 26px;\n}\n.task-sub-name[data-v-a77519ee]{font-size: 12px;color: #999999;line-height: 20px;\n}\n.task-logo[data-v-a77519ee]{margin-right: 20px;height: 30px;margin-top: 6px;\n}\n.add-task-btn[data-v-a77519ee]{width: 40px;height: 40px;background: #F2F2F2;color: #CCCCCC;line-height: 34px;text-align: center;font-size: 36px;\n}\n.add-task-msg[data-v-a77519ee]{line-height: 40px;margin-left: 26px;font-size: 14px;color: #999999;\n}\n.add-task-pop[data-v-a77519ee]{position: fixed;top: 0;left: 0;bottom: 0;right: 0;background: rgba(0,0,0,0.5);z-index: 110;\n}\n.add-task-pop-con[data-v-a77519ee]{position: absolute;left: 50%;top: 50%;transform: translate(-50%,-50%);width: 350px;height: 400px;background: #fff;\n}\n.add-task-top[data-v-a77519ee]{text-align: center;font-size: 16px;font-weight: bold;margin: 10px;border-bottom: 1px solid #ccc;line-height: 30px;padding-bottom: 10px;position: relative;\n}\n.add-task-top .close[data-v-a77519ee]{position: absolute;right: 0;font-size: 28px;font-weight: normal;color: #999;cursor: pointer;transition:0.8s ease all;width: 30px;height: 30px;text-align: center;line-height: 24px;\n}\n.add-task-top .close[data-v-a77519ee]:hover{color:#36A8FF;transform:rotate(360deg);\n}\n.att-img[data-v-a77519ee]{margin: 16px 0;\n}\n.att-msg[data-v-a77519ee]{text-align: center;color: #A7A7A7;margin-bottom: 24px;\n}\n.project-name[data-v-a77519ee],.project-intro[data-v-a77519ee]{width: 90%;border: 1px solid #ccc;height: 36px;line-height: 36px;display: block;margin: 16px auto;border-radius: 3px;text-indent: 10px;font-size: inherit;\n}\n.att-bents[data-v-a77519ee]{text-align: right;margin: 20px;\n}\n.att-bents > span[data-v-a77519ee]{display: inline-block;width: 60px;height: 26px;text-align: center;line-height: 26px;font-size: 14px;cursor: pointer;border-radius: 4px;\n}\n.cancel[data-v-a77519ee]{margin-right: 10px;border: 1px solid #D0D3D3;\n}\n.cancel[data-v-a77519ee]:hover{background: #fff;border: 1px solid #36A8FF;color: #36A8FF;font-weight: 700;\n}\n.save[data-v-a77519ee]{background: #36A8FF;color: #fff;\n}\n.delete[data-v-a77519ee]{background: #FF4949;color: #fff;float:left;\n}\r\n\r\n\r\n\r\n\r\n\r\n", ""]);

// exports


/***/ }),
/* 249 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.nav-top-bg[data-v-c38d3b7c]{position: fixed;left: 0;top: 0;right: 0;background: #fff;box-shadow: 0 0 10px #ccc;height: 61px;z-index: 100;\n}\n.nav-top[data-v-c38d3b7c]{position: relative;width: 1100px;margin: auto;\n}\n.logo[data-v-c38d3b7c]{position: absolute;z-index: 100;width: 40px;height: 40px;left: 0;top: 10px;/*cursor: pointer;*/\n}\n.personal-name[data-v-c38d3b7c]{position: absolute;right: 0;width: 40px;height: 40px;background: #69C8FA;border-radius: 50%;line-height: 40px;text-align: center;top: 10px;color: #fff;cursor: pointer;z-index: 100;\n}\n.personal-opt[data-v-c38d3b7c]{position: absolute;color: #000;background: #fff;width: 120px;box-shadow: 0 0 10px #ccc;top: 56px;right: -36px;z-index: 30;padding: 16px 0;\n}\n.personal-opt > div[data-v-c38d3b7c]{cursor: pointer;line-height: 30px;\n}\n.personal-opt > div[data-v-c38d3b7c]:hover{background: #69C8FA;color: #fff;\n}\n.ntb-con[data-v-c38d3b7c]{position: absolute;width: 1080px;height: 61px;left: 50%;margin-left: -540px;\n}\r\n", ""]);

// exports


/***/ }),
/* 250 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.task-title-detail{\n    margin-top:-5px;\n    line-height: 20px;\n    font-size:12px;\n    color: #ccc;\n}\n.task-title-detail em{\n    margin-right: 5px;\n    border-left:3px solid #ccc;\n}\n.myDialog {\n    width: 600px;\n}\n.my-dialog-title {\n    font-size: 16px;\n    font-weight: bold;\n}\n.my-dialog-title-tool {\n    float: right;\n}\n.red-border {\n    border-left: red 3px solid;\n}\n.orange-border {\n    border-left: orange 3px solid;\n}\n.task-username {\n    height: 40px;\n    background: #69C8FA;\n    border-radius: 50%;\n    line-height: 40px;\n    text-align: center;\n    color: #fff;\n    cursor: pointer;\n    /*z-index: 100;*/\n    margin-top: 24px;\n    margin-right: 20px;\n    width: 40px;\n}\n\n\n", ""]);

// exports


/***/ }),
/* 251 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.demo-table-expand {\n    font-size: 0;\n}\n.demo-table-expand label {\n    width: 60px;\n    color: #99a9bf;\n}\n.demo-table-expand .el-form-item {\n    margin-right: 0;\n    margin-bottom: 0 !important;\n    width: 50%;\n}\n.task-form {\n    margin-bottom: 0;\n}\n.task-form-edit {\n    margin-bottom: 10px;\n}\n.el-dialog__body {\n    padding: 20px !important;\n}\n", ""]);

// exports


/***/ }),
/* 252 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.stepActive[data-v-e45d7c10] {\n    box-shadow: 0 0 10px #20A0FF !important;\n}\n.mark-stage[data-v-e45d7c10] {\n    line-height: 90px;\n    margin-right: 20px;\n    font-size: 15px;\n}\n.star[data-v-e45d7c10] {\n    color: red;\n    padding: 1px;\n}\n.empty[data-v-e45d7c10] {\n    text-align: center;\n    font-size: 16px;\n    padding: 20px;\n}\n.task-lis[data-v-e45d7c10] {\n    background: #fff;\n    display: -webkit-flex;\n    display: -moz-flex;\n    display: -ms-flex;\n    display: -o-flex;\n    display: flex;\n    margin-bottom: 20px;\n    margin: 10px 0 20px;\n    cursor: pointer;\n}\n.task-lis[data-v-e45d7c10]:hover {\n    box-shadow: 0 0 10px #ccc;\n}\n.head-img[data-v-e45d7c10] {\n    width: 60px;\n    height: 60px;\n    /*border-radius: 50%;*/\n    overflow: hidden;\n    margin: 16px;\n}\n.main-task-detail[data-v-e45d7c10] {\n    flex: 1;\n    -webkit-flex: 1;\n    -moz-flex: 1;\n    -ms-flex: 1;\n    -o-flex: 1;\n}\n.task-mark[data-v-e45d7c10] {\n    line-height: 90px;\n    font-size: 18px;\n}\n.task-mark > img[data-v-e45d7c10], .task-mark > span[data-v-e45d7c10] {\n    vertical-align: middle;\n}\n.task-mark > span[data-v-e45d7c10] {\n    margin-right: 20px;\n    margin-left: 10px;\n}\n.task-name[data-v-e45d7c10] {\n    margin-top: 18px;\n    font-size: 16px;\n}\n.task-state[data-v-e45d7c10] {\n    margin-top: 10px;\n}\n.task-state .iconfont[data-v-e45d7c10] {\n    margin-right: 5px;\n}\n.task-state > span[data-v-e45d7c10], .task-data-show > span[data-v-e45d7c10] {\n    display: inline-block;\n    vertical-align: middle;\n}\n.task-end[data-v-e45d7c10] {\n    padding: 2px 10px;\n    color: #fff;\n}\n.task-end.red[data-v-e45d7c10] {\n    background: #FF4515;\n}\n.task-end.orange[data-v-e45d7c10] {\n    background: #FF9900;\n}\n.task-end.blue[data-v-e45d7c10] {\n    background: #36A8FF;\n}\n.task-end.green[data-v-e45d7c10] {\n    background: #339933;\n}\n.task-time-opt[data-v-e45d7c10] {\n    color: #36A8FF;\n    font-size: 20px;\n    margin-left: 16px;\n    cursor: pointer;\n}\n.task-data-show[data-v-e45d7c10] {\n    margin: 0 40px 0 20px;\n}\n.task-score[data-v-e45d7c10] {\n    font-size: 18px;\n    line-height: 92px;\n}\n.task-level[data-v-e45d7c10] {\n    width: 44px;\n    height: 44px;\n    line-height: 44px;\n    text-align: center;\n    border-radius: 50%;\n    color: #fff;\n    font-size: 20px;\n    margin-left: 16px;\n}\n.task-level.first[data-v-e45d7c10] {\n    background: #FF9900;\n}\n.task-level.second[data-v-e45d7c10] {\n    background: #99CC66;\n}\n.task-level.third[data-v-e45d7c10] {\n    background: #999900;\n}\n.task-level.forth[data-v-e45d7c10] {\n    background: #9993F1;\n}\n.task-key-tag[data-v-e45d7c10] {\n    margin-left: 10px;\n    display: inline-block;\n    vertical-align: middle;\n}\n.task-key-tag li[data-v-e45d7c10] {\n    display: inline-block;\n    margin-right: 20px;\n}\n.task-key-lis > span[data-v-e45d7c10] {\n    display: inline-block;\n    vertical-align: middle;\n}\n.task-key-lis .circle[data-v-e45d7c10] {\n    width: 8px;\n    height: 8px;\n    border-radius: 50%;\n    margin-right: 4px;\n}\n.task-key-lis .circle.pink[data-v-e45d7c10] {\n    background: #FF9966;\n}\n.task-key-lis .circle.purple[data-v-e45d7c10] {\n    background: #D863B0;\n}\n.task-key-lis .circle.red[data-v-e45d7c10] {\n    background: #CC0000;\n}\n.task-key-lis .circle.gray[data-v-e45d7c10] {\n    background: #999999;\n}\n.task-key-lis .circle.blue[data-v-e45d7c10] {\n    background: #0E0E9D;\n}\n", ""]);

// exports


/***/ }),
/* 253 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.ctpc-member-con[data-v-e45d7c10] {\n    margin: 15px 0;\n    padding-left: 10px; /* border-left: 1px solid #ccc; */\n    margin-left: 6px;\n    position: relative;\n}\n.trends[data-v-e45d7c10] {\n    /*background-color: #f2f2f2; */\n    /*padding-left: 10px;*/\n    line-height: 30px;\n    border: 1px solid #e4e8f1;\n}\n.trends ul[data-v-e45d7c10]{\n    padding-left: 10px;\n    list-style: circle;\n}\n.trends li[data-v-e45d7c10]{\n    /*list-style: circle!important;*/\n}\n.trends li[data-v-e45d7c10]:before{\n    content:\"*\";\n    float: left;\n    margin-right: 5px;\n    color: #f40;\n}\n.trends-title[data-v-e45d7c10] {\n    padding: 0 10px;\n    line-height: 30px;\n    background-color: #e4e8f1;\n}\n.trends-title a[data-v-e45d7c10] {\n    color: #20a0ff;\n}\n.ctpc-member-list[data-v-e45d7c10] {\n    height: 42px;\n    background: #fff;\n    border: 1px solid #ccc;\n    line-height: 42px;\n    color: #000;\n    padding: 0 4px;\n    position: relative;\n    margin-bottom: 10px;\n    box-shadow: 0 0 10px #ccc;\n    display: -webkit-flex;\n    display: -moz-flex;\n    display: -ms-flex;\n    display: -o-flex;\n    display: flex;\n}\n.ctpc-member-list.done[data-v-e45d7c10]:before {\n    content: '';\n    position: absolute;\n    left: -17px;\n    top: 12px;\n    width: 12px;\n    height: 12px;\n    border-radius: 50%;\n    background: #008000;\n    z-index: 110;\n}\n.ctpc-member-list.in[data-v-e45d7c10]:before {\n    content: '';\n    position: absolute;\n    left: -17px;\n    top: 12px;\n    width: 12px;\n    height: 12px;\n    border-radius: 50%;\n    background: #e4e8f1;\n    z-index: 110;\n}\n.ctpc-member-list > span[data-v-e45d7c10] {\n    display: inline-block;\n    vertical-align: middle;\n}\n.ctpc-member-job[data-v-e45d7c10] {\n    width: 110px;\n    /*  -webkit-flex: 1;\n      -moz-flex: 1;\n      -ms-flex: 1;\n      -o-flex: 1;\n      flex: 1;*/\n}\n.ctpc-member-job-time[data-v-e45d7c10] {\n    width: 110px;\n}\n.ctpc-member-end-time[data-v-e45d7c10] {\n    /*width: 110px;*/\n}\n.ctpc-member-assess[data-v-e45d7c10] {\n    width: 70px;\n    margin-left: 30px;\n}\n.ctpc-member-head[data-v-e45d7c10] {\n    width: 36px;\n    height: 36px;\n    border-radius: 50%;\n    background: #006699;\n    color: #fff;\n    font-size: 10px;\n    text-align: center;\n    line-height: 36px;\n    margin-top: 3px;\n    overflow: hidden;\n    margin-right: 10px;\n}\n\n", ""]);

// exports


/***/ }),
/* 254 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.assess-man-detail[data-v-e45d7c10] {\n    background: #fff;\n    border: 1px solid #ccc;\n    padding: 2px 6px;\n    line-height: 36px;\n    margin-right: 10px;\n}\n.assess-man-detail > span[data-v-e45d7c10] {\n    display: inline-block;\n}\n.amd-job[data-v-e45d7c10] {\n    width: 100px;\n}\n.amd-job-time[data-v-e45d7c10] {\n    width: 120px;\n}\n.amd-during-time[data-v-e45d7c10] {\n    width: 200px;\n}\n.amd-name[data-v-e45d7c10] {\n    width: 36px;\n    height: 36px;\n    text-align: center;\n    line-height: 36px;\n    border-radius: 50%;\n    background: #006699;\n    color: #fff;\n    font-size: 10px;\n}\n.assess-level-opt > span[data-v-e45d7c10] {\n    display: inline-block;\n    vertical-align: middle;\n    line-height: 36px;\n}\n.assess-msg-inp[data-v-e45d7c10] {\n    border: 1px solid #ccc;\n    padding: 10px;\n    width: 496px;\n}\n.alo-radio > input[data-v-e45d7c10], .alo-radio > label[data-v-e45d7c10] {\n    cursor: pointer;\n}\n\n/*.assess-task-list{margin-top: 16px;}*/\n/*.assess-task{height: 500px;overflow-y: scroll;}*/\n.assess-task[data-v-e45d7c10]::-webkit-scrollbar {\n    width: 10px;\n    background-color: #fff;\n}\n.assess-task[data-v-e45d7c10]::-webkit-scrollbar-thumb {\n    background-color: rgb(255, 255, 255);\n    background-image: -webkit-gradient(linear, 40% 0%, 75% 84%, from(rgb(54, 168, 255)), color-stop(0.6, rgb(54, 229, 255)), to(rgb(54, 192, 255)));\n    border-radius: 10px;\n}\n.assess-task[data-v-e45d7c10]::-webkit-scrollbar-track {\n    /* -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.1); */\n    background-color: #fff;\n    border-radius: 10px;\n}\n", ""]);

// exports


/***/ }),
/* 255 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.ctpc-list-con .el-select[data-v-e45d7c10] {\n    width: 188px;\n}\n.ctpc-tags[data-v-e45d7c10] {\n    position: relative;\n}\n.ctpc-tags > li[data-v-e45d7c10] {\n    display: inline-block;\n    line-height: 30px;\n}\n.ctpc-tag-lis > span[data-v-e45d7c10] {\n    display: inline-block;\n    vertical-align: middle;\n    font-size: 14px;\n}\n.ctpc-tag-lis .tag-lis-delete[data-v-e45d7c10]:hover {\n    text-shadow: 0 0 8px #ccc;\n    font-size: 18px !important;\n}\n.tag-lis-add[data-v-e45d7c10] {\n    position: relative;\n    margin-left: 10px;\n    cursor: pointer;\n}\n.ctpc-member-con[data-v-e45d7c10] {\n    padding-left: 10px; /* border-left: 1px solid #ccc; */\n    margin-left: 6px;\n    position: relative;\n}\n.ctpc-member-list[data-v-e45d7c10] {\n    height: 42px;\n    background: #fff;\n    border: 1px solid #ccc;\n    line-height: 42px;\n    color: #000;\n    padding: 0 4px;\n    position: relative;\n    margin-bottom: 10px;\n    box-shadow: 0 0 10px #ccc;\n    display: -webkit-flex;\n    display: -moz-flex;\n    display: -ms-flex;\n    display: -o-flex;\n    display: flex;\n}\n.ctpc-member-job[data-v-e45d7c10] {\n    width: 110px;\n    /*  -webkit-flex: 1;\n      -moz-flex: 1;\n      -ms-flex: 1;\n      -o-flex: 1;\n      flex: 1;*/\n}\n.ctpc-member-job-time[data-v-e45d7c10] {\n    width: 110px;\n}\n.ctpc-member-end-time[data-v-e45d7c10] {\n    /*width: 110px;*/\n}\n.ctpc-member-assess[data-v-e45d7c10] {\n    width: 70px;\n}\n.ctpc-member-head[data-v-e45d7c10] {\n    width: 36px;\n    height: 36px;\n    border-radius: 50%;\n    background: #006699;\n    color: #fff;\n    font-size: 10px;\n    text-align: center;\n    line-height: 36px;\n    margin-top: 3px;\n    overflow: hidden;\n    margin-right: 10px;\n}\n.bdl-line[data-v-e45d7c10] {\n    position: absolute;\n    left: 0;\n    bottom: 20px;\n    top: 14px;\n    border-left: 1px solid #ccc;\n}\n.add-member-opt[data-v-e45d7c10] {\n    cursor: pointer;\n    margin: 20px 10px;\n    /*width: 80px;*/\n}\n.add-member-opt > span[data-v-e45d7c10] {\n    display: inline-block;\n    vertical-align: middle;\n}\n.add-member-icon[data-v-e45d7c10] {\n    background: #36A8FF;\n    color: #fff;\n    width: 20px;\n    height: 20px;\n    text-align: center;\n    font-size: 20px;\n    line-height: 16px;\n    font-weight: bold;\n    border-radius: 50%;\n}\n.add-member-msg[data-v-e45d7c10] {\n    color: #36A8FF;\n    margin-left: 6px;\n}\n.ctpc-btns[data-v-e45d7c10] {\n    text-align: right;\n    margin-top: 20px;\n}\n.ctpc-btns > input[data-v-e45d7c10] {\n    display: inline-block;\n    width: 80px;\n    height: 28px;\n    margin-left: 12px;\n    cursor: pointer;\n    border-radius: 4px;\n}\n.ctpc-cancel[data-v-e45d7c10] {\n    background: #fff;\n    border: 1px solid #ccc;\n}\n.ctpc-cancel[data-v-e45d7c10]:hover {\n    background: #fff;\n    border: 1px solid #36A8FF;\n    color: #36A8FF;\n    font-weight: 700;\n}\n.ctpc-save[data-v-e45d7c10] {\n    background: #36A8FF;\n    color: #fff;\n}\n.add-member-basic[data-v-e45d7c10] {\n    background: #fff;\n    border: 1px solid #ccc;\n    margin-top: 10px;\n    padding: 10px;\n    font-size: 14px;\n}\n.add-member-basic-menu[data-v-e45d7c10] {\n    width: 82px;\n    text-align: right;\n}\n.add-member-basic-list[data-v-e45d7c10] { /* border-bottom: 1px solid #ccc; */\n    padding: 5px 0;\n    line-height: 34px;\n}\n.add-member-basic-list[data-v-e45d7c10]:last-child {\n    border: none;\n}\n.add-stage-opt[data-v-e45d7c10] {\n    color: #36A8FF;\n    cursor: pointer;\n    width: 400px;\n}\n.add-member-basic-msg .el-select[data-v-e45d7c10] {\n    width: 140px;\n}\n.add-member-basic-time[data-v-e45d7c10] {\n    margin-left: 40px;\n}\n.add-member-basic-msg .el-date-editor.el-input[data-v-e45d7c10] {\n    width: 140px;\n}\n.add-member-basic-end[data-v-e45d7c10] {\n    margin-left: 40px;\n}\n.tag-add-sel .el-select[data-v-e45d7c10] {\n    width: 188px;\n}\n.ctpc-member-delete[data-v-e45d7c10] {\n    font-size: 26px;\n    cursor: pointer;\n    margin-right: 4px;\n    position: absolute;\n    right: 1px;\n}\n.add-member-stage[data-v-e45d7c10] {\n    position: relative;\n}\n.ctpc-ins-edit .ctpc-btns[data-v-e45d7c10] {\n    margin-top: 0;\n}\n", ""]);

// exports


/***/ }),
/* 256 */
/***/ (function(module, exports, __webpack_require__) {

exports = module.exports = __webpack_require__(1)(false);
// imports


// module
exports.push([module.i, "\n.add-member-pop[data-v-f9a39a7e]{position: fixed;top: 0;left: 0;bottom: 0;right: 0;z-index: 100;background: rgba(0,0,0,0.5);\n}\n.alter-pwd-pop[data-v-f9a39a7e]{position: absolute;left: 50%;top: 50%;color: #000;background: #fff;transform: translate(-50%,-50%);z-index: 50;padding: 0 16px 20px;width: 460px;cursor: auto;\n}\n.alter-top[data-v-f9a39a7e]{border-bottom: 1px solid #ccc;color: #666;font-size: 14px;padding: 10px 0 6px;\n}\n.alter-top > span[data-v-f9a39a7e]{display: inline-block;line-height: 36px;\n}\n.close-alter-pwd[data-v-f9a39a7e]{font-size: 26px;width: 30px;height: 30px;line-height: 26px !important;cursor: pointer;text-align: center;transition:0.8s ease all;\n}\n.close-alter-pwd[data-v-f9a39a7e]:hover{color:#36A8FF;\n}\n.alter-list[data-v-f9a39a7e]{margin: 20px 30px;\n}\n.alter-menu[data-v-f9a39a7e]{width: 120px;text-align: left;font-size: 14px;\n}\n.pwd-inp[data-v-f9a39a7e]{border: 1px solid #ccc;height: 30px;width: 220px;border-radius: 3px;text-indent: 10px;\n}\n.save-alter-btn[data-v-f9a39a7e]{width: 100px;height: 30px;background: #3DA7F5;color: #fff;border-radius: 3px;display: block;margin-left: 150px;cursor: pointer;\n}\r\n\r\n", ""]);

// exports


/***/ }),
/* 257 */,
/* 258 */,
/* 259 */,
/* 260 */,
/* 261 */,
/* 262 */,
/* 263 */,
/* 264 */,
/* 265 */,
/* 266 */,
/* 267 */,
/* 268 */,
/* 269 */,
/* 270 */,
/* 271 */,
/* 272 */,
/* 273 */,
/* 274 */,
/* 275 */,
/* 276 */,
/* 277 */,
/* 278 */,
/* 279 */
/***/ (function(module, exports, __webpack_require__) {

var map = {
	"./af": 40,
	"./af.js": 40,
	"./ar": 47,
	"./ar-dz": 41,
	"./ar-dz.js": 41,
	"./ar-kw": 42,
	"./ar-kw.js": 42,
	"./ar-ly": 43,
	"./ar-ly.js": 43,
	"./ar-ma": 44,
	"./ar-ma.js": 44,
	"./ar-sa": 45,
	"./ar-sa.js": 45,
	"./ar-tn": 46,
	"./ar-tn.js": 46,
	"./ar.js": 47,
	"./az": 48,
	"./az.js": 48,
	"./be": 49,
	"./be.js": 49,
	"./bg": 50,
	"./bg.js": 50,
	"./bn": 51,
	"./bn.js": 51,
	"./bo": 52,
	"./bo.js": 52,
	"./br": 53,
	"./br.js": 53,
	"./bs": 54,
	"./bs.js": 54,
	"./ca": 55,
	"./ca.js": 55,
	"./cs": 56,
	"./cs.js": 56,
	"./cv": 57,
	"./cv.js": 57,
	"./cy": 58,
	"./cy.js": 58,
	"./da": 59,
	"./da.js": 59,
	"./de": 62,
	"./de-at": 60,
	"./de-at.js": 60,
	"./de-ch": 61,
	"./de-ch.js": 61,
	"./de.js": 62,
	"./dv": 63,
	"./dv.js": 63,
	"./el": 64,
	"./el.js": 64,
	"./en-au": 65,
	"./en-au.js": 65,
	"./en-ca": 66,
	"./en-ca.js": 66,
	"./en-gb": 67,
	"./en-gb.js": 67,
	"./en-ie": 68,
	"./en-ie.js": 68,
	"./en-nz": 69,
	"./en-nz.js": 69,
	"./eo": 70,
	"./eo.js": 70,
	"./es": 72,
	"./es-do": 71,
	"./es-do.js": 71,
	"./es.js": 72,
	"./et": 73,
	"./et.js": 73,
	"./eu": 74,
	"./eu.js": 74,
	"./fa": 75,
	"./fa.js": 75,
	"./fi": 76,
	"./fi.js": 76,
	"./fo": 77,
	"./fo.js": 77,
	"./fr": 80,
	"./fr-ca": 78,
	"./fr-ca.js": 78,
	"./fr-ch": 79,
	"./fr-ch.js": 79,
	"./fr.js": 80,
	"./fy": 81,
	"./fy.js": 81,
	"./gd": 82,
	"./gd.js": 82,
	"./gl": 83,
	"./gl.js": 83,
	"./gom-latn": 84,
	"./gom-latn.js": 84,
	"./he": 85,
	"./he.js": 85,
	"./hi": 86,
	"./hi.js": 86,
	"./hr": 87,
	"./hr.js": 87,
	"./hu": 88,
	"./hu.js": 88,
	"./hy-am": 89,
	"./hy-am.js": 89,
	"./id": 90,
	"./id.js": 90,
	"./is": 91,
	"./is.js": 91,
	"./it": 92,
	"./it.js": 92,
	"./ja": 93,
	"./ja.js": 93,
	"./jv": 94,
	"./jv.js": 94,
	"./ka": 95,
	"./ka.js": 95,
	"./kk": 96,
	"./kk.js": 96,
	"./km": 97,
	"./km.js": 97,
	"./kn": 98,
	"./kn.js": 98,
	"./ko": 99,
	"./ko.js": 99,
	"./ky": 100,
	"./ky.js": 100,
	"./lb": 101,
	"./lb.js": 101,
	"./lo": 102,
	"./lo.js": 102,
	"./lt": 103,
	"./lt.js": 103,
	"./lv": 104,
	"./lv.js": 104,
	"./me": 105,
	"./me.js": 105,
	"./mi": 106,
	"./mi.js": 106,
	"./mk": 107,
	"./mk.js": 107,
	"./ml": 108,
	"./ml.js": 108,
	"./mr": 109,
	"./mr.js": 109,
	"./ms": 111,
	"./ms-my": 110,
	"./ms-my.js": 110,
	"./ms.js": 111,
	"./my": 112,
	"./my.js": 112,
	"./nb": 113,
	"./nb.js": 113,
	"./ne": 114,
	"./ne.js": 114,
	"./nl": 116,
	"./nl-be": 115,
	"./nl-be.js": 115,
	"./nl.js": 116,
	"./nn": 117,
	"./nn.js": 117,
	"./pa-in": 118,
	"./pa-in.js": 118,
	"./pl": 119,
	"./pl.js": 119,
	"./pt": 121,
	"./pt-br": 120,
	"./pt-br.js": 120,
	"./pt.js": 121,
	"./ro": 122,
	"./ro.js": 122,
	"./ru": 123,
	"./ru.js": 123,
	"./sd": 124,
	"./sd.js": 124,
	"./se": 125,
	"./se.js": 125,
	"./si": 126,
	"./si.js": 126,
	"./sk": 127,
	"./sk.js": 127,
	"./sl": 128,
	"./sl.js": 128,
	"./sq": 129,
	"./sq.js": 129,
	"./sr": 131,
	"./sr-cyrl": 130,
	"./sr-cyrl.js": 130,
	"./sr.js": 131,
	"./ss": 132,
	"./ss.js": 132,
	"./sv": 133,
	"./sv.js": 133,
	"./sw": 134,
	"./sw.js": 134,
	"./ta": 135,
	"./ta.js": 135,
	"./te": 136,
	"./te.js": 136,
	"./tet": 137,
	"./tet.js": 137,
	"./th": 138,
	"./th.js": 138,
	"./tl-ph": 139,
	"./tl-ph.js": 139,
	"./tlh": 140,
	"./tlh.js": 140,
	"./tr": 141,
	"./tr.js": 141,
	"./tzl": 142,
	"./tzl.js": 142,
	"./tzm": 144,
	"./tzm-latn": 143,
	"./tzm-latn.js": 143,
	"./tzm.js": 144,
	"./uk": 145,
	"./uk.js": 145,
	"./ur": 146,
	"./ur.js": 146,
	"./uz": 148,
	"./uz-latn": 147,
	"./uz-latn.js": 147,
	"./uz.js": 148,
	"./vi": 149,
	"./vi.js": 149,
	"./x-pseudo": 150,
	"./x-pseudo.js": 150,
	"./yo": 151,
	"./yo.js": 151,
	"./zh-cn": 152,
	"./zh-cn.js": 152,
	"./zh-hk": 153,
	"./zh-hk.js": 153,
	"./zh-tw": 154,
	"./zh-tw.js": 154
};
function webpackContext(req) {
	return __webpack_require__(webpackContextResolve(req));
};
function webpackContextResolve(req) {
	var id = map[req];
	if(!(id + 1)) // check for number or string
		throw new Error("Cannot find module '" + req + "'.");
	return id;
};
webpackContext.keys = function webpackContextKeys() {
	return Object.keys(map);
};
webpackContext.resolve = webpackContextResolve;
module.exports = webpackContext;
webpackContext.id = 279;

/***/ }),
/* 280 */,
/* 281 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/auditSuccess.5989144.png";

/***/ }),
/* 282 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/doing.ffc2844.png";

/***/ }),
/* 283 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/finished.2d5caeb.jpg";

/***/ }),
/* 284 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/login_bg.4f41e92.png";

/***/ }),
/* 285 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQCAYAAACOEfKtAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6RjY3NjFGMzM1REMzMTFFNzk5NTFGNEQ0MTRGRTI2M0UiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6RjY3NjFGMzQ1REMzMTFFNzk5NTFGNEQ0MTRGRTI2M0UiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpFODMyMEQwQTVEQzMxMUU3OTk1MUY0RDQxNEZFMjYzRSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpGNjc2MUYzMjVEQzMxMUU3OTk1MUY0RDQxNEZFMjYzRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PsAJSUQAABBqSURBVHja7F0JdFPXtd225NmSbWw8zzYewMaAmacAgVDaAEkzLNImIeEnv5/2d8ic/J+/mja0azUrJeknSZP+pElWJkKA0kCYyhDCFBsbzwOeB9mWbUmeZUu25H/ufdbwJNmxwTFuo8N6S3rvvnffffudc/Y5515kl+HhYVhLUVHRbPp4hLZNtMXR5oHvtuhoq6HtOG1vp6enl1o3upgAJODc6WO3VCrdGRQU5CqTyeDh4QEXF5fvNHoMH51Oh56eHqhUKuPQ0ND/0eHHCMh+M4Aj4H0hl8vXR0ZGQiKRwCn2YjAY0NTUhK6urjPMQglEvetI2ysMvJiYGCd4YwjDJjo6GoTVOmatXAMLCwvnkNkWJCUlSZzgjV8TKyoqDGTOGUwDHwkMDHSCN0FNJJ5ggD3CAPweqaQTlQkKI1mGHQMwnrGtUyYmI5jFMwDdv+uhyvXICGburk4obkycADoBdALoBNAJoFOcAN4skU5mZ6reRlys3osa1RUMGfXw9wxFbNBCJAUvRtSMNLjQv6r2HDqvAV5ucsQEpiPAO/S679epbUWduhD9gz2Y6RuNhOBMGI1G1NOxqvYsGkcOenQquLl6IiV0NRbFbkWgT8T0BLClsxKf5v4PBo0DlgccUKKo+QT8vYIxUxaDoqYz+KrqfUswSgaQEfl9rEt+GBLX8efiQwY9zla8jwLFMQzDaD6+OvEhpEesg0arQHHLaRiMg/w4G1NR80mUKc9hW+YuhPnPmn4mfKH6ExF4Jtk0+5f05rfA290PJS1nRW3s4fMVR3C4aDeMw8Zxg3cgbxdd94UIPCblrefpPnIsjNmMW5N/Yn+tUYdLNfumnw80GIcw7ACA+MCFSA1bad7X6jocXl/ZdhEnS98kQIbHvA9rP1L8JzR0FDps7xloN3+fG3krIv3mOASRvYSbDiDTmIrWLBy4ugvtPfVYnrgNUld3czvzO2uTd4iumR2+btT+mIldrt4/5j2zag5xsEeT1NA1Vu7BBbfN+SmNyVIoYeNblXg/mrsr+bjrNcU3nhMXFhYOp6enT9h5f174Mlp7KuHrHoidt7zDjzd3XkNO/RG4STyRGXM7gsnvmfyjRtvCtbFQcRrlyvPo03dA5hmIUNksBMvj4OMRQP22IJrIxo98pq1o9V1o0JTA12MGenUaaPoUaO+tR2e/ElIXN8wJv5X86XoUNJ5EqN8s2uL5dcruWuTWfw69oR9LY+/i/o9p8utfbify6Ua4Xyq+n/YLIrOwCYNXVFQ0cQC7+tvxYdbT0A4K5ujnGYJ/X/WWQ3Orbc9Ddt3f0NhZhDsznkdi8MJvPawob72Ew4UvIco/nXzvHYifuYBro6385fx/oItIjom3WwC2L9tNLydgwgBOmIUvVe8zg8ekV68hczbA1cXCov2DvfQQL5OJ5JuPyb1mivrRDWnJ9BvIb6n4Q3pIvcc9hoHBPtSq8yHzCCTtjYG7xMvcxo4xYS+tMb8IcYGZ2Dz3CVH/jJ17dBZ/yZ6HPddts3/y7fvAqvavbQhkkLSyTXTsWPEeEXhMrB8gq/YQXvvyQXyS8yyBWGduGzQMjHlvU7unmw+ZZhW/fs/ZB0jLD5nPcSf3YS216lwcL3ndzorYS7eWa8TgU0IiA0M99o7UqhsWTFersuzOcZMIBFOnyqdY8D16gCG4uLhyX8lY8cOsZ/HqmW34KPs58m0NomvVfU344OunefvH2f/Fz18UvYVfz/o5V/keMXOJAKADTa4g4unQKi0P7WJvePpveHmTBmBMwDzRfnRABpFBEAeOmzQ5eFtxd/WCl7sw72LNfPFBi4k8/JHbcBQt3eUCEXWV4bOcX3M3YCKPT688D2VPBd9v6iqlAPoUfD0D+PUmaVAJoY3Mc4YoGjBJN7kKJuzlMPKKnbFA1B4XuGBqMpE75j+DYsVZdFOK5Okmw/yojcSORbQV45ak+xHhn0yMFklvXGG+Ji1ig9mRp0WsJRNq5d/XJj/EP0uVX4ru0Uc+KZ/YdFn8D4lBj/J9ayloOkGaSxlM0g5IyPdKGAtHrBnRLgllIxuR13jYfH6Qbywi/JIEx990msCbhy0ZTyKbXAnTvECfSAqxVk8NgMxhL6DBW0tZy3lcazvP06gZPuF4YMlLOF/1CYFagFkzl2F5wj08VgyifJXlolsynhBdzwCwlUZNIQfQUdDsNhLb+XsHY2vGUyLm1/Q1Y23SQ6SFbjwXjiGwVrIYlVyImkIflv7pB7VEXPOxataPbn4mwvxRZdslXjw4mPc7MrluTgrrU/4NO5b/Lw3yPspzpdzR16ryHfaxIuE+EYsLvrZ3hK37xAOm85YnbHPYT3VbLr6uOcDz6jVJD/L735qyg4+nd6ADByl4ZuOsVmWPO3WcdAAZYIMGnXm/racWeiNfZ4OO/ibsv/qiqJ2bpK6T56knSl8jX6S26zOBwpgHl76C1JA1ZiAlI47eFKIwokoJuQXbl77Kz3cUn54o3cMtgVVnbMOe/Xkv8uKGELZ0mX22id0Hp4pEmCkWN521GpxW1M6ykwvVe0XHCpvOcLbs1asJ4N/yGNBWWDnq9rm/Iq15nTt475Gg1ov8bAKRxY7lr1E89xi5gSgHDNrPCwwMGBZWlbdctCl07CXyqLF5qRa/eoWyJxaTTgmAbZQ+na18B1VtOSMBcpC9KbVni/brVLlWIUk9mdnBUftn9cG7FjxPee3KEe1cgjvmPct966iVoKq9vF9zccImVq1TXbW7xpfYmklJ8zlcqvmYUs3mqQFQ3dfI3/Khgt+TX/s7An0jOctZi8xDDKrepsxlHWR//u4uNNeV2fg5MtfQFfw7y2/ZvgiQshwc++AlS7BsA5DOxip83P1F+yGUfwf5ROFC5Sc4WvIKryR1aVunBsDeAY25lneu8l1crNyLTbN/Qb5KCGAlxH6rE+8XMWOoLFHUB6tG85iPgCu/cgJdauXo1Q4HqyY0bQoUXT6MNkX1CEDiHDZYFifaX02EwsbFMyKJDy8efFnxPi7XfWp5rlFKbZMOoG2QKgxiGA8te5VXhO/L/L254suCYFZQWBx3J/y9wjhBhMtTiCG3CxlC/oVRQRpLPLx8BVdRIpjqmuSHSasSeWYS6BONpfF3kxvJ5fdnwmJTNi42vgeW/pGYeAg5DYdsXtQUlfSD5bEosVEYRWcprwIvibvDJj68wOcm7s38DR5Z+Qafr7Au3SsqBVP28pFPEEAfQYNrhKwmVB5HLP6yqFZ5pGg35oStxbyo2/gx9lJNL5aV3GwliICfEg1MDV3FUzNr8SYfM0DMaltRLm+9wAPhryo/4pmI7byHWlkrmL1UHEgra8tw5C8v4KNdj/JPZf01m9xbUBdVc63DMZ4u/ytPCcuVF+yKwCxOdbMpOHhKZUgNXzU1ALLc9fa5T/KKMzPJxTF3IzlkORSaUtSrCkTJeUuX8ODZ9fvprR8WE8uAFrp++8JEfWkOPv3DT1GVexbtjRX8cx/tKyosfUvdhUykr1tlX7Wu/RufZzHlzQajpepS034VzZ0VSAtfi0UxP+TjZ8qwOf0JHi5NiQkLoUUmfrbmPT44VloSwps6FDQew7ZFv+OhyBAF09aTPqxiwoBmiTyPH/t7LbFknwXIrz57w25+xWgYwvkDb+K+5/4snK/tFoL6QR1tekjd3EfmRDQU831opXFDtA2CsmUeOJ8sewMLojbzwi7LVJivZLm0m+T610dedyrHzMAEHmfGXgUPlD/MeorPy7JZOEYclocxmCsidlMEqhYBEP0A1M3VDs9prSs1A6tutWQRhqFBq4qLuM4X4B3Bx8k07+PsZ9Cn16DDKt7zpBTvRsCblFzYDEK/0lwv/CzvBV5NWZFgSdbZJHuoPN7htQ2VeSO+0B0SN3fHL8zdi7MsLzRU5DkmOFkMn6MxCZtAyms4jgP5lP0YtCPzOc2YTJk0AFk6ZY79SFP+Uf4GnwDaOvc5pIWtx92ZvxbFWj5yS+xWW3IZ2r4uuLi6InnRBof9Jy/dKMRrnWo0VgqBsyuRkomRhbCpG/cu/A3mRf6A39dT6oNT194UV7WNuukJoKMJmezag0gKWYJNaf/JZ71Ol79tzoMlEjfMCIkdMUM9cs8c4N9vuedniEgSFwuiZy/G6ruE+Yrs0/u4T2QSGJYgKhiw/lltb0Pqo/y+tsRlyq0nUyZtaUdUwFy7eRBVX4OoCMHKSGzyxlRIjUtbBk1rncCeJz9AYvpyhMWm4J7Hd6OFQpmu9mb4h0QiNCaZR7r11/IIaEuhIiFtuahgwPpn9UBT3qyyyo9NEuE/Z3pq4NyItZQmeY8anOYrTgpBLGUAjGR4nrviB2a/xrRq357HUVeey8EKi5+NlCXrEUqAsv3Kgos4+OenzUTCzDd9hVDYrWzNNlegC0buw0nEK9wu3psXtWFSAZTs3LnzhZCQkBvuyF3qhXD/VFS3ZfOiZXLwKj5NyI6z4sMXlBkYhgXTq1MXID1iPeR+M9Hb1YHWhnKzKZdkHYeyoZJAc6U4sReKmhKcO/QWLh9712y6TBau/zFSM9egu1+F/VdfoL4FNmYT7gtjtvICRGxgBrVroCZLYHO/W+c947Acdr3S1tZ2fSsTvqngygiFhTHmzKKrCh9kPyk6b0HUFl4t7tf24N0XtxOQ7eO+h39QJB7+77/CzcMLR4v3oKTltKj9gcV/RKhfgqigy0IuU0FhsoRNrE/6Aks292ANnlCWty+gljSfEpy6twy3/eipCd1j44+f4eDxgkSr/VoZU8hinT1NNniT7gPHErmDtS4mc2aSmL4MKYs2jquvzHXbEJNsmVo1wmCvoV4hmCqZEgBZahcmTxang0FLRPvr7/05vH3HXpsinxGGVZt32PSzWLTPlrQ5Wpz0bYl0qm5057xn+YQ4849sNdX86O+JKzo+fli55VGc/PilUftYumk73D3ElaB1yTs42zJNZMs6MiI3YCpl0knkRkQ3oMWfHh/dlH/+8lHuM6eLfCskciPi4ekNTy/HxVWZf+i0Am9KfeDEtLDPccAqlWI6yrQCkP1X+uFhg8O2QV2/E8BvDML1o68O0PZ2wPY3bpwA2oLU1z1qG8uBtT0dTgDHkm51y5jtmlaFE8CxpL2pdsx2lbLOCeBYYirtjybKuvJpB+BNjQ0MhkF0d6jQo2lFp1qJ+rIrY55fmX8OkYkZlNKF0BbMP2/2791MKYA9ne2oLcvh1eaWujIiBQ28ZTPg5SuHp68f0pZugnQkVfNgn64SDBuHoNcJ7Kzv70VV8SX093bS1kVbB3zkQQiPm01bGmJS5kMeEPyvBeCAtg/K+jJ0qJoJDCMCQ6MRlZAOGT2odJQZuIn2361R0taKstyzwj1CohESPWtKwJxWufA/m0y7XPifUZwAOgF0AugE0AmgU5wA3kwA9dOxzjbdha33JullANaw30l2ysREr+e//FHPADze3d3tRGSieX0PX5Z8igH4jlqtNrD5CKeMTxhWKpWKAfa2K+XBxUNDQ28pFAonMuMUhhXDjGFnYuHHyIxP1dfXw6mJY2teQ0MDCCv2U/CPsWPOP0bwDTKuP0ZgLQQkWwP7KG1s8QpbVu/2HVc8NtNfxwgDwp/DEP1e1P8LMAC90ygUQSh4VgAAAABJRU5ErkJggg=="

/***/ }),
/* 286 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABIAAAASCAYAAABWzo5XAAACLklEQVQ4EZ1TO2hUQRQ9M29NrIRAUCR+KosgQnQjEolYaKXWYjaFnVrYGBELLVLYGLGwtDSa3RgIiJ0IgpUoWZWABNlKEDtF8JeNu3s9Z8xdXh5GwYF9d865Z+7cz2xAYe27ZycQMR6AEQRshuEz8aIZ5psZ7rw5GVYKRxKk/vcq37WtKKFK4pBzRctg7xi8Uh8Lz4u+FCgFyfAsBGxzgQEf6HxP20e7K8c3O4ZjryrhqXOyUZ9QQs2D8GCjbTjCW3csjIWDtIOtDgYpe5y0QG8WcH/PjPUJ+4qpJ8CoCAXJVjBavO31eGgsvMVxlvVg9WB/T8QlDyIb1VgnmPK5F6fDR8dr7GTo/Ig4wz59SbyhkvdH1j8iQj0pZpIXas+JfWILHmmvVuyv2XbttWIaMTdqbGL+8WFGXV0L2OLymN4Jkabj5N/sGl0b312rHi0KaMRDM9YdswvytnzbNrCko+KY2XL8hob7I4l5B6WIW5i09CScy9uwCVd44cAq97B+Nvx0f9g9Zz0bW1jiTTsTyRFrOmqsi1ImDEJ8VRzLa1vA0MtTYck1vAAo1+wAzROCXmGNWNNRY9UTlZPLRBJpLtcr4WYC/KRAAnurdlgvltt+4T8tZcIDmfvywbr90Btq8q9A8ZQycbEs8TJ/cypHh93HTK+Xq3ZRuJuRO93qsaV3whFrOvnGlmdtIhimXMvLJ9YN5KL17HDNLtB3Q34G+vrfgRRgeNbOs9RrjDT9C0k+xAuabKUvAAAAAElFTkSuQmCC"

/***/ }),
/* 287 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/waitAssess.9cdd270.png";

/***/ }),
/* 288 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/waitAudit.df47cf0.png";

/***/ }),
/* 289 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(212),
  /* template */
  __webpack_require__(311),
  /* styles */
  null,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\node_modules\\._element-ui@1.4.1@element-ui\\packages\\tabs\\src\\tab-pane.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] tab-pane.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-79848d0b", Component.options)
  } else {
    hotAPI.reload("data-v-79848d0b", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 290 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(319)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(214),
  /* template */
  __webpack_require__(299),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-0f1dfbc3",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\AddDepartment.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] AddDepartment.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-0f1dfbc3", Component.options)
  } else {
    hotAPI.reload("data-v-0f1dfbc3", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 291 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(322)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(215),
  /* template */
  __webpack_require__(302),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-256e98ea",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\AddMember.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] AddMember.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-256e98ea", Component.options)
  } else {
    hotAPI.reload("data-v-256e98ea", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 292 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(341)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(216),
  /* template */
  __webpack_require__(316),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-f9a39a7e",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\AlterPassword.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] AlterPassword.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-f9a39a7e", Component.options)
  } else {
    hotAPI.reload("data-v-f9a39a7e", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 293 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(328)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(217),
  /* template */
  __webpack_require__(308),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-6573cde1",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\CreateTask.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] CreateTask.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-6573cde1", Component.options)
  } else {
    hotAPI.reload("data-v-6573cde1", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 294 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(334)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(218),
  /* template */
  __webpack_require__(314),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-c38d3b7c",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\Index.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] Index.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-c38d3b7c", Component.options)
  } else {
    hotAPI.reload("data-v-c38d3b7c", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 295 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(331)
  __webpack_require__(330)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(221),
  /* template */
  __webpack_require__(310),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-67e10559",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\Login.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] Login.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-67e10559", Component.options)
  } else {
    hotAPI.reload("data-v-67e10559", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 296 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(320)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(222),
  /* template */
  __webpack_require__(300),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-176c51b4",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\ModifyMember.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] ModifyMember.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-176c51b4", Component.options)
  } else {
    hotAPI.reload("data-v-176c51b4", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 297 */
/***/ (function(module, exports, __webpack_require__) {

var disposed = false
function injectStyle (ssrContext) {
  if (disposed) return
  __webpack_require__(321)
}
var Component = __webpack_require__(4)(
  /* script */
  __webpack_require__(229),
  /* template */
  __webpack_require__(301),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-2346b0e8",
  /* moduleIdentifier (server only) */
  null
)
Component.options.__file = "C:\\gits\\armor\\armor-view\\src\\components\\board\\TaskBoard.vue"
if (Component.esModule && Object.keys(Component.esModule).some(function (key) {return key !== "default" && key.substr(0, 2) !== "__"})) {console.error("named exports are not supported in *.vue files.")}
if (Component.options.functional) {console.error("[vue-loader] TaskBoard.vue: functional components are not supported with templates, they should use render functions.")}

/* hot reload */
if (false) {(function () {
  var hotAPI = require("vue-loader/node_modules/vue-hot-reload-api")
  hotAPI.install(require("vue"), false)
  if (!hotAPI.compatible) return
  module.hot.accept()
  if (!module.hot.data) {
    hotAPI.createRecord("data-v-2346b0e8", Component.options)
  } else {
    hotAPI.reload("data-v-2346b0e8", Component.options)
  }
  module.hot.dispose(function (data) {
    disposed = true
  })
})()}

module.exports = Component.exports


/***/ }),
/* 298 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "organization-con clearfix"
  }, [_c('div', {
    staticClass: "department-structure fl"
  }, [_c('div', {
    staticClass: "ds-title"
  }, [_vm._v("部门结构"), _c('span', {
    staticClass: "add-department",
    on: {
      "click": _vm.addDepartDlgShow
    }
  }, [_vm._v("+")])]), _vm._v(" "), _c('el-tree', {
    attrs: {
      "data": _vm.departmentTree,
      "props": _vm.defaultProps,
      "default-expand-all": "",
      "expand-on-click-node": false
    },
    on: {
      "node-click": _vm.handleNodeClick
    }
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "department-member fl"
  }, [_c('div', {
    staticClass: "dm-title"
  }, [_vm._v("成员"), _c('span', {
    staticClass: "add-department",
    on: {
      "click": _vm.addUserDlgShow
    }
  }, [_vm._v("+")])]), _vm._v(" "), _c('div', {
    staticClass: "white-bg"
  }, [_c('div', {
    staticClass: "department-member-table"
  }, [_c('el-table', {
    staticStyle: {
      "width": "100%"
    },
    attrs: {
      "data": _vm.tableData,
      "stripe": ""
    }
  }, [_c('el-table-column', {
    attrs: {
      "prop": "name",
      "label": "姓名",
      "width": "100",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "account",
      "label": "用户名",
      "width": "100",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "jobName",
      "label": "职位",
      "width": "100",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "phone",
      "label": "手机号",
      "width": "116",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "createTime",
      "label": "创建日期",
      "width": "106",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "lastLogin",
      "label": "最后登录",
      "width": "106",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "operate",
      "label": "操作",
      "align": "center"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('el-button', {
          attrs: {
            "type": "text",
            "size": "small"
          },
          nativeOn: {
            "click": function($event) {
              $event.preventDefault();
              _vm.modifyUserDlgShow(scope.$index)
            }
          }
        }, [_vm._v("编辑")]), _vm._v(" "), _c('el-button', {
          attrs: {
            "type": "text",
            "size": "small"
          },
          nativeOn: {
            "click": function($event) {
              $event.preventDefault();
              _vm.resetUserPwdDlgShow(scope.$index)
            }
          }
        }, [_vm._v("重置密码")]), _vm._v(" "), _c('el-button', {
          attrs: {
            "type": "text",
            "size": "small"
          },
          nativeOn: {
            "click": function($event) {
              $event.preventDefault();
              _vm.deleteUserDlgShow(scope.$index)
            }
          }
        }, [_vm._v("删除")])]
      }
    }])
  })], 1)], 1)]), _vm._v(" "), _c('el-pagination', {
    attrs: {
      "current-page": _vm.queryForm.pageIndex,
      "page-size": _vm.pagination.pageSize,
      "layout": _vm.pagination.layout,
      "total": _vm.pagination.total
    },
    on: {
      "current-change": _vm.handleCurrentChange
    }
  })], 1), _vm._v(" "), _c('add-member', {
    ref: "showAddMember",
    on: {
      "handleUserDataRefresh": _vm.handleUserDataRefresh
    }
  }), _vm._v(" "), _c('modify-member', {
    ref: "showModifyMember",
    on: {
      "handleUserDataRefresh": _vm.handleUserDataRefresh
    }
  }), _vm._v(" "), _c('add-department', {
    ref: "showAddDepartment",
    on: {
      "handleDeptDataRefresh": _vm.handleDeptDataRefresh
    }
  })], 1)
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-03088b73", module.exports)
  }
}

/***/ }),
/* 299 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.showAddPop),
      expression: "showAddPop"
    }],
    staticClass: "add-member-pop"
  }, [_c('div', {
    staticClass: "add-member-pop-con"
  }, [_c('div', {
    staticClass: "ctpc-top clearfix"
  }, [_c('span', {
    staticClass: "fl"
  }, [_vm._v("添加部门")]), _c('span', {
    staticClass: "ctpc-top-close fr",
    on: {
      "click": _vm.hide
    }
  }, [_vm._v("×")])]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("部门名称")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-input', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请输入部门名称"
    },
    model: {
      value: (_vm.addForm.name),
      callback: function($$v) {
        _vm.addForm.name = $$v
      },
      expression: "addForm.name"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("上级部门")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-select', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请选择部门"
    },
    model: {
      value: (_vm.addForm.parentId),
      callback: function($$v) {
        _vm.addForm.parentId = $$v
      },
      expression: "addForm.parentId"
    }
  }, _vm._l((_vm.departmentLevel), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.label,
        "value": item.id
      }
    })
  }))], 1)]), _vm._v(" "), _c('div', {
    staticClass: "am-warn"
  }, [_vm._v(_vm._s(_vm.amWarn))]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-btns"
  }, [_c('input', {
    staticClass: "ctpc-cancel",
    attrs: {
      "type": "button",
      "value": "取消"
    },
    on: {
      "click": _vm.hide
    }
  }), _vm._v(" "), _c('input', {
    staticClass: "ctpc-save",
    attrs: {
      "type": "button",
      "value": "保存"
    },
    on: {
      "click": _vm.handleAddDeptBtnClick
    }
  })])])])
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-0f1dfbc3", module.exports)
  }
}

/***/ }),
/* 300 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.showAddPop),
      expression: "showAddPop"
    }],
    staticClass: "add-member-pop"
  }, [_c('div', {
    staticClass: "add-member-pop-con"
  }, [_c('div', {
    staticClass: "ctpc-top clearfix"
  }, [_c('span', {
    staticClass: "fl"
  }, [_vm._v("修改成员")]), _c('span', {
    staticClass: "ctpc-top-close fr",
    on: {
      "click": _vm.hide
    }
  }, [_vm._v("×")])]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("姓名")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-input', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请输入姓名"
    },
    model: {
      value: (_vm.modifyForm.name),
      callback: function($$v) {
        _vm.modifyForm.name = $$v
      },
      expression: "modifyForm.name"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("账号")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-input', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请输入账号"
    },
    model: {
      value: (_vm.modifyForm.account),
      callback: function($$v) {
        _vm.modifyForm.account = $$v
      },
      expression: "modifyForm.account"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("职位")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-input', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请输入职位"
    },
    model: {
      value: (_vm.modifyForm.jobName),
      callback: function($$v) {
        _vm.modifyForm.jobName = $$v
      },
      expression: "modifyForm.jobName"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("手机号")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-input', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请输入手机号"
    },
    model: {
      value: (_vm.modifyForm.phone),
      callback: function($$v) {
        _vm.modifyForm.phone = $$v
      },
      expression: "modifyForm.phone"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("用户权限")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-select', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请选择权限"
    },
    model: {
      value: (_vm.modifyForm.userRole),
      callback: function($$v) {
        _vm.modifyForm.userRole = $$v
      },
      expression: "modifyForm.userRole"
    }
  }, _vm._l((_vm.options), function(item) {
    return _c('el-option', {
      key: item.value,
      attrs: {
        "label": item.label,
        "value": item.value
      }
    })
  }))], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-btns"
  }, [_c('input', {
    staticClass: "ctpc-cancel",
    attrs: {
      "type": "button",
      "value": "取消"
    },
    on: {
      "click": _vm.hide
    }
  }), _vm._v(" "), _c('input', {
    staticClass: "ctpc-save",
    attrs: {
      "type": "button",
      "value": "保存"
    },
    on: {
      "click": _vm.handleModifyUserClick
    }
  })])])])
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-176c51b4", module.exports)
  }
}

/***/ }),
/* 301 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "task-board"
  }, [_c('div', {
    staticClass: "task-board-list clearfix",
    style: ({
      'width': _vm.taskBoxWidth
    })
  }, _vm._l((_vm.stageList), function(item, key) {
    return _c('div', {
      staticClass: "fl task-list",
      on: {
        "drop": function($event) {
          _vm.drop($event)
        },
        "dragover": function($event) {
          _vm.allowDrop($event)
        }
      }
    }, [_c('header', {
      attrs: {
        "data-id": item.id
      }
    }, [_vm._v(_vm._s(item.name) + " · " + _vm._s(item.tasks ? item.tasks.length : 0))]), _vm._v(" "), _c('ul', {
      staticClass: "task-item",
      attrs: {
        "stageId": item.id
      }
    }, _vm._l((item.tasks), function(task, keyTask) {
      return _c('li', {
        staticClass: "clearfix",
        attrs: {
          "draggable": "true",
          "taskId": task.id,
          "createBy": task.createBy
        },
        on: {
          "dragstart": function($event) {
            _vm.drag($event)
          },
          "click": function($event) {
            _vm.handleTaskItemClick(task.id)
          }
        }
      }, [_c('div', {
        staticClass: "fl complate",
        attrs: {
          "data-title": ""
        }
      }), _vm._v(" "), _c('div', {
        staticClass: "fl task-name"
      }, [_c('div', [_vm._v(_vm._s(task.name))]), _vm._v(" "), _c('span', {
        staticClass: "tips",
        class: task.endColor
      }, [_vm._v(_vm._s(task.endText))])]), _vm._v(" "), _c('div', {
        staticClass: "master-info fr"
      }, [_vm._v("\n                        " + _vm._s(task.userName) + "\n                    ")])])
    }))])
  }))])
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-2346b0e8", module.exports)
  }
}

/***/ }),
/* 302 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.showAddPop),
      expression: "showAddPop"
    }],
    staticClass: "add-member-pop"
  }, [_c('div', {
    staticClass: "add-member-pop-con"
  }, [_c('div', {
    staticClass: "ctpc-top clearfix"
  }, [_c('span', {
    staticClass: "fl"
  }, [_vm._v("添加成员")]), _c('span', {
    staticClass: "ctpc-top-close fr",
    on: {
      "click": _vm.hide
    }
  }, [_vm._v("×")])]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("姓名")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-input', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请输入姓名"
    },
    model: {
      value: (_vm.addForm.name),
      callback: function($$v) {
        _vm.addForm.name = $$v
      },
      expression: "addForm.name"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("账号")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-input', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请输入账号"
    },
    model: {
      value: (_vm.addForm.account),
      callback: function($$v) {
        _vm.addForm.account = $$v
      },
      expression: "addForm.account"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("职位")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-input', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请输入职位"
    },
    model: {
      value: (_vm.addForm.jobName),
      callback: function($$v) {
        _vm.addForm.jobName = $$v
      },
      expression: "addForm.jobName"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("手机号")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-input', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请输入手机号"
    },
    model: {
      value: (_vm.addForm.phone),
      callback: function($$v) {
        _vm.addForm.phone = $$v
      },
      expression: "addForm.phone"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("用户权限")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-select', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请选择权限"
    },
    model: {
      value: (_vm.addForm.userRole),
      callback: function($$v) {
        _vm.addForm.userRole = $$v
      },
      expression: "addForm.userRole"
    }
  }, _vm._l((_vm.options), function(item) {
    return _c('el-option', {
      key: item.value,
      attrs: {
        "label": item.label,
        "value": item.value
      }
    })
  }))], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-btns"
  }, [_c('input', {
    staticClass: "ctpc-cancel",
    attrs: {
      "type": "button",
      "value": "取消"
    },
    on: {
      "click": _vm.hide
    }
  }), _vm._v(" "), _c('input', {
    staticClass: "ctpc-save",
    attrs: {
      "type": "button",
      "value": "保存"
    },
    on: {
      "click": _vm.handleAddUserClick
    }
  })])])])
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-256e98ea", module.exports)
  }
}

/***/ }),
/* 303 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "nav-index-con"
  }, [_c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.userRole > 0),
      expression: "userRole>0"
    }],
    staticClass: "my-integral-con"
  }, [_c('p', {
    staticClass: "mic-title"
  }, [_vm._v("我的积分")]), _vm._v(" "), _c('div', {
    staticClass: "mic-main clearfix"
  }, _vm._l((_vm.integralItem), function(item) {
    return _c('div', {
      staticClass: "mic-item fl"
    }, [_c('div', {
      staticClass: "mic-item-title"
    }, [_vm._v(_vm._s(item.label))]), _vm._v(" "), _c('div', {
      staticClass: "mic-item-title",
      staticStyle: {
        "font-size": "15px"
      }
    }, [_vm._v("(" + _vm._s(item.time) + ")")]), _vm._v(" "), _c('div', {
      staticClass: "mic-item-integral"
    }, [_vm._v(_vm._s(item.score))])])
  }))]), _vm._v(" "), _c('div', {
    staticClass: "my-task-con"
  }, [_c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.userRole > 0),
      expression: "userRole>0"
    }]
  }, [_c('div', {
    staticClass: "add-task",
    on: {
      "click": _vm.createTaskClick
    }
  }, [_vm._m(0), _vm._v(" "), _c('span', [_vm._v("创建个人任务")])]), _vm._v(" "), _c('p', {
    staticClass: "mic-title"
  }, [_vm._v("我的任务")]), _vm._v(" "), _c('div', {
    staticClass: "my-task-detail"
  }, [_c('el-tabs', {
    on: {
      "tab-click": _vm.handleClick
    },
    model: {
      value: (_vm.activeName),
      callback: function($$v) {
        _vm.activeName = $$v
      },
      expression: "activeName"
    }
  }, [_c('el-tab-pane', {
    attrs: {
      "label": "进行中",
      "name": "doing"
    }
  }, [_c('task-item', {
    attrs: {
      "taskItems": _vm.task.doing,
      "isPrivate": true,
      "taskStatus": "TaskDoing",
      "projectList": _vm.projectList,
      "userList": _vm.userList,
      "stageList": _vm.stageList,
      "tagList": _vm.tagList
    },
    on: {
      "reload": _vm.reload
    }
  })], 1), _vm._v(" "), _c('el-tab-pane', {
    attrs: {
      "label": "已完成",
      "name": "completed"
    }
  }, [_c('task-item', {
    attrs: {
      "taskItems": _vm.task.finished,
      "isPrivate": true,
      "taskStatus": "finished",
      "projectList": _vm.projectList,
      "userList": _vm.userList,
      "stageList": _vm.stageList,
      "tagList": _vm.tagList
    }
  }), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (this.task.finished.length > 0),
      expression: "this.task.finished.length>0"
    }],
    staticClass: "pagination"
  }, [_c('el-pagination', {
    attrs: {
      "current-page": _vm.finishedPage.pageNum,
      "page-size": _vm.finishedPage.pageSize,
      "layout": _vm.finishedPageLayout,
      "total": _vm.finishedPage.total
    },
    on: {
      "current-change": _vm.handleFinishedPage,
      "update:currentPage": function($event) {
        _vm.finishedPage.pageNum = $event
      }
    }
  })], 1)], 1)], 1)], 1), _vm._v(" "), _c('div', [_c('p', {
    staticClass: "mic-title"
  }, [_vm._v("评价任务")]), _vm._v(" "), _c('el-tabs', {
    on: {
      "tab-click": _vm.handleClick
    },
    model: {
      value: (_vm.assessActiveName),
      callback: function($$v) {
        _vm.assessActiveName = $$v
      },
      expression: "assessActiveName"
    }
  }, [_c('el-tab-pane', {
    attrs: {
      "label": "待评价",
      "name": "waitAssess"
    }
  }, [_c('task-item', {
    attrs: {
      "taskItems": _vm.task.waitAssess,
      "isPrivate": true,
      "taskStatus": "WaitAssess",
      "projectList": _vm.projectList,
      "userList": _vm.userList,
      "stageList": _vm.stageList,
      "tagList": _vm.tagList
    },
    on: {
      "reload": _vm.reload
    }
  })], 1), _vm._v(" "), _c('el-tab-pane', {
    attrs: {
      "label": "已评价",
      "name": "commented"
    }
  }, [_c('task-item', {
    attrs: {
      "taskItems": _vm.task.commented,
      "isPrivate": true,
      "taskStatus": "WaitAssess",
      "projectList": _vm.projectList,
      "userList": _vm.userList,
      "stageList": _vm.stageList,
      "tagList": _vm.tagList
    }
  })], 1)], 1)], 1)]), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.userRole === 0),
      expression: "userRole===0"
    }]
  }, [_c('el-tabs', {
    on: {
      "tab-click": _vm.handleClick
    },
    model: {
      value: (_vm.auditTabsActiveName),
      callback: function($$v) {
        _vm.auditTabsActiveName = $$v
      },
      expression: "auditTabsActiveName"
    }
  }, [_c('el-tab-pane', {
    attrs: {
      "label": "待审核",
      "name": "wait"
    }
  }, [_c('task-item', {
    attrs: {
      "taskItems": _vm.task.waitAudit,
      "isPrivate": true,
      "taskStatus": "WaitAuditing",
      "projectList": _vm.projectList,
      "userList": _vm.userList,
      "stageList": _vm.stageList,
      "tagList": _vm.tagList
    },
    on: {
      "reload": _vm.reload
    }
  })], 1), _vm._v(" "), _c('el-tab-pane', {
    attrs: {
      "label": "审核通过",
      "name": "completed"
    }
  }, [_c('task-item', {
    attrs: {
      "taskItems": _vm.task.auditSuccess,
      "isPrivate": true,
      "taskStatus": "auditSuccess",
      "projectList": _vm.projectList,
      "userList": _vm.userList,
      "stageList": _vm.stageList,
      "tagList": _vm.tagList
    },
    on: {
      "reload": _vm.reload
    }
  }), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (this.task.auditSuccess.length > 0),
      expression: "this.task.auditSuccess.length>0"
    }],
    staticClass: "pagination"
  }, [_c('el-pagination', {
    attrs: {
      "current-page": _vm.auditSuccessPage.pageNum,
      "page-size": _vm.auditSuccessPage.pageSize,
      "layout": _vm.auditSuccessPageLayout,
      "total": _vm.auditSuccessPage.total
    },
    on: {
      "current-change": _vm.handleAuditSuccessPage,
      "update:currentPage": function($event) {
        _vm.auditSuccessPage.pageNum = $event
      }
    }
  })], 1)], 1)], 1)], 1)]), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "创建个人任务",
      "size": "tiny",
      "custom-class": "myDialog",
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "visible": _vm.createTaskVisible
    },
    on: {
      "update:visible": function($event) {
        _vm.createTaskVisible = $event
      }
    }
  }, [_c('el-form', {
    ref: "taskForm",
    attrs: {
      "model": _vm.taskForm,
      "rules": _vm.rules,
      "label-width": "80px"
    }
  }, [_c('el-form-item', {
    attrs: {
      "label": "项目",
      "prop": "projectId"
    }
  }, [_c('el-select', {
    attrs: {
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.taskForm.projectId),
      callback: function($$v) {
        _vm.taskForm.projectId = $$v
      },
      expression: "taskForm.projectId"
    }
  }, _vm._l((_vm.projectList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "label": "截止日期",
      "prop": "endTime"
    }
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "format": "yyyy-MM-dd",
      "placeholder": "选择日期时间"
    },
    model: {
      value: (_vm.taskForm.endTime),
      callback: function($$v) {
        _vm.taskForm.endTime = $$v
      },
      expression: "taskForm.endTime"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "label": "工作量",
      "prop": "taskHours"
    }
  }, [_c('el-input', {
    staticStyle: {
      "width": "100px"
    },
    attrs: {
      "maxlength": 2
    },
    model: {
      value: (_vm.taskForm.taskHours),
      callback: function($$v) {
        _vm.taskForm.taskHours = $$v
      },
      expression: "taskForm.taskHours"
    }
  }), _vm._v("\n                小时\n            ")], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "label": "任务名称",
      "prop": "taskName"
    }
  }, [_c('el-input', {
    model: {
      value: (_vm.taskForm.taskName),
      callback: function($$v) {
        _vm.taskForm.taskName = $$v
      },
      expression: "taskForm.taskName"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "label": "任务描述",
      "prop": "description"
    }
  }, [_c('el-input', {
    attrs: {
      "type": "textarea",
      "rows": 3
    },
    model: {
      value: (_vm.taskForm.description),
      callback: function($$v) {
        _vm.taskForm.description = $$v
      },
      expression: "taskForm.description"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "label": "阶段",
      "prop": "stageId"
    }
  }, [_c('el-select', {
    attrs: {
      "filterable": "",
      "default-first-option": "",
      "placeholder": "请选择阶段"
    },
    model: {
      value: (_vm.taskForm.stageId),
      callback: function($$v) {
        _vm.taskForm.stageId = $$v
      },
      expression: "taskForm.stageId"
    }
  }, _vm._l((_vm.stageList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "label": "标签",
      "prop": "tags"
    }
  }, [_c('el-select', {
    attrs: {
      "multiple": "",
      "placeholder": "请选择标签"
    },
    model: {
      value: (_vm.taskForm.tags),
      callback: function($$v) {
        _vm.taskForm.tags = $$v
      },
      expression: "taskForm.tags"
    }
  }, _vm._l((_vm.tagList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1)], 1), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    slot: "footer"
  }, [_c('el-button', {
    attrs: {
      "type": "primary"
    },
    on: {
      "click": function($event) {
        _vm.saveTaskInfo('taskForm')
      }
    }
  }, [_vm._v("立即创建")]), _vm._v(" "), _c('el-button', {
    on: {
      "click": function($event) {
        _vm.createTaskVisible = false
      }
    }
  }, [_vm._v("取 消")])], 1)], 1)], 1)
},staticRenderFns: [function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('span', {
    staticClass: "add-task-icon"
  }, [_c('i', {
    staticClass: "el-icon-plus"
  })])
}]}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-412fc1af", module.exports)
  }
}

/***/ }),
/* 304 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "intergral-con"
  }, [_c('div', {
    staticClass: "intergral-data-detail"
  }, [_c('el-table', {
    staticStyle: {
      "width": "50%",
      "float": "left"
    },
    attrs: {
      "data": _vm.quarter,
      "stripe": ""
    }
  }, [_c('el-table-column', {
    attrs: {
      "label": "季度排名",
      "align": "center"
    }
  }, [_c('el-table-column', {
    attrs: {
      "prop": "id",
      "label": "排名",
      "align": "center"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [(scope.row.id == '1') ? _c('div', [_c('img', {
          attrs: {
            "src": __webpack_require__(156),
            "alt": scope.row.id
          }
        })]) : _vm._e(), _vm._v(" "), (scope.row.id == '2') ? _c('div', [_c('img', {
          attrs: {
            "src": __webpack_require__(160),
            "alt": scope.row.id
          }
        })]) : _vm._e(), _vm._v(" "), (scope.row.id == '3') ? _c('div', [_c('img', {
          attrs: {
            "src": __webpack_require__(157),
            "alt": scope.row.id
          }
        })]) : _vm._e(), _vm._v(" "), (scope.row.id > '3') ? _c('div', [_vm._v(_vm._s(scope.row.id))]) : _vm._e()]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "name",
      "label": "成员",
      "align": "center"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('el-button', {
          attrs: {
            "type": "text",
            "size": "small"
          },
          nativeOn: {
            "click": function($event) {
              $event.preventDefault();
              _vm.clicklHistory(scope.$index, _vm.quarter)
            }
          }
        }, [_vm._v(_vm._s(scope.row.name))])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "integral",
      "label": "积分",
      "align": "center"
    }
  })], 1)], 1)], 1), _vm._v(" "), _c('div', {
    staticClass: "intergral-data-detail"
  }, [_c('el-table', {
    staticStyle: {
      "width": "48%",
      "float": "right"
    },
    attrs: {
      "data": _vm.year,
      "stripe": ""
    }
  }, [_c('el-table-column', {
    attrs: {
      "label": "年度排名",
      "align": "center"
    }
  }, [_c('el-table-column', {
    attrs: {
      "prop": "id",
      "label": "排名",
      "align": "center"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [(scope.row.id == '1') ? _c('div', [_c('img', {
          attrs: {
            "src": __webpack_require__(156),
            "alt": scope.row.id
          }
        })]) : _vm._e(), _vm._v(" "), (scope.row.id == '2') ? _c('div', [_c('img', {
          attrs: {
            "src": __webpack_require__(160),
            "alt": scope.row.id
          }
        })]) : _vm._e(), _vm._v(" "), (scope.row.id == '3') ? _c('div', [_c('img', {
          attrs: {
            "src": __webpack_require__(157),
            "alt": scope.row.id
          }
        })]) : _vm._e(), _vm._v(" "), (scope.row.id > '3') ? _c('div', [_vm._v(_vm._s(scope.row.id))]) : _vm._e()]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "name",
      "label": "成员",
      "align": "center"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('el-button', {
          attrs: {
            "type": "text",
            "size": "small"
          },
          nativeOn: {
            "click": function($event) {
              $event.preventDefault();
              _vm.clicklHistory(scope.$index, _vm.year)
            }
          }
        }, [_vm._v(_vm._s(scope.row.name))])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "integral",
      "label": "积分",
      "align": "center",
      "type": "danger"
    }
  })], 1)], 1)], 1)])
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-42ed7ea2", module.exports)
  }
}

/***/ }),
/* 305 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "stats-con"
  }, [_c('el-table', {
    attrs: {
      "data": _vm.statsData
    }
  }, [_c('el-table-column', {
    attrs: {
      "prop": "name",
      "label": "成员",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "inProcess",
      "label": "进行中任务",
      "align": "center"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(sco) {
        return [_c('el-button', {
          attrs: {
            "type": "text"
          },
          on: {
            "click": function($event) {
              _vm.getTask(sco.$index)
            }
          }
        }, [_vm._v(_vm._s(sco.row.inProcess))])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "hours",
      "label": "进行中任务耗时（小时）",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "delay",
      "label": "超时任务",
      "align": "center"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('span', {
          staticStyle: {
            "color": "red"
          },
          attrs: {
            "type": "text"
          }
        }, [_vm._v(_vm._s(scope.row.delay))])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "complete",
      "label": "已完成任务",
      "align": "center"
    }
  })], 1)], 1)
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-52cc4f8f", module.exports)
  }
}

/***/ }),
/* 306 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('button', {
    staticClass: "el-button",
    class: [
      _vm.type ? 'el-button--' + _vm.type : '',
      _vm.size ? 'el-button--' + _vm.size : '',
      {
        'is-disabled': _vm.disabled,
        'is-loading': _vm.loading,
        'is-plain': _vm.plain
      }
    ],
    attrs: {
      "disabled": _vm.disabled,
      "autofocus": _vm.autofocus,
      "type": _vm.nativeType
    },
    on: {
      "click": _vm.handleClick
    }
  }, [(_vm.loading) ? _c('i', {
    staticClass: "el-icon-loading"
  }) : _vm._e(), _vm._v(" "), (_vm.icon && !_vm.loading) ? _c('i', {
    class: 'el-icon-' + _vm.icon
  }) : _vm._e(), _vm._v(" "), (_vm.$slots.default) ? _c('span', [_vm._t("default")], 2) : _vm._e()])
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-5ce48d69", module.exports)
  }
}

/***/ }),
/* 307 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "intergralHistory-con"
  }, [_c('div', {
    staticClass: "intergralHistory-con-top clearfix"
  }, [_vm._l((_vm.menuList), function(list, index) {
    return _c('div', {
      staticClass: "fl menu-list",
      style: (_vm.tabActive(index)),
      on: {
        "click": function($event) {
          _vm.togTable(index)
        }
      }
    }, [_vm._v(_vm._s(list.name) + "\n    ")])
  }), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.diyStyle),
      expression: "diyStyle"
    }],
    staticClass: "fl menu-list"
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "placeholder": "选择日期"
    },
    model: {
      value: (_vm.queryForm.startTime),
      callback: function($$v) {
        _vm.queryForm.startTime = $$v
      },
      expression: "queryForm.startTime"
    }
  }), _vm._v(" "), _c('span', {
    staticClass: "div-line"
  }, [_vm._v("-")]), _vm._v(" "), _c('el-date-picker', {
    attrs: {
      "type": "date",
      "placeholder": "选择日期"
    },
    model: {
      value: (_vm.queryForm.endTime),
      callback: function($$v) {
        _vm.queryForm.endTime = $$v
      },
      expression: "queryForm.endTime"
    }
  }), _vm._v(" "), _c('img', {
    staticClass: "serch-btn",
    attrs: {
      "src": __webpack_require__(286),
      "alt": ""
    },
    on: {
      "click": function($event) {
        _vm.integralDate()
      }
    }
  })], 1), _vm._v(" "), _c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.permit),
      expression: "permit"
    }],
    staticStyle: {
      "float": "right",
      "position": "relative",
      "bottom": "12PX",
      "right": "100PX"
    },
    attrs: {
      "size": "large"
    },
    on: {
      "click": function($event) {
        _vm.editIntegralVisible = true
      }
    }
  }, [_vm._v("添加积分记录")]), _vm._v(" "), _c('el-button', {
    staticStyle: {
      "float": "right",
      "position": "relative",
      "bottom": "12PX",
      "right": "200PX"
    },
    attrs: {
      "size": "large"
    },
    on: {
      "click": function($event) {
        _vm.backButton()
      }
    }
  }, [_vm._v("返回上页")])], 2), _vm._v(" "), _c('el-table', {
    staticStyle: {
      "width": "100%",
      "bottom": "20px"
    },
    attrs: {
      "data": _vm.historyData,
      "stripe": ""
    }
  }, [_c('el-table-column', {
    attrs: {
      "prop": "name",
      "label": "成员",
      "align": "center",
      "width": "100px"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "integral",
      "label": "积分",
      "align": "center"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('el-tag', {
          attrs: {
            "type": scope.row.integral < 0 ? 'danger' : 'gray',
            "close-transition": ""
          }
        }, [_vm._v(_vm._s(scope.row.integral) + "\n        ")])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "taskHours",
      "label": "工作量",
      "align": "center",
      "width": "100px"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "origin",
      "label": "来源",
      "align": "center",
      "width": "200px"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('el-tag', {
          attrs: {
            "type": scope.row.origin === '任务系统-单人任务' ? 'warning' : 'success' && scope.row.origin === '手动录入' ? 'primary' : 'success',
            "close-transition": ""
          }
        }, [_vm._v(_vm._s(scope.row.origin) + "\n        ")])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "grade",
      "label": "评价",
      "align": "center",
      "width": "100px"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('el-button', {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: (_vm.permit),
            expression: "permit"
          }],
          attrs: {
            "type": "text"
          },
          on: {
            "click": function($event) {
              scope.row.type == '2' ? _vm.commentDetail(scope.$index, _vm.historyData) : ''
            }
          }
        }, [_vm._v(_vm._s(scope.row.grade))]), _vm._v(" "), _c('div', {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: (!_vm.permit),
            expression: "!permit"
          }],
          attrs: {
            "type": "text"
          }
        }, [_vm._v(_vm._s(scope.row.grade))])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "createTime",
      "label": "完成时间",
      "align": "center",
      "width": "200px"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "description",
      "label": "备注",
      "align": "center",
      "width": "400px"
    }
  })], 1), _vm._v(" "), _c('el-pagination', {
    attrs: {
      "page-size": _vm.historyPage.pageSize,
      "current-page": _vm.historyPage.currentPage,
      "layout": _vm.historyPage.layout,
      "total": _vm.historyPage.totals
    },
    on: {
      "current-change": _vm.integralHistory
    }
  }), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "新增个人积分记录",
      "size": "tiny",
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "visible": _vm.editIntegralVisible
    },
    on: {
      "update:visible": function($event) {
        _vm.editIntegralVisible = $event
      }
    }
  }, [_c('el-form', {
    ref: "integralForm",
    attrs: {
      "model": _vm.integralForm,
      "rules": _vm.rules,
      "label-width": "80px"
    }
  }, [_c('el-form-item', {
    attrs: {
      "label": "积分加减",
      "prop": "integral"
    }
  }, [_c('el-input', {
    model: {
      value: (_vm.integralForm.integral),
      callback: function($$v) {
        _vm.integralForm.integral = $$v
      },
      expression: "integralForm.integral"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "label": "积分备注",
      "prop": "description"
    }
  }, [_c('el-input', {
    attrs: {
      "type": "textarea",
      "rows": 3
    },
    model: {
      value: (_vm.integralForm.description),
      callback: function($$v) {
        _vm.integralForm.description = $$v
      },
      expression: "integralForm.description"
    }
  })], 1)], 1), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    slot: "footer"
  }, [_c('el-button', {
    attrs: {
      "type": "primary"
    },
    on: {
      "click": function($event) {
        _vm.saveIntegralInfo('integralForm')
      }
    }
  }, [_vm._v("立即创建")]), _vm._v(" "), _c('el-button', {
    on: {
      "click": function($event) {
        _vm.cancelIntegral()
      }
    }
  }, [_vm._v("取 消")])], 1)], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "评价详情",
      "top": "10%",
      "visible": _vm.showTaskCommentDetail,
      "size": "small"
    },
    on: {
      "update:visible": function($event) {
        _vm.showTaskCommentDetail = $event
      }
    }
  }, [_c('h2', {
    staticStyle: {
      "font-size": "20px",
      "margin-bottom": "20px"
    }
  }, [_vm._v("总体评价：  "), _c('span', [_vm._v(_vm._s(_vm.taskCommentDetail.commentGrade))])]), _vm._v(" "), _vm._l((_vm.taskCommentDetail.comments), function(item, index) {
    return _c('div', [_c('el-form', {
      staticClass: "demo-table-expand",
      attrs: {
        "label-position": "left",
        "inline": ""
      }
    }, [_c('el-form-item', {
      attrs: {
        "label": "姓名"
      }
    }, [_c('span', [_vm._v(_vm._s(item.commentUserName))])]), _vm._v(" "), _c('el-form-item', {
      attrs: {
        "label": "评价"
      }
    }, [_c('span', [_vm._v(_vm._s(item.grade))])]), _vm._v(" "), _c('el-form-item', {
      attrs: {
        "label": "描述"
      }
    }, [_c('span', [_vm._v(_vm._s(item.description))])])], 1)], 1)
  }), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    slot: "footer"
  }, [_c('el-button', {
    attrs: {
      "type": "primary"
    },
    on: {
      "click": function($event) {
        _vm.showTaskCommentDetail = false
      }
    }
  }, [_vm._v("确 定")])], 1)], 2)], 1)
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-5f256490", module.exports)
  }
}

/***/ }),
/* 308 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.showCreateTask),
      expression: "showCreateTask"
    }],
    staticClass: "create-task-pop"
  }, [_c('div', {
    staticClass: "create-task-pop-con"
  }, [_c('div', {
    staticClass: "ctpc-top clearfix"
  }, [_c('span', {
    staticClass: "fl"
  }, [_vm._v("创建多人任务")]), _vm._v(" "), _c('span', {
    staticClass: "my-dialog-title-tool"
  }, [_c('el-button', {
    attrs: {
      "type": "text",
      "icon": "check"
    },
    on: {
      "click": _vm.saveTask
    }
  }, [_vm._v("保存")]), _vm._v(" "), _c('el-button', {
    attrs: {
      "type": "text",
      "icon": "close"
    },
    on: {
      "click": _vm.hide
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-con"
  }, [_c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (!_vm.showDesc),
      expression: "!showDesc"
    }],
    staticClass: "ctpc-instruction-msg",
    on: {
      "click": _vm.showInsChange
    }
  }, [_vm._v("\n                " + _vm._s(_vm.insMsgShow) + "\n            ")]), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.showDesc),
      expression: "showDesc"
    }],
    staticClass: "ctpc-ins-edit"
  }, [_c('textarea', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.taskForm.description),
      expression: "taskForm.description"
    }],
    staticClass: "ctpc-instruction",
    attrs: {
      "placeholder": "任务描述"
    },
    domProps: {
      "value": (_vm.taskForm.description)
    },
    on: {
      "input": function($event) {
        if ($event.target.composing) { return; }
        _vm.taskForm.description = $event.target.value
      }
    }
  }), _vm._v(" "), _c('div', {
    staticClass: "ctpc-btns"
  }, [_c('input', {
    staticClass: "ctpc-cancel",
    attrs: {
      "type": "button",
      "value": "取消"
    },
    on: {
      "click": _vm.cancelEdit
    }
  }), _vm._v(" "), _c('input', {
    staticClass: "ctpc-save",
    attrs: {
      "type": "button",
      "value": "确定"
    },
    on: {
      "click": _vm.saveEdit
    }
  })])]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-main-con"
  }, [_c('div', {
    staticClass: "ctpc-list clearfix"
  }, [_vm._m(0), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list-con fl"
  }, [_c('el-input', {
    attrs: {
      "type": "text",
      "auto-complete": "off"
    },
    model: {
      value: (_vm.taskForm.taskName),
      callback: function($$v) {
        _vm.taskForm.taskName = $$v
      },
      expression: "taskForm.taskName"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list clearfix"
  }, [_vm._m(1), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list-con fl"
  }, [_c('el-select', {
    attrs: {
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.taskForm.projectId),
      callback: function($$v) {
        _vm.taskForm.projectId = $$v
      },
      expression: "taskForm.projectId"
    }
  }, _vm._l((_vm.projectList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list clearfix"
  }, [_vm._m(2), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list-con fl"
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "format": "yyyy-MM-dd",
      "placeholder": "选择日期"
    },
    model: {
      value: (_vm.taskForm.endTime),
      callback: function($$v) {
        _vm.taskForm.endTime = $$v
      },
      expression: "taskForm.endTime"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list clearfix"
  }, [_vm._m(3), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list-con fl"
  }, [_c('el-select', {
    attrs: {
      "clearable": "",
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.taskForm.priority),
      callback: function($$v) {
        _vm.taskForm.priority = $$v
      },
      expression: "taskForm.priority"
    }
  }, _vm._l((_vm.priorityList), function(item) {
    return _c('el-option', {
      key: item.value,
      attrs: {
        "label": item.label,
        "value": item.value
      }
    })
  }))], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list clearfix"
  }, [_vm._m(4), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list-con fl"
  }, [_c('el-select', {
    attrs: {
      "multiple-limit": 1,
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.taskForm.stageId),
      callback: function($$v) {
        _vm.taskForm.stageId = $$v
      },
      expression: "taskForm.stageId"
    }
  }, _vm._l((_vm.stageList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list clearfix"
  }, [_vm._m(5), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list-con fl"
  }, [_c('ul', {
    staticClass: "ctpc-tags"
  }, [_c('li', {
    staticClass: "tag-lis-add"
  }, [_c('div', {
    staticClass: "tag-add-sel"
  }, [_c('el-select', {
    attrs: {
      "multiple": "",
      "placeholder": "添加标签"
    },
    model: {
      value: (_vm.taskForm.tags),
      callback: function($$v) {
        _vm.taskForm.tags = $$v
      },
      expression: "taskForm.tags"
    }
  }, _vm._l((_vm.tagList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1)])])])]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-member-con"
  }, [_vm._l((_vm.taskUsers), function(item, index) {
    return _c('div', {
      staticClass: "ctpc-member-list clearfix in",
      class: item.cssClass
    }, [_c('span', {
      staticClass: "fl ctpc-member-head"
    }, [_vm._v(_vm._s(item.userName))]), _vm._v(" "), _c('span', {
      staticClass: "fl ctpc-member-job-time"
    }, [_vm._v("工作量:" + _vm._s(item.taskHours) + "工时")]), _vm._v(" "), _c('span', {
      staticClass: "fl ctpc-member-end-time"
    }, [_vm._v("截止:" + _vm._s(item.endTime))]), _vm._v(" "), _c('span', {
      staticStyle: {
        "position": "absolute",
        "right": "10px"
      }
    }, [_c('el-button', {
      attrs: {
        "type": "text",
        "icon": "edit"
      },
      on: {
        "click": function($event) {
          _vm.modifyStep(index, _vm.taskUsers)
        }
      }
    }), _vm._v(" "), _c('el-button', {
      attrs: {
        "type": "text",
        "icon": "close"
      },
      on: {
        "click": function($event) {
          _vm.deleteMember(index)
        }
      }
    })], 1)])
  }), _vm._v(" "), _c('div', {
    staticClass: "bdl-line"
  })], 2), _vm._v(" "), (_vm.showAddDetail) ? _c('div', {
    staticClass: "ctpc-add-member-detail"
  }, [_c('el-input', {
    attrs: {
      "type": "textarea",
      "placeholder": "描述该成员的工作内容...",
      "rows": 3
    },
    model: {
      value: (_vm.step.description),
      callback: function($$v) {
        _vm.step.description = $$v
      },
      expression: "step.description"
    }
  }), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic"
  }, [_c('div', {
    staticClass: "add-member-basic-list clearfix"
  }, [_vm._m(6), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-select', {
    attrs: {
      "filterable": "",
      "placeholder": "请选择"
    },
    on: {
      "change": _vm.stepUserChange
    },
    model: {
      value: (_vm.step.userId),
      callback: function($$v) {
        _vm.step.userId = $$v
      },
      expression: "step.userId"
    }
  }, _vm._l((_vm.userList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _vm._m(7), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('input', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.step.taskHours),
      expression: "step.taskHours"
    }],
    staticClass: "member-time-count",
    domProps: {
      "value": (_vm.step.taskHours)
    },
    on: {
      "input": function($event) {
        if ($event.target.composing) { return; }
        _vm.step.taskHours = $event.target.value
      }
    }
  }), _vm._v("工时\n                            ")])]), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-list clearfix"
  }, [_vm._m(8), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "format": "yyyy-MM-dd",
      "placeholder": "选择日期"
    },
    model: {
      value: (_vm.step.beginTime),
      callback: function($$v) {
        _vm.step.beginTime = $$v
      },
      expression: "step.beginTime"
    }
  })], 1), _vm._v(" "), _vm._m(9), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "format": "yyyy-MM-dd",
      "placeholder": "选择日期"
    },
    model: {
      value: (_vm.step.endTime),
      callback: function($$v) {
        _vm.step.endTime = $$v
      },
      expression: "step.endTime"
    }
  })], 1)])]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-btns"
  }, [_c('input', {
    staticClass: "ctpc-cancel",
    attrs: {
      "type": "button",
      "value": "取消"
    },
    on: {
      "click": _vm.cancelAddMember
    }
  }), _vm._v(" "), _c('input', {
    staticClass: "ctpc-save",
    attrs: {
      "type": "button",
      "value": "确定"
    },
    on: {
      "click": _vm.saveAddMember
    }
  })])], 1) : _vm._e(), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (!_vm.showAddDetail),
      expression: "!showAddDetail"
    }],
    staticClass: "add-member-opt",
    on: {
      "click": _vm.addMember
    }
  }, [_c('span', {
    staticClass: "add-member-icon"
  }, [_vm._v("+")]), _vm._v(" "), _c('span', {
    staticClass: "add-member-msg"
  }, [_vm._v("添加成员")])])])])])])
},staticRenderFns: [function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "ctpc-list-menu fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("任务名称")])
},function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "ctpc-list-menu fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("项目")])
},function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "ctpc-list-menu fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("截止日期")])
},function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "ctpc-list-menu fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("优先级")])
},function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "ctpc-list-menu fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("阶段")])
},function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "ctpc-list-menu fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("标签")])
},function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "add-member-basic-menu fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("姓名：")])
},function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "add-member-basic-menu add-member-basic-time fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("工作量：\n                            ")])
},function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "add-member-basic-menu fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("开始日期：")])
},function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "add-member-basic-menu add-member-basic-end fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("截止日期：\n                            ")])
}]}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-6573cde1", module.exports)
  }
}

/***/ }),
/* 309 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', [_c('div', {
    staticClass: "toggle-view"
  }, [_c('input', {
    attrs: {
      "type": "button",
      "value": _vm.btnVal
    },
    on: {
      "click": _vm.btnValFun
    }
  })]), _vm._v(" "), _c('div', {
    staticClass: "task-con"
  }, [_c('div', [_c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.btnValStatus == 1),
      expression: "btnValStatus == 1"
    }],
    staticClass: "task-top clearfix"
  }, [_c('div', {
    staticClass: "clearfix"
  }, [_c('div', {
    staticClass: "clearfix select-box"
  }, [_c('div', {
    staticClass: "task-top-list fl"
  }, [_c('span', {
    staticClass: "ttl-name"
  }, [_vm._v("项目")]), _vm._v(" "), _c('el-select', {
    attrs: {
      "clearable": "",
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.form.projectId),
      callback: function($$v) {
        _vm.form.projectId = $$v
      },
      expression: "form.projectId"
    }
  }, _vm._l((_vm.projectList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('div', {
    staticClass: "task-top-list fl"
  }, [_c('span', {
    staticClass: "ttl-name"
  }, [_vm._v("成员")]), _vm._v(" "), _c('el-select', {
    attrs: {
      "clearable": "",
      "filterable": "",
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.form.userId),
      callback: function($$v) {
        _vm.form.userId = $$v
      },
      expression: "form.userId"
    }
  }, _vm._l((_vm.userList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('div', {
    staticClass: "task-top-list fl"
  }, [_c('span', {
    staticClass: "ttl-name"
  }, [_vm._v("优先级")]), _vm._v(" "), _c('el-select', {
    attrs: {
      "clearable": "",
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.form.priority),
      callback: function($$v) {
        _vm.form.priority = $$v
      },
      expression: "form.priority"
    }
  }, _vm._l((_vm.priorityList), function(item) {
    return _c('el-option', {
      key: item.value,
      attrs: {
        "label": item.label,
        "value": item.value
      }
    })
  }))], 1), _vm._v(" "), _c('div', {
    staticClass: "task-top-list fl"
  }, [_c('span', {
    staticClass: "ttl-name"
  }, [_vm._v("类型")]), _vm._v(" "), _c('el-select', {
    attrs: {
      "clearable": "",
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.form.type),
      callback: function($$v) {
        _vm.form.type = $$v
      },
      expression: "form.type"
    }
  }, _vm._l((_vm.typeList), function(item) {
    return _c('el-option', {
      key: item.value,
      attrs: {
        "label": item.name,
        "value": item.value
      }
    })
  }))], 1), _vm._v(" "), _c('div', {
    staticClass: "task-top-list fl"
  }, [_c('span', {
    staticClass: "ttl-name"
  }, [_vm._v("状态")]), _vm._v(" "), _c('el-select', {
    attrs: {
      "clearable": "",
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.form.status),
      callback: function($$v) {
        _vm.form.status = $$v
      },
      expression: "form.status"
    }
  }, _vm._l((_vm.status), function(item) {
    return _c('el-option', {
      key: item.value,
      attrs: {
        "label": item.name,
        "value": item.value
      }
    })
  }))], 1), _vm._v(" "), _c('div', {
    staticClass: "task-top-list fl"
  }, [_c('span', {
    staticClass: "ttl-name"
  }, [_vm._v("截止日期")]), _vm._v(" "), _c('el-date-picker', {
    attrs: {
      "type": "daterange",
      "picker-options": _vm.pickerOptions,
      "placeholder": "选择日期"
    },
    on: {
      "change": _vm.timeChange
    },
    model: {
      value: (_vm.timeRange),
      callback: function($$v) {
        _vm.timeRange = $$v
      },
      expression: "timeRange"
    }
  })], 1)])]), _vm._v(" "), _c('transition', {
    attrs: {
      "name": "filter"
    }
  }, [_c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.open),
      expression: "open"
    }]
  }, [_c('div', {
    staticClass: "task-top-list clearfix"
  }, [_c('span', {
    staticClass: "ttl-name fl"
  }, [_vm._v("标签 ")]), _vm._v(" "), _c('div', {
    staticClass: "fl tag-name clearfix"
  }, _vm._l((_vm.tagList), function(item) {
    return _c('el-button', {
      key: item.id,
      staticClass: "fl",
      attrs: {
        "type": "",
        "size": "small"
      },
      on: {
        "click": function($event) {
          _vm.addFormTagId(item.id, 1, $event)
        }
      }
    }, [_vm._v(_vm._s(item.name) + "\n                                ")])
  }))]), _vm._v(" "), _c('div', {
    staticClass: "task-top-list  clearfix"
  }, [_c('span', {
    staticClass: "ttl-name fl"
  }, [_vm._v("阶段 ")]), _vm._v(" "), _c('div', {
    staticClass: "fl tag-name clearfix"
  }, _vm._l((_vm.stageList), function(item) {
    return _c('el-button', {
      key: item.id,
      staticClass: "fl",
      attrs: {
        "size": "small"
      },
      on: {
        "click": function($event) {
          _vm.addFormTagId(item.id, 2, $event)
        }
      }
    }, [_vm._v(_vm._s(item.name) + "\n                                ")])
  }))])])]), _vm._v(" "), _c('div', {
    staticClass: "clearfix"
  }, [_c('div', {
    staticClass: "task-top-list fl search-button"
  }, [_c('el-button', {
    attrs: {
      "type": "primary",
      "icon": "search",
      "size": "small",
      "loading": _vm.loading
    },
    on: {
      "click": function($event) {
        _vm.searchTask()
      }
    }
  }, [_vm._v("查询\n                        ")])], 1), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.permit),
      expression: "permit"
    }],
    staticClass: "task-top-list fl creat-task",
    on: {
      "click": _vm.createTaskClick
    }
  }, [_c('span', {
    staticClass: "ttl-add-icon"
  }, [_vm._v("+")]), _vm._v(" "), _c('span', {
    staticClass: "ttl-add-msg"
  }, [_vm._v("创建多人任务")])])]), _vm._v(" "), _c('div', {
    staticClass: "filter-btn"
  }, [_c('span', {
    class: _vm.open ? '' : 'open',
    on: {
      "click": _vm.openFun
    }
  }, [_vm._v(_vm._s(_vm.open ? '收起筛选' : '展开筛选'))])])], 1), _vm._v(" "), _c('div', {
    staticClass: "task-lis-con"
  }, [_c('task-item', {
    attrs: {
      "taskItems": _vm.taskItems,
      "isPrivate": false,
      "taskStatus": "taskList",
      "projectList": _vm.projectList,
      "userList": _vm.userList,
      "stageList": _vm.stageList,
      "tagList": _vm.tagList
    },
    on: {
      "reload": _vm.fetchTaskList
    }
  }), _vm._v(" "), _c('create-task', {
    ref: "createTaskPop",
    attrs: {
      "projectList": _vm.projectList,
      "userList": _vm.userList,
      "stageList": _vm.stageList,
      "tagList": _vm.tagList
    },
    on: {
      "handleFetchTaskList": _vm.fetchTaskList
    }
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "pagination"
  }, [_c('el-pagination', {
    attrs: {
      "current-page": _vm.page.pageNum,
      "page-size": _vm.page.pageSize,
      "layout": _vm.pageLayout,
      "total": _vm.page.total
    },
    on: {
      "current-change": _vm.handleCurrentChange,
      "update:currentPage": function($event) {
        _vm.page.pageNum = $event
      }
    }
  })], 1)]), _vm._v(" "), _c('task-board', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.btnValStatus == 2),
      expression: "btnValStatus == 2"
    }]
  })], 1)])
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-65c5fa76", module.exports)
  }
}

/***/ }),
/* 310 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "login"
  }, [_c('el-form', {
    staticClass: "demo-ruleForm login-container",
    attrs: {
      "label-position": "left",
      "label-width": "0px"
    }
  }, [_c('h1', {
    staticClass: "title"
  }, [_vm._v("系统登录 "), _c('em', [_vm._v(" SYSTEM LOGIN")])]), _vm._v(" "), _c('div', {
    staticClass: "form-items"
  }, [_c('el-form-item', {
    attrs: {
      "prop": "account"
    }
  }, [_c('el-input', {
    staticClass: "form-input",
    attrs: {
      "type": "text",
      "auto-complete": "off",
      "placeholder": "账号"
    },
    model: {
      value: (_vm.loginForm.account),
      callback: function($$v) {
        _vm.loginForm.account = $$v
      },
      expression: "loginForm.account"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "prop": "checkPass"
    }
  }, [_c('el-input', {
    staticClass: "form-input",
    attrs: {
      "type": "password",
      "auto-complete": "off",
      "placeholder": "密码"
    },
    model: {
      value: (_vm.loginForm.password),
      callback: function($$v) {
        _vm.loginForm.password = $$v
      },
      expression: "loginForm.password"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    staticStyle: {
      "width": "100%"
    }
  }, [_c('el-button', {
    staticClass: "form-input",
    staticStyle: {
      "width": "100%"
    },
    attrs: {
      "type": "primary",
      "loading": _vm.button.loading
    },
    on: {
      "click": _vm.login
    }
  }, [_vm._v("登录")])], 1)], 1)])], 1)
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-67e10559", module.exports)
  }
}

/***/ }),
/* 311 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.active),
      expression: "active"
    }],
    staticClass: "el-tab-pane"
  }, [_vm._t("default")], 2)
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-79848d0b", module.exports)
  }
}

/***/ }),
/* 312 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    attrs: {
      "id": "app"
    }
  }, [_c('router-view')], 1)
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-a2833670", module.exports)
  }
}

/***/ }),
/* 313 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "project-con"
  }, [_c('p', {
    staticClass: "mic-title"
  }, [_vm._v("所有项目")]), _vm._v(" "), _vm._l((_vm.TaskItem), function(list) {
    return _c('div', {
      staticClass: "task-item"
    }, [_c('img', {
      staticClass: "task-logo",
      attrs: {
        "src": __webpack_require__(159),
        "alt": ""
      }
    }), _vm._v(" "), _c('div', {
      staticClass: "task-info",
      on: {
        "click": function($event) {
          _vm.editProject(list.id, list.name, list.description)
        }
      }
    }, [_c('div', {
      staticClass: "task-name"
    }, [_vm._v(_vm._s(list.name))]), _vm._v(" "), _c('div', {
      staticClass: "task-sub-name"
    }, [_vm._v(_vm._s(list.description))])])])
  }), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.hasPermission),
      expression: "hasPermission"
    }],
    staticClass: "add-task-item",
    on: {
      "click": _vm.addTask
    }
  }, [_c('div', {
    staticClass: "add-task-btn"
  }, [_vm._v("＋")]), _vm._v(" "), _c('div', {
    staticClass: "add-task-msg"
  }, [_vm._v("创建新项目")])]), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.showAddTask),
      expression: "showAddTask"
    }],
    staticClass: "add-task-pop"
  }, [_c('div', {
    staticClass: "add-task-pop-con"
  }, [_c('div', {
    staticClass: "add-task-top"
  }, [_vm._v("\n        创建项目"), _c('span', {
    staticClass: "close",
    on: {
      "click": _vm.hidePop
    }
  }, [_vm._v("×")])]), _vm._v(" "), _c('img', {
    staticClass: "att-img",
    attrs: {
      "src": __webpack_require__(158),
      "alt": ""
    }
  }), _vm._v(" "), _c('p', {
    staticClass: "att-msg"
  }, [_vm._v("为不同的事物建立各自的项目")]), _vm._v(" "), _c('input', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.project.name),
      expression: "project.name"
    }],
    staticClass: "project-name",
    attrs: {
      "type": "text",
      "placeholder": "项目名称"
    },
    domProps: {
      "value": (_vm.project.name)
    },
    on: {
      "input": function($event) {
        if ($event.target.composing) { return; }
        _vm.project.name = $event.target.value
      }
    }
  }), _vm._v(" "), _c('textarea', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.project.description),
      expression: "project.description"
    }],
    staticClass: "project-intro",
    attrs: {
      "placeholder": "项目简介（选填）"
    },
    domProps: {
      "value": (_vm.project.description)
    },
    on: {
      "input": function($event) {
        if ($event.target.composing) { return; }
        _vm.project.description = $event.target.value
      }
    }
  }), _vm._v(" "), _c('div', {
    staticClass: "att-bents"
  }, [_c('span', {
    staticClass: "cancel",
    on: {
      "click": _vm.hidePop
    }
  }, [_vm._v("取消")]), _vm._v(" "), _c('span', {
    staticClass: "save",
    on: {
      "click": _vm.saveProject
    }
  }, [_vm._v("保存")])])])]), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.editProjectVisible),
      expression: "editProjectVisible"
    }],
    staticClass: "add-task-pop"
  }, [_c('div', {
    staticClass: "add-task-pop-con"
  }, [_c('div', {
    staticClass: "add-task-top"
  }, [_vm._v("\n        编辑项目"), _c('span', {
    staticClass: "close",
    on: {
      "click": _vm.hideEdit
    }
  }, [_vm._v("×")])]), _vm._v(" "), _c('img', {
    staticClass: "att-img",
    attrs: {
      "src": __webpack_require__(158),
      "alt": ""
    }
  }), _vm._v(" "), _c('input', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.project.name),
      expression: "project.name"
    }],
    staticClass: "project-name",
    attrs: {
      "type": "text",
      "placeholder": _vm.project.name
    },
    domProps: {
      "value": (_vm.project.name)
    },
    on: {
      "input": function($event) {
        if ($event.target.composing) { return; }
        _vm.project.name = $event.target.value
      }
    }
  }), _vm._v(" "), _c('textarea', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.project.description),
      expression: "project.description"
    }],
    staticClass: "project-intro",
    attrs: {
      "placeholder": "项目简介（选填）"
    },
    domProps: {
      "value": (_vm.project.description)
    },
    on: {
      "input": function($event) {
        if ($event.target.composing) { return; }
        _vm.project.description = $event.target.value
      }
    }
  }), _vm._v(" "), _c('div', {
    staticClass: "att-bents"
  }, [_c('span', {
    staticClass: "delete",
    on: {
      "click": _vm.deleteWindow
    }
  }, [_vm._v("删除项目")]), _vm._v(" "), _c('span', {
    staticClass: "cancel",
    on: {
      "click": _vm.hideEdit
    }
  }, [_vm._v("取消")]), _vm._v(" "), _c('span', {
    staticClass: "save",
    on: {
      "click": _vm.updateProject
    }
  }, [_vm._v("保存")])])])])], 2)
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-a77519ee", module.exports)
  }
}

/***/ }),
/* 314 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "nav-con",
    on: {
      "click": _vm.hidePerOpt
    }
  }, [_c('div', {
    staticClass: "nav-top-bg"
  }, [_c('div', {
    staticClass: "ntb-con"
  }, [_vm._m(0), _vm._v(" "), _c('div', {
    staticClass: "personal-name",
    on: {
      "click": function($event) {
        $event.preventDefault();
        $event.stopPropagation();
        _vm.personalOpt($event)
      }
    }
  }, [_vm._v("  " + _vm._s(_vm.getUserName) + "\r\n        "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.showPerOpt),
      expression: "showPerOpt"
    }],
    staticClass: "personal-opt"
  }, [_c('div', {
    staticClass: "alter-pwd",
    on: {
      "click": function($event) {
        $event.stopPropagation();
        $event.preventDefault();
        _vm.alterPwd($event)
      }
    }
  }, [_vm._v("修改密码")]), _vm._v(" "), _c('div', {
    staticClass: "logout-btn",
    on: {
      "click": function($event) {
        $event.stopPropagation();
        $event.preventDefault();
        _vm.handleLogout($event)
      }
    }
  }, [_vm._v("退出登录")])])])])]), _vm._v(" "), _c('div', {
    staticClass: "nav-top"
  }, [_c('div', {
    staticClass: "el-tab-bar"
  }, [_c('el-tabs', {
    on: {
      "tab-click": function($event) {
        _vm.handleClick(_vm.activeName)
      }
    },
    model: {
      value: (_vm.activeName),
      callback: function($$v) {
        _vm.activeName = $$v
      },
      expression: "activeName"
    }
  }, _vm._l((_vm.tabs), function(item, idx) {
    return _c('el-tab-pane', {
      key: idx,
      attrs: {
        "label": item.label,
        "name": item.name
      }
    })
  }))], 1)]), _vm._v(" "), _c('router-view'), _vm._v(" "), _c('alter-password', {
    ref: "alterPwdPop"
  })], 1)
},staticRenderFns: [function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "logo"
  }, [_c('img', {
    attrs: {
      "src": __webpack_require__(285),
      "alt": ""
    }
  })])
}]}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-c38d3b7c", module.exports)
  }
}

/***/ }),
/* 315 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', [_vm._l((_vm.taskItems), function(task) {
    return _c('div', {
      staticClass: "task-lis",
      class: task.borderClass,
      on: {
        "click": function($event) {
          _vm.taskItemClick(task.id, task.type)
        }
      }
    }, [_c('div', {
      staticClass: "head-img"
    }, [(task.reviewStatus == 1) ? _c('img', {
      attrs: {
        "src": __webpack_require__(288)
      }
    }) : (_vm.taskStatus == 'auditSuccess') ? _c('img', {
      attrs: {
        "src": __webpack_require__(281)
      }
    }) : ((_vm.taskStatus == 'TaskDoing' && task.reviewStatus == 3) || (_vm.taskStatus == 'taskList' && task.status < 3)) ? _c('img', {
      attrs: {
        "src": __webpack_require__(282)
      }
    }) : ((_vm.taskStatus == 'finished') || task.status == 3) ? _c('img', {
      attrs: {
        "src": __webpack_require__(283)
      }
    }) : (_vm.taskStatus == 'WaitAssess') ? _c('img', {
      attrs: {
        "src": __webpack_require__(287)
      }
    }) : _vm._e()]), _vm._v(" "), _c('div', {
      staticClass: "main-task-detail"
    }, [_c('div', {
      staticClass: "task-name"
    }, [(_vm.isPrivate) ? _c('span', [_vm._v("\n                      " + _vm._s(task.name) + "\n                      "), _vm._l((task.taskUsers), function(item, index) {
      return (task.type == 2) ? _c('span', [(item.userId == _vm.loginUserId && item.description != '') ? _c('span', [_vm._v("(" + _vm._s(item.description) + ")")]) : _vm._e()]) : _vm._e()
    })], 2) : _c('span', [_vm._v(_vm._s(task.name))])]), _vm._v(" "), _c('div', {
      staticClass: "task-state"
    }, [_c('i', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (task.type == 1),
        expression: "task.type==1"
      }],
      staticClass: "iconfont icon-person"
    }), _vm._v(" "), _c('i', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (task.type == 2),
        expression: "task.type==2"
      }],
      staticClass: "iconfont icon-people"
    }), _vm._v(" "), _c('span', {
      staticClass: "task-end",
      class: task.endColor
    }, [_vm._v(_vm._s(task.endText))]), _vm._v(" "), _c('span', {
      staticClass: "task-time-opt"
    }, [_c('i', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (_vm.taskStatus == 'TaskDoing' && task.reviewStatus == 3),
        expression: "taskStatus=='TaskDoing'  && task.reviewStatus ==3"
      }],
      staticClass: "el-icon-circle-check",
      on: {
        "click": function($event) {
          _vm.showFinishedPop(task.id, task.taskUsers[0].id, task.type)
        }
      }
    }), _vm._v(" "), _c('i', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (_vm.taskStatus == 'TaskDoing' && task.type == 1 && task.reviewStatus == 1),
        expression: "taskStatus=='TaskDoing' && task.type==1 && task.reviewStatus==1 "
      }],
      staticClass: "el-icon-edit",
      on: {
        "click": function($event) {
          _vm.modifyPrivateTask(task.id)
        }
      }
    }), _vm._v(" "), _c('i', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (_vm.taskStatus == 'WaitAssess'),
        expression: "taskStatus=='WaitAssess'"
      }],
      staticClass: "el-icon-star-off"
    }), _vm._v(" "), _c('i', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (_vm.taskStatus == 'WaitAuditing'),
        expression: "taskStatus=='WaitAuditing'"
      }],
      staticClass: "el-icon-edit",
      on: {
        "click": function($event) {
          _vm.showAuditPop(task.id, task.taskUsers[0].id)
        }
      }
    }), _vm._v(" "), _c('i', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (_vm.taskStatus == 'auditSuccess'),
        expression: "taskStatus=='auditSuccess' "
      }],
      staticClass: "el-icon-edit",
      on: {
        "click": function($event) {
          $event.stopPropagation();
          _vm.modifyPrivateTask(task.id)
        }
      }
    })]), _vm._v(" "), _c('ul', {
      staticClass: "task-key-tag"
    }, _vm._l((task.tags), function(tag) {
      return _c('li', {
        staticClass: "task-key-lis"
      }, [_c('span', {
        staticClass: "circle",
        style: ({
          background: tag.colorValue
        })
      }), _vm._v(" "), _c('span', {
        staticClass: "task-key-msg"
      }, [_vm._v(_vm._s(tag.name))])])
    }))])]), _vm._v(" "), _c('div', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (_vm.isPrivate && task.status == 3 && _vm.taskStatus != 'WaitAssess'),
        expression: "isPrivate && task.status==3 && taskStatus!='WaitAssess'"
      }],
      staticClass: "task-data-show"
    }, [_c('span', {
      staticClass: "task-score"
    }, [_vm._v("+" + _vm._s(task.userIntegral))]), _vm._v(" "), _c('span', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (task.type == 2),
        expression: "task.type==2"
      }],
      staticClass: "task-level first"
    }, [_vm._v(_vm._s(task.integralGrade))])]), _vm._v(" "), _c('div', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (!_vm.isPrivate && task.status == 1 && _vm.taskStatus != 'WaitAssess'),
        expression: "!isPrivate && task.status==1&& taskStatus!='WaitAssess'"
      }]
    }, [_c('span', {
      staticClass: "mark-stage"
    }, [_vm._v(_vm._s(task.stageName))])]), _vm._v(" "), _c('div', {
      staticClass: "task-mark"
    }, [_c('img', {
      attrs: {
        "src": __webpack_require__(159),
        "alt": ""
      }
    }), _vm._v(" "), _c('span', {
      staticClass: "mark-msg"
    }, [_vm._v(_vm._s(task.projectName))])]), _vm._v(" "), _c('div', {
      staticClass: "task-username"
    }, [_vm._v(_vm._s(task.userName))])])
  }), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.taskItems.length == 0),
      expression: "taskItems.length==0"
    }],
    staticClass: "empty"
  }, [_c('h2', [_vm._v("暂无数据")])]), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "任务信息",
      "top": "10%",
      "visible": _vm.showFinishedTask,
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "size": "tiny",
      "before-close": _vm.hideFinishedPop
    },
    on: {
      "update:visible": function($event) {
        _vm.showFinishedTask = $event
      }
    }
  }, [_c('el-form', [_c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "任务名称："
    }
  }, [_vm._v(_vm._s(_vm.taskDetail.name))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "任务描述："
    }
  }, [_vm._v(_vm._s(_vm.taskDetail.description))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "项目："
    }
  }, [_vm._v(_vm._s(_vm.taskDetail.projectName))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "阶段："
    }
  }, [_vm._v(_vm._s(_vm.taskDetail.stageName))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "优先级："
    }
  }, _vm._l((_vm.priorityList), function(item) {
    return (item.value == _vm.taskDetail.priority) ? _c('span', [_vm._v(_vm._s(item.label))]) : _vm._e()
  })), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "截止时间："
    }
  }, [_vm._v(_vm._s(_vm._f("formatDate")(_vm.taskDetail.endTime)))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "标签："
    }
  }, _vm._l((_vm.taskDetail.tags), function(item, key) {
    return _c('el-tag', {
      key: key,
      staticStyle: {
        "margin": "5px"
      },
      attrs: {
        "type": "gray"
      }
    }, [_vm._v("\n                      " + _vm._s(item.name) + "\n                  ")])
  })), _vm._v(" "), _vm._l((_vm.taskDetail.users), function(item, index) {
    return _c('div', [(item.userId == _vm.loginUserId) ? _c('el-card', {
      staticClass: "box-card"
    }, [_c('div', {
      staticClass: "text item"
    }, [_vm._v("\n                          工作量：" + _vm._s(item.taskHours) + " 工时\n                      ")]), _vm._v(" "), _c('div', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (_vm.taskDetail.type == 2),
        expression: "taskDetail.type==2"
      }],
      staticClass: "text item"
    }, [_vm._v("\n                          截止：" + _vm._s(_vm._f("formatDate")(item.endTime)) + "\n                      ")]), _vm._v(" "), _c('div', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (_vm.taskDetail.type == 2),
        expression: "taskDetail.type==2"
      }],
      staticClass: "text item"
    }, [_vm._v("\n                          描述：" + _vm._s(item.description) + "\n                      ")])]) : _vm._e()], 1)
  })], 2), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    slot: "footer"
  }, [_c('el-button', {
    attrs: {
      "type": "success"
    },
    on: {
      "click": _vm.finishTask
    }
  }, [_vm._v("已完成")])], 1)], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "任务信息",
      "top": "10%",
      "visible": _vm.showAuditTask,
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "custom-class": "myDialog",
      "size": "tiny",
      "before-close": _vm.hideAuditPop
    },
    on: {
      "update:visible": function($event) {
        _vm.showAuditTask = $event
      }
    }
  }, [_c('el-form', [_c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "任务名称："
    }
  }, [_vm._v(_vm._s(_vm.taskDetail.name))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "任务描述："
    }
  }, [_vm._v(_vm._s(_vm.taskDetail.description))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "项目："
    }
  }, [_vm._v(_vm._s(_vm.taskDetail.projectName))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "截止时间："
    }
  }, [_vm._v(_vm._s(_vm._f("formatDate")(_vm.taskDetail.endTime)))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "标签："
    }
  }, _vm._l((_vm.taskDetail.tags), function(item, key) {
    return _c('el-tag', {
      key: key,
      staticStyle: {
        "margin": "5px"
      },
      attrs: {
        "type": "gray"
      }
    }, [_vm._v("\n                      " + _vm._s(item.name) + "\n                  ")])
  })), _vm._v(" "), _vm._l((_vm.taskDetail.users), function(item, index) {
    return _c('div', [_c('el-form-item', {
      staticClass: "task-form",
      attrs: {
        "label": "工作量："
      }
    }, [_vm._v(_vm._s(item.taskHours) + " 工时")]), _vm._v(" "), _c('el-form-item', {
      staticClass: "task-form",
      attrs: {
        "label": "负责人："
      }
    }, [_vm._v(_vm._s(item.userName))])], 1)
  })], 2), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    slot: "footer"
  }, [_c('el-tooltip', {
    attrs: {
      "content": "删除该任务",
      "placement": "top"
    }
  }, [_c('el-button', {
    attrs: {
      "type": "danger",
      "icon": "delete"
    },
    on: {
      "click": _vm.deleteTask
    }
  })], 1), _vm._v(" "), _c('el-tooltip', {
    attrs: {
      "content": "编辑该任务",
      "placement": "top"
    }
  }, [_c('el-button', {
    attrs: {
      "type": "primary",
      "icon": "edit"
    },
    on: {
      "click": function($event) {
        _vm.modifyPrivateTask(_vm.taskDetail.id)
      }
    }
  })], 1), _vm._v(" "), _c('el-button', {
    attrs: {
      "type": "success"
    },
    on: {
      "click": _vm.acceptTask
    }
  }, [_vm._v("审核通过")])], 1)], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "任务详情",
      "top": "10%",
      "visible": _vm.showTaskDetail,
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "custom-class": "myDialog",
      "size": "tiny",
      "before-close": _vm.hideTaskDetail
    },
    on: {
      "update:visible": function($event) {
        _vm.showTaskDetail = $event
      }
    }
  }, [_c('el-form', [_c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "任务名称："
    }
  }, [_vm._v(_vm._s(_vm.taskDetail.name))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "任务描述："
    }
  }, [_vm._v(_vm._s(_vm.taskDetail.description))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "项目："
    }
  }, [_vm._v(_vm._s(_vm.taskDetail.projectName))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "阶段："
    }
  }, [_vm._v(_vm._s(_vm.taskDetail.stageName))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "优先级："
    }
  }, _vm._l((_vm.priorityList), function(item) {
    return (item.value == _vm.taskDetail.priority) ? _c('span', [_vm._v(_vm._s(item.label))]) : _vm._e()
  })), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "截止时间："
    }
  }, [_vm._v(_vm._s(_vm._f("formatDate")(_vm.taskDetail.endTime)))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "标签："
    }
  }, _vm._l((_vm.taskDetail.tags), function(item, key) {
    return _c('el-tag', {
      key: key,
      staticStyle: {
        "margin": "5px"
      },
      attrs: {
        "type": "gray"
      }
    }, [_vm._v("\n                      " + _vm._s(item.name) + "\n                  ")])
  })), _vm._v(" "), (_vm.taskDetail.type == 2) ? _c('div', {
    staticClass: "ctpc-member-con"
  }, [_vm._l((_vm.taskDetail.users), function(item, index) {
    return _c('div', {
      staticClass: "ctpc-member-list clearfix",
      class: item.status > 1 ? 'done' : 'in'
    }, [_c('span', {
      staticClass: "fl ctpc-member-head"
    }, [_vm._v(_vm._s(item.userName))]), _vm._v(" "), _c('span', {
      staticClass: "fl ctpc-member-job-time"
    }, [_vm._v("工作量:" + _vm._s(item.taskHours) + "工时")]), _vm._v(" "), _c('span', {
      staticClass: "fl ctpc-member-end-time"
    }, [_vm._v("截止:" + _vm._s(_vm._f("formatDate")(item.endTime)))]), _vm._v(" "), _c('span', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (item.status == 3),
        expression: "item.status==3"
      }],
      staticClass: "fl ctpc-member-assess"
    }, [_vm._v("评价：" + _vm._s(item.commentGrade))]), _vm._v(" "), _c('a', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (_vm.taskDetail.status > 1 && _vm.userRole === 0 && item.status == 3),
        expression: "taskDetail.status>1 && userRole===0 && item.status==3"
      }],
      attrs: {
        "href": "javascript:;"
      },
      on: {
        "click": function($event) {
          _vm.commentDetail(item.id)
        }
      }
    }, [_vm._v("查看评价")]), _vm._v(" "), _c('el-tooltip', {
      attrs: {
        "content": item.description,
        "placement": "top"
      }
    }, [_c('span', {
      staticClass: "fl",
      staticStyle: {
        "margin-left": "25px"
      }
    }, [_c('i', {
      staticClass: "el-icon-information"
    })])])], 1)
  }), _vm._v(" "), _c('div', {
    staticClass: "bdl-line"
  })], 2) : _vm._l((_vm.taskDetail.users), function(item, index) {
    return _c('div', [_c('el-form-item', {
      staticClass: "task-form",
      attrs: {
        "label": "工作量："
      }
    }, [_vm._v(_vm._s(item.taskHours) + " 工时")]), _vm._v(" "), _c('el-form-item', {
      staticClass: "task-form",
      attrs: {
        "label": "负责人："
      }
    }, [_vm._v(_vm._s(item.userName))])], 1)
  })], 2), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.taskLog.list.length > 0),
      expression: "taskLog.list.length>0"
    }],
    staticClass: "trends"
  }, [_c('div', {
    staticClass: "trends-title clearfix"
  }, [_c('b', {
    staticClass: "fl"
  }, [_vm._v("动态")]), _vm._v(" "), _c('a', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.taskLog.hasNextPage),
      expression: "taskLog.hasNextPage"
    }],
    staticClass: "fr",
    attrs: {
      "href": "javascript:;"
    },
    on: {
      "click": function($event) {
        _vm.taskLogMore(_vm.taskDetail.id)
      }
    }
  }, [_vm._v("显示较早的动态")])]), _vm._v(" "), _c('ul', {
    staticStyle: {
      "height": "100px",
      "overflow": "auto"
    }
  }, _vm._l((_vm.taskLog.list), function(item, index) {
    return _c('li', {
      key: index,
      staticClass: "clearfix"
    }, [_c('div', {
      staticStyle: {
        "float": "left",
        "width": "350px"
      }
    }, [_vm._v(" " + _vm._s(item.title) + " "), _c('div', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (item.content !== ''),
        expression: "item.content!==''"
      }],
      staticClass: "task-title-detail"
    }, [_c('em'), _vm._v(_vm._s(item.content))])]), _vm._v(" "), _c('span', {
      staticStyle: {
        "float": "right",
        "font-size": "13px",
        "padding-right": "10px"
      }
    }, [_vm._v(" " + _vm._s(_vm._f("formatTime")(item.createTime)))])])
  }))]), _vm._v(" "), _c('span', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.permit && _vm.taskDetail.status == 1),
      expression: "permit && taskDetail.status==1"
    }],
    staticClass: "dialog-footer",
    slot: "footer"
  }, [_c('el-tooltip', {
    attrs: {
      "content": "删除该任务",
      "placement": "top"
    }
  }, [_c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.showDelete),
      expression: "showDelete"
    }],
    attrs: {
      "type": "danger",
      "icon": "delete"
    },
    on: {
      "click": _vm.deleteTask
    }
  })], 1), _vm._v(" "), _c('el-tooltip', {
    attrs: {
      "content": "编辑该任务",
      "placement": "top"
    }
  }, [_c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: ((_vm.taskDetail.createBy == _vm.loginUserId || _vm.userRole === 0) && _vm.taskDetail.type == 2),
      expression: "(taskDetail.createBy==loginUserId  || userRole===0 )&& taskDetail.type==2"
    }],
    attrs: {
      "type": "primary",
      "icon": "edit"
    },
    on: {
      "click": _vm.showModifyDescription
    }
  })], 1), _vm._v(" "), _c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.taskDetail.status != 3 && _vm.userRole == 0 && _vm.taskDetail.type == 2),
      expression: "taskDetail.status!=3 && userRole==0 && taskDetail.type==2"
    }],
    attrs: {
      "type": "primary",
      "icon": "check"
    },
    on: {
      "click": _vm.completeTask
    }
  }, [_vm._v("完成")]), _vm._v(" "), _c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.taskDetail.status > 1),
      expression: "taskDetail.status>1"
    }],
    attrs: {
      "type": "primary"
    },
    on: {
      "click": function($event) {
        _vm.showTaskDetail = false
      }
    }
  }, [_vm._v("确定")])], 1)], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "填写修改备注",
      "top": "10%",
      "visible": _vm.modifyDescriptionVisible,
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "custom-class": "myDialog",
      "size": "tiny"
    },
    on: {
      "update:visible": function($event) {
        _vm.modifyDescriptionVisible = $event
      }
    }
  }, [_c('el-form', [_c('el-form-item', {
    attrs: {
      "label": "备注"
    }
  }, [_c('el-input', {
    attrs: {
      "type": "textarea",
      "placeholder": "请填写本次修改备注",
      "maxlength": 500,
      "auto-complete": "off"
    },
    model: {
      value: (_vm.modifyTaskForm.modifyDescription),
      callback: function($$v) {
        _vm.modifyTaskForm.modifyDescription = $$v
      },
      expression: "modifyTaskForm.modifyDescription"
    }
  })], 1)], 1), _vm._v(" "), _c('div', {
    staticClass: "dialog-footer",
    slot: "footer"
  }, [_c('el-button', {
    attrs: {
      "type": "primary"
    },
    on: {
      "click": function($event) {
        _vm.modifyTask(_vm.taskDetail.id)
      }
    }
  }, [_vm._v("确 定")])], 1)], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "评价",
      "top": "10%",
      "visible": _vm.showTaskComment,
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "custom-class": "myDialog",
      "size": "tiny",
      "before-close": _vm.hideWaitAssess
    },
    on: {
      "update:visible": function($event) {
        _vm.showTaskComment = $event
      }
    }
  }, [_c('div', {
    staticClass: "assess-task"
  }, _vm._l((_vm.commentStages), function(stage, index) {
    return _c('div', {
      staticClass: "assess-task-list"
    }, [_c('div', {
      staticClass: "assess-man-detail"
    }, [_c('span', {
      staticClass: "amd-job"
    }, [_vm._v(_vm._s(stage.stageName))]), _vm._v(" "), _c('span', {
      staticClass: "amd-job-time"
    }, [_vm._v("工作量：" + _vm._s(stage.taskHours) + "小时")]), _vm._v(" "), _c('span', {
      staticClass: "amd-during-time"
    }, [_vm._v("截止：" + _vm._s(_vm._f("formatDate")(stage.completeTime)))]), _vm._v(" "), _c('span', {
      staticClass: "amd-name"
    }, [_vm._v(_vm._s(stage.userName))])]), _vm._v(" "), (!stage.myComment) ? _c('div', [_c('el-form', [_c('el-form-item', {
      staticClass: "task-form",
      attrs: {
        "label": "请评价"
      }
    }, [_c('el-radio-group', {
      model: {
        value: (_vm.assessForm.comments[index].grade),
        callback: function($$v) {
          _vm.assessForm.comments[index].grade = $$v
        },
        expression: "assessForm.comments[index].grade"
      }
    }, [_c('el-radio', {
      staticClass: "radio",
      attrs: {
        "label": "A"
      }
    }, [_vm._v("A")]), _vm._v(" "), _c('el-radio', {
      staticClass: "radio",
      attrs: {
        "label": "B"
      }
    }, [_vm._v("B")]), _vm._v(" "), _c('el-radio', {
      staticClass: "radio",
      attrs: {
        "label": "C"
      }
    }, [_vm._v("C")])], 1), _vm._v(" "), _c('el-input', {
      attrs: {
        "type": "textarea",
        "placeholder": "请输入你的评价"
      },
      model: {
        value: (_vm.assessForm.comments[index].description),
        callback: function($$v) {
          _vm.assessForm.comments[index].description = $$v
        },
        expression: "assessForm.comments[index].description"
      }
    })], 1)], 1)], 1) : _c('div', [_c('p', [_vm._v("我的评价")]), _vm._v(" "), _c('div', [_c('p', [_vm._v("等级：" + _vm._s(stage.myComment.grade))]), _vm._v(" "), _c('p', [_vm._v("内容:" + _vm._s(stage.myComment.description))])])])])
  })), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    slot: "footer"
  }, [_c('el-button', {
    on: {
      "click": _vm.hideWaitAssess
    }
  }, [_vm._v("取消")]), _vm._v(" "), _c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (!_vm.allComment),
      expression: "!allComment"
    }],
    attrs: {
      "type": "primary"
    },
    on: {
      "click": _vm.taskAssess
    }
  }, [_vm._v("完成")])], 1)]), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "编辑多人任务",
      "top": "10%",
      "visible": _vm.showTaskModify,
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "size": "tiny",
      "show-close": false,
      "custom-class": "myDialog",
      "before-close": _vm.hideTaskModify
    },
    on: {
      "update:visible": function($event) {
        _vm.showTaskModify = $event
      }
    }
  }, [_c('span', {
    slot: "title"
  }, [_c('span', {
    staticClass: "my-dialog-title"
  }, [_vm._v("编辑任务")]), _vm._v(" "), _c('span', {
    staticClass: "my-dialog-title-tool"
  }, [_c('el-button', {
    attrs: {
      "type": "text",
      "icon": "check"
    },
    on: {
      "click": _vm.saveTaskInfo
    }
  }, [_vm._v("保存")]), _vm._v(" "), _c('el-button', {
    attrs: {
      "type": "text",
      "icon": "close"
    },
    on: {
      "click": _vm.hideTaskModify
    }
  })], 1)]), _vm._v(" "), _c('el-form', {
    attrs: {
      "label-width": "80px"
    }
  }, [_c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": ""
    }
  }, [_c('span', {
    slot: "label"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("任务名称")]), _vm._v(" "), _c('el-input', {
    staticStyle: {
      "width": "300px"
    },
    model: {
      value: (_vm.modifyTaskForm.taskName),
      callback: function($$v) {
        _vm.modifyTaskForm.taskName = $$v
      },
      expression: "modifyTaskForm.taskName"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": ""
    }
  }, [_c('span', {
    slot: "label"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("项目")]), _vm._v(" "), _c('el-select', {
    attrs: {
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.modifyTaskForm.projectId),
      callback: function($$v) {
        _vm.modifyTaskForm.projectId = $$v
      },
      expression: "modifyTaskForm.projectId"
    }
  }, _vm._l((_vm.projectList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": ""
    }
  }, [_c('span', {
    slot: "label"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("优先级")]), _vm._v(" "), _c('el-select', {
    attrs: {
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.modifyTaskForm.priority),
      callback: function($$v) {
        _vm.modifyTaskForm.priority = $$v
      },
      expression: "modifyTaskForm.priority"
    }
  }, _vm._l((_vm.priorityList), function(item) {
    return _c('el-option', {
      key: item.value,
      attrs: {
        "label": item.label,
        "value": item.value
      }
    })
  }))], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": ""
    }
  }, [_c('span', {
    slot: "label"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("截止日期")]), _vm._v(" "), _c('el-date-picker', {
    attrs: {
      "type": "datetime",
      "format": "yyyy-MM-dd",
      "placeholder": "选择日期时间"
    },
    model: {
      value: (_vm.modifyTaskForm.endTime),
      callback: function($$v) {
        _vm.modifyTaskForm.endTime = $$v
      },
      expression: "modifyTaskForm.endTime"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": ""
    }
  }, [_c('span', {
    slot: "label"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("阶段")]), _vm._v(" "), _c('el-select', {
    attrs: {
      "multiple-limit": 1,
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.modifyTaskForm.stageId),
      callback: function($$v) {
        _vm.modifyTaskForm.stageId = $$v
      },
      expression: "modifyTaskForm.stageId"
    }
  }, _vm._l((_vm.stageList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": ""
    }
  }, [_c('span', {
    slot: "label"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("标签")]), _vm._v(" "), _c('el-select', {
    attrs: {
      "multiple": "",
      "placeholder": "请选择标签"
    },
    model: {
      value: (_vm.modifyTaskForm.tags),
      callback: function($$v) {
        _vm.modifyTaskForm.tags = $$v
      },
      expression: "modifyTaskForm.tags"
    }
  }, _vm._l((_vm.tagList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": ""
    }
  }, [_c('span', {
    slot: "label"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("任务描述")]), _vm._v(" "), _c('el-input', {
    attrs: {
      "type": "textarea",
      "rows": 3
    },
    model: {
      value: (_vm.modifyTaskForm.description),
      callback: function($$v) {
        _vm.modifyTaskForm.description = $$v
      },
      expression: "modifyTaskForm.description"
    }
  })], 1)], 1), _vm._v(" "), _c('div', {
    staticClass: "ctpc-member-con"
  }, [_vm._l((_vm.modifyTaskForm.taskUsers), function(item, index) {
    return _c('div', {
      staticClass: "ctpc-member-list clearfix",
      class: [item.status > 1 ? 'done' : 'in', item.cssClass]
    }, [_c('span', {
      staticClass: "fl ctpc-member-head"
    }, [_vm._v(_vm._s(item.userName))]), _vm._v(" "), _c('span', {
      staticClass: "fl ctpc-member-job-time"
    }, [_vm._v("工作量:" + _vm._s(item.taskHours) + "工时")]), _vm._v(" "), _c('span', {
      staticClass: "fl ctpc-member-end-time"
    }, [_vm._v("截止:" + _vm._s(_vm._f("formatDate")(item.endTime)))]), _vm._v(" "), _c('span', {
      staticStyle: {
        "position": "absolute",
        "right": "10px"
      }
    }, [_c('el-button', {
      attrs: {
        "type": "text",
        "icon": "edit"
      },
      on: {
        "click": function($event) {
          _vm.modifyStep(index, _vm.modifyTaskForm.taskUsers)
        }
      }
    }), _vm._v(" "), _c('el-button', {
      attrs: {
        "type": "text",
        "icon": "close"
      },
      on: {
        "click": function($event) {
          _vm.deleteMember(index)
        }
      }
    })], 1)])
  }), _vm._v(" "), _c('div', {
    staticClass: "bdl-line"
  })], 2), _vm._v(" "), (_vm.showAddDetail) ? _c('div', {
    staticClass: "ctpc-add-member-detail"
  }, [_c('el-input', {
    attrs: {
      "type": "textarea",
      "placeholder": "描述该成员的工作内容...",
      "rows": 3
    },
    model: {
      value: (_vm.step.description),
      callback: function($$v) {
        _vm.step.description = $$v
      },
      expression: "step.description"
    }
  }), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic"
  }, [_c('div', {
    staticClass: "add-member-basic-list clearfix"
  }, [_c('div', {
    staticClass: "add-member-basic-menu fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("姓名：")]), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-select', {
    attrs: {
      "filterable": "",
      "placeholder": "请选择"
    },
    on: {
      "change": _vm.stepUserChange
    },
    model: {
      value: (_vm.step.userId),
      callback: function($$v) {
        _vm.step.userId = $$v
      },
      expression: "step.userId"
    }
  }, _vm._l((_vm.userList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-menu add-member-basic-time fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("工作量：\n                      ")]), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-input', {
    staticStyle: {
      "width": "70px"
    },
    model: {
      value: (_vm.step.taskHours),
      callback: function($$v) {
        _vm.step.taskHours = $$v
      },
      expression: "step.taskHours"
    }
  }), _vm._v("\n                          工时\n                      ")], 1)]), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-list clearfix"
  }, [_c('div', {
    staticClass: "add-member-basic-menu fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("开始日期：")]), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-date-picker', {
    attrs: {
      "format": "yyyy-MM-dd",
      "type": "date",
      "placeholder": "选择日期"
    },
    model: {
      value: (_vm.step.beginTime),
      callback: function($$v) {
        _vm.step.beginTime = $$v
      },
      expression: "step.beginTime"
    }
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-menu add-member-basic-end fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("截止日期：\n                      ")]), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "format": "yyyy-MM-dd",
      "placeholder": "选择日期"
    },
    model: {
      value: (_vm.step.endTime),
      callback: function($$v) {
        _vm.step.endTime = $$v
      },
      expression: "step.endTime"
    }
  })], 1)])]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-btns"
  }, [_c('input', {
    staticClass: "ctpc-cancel",
    attrs: {
      "type": "button",
      "value": "取消"
    },
    on: {
      "click": _vm.cancelAddMember
    }
  }), _vm._v(" "), _c('input', {
    staticClass: "ctpc-save",
    attrs: {
      "type": "button",
      "value": "确定"
    },
    on: {
      "click": _vm.saveAddMember
    }
  })])], 1) : _vm._e(), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (!_vm.showAddDetail),
      expression: "!showAddDetail"
    }],
    staticClass: "add-member-opt",
    on: {
      "click": _vm.addMember
    }
  }, [_c('span', {
    staticClass: "add-member-icon"
  }, [_vm._v("+")]), _vm._v(" "), _c('span', {
    staticClass: "add-member-msg"
  }, [_vm._v("添加成员")])])], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "评价详情",
      "top": "10%",
      "visible": _vm.showTaskCommentDetail,
      "size": "tiny",
      "before-close": _vm.hideTaskCommentDetail
    },
    on: {
      "update:visible": function($event) {
        _vm.showTaskCommentDetail = $event
      }
    }
  }, [_c('h2', {
    staticStyle: {
      "font-size": "20px",
      "margin-bottom": "20px"
    }
  }, [_vm._v("总体评价：  "), _c('span', [_vm._v(_vm._s(_vm.taskCommentDetail.commentGrade))])]), _vm._v(" "), _vm._l((_vm.taskCommentDetail.comments), function(item, index) {
    return _c('div', [_c('el-form', {
      staticClass: "demo-table-expand",
      attrs: {
        "label-position": "left",
        "inline": ""
      }
    }, [_c('el-form-item', {
      staticClass: "task-form",
      attrs: {
        "label": "姓名"
      }
    }, [_c('span', [_vm._v(_vm._s(item.commentUserName))])]), _vm._v(" "), _c('el-form-item', {
      staticClass: "task-form",
      attrs: {
        "label": "评价"
      }
    }, [_c('span', [_vm._v(_vm._s(item.grade))])]), _vm._v(" "), _c('el-form-item', {
      staticClass: "task-form",
      attrs: {
        "label": "描述"
      }
    }, [_c('span', [_vm._v(_vm._s(item.description))])])], 1)], 1)
  }), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    slot: "footer"
  }, [_c('el-button', {
    attrs: {
      "type": "primary"
    },
    on: {
      "click": _vm.hideTaskCommentDetail
    }
  }, [_vm._v("确 定")])], 1)], 2), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "编辑个人任务",
      "size": "tiny",
      "custom-class": "myDialog",
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "visible": _vm.showModifyPrivateTask
    },
    on: {
      "update:visible": function($event) {
        _vm.showModifyPrivateTask = $event
      }
    }
  }, [_c('el-form', {
    attrs: {
      "model": _vm.modifyPrivateTaskForm,
      "label-width": "80px"
    }
  }, [_c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": "项目"
    }
  }, [_c('el-select', {
    attrs: {
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.modifyPrivateTaskForm.projectId),
      callback: function($$v) {
        _vm.modifyPrivateTaskForm.projectId = $$v
      },
      expression: "modifyPrivateTaskForm.projectId"
    }
  }, _vm._l((_vm.projectList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": "截止日期"
    }
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "format": "yyyy-MM-dd",
      "placeholder": "选择日期时间"
    },
    model: {
      value: (_vm.modifyPrivateTaskForm.endTime),
      callback: function($$v) {
        _vm.modifyPrivateTaskForm.endTime = $$v
      },
      expression: "modifyPrivateTaskForm.endTime"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": "工作量"
    }
  }, [_c('el-input', {
    staticStyle: {
      "width": "100px"
    },
    attrs: {
      "maxlength": 2
    },
    model: {
      value: (_vm.modifyPrivateTaskForm.taskHours),
      callback: function($$v) {
        _vm.modifyPrivateTaskForm.taskHours = $$v
      },
      expression: "modifyPrivateTaskForm.taskHours"
    }
  }), _vm._v("\n                  小时\n              ")], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": "任务名称"
    }
  }, [_c('el-input', {
    model: {
      value: (_vm.modifyPrivateTaskForm.taskName),
      callback: function($$v) {
        _vm.modifyPrivateTaskForm.taskName = $$v
      },
      expression: "modifyPrivateTaskForm.taskName"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": "任务描述"
    }
  }, [_c('el-input', {
    attrs: {
      "type": "textarea",
      "rows": 3
    },
    model: {
      value: (_vm.modifyPrivateTaskForm.description),
      callback: function($$v) {
        _vm.modifyPrivateTaskForm.description = $$v
      },
      expression: "modifyPrivateTaskForm.description"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": "阶段"
    }
  }, [_c('el-select', {
    attrs: {
      "default-first-option": "",
      "placeholder": "请选择阶段"
    },
    model: {
      value: (_vm.modifyPrivateTaskForm.stageId),
      callback: function($$v) {
        _vm.modifyPrivateTaskForm.stageId = $$v
      },
      expression: "modifyPrivateTaskForm.stageId"
    }
  }, _vm._l((_vm.stageList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": "标签"
    }
  }, [_c('el-select', {
    attrs: {
      "multiple": "",
      "placeholder": "请选择标签"
    },
    model: {
      value: (_vm.modifyPrivateTaskForm.tags),
      callback: function($$v) {
        _vm.modifyPrivateTaskForm.tags = $$v
      },
      expression: "modifyPrivateTaskForm.tags"
    }
  }, _vm._l((_vm.tagList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1)], 1), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    slot: "footer"
  }, [_c('el-button', {
    attrs: {
      "type": "primary"
    },
    on: {
      "click": _vm.saveModifyPrivateTaskForm
    }
  }, [_vm._v("保存")]), _vm._v(" "), _c('el-button', {
    on: {
      "click": _vm.hideModifyPrivateTaskDialog
    }
  }, [_vm._v("取 消")])], 1)], 1)], 2)
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-e45d7c10", module.exports)
  }
}

/***/ }),
/* 316 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.showAlterPwd),
      expression: "showAlterPwd"
    }],
    staticClass: "add-member-pop"
  }, [_c('div', {
    staticClass: "alter-pwd-pop"
  }, [_c('div', {
    staticClass: "alter-top clearfix"
  }, [_c('span', {
    staticClass: "fl"
  }, [_vm._v("修改密码")]), _c('span', {
    staticClass: "close-alter-pwd fr",
    on: {
      "click": _vm.hide
    }
  }, [_vm._v("×")])]), _vm._v(" "), _c('div', {
    staticClass: "alter-con"
  }, [_c('div', {
    staticClass: "alter-list clearfix"
  }, [_c('div', {
    staticClass: "alter-menu fl"
  }, [_vm._v("旧密码")]), _vm._v(" "), _c('div', {
    staticClass: "alter-con fl"
  }, [_c('el-input', {
    attrs: {
      "type": "text",
      "placeholder": "请输入旧密码"
    },
    model: {
      value: (_vm.modifyForm.originalPassword),
      callback: function($$v) {
        _vm.modifyForm.originalPassword = $$v
      },
      expression: "modifyForm.originalPassword"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "alter-list clearfix"
  }, [_c('div', {
    staticClass: "alter-menu fl"
  }, [_vm._v("新密码")]), _vm._v(" "), _c('div', {
    staticClass: "alter-con fl"
  }, [_c('el-input', {
    attrs: {
      "type": "password",
      "placeholder": "请输入新密码"
    },
    model: {
      value: (_vm.modifyForm.newPassword),
      callback: function($$v) {
        _vm.modifyForm.newPassword = $$v
      },
      expression: "modifyForm.newPassword"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "alter-list clearfix"
  }, [_c('div', {
    staticClass: "alter-menu fl"
  }, [_vm._v("确认新密码")]), _vm._v(" "), _c('div', {
    staticClass: "alter-con fl"
  }, [_c('el-input', {
    attrs: {
      "type": "password",
      "placeholder": "请再次输入新密码"
    },
    model: {
      value: (_vm.modifyForm.sureNewPwd),
      callback: function($$v) {
        _vm.modifyForm.sureNewPwd = $$v
      },
      expression: "modifyForm.sureNewPwd"
    }
  })], 1)]), _vm._v(" "), _c('el-button', {
    staticClass: "save-alter-btn",
    attrs: {
      "type": "primary",
      "size": "small",
      "loading": _vm.button.loading
    },
    on: {
      "click": _vm.handleModifyPwd
    }
  }, [_vm._v(_vm._s(_vm.button.btnName))])], 1)])])
},staticRenderFns: []}
module.exports.render._withStripped = true
if (false) {
  module.hot.accept()
  if (module.hot.data) {
     require("vue-loader/node_modules/vue-hot-reload-api").rerender("data-v-f9a39a7e", module.exports)
  }
}

/***/ }),
/* 317 */,
/* 318 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(233);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("31cac098", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-03088b73\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Organization.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-03088b73\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Organization.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 319 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(234);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("062287de", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-0f1dfbc3\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./AddDepartment.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-0f1dfbc3\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./AddDepartment.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 320 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(235);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("429fd9ba", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-176c51b4\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./ModifyMember.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-176c51b4\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./ModifyMember.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 321 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(236);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("4c5584b1", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-2346b0e8\",\"scoped\":true,\"hasInlineConfig\":false}!../../../node_modules/._less-loader@4.0.5@less-loader/dist/cjs.js?{\"sourceMap\":false}!../../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./TaskBoard.vue", function() {
     var newContent = require("!!../../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-2346b0e8\",\"scoped\":true,\"hasInlineConfig\":false}!../../../node_modules/._less-loader@4.0.5@less-loader/dist/cjs.js?{\"sourceMap\":false}!../../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./TaskBoard.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 322 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(237);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("5a4a70e4", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-256e98ea\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./AddMember.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-256e98ea\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./AddMember.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 323 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(238);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("2a9ac5fa", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-412fc1af\",\"scoped\":false,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./NavIndex.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-412fc1af\",\"scoped\":false,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./NavIndex.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 324 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(239);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("8cd38434", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-412fc1af\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=1!./NavIndex.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-412fc1af\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=1!./NavIndex.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 325 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(240);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("50d31706", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-42ed7ea2\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Intergral.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-42ed7ea2\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Intergral.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 326 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(241);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("5b02caba", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-52cc4f8f\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Stats.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-52cc4f8f\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Stats.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 327 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(242);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("6750947d", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-5f256490\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./IntegralHistory.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-5f256490\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./IntegralHistory.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 328 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(243);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("d6c93de2", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-6573cde1\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./CreateTask.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-6573cde1\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./CreateTask.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 329 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(244);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("2815e228", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-65c5fa76\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Task.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-65c5fa76\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Task.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 330 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(245);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("26f9b3af", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-67e10559\",\"scoped\":false,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=1!./Login.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-67e10559\",\"scoped\":false,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=1!./Login.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 331 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(246);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("24479169", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-67e10559\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Login.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-67e10559\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Login.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 332 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(247);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("cabbe052", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-a2833670\",\"scoped\":false,\"hasInlineConfig\":false}!../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./App.vue", function() {
     var newContent = require("!!../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-a2833670\",\"scoped\":false,\"hasInlineConfig\":false}!../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./App.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 333 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(248);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("03084f02", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-a77519ee\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Project.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-a77519ee\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Project.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 334 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(249);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("d15511fe", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-c38d3b7c\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Index.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-c38d3b7c\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./Index.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 335 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(250);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("7cb792f6", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-e45d7c10\",\"scoped\":false,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./TaskItem.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-e45d7c10\",\"scoped\":false,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./TaskItem.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 336 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(251);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("1008e8fb", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-e45d7c10\",\"scoped\":false,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=5!./TaskItem.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-e45d7c10\",\"scoped\":false,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=5!./TaskItem.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 337 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(252);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("7d5ae9f8", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-e45d7c10\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=1!./TaskItem.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-e45d7c10\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=1!./TaskItem.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 338 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(253);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("63f6700e", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-e45d7c10\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=2!./TaskItem.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-e45d7c10\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=2!./TaskItem.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 339 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(254);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("1eaea5fa", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-e45d7c10\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=3!./TaskItem.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-e45d7c10\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=3!./TaskItem.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 340 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(255);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("6f5883fb", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-e45d7c10\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=4!./TaskItem.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-e45d7c10\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=4!./TaskItem.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ }),
/* 341 */
/***/ (function(module, exports, __webpack_require__) {

// style-loader: Adds some css to the DOM by adding a <style> tag

// load the styles
var content = __webpack_require__(256);
if(typeof content === 'string') content = [[module.i, content, '']];
if(content.locals) module.exports = content.locals;
// add the styles to the DOM
var update = __webpack_require__(2)("6d1debc6", content, false);
// Hot Module Replacement
if(false) {
 // When the styles change, update the <style> tags
 if(!content.locals) {
   module.hot.accept("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-f9a39a7e\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./AlterPassword.vue", function() {
     var newContent = require("!!../../node_modules/._css-loader@0.28.4@css-loader/index.js?{\"minimize\":false,\"sourceMap\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/style-compiler/index.js?{\"vue\":true,\"id\":\"data-v-f9a39a7e\",\"scoped\":true,\"hasInlineConfig\":false}!../../node_modules/._vue-loader@12.2.2@vue-loader/lib/selector.js?type=styles&index=0!./AlterPassword.vue");
     if(typeof newContent === 'string') newContent = [[module.id, newContent, '']];
     update(newContent);
   });
 }
 // When the module is disposed, remove the <style> tags
 module.hot.dispose(function() { update(); });
}

/***/ })
],[230]);
//# sourceMappingURL=app.6f8261bcddbe841c4aa5.js.map
webpackJsonp([1],[
/* 0 */,
/* 1 */,
/* 2 */,
/* 3 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_axios__ = __webpack_require__(206);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_axios___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_axios__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_element_ui__ = __webpack_require__(29);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_element_ui___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1_element_ui__);
// http



/* harmony default export */ __webpack_exports__["a"] = ({
    //请求基础路径
    API_ROOT: function () {
        if (true) {
            return 'http://zsy.dev.xueping.com/armor/api';
        } else if (process.env.NODE_ENV === 'testing') {
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
/* 4 */
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
            tmp.departmentId = null;
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
        let Base64 = __webpack_require__(307).Base64;
        return eval('(' + Base64.decode(output) + ')');
    }

});

/***/ }),
/* 5 */,
/* 6 */,
/* 7 */,
/* 8 */,
/* 9 */,
/* 10 */,
/* 11 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABIAAAASCAYAAABWzo5XAAACLklEQVQ4EZ1TO2hUQRQ9M29NrIRAUCR+KosgQnQjEolYaKXWYjaFnVrYGBELLVLYGLGwtDSa3RgIiJ0IgpUoWZWABNlKEDtF8JeNu3s9Z8xdXh5GwYF9d865Z+7cz2xAYe27ZycQMR6AEQRshuEz8aIZ5psZ7rw5GVYKRxKk/vcq37WtKKFK4pBzRctg7xi8Uh8Lz4u+FCgFyfAsBGxzgQEf6HxP20e7K8c3O4ZjryrhqXOyUZ9QQs2D8GCjbTjCW3csjIWDtIOtDgYpe5y0QG8WcH/PjPUJ+4qpJ8CoCAXJVjBavO31eGgsvMVxlvVg9WB/T8QlDyIb1VgnmPK5F6fDR8dr7GTo/Ig4wz59SbyhkvdH1j8iQj0pZpIXas+JfWILHmmvVuyv2XbttWIaMTdqbGL+8WFGXV0L2OLymN4Jkabj5N/sGl0b312rHi0KaMRDM9YdswvytnzbNrCko+KY2XL8hob7I4l5B6WIW5i09CScy9uwCVd44cAq97B+Nvx0f9g9Zz0bW1jiTTsTyRFrOmqsi1ImDEJ8VRzLa1vA0MtTYck1vAAo1+wAzROCXmGNWNNRY9UTlZPLRBJpLtcr4WYC/KRAAnurdlgvltt+4T8tZcIDmfvywbr90Btq8q9A8ZQycbEs8TJ/cypHh93HTK+Xq3ZRuJuRO93qsaV3whFrOvnGlmdtIhimXMvLJ9YN5KL17HDNLtB3Q34G+vrfgRRgeNbOs9RrjDT9C0k+xAuabKUvAAAAAElFTkSuQmCC"

/***/ }),
/* 12 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/waitAudit.df47cf0.png";

/***/ }),
/* 13 */
/***/ (function(module, exports, __webpack_require__) {

var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(225),
  /* template */
  __webpack_require__(352),
  /* styles */
  null,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 14 */,
/* 15 */,
/* 16 */,
/* 17 */,
/* 18 */,
/* 19 */,
/* 20 */,
/* 21 */,
/* 22 */,
/* 23 */,
/* 24 */,
/* 25 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/auditSuccess.5989144.png";

/***/ }),
/* 26 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(289)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(238),
  /* template */
  __webpack_require__(347),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-5f256490",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 27 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(287)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(239),
  /* template */
  __webpack_require__(343),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-42ed7ea2",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 28 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(291)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(246),
  /* template */
  __webpack_require__(349),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-65c5fa76",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
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
/* 156 */,
/* 157 */,
/* 158 */,
/* 159 */,
/* 160 */,
/* 161 */,
/* 162 */,
/* 163 */,
/* 164 */,
/* 165 */,
/* 166 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkJBODVBMUJBOEQ1NzExRTc5Njg5RjA1MDBFQTQ0QzQ1IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkJBODVBMUJCOEQ1NzExRTc5Njg5RjA1MDBFQTQ0QzQ1Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6QkE4NUExQjg4RDU3MTFFNzk2ODlGMDUwMEVBNDRDNDUiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6QkE4NUExQjk4RDU3MTFFNzk2ODlGMDUwMEVBNDRDNDUiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz6rtpSkAAADHUlEQVR42rxYzWsTURCfbZpNE1JKmkvxIhU04kVEMBWqoTkUxIsUxIueelCsB/8BT/oPiKCWHnsqfuBFD1r6QYoQ0IOgovUDBVFBk1oTkibZzTqTjLKfyXu7MQM/dt/um5lf5s2bNxvFMAywyHMF+iqHrf4HfZrZhziFmEYcQCT5eQHxGvEY8QCxKWtYkYxQBnEVcUzQfg5xBbEuGqEBQcNDiNuIVQkywHNXWXdIREGE0ChiBXGeIupjeRXWXWFbHaVbDsUQjxBp+4t6A2CnDlDDa1PnXxcCiIQxFCqAGnbYOsq2soiK3whdt5PR0HnxdxuVHQAdx5QFBLqnZ3/fa7rDXppt+loySuBZe1QK2+1rN+kwd5ZtSxO6Zs4Z+rVbJYyEIZ48NJd0bJFS2LYUoYOISfOD7bIcGTMp0rXJJPsQJjRjD39Dc58YimVg9MhLGJs2Wlc3IV2XpZuR2WWWWkO7yU3iexchPn5WKFJkw7bzjstEaL95UPNIYjVxCLTyJ6j9yHcl5GIjJUMoaR40dfdJ5Q834OfTcWiU33Ul5GIj6bdSe9bnemG+54e/F6GCJXEHeuAo1NlHN0JvzIPwYHBCqtPGWxlCOctRHwlOyMVGTobQffOADswgUSLdiPOwvSdD6AViw/xgJI657ZHcevUL1H+9Aq303rkflLauTTbYh1THmOHmSjFXbJnzjMgkhh0F0eAWZE22Y6S2c8GSmGg4OSK2fDSH5rr0RQv/yPjoqWPc6aXdKm+NGzTqgygaVB6IQER1zRmSvKNBk/zqIMWTiId2UuTQw6mX5NlWJWhPTQVsCjHP6y/dgbBu1qsY+vnqqCIuELHmt6QwE56bZd2KiI5sdVmvLU2BMlqC0J6vENr9vXWvRNv9iVFVwSgOg/55DPSPu1r30ct312Qc+Cp35EgrpkB7lur54ercZV5rdu60OSekfEQX7wQ+7WWEmttLiDm+DyRBz/EtxBnEEx5Tp7aESPS6HxIRcj5hIgN8P8Hv+kpomQul298tm/xuuV+EbiFO8HJ1Wkqac7MfOXRRItnn/tu275f8EWAA+9wBIsXQj44AAAAASUVORK5CYII="

/***/ }),
/* 167 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkMwMTM1RkRCOEQ1NzExRTdCRUFCRjdBODFEMjM0RjU4IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkMwMTM1RkRDOEQ1NzExRTdCRUFCRjdBODFEMjM0RjU4Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6QzAxMzVGRDk4RDU3MTFFN0JFQUJGN0E4MUQyMzRGNTgiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6QzAxMzVGREE4RDU3MTFFN0JFQUJGN0E4MUQyMzRGNTgiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz5afCihAAAD8klEQVR42rxYX0hTYRQ/d2vqrptmilmZRUJJEVEEs8gioyKCikgKsl4kioyg3oIiIiF77KWSXoJ6iSzsoR76H0okROCD4Z/MIVardP7ZdnWb2zrn7tPun+/O3U33gx/bd+93zvfjO+c739mEWCwGKjzeDBlFzWfVcEGKblYjDyH3INciC9nzYeRX5EtkC7LHrGOzgnYgryOrDN4vYdyFvIlsRV5Bfkh2AUuS83KQd5HvEojhoYrZ3GU+5kTQIuRb5GmkkEJ4BWb7lvlKK2Qi8gXSpX0hBXwwPuYFye+DcDgkP7PZskB0OMGZtwhy8VODLcxXNZmnKuiWVkwoOAmenwOyIC1CoSCEvEEY9Q6BmOuEkqVlkJWtipSL+TyVSsgogeu0u+Lu6+KK4e2gwdw65tu0oAZlztDODA70QTQa0Wf88iNcBzSXbMhWk1MNZkO2AblN+eDXDzdEI2oxiw+2QP663WDJFuWxv68dfjXXQkT69l8U2pDtilUVStNtbI2OZHfosHIQ8I/DhBRQTVjougEFmw7K3388vQRjnW/AUe6CZcdbdM7IlnwkWmO2HVLVGt/YiD6BR9ww9PEhBD2d4OtoBKm3GXerF8TSdVyH5CPXkad8tN2MINX+BjhJLPU0yZxGQdXVmbDxwPGxxoygQuVgitUZHor2PoCirbVxkYOdcg7xwPFRmPLVIQjG06T+Nhj58gyiQUkOl3N9XVqXv9FKw6pttNmMBWHYfj87BIPNF+Vx8a7z/OOMVTzRGrMJ6lIO7PZc3YTSk5+g4lpMPvqEsDduMl0CdHeQ6NA+6jYjqFU5yFuovxP93e/kT6pDlEelx+7LYwofDxwfrWYEPVUO6LjaRfUujbZfAs+LRpj82w9i2UaIBAPymMKnBdlqjjzhCTdfE7SwrcpqTeXf/b1LV61nTVKrFVZildZcsm0ztU7TwiY6ZZeRM2rJYWlZOVgs1uTF4Fyy0YiJsS7S9OVKbec9VWJiS7GyvEIXPh5oDs0lGw3I5/tU+6EL7BJ0KXeKLkq6m+g6oApMRU8QBPlo02ly5hfwcobQznym3DFSZ7cf+VzbqNGCBosaoZ35ktLtqamA7UQ2KXPKBGLMttqoGKbyq2MCeYaERb05SSthc6uZrTQfv8s+BNuWgOAIg7VEAmuxBIIzDIItGt+KsAViPhtE/ogQ8YgQ89vAfqD//Xz+UIwvjAtNfcuXOdfQF0ajmJ2oUeaEqTXsDx6nfdubwRTyHLKefU8LC9K0p972KPIVG/ciH1EDOdf9UDKgxSsVYoB9r2TvMiroNSuUvL9beti715kSdAe5j4UrUShpzu1M5NBZE8leP2/HPlP4J8AAmYBWic7Fp5IAAAAASUVORK5CYII="

/***/ }),
/* 168 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/u1285.26e9fbc.png";

/***/ }),
/* 169 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACMAAAAcCAYAAADr9QYhAAAD4UlEQVRIDcVXW2xMURRde2b6oIafSpGID6ESj8aMR0gkRUJDQgSTaqtCJBJfBH805ccP8QwhfHi1NRIhEuItfhBGEQQp4kM0fKBtPOfebZ2py9xrzKOKndw5Z++zH2vvs8+5dwT/icY0aSUUaxn+qfix7GZEWn3/A0u4QTcRyGHGHsZnplrYanCI+flXFD6k/SWALYw3LzmmgtD8GBxIFnZ1PjyqvQo/IxjPQzBPUWQpCpllD/orEh9K1MYQEYwjP5GP3xuHumLbqMgZDEtcDB+q6WAqg4yi4wFiUUJPBJIgf3K9KSOQzGQjlDWY0Ud0kE+wnhsbEUW+8Z5VkMwwEhr0VZYZc1T94TjqCGI1lQuy9J2zGgvYmhbM2EYdSKWj9Gz2+++S4utvt6msSYfairNEO/Dvouj0zqS/pKzM2Kj20zhucmv6/wsgJgbBvPy1MvXqsy00sqFyAkJnH9nY11Vwh0k84/yNxWx5q85hxgszJqV4+guYUClqaTwpo3FnNnHqniCQQ+/74ELLDPnstRt1UC/m52UGw+QfusCUX9ZA+yusY2ZpicFVFQf5bGiulhde5dKTGixqQ4nlR0FAUOldT8WzP6+6wLS3YjoRDkql7MgIoMUGFjdXyTVHZnrMsjCXWzKNsvH4gGJzCWb74mNyVjyOKy4wFFakKwr74ULAQuRGjbQZICMatKRAUM8eW8TAiYvQAZjjeOZerbx2gSGQsjRO9sd8WI4aXv6kUJMuFhubadM7jU12SxZ2GUUXGPL9Ullza7bFqmSVWQvvUb4OsY+npTpTb6Xy5ZVxNy7FauSckbvAMEuuuYmCvQ6QCVHt8cXCSepNcWt1jWOSn+LASsfa1WMM/MZZMCP5i7EFstzMw6e051cLp7oLiPHJDl9xt0oeJOYJ1plxJNL7SazhnxvefK9IB05zWm747iAe5c2xStmX7IuJ/qTwEZ3Mj6HzjoRgbB7146yQeb0PceR/OtLfFlZ8jdePq2fYkOae+EEEYrZxngvxj9XcJwRhMcYKVmR3KusEmO837y4GX5JKqTtkrHLnZbng52Xp9SuJo9obR5n9LO9it/CKD7wst+b7sfFaRD6m8+nTIHZ4gbC5DsQLUcxrfylL+ySdg9+t0a6D9tv5N6SU/VGXCYjxI/zAfsvtCTpO6eRlR0+MeDxb2hMyVQk1Yj51annWyzkWOrrekVvxiT1xhc8JdtuxWETee3XS8RJq0EZ+aM83Sub0EEzF7Sq5lMrI9FZHK0ZybTBL35f/Dgpo28a/Ge9o+6i5BY9Qz5dEF0lGR7Wvz0KUWZfxWNfdqpSdXfT1x2bfAJlGQYx0YpZ9AAAAAElFTkSuQmCC"

/***/ }),
/* 170 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyZpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKFdpbmRvd3MpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkJFMDVBREQ5OEQ1NzExRTc4Qzg5RDc2QUYwOTFCOEI0IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkJFMDVBRERBOEQ1NzExRTc4Qzg5RDc2QUYwOTFCOEI0Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6QkUwNUFERDc4RDU3MTFFNzhDODlENzZBRjA5MUI4QjQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6QkUwNUFERDg4RDU3MTFFNzhDODlENzZBRjA5MUI4QjQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz5x8QlwAAADkElEQVR42rxYzWsTURCffDQfK4U0MR89WCwWLUX02ApVsQdBvEhBPHkqolj/BD3Zowh6qIpHTyKKFz3U0g/qpUdFSq0gpqbSJqbxkGzSko/OrG/j7tu3zb5NzcCP7L6dmTf75vfmzcbTaDTAKLlcDjop8XjcdO936ec44griImIIEWPjecQKYgbxFrEm69gjuULnEfcRZx36X0LcQyw6XSGvQ8chxFPEvEQwwHTnmW3IiYGTgKKIOcRNWlEX6fUw2znma19pxSEF8R4xzD/Y2dmBsqpqv7VaTRvz+XwQDAYhrCjaLydnmK8xhOo2oEd8MNVqFf4UClogvNAzQqlU0gKK9PSA32+aYpj5vOEmZUTgCX5VsltbwmBEK2ijO8F8Swc0ZeQMvfl2Pg/8riQJ4GpQunghXbIhW45TU7IpO40YNQ4UtrehXq+blHp7e+Ewbls9mHK5DD/X10FV/1GEbMg2nkgYTUfZHJ+crtA4v/y7u7smhe7ubkimUto1BfEb61c4HIaj/f0WZ2QrSN24TMpMtaasWjdFIBCAUrGovX0e05LJZJrjiqJY9AU+zsmkbJBfIV4oCIIukUhE+6USoApeQODjhMwKxYw3ep2xLVa4Ikf6+rTrXxsbQh2Bj5jbSt0ymGMDAxqx9fS1I3YBmbyKtrSeJj0YInU6nbadSOAjL8OhVdrVRgJztQRisVgzTbTLWq0M+eDkq0xA1DZc0G/obOKJGo1Gm9yga/2eZAN5xOsLdt6STMremHqPUMjyhlQEadtX8LeVkG0wZOk+Xss2aEvGak0py2WzlmrdkqRer9aE+bu6jMMf9Von06DdpeOomVs8taPIG4/HeUtEumTDBdNgXaT04Upt53PjALUUiWRSRFBhmkhX0BeRzwW3PbXCOj1rg1apaDwSNmh4pgk4Q7LMN2iyXx1keBnxjg+KJrSZ1E6WmS+13Z46z0rAMyOnJKTBbMfsiqGbo4P29i0KzPfju+NImO4Ys1Wd2Mh+KC6GHz+AeiIF1ZOnoDY4BPVkChrKob+EVEvg3doE3+oK+L98Bm92E4oPpxdkJnD15UoTBeY2ke4zB/5p7RH1yMKcXb9q5ITUHOEXr9o+7WWETt07iEl23Zb427QvIK4hPrD7b4iXiJ6D7oecCE0+YggG2PUIe9bRgGZZoRT93bLGns12KqAniEssXfulknSmO8Gh2xJkn/xv275TsifAANzfXHQ3ifizAAAAAElFTkSuQmCC"

/***/ }),
/* 171 */
/***/ (function(module, exports, __webpack_require__) {

var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(229),
  /* template */
  __webpack_require__(345),
  /* styles */
  null,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 172 */
/***/ (function(module, exports, __webpack_require__) {

var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(230),
  /* template */
  __webpack_require__(344),
  /* styles */
  null,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 173 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(282)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(235),
  /* template */
  __webpack_require__(337),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-191026d6",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 174 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(285)
  __webpack_require__(286)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(242),
  /* template */
  __webpack_require__(342),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-412fc1af",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 175 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(279)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(243),
  /* template */
  __webpack_require__(334),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-03088b73",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 176 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(296)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(244),
  /* template */
  __webpack_require__(354),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-a77519ee",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 177 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(288)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(245),
  /* template */
  __webpack_require__(346),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-52cc4f8f",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 178 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(298)
  __webpack_require__(300)
  __webpack_require__(301)
  __webpack_require__(302)
  __webpack_require__(303)
  __webpack_require__(299)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(247),
  /* template */
  __webpack_require__(357),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-e45d7c10",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 179 */,
/* 180 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_moment__);

const _formatDate = value => {
    return __WEBPACK_IMPORTED_MODULE_0_moment___default()(value).format('YYYY-MM-DD');
};
/* harmony export (immutable) */ __webpack_exports__["_formatDate"] = _formatDate;

const _formatTime = value => {
    return __WEBPACK_IMPORTED_MODULE_0_moment___default()(value).format('HH:mm:ss');
};
/* harmony export (immutable) */ __webpack_exports__["_formatTime"] = _formatTime;

const _formatDateTime = value => {
    return __WEBPACK_IMPORTED_MODULE_0_moment___default()(value).format('YYYY-MM-DD HH:mm');
};
/* harmony export (immutable) */ __webpack_exports__["_formatDateTime"] = _formatDateTime;

const _dateFormat = (value, pattern) => {
    return __WEBPACK_IMPORTED_MODULE_0_moment___default()(value).format(pattern);
};
/* harmony export (immutable) */ __webpack_exports__["_dateFormat"] = _dateFormat;


/***/ }),
/* 181 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_vue__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_vue_router__ = __webpack_require__(359);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__components_Index__ = __webpack_require__(327);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__components_Index___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__components_Index__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_Login__ = __webpack_require__(328);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__components_Login___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__components_Login__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__components_Task__ = __webpack_require__(28);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__components_Task___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4__components_Task__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__components_Project__ = __webpack_require__(176);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__components_Project___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5__components_Project__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__components_Intergral__ = __webpack_require__(27);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__components_Intergral___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_6__components_Intergral__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__components_Organization__ = __webpack_require__(175);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__components_Organization___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_7__components_Organization__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__components_NavIndex__ = __webpack_require__(174);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__components_NavIndex___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_8__components_NavIndex__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__components_IntegralHistory__ = __webpack_require__(26);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__components_IntegralHistory___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_9__components_IntegralHistory__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__components_Stats__ = __webpack_require__(177);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__components_Stats___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_10__components_Stats__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__components_UserComments__ = __webpack_require__(331);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__components_UserComments___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_11__components_UserComments__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_12__components_Calculate__ = __webpack_require__(173);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_12__components_Calculate___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_12__components_Calculate__);














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
            name: 'taskList',
            path: 'task/:userId',
            component: __WEBPACK_IMPORTED_MODULE_4__components_Task___default.a
        }, {
            name: 'taskListFormComments',
            path: 'task/id/:taskId',
            component: __WEBPACK_IMPORTED_MODULE_4__components_Task___default.a
        }, {
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
        }, {
            path: 'comments',
            component: __WEBPACK_IMPORTED_MODULE_11__components_UserComments___default.a
        }, {
            path: 'calculate',
            component: __WEBPACK_IMPORTED_MODULE_12__components_Calculate___default.a
        }]
    }]
}));

/***/ }),
/* 182 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 183 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 184 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(295)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(231),
  /* template */
  __webpack_require__(353),
  /* styles */
  injectStyle,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
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
/* 211 */,
/* 212 */,
/* 213 */,
/* 214 */,
/* 215 */,
/* 216 */,
/* 217 */,
/* 218 */,
/* 219 */,
/* 220 */,
/* 221 */,
/* 222 */,
/* 223 */,
/* 224 */,
/* 225 */
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
    },
    handleInnerClick(evt) {
      if (this.disabled) {
        evt.stopPropagation();
      }
    }
  }
});

/***/ }),
/* 226 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_element_ui_src_mixins_emitter__ = __webpack_require__(24);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
  name: 'ElCheckbox',

  mixins: [__WEBPACK_IMPORTED_MODULE_0_element_ui_src_mixins_emitter__["a" /* default */]],

  componentName: 'ElCheckbox',

  data() {
    return {
      selfModel: false,
      focus: false
    };
  },

  computed: {
    model: {
      get() {
        return this.isGroup ? this.store : this.value !== undefined ? this.value : this.selfModel;
      },

      set(val) {
        if (this.isGroup) {
          let isLimitExceeded = false;
          this._checkboxGroup.min !== undefined && val.length < this._checkboxGroup.min && (isLimitExceeded = true);

          this._checkboxGroup.max !== undefined && val.length > this._checkboxGroup.max && (isLimitExceeded = true);

          isLimitExceeded === false && this.dispatch('ElCheckboxGroup', 'input', [val]);
        } else {
          this.$emit('input', val);
          this.selfModel = val;
        }
      }
    },

    isChecked() {
      if ({}.toString.call(this.model) === '[object Boolean]') {
        return this.model;
      } else if (Array.isArray(this.model)) {
        return this.model.indexOf(this.label) > -1;
      } else if (this.model !== null && this.model !== undefined) {
        return this.model === this.trueLabel;
      }
    },

    isGroup() {
      let parent = this.$parent;
      while (parent) {
        if (parent.$options.componentName !== 'ElCheckboxGroup') {
          parent = parent.$parent;
        } else {
          this._checkboxGroup = parent;
          return true;
        }
      }
      return false;
    },

    store() {
      return this._checkboxGroup ? this._checkboxGroup.value : this.value;
    }
  },

  props: {
    value: {},
    label: {},
    indeterminate: Boolean,
    disabled: Boolean,
    checked: Boolean,
    name: String,
    trueLabel: [String, Number],
    falseLabel: [String, Number]
  },

  methods: {
    addToStore() {
      if (Array.isArray(this.model) && this.model.indexOf(this.label) === -1) {
        this.model.push(this.label);
      } else {
        this.model = this.trueLabel || true;
      }
    },
    handleChange(ev) {
      this.$emit('change', ev);
      if (this.isGroup) {
        this.$nextTick(_ => {
          this.dispatch('ElCheckboxGroup', 'change', [this._checkboxGroup.value]);
        });
      }
    }
  },

  created() {
    this.checked && this.addToStore();
  }
});

/***/ }),
/* 227 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_element_ui_src_utils_popup__ = __webpack_require__(275);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_element_ui_src_mixins_emitter__ = __webpack_require__(24);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
  name: 'ElDialog',

  mixins: [__WEBPACK_IMPORTED_MODULE_0_element_ui_src_utils_popup__["a" /* default */], __WEBPACK_IMPORTED_MODULE_1_element_ui_src_mixins_emitter__["a" /* default */]],

  props: {
    title: {
      type: String,
      default: ''
    },

    modal: {
      type: Boolean,
      default: true
    },

    modalAppendToBody: {
      type: Boolean,
      default: true
    },

    lockScroll: {
      type: Boolean,
      default: true
    },

    closeOnClickModal: {
      type: Boolean,
      default: true
    },

    closeOnPressEscape: {
      type: Boolean,
      default: true
    },

    showClose: {
      type: Boolean,
      default: true
    },

    size: {
      type: String,
      default: 'small'
    },

    customClass: {
      type: String,
      default: ''
    },

    top: {
      type: String,
      default: '15%'
    },
    beforeClose: Function
  },

  watch: {
    visible(val) {
      this.$emit('update:visible', val);
      if (val) {
        this.$emit('open');
        this.$el.addEventListener('scroll', this.updatePopper);
        this.$nextTick(() => {
          this.$refs.dialog.scrollTop = 0;
        });
      } else {
        this.$el.removeEventListener('scroll', this.updatePopper);
        this.$emit('close');
      }
    }
  },

  computed: {
    sizeClass() {
      return `el-dialog--${this.size}`;
    },
    style() {
      return this.size === 'full' ? {} : { 'top': this.top };
    }
  },

  methods: {
    handleWrapperClick() {
      if (!this.closeOnClickModal) return;
      this.handleClose();
    },
    handleClose() {
      if (typeof this.beforeClose === 'function') {
        this.beforeClose(this.hide);
      } else {
        this.hide();
      }
    },
    hide(cancel) {
      if (cancel !== false) {
        this.$emit('update:visible', false);
        this.$emit('visible-change', false);
      }
    },
    updatePopper() {
      this.broadcast('ElSelectDropdown', 'updatePopper');
      this.broadcast('ElDropdownMenu', 'updatePopper');
    }
  },

  mounted() {
    if (this.visible) {
      this.rendered = true;
      this.open();
    }
  }
});

/***/ }),
/* 228 */
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

/* harmony default export */ __webpack_exports__["default"] = ({
  name: 'ElForm',

  componentName: 'ElForm',

  props: {
    model: Object,
    rules: Object,
    labelPosition: String,
    labelWidth: String,
    labelSuffix: {
      type: String,
      default: ''
    },
    inline: Boolean,
    showMessage: {
      type: Boolean,
      default: true
    }
  },
  watch: {
    rules() {
      this.validate();
    }
  },
  data() {
    return {
      fields: []
    };
  },
  created() {
    this.$on('el.form.addField', field => {
      if (field) {
        this.fields.push(field);
      }
    });
    /* istanbul ignore next */
    this.$on('el.form.removeField', field => {
      if (field.prop) {
        this.fields.splice(this.fields.indexOf(field), 1);
      }
    });
  },
  methods: {
    resetFields() {
      if (!this.model) {
        "production" !== 'production' && console.warn('[Element Warn][Form]model is required for resetFields to work.');
        return;
      }
      this.fields.forEach(field => {
        field.resetField();
      });
    },
    validate(callback) {
      if (!this.model) {
        console.warn('[Element Warn][Form]model is required for validate to work!');
        return;
      };
      let valid = true;
      let count = 0;
      // 如果需要验证的fields为空，调用验证时立刻返回callback
      if (this.fields.length === 0 && callback) {
        callback(true);
      }
      this.fields.forEach((field, index) => {
        field.validate('', errors => {
          if (errors) {
            valid = false;
          }
          if (typeof callback === 'function' && ++count === this.fields.length) {
            callback(valid);
          }
        });
      });
    },
    validateField(prop, cb) {
      var field = this.fields.filter(field => field.prop === prop)[0];
      if (!field) {
        throw new Error('must call validateField with valid prop string!');
      }

      field.validate('', cb);
    }
  }
});

/***/ }),
/* 229 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_element_ui_src_mixins_emitter__ = __webpack_require__(24);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__calcTextareaHeight__ = __webpack_require__(274);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_element_ui_src_utils_merge__ = __webpack_require__(44);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
  name: 'ElInput',

  componentName: 'ElInput',

  mixins: [__WEBPACK_IMPORTED_MODULE_0_element_ui_src_mixins_emitter__["a" /* default */]],

  data() {
    return {
      currentValue: this.value,
      textareaCalcStyle: {}
    };
  },

  props: {
    value: [String, Number],
    placeholder: String,
    size: String,
    resize: String,
    readonly: Boolean,
    autofocus: Boolean,
    icon: String,
    disabled: Boolean,
    type: {
      type: String,
      default: 'text'
    },
    name: String,
    autosize: {
      type: [Boolean, Object],
      default: false
    },
    rows: {
      type: Number,
      default: 2
    },
    autoComplete: {
      type: String,
      default: 'off'
    },
    form: String,
    maxlength: Number,
    minlength: Number,
    max: {},
    min: {},
    step: {},
    validateEvent: {
      type: Boolean,
      default: true
    },
    onIconClick: Function
  },

  computed: {
    validating() {
      return this.$parent.validateState === 'validating';
    },
    textareaStyle() {
      return __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_2_element_ui_src_utils_merge__["a" /* default */])({}, this.textareaCalcStyle, { resize: this.resize });
    }
  },

  watch: {
    'value'(val, oldValue) {
      this.setCurrentValue(val);
    }
  },

  methods: {
    handleBlur(event) {
      this.$emit('blur', event);
      if (this.validateEvent) {
        this.dispatch('ElFormItem', 'el.form.blur', [this.currentValue]);
      }
    },
    inputSelect() {
      this.$refs.input.select();
    },
    resizeTextarea() {
      if (this.$isServer) return;
      var { autosize, type } = this;
      if (!autosize || type !== 'textarea') return;
      const minRows = autosize.minRows;
      const maxRows = autosize.maxRows;

      this.textareaCalcStyle = __webpack_require__.i(__WEBPACK_IMPORTED_MODULE_1__calcTextareaHeight__["a" /* default */])(this.$refs.textarea, minRows, maxRows);
    },
    handleFocus(event) {
      this.$emit('focus', event);
    },
    handleInput(event) {
      const value = event.target.value;
      this.$emit('input', value);
      this.setCurrentValue(value);
      this.$emit('change', value);
    },
    handleIconClick(event) {
      if (this.onIconClick) {
        this.onIconClick(event);
      }
      this.$emit('click', event);
    },
    setCurrentValue(value) {
      if (value === this.currentValue) return;
      this.$nextTick(_ => {
        this.resizeTextarea();
      });
      this.currentValue = value;
      if (this.validateEvent) {
        this.dispatch('ElFormItem', 'el.form.change', [value]);
      }
    }
  },

  created() {
    this.$on('inputSelect', this.inputSelect);
  },

  mounted() {
    this.resizeTextarea();
  }
});

/***/ }),
/* 230 */
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
/* 231 */
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
/* 232 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(4);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
        this.$message({
          showClose: true,
          message: '添加部门成功',
          type: 'success'
        });
      });
    }
  }
});

/***/ }),
/* 233 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(4);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
        departmentId: '',
        email: ''
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
      this.addForm.email = '';
    },
    //部门ID
    setDeptId(deptId) {
      this.addForm.departmentId = deptId;
    },
    //添加用户
    handleAddUserClick() {
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.addForm.name) == '') {
        this.warnMsg("请填写用户名称");
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.addForm.account) == '') {
        this.warnMsg("请填写用户账号");
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.addForm.jobName) == '') {
        this.warnMsg("请填写用户职位");
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.addForm.phone) == '') {
        this.warnMsg("请填写用户手机号");
        return;
      }
      if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.addForm.userRole) == '') {
        this.warnMsg("请选择用户权限");
        return;
      }
      __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/user/add', this.addForm, res => {
        this.hide();
        this.$message({
          showClose: true,
          message: '用户添加成功',
          type: 'success'
        });
        this.$emit("handleUserDataRefresh");
      });
    },
    warnMsg(msg) {
      this.$message({
        showClose: true,
        message: msg,
        type: 'warning'
      });
    }
  }
});

/***/ }),
/* 234 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Helper__ = __webpack_require__(4);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Http__ = __webpack_require__(3);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
                this.warnMsg("请输入旧密码");
                return;
            }
            if (newPwd == '') {
                this.warnMsg("请输入新密码");
                return;
            }
            if (sureNewPwd == '') {
                this.warnMsg("请再次输入新密码");
                return;
            }
            if (newPwd != sureNewPwd) {
                this.warnMsg("两次输入的新密码不一致");
                return;
            }
            this.button.loading = true;
            this.button.btnName = '保存中';
            __WEBPACK_IMPORTED_MODULE_1__lib_Http__["a" /* default */].zsyPutHttp('/user/password', this.modifyForm, res => {
                this.$message({
                    showClose: true,
                    message: '修改成功',
                    type: 'success'
                });
                setTimeout(function () {
                    _this.$router.push("/");
                }, 1000);
            }, fail => {
                this.$message({
                    showClose: true,
                    message: fail.errMsg,
                    type: 'error'
                });
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
        },
        warnMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'warning'
            });
        }
    }
});

/***/ }),
/* 235 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1_moment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__lib_Helper__ = __webpack_require__(4);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
    name: 'Calculate',
    data() {
        return {
            calculate: [],
            queryForm: {
                startTime: '',
                endTime: '',
                datumIntegral: '',
                bonus: ''
            }
        };
    },
    methods: {
        calculateIntegral() {
            if (this.isDecimal(this.queryForm.datumIntegral) && this.isDecimal(this.queryForm.bonus)) {
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp("/stats/calculate", this.queryForm, res => {
                    this.calculate = res.data;
                });
            } else {
                this.$message({
                    showClose: true,
                    message: '基准积分与奖金为必填项，请检查并确认格式正确',
                    type: 'error'
                });
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
        getSummaries(param) {
            const { columns, data } = param;
            const sums = [];
            columns.forEach((column, index) => {
                if (index === 0) {
                    sums[index] = '总计';
                    return;
                }
                const values = data.map(item => Number(item[column.property]));
                if (!values.every(value => isNaN(value))) {
                    sums[index] = values.reduce((prev, curr) => {
                        const value = Number(curr);
                        if (!isNaN(value)) {
                            return prev + curr;
                        } else {
                            return prev;
                        }
                    }, 0).toFixed(2);
                    sums[index] += ' ';
                } else {
                    sums[index] = '';
                }
            });

            return sums;
        }
    }
});

/***/ }),
/* 236 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(4);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2_moment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_lodash__ = __webpack_require__(45);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_lodash___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_lodash__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
            facilityList: [{ label: '容易', value: 1 }, { label: '简单', value: 2 }, { label: '一般', value: 3 }, { label: '较难', value: 4 }, { label: '困难', value: 5 }],
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
            description: '',
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
            showAddStageTag: false,
            weekTime: {
                beginWeek: '',
                endWeek: ''
            },
            weekNumber: [],
            userWeekHour: []
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
        },
        sortWeekNumber() {
            return __WEBPACK_IMPORTED_MODULE_3_lodash___default.a.orderBy(this.weekNumber, 'weekNumber');
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
                description: stages[index].description,
                userWeeks: stages[index].userWeeks
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
            this.description = '';
        },
        saveAddMember() {
            var sumHours = 0;
            for (var i = 0; i < this.weekNumber.length; i++) {
                var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.weekNumber[i].hours);

                if (!ishours && ishours != 0) {
                    this.errorMsg('工作量填写错误');
                    return false;
                }
                if (this.weekNumber[i].hours > 99999.9 || this.weekNumber[i].hours < 0) {
                    this.errorMsg('工作量正确值应为0~99999.9');
                    return false;
                }
                sumHours += parseFloat(this.weekNumber[i].hours);
            }
            if (sumHours != this.step.taskHours) {
                this.errorMsg('周工作量与总工作量不符，请检查');
                return;
            }
            const valid = this.step.userId == '' || this.step.taskHours == '' || this.step.beginTime == '' || this.step.endTime == '';
            if (valid) {
                this.errorMsg('请将阶段填写完整');
                return;
            }
            var isnum = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.step.taskHours);
            if (!isnum) {
                this.errorMsg('工作量填写错误');
                return false;
            }
            if (this.step.taskHours > 99999.9 || this.step.taskHours < 0.1) {
                this.errorMsg('工作量正确值应为0.1~99999.9');
                return false;
            }

            this.showAddDetail = !this.showAddDetail;
            if (this.step.index === '') {
                let taskUser = {};
                taskUser.userId = this.step.userId;
                taskUser.userName = this.step.userName;
                taskUser.beginTime = __WEBPACK_IMPORTED_MODULE_2_moment___default()(this.step.beginTime).format('YYYY-MM-DD');
                taskUser.endTime = __WEBPACK_IMPORTED_MODULE_2_moment___default()(this.step.endTime).format('YYYY-MM-DD');
                taskUser.taskHours = this.step.taskHours;
                taskUser.description = this.description;
                taskUser.userWeeks = this.weekNumber;
                this.taskUsers.push(taskUser);
            } else {
                // 取消css
                this.taskUsers[this.step.index].cssClass = '';
                this.modifyTaskForm.taskUsers[this.step.index].description = this.description;
                this.taskUsers[this.step.index].userWeeks = this.weekNumber;
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
            this.description = '';
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
                this.warnMsg("请填写任务名称");
                return;
            }
            if (this.taskForm.projectId == '') {
                this.warnMsg("请选择项目");
                return;
            }
            if (this.taskForm.endTime == '') {
                this.warnMsg("请选择结束时间");
                return;
            }
            if (this.taskForm.stageId === '') {
                this.warnMsg("请选择项目阶段");
                return;
            }
            if (this.taskForm.priority === '') {
                this.warnMsg("请选择任务优先级");
                return;
            }
            if (this.taskForm.facility === '') {
                this.warnMsg("请选择任务难易度");
                return;
            }
            if (this.taskForm.tags.length == 0) {
                this.warnMsg("请选择至少一项标签");
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
                if (__WEBPACK_IMPORTED_MODULE_2_moment___default()(user.beginTime).year() != __WEBPACK_IMPORTED_MODULE_2_moment___default()(user.beginTime).year()) {
                    this.warnMsg("任务请勿跨年");
                    return;
                }
            });
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/task/create', param, resp => {
                this.$message({
                    showClose: true,
                    message: '任务创建成功',
                    type: 'success'
                });
                vm.taskForm.description = '';
                vm.taskForm.taskName = '';
                vm.taskForm.endTime = '';
                vm.taskForm.projectId = '';
                vm.taskForm.sta = '';
                vm.taskForm.priority = 1;
                vm.taskForm.tags = [];
                vm.taskUsers = [];
                // 刷新看板
                this.$root.eventBus.$emit("reloadBoard");
                // 刷新列表
                vm.$emit('handleFetchTaskList');
            });
            this.showCreateTask = false;
        },
        warnMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'warning'
            });
        },
        errorMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'error'
            });
        }

    },
    watch: {
        step: {
            deep: true,
            handler: function (val, oldVal) {
                this.weekNumber = [];
                let weekData = '';
                let param = this.weekNumber;
                if (this.step.userId != '' && this.step.taskHours != '' && this.step.beginTime != '' && this.step.endTime != '' && (__WEBPACK_IMPORTED_MODULE_2_moment___default()(this.step.beginTime).isBefore(this.step.endTime) || __WEBPACK_IMPORTED_MODULE_2_moment___default()(this.step.beginTime).isSame(this.step.endTime))) {
                    this.weekTime.beginWeek = __WEBPACK_IMPORTED_MODULE_2_moment___default()(this.step.beginTime).week();
                    this.weekTime.endWeek = __WEBPACK_IMPORTED_MODULE_2_moment___default()(this.step.endTime).week();
                    let beginYear = __WEBPACK_IMPORTED_MODULE_2_moment___default()(this.step.beginTime).year();
                    let endYear = __WEBPACK_IMPORTED_MODULE_2_moment___default()(this.step.endTime).year();
                    if (beginYear != endYear) {
                        for (let i = this.weekTime.beginWeek; i < __WEBPACK_IMPORTED_MODULE_2_moment___default()(this.step.beginTime).weeksInYear() + 1; i++) {
                            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/userWeek/' + this.step.userId + '/' + beginYear + '/' + i, {}, resp => {
                                weekData = {
                                    'weekNumber': i,
                                    'year': beginYear,
                                    'weekHours': resp.data,
                                    'range': __WEBPACK_IMPORTED_MODULE_2_moment___default()().year(beginYear).week(i).startOf('week').format('MM-DD') + '至' + __WEBPACK_IMPORTED_MODULE_2_moment___default()().year(beginYear).week(i).endOf('week').format('MM-DD')
                                };
                                param.push(weekData);
                            });
                        }
                        for (let i = 1; i < this.weekTime.endWeek + 1; i++) {
                            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/userWeek/' + this.step.userId + '/' + endYear + '/' + i, {}, resp => {
                                weekData = {
                                    'weekNumber': i,
                                    'year': endYear,
                                    'weekHours': resp.data,
                                    'range': __WEBPACK_IMPORTED_MODULE_2_moment___default()().year(endYear).week(i).startOf('week').format('MM-DD') + '至' + __WEBPACK_IMPORTED_MODULE_2_moment___default()().year(endYear).week(i).endOf('week').format('MM-DD')
                                };
                                param.push(weekData);
                            });
                        }
                    }
                    if (this.weekTime.beginWeek == this.weekTime.endWeek) {
                        console.log(this.weekTime.beginWeek);
                        __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/userWeek/' + this.step.userId + '/' + beginYear + '/' + this.weekTime.beginWeek, {}, resp => {
                            weekData = {
                                'weekNumber': this.weekTime.beginWeek,
                                'hours': this.step.taskHours,
                                'weekHours': resp.data,
                                'year': beginYear,
                                'range': __WEBPACK_IMPORTED_MODULE_2_moment___default()().year(beginYear).week(this.weekTime.beginWeek).startOf('week').format('MM-DD') + '至' + __WEBPACK_IMPORTED_MODULE_2_moment___default()().year(beginYear).week(this.weekTime.beginWeek).endOf('week').format('MM-DD')
                            };
                            param.push(weekData);
                        });
                    } else if (this.weekTime.endWeek - this.weekTime.beginWeek > 1) {
                        for (let i = this.weekTime.beginWeek; i < this.weekTime.endWeek + 1; i++) {
                            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/userWeek/' + this.step.userId + '/' + beginYear + '/' + i, {}, resp => {
                                param.push({
                                    'weekNumber': i,
                                    'hours': '',
                                    'weekHours': resp.data,
                                    'year': beginYear,
                                    'range': __WEBPACK_IMPORTED_MODULE_2_moment___default()().year(beginYear).week(i).startOf('week').format('MM-DD') + '至' + __WEBPACK_IMPORTED_MODULE_2_moment___default()().year(beginYear).week(i).endOf('week').format('MM-DD')
                                });
                            });
                        }
                    } else if (this.weekTime.endWeek - this.weekTime.beginWeek == 1) {
                        __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/userWeek/' + this.step.userId + '/' + beginYear + '/' + this.weekTime.beginWeek, {}, resp => {
                            param.push({
                                'weekNumber': this.weekTime.beginWeek,
                                'hours': '',
                                'weekHours': resp.data,
                                'year': beginYear,
                                'range': __WEBPACK_IMPORTED_MODULE_2_moment___default()().year(beginYear).week(this.weekTime.beginWeek).startOf('week').format('MM-DD') + '至' + __WEBPACK_IMPORTED_MODULE_2_moment___default()().year(beginYear).week(this.weekTime.beginWeek).endOf('week').format('MM-DD')
                            });
                        });
                        __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/userWeek/' + this.step.userId + '/' + beginYear + '/' + this.weekTime.endWeek, {}, resp => {
                            param.push({
                                'weekNumber': this.weekTime.endWeek,
                                'hours': '',
                                'weekHours': resp.data,
                                'year': beginYear,
                                'range': __WEBPACK_IMPORTED_MODULE_2_moment___default()().year(beginYear).week(this.weekTime.endWeek).startOf('week').format('MM-DD') + '至' + __WEBPACK_IMPORTED_MODULE_2_moment___default()().year(beginYear).week(this.weekTime.endWeek).endOf('week').format('MM-DD')
                            });
                        });
                    }
                    this.weekNumber = param;
                }
            }
        }
    }
});

/***/ }),
/* 237 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__Task__ = __webpack_require__(28);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__Task___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__Task__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__Project__ = __webpack_require__(176);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__Project___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__Project__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__Intergral__ = __webpack_require__(27);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__Intergral___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__Intergral__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__Organization__ = __webpack_require__(175);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__Organization___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__Organization__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__NavIndex__ = __webpack_require__(174);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__NavIndex___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4__NavIndex__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__AlterPassword__ = __webpack_require__(325);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__AlterPassword___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5__AlterPassword__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__lib_Helper__ = __webpack_require__(4);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__IntegralHistory__ = __webpack_require__(26);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_8__IntegralHistory___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_8__IntegralHistory__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__Stats__ = __webpack_require__(177);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_9__Stats___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_9__Stats__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__UploadAvatar_vue__ = __webpack_require__(330);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_10__UploadAvatar_vue___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_10__UploadAvatar_vue__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__Calculate_vue__ = __webpack_require__(173);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_11__Calculate_vue___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_11__Calculate_vue__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
            showAlterPwd: false,
            avatarUrl: '',
            deptVisible: false,
            deptOptions: [],
            dept: {
                children: 'children',
                label: 'label',
                id: 'id'
            },
            departmentId: '',
            organization: '',
            addDeptVisible: false,
            emailVisible: false,
            emailCode: ''
        };
    },
    created() {
        this.fetchMyProfile();
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
                label: '计算',
                name: 'calculate'
            });
            this.tabs.push({
                label: '评价',
                name: 'comments'
            });
            this.tabs.push({
                label: '组织',
                name: 'organization'
            });
        }
        if (__WEBPACK_IMPORTED_MODULE_6__lib_Helper__["a" /* default */].decodeToken().departmentId == 0) {
            __WEBPACK_IMPORTED_MODULE_7__lib_Http__["a" /* default */].zsyGetHttp(`/dept/all`, null, res => {
                this.deptOptions = res.data;
            });
            this.emailVisible = true;
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
        },
        alterAvatar() {
            // 修改头像
            this.showPerOpt = false;
            this.$refs.uploadAvatar.show();
        },
        fetchMyProfile() {
            // 获取我的个人信息
            __WEBPACK_IMPORTED_MODULE_7__lib_Http__["a" /* default */].zsyGetHttp('/user/myProfile', {}, res => {
                if (res.data.avatarUrl && res.data.avatarUrl != '') {
                    this.avatarUrl = res.data.avatarUrl;
                }
            });
        },
        deptChoose(data) {
            this.departmentId = data.id;
        },
        addDept() {
            if (this.organization.trim() != '' && this.organization != null && this.organization.length < 20) {
                __WEBPACK_IMPORTED_MODULE_7__lib_Http__["a" /* default */].zsyPostHttp('/dept/addOrganization?name=' + this.organization, null, res => {
                    this.$message({
                        showClose: true,
                        message: '创建组织成功',
                        type: 'success'
                    });
                    __WEBPACK_IMPORTED_MODULE_7__lib_Http__["a" /* default */].zsyGetHttp(`/dept/all`, null, res => {
                        this.deptOptions = res.data;
                    });
                    this.addDeptVisible = false;
                });
            } else {
                this.$message({
                    showClose: true,
                    message: '组织名称不能为空且长度小于20',
                    type: 'warning'
                });
                return;
            }
        },
        deptChange() {
            if (this.departmentId == '') {
                this.$message({
                    showClose: true,
                    message: '请选择组织',
                    type: 'warning'
                });
                return;
            }
            __WEBPACK_IMPORTED_MODULE_7__lib_Http__["a" /* default */].zsyPutHttp('/user/modifyDept/' + this.departmentId, null, res => {
                this.$message({
                    showClose: true,
                    message: '组织修改成功',
                    type: 'success'
                });
                window.localStorage.setItem("token", res.data);
                this.$router.go(0);
            });

            this.deptVisible = false;
        },
        sendEmail() {
            __WEBPACK_IMPORTED_MODULE_7__lib_Http__["a" /* default */].zsyGetHttp('/user/sendEmail' + this.emailCode, {}, res => {
                this.$message({
                    showClose: true,
                    message: "已重新发送",
                    type: 'success'
                });
            });
        },
        vaidateEmail() {
            if (this.emailCode != null && this.emailCode != '') {
                __WEBPACK_IMPORTED_MODULE_7__lib_Http__["a" /* default */].zsyGetHttp('/user/validateEmail/' + this.emailCode, {}, res => {
                    if (res.errCode != '00') {
                        this.$message({
                            showClose: true,
                            message: "邮箱验证失败，请检查后重试",
                            type: 'error'
                        });
                    } else {
                        this.$message({
                            showClose: true,
                            message: "邮箱验证成功",
                            type: 'success'
                        });
                        this.emailVisible = false;
                        this.deptVisible = true;
                    }
                });
            }
        }
    },
    components: {
        Task: __WEBPACK_IMPORTED_MODULE_0__Task___default.a,
        Project: __WEBPACK_IMPORTED_MODULE_1__Project___default.a,
        Intergral: __WEBPACK_IMPORTED_MODULE_2__Intergral___default.a,
        Organization: __WEBPACK_IMPORTED_MODULE_3__Organization___default.a,
        Calculate: __WEBPACK_IMPORTED_MODULE_11__Calculate_vue___default.a,
        NavIndex: __WEBPACK_IMPORTED_MODULE_4__NavIndex___default.a,
        AlterPassword: __WEBPACK_IMPORTED_MODULE_5__AlterPassword___default.a,
        IntegralHistory: __WEBPACK_IMPORTED_MODULE_8__IntegralHistory___default.a,
        Stats: __WEBPACK_IMPORTED_MODULE_9__Stats___default.a,
        UploadAvatar: __WEBPACK_IMPORTED_MODULE_10__UploadAvatar_vue___default.a
    }
});

/***/ }),
/* 238 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(4);
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





/* harmony default export */ __webpack_exports__["default"] = ({
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
    //选中任务tab
    this.$root.eventBus.$emit("handleTabSelected", "intergral");
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
          if (list[i].origin == 2) {
            list[i].origin = "手动录入";
            list[i].createTime = this.localeTimeString(list[i].createTime);
          } else if (list[i].origin == 3) {
            list[i].origin = "转移求助";
            list[i].createTime = this.localeTimeString(list[i].createTime);
          } else if (list[i].origin == 4) {
            list[i].origin = "Bug处理结果";
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
            this.$message({
              showClose: true,
              message: '积分格式错误',
              type: 'error'
            });
            return false;
          }
          __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/integral/add', this.integralForm, res => {
            this.$message({
              showClose: true,
              message: '积分添加成功',
              type: 'success'
            });
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
        if (curMonth >= 0 && curMonth <= 2) {
          startMonth = 0;
        } else if (curMonth >= 3 && curMonth <= 5) {
          startMonth = 3;
        } else if (curMonth >= 6 && curMonth <= 8) {
          startMonth = 6;
        } else if (curMonth >= 9 && curMonth <= 11) {
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
        time = __WEBPACK_IMPORTED_MODULE_2_moment___default()(time).format('YYYY-MM-DD HH:mm:ss');
        return time;
      } else {
        return "";
      }
    }
  }
});

/***/ }),
/* 239 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1_moment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__lib_Helper__ = __webpack_require__(4);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__IntegralHistory__ = __webpack_require__(26);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__IntegralHistory___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__IntegralHistory__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
            yearList: {
                curYear: '',
                prevYear: '',
                nextYear: ''
            },
            quarterShow: {
                prev: '',
                next: ''
            },
            yearShow: {
                prev: '',
                next: ''
            },
            quarterTime: '',
            yearTime: '',
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
            if (date == "quarter") {
                //本季度
                this.getDateString("quarter");
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(__WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].API_URI.INTEGRAL, this.queryForm, res => {
                    this.quarter = res.data;
                    this.countQuarterHistory(this.queryForm);
                    if (this.quarter.length < 1) {
                        console.log(11);
                    }
                });
            } else if (date == "year") {
                //今年
                this.getDateString("year");
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(__WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].API_URI.INTEGRAL, this.yearForm, res => {
                    this.year = res.data;
                    this.countYearHistory(this.yearForm);
                });
            }
        },
        //获取本年,季度,月的开始结束日期
        getDateString(date) {
            let now = new Date();
            let curMonth = now.getMonth();
            let curYear = now.getFullYear();
            let startMonth = 0;
            if (date == "month") {
                //本月的开始结束时间
                this.queryForm.startTime = this.localeTimeString(new Date(curYear, curMonth, 1));
                this.queryForm.endTime = this.localeTimeString(new Date(curYear, curMonth + 1, 1)); //下月第一天0点
            } else if (date == "quarter") {
                //本季度的开始结束时间
                if (curMonth >= 0 && curMonth <= 2) {
                    startMonth = 0;
                } else if (curMonth >= 3 && curMonth <= 5) {
                    startMonth = 3;
                } else if (curMonth >= 6 && curMonth <= 8) {
                    startMonth = 6;
                } else if (curMonth >= 9 && curMonth <= 11) {
                    startMonth = 9;
                }
                this.queryForm.startTime = this.localeTimeString(new Date(curYear, startMonth, 1));
                this.queryForm.endTime = this.localeTimeString(new Date(curYear, startMonth + 3, 1)); //下季度第一天0点
                this.quarterTime = this.localeStringTime(new Date(curYear, startMonth, 1)) + '—' + this.localeStringTime(new Date(curYear, startMonth + 3, 1) - 1);
            } else if (date == "year") {
                //本年的开始结束时间
                this.yearForm.startTime = this.localeTimeString(new Date(now.getFullYear(), 0, 1));
                this.yearForm.endTime = this.localeTimeString(new Date(now.getFullYear() + 1, 0, 1));
                this.yearTime = this.localeStringTime(new Date(curYear, 0, 1)) + '—' + this.localeStringTime(new Date(curYear + 1, 0, 1) - 1);
            }
        },
        //时间字符串格式化
        localeTimeString(time) {
            if (time != null && time != "") {
                time = __WEBPACK_IMPORTED_MODULE_1_moment___default()(time).format('YYYY-MM-DD HH:mm:ss');
                return time;
            } else {
                return "";
            }
        },
        //时间字符串格式化
        localeDayString(time) {
            if (time != null && time != "") {
                time = __WEBPACK_IMPORTED_MODULE_1_moment___default()(time).format('YYYY-MM-DD');
                return time;
            } else {
                return "";
            }
        },
        //时间字符串格式化
        localeStringTime(time) {
            if (time != null && time != "") {
                time = __WEBPACK_IMPORTED_MODULE_1_moment___default()(time).format('YYYY.MM.DD');
                return time;
            } else {
                return "";
            }
        },
        countYearHistory(time) {
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/integral/count', time, res => {
                if (res.data.prev == 0) {
                    this.yearShow.prev = 'none';
                } else {
                    this.yearShow.prev = '';
                }
                if (res.data.next == 0) {
                    this.yearShow.next = 'none';
                } else {
                    this.yearShow.next = '';
                }
            });
        },
        countQuarterHistory(time) {
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/integral/count', time, res => {
                if (res.data.prev == 0) {
                    this.quarterShow.prev = 'none';
                } else {
                    this.quarterShow.prev = '';
                }
                if (res.data.next == 0) {
                    this.quarterShow.next = 'none';
                } else {
                    this.quarterShow.next = '';
                }
            });
        },
        prevQuarterHistory() {
            var currQuarter = new Date(this.quarterTime.split('—')[0]);
            this.queryForm.endTime = new Date(currQuarter.getFullYear(), currQuarter.getMonth(), 1);
            this.queryForm.startTime = new Date(currQuarter.getFullYear(), currQuarter.getMonth() - 3, 1);
            this.quarterTime = this.localeStringTime(this.queryForm.startTime) + '—' + this.localeStringTime(new Date(this.queryForm.endTime) - 1);
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(__WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].API_URI.INTEGRAL, this.queryForm, res => {
                this.quarter = res.data;
                this.countQuarterHistory(this.queryForm);
            });
        },
        nextQuarterHistory() {
            var currQuarter = new Date(this.quarterTime.split('—')[1]);
            this.queryForm.startTime = new Date(currQuarter.getFullYear(), currQuarter.getMonth() + 1, 1);
            this.queryForm.endTime = new Date(currQuarter.getFullYear(), currQuarter.getMonth() + 4, 1);
            this.quarterTime = this.localeStringTime(this.queryForm.startTime) + '—' + this.localeStringTime(new Date(this.queryForm.endTime) - 1);
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(__WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].API_URI.INTEGRAL, this.queryForm, res => {
                this.quarter = res.data;
                this.countQuarterHistory(this.queryForm);
            });
        },
        prevYearHistory() {
            var currQuarter = new Date(this.yearTime.split('—')[0]);
            this.queryForm.endTime = new Date(currQuarter.getFullYear(), 0, 1);
            this.queryForm.startTime = new Date(currQuarter.getFullYear() - 1, 0, 1);
            this.yearTime = this.localeStringTime(this.queryForm.startTime) + '—' + this.localeStringTime(new Date(this.queryForm.endTime) - 1);
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(__WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].API_URI.INTEGRAL, this.queryForm, res => {
                this.year = res.data;
                this.countYearHistory(this.queryForm);
            });
        },
        nextYearHistory() {
            var currQuarter = new Date(this.yearTime.split('—')[1]);
            this.queryForm.startTime = new Date(currQuarter.getFullYear() + 1, 0, 1);
            this.queryForm.endTime = new Date(currQuarter.getFullYear() + 2, 0, 1);
            this.yearTime = this.localeStringTime(this.queryForm.startTime) + '—' + this.localeStringTime(new Date(this.queryForm.endTime) - 1);

            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(__WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].API_URI.INTEGRAL, this.queryForm, res => {
                this.year = res.data;
                this.countYearHistory(this.queryForm);
            });
        },
        createQuarterHistory(createElement) {
            return createElement('div', {
                'class': 'renderTableHead'
            }, [createElement('el-button', {
                attrs: {
                    type: 'text',
                    style: 'color:#ffffff;float: left;font-size:10px;display:' + this.quarterShow.prev
                },
                on: {
                    click: this.prevQuarterHistory
                }
            }, ['上一季度']), createElement('el-button', {
                attrs: {
                    type: 'text',
                    style: 'color:#ffffff;cursor:default'
                }
            }, ['季度排名(' + this.quarterTime + ')']), createElement('el-button', {
                attrs: {
                    type: 'text',
                    style: 'color:#ffffff;float: right;font-size:10px;display:' + this.quarterShow.next
                },
                on: {
                    click: this.nextQuarterHistory
                }
            }, ['下一季度'])]);
        },
        createYearHistory(createElement) {
            return createElement('div', {
                'class': 'renderTableHead'
            }, [createElement('el-button', {
                attrs: {
                    type: 'text',
                    style: 'color:#ffffff;float: left;font-size:10px;display:' + this.yearShow.prev
                },
                on: {
                    click: this.prevYearHistory
                }
            }, ['上一年度']), createElement('el-button', {
                attrs: {
                    type: 'text',
                    style: 'color:#ffffff;cursor:default'
                }
            }, ['年度排名(' + this.yearTime + ')']), createElement('el-button', {
                attrs: {
                    type: 'text',
                    style: 'color:#ffffff;float: right;font-size:10px;display:' + this.yearShow.next
                },
                on: {
                    click: this.nextYearHistory
                }
            }, ['下一年度'])]);
        }

    }
});

/***/ }),
/* 240 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Helper__ = __webpack_require__(4);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__node_modules_element_ui_packages_button_src_button__ = __webpack_require__(13);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__node_modules_element_ui_packages_button_src_button___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__node_modules_element_ui_packages_button_src_button__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_input_src_input__ = __webpack_require__(171);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_input_src_input___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_input_src_input__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
    components: {
        ElInput: __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_input_src_input___default.a,
        ElButton: __WEBPACK_IMPORTED_MODULE_2__node_modules_element_ui_packages_button_src_button___default.a },
    data() {
        var validateEmpty = (rule, value, callback) => {
            if (value.trim() == '') {
                callback(new Error());
            } else {
                callback();
            }
        };
        return {
            registerShow: false,
            loginShow: true,
            loginForm: {
                account: '',
                password: ''
            },
            button: {
                loading: false
            },
            userForm: {
                account: '',
                name: '',
                password: '',
                jobName: '',
                phone: '',
                email: '',
                userRole: Math.floor(Math.random() * 10)
            },
            loginForm: {
                account: '',
                password: ''
            },
            button: {
                loading: false
            },
            rules: {
                account: [{ required: true, message: '账号不能为空且长度在5~16之间(不支持中文)', trigger: 'change', min: 5, max: 16, validator: validateEmpty }],
                name: [{ required: true, message: '真实姓名不能为空且长度小于20', trigger: 'change', min: 1, max: 20, validator: validateEmpty }],
                password: [{ required: true, message: '密码长度在6~16之间', trigger: 'change', min: 6, max: 16, validator: validateEmpty }],
                jobName: [{ required: true, message: '职位不能超过10字', trigger: 'change', min: 1, max: 10, validator: validateEmpty }],
                phone: [{ required: true, message: '手机格式错误', trigger: 'change', pattern: /0?(13|14|15|18)[0-9]{9}/ }],
                email: [{ required: true, message: '邮箱格式错误', trigger: 'change', pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/ }]
            }
        };
    },
    methods: {
        login() {
            let _this = this;
            if (__WEBPACK_IMPORTED_MODULE_0__lib_Helper__["a" /* default */].trim(_this.loginForm.account) == '') {
                this.$message({
                    showClose: true,
                    message: '请输入用户名',
                    type: 'warning'
                });
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_0__lib_Helper__["a" /* default */].trim(_this.loginForm.password) == '') {
                this.$message({
                    showClose: true,
                    message: '请输入密码',
                    type: 'warning'
                });
                return;
            }
            _this.button.loading = true;
            __WEBPACK_IMPORTED_MODULE_1__lib_Http__["a" /* default */].zsyPostHttp(__WEBPACK_IMPORTED_MODULE_1__lib_Http__["a" /* default */].API_URI.LOGIN, _this.loginForm, res => {
                if (res.errCode != '00') {
                    _this.button.loading = false;
                    this.$message({
                        showClose: true,
                        message: res.errMsg,
                        type: 'error'
                    });
                } else {
                    _this.button.btnName = '登录成功,跳转中...';
                    window.localStorage.setItem("token", res.data);
                    _this.$router.push('/index/navIndex');
                }
            }, res => {
                _this.button.loading = false;
                this.$message({
                    showClose: true,
                    message: res.errMsg,
                    type: 'error'
                });
            }, e => {
                _this.button.loading = false;
            });
        },
        register(userForm) {
            let _this = this;
            _this.button.loading = true;
            this.$refs[userForm].validate(valid => {
                if (valid) {
                    __WEBPACK_IMPORTED_MODULE_1__lib_Http__["a" /* default */].zsyPostHttp('/user/register', _this.userForm, res => {
                        _this.loginForm.account = _this.userForm.account;
                        _this.loginForm.password = _this.userForm.password;
                        __WEBPACK_IMPORTED_MODULE_1__lib_Http__["a" /* default */].zsyPostHttp(__WEBPACK_IMPORTED_MODULE_1__lib_Http__["a" /* default */].API_URI.LOGIN, _this.loginForm, res => {
                            if (res.errCode != '00') {
                                _this.button.loading = false;
                                this.$message({
                                    showClose: true,
                                    message: res.errMsg,
                                    type: 'error'
                                });
                            } else {
                                _this.button.btnName = '登录成功,跳转中...';
                                window.localStorage.setItem("token", res.data);
                                _this.$router.push('/index/navIndex');
                            }
                        });
                    }, res => {
                        _this.button.loading = false;
                        this.$message({
                            showClose: true,
                            message: res.errMsg,
                            type: 'error'
                        });
                    }, e => {
                        _this.button.loading = false;
                    });
                } else {
                    _this.button.loading = false;
                    return;
                }
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
/* 241 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(4);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
                departmentId: '',
                status: '',
                email: ''
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
            statusOptions: [{
                value: 0,
                label: '正常使用'
            }, {
                value: 1,
                label: '冻结使用'
            }],
            deptOptions: [],
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
                this.modifyForm.status = res.data.status;
                this.modifyForm.email = res.data.email;
                this.modifyForm.departmentId = res.data.departmentId;
                this.showAddPop = true;
            });
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/dept/tree`, null, res => {
                this.deptOptions = res.data.children;
                this.deptOptions.unshift({ id: res.data.id, label: res.data.label });
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
            this.modifyForm.email = '';
        },
        //部门ID
        setUserId(userId) {
            this.modifyForm.userId = userId;
        },
        //修改用户
        handleModifyUserClick() {
            if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.modifyForm.name) == '') {
                this.warnMsg("请填写用户名称");
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.modifyForm.account) == '') {
                this.warnMsg("请填写用户账号");
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.modifyForm.jobName) == '') {
                this.warnMsg("请填写用户职位");
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.modifyForm.phone) == '') {
                this.warnMsg("请填写用户手机号");
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.modifyForm.userRole) == '') {
                this.warnMsg("请选择用户权限");
                return;
            }
            if (__WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].trim(this.modifyForm.status) == '') {
                this.warnMsg("请选择用户状态");
                return;
            }
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp(`/user/${this.modifyForm.userId}`, this.modifyForm, res => {
                this.hide();
                this.$message({
                    showClose: true,
                    message: '用户修改成功',
                    type: 'success'
                });
                this.$emit("handleUserDataRefresh");
            });
        },
        warnMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'warning'
            });
        }
    }
});

/***/ }),
/* 242 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__TaskItem__ = __webpack_require__(178);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__TaskItem___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__TaskItem__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__Intergral_vue__ = __webpack_require__(27);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__Intergral_vue___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__Intergral_vue__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__lib_Helper__ = __webpack_require__(4);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_moment__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
            activeHelpName: 'wait',
            activeLeaveName: 'wait',
            auditTabsActiveName: 'wait',
            auditHelpTabsActiveName: 'wait',
            editHelpDetailVisible: false,
            editHelpVisible: false,
            editLeaveVisible: false,
            leaveDetailVisible: false,
            helpDetailVisible: false,
            createTaskVisible: false,
            editLeaveDetailVisible: false,
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
            waitPage: {
                pageNum: 1,
                pageSize: 5,
                total: 0
            },
            leaveWaitPage: {
                pageNum: 1,
                pageSize: 5,
                total: 0
            },
            reviewPage: {
                pageNum: 1,
                pageSize: 5,
                total: 0
            },
            leavePassPage: {
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
                beginTime: '',
                priority: 1,
                tags: [],
                taskType: 1,
                taskHours: '',
                stageId: ''
            },
            helpForm: {
                id: '',
                description: '',
                time: '',
                userId: '',
                integral: '',
                name: '',
                username: ''
            },
            leaveForm: {
                id: '',
                description: '',
                beginTime: '',
                endTime: '',
                type: '',
                hours: '',
                typeName: ''
            },
            leaveType: [{ id: 1, name: '事假' }, { id: 2, name: '病假' }, { id: 3, name: '婚假' }, { id: 4, name: '产假' }, { id: 5, name: '丧假' }, { id: 6, name: '产检' }, { id: 7, name: '年假' }, { id: 8, name: '调休' }],
            userWeeks: [],
            weekTime: {
                beginWeek: '',
                endWeek: ''
            },
            leaveTimeRange: '',
            helpDetail: {
                id: '',
                description: '',
                time: '',
                userId: '',
                integral: '',
                name: '',
                username: ''
            },
            rules: {
                projectId: [{ required: true, message: '项目不能为空', trigger: 'change' }],
                endTime: [{ type: 'date', required: true, message: '截止时间不能为空', trigger: 'change' }],
                beginTime: [{ type: 'date', required: true, message: '截止时间不能为空', trigger: 'change' }],
                taskHours: [{ required: true, validator: validateEmpty, message: '工作量不能为空', trigger: 'blur' }],
                taskName: [{ required: true, validator: validateEmpty, message: '任务名称不能为空', trigger: 'blur' }, { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' }],
                description: [{ required: true, validator: validateEmpty, message: '描述不能为空', trigger: 'blur' }],
                stageId: [{ required: true, message: '阶段不能为空', trigger: 'change' }],
                tags: [{ type: 'array', required: true, message: '请至少选择一个标签', trigger: 'change' }]
            },
            helpRules: {
                integral: [{ required: true, message: '积分不能为空', trigger: 'blur' }],
                description: [{ required: true, message: '详情不能为空且不超过100字', trigger: 'blur', min: 1, max: 100 }],
                userId: [{ required: true, message: '求助人不能为空', trigger: 'change' }],
                time: [{ type: 'date', required: true, message: '转移时间不能为空', trigger: 'change' }]
            },
            leaveRules: {
                description: [{ required: true, message: '请假原因不能为空并保证在6字以上', trigger: 'change', min: 6 }]
            },
            task: {
                doing: [],
                finished: [],
                waitAssess: [],
                commented: [],
                waitAudit: [],
                auditSuccess: [],
                applyFail: []
            },
            review: {
                wait: [],
                review: []
            },
            leaveList: {
                wait: [],
                pass: []
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
        },
        waitPageLayout() {
            if (this.waitPage.total > 5) {
                return 'total, prev, pager, next';
            }
            return 'total, pager';
        },
        leaveWaitPageLayout() {
            if (this.leaveWaitPage.total > 5) {
                return 'total, prev, pager, next';
            }
            return 'total, pager';
        },
        reviewPageLayout() {
            if (this.reviewPage.total > 5) {
                return 'total, prev, pager, next';
            }
            return 'total, pager';
        },
        leavePassPageLayout() {
            if (this.leavePassPage.total > 5) {
                return 'total, prev, pager, next';
            }
            return 'total, pager';
        }
    },
    filters: {
        formatDate: function (value) {
            if (!value) return '';
            return __WEBPACK_IMPORTED_MODULE_4_moment___default()(value).format('YYYY年MM月DD日');
        },
        formatTime: function (value) {
            if (!value) return '';
            return __WEBPACK_IMPORTED_MODULE_4_moment___default()(value).format('YYYY年MM月DD日 HH:00:00');
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
            this.fetchUserLeaveList();
            this.fetchUserLeavePassList();
            //this.fetchApplyFailTask();
            if (this.userRole === 0) {
                // 所有审核通过的数据
                this.fetchTaskAuditSuccess();
                //待审核的积分转移，审核通过的积分转移
                this.fetchHelpWaitAdmin();
                this.fetchHelpReviewAdmin();
            } else {
                this.fetchMyHelpWaitList();
                this.fetchMyReviewSuccess();
            }
        },
        saveTaskInfo(formName) {
            let vm = this;
            this.taskForm.endTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.taskForm.endTime).toDate();
            this.taskForm.beiginTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.taskForm.beiginTime).toDate();
            this.$refs[formName].validate(valid => {
                if (valid) {
                    let userId = __WEBPACK_IMPORTED_MODULE_3__lib_Helper__["a" /* default */].decodeToken().userId;
                    var param = this.taskForm;
                    param.taskName = param.taskName.trim();
                    param.beiginTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(param.beiginTime).format('YYYY-MM-DD HH:00:00');
                    param.endTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(param.endTime).format('YYYY-MM-DD HH:00:00');
                    if (param.taskHours.length != parseFloat(param.taskHours).toString().length || parseFloat(param.taskHours) == "NaN") {
                        this.$message({ showClose: true, message: '工作量只能为数字或者小数', type: 'error' });
                        return false;
                    }
                    if (param.taskHours.trim() > 8 || param.taskHours.trim() < 0.1) {
                        this.$message({ showClose: true, message: '工作量正确值应为0.1~8', type: 'error' });
                        return false;
                    }
                    if (__WEBPACK_IMPORTED_MODULE_4_moment___default()(param.endTime).millisecond() < __WEBPACK_IMPORTED_MODULE_4_moment___default()(param.beginTime).millisecond() || __WEBPACK_IMPORTED_MODULE_4_moment___default()(param.endTime).week() != __WEBPACK_IMPORTED_MODULE_4_moment___default()(param.beginTime).week()) {
                        this.$message({ showClose: true, message: '请检查日期，个人任务请勿跨周进行', type: 'warning' });
                        return false;
                    }
                    var taskUsers = [{
                        userId: userId,
                        taskHours: param.taskHours.trim(),
                        beginTime: __WEBPACK_IMPORTED_MODULE_4_moment___default()().format('YYYY-MM-DD HH:mm:ss'),
                        endTime: __WEBPACK_IMPORTED_MODULE_4_moment___default()(param.endTime).format('YYYY-MM-DD 23:59:59'),
                        description: param.description.trim()
                    }];
                    param['taskUsers'] = taskUsers;
                    __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyPostHttp('/task/create', param, resp => {
                        this.$message({ showClose: true, message: '任务创建成功', type: 'success' });
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
                    endTime = el.taskUsers[0].endTime;
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
                items.push({ label: ' ', score: data.week, time: this.getDateString('week') });
                items.push({ label: '', score: data.month, time: this.getDateString('month') });
                items.push({ label: '', score: data.year, time: this.getDateString('year') });
                items.push({ label: '', score: data.quarterRank, time: this.getDateString('quarter') });
                items.push({ label: '', score: data.yearRank, time: this.getDateString('year') });
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
        //获取个人请假信息
        fetchUserLeaveList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/userLeave/1/' + this.leaveWaitPage.pageNum, {}, resp => {
                vm.leaveList.wait = resp.data.list;
                vm.leaveWaitPage.total = resp.data.total;
                vm.leaveWaitPage.pageNum = resp.data.pageNum;
            });
        },
        //获取请假通过信息
        fetchUserLeavePassList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/userLeave/3/' + this.leavePassPage.pageNum, {}, resp => {
                vm.leaveList.pass = resp.data.list;
                vm.leavePassPage.total = resp.data.total;
                vm.leavePassPage.pageNum = resp.data.pageNum;
            });
        },
        //保存用户转移积分
        saveHelpInfo(helpForm) {
            var param = this.helpForm;
            var help = {
                userId: param.userId,
                time: __WEBPACK_IMPORTED_MODULE_4_moment___default()(param.time).format('YYYY-MM-DD HH:mm:ss'),
                description: param.description.trim(),
                integral: param.integral
            };
            this.$refs[helpForm].validate(valid => {
                if (valid) {
                    if (!this.isDecimal(param.integral)) {
                        this.$message({ showClose: true, message: '积分格式错误', type: 'error' });
                        return false;
                    }
                    if (__WEBPACK_IMPORTED_MODULE_3__lib_Helper__["a" /* default */].decodeToken().userId == this.helpForm.userId) {
                        this.$message({ showClose: true, message: '求助目标不能是自己，请重试', type: 'error' });
                        return false;
                    }
                    if (this.helpForm.id != '') {
                        __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyPostHttp('/integral/editHelpDetail/' + this.helpForm.id, help, res => {
                            this.$message({ showClose: true, message: '转移积分更新成功，请等待审核', type: 'success' });
                            this.editHelpVisible = false;
                            this.clearHelpForm();
                        });
                    } else {
                        __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyPostHttp('/integral/add', help, res => {
                            this.$message({ showClose: true, message: '转移积分添加成功，请等待审核', type: 'success' });
                            this.editHelpVisible = false;
                            this.clearHelpForm();
                        });
                    }
                    this.reload();
                } else {
                    return false;
                }
            });
        },
        //获取我的待审核的积分转移
        fetchMyHelpWaitList() {
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/integral/getMyWaitList/' + this.waitPage.pageNum, {}, resp => {
                this.review.wait = resp.data.list;
                this.waitPage.total = resp.data.total;
            });
        },
        //所有待审核的积分转移
        fetchHelpWaitAdmin() {
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/integral/getHelpWaitList/' + this.waitPage.pageNum, {}, resp => {
                this.review.wait = resp.data.list;
                this.waitPage.total = resp.data.total;
            });
        },
        fetchHelpReviewAdmin() {
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/integral/getReviewList/' + this.reviewPage.pageNum, {}, resp => {
                this.review.review = resp.data.list;
                this.reviewPage.total = resp.data.total;
            });
        },
        // 获取所有我的审核通过的任务
        fetchMyReviewSuccess() {
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp(`/integral/getMyReviewList/` + this.reviewPage.pageNum, {}, resp => {
                this.review.review = resp.data.list;
                this.reviewPage.total = resp.data.total;
            });
        },
        //删除积分求助
        deleteHelp(id) {
            this.$confirm('此操作将删除该任务, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyDeleteHttp(`/integral/delete/` + id, {}, resp => {
                    this.$message({ showClose: true, message: '删除成功', type: 'success' });
                    this.reload();
                    this.helpDetailVisible = false;
                });
            }).catch(() => {});
        },
        //审核通过积分求助
        acceptHelpChange(id) {
            this.$confirm('确认通过?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp(`/integral/passReview/` + id, {}, resp => {
                    this.$message({ showClose: true, message: '审核成功', type: 'success' });
                    this.reload();
                    this.helpDetailVisible = false;
                });
            }).catch(() => {});
        },
        //积分转移详情
        reviewDetail(help) {
            this.helpDetailVisible = true;
            this.helpDetail.time = __WEBPACK_IMPORTED_MODULE_4_moment___default()(help.time).toDate();
            this.helpDetail.integral = help.integral;
            this.helpDetail.description = help.description;
            this.helpDetail.name = help.name.split(",")[1];
            this.helpDetail.username = help.name.split(",")[0];
            this.helpDetail.id = help.id;
            this.helpDetail.userId = help.userId;
        },
        editHelpDetail(help) {
            this.editHelpDetailVisible = true;
            this.helpForm.description = help.description;
            this.helpForm.integral = help.integral.toString();
            this.helpForm.time = __WEBPACK_IMPORTED_MODULE_4_moment___default()(help.time).toDate();
            this.helpForm.id = help.id;
            this.helpForm.name = help.name;
            this.helpForm.userId = help.userId;
            this.helpForm.username = help.username;
            this.editHelpVisible = true;
            this.helpDetailVisible = false;
        },
        leaveDetail(leave) {
            this.leaveDetailVisible = true;
            this.leaveForm.id = leave.id;
            this.leaveForm.description = leave.description;
            this.leaveForm.beginTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(leave.beginTime).toDate();
            this.leaveForm.endTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(leave.endTime).toDate();
            this.leaveForm.typeName = leave.typeName;
            this.leaveForm.type = leave.type;
            this.leaveForm.hours = leave.hours;
            this.userWeeks = leave.userWeeks;
        },
        editLeaveDetail(leave, index) {
            if (this.userRole == 0 && index != 1) {
                this.leaveDetail(leave);
            } else {
                this.editLeaveDetailVisible = true;
                this.leaveDetailVisible = false;
                this.editLeaveVisible = true;
                this.leaveForm.id = leave.id;
                this.leaveForm.description = leave.description;
                this.leaveForm.beginTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(leave.beginTime).toDate();
                this.leaveForm.endTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(leave.endTime).toDate();
                this.leaveForm.type = leave.type;
                this.leaveForm.hours = leave.hours;
                this.userWeeks = leave.userWeeks;
            }
        },
        clearHelpForm() {
            this.helpForm.integral = '';
            this.helpForm.time = '';
            this.helpForm.description = '';
            this.helpForm.userId = '';
            this.helpForm.id = '';
            this.helpForm.username = '';
            this.helpForm.name = '';
            this.editHelpDetailVisible = false;
        },
        clearLeaveForm() {
            this.leaveForm.id = this.leaveForm.endTime = this.leaveForm.beginTime = this.leaveForm.hours = this.leaveForm.type = this.leaveForm.description = '';
            this.userWeeks = [];
        },
        //待审核积分转移页
        handleWaitPage(currentPage) {
            this.waitPage.pageNum = currentPage;
            if (this.userRole > 0) {
                this.fetchMyHelpWaitList();
            } else {
                this.fetchHelpWaitAdmin();
            }
        },
        handleLeaveWaitPage(currentPage) {
            this.leaveWaitPage.pageNum = currentPage;
            if (this.userRole > 0) {
                this.fetchUserLeaveList();
            } else {
                this.fetchUserLeaveList();
            }
        },
        handleLeavePassPage(currentPage) {
            this.leavePassPage.pageNum = currentPage;
            if (this.userRole > 0) {
                this.fetchUserLeavePassList();
            } else {
                this.fetchUserLeavePassList();
            }
        },
        saveLeaveInfo(formName) {
            this.$refs[formName].validate(valid => {
                if (valid) {
                    if (this.leaveForm.type == '' || this.leaveForm.type == null) {
                        this.$message({ showClose: true, message: '请选择请假类型', type: 'warning' });
                        return false;
                    }
                    var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.leaveForm.hours);
                    if (!ishours) {
                        this.$message({ showClose: true, message: '请假时长填写错误', type: 'error' });
                        return false;
                    }
                    if (this.leaveForm.hours > 99999.9 || this.leaveForm.hours < 0) {
                        this.$message({ showClose: true, message: '请假时长正确值应为0~99999.9', type: 'error' });
                        return false;
                    }
                    this.weekTime.beginWeek = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.leaveForm.beginTime).week();
                    this.weekTime.endWeek = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.leaveForm.endTime).week();
                    this.leaveForm.beginTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.leaveForm.beginTime).toDate();
                    this.leaveForm.endTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.leaveForm.endTime).toDate();

                    let form = this.leaveForm;
                    form.beginTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(form.beginTime).format('YYYY-MM-DD HH:00:00');
                    form.endTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(form.endTime).format('YYYY-MM-DD HH:00:00');
                    if (this.weekTime.beginWeek != this.weekTime.endWeek || __WEBPACK_IMPORTED_MODULE_4_moment___default()(form.beginTime).isAfter(__WEBPACK_IMPORTED_MODULE_4_moment___default()(form.endTime)) || __WEBPACK_IMPORTED_MODULE_4_moment___default()(form.beginTime).isSame(__WEBPACK_IMPORTED_MODULE_4_moment___default()(form.endTime))) {
                        this.$message({ showClose: true, message: '请假日期有误,请检查(请假时间不能跨周、相同)', type: 'warning' });
                        return false;
                    }
                    form['userWeeks'] = this.userWeeks;
                    if (form.id != '') {
                        __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyPostHttp('/userLeave/editLeaveDetail/' + form.id, form, resp => {
                            this.$message({
                                showClose: true,
                                message: '新建请假申请成功',
                                type: 'success'
                            });
                            this.fetchUserLeaveList();
                            this.clearLeaveForm();
                            this.editLeaveVisible = false;
                            this.editLeaveDetailVisible = false;
                        });
                    } else {
                        __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyPostHttp('/userLeave/add', form, resp => {
                            this.$message({
                                showClose: true,
                                message: '请假申请修改成功',
                                type: 'success'
                            });
                            this.fetchUserLeaveList();
                            this.clearLeaveForm();
                            this.editLeaveVisible = false;
                            this.editLeaveDetailVisible = false;
                        });
                    }
                }
            });
        },
        deleteLeave(id) {
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyDeleteHttp('/userLeave/delete/' + id, null, resp => {
                this.$message({
                    showClose: true,
                    message: '请假申请删除成功',
                    type: 'success'
                });
                this.fetchUserLeaveList();
                this.clearLeaveForm();
                this.leaveDetailVisible = false;
            });
        },
        acceptLeave(id) {
            __WEBPACK_IMPORTED_MODULE_2__lib_Http__["a" /* default */].zsyGetHttp('/userLeave/passLeave/' + id, null, resp => {
                this.$message({
                    showClose: true,
                    message: '请假申请审核完成',
                    type: 'success'
                });
                this.fetchUserLeaveList();
                this.fetchUserLeavePassList();
                this.clearLeaveForm();
                this.leaveDetailVisible = false;
            });
        },
        handleReviewPage(currentPage) {
            this.reviewPage.pageNum = currentPage;
            if (this.userRole > 0) {
                this.fetchMyReviewSuccess();
            } else {
                this.fetchHelpReviewAdmin();
            }
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
                if (curMonth >= 0 && curMonth <= 2) {
                    startMonth = 0;
                } else if (curMonth >= 3 && curMonth <= 5) {
                    startMonth = 3;
                } else if (curMonth >= 6 && curMonth <= 8) {
                    startMonth = 6;
                } else if (curMonth >= 9 && curMonth <= 11) {
                    startMonth = 9;
                }
                return __WEBPACK_IMPORTED_MODULE_4_moment___default()(new Date(curYear, startMonth, 1)).format('YYYY-MM-DD') + "--" + __WEBPACK_IMPORTED_MODULE_4_moment___default()(new Date(curYear, startMonth + 3, 1) - 1).format('YYYY-MM-DD');
            }
        }
    },
    components: {
        TaskItem: __WEBPACK_IMPORTED_MODULE_0__TaskItem___default.a
    }
});

/***/ }),
/* 243 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__AddMember__ = __webpack_require__(324);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__AddMember___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__AddMember__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__ModifyMember__ = __webpack_require__(329);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__ModifyMember___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__ModifyMember__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__AddDepartment__ = __webpack_require__(323);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__AddDepartment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__AddDepartment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__lib_Http__ = __webpack_require__(3);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
                this.$message({ showClose: true, message: '重置密码成功', type: 'success' });
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
                this.$message({ showClose: true, message: '删除用户成功', type: 'success' });
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
/* 244 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(4);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__node_modules_element_ui_packages_button_src_button__ = __webpack_require__(13);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__node_modules_element_ui_packages_button_src_button___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__node_modules_element_ui_packages_button_src_button__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_tabs_src_tab_pane__ = __webpack_require__(172);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_tabs_src_tab_pane___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_tabs_src_tab_pane__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__node_modules_element_ui_packages_input_src_input__ = __webpack_require__(171);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__node_modules_element_ui_packages_input_src_input___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4__node_modules_element_ui_packages_input_src_input__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__node_modules_element_ui_packages_form_src_form__ = __webpack_require__(322);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__node_modules_element_ui_packages_form_src_form___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5__node_modules_element_ui_packages_form_src_form__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
    components: {
        ElForm: __WEBPACK_IMPORTED_MODULE_5__node_modules_element_ui_packages_form_src_form___default.a,
        ElInput: __WEBPACK_IMPORTED_MODULE_4__node_modules_element_ui_packages_input_src_input___default.a,
        ElTabPane: __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_tabs_src_tab_pane___default.a,
        ElButton: __WEBPACK_IMPORTED_MODULE_2__node_modules_element_ui_packages_button_src_button___default.a },
    name: 'Project',
    data() {
        return {
            activeName: "project",
            activeNames: 1,
            editProjectVisible: false,
            inputVisible: true,
            addStageVisible: false,
            editStageVisible: false,
            TaskItem: '',
            showAddTask: false,
            tagList: [],
            stageList: [],
            TagName: '',
            TagId: '',
            inputVisible: false,
            stage: {
                id: '',
                name: '',
                sort: ''
            },
            project: {
                imageUrl: '',
                name: '',
                description: ''
            }, //待编辑项目ID
            editProjectId: '',
            color: ['', 'primary', 'success', 'warning', 'danger', 'info', 'primary', 'success']
        };
    },
    beforeMount: function () {
        //选中项目tab
        this.$root.eventBus.$emit("handleTabSelected", "project");
        this.projectList();
        this.fetchTagList();
        this.fetchStageList();
    },
    computed: {
        //是否有权限
        hasPermission() {
            return __WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].decodeToken().userRole <= 1;
        },
        isAdmin() {
            return __WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].decodeToken().userRole == 0;
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
                    this.successMsg('删除成功');
                    this.project.name = this.project.description = this.project.imageUrl = this.editProjectId = '';
                    this.editProjectVisible = false;
                    this.projectList();
                });
            }).catch(() => {});
        },
        editProject(id, name, description, url) {
            if (this.hasPermission) {
                this.editProjectId = id;
                this.project.name = name;
                this.project.description = description;
                this.project.imageUrl = url;
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
                    this.successMsg('项目更新成功');
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
                this.warnMsg("项目名称不能为空且不超过15字");
                return false;
            }
        },
        saveProject() {
            if (this.saveAdd()) {
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp(__WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].API_URI.ADDPROJECT, this.project, res => {
                    this.successMsg("项目添加成功");
                    this.projectList();
                    this.project.name = this.project.description = this.editProjectId = '';
                    this.showAddTask = false;
                });
            }
        },
        beforeAvatarUpload(file) {
            const isImage = file.type.indexOf('image') === 0;
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isImage) {
                this.errorMsg("请选择正确的图片文件");
            }
            if (!isLt2M) {
                this.errorMsg("上传头像图片大小不能超过 2MB");
            }
            return isImage && isLt2M;
        },
        upload(file) {
            var data = new FormData();
            data.append('uploadFile', file.file);
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/upload/image', data, res => {
                this.project.imageUrl = res.data.url;
            });
        },
        fetchTagList() {
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/tag/list', {}, resp => {
                this.tagList = resp.data;
            });
        },
        deleteTagButton(tag) {
            this.$confirm('确认删除?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyDeleteHttp('/tag/' + tag.id, null, res => {
                    this.successMsg("标签删除成功");
                    this.tagList.splice(this.tagList.indexOf(tag), 1);
                });
            }).catch(() => {});
        },
        fetchStageList() {
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/stage/list', {}, resp => {
                this.stageList = resp.data;
            });
        },
        saveStage() {
            var reg = /^\+?[1-9][0-9]*$/;
            this.stage.sort = this.stage.sort.trim();
            this.stage.name = this.stage.name.trim();
            if (!reg.test(this.stage.sort) || this.stage.sort < 1 || this.stage.sort == '' || this.stage.sort > 100) {
                this.warnMsg("阶段优先级须为大于0并小于100的整数");
                return false;
            } else if (this.stage.name != '' && this.stage.name.length > 0 && this.stage.name.length <= 10) {
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/stage/add', this.stage, res => {
                    this.successMsg("阶段添加成功");
                    this.stage.name = this.stage.sort = '';
                    this.fetchStageList();
                    this.addStageVisible = false;
                });
            } else {
                this.warnMsg("阶段名称不为空且不超过10个字");
            }
        },
        clearStage() {
            this.stage.sort = '';
            this.stage.name = '';
        },
        deleteStage(index, rows) {
            this.$confirm('确认删除?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyDeleteHttp(`/stage/` + rows[index].id, {}, resp => {
                    this.successMsg("阶段删除成功");
                    this.fetchStageList();
                });
            }).catch(() => {});
        },
        editStageDialog(index, rows) {
            this.editStageVisible = true;
            this.stage.id = rows[index].id;
            this.stage.sort = rows[index].sort;
            this.stage.name = rows[index].name;
        },
        editStage() {
            var reg = /^\+?[1-9][0-9]*$/;
            if (!reg.test(this.stage.sort) || this.stage.sort < 1 || this.stage.sort == '' || this.stage.sort > 100) {
                this.warnMsg("阶段优先级须为大于0并小于100的整数");
                return false;
            } else if (this.stage.name != '' && this.stage.name.trim().length > 0 && this.stage.name.trim().length <= 10) {
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp(`/stage/edit`, this.stage, resp => {
                    this.successMsg("阶段更新成功");
                    this.fetchStageList();
                    this.editStageVisible = false;
                });
            } else {
                this.warnMsg("阶段名称不为空且不超过10个字");
            }
        },
        showTag() {
            this.inputVisible = true;
            this.$nextTick(_ => {
                this.$refs.saveTagInput.$refs.input.focus();
            });
        },
        handleInputTagConfirm() {
            if (this.TagName != '' && this.TagName.trim().length > 0 && this.TagName.length <= 10) {
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/tag/add?name=' + this.TagName, null, res => {
                    this.successMsg("标签添加成功");
                    this.fetchTagList;
                    this.tagList.push({ color: "7", id: res.data, name: this.TagName });
                    this.inputVisible = false;
                    this.TagName = '';
                });
            } else {
                this.inputVisible = false;
                this.TagName = '';
                this.warnMsg("标签名不为空且不超过10个字");
            }
        },
        msg(msg) {
            this.$message({
                showClose: true,
                message: msg
            });
        },
        successMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'success'
            });
        },

        warnMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'warning'
            });
        },
        errorMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'error'
            });
        }
    }
});

/***/ }),
/* 245 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(4);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__node_modules_element_ui_packages_button_src_button__ = __webpack_require__(13);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__node_modules_element_ui_packages_button_src_button___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__node_modules_element_ui_packages_button_src_button__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_dialog_src_component__ = __webpack_require__(321);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_dialog_src_component___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_dialog_src_component__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_moment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__node_modules_element_ui_packages_tabs_src_tab_pane__ = __webpack_require__(172);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__node_modules_element_ui_packages_tabs_src_tab_pane___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5__node_modules_element_ui_packages_tabs_src_tab_pane__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__Task__ = __webpack_require__(28);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__Task___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_6__Task__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
    components: {
        ElButton: __WEBPACK_IMPORTED_MODULE_2__node_modules_element_ui_packages_button_src_button___default.a,
        ElTabPane: __WEBPACK_IMPORTED_MODULE_5__node_modules_element_ui_packages_tabs_src_tab_pane___default.a },
    name: 'IntegralHistory',
    data() {
        return {
            activeName: 'stat',
            createBugSolvingVisible: false,
            updateBugSolvingVisible: false,
            statsData: [],
            pesonalTaskData: [],
            modifyId: '',
            bugList: {
                userId: '',
                startTime: '',
                endTime: '',
                pageNum: 1
            },
            leaveList: {
                userId: '',
                beginTime: '',
                endTime: '',
                pageNum: 1
            },
            bugFormPage: {
                pageSize: 10,
                total: 0
            },
            leaveFormPage: {
                pageSize: 10,
                total: 0
            },
            bugForm: {
                projectId: '',
                description: '',
                createTime: '',
                processTime: ''
            },
            persanalForm: {
                userId: '',
                startTime: '',
                endTime: ''
            },
            daterange: '',
            bugDaterange: '',
            leaveDaterange: '',
            bugDetailForm: {},
            bugManage: [],
            leaveManage: [],
            addMemberIndex: {
                index: '',
                userId: '',
                integral: '',
                userName: ''
            },
            stepTemp: {},
            bugDetailVisible: false,
            projectForm: [],
            userList: [],
            bugUsers: [],
            showAddDetail: false,
            statsPage: {
                layout: "total, pager",
                currentPage: 1,
                pageSize: 10,
                totals: 0,
                pageNum: 0
            },
            userWeekForm: {
                weekNumber: '',
                date: ''
            },
            userWeekData: [],
            currentWeek: __WEBPACK_IMPORTED_MODULE_4_moment___default()().week(),
            pickerOptions: {
                shortcuts: [{
                    text: '本周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(__WEBPACK_IMPORTED_MODULE_4_moment___default()().startOf('week').valueOf());
                        end.setTime(__WEBPACK_IMPORTED_MODULE_4_moment___default()().endOf('week').valueOf());
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '本月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(__WEBPACK_IMPORTED_MODULE_4_moment___default()().startOf('month').valueOf());
                        end.setTime(__WEBPACK_IMPORTED_MODULE_4_moment___default()().endOf('month').valueOf());
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '本季度',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(__WEBPACK_IMPORTED_MODULE_4_moment___default()().startOf('quarter').valueOf());
                        end.setTime(__WEBPACK_IMPORTED_MODULE_4_moment___default()().endOf('quarter').valueOf());
                        picker.$emit('pick', [start, end]);
                    }
                }]
            },
            pickerWeek: {
                firstDayOfWeek: 1
            }
        };
    },
    beforeMount: function () {
        this.getStats(this.statsPage.currentPage);
        //选中任务tab
        this.$root.eventBus.$emit("handleTabSelected", "stats");
        this.fetchUserList();
        this.fetchProjectList();
        this.fetchProjectList();
        this.getBugList();
        this.getLeaveList();
    },
    computed: {
        permit() {
            let userRole = __WEBPACK_IMPORTED_MODULE_1__lib_Helper__["a" /* default */].decodeToken().userRole;
            return userRole < 2;
        },
        pageLayout() {
            if (this.bugFormPage.total > 0) {
                return 'total, prev, pager, next';
            }
            return 'total, pager';
        },
        leavePageLayout() {
            if (this.leaveFormPage.total > 0) {
                return 'total, prev, pager, next';
            }
            return 'total, pager';
        }
    },
    filters: {
        formatDate: function (value) {
            if (!value) return '';
            return __WEBPACK_IMPORTED_MODULE_4_moment___default()(value).format('YYYY年MM月DD日 HH:00:00');
        }
    },
    methods: {
        getStats(currentPage) {
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/stats/list/`, {}, resp => {
                this.statsData = resp.data;
            });
        },
        getCurrentWeek() {
            this.userWeekForm.date = __WEBPACK_IMPORTED_MODULE_4_moment___default()().toDate();
        },
        getTask(index) {
            this.$router.push({ name: 'taskList', params: { userId: this.statsData[index].id } });
        },
        getPesonTask(taskId) {
            this.$router.push({ name: 'taskListFormComments', params: { taskId: taskId } });
        },
        getBugList() {
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/bug/list/`, this.bugList, resp => {
                this.bugManage = resp.data.list;
                this.bugFormPage.total = resp.data.total;
            });
        },
        getLeaveList() {
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp(`/userLeave/list/`, this.leaveList, resp => {
                this.leaveManage = resp.data.list;
                this.leaveFormPage.total = resp.data.total;
            });
        },
        bugTimeChange(time) {
            // 选择结束时间
            time = time.split(' - ');
            if (time && time.length == 2) {
                this.bugList.startTime = `${time[0]} 00:00:00`;
                this.bugList.endTime = `${time[1]} 23:59:59`;
            } else {
                this.bugList.startTime = this.bugList.endTime = this.bugDaterange = '';
            }
        },
        leaveTimeChange(time) {
            // 选择结束时间
            time = time.split(' - ');
            if (time && time.length == 2) {
                this.leaveList.beginTime = `${time[0]} 00:00:00`;
                this.leaveList.endTime = `${time[1]} 23:59:59`;
            } else {
                this.leaveList.startTime = this.leaveList.endTime = this.leaveDaterange = '';
            }
        },
        timeChange(time) {
            // 选择结束时间
            time = time.split(' 至 ');
            if (time && time.length == 2) {
                this.persanalForm.startTime = `${time[0]} 00:00:00`;
                this.persanalForm.endTime = `${time[1]} 23:59:59`;
            } else {
                this.persanalForm.startTime = this.persanalForm.endTime = this.daterange = '';
            }
        },
        getPersonalData() {
            if (this.persanalForm.userId == null || this.persanalForm.userId === '') {
                this.errorMsg("请选择你想查询的用户");
            }
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/stats/personTaskList`, this.persanalForm, resp => {
                this.pesonalTaskData = resp.data;
            });
        },
        saveBugForm() {
            if (this.bugForm.projectId == '' || this.bugForm.description == '' || this.bugForm.createTime == '' || this.bugForm.processTime == '') {
                this.errorMsg('请将问题信息填写完整');
                return;
            }
            this.bugForm.createTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.bugForm.createTime).format('YYYY-MM-DD HH:mm:ss');
            this.bugForm.processTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.bugForm.processTime).format('YYYY-MM-DD HH:mm:ss');
            let param = this.bugForm;
            param.projectId = param.projectId.trim();
            param.description = param.description.trim();
            param['bugUsers'] = this.bugUsers;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/bug/add', param, resp => {
                this.$message({
                    showClose: true,
                    message: 'Bug处理结果创建成功',
                    type: 'success'
                });
                this.bugForm.projectId = this.bugForm.description = '';
                this.bugForm.createTime = this.bugForm.processTime = '';
                this.createBugSolvingVisible = false;
                this.bugUsers = [];
                this.getBugList();
            });
        },
        editBugForm(id) {
            if (this.bugForm.projectId == '' || this.bugForm.description == '' || this.bugForm.createTime == '' || this.bugForm.processTime == '') {
                this.errorMsg('请将问题信息填写完整');
                return;
            }
            this.bugForm.createTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.bugForm.createTime).format('YYYY-MM-DD HH:mm:ss');
            this.bugForm.processTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.bugForm.processTime).format('YYYY-MM-DD HH:mm:ss');
            let param = this.bugForm;
            param.projectId = param.projectId.trim();
            param.description = param.description.trim();
            param['bugUsers'] = this.bugUsers;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp('/bug/update/' + this.modifyId, param, resp => {
                this.$message({
                    showClose: true,
                    message: 'Bug处理结果更新成功',
                    type: 'success'
                });
                this.bugForm.projectId = this.bugForm.description = '';
                this.bugForm.createTime = this.bugForm.processTime = '';
                this.updateBugSolvingVisible = false;
                this.bugUsers = [];
                this.getBugList();
            });
        },
        createBugSolve() {
            this.createBugSolvingVisible = true;
            this.bugForm.description = this.bugForm.projectId = this.bugForm.createTime = this.bugForm.processTime = '';
            this.bugUsers = [];
            this.showAddDetail = false;
            this.addMemberIndex = {
                index: '',
                userId: '',
                userName: '',
                integral: ''
            };
        },
        saveAddMember() {
            if (this.addMemberIndex.userId == '' || this.addMemberIndex.integral == '') {
                this.errorMsg('请将积分信息填写完整');
                return;
            }
            this.showAddDetail = !this.showAddDetail;
            if (this.addMemberIndex.index === '') {
                let bugUser = {};
                bugUser.userId = this.addMemberIndex.userId;
                bugUser.userName = this.addMemberIndex.userName;
                bugUser.integral = this.addMemberIndex.integral;
                this.bugUsers.push(bugUser);
            } else {
                // 取消css
                this.bugUsers[this.addMemberIndex.index].cssClass = '';
            }

            this.addMemberIndex = {
                index: '',
                userId: '',
                userName: '',
                integral: ''
            };
            this.stepTemp = {};
        },
        modifyMember(index, stages) {
            this.stepTemp = {
                userId: stages[index].userId,
                userName: stages[index].userName,
                integral: stages[index].integral
            };
            this.bugUsers.forEach(item => {
                item.cssClass = '';
            });
            this.bugUsers[index].cssClass = 'stepActive';
            this.addMemberIndex = stages[index];
            this.addMemberIndex.index = index;
            this.showAddDetail = true;
        },
        deleteMember(index) {
            this.bugUsers.splice(index, 1);
            if (this.bugUsers.length == 0) {
                this.showAddDetail = false;
                this.addMemberIndex = {
                    index: '',
                    userId: '',
                    userName: '',
                    integral: ''
                };
            }
        },
        cancelAddMember() {
            this.showAddDetail = !this.showAddDetail;
        },
        editBugDetail(bugDetailForm) {
            this.updateBugSolvingVisible = true;
            this.bugDetailVisible = false;
            this.bugForm.projectId = bugDetailForm.projectId;
            this.bugForm.createTime = bugDetailForm.createTime;
            this.bugForm.processTime = bugDetailForm.processTime;
            this.bugForm.description = bugDetailForm.description;
            this.bugUsers = bugDetailForm.bugUsers;
        },
        deleteBug() {
            this.$confirm('确认删除?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyDeleteHttp('/bug/' + this.modifyId, null, resp => {
                    this.$message({
                        showClose: true,
                        message: 'Bug处理结果删除成功',
                        type: 'success'
                    });
                });
                this.bugDetailVisible = false;
                this.getBugList();
            }).catch(() => {});
        },
        fetchUserList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/user/effective', {}, resp => {
                vm.userList = resp.data;
            });
        },
        fetchProjectList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/project/list', {}, resp => {
                vm.projectForm = resp.data;
            });
        },
        stepUserChange(val) {
            let vm = this;
            this.userList.forEach(user => {
                if (user.id === val) {
                    vm.addMemberIndex.userName = user.name;
                }
            });
        },
        bugDetail(row) {
            this.bugDetailVisible = true;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/bug/' + row.id, null, resp => {
                this.bugDetailForm = resp.data;
            });
            this.modifyId = row.id;
        },
        handleCurrentChange(currentPage) {
            this.bugList.pageNum = currentPage;
            this.getBugList();
        },
        leaveHandleCurrentChange(currentPage) {
            this.leaveList.pageNum = currentPage;
            this.getLeaveList();
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
        errorMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'error'
            });
        },
        getSummaries(param) {
            const { columns, data } = param;
            const sums = [];
            columns.forEach((column, index) => {
                if (index === 0) {
                    sums[index] = '总计';
                    return;
                }
                if (index < 4) {
                    sums[index] = '';
                    return;
                }
                const values = data.map(item => Number(item[column.property]));
                if (!values.every(value => isNaN(value))) {
                    sums[index] = values.reduce((prev, curr) => {
                        const value = Number(curr);
                        if (!isNaN(value)) {
                            return prev + curr;
                        } else {
                            return prev;
                        }
                    }, 0);
                }
            });

            return sums;
        },
        getUserWeekStats() {
            if (this.userWeekForm.date != '') {
                this.userWeekForm.date = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.userWeekForm.date).format('YYYY-MM-DD HH:mm:ss');
                this.userWeekForm.weekNumber = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.userWeekForm.date).week();
                if (this.userWeekForm.date != '') {
                    __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPostHttp('/stats/weekStats', this.userWeekForm, resp => {
                        this.userWeekData = resp.data;
                    });
                } else {
                    this.errorMsg('请选择统计信息');
                }
            } else {
                this.errorMsg("请选择时间");
            }
        },
        getPesonStats(id) {
            this.activeName = 'personal';
            this.persanalForm.userId = id;
            this.persanalForm.startTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.userWeekForm.date).startOf('week').format("YYYY-MM-DD 00:00:00");
            this.persanalForm.endTime = __WEBPACK_IMPORTED_MODULE_4_moment___default()(this.userWeekForm.date).endOf('week').format("YYYY-MM-DD 23:59:59");
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/stats/personTaskList`, this.persanalForm, resp => {
                this.pesonalTaskData = resp.data;
            });
        }

    }
});

/***/ }),
/* 246 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__CreateTask__ = __webpack_require__(326);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__CreateTask___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0__CreateTask__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__TaskItem__ = __webpack_require__(178);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__TaskItem___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__TaskItem__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__board_TaskBoard__ = __webpack_require__(332);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__board_TaskBoard___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_2__board_TaskBoard__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4__lib_Helper__ = __webpack_require__(4);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5_moment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__node_modules_element_ui_packages_checkbox_src_checkbox__ = __webpack_require__(320);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__node_modules_element_ui_packages_checkbox_src_checkbox___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_6__node_modules_element_ui_packages_checkbox_src_checkbox__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__node_modules_element_ui_packages_button_src_button__ = __webpack_require__(13);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__node_modules_element_ui_packages_button_src_button___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_7__node_modules_element_ui_packages_button_src_button__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
            btnValStatus: 1, /*1是列表模式，2是看板模式*/
            loading: true,
            sortList: [{ id: 1, name: '优先级', tips: '排序' }, { id: 2, name: '截止时间', tips: '升序' }, { id: 3, name: '完成时间', tips: '升序' }, { id: 4, name: '创建时间', tips: '降序' }],
            timeRange: '',
            publishText: '设置下次发版时间',
            publishHide: true,
            projectList: [],
            userList: [],
            stageList: [],
            tagList: [],
            publishTime: '',
            publishType: false,
            publishVisible: false,
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
                taskId: '',
                projectId: '',
                userId: '',
                stageId: [],
                tagId: [],
                type: 2,
                status: 1,
                priority: '',
                beginTime: '',
                endTime: '',
                sort: '2',
                createBy: ''
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
        if (typeof this.$route.params.userId != "undefined") {
            this.form.userId = this.$route.params.userId;
            this.form.type = '';
            window.localStorage.removeItem("viewType");
        }
        if (typeof this.$route.params.taskId != "undefined") {
            this.form.taskId = this.$route.params.taskId;
            this.form.type = '';
            this.form.status = '';
            window.localStorage.removeItem("viewType");
        }
        // 视图状态
        const viewType = window.localStorage.getItem("viewType");
        const publishType = window.localStorage.getItem("publishType");
        if (publishType == 0 || publishType == null) {
            this.publishType = false;
        } else {
            this.publishType = true;
        }
        if (viewType != null && viewType !== '') {
            this.btnValStatus = viewType;
        }
        this.fetchProjectList();
        this.fetchUserList();
        this.fetchStageList();
        this.fetchTagList();
        this.fetchTaskList();
        this.getPublishTime();
        //选中任务tab
        this.$root.eventBus.$emit("handleTabSelected", "task");
    },

    computed: {
        permit() {
            let userRole = __WEBPACK_IMPORTED_MODULE_4__lib_Helper__["a" /* default */].decodeToken().userRole;
            return userRole < 2;
        },
        userRole() {
            let userRole = __WEBPACK_IMPORTED_MODULE_4__lib_Helper__["a" /* default */].decodeToken().userRole;
            return userRole;
        },
        pageLayout() {
            if (this.page.total > 0) {
                return 'total, prev, pager, next';
            }
            return 'total, pager';
        }
    },
    watch: {
        btnValStatus: function (val) {
            if (val == 1) {
                document.getElementById('app').style.overflowY = 'auto';
            } else {
                document.getElementById('app').style.overflowY = 'hidden';
            }
        }
    },
    beforeDestroy() {
        document.getElementById('app').style.overflowY = 'auto';
    },
    methods: {
        btnValFun() {
            if (this.btnValStatus == 1) {
                // 刷新看板
                //this.$root.eventBus.$emit("reloadBoard");
                this.btnValStatus = 2;
                setTimeout(() => {
                    this.$refs.taskBoard.drugList();
                }, 0);
            } else {
                // 刷新列表
                this.fetchTaskList();
                this.btnValStatus = 1;
            }

            // 记住状态
            window.localStorage.setItem("viewType", this.btnValStatus);
        },
        setPublish() {
            if (this.userRole == 0) this.publishVisible = true;
        },
        setPublishTime() {
            let vm = this;
            if (vm.publishTime == '') {
                this.$message({
                    showClose: true,
                    message: '请填写发版时间',
                    type: 'warning'
                });
                return;
            }
            vm.publishTime = __WEBPACK_IMPORTED_MODULE_5_moment___default()(vm.publishTime).format('YYYY-MM-DD 23:59:59');
            __WEBPACK_IMPORTED_MODULE_3__lib_Http__["a" /* default */].zsyPutHttp('/task/publish/' + vm.publishTime, {}, resp => {
                if (resp.errCode == "00") {
                    this.$message({
                        showClose: true,
                        message: '设置下次发版时间成功',
                        type: 'success'
                    });
                    this.publishVisible = false;
                    this.getPublishTime();
                    if (this.publishType) {
                        this.$router.go(0);
                    } else {
                        this.publishType = false;
                    }
                }
            });
        },
        getPublishTime() {
            __WEBPACK_IMPORTED_MODULE_3__lib_Http__["a" /* default */].zsyGetHttp('/task/publish', {}, resp => {
                if (resp.data != null) {
                    if (resp.data == "946742399000" && this.userRole != 0) {
                        this.publishHide = false;
                    } else if (resp.data == "946742399000" && this.userRole == 0) {
                        this.publishText = "设置下次发版时间";
                    } else {
                        this.publishText = "下次发版时间:" + __WEBPACK_IMPORTED_MODULE_5_moment___default()(resp.data).format("YYYY-MM-DD");
                        this.publishHide = true;
                    }
                }
            });
        },
        publishTask() {
            if (this.publishType) {
                window.localStorage.setItem("publishType", 1);
            } else {
                window.localStorage.setItem("publishType", 0);
            }
            this.$router.go(0);
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
        },
        /** 排序方式**/
        choiceSort(id, $event) {
            if (id == this.form.sort) {
                this.form.sort = '';
            } else {
                this.form.sort = id;
            }
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
            this.form.taskId = '';
            this.fetchTaskList();
        },
        fetchTaskList() {
            this.loading = true;
            this.taskItems = [];
            let vm = this;
            let param = {};
            param['pageNum'] = this.page.pageNum || 1;
            param['pageSize'] = this.page.pageSize;
            if (this.form.taskId !== '') {
                param['taskId'] = this.form.taskId;
            }
            if (this.form.projectId !== '') {
                param['projectId'] = this.form.projectId;
            }
            if (this.form.userId !== '') {
                param['userId'] = this.form.userId;
            }
            if (this.form.createBy !== '') {
                param['createBy'] = this.form.createBy;
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
            param['sort'] = this.form.sort;
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
            // 刷新看板
            //this.$root.eventBus.$emit("reloadBoard");
        }
    },
    components: {
        ElButton: __WEBPACK_IMPORTED_MODULE_7__node_modules_element_ui_packages_button_src_button___default.a,
        ElCheckbox: __WEBPACK_IMPORTED_MODULE_6__node_modules_element_ui_packages_checkbox_src_checkbox___default.a,
        TaskItem: __WEBPACK_IMPORTED_MODULE_1__TaskItem___default.a,
        CreateTask: __WEBPACK_IMPORTED_MODULE_0__CreateTask___default.a,
        TaskBoard: __WEBPACK_IMPORTED_MODULE_2__board_TaskBoard___default.a
    }
});

/***/ }),
/* 247 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1_moment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__lib_Helper__ = __webpack_require__(4);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_lodash__ = __webpack_require__(45);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_lodash___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_lodash__);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
        userList: Array,
        viewType: ''
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
                beginTime: '',
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
                facility: '',
                tags: [],
                taskType: 2,
                stageId: '',
                taskUsers: [],
                modifyDescription: ''
            },
            priorityList: [{ label: '普通', value: 1 }, { label: '紧急', value: 2 }, { label: '非常紧急', value: 3 }],
            facilityList: [{ label: '容易', value: 1 }, { label: '简单', value: 2 }, { label: '一般', value: 3 }, { label: '较难', value: 4 }, { label: '困难', value: 5 }],
            statusOptions: [{ name: '进行中', id: 1 }, { name: '已完成', id: 2 }],
            step: {
                index: '',
                stageId: '',
                stageName: '',
                userId: '',
                userName: '',
                taskHours: '',
                beginTime: '',
                endTime: '',
                completeHours: '',
                completeTime: '',
                description: '',
                status: ''
            },
            otherStep: {
                description: '',
                status: ''
            },
            stepTemp: {},
            weekTime: {
                beginWeek: '',
                endWeek: ''
            },
            weekNumber: [],
            weekNumberTemp: [] /*
                               projectList: [],
                               stageList: [],
                               tagList: [],*/
        };
    },
    beforeMount() {
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
        },
        sortWeekNumber() {
            if (this.weekNumber.length != null) {
                for (let i = 0; i < this.weekNumber.length; i++) {
                    for (let x = 0; x < this.weekNumberTemp.length; x++) {
                        if (this.weekNumber[i] != null) {
                            if (this.weekNumber[i].weekNumber == this.weekNumberTemp[x].weekNumber) {
                                this.weekNumber[i].hoursTemp = this.weekNumberTemp[x].hours;
                            }
                        }
                    }
                }
            }
            return __WEBPACK_IMPORTED_MODULE_3_lodash___default.a.orderBy(this.weekNumber, 'weekNumber');
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
                this.$message({ showClose: true, message: '任务审核成功', type: 'success' });
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
                this.$message({ showClose: true, message: '任务打回成功', type: 'success' });
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
                    this.$message({ showClose: true, message: '操作成功', type: 'success' });
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
            var vm = this;
            // 待审核点击
            if (vm.taskStatus == 'WaitAuditing') {
                vm.taskItems.forEach(task => {
                    if (task.id === taskId) {
                        vm.showAuditPop(task.id, task.taskUsers[0].id);
                    }
                });
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
                    this.$root.eventBus.$emit('reloadBoard');
                    this.$message({ showClose: true, message: '操作成功', type: 'success' });
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
                    this.$root.eventBus.$emit('reloadBoard');
                    this.$message({ showClose: true, message: '删除成功', type: 'success' });
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
                this.$message({ showClose: true, message: '评价成功', type: 'success' });
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
                this.warnMsg("请选择项目");
                return;
            }
            if (this.modifyPrivateTaskForm.endTime == '') {
                this.warnMsg("请选择结束时间");
                return;
            }
            if (this.modifyPrivateTaskForm.taskHours == '') {
                this.warnMsg("请输入工作量");
                return;
            }
            if (this.modifyPrivateTaskForm.taskName.trim() == '') {
                this.warnMsg("请填写任务名称");
                return;
            }
            if (this.modifyPrivateTaskForm.description.trim() == '') {
                this.warnMsg("请填写任务备注");
                return;
            }

            if (this.modifyPrivateTaskForm.stageId === '') {
                this.warnMsg("请选择项目阶段");
                return;
            }
            if (this.modifyPrivateTaskForm.tags.length == 0) {
                this.warnMsg("请选择至少一项标签");
                return;
            }

            var isNum = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.modifyPrivateTaskForm.taskHours);
            if (!isNum) {
                this.$message({ showClose: true, message: '工作量填写错误', type: 'error' });
                return false;
            }
            if (this.modifyPrivateTaskForm.taskHours > 8 || this.modifyPrivateTaskForm.taskHours < 0.1) {
                this.$message({ showClose: true, message: '工作量正确值应为0.1~8', type: 'error' });
                return false;
            }
            if (__WEBPACK_IMPORTED_MODULE_1_moment___default()(this.modifyPrivateTaskForm.endTime).millisecond() < __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.modifyPrivateTaskForm.beginTime).millisecond() || __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.modifyPrivateTaskForm.endTime).week() != __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.modifyPrivateTaskForm.beginTime).week()) {
                this.$message({ showClose: true, message: '请检查日期，个人任务请勿跨周进行', type: 'error' });
                return false;
            }

            this.modifyPrivateTaskForm.taskName = this.modifyPrivateTaskForm.taskName.trim();
            this.modifyPrivateTaskForm.endTime = __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.modifyPrivateTaskForm.endTime).format('YYYY-MM-DD 23:59:59');
            this.modifyPrivateTaskForm.taskUsers = [{
                userId: this.modifyPrivateTaskForm.userId,
                taskHours: this.modifyPrivateTaskForm.taskHours,
                beginTime: __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.modifyPrivateTaskForm.beginTime).format('YYYY-MM-DD HH:mm:ss'),
                endTime: __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.modifyPrivateTaskForm.endTime).format('YYYY-MM-DD 23:59:59'),
                description: this.modifyPrivateTaskForm.description.trim()
            }];
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp(`/task/modify/${this.modifyPrivateTaskForm.id}`, this.modifyPrivateTaskForm, resp => {
                this.$message({ showClose: true, message: '任务修改成功', type: 'success' });
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
                this.modifyPrivateTaskForm.beginTime = resp.data.users[0].beginTime;
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
                facility: 1,
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
                this.modifyTaskForm.facility = resp.data.facility;
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
                status: stages[index].status,
                userWeeks: stages[index].userWeeks
            };
            this.step = stages[index];
            this.step.index = index;
            this.otherStep.description = stages[index].description;
            this.otherStep.status = stages[index].status;
            this.modifyTaskForm.taskUsers.forEach(item => {
                item.cssClass = '';
            });
            this.modifyTaskForm.taskUsers[index].cssClass = 'stepActive';
            this.showAddDetail = true;
            this.weekNumberTemp = stages[index].userWeeks;
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
            this.modifyTaskForm.facility = 1;
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
            this.otherStep.description = '';
            this.otherStep.status = '';
            this.weekNumberTemp = [];
        },
        saveAddMember() {
            var sumHours = 0;
            for (var i = 0; i < this.weekNumber.length; i++) {
                if (this.weekNumber[i].hours == '' || this.weekNumber[i].hours === undefined) {
                    if (this.weekNumber[i].hoursTemp !== undefined && this.weekNumber[i].hoursTemp != '') {
                        this.weekNumber[i].hours = this.weekNumber[i].hoursTemp;
                    } else {
                        this.weekNumber[i].hours = 0;
                    }
                }
                var ishours = /^(([0-9]+[\.]?[0-9]+)|[1-9])$/.test(this.weekNumber[i].hours);
                if (!ishours && ishours != 0) {
                    this.errorMsg('工作量填写错误');
                    return false;
                }
                if (this.weekNumber[i].hours > 99999.9 || this.weekNumber[i].hours < 0) {
                    this.errorMsg('工作量正确值应为0~99999.9');
                    return false;
                }
                sumHours += parseFloat(this.weekNumber[i].hours);
            }
            if (sumHours != this.step.taskHours) {
                this.errorMsg('周工作量与总工作量不符，请检查');
                return;
            }
            const valid = this.step.userId == '' || this.step.taskHours == '' || this.step.beginTime == '' || this.step.endTime == '';
            if (valid) {
                this.warnMsg('请将阶段填写完整');
                return;
            }
            if (this.step.index === '') {
                let taskUser = {};
                taskUser.userId = this.step.userId;
                taskUser.userName = this.step.userName;
                taskUser.beginTime = this.step.beginTime;
                taskUser.endTime = this.step.endTime;
                taskUser.taskHours = this.step.taskHours;
                taskUser.description = this.otherStep.description;
                taskUser.userWeeks = this.weekNumber;
                taskUser.status = this.otherStep.status;
                this.modifyTaskForm.taskUsers.push(taskUser);
            } else {
                // 取消css
                this.modifyTaskForm.taskUsers[this.step.index].cssClass = '';
                this.modifyTaskForm.taskUsers[this.step.index].description = this.otherStep.description;
                this.modifyTaskForm.taskUsers[this.step.index].status = this.otherStep.status;
                this.modifyTaskForm.taskUsers[this.step.index].userWeeks = this.weekNumber;
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
            this.otherStep.description = '';
            this.otherStep.status = '';
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
                this.warnMsg("请填写任务名称");
                return;
            }
            if (this.modifyTaskForm.projectId == '') {
                this.warnMsg("请选择项目");
                return;
            }
            if (this.modifyTaskForm.endTime == '') {
                this.warnMsg("请选择结束时间");
                return;
            }
            if (this.modifyTaskForm.stageId === '') {
                this.warnMsg("请选择项目阶段");
                return;
            }
            if (this.modifyTaskForm.tags.length == 0) {
                this.warnMsg("请选择至少一项标签");
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
                if (user.userWeeks == null) {
                    this.errorMsg('请检查周工作量是否填写完整'); //判断不可用
                    return false;
                }
            });
            param.endTime = __WEBPACK_IMPORTED_MODULE_1_moment___default()(param.endTime).format('YYYY-MM-DD 23:59:59');
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp(`/task/modify/${this.modifyTaskForm.id}`, param, resp => {
                this.$message({ showClose: true, message: '任务修改成功', type: 'success' });
                this.hideTaskModify();
                // 刷新看板
                //this.$root.eventBus.$emit("reloadBoard");
                // 刷新列表
                vm.$emit('reload');
                // 刷新看板
                this.$root.eventBus.$emit('reloadBoard');
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
        },
        taskStepStatus(item, taskUserNum) {
            const commented = item.commentNum > 0 && item.commentNum == taskUserNum - 1;
            let className = 'in';
            if (item.status == 1) {
                // 进行中
                className = "in";
            } else if (item.status > 1 && !commented) {
                // 已完成未评级
                className = "done";
            } else {
                // 已评价
                className = "finished";
            }
            return className;
        },
        //评审任务
        examineTask(id, exam) {
            var examText;
            if (exam != '0') {
                examText = '确定评审完成?';
            } else {
                examText = '确定取消评审?';
            }
            this.$confirm(examText, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                let vm = this;
                __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyPutHttp('/task/examine/' + exam + '/' + id, {}, resp => {
                    if (exam != '0') {
                        this.$message({ showClose: true, message: '评审成功', type: 'success' });
                    } else {
                        this.$message({ showClose: true, message: '取消评审成功', type: 'success' });
                    }
                    vm.$emit('reload');
                    // 刷新看板
                    this.$root.eventBus.$emit('reloadBoard');
                    this.showTaskDetail = false;
                });
            }).catch(() => {});
        },
        warnMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'warning'
            });
        },
        errorMsg(msg) {
            this.$message({
                showClose: true,
                message: msg,
                type: 'error'
            });
        }
    },
    created() {
        // 监听看板任务点击事件
        var vm = this;
        vm.$root.eventBus.$off('handleBoardClick');
        vm.$root.eventBus.$on("handleBoardClick", taskId => {
            vm.showTaskDetail = true;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp(`/task/detail/${taskId}`, {}, resp => {
                vm.taskDetail = resp.data;
            });
            vm.getTaskLog(taskId);
        });
    },
    watch: {
        step: {
            deep: true,
            handler: function (val, oldVal) {
                this.weekNumber = [];
                let weekData = '';
                let param = this.weekNumber;
                if (this.step.userId != '' && this.step.taskHours != '' && this.step.beginTime != '' && this.step.endTime != '' && (__WEBPACK_IMPORTED_MODULE_1_moment___default()(this.step.beginTime).isBefore(this.step.endTime) || __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.step.beginTime).isSame(this.step.endTime))) {
                    this.weekTime.beginWeek = __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.step.beginTime).week();
                    this.weekTime.endWeek = __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.step.endTime).week();
                    let beginYear = __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.step.beginTime).year();
                    let endYear = __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.step.endTime).year();
                    if (beginYear != endYear) {
                        for (let i = this.weekTime.beginWeek; i < __WEBPACK_IMPORTED_MODULE_1_moment___default()(this.step.beginTime).weeksInYear() + 1; i++) {
                            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/userWeek/' + this.step.userId + '/' + beginYear + '/' + i, {}, resp => {
                                weekData = { 'weekNumber': i, 'hours': '', 'year': beginYear, 'weekHours': resp.data, 'range': __WEBPACK_IMPORTED_MODULE_1_moment___default()().year(beginYear).week(i).startOf('week').format('MM-DD') + '至' + __WEBPACK_IMPORTED_MODULE_1_moment___default()().year(beginYear).week(i).endOf('week').format('MM-DD') };
                                param.push(weekData);
                            });
                        }
                        for (let i = 1; i < this.weekTime.endWeek + 1; i++) {
                            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/userWeek/' + this.step.userId + '/' + endYear + '/' + i, {}, resp => {
                                weekData = { 'weekNumber': i, 'hours': '', 'year': endYear, 'weekHours': resp.data, 'range': __WEBPACK_IMPORTED_MODULE_1_moment___default()().year(endYear).week(i).startOf('week').format('MM-DD') + '至' + __WEBPACK_IMPORTED_MODULE_1_moment___default()().year(endYear).week(i).endOf('week').format('MM-DD') };
                                param.push(weekData);
                            });
                        }
                    }
                    if (this.weekTime.beginWeek == this.weekTime.endWeek) {
                        __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/userWeek/' + this.step.userId + '/' + beginYear + '/' + this.weekTime.beginWeek, {}, resp => {
                            weekData = { 'weekNumber': this.weekTime.beginWeek, 'hours': this.step.taskHours, 'weekHours': resp.data, 'year': beginYear, 'range': __WEBPACK_IMPORTED_MODULE_1_moment___default()().year(beginYear).week(this.weekTime.beginWeek).startOf('week').format('MM-DD') + '至' + __WEBPACK_IMPORTED_MODULE_1_moment___default()().year(beginYear).week(this.weekTime.beginWeek).endOf('week').format('MM-DD') };
                            param.push(weekData);
                        });
                    } else if (this.weekTime.endWeek - this.weekTime.beginWeek > 1) {
                        for (let i = this.weekTime.beginWeek; i < this.weekTime.endWeek + 1; i++) {
                            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/userWeek/' + this.step.userId + '/' + beginYear + '/' + i, {}, resp => {
                                weekData = { 'weekNumber': i, 'hours': '', 'year': beginYear, 'weekHours': resp.data, 'range': __WEBPACK_IMPORTED_MODULE_1_moment___default()().week(i).year(beginYear).startOf('week').format('MM-DD') + '至' + __WEBPACK_IMPORTED_MODULE_1_moment___default()().year(beginYear).week(i).endOf('week').format('MM-DD') };
                                param.push(weekData);
                            });
                        }
                    } else if (this.weekTime.endWeek - this.weekTime.beginWeek == 1) {
                        __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/userWeek/' + this.step.userId + '/' + beginYear + '/' + this.weekTime.beginWeek, {}, resp => {
                            param.push({ 'weekNumber': this.weekTime.beginWeek, 'hours': '', 'year': beginYear, 'weekHours': resp.data, 'range': __WEBPACK_IMPORTED_MODULE_1_moment___default()().year(beginYear).week(this.weekTime.beginWeek).startOf('week').format('MM-DD') + '至' + __WEBPACK_IMPORTED_MODULE_1_moment___default()().year(beginYear).week(this.weekTime.beginWeek).endOf('week').format('MM-DD') });
                        });
                        __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/userWeek/' + this.step.userId + '/' + endYear + '/' + this.weekTime.endWeek, {}, resp => {
                            param.push({ 'weekNumber': this.weekTime.endWeek, 'hours': '', 'year': beginYear, 'weekHours': resp.data, 'range': __WEBPACK_IMPORTED_MODULE_1_moment___default()().year(beginYear).week(this.weekTime.endWeek).startOf('week').format('MM-DD') + '至' + __WEBPACK_IMPORTED_MODULE_1_moment___default()().year(beginYear).week(this.weekTime.endWeek).endOf('week').format('MM-DD') });
                        });
                    }
                    this.weekNumber = param;
                }
            }
        }
    }

});

/***/ }),
/* 248 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Helper__ = __webpack_require__(4);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Http__ = __webpack_require__(3);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
            showUploadAvatarDialog: false,
            imageUrl: ''
        };
    },
    methods: {
        show() {
            this.showUploadAvatarDialog = true;
        },
        hide() {
            this.showUploadAvatarDialog = false;
            this.imageUrl = '';
        },
        beforeAvatarUpload(file) {

            const isImage = file.type.indexOf('image') === 0;
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isImage) {
                this.$message({ showClose: true, message: '请选择正确的图片文件', type: 'error' });
            }
            if (!isLt2M) {
                this.$message({ showClose: true, message: '上传头像图片大小不能超过 2MB', type: 'error' });
            }
            return isImage && isLt2M;
        },
        /**
         * 覆盖默认的上传行为
         */
        upload(file) {
            var data = new FormData();
            data.append('uploadFile', file.file);
            __WEBPACK_IMPORTED_MODULE_1__lib_Http__["a" /* default */].zsyPostHttp('/upload/image', data, res => {
                this.imageUrl = res.data.url;
            });
        },
        saveAvatar() {
            if (this.imageUrl == '') {
                this.$message({ showClose: true, message: '请选择图片文件', type: 'error' });
                return false;
            }
            let userId = __WEBPACK_IMPORTED_MODULE_0__lib_Helper__["a" /* default */].decodeToken().userId;
            __WEBPACK_IMPORTED_MODULE_1__lib_Http__["a" /* default */].zsyPutHttp(`/user/modifyAvatar`, { userId: userId, url: this.imageUrl }, res => {
                this.$message({ showClose: true, message: '更换头像成功', type: 'success' });
                this.$router.go(0);
            });
        }
    }
});

/***/ }),
/* 249 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(4);
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
    name: 'UserComments',
    data() {
        return {
            data: {},
            form: {
                pageNum: 1,
                userId: '',
                grade: ''
            },
            userList: [],
            gradeList: ['A', 'B', 'C']
        };
    },
    created() {
        this.fetchUserComments();
        this.fetchUserList();
    },
    beforeMount: function () {
        //选中任务tab
        this.$root.eventBus.$emit("handleTabSelected", "comments");
    },
    computed: {
        pageLayout() {
            if (this.data.total > 0) {
                return 'total, prev, pager, next';
            }
            return 'total, pager';
        }
    },
    methods: {
        fetchUserComments() {
            let vue = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/stats/userComments', this.form, resp => {
                vue.data = resp.data;
            });
        },
        handleCurrentChange(currentPage) {
            this.form.pageNum = currentPage;
            this.fetchUserComments();
        },
        fetchUserList() {
            let vm = this;
            __WEBPACK_IMPORTED_MODULE_0__lib_Http__["a" /* default */].zsyGetHttp('/user/effective', {}, resp => {
                vm.userList = resp.data;
            });
        },
        onSubmit() {
            this.form.pageNum = 1;
            this.fetchUserComments();
        },
        toTaskList(taskId) {
            console.log(taskId);
            this.$router.push({ name: 'taskListFormComments', params: { taskId: taskId } });
        }

    }
});

/***/ }),
/* 250 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_moment__ = __webpack_require__(0);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_moment___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_0_moment__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__lib_Helper__ = __webpack_require__(4);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__lib_move__ = __webpack_require__(251);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_button_src_button__ = __webpack_require__(13);
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

var dom;





__WEBPACK_IMPORTED_MODULE_0_moment___default.a.locale('zh-cn');
/* harmony default export */ __webpack_exports__["default"] = ({
    components: { ElButton: __WEBPACK_IMPORTED_MODULE_3__node_modules_element_ui_packages_button_src_button___default.a },
    name: "task-board",
    data() {
        return {
            stageList: [],
            taskBoxWidth: '1200px',
            cursor: ''
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
            this.$root.eventBus.$emit("handleBoardClick", taskId);
        },
        getData() {
            const publishType = window.localStorage.getItem("publishType");
            // 获取阶段
            let that = this;
            that.http.zsyGetHttp('/stage/list', {}, res => {
                that.stageList = res.data;
                that.taskBoxWidth = that.stageList.length * 270 + "px";
                that.stageList.forEach(stage => {
                    if (publishType == 1) {
                        // 获取任务
                        that.http.zsyGetHttp(`/task/tasksByStageTime/${stage.id}`, {}, res => {
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
                    } else {
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
                    }
                });
            });
        },
        drugList() {
            let vm = this;
            if (this.$parent.btnValStatus == 2) {
                var oUl = document.getElementById('task-board-list');
                /*var oUl = document.queryselector(".task-board-list");
                 var aLi = document.queryselectorAll(".task-list");*/
                var aLi = oUl.children;
                var zIndex = 2;
                //0.布局转换
                var aPos = [];

                for (var i = 0; i < aLi.length; i++) {
                    aPos.push({ left: aLi[i].offsetLeft, top: aLi[i].offsetTop });
                    aLi[i].style.left = aPos[i].left + 'px';
                    aLi[i].style.top = aPos[i].top + 'px';
                }
                for (var i = 0; i < aLi.length; i++) {
                    aLi[i].style.position = 'absolute';
                    aLi[i].style.margin = 0;
                    aLi[i].index = i;
                }

                //1.拖拽
                for (var i = 0; i < aLi.length; i++) {
                    drag(aLi[i].children[0], aLi[i]); //拖拽每一个图标
                }
            } else {
                console.log("列表模式");
            }

            function drag(obj, objBox) {
                if (vm.loginUserRole > 0) {
                    return;
                }
                obj.onmousedown = function (ev) {
                    var oEvt = ev || event;
                    var disX = oEvt.clientX - objBox.offsetLeft;
                    var disY = oEvt.clientY - objBox.offsetTop;
                    objBox.style.zIndex = zIndex++;
                    clearInterval(objBox.timer);
                    document.onmousemove = function (ev) {
                        var oEvt = ev || event;
                        objBox.style.left = oEvt.clientX - disX + 'px';
                        objBox.style.top = oEvt.clientY - disY + 'px';

                        //2.move时碰撞
                        var nearObj = findNearest(objBox);
                        if (nearObj && nearObj != objBox) {
                            //撞到了
                            //动所有的房客
                            //交换索引，所有房客
                            //'所有'有条件的
                            var n = objBox.index;
                            var m = nearObj.index;
                            for (var i = 0; i < aLi.length; i++) {
                                //n<aLi[i].index<=m
                                //m<=aLi[i].index<n
                                if (aLi[i].index > n && aLi[i].index <= m) {
                                    //←
                                    aLi[i].index--;
                                    __WEBPACK_IMPORTED_MODULE_2__lib_move__["a" /* default */].move(aLi[i], aPos[aLi[i].index]);
                                } else if (aLi[i].index >= m && aLi[i].index < n) {
                                    //→
                                    aLi[i].index++;
                                    __WEBPACK_IMPORTED_MODULE_2__lib_move__["a" /* default */].move(aLi[i], aPos[aLi[i].index]);
                                }
                            }
                            objBox.index = m;
                        }
                    };
                    document.onmouseup = function () {
                        document.onmousemove = document.onmouseup = null;

                        //抓着的对象回自个位置
                        __WEBPACK_IMPORTED_MODULE_2__lib_move__["a" /* default */].move(objBox, aPos[objBox.index]);
                        vm.dragStage(objBox.getElementsByTagName('header')[0].getAttribute("data-id"), objBox.index);
                    };
                    return false;
                };
            }
            function findNearest(obj) {
                var minDis = 99999999;
                var minIndex = -1;
                for (var i = 0; i < aLi.length; i++) {
                    //if(obj==aLi[i]) continue;
                    if (collTest(obj, aLi[i])) {
                        //撞到的房子
                        //还要找最近

                        var dis = getDis(obj, aLi[i]); //取出来的也是obj1到房子的距离
                        if (dis < minDis) {
                            minDis = dis;
                            minIndex = i;
                        }
                    }
                }
                if (minIndex == -1) {
                    return null;
                } else {
                    return aLi[minIndex]; //就返房客出去
                }
            }
            function getDis(obj1, obj2) {
                var a = aPos[obj2.index].left - obj1.offsetLeft;
                var b = aPos[obj2.index].top - obj1.offsetTop;
                return Math.sqrt(a * a + b * b);
            }
            function collTest(obj1, obj2) {
                //obj1对象和obj2的位置（房子)撞
                var l1 = obj1.offsetLeft;
                var t1 = obj1.offsetTop;
                var r1 = obj1.offsetLeft + obj1.offsetWidth;
                var b1 = obj1.offsetTop + obj1.offsetHeight;

                var l2 = aPos[obj2.index].left;
                var t2 = aPos[obj2.index].top;
                var r2 = aPos[obj2.index].left + obj2.offsetWidth;
                var b2 = aPos[obj2.index].top + obj2.offsetHeight;

                if (l1 > r2 || t1 > b2 || r1 < l2 || b1 < t2) {
                    return false;
                } else {
                    return true;
                }
            }
        },
        dragStage(stage, index) {
            var num = 0;
            if (index == this.stageList.length) {
                //移到最后
                num = 1;
            }
            if (index == 0) {
                //移到第一个
                num = 2;
            }
            this.http.zsyPutHttp('/stage/move', {
                id: stage,
                name: '移动专用' + Math.floor(Math.random() * 1000),
                sort: index,
                num: num
            }, res => {});
        }
    },
    created() {
        this.getData();
    },
    beforeMount() {
        let vm = this;
        if (this.loginUserRole == 0) {
            this.cursor = 'move';
        }
        this.$root.eventBus.$on("reloadBoard", () => {
            vm.getData();
        });
    },
    mounted() {
        setTimeout(() => {
            this.drugList();
        }, 400);
    },
    beforeDestroy() {}
});

/***/ }),
/* 251 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
/* harmony default export */ __webpack_exports__["a"] = ({
    //去空格
    getStyle: function (obj, attr) {
        return obj.currentStyle ? obj.currentStyle[attr] : getComputedStyle(obj, false)[attr];
    },
    //解Token
    move: function (obj, json, optional) {

        optional = optional || {};
        optional.time = optional.time || 300;
        optional.fn = optional.fn || null;
        optional.type = optional.type || 'ease-out';

        var start = {};
        var dis = {};
        for (var key in json) {
            start[key] = parseFloat(this.getStyle(obj, key)); //初始
            dis[key] = json[key] - start[key]; //运动距离{width:100,height:450}
        }
        var count = Math.round(optional.time / 30); //总次数
        var n = 0; //第几次

        clearInterval(obj.timer);
        obj.timer = setInterval(function () {
            n++; //递增
            for (var key in json) {
                //计算每个属性并修改
                switch (optional.type) {
                    case 'linear':
                        var a = n / count;
                        var cur = start[key] + dis[key] * a;
                        break;
                    case 'ease-in':
                        //加速
                        var a = n / count;
                        var cur = start[key] + dis[key] * a * a * a;
                        break;
                    case 'ease-out':
                        var a = 1 - n / count;
                        var cur = start[key] + dis[key] * (1 - a * a * a);
                        break;
                }
                if (key == 'opacity') {
                    obj.style.opacity = cur;
                    obj.style.filter = 'alpha(opacity=' + cur * 100 + ')';
                } else {
                    obj.style[key] = cur + 'px';
                }
            }
            if (n == count) {
                //停止条件
                clearInterval(obj.timer);
                optional.fn && optional.fn();
            }
        }, 30);
    }

});

/***/ }),
/* 252 */
/***/ (function(module, __webpack_exports__, __webpack_require__) {

"use strict";
Object.defineProperty(__webpack_exports__, "__esModule", { value: true });
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_0_vue__ = __webpack_require__(5);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__App__ = __webpack_require__(184);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_1__App___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_1__App__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_2__router__ = __webpack_require__(181);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_element_ui__ = __webpack_require__(29);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_3_element_ui___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_3_element_ui__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_element_ui_lib_theme_default_index_css__ = __webpack_require__(182);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_4_element_ui_lib_theme_default_index_css___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_4_element_ui_lib_theme_default_index_css__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__assets_css_base_css__ = __webpack_require__(183);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_5__assets_css_base_css___default = __webpack_require__.n(__WEBPACK_IMPORTED_MODULE_5__assets_css_base_css__);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_6__lib_Http__ = __webpack_require__(3);
/* harmony import */ var __WEBPACK_IMPORTED_MODULE_7__lib_Filter__ = __webpack_require__(180);
// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.



// element-ui


// common css


__WEBPACK_IMPORTED_MODULE_0_vue__["default"].use(__WEBPACK_IMPORTED_MODULE_3_element_ui___default.a);
__WEBPACK_IMPORTED_MODULE_0_vue__["default"].config.productionTip = false;


__WEBPACK_IMPORTED_MODULE_0_vue__["default"].prototype.http = __WEBPACK_IMPORTED_MODULE_6__lib_Http__["a" /* default */];


Object.keys(__WEBPACK_IMPORTED_MODULE_7__lib_Filter__).forEach(k => __WEBPACK_IMPORTED_MODULE_0_vue__["default"].filter(k, __WEBPACK_IMPORTED_MODULE_7__lib_Filter__[k])); // 注册过滤器


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
/* 253 */,
/* 254 */,
/* 255 */,
/* 256 */,
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
/* 278 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 279 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 280 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 281 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 282 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 283 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 284 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 285 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 286 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 287 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 288 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 289 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 290 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 291 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 292 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 293 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 294 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 295 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 296 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 297 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 298 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 299 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 300 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 301 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 302 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 303 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 304 */
/***/ (function(module, exports) {

// removed by extract-text-webpack-plugin

/***/ }),
/* 305 */,
/* 306 */,
/* 307 */,
/* 308 */
/***/ (function(module, exports, __webpack_require__) {

var map = {
	"./af": 46,
	"./af.js": 46,
	"./ar": 53,
	"./ar-dz": 47,
	"./ar-dz.js": 47,
	"./ar-kw": 48,
	"./ar-kw.js": 48,
	"./ar-ly": 49,
	"./ar-ly.js": 49,
	"./ar-ma": 50,
	"./ar-ma.js": 50,
	"./ar-sa": 51,
	"./ar-sa.js": 51,
	"./ar-tn": 52,
	"./ar-tn.js": 52,
	"./ar.js": 53,
	"./az": 54,
	"./az.js": 54,
	"./be": 55,
	"./be.js": 55,
	"./bg": 56,
	"./bg.js": 56,
	"./bm": 57,
	"./bm.js": 57,
	"./bn": 58,
	"./bn.js": 58,
	"./bo": 59,
	"./bo.js": 59,
	"./br": 60,
	"./br.js": 60,
	"./bs": 61,
	"./bs.js": 61,
	"./ca": 62,
	"./ca.js": 62,
	"./cs": 63,
	"./cs.js": 63,
	"./cv": 64,
	"./cv.js": 64,
	"./cy": 65,
	"./cy.js": 65,
	"./da": 66,
	"./da.js": 66,
	"./de": 69,
	"./de-at": 67,
	"./de-at.js": 67,
	"./de-ch": 68,
	"./de-ch.js": 68,
	"./de.js": 69,
	"./dv": 70,
	"./dv.js": 70,
	"./el": 71,
	"./el.js": 71,
	"./en-au": 72,
	"./en-au.js": 72,
	"./en-ca": 73,
	"./en-ca.js": 73,
	"./en-gb": 74,
	"./en-gb.js": 74,
	"./en-ie": 75,
	"./en-ie.js": 75,
	"./en-nz": 76,
	"./en-nz.js": 76,
	"./eo": 77,
	"./eo.js": 77,
	"./es": 80,
	"./es-do": 78,
	"./es-do.js": 78,
	"./es-us": 79,
	"./es-us.js": 79,
	"./es.js": 80,
	"./et": 81,
	"./et.js": 81,
	"./eu": 82,
	"./eu.js": 82,
	"./fa": 83,
	"./fa.js": 83,
	"./fi": 84,
	"./fi.js": 84,
	"./fo": 85,
	"./fo.js": 85,
	"./fr": 88,
	"./fr-ca": 86,
	"./fr-ca.js": 86,
	"./fr-ch": 87,
	"./fr-ch.js": 87,
	"./fr.js": 88,
	"./fy": 89,
	"./fy.js": 89,
	"./gd": 90,
	"./gd.js": 90,
	"./gl": 91,
	"./gl.js": 91,
	"./gom-latn": 92,
	"./gom-latn.js": 92,
	"./gu": 93,
	"./gu.js": 93,
	"./he": 94,
	"./he.js": 94,
	"./hi": 95,
	"./hi.js": 95,
	"./hr": 96,
	"./hr.js": 96,
	"./hu": 97,
	"./hu.js": 97,
	"./hy-am": 98,
	"./hy-am.js": 98,
	"./id": 99,
	"./id.js": 99,
	"./is": 100,
	"./is.js": 100,
	"./it": 101,
	"./it.js": 101,
	"./ja": 102,
	"./ja.js": 102,
	"./jv": 103,
	"./jv.js": 103,
	"./ka": 104,
	"./ka.js": 104,
	"./kk": 105,
	"./kk.js": 105,
	"./km": 106,
	"./km.js": 106,
	"./kn": 107,
	"./kn.js": 107,
	"./ko": 108,
	"./ko.js": 108,
	"./ky": 109,
	"./ky.js": 109,
	"./lb": 110,
	"./lb.js": 110,
	"./lo": 111,
	"./lo.js": 111,
	"./lt": 112,
	"./lt.js": 112,
	"./lv": 113,
	"./lv.js": 113,
	"./me": 114,
	"./me.js": 114,
	"./mi": 115,
	"./mi.js": 115,
	"./mk": 116,
	"./mk.js": 116,
	"./ml": 117,
	"./ml.js": 117,
	"./mr": 118,
	"./mr.js": 118,
	"./ms": 120,
	"./ms-my": 119,
	"./ms-my.js": 119,
	"./ms.js": 120,
	"./my": 121,
	"./my.js": 121,
	"./nb": 122,
	"./nb.js": 122,
	"./ne": 123,
	"./ne.js": 123,
	"./nl": 125,
	"./nl-be": 124,
	"./nl-be.js": 124,
	"./nl.js": 125,
	"./nn": 126,
	"./nn.js": 126,
	"./pa-in": 127,
	"./pa-in.js": 127,
	"./pl": 128,
	"./pl.js": 128,
	"./pt": 130,
	"./pt-br": 129,
	"./pt-br.js": 129,
	"./pt.js": 130,
	"./ro": 131,
	"./ro.js": 131,
	"./ru": 132,
	"./ru.js": 132,
	"./sd": 133,
	"./sd.js": 133,
	"./se": 134,
	"./se.js": 134,
	"./si": 135,
	"./si.js": 135,
	"./sk": 136,
	"./sk.js": 136,
	"./sl": 137,
	"./sl.js": 137,
	"./sq": 138,
	"./sq.js": 138,
	"./sr": 140,
	"./sr-cyrl": 139,
	"./sr-cyrl.js": 139,
	"./sr.js": 140,
	"./ss": 141,
	"./ss.js": 141,
	"./sv": 142,
	"./sv.js": 142,
	"./sw": 143,
	"./sw.js": 143,
	"./ta": 144,
	"./ta.js": 144,
	"./te": 145,
	"./te.js": 145,
	"./tet": 146,
	"./tet.js": 146,
	"./th": 147,
	"./th.js": 147,
	"./tl-ph": 148,
	"./tl-ph.js": 148,
	"./tlh": 149,
	"./tlh.js": 149,
	"./tr": 150,
	"./tr.js": 150,
	"./tzl": 151,
	"./tzl.js": 151,
	"./tzm": 153,
	"./tzm-latn": 152,
	"./tzm-latn.js": 152,
	"./tzm.js": 153,
	"./uk": 154,
	"./uk.js": 154,
	"./ur": 155,
	"./ur.js": 155,
	"./uz": 157,
	"./uz-latn": 156,
	"./uz-latn.js": 156,
	"./uz.js": 157,
	"./vi": 158,
	"./vi.js": 158,
	"./x-pseudo": 159,
	"./x-pseudo.js": 159,
	"./yo": 160,
	"./yo.js": 160,
	"./zh-cn": 161,
	"./zh-cn.js": 161,
	"./zh-hk": 162,
	"./zh-hk.js": 162,
	"./zh-tw": 163,
	"./zh-tw.js": 163
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
webpackContext.id = 308;

/***/ }),
/* 309 */,
/* 310 */,
/* 311 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/doing.ffc2844.png";

/***/ }),
/* 312 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/finished.2d5caeb.jpg";

/***/ }),
/* 313 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/icon_1.a62f59b.png";

/***/ }),
/* 314 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/icon_2.df92254.png";

/***/ }),
/* 315 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/icon_3.3612886.png";

/***/ }),
/* 316 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/icon_4.1697209.png";

/***/ }),
/* 317 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/icon_5.9d627a4.png";

/***/ }),
/* 318 */
/***/ (function(module, exports) {

module.exports = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQCAYAAACOEfKtAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyhpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMDY3IDc5LjE1Nzc0NywgMjAxNS8wMy8zMC0yMzo0MDo0MiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTUgKE1hY2ludG9zaCkiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6RjY3NjFGMzM1REMzMTFFNzk5NTFGNEQ0MTRGRTI2M0UiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6RjY3NjFGMzQ1REMzMTFFNzk5NTFGNEQ0MTRGRTI2M0UiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDpFODMyMEQwQTVEQzMxMUU3OTk1MUY0RDQxNEZFMjYzRSIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDpGNjc2MUYzMjVEQzMxMUU3OTk1MUY0RDQxNEZFMjYzRSIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PsAJSUQAABBqSURBVHja7F0JdFPXtd225NmSbWw8zzYewMaAmacAgVDaAEkzLNImIeEnv5/2d8ic/J+/mja0azUrJeknSZP+pElWJkKA0kCYyhDCFBsbzwOeB9mWbUmeZUu25H/ufdbwJNmxwTFuo8N6S3rvvnffffudc/Y5515kl+HhYVhLUVHRbPp4hLZNtMXR5oHvtuhoq6HtOG1vp6enl1o3upgAJODc6WO3VCrdGRQU5CqTyeDh4QEXF5fvNHoMH51Oh56eHqhUKuPQ0ND/0eHHCMh+M4Aj4H0hl8vXR0ZGQiKRwCn2YjAY0NTUhK6urjPMQglEvetI2ysMvJiYGCd4YwjDJjo6GoTVOmatXAMLCwvnkNkWJCUlSZzgjV8TKyoqDGTOGUwDHwkMDHSCN0FNJJ5ggD3CAPweqaQTlQkKI1mGHQMwnrGtUyYmI5jFMwDdv+uhyvXICGburk4obkycADoBdALoBNAJoFOcAN4skU5mZ6reRlys3osa1RUMGfXw9wxFbNBCJAUvRtSMNLjQv6r2HDqvAV5ucsQEpiPAO/S679epbUWduhD9gz2Y6RuNhOBMGI1G1NOxqvYsGkcOenQquLl6IiV0NRbFbkWgT8T0BLClsxKf5v4PBo0DlgccUKKo+QT8vYIxUxaDoqYz+KrqfUswSgaQEfl9rEt+GBLX8efiQwY9zla8jwLFMQzDaD6+OvEhpEesg0arQHHLaRiMg/w4G1NR80mUKc9hW+YuhPnPmn4mfKH6ExF4Jtk0+5f05rfA290PJS1nRW3s4fMVR3C4aDeMw8Zxg3cgbxdd94UIPCblrefpPnIsjNmMW5N/Yn+tUYdLNfumnw80GIcw7ACA+MCFSA1bad7X6jocXl/ZdhEnS98kQIbHvA9rP1L8JzR0FDps7xloN3+fG3krIv3mOASRvYSbDiDTmIrWLBy4ugvtPfVYnrgNUld3czvzO2uTd4iumR2+btT+mIldrt4/5j2zag5xsEeT1NA1Vu7BBbfN+SmNyVIoYeNblXg/mrsr+bjrNcU3nhMXFhYOp6enT9h5f174Mlp7KuHrHoidt7zDjzd3XkNO/RG4STyRGXM7gsnvmfyjRtvCtbFQcRrlyvPo03dA5hmIUNksBMvj4OMRQP22IJrIxo98pq1o9V1o0JTA12MGenUaaPoUaO+tR2e/ElIXN8wJv5X86XoUNJ5EqN8s2uL5dcruWuTWfw69oR9LY+/i/o9p8utfbify6Ua4Xyq+n/YLIrOwCYNXVFQ0cQC7+tvxYdbT0A4K5ujnGYJ/X/WWQ3Orbc9Ddt3f0NhZhDsznkdi8MJvPawob72Ew4UvIco/nXzvHYifuYBro6385fx/oItIjom3WwC2L9tNLydgwgBOmIUvVe8zg8ekV68hczbA1cXCov2DvfQQL5OJ5JuPyb1mivrRDWnJ9BvIb6n4Q3pIvcc9hoHBPtSq8yHzCCTtjYG7xMvcxo4xYS+tMb8IcYGZ2Dz3CVH/jJ17dBZ/yZ6HPddts3/y7fvAqvavbQhkkLSyTXTsWPEeEXhMrB8gq/YQXvvyQXyS8yyBWGduGzQMjHlvU7unmw+ZZhW/fs/ZB0jLD5nPcSf3YS216lwcL3ndzorYS7eWa8TgU0IiA0M99o7UqhsWTFersuzOcZMIBFOnyqdY8D16gCG4uLhyX8lY8cOsZ/HqmW34KPs58m0NomvVfU344OunefvH2f/Fz18UvYVfz/o5V/keMXOJAKADTa4g4unQKi0P7WJvePpveHmTBmBMwDzRfnRABpFBEAeOmzQ5eFtxd/WCl7sw72LNfPFBi4k8/JHbcBQt3eUCEXWV4bOcX3M3YCKPT688D2VPBd9v6iqlAPoUfD0D+PUmaVAJoY3Mc4YoGjBJN7kKJuzlMPKKnbFA1B4XuGBqMpE75j+DYsVZdFOK5Okmw/yojcSORbQV45ak+xHhn0yMFklvXGG+Ji1ig9mRp0WsJRNq5d/XJj/EP0uVX4ru0Uc+KZ/YdFn8D4lBj/J9ayloOkGaSxlM0g5IyPdKGAtHrBnRLgllIxuR13jYfH6Qbywi/JIEx990msCbhy0ZTyKbXAnTvECfSAqxVk8NgMxhL6DBW0tZy3lcazvP06gZPuF4YMlLOF/1CYFagFkzl2F5wj08VgyifJXlolsynhBdzwCwlUZNIQfQUdDsNhLb+XsHY2vGUyLm1/Q1Y23SQ6SFbjwXjiGwVrIYlVyImkIflv7pB7VEXPOxataPbn4mwvxRZdslXjw4mPc7MrluTgrrU/4NO5b/Lw3yPspzpdzR16ryHfaxIuE+EYsLvrZ3hK37xAOm85YnbHPYT3VbLr6uOcDz6jVJD/L735qyg4+nd6ADByl4ZuOsVmWPO3WcdAAZYIMGnXm/racWeiNfZ4OO/ibsv/qiqJ2bpK6T56knSl8jX6S26zOBwpgHl76C1JA1ZiAlI47eFKIwokoJuQXbl77Kz3cUn54o3cMtgVVnbMOe/Xkv8uKGELZ0mX22id0Hp4pEmCkWN521GpxW1M6ykwvVe0XHCpvOcLbs1asJ4N/yGNBWWDnq9rm/Iq15nTt475Gg1ov8bAKRxY7lr1E89xi5gSgHDNrPCwwMGBZWlbdctCl07CXyqLF5qRa/eoWyJxaTTgmAbZQ+na18B1VtOSMBcpC9KbVni/brVLlWIUk9mdnBUftn9cG7FjxPee3KEe1cgjvmPct966iVoKq9vF9zccImVq1TXbW7xpfYmklJ8zlcqvmYUs3mqQFQ3dfI3/Khgt+TX/s7An0jOctZi8xDDKrepsxlHWR//u4uNNeV2fg5MtfQFfw7y2/ZvgiQshwc++AlS7BsA5DOxip83P1F+yGUfwf5ROFC5Sc4WvIKryR1aVunBsDeAY25lneu8l1crNyLTbN/Qb5KCGAlxH6rE+8XMWOoLFHUB6tG85iPgCu/cgJdauXo1Q4HqyY0bQoUXT6MNkX1CEDiHDZYFifaX02EwsbFMyKJDy8efFnxPi7XfWp5rlFKbZMOoG2QKgxiGA8te5VXhO/L/L254suCYFZQWBx3J/y9wjhBhMtTiCG3CxlC/oVRQRpLPLx8BVdRIpjqmuSHSasSeWYS6BONpfF3kxvJ5fdnwmJTNi42vgeW/pGYeAg5DYdsXtQUlfSD5bEosVEYRWcprwIvibvDJj68wOcm7s38DR5Z+Qafr7Au3SsqBVP28pFPEEAfQYNrhKwmVB5HLP6yqFZ5pGg35oStxbyo2/gx9lJNL5aV3GwliICfEg1MDV3FUzNr8SYfM0DMaltRLm+9wAPhryo/4pmI7byHWlkrmL1UHEgra8tw5C8v4KNdj/JPZf01m9xbUBdVc63DMZ4u/ytPCcuVF+yKwCxOdbMpOHhKZUgNXzU1ALLc9fa5T/KKMzPJxTF3IzlkORSaUtSrCkTJeUuX8ODZ9fvprR8WE8uAFrp++8JEfWkOPv3DT1GVexbtjRX8cx/tKyosfUvdhUykr1tlX7Wu/RufZzHlzQajpepS034VzZ0VSAtfi0UxP+TjZ8qwOf0JHi5NiQkLoUUmfrbmPT44VloSwps6FDQew7ZFv+OhyBAF09aTPqxiwoBmiTyPH/t7LbFknwXIrz57w25+xWgYwvkDb+K+5/4snK/tFoL6QR1tekjd3EfmRDQU831opXFDtA2CsmUeOJ8sewMLojbzwi7LVJivZLm0m+T610dedyrHzMAEHmfGXgUPlD/MeorPy7JZOEYclocxmCsidlMEqhYBEP0A1M3VDs9prSs1A6tutWQRhqFBq4qLuM4X4B3Bx8k07+PsZ9Cn16DDKt7zpBTvRsCblFzYDEK/0lwv/CzvBV5NWZFgSdbZJHuoPN7htQ2VeSO+0B0SN3fHL8zdi7MsLzRU5DkmOFkMn6MxCZtAyms4jgP5lP0YtCPzOc2YTJk0AFk6ZY79SFP+Uf4GnwDaOvc5pIWtx92ZvxbFWj5yS+xWW3IZ2r4uuLi6InnRBof9Jy/dKMRrnWo0VgqBsyuRkomRhbCpG/cu/A3mRf6A39dT6oNT194UV7WNuukJoKMJmezag0gKWYJNaf/JZ71Ol79tzoMlEjfMCIkdMUM9cs8c4N9vuedniEgSFwuiZy/G6ruE+Yrs0/u4T2QSGJYgKhiw/lltb0Pqo/y+tsRlyq0nUyZtaUdUwFy7eRBVX4OoCMHKSGzyxlRIjUtbBk1rncCeJz9AYvpyhMWm4J7Hd6OFQpmu9mb4h0QiNCaZR7r11/IIaEuhIiFtuahgwPpn9UBT3qyyyo9NEuE/Z3pq4NyItZQmeY8anOYrTgpBLGUAjGR4nrviB2a/xrRq357HUVeey8EKi5+NlCXrEUqAsv3Kgos4+OenzUTCzDd9hVDYrWzNNlegC0buw0nEK9wu3psXtWFSAZTs3LnzhZCQkBvuyF3qhXD/VFS3ZfOiZXLwKj5NyI6z4sMXlBkYhgXTq1MXID1iPeR+M9Hb1YHWhnKzKZdkHYeyoZJAc6U4sReKmhKcO/QWLh9712y6TBau/zFSM9egu1+F/VdfoL4FNmYT7gtjtvICRGxgBrVroCZLYHO/W+c947Acdr3S1tZ2fSsTvqngygiFhTHmzKKrCh9kPyk6b0HUFl4t7tf24N0XtxOQ7eO+h39QJB7+77/CzcMLR4v3oKTltKj9gcV/RKhfgqigy0IuU0FhsoRNrE/6Aks292ANnlCWty+gljSfEpy6twy3/eipCd1j44+f4eDxgkSr/VoZU8hinT1NNniT7gPHErmDtS4mc2aSmL4MKYs2jquvzHXbEJNsmVo1wmCvoV4hmCqZEgBZahcmTxang0FLRPvr7/05vH3HXpsinxGGVZt32PSzWLTPlrQ5Wpz0bYl0qm5057xn+YQ4849sNdX86O+JKzo+fli55VGc/PilUftYumk73D3ElaB1yTs42zJNZMs6MiI3YCpl0knkRkQ3oMWfHh/dlH/+8lHuM6eLfCskciPi4ekNTy/HxVWZf+i0Am9KfeDEtLDPccAqlWI6yrQCkP1X+uFhg8O2QV2/E8BvDML1o68O0PZ2wPY3bpwA2oLU1z1qG8uBtT0dTgDHkm51y5jtmlaFE8CxpL2pdsx2lbLOCeBYYirtjybKuvJpB+BNjQ0MhkF0d6jQo2lFp1qJ+rIrY55fmX8OkYkZlNKF0BbMP2/2791MKYA9ne2oLcvh1eaWujIiBQ28ZTPg5SuHp68f0pZugnQkVfNgn64SDBuHoNcJ7Kzv70VV8SX093bS1kVbB3zkQQiPm01bGmJS5kMeEPyvBeCAtg/K+jJ0qJoJDCMCQ6MRlZAOGT2odJQZuIn2361R0taKstyzwj1CohESPWtKwJxWufA/m0y7XPifUZwAOgF0AugE0AmgU5wA3kwA9dOxzjbdha33JullANaw30l2ysREr+e//FHPADze3d3tRGSieX0PX5Z8igH4jlqtNrD5CKeMTxhWKpWKAfa2K+XBxUNDQ28pFAonMuMUhhXDjGFnYuHHyIxP1dfXw6mJY2teQ0MDCCv2U/CPsWPOP0bwDTKuP0ZgLQQkWwP7KG1s8QpbVu/2HVc8NtNfxwgDwp/DEP1e1P8LMAC90ygUQSh4VgAAAABJRU5ErkJggg=="

/***/ }),
/* 319 */
/***/ (function(module, exports, __webpack_require__) {

module.exports = __webpack_require__.p + "static/img/waitAssess.9cdd270.png";

/***/ }),
/* 320 */
/***/ (function(module, exports, __webpack_require__) {

var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(226),
  /* template */
  __webpack_require__(340),
  /* styles */
  null,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 321 */
/***/ (function(module, exports, __webpack_require__) {

var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(227),
  /* template */
  __webpack_require__(355),
  /* styles */
  null,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 322 */
/***/ (function(module, exports, __webpack_require__) {

var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(228),
  /* template */
  __webpack_require__(341),
  /* styles */
  null,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 323 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(280)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(232),
  /* template */
  __webpack_require__(335),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-0f1dfbc3",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 324 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(284)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(233),
  /* template */
  __webpack_require__(339),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-256e98ea",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 325 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(304)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(234),
  /* template */
  __webpack_require__(358),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-f9a39a7e",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 326 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(290)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(236),
  /* template */
  __webpack_require__(348),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-6573cde1",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 327 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(297)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(237),
  /* template */
  __webpack_require__(356),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-c38d3b7c",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 328 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(293)
  __webpack_require__(292)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(240),
  /* template */
  __webpack_require__(350),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-67e10559",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 329 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(281)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(241),
  /* template */
  __webpack_require__(336),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-176c51b4",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 330 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(278)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(248),
  /* template */
  __webpack_require__(333),
  /* styles */
  injectStyle,
  /* scopeId */
  null,
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 331 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(294)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(249),
  /* template */
  __webpack_require__(351),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-72631b1f",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 332 */
/***/ (function(module, exports, __webpack_require__) {

function injectStyle (ssrContext) {
  __webpack_require__(283)
}
var Component = __webpack_require__(1)(
  /* script */
  __webpack_require__(250),
  /* template */
  __webpack_require__(338),
  /* styles */
  injectStyle,
  /* scopeId */
  "data-v-2346b0e8",
  /* moduleIdentifier (server only) */
  null
)

module.exports = Component.exports


/***/ }),
/* 333 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('el-dialog', {
    attrs: {
      "title": "上传头像",
      "visible": _vm.showUploadAvatarDialog,
      "size": "tiny",
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "before-close": _vm.hide
    },
    on: {
      "update:visible": function($event) {
        _vm.showUploadAvatarDialog = $event
      }
    }
  }, [_c('el-upload', {
    staticClass: "avatar-uploader",
    attrs: {
      "action": "",
      "show-file-list": false,
      "http-request": _vm.upload,
      "before-upload": _vm.beforeAvatarUpload
    }
  }, [(_vm.imageUrl) ? _c('img', {
    staticClass: "avatar",
    attrs: {
      "src": _vm.imageUrl
    }
  }) : _c('i', {
    staticClass: "el-icon-plus avatar-uploader-icon"
  })]), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    attrs: {
      "slot": "footer"
    },
    slot: "footer"
  }, [_c('el-button', {
    on: {
      "click": _vm.hide
    }
  }, [_vm._v("取 消")]), _vm._v(" "), _c('el-button', {
    attrs: {
      "type": "primary"
    },
    on: {
      "click": _vm.saveAvatar
    }
  }, [_vm._v("确 定")])], 1)], 1)
},staticRenderFns: []}

/***/ }),
/* 334 */
/***/ (function(module, exports) {

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

/***/ }),
/* 335 */
/***/ (function(module, exports) {

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
        _vm.$set(_vm.addForm, "name", $$v)
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
        _vm.$set(_vm.addForm, "parentId", $$v)
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

/***/ }),
/* 336 */
/***/ (function(module, exports) {

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
        _vm.$set(_vm.modifyForm, "name", $$v)
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
        _vm.$set(_vm.modifyForm, "account", $$v)
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
        _vm.$set(_vm.modifyForm, "jobName", $$v)
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
        _vm.$set(_vm.modifyForm, "phone", $$v)
      },
      expression: "modifyForm.phone"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("邮箱地址")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-input', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请输入邮箱地址"
    },
    model: {
      value: (_vm.modifyForm.email),
      callback: function($$v) {
        _vm.$set(_vm.modifyForm, "email", $$v)
      },
      expression: "modifyForm.email"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("所属部门")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-select', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请选择部门"
    },
    model: {
      value: (_vm.modifyForm.departmentId),
      callback: function($$v) {
        _vm.$set(_vm.modifyForm, "departmentId", $$v)
      },
      expression: "modifyForm.departmentId"
    }
  }, _vm._l((_vm.deptOptions), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.label,
        "value": item.id
      }
    })
  }))], 1)]), _vm._v(" "), _c('div', {
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
        _vm.$set(_vm.modifyForm, "userRole", $$v)
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
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("用户状态")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-select', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请选择状态"
    },
    model: {
      value: (_vm.modifyForm.status),
      callback: function($$v) {
        _vm.$set(_vm.modifyForm, "status", $$v)
      },
      expression: "modifyForm.status"
    }
  }, _vm._l((_vm.statusOptions), function(item) {
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

/***/ }),
/* 337 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "calculate-con"
  }, [_c('div', {
    staticClass: "calculate-con-top clearfix"
  }, [_c('div', {
    staticClass: "clearfix select-box"
  }, [_c('div', {
    staticClass: "calculate-top-list fl"
  }, [_c('el-input', {
    model: {
      value: (_vm.queryForm.datumIntegral),
      callback: function($$v) {
        _vm.$set(_vm.queryForm, "datumIntegral", $$v)
      },
      expression: "queryForm.datumIntegral"
    }
  }, [_c('template', {
    attrs: {
      "slot": "prepend"
    },
    slot: "prepend"
  }, [_vm._v("基准积分:")])], 2)], 1), _vm._v(" "), _c('div', {
    staticClass: "calculate-top-list fl"
  }, [_c('el-input', {
    model: {
      value: (_vm.queryForm.bonus),
      callback: function($$v) {
        _vm.$set(_vm.queryForm, "bonus", $$v)
      },
      expression: "queryForm.bonus"
    }
  }, [_c('template', {
    attrs: {
      "slot": "prepend"
    },
    slot: "prepend"
  }, [_vm._v("绩效奖金池:")])], 2)], 1), _vm._v(" "), _c('div', {
    staticClass: "calculate-top-list fl"
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "placeholder": "选择开始日期"
    },
    model: {
      value: (_vm.queryForm.startTime),
      callback: function($$v) {
        _vm.$set(_vm.queryForm, "startTime", $$v)
      },
      expression: "queryForm.startTime"
    }
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "calculate-top-list fl"
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "placeholder": "选择结束日期"
    },
    model: {
      value: (_vm.queryForm.endTime),
      callback: function($$v) {
        _vm.$set(_vm.queryForm, "endTime", $$v)
      },
      expression: "queryForm.endTime"
    }
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "calculate-top-list fl",
    staticStyle: {
      "width": "50px"
    }
  }, [_c('img', {
    staticClass: "search-btn",
    attrs: {
      "src": __webpack_require__(11),
      "alt": ""
    },
    on: {
      "click": function($event) {
        _vm.calculateIntegral()
      }
    }
  })])])]), _vm._v(" "), _c('div', {
    staticClass: "calculate-data-detail"
  }, [_c('el-table', {
    attrs: {
      "data": _vm.calculate,
      "summary-method": _vm.getSummaries,
      "show-summary": ""
    }
  }, [_c('el-table-column', {
    attrs: {
      "prop": "id",
      "label": "序号",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "name",
      "label": "成员",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "integral",
      "label": "绩效积分",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "bonus",
      "label": "奖金",
      "align": "center"
    }
  })], 1)], 1)])
},staticRenderFns: []}

/***/ }),
/* 338 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "task-board"
  }, [_c('div', {
    staticClass: "task-board-list clearfix",
    style: ({
      'width': _vm.taskBoxWidth
    }),
    attrs: {
      "id": "task-board-list"
    }
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
      style: ('cursor: ' + _vm.cursor),
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
        class: task.borderClass,
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
        directives: [{
          name: "show",
          rawName: "v-show",
          value: (task.examine == 1),
          expression: "task.examine==1"
        }],
        staticClass: "trans"
      }), _vm._v(" "), _c('div', {
        staticClass: "fl complate",
        attrs: {
          "data-title": ""
        }
      }), _vm._v(" "), _c('div', {
        staticClass: "fl task-name"
      }, [_c('div', {
        staticStyle: {
          "font-size": "16px",
          "padding": "12px 0"
        }
      }, [_vm._v(_vm._s(task.name))]), _vm._v(" "), (item.name == '已发布' && task.status == 3) ? _c('div', [_c('span', {
        staticClass: "tips purple"
      }, [_vm._v(_vm._s(task.endText))])]) : _c('div', [_c('span', {
        staticClass: "tips",
        class: task.endColor
      }, [_vm._v(_vm._s(task.endText))])]), _vm._v(" "), _c('div', {
        staticStyle: {
          "border-top": "5px",
          "padding": "2px"
        }
      }, [(task.status == 1 && task.delayNo != 0) ? _c('span', {
        staticClass: "tips orange"
      }, [_vm._v("超时人数:" + _vm._s(task.delayNo))]) : _vm._e()])]), _vm._v(" "), _c('div', {
        staticClass: "master-info fr ellipsis"
      }, [(task.avatarUrl && task.avatarUrl != '') ? _c('img', {
        attrs: {
          "src": task.avatarUrl,
          "alt": task.userName
        }
      }) : _c('span', [_vm._v(_vm._s(task.userName))])])])
    }))])
  }))])
},staticRenderFns: []}

/***/ }),
/* 339 */
/***/ (function(module, exports) {

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
        _vm.$set(_vm.addForm, "name", $$v)
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
        _vm.$set(_vm.addForm, "account", $$v)
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
        _vm.$set(_vm.addForm, "jobName", $$v)
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
        _vm.$set(_vm.addForm, "phone", $$v)
      },
      expression: "addForm.phone"
    }
  })], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ftp-list clearfix"
  }, [_c('div', {
    staticClass: "ftp-menus fl"
  }, [_vm._v("邮箱地址")]), _vm._v(" "), _c('div', {
    staticClass: "ftp-msg fl"
  }, [_c('el-input', {
    staticClass: "w280",
    attrs: {
      "placeholder": "请输入邮箱地址"
    },
    model: {
      value: (_vm.addForm.email),
      callback: function($$v) {
        _vm.$set(_vm.addForm, "email", $$v)
      },
      expression: "addForm.email"
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
        _vm.$set(_vm.addForm, "userRole", $$v)
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

/***/ }),
/* 340 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('label', {
    staticClass: "el-checkbox"
  }, [_c('span', {
    staticClass: "el-checkbox__input",
    class: {
      'is-disabled': _vm.disabled,
      'is-checked': _vm.isChecked,
      'is-indeterminate': _vm.indeterminate,
      'is-focus': _vm.focus
    }
  }, [_c('span', {
    staticClass: "el-checkbox__inner"
  }), _vm._v(" "), (_vm.trueLabel || _vm.falseLabel) ? _c('input', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.model),
      expression: "model"
    }],
    staticClass: "el-checkbox__original",
    attrs: {
      "type": "checkbox",
      "name": _vm.name,
      "disabled": _vm.disabled,
      "true-value": _vm.trueLabel,
      "false-value": _vm.falseLabel
    },
    domProps: {
      "checked": Array.isArray(_vm.model) ? _vm._i(_vm.model, null) > -1 : _vm._q(_vm.model, _vm.trueLabel)
    },
    on: {
      "change": [function($event) {
        var $$a = _vm.model,
          $$el = $event.target,
          $$c = $$el.checked ? (_vm.trueLabel) : (_vm.falseLabel);
        if (Array.isArray($$a)) {
          var $$v = null,
            $$i = _vm._i($$a, $$v);
          if ($$el.checked) {
            $$i < 0 && (_vm.model = $$a.concat([$$v]))
          } else {
            $$i > -1 && (_vm.model = $$a.slice(0, $$i).concat($$a.slice($$i + 1)))
          }
        } else {
          _vm.model = $$c
        }
      }, _vm.handleChange],
      "focus": function($event) {
        _vm.focus = true
      },
      "blur": function($event) {
        _vm.focus = false
      }
    }
  }) : _c('input', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.model),
      expression: "model"
    }],
    staticClass: "el-checkbox__original",
    attrs: {
      "type": "checkbox",
      "disabled": _vm.disabled,
      "name": _vm.name
    },
    domProps: {
      "value": _vm.label,
      "checked": Array.isArray(_vm.model) ? _vm._i(_vm.model, _vm.label) > -1 : (_vm.model)
    },
    on: {
      "change": [function($event) {
        var $$a = _vm.model,
          $$el = $event.target,
          $$c = $$el.checked ? (true) : (false);
        if (Array.isArray($$a)) {
          var $$v = _vm.label,
            $$i = _vm._i($$a, $$v);
          if ($$el.checked) {
            $$i < 0 && (_vm.model = $$a.concat([$$v]))
          } else {
            $$i > -1 && (_vm.model = $$a.slice(0, $$i).concat($$a.slice($$i + 1)))
          }
        } else {
          _vm.model = $$c
        }
      }, _vm.handleChange],
      "focus": function($event) {
        _vm.focus = true
      },
      "blur": function($event) {
        _vm.focus = false
      }
    }
  })]), _vm._v(" "), (_vm.$slots.default || _vm.label) ? _c('span', {
    staticClass: "el-checkbox__label"
  }, [_vm._t("default"), _vm._v(" "), (!_vm.$slots.default) ? [_vm._v(_vm._s(_vm.label))] : _vm._e()], 2) : _vm._e()])
},staticRenderFns: []}

/***/ }),
/* 341 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('form', {
    staticClass: "el-form",
    class: [
      _vm.labelPosition ? 'el-form--label-' + _vm.labelPosition : '',
      {
        'el-form--inline': _vm.inline
      }
    ]
  }, [_vm._t("default")], 2)
},staticRenderFns: []}

/***/ }),
/* 342 */
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
  }, _vm._l((_vm.integralItem), function(item, key) {
    return _c('div', {
      staticClass: "mic-item fl"
    }, [_c('div', {
      staticClass: "mic-item-title"
    }, [_c('img', {
      staticClass: "icon-score",
      attrs: {
        "src": ("" + (__webpack_require__(361)("./icon_" + (key+1) + ".png")))
      }
    }), _vm._v(_vm._s(item.label))]), _vm._v(" "), _c('div', {
      staticClass: "mic-item-title",
      staticStyle: {
        "font-size": "12px"
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
  }, [_vm._m(0), _vm._v(" "), _c('span', [_vm._v("创建个人任务")])]), _vm._v(" "), _c('div', {
    staticClass: "add-task help",
    on: {
      "click": function($event) {
        _vm.editHelpVisible = true
      }
    }
  }, [_vm._m(1), _vm._v(" "), _c('span', [_vm._v("创建积分转移")])]), _vm._v(" "), _c('div', {
    staticClass: "add-task leave",
    on: {
      "click": function($event) {
        _vm.editLeaveVisible = true
      }
    }
  }, [_vm._m(2), _vm._v(" "), _c('span', [_vm._v("请假申请")])]), _vm._v(" "), _c('p', {
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
      "viewType": 1,
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
      "viewType": 1,
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
        _vm.$set(_vm.finishedPage, "pageNum", $event)
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
      "viewType": 1,
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
      "viewType": 1,
      "tagList": _vm.tagList
    }
  })], 1)], 1)], 1), _vm._v(" "), _c('div', [_c('p', {
    staticClass: "mic-title"
  }, [_vm._v("我的积分转移")]), _vm._v(" "), _c('div', {
    staticClass: "my-task-detail"
  }, [_c('el-tabs', {
    on: {
      "tab-click": _vm.handleClick
    },
    model: {
      value: (_vm.activeHelpName),
      callback: function($$v) {
        _vm.activeHelpName = $$v
      },
      expression: "activeHelpName"
    }
  }, [_c('el-tab-pane', {
    attrs: {
      "label": "审核中",
      "name": "wait"
    }
  }, [_vm._l((_vm.review.wait), function(help) {
    return _c('div', {
      staticClass: "task-lis"
    }, [_c('div', {
      staticClass: "head-img"
    }, [_c('img', {
      attrs: {
        "src": __webpack_require__(12)
      }
    })]), _vm._v(" "), _c('div', {
      staticClass: "main-task-detail"
    }, [_c('div', {
      staticClass: "task-name"
    }, [_c('span', [_vm._v(_vm._s(help.description))])]), _vm._v(" "), _c('div', {
      staticClass: "task-state"
    }, [_c('span', {
      staticClass: "task-end blue"
    }, [_vm._v("求助目标：" + _vm._s(help.name.split(",")[0]))]), _vm._v(" "), _c('span', {
      staticClass: "task-end blue"
    }, [_vm._v(_vm._s(_vm._f("formatDate")(help.time)))]), _vm._v(" "), _c('span', {
      staticClass: "task-time-opt"
    }, [_c('i', {
      staticClass: "el-icon-edit",
      on: {
        "click": function($event) {
          _vm.editHelpDetail(help)
        }
      }
    })])])]), _vm._v(" "), _c('div', {
      staticClass: "task-data-show"
    }, [_c('span', {
      staticClass: "task-score"
    }, [_vm._v("求助转移：" + _vm._s(help.integral) + " 分")])]), _vm._v(" "), _c('div', {
      staticClass: "task-username"
    }, [(help.avatarUrl && help.avatarUrl != '') ? _c('img', {
      staticClass: "task-avatar",
      attrs: {
        "src": help.avatarUrl
      }
    }) : _c('span', [_vm._v(_vm._s(help.name.split(",")[1]))])])])
  }), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (this.review.wait.length > 0),
      expression: "this.review.wait.length>0"
    }],
    staticClass: "pagination"
  }, [_c('el-pagination', {
    attrs: {
      "current-page": _vm.waitPage.pageNum,
      "page-size": _vm.waitPage.pageSize,
      "layout": _vm.waitPageLayout,
      "total": _vm.waitPage.total
    },
    on: {
      "current-change": _vm.handleWaitPage,
      "update:currentPage": function($event) {
        _vm.$set(_vm.waitPage, "pageNum", $event)
      }
    }
  })], 1), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.review.wait.length == 0),
      expression: "review.wait.length==0"
    }],
    staticClass: "empty"
  }, [_c('h2', [_vm._v("暂无数据")])])], 2), _vm._v(" "), _c('el-tab-pane', {
    attrs: {
      "label": "完成审核",
      "name": "review"
    }
  }, [_vm._l((_vm.review.review), function(help) {
    return _c('div', {
      staticClass: "task-lis",
      on: {
        "click": function($event) {
          _vm.reviewDetail(help)
        }
      }
    }, [_c('div', {
      staticClass: "head-img"
    }, [_c('img', {
      attrs: {
        "src": __webpack_require__(25)
      }
    })]), _vm._v(" "), _c('div', {
      staticClass: "main-task-detail"
    }, [_c('div', {
      staticClass: "task-name"
    }, [_c('span', [_vm._v(_vm._s(help.description))])]), _vm._v(" "), _c('div', {
      staticClass: "task-state"
    }, [_c('span', {
      staticClass: "task-end blue"
    }, [_vm._v("求助目标：" + _vm._s(help.name.split(",")[0]))]), _vm._v(" "), _c('span', {
      staticClass: "task-end blue"
    }, [_vm._v(_vm._s(_vm._f("formatDate")(help.time)))]), _vm._v(" "), _c('span', {
      staticClass: "task-time-opt"
    }, [_c('i', {
      staticClass: "el-icon-circle-check"
    })])])]), _vm._v(" "), _c('div', {
      staticClass: "task-data-show"
    }, [_c('span', {
      staticClass: "task-score"
    }, [_vm._v("转移完成：-" + _vm._s(help.integral) + " 分")])]), _vm._v(" "), _c('div', {
      staticClass: "task-username"
    }, [(help.avatarUrl && help.avatarUrl != '') ? _c('img', {
      staticClass: "task-avatar",
      attrs: {
        "src": help.avatarUrl
      }
    }) : _c('span', [_vm._v(_vm._s(help.name.split(",")[1]))])])])
  }), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (this.review.review.length > 0),
      expression: "this.review.review.length>0"
    }],
    staticClass: "pagination"
  }, [_c('el-pagination', {
    attrs: {
      "current-page": _vm.reviewPage.pageNum,
      "page-size": _vm.reviewPage.pageSize,
      "layout": _vm.reviewPageLayout,
      "total": _vm.reviewPage.total
    },
    on: {
      "current-change": _vm.handleReviewPage,
      "update:currentPage": function($event) {
        _vm.$set(_vm.reviewPage, "pageNum", $event)
      }
    }
  })], 1), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.review.review.length == 0),
      expression: "review.review.length==0"
    }],
    staticClass: "empty"
  }, [_c('h2', [_vm._v("暂无数据")])])], 2)], 1)], 1)])]), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.userRole === 0),
      expression: "userRole===0"
    }]
  }, [_c('p', {
    staticClass: "mic-title"
  }, [_vm._v("个人任务审核")]), _vm._v(" "), _c('el-tabs', {
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
      "viewType": 1,
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
      "viewType": 1,
      "stageList": _vm.stageList,
      "viewType": 1,
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
        _vm.$set(_vm.auditSuccessPage, "pageNum", $event)
      }
    }
  })], 1)], 1)], 1), _vm._v(" "), _c('p', {
    staticClass: "mic-title"
  }, [_vm._v("积分求助转移")]), _vm._v(" "), _c('el-tabs', {
    on: {
      "tab-click": _vm.handleClick
    },
    model: {
      value: (_vm.auditHelpTabsActiveName),
      callback: function($$v) {
        _vm.auditHelpTabsActiveName = $$v
      },
      expression: "auditHelpTabsActiveName"
    }
  }, [_c('el-tab-pane', {
    attrs: {
      "label": "待审核",
      "name": "wait"
    }
  }, [_vm._l((_vm.review.wait), function(help) {
    return _c('div', {
      staticClass: "task-lis",
      on: {
        "click": function($event) {
          _vm.reviewDetail(help)
        }
      }
    }, [_c('div', {
      staticClass: "head-img"
    }, [_c('img', {
      attrs: {
        "src": __webpack_require__(12)
      }
    })]), _vm._v(" "), _c('div', {
      staticClass: "main-task-detail"
    }, [_c('div', {
      staticClass: "task-name"
    }, [_c('span', [_vm._v(_vm._s(help.description))])]), _vm._v(" "), _c('div', {
      staticClass: "task-state"
    }, [_c('span', {
      staticClass: "task-end blue"
    }, [_vm._v("求助目标：" + _vm._s(help.name.split(",")[0]))]), _vm._v(" "), _c('span', {
      staticClass: "task-end blue"
    }, [_vm._v(_vm._s(_vm._f("formatDate")(help.time)))]), _vm._v(" "), _c('span', {
      staticClass: "task-time-opt"
    }, [_c('i', {
      staticClass: "el-icon-edit"
    })])])]), _vm._v(" "), _c('div', {
      staticClass: "task-data-show"
    }, [_c('span', {
      staticClass: "task-score"
    }, [_vm._v("求助转移：" + _vm._s(help.integral) + " 分")])]), _vm._v(" "), _c('div', {
      staticClass: "task-username"
    }, [(help.avatarUrl && help.avatarUrl != '') ? _c('img', {
      staticClass: "task-avatar",
      attrs: {
        "src": help.avatarUrl
      }
    }) : _c('span', [_vm._v(_vm._s(help.name.split(",")[1]))])])])
  }), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (this.review.wait.length > 0),
      expression: "this.review.wait.length>0"
    }],
    staticClass: "pagination"
  }, [_c('el-pagination', {
    attrs: {
      "current-page": _vm.waitPage.pageNum,
      "page-size": _vm.waitPage.pageSize,
      "layout": _vm.waitPageLayout,
      "total": _vm.waitPage.total
    },
    on: {
      "current-change": _vm.handleWaitPage,
      "update:currentPage": function($event) {
        _vm.$set(_vm.waitPage, "pageNum", $event)
      }
    }
  })], 1), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.review.wait.length == 0),
      expression: "review.wait.length==0"
    }],
    staticClass: "empty"
  }, [_c('h2', [_vm._v("暂无数据")])])], 2), _vm._v(" "), _c('el-tab-pane', {
    attrs: {
      "label": "审核通过",
      "name": "completed"
    }
  }, [_vm._l((_vm.review.review), function(help) {
    return _c('div', {
      staticClass: "task-lis",
      on: {
        "click": function($event) {
          _vm.reviewDetail(help)
        }
      }
    }, [_c('div', {
      staticClass: "head-img"
    }, [_c('img', {
      attrs: {
        "src": __webpack_require__(12)
      }
    })]), _vm._v(" "), _c('div', {
      staticClass: "main-task-detail"
    }, [_c('div', {
      staticClass: "task-name"
    }, [_c('span', [_vm._v(_vm._s(help.description))])]), _vm._v(" "), _c('div', {
      staticClass: "task-state"
    }, [_c('span', {
      staticClass: "task-end blue"
    }, [_vm._v("求助目标：" + _vm._s(help.name.split(",")[0]))]), _vm._v(" "), _c('span', {
      staticClass: "task-end blue"
    }, [_vm._v(_vm._s(_vm._f("formatDate")(help.time)))]), _vm._v(" "), _c('span', {
      staticClass: "task-time-opt"
    }, [_c('i', {
      staticClass: "el-icon-circle-check"
    })])])]), _vm._v(" "), _c('div', {
      staticClass: "task-data-show"
    }, [_c('span', {
      staticClass: "task-score"
    }, [_vm._v("转移完成：-" + _vm._s(help.integral) + " 分")])]), _vm._v(" "), _c('div', {
      staticClass: "task-username"
    }, [(help.avatarUrl && help.avatarUrl != '') ? _c('img', {
      staticClass: "task-avatar",
      attrs: {
        "src": help.avatarUrl
      }
    }) : _c('span', [_vm._v(_vm._s(help.name.split(",")[1]))])])])
  }), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (this.review.review.length > 0),
      expression: "this.review.review.length>0"
    }],
    staticClass: "pagination"
  }, [_c('el-pagination', {
    attrs: {
      "current-page": _vm.reviewPage.pageNum,
      "page-size": _vm.reviewPage.pageSize,
      "layout": _vm.waitPageLayout,
      "total": _vm.reviewPage.total
    },
    on: {
      "current-change": _vm.handleReviewPage,
      "update:currentPage": function($event) {
        _vm.$set(_vm.reviewPage, "pageNum", $event)
      }
    }
  })], 1), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.review.review.length == 0),
      expression: "review.review.length==0"
    }],
    staticClass: "empty"
  }, [_c('h2', [_vm._v("暂无数据")])])], 2)], 1)], 1), _vm._v(" "), _c('div', [_c('p', {
    staticClass: "mic-title"
  }, [_vm._v(_vm._s(_vm.userRole != 0 ? '请假申请' : '请假申请审核'))]), _vm._v(" "), _c('div', {
    staticClass: "my-task-detail"
  }, [_c('el-tabs', {
    on: {
      "tab-click": _vm.handleClick
    },
    model: {
      value: (_vm.activeLeaveName),
      callback: function($$v) {
        _vm.activeLeaveName = $$v
      },
      expression: "activeLeaveName"
    }
  }, [_c('el-tab-pane', {
    attrs: {
      "label": _vm.userRole != 0 ? '审核中' : '待审核',
      "name": "wait"
    }
  }, [_vm._l((_vm.leaveList.wait), function(leave) {
    return _c('div', {
      staticClass: "task-lis",
      on: {
        "click": function($event) {
          _vm.editLeaveDetail(leave, 0)
        }
      }
    }, [_c('div', {
      staticClass: "head-img"
    }, [_c('img', {
      attrs: {
        "src": __webpack_require__(12)
      }
    })]), _vm._v(" "), _c('div', {
      staticClass: "main-task-detail"
    }, [_c('div', {
      staticClass: "task-name"
    }, [_c('span', [_vm._v(_vm._s(leave.description))])]), _vm._v(" "), _c('div', {
      staticClass: "task-state"
    }, [_c('span', {
      staticClass: "task-end blue"
    }, [_vm._v(_vm._s(_vm._f("formatDate")(leave.createTime)))]), _vm._v(" "), _c('span', {
      staticClass: "task-time-opt"
    }, [_c('i', {
      staticClass: "el-icon-edit"
    })])])]), _vm._v(" "), _c('div', {
      staticClass: "task-username"
    }, [(leave.avatarUrl && leave.avatarUrl != '') ? _c('img', {
      staticClass: "task-avatar",
      attrs: {
        "src": leave.avatarUrl
      }
    }) : _c('span', [_vm._v(_vm._s(leave.userName))])])])
  }), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (this.leaveList.wait.length > 0),
      expression: "this.leaveList.wait.length>0"
    }],
    staticClass: "pagination"
  }, [_c('el-pagination', {
    attrs: {
      "current-page": _vm.leaveWaitPage.pageNum,
      "page-size": _vm.leaveWaitPage.pageSize,
      "layout": _vm.leaveWaitPageLayout,
      "total": _vm.leaveWaitPage.total
    },
    on: {
      "current-change": _vm.handleLeaveWaitPage,
      "update:currentPage": function($event) {
        _vm.$set(_vm.leaveWaitPage, "pageNum", $event)
      }
    }
  })], 1), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.leaveList.wait.length == 0),
      expression: "leaveList.wait.length==0"
    }],
    staticClass: "empty"
  }, [_c('h2', [_vm._v("暂无数据")])])], 2), _vm._v(" "), _c('el-tab-pane', {
    attrs: {
      "label": _vm.userRole != 0 ? '完成审核' : '审核通过',
      "name": "review"
    }
  }, [_vm._l((_vm.leaveList.pass), function(leave) {
    return _c('div', {
      staticClass: "task-lis",
      on: {
        "click": function($event) {
          _vm.leaveDetail(leave)
        }
      }
    }, [_c('div', {
      staticClass: "head-img"
    }, [_c('img', {
      attrs: {
        "src": __webpack_require__(25)
      }
    })]), _vm._v(" "), _c('div', {
      staticClass: "main-task-detail"
    }, [_c('div', {
      staticClass: "task-name"
    }, [_c('span', [_vm._v(_vm._s(leave.description))])]), _vm._v(" "), _c('div', {
      staticClass: "task-state"
    }, [_c('span', {
      staticClass: "task-end blue"
    }, [_vm._v(_vm._s(_vm._f("formatDate")(leave.createTime)))]), _vm._v(" "), _c('span', {
      staticClass: "task-time-opt"
    }, [_c('i', {
      staticClass: "el-icon-circle-check"
    })])])]), _vm._v(" "), _c('div', {
      staticClass: "task-username"
    }, [(leave.avatarUrl && leave.avatarUrl != '') ? _c('img', {
      staticClass: "task-avatar",
      attrs: {
        "src": leave.avatarUrl
      }
    }) : _c('span', [_vm._v(_vm._s(leave.userName))])])])
  }), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (this.leaveList.pass.length > 0),
      expression: "this.leaveList.pass.length>0"
    }],
    staticClass: "pagination"
  }, [_c('el-pagination', {
    attrs: {
      "current-page": _vm.leavePassPage.pageNum,
      "page-size": _vm.leavePassPage.pageSize,
      "layout": _vm.leavePassPageLayout,
      "total": _vm.leavePassPage.total
    },
    on: {
      "current-change": _vm.handleLeavePassPage,
      "update:currentPage": function($event) {
        _vm.$set(_vm.leavePassPage, "pageNum", $event)
      }
    }
  })], 1), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.leaveList.pass.length == 0),
      expression: "leaveList.pass.length==0"
    }],
    staticClass: "empty"
  }, [_c('h2', [_vm._v("暂无数据")])])], 2)], 1)], 1)])]), _vm._v(" "), _c('el-dialog', {
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
        _vm.$set(_vm.taskForm, "projectId", $$v)
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
      "label": "开始日期",
      "prop": "beginTime"
    }
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "format": "yyyy-MM-dd",
      "placeholder": "选择日期时间"
    },
    model: {
      value: (_vm.taskForm.beginTime),
      callback: function($$v) {
        _vm.$set(_vm.taskForm, "beginTime", $$v)
      },
      expression: "taskForm.beginTime"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
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
        _vm.$set(_vm.taskForm, "endTime", $$v)
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
      "maxlength": 6
    },
    model: {
      value: (_vm.taskForm.taskHours),
      callback: function($$v) {
        _vm.$set(_vm.taskForm, "taskHours", $$v)
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
        _vm.$set(_vm.taskForm, "taskName", $$v)
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
        _vm.$set(_vm.taskForm, "description", $$v)
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
        _vm.$set(_vm.taskForm, "stageId", $$v)
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
        _vm.$set(_vm.taskForm, "tags", $$v)
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
    attrs: {
      "slot": "footer"
    },
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
  }, [_vm._v("取 消")])], 1)], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "积分求助转移",
      "size": "tiny",
      "custom-class": "myDialog",
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "visible": _vm.editHelpVisible,
      "show-close": false
    },
    on: {
      "update:visible": function($event) {
        _vm.editHelpVisible = $event
      }
    }
  }, [_c('el-form', {
    ref: "helpForm",
    attrs: {
      "model": _vm.helpForm,
      "rules": _vm.helpRules,
      "label-width": "80px"
    }
  }, [_c('el-form-item', {
    attrs: {
      "label": "任务详情",
      "prop": "description"
    }
  }, [_c('el-input', {
    attrs: {
      "type": "textarea",
      "rows": 3
    },
    model: {
      value: (_vm.helpForm.description),
      callback: function($$v) {
        _vm.$set(_vm.helpForm, "description", $$v)
      },
      expression: "helpForm.description"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "label": "求助目标",
      "prop": "userId"
    }
  }, [_c('el-select', {
    attrs: {
      "placeholder": "成员列表"
    },
    model: {
      value: (_vm.helpForm.userId),
      callback: function($$v) {
        _vm.$set(_vm.helpForm, "userId", $$v)
      },
      expression: "helpForm.userId"
    }
  }, _vm._l((_vm.userList), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('el-form-item', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.userRole == 0),
      expression: "userRole==0"
    }],
    attrs: {
      "label": "发起者",
      "prop": "name"
    }
  }, [_vm._v(_vm._s(_vm.helpForm.name))]), _vm._v(" "), _c('el-form-item', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.userRole == 0),
      expression: "userRole==0"
    }],
    attrs: {
      "label": "原求助目标",
      "prop": "username"
    }
  }, [_vm._v(_vm._s(_vm.helpForm.username))]), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "label": "任务积分",
      "prop": "integral"
    }
  }, [_c('el-input', {
    staticStyle: {
      "width": "100px"
    },
    attrs: {
      "type": "input"
    },
    model: {
      value: (_vm.helpForm.integral),
      callback: function($$v) {
        _vm.$set(_vm.helpForm, "integral", $$v)
      },
      expression: "helpForm.integral"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    staticStyle: {},
    attrs: {
      "label": "转移日期",
      "prop": "time"
    }
  }, [_c('el-date-picker', {
    attrs: {
      "type": "datetime",
      "format": "yyyy-MM-dd HH:mm",
      "placeholder": "选择日期时间"
    },
    model: {
      value: (_vm.helpForm.time),
      callback: function($$v) {
        _vm.$set(_vm.helpForm, "time", $$v)
      },
      expression: "helpForm.time"
    }
  })], 1)], 1), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    attrs: {
      "slot": "footer"
    },
    slot: "footer"
  }, [_c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (!_vm.editHelpDetailVisible),
      expression: "!editHelpDetailVisible"
    }],
    attrs: {
      "type": "primary"
    },
    on: {
      "click": function($event) {
        _vm.saveHelpInfo('helpForm')
      }
    }
  }, [_vm._v("立即创建")]), _vm._v(" "), _c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.editHelpDetailVisible),
      expression: "editHelpDetailVisible"
    }],
    attrs: {
      "type": "primary"
    },
    on: {
      "click": function($event) {
        _vm.saveHelpInfo('helpForm')
      }
    }
  }, [_vm._v("立即更新")]), _vm._v(" "), _c('el-button', {
    on: {
      "click": function($event) {
        _vm.editHelpVisible = false, _vm.clearHelpForm()
      }
    }
  }, [_vm._v("取 消")])], 1)], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "积分求助转移详情",
      "custom-class": "myDialog",
      "visible": _vm.helpDetailVisible,
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "top": "10%",
      "size": "tiny"
    },
    on: {
      "update:visible": function($event) {
        _vm.helpDetailVisible = $event
      }
    }
  }, [_c('el-form', [_c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "任务描述："
    }
  }, [_vm._v(_vm._s(_vm.helpDetail.description))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "发起者："
    }
  }, [_vm._v(_vm._s(_vm.helpDetail.name))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "求助目标："
    }
  }, [_vm._v(_vm._s(_vm.helpDetail.username))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "时间："
    }
  }, [_vm._v(_vm._s(_vm._f("formatDate")(_vm.helpDetail.time)))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "积分："
    }
  }, [_vm._v(_vm._s(_vm.helpDetail.integral) + "分")])], 1), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    attrs: {
      "slot": "footer"
    },
    slot: "footer"
  }, [_c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.userRole == 0 && _vm.auditHelpTabsActiveName == 'wait'),
      expression: "userRole==0&&auditHelpTabsActiveName=='wait'"
    }]
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
      "click": function($event) {
        _vm.deleteHelp(_vm.helpDetail.id)
      }
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
        _vm.editHelpDetail(_vm.helpDetail)
      }
    }
  })], 1), _vm._v(" "), _c('el-button', {
    attrs: {
      "type": "success"
    },
    on: {
      "click": function($event) {
        _vm.acceptHelpChange(_vm.helpDetail.id)
      }
    }
  }, [_vm._v("审核通过")])], 1)])], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "请假申请",
      "size": "tiny",
      "custom-class": "myDialog",
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "visible": _vm.editLeaveVisible,
      "show-close": false
    },
    on: {
      "update:visible": function($event) {
        _vm.editLeaveVisible = $event
      }
    }
  }, [_c('el-form', {
    ref: "leaveForm",
    attrs: {
      "model": _vm.leaveForm,
      "rules": _vm.leaveRules,
      "label-width": "110px"
    }
  }, [_c('el-form-item', {
    attrs: {
      "label": "请假原因",
      "prop": "description"
    }
  }, [_c('el-input', {
    attrs: {
      "type": "textarea",
      "rows": 3
    },
    model: {
      value: (_vm.leaveForm.description),
      callback: function($$v) {
        _vm.$set(_vm.leaveForm, "description", $$v)
      },
      expression: "leaveForm.description"
    }
  })], 1), _vm._v(" "), _c('span', {
    staticClass: "star",
    staticStyle: {
      "float": "left",
      "margin-top": "7px",
      "margin-right": "-8px",
      "margin-left": "8px"
    }
  }, [_vm._v("*")]), _c('el-form-item', {
    attrs: {
      "label": "请假开始日期",
      "prop": "beginTime"
    }
  }, [_c('el-date-picker', {
    attrs: {
      "type": "datetime",
      "format": "yyyy-MM-dd HH:00:00",
      "placeholder": "选择日期时间"
    },
    model: {
      value: (_vm.leaveForm.beginTime),
      callback: function($$v) {
        _vm.$set(_vm.leaveForm, "beginTime", $$v)
      },
      expression: "leaveForm.beginTime"
    }
  })], 1), _vm._v(" "), _c('span', {
    staticClass: "star",
    staticStyle: {
      "float": "left",
      "margin-top": "7px",
      "margin-right": "-8px",
      "margin-left": "8px"
    }
  }, [_vm._v("*")]), _c('el-form-item', {
    attrs: {
      "label": "请假结束日期",
      "prop": "endTime"
    }
  }, [_c('el-date-picker', {
    attrs: {
      "type": "datetime",
      "format": "yyyy-MM-dd HH:00:00",
      "placeholder": "选择日期时间"
    },
    model: {
      value: (_vm.leaveForm.endTime),
      callback: function($$v) {
        _vm.$set(_vm.leaveForm, "endTime", $$v)
      },
      expression: "leaveForm.endTime"
    }
  })], 1), _vm._v(" "), _c('span', {
    staticClass: "star",
    staticStyle: {
      "float": "left",
      "margin-top": "7px",
      "margin-right": "-8px",
      "margin-left": "8px"
    }
  }, [_vm._v("*")]), _c('el-form-item', {
    attrs: {
      "label": "请假类型",
      "prop": "type"
    }
  }, [_c('el-select', {
    attrs: {
      "placeholder": "请选择类型"
    },
    model: {
      value: (_vm.leaveForm.type),
      callback: function($$v) {
        _vm.$set(_vm.leaveForm, "type", $$v)
      },
      expression: "leaveForm.type"
    }
  }, _vm._l((_vm.leaveType), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('span', {
    staticClass: "star",
    staticStyle: {
      "float": "left",
      "margin-top": "7px",
      "margin-right": "-8px",
      "margin-left": "8px"
    }
  }, [_vm._v("*")]), _c('el-form-item', {
    attrs: {
      "label": "请假时长",
      "prop": "hours"
    }
  }, [_c('el-input', {
    staticStyle: {
      "width": "100px"
    },
    attrs: {
      "type": "input"
    },
    model: {
      value: (_vm.leaveForm.hours),
      callback: function($$v) {
        _vm.$set(_vm.leaveForm, "hours", $$v)
      },
      expression: "leaveForm.hours"
    }
  }), _vm._v(" 小时\n            ")], 1)], 1), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    attrs: {
      "slot": "footer"
    },
    slot: "footer"
  }, [_c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (!_vm.editLeaveDetailVisible),
      expression: "!editLeaveDetailVisible"
    }],
    attrs: {
      "type": "primary"
    },
    on: {
      "click": function($event) {
        _vm.saveLeaveInfo('leaveForm')
      }
    }
  }, [_vm._v("立即创建")]), _vm._v(" "), _c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.editLeaveDetailVisible),
      expression: "editLeaveDetailVisible"
    }],
    attrs: {
      "type": "primary"
    },
    on: {
      "click": function($event) {
        _vm.saveLeaveInfo('leaveForm')
      }
    }
  }, [_vm._v("立即更新")]), _vm._v(" "), _c('el-button', {
    on: {
      "click": function($event) {
        _vm.editLeaveVisible = false, _vm.editLeaveDetailVisible = false, _vm.clearLeaveForm()
      }
    }
  }, [_vm._v("取 消")])], 1)], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "请假申请详情",
      "visible": _vm.leaveDetailVisible,
      "custom-class": "myDialog",
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "top": "10%",
      "size": "tiny"
    },
    on: {
      "update:visible": function($event) {
        _vm.leaveDetailVisible = $event
      }
    }
  }, [_c('el-form', [_c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "请假原因："
    }
  }, [_vm._v(_vm._s(_vm.leaveForm.description))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "请假开始时间："
    }
  }, [_vm._v(_vm._s(_vm._f("formatTime")(_vm.leaveForm.beginTime)))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "请假结束时间："
    }
  }, [_vm._v(_vm._s(_vm._f("formatTime")(_vm.leaveForm.endTime)))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "类型："
    }
  }, [_vm._v(_vm._s(_vm.leaveForm.typeName))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "请假时长："
    }
  }, [_vm._v(_vm._s(_vm.leaveForm.hours) + "小时")])], 1), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    attrs: {
      "slot": "footer"
    },
    slot: "footer"
  }, [_c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.userRole == 0),
      expression: "userRole==0"
    }]
  }, [_c('el-tooltip', {
    attrs: {
      "content": "删除该申请",
      "placement": "top"
    }
  }, [_c('el-button', {
    attrs: {
      "type": "danger",
      "icon": "delete"
    },
    on: {
      "click": function($event) {
        _vm.deleteLeave(_vm.leaveForm.id)
      }
    }
  })], 1), _vm._v(" "), _c('el-tooltip', {
    attrs: {
      "content": "编辑该申请",
      "placement": "top"
    }
  }, [_c('el-button', {
    attrs: {
      "type": "primary",
      "icon": "edit"
    },
    on: {
      "click": function($event) {
        _vm.editLeaveDetail(_vm.leaveForm, 1)
      }
    }
  })], 1), _vm._v(" "), _c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.activeLeaveName == 'wait'),
      expression: "activeLeaveName=='wait'"
    }],
    attrs: {
      "type": "success"
    },
    on: {
      "click": function($event) {
        _vm.acceptLeave(_vm.leaveForm.id)
      }
    }
  }, [_vm._v("审核通过")])], 1)])], 1)], 1)
},staticRenderFns: [function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('span', {
    staticClass: "add-task-icon"
  }, [_c('i', {
    staticClass: "el-icon-plus"
  })])
},function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('span', {
    staticClass: "add-task-icon"
  }, [_c('i', {
    staticClass: "el-icon-plus"
  })])
},function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('span', {
    staticClass: "add-task-icon"
  }, [_c('i', {
    staticClass: "el-icon-plus"
  })])
}]}

/***/ }),
/* 343 */
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
      "align": "center",
      "render-header": _vm.createQuarterHistory
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
            "src": __webpack_require__(166),
            "alt": scope.row.id
          }
        })]) : _vm._e(), _vm._v(" "), (scope.row.id == '2') ? _c('div', [_c('img', {
          attrs: {
            "src": __webpack_require__(170),
            "alt": scope.row.id
          }
        })]) : _vm._e(), _vm._v(" "), (scope.row.id == '3') ? _c('div', [_c('img', {
          attrs: {
            "src": __webpack_require__(167),
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
      "align": "center",
      "render-header": _vm.createYearHistory
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
            "src": __webpack_require__(166),
            "alt": scope.row.id
          }
        })]) : _vm._e(), _vm._v(" "), (scope.row.id == '2') ? _c('div', [_c('img', {
          attrs: {
            "src": __webpack_require__(170),
            "alt": scope.row.id
          }
        })]) : _vm._e(), _vm._v(" "), (scope.row.id == '3') ? _c('div', [_c('img', {
          attrs: {
            "src": __webpack_require__(167),
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

/***/ }),
/* 344 */
/***/ (function(module, exports) {

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

/***/ }),
/* 345 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    class: [
      _vm.type === 'textarea' ? 'el-textarea' : 'el-input',
      _vm.size ? 'el-input--' + _vm.size : '',
      {
        'is-disabled': _vm.disabled,
        'el-input-group': _vm.$slots.prepend || _vm.$slots.append,
        'el-input-group--append': _vm.$slots.append,
        'el-input-group--prepend': _vm.$slots.prepend
      }
    ]
  }, [(_vm.type !== 'textarea') ? [(_vm.$slots.prepend) ? _c('div', {
    staticClass: "el-input-group__prepend"
  }, [_vm._t("prepend")], 2) : _vm._e(), _vm._v(" "), _vm._t("icon", [(_vm.icon) ? _c('i', {
    staticClass: "el-input__icon",
    class: [
      'el-icon-' + _vm.icon,
      _vm.onIconClick ? 'is-clickable' : ''
    ],
    on: {
      "click": _vm.handleIconClick
    }
  }) : _vm._e()]), _vm._v(" "), (_vm.type !== 'textarea') ? _c('input', _vm._b({
    ref: "input",
    staticClass: "el-input__inner",
    attrs: {
      "autocomplete": _vm.autoComplete
    },
    domProps: {
      "value": _vm.currentValue
    },
    on: {
      "input": _vm.handleInput,
      "focus": _vm.handleFocus,
      "blur": _vm.handleBlur
    }
  }, 'input', _vm.$props, false)) : _vm._e(), _vm._v(" "), (_vm.validating) ? _c('i', {
    staticClass: "el-input__icon el-icon-loading"
  }) : _vm._e(), _vm._v(" "), (_vm.$slots.append) ? _c('div', {
    staticClass: "el-input-group__append"
  }, [_vm._t("append")], 2) : _vm._e()] : _c('textarea', _vm._b({
    ref: "textarea",
    staticClass: "el-textarea__inner",
    style: (_vm.textareaStyle),
    domProps: {
      "value": _vm.currentValue
    },
    on: {
      "input": _vm.handleInput,
      "focus": _vm.handleFocus,
      "blur": _vm.handleBlur
    }
  }, 'textarea', _vm.$props, false))], 2)
},staticRenderFns: []}

/***/ }),
/* 346 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "stats-con"
  }, [_c('el-tabs', {
    staticStyle: {
      "position": "relative",
      "margin-bottom": "20px"
    },
    on: {
      "tab-click": function($event) {}
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
      "label": "任务统计",
      "name": "stat"
    }
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
      "label": "我的任务/进行中任务",
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
        }, [_vm._v(_vm._s(sco.row.inProcess) + " / " + _vm._s(sco.row.multiTask))])]
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
  })], 1)], 1), _vm._v(" "), _c('el-tab-pane', {
    attrs: {
      "label": "线上bug",
      "name": "bug"
    }
  }, [_c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-select', {
    attrs: {
      "clearable": "",
      "filterable": "",
      "placeholder": "筛选用户"
    },
    model: {
      value: (_vm.bugList.userId),
      callback: function($$v) {
        _vm.$set(_vm.bugList, "userId", $$v)
      },
      expression: "bugList.userId"
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
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-date-picker', {
    attrs: {
      "type": "daterange",
      "placeholder": "选择日期范围",
      "unlink-panels": "",
      "picker-options": _vm.pickerOptions
    },
    on: {
      "change": _vm.bugTimeChange
    },
    model: {
      value: (_vm.bugDaterange),
      callback: function($$v) {
        _vm.bugDaterange = $$v
      },
      expression: "bugDaterange"
    }
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('img', {
    staticClass: "search-btn",
    attrs: {
      "src": __webpack_require__(11),
      "alt": ""
    },
    on: {
      "click": function($event) {
        _vm.getBugList()
      }
    }
  })]), _vm._v(" "), _c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.permit),
      expression: "permit"
    }],
    staticClass: "add-member-basic-msg fl",
    staticStyle: {
      "margin-left": "300px",
      "margin-bottom": "10px"
    },
    attrs: {
      "type": "primary"
    },
    on: {
      "click": _vm.createBugSolve
    }
  }, [_vm._v("创建bug处理")]), _vm._v(" "), _c('el-table', {
    attrs: {
      "data": _vm.bugManage,
      "border": ""
    }
  }, [_c('el-table-column', {
    attrs: {
      "prop": "projectId",
      "label": "序号",
      "align": "center",
      "width": "100"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "description",
      "label": "问题描述",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "projectName",
      "label": "问题项目",
      "align": "center",
      "width": "130"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "users",
      "label": "负责人",
      "align": "center",
      "width": "200"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "createTime",
      "label": "发现日期",
      "width": "130"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "processTime",
      "label": "处理日期",
      "width": "130"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "label": "操作",
      "width": "100"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('el-button', {
          attrs: {
            "type": "text",
            "size": "small"
          },
          on: {
            "click": function($event) {
              _vm.bugDetail(scope.row)
            }
          }
        }, [_vm._v("查看")])]
      }
    }])
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "pagination"
  }, [_c('el-pagination', {
    attrs: {
      "current-page": _vm.bugList.pageNum,
      "page-size": _vm.bugFormPage.pageSize,
      "layout": _vm.pageLayout,
      "total": _vm.bugFormPage.total
    },
    on: {
      "current-change": _vm.handleCurrentChange,
      "update:currentPage": function($event) {
        _vm.$set(_vm.bugList, "pageNum", $event)
      }
    }
  })], 1)], 1), _vm._v(" "), _c('el-tab-pane', {
    attrs: {
      "label": "个人任务",
      "name": "personal"
    }
  }, [_c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-select', {
    attrs: {
      "clearable": "",
      "filterable": "",
      "placeholder": "筛选用户"
    },
    model: {
      value: (_vm.persanalForm.userId),
      callback: function($$v) {
        _vm.$set(_vm.persanalForm, "userId", $$v)
      },
      expression: "persanalForm.userId"
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
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-date-picker', {
    attrs: {
      "type": "daterange",
      "placeholder": "选择日期范围",
      "range-separator": " 至 ",
      "picker-options": _vm.pickerOptions
    },
    on: {
      "change": _vm.timeChange
    },
    model: {
      value: (_vm.daterange),
      callback: function($$v) {
        _vm.daterange = $$v
      },
      expression: "daterange"
    }
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('img', {
    staticClass: "search-btn",
    attrs: {
      "src": __webpack_require__(11),
      "alt": ""
    },
    on: {
      "click": function($event) {
        _vm.getPersonalData()
      }
    }
  })]), _vm._v(" "), _c('el-table', {
    attrs: {
      "data": _vm.pesonalTaskData,
      "border": "",
      "summary-method": _vm.getSummaries,
      "show-summary": ""
    }
  }, [_c('el-table-column', {
    attrs: {
      "prop": "id",
      "label": "序号",
      "align": "center",
      "width": "80"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "taskName",
      "label": "任务名称",
      "align": "center",
      "width": "150"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(sco) {
        return [_c('a', {
          staticStyle: {
            "color": "#20a0ff",
            "cursor": "pointer"
          },
          on: {
            "click": function($event) {
              _vm.getPesonTask(sco.row.taskId)
            }
          }
        }, [_vm._v(_vm._s(sco.row.taskName))])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "taskDescription",
      "label": "任务描述",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "description",
      "label": "工作内容",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "createTime",
      "label": "开始日期",
      "width": "120"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "endTime",
      "label": "截止日期",
      "width": "120"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "taskHours",
      "label": "工作量",
      "width": "80"
    }
  })], 1)], 1), _vm._v(" "), _c('el-tab-pane', {
    attrs: {
      "label": "周任务统计",
      "name": "week"
    }
  }, [_c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-date-picker', {
    attrs: {
      "picker-options": _vm.pickerWeek,
      "type": "week",
      "format": "yyyy 第 WW 周",
      "placeholder": "选择周"
    },
    model: {
      value: (_vm.userWeekForm.date),
      callback: function($$v) {
        _vm.$set(_vm.userWeekForm, "date", $$v)
      },
      expression: "userWeekForm.date"
    }
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-button', {
    attrs: {
      "type": "text"
    },
    on: {
      "click": function($event) {
        _vm.getCurrentWeek()
      }
    }
  }, [_vm._v("当前第" + _vm._s(_vm.currentWeek) + "周")])], 1), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('img', {
    staticClass: "search-btn",
    attrs: {
      "src": __webpack_require__(11),
      "alt": ""
    },
    on: {
      "click": function($event) {
        _vm.getUserWeekStats()
      }
    }
  })]), _vm._v(" "), _c('el-table', {
    attrs: {
      "data": _vm.userWeekData,
      "border": ""
    }
  }, [_c('el-table-column', {
    attrs: {
      "type": "index",
      "label": "序号",
      "width": "80"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "userName",
      "label": "用户",
      "align": "center",
      "width": "80"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(sco) {
        return [_c('a', {
          staticStyle: {
            "color": "#20a0ff",
            "cursor": "pointer"
          },
          on: {
            "click": function($event) {
              _vm.getPesonStats(sco.row.userId)
            }
          }
        }, [_vm._v(_vm._s(sco.row.userName))])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "taskName",
      "label": "任务名称",
      "align": "center"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(sco) {
        return [_c('div', {
          staticStyle: {
            "white-space": "pre-wrap",
            "text-align": "left"
          }
        }, [_vm._v(_vm._s(sco.row.taskName))])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "hours",
      "label": "周工作量",
      "width": "120"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('span', {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: (scope.row.hours <= 40),
            expression: "scope.row.hours<=40"
          }],
          attrs: {
            "type": "text"
          }
        }, [_vm._v(_vm._s(scope.row.hours))]), _vm._v(" "), _c('span', {
          directives: [{
            name: "show",
            rawName: "v-show",
            value: (scope.row.hours > 40),
            expression: "scope.row.hours>40"
          }],
          staticStyle: {
            "color": "red"
          },
          attrs: {
            "type": "text"
          }
        }, [_vm._v(_vm._s(scope.row.hours))])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "leaveHours",
      "label": "请假时间",
      "width": "100"
    }
  })], 1)], 1), _vm._v(" "), _c('el-tab-pane', {
    attrs: {
      "label": "请假统计",
      "name": "leave"
    }
  }, [_c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-select', {
    attrs: {
      "clearable": "",
      "filterable": "",
      "placeholder": "筛选用户"
    },
    model: {
      value: (_vm.leaveList.userId),
      callback: function($$v) {
        _vm.$set(_vm.leaveList, "userId", $$v)
      },
      expression: "leaveList.userId"
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
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-date-picker', {
    attrs: {
      "type": "daterange",
      "placeholder": "选择日期范围",
      "unlink-panels": "",
      "picker-options": _vm.pickerOptions
    },
    on: {
      "change": _vm.leaveTimeChange
    },
    model: {
      value: (_vm.leaveDaterange),
      callback: function($$v) {
        _vm.leaveDaterange = $$v
      },
      expression: "leaveDaterange"
    }
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('img', {
    staticClass: "search-btn",
    attrs: {
      "src": __webpack_require__(11),
      "alt": ""
    },
    on: {
      "click": function($event) {
        _vm.getLeaveList()
      }
    }
  })]), _vm._v(" "), _c('el-table', {
    attrs: {
      "data": _vm.leaveManage,
      "border": ""
    }
  }, [_c('el-table-column', {
    attrs: {
      "type": "index",
      "label": "序号",
      "width": "80"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "description",
      "label": "请假原因",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "userName",
      "label": "请假人",
      "align": "center",
      "width": "130"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "hours",
      "label": "时长",
      "align": "center",
      "width": "80"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "typeName",
      "label": "类型",
      "align": "center",
      "width": "80"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "beginTime",
      "label": "开始日期",
      "width": "150",
      "align": "center"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('div', {
          attrs: {
            "type": "text",
            "size": "small"
          }
        }, [_vm._v(_vm._s(_vm._f("formatDate")(scope.row.beginTime)))])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "endTime",
      "label": "结束日期",
      "width": "150",
      "align": "center"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('div', {
          attrs: {
            "type": "text",
            "size": "small"
          }
        }, [_vm._v(_vm._s(_vm._f("formatDate")(scope.row.endTime)))])]
      }
    }])
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "pagination"
  }, [_c('el-pagination', {
    attrs: {
      "current-page": _vm.leaveList.pageNum,
      "page-size": _vm.leaveFormPage.pageSize,
      "layout": _vm.leavePageLayout,
      "total": _vm.leaveFormPage.total
    },
    on: {
      "current-change": _vm.leaveHandleCurrentChange,
      "update:currentPage": function($event) {
        _vm.$set(_vm.leaveList, "pageNum", $event)
      }
    }
  })], 1)], 1)], 1), _vm._v(" "), _c('el-dialog', {
    staticStyle: {
      "width": "auto"
    },
    attrs: {
      "title": "创建Bug处理结果",
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "visible": _vm.createBugSolvingVisible
    },
    on: {
      "update:visible": function($event) {
        _vm.createBugSolvingVisible = $event
      }
    }
  }, [_c('div', {
    staticClass: "ctpc-con"
  }, [_c('div', {
    staticStyle: {
      "display": "inline"
    }
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("问题项目")]), _vm._v(" "), _c('div', {
    staticStyle: {
      "display": "inline",
      "margin-left": "30px"
    }
  }, [_c('el-select', {
    attrs: {
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.bugForm.projectId),
      callback: function($$v) {
        _vm.$set(_vm.bugForm, "projectId", $$v)
      },
      expression: "bugForm.projectId"
    }
  }, _vm._l((_vm.projectForm), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('div', {
    staticStyle: {
      "margin-top": "20px"
    }
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("问题描述")]), _vm._v(" "), _c('el-input', {
    staticStyle: {
      "position": "relative",
      "margin-left": "100px",
      "margin-top": "-20px",
      "width": "80%"
    },
    attrs: {
      "type": "textarea",
      "rows": 3
    },
    model: {
      value: (_vm.bugForm.description),
      callback: function($$v) {
        _vm.$set(_vm.bugForm, "description", $$v)
      },
      expression: "bugForm.description"
    }
  }), _vm._v(" "), _c('div', {
    staticStyle: {
      "margin-top": "20px",
      "margin-bottom": "-20px"
    }
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("\t发现日期")]), _vm._v(" "), _c('el-date-picker', {
    staticStyle: {
      "position": "relative",
      "margin-left": "100px"
    },
    attrs: {
      "type": "date",
      "placeholder": "选择发现日期"
    },
    model: {
      value: (_vm.bugForm.createTime),
      callback: function($$v) {
        _vm.$set(_vm.bugForm, "createTime", $$v)
      },
      expression: "bugForm.createTime"
    }
  }), _vm._v(" "), _c('div', {
    staticStyle: {
      "margin-top": "20px",
      "margin-bottom": "-20px"
    }
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("\t处理日期")]), _vm._v(" "), _c('el-date-picker', {
    staticStyle: {
      "position": "relative",
      "margin-left": "100px"
    },
    attrs: {
      "type": "date",
      "placeholder": "选择处理日期"
    },
    model: {
      value: (_vm.bugForm.processTime),
      callback: function($$v) {
        _vm.$set(_vm.bugForm, "processTime", $$v)
      },
      expression: "bugForm.processTime"
    }
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "ctpc-member-con"
  }, _vm._l((_vm.bugUsers), function(item, index) {
    return _c('div', {
      staticClass: "ctpc-member-list clearfix in",
      class: item.cssClass
    }, [_c('span', {
      staticClass: "fl ctpc-member-head"
    }, [_vm._v(_vm._s(item.userName))]), _vm._v(" "), _c('span', {
      staticClass: "fl ctpc-member-job-time"
    }, [_vm._v("积分:" + _vm._s(item.integral))]), _vm._v(" "), _c('span', {
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
          _vm.modifyMember(index, _vm.bugUsers)
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
  })), _vm._v(" "), (_vm.showAddDetail) ? _c('div', {
    staticClass: "ctpc-add-member-detail"
  }, [_c('div', {
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
      value: (_vm.addMemberIndex.userId),
      callback: function($$v) {
        _vm.$set(_vm.addMemberIndex, "userId", $$v)
      },
      expression: "addMemberIndex.userId"
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
  }, [_vm._v("*")]), _vm._v("积分：\n                        ")]), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('input', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.addMemberIndex.integral),
      expression: "addMemberIndex.integral"
    }],
    staticClass: "member-time-count",
    staticStyle: {
      "width": "80px"
    },
    attrs: {
      "maxlength": 6
    },
    domProps: {
      "value": (_vm.addMemberIndex.integral)
    },
    on: {
      "input": function($event) {
        if ($event.target.composing) { return; }
        _vm.$set(_vm.addMemberIndex, "integral", $event.target.value)
      }
    }
  })])])]), _vm._v(" "), _c('div', {
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
  })])]) : _vm._e(), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (!_vm.showAddDetail),
      expression: "!showAddDetail"
    }],
    staticClass: "add-member-opt",
    on: {
      "click": function($event) {
        _vm.showAddDetail = !_vm.showAddDetail;
      }
    }
  }, [_c('span', {
    staticClass: "add-member-icon"
  }, [_vm._v("+")]), _vm._v(" "), _c('span', {
    staticClass: "add-member-msg"
  }, [_vm._v("添加成员")])]), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    attrs: {
      "slot": "footer"
    },
    slot: "footer"
  }, [_c('el-button', {
    attrs: {
      "type": "primary"
    },
    on: {
      "click": function($event) {
        _vm.saveBugForm('bugForm')
      }
    }
  }, [_vm._v("立即创建")]), _vm._v(" "), _c('el-button', {
    on: {
      "click": function($event) {
        _vm.createBugSolvingVisible = false
      }
    }
  }, [_vm._v("取 消")])], 1)]), _vm._v(" "), _c('el-dialog', {
    staticStyle: {
      "width": "auto"
    },
    attrs: {
      "title": "更新Bug处理",
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "visible": _vm.updateBugSolvingVisible
    },
    on: {
      "update:visible": function($event) {
        _vm.updateBugSolvingVisible = $event
      }
    }
  }, [_c('div', {
    staticClass: "ctpc-con"
  }, [_c('div', {
    staticStyle: {
      "display": "inline"
    }
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("问题项目")]), _vm._v(" "), _c('div', {
    staticStyle: {
      "display": "inline",
      "margin-left": "30px"
    }
  }, [_c('el-select', {
    attrs: {
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.bugForm.projectId),
      callback: function($$v) {
        _vm.$set(_vm.bugForm, "projectId", $$v)
      },
      expression: "bugForm.projectId"
    }
  }, _vm._l((_vm.projectForm), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1), _vm._v(" "), _c('div', {
    staticStyle: {
      "margin-top": "20px"
    }
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("问题描述")]), _vm._v(" "), _c('el-input', {
    staticStyle: {
      "position": "relative",
      "margin-left": "100px",
      "margin-top": "-20px",
      "width": "80%"
    },
    attrs: {
      "type": "textarea",
      "rows": 3
    },
    model: {
      value: (_vm.bugForm.description),
      callback: function($$v) {
        _vm.$set(_vm.bugForm, "description", $$v)
      },
      expression: "bugForm.description"
    }
  }), _vm._v(" "), _c('div', {
    staticStyle: {
      "margin-top": "20px",
      "margin-bottom": "-20px"
    }
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("\t发现日期")]), _vm._v(" "), _c('el-date-picker', {
    staticStyle: {
      "position": "relative",
      "margin-left": "100px"
    },
    attrs: {
      "type": "date",
      "placeholder": "选择发现日期"
    },
    model: {
      value: (_vm.bugForm.createTime),
      callback: function($$v) {
        _vm.$set(_vm.bugForm, "createTime", $$v)
      },
      expression: "bugForm.createTime"
    }
  }), _vm._v(" "), _c('div', {
    staticStyle: {
      "margin-top": "20px",
      "margin-bottom": "-20px"
    }
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("\t处理日期")]), _vm._v(" "), _c('el-date-picker', {
    staticStyle: {
      "position": "relative",
      "margin-left": "100px"
    },
    attrs: {
      "type": "date",
      "placeholder": "选择处理日期"
    },
    model: {
      value: (_vm.bugForm.processTime),
      callback: function($$v) {
        _vm.$set(_vm.bugForm, "processTime", $$v)
      },
      expression: "bugForm.processTime"
    }
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "ctpc-member-con"
  }, _vm._l((_vm.bugUsers), function(item, index) {
    return _c('div', {
      staticClass: "ctpc-member-list clearfix in",
      class: item.cssClass
    }, [_c('span', {
      staticClass: "fl ctpc-member-head"
    }, [_vm._v(_vm._s(item.userName))]), _vm._v(" "), _c('span', {
      staticClass: "fl ctpc-member-job-time"
    }, [_vm._v("积分:" + _vm._s(item.integral))]), _vm._v(" "), _c('span', {
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
          _vm.modifyMember(index, _vm.bugUsers)
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
  })), _vm._v(" "), (_vm.showAddDetail) ? _c('div', {
    staticClass: "ctpc-add-member-detail"
  }, [_c('div', {
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
      value: (_vm.addMemberIndex.userId),
      callback: function($$v) {
        _vm.$set(_vm.addMemberIndex, "userId", $$v)
      },
      expression: "addMemberIndex.userId"
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
  }, [_vm._v("*")]), _vm._v("积分：\n                    ")]), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('input', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.addMemberIndex.integral),
      expression: "addMemberIndex.integral"
    }],
    staticClass: "member-time-count",
    staticStyle: {
      "width": "80px"
    },
    attrs: {
      "maxlength": 6
    },
    domProps: {
      "value": (_vm.addMemberIndex.integral)
    },
    on: {
      "input": function($event) {
        if ($event.target.composing) { return; }
        _vm.$set(_vm.addMemberIndex, "integral", $event.target.value)
      }
    }
  })])])]), _vm._v(" "), _c('div', {
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
  })])]) : _vm._e(), _vm._v(" "), _c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (!_vm.showAddDetail),
      expression: "!showAddDetail"
    }],
    staticClass: "add-member-opt",
    on: {
      "click": function($event) {
        _vm.showAddDetail = !_vm.showAddDetail;
      }
    }
  }, [_c('span', {
    staticClass: "add-member-icon"
  }, [_vm._v("+")]), _vm._v(" "), _c('span', {
    staticClass: "add-member-msg"
  }, [_vm._v("添加成员")])]), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    attrs: {
      "slot": "footer"
    },
    slot: "footer"
  }, [_c('el-button', {
    attrs: {
      "type": "primary"
    },
    on: {
      "click": function($event) {
        _vm.editBugForm('bugForm')
      }
    }
  }, [_vm._v("立即更新")]), _vm._v(" "), _c('el-button', {
    on: {
      "click": function($event) {
        _vm.updateBugSolvingVisible = false
      }
    }
  }, [_vm._v("取 消")])], 1)]), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "Bug处理结果详情",
      "size": "tiny",
      "close-on-click-modal": false,
      "close-on-press-escape": false,
      "visible": _vm.bugDetailVisible
    },
    on: {
      "update:visible": function($event) {
        _vm.bugDetailVisible = $event
      }
    }
  }, [_c('el-form', [_c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "项目名称："
    }
  }, [_vm._v(_vm._s(_vm.bugDetailForm.projectName))]), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form",
    attrs: {
      "label": "问题描述："
    }
  }, [_vm._v(_vm._s(_vm.bugDetailForm.description))]), _vm._v(" "), _vm._l((_vm.bugDetailForm.bugUsers), function(item, index) {
    return _c('div', {
      staticClass: "ctpc-member-list clearfix"
    }, [_c('span', {
      staticClass: "fl ctpc-member-head"
    }, [_vm._v(_vm._s(item.userName))]), _vm._v(" "), _c('span', {
      staticClass: "fl ctpc-member-job-time"
    }, [_vm._v("积分:" + _vm._s(item.integral))])])
  })], 2), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    attrs: {
      "slot": "footer"
    },
    slot: "footer"
  }, [_c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.permit),
      expression: "permit"
    }]
  }, [_c('el-tooltip', {
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
        _vm.editBugDetail(_vm.bugDetailForm)
      }
    }
  })], 1), _vm._v(" "), _c('el-tooltip', {
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
      "click": function($event) {
        _vm.deleteBug()
      }
    }
  })], 1)], 1)])], 1)], 1)
},staticRenderFns: []}

/***/ }),
/* 347 */
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
        _vm.$set(_vm.queryForm, "startTime", $$v)
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
        _vm.$set(_vm.queryForm, "endTime", $$v)
      },
      expression: "queryForm.endTime"
    }
  }), _vm._v(" "), _c('img', {
    staticClass: "serch-btn",
    attrs: {
      "src": __webpack_require__(11),
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
            "type": scope.row.origin === '任务系统-单人任务' ? 'warning' : 'success' && scope.row.origin === '手动录入' ? 'primary' : 'success' && scope.row.origin === '转移求助' ? 'danger' : 'success' && scope.row.origin === 'Bug处理结果' ? 'info' : 'success',
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
        _vm.$set(_vm.integralForm, "integral", $$v)
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
        _vm.$set(_vm.integralForm, "description", $$v)
      },
      expression: "integralForm.description"
    }
  })], 1)], 1), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    attrs: {
      "slot": "footer"
    },
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
    attrs: {
      "slot": "footer"
    },
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

/***/ }),
/* 348 */
/***/ (function(module, exports) {

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
        _vm.$set(_vm.taskForm, "description", $event.target.value)
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
        _vm.$set(_vm.taskForm, "taskName", $$v)
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
        _vm.$set(_vm.taskForm, "projectId", $$v)
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
        _vm.$set(_vm.taskForm, "endTime", $$v)
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
        _vm.$set(_vm.taskForm, "priority", $$v)
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
      "clearable": "",
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.taskForm.facility),
      callback: function($$v) {
        _vm.$set(_vm.taskForm, "facility", $$v)
      },
      expression: "taskForm.facility"
    }
  }, _vm._l((_vm.facilityList), function(item) {
    return _c('el-option', {
      key: item.value,
      attrs: {
        "label": item.label,
        "value": item.value
      }
    })
  }))], 1)]), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list clearfix"
  }, [_vm._m(5), _vm._v(" "), _c('div', {
    staticClass: "ctpc-list-con fl"
  }, [_c('el-select', {
    attrs: {
      "multiple-limit": 1,
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.taskForm.stageId),
      callback: function($$v) {
        _vm.$set(_vm.taskForm, "stageId", $$v)
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
  }, [_vm._m(6), _vm._v(" "), _c('div', {
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
        _vm.$set(_vm.taskForm, "tags", $$v)
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
      value: (_vm.description),
      callback: function($$v) {
        _vm.description = $$v
      },
      expression: "description"
    }
  }), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic"
  }, [_c('div', {
    staticClass: "add-member-basic-list clearfix"
  }, [_vm._m(7), _vm._v(" "), _c('div', {
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
        _vm.$set(_vm.step, "userId", $$v)
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
  }))], 1), _vm._v(" "), _vm._m(8), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('input', {
    directives: [{
      name: "model",
      rawName: "v-model",
      value: (_vm.step.taskHours),
      expression: "step.taskHours"
    }],
    staticClass: "member-time-count",
    staticStyle: {
      "width": "80px"
    },
    attrs: {
      "maxlength": 6
    },
    domProps: {
      "value": (_vm.step.taskHours)
    },
    on: {
      "change": function($event) {},
      "input": function($event) {
        if ($event.target.composing) { return; }
        _vm.$set(_vm.step, "taskHours", $event.target.value)
      }
    }
  }), _vm._v("工时\n                            ")])]), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-list clearfix"
  }, [_vm._m(9), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "format": "yyyy-MM-dd",
      "placeholder": "选择日期"
    },
    on: {
      "change": function($event) {}
    },
    model: {
      value: (_vm.step.beginTime),
      callback: function($$v) {
        _vm.$set(_vm.step, "beginTime", $$v)
      },
      expression: "step.beginTime"
    }
  })], 1), _vm._v(" "), _vm._m(10), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "format": "yyyy-MM-dd",
      "placeholder": "选择日期"
    },
    on: {
      "change": function($event) {}
    },
    model: {
      value: (_vm.step.endTime),
      callback: function($$v) {
        _vm.$set(_vm.step, "endTime", $$v)
      },
      expression: "step.endTime"
    }
  })], 1)]), _vm._v(" "), _vm._l((_vm.sortWeekNumber), function(item, index) {
    return _c('div', [_c('div', {
      staticClass: "add-member-basic-list clearfix"
    }, [_c('div', {
      staticClass: "fl",
      staticStyle: {
        "margin-left": "5px"
      }
    }, [_c('span', {
      staticClass: "star"
    }, [_vm._v("*")]), _vm._v("第" + _vm._s(item.weekNumber) + "周工作量(" + _vm._s(item.range) + ")：")]), _vm._v(" "), _c('input', {
      directives: [{
        name: "model",
        rawName: "v-model",
        value: (item.hours),
        expression: "item.hours"
      }],
      staticClass: "member-time-week",
      staticStyle: {
        "width": "80px"
      },
      attrs: {
        "maxlength": 6
      },
      domProps: {
        "value": (item.hours)
      },
      on: {
        "input": function($event) {
          if ($event.target.composing) { return; }
          _vm.$set(item, "hours", $event.target.value)
        }
      }
    }), _vm._v("    已有工作量:\n                                "), _c('div', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (parseFloat(item.weekHours) + parseFloat(item.hours == '' ? 0 : item.hours) >= 40),
        expression: "parseFloat(item.weekHours)+parseFloat(item.hours==''?0:item.hours)>=40"
      }],
      staticClass: "f1",
      staticStyle: {
        "color": "red",
        "display": "inline"
      }
    }, [_vm._v(_vm._s(parseFloat(item.weekHours) + parseFloat(item.hours == '' ? 0 : item.hours)))]), _vm._v(" "), _c('div', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (parseFloat(item.weekHours) + parseFloat(item.hours == '' ? 0 : item.hours) < 40),
        expression: "parseFloat(item.weekHours)+parseFloat(item.hours==''?0:item.hours)<40"
      }],
      staticClass: "f1",
      staticStyle: {
        "display": "inline"
      }
    }, [_vm._v(_vm._s(parseFloat(item.weekHours) + parseFloat(item.hours == '' ? 0 : item.hours)))])])])
  })], 2), _vm._v(" "), _c('div', {
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
  }, [_vm._v("*")]), _vm._v("难易度")])
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

/***/ }),
/* 349 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', [_c('div', {
    staticClass: "toggle-view"
  }, [_c('input', {
    attrs: {
      "type": "button",
      "value": _vm.btnValStatus == 1 ? '点击切换到看板模式' : '点击切换到列表模式'
    },
    on: {
      "click": _vm.btnValFun
    }
  }), _vm._v(" "), _c('input', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.permit),
      expression: "permit"
    }],
    attrs: {
      "type": "button",
      "value": "创建多人任务"
    },
    on: {
      "click": _vm.createTaskClick
    }
  }), _vm._v(" "), _c('input', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.btnValStatus == 2 && _vm.publishHide),
      expression: "btnValStatus == 2&&publishHide"
    }],
    staticStyle: {
      "margin-left": "450px"
    },
    attrs: {
      "type": "button",
      "value": _vm.publishText
    },
    on: {
      "click": _vm.setPublish
    }
  }), _vm._v(" "), _c('el-checkbox', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.btnValStatus == 2 && _vm.publishHide),
      expression: "btnValStatus == 2&&publishHide"
    }],
    staticStyle: {
      "display": "inline-block",
      "margin-left": "10px"
    },
    on: {
      "change": _vm.publishTask
    },
    model: {
      value: (_vm.publishType),
      callback: function($$v) {
        _vm.publishType = $$v
      },
      expression: "publishType"
    }
  }, [_vm._v("仅显示发版时间之前的任务\n        ")])], 1), _vm._v(" "), _c('div', {
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
        _vm.$set(_vm.form, "projectId", $$v)
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
        _vm.$set(_vm.form, "userId", $$v)
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
        _vm.$set(_vm.form, "priority", $$v)
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
  }, [_vm._v("创建人")]), _vm._v(" "), _c('el-select', {
    attrs: {
      "clearable": "",
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.form.createBy),
      callback: function($$v) {
        _vm.$set(_vm.form, "createBy", $$v)
      },
      expression: "form.createBy"
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
  }, [_vm._v("类型")]), _vm._v(" "), _c('el-select', {
    attrs: {
      "clearable": "",
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.form.type),
      callback: function($$v) {
        _vm.$set(_vm.form, "type", $$v)
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
        _vm.$set(_vm.form, "status", $$v)
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
  })], 1)])]), _vm._v(" "), _c('div', {
    staticClass: "task-top-list  clearfix"
  }, [_c('span', {
    staticClass: "ttl-name fl"
  }, [_vm._v("排序 ")]), _vm._v(" "), _c('div', {
    staticClass: "fl tag-name ",
    staticStyle: {
      "width": "400px"
    }
  }, _vm._l((_vm.sortList), function(item) {
    return _c('el-tooltip', {
      key: item.id,
      attrs: {
        "content": item.name + item.tips,
        "enterable": false,
        "placement": "top"
      }
    }, [_c('el-button', {
      staticClass: "fl",
      class: _vm.form.sort == item.id ? 'active' : '',
      attrs: {
        "size": "small"
      },
      on: {
        "click": function($event) {
          _vm.choiceSort(item.id, $event)
        }
      }
    }, [_vm._v(_vm._s(item.name) + "\n                            ")])], 1)
  })), _vm._v(" "), _c('el-button', {
    staticClass: "f1",
    staticStyle: {
      "margin-left": "300px"
    },
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
  }, [_vm._v("查询")])], 1), _vm._v(" "), _c('transition', {
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
      "viewType": _vm.btnValStatus,
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
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.btnValStatus == 1),
      expression: "btnValStatus == 1"
    }],
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
        _vm.$set(_vm.page, "pageNum", $event)
      }
    }
  })], 1)]), _vm._v(" "), _c('task-board', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.btnValStatus == 2),
      expression: "btnValStatus == 2"
    }],
    ref: "taskBoard"
  })], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "设置发版时间",
      "visible": _vm.publishVisible,
      "size": "tiny"
    },
    on: {
      "update:visible": function($event) {
        _vm.publishVisible = $event
      }
    }
  }, [_c('el-date-picker', {
    staticStyle: {
      "margin-left": "50px"
    },
    attrs: {
      "type": "date",
      "placeholder": "选择下次发版日期"
    },
    model: {
      value: (_vm.publishTime),
      callback: function($$v) {
        _vm.publishTime = $$v
      },
      expression: "publishTime"
    }
  }), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    attrs: {
      "slot": "footer"
    },
    slot: "footer"
  }, [_c('el-button', {
    attrs: {
      "type": "primary"
    },
    on: {
      "click": _vm.setPublishTime
    }
  }, [_vm._v("确定")]), _vm._v(" "), _c('el-button', {
    attrs: {
      "type": "warning"
    },
    on: {
      "click": function($event) {
        _vm.publishVisible = false
      }
    }
  }, [_vm._v("取消")])], 1)], 1)], 1)
},staticRenderFns: []}

/***/ }),
/* 350 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "login"
  }, [_c('el-form', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.loginShow),
      expression: "loginShow"
    }],
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
        _vm.$set(_vm.loginForm, "account", $$v)
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
    nativeOn: {
      "keyup": function($event) {
        if (!('button' in $event) && _vm._k($event.keyCode, "enter", 13, $event.key)) { return null; }
        _vm.login($event)
      }
    },
    model: {
      value: (_vm.loginForm.password),
      callback: function($$v) {
        _vm.$set(_vm.loginForm, "password", $$v)
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
  }, [_vm._v("登录")])], 1)], 1)]), _vm._v(" "), _c('el-form', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.registerShow),
      expression: "registerShow"
    }],
    ref: "userForm",
    staticClass: "demo-ruleForm login-container",
    attrs: {
      "label-position": "left",
      "label-width": "0px",
      "rules": _vm.rules,
      "model": _vm.userForm
    }
  }, [_c('h1', {
    staticClass: "title"
  }, [_vm._v("用户注册 "), _c('em', [_vm._v(" USER REGISTER")])]), _vm._v(" "), _c('div', {
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
      value: (_vm.userForm.account),
      callback: function($$v) {
        _vm.$set(_vm.userForm, "account", $$v)
      },
      expression: "userForm.account"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "prop": "name"
    }
  }, [_c('el-input', {
    staticClass: "form-input",
    attrs: {
      "type": "text",
      "auto-complete": "off",
      "placeholder": "真实姓名"
    },
    model: {
      value: (_vm.userForm.name),
      callback: function($$v) {
        _vm.$set(_vm.userForm, "name", $$v)
      },
      expression: "userForm.name"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "prop": "password"
    }
  }, [_c('el-input', {
    staticClass: "form-input",
    attrs: {
      "type": "password",
      "auto-complete": "off",
      "placeholder": "密码"
    },
    model: {
      value: (_vm.userForm.password),
      callback: function($$v) {
        _vm.$set(_vm.userForm, "password", $$v)
      },
      expression: "userForm.password"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "prop": "jobName"
    }
  }, [_c('el-input', {
    staticClass: "form-input",
    attrs: {
      "type": "text",
      "auto-complete": "off",
      "placeholder": "职位"
    },
    model: {
      value: (_vm.userForm.jobName),
      callback: function($$v) {
        _vm.$set(_vm.userForm, "jobName", $$v)
      },
      expression: "userForm.jobName"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "prop": "phone"
    }
  }, [_c('el-input', {
    staticClass: "form-input",
    attrs: {
      "type": "text",
      "auto-complete": "off",
      "placeholder": "手机"
    },
    model: {
      value: (_vm.userForm.phone),
      callback: function($$v) {
        _vm.$set(_vm.userForm, "phone", $$v)
      },
      expression: "userForm.phone"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "prop": "email"
    }
  }, [_c('el-input', {
    staticClass: "form-input",
    attrs: {
      "type": "text",
      "auto-complete": "off",
      "placeholder": "邮箱"
    },
    model: {
      value: (_vm.userForm.email),
      callback: function($$v) {
        _vm.$set(_vm.userForm, "email", $$v)
      },
      expression: "userForm.email"
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
      "click": function($event) {
        _vm.register('userForm')
      }
    }
  }, [_vm._v("注册")])], 1), _vm._v(" "), _c('el-form-item', {
    staticStyle: {
      "width": "100%",
      "height": "50%"
    }
  }, [_c('el-button', {
    staticClass: "form-input",
    staticStyle: {
      "width": "100%",
      "height": "50%"
    },
    attrs: {
      "type": "text"
    },
    on: {
      "click": function($event) {
        _vm.registerShow = false, _vm.loginShow = true
      }
    }
  }, [_vm._v("返回登录")])], 1)], 1)])], 1)
},staticRenderFns: []}

/***/ }),
/* 351 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "userCommentsBlock"
  }, [_c('el-form', {
    attrs: {
      "inline": true
    }
  }, [_c('el-form-item', {
    attrs: {
      "label": ""
    }
  }, [_c('el-select', {
    attrs: {
      "filterable": "",
      "clearable": "",
      "placeholder": "评价人"
    },
    model: {
      value: (_vm.form.userId),
      callback: function($$v) {
        _vm.$set(_vm.form, "userId", $$v)
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
  }))], 1), _vm._v(" "), _c('el-form-item', {
    attrs: {
      "label": ""
    }
  }, [_c('el-select', {
    attrs: {
      "clearable": "",
      "placeholder": "等级"
    },
    model: {
      value: (_vm.form.grade),
      callback: function($$v) {
        _vm.$set(_vm.form, "grade", $$v)
      },
      expression: "form.grade"
    }
  }, _vm._l((_vm.gradeList), function(item) {
    return _c('el-option', {
      key: item,
      attrs: {
        "label": item,
        "value": item
      }
    })
  }))], 1), _vm._v(" "), _c('el-form-item', [_c('el-button', {
    attrs: {
      "type": "primary"
    },
    on: {
      "click": _vm.onSubmit
    }
  }, [_vm._v("查询")])], 1)], 1), _vm._v(" "), _c('el-table', {
    staticStyle: {
      "width": "100%"
    },
    attrs: {
      "data": _vm.data.list,
      "stripe": ""
    }
  }, [_c('el-table-column', {
    attrs: {
      "label": "任务名称"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('el-button', {
          attrs: {
            "type": "text"
          },
          on: {
            "click": function($event) {
              _vm.toTaskList(scope.row.taskId)
            }
          }
        }, [_vm._v(" " + _vm._s(scope.row.taskName))])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "userName",
      "label": "评价人"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "grade",
      "label": "评价等级"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('span', {
          style: (scope.row.grade === 'C' ? 'color: #ff4949;' : '')
        }, [_vm._v(_vm._s(scope.row.grade))])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "prop": "description",
      "label": "评价描述"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "label": "任务负责人"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('el-popover', {
          attrs: {
            "trigger": "hover",
            "placement": "top"
          }
        }, [_c('p', [_vm._v(_vm._s(scope.row.taskUserDesc))]), _vm._v(" "), _c('div', {
          staticClass: "name-wrapper",
          attrs: {
            "slot": "reference"
          },
          slot: "reference"
        }, [_c('el-tag', [_vm._v(_vm._s(scope.row.taskUserName))])], 1)])]
      }
    }])
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "label": "评价时间"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('span', [_vm._v(_vm._s(_vm._f("_formatDateTime")(scope.row.createTime)))])]
      }
    }])
  })], 1), _vm._v(" "), _c('div', {
    staticClass: "pagination"
  }, [_c('el-pagination', {
    attrs: {
      "current-page": _vm.data.pageNum,
      "page-size": _vm.data.pageSize,
      "layout": _vm.pageLayout,
      "total": _vm.data.total
    },
    on: {
      "current-change": _vm.handleCurrentChange,
      "update:currentPage": function($event) {
        _vm.$set(_vm.data, "pageNum", $event)
      }
    }
  })], 1)], 1)
},staticRenderFns: []}

/***/ }),
/* 352 */
/***/ (function(module, exports) {

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
    staticClass: "el-icon-loading",
    on: {
      "click": _vm.handleInnerClick
    }
  }) : _vm._e(), _vm._v(" "), (_vm.icon && !_vm.loading) ? _c('i', {
    class: 'el-icon-' + _vm.icon,
    on: {
      "click": _vm.handleInnerClick
    }
  }) : _vm._e(), _vm._v(" "), (_vm.$slots.default) ? _c('span', {
    on: {
      "click": _vm.handleInnerClick
    }
  }, [_vm._t("default")], 2) : _vm._e()])
},staticRenderFns: []}

/***/ }),
/* 353 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    attrs: {
      "id": "app"
    }
  }, [_c('router-view')], 1)
},staticRenderFns: []}

/***/ }),
/* 354 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "project-con"
  }, [_c('el-tabs', {
    on: {
      "tab-click": function($event) {}
    },
    model: {
      value: (_vm.activeName),
      callback: function($$v) {
        _vm.activeName = $$v
      },
      expression: "activeName"
    }
  }, [(_vm.isAdmin) ? _c('div', [_c('el-tab-pane', {
    staticStyle: {
      "line-height": "50px"
    },
    attrs: {
      "label": "标签管理",
      "name": "tag"
    }
  }, [_vm._l((_vm.tagList), function(tag) {
    return _c('el-tag', {
      key: tag.name,
      staticStyle: {
        "width": "100px",
        "height": "40px",
        "line-height": "40px",
        "text-align": "center",
        "font-size": "15px",
        "margin-left": "10px"
      },
      style: ('width:' + (tag.name.length) * 40 + 'px'),
      attrs: {
        "closable": true,
        "close-transition": false,
        "type": _vm.color[tag.color]
      },
      on: {
        "close": function($event) {
          _vm.deleteTagButton(tag)
        }
      }
    }, [_vm._v(_vm._s(tag.name))])
  }), _vm._v(" "), (_vm.inputVisible) ? _c('el-input', {
    ref: "saveTagInput",
    staticClass: "input-new-tag",
    staticStyle: {
      "width": "100px",
      "height": "40px",
      "line-height": "40px",
      "font-size": "15px"
    },
    attrs: {
      "size": "large"
    },
    on: {
      "blur": _vm.handleInputTagConfirm
    },
    model: {
      value: (_vm.TagName),
      callback: function($$v) {
        _vm.TagName = $$v
      },
      expression: "TagName"
    }
  }) : _c('el-button', {
    staticClass: "button-new-tag",
    attrs: {
      "size": "large"
    },
    on: {
      "click": _vm.showTag
    }
  }, [_vm._v("+ New Tag")])], 2), _vm._v(" "), _c('el-tab-pane', {
    attrs: {
      "label": "阶段管理",
      "name": "stage"
    }
  }, [_c('el-button', {
    staticStyle: {
      "margin-bottom": "10px",
      "margin-left": "1000px"
    },
    attrs: {
      "type": "primary",
      "size": "middle"
    },
    on: {
      "click": function($event) {
        _vm.addStageVisible = true;
        _vm.clearStage()
      }
    }
  }, [_vm._v("添加阶段")]), _vm._v(" "), _c('el-table', {
    staticStyle: {
      "width": "100%",
      "margin": "auto"
    },
    attrs: {
      "data": _vm.stageList
    }
  }, [_c('el-table-column', {
    staticStyle: {
      "text-align": "center"
    },
    attrs: {
      "label": "序号",
      "prop": "num"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "label": "阶段名称",
      "prop": "name"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "label": "阶段优先级",
      "prop": "sort",
      "align": "center"
    }
  }), _vm._v(" "), _c('el-table-column', {
    attrs: {
      "label": "编辑"
    },
    scopedSlots: _vm._u([{
      key: "default",
      fn: function(scope) {
        return [_c('el-button', {
          attrs: {
            "size": "small",
            "type": "primary"
          },
          on: {
            "click": function($event) {
              _vm.editStageDialog(scope.$index, _vm.stageList)
            }
          }
        }, [_vm._v("编辑")]), _vm._v(" "), _c('el-button', {
          attrs: {
            "size": "small",
            "type": "danger"
          },
          on: {
            "click": function($event) {
              _vm.deleteStage(scope.$index, _vm.stageList)
            }
          }
        }, [_vm._v("删除")])]
      }
    }])
  })], 1)], 1)], 1) : _c('div'), _vm._v(" "), _c('el-tab-pane', {
    attrs: {
      "label": "项目管理",
      "name": "project"
    }
  }, [_vm._l((_vm.TaskItem), function(list) {
    return _c('div', {
      staticClass: "task-item"
    }, [(list.imageUrl) ? _c('img', {
      staticClass: "task-logo",
      staticStyle: {
        "width": "40px",
        "height": "40px",
        "border-radius": "50%"
      },
      attrs: {
        "src": list.imageUrl
      }
    }) : _c('img', {
      staticClass: "task-logo",
      attrs: {
        "src": __webpack_require__(169)
      }
    }), _vm._v(" "), _c('div', {
      staticClass: "task-info",
      on: {
        "click": function($event) {
          _vm.editProject(list.id, list.name, list.description, list.imageUrl)
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
  }, [_vm._v("创建新项目")])])], 2)], 1), _vm._v(" "), _c('div', {
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
  }, [_vm._v("×")])]), _vm._v(" "), _c('el-upload', {
    staticClass: "avatar-uploader",
    attrs: {
      "action": "",
      "show-file-list": false,
      "http-request": _vm.upload,
      "before-upload": _vm.beforeAvatarUpload
    }
  }, [(_vm.project.imageUrl) ? _c('img', {
    staticClass: "avatar",
    attrs: {
      "src": _vm.project.imageUrl
    }
  }) : _c('img', {
    staticClass: "avatar",
    attrs: {
      "src": __webpack_require__(168)
    }
  }), _vm._v(" "), _c('p', {
    staticClass: "att-msg"
  }, [_vm._v("点击图片为你的项目上传项目图")])]), _vm._v(" "), _c('input', {
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
        _vm.$set(_vm.project, "name", $event.target.value)
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
        _vm.$set(_vm.project, "description", $event.target.value)
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
  }, [_vm._v("保存")])])], 1)]), _vm._v(" "), _c('div', {
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
  }, [_vm._v("×")])]), _vm._v(" "), _c('el-upload', {
    staticClass: "avatar-uploader",
    attrs: {
      "action": "",
      "show-file-list": false,
      "http-request": _vm.upload,
      "before-upload": _vm.beforeAvatarUpload
    }
  }, [(_vm.project.imageUrl) ? _c('img', {
    staticStyle: {
      "height": "180px",
      "width": "200px"
    },
    attrs: {
      "src": _vm.project.imageUrl
    }
  }) : _c('img', {
    staticStyle: {
      "height": "180px",
      "width": "200px"
    },
    attrs: {
      "src": __webpack_require__(168)
    }
  })]), _vm._v(" "), _c('p', {
    staticClass: "att-msg"
  }, [_vm._v("点击图片为你的项目上传项目图")]), _vm._v(" "), _c('input', {
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
        _vm.$set(_vm.project, "name", $event.target.value)
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
        _vm.$set(_vm.project, "description", $event.target.value)
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
  }, [_vm._v("保存")])])], 1)]), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "添加阶段",
      "visible": _vm.addStageVisible,
      "size": "tiny"
    },
    on: {
      "update:visible": function($event) {
        _vm.addStageVisible = $event
      }
    }
  }, [_c('el-input', {
    staticStyle: {
      "text-align": "center",
      "width": "400px",
      "margin-bottom": "10px"
    },
    attrs: {
      "placeholder": "请输入阶段优先级（请勿重复）"
    },
    model: {
      value: (_vm.stage.sort),
      callback: function($$v) {
        _vm.$set(_vm.stage, "sort", $$v)
      },
      expression: "stage.sort"
    }
  }, [_c('template', {
    attrs: {
      "slot": "prepend"
    },
    slot: "prepend"
  }, [_vm._v("阶段优先级:")])], 2), _vm._v(" "), _c('el-input', {
    staticStyle: {
      "text-align": "center",
      "width": "400px"
    },
    attrs: {
      "placeholder": "请输入阶段名称"
    },
    model: {
      value: (_vm.stage.name),
      callback: function($$v) {
        _vm.$set(_vm.stage, "name", $$v)
      },
      expression: "stage.name"
    }
  }, [_c('template', {
    attrs: {
      "slot": "prepend"
    },
    slot: "prepend"
  }, [_vm._v("阶段名称:")])], 2), _vm._v(" "), _c('div', {
    staticStyle: {
      "display": "block"
    }
  }, [_c('el-button', {
    staticStyle: {
      "margin-top": "15px",
      "margin-left": "300px"
    },
    attrs: {
      "type": "primary"
    },
    on: {
      "click": _vm.saveStage
    }
  }, [_vm._v("确认")])], 1)], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "编辑阶段",
      "visible": _vm.editStageVisible,
      "size": "tiny"
    },
    on: {
      "update:visible": function($event) {
        _vm.editStageVisible = $event
      }
    }
  }, [_c('el-input', {
    staticStyle: {
      "width": "250px",
      "margin-bottom": "10px"
    },
    attrs: {
      "placeholder": _vm.stage.sort + ''
    },
    model: {
      value: (_vm.stage.sort),
      callback: function($$v) {
        _vm.$set(_vm.stage, "sort", $$v)
      },
      expression: "stage.sort"
    }
  }, [_c('template', {
    attrs: {
      "slot": "prepend"
    },
    slot: "prepend"
  }, [_vm._v("阶段优先级:")])], 2), _vm._v(" "), _c('el-input', {
    staticStyle: {
      "width": "400px"
    },
    attrs: {
      "placeholder": _vm.stage.name
    },
    model: {
      value: (_vm.stage.name),
      callback: function($$v) {
        _vm.$set(_vm.stage, "name", $$v)
      },
      expression: "stage.name"
    }
  }, [_c('template', {
    attrs: {
      "slot": "prepend"
    },
    slot: "prepend"
  }, [_vm._v("阶段名称:")])], 2), _vm._v(" "), _c('el-button', {
    attrs: {
      "type": "primary"
    },
    on: {
      "click": _vm.editStage
    }
  }, [_vm._v("确认")])], 1)], 1)
},staticRenderFns: []}

/***/ }),
/* 355 */
/***/ (function(module, exports) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('transition', {
    attrs: {
      "name": "dialog-fade"
    }
  }, [_c('div', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.visible),
      expression: "visible"
    }],
    staticClass: "el-dialog__wrapper",
    on: {
      "click": function($event) {
        if ($event.target !== $event.currentTarget) { return null; }
        _vm.handleWrapperClick($event)
      }
    }
  }, [_c('div', {
    ref: "dialog",
    staticClass: "el-dialog",
    class: [_vm.sizeClass, _vm.customClass],
    style: (_vm.style)
  }, [_c('div', {
    staticClass: "el-dialog__header"
  }, [_vm._t("title", [_c('span', {
    staticClass: "el-dialog__title"
  }, [_vm._v(_vm._s(_vm.title))])]), _vm._v(" "), (_vm.showClose) ? _c('button', {
    staticClass: "el-dialog__headerbtn",
    attrs: {
      "type": "button",
      "aria-label": "Close"
    },
    on: {
      "click": _vm.handleClose
    }
  }, [_c('i', {
    staticClass: "el-dialog__close el-icon el-icon-close"
  })]) : _vm._e()], 2), _vm._v(" "), (_vm.rendered) ? _c('div', {
    staticClass: "el-dialog__body"
  }, [_vm._t("default")], 2) : _vm._e(), _vm._v(" "), (_vm.$slots.footer) ? _c('div', {
    staticClass: "el-dialog__footer"
  }, [_vm._t("footer")], 2) : _vm._e()])])])
},staticRenderFns: []}

/***/ }),
/* 356 */
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
  }, [(_vm.avatarUrl != '') ? _c('img', {
    staticClass: "personal-headimg",
    attrs: {
      "src": _vm.avatarUrl,
      "alt": ""
    }
  }) : _c('span', [_vm._v(_vm._s(_vm.getUserName))]), _vm._v(" "), _c('div', {
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
    staticClass: "alter-avatar",
    on: {
      "click": function($event) {
        $event.stopPropagation();
        $event.preventDefault();
        _vm.alterAvatar($event)
      }
    }
  }, [_vm._v("修改头像")]), _vm._v(" "), _c('div', {
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
  }), _vm._v(" "), _c('upload-avatar', {
    ref: "uploadAvatar"
  }), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "邮箱验证",
      "visible": _vm.emailVisible,
      "show-close": false,
      "close-on-press-escape": false,
      "close-on-click-modal": false,
      "size": "tiny"
    },
    on: {
      "update:visible": function($event) {
        _vm.emailVisible = $event
      }
    }
  }, [_c('el-input', {
    staticStyle: {
      "width": "100%"
    },
    attrs: {
      "placeholder": "请输入邮箱获取的验证码"
    },
    model: {
      value: (_vm.emailCode),
      callback: function($$v) {
        _vm.emailCode = $$v
      },
      expression: "emailCode"
    }
  }), _vm._v(" "), _c('el-button', {
    staticStyle: {
      "margin-top": "10px"
    },
    attrs: {
      "type": "warning"
    },
    on: {
      "click": _vm.sendEmail
    }
  }, [_vm._v("没有收到邮件？点击重新发送")]), _vm._v(" "), _c('el-button', {
    staticStyle: {
      "margin-left": "200px"
    },
    attrs: {
      "type": "primary"
    },
    on: {
      "click": _vm.vaidateEmail
    }
  }, [_vm._v("确定")])], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "选择部门",
      "visible": _vm.deptVisible,
      "show-close": false,
      "close-on-press-escape": false,
      "close-on-click-modal": false,
      "size": "tiny"
    },
    on: {
      "update:visible": function($event) {
        _vm.deptVisible = $event
      }
    }
  }, [_c('el-tree', {
    ref: "tree",
    attrs: {
      "data": _vm.deptOptions,
      "props": _vm.dept
    },
    on: {
      "node-click": _vm.deptChoose
    }
  }), _vm._v(" "), _c('div', {
    staticStyle: {
      "margin-bottom": "10px",
      "margin-top": "20px"
    }
  }, [_c('el-button', {
    staticStyle: {
      "margin-left": "25px"
    },
    attrs: {
      "type": "warning"
    },
    on: {
      "click": function($event) {
        _vm.addDeptVisible = true, _vm.deptName = ''
      }
    }
  }, [_vm._v("新建组织")]), _vm._v(" "), _c('el-button', {
    staticStyle: {
      "margin-left": "330px"
    },
    attrs: {
      "type": "primary"
    },
    on: {
      "click": _vm.deptChange
    }
  }, [_vm._v("确定")])], 1)], 1), _vm._v(" "), _c('el-dialog', {
    attrs: {
      "title": "新增组织",
      "visible": _vm.addDeptVisible,
      "size": "tiny"
    },
    on: {
      "update:visible": function($event) {
        _vm.addDeptVisible = $event
      }
    }
  }, [_c('el-input', {
    staticStyle: {
      "width": "80%"
    },
    attrs: {
      "placeholder": "请输入组织名称"
    },
    model: {
      value: (_vm.organization),
      callback: function($$v) {
        _vm.organization = $$v
      },
      expression: "organization"
    }
  }), _vm._v(" "), _c('div', {
    staticStyle: {
      "margin-bottom": "10px",
      "margin-top": "20px"
    }
  }, [_c('el-button', {
    staticStyle: {
      "margin-left": "330px"
    },
    attrs: {
      "type": "primary"
    },
    on: {
      "click": _vm.addDept
    }
  }, [_vm._v("确定")])], 1)], 1)], 1)
},staticRenderFns: [function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', {
    staticClass: "logo"
  }, [_c('img', {
    attrs: {
      "src": __webpack_require__(318),
      "alt": ""
    }
  })])
}]}

/***/ }),
/* 357 */
/***/ (function(module, exports, __webpack_require__) {

module.exports={render:function (){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;
  return _c('div', [_vm._l((_vm.taskItems), function(task) {
    return _c('div', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (_vm.viewType == 1),
        expression: "viewType==1"
      }],
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
        "src": __webpack_require__(12)
      }
    }) : (_vm.taskStatus == 'auditSuccess') ? _c('img', {
      attrs: {
        "src": __webpack_require__(25)
      }
    }) : ((_vm.taskStatus == 'TaskDoing' && task.reviewStatus == 3) || (_vm.taskStatus == 'taskList' && task.status < 3)) ? _c('img', {
      attrs: {
        "src": __webpack_require__(311)
      }
    }) : ((_vm.taskStatus == 'finished') || task.status == 3) ? _c('img', {
      attrs: {
        "src": __webpack_require__(312)
      }
    }) : (_vm.taskStatus == 'WaitAssess') ? _c('img', {
      attrs: {
        "src": __webpack_require__(319)
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
    }), _vm._v(" "), (task.status == 3 && task.stageName == '已发布') ? _c('span', {
      staticClass: "task-end purple"
    }, [_vm._v(_vm._s(task.endText))]) : _c('span', {
      staticClass: "task-end",
      class: task.endColor
    }, [_vm._v(_vm._s(task.endText))]), _vm._v(" "), (task.status == 1 && task.delayNo != 0 && task.delayNo != null) ? _c('span', {
      staticClass: "task-end orange"
    }, [_vm._v("超时人数:" + _vm._s(task.delayNo))]) : _vm._e(), _vm._v(" "), _c('span', {
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
      staticClass: "task-mark",
      staticStyle: {
        "position": "relative",
        "left": "-10px"
      }
    }, [(task.projectImage) ? _c('img', {
      staticStyle: {
        "width": "40px",
        "height": "40px",
        "border-radius": "50%"
      },
      attrs: {
        "src": task.projectImage
      }
    }) : _c('img', {
      attrs: {
        "src": __webpack_require__(169),
        "alt": ""
      }
    }), _vm._v(" "), _c('span', {
      staticClass: "mark-msg"
    }, [_vm._v(_vm._s(task.projectName))])]), _vm._v(" "), _c('div', {
      staticClass: "task-username"
    }, [(task.avatarUrl && task.avatarUrl != '') ? _c('img', {
      staticClass: "task-avatar",
      attrs: {
        "src": task.avatarUrl,
        "alt": task.userName
      }
    }) : _c('span', [_vm._v(_vm._s(task.userName))])])])
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
    attrs: {
      "slot": "footer"
    },
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
    attrs: {
      "slot": "footer"
    },
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
    staticStyle: {
      "white-space": "pre-wrap"
    },
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
      "label": "难易度："
    }
  }, _vm._l((_vm.facilityList), function(item) {
    return (item.value == _vm.taskDetail.facility) ? _c('span', [_vm._v(_vm._s(item.label))]) : _vm._e()
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
      class: _vm.taskStepStatus(item, _vm.taskDetail.users.length)
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
    attrs: {
      "slot": "footer"
    },
    slot: "footer"
  }, [_c('el-tooltip', {
    attrs: {
      "content": "评审任务",
      "placement": "top"
    }
  }, [_c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.userRole === 0 && _vm.taskDetail.examine === 0),
      expression: "userRole===0&&taskDetail.examine===0"
    }],
    staticStyle: {
      "text-align": "left"
    },
    attrs: {
      "type": "primary"
    },
    on: {
      "click": function($event) {
        _vm.examineTask(_vm.taskDetail.id, 1)
      }
    }
  }, [_vm._v("已评审")])], 1), _vm._v(" "), _c('el-tooltip', {
    attrs: {
      "content": "评审任务",
      "placement": "top"
    }
  }, [_c('el-button', {
    directives: [{
      name: "show",
      rawName: "v-show",
      value: (_vm.userRole === 0 && _vm.taskDetail.examine === 1),
      expression: "userRole===0&&taskDetail.examine===1"
    }],
    staticStyle: {
      "text-align": "left"
    },
    attrs: {
      "type": "danger"
    },
    on: {
      "click": function($event) {
        _vm.examineTask(_vm.taskDetail.id, 0)
      }
    }
  }, [_vm._v("未评审")])], 1), _vm._v(" "), _c('el-tooltip', {
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
        _vm.$set(_vm.modifyTaskForm, "modifyDescription", $$v)
      },
      expression: "modifyTaskForm.modifyDescription"
    }
  })], 1)], 1), _vm._v(" "), _c('div', {
    staticClass: "dialog-footer",
    attrs: {
      "slot": "footer"
    },
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
          _vm.$set(_vm.assessForm.comments[index], "grade", $$v)
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
          _vm.$set(_vm.assessForm.comments[index], "description", $$v)
        },
        expression: "assessForm.comments[index].description"
      }
    })], 1)], 1)], 1) : _c('div', [_c('p', [_vm._v("我的评价")]), _vm._v(" "), _c('div', [_c('p', [_vm._v("等级：" + _vm._s(stage.myComment.grade))]), _vm._v(" "), _c('p', [_vm._v("内容:" + _vm._s(stage.myComment.description))])])])])
  })), _vm._v(" "), _c('span', {
    staticClass: "dialog-footer",
    attrs: {
      "slot": "footer"
    },
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
    attrs: {
      "slot": "title"
    },
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
    attrs: {
      "slot": "label"
    },
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
        _vm.$set(_vm.modifyTaskForm, "taskName", $$v)
      },
      expression: "modifyTaskForm.taskName"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": ""
    }
  }, [_c('span', {
    attrs: {
      "slot": "label"
    },
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
        _vm.$set(_vm.modifyTaskForm, "projectId", $$v)
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
    attrs: {
      "slot": "label"
    },
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
        _vm.$set(_vm.modifyTaskForm, "priority", $$v)
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
    attrs: {
      "slot": "label"
    },
    slot: "label"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("难易度")]), _vm._v(" "), _c('el-select', {
    attrs: {
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.modifyTaskForm.facility),
      callback: function($$v) {
        _vm.$set(_vm.modifyTaskForm, "facility", $$v)
      },
      expression: "modifyTaskForm.facility"
    }
  }, _vm._l((_vm.facilityList), function(item) {
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
    attrs: {
      "slot": "label"
    },
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
        _vm.$set(_vm.modifyTaskForm, "endTime", $$v)
      },
      expression: "modifyTaskForm.endTime"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
    staticClass: "task-form-edit",
    attrs: {
      "label": ""
    }
  }, [_c('span', {
    attrs: {
      "slot": "label"
    },
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
        _vm.$set(_vm.modifyTaskForm, "stageId", $$v)
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
    attrs: {
      "slot": "label"
    },
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
        _vm.$set(_vm.modifyTaskForm, "tags", $$v)
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
    attrs: {
      "slot": "label"
    },
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
        _vm.$set(_vm.modifyTaskForm, "description", $$v)
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
      value: (_vm.otherStep.description),
      callback: function($$v) {
        _vm.$set(_vm.otherStep, "description", $$v)
      },
      expression: "otherStep.description"
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
        _vm.$set(_vm.step, "userId", $$v)
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
        _vm.$set(_vm.step, "taskHours", $$v)
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
        _vm.$set(_vm.step, "beginTime", $$v)
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
        _vm.$set(_vm.step, "endTime", $$v)
      },
      expression: "step.endTime"
    }
  })], 1)]), _vm._v(" "), _vm._l((_vm.sortWeekNumber), function(item, index) {
    return _c('div', [_c('div', {
      staticClass: "add-member-basic-list clearfix"
    }, [_c('div', {
      staticClass: "fl",
      staticStyle: {
        "margin-left": "5px"
      }
    }, [_c('span', {
      staticClass: "star"
    }, [_vm._v("*")]), _vm._v("第" + _vm._s(item.weekNumber) + "周工作量(" + _vm._s(item.range) + ")：")]), _vm._v(" "), _c('input', {
      directives: [{
        name: "model",
        rawName: "v-model",
        value: (item.hours),
        expression: "item.hours"
      }],
      staticClass: "member-time-week",
      staticStyle: {
        "width": "80px"
      },
      attrs: {
        "maxlength": 6,
        "placeholder": item.hoursTemp
      },
      domProps: {
        "value": (item.hours)
      },
      on: {
        "input": function($event) {
          if ($event.target.composing) { return; }
          _vm.$set(item, "hours", $event.target.value)
        }
      }
    }), _vm._v("    已有工作量:\n                              "), _c('div', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (parseFloat(item.weekHours == '' ? 0 : item.weekHours) + parseFloat(item.hours == '' ? 0 : item.hours) - parseFloat(!(item.hours == '' ? 0 : item.hoursTemp) ? 0 : item.hoursTemp) > 40),
        expression: "parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours) - parseFloat(!(item.hours==''?0:item.hoursTemp)?0:item.hoursTemp) > 40"
      }],
      staticClass: "f1",
      staticStyle: {
        "color": "red",
        "display": "inline"
      }
    }, [_vm._v(_vm._s(parseFloat(item.weekHours == '' ? 0 : item.weekHours) + parseFloat(item.hours == '' ? 0 : item.hours) - parseFloat(!(item.hours == '' ? 0 : item.hoursTemp) ? 0 : item.hoursTemp)))]), _vm._v(" "), _c('div', {
      directives: [{
        name: "show",
        rawName: "v-show",
        value: (parseFloat(item.weekHours == '' ? 0 : item.weekHours) + parseFloat(item.hours == '' ? 0 : item.hours) - parseFloat(!(item.hours == '' ? 0 : item.hoursTemp) ? 0 : item.hoursTemp) <= 40),
        expression: "parseFloat(item.weekHours==''?0:item.weekHours) + parseFloat(item.hours==''?0:item.hours) - parseFloat(!(item.hours==''?0:item.hoursTemp)?0:item.hoursTemp) <=40"
      }],
      staticClass: "f1",
      staticStyle: {
        "display": "inline"
      }
    }, [_vm._v(_vm._s(parseFloat(item.weekHours == '' ? 0 : item.weekHours) + parseFloat(item.hours == '' ? 0 : item.hours) - parseFloat(!(item.hours == '' ? 0 : item.hoursTemp) ? 0 : item.hoursTemp)))])])])
  }), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-list clearfix"
  }, [_c('div', {
    staticClass: "add-member-basic-menu fl"
  }, [_c('span', {
    staticClass: "star"
  }, [_vm._v("*")]), _vm._v("状态：")]), _vm._v(" "), _c('div', {
    staticClass: "add-member-basic-msg fl"
  }, [_c('el-select', {
    attrs: {
      "filterable": "",
      "placeholder": "请选择"
    },
    model: {
      value: (_vm.otherStep.status),
      callback: function($$v) {
        _vm.$set(_vm.otherStep, "status", $$v)
      },
      expression: "otherStep.status"
    }
  }, _vm._l((_vm.statusOptions), function(item) {
    return _c('el-option', {
      key: item.id,
      attrs: {
        "label": item.name,
        "value": item.id
      }
    })
  }))], 1)])], 2), _vm._v(" "), _c('div', {
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
    attrs: {
      "slot": "footer"
    },
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
        _vm.$set(_vm.modifyPrivateTaskForm, "projectId", $$v)
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
      "label": "开始日期"
    }
  }, [_c('el-date-picker', {
    attrs: {
      "type": "date",
      "format": "yyyy-MM-dd",
      "placeholder": "选择日期时间"
    },
    model: {
      value: (_vm.modifyPrivateTaskForm.beginTime),
      callback: function($$v) {
        _vm.$set(_vm.modifyPrivateTaskForm, "beginTime", $$v)
      },
      expression: "modifyPrivateTaskForm.beginTime"
    }
  })], 1), _vm._v(" "), _c('el-form-item', {
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
        _vm.$set(_vm.modifyPrivateTaskForm, "endTime", $$v)
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
      "maxlength": 6
    },
    model: {
      value: (_vm.modifyPrivateTaskForm.taskHours),
      callback: function($$v) {
        _vm.$set(_vm.modifyPrivateTaskForm, "taskHours", $$v)
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
        _vm.$set(_vm.modifyPrivateTaskForm, "taskName", $$v)
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
        _vm.$set(_vm.modifyPrivateTaskForm, "description", $$v)
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
        _vm.$set(_vm.modifyPrivateTaskForm, "stageId", $$v)
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
        _vm.$set(_vm.modifyPrivateTaskForm, "tags", $$v)
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
    attrs: {
      "slot": "footer"
    },
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

/***/ }),
/* 358 */
/***/ (function(module, exports) {

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
        _vm.$set(_vm.modifyForm, "originalPassword", $$v)
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
        _vm.$set(_vm.modifyForm, "newPassword", $$v)
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
        _vm.$set(_vm.modifyForm, "sureNewPwd", $$v)
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

/***/ }),
/* 359 */,
/* 360 */,
/* 361 */
/***/ (function(module, exports, __webpack_require__) {

var map = {
	"./icon_1.png": 313,
	"./icon_2.png": 314,
	"./icon_3.png": 315,
	"./icon_4.png": 316,
	"./icon_5.png": 317
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
webpackContext.id = 361;

/***/ })
],[252]);
//# sourceMappingURL=app.58ac74b2e3637d6e8fcc.js.map
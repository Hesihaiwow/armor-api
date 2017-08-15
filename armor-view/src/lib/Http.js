// http
import Axios from 'axios'
import { Message } from 'element-ui';

export default {
    //请求基础路径
    API_ROOT: function () {
        if (process.env.NODE_ENV === 'production'){
            return 'http://zsy.zhixinhuixue.com/';
        } else if (process.env.NODE_ENV === 'testing') {
            return 'http://zsy.dev.xueping.com/armor/api';
        } else {
            return 'http://localhost:10012/armor/';
        }
    },
    //所有请求URI
    API_URI:{
        //用户登陆
        LOGIN:'/user/login'
    },
    //请求方法
    HTTP_METHOD:{
        GET: "get",
        POST: "post",
        PUT: "put",
        DELETE: "delete"
    },
    //请求超时时间8s
    TIME_OUT: 8000,
    //返回数据类型JSON
    RESPONSE_TYPE: 'json',

    //AXIOS请求
   zsyHttpBase: function(_url, _method, _data = {}, success, fail, _catch) {
        let _this = this;
        //请求全局配置
        let config = {};
        config.baseURL = _this.API_ROOT();
        config.url = _url;
        if (_method && _method == _this.HTTP_METHOD.GET) {
            config.params = _data;
        } else if (_method && (_method == _this.HTTP_METHOD.POST
                || _method == _this.HTTP_METHOD.PUT)) {
            config.data = _data;
        }
        config.method = _method;
        config.timeout = _this.TIME_OUT;
        config.responseType = _this.RESPONSE_TYPE;
        config.headers = { 'Authorization': "Bearer " + window.localStorage.getItem("token") };
        Axios(config).then((response) => {
            let res = response.data;
            if (res.errCode != "00") {
                if (res.errCode == "02") {
                    //重新登录
                    _this.$router.push("/");
                    window.localStorage.clear();
                } else {
                    if (typeof fail == "function") {
                        fail(res);
                    } else {
                        if (res.errMsg != "") {
                            Message.error(res.errMsg);
                        }
                    }
                }
            } else {
                success && success(res);
            };
        }).catch((err) => {
            _catch && _catch(err);
            if (err.message.indexOf("Network Error") != -1) {
                Message.error('网络异常,请检查您的网络连接,请稍后重试.');
            } else if (err.message.indexOf("timeout") != -1) {
                Message.error('当前网络连接不给力，请稍后重试.');
            } else if (err.message.indexOf("code 503") != -1) {
                Message.error('当前服务不可用');
            } else {
                Message.error('系统异常');
            }
        })
    },
    // GET请求
    zsyGetHttp: function (_url, _data = {}, success, fail, _catch) {
        let _this = this;
        _this.zsyHttpBase(_url,_this.HTTP_METHOD.GET,_data,success,fail,_catch);
    },
    // POST请求
    zsyPostHttp: function (_url, _data = {}, success, fail, _catch) {
        let _this = this;
        _this.zsyHttpBase(_url,_this.HTTP_METHOD.POST,_data,success,fail,_catch);
    },
    // PUT请求
    zsyPutHttp: function (_url, _data = {}, success, fail, _catch) {
        let _this = this;
        _this.zsyHttpBase(_url,_this.HTTP_METHOD.PUT,_data,success,fail,_catch);
    },
    // DELETE请求
    zsyDeleteHttp: function (_url, _data = {}, success, fail, _catch) {
        let _this = this;
        _this.zsyHttpBase(_url,_this.HTTP_METHOD.DELETE,_data,success,fail,_catch);
    }
}
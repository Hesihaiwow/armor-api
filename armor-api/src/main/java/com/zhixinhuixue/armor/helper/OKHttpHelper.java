package com.zhixinhuixue.armor.helper;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.zhixinhuixue.armor.exception.ZSYApiException;
import com.zhixinhuixue.armor.exception.ZSYServiceException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.alibaba.fastjson.JSON.parseObject;
import static com.alibaba.fastjson.JSON.toJSONString;


/**
 * Created by Akuma on 2016/12/23.
 */
public class OKHttpHelper {

    /**
     * 连接超时时间(1分钟)
     */
    public static final int CONNECT_TIMEOUT = 10;
    /**
     * 读取超时时间(2分钟)
     */
    public static final int READ_TIMEOUT = 120;
    /**
     * 写的超时时间(2分钟)
     */
    public static final int WRITE_TIMEOUT = 120;

    /**
     * 请求头
     */
    public static final String HTTP_REQUEST_HEAD = "Authorization";

    /**
     * application/json;charset=utf-8
     */
    public static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * Post空数据
     */
    private static final RequestBody EMPTY_BODY = RequestBody.create(APPLICATION_JSON, new JSONObject().toJSONString());

    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(OKHttpHelper.class);

    /**
     * 客户端
     */
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build();


    /**
     * SSL客户端
     */
    private static final OkHttpClient sslClient = new OkHttpClient().newBuilder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .sslSocketFactory(createSSLSocketFactory(), new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }).hostnameVerifier((hostname, session) -> true)
            .build();

    /**
     * GET请求
     *
     * @param url 请求URL(参数可加在URL后)
     * @return
     */
    public static String get(String url) {
        isUrlNullOrEmpty(url);
        Request request = new Request.Builder().url(url).build();
        return handleResponse(request, client);
    }

    /**
     * GET请求
     *
     * @param url    请求URL
     * @param params 参数 k-v
     * @return
     */
    public static String get(String url, List<OkHttpParam> params) {
        isUrlNullOrEmpty(url);
        Request request = new Request.Builder().url(componentUrl(url, params)).build();
        return handleResponse(request, client);
    }

    /**
     * GET请求(basic验证)
     *
     * @param url      请求URL(参数可加在URL后)
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static String getWithBasic(String url, String userName, String password) {
        isUrlNullOrEmpty(url);
        String credential = Credentials.basic(userName, password);
        Request request = new Request.Builder().header(HTTP_REQUEST_HEAD, credential).url(url).build();
        return handleResponse(request, client);
    }

    /**
     * GET请求(basic验证)
     *
     * @param url      请求URL
     * @param params   参数 k-v
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static String getWithBasic(String url, List<OkHttpParam> params, String userName, String password) {
        isUrlNullOrEmpty(url);
        String credential = Credentials.basic(userName, password);
        Request request = new Request.Builder().header(HTTP_REQUEST_HEAD, credential).url(componentUrl(url, params)).build();
        return handleResponse(request, client);
    }


    /**
     * GET请求
     *
     * @param url 请求URL(参数可加在URL后)
     * @return
     */
    public static String sslGet(String url) {
        isUrlNullOrEmpty(url);
        Request request = new Request.Builder().url(url).build();
        return handleResponse(request, sslClient);
    }

    /**
     * GET请求
     *
     * @param url    请求URL
     * @param params 参数 k-v
     * @return
     */
    public static String sslGet(String url, List<OkHttpParam> params) {
        isUrlNullOrEmpty(url);
        Request request = new Request.Builder().url(componentUrl(url, params)).build();
        return handleResponse(request, sslClient);
    }


    /**
     * POST请求
     *
     * @param url 请求URL(参数可加在URL后)
     * @return
     */
    public static String post(String url) {
        isUrlNullOrEmpty(url);
        Request request = new Request.Builder().url(url).post(EMPTY_BODY).build();
        return handleResponse(request, client);
    }

    /**
     * POST请求
     *
     * @param url    请求URL
     * @param params 参数 k-v
     * @return
     */
    public static String post(String url, List<OkHttpParam> params) {
        isUrlNullOrEmpty(url);
        RequestBody body = RequestBody.create(APPLICATION_JSON, convertListToJson(params));
        Request request = new Request.Builder().url(url).post(body).build();
        return handleResponse(request, client);
    }

    /**
     * POST请求
     *
     * @param url  请求URL
     * @param json 参数 k-v
     * @return
     */
    public static String post(String url, String json) {
        isUrlNullOrEmpty(url);
        RequestBody body = RequestBody.create(APPLICATION_JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        return handleResponse(request, client);
    }

    /**
     * POST请求(basic验证)
     *
     * @param url      请求URL
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static String postWithBasic(String url, String userName, String password) {
        isUrlNullOrEmpty(url);
        String credential = Credentials.basic(userName, password);
        Request request = new Request.Builder().header(HTTP_REQUEST_HEAD, credential).url(url).post(EMPTY_BODY).build();
        return handleResponse(request, client);
    }

    /**
     * GET请求(basic验证)
     *
     * @param url      请求URL
     * @param params   参数 k-v
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static String postWithBasic(String url, List<OkHttpParam> params, String userName, String password) {
        isUrlNullOrEmpty(url);
        RequestBody body = RequestBody.create(APPLICATION_JSON, convertListToJson(params));
        String credential = Credentials.basic(userName, password);
        Request request = new Request.Builder().header(HTTP_REQUEST_HEAD, credential).url(url).post(body).build();
        return handleResponse(request, client);
    }

    /**
     * GET请求(basic验证)
     *
     * @param url      请求URL
     * @param json     参数 k-v
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static String postWithBasic(String url, String json, String userName, String password) {
        isUrlNullOrEmpty(url);
        if (logger.isDebugEnabled()) {
            logger.debug("Http Request Parameters:{}", json);
        }
        RequestBody body = RequestBody.create(APPLICATION_JSON, json);
        String credential = Credentials.basic(userName, password);
        Request request = new Request.Builder().header(HTTP_REQUEST_HEAD, credential).url(url).post(body).build();
        return handleResponse(request, client);
    }


    /**
     * POST请求
     *
     * @param url 请求URL(参数可加在URL后)
     * @return
     */
    public static String sslPost(String url) {
        isUrlNullOrEmpty(url);
        Request request = new Request.Builder().url(url).post(EMPTY_BODY).build();
        return handleResponse(request, sslClient);
    }

    /**
     * POST请求
     *
     * @param url    请求URL
     * @param params 参数 k-v
     * @return
     */
    public static String sslPost(String url, List<OkHttpParam> params) {
        isUrlNullOrEmpty(url);
        RequestBody body = RequestBody.create(APPLICATION_JSON, convertListToJson(params));
        Request request = new Request.Builder().url(url).post(body).build();
        return handleResponse(request, sslClient);
    }

    /**
     * POST请求
     *
     * @param url  请求URL
     * @param json 参数 k-v
     * @return
     */
    public static String sslPost(String url, String json) {
        isUrlNullOrEmpty(url);
        if (logger.isDebugEnabled()) {
            logger.debug("Http Request Parameters:{}", json);
        }
        RequestBody body = RequestBody.create(APPLICATION_JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        return handleResponse(request, sslClient);
    }


    public static OkHttpClient getClient() {
        return client;
    }

    /**
     * 组装URl
     *
     * @param url    请求url
     * @param params 请求参数
     * @return
     */
    public static String componentUrl(String url, List<OkHttpParam> params) {
        if (!CollectionUtils.isEmpty(params)) {
            StringBuilder stringBuffer = new StringBuilder(url);
            if (!url.substring(url.length() - 1).equals("?")) {
                stringBuffer.append("?");
            }
            params.forEach(okHttpParam ->
                    stringBuffer.append(String.format("%s=%s&", okHttpParam.getKey(), okHttpParam.getValue()))
            );
            url = stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1);
            if (logger.isDebugEnabled()) {
                logger.debug("Http Request Parameters:{}", toJSONString(params));
            }
        }
        return url;
    }

    /**
     * 发送请求，处理请求
     *
     * @param request
     * @return
     */
    public static String handleResponse(Request request, OkHttpClient client) {
        try {
            long var1 = System.currentTimeMillis();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("Request Time: %s,Request Result: %s.", String.valueOf(System.currentTimeMillis() - var1), result));
                }
                return result;
            } else {
                throw new ZSYServiceException(String.format("Request %s Fail,Error Code: %s.", request.url().url(), response.code()));
            }
        } catch (IOException e) {
            throw new ZSYServiceException(e.getMessage());
        }
    }

    /**
     * 将List转化成JSON
     *
     * @param params
     * @return
     */
    private static String convertListToJson(List<OkHttpParam> params) {
        JSONObject object = new JSONObject();
        if (!CollectionUtils.isEmpty(params)) {
            params.forEach(okHttpParam ->
                    object.put(okHttpParam.getKey(), okHttpParam.getValue())
            );
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Http Request Parameters:{}", object.toJSONString());
        }
        return object.toJSONString();
    }

    /**
     * 校验请求url
     *
     * @param url
     */
    private static void isUrlNullOrEmpty(String url) {
        if (Strings.isNullOrEmpty(url)) {
            throw new ZSYServiceException("Http Request Uri is null or empty!");
        }
        logger.info("Http Request Uri:{}", url);
    }

    /**
     * OKHttp请求参数
     */
    public static class OkHttpParam {

        private String key;
        private String value;

        public OkHttpParam(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 判断zsy接口返回是否正常
     *
     * @param result
     * @return
     */
    public static ZsyApiResult convertZsyApiCallBack(String result) {
        if (result.contains("{\"status\":0,\"error\":")) {
            return new ZsyApiResult(false, parseObject(result).getString("error"), null);
        }
        if (result.contains("{\"success\":0,")) {
            return new ZsyApiResult(false, parseObject(result).getString("msg"), null);
        }
        return new ZsyApiResult(true, "", result);
    }

    /**
     * 判断api接口返回是否正常
     *
     * @param result
     * @return
     */
    public static ZsyApiResult convertApiCallBack4Java(String result) {
        if (!result.contains("\"errCode\":\"00\"")) {
            return new ZsyApiResult(false, parseObject(result).getString("errMsg"), null);
        }
        return new ZsyApiResult(true, "", result);
    }

    public static class ZsyApiResult {

        public ZsyApiResult(boolean success, String errMsg, String data) {
            this.success = success;
            this.errMsg = errMsg;
            this.data = data;
        }

        private boolean success;
        private String errMsg;
        private String data;

        public boolean isSuccess() {
            return success;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public String get() {
            if (this.success) {
                return this.data;
            } else {
                throw new ZSYApiException(this.errMsg);
            }
        }
    }


    /**
     * 创建SSL链接工厂
     *
     * @return
     */
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;
        try {
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, null, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            sSLSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new ZSYServiceException(e.getMessage());
        }
        return sSLSocketFactory;
    }

}

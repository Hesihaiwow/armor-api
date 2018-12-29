package com.zhixinhuixue.armor.helper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import com.zhixinhuixue.armor.exception.ZSYApiException;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by Akuma on 2016/12/23.
 */
public class ZSYOKHttpHelper {

    /**
     * 请求头
     */
    public static final String HTTP_REQUEST_HEAD = "Authorization";


    //连接超时时间(1分钟)
    public static final int CONNECT_TIMEOUT = 60;
    //读取超时时间(2分钟)
    public static final int READ_TIMEOUT = 120;
    //写的超时时间(2分钟)
    public static final int WRITE_TIMEOUT = 120;

    //application/json utf-8
    public static final MediaType APPLICATION_JSON = MediaType.parse("application/json; charset=utf-8");

    //post空数据
    private static final RequestBody EMPTY_BODY = RequestBody.create(APPLICATION_JSON, new JSONObject().toJSONString());

    private static final Logger logger = LoggerFactory.getLogger(ZSYOKHttpHelper.class);

    //OkHttp客户端
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .build();


    //SSL OkHttp客户端
    private static final OkHttpClient sslClient = new OkHttpClient().newBuilder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .sslSocketFactory(createSSLSocketFactory(), new X509TrustManager() {
                @Override
                public void checkClientTrusted(
                        X509Certificate[] chain,
                        String authType) {
                }

                @Override
                public void checkServerTrusted(
                        X509Certificate[] chain,
                        String authType) {
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
    public static String authGet(String url, String userName, String password) {
        isUrlNullOrEmpty(url);
        String credential = Credentials.basic(userName, password);
        Request request = new Request.Builder().header("Authorization", credential).url(url).build();
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
    public static String authGet(String url, List<OkHttpParam> params, String userName, String password) {
        isUrlNullOrEmpty(url);
        String credential = Credentials.basic(userName, password);
        Request request = new Request.Builder().header("Authorization", credential).url(componentUrl(url, params)).build();
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
    public static String authPost(String url, String userName, String password) {
        isUrlNullOrEmpty(url);
        String credential = Credentials.basic(userName, password);
        Request request = new Request.Builder().header("Authorization", credential).url(url).post(EMPTY_BODY).build();
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
    public static String authPost(String url, List<OkHttpParam> params, String userName, String password) {
        isUrlNullOrEmpty(url);
        RequestBody body = RequestBody.create(APPLICATION_JSON, convertListToJson(params));
        String credential = Credentials.basic(userName, password);
        Request request = new Request.Builder().header("Authorization", credential).url(url).post(body).build();
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
    public static String authPost(String url, String json, String userName, String password) {
        isUrlNullOrEmpty(url);
        logger.info("请求参数:{}", json);
        RequestBody body = RequestBody.create(APPLICATION_JSON, json);
        String credential = Credentials.basic(userName, password);
        Request request = new Request.Builder().header("Authorization", credential).url(url).post(body).build();
        return handleResponse(request, client);
    }


    /**
     * PUT请求(basic验证)
     *
     * @param url      请求URL
     * @param json     参数 k-v
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static String authPut(String url, String json, String userName, String password) {
        isUrlNullOrEmpty(url);
        logger.info("请求参数:{}", json);
        RequestBody body = RequestBody.create(APPLICATION_JSON, json);
        String credential = Credentials.basic(userName, password);
        Request request = new Request.Builder().header("Authorization", credential).url(url).put(body).build();
        return handleResponse(request, client);
    }

    /**
     * PUT请求(basic验证)
     *
     * @param url      请求URL
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    public static String authPut(String url, String userName, String password) {
        isUrlNullOrEmpty(url);
        String credential = Credentials.basic(userName, password);
        Request request = new Request.Builder().header("Authorization", credential).url(url).put(EMPTY_BODY).build();
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
        logger.info("请求参数:{}", json);
        RequestBody body = RequestBody.create(APPLICATION_JSON, json);
        Request request = new Request.Builder().url(url).post(body).build();
        return handleResponse(request, sslClient);
    }


    /**
     * 组装URl
     *
     * @param url    请求url
     * @param params 请求参数
     * @return
     */
    public static String componentUrl(String url, List<OkHttpParam> params) {
        if (params != null && !params.isEmpty()) {
            StringBuffer stringBuffer = new StringBuffer(url);
            if (!url.substring(url.length() - 1, url.length()).equals("?")) {
                stringBuffer.append("?");
            }
            params.forEach(okHttpParam -> stringBuffer.append(String.format("%s=%s&", okHttpParam.getKey(), okHttpParam.getValue())));
            url = stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1);
            logger.info("请求参数:{}", JSONArray.toJSONString(params));
        }
        return url;
    }

    /**
     * 发送请求，处理请求
     *
     * @param request
     * @return
     */
    private static String handleResponse(Request request, OkHttpClient client) {
        try {
            long var1 = System.currentTimeMillis();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                if (logger.isDebugEnabled()) {
                    logger.debug(String.format("请求时间：%s,请求结果：%s.", String.valueOf(System.currentTimeMillis() - var1), result));
                }
                return result;
            } else {
                throw new ZSYApiException(String.format("请求 %s 失败,失败码:%s.", request.url().url(), response.code()));
            }
        } catch (IOException e) {
            throw new ZSYApiException(e.getMessage());
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
        if (params != null && !params.isEmpty()) {
            params.stream().forEach(okHttpParam -> object.put(okHttpParam.getKey(), okHttpParam.getValue()));
        }
        logger.info("请求参数:{}", object.toJSONString());
        return object.toJSONString();
    }

    /**
     * 校验请求url
     *
     * @param url
     */
    private static void isUrlNullOrEmpty(String url) {
        if (Strings.isNullOrEmpty(url)) {
            throw new ZSYApiException("请求url为空.");
        }
        logger.info("请求URL:{}", url);
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
    public static ZSYOKHttpHelper.IMSApiResult convertAPICallBack(String result) {
        JSONObject resultObj = JSONObject.parseObject(result);
        Object errCode = resultObj.get("errCode");
        if (errCode == null || !String.valueOf(errCode).equals("00")) {
            logger.info("调用远程接口失败：{}", resultObj.getString("errMsg"));
            return new ZSYOKHttpHelper.IMSApiResult(false, resultObj.getString("errMsg"), null);
        }
        return new ZSYOKHttpHelper.IMSApiResult(true, "", resultObj.getString("data"));
    }

    public static class IMSApiResult {

        public IMSApiResult(boolean success, String errMsg, String data) {
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
            e.printStackTrace();
        }
        return sSLSocketFactory;
    }

}

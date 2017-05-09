package com.miuhouse.yourcompany.student.http;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HttpStack;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**使用OkHttp实现Volley的传输层部分
 * Created by khb on 2016/5/12.
 */
public class OkHttpStack implements HttpStack {

    private final OkHttpClient mClient;

    public OkHttpStack(OkHttpClient mClient) {
        this.mClient = mClient;
    }

    private static void setConnectionParametersForRequest(okhttp3.Request.Builder builder, Request<?> request)
            throws AuthFailureError {
        switch (request.getMethod()){
            case Request.Method.DEPRECATED_GET_OR_POST:
                byte[] postBody = request.getPostBody();
                if (postBody != null){
                    builder.post(RequestBody.create(MediaType.parse(request.getPostBodyContentType()), postBody));
                }
                break;
            case Request.Method.GET:
                builder.get();
                break;
            case Request.Method.DELETE:
                builder.delete();
                break;
            case Request.Method.POST:
                builder.post(createRequestBody(request));
                break;
            case Request.Method.PUT:
                builder.post(createRequestBody(request));
                break;
            case Request.Method.HEAD:
                builder.head();
                break;
            case Request.Method.OPTIONS:
                builder.method("OPTIONS", null);
                break;
            case Request.Method.TRACE:
                builder.method("TRACE", null);
                break;
            case Request.Method.PATCH:
                builder.patch(createRequestBody(request));
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static RequestBody createRequestBody(Request<?> request) throws AuthFailureError {
        final byte[] body = request.getBody();
        if (body == null){
            return null;
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse(request.getBodyContentType()), body);
        return requestBody;
    }

    @SuppressWarnings("deprecation")
    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders)
            throws IOException, AuthFailureError {
        int timeoutMs = request.getTimeoutMs();
        OkHttpClient client = mClient.newBuilder()
                .readTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .connectTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .writeTimeout(timeoutMs, TimeUnit.MILLISECONDS)
                .build();
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        Map<String, String> header = request.getHeaders();
        for (final String name :
                header.keySet()) {
            builder.addHeader(name, header.get(name));
        }
        for (final String name :
                additionalHeaders.keySet()) {
            builder.addHeader(name, additionalHeaders.get(name));
        }
        setConnectionParametersForRequest(builder, request);
        okhttp3.Request okHttp3Request = builder.url(request.getUrl()).build();
        Response response = client.newCall(okHttp3Request).execute();
        Protocol protocol = response.protocol();
        ProtocolVersion version = parseProtocol(protocol);
        BasicStatusLine responseStatus = new BasicStatusLine(version, response.code(), response.message());
        BasicHttpResponse basicHttpResponse = new BasicHttpResponse(responseStatus);
        basicHttpResponse.setEntity(entityFromOkHttpResponse(response));
        Headers responseHeaders = response.headers();
        for (int i = 0, len = responseHeaders.size(); i<len; i++){
            final String name = responseHeaders.name(i);
            final String value = responseHeaders.value(i);
            if (name != null){
                basicHttpResponse.addHeader(name, value);
            }
        }
        return basicHttpResponse;
    }


    @SuppressWarnings("deprecation")
    private static ProtocolVersion parseProtocol(Protocol protocol) {
        switch (protocol){
            case HTTP_1_0:
                return new ProtocolVersion("HTTP", 1, 0);
            case HTTP_1_1:
                return new ProtocolVersion("HTTP", 1, 1);
            case HTTP_2:
                return new ProtocolVersion("HTTP", 2, 0);
            case SPDY_3:
                return new ProtocolVersion("SPDY", 3, 1);
        }
        throw new IllegalAccessError("Unknown protocol");
    }

    @SuppressWarnings("deprecation")
    private static HttpEntity entityFromOkHttpResponse(Response response)
            throws IOException {
        BasicHttpEntity entity = new BasicHttpEntity();
        ResponseBody body = response.body();
        entity.setContent(body.byteStream());
        entity.setContentEncoding(response.header("Content-Encoding"));
        if (body.contentType() != null){
            entity.setContentType(body.contentType().type());
        }
        return entity;
    }
}

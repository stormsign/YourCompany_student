package com.miuhouse.yourcompany.student.http.requestbody;


import com.miuhouse.yourcompany.student.http.listener.ProgressListener;
import com.miuhouse.yourcompany.student.http.listener.ProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/** 带进度回调的请求
 * Created by khb on 2016/5/18.
 */
public class ProgressRequestBody extends RequestBody {

    private RequestBody requestBody;
    private ProgressListener progressListener;
    private BufferedSink bufferedSink;

    public ProgressRequestBody(RequestBody requestBody, ProgressListener progressListener){
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }



    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null){
            bufferedSink = Okio.buffer(sink(sink));
        }
        requestBody.writeTo(bufferedSink);
        bufferedSink.flush();
    }

    private Sink sink(BufferedSink sink) {
        return new ForwardingSink(sink) {
            long bytesRead = 0L;
            long totalBytes = 0L;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (totalBytes == 0L){
                    totalBytes = contentLength();
                }
                bytesRead += byteCount;
                progressListener.onProgress(bytesRead, totalBytes, bytesRead == contentLength());
            }
        };
    }

}

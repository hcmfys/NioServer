package org.springbus.test.http2;

import okhttp3.*;
import okhttp3.internal.http2.Http2Connection;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class OkHttp2Test {


    public static void main(String[] args) {
        OkHttpClient.Builder builder = new OkHttpClient().
                newBuilder().followRedirects(false);
       builder.protocols(Arrays.asList( Protocol.HTTP_2,Protocol.HTTP_1_1));
        builder.pingInterval(1, TimeUnit.SECONDS);



        final Request request = new Request.Builder()
                .url("https://g.alicdn.com")
                .header("Connection","Upgrade")
                .header("Upgrade","HTTP/2.0")
                .get()
                .build();



        OkHttpClient client = builder.build();

        client.newCall(request).enqueue(new Callback() {
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            public void onResponse(Call call, Response response) throws IOException {
                response.request().headers().toString();


                ResponseBody body = response.body();
                String ret = body.string();
                System.out.println(response.protocol());
                System.out.println(response.headers());
                //System.out.println(response.request().headers().toString());
                System.out.println(response.handshake().tlsVersion());
               System.out.println(ret);
                response.close();
                response.body().close();

            }
        });

    }
}

package com.example.cloudconfigclient;

import okhttp3.*;
import okio.BufferedSink;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RefreshScope // 使用该注解的类，会在接到SpringCloud配置中心配置刷新的时候，自动将新的配置更新到该类对应的字段中。
public class HelloController {
    @Value("${neo.hello}")
    private String hello;

    @RequestMapping("/hello")
    public String from() {
        return this.hello;
    }

    public String refresh(){
        Request request = new Request.Builder().url("http://localhost:8002/refresh").post(new RequestBody() {
            @Override
            public MediaType contentType() {
                return null;
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
            }
        }).build();

        Response response = null;
        try {
            response = new OkHttpClient().newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return response.toString();
    }

    public static void main(String...args){
        System.out.println(new HelloController().refresh());
    }
}

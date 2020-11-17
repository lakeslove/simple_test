package com.lxtest.springbootdemo.test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.lxtest.springbootdemo.test.common.AbstractTest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.Test;


public class SimpleTest extends AbstractTest {

  @Test
  public void getTest1() throws Exception {
    // 从响应模型中获取响应实体
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpGet httpGet = new HttpGet("https://x-agent3.i-counting.cn/api/5b8607fead29e30001932fd3/aid-accounting/%E9%93%B6%E8%A1%8C%E8%B4%A6%E6%88%B7");
    httpGet.addHeader("authorization","Bearer 4e652bfb-60a9-4c9a-8f14-7f88816b9716");
    CloseableHttpResponse response = httpClient.execute(httpGet);

    //解析返回结果
    HttpEntity responseEntity = response.getEntity();
    String returnResult = getJsonString(responseEntity);
    System.out.println(returnResult);

    //验证结果
    String expectResult = readJsonFromResource("aidAccountingList.json");
    System.out.println(expectResult);
    assertJsonObject(
        listFromJsonString(expectResult, new TypeReference<List>(){}),
        listFromJsonString(returnResult,new TypeReference<List>(){})
    );
  }




}

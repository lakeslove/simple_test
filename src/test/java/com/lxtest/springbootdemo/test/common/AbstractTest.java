package com.lxtest.springbootdemo.test.common;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.io.Resources;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.json.JSONException;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.yaml.snakeyaml.Yaml;

public class AbstractTest {
  Map<String, String> fileCache = new ConcurrentHashMap<>();
  static final ObjectMapper objectMapper = new ObjectMapper();
  {
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.registerModule(new JavaTimeModule());
  }

  /**
   * 获取文件流.
   *
   * @param path 文件路径
   */
  public String fromResource(String path, Charset charset) {
    if (fileCache.containsKey(path)) {
      return fileCache.get(path);
    }

    try {
      String content = Resources.toString(Resources.getResource(path), charset);
      fileCache.put(path, content);
      return content;
    } catch (IOException e) {
      throw new IllegalStateException("文件读取失败: " + path, e);
    }
  }

  public String fromResource(String path) {
    return fromResource(path, Charsets.UTF_8);
  }

  /**
   * 将json转换为对象.
   *
   * @param path 文件路径
   */
  public <T> T fromJson(String path, Class<T> cls) {
    try {
      return objectMapper.readValue(this.fromResource(path, Charsets.UTF_8), cls);
    } catch (Exception e) {
      throw new IllegalStateException("读取json失败:" + path, e);
    }
  }

  /**
   * 将json转换为对象.
   *
   * @param path 文件路径
   */
  public <T> T fromJson(String path, TypeReference typeReference) {
    try {
      return objectMapper.readValue(this.fromResource(path, Charsets.UTF_8), typeReference);
    } catch (Exception e) {
      throw new IllegalStateException("读取json失败:" + path, e);
    }
  }

  /**
   * 获取测试资源文件.
   * @param name 文件名
   */
  public static File getResource(String name) {
    ClassLoader classLoader = AbstractTest.class.getClassLoader();
    File file = new File(classLoader.getResource(name).getFile());
    return file;
  }

  public String readJsonFromResource(String path) {
    return this.fromResource(path, com.google.common.base.Charsets.UTF_8);
  }

  /**
   * 将json数组转换为对象列表.
   *
   * @param path 文件路径
   */
  public <T> List<T> listFromJson(String path, TypeReference typeReference) {
    try {
      return objectMapper.readValue(fromResource(path, Charsets.UTF_8), typeReference);
    } catch (Exception e) {
      throw new IllegalStateException("读取json失败:" + path, e);
    }
  }


  /**
   * 将json数组转换为对象列表.
   *
   */
  public <T> List<T> listFromJsonString(String JSON, TypeReference typeReference) {
    try {
      return objectMapper.readValue(JSON, typeReference);
    } catch (Exception e) {
      throw new IllegalStateException("解析json失败:" + JSON, e);
    }
  }


  protected <T> T fromObject(Object value, Class<T> valueType) {
    try {
      return objectMapper.readValue(toJson(value), valueType);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException("转化Object失败", e);
    }
  }

  protected Map toMap(Object value) {
    try {
      return objectMapper.readValue(toJson(value), Map.class);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException("转化map失败", e);
    }
  }

  protected String toJson(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException("转化json失败", e);
    }
  }

  protected void assertJsonResource(
      final String resourceName,
      final Object actual,
      final String... ignoreFieldRegexPath) {
    String expectedJson = readJsonFromResource(resourceName);
    String actualJson = toJson(actual);
    assertJsonString(expectedJson, actualJson, ignoreFieldRegexPath);
  }

  protected void assertJsonObject(
      final Object expected,
      final Object actual,
      final String... ignoreFieldRegexPath) {

    String expectedJson = toJson(tryToList(expected));
    String actualJson = toJson(tryToList(actual));
    assertJsonString(expectedJson, actualJson, ignoreFieldRegexPath);
  }

  private Object tryToList(Object object) {
    if (object instanceof Set) {
      Set set = (Set) object;
      ArrayList list = new ArrayList(set);
      list.sort(Comparator.naturalOrder());
      return list;
    }
    return object;
  }

  protected void assertJsonString(
      final String expectedJson,
      final String actualJson,
      final String... ignoreFieldRegexPath) {
    try {
      DefaultComparator customComparator = new CustomIgnoreComparator(
          JSONCompareMode.STRICT, ignoreFieldRegexPath
      );
      JSONAssert.assertEquals(expectedJson, actualJson, customComparator);
    } catch (org.json.JSONException e) {
      e.printStackTrace();
      Assert.fail(expectedJson);
    }
  }

  protected <T> void assertSet(Set<T> expected, Set<T> actual) {
    if (expected == null) {
      Assert.assertNull(actual);
    } else {
      Assert.assertEquals(expected.size(), actual.size());
      for (T item : expected) {
        Assert.assertTrue(actual.contains(item));
      }
    }
  }


  public static class CustomIgnoreComparator extends DefaultComparator {

    private List<Pattern> ignoreFieldRegexPathList = null;

    CustomIgnoreComparator(JSONCompareMode mode, String... ignoreFieldRegexPath) {
      super(mode);
      if (ignoreFieldRegexPath != null) {
        this.ignoreFieldRegexPathList = Arrays.stream(ignoreFieldRegexPath)
            .map(Pattern::compile)
            .collect(Collectors.toList());
      }
    }

    @Override
    public void compareValues(
        String prefix,
        Object expectedValue,
        Object actualValue,
        JSONCompareResult result) throws JSONException {
      if (matches(prefix)) {
        return;
      }
      super.compareValues(prefix, expectedValue, actualValue, result);
    }

    private boolean matches(String path) {
      if (ignoreFieldRegexPathList == null) {
        return false;
      }
      for (Pattern pattern : ignoreFieldRegexPathList) {
        if (pattern.matcher(path).matches()) {
          return true;
        }
      }
      return false;
    }
  }

  protected Consumer<EntityExchangeResult<byte[]>> assertJsonFile(
      final String jsonFile,
      final String... ignoreFieldRegexPath) {
    String expectedJson = readJsonFromResource(jsonFile);
    return x -> assertJsonString(expectedJson, new String(x.getResponseBody(), com.google.common.base.Charsets.UTF_8), ignoreFieldRegexPath);
  }

  public String getJsonString(HttpEntity responseEntity) throws Exception {
    byte[] responseBytes = getData(responseEntity);
    return new String(responseBytes);
  }

  public static byte[] getData(HttpEntity httpEntity) throws Exception{
    BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(httpEntity);
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    bufferedHttpEntity.writeTo(byteArrayOutputStream);
    byte[] responseBytes = byteArrayOutputStream.toByteArray();
    return responseBytes;
  }



}

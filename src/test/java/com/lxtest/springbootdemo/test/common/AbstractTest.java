package com.lxtest.springbootdemo.test.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.security.auth.Subject;
import org.json.JSONException;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ReactiveHttpOutputMessage;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;

public abstract class AbstractTest {

  private final ObjectMapper objectMapper = new ObjectMapper() {
    {
      this.registerModule(new JacksonConfig.MoneyModule());
      this.registerModule(new JavaTimeModule());
    }
  };

  ObjectMapper injectObjectMapper;

  @Autowired(required = false)
  public void setInjectObjectMapper(ObjectMapper injectObjectMapper) {
    injectObjectMapper.registerModule(new MoneyJacksonConfig.MoneyModule());
    this.injectObjectMapper = injectObjectMapper;
  }

  /**
   * 获取测试资源文件.
   *
   * @param name 文件名
   */
  public static File getResource(String name) {
    ClassLoader classLoader = AbstractTest.class.getClassLoader();
    File file = new File(classLoader.getResource(name).getFile());
    return file;
  }

  private ObjectMapper getObjectMapper() {
    if (injectObjectMapper != null) {
      return injectObjectMapper;
    }
    return objectMapper;
  }

  protected JsonPathExpression buildJsonPath(Object value) {
    try {
      return JsonPathExpression.parse(getObjectMapper().writeValueAsString(value));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    throw new RuntimeException("buildJsonPathExpression");
  }

  protected <T> T fromJson(String path, Class<T> cls) {
    try {
      String json = readJsonFromResource(path);
      return getObjectMapper().readValue(json, cls);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException("反序列化json失败:" + path, e);
    }
  }

  protected <T> T fromJson(String path, TypeReference typeReference) {
    try {
      String json = readJsonFromResource(path);
      return getObjectMapper().readValue(json, typeReference);
    } catch (Exception e) {
      e.printStackTrace();
      throw new IllegalStateException("反序列化json失败:" + path, e);
    }
  }

  protected Map<String, Subject> buildSubjectsFromJson(String jsonPath) {
    return fromJson(
        jsonPath,
        new TypeReference<Map<String, Subject>>() {
        }
    );
  }

  protected BodyInserter<Object, ReactiveHttpOutputMessage> buildBodyFromJsonFile(String filePath) {
    return BodyInserters.fromObject(fromJson(filePath, Object.class));
  }

  protected BodyInserter<Object, ReactiveHttpOutputMessage> buildBodyFromObject(Object obj) {
    return BodyInserters.fromObject(obj);
  }

  private String readJsonFromResource(String path) {
    return this.fromResource(path, Charsets.UTF_8);
  }

  private String fromResource(String path, Charset charset) {
    try {
      return Resources.toString(Resources.getResource(path), charset);
    } catch (IOException e) {
      e.printStackTrace();
      throw new IllegalStateException("文件读取失败: " + path, e);
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

  protected <K, V> Map<K, V> singletonMap(Function<V, K> key, V value) {
    return Collections.singletonMap(key.apply(value), value);
  }

  protected <K, V> Map<K, V> unmodifiableMap(Function<V, K> key, V... values) {
    Map<K, V> map = new HashMap<>(values.length);
    for (V value : values) {
      map.put(key.apply(value), value);
    }
    return Collections.unmodifiableMap(map);
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

  protected List<Voucher> buildVoucherList(String file) {
    return fromJson(
        file,
        new TypeReference<List<Voucher>>() {
        }
    );
  }

  protected Map<String, Employee> buildEmployeeMap(String file) {
    return fromJson(
        file,
        new TypeReference<Map<String, Employee>>() {
        }
    );
  }

  protected Map<InsuranceType, Insurance> buildInsuranceMap(String file) {
    return fromJson(
        file,
        new TypeReference<Map<InsuranceType, Insurance>>() {
        }
    );
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
    } catch (JSONException e) {
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
    return x -> assertJsonString(expectedJson, new String(x.getResponseBody(), Charsets.UTF_8), ignoreFieldRegexPath);
  }

}

package com.hero.ddd.infrastructure.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public final class Values {
  private Values() {
    throw new AssertionError("noop");
  }

  /**
   * 如果Set为null，则返回{@code Collections#emptySet()}，否则原样返回。
   * 
   * @param collection 集合数据
   * @return 如果Set为null，则返回{@code Collections#emptySet()}，否则原样返回。
   */
  public static <T> Set<T> orEmpty(Set<T> collection) {
    if (collection == null) {
      return Collections.emptySet();
    }
    return collection;
  }

  /**
   * 如果List为null，则返回{@code Collections#emptyList()}，否则原样返回。
   * 
   * @param collection 集合数据
   * @return 如果List为null，则返回{@code Collections#emptyList()}，否则原样返回。
   */
  public static <T> List<T> orEmpty(List<T> collection) {
    if (collection == null) {
      return Collections.emptyList();
    }
    return collection;
  }

  /**
   * 如果Map为null，则返回{@code Collections#emptyList()}，否则原样返回。
   * 
   * @param collection 集合数据
   * @return 如果Map为null，则返回{@code Collections#emptyList()}，否则原样返回。
   */
  public static <K, V> Map<K, V> orEmpty(Map<K, V> collection) {
    if (collection == null) {
      return Collections.emptyMap();
    }
    return collection;
  }

  /**
   * 如果字符串为null，则返回空字符串，否则原样返回。
   * 
   * @param collection 集合数据
   * @return 如果字符串为null，则返回空字符串，否则原样返回。
   */
  public static <K, V> String orEmpty(String string) {
    if (string == null) {
      return "";
    }
    return string;
  }

  /**
   * 如果数据(data)为null则返回默认值(defaultValue)，否则原样返回
   * 
   * @param data         数据
   * @param defaultValue 默认值
   * @return 如果数据(data)为null则返回默认值(defaultValue)，否则原样返回
   */
  public static <T> T orDefault(T data, T defaultValue) {
    if (data == null) {
      return defaultValue;
    }
    return data;
  }

  /**
   * 如果数据(data)为null则返回默认值(defaultValue)，否则原样返回
   * 
   * @param data         数据
   * @param defaultValue 默认值
   * @return 如果数据(data)为null则返回默认值(defaultValue)，否则原样返回
   */
  public static <T> T orDefault(T data, Supplier<T> defaultValue) {
    if (data == null) {
      return defaultValue.get();
    }
    return data;
  }

  /**
   * 如果数据(data)不满足条件(assertCondition=false)返回默认值(defaultValue)，否则原样返回
   * 
   * @param data            数据
   * @param assertCondition Predicate断言条件，true=原样返回，false=返回默认值
   * @param defaultValue    默认值
   * @return 如果数据(data)不满足条件(assertCondition=false)返回默认值(defaultValue)，否则原样返回
   */
  public static <T> T orDefault(T data, Predicate<T> assertCondition, Supplier<T> defaultValue) {
    if (!assertCondition.test(data)) {
      return defaultValue.get();
    }
    return data;
  }

  /**
   * 如果数据(data)不满足条件(assertCondition=false)返回默认值(defaultValue)，否则原样返回
   * 
   * @param data            数据
   * @param assertCondition Predicate断言条件，true=原样返回，false=返回默认值
   * @param defaultValue    默认值
   * @return 如果数据(data)不满足条件(assertCondition=false)返回默认值(defaultValue)，否则原样返回
   */
  public static <T> T orDefault(T data, Predicate<T> assertCondition, T defaultValue) {
    if (!assertCondition.test(data)) {
      return defaultValue;
    }
    return data;
  }

  /**
   * 获取字符串的长度，如果为null，则返回0；
   * 
   * @param sequence 字符串
   * @return null则返回0，否则返回{@link CharSequence#length()}
   */
  public static int ofSize(CharSequence sequence) {
    if (sequence == null) {
      return 0;
    }
    return sequence.length();
  }

  /**
   * 获取数组的长度，如果为null，则返回0；
   * 
   * @param array 数组
   * @return null则返回0，否则返回{@link array#length}
   */
  public static int ofSize(Object[] array) {
    if (array == null) {
      return 0;
    }
    return array.length;
  }

  /**
   * 获取集合中元素的个数，如果为null，则返回0；
   * 
   * @param data 集合
   * @return null则返回0，否则返回{@link Collection#size()}
   */
  public static int ofSize(Collection<?> data) {
    if (data == null) {
      return 0;
    }
    return data.size();
  }

  /**
   * 获取Map中元素的个数，如果为null，则返回0；
   * 
   * @param data 集合
   * @return null则返回0，否则返回{@link Map#size()}
   */
  public static int ofSize(Map<?, ?> data) {
    if (data == null) {
      return 0;
    }
    return data.size();
  }
}

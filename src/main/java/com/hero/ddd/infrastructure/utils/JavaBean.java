package com.hero.ddd.infrastructure.utils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.ClassUtils;

public final class JavaBean {
  private static final ConversionService DEFAULT_CONVERSION = new DefaultConversionService();

  private JavaBean() {
    throw new AssertionError("不支持实例化");
  }

  /**
   * 字段拷贝，如果source或者beanCreator为null，则返回null。当字段名一样，但类型不一样时，会忽略该字段。
   * </p>
   * 使用示例：
   * 
   * <pre>
   * JavaBean.copyFrom(sourceBean, TargetBean::new)
   * </pre>
   * 
   * @param sourceBean        待Copy的对象
   * @param targetBeanCreator 用于构造目标对象实例
   * @exception UnsupportedOperationException
   */
  public static <T> T copyFrom(Object sourceBean, Supplier<T> targetBeanCreator) {
    return copyFrom(sourceBean, targetBeanCreator, false);
  }

  /**
   * 字段拷贝，如果source或者beanCreator为null，则返回null。
   * </p>
   * 字段类型转换：当两个Bean字段名一样，但是字段类型不一样时，是否自动转换，默认false(当字段名一样，但类型不一样时，会忽略该字段)。
   * </p>
   * 目前主要应用在后端是long/double类型，但是前端js不支持这个精度，因此需要Number转换为String传递的场景。
   * </p>
   * 使用示例：
   * 
   * <pre>
   * JavaBean.copyFrom(sourceBean, TargetBean::new, true);
   * </pre>
   * 
   * @param sourceBean            待Copy的对象
   * @param targetBeanCreator     用于构造目标对象实例
   * @param enableTypeAutoConvert 字段copy时，类型不一致时是否自动转换类型
   * @exception UnsupportedOperationException
   */
  public static <T> T copyFrom(Object sourceBean, Supplier<T> targetBeanCreator,
      boolean enableTypeAutoConvert) {
    return copyFrom(sourceBean, targetBeanCreator, enableTypeAutoConvert
        ? JavaBean.Specs.TYPE_AUTO_CONVERT
        : JavaBean.Specs.DEFAULT);
  }

  /**
   * 字段拷贝，如果source或者beanCreator为null，则返回null。
   * </p>
   * 你可以使用{@link Specs}来定义copy规则，比如，是否开启字段类型自动转换、是否过滤某些字段、是否只copy部分字段。
   * </p>
   * 字段类型转换：当两个Bean字段名一样，但是字段类型不一样时，是否自动转换，默认false(当字段名一样，但类型不一样时，会忽略该字段)。
   * </p>
   * 使用示例：
   * 
   * <pre>
   * JavaBean.copyFrom(sourceBean, TargetBean::new, JavaBean.Specs.define()
   *     .enableTypeAutoConvert().includeThese("id", "name")
   *     .excludeThese("birthday", "realName"));
   * </pre>
   * 
   * @param sourceBean        待Copy的对象
   * @param targetBeanCreator 用于构造目标对象实例
   * @param beanCopyRule      字段复制的一些规则，不能为null
   * @exception UnsupportedOperationException
   */
  public static <T> T copyFrom(Object sourceBean, Supplier<T> targetBeanCreator, JavaBean.Specs beanCopyRule) {
    if (beanCopyRule == null) {
      throw new UnsupportedOperationException("请指定beanCopyRule");
    }
    if (sourceBean == null || targetBeanCreator == null) {
      return null;
    } else {
      try {
        T target = targetBeanCreator.get();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target.getClass());
        for (PropertyDescriptor targetPd : targetPds) {
          Method writeMethod = targetPd.getWriteMethod();
          if (writeMethod != null) {
            if (beanCopyRule.isFieldExclude(targetPd.getName())) {
              // 先过滤黑名单
              continue;
            }
            if (!beanCopyRule.isFieldInclude(targetPd.getName())) {
              // 不在白名单里
              continue;
            }
            PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(sourceBean.getClass(), targetPd.getName());
            if (sourcePd != null) {
              Method readMethod = sourcePd.getReadMethod();
              if (readMethod != null) {
                // getter方法存在，我们判断下字段类型是否兼容。
                boolean fieldTypeCompatible = ClassUtils
                    .isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType());
                // 类型不兼容但是也没有开启类型自动转换，直接忽略这个字段
                if (fieldTypeCompatible == false && !beanCopyRule.isTypeAutoConvert()) {
                  continue;
                }
                try {
                  if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                    readMethod.setAccessible(true);
                  }
                  Object value = readMethod.invoke(sourceBean);
                  // 尝试类型转换
                  if (value != null && fieldTypeCompatible == false
                      && beanCopyRule.isTypeAutoConvert()) {
                    // 检查能否转换，不能转换忽略掉，要打印日志吗
                    if (!DEFAULT_CONVERSION.canConvert(readMethod.getReturnType(),
                        writeMethod.getParameterTypes()[0])) {
                      continue;
                    }
                    // 如果转换异常的话，直接报错吧。
                    value = DEFAULT_CONVERSION.convert(value, writeMethod.getParameterTypes()[0]);
                  }
                  if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                    writeMethod.setAccessible(true);
                  }
                  writeMethod.invoke(target, value);
                } catch (Throwable ex) {
                  throw new FatalBeanException(
                      "Could not copy property '" + targetPd.getName() + "' from source to target",
                      ex);
                }
              }
            }
          }
        }
        return target;
      } catch (BeansException e) {
        throw new UnsupportedOperationException(
            "====>> class copy error: from 【" + sourceBean.getClass() + "】 to targetClass 】", e);
      }
    }
  }

  /**
   * 将sourceList复制到新的List，List元素的类型为beanCreator生成的对象。
   * </p>
   * 如果sourceList或beanCreator为null，则返回空List
   * </p>
   * 使用示例：
   * 
   * <pre>
   * JavaBean.copyFrom(sourceBeanList, TargetBean::new);
   * </pre>
   * 
   * @param sourceBeanList    待Copy的对象集合
   * @param targetBeanCreator 构造目标Bean
   * @return never return null
   * @exception UnsupportedOperationException
   */
  public static <S, T> List<T> copyFrom(Collection<S> sourceBeanList, Supplier<T> targetBeanCreator) {
    return copyFrom(sourceBeanList, targetBeanCreator, false, null);
  }

  /**
   * 将sourceList复制到新的List，List元素的类型为beanCreator生成的对象。
   * </p>
   * 如果sourceList或beanCreator为null，则返回空List
   * </p>
   * 使用示例：
   * 
   * <pre>
   * JavaBean.copyFrom(sourceBeanList, TargetBean::new,
   *     (sourceBean, targetBean) -> {
   *       targetBean.setUserId(sourceBean.getUserCode());
   *     });
   * </pre>
   * 
   * @param sourceBeanList    待Copy的对象集合
   * @param targetBeanCreator 构造目标Bean
   * @param beanPostProcessor 标准的字段自动Copy之后，允许手动做一些后续处理，比如手动copy非标准字段
   * @return never return null
   * @exception UnsupportedOperationException
   */
  public static <S, T> List<T> copyFrom(Collection<S> sourceBeanList, Supplier<T> targetBeanCreator, BiConsumer<S, T> beanPostProcessor) {
    return copyFrom(sourceBeanList, targetBeanCreator, false, beanPostProcessor);
  }

  /**
   * 将sourceList复制到新的List，List元素的类型为beanCreator生成的对象。
   * </p>
   * 如果sourceList或beanCreator为null，则返回空List
   * </p>
   * 字段类型转换：当两个Bean字段名一样，但是字段类型不一样时，是否自动转换，默认false。
   * </p>
   * 目前主要应用在后端是long/double类型，但是前端js不支持这个精度，因此需要Number转换为String传递的场景。
   * </p>
   * 使用示例：
   * 
   * <pre>
   * JavaBean.copyFrom(sourceBeanList, TargetBean::new, false);
   * </pre>
   * 
   * @param sourceBeanList        待Copy的对象集合
   * @param targetBeanCreator     构造目标Bean
   * @param enableTypeAutoConvert 字段copy时，类型不一致时是否自动转换类型
   * @return never return null
   * @exception UnsupportedOperationException
   */
  public static <S, T> List<T> copyFrom(Collection<S> sourceBeanList, Supplier<T> targetBeanCreator,
      boolean enableTypeAutoConvert) {
    return copyFrom(sourceBeanList, targetBeanCreator, enableTypeAutoConvert, null);
  }

  /**
   * 将sourceList复制到新的List，List元素的类型为beanCreator生成的对象。
   * </p>
   * 如果sourceList或beanCreator为null，则返回空List
   * </p>
   * 字段类型转换：当两个Bean字段名一样，但是字段类型不一样时，是否自动转换，默认false。
   * </p>
   * 目前主要应用在后端是long/double类型，但是前端js不支持这个精度，因此需要Number转换为String传递的场景。
   * </p>
   * 使用示例：
   * 
   * <pre>
   * JavaBean.copyFrom(sourceBeanList, TargetBean::new, false,
   *     (sourceBean, targetBean) -> {
   *       targetBean.setUserId(sourceBean.getUserCode());
   *     });
   * </pre>
   * 
   * @param sourceBeanList        待Copy的对象集合
   * @param targetBeanCreator     构造目标Bean
   * @param enableTypeAutoConvert 字段copy时，类型不一致时是否自动转换类型
   * @param beanPostProcessor     标准的字段自动Copy之后，允许手动做一些后续处理，比如手动copy非标准字段
   * @return never return null
   * @exception UnsupportedOperationException
   */
  public static <S, T> List<T> copyFrom(Collection<S> sourceBeanList, Supplier<T> targetBeanCreator,
      boolean enableTypeAutoConvert, BiConsumer<S, T> beanPostProcessor) {
    return copyFrom(sourceBeanList, targetBeanCreator, enableTypeAutoConvert
        ? JavaBean.Specs.TYPE_AUTO_CONVERT
        : JavaBean.Specs.DEFAULT, beanPostProcessor);
  }

  /**
   * 将sourceList复制到新的List，List元素的类型为beanCreator生成的对象。
   * </p>
   * 如果sourceList或beanCreator为null，则返回空List。
   * </p>
   * 你可以使用{@link Specs}来定义copy规则，比如，是否开启字段类型自动转换、是否过滤某些字段、是否只copy部分字段。
   * </p>
   * 字段类型转换：当两个Bean字段名一样，但是字段类型不一样时，是否自动转换，默认false。
   * </p>
   * 使用示例：
   * 
   * <pre>
   * JavaBean.copyFrom(sourceBeanList, TargetBean::new, JavaBean.Specs.define()
   *     .enableTypeAutoConvert().includeThese("id", "name").excludeThese("birthday", "realName"),
   *     (sourceBean, targetBean) -> {
   *       targetBean.setUserId(sourceBean.getUserCode());
   *     });
   * </pre>
   * 
   * @param sourceBeanList    待Copy的对象集合
   * @param targetBeanCreator 构造目标Bean
   * @param beanCopyRule      字段复制的一些规则，不能为null
   * @param beanPostProcessor 标准的字段自动Copy之后，允许手动做一些后续处理，比如手动copy非标准字段
   * @return never return null
   * @exception UnsupportedOperationException
   */
  public static <S, T> List<T> copyFrom(Collection<S> sourceBeanList, Supplier<T> targetBeanCreator,
      JavaBean.Specs beanCopyRule, BiConsumer<S, T> beanPostProcessor) {
    if (beanCopyRule == null) {
      throw new UnsupportedOperationException("请指定beanCopyRule");
    }
    if (sourceBeanList == null || sourceBeanList.isEmpty() || targetBeanCreator == null) {
      return Collections.emptyList();
    } else {
      try {
        List<T> targetList = new ArrayList<>(sourceBeanList.size());
        boolean hasBeanProcessor = (beanPostProcessor != null);
        for (S sourceBean : sourceBeanList) {
          T targetBean = copyFrom(sourceBean, targetBeanCreator, beanCopyRule);
          if (targetBean == null) {
            continue;
          }
          if (hasBeanProcessor) {
            beanPostProcessor.accept(sourceBean, targetBean);
          }
          targetList.add(targetBean);
        }
        return targetList;
      } catch (BeansException e) {
        throw new UnsupportedOperationException("bean fields copy error", e);
      }
    }
  }

  /**
   * 关于BeanCopy的一些描述规则
   */
  public static class Specs {
    /**
     * 默认行为，不开启类型转换，也不过滤字段
     */
    private static final Specs DEFAULT = Specs.define();

    /**
     * 支持类型转换，但不支持过滤字段
     */
    private static final Specs TYPE_AUTO_CONVERT = Specs.define().enableTypeAutoConvert();

    /**
     * 定义一组Bean Copy的规则
     */
    public static Specs define() {
      return new Specs();
    }

    /**
     * 字段类型是否自动转换，当两个Bean字段名一样，但是字段类型不一样时，是否自动转换。
     * 目前主要应用在后端是long/double类型，但是前端js不支持这个精度，因此需要Number转换为String传递
     */
    private boolean typeAutoConvert = false;

    /**
     * 排除那些字段（黑名单）
     */
    private Set<String> excludedFields;

    /**
     * 只包含那些字段（白名单）
     */
    private Set<String> includedFields;

    /**
     * 字段类型是否自动转换，当两个Bean字段名一样，但是字段类型不一样时，是否自动转换。
     * </p>
     * 目前主要应用在后端是long/double类型，但是前端js不支持这个精度，因此需要Number转换为String传递
     */
    public Specs enableTypeAutoConvert() {
      this.typeAutoConvert = true;
      return this;
    }

    /**
     * 在执行bean copy时，排除掉Target Bean的这些字段（黑名单）。
     * </p>
     * 当同时存在字段黑名单和白名单时，黑名单优先，其次是白名单。
     * 
     * @param fieldNames 字段名
     */
    public Specs excludeThese(String... fieldNames) {
      if (fieldNames != null && fieldNames.length > 0) {
        this.excludedFields = new HashSet<>(Arrays.asList(fieldNames));
      }
      return this;
    }

    /**
     * 在执行bean copy时，只向Target Bean的这些字段赋值（白名单）。
     * </p>
     * 当同时存在字段黑名单和白名单时，黑名单优先，其次是白名单。
     * 
     * @param fieldNames 字段名
     */
    public Specs includeThese(String... fieldNames) {
      if (fieldNames != null && fieldNames.length > 0) {
        this.includedFields = new HashSet<>(Arrays.asList(fieldNames));
      }
      return this;
    }

    boolean isTypeAutoConvert() {
      return this.typeAutoConvert;
    }

    boolean isFieldExclude(String fieldName) {
      return this.excludedFields != null && this.excludedFields.contains(fieldName);
    }

    boolean isFieldInclude(String fieldName) {
      return this.includedFields == null || this.includedFields.contains(fieldName);
    }
  }
}

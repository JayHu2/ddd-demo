package com.hero.ddd.domain.hrmanage.organization;

import java.util.List;
import java.util.function.Supplier;

// 组织聚合
@SuppressWarnings("unused")
public class Organization {

  private String code;
  private String name;
  // 此处不是很建议把 parentCode 用 另一个 Organization 聚合来表示，因为我们会期望从 ROOT 节点开始的 Organization 聚合按层递归的时候 present 与 child的parent 是同一个实例， 维护成本比较高
  
  private String parentCode;

  // 一般组织会有很多层，如果按正常的方式构建聚合，则会给系统带来巨大的性能开销， 所以一般建议都是用 懒加载的方式加载数据，即在访问 getChilds() 时加载数据
  // 懒加载的方式有很多， 可以自己写，也可以用 spring-data 结合 spring-data-repository 来实现 相同在 get 时才会加载的效果
  // 个人就用明文的方式来实现了
  private List<Organization> childs;

  // 懒加载数据提供器
  private Supplier<List<Organization>> lazyChildsSupplier;

  public Organization(String code, String name, String parentCode,
      Supplier<List<Organization>> lazyChildsSupplier) {
    this.code = code;
    this.name = name;
    this.parentCode = parentCode;
    this.lazyChildsSupplier = lazyChildsSupplier;
  }

  public List<Organization> getChilds() {
    if (this.childs == null) {
      this.childs = this.lazyChildsSupplier.get();
    }
    return this.childs;
  }

}

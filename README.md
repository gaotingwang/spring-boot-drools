# spring-boot-drools
### KIE的生命周期

- 编写：编写规则文件，比如：DRL，BPMN2、决策表、实体类等。
- 构建：构建一个可以发布部署的组件，对于KIE来说是JAR文件。
- 测试：部署之前对规则进行测试。
- 部署：利用Maven仓库将jar部署到应用程序。
- 使用：程序加载jar文件，通过KieContainer对其进行解析创建KieSession。
- 执行：通过KieSession对象的API与Drools引擎进行交互，执行规则。
- 交互：用户通过命令行或者UI与引擎进行交互。
- 管理：管理KieSession或者KieContainer对象。



### 组成

- KieServices 

  规则引擎服务，其实例是提供了一个服务注册列表，提供访问KIE关于构建和运行的相关对象。通过它来获取来的各种对象，完成规则构建、管理和执行操作。

  - 获取KieContainer
  - 获取KieResponsitory
  - 获取KieSession

- KieContainer

  就是一个KieBase的容器，内部依旧是通过KieBase来创建KieSession。

- KieBase

  知识仓库，包含若干规则、流程和方法等，KieBase本身不包含运行时的相关数据。如果要执行KieBase中规则，需要根据KieBase创建KieSession。

- KieResponsitory

  KieResponsitory是一个单例对象，是存放管理KieModule的仓库，KieModule由`kmodule.xml`来定义。

- KieSession

  真正执行规则运行时的会话

  

### Rules语法

```java

package rules;
dialect "java"; // 指定Java语言

import com.gtw.drools.model.UserInfoFact;


rule "规则名称一" // 需唯一
// 规则属性  
  
  	when
      	$d : Double($d == 100) // 满足括号中的规则
      	$u : User(age > 18) // 多个规则需要同时满足，即$d和$u要同时满足
  	then
      	System.out.println("rule01 drl test result :  " + $d);
end
  
/*谨慎insert、update、modify、delete操作，会变更Fact，导致之前执行过的规则会再次触发，防止自身触发no-loop true，防止触发其他规则，在其他规则上加lock-on-active true*/      
rule "rule - a"
no-loop true // 这里也可以用lock-on-active true ，Fact变更不会触发再次执行
    when
        $lover : LoverFact(name != "王五");
    then
        System.out.println("rule - a:" + $lover.getName());
        $lover.setName("李四");
        update($lover);
end

// lock-on-active 不会由Fact的变更再次触发
rule "rule - b"
lock-on-active true
    when
        $lover : LoverFact(name == "李四");
    then
        System.out.println("rule - b:" + $lover.getName());
end
  
/*salience指定规则执行顺序，值越大，优先级越高。不指定默认为0，执行顺序随机，可以指定为负数*/
rule "salience - test3"
salience sa // 也可以动态指定，由Person age动态决定执行顺序
		when 
  			$p : Person(sa : age)
    then
        System.out.println("salience - test3:" + $p.getAge());
end
  

rule "rule - b"
agenda-group "aaa" // agenda-group 需要激活才会执行该规则
//auto-focus true // 默认当前agenda自动focus
activation-group "group01" // 相同activation-group只会评估一个，默认按顺序执行第一个；指定salience，则选salience最大的执行
salience 1000
    when
        $lover : LoverFact();
    then
        System.out.println("2");
end

```



## 决策表

### RuleSet区域
- RuleSet -- package
- Sequential -- 指定为TRUE，按excel表格顺序执行
- SequentialMaxPriority
- SequentialMinPriority
- EscapeQuotes
- NumericDisabled
- Import -- 引入依赖包
- Variables -- 定义全局变量 global
- Functions -- 定义function函数
- Queries
- Declare

### RuleTable区域

- NAME -- 规则名称，即为每个规则指定的名称，须唯一
- DESCRIPTION -- 规则描述，会在规则文件中加注释
- CONDITION -- 即指定LHS中内容
- ACTION -- 即指定RHS中内容
- METADATA

#### 规则属性

- PRIORITY 设置优先级，对应规则里的salience
- DURATION
- TIMER
- ENABLED
- CALENDARS
- NO-LOOP
- LOCK-ON-ACTIVE
- AUTO-FOCUS
- ACTIVATION-GROUP
- AGENDA-GROUP
- RULEFLOW-GROUP
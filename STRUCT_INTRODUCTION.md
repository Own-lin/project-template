##  模块结构介绍

脚手架的模块分为三个(如果是微服务可将core中的support另外划分出一个独立的模块)：
- core
- client
- model

他们三者的依赖关系可从每个模块的pom.xml中看出(是否dependence了当前项目的其他模块)。

最基础的模块为`model` 是client和core的基础，而client模块与core模块虽然没有非常直接的关联， 但是`client`模块**实际请求**的是`core`模块的`controller`

### 依赖关系

最上层的pom.xml定义了常用依赖的版本， 子模块不用再声明version就可直接根据声明的版本进行导入。

修改整个项目的版本时， 只需要使用以及引入的名为version的maven插件进行设置即可(或者执行`mvn versions:set`命令) 然后填入版本即可同时修改所有子模块的版本。

##  代码分层介绍

### core

- config: 定义spring或者中间件配置
- controller: 从页面直接请求的接口
- dao: 数据库操作层
- server: 服务层， 用于对外暴露可供远程调用的API(即使远程调用内容与controller中某个冲突， 也要进行区分)
- service: 业务处理层， 用于处理大方向上的业务逻辑
  - manager: 管理层， 用于处理service中较为复杂的部分可以抽离的逻辑(例如，需要经过一大段逻辑并且有涉及调用其他业务层数据时)
  - strategy: 策略层，对service层提供设计模式支持
- support: 与框架、中间件绑定的业务工具， 往往在业务中起到较为重要的作用
  - ability: 能力层， 为开发者与运维人员提供能力支持（例如: 异常告警）
  - event: spring`事件DTO`，定义一个事件并通过事件广播器(ApplicationEventPublisher)广播该事件(即传入`事件DTO`), 然后在SpringEventManager中接收消费他
- util: 简单的工具， 例如可以对Spring或者apache-common工具里的Assert工具进行自定义, 返回自己想要的异常
- resources/static/db.json: 数据源信息， 可通过application文件中的`datasource.config-path`配置来指定db.json存放的位置

### client

- client: 远程调用客户端， 其他服务引入client模块时可以直接访问client包下的类的方法来实现跨进程调用
- factory: client的降级处理工厂， 客户端调用失败或者限流后如果需要触发对应逻辑可以在这个包下实现

### model

- DO: 实体对象， 一个实体对应一个表 不要改添加其他属性或者逻辑
- DTO: 业务传递对象， 一般来说是作为请求对象或业务处理的中间对象存在的
- VO: 数据呈现对象， 每个接口得到的返回对象都可能不一样 所以需要定义不同的VO
- base: 基础模型， 包括异常与部分非具体业务的枚举
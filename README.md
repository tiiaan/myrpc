# myrpc (a toy rpc framework)

### 特性

### 棘手的点


### 使用方法

引入 SpringBoot Starter 场景依赖
```xml
<!-- 提供端 -->
<dependency>
  <groupId>com.tiiaan.rpc</groupId>
  <artifactId>myrpc-server-spring-boot-starter</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>

<!-- 消费端 -->
<dependency>
    <groupId>com.tiiaan.rpc</groupId>
    <artifactId>myrpc-client-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

修改 YAML 配置文件，以提供端为例
```yaml
myrpc:
  server:
    port: 9000 # RPC 通信端口号
    address: 127.0.0.1:8848 # Nacos 注册中心地址
```

提供端使用 `@MyService(version = "0.1")` 注解暴露服务
```java
@MyService(version = "0.1")
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String message) {
        return "myrpc: " + message;
    }
}
```

消费端使用 `@MyReference(version = "0.1")` 注解注入服务引用
```java
@RestController
public class HelloController {
    
    @MyReference(version = "0.1")
    private HelloService helloService;
    
    @GetMapping("/hello")
    public String hello() {
        return helloService.hello("Hello World!");
    }
}
```


### 项目结构



### 如何发送请求？


### 如何获取响应结果？
使用 CompletableFuture 接收响应结果。但是由于服务消费方会并发调用多个服务，一段时间后，消费方线程池会收到多个响应对象。此时要考虑一个问题，如何将每个响应对象传递给相应的 Future 且不出错？ 

我的解决方案是为每个请求生成唯一 ID 添加到请求头中，然后将「ID-Future」以键值对的形式保存到 Map。

```java
Map<String, CompletableFuture<MyRpcResponse<Object>>> FUTURES = new ConcurrentHashMap<>();
```

```java
//每次发送请求，都把请求 ID 和 Future 对象以键值对的形式存到 Holder 中
futuresHolder.put(myRpcRequest.getRequestId(), completableFuture);
```

```java
//每次消费端接收到响应结果，会根据 ID 取出对应的 Future 对象，然后将结果设置到 Future 中
public void complete(MyRpcResponse<Object> myRpcResponse) {
  CompletableFuture<MyRpcResponse<Object>> completableFuture = FUTURES.remove(myRpcResponse.getRequestId());
  if (completableFuture != null) {
    completableFuture.complete(myRpcResponse);
  } else {
    throw new IllegalStateException();
  }
}
```



### @MyService 注解实现原理



### @MyReference 注解实现原理




### 负载均衡
**一致性哈希解决什么问题？**

在一些场景中，我们对负载均衡往往有2个期望：(1)期望请求尽可能均匀、分散地打到不同的节点上；(2)期望相同的请求尽可能打到相同的节点上，以便于充分利用本地缓存。

哈希取余 `hash(id)%N` 看似完美匹配这两点需求，但假设因为业务扩张新增一个节点，`hash(id)%(N+1)` 的结果极大可能跟原先相去甚远，导致所有节点上的本地缓存全部扑空。对此，一致性哈希，平滑迁移

**如何用 CAS 解决多线程轮询？**
```java
private AtomicInteger atomicInteger = new AtomicInteger(0);

private final int getAndIncrement() {
    int current;
    int next;
    do {
        current = this.atomicInteger.get();
        next = current == Integer.MAX_VALUE ? 0 : current + 1;
    } while (!atomicInteger.compareAndSet(current, next));
    return next;
}

@Override
protected <T> T doSelect(List<T> candidates) {
    return candidates.get(getAndIncrement() % candidates.size());
}
```



### 服务缓存
**服务缓存解决什么问题？**

1. 避免每次消费服务都要去注册中心拉取节点列表
2. 避免注册中心闪断导致服务不可用

**服务缓存带来什么问题？**

1. 如果某个服务节点不可用或下线，如何及时摘除节点？
2. 新上线的服务节点如何及时收到流量？
3. 如果调用方拿到的节点不是最新的(比如在命中缓存之后、发起请求之前，节点下线了)，如何处理？



### SPI
- [官方文档 | Dubbo2 SPI 源代码分析 | Apache Dubbo](https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/source/dubbo-spi/)
- [官方文档 | Dubbo2 SPI 自适应拓展 | Apache Dubbo](https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/source/adaptive-extension/)
- [官方文档 | SPI 扩展实现 | Apache Dubbo](https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/impls/)




### 可拓展协议








### 更新日志

- [[v0.0] d143559](https://github.com/tiiaan/myrpc/commit/d1435593ed8186ffd2e01a7617cd83b0e37557c8)
  - RPC HelloWorld，服务提供者、服务消费者通过单个服务直连
  - 使用 Socket 实现网络传输
- [[v0.1] 4c93f18](https://github.com/tiiaan/myrpc/commit/4c93f18c8d066a43669c810199c46ad27e0a0928)
  - 增加多服务，使用简易服务注册表管理
- [[v0.2] 2152c89](https://github.com/tiiaan/myrpc/commit/2152c89e38113ff7eaf96b77cfb84970b9b258e8) ⭐️⭐️⭐️
  - 使用 Netty 替换 Socket 实现网络传输
- [[v0.3] e7dd653](https://github.com/tiiaan/myrpc/commit/e7dd653d24fa17c8c2c1a1e97af06230295cf59e)
  - 使用 Kryo 序列化替换 JSON 序列化
- [[v0.4] 71b97aa](https://github.com/tiiaan/myrpc/commit/71b97aa4622c7007b09b0008766556068cc6be35) ⭐️⭐️⭐️
  - 使用 Nacos 注册中心管理服务提供者地址
- [[v0.5] 9800f22](https://github.com/tiiaan/myrpc/commit/9800f22654c8e85bac0d4b54629ae2f7460403a6)
  - 增加优雅关闭钩子
- [[v0.6] 705fa87](https://github.com/tiiaan/myrpc/commit/705fa87d315f96b26804b5db43a002a75ff3ce2e)
  - 增加 Random 随机负载均衡
- [[v0.7] 6b0811c](https://github.com/tiiaan/myrpc/commit/6b0811ce870077fa2e02d5d8a8073d52e12e5d84)
  - 增加 Netty Channel 复用缓存
  - 使用 CompletableFuture 异步接收返回结果，增加唯一请求 ID 字段，响应结果通过 ID 传给指定的 Future
- [[v0.8] bc0f80ed](https://github.com/tiiaan/myrpc/commit/bc0f80ed95701be7e60b4fdfb186e0f8c3c36f4d)
  - 增加 Hessian 序列化
- [[v0.9] e22690ee](https://github.com/tiiaan/myrpc/commit/e22690ee414c4d76ae238a1b61db021ef6922361)
  - 修复网络传输 BUG
- [[v0.10] 93b906f](https://github.com/tiiaan/myrpc/commit/93b906fe485cab4d63afc616d3b9fcbdf7b27334)
  - 增加 RoundRobin 轮询负载均衡
- [[v0.11] 8927cd6](https://github.com/tiiaan/myrpc/commit/8927cd63f69a1ca8f91562209c3f36d03888996c)
  - 增加多版本灰度发布
- [[v0.12] afa54b7](https://github.com/tiiaan/myrpc/commit/afa54b7daea7a154dd5dec71a81c993e950f478c) ⭐️⭐️⭐️
  - 集成 Spring
  - 使用自定义注解 @MyService 自动注册服务
- [[v0.13] 9d43d6b](https://github.com/tiiaan/myrpc/commit/9d43d6b139ac9ccb43dff32c5db4faa48928b82f)
  - 使用自定义注解 @MyReference 注入代理消费服务
- [[v0.14] a61be68](https://github.com/tiiaan/myrpc/commit/a61be6878290d0c4bbe3b00aad7f422882f96d7b) ⭐️⭐️⭐️⭐️⭐️
  - 实现 SPI 机制，参考 Dubbo SPI 源码实现 ExtensionLoader
  - 使用自定义注解 @MySPI 注册拓展接口，支持负载均衡拓展、序列化拓展、注册中心拓展、网络传输拓展
- [[v0.15] a6499f7](https://github.com/tiiaan/myrpc/commit/a6499f711c2e3f7bf8220fae5b152c632fa283b4) ⭐️⭐️⭐️⭐️⭐️
  - 增加注册中心缓存，避免每次消费服务都从注册中心全量请求节点列表
  - 使用 Nacos 订阅机制，服务提供者宕机或增加机器部署，注册中心都会立即推送事件通知消费者，及时更新注册中心缓存，摘除不可用节点、分配流量给新上线节点
- [[v0.16] b927b41](https://github.com/tiiaan/myrpc/commit/b927b41e8d28533fd3842db90c3deb70400a2684) ⭐️⭐️⭐️⭐️⭐️
  - 修复注册中心缓存 BUG，解决命中之后节点过期问题，消费者发送请求前如果发现节点过期会安全重连其他节点
  - 如果服务提供者全部宕机，参考 Dubbo，服务消费者将无限次重连等待服务提供者恢复
- [[v0.17]]()
  - 重构传输协议，实现可拓展协议头
- [[v0.18]]()
  - 修复 RoundRobin 轮询负载均衡 BUG，增加接口级轮询，避免相互影响
- [[v0.19]]()
  - 封装 SpringBoot Starter，优化项目结构




### TODO
1. HTTP 协议 RPC
2. 应用级服务注册，平时做微服务项目时发现，服务注册都是以 application 为单位，而不是 interface
3. 想学一下时间轮







# myrpc (a toy rpc framework)

### 特性

### 棘手的点

### 性能测试

### 更新日志

- [[v0.0]](https://github.com/tiiaan/myrpc/commit/d1435593ed8186ffd2e01a7617cd83b0e37557c8)
  - RPC HelloWorld，服务提供者、服务消费者通过单个服务直连
  - 使用 Socket 实现网络传输

- [[v0.1]](https://github.com/tiiaan/myrpc/commit/4c93f18c8d066a43669c810199c46ad27e0a0928) 
  - 增加多服务，使用简易服务注册表管理

- [[v0.2]](https://github.com/tiiaan/myrpc/commit/2152c89e38113ff7eaf96b77cfb84970b9b258e8) 
  - 使用 Netty 替换 Socket 实现网络传输
  - 增加 JSON 序列化

- [[v0.3]](https://github.com/tiiaan/myrpc/commit/e7dd653d24fa17c8c2c1a1e97af06230295cf59e) 
  - 使用 Kryo 序列化替换 JSON 序列化

- [[v0.4]](https://github.com/tiiaan/myrpc/commit/71b97aa4622c7007b09b0008766556068cc6be35) 
  - 使用 Nacos 注册中心管理服务提供者地址

- [[v0.5]]() 
  - 增加优雅关闭钩子

- [[v0.6]]() 
  - 增加 Random 随机负载均衡

- [[v0.7]]() 
  - 增加 Netty Channel 复用缓存
  - 使用 CompletableFuture 异步接收返回结果，增加唯一请求 ID 字段，响应结果通过 ID 传给指定的 Future

- [[v0.8]]() 
  - 增加 Hessian 序列化

- [[v0.9]]() 
  - 修复网络传输 BUG

- [[v0.10]]() 
  - 增加 RoundRobin 轮询负载均衡

- [[v0.11]]() 
  - 增加多版本灰度发布

- [[v0.12]]() 
  - 集成 Spring
  - 使用自定义注解 @MyService 自动注册服务

- [[v0.13]]() 
  - 使用自定义注解 @MyReference 注入代理消费服务

- [[v0.14]]() 
  - 实现 SPI 机制，参考 Dubbo SPI 源码实现 ExtensionLoader
  - 使用自定义注解 @MySPI 注册拓展接口，支持负载均衡拓展、序列化拓展、注册中心拓展、网络传输拓展

- [[v0.15]]() 
  - 增加消费者本地缓存，避免每次消费服务都从注册中心请求节点列表
  - 使用 Nacos 订阅机制，服务提供者宕机或增加机器部署，注册中心都会立即推送事件通知消费者，及时更新本地缓存，摘除不可用节点、分配流量给新上线节点

- [[v0.16]]() 
  - 修复本地缓存 BUG，解决命中之后节点过期问题，消费者发送请求前如果发现节点过期会安全重连其他节点
  - 如果服务提供者全部宕机，参考 Dubbo，服务消费者将无限次重连等待服务提供者恢复

- [[v0.17]]() 
  - 重构传输协议，实现可拓展协议头

- [[v0.18]]() 
  - 修复 RoundRobin 轮询负载均衡 BUG，增加接口级轮询，避免相互影响


### 服务节点本地缓存设计思路
##### 本地缓存解决什么问题？
1. 避免每次消费服务都要去注册中心拉取节点列表
2. 避免注册中心故障导致服务不可用


##### 本地缓存带来什么问题？
1. 如果某个服务节点不可用或下线，如何及时摘除节点？
2. 新上线的服务节点如何及时收到流量？
3. 如果调用方拿到的节点不是最新的(比如在命中缓存之后、发起请求之前，节点下线了)，如何处理？


### SPI 设计思路
- [官方文档 | Dubbo2 SPI 源代码分析 | Apache Dubbo](https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/source/dubbo-spi/)
- [官方文档 | Dubbo2 SPI 自适应拓展 | Apache Dubbo](https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/source/adaptive-extension/)
- [官方文档 | SPI 扩展实现 | Apache Dubbo](https://cn.dubbo.apache.org/zh-cn/docsv2.7/dev/impls/)


### 可拓展协议设计思路





### ToDo







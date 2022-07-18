# myrpc (a toy rpc framework)

### 一些特性

### 棘手的点

### 我的收获

### 更新日志

- [[v0.0]](https://github.com/tiiaan/myrpc/commit/d1435593ed8186ffd2e01a7617cd83b0e37557c8) 使用 Socket 实现网络传输，Server/Client 单服务直连
- [[v0.1]](https://github.com/tiiaan/myrpc/commit/4c93f18c8d066a43669c810199c46ad27e0a0928) 增加简易服务注册表管理多服务
- [[v0.2]](https://github.com/tiiaan/myrpc/commit/2152c89e38113ff7eaf96b77cfb84970b9b258e8) 使用 Netty  实现网络传输，使用 JSON 序列化
- [[v0.3]](https://github.com/tiiaan/myrpc/commit/e7dd653d24fa17c8c2c1a1e97af06230295cf59e) 增加 Kryo 序列化 
- [[v0.4]](https://github.com/tiiaan/myrpc/commit/71b97aa4622c7007b09b0008766556068cc6be35) 使用 Nacos 注册中心管理服务提供者地址
- [[v0.5]]() 增加优雅关闭钩子
- [[v0.6]]() 增加 Random 随机负载均衡
- [[v0.7]]() 增加 Netty Channel 复用缓存，使用 CompletableFuture 优化接收返回结果，增加唯一请求 ID 字段
- [[v0.8]]() 增加 Hessian 序列化
- [[v0.9]]() 修复网络传输 BUG
- [[v0.10]]() 增加 RoundRobin 轮询负载均衡
- [[v0.11]]() 增加多版本灰度发布
- [[v0.12]]() 集成 Spring，使用自定义注解 @MyService 自动注册服务
- [[v0.13]]() 使用自定义注解 @MyReference 消费服务
- [[v0.14]]() 使用 SPI 机制实现负载均衡拓展、序列化拓展、注册中心拓展、网络传输拓展，使用自定义 @MySPI 注解标识拓展接口
- [[v0.15]]() 修复 RoundRobin 轮询负载均衡 BUG，增加方法级轮询，避免不同方法相互影响


### 印象深刻的 Bug

### 未来想做

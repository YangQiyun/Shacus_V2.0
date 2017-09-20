#融云 2.0 快速集成 Demo 

##主要功能： 
1、演示如何正确的进行 RongIM.init() 操作

2、演示如何进行 connect 操作，以及演示 token 获取过程

3、演示如何配置，调用 会话列表，以及如何在收到 push 消息以后做自动重连

4、演示如何配置，调用 聚合会话列表

5、演示如何配置，调用 会话页面，以及如何在收到 push 消息以后做自动重连

6、演示如何创建讨论组


##代码结构：

1、activity 包：集成 和 调用 融云的 SDK

2、test 包:test 包下面为集成融云 fragment 的其他的方式，在 demo 中并没有调用，只是一个演示的作用，你可以根据自己的需求进行调用

3、AndroidManifest.xml ：个别 Activity 上面有注释 “测试代码，看 README 注释”，有这个注释的均为测试 acitivity，主要是为了演示 融云 Fragment 的其他调用方式，其中都将 intent-filter 注释掉了，当你需要用到的时候打开即可。


##常见问题：
1、什么是 push 消息？

  push消息解释： http://support.rongcloud.cn/kb/MzE5
  
2、什么是聚合会话列表？

  看一下demo 中"聚合会话列表.png" 这张图片。
  
3、intent-filter 是什么？

  http://developer.android.com/guide/topics/manifest/intent-filter-element.html 




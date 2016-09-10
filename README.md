本项目主要是写一个从SVN上爬取文本内容，然后交给solr做全文索引，并提供搜索的功能的项目。
除了本项目，还需要配置的服务有：
redis：用于做消息队列
solr：用于做检索

爬虫基本思路：
1.页面接受到待趴取的起始SVN目录
2.启动趴取任务线程,controller 返回页面，提示正在爬虫
3.趴取任务线程(scheduleThread)初始化SVN服务，初始化处理SVN目录线程(SVNDirThread)，初始化处理SVN文档的线程(SVNFileThread)，初始化提交solr的线程(SolrHandleThread)，并提交初始SVN到redis服务
   scheduleThread需要监控SVNDirThread，SVNFileThread，SolrHandleThread，并且本身也需要被线程管理类（ThreadManager）所管理
4.趴取任务线程从controller接收到的SVN地址交给SVN目录处理线程。
5.由java线程池启动三个线程，开始任务。



处理SVN目录线程：
    从redis的目录set中获取第一个SVNDir，处理此SVN的响应，如果是有子目录，拼接完子目录之后继续放到redis上上。
      （redis上的key为  “com.shark.search4SVN.SVNDir”，目前考虑是一个 set）
       //TODO 如何一次获取多个 SVNDir，并其多个线程处理？

处理SVN文档线程：
   从redis的文档set中获取第一个SVNFile，处理该SVN的响应，使用tika解析文档，组装成Solr Document，序列化到redis上去
     （redis上的key为 “com.shark.search4SVN.SVNFile”,目前考虑是一个 set）

提交solr的线程:
   从redis获取一个solr Document，提交到solr server
     （redis上的key为 “com.shark.search4SVN.Solr”,目前考虑是一个 set）
     //TODO 如何一次获取多个 SVNDir，并其多个线程处理？

redis上需要多开一个队列，保存已经处理完的SVN链接 （redis上的key为 "com.shark.search4SVn.finishedDoc"）

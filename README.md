本项目主要是写一个从SVN上爬取文本内容，然后交给solr做全文索引，并提供搜索的功能的项目。
除了本项目，还需要配置的服务有：
solr服务器(tomcat+solr+hanlp)：用于做检索


趴取基本思路：
引入Disruptor框架，设置三个处理单元：
1. SVN处理单元:  如果从Disruptor获取到的是SVN目录，则列举该目录下所有的目录，将这些目录重新丢到Disruptor处理
                如果从Dirsruptor获取到的是SVN文件，则启动一个线程任务，解析文件内容，生成一个自定义Solr文档结构，将其重新丢到Disruptor处理
2. Solr处理单元：从 Disruptor中获取的是自定义Solr文档，则 启动一个线程任务，将其提交到 Solr 服务器。

TODO:
1. 引入定期任务，定期把配置过的URL定期更新其solr上的索引
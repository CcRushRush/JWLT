###rabbitMQ应用场景：异步处理、系统解耦（多个系统不需直接交互、通过消息进行业务流转）、流量销峰（缓冲处理）
###三个核心要素：消息生产者、消息队列、消息消费者
###交换器（用来选择指定的mq队列，也可以称为中转站），有四个类型，分别如下：
1. Direct Exchange 直接交换机：根据路由键完全匹配进行选择路由消息队列；
2. Topic Exchange 通配符交换机：*匹配多个单词，#匹配一个单词，所谓的单词是通过。隔开的；
3. Fanout Exchange 广播交换机：投递到所有绑定的队列，不需要规则；
4. Headers Exchange 基于消息内容中的headers属性进行匹配。
 
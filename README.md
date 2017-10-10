# d-transaction
A simple distributed transaction example.



修改端口，启动2个应用。

BarService第30行是主动抛异常，让事务回滚。
如果不抛异常，事务正常进行。
如果抛异常，事务正常回滚。

可以通过观察控制台打印的sql。

本例子来自《轻量级微服务架构》下册。

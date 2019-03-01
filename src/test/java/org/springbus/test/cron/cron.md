
http://www.atool.org/crontab.php

0    2    *    *    6
*    *    *    *    *    *
-    -    -    -    -    -
|    |    |    |    |    |
|    |    |    |    |    + year [optional]
|    |    |    |    +----- day of week (0 - 7) (Sunday=0 or 7) 
|    |    |    +---------- month (1 - 12)
|    |    +--------------- day of month (1 - 31)
|    +-------------------- hour (0 - 23)
+------------------------- min (0 - 59)

Crontab介绍 | Introduce Crontab

crontab命令常见于Unix和类Unix的操作系统之中，用于设置周期性被执行的指令。
该命令从标准输入设备读取指令，并将其存放于“crontab”文件中，以供之后读取和执行。
crontab储存的指令被守护进程激活，crond常常在后台运行，每一分钟检查是否有预定的作业需要执行。
crontab文件的每一行均遵守特定的格式，由空格或tab分隔为数个领域，每个领域可以放置单一或多个数值。

时程表的格式:f1 f2 f3 f4 f5 program，
其中 f1 是表示分钟，f2 表示小时，f3 表示一个月份中的第几日，
f4 表示月份，f5 表示一个星期中的第几天。program 表示要执行的程式。
Crontab使用 | Crontab Using
cron是一个linux下的定时执行工具，可以在无需人工干预的情况下运行作业。
由于Cron是Linux的内置服务，但它不自动起来，可以用以下的方法启动、关闭这个服务。
cron服务提供crontab命令来设定cron服务的，
以下是这个命令的一些参数与说明： 
crontab -u //设定某个用户的cron服务，一般root用户在执行这个命令的时候需要此参数；
crontab -l //列出某个用户cron服务的详细内容；
crontab -r //删除某个用户的cron服务；
crontab -e //编辑某个用户的cron服务。

Crontab例子 | Crontab Example


30 21 * * * /usr/local/etc/rc.d/lighttpd restart 表示每晚的21:30重启lighttpd
45 4 1,10,22 * * /usr/local/etc/rc.d/lighttpd restart 表示每月1、10、22日的4 : 45重启lighttpd
10 1 * * 6,0 /usr/local/etc/rc.d/lighttpd restart 表示每周六、周日的1 : 10重启lighttpd
0,30 18-23 * * * /usr/local/etc/rc.d/lighttpd restart 表示在每天18 : 00至23 : 00之间每隔30分钟重启lighttpd
0 23 * * 6 /usr/local/etc/rc.d/lighttpd restart 表示每星期六的11 : 00 pm重启lighttpd
0 */1 * * * /usr/local/etc/rc.d/lighttpd restart 每一小时重启lighttpd
* 23-7/1 * * * /usr/local/etc/rc.d/lighttpd restart 晚上11点到早上7点之间，每隔一小时重启lighttpd
0 11 4 * mon-wed /usr/local/etc/rc.d/lighttpd restart 每月的4号与每周一到周三的11点重启lighttpd
0 4 1 jan * /usr/local/etc/rc.d/lighttpd restart 一月一号的4点重启lighttpd

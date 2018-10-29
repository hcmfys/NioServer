package org.springbus.test.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorHelloworld {
    private static final String CONNECT_ADDR = "10.100.163.8:2181";
    private static final int SESSION_TIMEOUT = 5000;

    public static void mainApp(String[] args) throws Exception {
        //重试策略，初试时间1秒，重试10次
        RetryPolicy policy = new ExponentialBackoffRetry(1000, 10);
        //通过工厂创建Curator
        CuratorFramework curator = CuratorFrameworkFactory.builder().connectString(CONNECT_ADDR)
                .sessionTimeoutMs(SESSION_TIMEOUT).retryPolicy(policy).build();
        //开启连接
        curator.start();




        curator.delete().deletingChildrenIfNeeded().forPath("/mj/$Jobs");
        curator.close();
    }
}

package org.springbus.test.zk;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;


import java.util.List;

/**
 * zookeeper znode递归删除
 * @author LiJie
 *
 */
public class Zk2 {

    private static final String connectString = "10.100.163.8:2181";

    private static final int sessionTimeout = 8000;

    private static ZooKeeper zookeeper = null;

    private static  ZooKeeper zk = null;

    /**
     * main函数
     *
     * @param
     * @throws Exception
     */
    public static void mainApp(String[] args) throws Exception {
        zk =  new ZooKeeper(connectString, sessionTimeout, null);
        //调用rmr,删除所有目录
        while(true) {
            List<String> children = zk.getChildren("/mj/$Jobs", null);
            System.out.println("current size ="+children.size());
            Thread.sleep(2000);
        }
    }





}
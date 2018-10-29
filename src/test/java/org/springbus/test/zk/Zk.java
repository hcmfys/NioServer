package org.springbus.test.zk;

import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.List;

/**
 * zookeeper znode递归删除
 * @author LiJie
 *
 */
public class Zk {

    private static final String connectString = "10.100.163.8:2181";

    private static final int sessionTimeout = 8000;

    private static ZooKeeper zookeeper = null;

    private static  ZooKeeper zk = null;

    /**
     * main函数
     *
     * @param args
     * @throws Exception
     */
    public static void mainApp(String[] args) throws Exception {
        zk = getZookeeper();
        //调用rmr,删除所有目录
        rmr("/mj/$Jobs");
    }

    /**
     * 递归删除 因为zookeeper只允许删除叶子节点，如果要删除非叶子节点，只能使用递归
     *
     * @param path
     * @throws IOException
     */
    public static void rmr(String path) throws Exception {

        //获取路径下的节点
        List<String> children = zk.getChildren(path,  null);
        if(children!=null && children.size()>0 ) {
            System.out.println("size=" + children.size());
            for (String pathCd : children) {
                rmr(path + "/" + pathCd);
            }
        }else{
            System.out.println("size=" + children.size()  +" and  delete ..." +path);
            zk.delete(path  , -1);
        }

    }

    /**
     * 获取Zookeeper实例
     *
     * @return
     * @throws IOException
     */
    public static ZooKeeper getZookeeper() throws IOException {
        zookeeper = new ZooKeeper(connectString, sessionTimeout, null);
        return zookeeper;
    }

}
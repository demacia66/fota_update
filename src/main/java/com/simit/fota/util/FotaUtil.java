package com.simit.fota.util;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class FotaUtil {

    /**
     * 生成随机字符串
     * @return
     */
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }


    /**
     * 生成随机数
     * @param place 数据位数
     */
    public static String generateNumber(int place){
        Random random = new Random();
        int a = 1;
        for (int i = 0; i < place; i++) {
            a *= 10;
        }
        int rand = random.nextInt(a);
        String ticket = "" + rand;
        int differ = place - ticket.length();
        for (int i = 0; i < differ; i++) {
            ticket = "0" + ticket;
        }
        return ticket;
    }


    public static String getLocalPort() throws Exception {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        Set<ObjectName> objectNames = mBeanServer.queryNames(new ObjectName("*:type=Connector,*"), null);
        if (objectNames == null || objectNames.size() <= 0) {
            throw new IllegalStateException("Cannot get the names of MBeans controlled by the MBean server.");
        }
        for (ObjectName objectName : objectNames) {
            String protocol = String.valueOf(mBeanServer.getAttribute(objectName, "protocol"));
            String port = String.valueOf(mBeanServer.getAttribute(objectName, "port"));
            // windows下属性名称为HTTP/1.1, linux下为org.apache.coyote.http11.Http11NioProtocol
            if (protocol.equals("HTTP/1.1") || protocol.equals("org.apache.coyote.http11.Http11NioProtocol")) {
                return port;
            }
        }
        throw new IllegalStateException("Failed to get the HTTP port of the current server");
    }
}

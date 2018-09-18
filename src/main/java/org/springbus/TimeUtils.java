package org.springbus;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {


    /**
     * 毫秒转换成时间
     * @return
     */
    public static  String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fmDate=simpleDateFormat.format(new Date());
        return fmDate;
    }
}

package org.springbus.agent;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtils {

    public  static  void writeFile(String filePath,byte[] bytes) {
        try {
            File f=new File(filePath);
            File  parentFile=f.getParentFile();
            if(!parentFile.exists()){
                parentFile.mkdirs();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(bytes, 0, bytes.length);
            fileOutputStream.close();
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

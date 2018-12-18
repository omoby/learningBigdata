package JVM;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: ConstantOutOfMemory
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-27 下午10:35
 * Description:
 */
public class ConstantOutOfMemory {
    public static void main(String[] args){
        try{
            List<String> stringList = new ArrayList<String>();
            int item = 0;
            while (true){
                stringList.add(String.valueOf(item++).intern());
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}

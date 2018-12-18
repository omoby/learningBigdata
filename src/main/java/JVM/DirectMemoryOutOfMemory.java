package JVM;

import java.nio.ByteBuffer;

/**
 * FileName: DirectMemoryOutOfMemory
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-27 下午10:41
 * Description:
 */
public class DirectMemoryOutOfMemory {
    private static final int ONE_GB = 1024*1024*1024;
    private static int count = 1;
    public static void main(String[] args){
        try {
            while (true){
                ByteBuffer buffer = ByteBuffer.allocateDirect(ONE_GB);
                count++;
            }
        }catch (Exception e){
            System.out.println("Exception:instance created "+count);
            e.printStackTrace();
        }catch (Error e){
            System.out.println("Error:instance created "+count);
            e.printStackTrace();
        }
    }
}

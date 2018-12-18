package DT;

import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * FileName: JavaNIO
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-3 上午10:39
 * Description:
 *
 */
public class JavaNIO {
    public static void main(String[] args){
        IntBuffer intBuffer = IntBuffer.allocate(100);//开辟容量大小为100的IntBuffer
        ByteBuffer.allocate(5);//直接开辟缓冲区
        //ByteBuffer,CharBuffer,ShortBuffer,LongBuffer,FloatBuffer,DoubleBuffer
        System.out.println("Position: "+intBuffer.position()+ " ,Limit: "+intBuffer.limit()+" Capacity: "+intBuffer.capacity());
        int[] data = {9,8,7,6,5,4,3,2,1,0}; //定义整型数组
        intBuffer.put(100); //向缓存区写入整数100
        intBuffer.put(data);//直接把数组放入缓存区
        System.out.println("Position: "+intBuffer.position()+ " ,Limit: "+intBuffer.limit()+" Capacity: "+intBuffer.capacity());
        intBuffer.flip(); //重设缓冲区,
        System.out.println("Position: "+intBuffer.position()+ " ,Limit: "+intBuffer.limit()+" Capacity: "+intBuffer.capacity());
        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }

        intBuffer.position(2);
        intBuffer.limit(4);
        IntBuffer sliced = intBuffer.slice();
        for (int i = 0; i < sliced.capacity();i++){
            System.out.println(sliced.get(i));
            int item = sliced.get(i);
            sliced.put(item-100);

        }
        System.out.println("+++++++++++++++");
        sliced.flip();
        while(sliced.hasRemaining()){
            System.out.println(sliced.get());
        }

    }
}

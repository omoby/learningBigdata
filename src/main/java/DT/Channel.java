package DT;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * FileName: Channel
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-3 下午12:29
 * Description:
 */
public class Channel {
    public static void main(String[] args){
        //channelNIOWriteData(); //写数据
        channelNIOReadData(); //读文件
        channelMap(); //内存映射
    }

    private static void channelMap() {
        File file = new File("/usr/local/spark/README.md");
        FileInputStream input = null;
        try {
            input =new FileInputStream(file);
        }catch (Exception e){
            e.printStackTrace();
        }
        FileChannel fileChannelInput = null;
        fileChannelInput = input.getChannel();
        MappedByteBuffer map = null;
        try {
            map  = fileChannelInput.map(FileChannel.MapMode.READ_ONLY,0,file.length()); //将文件映射到内存中去
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = new byte[(int) file.length()];
        int index = 0;
        while (map.hasRemaining()){
            data[index++] = map.get();
        }
        System.out.println(new String(data));
        try {
            fileChannelInput.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void channelNIOReadData() {
        File fileInput = new File("/home/hadoop/learnJava/nio.txt");
        File fileOutput = new File("/home/hadoop/learnJava/nioout.txt");
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(fileInput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            output = new FileOutputStream(fileOutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        FileChannel fileChannelInput = null;
        FileChannel fileChannelOutput = null;
        fileChannelInput = input.getChannel();
        fileChannelOutput = output.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int tmp = 0;
        try{
            while ((tmp = fileChannelInput.read(buffer))!= -1){
                buffer.flip();
                fileChannelOutput.write(buffer);
                buffer.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            input.close();
            output.close();
            fileChannelInput.close();
            fileChannelOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void channelNIOWriteData() {
        String[] data = {"Scala","Spark","Java","Hadoop"};
        File file  = new File("/home/hadoop/learnJava/nio.txt");
        FileOutputStream fileOutputStream = null;
        FileChannel fileChannel = null;
        try {
             fileOutputStream = new FileOutputStream(file);
            fileChannel = fileOutputStream.getChannel();

        } catch (Exception e) {
            e.printStackTrace();
        }
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        for (String item:data){
            buffer.put(item.getBytes());
        }
        buffer.flip();
        try {
            fileChannel.write(buffer);
            fileChannel.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

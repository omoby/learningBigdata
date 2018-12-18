package DT;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * FileName: javaNet
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-3 下午4:55
 * Description:
 */
public class javaNet {
    public static void main(String[] aegs) throws Exception {


        ServerSocket server = null;
        Socket client = null;
        PrintStream output = null;
        server = new ServerSocket(9999);
        boolean flag = true;
        while (flag){
            System.out.println("------------It's ready to accept client's request-------------");
            client = server.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
        }

        output = new PrintStream(client.getOutputStream());
        output.println("I get it!");
        output.close();
        client.close();
        server.close();
    }

    /**
     *获取IP地址
     * @throws Exception
     */
    public static void getIp() throws Exception{
        InetAddress local = null;
        InetAddress remote = null;
        local = InetAddress.getLocalHost(); //返回本地主机
        //在给定主机名的情况下确定主机的 IP 地址。
        //主机名可以是机器名（如 "java.sun.com"），也可以是其 IP 地址的文本表示形式。如果提供字面值 IP 地址，则仅检查地址格式的有效性。
        remote = InetAddress.getByName("spark.apache.org");
        System.out.println("Local IP: "+local.getHostAddress()); //返回IP地址字符串（以文本表现形式）
        System.out.println("Spark IP: "+ remote.getHostAddress());//返回IP地址字符串（以文本表现形式）
        URL url = new URL("http://spark.apache.org");
        InputStream inputStream = url.openStream();
        Scanner sc = new Scanner(inputStream);
        sc.useDelimiter("\n"); //设置输入的分隔符
        while (sc.hasNext()){
            System.out.println(sc.next());
        }

    }

    /**
     * 获取内容的长度和类型
     * @throws Exception
     */
    public static void getType() throws Exception{
        URL url = new URL("http://spark.apache.org");
        URLConnection urlConnection = url.openConnection();
        System.out.println("The length: "+urlConnection.getContentLength());
        System.out.println("The Content Type: "+urlConnection.getContentType());
    }

    /**
     * Socket编程
     * @throws Exception
     */
    public static void socketClient() throws  Exception{
        ServerSocket server = null;
        Socket client = null;
        PrintStream output = null;
        server = new ServerSocket(9999);
        System.out.println("------------It's ready to accept client's request-------------");
        client = server.accept();
        output = new PrintStream(client.getOutputStream());
        output.println("I get it!");
        output.close();
        client.close();
        server.close();
    }
}

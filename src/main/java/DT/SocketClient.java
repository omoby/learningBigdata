package DT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * FileName: SocketClient
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-3 下午5:32
 * Description:
 */
public class SocketClient {
    public static void main(String[] args) throws Exception{
        Socket client = null;
        client = new Socket("localhost",9999);
        BufferedReader bufferedReader = null;
        bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        System.out.println("The content from server is : "+bufferedReader.readLine());
        bufferedReader.close();
        client.close();


    }
}

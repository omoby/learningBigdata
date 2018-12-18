package NowCoder;

import java.util.Scanner;

/**
 * FileName: HexToTen
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-25 上午10:04
 * Description:华为 写出一个程序，接受一个十六进制的数值字符串，输出该数值的十进制字符串。（多组同时输入 ）
 */
public class HexToTen {
    public static void main(String[] args){
        Scanner input  = new Scanner(System.in);
        while(input.hasNext()){
            String in = input.next();
            String num = in.substring(2).toUpperCase();
            int result = 0;
            for(int i = 0; i < num.length();i++){
                char c = num.charAt(i);
                if(c >='0' && c <= '9'){
                    result = result * 16 +Integer.parseInt(String.valueOf(c));
                }else if(c >='A' && c <= 'F'){
                    int mi = (int)(c) - 55;
                    result = result * 16 + mi;
                }else{
                    result = 0;
                }
            }
            System.out.println(result);
        }

    }
}
/*
import java.util.*;
public class Main{
    public static void main(String[] args){
        Scanner input =  new Scanner(System.in);
        while(input.hasNext()){
            String hex = input.next();
            System.out.println(Integer.decode(hex));
        }

    }
}
 */

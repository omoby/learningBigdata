package NowCoder;

import java.util.Scanner;

/**
 * FileName: RevserString
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:54
 * Description:
 * 题目描述
 * 写出一个程序，接受一个字符串，然后输出该字符串反转后的字符串。例如：
 */

public class RevserString {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(input.hasNext()){
            StringBuffer buf = new StringBuffer(input.nextLine());
            String result = buf.reverse().toString();
            System.out.println(result);
        }
    }
}

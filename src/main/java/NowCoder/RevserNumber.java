package NowCoder;

import java.util.Scanner;

/**
 * FileName: RevserNumber
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:52
 * Description:
 * 题目描述
 * 描述：
 * 输入一个整数，将这个整数以字符串的形式逆序输出
 * 程序不考虑负数的情况，若数字含有0，则逆序形式也含有0，如输入为100，则输出为001
 */
public class RevserNumber {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(input.hasNext()){
            String number = input.next();
            String result= "";
            for(int i = number.length()-1;i >= 0;i--)
                result +=number.charAt(i);
            System.out.println(result);
        }
    }
}

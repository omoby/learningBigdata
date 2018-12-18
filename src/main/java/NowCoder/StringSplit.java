package NowCoder;

import java.util.Scanner;

/**
 * FileName: StringSplit
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:39
 * Description:题目描述
 * •连续输入字符串，请按长度为8拆分每个字符串后输出到新的字符串数组；
 * •长度不是8整数倍的字符串请在后面补数字0，空字符串不处理。
 */

public class StringSplit {
    public static void main(String[] args){
        Scanner input =  new Scanner(System.in);
        while(input.hasNextLine()){
            String s  = input.nextLine();
            splited(s);
        }
    }
    public static void splited(String s){
        while(s.length()>=8){
            System.out.println(s.substring(0,8));
            s=s.substring(8);
        }
        if(s.length()<8 && s.length()>0){
            s+="00000000";
            System.out.println(s.substring(0,8));
        }
    }
}

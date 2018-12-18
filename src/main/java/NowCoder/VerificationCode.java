package NowCoder;

import java.util.Scanner;

/**
 * FileName: VerificationCode
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午6:04
 * Description:题目描述
 * 密码要求:
 * 1.长度超过8位
 * 2.包括大小写字母.数字.其它符号,以上四种至少三种
 * 3.不能有相同长度超2的子串重复
 * 说明:长度超过2的子串
 */
public class VerificationCode {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(input.hasNext()){
            String str = input.next();
            if(str.length() < 9 || str ==null){
                System.out.println("NG");
            }else{


                int[] arr = {0,0,0,0};
                for(int i = 0; i < str.length(); i++){
                    if(str.charAt(i) >= '0' && str.charAt(i) <= '9')
                        arr[0] = 1;
                    else if(str.charAt(i) >= 'a' && str.charAt(i) <= 'z')
                        arr[1] = 1;
                    else if(str.charAt(i) >= 'A' && str.charAt(i) <= 'Z')
                        arr[2] = 1;
                    else
                        arr[3] = 1;
                }
                int total = 0;
                for(int i = 0; i<arr.length;i++)
                    total += arr[i];
                if(total >=3 && checkSubString(str))
                    System.out.println("OK");
                else
                    System.out.println("NG");
            }
        }
    }
    public static boolean checkSubString(String str){
        for(int i = 0; i < str.length()-3; i++){
            String str1 = str.substring(i,i+3);
            String str2 = str.substring(i+3,str.length());
            if(str2.contains(str1))
                return false;
        }
        return true;
    }
}

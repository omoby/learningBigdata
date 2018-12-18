package NowCoder;

import java.util.Arrays;
import java.util.Scanner;

/**
 * FileName: CombinedString
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午6:38
 * Description:
 * 题目描述
 * 按照指定规则对输入的字符串进行处理。
 *
 * 详细描述：
 *
 * 将输入的两个字符串合并。
 *
 * 对合并后的字符串进行排序，要求为：下标为奇数的字符和下标为偶数的字符分别从小到大排序。这里的下标意思是字符在字符串中的位置。
 *
 * 对排序后的字符串进行操作，如果字符为‘0’——‘9’或者‘A’——‘F’或者‘a’——‘f’，则对他们所代表的16进制的数进行BIT倒序的操作，并转换为相应的大写字符。如字符为‘4’，为0100b，则翻转后为0010b，也就是2。转换后的字符为‘2’； 如字符为‘7’，为0111b，则翻转后为1110b，也就是e。转换后的字符为大写‘E’。
 *
 *
 * 举例：输入str1为"dec"，str2为"fab"，合并为“decfab”，分别对“dca”和“efb”进行排序，排序后为“abcedf”，转换后为“5D37BF”
 *
 * 接口设计及说明：
 *
 * /*
 *
 * 功能:字符串处理
 *
 * 输入:两个字符串,需要异常处理
 *
 * 输出:合并处理后的字符串，具体要求参考文档
 *
 * 返回:无
 *
 *
 *
         *void ProcessString(char*str1,char*str2,char*strOutput)
         *
         *{
         *
         *}
 */
public class CombinedString {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(input.hasNext()){
            StringBuffer res = new StringBuffer();
            String str1 = input.next();
            String str2 = input.next();
            String com = str1 + str2;
            str1 = "";
            str2 = "";
            for(int i = 0; i < com.length();i++){
                if(i % 2 == 0)
                    str1 += com.charAt(i);
                else
                    str2 += com.charAt(i);
            }
            char[] str11 = str1.toCharArray();
            char[] str22 = str2.toCharArray();
            Arrays.sort(str11);
            Arrays.sort(str22);
            com = "";
            int le1 = str11.length;
            int le2 = str22.length;
            int i = 0;
            for( i = 0; i <Math.min(le1,le2);i++){
                com += str11[i];
                com += str22[i];
            }
            if(le1 > le2 )
                for (int j = i; j < Math.max(le1,le2);j++)
                    com += str11[j];
            if(le1 < le2)
                for (int j = i; j < Math.max(le1,le2);j++)
                    com += str22[j];
            for(i = 0 ; i < com.length(); i++){
                char c = com.charAt(i);
                if(String.valueOf(c).matches("[A-Fa-f]")){
                    String string = revser(Integer.toBinaryString(Integer.valueOf(Character.toLowerCase(c))-87));
                    int x = Integer.parseInt(string,2);
                    res.append(NX(x));
                }else if(String.valueOf(c).matches("[0-9]")){
                    String f = "";
                    String hex = Integer.toBinaryString(Integer.valueOf(String.valueOf(c)));
                    if(hex.length()<4){
                        for(int j = 0; j < 4-hex.length();j++)
                            f += "0";
                    }
                    String string = revser(f+hex);
                    int x = Integer.parseInt(string,2);
                    res.append(NX(x));
                }else
                    res.append(c);
            }
            System.out.println(res.toString());
        }
    }
    public static String revser(String str){
        StringBuffer re = new StringBuffer();
        return re.append(str).reverse().toString();
    }

    public static String NX(int x){
        if(x == 10)
            return "A";
        else if(x == 11)
            return "B";
        else if(x ==12)
            return "C";
        else if(x == 13)
            return "D";
        else if(x == 14)
            return "E";
        else if(x == 15)
            return "F";
        return String.valueOf(x);
    }
}

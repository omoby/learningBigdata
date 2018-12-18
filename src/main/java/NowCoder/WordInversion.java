package NowCoder;

import java.util.Scanner;

/**
 * FileName: WordInversion
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午6:39
 * Description:题目描述
 * 对字符串中的所有单词进行倒排。
 *
 * 说明：
 *
 * 1、每个单词是以26个大写或小写英文字母构成；
 *
 * 2、非构成单词的字符均视为单词间隔符；
 *
 * 3、要求倒排后的单词间隔符以一个空格表示；如果原字符串中相邻单词间有多个间隔符时，倒排转换后也只允许出现一个空格间隔符；
 *
 * 4、每个单词最长20个字母；
 */
public class WordInversion {
    public  static  void main(String[] args){
        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()){
            String str = input.nextLine();
            String result = "";
            str = str.trim();
            str = " "+str;
            int length = str.length();
            if (length == 2){
                System.out.println(str.trim());
            }else if (length>2){
                int i = length-1;
                int j = length;
                boolean flag = true;
                while (i >=0){
                    char c = str.charAt(i);
                    if ((c >='A' && c <= 'Z') || (c >= 'a' && c <= 'z')){
                        i--;
                        flag = true;
                    }else if (flag){
                        flag = false;
                        result += str.substring(i+1,j);
                        result += " ";
                        j = i;
                        i--;
                    }else {
                        j = i;
                        i--;
                    }
                }
                System.out.println(result.trim());
            }
        }
    }
}

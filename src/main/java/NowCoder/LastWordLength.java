package NowCoder;

import java.util.Scanner;

/**
 * FileName: LastWordLength
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:35
 * Description:计算字符串最后一个单词的长度，单词以空格隔开。
 */
public class LastWordLength {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        String s = "";
        while(input.hasNextLine()){
            s = input.nextLine();
            System.out.println(s.length()-1 - s.lastIndexOf(" "));
        }
    }
}


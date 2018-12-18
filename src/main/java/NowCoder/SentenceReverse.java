package NowCoder;

import java.util.Scanner;

/**
 * FileName: SentenceReverse
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:55
 * Description:题目描述
 * 将一个英文语句以单词为单位逆序排放。例如“I am a boy”，逆序排放后为“boy a am I”
 * 所有单词之间用一个空格隔开，语句中除了英文字母外，不再包含其他字符
 *
 *
 * 接口说明
 *
 * /**
 *  * 反转句子
 *  *
 *  * @param sentence 原句子
 *  * @return 反转后的句子
 *
 *public String reverse(String sentence);
 */
public class SentenceReverse {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(input.hasNextLine()){
            String result = "";
            String[] splited = input.nextLine().split(" ");
            for(int i = splited.length-1;i >=0;i--)
                result+=splited[i]+" ";
            System.out.println(result.trim());
        }
    }
}

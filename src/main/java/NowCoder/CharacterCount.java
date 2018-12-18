package NowCoder;

import java.util.Scanner;
import java.util.TreeSet;

/**
 * FileName: CharacterCount
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:50
 * Description:题目描述
 * 编写一个函数，计算字符串中含有的不同字符的个数。字符在ACSII码范围内(0~127)。不在范围内的不作统计。
 */
public class CharacterCount {
    public static void main(String[] args){
        Scanner input  = new Scanner(System.in);
        while(input.hasNextLine()){
            String str = input.nextLine();
            TreeSet<Integer> set  =  new TreeSet<>();
            int total = 0;
            for(int i = 0; i < str.length();i++){
                char c = str.charAt(i);
                int num =(int)c;
                if(num >-1 && num < 128){
                    if(!set.contains(num)){
                        set.add(num);
                        total++;
                    }

                }
            }
            System.out.println(total);
        }
    }
}

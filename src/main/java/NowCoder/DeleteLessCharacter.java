package NowCoder;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * FileName: DeleteLessCharacter
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午6:10
 * Description:题目描述
 * 实现删除字符串中出现次数最少的字符，若多个字符出现次数一样，则都删除。输出删除这些单词后的字符串，字符串中其它字符保持原来的顺序。
 */
public class DeleteLessCharacter {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while (input.hasNext()){
            String str = input.next();
            Map<Character,Integer> set = new HashMap<>();
            for(int i =0; i < str.length();i++){
                char c= str.charAt(i);
                if(set.containsKey(c))
                    set.put(c,set.get(c)+1);
                else
                    set.put(c,1);
            }
            int min = 100;
            char[] dele = new char[20];
            int count = 0;
            for(char ele:set.keySet()){
                if(min > set.get(ele)) {
                    min = set.get(ele);
                }
            }
            Iterator<Map.Entry<Character,Integer>> map = set.entrySet().iterator();
            while (map.hasNext()){
                Map.Entry<Character, Integer> entry = map.next();
                int value = entry.getValue();
                if(min == value){
                    map.remove();
                    dele[count] = entry.getKey();
                    count++;
                }

            }
            for(int i = 0; i < str.length();i++){
                boolean flage = true;
                for (int j = 0; j < count; j++){
                    if (str.charAt(i) == dele[j])
                        flage = false;
                }
                if(flage)
                    System.out.print(str.charAt(i));
            }
            System.out.println();

        }
    }
}

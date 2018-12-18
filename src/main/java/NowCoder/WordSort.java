package NowCoder;

import java.util.ArrayList;
import java.util.Scanner;

import static java.util.Collections.sort;

/**
 * FileName: WordSort
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:57
 * Description:题目描述
 * 给定n个字符串，请对n个字符串按照字典序排列。
 */
public class WordSort {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        ArrayList<String> set = new ArrayList<String>();
        int number = input.nextInt();
        for(int i = 0; i <= number&&input.hasNext();i++){
            set.add(input.next());
        }
        sort(set);
        for(String ele:set){
            System.out.println(ele);
        }
    }
}

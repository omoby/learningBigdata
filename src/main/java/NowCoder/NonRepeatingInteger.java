package NowCoder;

import java.util.Scanner;
import java.util.TreeSet;

/**
 * FileName: NonRepeatingInteger
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:48
 * Description:题目描述
 输入一个int型整数，按照从右向左的阅读顺序，返回一个不含重复数字的新的整数。
 */
public class NonRepeatingInteger {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(input.hasNext()){
            TreeSet<Integer> set = new TreeSet<>();
            int num = input.nextInt();
            String result= "";
            int left = 0;
            if(num < 0)
                result +="-";
            while(num !=0){
                left = num %10;
                num /= 10;
                if(!set.contains(left)){
                    result += left;
                    set.add(left);
                }
            }
            System.out.println(result);
        }
    }
}

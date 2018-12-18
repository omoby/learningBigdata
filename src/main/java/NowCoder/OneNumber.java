package NowCoder;

import java.util.Scanner;

/**
 * FileName: OneNumber
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:58
 * Description:题目描述
 * 输入一个int型的正整数，计算出该int型数据在内存中存储时1的个数。
 */
public class OneNumber {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        while(sc.hasNext()){
            int number = sc.nextInt();
            int count = 0;
            while(number >0){
                count+=number %2;
                number /= 2;
            }
            System.out.println(count);
        }
    }
}

package NowCoder;

import java.util.Scanner;

/**
 * FileName: PrimeFactor
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:42
 * Description:题目描述
 * 功能:输入一个正整数，按照从小到大的顺序输出它的所有质数的因子（如180的质数因子为2 2 3 3 5 ）
 *
 * 最后一个数后面也要有空格
 */
public class PrimeFactor {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(input.hasNextLong()){
            long num = input.nextLong();
            long t = num;
            String result= "";
            for(long i =2;i<=t;i++){
                while(num %i==0){
                    num /=i;
                    result = result+i+" ";
                }
            }
            System.out.println(result);
        }
    }
}


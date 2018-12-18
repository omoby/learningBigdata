package NowCoder;

import java.util.Scanner;

/**
 * FileName: Approximation
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:43
 * Description:题目描述
 * 写出一个程序，接受一个正浮点数值，输出该数值的近似整数值。如果小数点后数值大于等于5,向上取整；小于5，则向下取整
 */
public class Approximation {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(input.hasNext()){
            double number = input.nextDouble();
            int num = (int)number;
            int result = 0;
            if(number - num < 0.5)
                result = num;
            else
                result = num+1;
            System.out.println(result);
        }
    }
}

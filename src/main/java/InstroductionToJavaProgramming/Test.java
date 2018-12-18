package InstroductionToJavaProgramming;

import java.util.Scanner;

/**
 * FileName: Test
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-2 上午11:37
 * Description:
 */
public class Test {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (input.hasNext()){
            double a = input.nextDouble();
            double b = input.nextDouble();
            double e = input.nextDouble();
            double c = input.nextDouble();
            double d = input.nextDouble();
            double f = input.nextDouble();
            if((a*d - b*c) != 0){
                double x = (d *e - b * f) /(a*d - b*c);
                double y = ( a * f - e *c) /(a*d - b*c);
                System.out.println("x : "+x);
                System.out.println("y : "+y);
            }else
                System.out.println("Didn't have result!");
        }
    }


}

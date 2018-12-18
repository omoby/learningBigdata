package NowCoder;

import java.math.BigInteger;
import java.util.Scanner;

/**
 * FileName: MaxPoint
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-30 上午10:59
 * Description:
 */
public class MaxPoint {
    public static  void main(String[] args){
        Scanner input = new Scanner(System.in);
            int num = input.nextInt();
            BigInteger[][] bigIntegers = new BigInteger[num][3];
            for(int i = 0; i < num; i++){
                bigIntegers[i][0] = input.nextBigInteger();
                bigIntegers[i][1] = input.nextBigInteger();
                bigIntegers[i][2] = BigInteger.valueOf(1);
            }
            for (int i = 0; i < num; i++){
                BigInteger x = bigIntegers[i][0];
                BigInteger y = bigIntegers[i][1];
                for (int j = 0; i!=j &&j < num;j++){
                    if (bigIntegers[j][0].compareTo(x)>=0 && bigIntegers[j][1].compareTo(y) >=0){
                        bigIntegers[i][2] = BigInteger.valueOf(0);
                        break;
                    }
                }
            }
            for(int i = 0; i < num; i++){
                if (bigIntegers[i][2] == BigInteger.valueOf(1)){
                    System.out.println(bigIntegers[i][0] + " "+ bigIntegers[i][1]);
                }
            }
    }
}

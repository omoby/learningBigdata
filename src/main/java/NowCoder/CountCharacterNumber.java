package NowCoder;

import java.util.Scanner;

/**
 * FileName: CountCharacterNumber
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:36
 * Description:
 */
public class CountCharacterNumber {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        String str = input.nextLine();
        String ch = input.nextLine();
        str = str.toUpperCase();
        char c = ch.toUpperCase().charAt(0);
        int count = 0;
        for(int i = 0;i < str.length();i++){
            if(str.charAt(i) == c)
                count++;
        }
        System.out.println(count);
    }
}

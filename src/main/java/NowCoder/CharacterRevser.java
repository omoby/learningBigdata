package NowCoder;

import java.util.Scanner;

/**
 * FileName: CharacterRevser
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午3:28
 * Description:
 */
public class CharacterRevser {
    public  static  void main(String[] args){
        Scanner input = new Scanner(System.in);
        while (input.hasNextLine()){
            String str = input.nextLine();
            String result = "";
            str = str.trim();
            str = " "+str;
            int length = str.length();
            if (length == 2){
                System.out.println(str.trim());
            }else if (length>2){
                int i = length-1;
                int j = length;
                boolean flag = true;
                while (i >=0){
                    char c = str.charAt(i);
                    if ((c >='A' && c <= 'Z') || (c >= 'a' && c <= 'z')){
                        i--;
                        flag = true;
                    }else if (flag){
                        flag = false;
                        result += str.substring(i+1,j);
                        result += " ";
                        j = i;
                        i--;
                    }else {
                        j = i;
                        i--;
                    }
                }
                System.out.println(result.trim());
            }
        }
    }
}

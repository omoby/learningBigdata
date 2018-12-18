package NowCoder;

import java.util.Arrays;
import java.util.Scanner;

/**
 * FileName: CharacterComp
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-25 下午1:31
 * Description:
 */
public class CharacterComp {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(input.hasNext()){
            StringBuffer res = new StringBuffer();
            String str1 = input.next();
            String str2 = input.next();
            String com = str1 + str2;
            str1 = "";
            str2 = "";
            for(int i = 0; i < com.length();i++){
                if(i % 2 == 0)
                    str1 += com.charAt(i);
                else
                    str2 += com.charAt(i);
            }
            char[] str11 = str1.toCharArray();
            char[] str22 = str2.toCharArray();
            Arrays.sort(str11);
            Arrays.sort(str22);
            com = "";
            int le1 = str11.length;
            int le2 = str22.length;
            int i = 0;
            for( i = 0; i <Math.min(le1,le2);i++){
                com += str11[i];
                com += str22[i];
            }
            if(le1 > le2 )
                for (int j = i; j < Math.max(le1,le2);j++)
                    com += str11[j];
            if(le1 < le2)
                for (int j = i; j < Math.max(le1,le2);j++)
                    com += str22[j];
            for(i = 0 ; i < com.length(); i++){
                char c = com.charAt(i);
                if(String.valueOf(c).matches("[A-Fa-f]")){
                    String string = revser(Integer.toBinaryString(Integer.valueOf(Character.toLowerCase(c))-87));
                    int x = Integer.parseInt(string,2);
                    res.append(NX(x));
                }else if(String.valueOf(c).matches("[0-9]")){
                    String f = "";
                    String hex = Integer.toBinaryString(Integer.valueOf(String.valueOf(c)));
                    if(hex.length()<4){
                        for(int j = 0; j < 4-hex.length();j++)
                            f += "0";
                    }
                    String string = revser(f+hex);
                    int x = Integer.parseInt(string,2);
                    res.append(NX(x));
                }else
                    res.append(c);
            }
            System.out.println(res.toString());
        }
    }
    public static String revser(String str){
        StringBuffer re = new StringBuffer();
        return re.append(str).reverse().toString();
    }

    public static String NX(int x){
        if(x == 10)
            return "A";
        else if(x == 11)
            return "B";
        else if(x ==12)
            return "C";
        else if(x == 13)
            return "D";
        else if(x == 14)
            return "E";
        else if(x == 15)
            return "F";
        return String.valueOf(x);
    }
}

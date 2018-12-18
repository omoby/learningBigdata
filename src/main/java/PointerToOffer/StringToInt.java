package PointerToOffer;

/**
 * FileName: StringToInt
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-27 下午2:42
 * Description:将输入的字符串转化为整数
 */
public class StringToInt {
    public static void main(String[] args){
        int max = Integer.MAX_VALUE;
        int min = Integer.MIN_VALUE;
        System.out.println("Max: "+max);
        System.out.println("Min "+min);
        stringToInt("181848132424524");
        stringToInt("-sba154815");

    }
    public static void stringToInt(String string){
        if (string == null || string.length()==0 || isNumber(string) == false){
            System.out.println("Input is Error!");

        }else {
            int data = 0;
            int mask = 1;
            if (string.charAt(0) == '-'){
                mask = -1;
                for (int index = 1; index < string.length(); index++){
                    data = data * 10 + (int)(string.charAt(index)-'0');
                }
                if (data * mask >= Integer.MIN_VALUE)
                    System.out.println(data*mask);
                else
                    System.out.println("Input number too Lower");
            }else {
                for (int index = 0; index < string.length(); index++){
                    data = data * 10 + (char)(string.charAt(index) - '0');
                }
                if (data <= Integer.MAX_VALUE)
                    System.out.println(data);
                else
                    System.out.println("Input number too Bigger!");
            }



        }
    }
    public  static boolean isNumber(String s){
        int index = 0;
        if (s.charAt(0) == '-' && s.length() >1){
            index = 1;
        }
        for ( ; index < s.length();index++){
            if (s.charAt(index) <'0' || s.charAt(index) >'9')
                return false;
        }
        return true;
    }
}

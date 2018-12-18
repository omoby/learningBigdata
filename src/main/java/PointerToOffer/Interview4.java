package PointerToOffer;

/**
 * FileName: Interview4
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-1 下午4:36
 * Description:
 */
public class Interview4 {
    public static void main(String[] args){
        String str = "We Are Happy.";
        String newString = replaceSpace(str);
        System.out.println(newString);

    }
    public static String replaceSpace(String str) {
        if (str == null)
            return null;
        StringBuffer newString = new StringBuffer();
        for (int i = 0; i <str.length();i++){
            if (str.charAt(i)==' '){
                newString.append("%20");
            }else
                newString.append(str.charAt(i));
        }
        return newString.toString();
    }
}

package NowCoder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * FileName: SimplePassword
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午6:06
 * Description:题目描述
 * 密码是我们生活中非常重要的东东，我们的那么一点不能说的秘密就全靠它了。哇哈哈. 接下来渊子要在密码之上再加一套密码，虽然简单但也安全。
 * 假设渊子原来一个BBS上的密码为zvbo9441987,为了方便记忆，他通过一种算法把这个密码变换成YUANzhi1987，这个密码是他的名字和出生年份，怎么忘都忘不了，而且可以明目张胆地放在显眼的地方而不被别人知道真正的密码。
 * 他是这么变换的，大家都知道手机上的字母： 1--1， abc--2, def--3, ghi--4, jkl--5, mno--6, pqrs--7, tuv--8 wxyz--9, 0--0,就这么简单，渊子把密码中出现的小写字母都变成对应的数字，数字和其他的符号都不做变换，
 * 声明：密码中没有空格，而密码中出现的大写字母则变成小写之后往后移一位，如：X，先变成小写，再往后移一位，不就是y了嘛，简单吧。记住，z往后移是a哦。
 */
public class SimplePassword {
    public static void main(String[] args){
        Scanner input  = new Scanner(System.in);
        Map<String,Integer> set  = new LinkedHashMap<String,Integer>();
        set.put("1",1);
        set.put("abc",2);
        set.put("def",3);
        set.put("ghi",4);
        set.put("jkl",5);
        set.put("mno",6);
        set.put("pqrs",7);
        set.put("tuv",8);
        set.put("wxyz",9);
        set.put("0",0);
        while(input.hasNext()){
            String result = "";
            String str = input.next();
            for(int i =0 ;i < str.length();i++){
                char c = str.charAt(i);
                if(c >='A' && c < 'Z')
                    result +=String.valueOf((char)(c+1));
                else if(c=='Z')
                    result +=String.valueOf('A');
                else if(c >= 'a' && c <= 'z'){
                    for(String ele:set.keySet()){
                        if (ele.contains(String.valueOf(c)))
                            result += String.valueOf(set.get(ele));
                    }
                }
                else
                    result +=String.valueOf(c);
            }
            System.out.println(result.toLowerCase());
        }
    }
}

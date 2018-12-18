package javabook.sort;

/**
 * FileName: KMP
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-21 上午9:55
 * Description:
 */
public class KMP {
    public static void main(String[] args){
        String target = "abcdacbdacdb";
        String mode = "acbd";
        boolean result = javabook.sort.KMPAlgotithm.KMP(target,mode);
        System.out.println(result);
    }
}

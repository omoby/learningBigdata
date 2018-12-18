package javabook.sort;

/**
 * FileName: KMPAlgotithm
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-21 上午9:49
 * Description:
 */
public class KMPAlgotithm {
    public  static boolean KMP(String target,String mode){
        char[] tar = target.toCharArray();
        char[] mod = mode.toCharArray();
        int[] next = getNext(mode);
        int i = 0;
        int j = 0;
        while(i < tar.length && j < mod.length){
            if (j == -1 || tar[i] == mod[j]){
                i++;
                j++;
            }else{
                //i = i-j+1;
                //j = 0;
                j = next[j];
            }
        }
        if (j == mod.length)
            return true;
        else
            return false;
    }

    public static int[] getNext(String mode){
        int[] next = new int[mode.length()];
        int j = -1;
        int k = 0;
        next[0] = -1;
        while (k < mode.length()-1){
            if(j == -1 || mode.charAt(j) == mode.charAt(k)){
                ++j;
                ++k;
                next[k] = j;
            }else{
                j = next[j];
            }
        }
        return next;
    }

}

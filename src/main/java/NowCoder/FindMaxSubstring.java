package NowCoder;

import java.util.Scanner;

/**
 * FileName: FindMaxSubstring
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午4:12
 * Description:Catcher是MCA国的情报员，他工作时发现敌国会用一些对称的密码进行通信，比如像这些ABBA，ABA，A，123321，但是他们有时会在开始或结束时加入一些无关的字符以防止别国破解。比如进行下列变化 ABBA->12ABBA,ABA->ABAKK,123321->51233214　。因为截获的串太长了，而且存在多种可能的情况（abaaab可看作是aba,或baaab的加密形式），Cathcer的工作量实在是太大了，他只能向电脑高手求助，你能帮Catcher找出最长的有效密码串吗？
 */
/*
public class FindMaxSubstring {
    public  static  void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(input.hasNextLine()){
            String str1 = input.nextLine();
            if (str1.length() >0 && str1 != null){
                String str2 = new StringBuffer(str1).reverse().toString();
                int max = findMaxSubstring(str1,str2);
                System.out.println(max);
            }

        }
    }

    public static int findMaxSubstring(String str1,String str2){
        int lengt = str1.length();
        int[][] arr = new int[lengt+1][lengt+1];
        int max = 0;
        for (int i = 1; i < lengt+1;i++){
            char a = str1.charAt(i-1);
            for (int j =1; j < lengt+1; j++){
                char b = str2.charAt(j-1);
                if(a == b){
                    arr[i][j] = arr[i-1][j-1] + 1;
                }else{
                    arr[i][j] = 0;
                }
                if (max < arr[i][j])
                    max = arr[i][j];
            }
        }
        return  max;
    }
}
*/
import java.util.Scanner;
public class FindMaxSubstring{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(input.hasNextLine()){
            String str1 = input.nextLine();
            if(str1.length() > 0){
                String str2 = new StringBuilder(str1).reverse().toString();
                int max = findMaxSubstring(str1,str2);
                System.out.println(max);
            }
        }
    }
    public static int findMaxSubstring(String str1,String str2){
        int le = str1.length();
        int max = 0;
        int[][] arr = new int[le+1][le+1];
        for(int i = 1; i < le+1; i++){
            char c1 = str1.charAt(i-1);
            for(int j = 1;j < le+1;j++){
                char c2 = str2.charAt(j-1);
                if(c1 == c2){
                    arr[i][j] = arr[i-1][j-1]+1;
                }else{
                    arr[i][j] = 0;
                }
                if(max < arr[i][j])
                    max = arr[i][j];
            }

        }
        return max;
    }
}

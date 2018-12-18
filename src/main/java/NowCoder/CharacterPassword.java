package NowCoder;

import java.util.Scanner;

/**
 * FileName: CharacterPassword
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午6:36
 * Description:
 */
public class CharacterPassword {

    public static void main(String[] args){
        Scanner input  = new Scanner(System.in);
        while(input.hasNext()){
            char[] str1 = input.nextLine().toCharArray();
            char[] str11 = new char[str1.length];
            Encrypt(str1,str11);
            char[] str2 = input.nextLine().toCharArray();
            char[] str22 = new char[str2.length];
            unEncrypt(str2,str22);
        }


    }
    public static void Encrypt(char aucPassword[],char aucResult[]){
        int length = aucPassword.length;
        for (int i = 0; i < length; i++){
            char c = aucPassword[i];
            if(c >='0' && c <'9')
                aucResult[i] = (char)(c+1);
            else if (c =='9')
                aucResult[i] = '0';
            else if (c >='a' && c <'z')
                aucResult[i] = (char)(c-32+1);
            else if (c == 'z')
                aucResult[i] = 'A';
            else if(c >='A' && c < 'Z')
                aucResult[i] = (char)(c+32+1);
            else if(c == 'Z')
                aucResult[i] = 'a';
            else
                aucResult[i] = c;

        }
        String pas = "";
        for (int i = 0; i < length; i++)
            pas+=aucResult[i];
        //pas+="\0";
        System.out.println(pas);
    }
    public static void unEncrypt(char result[] ,char password[]){
        int length  = result.length;
        for (int i = 0; i < length;i++){
            char c = result[i];
            if(c >'0' && c <= '9')
                password[i] = (char)(c-1);
            else if (c == '0')
                password[i] = '9';
            else if(c >='B'&& c <= 'Z')
                password[i] = (char)(c+32-1);
            else if (c == 'A')
                password[i] = 'z';
            else if(c >'a' && c <= 'z')
                password[i] = (char)(c -32 -1);
            else if (c == 'a')
                password[i] ='Z';
            else
                password[i] = c;

        }
        String string = "";
        for (int i = 0; i < length; i++)
            string+=password[i];
        //string +="\0";
        System.out.println(string);

    }
}


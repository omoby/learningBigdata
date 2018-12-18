package NowCoder;

/**
 * FileName: IP
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-25 上午10:23
 * Description:
 */

import java.util.Scanner;

public class IP {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        int a = 0, b = 0,c = 0, d = 0, e = 0;
        int err = 0;
        int pip = 0;
        while(input.hasNext()){
            String str = input.next();
            String[] ip = str.split("~")[0].split("\\.");
            String[] ipMask = str.split("~")[1].split("\\.");
            if(checkMask(ipMask)){
                if(checkIp(ip)){
                    int first = Integer.parseInt(ip[0]);
                    int second = Integer.parseInt(ip[1]);
                    if( first>=0 && first <=126){
                        a++;
                        if(first == 10){
                            pip++;
                            System.out.println(str);
                        }

                    }else if(first >= 128 && first <=191){
                        b++;
                        if(first == 172 && (second >=16 && second <=31)){
                            pip++;
                            System.out.println(str);
                        }

                    }else if(first >= 192 && first <= 223){
                        c++;
                        if(first == 192 && second ==168 ){
                            pip++;
                            System.out.println(str);
                        }

                    }else if(first >= 224 && first <= 239){
                        d++;
                    }else if(first >= 240 && first <= 255){
                        e++;
                    }
                }else
                    err++;
            }else
                err++;
        }
        System.out.println(a +" "+ b +" "+ c+ " "+d+" "+e+" "+err+" "+pip);
    }

    /**
     * 检查子网掩码是否合法
     * @param mask
     * @return
     */
    public static boolean checkMask(String[] mask){
        if(mask[0].equals("255")){
            if(mask[1].equals("255")){
                if(mask[2].equals("255")){
                    if(mask[3].equals("254")||mask[3].equals("252")||mask[3].equals("248")||
                            mask[3].equals("240")||mask[3].equals("224")||mask[3].equals("192")||
                            mask[3].equals("128")||mask[3].equals("0"))
                        return true;
                    else
                        return false;

                }else if(mask[2].equals("254")||mask[2].equals("252")||mask[2].equals("248")||
                        mask[2].equals("240")||mask[2].equals("224")||mask[2].equals("192")||
                        mask[2].equals("128")||mask[2].equals("0"))
                    if(mask[3].equals("0"))
                        return true;
                return false;
            }else if(mask[1].equals("254")||mask[1].equals("252")||mask[1].equals("248")||
                    mask[1].equals("240")||mask[1].equals("224")||mask[1].equals("192")||
                    mask[1].equals("128")||mask[1].equals("0"))
                if(mask[2].equals("0")&&mask[3].equals("0"))
                    return true;
            return false;

        }else if(mask[0].equals("254")||mask[0].equals("252")||mask[0].equals("248")||
                mask[0].equals("240")||mask[0].equals("224")||mask[0].equals("192")||
                mask[0].equals("128")||mask[0].equals("0")){
            if(mask[1].equals("0")&&mask[2].equals("0")&&mask[3].equals("0"))
                return true;
            else
                return false;
        }else
            return false;
    }

    /**
     * 检查ip是否合法方法
     * @param ip
     * @return
     */
    public  static  boolean checkIp(String[] ip){
        if(ip.length==4&& !ip[0].equals("")&& !ip[1].equals("")&& !ip[2].equals("")&& !ip[3].equals(""))
            return true;
        else
            return false;
    }

}
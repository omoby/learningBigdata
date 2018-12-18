package NowCoder;

import java.util.Scanner;

/**
 * FileName: IPMask
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-21 下午2:41
 * Description:题目描述
 * 请解析IP地址和对应的掩码，进行分类识别。要求按照A/B/C/D/E类地址归类，不合法的地址和掩码单独归类。
 *
 *
 * 所有的IP地址划分为 A,B,C,D,E五类
 *
 *
 * A类地址1.0.0.0~126.255.255.255;
 *
 *
 * B类地址128.0.0.0~191.255.255.255;
 *
 *
 * C类地址192.0.0.0~223.255.255.255;
 *
 *
 * D类地址224.0.0.0~239.255.255.255；
 *
 *
 * E类地址240.0.0.0~255.255.255.255
 *
 *
 *
 *
 * 私网IP范围是：
 *
 *
 * 10.0.0.0～10.255.255.255
 *
 *
 * 172.16.0.0～172.31.255.255
 *
 *
 * 192.168.0.0～192.168.255.255
 *
 *
 *
 *
 * 子网掩码为前面是连续的1，然后全是0。（例如：255.255.255.32就是一个非法的掩码）
 * 本题暂时默认以0开头的IP地址是合法的，比如0.1.1.2，是合法地址
 *
 * 输入描述:
 * 多行字符串。每行一个IP地址和掩码，用~隔开。
 *
 * 输出描述:
 * 统计A、B、C、D、E、错误IP地址或错误掩码、私有IP的个数，之间以空格隔开。
 */
public class IPMask {
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
                    if( first>=1 && first <=126){
                        a++;
                        if(first == 10)
                            pip++;
                    }else if(first >= 128 && first <=191){
                        b++;
                        if(first == 172 && (second >=16 && second <=31))
                            pip++;
                    }else if(first >= 192 && first <= 223){
                        c++;
                        if(first == 192 && second >=168 )
                            pip++;
                    }else if(first >= 224 && first <= 239){
                        d++;
                    }else if(first >= 240 && first <= 255){
                        e++;
                    }else
                        err++;
                }else
                    err++;
            }else
                err++;
        }
        System.out.println(a +" "+ b +" "+ c+ " "+d+" "+e+" "+err+" "+pip);
    }

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
    public  static  boolean checkIp(String[] ip){
        if(ip.length==4&& !ip[0].equals("")&& !ip[1].equals("")&& !ip[2].equals("")&& !ip[3].equals(""))
            return true;
        else
            return false;
    }

}
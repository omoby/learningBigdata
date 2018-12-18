package NowCoder;

import java.util.Scanner;

/**
 * FileName: SodaBottle
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午6:08
 * Description:题目描述
 * 有这样一道智力题：“某商店规定：三个空汽水瓶可以换一瓶汽水。小张手上有十个空汽水瓶，她最多可以换多少瓶汽水喝？”答案是5瓶，方法如下：先用9个空瓶子换3瓶汽水，喝掉3瓶满的，喝完以后4个空瓶子，用3个再换一瓶，喝掉这瓶满的，这时候剩2个空瓶子。然后你让老板先借给你一瓶汽水，喝掉这瓶满的，喝完以后用3个空瓶子换一瓶满的还给老板。如果小张手上有n个空汽水瓶，最多可以换多少瓶汽水喝？
 */
public class SodaBottle {
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        while(input.hasNext()){
            int bo = input.nextInt();
            int total =0;
            int re = 0;
            int me = 0;
            if (bo>0){
                while(bo>1){
                    me = bo / 3;
                    re = bo % 3;
                    total += me;
                    if (me+re <=1)
                        bo = 0;
                    else if(me + re == 2){
                        total+=1;
                        bo = 0;
                    }else
                        bo = me +re;
                }
                System.out.println(total);
            }

        }

    }
}

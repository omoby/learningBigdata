package NowCoder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * FileName: SimpleErrorRecord
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午6:03
 * Description:题目描述
 * 开发一个简单错误记录功能小模块，能够记录出错的代码所在的文件名称和行号。
 *
 *
 *
 * 处理：
 *
 *
 *
 * 1、 记录最多8条错误记录，循环记录，对相同的错误记录（净文件名称和行号完全匹配）只记录一条，错误计数增加；
 *
 *
 *
 * 2、 超过16个字符的文件名称，只记录文件的最后有效16个字符；
 *
 *
 *
 * 3、 输入的文件可能带路径，记录文件名称不能带路径
 */
public class SimpleErrorRecord {
    public  static  void main(String[] args){
        Scanner input = new Scanner(System.in);
        Map<String,Integer> map = new LinkedHashMap<String,Integer>();
        while(input.hasNext()){
            String path = input.next();
            long line = input.nextLong();
            String[] splited = path.split("\\\\");
            String last = splited[splited.length-1];
            if(last.length() >16)
                last = last.substring(last.length()-16);
            String key = last+" "+line;
            if (map.containsKey(key))
                map.put(key,map.get(key)+1);
            else
                map.put(key,1);
        }
        int count = 0;
        for(String key:map.keySet()){
            count++;
            if (count>map.size()-8)
                System.out.println(key+ " " + map.get(key));
        }
    }
}

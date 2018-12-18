package NowCoder;

import java.util.Scanner;

/**
 * FileName: CombinedRecordTable
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-26 下午5:45
 * Description:题目描述
 * 数据表记录包含表索引和数值，请对表索引相同的记录进行合并，即将相同索引的数值进行求和运算，输出按照key值升序进行输出。
 */
public class CombinedRecordTable {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int count = sc.nextInt();
            int[] arr = new int[count];
            for (int i = 0; i < count; i++) {
                int key = sc.nextInt();
                int value = sc.nextInt();
                if (arr[key] == 0)
                    arr[key] = value;
                else {
                    arr[key] += value;
                }
            }
            for (int i = 0; i < count; i++)
                if (arr[i] != 0)
                    System.out.println(i + " " + arr[i]);
        }
    }
}

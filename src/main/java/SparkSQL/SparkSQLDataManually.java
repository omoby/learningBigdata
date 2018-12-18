package SparkSQL;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static org.datanucleus.NucleusContext.random;

/**
 * FileName: SparkSQLDataManually
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-11-11 下午8:56
 * Description:
 * 论坛数据自动生成代码，数据格式如下：
 * date:日期，格式yyyy-MM-dd
 * timestamp:时间戳
 * userID：用户ID
 * pageID：页面ID
 * chanelID：具体板块ID
 * ation：点击和注册
 */
public class SparkSQLDataManually {
    //具体的论坛频道
    static   String[] channelNames = new String[]{
            "Spark","Scala","Kafka","Flink","hadoop","storm","Hive","ML","Impala","HBase"
    };
    /**
     * 通过传进来的参数指定数据规模的大小
     */
   static StringBuffer userLogBuffer = new StringBuffer();
    static  String[] actionNames = new String[]{"View","Registe"};
    static String yesterdayFormated;

    public static void main(String[] args){

        long numberItems = 500000;
        String path = "/home/hadoop/learnSpark/SparkSQLDataManually";
        if (args.length >0){
            numberItems = Integer.valueOf(args[0]);
        }
        System.out.println("User log number is : "+numberItems);

        //昨天的时间生成
        yesterdayFormated = yesterday();
        userLogs(numberItems,path);


    }

    private static void userLogs(long numberItems, String path) {
        Random random = new Random();
        StringBuffer userLogBuffer = new StringBuffer("");
        int[] unregisteredUsers = new int[]{1,2,3,4,5,6,7,8};
        for (int i = 0; i < numberItems; i++){
            long timestamp = new Date().getTime();
            Long userID = 0L;
            long pageID = 0L;
            //随机生成的用户ID

            if (unregisteredUsers[random.nextInt(8)]==1){
                userID = null;
            }else {
                userID = (long)random.nextInt((int) numberItems);
            }
            //随机生成的页面的id
            pageID = random.nextInt((int)numberItems);
            //随机生成的ChannelID
            String channel = channelNames[random.nextInt(10)];
            //随机生成action行为
            String action = actionNames[random.nextInt(2)];
            userLogBuffer.append(yesterdayFormated)
                    .append("\t")
                    .append(timestamp)
                    .append("\t")
                    .append(userID)
                    .append("\t")
                    .append(pageID)
                    .append("\t")
                    .append(channel)
                    .append("\t")
                    .append(action)
                    .append("\n");

        }
        System.out.println(userLogBuffer.toString());
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path+"/userLogs.log")));
            printWriter.write(userLogBuffer.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            printWriter.close();
        }
    }

    private static String yesterday(){
        SimpleDateFormat date =  new SimpleDateFormat("yyyy-MM-dd");

        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date());
        calender.add(Calendar.DATE,-1);

        Date yesterday = calender.getTime();
        return date.format(yesterday);


    }
}

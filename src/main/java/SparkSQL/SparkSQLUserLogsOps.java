package SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.hive.HiveContext;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import  java.lang.Object;

/*
 * FileName: SparkSQLUserLogsOps
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-11-12 下午10:19
 * Description:
 * Table in hive database creation
 *  * sqlContext.sql("create table userlogs(date string, timestamp bigint, userID bigint, pageID bigint, channel string, action string) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\t' LINES TERMINATED BY '\n'")
 */

public class SparkSQLUserLogsOps {
    public static void main(String[] args){
        //创建SparkConf用于读取系统信息并设置运用程序的名称
        SparkConf conf = new SparkConf().setAppName("SparkSQLUserLogsOps").setMaster("spark://Master:7077");
        //创建JavaSparkContext对象实例作为整个Driver的核心基石
        JavaSparkContext sc = new JavaSparkContext(conf);
        //设置输出log的等级
        sc.setLogLevel("INFO");
        //创建SQLContext上下文对象，用于SqL的分析
        HiveContext hiveContext = new HiveContext(sc.sc());
        String yesterday = getTwodaysago();
        pvStatistic(hiveContext,yesterday); //pv
        hotChannel(hiveContext,yesterday);//热门板块
        uvStatistic(hiveContext,yesterday); //uv
        jumpOutStatistic(hiveContext,yesterday);//跳出页面
        newUserRegisterPercentStatistic(hiveContext, yesterday); //新用户注册的比例
    }

    private static void newUserRegisterPercentStatistic(HiveContext hiveContext, String yesterday) {
        hiveContext.sql("use hive");

        String newUserSQL = "select count(*) "
                + "from userlogs "
                + "where action = 'View' and date='"+ yesterday+"' and userID is NULL "
                ;
        String RegisterUserSQL = "SELECT count(*) "
                + "from userlogs"
                + "where action = 'Register' and date='"+ yesterday+"' "
                ;

        Object newUser =  hiveContext.sql(newUserSQL).collect();
        Object RegisterUser =  hiveContext.sql(RegisterUserSQL).collect();

        double total = Double.valueOf(newUser.toString());
        double register = Double.valueOf(RegisterUser.toString());

        System.out.println("模拟新用户注册比例：" + register / total);
    }

    private static void jumpOutStatistic(HiveContext hiveContext, String yesterday) {
        hiveContext.sql("use hive");

        String totalPvSQL = "select count(*) "
                + "from "
                + "userlogs "
                + "where action = 'View' and date='"+ yesterday+"' "
                ;

        String pv2OneSQL = "SELECT count(*) "
                + "from "
                + "(SELECT count(*) totalNumber from userlogs "
                + "where action = 'View' and date='"+ yesterday+"' "
                + "group by userID "
                + "having totalNumber = 1) subquery "
                ;

        Object totalPv = hiveContext.sql(totalPvSQL).collect();
        Object pv2One = hiveContext.sql(pv2OneSQL).collect();

        double total = Double.valueOf(totalPv.toString());
        double pv21 = Double.valueOf(pv2One.toString());
        System.out.println("跳出率为" + pv21 / total);
    }


    private static void uvStatistic(HiveContext hiveContext, String yesterday){

        hiveContext.sql("use hive");

        String sqlText = "select date, pageID, uv "
                + "from "
                + "(select date, pageID, count(distinct(userID)) uv from userlogs "
                + "where action = 'View' and date='"+ yesterday+"' "
                + "group by date, pageID) subquery "
                + "order by uv desc "
                ;
        hiveContext.sql(sqlText).show();
    }

    private static void hotChannel(HiveContext hiveContext, String yesterday) {
        hiveContext.sql("use hive");
        String sqlText = "select date, pageID, pv "
                + "from "
                + "(select date, pageID, count(1) pv from userlogs "
                + "where action = 'View' and date='"+ yesterday+"' "
                + "group by date, pageID) subquery "
                + "order by pv desc "
                ;
        hiveContext.sql(sqlText).show();

    }

    private static void pvStatistic(HiveContext hiveContext, String twodaysago) {
        hiveContext.sql("use hive");
        String  sqlText = "select date,pageID,pv "
                +" from (select date,pageID,count(*) pv from userlogs  " +
                "where action = 'View' and date = '"+twodaysago+"' group by date,pageID ) subqurey order by pv desc limit 10";
        hiveContext.sql(sqlText).show();
    }


    private static String getTwodaysago() {
        SimpleDateFormat date =  new SimpleDateFormat("yyyy-MM-dd");

        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date());
        calender.add(Calendar.DATE,-2);

        Date yesterday = calender.getTime();
        return date.format(yesterday);
    }
}

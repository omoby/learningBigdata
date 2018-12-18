package SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: TheTopBySparkSQL
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-11-19 上午8:38
 * Description:
 * 项目：以京东找出搜索平台排名的产品，The hottest
 * 元数据：date，userID,item,city,device
 * 总体思路：混合使用Spark SQL和Spark Core的内容
 * 第一步：原始的ETL，过滤数据后产生目标数据，实际企业中可能过滤条件非常复杂（进行广播），使用RDD的filter等进行操作；
 * 第二步：过滤后的目标数据进行指定条件的查询，查询条件也可能非常复杂（进行广播），使用RDD的filter算子；
 * 第三步：由于商品是分为种类的，我们在得出最终的结果之前，首先会基于商品进行UV（当然也可以对用户的商品的访问PV），此时要对商品镜像UV=计算的话，必须构建K-V的 RDD，例如构建过程为为(dateItem,UserID)以方便进行groupByKey，在调用了的groupByKey之后对user进行去重，并计算出每一天每一种商品的UV，最终计算出来的的结果的数据类型（）；
 * 第四步：使用开窗函数row_number统计出每日商品UV前五名的内容，row_number（）OVER (PARTITION BY  date ORDER BY UV DESC) rank,此时会产生以date为日期、item、uv为Row的dataFrame
 * 第五步：DataFrame转换成RDD，根据日期进行分组并分析出弥天排名为前5的热搜item；
 * 第六步：进行Key-Values交换，然后调用sortByKey进行点击热度排名；
 * 第七步：再次进行Key-Value交换，得出目标数据为（data#item，UV）的格式；
 * 第八步：通过RDD直接操作MYSQL等把结果放入生成系统中的DB中，通过Java EE等Server技术进行可视化以提供市场营销人员、仓库调度系统、快递系统、仓库决策人员吃用数据创造价值；
 * 当然也可以放在Hive中，Java EE等技术通过JDBC等链接访问Hive；
 * 当然也可以放在Spark SQL中，通过Thrift技术通过Java EE使用等；
 * 当然，如果像双十一等时候，一般首选放在Redis中，这样可以实现类似秒杀系统的响应速度
 */
public class TheTopBySparkSQL {
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("TheTopBySparkSQL");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = new HiveContext(sc);
        String path = "";
        JavaRDD<String> lines0 = sc.textFile("/home/hadoop/IdeaScala/SparkSQLUserlogsHottest.test.log");
        /*元数据：Date、UserID、Item、City、Device；
                  (date#Item#userID)
        */

        //定义广播变量
        String devicebd ="iphone";
        final Broadcast<String> broadcastdevice =sc.broadcast(devicebd);
        // 过滤
        // lines.filter();
        JavaRDD<String> lines =lines0.filter(new Function<String, Boolean>() {
            @Override
            public Boolean call(String s) throws Exception {
                return s.contains(broadcastdevice.value());
            }
        });

       // 验证
        List<String> listRow000 = lines.collect();
            for(String row : listRow000){
                System.out.println(row);
            }

        //组拼字符串(date#Item#userID)  构建KV(date#Item#userID，1)

        JavaPairRDD<String, Integer> pairs = lines.mapToPair(new PairFunction<String, String, Integer>() {
            private static final long serialVersionUID =1L ;
            @Override
            public Tuple2<String, Integer> call(String line) throws Exception {
                        String[] splitedLine =line.split("\t");
                        int one = 1;
                        String dataanditemanduserid = splitedLine[0] +"#"+ splitedLine[2]+"#"+String.valueOf(splitedLine[1]);
                        return new Tuple2<String,Integer>(String.valueOf(dataanditemanduserid),Integer.valueOf(one));
                    }
                });


        //  验证
        List<Tuple2<String,Integer>> listRow = pairs.collect();
                for(Tuple2<String,Integer> row : listRow){
                    System.out.println(row._1);
                    System.out.println(row._2);
                }


        //reducebykey，统计计数

        JavaPairRDD<String, Integer> reduceedPairs =pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
                @Override
                public Integer call(Integer v1, Integer v2) throws Exception {
                    return v1 + v2 ;
                }
            });


        List<Tuple2<String,Integer>> reduceedRow = reduceedPairs.collect();
        //动态组拼出JSON
        List<String> peopleInformations = new ArrayList<String>();
        for(Tuple2<String,Integer> row : reduceedRow){
            //拆分三个字段
            String[] rowSplitedLine =row._1.split("#");
            String rowuserID = rowSplitedLine[2];
            String rowitemID = rowSplitedLine[1];
            String rowdateID = rowSplitedLine[0];
            //拼接json  元数据：Date、UserID、Item、City、Device
            String jsonzip= "{\"Date\":\""+ rowdateID +"\", \"UserID\":\""+ rowuserID+"\", \"Username\":\""+ rowuserID+"\", \"Item\":\""+ rowitemID+"\", \"count\":"+ row._2 +" }";
            peopleInformations.add(jsonzip);
        }

        //打印验证peopleInformations
        for(String row : peopleInformations){
            System.out.println(row.toString());
            //System.out.println(row._2);
        }

        //通过内容为JSON的RDD来构造DataFrame
        JavaRDD<String> peopleInformationsRDD = sc.parallelize(peopleInformations);
        Dataset peopleInformationsDF = sqlContext.read().json(peopleInformationsRDD);
        //注册成为临时表
        peopleInformationsDF.registerTempTable("peopleInformations");
        /* 使用子查询的方式完成目标数据的提取，在目标数据内幕使用窗口函数row_number来进行分组排序:
         * PARTITION BY :指定窗口函数分组的Key；
         * ORDER BY：分组后进行排序；
         */
        String sqlText = "SELECT UserID,Item, count "
                + "FROM ("
                + "SELECT "
                + "UserID,Item, count,"
                + "row_number() OVER (PARTITION BY UserID ORDER BY count DESC) rank"
                +" FROM peopleInformations "
                + ") sub_peopleInformations "
                + "WHERE rank <= 3 " ;

        Dataset execellentNameAgeDF = sqlContext.sql(sqlText);
        execellentNameAgeDF.show();
        execellentNameAgeDF.write().format("json").save(path+"Result");
    }
}

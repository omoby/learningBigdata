package SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;



/**
 * FileName: DataFrameOps
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-27 下午3:22
 * Description:使用Java的方式实战Dataframe的操作
 */
public class DataFrameJavaOps {
    public static void main(String[] args){
        //创建SparkConf用于读取系统信息并设置运用程序的名称
        SparkConf conf = new SparkConf().setAppName("DataFrameOps").setMaster("spark://Master:7077");
        //创建JavaSparkContext对象实例作为整个Driver的核心基石
        JavaSparkContext sc = new JavaSparkContext(conf);
        //设置输出log的等级
        sc.setLogLevel("INFO");
        //创建SQLContext上下文对象，用于SqL的分析
        SQLContext sqlContext = new SQLContext(sc);
        //创建dataFrame，可以简单的认为DataFrame是一张表
        Dataset ds = sqlContext.read().json("file:///usr/local/spark/examples/src/main/resources/people.json");
        //select * from table
        ds.show();
        //desc table
        ds.printSchema();
        //select name from table;
        ds.select("name").show();
        //select name,age+10 from table;
        ds.select(ds.col("name"),ds.col("age").plus(10)).show();
        //select * from table where age > 20;
        ds.filter(ds.col("age").gt(20)).show();
        //select count(1) from table group by age;
        ds.groupBy(ds.col("age")).count().show();

        sc.close();
    }
}

package SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;

import java.util.List;

/**
 * FileName: SparkSQLParquetOps
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-31 下午11:10
 * Description:
 */
public class SparkSQLParquetOps {
    public static void main(String[] args){
        //创建SparkConf用于读取系统信息并设置运用程序的名称
        SparkConf conf = new SparkConf().setAppName("DataFrameOps").setMaster("spark://Master:7077");
        //创建JavaSparkContext对象实例作为整个Driver的核心基石
        JavaSparkContext sc = new JavaSparkContext(conf);
        //设置输出log的等级
        sc.setLogLevel("INFO");
        //创建SQLContext上下文对象，用于SqL的分析
        SQLContext sqlContext = new SQLContext(sc);
        Dataset userDS =sqlContext.read().parquet("/usr/local/spark/examples/src/main/resources/users.parquet");
        userDS.registerTempTable("users");
        Dataset result = sqlContext.sql("select name from users");
       /* JavaRDD<String> resultRDD = result.javaRDD().map(new Function<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return "This name is :"+row.getAs("name");
            }
        });*/
        List<Row> listRow = result.javaRDD().collect();
        for (Row row:listRow){
            System.out.println(row);
        }


    }

}

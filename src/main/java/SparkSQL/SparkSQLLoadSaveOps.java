package SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SaveMode;


/**
 * FileName: SparkSQLLoadSaveOps
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-29 下午8:18
 * Description:
 */
public class SparkSQLLoadSaveOps {
    public static void main(String[] args){
        //创建SparkConf用于读取系统信息并设置运用程序的名称
        SparkConf conf  = new SparkConf().setAppName("RDDToDataFrameByReflection").setMaster("local");
        //创建JavaSparkContext对象实例作为整个Driver的核心基石
        JavaSparkContext sc = new JavaSparkContext(conf);
        //设置输出log的等级,可以设置INFO,WARN,ERROR
        sc.setLogLevel("ERROR");
        //创建SQLContext上下文对象，用于SqL的分析
        SQLContext sqlContext = new SQLContext(sc);
        Dataset peopleDS = sqlContext.read().format("json").load("/usr/local/spark/examples/src/main/resources/people.json");

        String savePath = "/home/hadoop/peopleName.json";
        //peopleDS.select("name").write().format("json").save(savePath);
        peopleDS.select("name").write().format("json").mode(SaveMode.Append).save(savePath);

    }
}

package SparkSQL

import org.apache.spark.sql.{Dataset, SQLContext, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * FileName: DataFrameOps
  * Author:   hadoop
  * Email:    3165845957@qq.com
  * Date:     18-10-27 下午4:03
  * Description:使用Scala的方式实战Dataframe的操作
  *
  */
object DataFrameScalaOps {
  def main(args: Array[String]): Unit = {
    //创建SparkConf用于读取系统信息并设置运用程序的名称
    val conf = new SparkConf().setAppName("DataFrameScalaOps").setMaster("spark://Master:7077")
    //创建JavaSparkContext对象实例作为整个Driver的核心基石
    val sc = new SparkContext(conf)
    //方便查看输出的日志信息，也可以设置为WARN、ERROR
    sc.setLogLevel("ERROR")
    //创建SQLContext上下文对象，用于SqL的分析
    val sqlContex = new SQLContext(sc)
    //创建dataFrame，可以简单的认为DataFrame是一张表
    val ds = sqlContex.read.json("file:///usr/local/spark/examples/src/main/resources/people.json")
    //select * from table;
    ds.show
    //desc printShema
    ds.printSchema()
    //select name from table;
    ds.select("name").show()
    //select name,age+10 from table;
    ds.select(ds("name"),ds("age")+10).show()
    //select * from table where age >10;
    ds.filter(ds("age") >10).show()
    //select count(1) from table group by age;
    ds.groupBy("age").count().show()

  }

}

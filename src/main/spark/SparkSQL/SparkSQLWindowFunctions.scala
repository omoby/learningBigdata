package SparkSQL

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext

/**
  * FileName: SparkSQLWindowFunctions
  * Author:   hadoop
  * Email:    3165845957@qq.com
  * Date:     18-11-10 上午8:53
  * Description:
  *
  */
object SparkSQLWindowFunctions {
  def main(args: Array[String]): Unit = {
    //创建SparkConf用于读取系统信息并设置运用程序的名称
    val conf = new SparkConf().setAppName("DataFrameScalaOps").setMaster("local")
    //创建JavaSparkContext对象实例作为整个Driver的核心基石
    val sc = new SparkContext(conf)
    //方便查看输出的日志信息，也可以设置为WARN、ERROR
    sc.setLogLevel("ERROR")
    //创建SQLContext上下文对象，用于SqL的分析
    val hiveContext = new HiveContext(sc)
    //要使用SparkSQL的内置函数，就一定要导入SQL下的隐式转换
    import hiveContext.implicits._
    hiveContext.sql("use hive") //使用名称为Hive的数据库，所有的表都在这个库中
    /**
      * 如果要创建的表存在就删除，然后创建我们要导入的数据
      */
    hiveContext.sql("drop table if exists scores")
    hiveContext.sql("create table if not scores(name string,score int) row format delimited fields terminated by ' ' lines terminated by  '\\n'")
    //把要处理的数据导入到Hive的表中
    hiveContext.sql("load data inpath '/home/hadoop/topNGroup.txt'")
    /**
      * 使用子查询的方式完成目标数据的提取，在幕表数据内幕使用窗口函数row_number进行分组排序，Partition by:指定窗口函授分组的Key
      * order by:分组进行排序
      */
    val result = hiveContext.sql("select name,score from (" +
      "select " +
      "name," +
      "score, " +
      "row_number() over (Partitopn by name order by score desc) rank " +
      "from scores )" +
      "sub_scores " +
      "where rank <=4")
    result.show()//将结果打印到控制台
    hiveContext.sql("drop table if exists sortedscore")
    result.registerTempTable("sortedscore")

  }

}

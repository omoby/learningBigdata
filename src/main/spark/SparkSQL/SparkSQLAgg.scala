package SparkSQL

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{Row, SQLContext, types}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}
import org.apache.spark.sql.functions._


/**
  * 使用Spark SQL中的内置函数对数据进行分析，与普通的Spark SQL API不同的是DataFrame内置函数操作的结果是返回一个Column对象，
  * 而DataFrame天生就是"A distributed collection of data organized into named columns."，这就为数据的复杂分析建立了坚实的基础
  * 并提供了极大的方便性，例如在操作DataFrame的方法中可以随时调用内置函数进行业务需要的处理，这之于我们构建复杂的业务逻辑而言
  * 非常可以极大地减少不必要的时间消耗（基本上就是实际模型的映射），让我们聚焦在数据分析上，这对于提高工程师的生产力而言是非常有价值的。
  * Spark 1.5.x开始提供了大量的内置函数，例如agg：
  * def agg(aggExpr: (String, String), aggExprs: (String, String)*): DataFrame = {
  *   groupBy().agg(aggExpr, aggExprs : _*)
  * }
  * 还有max,mean,min,sum,avg,explode,size,sort_array,day,to_date,abs,acos,asin,atan
  * 总体而言，内置函数包含五大基本类型：
  * 1，聚合函数，例如countDistinct,sumDistinct等
  * 2，集合函数，例如sort_array
  * 3，日期时间函数，例如hour,quarter,next_day
  * 4，数学函数，例如asin,atan,sqrt,tan,round等；
  * 5，开窗函数，例如rowNumber等
  * 6，字符串函数，concat,format_number,rexexp_extract
  * 7，其它函数，isNaN,sha,randn,callUDF,callUDAF
  */

object SparkSQLAgg {
  def main(args: Array[String]): Unit = {
    //创建SparkConf用于读取系统信息并设置运用程序的名称
    val conf = new SparkConf().setAppName("DataFrameScalaOps").setMaster("local")
    //创建JavaSparkContext对象实例作为整个Driver的核心基石
    val sc = new SparkContext(conf)
    //方便查看输出的日志信息，也可以设置为WARN、ERROR
    sc.setLogLevel("ERROR")
    //创建SQLContext上下文对象，用于SqL的分析
    val sqlContex = new SQLContext(sc)
    //要使用SparkSQL的内置函数，就一定要导入SQL下的隐式转换
    import sqlContex.implicits._
    /**
      * 模拟电商访问的数据，实际情况会比模拟数据复杂很多，最后生成RDD
      */
    val userData = Array(
      "2018-11-9,001,http://spark.apache.org/,1100",
      "2018-11-10,004,http://hadoop.apache.org/,11000",
      "2018-11-10,003,http://flink.apache.org/,1500",
      "2018-11-9,002,http://hive.apache.org/,1300",
      "2018-11-9,003,http://parquet.apache.org/,110",
      "2018-11-9,004,http://kafka.apache.org/,10020",
      "2018-11-9,002,http://spark.apache.org/,1002"
    )
    //生成RDD分布式集合对象
    val userDataRDD = sc.parallelize(userData)

    /**
      * 根据业务需要对数据镜像预处理生成DataFrame，要想把RDD转换成DataFrame，需要先把RDD中的元素类型变成Row类型，
      * 与此同时要提供DataFrame中的Columns的元数据信息描述
      */
    val userDataRow = userDataRDD.map(row=>{
      val splited = row.split(",")
      Row(splited(0),splited(1).toInt,splited(2),splited(3).toInt)
    })
    val structTypes = StructType(Array(
        StructField("time",StringType,true),
        StructField("id",IntegerType,true),
        StructField("url",StringType,true),
        StructField("amount",IntegerType,true)
    ))
    val userDataDS = sqlContex.createDataFrame(userDataRow,structTypes)

    /**
      * 使用SparkSQL提供的内置函数对DataFrame进行操作，特别注意：内部函数生成Column对象且自动CG
      */

    userDataDS.groupBy("time")
      .agg('time,countDistinct('id))
      .rdd
      .map(row => Row(row(1),row(2)))
      .collect().foreach(println)

    userDataDS.groupBy("time").agg('time,sum('amount)).rdd
      .map(row=>Row(row(1),row(2)))
      .collect()
      .foreach(println)
    userDataDS.groupBy("time").agg('time,max('amount))
      .rdd
      .map(row=>Row(row(1),row(2)))
      .collect()
      .foreach(println)

  }

}

package SparkSQL

import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.types._

/**
  * FileName: SparkSQLUDFAndUDAF
  * Author:   hadoop
  * Email:    3165845957@qq.com
  * Date:     18-11-10 上午10:46
  * Description:
  *通过案例实战SparkSQL下的UDF和UDAF的具体使用
  * UDF：User Define Function，用户自定义的函数，函数的输入是一个具体的数据记录，实现上讲就是简单的scala代码
  * UDAF：User Define Aggregation Function,用户自定义的聚合函数，函数本身作用于数据集合，能够在聚合操作的基础上自定义操作
  * 实质上讲，例如说UDF会被sparkSQL中的Catalyst分装成为Expression，最终会通过eval方法来计算输入的数据Row（此处的Row和dataframe中的Row没有任何关系）
  */
object SparkSQLUDFAndUDAF {
  def main(args: Array[String]): Unit = {
    //创建SparkConf用于读取系统信息并设置运用程序的名称
    val conf = new SparkConf().setAppName("SparkSQLUDFAndUDAF").setMaster("local")
    //创建JavaSparkContext对象实例作为整个Driver的核心基石
    val sc = new SparkContext(conf)
    //方便查看输出的日志信息，也可以设置为WARN、ERROR
    sc.setLogLevel("ERROR")
    //创建SQLContext上下文对象，用于SqL的分析
    val sqlContext = new SQLContext(sc)
    //模拟数据
    val bigData = Array(
      "Spark","Spark","Hadoop", "Spark","Spark","Hadoop"
    )
    /**
      * 创建提供的数据常见DAtaframe
      */
    val bigDataRDD = sc.parallelize(bigData)
    val bigDataRDDRow = bigDataRDD.map((item=>Row(item)))
    val structType = StructType(Array(
      StructField("word",StringType,true)
    ))
    val bigDataDS = sqlContext.createDataFrame(bigDataRDDRow,structType)
    bigDataDS.registerTempTable("bigDataTable")

    /**
      * 通过SQLContext注册UDF，在scala2.10.x版本UDF函数最多可以接受22个输入参数
      */
    sqlContext.udf.register("computeLength",(input:String)=>input.length)
    //直接在SQL语句中使用UDF，就像使用SQL自带的内部函数一样
    sqlContext.sql("select word,computeLength(word) as length from bigDataTable").show()

    sqlContext.udf.register("wordcount",new MyUDAF)
    sqlContext.sql("select word,wordcount(word) as count,computeLength(word) as length from bigDataTable group by word").show()

    while (true){

    }
  }


}

/**
  * 按照模板实现UDAF
  */

class  MyUDAF extends UserDefinedAggregateFunction{
  /**
    * 该方法指定具体输入数据的类型,在这里指定的数据列名和输入的列名没有关系
    * @return
    */
  override def inputSchema: StructType = StructType(Array(StructField("input",StringType,true)))

  /**
    * 在进行聚合操作的时候所要处理的数据的结果的类型
    * @return
    */
  override def bufferSchema: StructType = StructType(Array(StructField("count",IntegerType,true)))

  /**
    * 指定UDAF函数计算后返回的结果类型
    * @return
    */
  override def dataType: DataType = IntegerType

  /**
    *指定数据一致性
    * @return
    */
  override def deterministic: Boolean = true

  /**
    * 在aggregate之前每组数据的初始化结果
    * @param buffer
    */
  override def initialize (buffer: MutableAggregationBuffer): Unit = (buffer(0) = 0)

  /**
    * 在进行聚合的时候，每当性的值进来，对分组的聚合如何进行计算
    * 本地的聚合操作，相当于Hadoop MapReduce模型这的Combiner
    * @param buffer
    * @param input
    */
  override def update (buffer: MutableAggregationBuffer, input: Row): Unit = {
    buffer(0) = buffer.getAs[Int](0)+1
  }

  /**
    * 在分布式节点进行local Reduce完成后需要进行全局级别的Merge操作
    * @param buffer1
    * @param buffer2
    */
  override def merge (buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    buffer1(0) = buffer1.getAs[Int](0)+buffer2.getAs[Int](0)
  }

  /**
    * 返回UDAF最后的计算结果
    * @param buffer
    * @return
    */

  override def evaluate (buffer: Row): Any = buffer.getAs[Int](0)
}
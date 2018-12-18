package SparkSQL


import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}


/**
  * FileName: SparkSQLToHive
  * Author:   hadoop
  * Email:    3165845957@qq.com
  * Date:     18-11-9 下午12:25
  * Description:
  */
object SparkSQLToHive {
  def main(args: Array[String]): Unit = {
    //创建SparkConf用于读取系统信息并设置运用程序的名称
    val conf = new SparkConf().setMaster("local").setAppName("SparkSQLToHive")
    //创建JavaSparkContext对象实例作为整个Driver的核心基石
    val sc = new SparkContext(conf)
    //设置输出log的等级,可以设置INFO,WARN,ERROR
    sc.setLogLevel("INFO")
    //创建SQLContext上下文对象，用于SqL的分析
  val hiveContext = SparkSession
    .builder()
    .enableHiveSupport()
    .appName("SparkSQLToHive")
    .config("spark.some.config.option", "some-value")
    .getOrCreate()
    /**
      * 在目前企业级大数据Spark开发的时候，绝大多数是采用Hive作为数据仓库的spark提供Hive的支持功能，Spark通过HiveContex可以操作Hive中的数据
      * 基于HiveContext可以使用sql/hsql两种方式编写SQL语句对Hive进行操作，
      * 包括创建表、从删除表、往表里导入数据构造各种SQL语法构造各种sql语句对表中的数据进行操作；
      * 二、也可以直接通过saveAsTable的方式把DataFrame中的数据保存到Hive数据仓库中
      * 三、可以直接通过HiveContext.table方法直接加载Hive中的表来生成dataframe
      */
    hiveContext.sql("use hive") //使用Hive数据仓库中的hive数据库
    hiveContext.sql("drop table if exists people") //删除同名的table
    hiveContext.sql("create table if not exists people (name String,age Int)") //创建自定义的table
    hiveContext.sql("load data local inpath '/home/hadoop/people/people.txt' into table people") //导入本地数据到Hive数据仓库中，背后实际上发生了数据的拷贝，当然也可以通过load data input去获得HDFS等上面的数据到Hive（此时发生数据的移动）
    hiveContext.sql("drop table if exists score")
    hiveContext.sql("create table if not exists score (name String,score Int)")
    hiveContext.sql("load data local inpath '/home/hadoop/people/people.txt' into table score")


    //   通过HiveContext使用join直接基于Hive中的两张表进行操作获得成绩大于90分的人的name,age,score
    val resultDS = hiveContext.sql("select people.name,people.age,score.score from people join score on people.name = score.name")

    /**
      * 通过registerTempTable创建一张Hive Manager Table ，数据的元数据和数据即将具体的数据位置都是由Hive数据仓库进行管理，当删除的时候，数据也会一起被删除（磁盘上的数据也被删除）
      */
    hiveContext.sql("drop table if exists peopleinfor")
    resultDS.registerTempTable("peopleinfor")


      //* 使用HiveContext的table方法可以直接去读取Hive数据仓库中的table并生成DataFrame，接下来可以进行机器学习、图计算、各种复杂etl等操作
    val dataFromHive = hiveContext.table("peopleinfor")
    dataFromHive.show()
  }

}

/*
package SparkSQL

import org.apache.spark.sql.hive.HiveContext
import org.apache.spark.{SparkConf, SparkContext}


object SparkSQL2Hive {
  def main(args:Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("SparkSQL2Hive") //设置应用程序的名称，在程序运行的监控界面可以看到名称
    conf.setMaster("local")
    val sc = new SparkContext(conf)

    val hiveContext = new HiveContext(sc)
    hiveContext.sql("use hive")  //前面创建好了这个数据库
    //第一张学生姓名年龄表
    hiveContext.sql("DROP TABLE IF EXISTS people") //删除同名的Table
    hiveContext.sql("CREATE TABLE IF NOT EXISTS people(name STRING,age INT) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\t' LINES TERMINATED BY '\\n'") //创建自定义的表
    //把本地数据加载到Hive数据仓库中，发生了数据的拷贝。也可以通过LOAD DATA INPATH去获得HDFS等上面的数据到Hive，发生了数据的移动。
    hiveContext.sql("LOAD DATA LOCAL INPATH '/home/hadoop/people/people.txt' INTO TABLE people") //导入数据

    //第二张学生姓名成绩表
    hiveContext.sql("DROP TABLE IF EXISTS score")
    hiveContext.sql("CREATE TABLE IF NOT EXISTS score(name STRING,score INT) ROW FORMAT DELIMITED FIELDS TERMINATED BY '\\t' LINES TERMINATED BY '\\n'")
    hiveContext.sql("LOAD DATA LOCAL INPATH '/home/hadoop/people/score.txt' INTO TABLE score")


    val resultDF = hiveContext.sql("SELECT people.name,people.age,score.score FROM people  JOIN score  ON people.name=score.name where score.score > 90")


    hiveContext.sql("DROP TABLE IF EXISTS peoplescoreinformation") //删除同名的Table
    resultDF.registerTempTable("peopleinformation")

    val dataFromHive = hiveContext.table("peopleinformation")
    dataFromHive.show()
  }
}
*/

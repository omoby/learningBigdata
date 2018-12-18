package DT

import org.apache.spark.{SparkConf, SparkContext}

/**
  * 使用scala开发本地测试的spark WordCount程序
  *
  * @author ombey
  * @email 3165845957@qq.com
  */
object WordCountCluster {
  def main(args:Array[String]): Unit ={
    /**
      * 第一步：创建Spark配置对象SparkConf，设置spark程序的运行时信息
      * 例如通过setMaster来设置要连接的spark集群的Master的URl，
      * 如果设置为local，则代表spakr程序在本地运行，适合机器配置差
      */
    val conf = new SparkConf() //创建SparkConf对象
    conf.setAppName("Word Count") //设置运用程序的名称，在程序运行的监控界面可以看到名称
    //conf.setMaster("local[4]") //此时在本地运行，不需要安装spark集群,如果这里不写，
                                // spark-submit命令上可以指定，增加程序灵活性
    //conf.setMaster("spark://Master:7077")//此时在spark集群运行

    /**
      * 第二步：创建SparkContext对象，
      * SparkContext是spark程序的所有功能的唯一入口，无论是是采用scala、Java，Python、R编程，
      * 都必须有一个SparkCont
      * SparkContext的核心作用：初始化spark应用程序所需要的核心组件，包括DAGScheduler
      * TaskScheduler，SchedulerBackend；同时还会负责spark往Master注册程序等
      * SparkContext是spark应用程序中最为重要的一个对象
      */

    val sc = new SparkContext(conf) //创建SparkContext对象，通过闯入SparkConf实例来定制Spark运行程序

    /**
      * 第三步：根据具体的数据来源（HDFS、HBase、Local FS、Db，S3等） 通过SparkContext来创建RDD
      * RDD创建基本有三种方式：根据外部的数据来源（例如HDFS），根据Spark集群，由其他的RDD操作
      * RDD会被划分为一系列的Partitions，分配到每一个Partition的数据属于一个Task处理范畴
      *
      */
    val lines = sc.textFile("hdfs://localhost:9000/spark/input") //path:数据路径(此时是HDFS文件) 集群会自动配置
    //val lines:RDD[String]= sc.textFile("hdfs://localhost:9000/spark/input",2) //path:数据路径 minPartition：文件分片（并行度）

    /**
      * 第四步:对初始的RDD进行Transform级别的处理，例如map、filter的高级别函数等的编程，来进行具体的数据计算，
      * 4.1步：将每一行的字符串拆分成单个的单词
      */
    val words = lines.flatMap(line=>line.split(" ")) //对每一行的字符串进行单词拆分，并且把所有行的拆分结果通过flatMap合并一个大集合

    /**
      * 第四步:对初始的RDD进行Transform级别的处理，例如map、filter的高级别函数等的编程，来进行具体的数据计算，
      * 4.2步：将每一行的字符串拆分成单个的单词的基础上对每一个单词计数为1，也就是word=>(word,1)
      */
    val pairs = words.map(word=>(word,1))

    /**
      * 第四步:对初始的RDD进行Transform级别的处理，例如map、filter的高级别函数等的编程，来进行具体的数据计算，
      * 4.3步：在每个单词实例计数为1的基础智商统计每个单词在文本中的出现的总次数
      */
    val wordCounts = pairs.reduceByKey(_+_) //对相同的key进行value的累加（包括local和Reducer级别同时Reduce）
    wordCounts.sortByKey().collect().foreach(wordNumberPair=>println(wordNumberPair._1 + " : "+wordNumberPair._2)) //tuple index从1开始
    sc.stop() //关闭SparkContext
  }

}

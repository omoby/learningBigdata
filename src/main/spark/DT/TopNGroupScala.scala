package DT

import org.apache.spark.{SparkConf, SparkContext}

/**
  * FileName: TopNGroup
  * Author:   hadoop
  * Email:    3165845957@qq.com
  * Date:     18-9-8 下午10:05
  * Description:使用Scala的方式开发分组topN程序
  *
  */
object TopNGroupScala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("TopNGroup")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
    val lines = sc.textFile("file:///home/hadoop/topngroup.txt")
    val pairs = lines.map(line=> {
      val splited = line.split(" ")
      (splited(0),splited(1).toInt)
    })
    val groupedPairs = pairs.groupByKey()
    val sortedPairs = groupedPairs.sortByKey(false).map(pair=>{
      (pair._1,pair._2.toList.sortWith(_>_).take(5))
    })
    sortedPairs.foreach(pair=>println(pair._1+" : "+pair._2))
    sc.stop()
  }

}

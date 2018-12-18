package DT

import org.apache.spark.{SparkConf, SparkContext}

/**
  * FileName: SecondarySortApp
  * Author:   hadoop
  * Email:    3165845957@qq.com
  * Date:     18-9-7 下午2:53
  * Description:
  * 二次排序具体实现步骤
  * 1.按照Ordered和Serializable借口实现自定义排序的Key
  * 2.要将进行二次排序的文件加载进来生成<Key,Value>类型的RDD
  * 3.使用sortByKey基于自定义的key进行二次排序
  * 4.去除掉排序的key，只保留排序的结果
  *
  */
object SecondarySortApp {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("SecondarySortApp")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
    val file = sc.textFile("file:///home/hadoop/spark.txt")
    val pairWithSortKey = file.map(line=> (
      new SecondarySortKey(line.split(" ")(0).toInt, line.split(" ")(1).toInt),line
      ))
   /* val pairWithSortKey = file.map(line=>(
      SecondarySortKey.apply(line.split(" ")(0).toInt, line.split(" ")(1).toInt),line
    ))*/
    val sorted = pairWithSortKey.sortByKey(false)
    val sortedResult = sorted.map(sortedLine=>sortedLine._2)
    sortedResult.collect().foreach(println)
  }


}

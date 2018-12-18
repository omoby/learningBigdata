package DT

import org.apache.spark.{SparkConf, SparkContext}

/**
  * FileName: TopNBasic
  * Author:   hadoop
  * Email:    3165845957@qq.com
  * Date:     18-9-8 下午7:10
  * Description:
  *
  */
object TopNBasic {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("TopNBasic")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
    val data = sc.textFile("file:///home/hadoop/basictopn.txt")
    val pairs = data.map(line=>(line.toInt,line)) //生成key value键值对，以方便排序
    val sortedPair = pairs.sortByKey(false)//降序排序
    val sortedData = sortedPair.map(pair=>pair._2)//过滤出排序后的内容本身
    val top5 = sortedData.take(5)//获取排名前五位的元素内容
    for (elem <- top5) {
      println(elem)
    }
    sc.stop()
  }

}

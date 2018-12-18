package DT

import org.apache.spark.{SparkConf, SparkContext}

object SparkCluster {
  def main(args: Array[String]): Unit = {
    val filePath = "hdfs://localhost:9000/spark/input"
    val  conf = new SparkConf()
    conf.setAppName("Word Count")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
    val lines = sc.textFile(filePath)
    val words = lines.flatMap(line=>line.split(" "))
    val wordPairs = words.map(word=>(word,1))
    val reduced = wordPairs.reduceByKey(_+_)
    reduced.foreach(pair=>println(pair._1+" : "+pair._2))
  }
}

package DT

import org.apache.spark.{SparkConf, SparkContext}

object RDDBasedOnLocalFile {
  def main(args:Array[String]): Unit ={
    val conf = new SparkConf()
    conf.setAppName("RDDBasedOnCollection")
    conf.setMaster("local[4]")
    val  sc = new SparkContext(conf)
    val rdd = sc.textFile("file:///home/hadoop/data.txt")
    val linesLength = rdd.map(line=>line.length)
    val sum = linesLength.reduce(_+_)
    println("The Totol characters of the file is: " + sum)
    sc.stop()
  }

}

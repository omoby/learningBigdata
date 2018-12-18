package dblab

import org.apache.spark.{SparkConf, SparkContext}

object SimpleApp {
  def main(args: Array[String]): Unit = {
    val logFile = "file:///usr/local/spark/README.md"
    val conf = new SparkConf().setMaster("local[4]").setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val file = sc.textFile(logFile,2)
    val numAs = file.filter(line=>line.contains("a")).count()
    val numBs = file.filter(line=>line.contains("b")).count()
    println("Lines with a: %s ,Lines with b: %s".format(numAs,numBs))
  }

}

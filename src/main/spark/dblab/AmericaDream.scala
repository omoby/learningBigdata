package dblab

import org.apache.spark.{SparkConf, SparkContext}

import scala.tools.scalap.Properties

object AmericaDream {
  def main(args: Array[String]): Unit = {
    val master = Properties.envOrElse("MASTER","local[4]")
    val conf = new SparkConf().setMaster("local[4]").setAppName("America Dream")
    val sc = new SparkContext(conf)
    val file = sc.textFile("hdfs://localhost:9000/spark/input/")
    val words = file.flatMap(line=>line.split(" "))
    val wordNumber = words.count()
    val wordCount = words.map(word=>(word,1)).reduceByKey((_+_))
    wordCount.foreach(println)
    var totalNumber:Int = 0
    var linenum:Int= 0
    file.foreach(line=>{
      val tname = Thread.currentThread().getName
      val lineWordsNumber = line.split(" ").length
      linenum =linenum +1
      totalNumber += lineWordsNumber
      println("CurrentThread: " +tname)
      println("lineNumber: " + linenum)
      println("lineWords: " + lineWordsNumber)
      println("lineData: " + line)
      println("TotalNumber: "+ totalNumber)

      println()

    })
    println("lineNumber: " + linenum)
    println("TotalNumber: "+wordNumber)
  }

}

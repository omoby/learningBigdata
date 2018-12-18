package DT

import org.apache.spark.{SparkConf, SparkContext}

object TextLines {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("TextLines")
    conf.setMaster("local[4]")
    val sc = new SparkContext(conf)
    //通过RDD，以及mappartit获取文件每一行的内容
    val file = sc.textFile("file:///home/hadoop/data.txt")
    val lineCount = file.map(line=>(line,1))//每一行变成（line,1）de Tuple
    val textLengths = lineCount.reduceByKey(_+_)
    //textLengths.collect().foreach(pair=>println(pair._1+" : "+pair._2))
    textLengths.foreach(pair=>println(pair._1+" : "+ pair._2))

  }

}

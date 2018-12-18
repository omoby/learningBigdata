package DT

import org.apache.spark.{SparkConf, SparkContext}

object RDDBasedOnCollection {
  def main(args:Array[String]): Unit ={
    val conf = new SparkConf()
    conf.setAppName("RDDBasedOnCollection")
    conf.setMaster("local[4]")
    val  sc = new SparkContext(conf)
    val numbers = 1 to 100 //创建一个结合
    val rdd = sc.parallelize(numbers)
    val sum = rdd.reduce(_+_) //1+2 = 3 3 + 3 = 6 6 + 4 = 10
    println("1+2+3+...+99+100 = "+sum)
    sc.stop()
  }

}

package dblab

import org.apache.spark.{SparkConf, SparkContext}

import scala.tools.scalap.Properties

object RDDTest {
  def main(args: Array[String]): Unit = {
    val master = Properties.envOrElse("MASTER","local[4]")
    val conf = new SparkConf()
    conf.setMaster("local[4]")
    conf.setAppName("Spark Test")
    val sc = new SparkContext(conf)

    val rdd = sc.parallelize(List(1,2,3,4,5))
    val e = rdd.first()
    rdd.foreach(l=>{
      val tname = Thread.currentThread().getName
      println(tname +" " + l)

    })

  }

}

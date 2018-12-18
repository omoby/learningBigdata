package dblab

import java.text.SimpleDateFormat
import java.util.{Date, Locale}

import org.apache.spark.{SparkConf, SparkContext}

/**
  * FileName: Logs
  * Author:   hadoop
  * Email:    3165845957@qq.com
  * Date:     18-9-13 上午10:23
  * Description:
  *
  */
object Logs {
  def main(args: Array[String]): Unit = {
    if (args.length != 2) {
     /* println("arsg: inputPath outputPath")
      return
    } else {*/
      val conf = new SparkConf()
      conf.setAppName("Web Logs")
      conf.setMaster("local[1]")
      val sc = new SparkContext(conf)
      //val lines = sc.textFile("file:////home/hadoop/IdeaScala/TechBbs/example_data.log")
      val lines = sc.textFile("hdfs://Master:9000/techbbs/input")
      val containsStatic = "GET /static/"
      val containsUc_server = "GET /uc_server/"
      val filtered = lines.filter(!_.contains(containsStatic)).filter(!_.contains(containsUc_server)).filter(!_.contains("30s"))
      val arrayRDD = filtered.map(line => {
        if(line.contains("GET")){
          val splited = line.split("\\|")
          val ip = splited(0)
          val timeStamp = DateFormat(splited(1).split("\\[")(1).split(" ")(0))
          val URLstr = splited(2).split("/", 2)(1).split(" ")(0)
          val status = splited(3)
          val traffic = splited(4)
          /*if(URLstr.contains("/plugin.php?"))
            printErr(line)*/
          ip + "|" + timeStamp + "|" + URLstr + "|" + status + "|" + traffic
        }else if (line.contains("POST")){
          val splited = line.split("\\|")
          val ip = splited(0)
          val timeStamp = DateFormat(splited(1).split("\\[")(1).split(" ")(0))
          val URLstr = splited(1).split(" ")(3).split("/", 2)(1).split(" ")(0)
          val status = splited(2)
          val traffic = splited(3)
          /*if(URLstr.contains("/plugin.php?"))
            printErr(line)*/
          ip + "|" + timeStamp + "|" + URLstr + "|" + status + "|" + traffic

        }else if(line.contains("close")){
          //printErr(line)
          val splited = line.split("\\|")
          val ip = splited(0)
          val timeStamp = DateFormat(splited(1).split("\\[")(1).split(" ")(0))
          val URLstr = splited(1).split(" ",3)(2).split("\"", 3)(1).split(" ")(0)
          val status = splited(2)
          val traffic = splited(3)
          ip + "|" + timeStamp + "|" + URLstr + "|" + status + "|" + traffic
        }
      })
      arrayRDD.collect
      //("file:///home/hadoop/IdeaScala/TechBbs/Err")
      //arrayRDD.saveAsTextFile("hdfs://Master:9000/techbbs/output")
     // arrayRDD.saveAsTextFile(args(1))
    }

    /**
      * 输入时间格式：30/May/2017:17:38:20
      * 返回时间格式：20170530173820
      *
      * @param time
      */
    def DateFormat(time: String): String = {
      val fromFormat = new SimpleDateFormat("d/MMM/yyyy:HH:mm:ss", Locale.ENGLISH)
      val toFormat = new SimpleDateFormat("yyyyMMddHHmmss")
      val fromtime = fromFormat.parse(time)
      val resultTime = toFormat.format(fromtime)
      resultTime
    }

    def printErr(line:String): Unit ={
      println("--------------------------------------------")
      println(line)
      println("--------------------------------------------")

    }
  }

}

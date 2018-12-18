package SparkSQL

import java.util

import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

object SparkSQLUserLogsHosttest {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("SparkSQLUserLogsHosttest").setMaster("local[4]")
    val sc = new SparkContext(conf)
   // val sqlContext = new HiveContext(sc)
    val spark = SparkSession.builder().appName("SparkSQLUserLogsHosttest").master("local[4]").config("spark.sql.warehouse.dir", "/home/hadoop/IdeaScala/LearningBigdata/spark-warehouse").getOrCreate()

    var path = "/home/hadoop/IdeaScala/SparkSQLUserlogsHottest.log"
    val file = sc.textFile(path)

    val devicebd = "iphone"
    val broadcastdevice = sc.broadcast(devicebd)
    val lines = file.filter(line=>{
      line.contains(broadcastdevice.value)
    })
    val listRow = lines.collect()
    for(row <- listRow){
      println(row)
    }

    val pairs = lines.map(line=>{
      val splited = line.split("\t")
      val one = 1
      val dataanditemanduserid = splited(0)+"#"+splited(2)+"#"+splited(1)
      (dataanditemanduserid,one)
    })

    val pairRow = pairs.collect()
    for(pair <- pairRow){
      println(pair)
    }

    val reduceedPairs = pairs.reduceByKey(_+_)
    val reduceedRow = reduceedPairs.collect()
    val peopleInformation:util.ArrayList[String] = new util.ArrayList[String]()
    for(eachRow <- reduceedRow) {
      println(eachRow)
      val rowSplited = eachRow._1.split("#")
      val userID = rowSplited(2)
      val itemID = rowSplited(1)
      val dateID = rowSplited(0)
      //val jsonzip = "{ Date :" +dateID+", UserID :"+userID+",Username :"+userID+",Item : "+itemID+",count : "+ eachRow._2+"}"
      val jsonzip = "{\"Date\":\"" + dateID + "\", \"UserID\":\"" + userID + "\", \"Username\":\"" + userID + "\", \"Item\":\"" + itemID + "\", \"count\":" + eachRow._2 + "}"
      peopleInformation.add(jsonzip)
    }

    for (row <- peopleInformation.toArray()){
      println(row)
    }

    val peopleInformationRDD = sc.parallelize(peopleInformation.toArray())
    val peopleInformationDS = spark.read.json(peopleInformationRDD.toString())
    peopleInformationDS.createOrReplaceTempView("peopleInformations")
    val sqlText = "SELECT UserID,Item, count "+
      "FROM ("+
      "SELECT "+
      "UserID,Item, count,"+
      "row_number() OVER (PARTITION BY UserID ORDER BY count DESC) rank"+
      " FROM peopleInformations "+
      ") sub_peopleInformations "+
      "WHERE rank <= 3 "
    val execellentNameAgeDS = spark.sql(sqlText)
    execellentNameAgeDS.show()
    execellentNameAgeDS.write.format("json").save(""+"Result")

  }

}

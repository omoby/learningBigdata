
package SparkSQL

import org.apache.spark.sql.{RowFactory, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}
;

/**
 * FileName: SparkSQLWithJionScala
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-11-8 下午5:06
 * Description:
 */
object SparkSQLWithJionScala {
    def main(args: Array[String]): Unit = {
        //创建SparkConf用于读取系统信息并设置运用程序的名称
        val conf = new SparkConf().setAppName("SparkSQLWithJionScala").setMaster("local")
        //创建JavaSparkContext对象实例作为整个Driver的核心基石
        val sc = new SparkContext(conf)
        //方便查看输出的日志信息，也可以设置为WARN、ERROR
        sc.setLogLevel("ERROR")
        //创建SQLContext上下文对象，用于SqL的分析
        val sqlContext = new SQLContext(sc)
        //创建dataFrame，可以简单的认为DataFrame是一张表
        val personScoreDS = sqlContext.read.json("file:///home/hadoop/score.json")
       //将成绩表注册为临时表
        personScoreDS.registerTempTable("personScores")
        //选择出成绩优秀的成绩记录
        val excellentStudentsDS = sqlContext.sql("select  name ,score from personScores where score >= 90")
       //取出成绩优秀的学生姓名
        val  excellentStudentName = excellentStudentsDS.rdd.map(_(0))
       //学生的信息姓名，年龄
        val peopleInformations = Array(
            "{\"name\":\"Michael\", \"age\":20}",
            "{\"name\":\"Andy\", \"age\":17}",
            "{\"name\":\"Justin\", \"age\":19}"
        )
      //将学生的信息姓名，年龄注册为RDD
       val peopleInformationRDD = sc.parallelize(peopleInformations)
      //将学生信息注册为JSON格式
        val  peopleInformationDS = sqlContext.read.json(peopleInformationRDD)
      //将学生的信息注册为临时表
        peopleInformationDS.registerTempTable("peopleInformations")
      /**
        * 查询成绩为优秀的学生的信息
        */
        val sqlText = new StringBuilder()
        sqlText.append("select name,age from peopleInformations where name in (")
        val students:Array[Any] = excellentStudentName.collect()
        for (i <- 0 until students.size){
            sqlText.append("'" + students(i).toString + "'")
            if (i != students.size-1)
                sqlText.append(",")
        }
        sqlText.append(")")
      val sqlString = sqlText.toString()
        val excellentStudentNameAgeDS = sqlContext.sql(sqlString)
      //将学生的成绩表和信息表进行jion操作
        val resultRDD = excellentStudentsDS.rdd.map(row=>(row.getAs("name").toString,row.getLong(1))).join(excellentStudentNameAgeDS.rdd.map(line=>(line.getAs("name").toString,line.getLong(1))))
      /**
        * 将jion后的信息进行整理
        */
        val resultRowRDD = resultRDD.map(tuple=>{
            val name = tuple._1
            val age:java.lang.Integer=tuple._2._2.toInt
            val score:java.lang.Integer= tuple._2._1.toInt
          RowFactory.create(name, age, score)
        })
      //生成dataFrame
      val personDS = sqlContext.createDataFrame(resultRowRDD.map(row => PersonAgeScore(row.getString(0),row.getInt(1),row.getInt(2))))
      personDS.show()
      personDS.write.json("file:///home/hadoop/json")


    }
}
case class PersonAgeScore(name: String, age: Int, score: Int)


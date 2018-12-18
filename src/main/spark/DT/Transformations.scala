package DT

import org.apache.spark.{SparkConf, SparkContext}

/**
  * FileName: Transformations
  * Author:   hadoop
  * Email:    3165845957@qq.com
  * Date:     18-9-6 上午10:26
  * Description:最常用最重要的Transformations案例实战讲解
  *
  */
object Transformations {
  def main(args: Array[String]): Unit = {
    /**
      * 创建sparkConf并且初始化配置
      */
    val sc = sparkContext("Transformations") //创建SpaContex
    //mapTransformation(sc)//map案例
    //filterTransformation(sc) //filter案例
    //flatMapTransformation(sc) //flapMap案例
    groupByKeyTansformation(sc) //groupByKey案例
    //reduceByKeyTansformation(sc) //reduceByKey的案例
    // joinTransformation(sc) //join案例
    //cogroupTransformation(sc) //cogroup案例
    sc.stop() //停止Sparkcontext，销毁Driver对象，释放资源


  }

  def sparkContext(name:String)={
    val conf = new SparkConf()
    conf.setAppName("Transformations")
    conf.setMaster("local[4]")
    val sc = new SparkContext(conf)//创建Sparkcontext，是第一个RDD的唯一入口，也是Driver的灵魂，是通往集群的唯一通道
    sc
  }

  def mapTransformation(sc:SparkContext): Unit ={
    val nums = sc.parallelize(1 to 10) //根据集合创建RDD
    val mapped = nums.map(iter=>2 * iter) //map适用于任何元素，且对其作用的集合中的每一个元素循环遍历并调用其作为参数的函数对每一个元素进行具体化处理
    mapped.collect().foreach(println) //收集计算结果并通过foreach循环打印
  }

  def filterTransformation(sc:SparkContext): Unit ={
    val nums = sc.parallelize(1 to 10) //根据集合创建RDD
    val filtered = nums.filter(iter=>iter%2 == 0) //根据filter中的函数的Boolean来判断符合条件的元素，并基于这些元素构成新的MapPartitionRDD
    filtered.collect().foreach(println)//收集计算结果并通过foreach循环打印
  }

  def flatMapTransformation(sc:SparkContext): Unit ={
    val bigData = Array("Scala Spark","Java Hadoop","Java Tachyon")//实例化字符串类型的Array
    val bigDataString = sc.parallelize(bigData) //创建以字符串为元素类型的ParalleCollection RDD
    val words = bigDataString.flatMap(line=>line.split(" "))//首先是通过传入的作为参数的函数来作用于RDD的每一个字符串进行单词切分(是以集合的形式存在)，然后把切分后的结果合并成一个大的数据集合，为Scala Spark Java Hadoop Java Tachyon
    words.collect().foreach(println)//收集计算结果并通过foreach循环打印

  }
  def groupByKeyTansformation(sc: SparkContext): Unit ={
    val data = Array(Tuple2(100,"Spark"),Tuple2(70,"Hadoop"),Tuple2(90,"Tachyon"),Tuple2(80,"Scala"),Tuple2(100,"Java"),Tuple2(70,"C"),Tuple2(90,"Hive"),Tuple2(80,"C++"))
    val dataRDD = sc.parallelize(data)//创建RDD
    val grouped = dataRDD.groupByKey()//按照相同的key对value进行分组，分组后的value是一个集合
    grouped.collect().foreach(println)//收集计算结果并通过foreach循环打印
  }

  def reduceByKeyTansformation(sc:SparkContext): Unit ={
    val file = sc.textFile("hdfs://localhost:9000/spark/input")
    val wordCountOrderd = file.flatMap(line=>line.split(" ")).map(word=>(word,1)).reduceByKey(_+_).sortByKey()
    wordCountOrderd.collect().foreach(pair=>println(pair._1+" : "+pair._2))//打印reduceByKey之后的结果
  }

  def joinTransformation(sc:SparkContext): Unit ={
    val studentNames = Array(
      Tuple2(1,"Spark"),
      Tuple2(2,"Tachyon"),
      Tuple2(3,"Hadoop")
    )

    val studentScores = Array(
      Tuple2(1,100),
      Tuple2(2,95),
      Tuple2(3,65)
    )

    val names = sc.parallelize(studentNames)
    val scores = sc.parallelize(studentScores)
    val studentNameAndScores = names.join(scores)
    studentNameAndScores.collect().foreach(println)
  }
  def cogroupTransformation(sc:SparkContext): Unit ={
    val studentNames = Array(
      Tuple2(1,"Spark"),
      Tuple2(2,"Tachyon"),
      Tuple2(3,"Hadoop")
    )

    val studentScores = Array(
      Tuple2(1,100),
      Tuple2(2,95),
      Tuple2(3,65),
      Tuple2(1,80),
      Tuple2(2,90),
      Tuple2(3,70)
    )
    val names = sc.parallelize(studentNames)
    val scores = sc.parallelize(studentScores)
    val nameScore = names.cogroup(scores)
    nameScore.foreach(pair=>println("ID: "+pair._1+"\nName: "+pair._2._1+"\nScore： "+pair._2._2+"\n"))

  }


}

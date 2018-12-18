package quicklearnscala

import java.io.File
import java.util
import java.util.{Calendar, Properties, TreeMap}

import scala.collection.{SortedMap, mutable}
import scala.io.Source
import scala.collection.JavaConversions.propertiesAsScalaMap

object ScalaChapter4 {

  /**
    * 1 . 设置一个映射 ,其中包含你想要的一些装备, 以及它们的价格 。 然后构建另一
    * 个映射 , 采用同一组键 ,但在价格上打 9 折 。
    *
    * @param map
    * @return
    */
  def execise1(map:Map[String,Double]):Map[String,Double] ={
    for ((k,v)<- map) print(k +" -> "+ v)
   val newMap= for ((k,v) <- map) yield (k,v * 0.9)
    newMap
  }

  /**
    * /2.编写一段程序，从文件中读取单词；用一个可变的映射清点单词出现的频率
    * @param file
    * @return
    */
  def execise2(file:String):mutable.HashMap[String,Int] = {
    var map = new mutable.HashMap[String,Int]()
    val lines = Source.fromFile(file)
    lines.getLines().foreach(
      line =>{
        for (word <- line.split(" ")){
          if (!map.keySet.contains(word.toLowerCase())) map(word.toLowerCase()) = 0
          map(word.toLowerCase())=map(word.toLowerCase())+1
        }
      }
    )
    map
  }

  /**
    * //3.重复做前一个练习，这次用不可变映射
    *
    * @param file
    * @return
    */

  def execise3(file:String):Map[String,Int]={
    var map = Map[String,Int]()
    val lines = Source.fromFile(file)
    lines.getLines().foreach(
      line=>{
        for (word<- line.split(" ")){
          if(!map.keySet.contains(word.toLowerCase())){
            val newMap:Map[String,Int] = map+(word.toLowerCase()->1 )
            map = newMap
          }else{
            val count:Int = map(word.toLowerCase())+1
            var newMap:Map[String,Int] = map - word.toLowerCase() +(word.toLowerCase()->count)
            map = newMap
          }
        }
      }
    )
    map
  }

  /**
    * 4 . 重复前一个练习 , 这次用已排序的映射,以便单词可以按顺序打印出来 。
    * @param file
    * @return
    */
  def execise4(file:String):SortedMap[String,Int]={
    var map = SortedMap[String,Int]()
    val lines = Source.fromFile(file)
    lines.getLines().foreach(
      line=>{
        for (word <- line.split(" ")){
          if (!map.keySet.contains(word.toLowerCase())) {
            var newMap:SortedMap[String,Int] = map + (word.toLowerCase->1)
            map = newMap
          }else{
            val count:Int = map(word.toLowerCase())+1
            var newMap:SortedMap[String,Int] = map - word.toLowerCase()+(word.toLowerCase->count)
            map = newMap
          }
        }
      }
    )

  map

  }
/*
  def execise5(file:String):TreeMap[String,Int]={
    var map:util.TreeMap[String, Int] =new TreeMap[String,Int]()
    val lines = Source.fromFile(file)
    lines.getLines().foreach(
      line=>{
        for(word <- line.split(" ")) {
          map(word) = map.getOrElse(word,0) + 1
        }
      }
    )
    map
  }*/

  def execise6():mutable.LinkedHashMap[String,Int]={
    var map = new mutable.LinkedHashMap[String, Int]()
    map += ("Monday" -> Calendar.MONDAY)
    map += ("Tuesday" ->Calendar.TUESDAY)
    map += ("Wednesday"->Calendar.WEDNESDAY)
    map += ("Thursday"->Calendar.THURSDAY)
    map += ("Friday"->Calendar.FRIDAY)
    map += ("Saturday"->Calendar.SATURDAY)
    map += ("Sunday"->Calendar.SUNDAY)
    map

  }
  def execise7():Unit={
    val map:scala.collection.Map[String,String] = System.getProperties()
     val keys = map.keySet
    val keyLength = for (key<-keys) yield key.length
    val maxKeyLength = keyLength.max
    for (key <- keys){
      print(key)
      print(" " * (maxKeyLength - key.length))
      print(" | ")
      println(map(key))
    }
  }

  //8.编写一个函数minmax(values:Array[Int]),返回数组中最大值和最小值的对偶
  def execise8(values:Array[Int]):Tuple2[Int,Int]={
    Tuple2[Int,Int](values.min,values.max)
  }

  //9.编写一个函数lteqgt(values:Array[Int],v:Int),返回数组中小于v，等于v和大于v的数量
  //要求一起返回
  def execise9(values:Array[Int],v:Int):Tuple3[Int,Int,Int]={
    var lcount = values.count(_ < v)
    var ecount = values.count(_ == v)
    var gcount = values.count(_ > v)
    Tuple3[Int,Int,Int](lcount,ecount,gcount)
  }

  def main(args: Array[String]): Unit = {
    println("--------------execise1------------")
    val map: Map[String, Double] = Map("book" -> 10, "iphone" -> 3900, "ipad" -> 1000)
    val newMap: Map[String, Double] = execise1(map)
    for ((k, v) <- newMap) print(k + " -> " + v)
    println()

    println("------------execise2-------------------")
    val file = "/home/hadoop/data.txt"
    val map2 = execise2(file)
    for ((k, v) <- map2)
      println(k + " : " + v)
    println()

    println("------------execise3-------------------")
    val file1 = "/home/hadoop/data.txt"
    val map3 = execise3(file1)
    for ((k, v) <- map3)
      println(k + " : " + v)
    println()

    println("--------execise4------------")
    val file2 = "/home/hadoop/data.txt"
    val map4 = execise4(file2)
    for ((k,v)<-map4)
      println(k+" -> "+v)
    println()

  /*  println("--------execise5------------")
    val file5 = "/home/hadoop/data.txt"
    val map5 = execise5(file5)
    for ((k,v)<-map5)
      println(k+" -> "+v)
    println()

*/

    val mapday = execise6()
    for(k<-mapday)
      println(k )
    println()
    execise7()
    println("--------execise9------------")

    val va = execise9(Array(1,2,3,4,1,23,45,2),3)

    println(va)
  }

}

package quicklearnscala

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object ScalaChapter3 {
  /**
    * 1 . 编写一段代码, 将 a 设置为一个和n随机整数的数组 ,要求随机数介于 0 (包含)和n(不包含)之间 。
    * @param n
    * @return
    */
  def execiseone(n:Int):Array[Int] ={
    val random:Random = new Random();
    val arrayRandom:Array[Int] = new Array[Int](n)
    for (i <- 0 until n)
      arrayRandom(i) = random.nextInt(n)
    arrayRandom
  }

  /**
    * 2. 编写一个循环 ,将整数数组中相邻的元素置换 。 例如, Array ( 1 , 2 , 3 , 4 , 5 ) 经过置换后变为 Arxay ( 2,1 , 4 , 3,5 )
    * @param array
    * @return
    */
  def execisetwo(array:Array[Int]):Array[Int] ={
    var temp = 0
    for(i <- 0 until(array.length-1,2)){
      temp = array(i)
      array(i) = array(i+1)
      array(i+1) = temp
    }
    array

  }

  /**
    * 3. 重复前一个练习,不过这一次生成一个新的值交换过的数组 。 用 for / yield 。
    * @param array
    * @return
    */
  def execisethree(array: Array[Int]):Array[Int]= {
    val newArray = for (i <- Range(0, array.length)) yield {
      if (i == array.length -1 && (i+1) % 2 == 1){
        array(i)
      }
      else if (i % 2 == 0) {
        array(i + 1)
      }
      else {
        array(i - 1)
      }
    }
    newArray.toArray
  }

  /**
    * 5 . 如何计算 Array [ Double ] 的平均值
    * @param array
    * @return
    */

  def execisefour(array: Array[Int]):Array[Int] ={
    var posArray:Array[Int] = for (ele <- array if (ele > 0)) yield  ele
    val negArray:Array[Int] = for (ele <- array if (ele <= 0)) yield  ele
    posArray ++= negArray
    posArray
  }

  /**
    * 5 . 如何计算 Array [ Double ] 的平均值 ?
    * @param array
    * @return
    */
  def execisefive(array: Array[Double]):Double={
    val ln = array.length
    val result = array.sum/ln
    result
  }

  /**
    * 6 . 如何重新组织 Array[Int]的元素将它们以反序排列? 对于 ArrayBuffer [Int] 你又会怎么做呢 ?
    * @param array
    * @return
    */
  def execisesix(array: Array[Int]):Array[Int] ={
    array.sortWith(_>_)
  }
  def execisesix1(array: ArrayBuffer[Int]):ArrayBuffer[Int]={
    array.sorted.reverse
  }

  /**
    * 8 . 重新编写 3.4 节结尾的示例 。 收集负值元素的下标 ,反序,去掉最后一个下标,然后对每一个下标调用 a . remove ( i ) 。 比较这样做的效率和 3.4 节中另外两种方法的效率 。
    * @param array
    * @return
    */

  def execiseseven(array: ArrayBuffer[Int]):ArrayBuffer[Int] ={
    val ind = for (i <- 0 until(array.length) if  array(i) < 0) yield i
    val dropInd = ind.reverse.dropRight(1)
    val tmp = array
    for(i <- dropInd)
      tmp.remove(i)
    tmp
  }

  def execiseseven1(array: ArrayBuffer[Int]):ArrayBuffer[Int]={
    var first = true
    var n = array.length
    var i = 0
    while (i<n){
      if (array(i)>=0) i+=1
      else {
        if (first) {first = false; i+=1}
        else {array.remove(i); n-=1}
      }
    }
    array
  }
  def  execiseseven2(array:ArrayBuffer[Int]):ArrayBuffer[Int]={
    var first = true
    val indexes = for(i <- 0 until array.length if first || array(i)>=0) yield{
      if(array(i)<0) first = false; i
    }
    for( j <- 0 until indexes.length) array(j) = array(indexes(j))
    array.trimEnd(array.length - indexes.length)
    array
  }

  def TimeZone():Array[String] = {
     val arr = java.util.TimeZone.getAvailableIDs()
     val res = for(ele <- arr if ele.startsWith("America/")) yield {
       ele.drop("America/".length)
     }
     scala.util.Sorting.quickSort(res)
     res
  }

  /**
    * 测试
    * @param args
    */
  def main(args: Array[String]): Unit = {
    println("--------------execiseone------------")
    val arrayone:Array[Int] = execiseone(9)
    for (ele<- arrayone)
      print(ele+" ")
    println()


    println("--------------execisetwo------------")
    val arraytow:Array[Int] = execisetwo(Array(1,2,3,4,5))
    for (i <- 0 until(arraytow.length)){
      print(arraytow(i)+" ")
    }
    print("\n")

    println("--------------execisthree------------")
    val arraythree:Array[Int] = execisethree(Array(1,2,3,4,5))
    for (i <- 0 until(arraythree.length)){
      print(arraythree(i)+" ")
    }
    print("\n")


    println("--------------execisfour------------")
    val arrayfour:Array[Int] = execisefour(Array(-1,2,1-4,9,0,-12))
    for (i <- 0 until(arrayfour.length)){
      print(arrayfour(i)+" ")
    }
    print("\n")


    println("--------------execisefive-------------")
    val arrayfive:Double = execisefive(Array(4,2,3.4,5))
    println(arrayfive)
    println()

    println("--------------execisesix1-----------")
    val arraysix1:Array[Int] = execisesix(Array(2,1,4,3,65))
    for (ele <- arraysix1)
      print(ele +" ")
    println()

    println("--------------execisesix1-----------")
    val arraysix2:ArrayBuffer[Int] = execisesix1(ArrayBuffer(2,1,4,3,65))
    for (ele <- arraysix2)
      print(ele +" ")
    println()

    println("-----------execiseseven--------------")
    var avgMilliSec =0.0
    val a= ArrayBuffer(-2,-3,1,-5,3,6,7)
    val n = 1000
    var arrayseven = new ArrayBuffer[Int]()
    var start = System.currentTimeMillis()
    for (i<- 1 to n){
       arrayseven= execiseseven(a)
    }
    var end = System.currentTimeMillis()
    var time = (end - start) * n
    println("methed one runtime: "+ time)
    for (ele<- arrayseven)
      print(ele+" ")
    println()

    start = System.currentTimeMillis()
    for (i <- 1 to n)
      arrayseven = execiseseven1(a)
    end = System.currentTimeMillis()
    time = (end - start) * n
    println("methed two runtime: "+ time)
    for (ele <- arrayseven)
      print(ele+" ")
    println()

    start = System.currentTimeMillis()
    for (i <- 1 to n)
      arrayseven = execiseseven2(a)
    end = System.currentTimeMillis()
    time = (end - start) * n
    println("methed tree runtime: "+time)
    for (ele <- arrayseven)
      print(ele+" ")
    println()

    println("----------------------TimeZone------------")
    val arraynight = TimeZone()

    for (i <- 0 until(arraynight.length))
      {
        if((i+1) %10 == 0)
          println( arraynight(i))
        else
          print(arraynight(i) + " ")
      }

    println()
  }
}

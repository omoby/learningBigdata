import scala.collection.mutable.ArrayBuffer

//移除一个数组中第一个负数以后的负数

object Scala{
  def main(args:Array[String]): Unit ={
    /*var arr = ArrayBuffer(1,2,-3,4,-2,-1,2,4,9,-3)
    var firstNegative = true
    var length = arr.length
    var index = 0
    while(index < length){
      if (arr(index) >= 0)
        index += 1
      else{
        if(firstNegative ){
          firstNegative = false
          index += 1
        }else{
          arr.remove(index)
          length -= 1

        }
      }
    }
    arr.toArray.foreach(ele=>print(ele + " "))
    */
    var arr = ArrayBuffer(1,2,-3,4,-2,-1,2,4,9,-3)
    var firstNagtive = true
    val indexs = for(index <- 0 until arr.length if firstNagtive || arr(index) >=0) yield {
      if (firstNagtive) firstNagtive = false
      index
    }
    for (index <- 0 until indexs.length){
      arr(index) = arr(indexs(index))
    }
    arr.trimEnd(arr.length - indexs.length)

  }

}

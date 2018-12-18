import javax.annotation.processing.FilerException

/*
package quicklearnscala

import java.io.FileNotFoundException
import scala.io.Source

object scalaTest {
  def main(args: Array[String]): Unit = {
   val n = 10
    def fun:Any={
      for (i<-1 to 10){
        if (i==10) return i
        println(i)
      }
    }

   //变长参数
    def sum(number:Int*):Int={
      var s = 0
      for (element<-number)
        s += element
      s
    }
 //过程
    def morning(content:String){
      val result = "Good " +content
      println(result )
    }
    def getFile(): Unit ={
      try{
        val content = Source.fromFile("/usr/local/spark/README.md").mkString
      }catch {
        case _:FileNotFoundException=>println("Oops!! File not Found")
      }finally {
        println("ByeBye world!")
      }
    }


  }

}
*/




/**
  * scala光速入门笔记
  * scala和java的关系
  *   1.都是运行在jvm上，scala是升级的java
  *   2.scala是一门函数式编程
  * saprk对scala的使用
  *
  * scala解释器命令行的变量为val--值变量(不可变变量)
  *                       var---可变变量
  *尽量使用val,确保数据在传输过程中不变
  *变量指定类型后赋值为指定的类型或者是子类型，变量声明后必须初始化，否则会出错
  *声明变量时：变量类型 变量名称：变量值类型 = 值 （变量值类型会根据后面 的值自动推断）
  *
  * val name1,name2,name3:String = "Scala"
  *apply是scala中构造的工厂函数
  * 循环
  * while
  * for
  * break对象
  * import scala.util.control.Breaks._
  *
  *val n = 10
  * def f1:Int={
  * for(i<-1 to 10){
  * if(i==10) return i
  * println(i)
  * }
  * }
  *
  * sum(1 to 100:_*)
  *过程
  * 没有返回值的函数就是过程
  * Array:不可变
  *
  * 变长数组
  * import scala.collection.mutable.ArrayBuffer
  * ArrayBuffer
  * Map--不可变集合
  * tuple--元组
  *
  * spark源码
  *
  *scala类
  *
  * private 属性的变量或者方法、函数外部不能访问
  * class HiScala{
  * private var name = "Spark"
  * def sayName(){println(name)}
  * def getName=name
  * }
  * private 自动生成的getter和setterye也是private
  *
  * class HiScala{
  *   var name = "Scala"
  *   def sayName(){println(name)}
  *   def getName = name
  *  }
  *  val scala = new HiScala
  *  println(scala.name) //调用自动生成的getter方法
  *  scala.name="Spark" //调用自动生成的setter
  *
  *
  * class Person{
  * private var myName = "Flink"
  * def name = this.myName  //getter方法
  * def name_=(newName:String): Unit ={ //复写setter
  * myName = newName
  * println("Hi: "+ myName)
  * }
  * }
  *
  *
  * class Person{
  * private var myName = "Flink"
  * def name = this.myName
  * def update(newName:String): Unit ={ //复写setter
  * myName = newName
  * println("Hi: "+ myName)
  * }
  * }
  *
  * private[this] //对象私有
  *
  *构造器
  * 与类名放在一起的是默认构造器
  *
  *
  * object--所有成员都是静态的
  * 伴生类和伴生对象必须在同一个文件中
  *
  * 抽象类
  * class Person{
  * println("Big data")
  * var name = "Flink"
  * var age = 12
  * def update(newName:String): Unit ={ //复写setter
  * println("Hi: "+ newName)
  * }
  * def talk(p:Person)={
  * println("Hello Scala " )
  * }
  * def this(name:String){
  * this()
  *         this.name = name
  * }
  *
  * def this(name:String,age:Int){
  * this(name)
  *         this.age = age
  * }
  * }
  *
  * object Person{
  * println("Scala")
  * val salary = 0.0
  * def getSalary = salary
  * }
  *
  * sparkcontext
  *
  *函数和变量都是一等公民
  *
  * def fun(name:String){
  * println(name)
  * }
  * val fun1_v = fun _ //函数赋值给变量，函数名后面有_
  *
  *匿名函数
  *val fun2 = (content:String)=>println(content) //函数没有名称
  *
  * 高阶函数--函数的参数也是函数
  *     函数的参数可以是变量 函数又可以赋值给变量
  *  val HiSCala = (content:String) =>println(content)
  *  def bigData(func:(String)=>Unit,content:String){func(content)}
  *  bigData(hiScala,"Spark")
  *
  *高阶函数可能是一个函数
  *  def func_Returned(content:String)=(message:String)=>println(message+“ ”+content)
  *
  *  1.函数的参数是函数
  *  2.函数的返回值是函数
  *def spark(func:(String)=>Unit,name:String){func(name)}
  * spark: (func: String => Unit, name: String)Unit
  *
  * scala> spark((name:String)=>println(name),"Scala")
  * Scala
  *
  * scala> spark((name)=>println(name),"Scala")
  * Scala
  *
  * scala> spark(name=>println(name),"Scala")
  * Scala
  *
  * scala> spark(println(_),"Scala")
  * Scala
  *
  * scala> spark(println,"Scala")
  * Scala
  *
  *
  * val arr = Array(1,2,3,4,5,6,7,8,9)
  * scala> arr.map(2*_)
  * res8: Array[Int] = Array(2, 4, 6, 8, 10, 12, 14, 16, 18)
  *
  * scala> arr.map(2*_).foreacH(println)
  * <console>:13: error: value foreacH is not a member of Array[Int]
  *        arr.map(2*_).foreacH(println)
  * ^
  *
  * scala> arr.map(2*_).foreach(println)
  * 2
  * 4
  * 6
  * 8
  * 10
  * 12
  * 14
  * 16
  * 18
  *
  * scala> arr.map(2*_).filter(_>10).foreach(println)
  * 12
  * 14
  * 16
  * 18
  *
  * 闭包--函数的变量超出作用域是还能还能被函数访问
  * scala> def scala(content:String)=(message:String)=>println(content+" "+message)
  * scala: (content: String)String => Unit
  *
  * scala> val funResult = scala("Spark")
  * funResult: String => Unit = $$Lambda$1259/1498401570@1399ad68
  *
  * scala> funResult("Flink")
  * Spark Flink
  * 函数内部的变量
  *科里化
  *
  * scala> def sum(x:Int,y:Int)=x+y
  * sum: (x: Int, y: Int)Int
  **
  *scala> sum(12,12)
  *res13: Int = 24
  **
  *scala> def sum_Currying(x:Int) =(y:Int)=>x+y
  *sum_Currying: (x: Int)Int => Int
  **
  *scala> cum_Currying(1)(2)
  *<console>:12: error: not found: value cum_Currying
  *cum_Currying(1)(2)
  * ^
  **
 *scala> sum_Currying(1)(2)
  * res15: Int = 3
  *
  *
  * scala> def sum_Currying_Better(x:Int)(y:Int) = x+y
  * sum_Currying_Better: (x: Int)(y: Int)Int
  *
  * scala>  sum_Currying_Better(12)(13)
  * res16: Int = 25
  *
  *集合操作
  * scala> (1 to 100).reduceLeft(_+_)
  * res17: Int = 5050
  *
  * scala> val list = List("Scala","Spark","Flink")
  * list: List[String] = List(Scala, Spark, Flink)
  *
  * scala> list.map("The content is: "+ _)
  * res18: List[String] = List(The content is: Scala, The content is: Spark, The content is: Flink)
  *
  *
  * scala> cal.flatMap(_.split(" "))
  * res20: List[String] = List(The, content, is:, Scala, The, content, is:, Spark, The, content, is:, Flink)
  *
  * scala> cal.flatMap(_.split(" ")).foreach(print)
  * Thecontentis:ScalaThecontentis:SparkThecontentis:Flink
  *
  *拉链操作
  *scala> list.zip(List(10,6,5))
  *res24: List[(String, Int)] = List((Scala,10), (Spark,6), (Flink,5))
  *
  *
  * 模式匹配 类型
  * def bigData(data:String): Unit ={
  * data match {
  * case "Spark"=>println("Wow!!!")
  * case "Hadoop"=>println("Ok")
  * case "Scala"=>println("Scala")
  * case _ => println("No match!")
  * }
  * }
  * scala> bigData("Hadoop")
  * Ok
  *
  *
  *
    def bigData(data:String): Unit ={
        data match {
          case "Spark"=>println("Wow!!!")
          case "Hadoop"=>println("Ok")
          case "Scala"=>println("Scala")
          case _ if(data == "Flink") =>println("Cool,Flink")
          case _ => println("No match")
        }
      }
  scala> bigData("Flink")
  Cool,Flink


   def bigData(data:String): Unit ={
    data match {
      case "Spark"=>println("Wow!!!")
      case "Hadoop"=>println("Ok")
      case "Scala"=>println("Scala")
      case data_ if(data_ == "Flink") =>println("Cool,Flink")
      case _ => println("No match")
    }
  }

 def bigData(data:String): Unit ={
    data match {
      case "Spark"=>println("Wow!!!")
      case "Hadoop"=>println("Ok")
      case "Scala"=>println("Scala")
      case data_ if(data_ == "Flink") =>println("Cool "+data_)
      case _ => println("No match")
    }
  }


   def exception(e:Exception): Unit ={
    e match {
      case file: FilerException=>println("File not Found")
      case _:Exception=>println("Exception ",e)
    }
集合进行模式匹配

scala> :past
// Entering paste mode (ctrl-D to finish)

def data(array:Array[String]): Unit ={
    array match {
      case Array("scala")=>println("Scala")
      case Array(spark,hadoop,flink)=>println(spark+" " +hadoop+" "+flink)
      case Array("Spark",_*)=>println("Spark ...")
      case _ => println("No match")
    }
  }

// Exiting paste mode, now interpreting.

data: (array: Array[String])Unit

scala> data(Array("Spark"))
Spark ...

scala> data(Array("scala"))
Scala

scala> data(Array("Spark","Spark","Kafka"))
Spark Spark Kafka

  case class 样例类
 case class Person(name:String)
  //默认情况下，scala自动以var修饰，
  case class ==java中的bean
  自己的实例化调用apply方法
  class Person(name:String)
  case class Worker(name:String,salary:Double) extends Person(name)
   case class Student(name:String,score:Double) extends Person(name)

 def sayHi(person:Person){
    person match{
        case Student(name,score)=>println("student "+name+" "+score)
        case Worker(name,salary)=>println("worker "+name+" "+salary)
        case _=>println("No match")
       }
   }

泛型
  类
   class Person[T](val content:T){//类
   def getContent(id:T)={//函数
     "id: "+id +"content: " + content
   }
 }

 边界
  上边界 <:
  下边界 >:

  View Bounds--视图界定 语法 <% --对类型进行隐式转换
  类型进行隐式转换后看看在不在边界范围内
  T:类型
  类型[T]
  在上文
 class Compare[T:Ordering](val n1:T,val n2:T){
    def bigger(implicit ordered:Ordering[T])=if(ordered.compare(n1,n2)>0) n1 else n2
  }

  力变和谐变

T ClassTag
classTag


  隐式转换
  implicit

   class Person(val name:String)

  class Engineer(val name:String,val salary:Double){
    def code = println("Coding.....")
  }
  implicit def person2Engineer(p:Person):Engineer={
    new Engineer(p.name,10000)
  }
  def toCode(p:Person): Unit ={
    p.code
  }
  toCode(new Person("Scala")) //new Person 返回的是engineer
  从上下文类中或者是伴生对象中找隐式函数
  在上下文中通过隐士产生注入
  范围：1.当前作用域
       2.到隐士参数的类型的伴生对象中去找隐士值



    package quicklearnscala
  import java.io.File
  import scala.io.Source
  class RicherFile(val file:File){
    def read = Source.fromFile(file.getPath()).mkString
  }
  class file_Implicits(path:String) extends File(path)
  object file_Implicits{
    implicit def file2RicherFile(file:File) = new RicherFile(file) //File-->RicherFile
  }
  object Implicits_Internals{
    def main(args: Array[String]): Unit = {
      val file = new file_Implicits("/home/hadoop/output.txt")
      println(file.read)
      file.read
    }
  }

  科里化


//隐式值
  class Level (val level:Int)
  def toWork(name:String)(implicit l: Level)=(println(name+" "+l.level))
  //


  /**
  * 隐式函数
  */
object Context_Implicits{
  implicit val defualt:String = "Flink"
}

object Param{
  def print(content:String)(implicit  language:String): Unit ={
    println(language +" : "+content)
  }
}

object Implicits_Parameters{
  def main(args: Array[String]): Unit = {
    Param.print("Spark")("Scala")
    import Context_Implicits._
    Param.print("Hadoop")
  }
}


  //隐式对象


abstract class Template[T]{
  def add(x:T,y:T):T
}
abstract class SubTemplate[T] extends Template[T]{
  def unit:T
}
object Implicits_Object{
  def main(args: Array[String]): Unit = {
    implicit object StringAdd extends SubTemplate[String]{
      override def add(x: String, y: String): String= x concat y

      override def unit: String = ""
    }
    implicit object IntAdd extends SubTemplate[Int] {
      override def add(x: Int, y: Int): Int = x + y

      override def unit: Int = 0
    }
    def sum[T](xs:List[T])(implicit m:SubTemplate[T]):T=
      if(xs.isEmpty) m.unit
      else m.add(xs.head,sum(xs.tail))
      println(sum(List(1,2,3,4,5,6)))
      println(sum(List("Scala","Spark","Flink")))
    }


}

  //隐式类
import java.io.File

import scala.io.Source

object Context_Helper{
  implicit class FileInhansome(file:File){
    def read = Source.fromFile(file.getPath).mkString
  }
  implicit class Op(x:Int){
    def addSAP(second:Int) = x + second
  }
}
object Implicits_Class{
  def main(args: Array[String]): Unit = {
    import Context_Helper._
    println(1.addSAP(2))
    println(new File("/home/hadoop/data.txt").read)
  }
}


  //并发编程
  Actor---scala //去掉共享全局变量的加锁的机制
  Thread---java //java--共享全局变量的加锁的机制
  actor---akka

  *
  *
  *
  *
  */

class ScalaTest{
  class Person(val name:String)

  class Engineer(val name:String,val salary:Double){
    def code = println("Coding.....")
  }
  implicit def person2Engineer(p:Person):Engineer={
    new Engineer(p.name,10000)
  }
  def toCode(p:Person): Unit ={
    p.code
  }
  toCode(new Person("Scala")) //new Person 返回的是engineer

  class Level (val level:Int)
  def toWork(name:String)(implicit l: Level)=(println(name+" "+l.level))









}

package DT

/**
  * FileName: HelloScala
  * Author:   hadoop
  * Email:    3165845957@qq.com
  * Date:     18-11-5 上午9:32
  * Description:object作为Scala中的一个关键字，相当于Java中的public static class这样的修饰符，也就是说object中的成员是静态的成员变量，所以main方法是静态的，不需要类的实例就可以被虚拟机调用，这正式作为JVM入口函数的必备条件。
  * 疑问：object是不是一个对象（此时你一定是从Java的角度考虑的，但事实是Scala中的静态类，不是对象）
  *从Spark的Master和Worker的源码中可以发现其入口是main方法是在object中的
  */
object HelloScala {
  /**
    * 1.def 是scala的关键字，所有的def定义的内容都是函数或者方法
    * 2.main是方法，因为被def定义且不具有函数的特点
    * 3.main是SCala语言中规定的scala的运用程序的入口，一个运行的scala运用程序只能有一个main方法
    * 4.args: Array[String],其中args是参数名称，Array[String]表明程序运行时候传入参数的类型
    * 5.: Unit表明main入口方法的类型是Unit，执行main方法后返回Unit
    * 6.Unit相当于Java中的void类型
    * 7.=表明main方法执行的结果是由谁来赋值的，或者说main方法的方法体在=右面
    * 8.方法体一般用{}来封装，里面可以有很多条语句
    * 9.{}语句块默认情况下最后一条语句的结果类型就是{}的返回类型
    * 10.跟踪println源代码收获发现scala的println的IO操作是借助Java的IO操作，也就是说scala调用了Java
    * 11.如果方法或者函数的类型或者返回类型是Unit类型，就可以把”: Unit =“去掉，其他非此方法都不能去掉
    * 12.关于println打印出内容到控制台，底层接住了Java IO的功能，一个事实是scala经常会用Java的实现来缩短开发时间，比如说来操作（BD，NoSQL（Cassadra，Hbase等））的JDBC，在例如关于线程Thread的操作，scala往往也会使用Java中的Thread；
    * 13.按照当今OS的原理，程序的main方法都是运行在主线程中的，OS的运行分为Kernel Space和user Space,运行程序是运行在User Space中的，运用程序scala所在的进程一般都是通过OS Fork出来的，被Fork出来的运用程序进程默认会有主线程 ，而我们的main方法就是默认在主线程中的
    * @param args
    */
  def main(args: Array[String]): Unit = {
    println("Hello Scala!!!")
    println(args.length)
  }

}

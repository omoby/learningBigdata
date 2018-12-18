package DT;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;


/**
 * 使用java的方式开发进行本地测试Spark的WordCount程序
 * @author ombey
 * @email 3165845957@qq.com
 */
public class JavaWordCount {

    public static void main(String[] args) {
        /**
         * 第一步：创建Spark配置对象SparkConf，设置spark程序的运行时信息
         * 例如通过setMaster来设置要连接的spark集群的Master的URl，
         *  如果设置为local，则代表spakr程序在本地运行，适合机器配置差
         */
        SparkConf conf = new SparkConf().setAppName("Spark WordCount Written by Jave").setMaster("local");

        /**
         * 第二步：创建SparkContext对象，
         * SparkContext是spark程序的所有功能的唯一入口，无论是是采用scala、
         * Java，Python、R都必须有一个SparkCont(不同的语言具体的名称不同，java为JavaSparkContext)
         * SparkContext的核心作用：初始化spark应用程序所需要的核心组件，包括DAGScheduler
         * TaskScheduler，SchedulerBackend
         * 同时还会负责spark程序的往Master注册程序等
         * SparkContext是spark应用程序中最为重要的一个对象
         */
        JavaSparkContext sc = new JavaSparkContext(conf); //其底层实际上就是scala的SparkContext

        /**
         * 第三步：根据具体的数据来源（HDFS、HBase、Local FS、Db，S#
         * 等） 通过SparkContext来创建RDD
         * RDD创建基本有三种方式：根据外部的数据来源（例如HDFS），根据Scala集群，由其他的RDD操作
         * RDD会被划分为一系列的Partitions，分配到每一个Partition的数据属于一个Task处理范畴
         *
         */
        JavaRDD<String> lines = sc.textFile("/home/hadoop/file2.txt");

        /**
         * 第四步:对初始的RDD进行Transform级别的处理，例如map、filter的高级别函数等的
         * 编程，来进行具体的数据计算，
         * 4.1步：将每一行的字符串拆分成单个的单词
         */

        JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {//如果是scala，由于SAM转换，所以可以写成 val words = lines.flatMap(line=>line.split(" "))
            @Override
            public Iterator<String> call(String s) {
                return Arrays.asList(s.split(" ")).iterator();
            }
        });


        /**
         * 第四步:对初始的RDD进行Transform级别的处理，例如map、filter的高级别函数等的
         * 编程，来进行具体的数据计算，
         * 4.2步：将每一行的字符串拆分成单个的单词的基础上对每一个单词计数为1，也就是word=>(word,1)
         */
        JavaPairRDD<String,Integer> pairs = words.mapToPair(new PairFunction<String, String, Integer>() {
            @Override
            public Tuple2<String, Integer> call(String word) throws Exception {
                return new Tuple2<String,Integer>(word,1);
            }
        });

        /**
         * 第四步:对初始的RDD进行Transform级别的处理，例如map、filter的高级别函数等的
         * 编程，来进行具体的数据计算，
         * 4.3步：在每个单词实例计数为1的基础智商统计每个单词在文本中的出现的总次数
         */
        JavaPairRDD<String,Integer> wordcount = pairs.reduceByKey(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer v1, Integer v2) throws Exception {
                return v1+v2;
            }
        });

        wordcount.foreach(new VoidFunction<Tuple2<String, Integer>>() {
            @Override
            public void call(Tuple2<String, Integer> pairs) throws Exception {
                System.out.println(pairs._1+" : "+ pairs._2);
            }
        });
        System.out.println("Java Word Count!!");
        sc.close();


    }
}

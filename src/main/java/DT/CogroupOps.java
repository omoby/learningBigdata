package DT;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class CogroupOps {
    public static void main(String[] args){
        /**
         * 第一步：创建Spark配置对象SparkConf，设置spark程序的运行时信息
         * 例如通过setMaster来设置要连接的spark集群的Master的URl，
         *  如果设置为local，则代表spakr程序在本地运行，适合机器配置差
         */
        SparkConf conf = new SparkConf().setAppName("Spark CogroupOps Written by Jave").setMaster("local");

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

        List<Tuple2<Integer, String>> nameList = Arrays.asList(
                new Tuple2<Integer,String>(1,"Spark"),
                new Tuple2<Integer,String>(2,"Tachyon"),
                new Tuple2<Integer,String>(3,"Hadoop")
                );
        List<Tuple2<Integer, Integer>> scoreList = Arrays.asList(
                new Tuple2<Integer,Integer>(1,100),
                new Tuple2<Integer,Integer>(2,90),
                new Tuple2<Integer,Integer>(3,70),
                new Tuple2<Integer,Integer>(1,110),
                new Tuple2<Integer,Integer>(2,95),
                new Tuple2<Integer,Integer>(3,60)
        );
        JavaPairRDD<Integer, String> names= sc.parallelizePairs(nameList);
        JavaPairRDD<Integer, Integer> scores =  sc.parallelizePairs(scoreList);
        JavaPairRDD<Integer,Tuple2<Iterable<String>,Iterable<Integer>>> nameScores = names.cogroup(scores);
        nameScores.foreach(new VoidFunction<Tuple2<Integer, Tuple2<Iterable<String>, Iterable<Integer>>>>() {
            private  long serialVersionUID = 1L;
            @Override
            public void call(Tuple2<Integer, Tuple2<Iterable<String>, Iterable<Integer>>> t) throws Exception {
                System.out.println("ID "+ t._1);
                System.out.println("Name: "+ t._2._1);
                System.out.println("Score: "+ t._2._2);
                System.out.println("============================");
            }
        });
        sc.close();
    }
}

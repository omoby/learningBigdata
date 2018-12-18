package DT;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple1;
import scala.Tuple2;
/**
 * 二次排序具体实现步骤
 * 1.按照Ordered和Serializable借口实现自定义排序的Key
 * 2.要将进行二次排序的文件加载进来生成<Key,Value>类型的RDD
 * 3.使用sortByKey基于自定义的key进行二次排序
 * 4.去除掉排序的key，只保留排序的结果
 */
public class SecondarySortKeyApp {
    public static void main(String[] args){
        /**
         * 第一步：创建Spark配置对象SparkConf，设置spark程序的运行时信息
         * 例如通过setMaster来设置要连接的spark集群的Master的URl，
         *  如果设置为local，则代表spakr程序在本地运行，适合机器配置差
         */
        SparkConf conf = new SparkConf().setAppName("Spark SecondarySortKeyApp Written by Jave").setMaster("local");

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
        JavaRDD<String> lines = sc.textFile("file:///home/hadoop/spark.txt");
        JavaPairRDD<SecondarySort,String> pairs = lines.mapToPair(new PairFunction<String,SecondarySort,String>(){
            private static final long serialVersionUID = 1L;
            @Override
            public Tuple2<SecondarySort,String> call (String line) throws Exception{
                String[] splited = line.split(" ");
                SecondarySort key = new SecondarySort(Integer.valueOf(splited[0]),Integer.valueOf(splited[1]));
                return new Tuple2<SecondarySort,String>(key,line);
            }

        });
        JavaPairRDD<SecondarySort,String> sorted = pairs.sortByKey();
        //过滤掉自定义的key，保留排序的结果
        JavaRDD<String> secondarySorted= sorted.map(new Function<Tuple2<SecondarySort, String>, String>() {

            private static final long servialVersion = 1L;
            @Override
            public String call(Tuple2<SecondarySort, String> sortedContent) throws Exception {
                return  sortedContent._2;
            }
        });
        secondarySorted.foreach(new VoidFunction<String>() {
            @Override
            public void call(String s) throws Exception {
                System.out.println(s);
            }
        });
    }


}

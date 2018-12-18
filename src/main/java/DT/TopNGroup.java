package DT;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.api.java.function.VoidFunction;
import scala.Tuple2;

import java.util.Arrays;
import java.util.Iterator;

/**
 * FileName: TopNGroup
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-9-8 下午7:51
 * Description:使用java的方式开发分组topN程序
 */
public class TopNGroup {
    public static void main(String[] args){
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
        JavaRDD<String> lines = sc.textFile("file:///home/hadoop/topngroup.txt");
        //把每行数据变成符合要求的(key,value)的方式
        JavaPairRDD<String,Integer> pairs = lines.mapToPair(new PairFunction<String, String, Integer>() {
            private static final long  servialVersion = 1L;
            @Override
            public Tuple2<String, Integer> call(String s) throws Exception {
                String[] splited = s.split(" ");
                return new Tuple2<String,Integer>(splited[0],Integer.valueOf(splited[1]));
            }
        });
        JavaPairRDD<String,Iterable<Integer>> groupedPairs = pairs.groupByKey();//对数据进行分组

        //获取top N
        JavaPairRDD<String,Iterable<Integer>> top5 = groupedPairs.mapToPair(new PairFunction<Tuple2<String, Iterable<Integer>>, String,Iterable<Integer>>()       {
            private static final long servialVersion = 1L;
            @Override
            public Tuple2<String, Iterable<Integer>> call(Tuple2<String, Iterable<Integer>> groupedData) throws Exception {
                Integer[] top5 = new Integer[5];//保存Top5的数据本身
                String groupedKey = groupedData._1; //获取分组的组名
                Iterator<Integer> groupedValue = (Iterator<Integer>) groupedData._2.iterator();//获取每一组的内容集合
                while(groupedValue.hasNext()){ //查看是否有下一个元素，如果有就继续循环
                    Integer value = groupedValue.next();//h获取当前循环的元素本身的内容
                    for(int i = 0; i < 5; i++){
                        if (top5[i] == null){
                            top5[i] = value;
                            break;
                        }else if (value > top5[i]){
                            for (int j = 4; j > i;j--){
                                top5[j] = top5[j-1];
                            }
                            top5[i] = value;
                            break;
                        }
                    }
                }
                return new Tuple2<String,Iterable<Integer>>(groupedKey, Arrays.asList(top5));
            }
        });
        //打印分组后的topN
        top5.foreach(new VoidFunction<Tuple2<String, Iterable<Integer>>>() {
           private static final long servialVersionUID = 1L;
            @Override
            public void call(Tuple2<String, Iterable<Integer>> topped) throws Exception {
                System.out.println("Group Key: "+ topped._1);//获取groupKey
                Iterator<Integer> toppedValue = (Iterator<Integer>) topped._2.iterator();//获取groupvalue
                while (toppedValue.hasNext()){//打印具体的Top N
                    Integer value  = toppedValue.next();
                    System.out.println(value);
                }
                System.out.println("===================");
            }
        });
        sc.close();

    }
}

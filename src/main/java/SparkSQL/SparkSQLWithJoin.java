
package SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.List;


/**
 * FileName: SparkSQLWithJoin
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-11-8 上午10:48
 * Description:
 */

public class SparkSQLWithJoin {
    public static void main(String[] args){
        //创建SparkConf用于读取系统信息并设置运用程序的名称
        SparkConf conf = new SparkConf().setAppName("SparkSQLWithJoin").setMaster("local");
        //创建JavaSparkContext对象实例作为整个Driver的核心基石
        JavaSparkContext sc = new JavaSparkContext(conf);
        //设置输出log的等级
        sc.setLogLevel("ERROR");
        //创建SQLContext上下文对象，用于SqL的分析
        SQLContext sqlContext = new SQLContext(sc);
        //创建dataFrame，可以简单的认为DataFrame是一张表
        Dataset peopleDS = sqlContext.read().json("file:///home/hadoop/people.json");
        //基于JSON构建的DataFrame来注册的临时表
        peopleDS.registerTempTable("peopleScore");
        //查询出分数大于90的人
        Dataset excellentScore = sqlContext.sql("select name ,score from peopleScore where score >= 90");

        /**
         * 在Dataframe的基础上转化为RDD,通过map操作计算出分数大于90的所有姓名
         */
        List<String> excellentScoreNameList = excellentScore.javaRDD().map(new Function<Row ,String>() {

            @Override
            public String call(Row row) throws Exception {
                return row.getAs("name");
            }
        }).collect();
        //动态组拼出JSON
        List<String> peopleInformations = new ArrayList<String>();
        peopleInformations.add("{\"name\":\"Michael\",\"age\":20}");
        peopleInformations.add("{\"name\":\"Andy\", \"age\":30}");
        peopleInformations.add("{\"name\":\"Justin\", \"age\":19}");
        //通过内容为JSON的rdd来构造dataframe
        JavaRDD<String> peopleInformationRDD = sc.parallelize(peopleInformations);
        Dataset peopleInformationDS = sqlContext.read().json(peopleInformationRDD);
        //注册为临时表
        peopleInformationDS.registerTempTable("peopleInformations");
        //查询成绩优秀的人的姓名和年龄的sql语句
        String sqlText =  "select name,age from peopleInformations where name in (";
        for(int i = 0;i < excellentScoreNameList.size();i++){
            sqlText+="'"+ excellentScoreNameList.get(i)+"'";
            if (i < excellentScoreNameList.size()-1){
                sqlText+=",";

            }
        }
        sqlText +=")";
        //执行sql语句得到一个Dataset
        Dataset excellentNameAgeDS =  sqlContext.sql(sqlText);
        //将成绩优秀的人的成绩和年龄进行jion操作
        JavaPairRDD<String,Tuple2<Integer,Integer>> resultRDD = excellentScore.javaRDD().mapToPair(new PairFunction<Row,String,Integer>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Tuple2<String, Integer> call(Row row) throws Exception {
                return new Tuple2<String,Integer>(row.getAs("name"),(int)row.getLong(1));
            }
        }).join(excellentNameAgeDS.javaRDD().mapToPair(new PairFunction<Row,String,Integer>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Tuple2<String, Integer> call(Row row) throws Exception {
                return new Tuple2<String,Integer>(row.getAs("name"),(int)row.getLong(1));
            }
        }));
        //调用RowFactory工厂方法生成记录
        JavaRDD<Row> reusltRowRDD = resultRDD.map(new Function<Tuple2<String, Tuple2<Integer, Integer>>, Row>() {

            @Override
            public Row call(Tuple2<String, Tuple2<Integer, Integer>> tuple) throws Exception {
                return RowFactory.create(tuple._1,tuple._2._2,tuple._2._1);
            }
        });

       /**
         * 动态构造DataFrame的元数据，一般而言，有多少列以及每列的具体类型可能来自于json文件，也可能来自于数据库
         */
        List<StructField> structFields = new ArrayList<StructField>();
        structFields.add(DataTypes.createStructField("name", DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("age", DataTypes.IntegerType,true));
        structFields.add(DataTypes.createStructField("score", DataTypes.IntegerType,true));
        //构建StructType,用于最后DataFrame元数据的描述
        StructType structType = DataTypes.createStructType(structFields);
        //生成Dataset
        Dataset personDS = sqlContext.createDataFrame(reusltRowRDD,structType);
        personDS.show();
        personDS.write().format("json").save("file:///home/hadoop/peopleResult.json");
        sc.close();
    }
}

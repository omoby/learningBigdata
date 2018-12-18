package SparkSQL;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;


import java.util.ArrayList;
import java.util.List;

/**
 * FileName: RDDToDataFrameByProgramatically
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-28 下午8:00
 * Description:
 */
public class RDDToDataFrameByProgramatically {
    public static void main(String[] args){
        //创建SparkConf用于读取系统信息并设置运用程序的名称
        SparkConf conf  = new SparkConf().setAppName("RDDToDataFrameByReflection").setMaster("local");
        //创建JavaSparkContext对象实例作为整个Driver的核心基石
        JavaSparkContext sc = new JavaSparkContext(conf);
        //设置输出log的等级,可以设置INFO,WARN,ERROR
        sc.setLogLevel("ERROR");
        //创建SQLContext上下文对象，用于SqL的分析
        SQLContext sqlContext = new SQLContext(sc);
        //创建RDD，读取textFile
        JavaRDD<String> lines = sc.textFile("/home/hadoop/person.txt");
        /**
         * 第一步：在RDD的基础上创建类型为Row的RDD
         */
        JavaRDD<Row> personRDD = lines.map(new Function<String, Row>() {
            @Override
            public Row call(String line) throws Exception {
                String[] splited = line.split(",");
                return RowFactory.create(Integer.valueOf(splited[0]),splited[1], Integer.valueOf(splited[2]));
            }
        });
        /**
         * 第二部：动态构造DataFrame的元数据，一般而言，有多少列以及每列的具体类型可能来自于json文件，也可能来自于数据库
         */
        List<StructField> structFields = new ArrayList<StructField>();
        structFields.add(DataTypes.createStructField("id", DataTypes.IntegerType,true));
        structFields.add(DataTypes.createStructField("name", DataTypes.StringType,true));
        structFields.add(DataTypes.createStructField("age", DataTypes.IntegerType,true));
        //构建StructType,用于最后DataFrame元数据的描述
        StructType structType = DataTypes.createStructType(structFields);

        /**
         * 第三步：基于已有的MetaData以及RDD<Row>来构造DataFrame
         */
       Dataset personDs =  sqlContext.createDataFrame(personRDD,structType);
        /**
         * 第四步：注册成为临时表以供后续的SQL查询操作
         */
        personDs.registerTempTable("person");
        /**
         * 第五步：进行数据的多维度分析
         */
        Dataset result = sqlContext.sql("select * from person where age > 8 ");
        /**
         * 第六步：对结果进行处理，包括由dataFrame转换成为RDD<Row>以及结果的持久化
         */
        List<Row> listRow = result.javaRDD().collect();
        for (Row row :listRow){
            System.out.println(row);
        }

    }
}

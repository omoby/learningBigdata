package SparkSQL;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import java.util.List;

/**
 * FileName: RDDToDataFrameByReflection
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-28 下午3:27
 * Description:使用反射的方式将RDD转换成为DataFrame
 */
public class RDDToDataFrameByReflection {
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
         * 将读入的RDD数据转化为Person类型的DataFrame
         */
        JavaRDD<Person> person = lines.map(new Function<String, Person>() {
            @Override
            public Person call(String line) throws Exception {
                String[] splited = line.split(",");
                Person p = new Person();
                p.setId(Integer.valueOf(splited[0].trim()));
                p.setName(splited[1]);
                p.setAge(Integer.valueOf(splited[2].trim()));
                return p;
            }
        });
        /**
         * reateDataFrame方法来自于sqlContext，有两个参数，第一个是RDD，这里就是lines.map之后的person
         * 这个RDD里的类型是Person，即每条记录都是Person，Person其实是有id,name,age的，
         * JavaRDD本身并不知道id,name,age信息，所以要创建DataFrame，DataFrame需要知道id,name,age信息，
         * DataFrame怎么知道的呢？这里用createDataFrame时传入两个参数，第一个的RDD本身，第二个参数是
         * 对RDD中每条数据的元数据的描述，这里就是java bean class，即person.class
         * 实际上工作原理是：person.class传入时本身会用反射的方式创建DataFrame，
         * 在底层通过反射的方式获得Person的所有fields，结合RDD本身，就生成了DataFrame
         */
       Dataset ds =  sqlContext.createDataFrame(person,Person.class);
        //将DataFrame变成一个TempTable
       ds.registerTempTable("person");
        //在内存中就会生成一个persons的表，在这张临时表上就可以写SQL语句了
       Dataset bigDatas = sqlContext.sql("select * from person where age >= 6");
        //转过来就可以把查询后的结果变成 RDD，返回的是JavaRDD<Row>
       JavaRDD<Row> bigdataRDD = bigDatas.javaRDD();
        //再对RDD进行map操作。元素是一行一行的数据(SQL的Row)，结果是Person，再次还原成Person。
        //这里返回的是具体的每条RDD的元素。
      JavaRDD<Person> result =  bigdataRDD.map(new Function<Row, Person>() {
           @Override
           public Person call(Row row) throws Exception {
               Person p = new Person();
               /**
                * 转化为DataFrame后，dataFrame对数据字段进行了结构优化，
                * 对字段进行了排序，所以使用下面的方式是不能按正确数据顺序访问数据的
                *         p.setId(row.getInt(0));
                *         p.setName(row.getString(1));
                *         p.setAge(row.getInt(2));
                */

               p.setId(row.getInt(1));
               p.setName(row.getString(2));
               p.setAge(row.getInt(0));
               return p;
           }
       });
      List<Person> personList =  result.take(3);
     for (Person p : personList){
         System.out.println(p);
     }

    }
}


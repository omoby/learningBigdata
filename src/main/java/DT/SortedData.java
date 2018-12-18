package DT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;


/**
 * FileName: SortedData
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-6 上午10:54
 * Description:
 * 数字排序
 */

public class SortedData {
    /**
     * 使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
     */

    public static class forSortedMapper extends Mapper<Object, Text, IntWritable, IntWritable> {
        private IntWritable mapperValue = new IntWritable(); //存放key的值
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString(); //获取读取的值，转化为String
            mapperValue.set(Integer.parseInt(line)); //将String转化为Int类型
            context.write(mapperValue,new IntWritable(1)); //将每一条记录标记为（key，value） key--数字 value--出现的次数
                                                                //每出现一次就标记为（number，1）
        }
    }


/**
     * 使用Reducer将输入的key本身作为key直接输出
     */


 public static class forSortedReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable>{
        private IntWritable postion = new IntWritable(1); //存放名次
        @Override
        protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            for (IntWritable item :values){ //同一个数字可能出多次，就要多次并列排序
                context.write(postion,key); //写入名次和具体数字
                System.out.println(postion + "\t"+ key);
                postion = new IntWritable(postion.get()+1); //名次加1
            }
        }
    }


    public static void main(String[] args) throws Exception {


        Configuration conf = new Configuration(); //设置MapReduce的配置
        conf.set("mapred.job.tracker", "192.168.1.108:9000");
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if(otherArgs.length < 2){
            System.out.println("Usage: SortedData <in> [<in>...] <out>");
            System.exit(2);
        }

        //设置作业
        //Job job = new Job(conf);
        Job job = Job.getInstance(conf);
        job.setJarByClass(SortedData.class);
        job.setJobName("SortedData");
        //设置处理map,reduce的类
        job.setMapperClass(forSortedMapper.class);
        job.setReducerClass(forSortedReducer.class);
        //设置输入输出格式的处理
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        //设定输入输出路径
        for (int i = 0; i < otherArgs.length-1;++i){
            FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length-1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }

}

/*
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class SortedData {

    public static class Map extends
            Mapper<Object, Text, IntWritable, IntWritable> {

        private static IntWritable data = new IntWritable();

        // 实现map函数
        public void map(Object key, Text value, Context context)

                throws IOException, InterruptedException {

            String line = value.toString(); // 将输入的每一行数据转换为String类型

            data.set(Integer.parseInt(line)); // 将String 转换为Integer

            context.write(data, new IntWritable(1)); // 将 date->key
            // 统计key出现的次数自增为value
        }
    }

    // reduce将输入中的key复制到输出数据的key上，并直接输出 这是数据区重的思想
    public static class Reduce extends
            Reducer<IntWritable, IntWritable, IntWritable, IntWritable> {

        private static IntWritable linenum = new IntWritable(1);
        private IntWritable result = new IntWritable();

        // 实现reduce函数

        public void reduce(IntWritable key, Iterable<IntWritable> values,  //Iterable转为List
                           Context context)

                throws IOException, InterruptedException {

            for (IntWritable val : values) {

                context.write(linenum, key);

                linenum = new IntWritable(linenum.get() + 1);
                System.out.println("posttion: "+linenum + " "+" key: "+ key);
            }
        }

    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();

        //conf.set("mapred.job.tracker", "192.168.60.129:9000");

        // 指定带运行参数的目录为输入输出目录
        String[] otherArgs = new GenericOptionsParser(conf, args)
                .getRemainingArgs();

        */
/*
         * 指定工程下的input2为文件输入目录 output2为文件输出目录 String[] ioArgs = new String[] {
         * "input2", "output2" };
         *
         * String[] otherArgs = new GenericOptionsParser(conf, ioArgs)
         * .getRemainingArgs();
         *//*


        if (otherArgs.length != 2) { // 判断路径参数是否为2个

            System.err.println("Usage: Data Deduplication <in> <out>");

            System.exit(2);

        }

        // set maprduce job name
        Job job = new Job(conf, "Data sort");

        job.setJarByClass(SortedData.class);

        // 设置Map、Combine和Reduce处理类

        job.setMapperClass(Map.class);

        job.setCombinerClass(Reduce.class);

        job.setReducerClass(Reduce.class);

        // 设置输出类型

        job.setOutputKeyClass(IntWritable.class);

        job.setOutputValueClass(IntWritable.class);

        // 设置输入和输出目录

        FileInputFormat.addInputPath(job, new Path(otherArgs[0]));

        FileOutputFormat.setOutputPath(job, new Path(otherArgs[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }

}
*/
/*
import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class SortedData {

    */
/**
     * @param
     *//*

    public static class Map extends Mapper<Object,Text,IntWritable,IntWritable>{
        private static IntWritable data = new IntWritable();
        public void map(Object key,Text value,Context context) throws IOException, InterruptedException{
            String line = value.toString();
            data.set(Integer.parseInt(line));
            context.write(data, new IntWritable(1));
        }
    }
    public static class Reduce extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable>{
        private static IntWritable linenum = new IntWritable(1);
        public void reduce(IntWritable key,Iterable<IntWritable> values,Context context) throws IOException, InterruptedException{
            for(IntWritable val:values){
                context.write(linenum,key);
                System.out.println("posttion: "+linenum + " "+" key: "+ key);
                linenum = new IntWritable(linenum.get()+1);


            }
        }
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // TODO Auto-generated method stub
        //初始化配置
        Configuration conf = new Configuration();

        */
/*类比与之前默认的args，只是在程序中实现配置，这样不必去eclipse的arguments属性添加参数，
         **但是认为作用一样根据个人喜好设置,如下图所示：
         *//*

        //设置输入输出路径
        //String[] ioArgs = new String[]{"hdfs://localhost:9000/home/xd/hadoop_tmp/Sort_in","hdfs://localhost:9000/home/xd/hadoop_tmp/Sort_out"};
        String[] otherArgs = new GenericOptionsParser(conf, args)
                .getRemainingArgs();

        */
/*String[] otherArgs = new GenericOptionsParser(conf,ioArgs).getRemainingArgs();*//*


        if(otherArgs.length!=2){
            System.err.println("Usage:Data Deduplication <in> <out>");
            System.exit(2);
        }
        //设置作业
        Job job = new Job(conf,"Datasort Job");
        job.setJarByClass(SortedData.class);

        //设置处理map,reduce的类
        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        //设置输入输出格式的处理
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);

        //设定路径
        FileInputFormat.addInputPath(job,new Path(otherArgs[0]));
        FileOutputFormat.setOutputPath(job,new Path(otherArgs[1]));
        */
/*
         * 对应于自动的寻找路径
         * FileInputFormat.addInputPath(job,new Path(args[0]));
         * FileOutputFormat.setOutputPath(job,new Path(args[1]));
         * *//*

        job.waitForCompletion(true);

        //打印相关信息
        System.out.println("任务名称: "+job.getJobName());
        System.out.println("任务成功: "+(job.isSuccessful()?"Yes":"No"));
    }
}
*/


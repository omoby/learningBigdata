package DT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.chain.ChainReducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;


/**
 * FileName: ChainMapperReducer
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-10 下午2:08
 * Description:
 */
public class ChainMapperReducer {
    //第一个Mapper，过滤出价格小于10000的商品
    public static class  ChainMapper1 extends Mapper<LongWritable, Text, Text, IntWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString().trim();
            if (line.length() >1){
                String[] splited = line.split("\t");
                int price = Integer.valueOf(splited[1]);
                if (price < 10000){
                    context.write(new Text(splited[0].trim()),new IntWritable(price));
                }
            }
        }
    }
    //第二个Mapper过滤出价格大于100的商品
    public static class  ChainMapper2 extends Mapper<Text, IntWritable, Text, IntWritable> {

        @Override
        protected void map(Text key, IntWritable value, Context context) throws IOException, InterruptedException {
            if (value.get() > 100){
                context.write(key,value);
            }
        }
    }

    /**
     * 计算过滤出来的每一种商品的总销售额
     */

    public static class ChainToReducer extends Reducer<Text,IntWritable, Text,IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
         int summary = 0;
         for (IntWritable item :values){
             summary += item.get();
         }
         context.write(key,new IntWritable(summary));

        }
    }

    //第三个Mapper，对Reducer对售额大于5000的商品销进行过滤
    public static class ChainMapper3 extends Mapper<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void map(Text key, IntWritable value, Context context) throws IOException, InterruptedException {
            if (value.get() > 5000){
                context.write(key,value);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();//设置MapReduce的配置
        Path outputPath = new Path(args[1]);
        FileSystem hdfs = outputPath.getFileSystem(conf);
        // 判断路径是否存在，如果存在，则删除
        if (hdfs.isDirectory(outputPath)){
            hdfs.delete(outputPath,true);
        }
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if(otherArgs.length < 2){
            System.out.println("Usage: ChainMapperReducer <in> [<in>...] <out>");
            System.exit(2);
        }
        Job job = Job.getInstance(conf,"ChainMapperReducer");
        //设置主类
        job.setJarByClass(ChainMapperReducer.class);
        //设置处理Mapper
        job.setMapperClass(ChainMapper1.class);
        //设置Reducer
        job.setReducerClass(ChainToReducer.class);
        ChainMapper.addMapper(job,ChainMapper1.class,LongWritable.class,Text.class,Text.class,IntWritable.class,new Configuration());
        ChainMapper.addMapper(job, ChainMapper2.class, Text.class, IntWritable.class, Text.class, IntWritable.class, new Configuration());
         ChainReducer.setReducer(job,ChainToReducer.class, Text.class, IntWritable.class, Text.class, IntWritable.class, new Configuration());
        ChainReducer.addMapper(job, ChainMapper3.class, Text.class, IntWritable.class, Text.class, IntWritable.class, new Configuration());
        //设置map输出的key类型
        job.setMapOutputKeyClass(Text.class);
        //设置map输出的value类型
        job.setMapOutputValueClass(IntWritable.class);
        //设置Reducer输出的Key类型
        job.setOutputKeyClass(Text.class);
        //设置Reducer输出的value类型
        job.setOutputValueClass(IntWritable.class);
        //设定输入路径
        for (int i = 0; i < otherArgs.length-1;++i){
            FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
        }
        //设置输出路径
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length-1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }
}

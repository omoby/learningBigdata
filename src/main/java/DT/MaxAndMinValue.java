package DT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
/**
 * FileName: MaxAndMinValue
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-6 下午5:24
 * Description:
 * 求数据的最大值和最小值
 */
public class MaxAndMinValue {
    /**
     * 使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
     */

    public static class MaxAndMinValueMapper extends Mapper<Object, Text, Text, LongWritable> {
        private LongWritable data = new LongWritable(0);
        private  Text keyForReduce = new Text();
        @Override
        protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            data.set(Long.parseLong(value.toString()));
            context.write(keyForReduce,data);
        }
    }


    /**
     * 使用Reducer将输入的key本身作为key直接输出
     */


    public static class MaxAndMinValueReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        private long maxValue = Long.MIN_VALUE;
        private long minValue = Long.MAX_VALUE;

        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            for (LongWritable item : values){
                if (item.get() > maxValue){
                    maxValue = item.get();
                }
                if (item.get() < minValue){
                    minValue = item.get();
                }
            }
            context.write(new Text("MaxValue"),new LongWritable(maxValue));
            context.write(new Text("MinValue"),new LongWritable(minValue));
        }
    }


    public static void main(String[] args) throws Exception {


        Configuration conf = new Configuration(); //设置MapReduce的配置
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if(otherArgs.length < 2){
            System.out.println("Usage: MaxAndMinValue <in> [<in>...] <out>");
            System.exit(2);
        }

        //设置作业
        //Job job = new Job(conf);
        Job job = Job.getInstance(conf);
        job.setJarByClass(MaxAndMinValue.class);
        job.setJobName("MaxAndMinValue");
        //设置处理map,reduce的类
        job.setMapperClass(MaxAndMinValueMapper.class);
        job.setReducerClass(MaxAndMinValueReducer.class);
        //设置输入输出格式的处理
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        //设定输入输出路径
        for (int i = 0; i < otherArgs.length-1;++i){
            FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length-1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }
}

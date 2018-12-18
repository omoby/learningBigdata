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
 * FileName: DdfferentData
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-6 上午9:34
 * Description:
 * hadoop过滤出不同的数据
 */
public class DdfferentData {
    /**
     * 使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
     */
    public static class forDifferenceMapper extends Mapper<Object, Text, Text, Text> {
        private final IntWritable one = new IntWritable(1);
        private Text mapperValue = new Text(); //存放key的值
        public void map(Object key, Text value, Context context)
                throws IOException, InterruptedException {
            context.write(value,mapperValue);

        }
    }

    /**
     * 使用Reducer将输入的key本身作为key直接输出
     */
    public static class forDifferenceReducer extends Reducer<Text, Text, Text, Text> {
        private Text reduceValue = new Text();
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
           context.write(key,reduceValue);
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration(); //设置MapReduce的配置
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if(otherArgs.length < 2){
            System.out.println("Usage: DeferentData <in> [<in>...] <out>");
            System.exit(2);
        }

        //Job job = new Job(conf);
        Job job = Job.getInstance(conf);
        job.setJarByClass(DdfferentData.class);
        job.setJobName("DeferentData");
        job.setMapperClass(forDifferenceMapper.class);
        job.setCombinerClass(forDifferenceReducer.class);//加速MapReduce并行效率
        job.setReducerClass(forDifferenceReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        for (int i = 0; i < otherArgs.length-1;++i){
            FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length-1]));

        System.exit(job.waitForCompletion(true)?0:1);
    }
}

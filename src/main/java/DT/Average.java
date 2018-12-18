package DT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.util.Iterator;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * FileName: Average
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-6 下午4:39
 * Description:
 */
public class Average {
        /**
         * 使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
         */

        public static class AverageMapper extends Mapper<Object, Text, Text, FloatWritable> {
            @Override
            protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {
                String data = value.toString();
                StringTokenizer splited = new StringTokenizer(data,"\n"); //成绩分隔符
                while (splited.hasMoreElements()){
                    StringTokenizer recored = new StringTokenizer(splited.nextToken());
                    String name = recored.nextToken();
                    String score = recored.nextToken();
                    context.write(new Text(name),new FloatWritable(Float.valueOf(score)));
                    System.out.println("name: "+name +" score: "+score);
                }
            }
        }


        /**
         * 使用Reducer将输入的key本身作为key直接输出
         */


        public static class AverageReducer extends Reducer<Text, FloatWritable, Text, FloatWritable> {
            @Override
            protected void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
                Iterator<FloatWritable> iterator =  values.iterator();
                float sum = 0;
                int count = 0;
                while (iterator.hasNext()){
                    float tmp = iterator.next().get();
                    sum += tmp;
                    count++;
                }
                float average  = sum /count;
                context.write(key,new FloatWritable(average));
                System.out.println("ke: "+key + " ave: "+ average);
            }
        }


        public static void main(String[] args) throws Exception {


            Configuration conf = new Configuration(); //设置MapReduce的配置
            String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
            if(otherArgs.length < 2){
                System.out.println("Usage: SortedData <in> [<in>...] <out>");
                System.exit(2);
            }

            //设置作业
            //Job job = new Job(conf);
            Job job = Job.getInstance(conf);
            job.setJarByClass(Average.class);
            job.setJobName("Average");
            //设置处理map,reduce的类
            job.setMapperClass(AverageMapper.class);
            job.setReducerClass(AverageReducer.class);
            //设置输入输出格式的处理
            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(FloatWritable.class);
            //设定输入输出路径
            for (int i = 0; i < otherArgs.length-1;++i){
                FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
            }
            FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length-1]));
            System.exit(job.waitForCompletion(true)?0:1);
        }



}

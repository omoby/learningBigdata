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
 * FileName: URLLog
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-6 下午10:23
 * Description:
 * 不同访问方式，每种URL访问多少次
 */
public class URLLog {
    /**
     * 使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
     */
    public static class URLLogMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
        private LongWritable resultValue = new LongWritable(1); //标记每条日志访问方式为一次
        private Text text = new Text(); //存储具体的访问方式
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString(); //将读入的每一行转化为是String
            String result = handleLine(line);//获取具体的访问方式和访问的URl
            if (result != null && result.length() >0){ //如果有记录
                text.set(result); //添加这条记录
                context.write(text,resultValue); //将这条URL写入磁盘(URL,1)
            }
        }

        /**
         * 根据传入的日志，返回URL和访问链接的字符串
         * @param line
         * @return
         */

        private String handleLine(String line) {
            StringBuffer buffer = new StringBuffer();
            if (line.length() > 0 && line.contains("HTTP/1.1")){ //这里只是做简单的过滤
                if (line.contains("GET")){//GET方式
                    buffer.append(line.substring(line.indexOf("GET"),line.indexOf("HTTP/1.1")).trim()); //截取获取方式和资源链接
                }else if (line.contains("POST")){ //POST方式
                    buffer.append(line.substring(line.indexOf("POST"),line.indexOf("HTTP/1.1")).trim()); //截取获取方式和资源链接
                }
            }
            return buffer.toString(); //返回URL和访问链接的字符串
        }


    }


    /**
     * 使用Reducer将输入的key本身作为key直接输出
     */


    public static class URLLogReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        private LongWritable totalResult = new LongWritable(1); //保存相同访问链接出现的总次数
        @Override
        protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
            int total = 0; //统计相同链接的总次数
            for (LongWritable item :values){
                total += item.get();
            }
            totalResult.set(total);
            context.write(key,totalResult); //将统计结果写入磁盘
        }

    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();//设置MapReduce的配置
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if(otherArgs.length < 2){
            System.out.println("Usage: URLLog <in> [<in>...] <out>");
            System.exit(2);
        }

        //设置作业
        //Job job = new Job(conf);
        Job job = Job.getInstance(conf);
        job.setJarByClass(URLLog.class);
        job.setJobName("URLLog");
        //设置处理map,reduce的类
        job.setMapperClass(URLLogMapper.class);
        job.setReducerClass(URLLogReducer.class);
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

package DT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

/**
 * FileName: TemperatureComputation
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-5 下午11:21
 * Description:
 * 通过分析气象的日志数据来具体计算出气象日志的相关统计数据
 *
 * 0067011990999991950051507004888888889999999N9+00001+9999999999999999999999
 * 0067011990999991950051512004888888889999999N9+00221+9999999999999999999999
 * 0067011990999991950051518004888888889999999N9-00111+9999999999999999999999
 * 0067011990999991949032412004888888889999999N9+01111+9999999999999999999999
 * 0067011990999991950032418004888888880500001N9+00001+9999999999999999999999
 * 0067011990999991950051507004888888880500001N9+00781+9999999999999999999999
 */
public class TemperatureComputation {
    public static class TemperatureMapper extends Mapper<LongWritable, Text,Text, IntWritable>{
        private static final int MISSING = 9999;
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String data = value.toString();//获取输入的一行数据
            String year = data.substring(15,19);//获取年,第15-19个字符表示year，例如1950年、1949年等；
            int temperature = 0;
            //第45-50个字符表示的是温度，例如-00111、+00001
            if ('+' == data.charAt(45)){ //截取气温的符号
                temperature = Integer.parseInt(data.substring(46,50)); //气温是正数，不用获得“+”
            }else {
                temperature = Integer.parseInt(data.substring(45,50)); //气温是负数，直接将“-”加入到数据中
            }
            //第50位只能是0、1、4、5、9等几个数字；
            String valueDataFlag = data.substring(50,51);
            if (temperature != MISSING && valueDataFlag.matches("[01459]")){
                context.write(new Text(year),new IntWritable(temperature));
            }
        }
    }
    public static class TemperatureReduce extends Reducer<Text,IntWritable,Text,IntWritable>{
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            //提取最低温度的年份和温度
            int coldesTemperature = Integer.MAX_VALUE;
            for (IntWritable item :values){
                coldesTemperature = Math.min(coldesTemperature,item.get());
            }
            context.write(key,new IntWritable(coldesTemperature));
        }
    }


    public static void main(String[] args) throws Exception{
        if (args.length != 2){
            System.err.println("Usage: TemperatureComputation<input path> <output path>");
            System.exit(-1);
        }
        Configuration conf = new Configuration();
//        Job job = new Job(conf);
        Job job = Job.getInstance(conf);
        job.setJarByClass(TemperatureComputation.class);
        job.setJobName("TemperatureComputation");

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapperClass(TemperatureComputation.TemperatureMapper.class);
        job.setCombinerClass(TemperatureComputation.TemperatureReduce.class);//在本地先进行归并
        job.setReducerClass(TemperatureComputation.TemperatureReduce.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);


    }
}

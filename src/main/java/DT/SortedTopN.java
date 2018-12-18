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
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.IOException;
import java.util.Arrays;


/**
 * FileName: SortedTopN
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-6 下午7:50
 * Description:
 */
public class SortedTopN {
    /**
     * 使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
     */
    public static class SortedTopNMapper extends Mapper<LongWritable, Text, Text, Text> {
        int[] topN;//存放TopN的数组
        int length; //存放TopN的个数
        //初始化
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            length = context.getConfiguration().getInt("topn",5); //获取输入的TopN的长度
            topN = new int[length+1]; //初始化TopN数组，TopN[0]存储当前订单的金额
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
           String[] data = value.toString().split(","); //数据格式为8,1818,9000,20，按“,“进行切分
           if (4 == data.length){ //符合格式的数据才进行处理
               int cost = Integer.valueOf(data[2]); //获取订单的消费金额
               topN[0] = cost; //TopN[0]存储最小的消费金额
               Arrays.sort(topN); //对TopN进行生序排序
           }

        }
         //结束的时候销毁
        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            for (int i = 1; i < length+1;i++){ //统计出每一个Mapper的TopN金额
                context.write(new Text(String.valueOf(topN[i])),new Text(String.valueOf(topN[i])));
            }
        }
    }


    /**
     * 使用Reducer将输入的key本身作为key直接输出
     */


    public static class SortedTopNReducer extends Reducer<Text, Text, Text, Text> {
        int[] topN; //Reducer端的TopN数组，存放最终的TopN
        int length; //最终的Reducer的TopN的长度
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            length = context.getConfiguration().getInt("topn",5); // 接受用户输入的指定TopN长度
            topN = new int[length+1]; //初始化TopN数组，TopN[0]存储当前订单的金额
        }

        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
                topN[0] = Integer.valueOf(key.toString()); //获取每次Mapper进来的TopN，在所有Mapper的TopN中找出最大值的TopN
                Arrays.sort(topN); //对TopN进行升序排序
        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {
            for (int i = length; i  > 0;i--){//截取最大的TopN
                context.write(new Text(String.valueOf(length-i +1)),new Text(String.valueOf(topN[i])));
            }
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();//设置MapReduce的配置
        conf.setInt("topn",3);

        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if(otherArgs.length < 2){
            System.out.println("Usage: SortedTopN <in> [<in>...] <out>");
            System.exit(2);
        }

        //设置作业
        //Job job = new Job(conf);
        Job job = Job.getInstance(conf);
        job.setJarByClass(SortedTopN.class);
        job.setJobName("SortedTopN");
        //设置处理map,reduce的类
        job.setMapperClass(SortedTopNMapper.class);
        job.setReducerClass(SortedTopNReducer.class);
        //设置输入输出格式的处理
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        //设定输入输出路径
        for (int i = 0; i < otherArgs.length-1;++i){
            FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length-1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }

}

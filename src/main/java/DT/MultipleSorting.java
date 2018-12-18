package DT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;

/**
 * FileName: MultipleSorting
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-10 上午8:58
 * Description:
 */
public class MultipleSorting {
    //使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
    public static class MultipleSortingMapper extends Mapper<LongWritable, Text, IntMultiplePair, IntWritable> {
        private IntMultiplePair intMutiplePair = new IntMultiplePair();
        private IntWritable intWritable = new IntWritable(0);

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String data = value.toString();
            String[] splited = data.split(" ");
            intMutiplePair.setFirst(splited[0]);
            intMutiplePair.setSecond(Integer.valueOf(splited[1]));
            intWritable.set(Integer.valueOf(splited[1]));
            context.write(intMutiplePair,intWritable);
        }
    }


    public static class MultipleSortingReducer extends Reducer<IntMultiplePair,IntWritable, Text,Text> {
        @Override
        protected void reduce(IntMultiplePair key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            StringBuffer buffer = new StringBuffer();
            Iterator<IntWritable> iter = values.iterator();
            while (iter.hasNext()){
                buffer.append(iter.next().get()+",");
            }
            int length = buffer.toString().length();
            String result = buffer.toString().substring(0,length-1);
            context.write(new Text(key.getFirst()),new Text(result));
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
            System.out.println("Usage: MultipleSorting <in> [<in>...] <out>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf,"MultipleSorting");
        //设置主类
        job.setJarByClass(MultipleSorting.class);
        //设置处理Mapper
        job.setMapperClass(MultipleSortingMapper.class);
        //设置map输出的key类型
        job.setMapOutputKeyClass(IntMultiplePair.class);
        //设置map输出的value类型
        job.setMapOutputValueClass(IntWritable.class);
        //设置Reducer
        job.setReducerClass(MultipleSortingReducer.class);
        //设置分区
        job.setPartitionerClass(MyMultipleSortingPartitioner.class);
        //设置分组排序
        job.setSortComparatorClass(IntMultipleSortingComparator.class);
        //
        job.setGroupingComparatorClass(GroupingIntMultipleComparator.class);
        //设置Reducer输出的Key类型
        job.setOutputKeyClass(Text.class);
        //设置Reducer输出的value类型
        job.setOutputValueClass(Text.class);
        //设定输入路径
        for (int i = 0; i < otherArgs.length-1;++i){
            FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
        }
        //设置输出路径
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length-1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }
}


class IntMultiplePair implements WritableComparable<IntMultiplePair>{
        private String first;
        private int second;


    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public IntMultiplePair() {
    }

    public IntMultiplePair(String first, int second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(IntMultiplePair intPair) {

        return 0;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.first);
        dataOutput.writeInt(this.second);
    }


    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.first = dataInput.readUTF();
        this.second = dataInput.readInt();
    }

}

class GroupingIntMultipleComparator extends WritableComparator{
    public GroupingIntMultipleComparator() {
        super(IntMultiplePair.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        IntMultiplePair x = (IntMultiplePair)a;
        IntMultiplePair y = (IntMultiplePair)b;
        return x.getFirst().compareTo(y.getFirst());
    }
}
class IntMultipleSortingComparator extends WritableComparator{
    public IntMultipleSortingComparator() {
        super(IntMultiplePair.class,true);
    }

    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        IntMultiplePair x = (IntMultiplePair)a;
        IntMultiplePair y = (IntMultiplePair)b;

        if (!x.getFirst().equals(y.getFirst())){
            return x.getFirst().compareTo(y.getFirst());
        }else {
            return x.getSecond()-y.getSecond();
        }
    }
}
class MyMultipleSortingPartitioner extends Partitioner<IntMultiplePair, IntWritable>{
    @Override
    public int getPartition(IntMultiplePair intPair, IntWritable intWritable, int i) {
        return (intPair.getFirst().hashCode()&Integer.MAX_VALUE) % i;
    }
}


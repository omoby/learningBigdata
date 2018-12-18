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
import java.util.Objects;

/**
 * FileName: SecondarySortMR
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-9 上午10:46
 * Description:
 */
public class SecondarySortMR {
    //使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
    public static class SecondarySortMRMapper extends Mapper<LongWritable, Text, IntPair2, Text> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String  line = value.toString();
            String[] splited = line.split("\t");
            IntPair2 item = new IntPair2(splited[0],splited[1]);
            context.write(item,value);
        }
    }

    //使用Reducer将输入的key本身作为key直接输出


    public static class SecondarySortMRReducer extends Reducer<IntPair2, Text, NullWritable,Text> {
        @Override
        protected void reduce(IntPair2 key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text item :values){
                context.write(NullWritable.get(),item);
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
            System.out.println("Usage: SecondarySortMR <in> [<in>...] <out>");
            System.exit(2);
        }

        Job job = Job.getInstance(conf,"SecondarySortMR");
        //设置主类
        job.setJarByClass(SecondarySortMR.class);
        //设置处理Mapper
        job.setMapperClass(SecondarySortMRMapper.class);
        //设置map输出的key类型
        job.setMapOutputKeyClass(IntPair2.class);
        //设置map输出的value类型
        job.setMapOutputValueClass(Text.class);
        //设置Reducer
        job.setReducerClass(SecondarySortMRReducer.class);
        //设置Reducer输出的Key类型
        job.setOutputKeyClass(NullWritable.class);
        //设置Reducer输出的value类型
        job.setOutputValueClass(Text.class);
        //设置分区数
        job.setPartitionerClass(MyPartitioner.class);
        //设置分组函数
        job.setGroupingComparatorClass(MyGroupComparator.class);
        //设定输入路径
        for (int i = 0; i < otherArgs.length-1;++i){
            FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
        }
        //设置输出路径
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length-1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
class MyGroupComparator extends WritableComparator{
    public MyGroupComparator() {
        super(IntPair2.class,true);
    }
}
class MyPartitioner extends Partitioner<IntPair2,Text>{
    @Override
    public int getPartition(IntPair2 intPair2, Text text, int numPartition) {
        return (intPair2.hashCode()&Integer.MAX_VALUE )% numPartition;
    }
}
/*
class IntPair2 implements WritableComparable<IntPair2>{
    private String first;
    private String second;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public IntPair2() {
    }

    public IntPair2(String first, String second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(IntPair2 intPair) {
        if (this.first.equals(intPair.first)){
            return this.first.compareTo(intPair.first);
        }else {
            if (this.second.equals(intPair.second)){
                return  this.second.compareTo(intPair.second);
            }else {
                return 0;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntPair2 intPair = (IntPair2) o;
        return Objects.equals(first, intPair.first) && Objects.equals(second, intPair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.first);
        dataOutput.writeUTF(this.second);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.first = dataInput.readUTF();
        this.second = dataInput.readUTF();

    }
}*/
class IntPair2 implements WritableComparable<IntPair2>{
    private String first;
    private String second;

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public IntPair2() {
    }

    public IntPair2(String first, String second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public int compareTo(IntPair2 intPair) {
        if (this.first.equals(intPair.first)){
            return this.first.compareTo(intPair.first);
        }else {
            if (this.second.equals(intPair.second)){
                return  this.second.compareTo(intPair.second);
            }else {
                return 0;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntPair2 intPair = (IntPair2) o;
        return Objects.equals(first, intPair.first) && Objects.equals(second, intPair.second);
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(this.first);
        dataOutput.writeUTF(this.second);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.first = dataInput.readUTF();
        this.second = dataInput.readUTF();

    }
}

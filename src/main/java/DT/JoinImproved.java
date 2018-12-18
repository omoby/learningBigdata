package DT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * FileName: JoinImproved
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-7 下午2:37
 * Description:
 */

public class JoinImproved {
    //使用Mapper将数据文件中的数据本身作为Mapper输出的key直接输出
    public static class JoinImprovedMapper extends Mapper<LongWritable, Text, MemberKey, MemberInformation> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String data = value.toString();
            String[] dataSplited = data.split("\t");
            if (dataSplited.length== 2){
                MemberInformation memberInformation = new MemberInformation();
                memberInformation.setAddressNo(dataSplited[0]);
                memberInformation.setAddressName(dataSplited[1]);
                MemberKey memberKey = new MemberKey();
                memberKey.setKeyID(Integer.parseInt(dataSplited[0]));
                memberKey.setFlag(true);
                context.write(memberKey,memberInformation);
            }else {
                MemberInformation memberInformation = new MemberInformation();
                memberInformation.setMemberNo(dataSplited[0]);
                memberInformation.setMemberName(dataSplited[1]);
                memberInformation.setAddressNo(dataSplited[2]);
                MemberKey memberKey = new MemberKey();
                memberKey.setKeyID(Integer.parseInt(dataSplited[2]));
                memberKey.setFlag(false);
                context.write(memberKey,memberInformation);
            }
        }
    }

   //使用Reducer将输入的key本身作为key直接输出


    public static class JoinImprovedReducer extends Reducer<MemberKey, MemberInformation, NullWritable,Text> {
        @Override
        protected void reduce(MemberKey key, Iterable<MemberInformation> values, Context context) throws IOException, InterruptedException {
            MemberInformation memberInformation = null;
            int count = 0;
            for (MemberInformation item :values){
                if (count == 0){
                    memberInformation = new MemberInformation(item);
                    count++;
                }else {
                    item.setAddressName(memberInformation.getAddressName());
                    context.write(NullWritable.get(),new Text(item.toString()));
                }

            }
        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();//设置MapReduce的配置
        String[] otherArgs = new GenericOptionsParser(conf,args).getRemainingArgs();
        if(otherArgs.length < 2){
            System.out.println("Usage: JoinImproved <in> [<in>...] <out>");
            System.exit(2);
        }

        //设置作业
        //Job job = new Job(conf);
        Job job = Job.getInstance(conf);
        job.setJarByClass(JoinImproved.class);
        job.setJobName("JoinImproved");

        job.setGroupingComparatorClass(JoinGroupComparator.class);
        //设置处理map,reduce的类
        job.setMapperClass(JoinImprovedMapper.class);
        job.setMapOutputKeyClass(MemberKey.class);
        job.setMapOutputValueClass(MemberInformation.class);
        //设置输入输出格式的处理
        job.setReducerClass(JoinImprovedReducer.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        //设定输入输出路径
        for (int i = 0; i < otherArgs.length-1;++i){
            FileInputFormat.addInputPath(job,new Path(otherArgs[i]));
        }
        FileOutputFormat.setOutputPath(job, new Path(otherArgs[otherArgs.length-1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }
}
class JoinGroupComparator extends WritableComparator{
    protected JoinGroupComparator() {
        super(MemberKey.class, true);
    }
    //两个MemberKey进行比较排序
    @Override
    public int compare(WritableComparable a, WritableComparable b) {
        MemberKey a1=(MemberKey)a;
        MemberKey b1=(MemberKey)b;
        if(a1.getKeyID()==b1.getKeyID()){
            return 0;
        }else{
            return a1.getKeyID()>b1.getKeyID()?1:-1;
        }
    }
}


class MemberKey implements WritableComparable<MemberKey> {
    private int keyID;
    private boolean flag; // true：address false：person

    public MemberKey(int addressNo, boolean flag) {
        super();
        this.keyID = addressNo;
        this.flag = flag;
    }

    public MemberKey() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(keyID);
        dataOutput.writeBoolean(flag);

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.keyID = dataInput.readInt();
        this.flag = dataInput.readBoolean();

    }

    // partitioner执行时调用hashcode()方法和compareTo()方法
    // compareTo()方法作为shuffle排序的默认方法
    @Override
    public int hashCode() {
        return this.keyID; // 按AddreNo进行分组
    }

    //用于排序,将相同的AddressNo的地址表和人员表,将地址表放到首位
    @Override
    public int compareTo(MemberKey o) {
        if (this.keyID == o.getKeyID()) { // 如果是同一个AddressNo的数据则判断是Person还是Address表
            if (this.flag == o.isFlag()) {  //如果属性相同属于同种类型的表,返回0
                return 0;
            } else {
                return this.flag ? -1 : 1; // true表示Address表 返回更小的值,将排至values队首
            }
        } else {
            return this.keyID - o.getKeyID() > 0 ? 1 : -1;  //按AddressNo排序
        }
    }

    public int getKeyID() {
        return keyID;
    }

    public void setKeyID(int keyID) {
        this.keyID = keyID;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}

class MemberInformation implements WritableComparable {
    private String memberNo = "";
    private String memberName = "";
    private String addressNo = "";
    private String addressName = "";

    public MemberInformation(MemberInformation bean) {
        this.memberName = bean.getMemberName();
        this.memberNo = bean.getMemberNo();
        this.addressName = bean.getAddressName();
        this.addressNo = bean.getAddressNo();
    }

    public MemberInformation() {
        super();
        // TODO Auto-generated constructor stub
    }

    public MemberInformation(String numberNo, String numberName, String addressNo,
                             String addressName) {
        super();
        this.memberNo = numberNo;
        this.memberName = numberName;
        this.addressNo = addressNo;
        this.addressName = addressName;
    }


    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(String addressNo) {
        this.addressNo = addressNo;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(memberNo);
        out.writeUTF(memberName);
        out.writeUTF(addressNo);
        out.writeUTF(addressName);

    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.memberNo = in.readUTF();
        this.memberName = in.readUTF();
        this.addressNo = in.readUTF();
        this.addressName = in.readUTF();
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public String toString() {
        return  memberNo + " " + memberName + " " + addressNo + " " + addressName;
    }

}



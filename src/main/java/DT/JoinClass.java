/*
package DT;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

*/
/**
 * FileName: JoinClass
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-7 下午7:20
 * Description:
 *//*

public class JoinClass {
    */
/*
     * map类使key,value分别进行处理
     *//*

    public static class JoinClassMapper extends Mapper<LongWritable, Text, MemberKey, MemberInformation> {
        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {
            String line = value.toString();
            String str[] = line.split("\t");
            if (str.length == 2) {
                // address表
                MemberInformation memberInformation = new MemberInformation();
                memberInformation.setAddressNo(str[0]);
                memberInformation.setAddressName(str[1]);

                MemberKey AddreKey = new MemberKey();
                AddreKey.setKeyID(Integer.parseInt(str[0]));
                AddreKey.setFlag(true); // true表示地区表
                context.write(AddreKey, memberInformation);
            } else {
                // number表
                MemberInformation number = new MemberInformation();
                number.setMemberNo(str[0]);
                number.setMemberName(str[1]);
                number.setAddressNo(str[2]);

                MemberKey PerKey = new MemberKey();
                PerKey.setKeyID(Integer.parseInt(str[2]));
                PerKey.setFlag(false);// false表示人员表
                context.write(PerKey, number);

            }
        }

    }

    public static class JoinClassReducer extends Reducer<MemberKey, MemberInformation, NullWritable, Text> {
        @Override
        protected void reduce(MemberKey key, Iterable<MemberInformation> values, Context context)
                throws IOException, InterruptedException {
            MemberInformation information = null;
            int count = 0;
            for (MemberInformation item : values) {
                if (count == 0) {
                    information = new MemberInformation(item); // Address地址表为values的第一个值
                    count++;
                } else {
                    // 其余全为person表
                    // 没有list数组,节省大量内存空间
                    item.setAddressName(information.getAddressName());
                    context.write(NullWritable.get(), new Text(item.toString()));
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = new Job(conf);
        job.setJarByClass(JoinClass.class);

        //设置自定义的group
        job.setGroupingComparatorClass(JoinGroupComparator.class);

        job.setMapperClass(JoinClassMapper.class);
        job.setMapOutputKeyClass(MemberKey.class);
        job.setMapOutputValueClass(MemberInformation.class);

        job.setReducerClass(JoinClassReducer.class);
        job.setOutputKeyClass(NullWritable.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.waitForCompletion(true);
    }



}

*/
/*
 * 实现Group分组
 * shuffle的group过程默认的是使用的key(MemberKey)的compareTo()方法
 * 刚才我们添加的自定义的Key没有办法将具有相同AddressNo的地址和人员放到同一个group中(因为从compareTo()方法中可以看出他们是不相等的)
 * 我们需要的就是自己定义一个groupComparer就可以
 * 实现比较器
 *//*

class JoinGroupComparator extends WritableComparator{

    protected JoinGroupComparator() {
        super(MemberKey.class, true);
    }
    //两个BeanKey进行比较排序
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



*/
/*
 * map输出的key
 *//*

class MemberKey implements WritableComparable<MemberKey> {
    private int keyID;
    private boolean flag; // true：address false：person

    public MemberKey(int addreNo, boolean isPrimary) {
        super();
        this.keyID = addreNo;
        this.flag = isPrimary;
    }

    public MemberKey() {
        super();
        // TODO Auto-generated constructor stub
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(keyID);
        out.writeBoolean(flag);

    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.keyID = in.readInt();
        this.flag = in.readBoolean();

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
*/
/*
 * 人员和地址的通用bean
 * 用作map输出的value
 *//*

class MemberInformation implements WritableComparable {
    private String numberNo = " ";
    private String numberName = " ";
    private String addressNo = " ";
    private String addressName = " ";

    public MemberInformation(MemberInformation bean) {
        this.numberName = bean.getMemberName();
        this.numberNo = bean.getMemberNo();
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
        this.numberNo = numberNo;
        this.numberName = numberName;
        this.addressNo = addressNo;
        this.addressName = addressName;
    }


    public String getMemberNo() {
        return numberNo;
    }

    public void setMemberNo(String numberNo) {
        this.numberNo = numberNo;
    }

    public String getMemberName() {
        return numberName;
    }

    public void setMemberName(String numberName) {
        this.numberName = numberName;
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
        out.writeUTF(numberNo);
        out.writeUTF(numberName);
        out.writeUTF(addressNo);
        out.writeUTF(addressName);

    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.numberNo = in.readUTF();
        this.numberName = in.readUTF();
        this.addressNo = in.readUTF();
        this.addressName = in.readUTF();
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public String toString() {
        return  numberNo + " " + numberName + " " + addressNo + " " + addressName;
    }

}
*/

package JVM;

/**
 * FileName: JVM
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-28 上午11:09
 * Description:
 */

/**
 * 从JVM调用的角度分析Java程序对内存空间的使用
 * 当JVM进程启动的时候,会从类加载路径中找到包含main
 * 方法的入口类jvm,找到JVM后会直接读取该文件中的二进制
 * 数据并且把该类的信息放到运行的Method内存区域中;
 * 然后会定位代JVM中的main方法的字节码并开始执行main方
 * 法中的指令; Student student = new Student("ombey");
 * 此时会创建Student实例并且使用student来应用该对象(或者说对该对象命名)
 * 其内幕如下:
 * 第一步:JVM会直接代Method区域中区查找Student类的信息,此时发现灭有Student类,
 * 就通过类加载器加载该Student类文件;
 * 第二步:在JVM的Method区域加载到了Student类之后会在Heap区中为Stdudent实例对象
 * 分配内存并且在Student实例对象中持有指向方法区域中的Student类的应用(内存地址)
 * 第三步:JVM实例完后在当前线程中的Stack中的reference建立实际的应用对象关系,此时会赋值给Student
 *
 * 在JVM中方法调用一定是线程的行为,也就是说方法调用本身会在调用线程的方法调用栈;
 * 线程的方法调用栈(Method Stack Frames)每一个方法调用就是方法调用栈中的一个Frame,
 * 该frame包含了方法的参数,局部变量,临时数据等
 */
public class JVM {
    /**
     * 在JVm运行的时候会通过反射到Method区域找到入口类的入口方法main
     * @param args
     */
    //main方法放在Method方法区中的
    public static void main(String[] args){
        /**
         * student是放在主线程的Stack区域中的
         * Student对象实例是放在所有线程共享的Heap区域中的
         */
        Student student = new Student("ombey");
        /**
         * 首先会通过studen指针(句柄)找Student对象,当找
         * 到该对象后会通过对象内部区域中的指针来调用具体的
         * 方法去执行任务
         */
        student.sayHello();
    }
}
class Student{
    //name本身作为成员是放在Stack区域的,但是name指向的String对象是放在堆(Heap)中的
    private String name;
    //构造函数
    public Student(String name){
        this.name = name;
    }
    //sayHello这个方法是放在方法区域中的
    public void sayHello(){
        System.out.println("Hello,This is "+ this.name);
    }
}

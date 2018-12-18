package JVM;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName: HeapOutOfMemory
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-27 下午9:45
 * Description:一般情况下，heap中只存在对象
 */
class Person{}

public class HeapOutOfMemory {
    public static void main(String[] args){
        System.out.println("HeapOutOfMemory");
        List<Person> persons = new ArrayList<Person>();
        int counter = 0;
        while (true){
            persons.add(new Person());
            System.out.println("Instance: "+ (++counter));
        }
    }
}

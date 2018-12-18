package DT;

import java.util.*;

/**
 * FileName: JavaMap
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-3 下午9:07
 * Description:
 */
public class JavaMap {

    public static void main(String[] args){
       // mapOps();
        //treeMapOps();
        //weakHashMap();
        //indentiyHashMapOps();
        //sortedHashMapOps();
        foreachHashMapOps();

    }

    /**
     * EnterySet--返回此映射中包含的映射关系的 Set 视图；
     */
    private static void foreachHashMapOps() {
        SortedMap<String,Integer> data = null;
        data = new TreeMap<String,Integer>();
        data.put("Hadoop",10);
        data.put("Spark",6);
        data.put("Scala",14);

        data.put("Tachyon",5);
        data.put("Kafka",4);
        for (Map.Entry<String,Integer> item :data.entrySet()){
            System.out.println(item.getKey() +" -> "+item.getValue());
        }

    }

    /**
     * SortedMap--该映射是根据其键的自然顺序进行排序的，或者根据通常在创建有序映射时提供的 Comparator 进行排序
     */
    private static void sortedHashMapOps() {
        SortedMap<String,Integer> data = null;
        data = new TreeMap<String,Integer>();
        data.put("Hadoop",10);
        data.put("Spark",6);
        data.put("Scala",14);

        data.put("Tachyon",5);
        data.put("Kafka",4);
        System.out.println("First key： "+data.firstKey());
        System.out.println("First Value： "+data.get(data.firstKey()));
        System.out.println("Last key： "+data.lastKey());
        System.out.println("Last value： "+data.get(data.lastKey()));
        System.out.println(((TreeMap<String, Integer>) data).headMap("Kafka"));
        System.out.println(((TreeMap<String, Integer>) data).tailMap("Spark"));
        System.out.println(((TreeMap<String, Integer>) data).subMap("Hadoop","Spark"));

    }

    /**
     * IdentityHashMap--此类利用哈希表实现 Map 接口，比较键（和值）时使用引用相等性代替对象相等性。
     */
    private static void indentiyHashMapOps() {
        Map<Coder,String> data = null;
        data = new IdentityHashMap<>();
        data.put(new Coder("Spark",6),"Spark6");
        data.put(new Coder("Spark",6),"Spark66");
        data.put(new Coder("Hadoop",10),"Hadoop10");
        Set<Map.Entry<Coder,String>> entrySet = null;
        entrySet = data.entrySet();
        Iterator<Map.Entry<Coder,String>> iterator = null;
        iterator = entrySet.iterator();
        while (iterator.hasNext()){
            Map.Entry<Coder, String> item = iterator.next();
            System.out.println(item.getKey()+" -> "+item.getValue());
        }

    }

    /**
     * 以弱键实现的基于哈希表的 Map。在 WeakHashMap 中，当某个键不再正常使用时，
     * 将自动移除其条目。更精确地说，对于一个给定的键，其映射的存在并不阻止垃圾回收
     * 器对该键的丢弃，这就使该键成为可终止的，被终止，然后被回收
     */

    private static void weakHashMap() {
        Map<String,Integer> data = null;
        data = new WeakHashMap<>();
        data.put(new String("Hadoop"),10);
        data.put(new String("Spark"),6);
        data.put(new String("Scala"),14);
        data.put(new String("Kafka"),4);
        data.put(new String("Tachyon"),5);
        System.gc();
        System.out.println(data);

    }

    /**
     * TreeMap
     *
     */

    private static void treeMapOps() {
        Map<String,Integer> data = null;
        data = new TreeMap<String,Integer>();
        data.put("Hadoop",10);
        data.put("Spark",6);
        data.put("Scala",14);
        data.put("Kafka",4);
        data.put("Tachyon",5);
        System.out.println(data.get("Spark"));//根据key获取value
        if (data.containsKey("Hadoop")){//判断key是否存在
            System.out.println(data.get("Hadoop"));
        }
        if (data.containsValue(14)){ //判断key是否存在
            System.out.println("Spark is here!");
        }
        //获取key的集合
        Set<String> keys = data.keySet();
        Iterator<String> keyIterator=keys.iterator();
        while (((Iterator) keyIterator).hasNext()){
            System.out.println(((Iterator) keyIterator).next());
        }
        //获取values的集合
        Collection<Integer> values = data.values();
        Iterator<Integer> valuesIerator = values.iterator();
        while (valuesIerator.hasNext()){
            System.out.println(valuesIerator.next());
        }
    }

    /**
     *  Map--将键映射到值的对象。一个映射不能包含重复的键；每个键最多只能映射到一个值。
     * 	HashMap---无序：基于哈希表的 Map 接口的实现。此实现提供所有可选的映射操作，并允许使用 null 值和 null 键。
     */
    private static void mapOps() {
        Map<String,Integer> data = null;
        data = new HashMap<String,Integer>();
        data.put("Hadoop",10);
        data.put("Spark",6);
        data.put("Scala",14);
        data.put("Kafka",4);
        data.put("Tachyon",5);
        System.out.println(data.get("Spark"));//根据key获取value
        if (data.containsKey("Hadoop")){//判断key是否存在
            System.out.println(data.get("Hadoop"));
        }
        if (data.containsValue(14)){ //判断key是否存在
            System.out.println("Spark is here!");
        }
        //获取key的集合
        Set<String> keys = data.keySet();
        Iterator<String> keyIterator=keys.iterator();
        while (((Iterator) keyIterator).hasNext()){
            System.out.println(((Iterator) keyIterator).next());
        }
        //获取values的集合
        Collection<Integer> values = data.values();
        Iterator<Integer> valuesIerator = values.iterator();
        while (valuesIerator.hasNext()){
            System.out.println(valuesIerator.next());
        }
    }
}
class Coder {
    private String name;
    private  int age;

    public Coder(String name, int i) {
        this.name  = name;
        this.age = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coder person = (Coder) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    @Override
    public String toString() {
        return "Coder{" + "name='" + name + '\'' + ", age=" + age + '}';
    }
}
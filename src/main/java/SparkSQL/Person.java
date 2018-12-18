package SparkSQL;

import java.io.Serializable;

/**
 * FileName: Person
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-28 下午4:27
 * Description:
 */
public class Person implements Serializable {

        private int id;
        private String name;
        private int age;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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
        public String toString() {
            return "Person{" + "id=" + id + ", name='" + name + '\'' + ", age=" + age + '}';
        }

}

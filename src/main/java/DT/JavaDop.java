package DT;

/**
 * FileName: JavaDop
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-10-1 下午5:28
 * Description:
 */
public class JavaDop {
    public static void main(String[] args){
        /*Student s = new Student();
        s.setName("Spark");
        s.setAge(6);
        s.setSchool("XMU");
        System.out.println("name :"+ s.getName() + " "+ "Age ；"+ s.getAge()+" School :"+ s.getSchool());
        s.print();*/
        Dream bigData = new BigData();
        bigData.toDo();
    }
}
class Person{
    private String name;
    private int age;
    protected void print(){
        System.out.println("Student: sayHello");
    }

    public Person(){
        System.out.println("Person's constructs!");
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

}

class Student extends Person{
    private String school;
    public Student(){
        System.out.println("Student's constructs!");

    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }
}
abstract  class Aninal{
    private String name;
    public abstract void act();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
class Cat extends Aninal{
    @Override
    public void act(){
        System.out.println("It's show time!");
    }
}
 interface Dream{
    public String ID  = "001";
    public void toDo();
 }
 class BigData implements Dream{
    @Override
     public void toDo(){
        System.out.println("Life is short,you need Spark!");
    }
 }
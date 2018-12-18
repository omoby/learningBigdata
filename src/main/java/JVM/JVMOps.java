package JVM;

/**
 * FileName: JVMOps
 * Author:   hadoop
 * Email:    3165845957@qq.com
 * Date:     18-11-2 下午5:34
 * Description:
 */
public class JVMOps {
    /**
     * 在JVM运行的时候会通过反射的方式到Method区域再带入口类的入口函数
     * @param args
     */
    public static void main(String[] args){
        Worker worker = new Worker();
        while (true){
            worker.useWorker();
        }

    }
}

class Worker{
    public Worker worker;

    public Worker getWorker() {
        return null == worker?new Worker(): worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }
    public void useWorker(){
        Worker obj = getWorker();
    }
    public void useWorker2(){
        Worker obj = new Worker();
    }
}

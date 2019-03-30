
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Log {

    private BlockingQueue<String> queue;

    public Log() {
        this.queue = new ArrayBlockingQueue<>(1000);
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }

    public void add(String e) throws InterruptedException {
        this.queue.offer(e, 5, TimeUnit.SECONDS);
    }

    public String get() throws InterruptedException {
        return this.queue.take();
    }
    
    public void setQueue(BlockingQueue<String> queue) {
        this.queue = queue;
    }

}

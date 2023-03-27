import java.util.ArrayDeque;
import java.util.Queue;

public class ProducerConsumer {
    private static int BUFFER_SIZE = 5;
    private Queue<Integer> buffer = new ArrayDeque<Integer>();
    private static int NUMBER_OF_ITEMS = 20;

    public ProducerConsumer() {
    }

    public void start() throws InterruptedException {
        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
                        if (i > NUMBER_OF_ITEMS / 2) {
                            produce(i, 2000);
                        } else {
                            produce(i, 100);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
                        if (i > NUMBER_OF_ITEMS / 2) {
                            consume(100);
                        } else {
                            consume(2000);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }

    private void produce(int item, int millisToSleep) throws InterruptedException {
        synchronized (buffer) {
            while (buffer.size() == BUFFER_SIZE) {
                buffer.wait();
            }
            buffer.add(item);
            System.out.println("Produced number " + item);
            buffer.notify();
        }
        Thread.sleep(millisToSleep);
    }

    private void consume(int millisToSleep) throws InterruptedException {
        synchronized (buffer) {
            while (buffer.size() == 0) {
                buffer.wait();
            }
            int item = buffer.poll();
            System.out.println("Consumed number " + item);
            buffer.notify();
        }
        Thread.sleep(millisToSleep);
    }
}

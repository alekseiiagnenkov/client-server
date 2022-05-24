import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TestClass {

    public static final ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();
    public static final ConcurrentLinkedQueue<String> result = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(new Runnable() {
                boolean run = true;

                @Override
                public void run() {
                    while (run)
                        try {
                            if(!queue.isEmpty()) {
                                String something = queue.remove();
                                if (something != null) {
                                    System.out.println(id + "---> " + Thread.currentThread().getName() + " [" + LocalTime.now() + "] " + something);
                                    result.add(something);
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    Thread.currentThread().interrupt();
                }


            }).start();
        }
        while (true) {

            queue.clear();
            result.clear();

            for (int i = 0; i < 10; i++) {
                //System.out.println(LocalTime.now() + " " +"hello" + i);
                queue.add("hello" + i);
            }

            new Scanner(System.in).nextInt();

            Thread.sleep(1000);
            System.out.println(LocalTime.now() + " " + Arrays.toString(result.toArray()));
            System.out.println(LocalTime.now() + " " + Arrays.toString(queue.toArray()));
            System.out.println("--------------------------------------------------------");
/*        Handler handler = new Handler();
        handler.run();*/
        }
    }
}


import java.util.concurrent.*;

class Foo {
    private final CountDownLatch cdl = new CountDownLatch(1);
    private final CountDownLatch cdl2 = new CountDownLatch(1);

    public void first(Runnable r) {
        System.out.print("First");
        cdl.countDown();
    }

    public void second(Runnable r) {
        try {
            cdl.await();
            System.out.print("Second");
            cdl2.countDown();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public void third(Runnable r) {
        try {
            cdl2.await();
            System.out.print("Third");
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }
}

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Foo foo = new Foo();

        CompletableFuture.runAsync(() -> {
            foo.first(new Thread());
        });

        CompletableFuture.runAsync(() -> {
            foo.third(new Thread());
        });

        CompletableFuture.runAsync(() -> {
            foo.second(new Thread());
        });

        Thread.sleep(10);
    }
}


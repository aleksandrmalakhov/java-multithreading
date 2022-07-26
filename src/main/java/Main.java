import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Main {
    private static final char[] LETTERS = {'У', 'К', 'Е', 'Н', 'Х', 'В', 'А', 'Р', 'О', 'С', 'М', 'Т'};

    public static void main(String[] args) throws InterruptedException, IOException {
        System.out.println("START - " + LocalTime.now());

        ExecutorService service = Executors.newCachedThreadPool();
        CarNumberCreator creator = new CarNumberCreator();

        int maxRegionCode = 99;
        int countThread = Runtime.getRuntime().availableProcessors();
        long startTime = System.currentTimeMillis();

        Map<Integer, Integer> startStop = mapStartStop(countThread, maxRegionCode);
        startStop.forEach((key, value) ->
                service.execute(() ->
                        creator.create(Thread.currentThread().getName(), key, value, LETTERS)));

        service.shutdown();

        boolean end = service.awaitTermination(60, TimeUnit.SECONDS);
        if (end) {
            try (Stream<Path> stream = Files.list(Paths.get("src/main/resources"))) {
                var count = stream.count();

                System.out.println("Durations - " + (System.currentTimeMillis() - startTime) + " ms");
                Assert.assertEquals(countThread, count);
            }
        }

        System.out.println("END - " + LocalTime.now());
    }

    private static Map<Integer, Integer> mapStartStop(int countThread, int maxRegionCode) {
        Map<Integer, Integer> map = new LinkedHashMap<>();

        int countRegionsInThread = 100 / countThread;
        int start = 1;
        int stop;

        for (int i = 1; i <= countThread; i++) {
            if (i == countThread) {
                stop = maxRegionCode;
            } else {
                stop = i * countRegionsInThread;
            }
            map.put(start, stop);
            start = ++stop;
        }
        return map;
    }
}
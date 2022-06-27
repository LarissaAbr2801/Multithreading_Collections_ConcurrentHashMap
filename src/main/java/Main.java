import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static final int ARRAY_LENGTH_FOR_PREFILLING = 1000;
    static final int ARRAY_LENGTH = 99_999_000;
    static final int ARRAY_ORIGIN = 0;
    static final int ARRAY_BOUND = 100;

    static long timeForAdding;
    static long timeForReading;

    static final int THREADS_QUANTITY = 4;

    public static void main(String[] args) {
        Map<Integer, Integer> hashMap = Collections.synchronizedMap(new HashMap<>());

        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();

        int[] array = generatingIntegerArray(ARRAY_LENGTH, ARRAY_ORIGIN, ARRAY_BOUND);

        //предзаполнение 1000 элементами
        add(generatingIntegerArray(ARRAY_LENGTH_FOR_PREFILLING, ARRAY_ORIGIN, ARRAY_BOUND), hashMap);
        add(generatingIntegerArray(ARRAY_LENGTH_FOR_PREFILLING, ARRAY_ORIGIN, ARRAY_BOUND),
                concurrentHashMap);

        ExecutorService service = Executors.newFixedThreadPool(THREADS_QUANTITY);

        for (int i = 0; i < THREADS_QUANTITY; i++) {
            service.submit(() -> add(array, hashMap));
        }
        System.out.println("Затраченное время на добавление элементов в обертку: " + timeForAdding);

        //сброс значения поля для подсчета времени для concurrent
        timeForAdding = 0;

        for (int i = 0; i < THREADS_QUANTITY; i++) {
            service.submit(() -> add(array, concurrentHashMap));
        }
        System.out.println("Затраченное время на добавление элементов в concurrent: " + timeForAdding);

        for (int i = 0; i < THREADS_QUANTITY; i++) {
            service.submit(() -> read(array, hashMap));
        }
        System.out.println("Затраченное время на чтение элементов из обертку: " + timeForReading);

        //сброс значения поля для подсчета времени для concurrent
        timeForReading = 0;

        for (int i = 0; i < THREADS_QUANTITY; i++) {
            service.submit(() -> read(array, concurrentHashMap));
        }
        System.out.println("Затраченное время на чтение элементов из concurrent: " + timeForReading);

        service.shutdown();
    }

    public static int[] generatingIntegerArray(int length, int origin, int bound) {
        return new Random().ints(length, origin, bound).toArray();
    }

    public static void add(int[] array, Map<Integer, Integer> hashMap) {
        for (int i = 0; i < array.length; i++) {
            long startTimeAddToHash = System.currentTimeMillis();
            hashMap.put(i, i);
            long endTimeAddToHash = System.currentTimeMillis();
            timeForAdding += endTimeAddToHash - startTimeAddToHash;
        }
    }

    public static void read(int[] array, Map<Integer, Integer> hashMap) {
        for (int i = 0; i < array.length; i++) {
            long startTimeAddToHash = System.currentTimeMillis();
            hashMap.get(i);
            long endTimeAddToHash = System.currentTimeMillis();
            timeForReading += endTimeAddToHash - startTimeAddToHash;
        }
    }
}


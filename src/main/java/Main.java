import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    static final int ARRAY_LENGTH_FOR_PREFILLING = 1000;
    static final int ARRAY_LENGTH = 99_999_000;
    static final int ARRAY_ORIGIN = 0;
    static final int ARRAY_BOUND = 100;

    static final int THREADS_QUANTITY = 6;

    public static void main(String[] args) {
        Map<Integer, Integer> hashMap = Collections.synchronizedMap(new HashMap<>());

        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();

        int[] array = generatingIntegerArray(ARRAY_LENGTH, ARRAY_ORIGIN, ARRAY_BOUND);

        //предзаполнение 1000 элементами
        add(generatingIntegerArray(ARRAY_LENGTH_FOR_PREFILLING, ARRAY_ORIGIN, ARRAY_BOUND), hashMap);
        add(generatingIntegerArray(ARRAY_LENGTH_FOR_PREFILLING, ARRAY_ORIGIN, ARRAY_BOUND),
                concurrentHashMap);

        ExecutorService service = Executors.newFixedThreadPool(THREADS_QUANTITY);

        countTimeForAdding(array, hashMap, service, "добавление элементов в обертку:");
        countTimeForAdding(array, concurrentHashMap, service, "добавление элементов в concurrent:");

        countTimeForReading(array, hashMap, service, "чтение элементов из обертки:");
        countTimeForReading(array, concurrentHashMap, service, "чтение элементов из concurrent:");

        service.shutdown();
    }

    public static int[] generatingIntegerArray(int length, int origin, int bound) {
        return new Random().ints(length, origin, bound).toArray();
    }

    public static void add(int[] array, Map<Integer, Integer> hashMap) {
        for (int i = 0; i < array.length; i++) {
            hashMap.put(i, i);
        }
    }

    public static void read(int[] array, Map<Integer, Integer> hashMap) {
        for (int i = 0; i < array.length; i++) {
            hashMap.get(i);
        }
    }

    public static void countTimeForAdding(int[] array, Map<Integer, Integer> map,
                                          ExecutorService service, String str) {
        long startTimeAddToHash = System.currentTimeMillis();
        for (int i = 0; i < THREADS_QUANTITY; i++) {
            service.submit(() -> add(array, map));
        }
        long endTimeAddToHash = System.currentTimeMillis();
        System.out.println("Затраченное время на " + str
                + (endTimeAddToHash - startTimeAddToHash));
    }

    public static void countTimeForReading(int[] array, Map<Integer, Integer> map,
                                           ExecutorService service, String str) {
        long startTimeReadFromHash = System.currentTimeMillis();
        for (int i = 0; i < THREADS_QUANTITY; i++) {
            service.submit(() -> read(array, map));
        }
        long endTimeReadFromHash = System.currentTimeMillis();
        System.out.println("Затраченное время на " + str
                + (endTimeReadFromHash - startTimeReadFromHash));

    }
}


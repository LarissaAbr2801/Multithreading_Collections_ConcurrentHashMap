import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    static final int ARRAY_LENGTH_FOR_PREFILLING = 1000;
    static final int ARRAY_LENGTH = 9_999_000;
    static final int ARRAY_ORIGIN = 0;
    static final int ARRAY_BOUND = 100;

    static final int THREADS_QUANTITY = 4;

    public static void main(String[] args) {
        Map<Integer, Integer> hashMap = Collections.synchronizedMap(new HashMap<>());

        Map<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();

        int[] array = generatingIntegerArray(ARRAY_LENGTH, ARRAY_ORIGIN, ARRAY_BOUND);

        //предзаполнение 1000 элементами
        add(generatingIntegerArray(ARRAY_LENGTH_FOR_PREFILLING, ARRAY_ORIGIN, ARRAY_BOUND), hashMap);
        add(generatingIntegerArray(ARRAY_LENGTH_FOR_PREFILLING, ARRAY_ORIGIN, ARRAY_BOUND),
                concurrentHashMap);

        System.out.println("Затраченное время на добавление элементов в hashMap: "
                + countTimeForAdding(array, hashMap));
        System.out.println("Затраченное время на добавление элементов в concurrent: "
                + countTimeForAdding(array, concurrentHashMap));
        System.out.println("Затраченное время на чтение элементов из hashMap: "
                + countTimeForReading(array, hashMap));
        System.out.println("Затраченное время на чтение элементов из concurrent: "
                + countTimeForReading(array, concurrentHashMap));

        System.out.println(hashMap.get(9000));
        System.out.println(concurrentHashMap.get(9000));
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

    public static long countTimeForAdding(int[] array, Map<Integer, Integer> hashMap) {
        long startTimeToAdd = System.currentTimeMillis();

        for (int i = 0; i < THREADS_QUANTITY; i++) {
            new Thread(() -> add(array, hashMap)).start();
        }

        long endTimeToAdd = System.currentTimeMillis();

        return endTimeToAdd - startTimeToAdd;
    }

    public static long countTimeForReading(int[] array, Map<Integer, Integer> hashMap) {
        long startTimeToRead = System.currentTimeMillis();

        for (int i = 0; i < THREADS_QUANTITY; i++) {
            new Thread(() -> read(array, hashMap)).start();
        }

        long endTimeToRead = System.currentTimeMillis();

        return endTimeToRead - startTimeToRead;
    }
}


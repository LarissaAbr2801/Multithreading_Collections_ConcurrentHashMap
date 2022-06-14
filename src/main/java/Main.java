 import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
 public class Main {

     static final int ARRAY_LENGTH = 100_000;
     static final int ARRAY_ORIGIN = 0;
     static final int ARRAY_BOUND = 100;

    public static void main(String[] args) {
        Map<Integer, Integer> hashMap = Collections.synchronizedMap(new HashMap<>());

        ConcurrentHashMap<Integer, Integer> concurrentHashMap = new ConcurrentHashMap<>();

        int[] array = generatingIntegerArray(ARRAY_LENGTH, ARRAY_ORIGIN, ARRAY_BOUND);

        long startTimeAddToHash = System.currentTimeMillis();
        new Thread(() -> add(array, hashMap)).start();
        //new Thread(() -> add(array, hashMap)).start();
        long endTimeAddToHash = System.currentTimeMillis();
        System.out.println("Затраченное время на добавление элементов в обертку:"
                + (endTimeAddToHash - startTimeAddToHash));

        long startTimeAddToConcurrent = System.currentTimeMillis();
        new Thread(() -> add(array, concurrentHashMap)).start();
        //new Thread(() -> add(array, concurrentHashMap)).start();
        long endTimeAddToConcurrent = System.currentTimeMillis();
        System.out.println("Затраченное время на добавление элементов в concurrent:"
                + (endTimeAddToConcurrent - startTimeAddToConcurrent));

        long startTimeReadFromHash = System.currentTimeMillis();
        new Thread(() -> read(array, hashMap)).start();
        new Thread(() -> read(array, hashMap)).start();
        long endTimeReadFromHash = System.currentTimeMillis();
        System.out.println("Затраченное время на чтение элементов из обертки:"
                + (endTimeReadFromHash - startTimeReadFromHash));

        long startTimeReadFromConcurrent = System.currentTimeMillis();
        new Thread(() -> read(array, concurrentHashMap)).start();
        new Thread(() -> read(array, concurrentHashMap)).start();
        long endTimeReadFromConcurrent = System.currentTimeMillis();
        System.out.println("Затраченное время на чтение элементов из concurrent:"
                + (endTimeReadFromConcurrent - startTimeReadFromConcurrent));


        System.out.println(hashMap.get(1));
        System.out.println(concurrentHashMap.get(1));
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
}


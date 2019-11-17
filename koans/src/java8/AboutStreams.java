package java8;

import com.sandwich.koan.Koan;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.sandwich.koan.constant.KoanConstants.__;
import static com.sandwich.util.Assert.assertEquals;

public class AboutStreams {

    String str = "";

    List<String> places = Arrays.asList("Belgrade", "Zagreb", "Sarajevo", "Skopje", "Ljubljana", "Podgorica");

    @Koan
    public void simpleCount() {
        long count = places.stream().count();
        assertEquals(count, (long) places.size());
    }

    @Koan
    public void filteredCount() {
        long count = places.stream()
                .filter(s -> s.startsWith("S"))
                .count();
        assertEquals(count, 2L);
    }

    @Koan
    public void max() {
        String longest = places.stream()
                .max(Comparator.comparing(cityName -> cityName.length()))
                .get();
        assertEquals(longest, "Ljubljana");
    }

    @Koan
    public void min() {
        String shortest = places.stream()
                .min(Comparator.comparing(cityName -> cityName.length()))
                .get();
        assertEquals(shortest, "Zagreb");
    }

    @Koan
    public void reduce() {
        String join = places.stream()
                .reduce("", String::concat);
        assertEquals(join, String.join("", places));
    }

    @Koan
    public void reduceWithoutStarterReturnsOptional() {
        Optional<String> join = places.stream()
                .reduce(String::concat);
        assertEquals(join.get(), String.join("", places));
    }

    @Koan
    public void join() {
        String join = places.stream()
                .reduce((accumulated, cityName) -> accumulated + "\", \"" + cityName)
                .get();
        assertEquals(join, String.join("\", \"", places));
    }

    @Koan
    public void stringJoin() {
        String join = places.stream()
                .collect(Collectors.joining("\", \""));
        assertEquals(join, String.join("\", \"", places));
    }

    @Koan
    public void mapReduce() {
        OptionalDouble averageLengthOptional = places.stream()
                .mapToInt(String::length)
                .average();
        double averageLength = Math.round(averageLengthOptional.getAsDouble());
        assertEquals(averageLength, 8.0);
    }

    @Koan
    public void parallelMapReduce() {
        int lengthSum = places.parallelStream()
                .mapToInt(String::length)
                .sum();

        int totalLen = 0;
        for (String p : places) {
            totalLen += p.length();
        }

        assertEquals(lengthSum, totalLen);
    }

    @Koan
    public void limitSkip() {
        int lengthSum_Limit_3_Skip_1 = places.stream()
                .mapToInt(String::length)
                .limit(3)
                .skip(1)
                .sum();
        assertEquals(lengthSum_Limit_3_Skip_1, places.get(1).length() + places.get(2).length());
    }

    @Koan
    public void lazyEvaluation() {
        Stream stream = places.stream()
                .filter(s -> {
                    str = "hello";
                    return s.startsWith("S");
                });
        assertEquals(str, ""); // not terminating so the filter function did not get called
    }

    @Koan
    public void sumRange() {
        int sum = IntStream.range(1, 4).sum(); // start inclusive end exclusive
        // not 1 + 2 + 3 + 4;
        // 1 + 2 + 3;
        assertEquals(sum, 6);
    }

    @Koan
    public void rangeToList() {
        List<Integer> range = IntStream.range(1, 4)
                .boxed()
                .collect(Collectors.toList());
        assertEquals(range, Arrays.asList(1, 2, 3));
    }
}

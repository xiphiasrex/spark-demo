package com.example.spark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;

import scala.Tuple2;

public class SparkApp {

    private static class TupleComparator implements Comparator<Tuple2<Integer, String>>, Serializable {
        @Override
        public int compare(Tuple2<Integer, String> tuple1, Tuple2<Integer, String> tuple2) {
            return tuple1._1 - tuple2._1;
        }
    }

    public static void main(String[] args) {

        // Large text file ~1.6GB
        String dataFile = "/wrk/docker/data/big.txt";

        // Creating a very simple configuration for a Spark context
        SparkConf conf = new SparkConf().setAppName("Spark Demo");
        JavaSparkContext sc = new JavaSparkContext(conf);

        JavaPairRDD<Tuple2<Integer, String>, Integer> countInKey = sc
                .textFile(dataFile)
                .flatMap(str -> Arrays.asList(str.split(" ")).iterator())
                .mapToPair(str -> new Tuple2<>(str, 1))
                .reduceByKey(Integer::sum)
                .mapToPair(a -> new Tuple2<>(new Tuple2<Integer, String>(a._2, a._1), null));

        List<Tuple2<Tuple2<Integer, String>, Integer>> wordSortedByCount = countInKey
                .sortByKey(new TupleComparator(), false)
                .collect();

        List<String> result = new ArrayList<>();
        IntStream.range(0, 5).forEach(i -> result.add(wordSortedByCount.get(i).toString()));

        System.out.println("==============================================");
        for (String s : result) {
            System.out.println(s);
        }
        System.out.println("==============================================");

        sc.stop();
        sc.close();
    }
}

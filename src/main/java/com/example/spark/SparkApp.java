package com.example.spark;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkApp {

    public static void main(String[] args) {

        String dataFile = "/wrk/data/big.txt";
        SparkConf conf = new SparkConf().setAppName("Spark Demo");
        JavaSparkContext sc = new JavaSparkContext(conf);

        getWordCount(sc, dataFile, "the");

        sc.stop();
        sc.close();
    }

    private static void getWordCount(JavaSparkContext sc, String dataFile, String search) {
        long count = sc.textFile(dataFile)
            .flatMap(str -> Arrays.asList(StringUtils.split(str)).iterator())
            .filter(x -> StringUtils.equals(x, search))
            .count();

        System.out.println("==============================================");
        System.out.println(search + ": " + count);
        System.out.println("==============================================");
    }
}

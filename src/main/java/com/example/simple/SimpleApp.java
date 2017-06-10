package com.example.simple;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.lang.time.StopWatch;

public class SimpleApp {

    public static void main(String[] args) {

        StopWatch sw = new StopWatch();
        sw.start();

        int a = 0;
        int b = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("/wrk/docker/data/big.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("a")) {
                    a++;
                }
                if (line.contains("b")) {
                    b++;
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        sw.stop();

        System.out.println("Total time: " + (double) sw.getTime() / 1000);
        System.out.println("==============================================");
        System.out.println("Lines with a: " + a + ", lines with b: " + b);
        System.out.println("==============================================");

    }

}

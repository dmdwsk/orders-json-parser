package com.orders.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orders.entities.Order;
import com.orders.parser.JsonOrderParser;
import com.orders.statistics.StatisticsCalculator;
import com.orders.xml.XmlWriter;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println("Usage: java -jar app.jar <folderPath> <attributeName> [threadCount]");
        }
        String folderPath = args[0];
        String attributeName = args[1];
        int threadCount =(args.length >=3) ? Integer.parseInt(args[2]) : 4;
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonOrderParser parser = new JsonOrderParser(mapper);
            List<Order> orders = parser.parse(folderPath, threadCount);

            StatisticsCalculator calculator = new StatisticsCalculator();
            Map<String, Long> stats = calculator.calculate(orders, attributeName);

            XmlWriter writer = new XmlWriter();
            writer.write(stats, attributeName, folderPath);

            System.out.println("Completed successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

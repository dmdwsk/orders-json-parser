package com.orders.cli.statistics;

import com.orders.cli.entities.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsCalculator {
    public Map<String, Long> calculate(List<Order> orders, String attributeName) {
        Map<String, Long> stats = new HashMap<>();
        if (orders == null || orders.isEmpty()){
            return stats;
        }

        switch (attributeName) {
            case "customerName":
                for(Order order : orders){
                    String value = order.getCustomerName();
                    if(value == null || value.isBlank()){
                        continue;
                    }else {
                        stats.put(value, stats.getOrDefault(value, 0L) + 1);
                    }
                }
                break;

            case "date":
                for(Order order : orders){
                    String value = order.getDate();
                    if(value == null || value.isBlank()){
                        continue;
                    }else {
                        stats.put(value, stats.getOrDefault(value, 0L) + 1);
                    }
                }
                break;

            case "products":
                for (Order order : orders) {
                    String raw = order.getProducts();

                    if (raw == null || raw.isBlank()) {
                        continue;
                    }

                    String[] products = raw.split(",");

                    for (String p : products) {
                        String product = p.trim();

                        if (product.isEmpty()) {
                            continue;
                        }

                        stats.put(product, stats.getOrDefault(product, 0L) + 1);
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown attribute: " + attributeName);
        }
        return stats;
    }
}

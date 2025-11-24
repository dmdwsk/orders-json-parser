import com.orders.entities.Order;
import com.orders.statistics.StatisticsCalculator;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StatisticsCalculatorTest {
    @Test
    void testCountByCustomerName() {
        List<Order> orders = List.of(
                new Order("1", "Alice", "2024-01-01", 100.0, "mouse"),
                new Order("2", "Bob", "2024-01-02", 200.0, "keyboard"),
                new Order("3", "Alice", "2024-01-03", 300.0, "laptop")
        );
        StatisticsCalculator calc = new StatisticsCalculator();
        Map<String, Long> stats = calc.calculate(orders, "customerName");

        assertEquals(2L, stats.get("Alice"));
        assertEquals(1L, stats.get("Bob"));
    }

    @Test
    void testCountByDate() {
        List<Order> orders = List.of(
                new Order("1", "Alice", "2024-01-01", 150.0, "mouse"),
                new Order("2", "Bob", "2024-01-01", 300.0, "keyboard"),
                new Order("3", "Carl", "2024-01-02", 500.0, "monitor")
        );
        StatisticsCalculator calc = new StatisticsCalculator();
        Map<String, Long> stats = calc.calculate(orders, "date");

        assertEquals(2L, stats.get("2024-01-01"));
        assertEquals(1L, stats.get("2024-01-02"));
    }

    @Test
    void testCountByProducts() {
        List<Order> orders = List.of(
                new Order("1", "Alice", "2024-01-01", 100.0, "mouse, keyboard"),
                new Order("2", "Bob", "2024-01-02", 200.0, "mouse"),
                new Order("3", "Carl", "2024-01-03", 300.0, "keyboard, mouse")
        );
        StatisticsCalculator calc = new StatisticsCalculator();
        Map<String, Long> stats = calc.calculate(orders, "products");

        assertEquals(3L, stats.get("mouse"));
        assertEquals(2L, stats.get("keyboard"));

    }

    @Test
    void testUnknownAttributeThrowsException() {
        List<Order> orders = List.of(
                new Order("1", "Alice", "2024-01-01", 100.0, "mouse")
        );

        StatisticsCalculator calc = new StatisticsCalculator();

        assertThrows(IllegalArgumentException.class,
                () -> calc.calculate(orders, "unknownAttribute"));
    }
}

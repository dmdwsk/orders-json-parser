import com.fasterxml.jackson.databind.ObjectMapper;
import com.orders.entities.Order;
import com.orders.parser.FileReaderTask;
import org.junit.jupiter.api.Test;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import static org.junit.jupiter.api.Assertions.*;

public class FileReaderTaskTest {
    @Test
    void testValidJsonReturnsOrder() throws Exception {
        Path temp = Files.createTempFile("order-valid", ".json");

        String json = """
        {
          "orderId": "1",
          "customerName": "Alice",
          "date": "2024-01-01",
          "totalPrice": 100.0,
          "products": "mouse, keyboard"
        }
        """;

        Files.writeString(temp, json);

        FileReaderTask task = new FileReaderTask(temp, new ObjectMapper());
        FutureTask<Order> future = new FutureTask<>(task);
        future.run();

        Order o = future.get();

        assertNotNull(o);
        assertEquals("Alice", o.getCustomerName());
        assertEquals("mouse, keyboard", o.getProducts());
    }
    @Test
    void testInvalidJsonThrowsException() throws Exception {
        Path temp = Files.createTempFile("order-bad", ".json");

        String badJson = """
        { "orderId": 1, BAD JSON }
        """;

        Files.writeString(temp, badJson);

        FileReaderTask task = new FileReaderTask(temp, new ObjectMapper());
        FutureTask<Order> future = new FutureTask<>(task);
        future.run();

        assertThrows(ExecutionException.class, future::get);
    }
    @Test
    void testMissingFileThrowsException() {
        Path missing = Path.of("file_does_not_exist_123.json");

        FileReaderTask task = new FileReaderTask(missing, new ObjectMapper());
        FutureTask<Order> future = new FutureTask<>(task);
        future.run();

        assertThrows(ExecutionException.class, future::get);
    }
}


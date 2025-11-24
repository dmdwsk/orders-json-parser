import com.fasterxml.jackson.databind.ObjectMapper;
import com.orders.entities.Order;
import com.orders.parser.JsonOrderParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonOrderParserTest {

    @Test
    void testParseSingleFile() throws IOException {
        Path tempDir = Files.createTempDirectory("orders-test");
        Path file = tempDir.resolve("order.json");

        String json = """
        {
          "orderId": "1",
          "customerName": "Alice",
          "date": "2024-01-01",
          "totalPrice": 100.0,
          "products": "mouse, keyboard"
        }
        """;

        Files.writeString(file, json);

        JsonOrderParser parser = new JsonOrderParser(new ObjectMapper());
        List<Order> orders = parser.parse(tempDir.toString(), 2);

        assertEquals(1, orders.size());
        assertEquals("Alice", orders.get(0).getCustomerName());
        assertEquals("mouse, keyboard", orders.get(0).getProducts());
    }
    @Test
    void testParseInvalidFolderThrows() {
        JsonOrderParser parser = new JsonOrderParser(new ObjectMapper());

        assertThrows(IllegalArgumentException.class,
                () -> parser.parse("folder-does-not-exist", 2));
    }
    @Test
    void testParseMultipleFiles() throws IOException {
        Path tempDir = Files.createTempDirectory("orders-test2");

        String j1 = """
        {"orderId":"1","customerName":"A","date":"2024-01-01","totalPrice":10,"products":"mouse"}
        """;
        String j2 = """
        {"orderId":"2","customerName":"B","date":"2024-01-02","totalPrice":20,"products":"keyboard"}
        """;

        Files.writeString(tempDir.resolve("o1.json"), j1);
        Files.writeString(tempDir.resolve("o2.json"), j2);

        JsonOrderParser parser = new JsonOrderParser(new ObjectMapper());
        List<Order> orders = parser.parse(tempDir.toString(), 4);

        assertEquals(2, orders.size());
    }
}


import com.orders.xml.XmlWriter;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class XmlWriterTest {

    @Test
    void testXmlWriterCreatesCorrectXml() throws Exception {


        Path tempDir = Files.createTempDirectory("xml-writer-test");

        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("mouse", 3L);
        stats.put("keyboard", 2L);

        XmlWriter writer = new XmlWriter();


        writer.write(stats, "products", tempDir.toString());

        Path xmlFile = tempDir.resolve("statistics_by_products.xml");


        assertTrue(Files.exists(xmlFile), "XML file should be created");


        Document doc = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(Files.newInputStream(xmlFile));

        doc.getDocumentElement().normalize();


        assertEquals("statistics", doc.getDocumentElement().getNodeName());

        NodeList items = doc.getElementsByTagName("item");
        assertEquals(2, items.getLength(), "Should be 2 <item> elements");

        NodeList values = doc.getElementsByTagName("value");
        NodeList counts = doc.getElementsByTagName("count");

        assertEquals("mouse", values.item(0).getTextContent());
        assertEquals("keyboard", values.item(1).getTextContent());

        assertEquals("3", counts.item(0).getTextContent());
        assertEquals("2", counts.item(1).getTextContent());
    }
    @Test
    void testXmlWriterThrowsOnEmptyStats() {
        XmlWriter writer = new XmlWriter();
        Map<String, Long> emptyStats = Map.of();

        assertThrows(IllegalArgumentException.class, () ->
                writer.write(emptyStats, "products", "/tmp")
        );
    }
    @Test
    void testXmlWriterThrowsIfDirectoryDoesNotExist() {
        XmlWriter writer = new XmlWriter();

        Map<String, Long> stats = new LinkedHashMap<>();
        stats.put("mouse", 3L);

        String invalidFolder = "/path/does/not/exist/___123";

        assertThrows(RuntimeException.class, () ->
                writer.write(stats, "products", invalidFolder)
        );
    }
}

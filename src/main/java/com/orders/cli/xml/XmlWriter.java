package com.orders.cli.xml;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class XmlWriter {
    public void write(Map<String, Long> stats, String attributeName, String outputFolder){
        if (stats == null || stats.isEmpty()) {
            throw new IllegalArgumentException("No statistics available to write for attribute: " + attributeName);
        }
        List<Map.Entry<String, Long>> sorted = stats.entrySet().stream()
                .sorted( (a,b) -> Long.compare(b.getValue(), a.getValue()) )
                .toList();
        StringBuilder sb = new StringBuilder();
        sb.append("<statistics>\n");
        for(Map.Entry<String,Long>entry : sorted){
            sb.append("  <item>\n");
            sb.append("    <value>").append(entry.getKey()).append("</value>\n");
            sb.append("    <count>").append(entry.getValue()).append("</count>\n");
            sb.append("  </item>\n");
        }
        sb.append("</statistics>");
        String fileName = "statistics_by_" + attributeName + ".xml";
        Path outputPath = Path.of(outputFolder,fileName);
        try {
            Files.writeString(outputPath, sb.toString());
            System.out.println("XML successfully written to: " + outputPath.toAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write XML file: " + outputPath, e);
        }
    }
}

package com.orders.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orders.entities.Order;

import javax.imageio.IIOException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class JsonOrderParser {
    public final ObjectMapper mapper;

    public JsonOrderParser(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    public List<Order> parse(String folderPath, int threadCount) {
        Path folder = Path.of(folderPath);
        if (!Files.exists(folder) || !Files.isDirectory(folder)) {
            throw new IllegalArgumentException("Invalid folder path: " + folderPath);
        }

        List<Path> jsonFiles;
        try{
            jsonFiles = Files.list(folder)
                    .filter(path -> path.getFileName()
                            .toString().endsWith(".json")).toList();
        }catch (IOException e){
           throw new RuntimeException("Failed to read: " + folderPath,e);
        }

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<Order>> futures = new ArrayList<>();
        for(Path file : jsonFiles){
            FileReaderTask fileReaderTask = new FileReaderTask(file,mapper);
            futures.add(executor.submit(fileReaderTask));
        }
        List<Order> orders = new ArrayList<>();
        for(Future<Order> future : futures){
            try {
                Order order = future.get();
                if(order != null){
                    orders.add(order);
                }
            }catch (Exception e){
                System.err.println("Failed to process a file: " + e.getMessage() );
            }
        }
        executor.shutdown();
        return orders;

    }
}

package com.orders.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orders.entities.Order;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Callable;

public class FileReaderTask implements Callable<Order> {
    private final Path filePath;
    private final ObjectMapper mapper;

    public FileReaderTask(Path filePath, ObjectMapper mapper) {
        this.filePath = filePath;
        this.mapper = mapper;
    }


    @Override
    public Order call() throws Exception {
       try(BufferedReader bufferedReader = Files.newBufferedReader(filePath)){
           return mapper.readValue(bufferedReader,Order.class);
       }catch (Exception e){
           throw new RuntimeException("Failed to read file: " + filePath, e);
       }
    }
}

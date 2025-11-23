package com.orders.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orders.entities.Order;

import java.nio.file.Path;
import java.util.List;

public class JsonOrderParser {
    public final ObjectMapper mapper;

    public JsonOrderParser(ObjectMapper mapper) {
        this.mapper = mapper;
    }
    public List<Order> parse(String folderPath, int threadCount){

    }
}

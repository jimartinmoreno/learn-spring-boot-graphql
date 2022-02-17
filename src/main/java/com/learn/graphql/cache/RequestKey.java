package com.learn.graphql.cache;

import lombok.Value;

import java.util.List;

@Value
public class RequestKey {

    String userId;
    List<String> queries;

}

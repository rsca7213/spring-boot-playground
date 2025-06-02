package com.playground.api.dtos.product;

import lombok.Value;

import java.util.UUID;

@Value
public class FindProductParams {
    UUID id;
}

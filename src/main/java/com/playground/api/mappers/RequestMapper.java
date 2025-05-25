package com.playground.api.mappers;

public interface RequestMapper<Request, Response, Model> {
    Model requestToModel(Request request);
    Response modelToResponse(Model model);
}
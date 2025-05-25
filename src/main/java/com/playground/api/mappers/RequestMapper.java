package com.playground.api.mappers;

public interface RequestMapper<Request, Response, Model> {
    Model requestToModel(Request request);
    Class<Request> getRequestType();
    Response modelToResponse(Model model);
    Class<Response> getResponseType();
}
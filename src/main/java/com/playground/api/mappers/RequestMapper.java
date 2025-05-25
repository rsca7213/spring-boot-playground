package com.playground.api.mappers;

import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.Exception;
import org.springframework.http.HttpStatus;

public interface RequestMapper<Request, Response, Model> {
    default Model requestToModel(Request request) {
        throw new Exception("An error ocurred while converting the request to model", ErrorCode.UNSUPPORTED_OPERATION, HttpStatus.NOT_IMPLEMENTED);
    }

    default Response modelToResponse(Model model) {
        throw new Exception("An error ocurred while converting the model to response",  ErrorCode.UNSUPPORTED_OPERATION, HttpStatus.NOT_IMPLEMENTED);
    }
}
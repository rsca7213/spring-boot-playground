package com.playground.api.mappers;

import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.UnsupportedException;

public interface RequestMapper<Request, Response, Model> {
    default Model requestToModel(Request request) {
        throw new UnsupportedException("An error ocurred while converting the request to model", ErrorCode.UNSUPPORTED_OPERATION);
    }

    default Response modelToResponse(Model model) {
        throw new UnsupportedException("An error ocurred while converting the model to response", ErrorCode.UNSUPPORTED_OPERATION);
    }
}
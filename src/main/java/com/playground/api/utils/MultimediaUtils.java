package com.playground.api.utils;

import com.playground.api.enums.AllowedFileMimeTypes;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MultimediaUtils {
    public String validateAndExtractFileExtension(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !AllowedFileMimeTypes.isValidMimeType(contentType)) {
            throw new ApiException("The uploaded file is not of a valid type (JPG, PNG or WEBP)", ErrorCode.FILE_CONTENT_ERROR, HttpStatus.BAD_REQUEST);
        }

        // Extract the file extension
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.') + 1) : null;

        if (fileExtension == null) {
            throw new ApiException("The file is not of a valid extension", ErrorCode.FILE_CONTENT_ERROR, HttpStatus.BAD_REQUEST);
        }

        return fileExtension;
    }
}

package com.playground.api.integrations.ports;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface MultimediaStorageService {
    String upload(MultipartFile file, String fileName);
}

package com.crackelets.bigfun.platform.storage.service;

import com.crackelets.bigfun.platform.storage.domain.MyFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface MyFileService {
    String uploadFile(MultipartFile file, String filename) throws IOException;

}

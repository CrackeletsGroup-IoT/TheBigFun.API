package com.crackelets.bigfun.platform.storage.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MyFileService {
  String uploadFile(MultipartFile file, String filename, String containerName) throws IOException;

}
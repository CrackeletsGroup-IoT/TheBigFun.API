package com.crackelets.bigfun.platform.storage.service.impl;

import com.crackelets.bigfun.platform.storage.domain.MyFile;
import com.crackelets.bigfun.platform.storage.service.AzureBlobStorageService;
import com.crackelets.bigfun.platform.storage.service.MyFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class MyFileServiceImpl implements MyFileService {
    @Autowired
    private AzureBlobStorageService azureBlobStorageService;

    @Autowired
    public MyFileServiceImpl() {
    }

    public String uploadFile(MultipartFile file, String filename) throws IOException {
        String azureUrl = this.azureBlobStorageService.uploadFileToBlobStorage(filename, file.getInputStream(), (int)file.getSize());
        MyFile fileEntity = new MyFile();
        fileEntity.setFilename(filename);
        fileEntity.setAzureUrl(azureUrl);
        return fileEntity.getAzureUrl();
    }
}

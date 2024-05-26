package com.crackelets.bigfun.platform.storage.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.crackelets.bigfun.platform.storage.service.AzureBlobStorageService;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class AzureBlobStorageServiceImpl implements AzureBlobStorageService {
    private final String string = "";

    private final String containerName = "the-big-fun-files";

    public AzureBlobStorageServiceImpl() {
    }

    public String uploadFileToBlobStorage(String filename, InputStream inputStream, Integer length) {
        BlobClient blobClient = (new BlobClientBuilder()).
                connectionString(string).containerName(containerName).blobName(filename).buildClient();
        blobClient.upload(inputStream, (long)length, true);
        return blobClient.getBlobUrl();
    }
}
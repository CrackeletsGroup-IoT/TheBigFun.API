package com.crackelets.bigfun.platform.storage.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.crackelets.bigfun.platform.storage.service.AzureBlobStorageService;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class AzureBlobStorageServiceImpl implements AzureBlobStorageService {
    private final String string =
            "DefaultEndpointsProtocol=https;AccountName=thebigfun;AccountKey=bn/4CjRo9kKnEQ5aSKZlkDde9jPI6bvXX7if15AVqGD/3ezntog0RM+ppOd8w+IC90l5fYdO/ven+ASthVvDVA==;EndpointSuffix=core.windows.net";
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
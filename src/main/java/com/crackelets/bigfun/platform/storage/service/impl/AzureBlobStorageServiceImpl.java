package com.crackelets.bigfun.platform.storage.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobClientBuilder;
import com.crackelets.bigfun.platform.storage.service.AzureBlobStorageService;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class AzureBlobStorageServiceImpl implements AzureBlobStorageService {

  public AzureBlobStorageServiceImpl() {}



  public String uploadFileToBlobStorage(String filename, InputStream inputStream, Integer length, String containerName) {
    String connectionString = "DefaultEndpointsProtocol=https;AccountName=thebigfun;AccountKey=bn/4CjRo9kKnEQ5aSKZlkDde9jPI6bvXX7if15AVqGD/3ezntog0RM+ppOd8w+IC90l5fYdO/ven+ASthVvDVA==;EndpointSuffix=core.windows.net";
    BlobClient blobClient = (new BlobClientBuilder()).
      connectionString(connectionString).containerName(containerName).blobName(filename).buildClient();
    blobClient.upload(inputStream, (long)length, true);
    return blobClient.getBlobUrl();
  }
}

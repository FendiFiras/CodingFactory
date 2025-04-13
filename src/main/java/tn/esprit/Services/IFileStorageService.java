package tn.esprit.Services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileStorageService {

    public String storeFile(MultipartFile file);
}

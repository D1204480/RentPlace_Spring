package fcu.iecs.demo.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import fcu.iecs.demo.model.Image;
import fcu.iecs.demo.repository.ImageRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    private static final String UPLOAD_DIR = "uploads/";

    // 儲存圖片
    public Image saveImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        String fileSize = String.valueOf(file.getSize());
        Path path = Paths.get(UPLOAD_DIR + fileName);

        // 儲存檔案
        Files.createDirectories(path.getParent());
        file.transferTo(path.toFile());

        // 儲存資料庫
        Image image = new Image();
        image.setImageName(fileName);
        image.setImagePath(path.toString());
        image.setImageSize(fileSize);
        image.setImageType(fileType);
        return imageRepository.save(image);
    }

    // 讀取圖片
    public Image getImage(Long imageId) {
        return imageRepository.findById(imageId).orElse(null);
    }

    // 刪除圖片
    public void deleteImage(Long imageId) throws IOException {
        Image image = getImage(imageId);
        if (image != null) {
            File file = new File(image.getImagePath());
            boolean deleted = file.exists() && file.delete();
            if (!deleted) {
                // Handle the case when file deletion fails
                System.err.println("Failed to delete the file: " + file.getAbsolutePath());
            }
            imageRepository.delete(image); // 刪除資料庫紀錄
        }
    }
}

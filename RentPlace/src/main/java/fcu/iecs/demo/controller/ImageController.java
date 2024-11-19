package fcu.iecs.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import fcu.iecs.demo.model.Image;
import fcu.iecs.demo.service.ImageService;
@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/images")  // 更新 URL 路徑為 images
public class ImageController {

    @Autowired
    private ImageService imageService;

    // 上傳圖片
    @PostMapping("/upload")
    public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            Image savedImage = imageService.saveImage(file);
            return new ResponseEntity<>(savedImage, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 取得圖片
    @GetMapping("/{imageId}")
    public ResponseEntity<Image> getImage(@PathVariable Long imageId) {
        Image image = imageService.getImage(imageId);
        return image != null ? new ResponseEntity<>(image, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // 刪除圖片
    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        try {
            imageService.deleteImage(imageId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
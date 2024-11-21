package fcu.iecs.demo.service;

import  fcu.iecs.demo.model.Image;
import fcu.iecs.demo.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    public Optional<Image> getImageById(Integer imageId) {
        return imageRepository.findById(imageId);
    }

    public Image addImage(Image image) {
        return imageRepository.save(image);
    }

    public void deleteImage(Integer imageId) {
        imageRepository.deleteById(imageId);
    }
}

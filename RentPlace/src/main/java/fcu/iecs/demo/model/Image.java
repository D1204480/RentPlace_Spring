package fcu.iecs.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "image", indexes = {
        @Index(name = "idx_image_name", columnList = "image_name"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long imageId;

    @NotBlank(message = "Image name cannot be blank")
    @Size(max = 255, message = "Image name cannot exceed 255 characters")
    @Column(name = "image_name", nullable = false, length = 255)
    private String imageName;

    @NotBlank(message = "Image path cannot be blank")
    @Column(name = "image_path", nullable = false, length = 255)
    private String imagePath;

    @Column(name = "image_size", nullable = false, length = 255)
    private String imageSize;

    @NotBlank(message = "Image type cannot be blank")
    @Column(name = "image_type", nullable = false, length = 255)
    private String imageType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(name = "description", length = 1000)
    private String description;

    @Transient
    private String downloadUrl;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Convert bytes to human-readable size
    public void setImageSizeInBytes(long bytes) {
        this.imageSize = String.valueOf(bytes);
    }

    // Get size in bytes
    public Long getImageSizeInBytes() {
        try {
            return Long.parseLong(this.imageSize);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    // Get human-readable size
    public String getFormattedSize() {
        try {
            long bytes = Long.parseLong(this.imageSize);
            final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
            int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
            return String.format("%.1f %s",
                    bytes / Math.pow(1024, digitGroups),
                    units[digitGroups]);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return this.imageSize;
        }
    }

    // Get file extension
    public String getFileExtension() {
        return imageName != null && imageName.contains(".")
                ? imageName.substring(imageName.lastIndexOf(".")).toLowerCase()
                : "";
    }

    // Check if image is of given MIME type
    public boolean isImageType(String mimeType) {
        return imageType != null && imageType.equalsIgnoreCase(mimeType);
    }

    // Check if image is supported format (JPEG, PNG, GIF)
    public boolean isSupportedImageFormat() {
        String[] supportedTypes = {"image/jpeg", "image/png", "image/gif"};
        return imageType != null &&
                Arrays.asList(supportedTypes).contains(imageType.toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;
        Image image = (Image) o;
        return Objects.equals(imageId, image.imageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageId);
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + imageId +
                ", name='" + imageName + '\'' +
                ", type='" + imageType + '\'' +
                ", size='" + getFormattedSize() + '\'' +
                ", created='" + createdAt + '\'' +
                '}';
    }
}
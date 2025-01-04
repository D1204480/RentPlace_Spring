package fcu.iecs.demo.service;

import fcu.iecs.demo.dto.VenueDTO;
import fcu.iecs.demo.model.*;
import fcu.iecs.demo.repository.CloseDateRepository;
import fcu.iecs.demo.repository.EquipmentRepository;
import fcu.iecs.demo.repository.ImageRepository;
import fcu.iecs.demo.repository.VenueRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
//public class VenueService {
//
//    private final VenueRepository venueRepository;
//
//    public VenueService(VenueRepository venueRepository) {
//        this.venueRepository = venueRepository;
//    }
//
//    public List<Venue> getAllVenues() {
//        return venueRepository.findAll();
//    }
//
//    public Optional<Venue> getVenueById(Integer id) {
//        return venueRepository.findById(id);
//    }
//
//    public Venue addVenue(Venue venue) {
//        return venueRepository.save(venue);
//    }
//
//    public Venue updateVenue(Integer id, Venue venueDetails) {
//        return venueRepository.findById(id).map(venue -> {
//            venue.setPlaceName(venueDetails.getPlaceName());
//            venue.setVenueType(venueDetails.getVenueType());
//            venue.setVenueName(venueDetails.getVenueName());
//            venue.setRegionName(venueDetails.getRegionName());
//            venue.setAddress(venueDetails.getAddress());
//            venue.setUnitPrice(venueDetails.getUnitPrice());
//            venue.setUnit(venueDetails.getUnit());
//            venue.setCapacity(venueDetails.getCapacity());
//            venue.setAvailableTime(venueDetails.getAvailableTime());
//            venue.setRemark(venueDetails.getRemark());
//            venue.setImageId(venueDetails.getImageId());
//            return venueRepository.save(venue);
//        }).orElseThrow(() -> new RuntimeException("Venue not found with id " + id));
//    }
//
//    public void deleteVenue(Integer id) {
//        venueRepository.deleteById(id);
//    }
//}

public class VenueService {
  private final VenueRepository venueRepository;
  private final ImageRepository imageRepository; // 新增
  private final EquipmentRepository equipmentRepository; // 新增
  private final CloseDateRepository closeDateRepository; // 新增


  public VenueService(VenueRepository venueRepository,
                      ImageRepository imageRepository,
                      EquipmentRepository equipmentRepository,
                      CloseDateRepository closeDateRepository) {
    this.venueRepository = venueRepository;
    this.imageRepository = imageRepository;
    this.equipmentRepository = equipmentRepository;
    this.closeDateRepository = closeDateRepository;
  }

  // 未找到場地的錯誤處理
  public class VenueNotFoundException extends RuntimeException {
    public VenueNotFoundException(Integer id) {
      super("Venue not found with id: " + id);
    }
  }

  public List<Venue> getAllVenues() {
    return venueRepository.findAll();
  }

  public Venue getVenueById(Integer id) {
    return venueRepository.findById(id)
        .orElseThrow(() -> new VenueNotFoundException(id));
  }

  public Venue addVenue(VenueDTO venueDTO) {
    // 先處理 Image
    Integer imageId = null;
    if (venueDTO.getImageName() != null && !venueDTO.getImageName().trim().isEmpty()) {
      Image image = new Image();
      image.setImageName(venueDTO.getImageName());
      // 設定其他必要的 Image 欄位
      image.setImagePath("/images/" + venueDTO.getImageName());  // 根據實際路徑調整
      image.setImageSize("default");  // 根據需求調整
      image.setImageType("jpg");      // 根據實際類型調整

      Image savedImage = imageRepository.save(image);
      imageId = savedImage.getImageId();
    }

    // 建立 Venue
    Venue venue = new Venue();
    venue.setPlaceName(venueDTO.getPlaceName());
    venue.setVenueType(venueDTO.getVenueType());
    venue.setVenueName(venueDTO.getVenueName());
    venue.setRegionName(venueDTO.getRegionName());
    venue.setAddress(venueDTO.getAddress());
    venue.setUnitPrice(Double.parseDouble(venueDTO.getUnitPrice())); // String 轉 Double
    venue.setUnit(venueDTO.getUnit());
    venue.setCapacity(venueDTO.getCapacity());
    venue.setAvailableTime(venueDTO.getAvailableTime());
    venue.setRemark(venueDTO.getRemark());
    venue.setImageId(imageId);  // 設定剛創建的 imageId
    venue.setPhoneNumber(venueDTO.getPhoneNumber());

    Venue savedVenue = venueRepository.save(venue);

    // 處理 equipment 陣列
    if (venueDTO.getEquipment() != null && venueDTO.getEquipment().length > 0) {
      Arrays.stream(venueDTO.getEquipment())
          .forEach(equipmentName -> {
            Equipment equipment = new Equipment();
            equipment.setEquipmentName(equipmentName);
            equipment.setVenue(savedVenue);
            equipmentRepository.save(equipment);
          });
    }

    // 處理 closeDates 陣列
    if (venueDTO.getCloseDates() != null && venueDTO.getCloseDates().length > 0) {
      Arrays.stream(venueDTO.getCloseDates())
          .forEach(date -> {
            CloseDate closeDate = new CloseDate();
            closeDate.setCloseDate(LocalDate.parse(date));
            closeDate.setVenueId(savedVenue.getId());  // 設定 venue_id
            closeDate.setStatusId(12);  // 設定 status_id
            closeDateRepository.save(closeDate);
          });
    }

    return savedVenue;
  }

  public Venue updateVenue(Integer id, VenueDTO venueDTO) {
    Venue venue = venueRepository.findById(id)
        .orElseThrow(() -> new VenueNotFoundException(id));

    // 處理 Image
    if (venueDTO.getImageName() != null && !venueDTO.getImageName().trim().isEmpty()) {
      // 建立新圖片
      Image image = new Image();
      image.setImageName(venueDTO.getImageName());
      image.setImagePath("/images/" + venueDTO.getImageName());
      image.setImageSize("default");
      image.setImageType("jpg");
      Image savedImage = imageRepository.save(image);

      // 更新 venue 的 imageId
      venue.setImageId(savedImage.getImageId());
    } else {
      // 如果沒有新圖片，保持原有的 imageId
      // venue.setImageId(null);  // 移除這行，不要設為 null
    }

    // 更新其他欄位
    venue.setPlaceName(venueDTO.getPlaceName());
    venue.setVenueType(venueDTO.getVenueType());
    venue.setVenueName(venueDTO.getVenueName());
    venue.setRegionName(venueDTO.getRegionName());
    venue.setAddress(venueDTO.getAddress());
    venue.setUnitPrice(Double.parseDouble(venueDTO.getUnitPrice()));
    venue.setUnit(venueDTO.getUnit());
    venue.setCapacity(venueDTO.getCapacity());
    venue.setAvailableTime(venueDTO.getAvailableTime());
    venue.setRemark(venueDTO.getRemark());
    venue.setPhoneNumber(venueDTO.getPhoneNumber());

    // 先儲存 venue
    Venue savedVenue = venueRepository.save(venue);

    // 更新 equipment
    equipmentRepository.deleteByVenueId(id);
    if (venueDTO.getEquipment() != null && venueDTO.getEquipment().length > 0) {
      Arrays.stream(venueDTO.getEquipment())
          .forEach(equipmentName -> {
            Equipment equipment = new Equipment();
            equipment.setEquipmentName(equipmentName);
            equipment.setVenue(savedVenue);
            equipmentRepository.save(equipment);
          });
    }

    // 更新 closeDates
    closeDateRepository.deleteByVenueId(id);
    if (venueDTO.getCloseDates() != null && venueDTO.getCloseDates().length > 0) {
      Arrays.stream(venueDTO.getCloseDates())
          .forEach(date -> {
            CloseDate closeDate = new CloseDate();
            closeDate.setCloseDate(LocalDate.parse(date));
            closeDate.setVenueId(savedVenue.getId());
            closeDate.setStatusId(12);
            closeDateRepository.save(closeDate);
          });
    }

    return savedVenue;
  }

  @Transactional
  public void deleteVenue(Integer id) {
    Venue venue = venueRepository.findById(id)
        .orElseThrow(() -> new VenueNotFoundException(id));

    // 刪除關聯的 equipment
    equipmentRepository.deleteByVenueId(id);

    // 刪除關聯的 closeDates
    closeDateRepository.deleteByVenueId(id);

    // 刪除 venue
    venueRepository.delete(venue);
  }
}
package fcu.iecs.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VenueDTO {
  // 接收前端資料用的 DTO
  private String placeName;
  private String venueType;
  private String venueName;
  private String regionName;
  private String address;
  private String unitPrice;   // 接收時用 String
  private String unit;
  private int capacity;
  private String availableTime;
  private String remark;
  private String imageId;  // 改為 String 以接收空字串
  private String imageName;  // 用於新增/更新圖片
  private String phoneNumber;

  // equipment 改為 String 陣列
  private String[] equipment;
  private String[] closeDates;  // 接收日期陣列 (休館日)
}

package fcu.iecs.demo.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "Reservations")
public class Reservation {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_id")
  private Integer reservationId;

  @Column(name = "venue_id", nullable = false)
  private Integer venueId;

  @Column(name = "user_id", length = 50, nullable = false)
  private String userId;

  @Column(name = "time_period_id")
  private Integer timePeriodId;

  @Column(name = "reservation_date", nullable = false)
  private LocalDate reservationDate;

  @Column(name = "remark")
  private String remark;

  @Column(name = "apply_apartment")
  private String applyApartment;

  @Column(name = "content")
  private String content;

  @Column(name = "status_id")
  private Integer statusId;

  @ManyToOne
  @JoinColumn(name = "venue_id", insertable = false, updatable = false)  // 這裡指定資料庫中的外鍵欄位名稱
  private Venue venue;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "time_period_id", referencedColumnName = "time_period_id", insertable = false, updatable = false)
  private TimePeriod timePeriod;

  // 與 Status 的關聯表
  @ManyToOne(fetch = FetchType.EAGER)  // 確保在查詢 Reservation 時會自動載入關聯的 Status 資訊
  @JoinColumn(name = "status_id", referencedColumnName = "status_id", insertable=false, updatable=false)   // 明確指定關聯欄位
  private Status timePeriod_statusInfo;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "reservation_id", referencedColumnName = "reservation_id")
  private Set<ReservationEquipment> reservationEquipments = new HashSet<>();



  // Getters and Setters
  public Integer getReservationId() {
    return reservationId;
  }

  public void setReservationId(Integer reservationId) {
    this.reservationId = reservationId;
  }

  public Integer getVenueId() {
    return venueId;
  }

  public void setVenueId(Integer venueId) {
    this.venueId = venueId;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public Integer getTimePeriodId() {
    return timePeriodId;
  }

  public void setTimePeriodId(Integer timePeriodId) {
    this.timePeriodId = timePeriodId;
  }

  public LocalDate getReservationDate() {
    return reservationDate;
  }

  public void setReservationDate(LocalDate reservationDate) {
    this.reservationDate = reservationDate;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public String getApplyApartment() {
    return applyApartment;
  }

  public void setApplyApartment(String applyApartment) {
    this.applyApartment = applyApartment;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Integer getStatusId() {
    return statusId;
  }

  public void setStatusId(Integer statusId) {
    this.statusId = statusId;
  }

  public Venue getVenue() {
    return venue;
  }

  public void setVenue(Venue venue) {
    this.venue = venue;
  }

  public TimePeriod getTimePeriod() {
    return timePeriod;
  }

  public void setTimePeriod(TimePeriod timePeriod) {
    this.timePeriod = timePeriod;
  }

  public Status getTimePeriod_statusInfo() {
    return timePeriod_statusInfo;
  }

  public void setTimePeriod_statusInfo(Status timePeriod_statusInfo) {
    this.timePeriod_statusInfo = timePeriod_statusInfo;
  }

  @Transient  // 不會儲存到資料庫
  private PaymentMethod paymentMethod;

  // 添加 getter 和 setter
  public PaymentMethod getPaymentMethod() {
    return paymentMethod;
  }

  public void setPaymentMethod(PaymentMethod paymentMethod) {
    this.paymentMethod = paymentMethod;
  }

  // 新增處理設備ID的方法
  @Transient  // 這個欄位不會映射到資料庫
  private List<Integer> equipmentIds;

  public List<Integer> getEquipmentIds() {
    return equipmentIds;
  }

  public void setEquipmentIds(List<Integer> equipmentIds) {
    this.equipmentIds = equipmentIds;
    if (equipmentIds != null) {
      this.reservationEquipments.clear();
      for (Integer equipmentId : equipmentIds) {
        ReservationEquipment re = new ReservationEquipment();
        re.setReservationId(this.getReservationId());  // 設置預訂ID
        re.setEquipmentId(equipmentId);
        this.reservationEquipments.add(re);
      }
    }
  }

  // 新增 getter/setter
  public Set<ReservationEquipment> getReservationEquipments() {
    return reservationEquipments;
  }

  public void setReservationEquipments(Set<ReservationEquipment> reservationEquipments) {
    this.reservationEquipments = reservationEquipments;
  }

  @Transient  // 不儲存到資料庫
  private BigDecimal totalAmount;

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }


  // 修改單一時段為多時段
  @Transient  // 用於接收前端資料,不存入資料庫
  private List<Integer> timePeriodIds; // 改為複數

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "reservation_id")
  private Set<ReservationTimePeriod> reservationTimePeriods = new HashSet<>();

  // 新增 getter/setter
  public List<Integer> getTimePeriodIds() {
    return timePeriodIds;
  }

  public void setTimePeriodIds(List<Integer> timePeriodIds) {
    this.timePeriodIds = timePeriodIds;
    if (timePeriodIds != null) {
      this.reservationTimePeriods.clear();
      for (Integer timePeriodId : timePeriodIds) {
        ReservationTimePeriod rtp = new ReservationTimePeriod();
        rtp.setTimePeriodId(timePeriodId);
        this.reservationTimePeriods.add(rtp);
      }
    }
  }


  // 獲取格式化後的時段文字
  @JsonProperty("timePeriodText")
  public String getTimePeriodText() {
    return timePeriod != null ? timePeriod.getTimePeriod() : null;
  }

  // 獲取格式化後的狀態文字
  @JsonProperty("statusText")
  public String getStatusText() {
    return timePeriod_statusInfo != null ? timePeriod_statusInfo.getStatus() : null;
  }


  @Transient
  private Map<String, List<String>> equipmentCategories;

  // 獲取分類後的設備清單
  @JsonProperty("equipmentCategories")
  public Map<String, List<String>> getEquipmentCategories() {
    if (venue == null || venue.getEquipment() == null) {
      return Collections.emptyMap();
    }

    Map<String, List<String>> categories = new HashMap<>();

    // 基礎設施
    List<String> basic = new ArrayList<>();
    // 媒體設備
//        List<String> media = new ArrayList<>();
    // 無障礙設施
    List<String> accessibility = new ArrayList<>();

    for (Equipment equipment : venue.getEquipment()) {
      String name = equipment.getEquipmentName();

      // 根據設備名稱進行分類
      if (isBasicEquipment(name)) {
        basic.add(name);
//            } else if (isMediaEquipment(name)) {
//                media.add(name);
      } else if (isAccessibilityEquipment(name)) {
        accessibility.add(name);
      }
    }

    // 只添加非空的分類
    if (!basic.isEmpty()) categories.put("basic", basic);
//        if (!media.isEmpty()) categories.put("media", media);
    if (!accessibility.isEmpty()) categories.put("accessibility", accessibility);

    return categories;
  }

  // 判斷設備類型的輔助方法
  private boolean isBasicEquipment(String name) {
    return Arrays.asList("桌子", "椅子", "冷氣", "麥克風","投影機", "電視機", "音響", "白板").contains(name);
  }

//    private boolean isMediaEquipment(String name) {
//        return Arrays.asList("麥克風", "音響", "白板").contains(name);
//    }

  private boolean isAccessibilityEquipment(String name) {
    return Arrays.asList("飲水機","電梯", "停車場", "無障礙設施").contains(name);
  }

  @Transient  // 不會儲存到資料庫
  private String virtualAccount;

  public String getVirtualAccount() {
    return virtualAccount;
  }

  public void setVirtualAccount(String virtualAccount) {
    this.virtualAccount = virtualAccount;
  }

}

package fcu.iecs.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Reservation_Equipment")
//public class ReservationEquipment {
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @Column(name = "reservation_equipment_id")
//  private Integer reservationEquipmentId;
//
//  @Column(name = "reservation_id")
//  private Integer reservationId;
//
//  @Column(name = "equipment_id")
//  private Integer equipmentId;
//
//  // getters and setters
//
//  public Integer getReservationEquipmentId() {
//    return reservationEquipmentId;
//  }
//
//  public void setReservationEquipmentId(Integer reservationEquipmentId) {
//    this.reservationEquipmentId = reservationEquipmentId;
//  }
//
//  public Integer getReservationId() {
//    return reservationId;
//  }
//
//  public void setReservationId(Integer reservationId) {
//    this.reservationId = reservationId;
//  }
//
//  public Integer getEquipmentId() {
//    return equipmentId;
//  }
//
//  public void setEquipmentId(Integer equipmentId) {
//    this.equipmentId = equipmentId;
//  }
//}

public class ReservationEquipment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_equipment_id")
  private Integer reservationEquipmentId;

  // 修改：移除單純的 Column 注解，改用 ManyToOne 建立關聯
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reservation_id")
  private Reservation reservation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "equipment_id")
  private Equipment equipment;

  // Getters and Setters
  public Integer getReservationEquipmentId() {
    return reservationEquipmentId;
  }

  public void setReservationEquipmentId(Integer reservationEquipmentId) {
    this.reservationEquipmentId = reservationEquipmentId;
  }

  public Reservation getReservation() {
    return reservation;
  }

  public void setReservation(Reservation reservation) {
    this.reservation = reservation;
  }

  public Equipment getEquipment() {
    return equipment;
  }

  public void setEquipment(Equipment equipment) {
    this.equipment = equipment;
  }
}
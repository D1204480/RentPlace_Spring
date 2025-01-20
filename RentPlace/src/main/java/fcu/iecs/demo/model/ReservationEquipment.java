package fcu.iecs.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Reservation_Equipment")
public class ReservationEquipment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_equipment_id")
  private Integer reservationEquipmentId;

  // 移除原本的 reservationId，改用關聯
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reservation_id")
  private Reservation reservation;

  // 移除原本的 equipmentId，改用關聯
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "equipment_id")
  private Equipment equipment;

  // getters and setters

  public Integer getReservationEquipmentId() {
    return reservationEquipmentId;
  }

  public void setReservationEquipmentId(Integer reservationEquipmentId) {
    this.reservationEquipmentId = reservationEquipmentId;
  }

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

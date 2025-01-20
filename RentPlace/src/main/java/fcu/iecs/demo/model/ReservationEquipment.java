package fcu.iecs.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Reservation_Equipment")
public class ReservationEquipment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "reservation_equipment_id")
  private Integer reservationEquipmentId;

  @Column(name = "reservation_id")
  private Integer reservationId;

  @Column(name = "equipment_id")
  private Integer equipmentId;

  // getters and setters

  public Integer getReservationEquipmentId() {
    return reservationEquipmentId;
  }

  public void setReservationEquipmentId(Integer reservationEquipmentId) {
    this.reservationEquipmentId = reservationEquipmentId;
  }

  public Integer getReservationId() {
    return reservationId;
  }

  public void setReservationId(Integer reservationId) {
    this.reservationId = reservationId;
  }

  public Integer getEquipmentId() {
    return equipmentId;
  }

  public void setEquipmentId(Integer equipmentId) {
    this.equipmentId = equipmentId;
  }
}

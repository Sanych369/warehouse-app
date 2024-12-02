package ru.damrin.app.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store")
public class StoreEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "responsible_person")
  private String responsiblePerson;

  @Column(name = "good_name")
  private String goodName;

  @Column(name = "purchase_price")
  private BigDecimal purchasePrice;

  @Column(name = "arrived_total")
  private Long arrivedTotal;

  @Column(name = "consumption_total")
  private Long consumptionTotal;

  @Column(name = "reason")
  private String reason;

  @CreatedDate
  @Column(name = "created_at")
  private LocalDateTime createdAt;

}

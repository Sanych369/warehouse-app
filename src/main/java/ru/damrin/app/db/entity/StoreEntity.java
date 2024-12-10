package ru.damrin.app.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * JPA сущность склада.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store")
@EntityListeners(AuditingEntityListener.class)
public class StoreEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "responsible_person")
  private String responsiblePerson;

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

  @ManyToOne
  private UserEntity user;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "good_name", referencedColumnName = "name")
  private GoodEntity good;

  @PrePersist
  private void prePersist() {
    this.responsiblePerson = this.user.getName() + " " + this.user.getSurname();
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass = o instanceof HibernateProxy proxy
        ? proxy.getHibernateLazyInitializer().getPersistentClass()
        : o.getClass();
    Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy
        ? proxy.getHibernateLazyInitializer().getPersistentClass()
        : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    StoreEntity that = (StoreEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy proxy
        ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}

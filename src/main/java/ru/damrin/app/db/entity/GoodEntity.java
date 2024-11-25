package ru.damrin.app.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@Table(name = "goods")
@NoArgsConstructor
@AllArgsConstructor
public class GoodEntity {

  @Id
  @Column(name = "name")
  private String name;

  @Enumerated(EnumType.STRING)
  @ManyToOne
  @JoinColumn(name = "category_name")
  private CategoryEntity category;

  @Column(name = "purchase_price")
  private BigDecimal purchasePrice;

  @Column(name = "sale_price")
  private BigDecimal salePrice;

  @Column(name = "balance")
  private Long balance;

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
    GoodEntity that = (GoodEntity) o;
    return getName() != null && Objects.equals(getName(), that.getName());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy proxy
        ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }

  @PrePersist
  public void prePersist() {
    this.setSalePrice(purchasePrice.add(
            purchasePrice.divide(new BigDecimal(100), RoundingMode.HALF_UP)
                .multiply(category.getMarkupPercentage()))
        .setScale(0, RoundingMode.HALF_UP));
  }
}

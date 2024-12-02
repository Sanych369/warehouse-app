package ru.damrin.app.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import ru.damrin.app.common.exception.WarehouseAppException;

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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @ManyToOne
  @Enumerated(EnumType.STRING)
  @JoinColumn(name = "category_id")
  private CategoryEntity category;

  @Column(name = "purchase_price")
  private BigDecimal purchasePrice;

  @Column(name = "sale_price")
  private Long salePrice;

  @Column(name = "balance", insertable = false)
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
        .setScale(0, RoundingMode.HALF_UP).longValue());
  }

  @PreRemove
  private void preRemove() {
    if (this.balance > 0) {
      throw new WarehouseAppException(String.format(
          "Невозможно удалить товар: %s. Текущий остаток: %d", this.name, this.balance)
      );
    }
  }
}

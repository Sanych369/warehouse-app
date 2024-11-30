package ru.damrin.app.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.damrin.app.common.exception.WarehouseAppException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "category")
public class CategoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "markup_percentage")
  private BigDecimal markupPercentage;

  @OneToMany(mappedBy = "category",
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Set<GoodEntity> goods;

  @PreRemove
  private void preRemove() {
    if (goods.stream().map(GoodEntity::getBalance).anyMatch(balance -> balance > 0)) {
      throw new WarehouseAppException("Есть остатки товаров. Внимательнее");
    }
  }

  @PreUpdate //SaveAndFlush
  private void preUpdate() {
    goods.iterator().forEachRemaining(this::recalculate);
  }

  /**
   * Метод обновления наценки товара при изменении категории.
   * @param good - товар в текущей категории
   */
  private void recalculate(GoodEntity good) {
    good.setSalePrice(good.getPurchasePrice().add(
        good.getPurchasePrice()
            .divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP)
            .multiply(this.markupPercentage)));
  }
}

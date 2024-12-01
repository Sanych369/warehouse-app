package ru.damrin.app.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
      orphanRemoval = true,
  fetch = FetchType.EAGER)
  private Set<GoodEntity> goods;

  @PreRemove
  private void preRemove() {
    if (goods.stream()
        .map(GoodEntity::getBalance)
        .anyMatch(balance -> balance > 0)) {
      throw new WarehouseAppException(String.format("Невозможно удалить категорию %s. Имеются товары на балансе", name));
    }
  }
}

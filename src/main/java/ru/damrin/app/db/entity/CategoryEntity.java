package ru.damrin.app.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.damrin.app.common.converter.CategoryConverter;
import ru.damrin.app.common.enums.GoodCategory;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class CategoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Convert(converter = CategoryConverter.class)
  @Column(name = "category")
  private GoodCategory category;

  @Builder.Default
  @OneToMany(mappedBy = "category",
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Set<GoodEntity> goods = new HashSet<>();

  public void addGood(GoodEntity good) {
    good.setCategory(this);
    goods.add(good);
  }

  public void removeGood(GoodEntity good) {
    goods.remove(good);
  }
}

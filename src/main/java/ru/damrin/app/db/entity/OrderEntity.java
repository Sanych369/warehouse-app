package ru.damrin.app.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.damrin.app.common.exception.WarehouseAppException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @OneToOne
  @JoinColumn(name = "company_id")
  private CompanyEntity company;

  @Builder.Default
  @Column(name = "total_amount")
  private Long totalAmount = 0L;

  @CreatedDate
  @Column(name = "created_at")
  private LocalDate createdAt;

  @Builder.Default
  @OneToMany(mappedBy = "order", fetch = FetchType.EAGER,
      cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<OrdersGoodsEntity> ordersGoods = new HashSet<>();

  public void addOrdersGoods(OrdersGoodsEntity ordersGoods) {
    ordersGoods.setOrder(this);
    totalAmount += ordersGoods.getSum();
    this.ordersGoods.add(ordersGoods);
  }

  public void removeOrdersGoodsByName(String goodName) {
    var order = ordersGoods.stream()
        .filter(ordersGoodsEntity -> ordersGoodsEntity.getGoodName().equals(goodName))
        .findFirst()
        .orElseThrow(() -> new WarehouseAppException(
            String.format("Позиция %s не найдена в заказе. Проверьте наличие позиции", goodName)));
      order.setOrder(null);
      totalAmount -= order.getSum();
      this.ordersGoods.remove(order);
  }
}

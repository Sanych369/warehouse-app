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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.damrin.app.common.exception.WarehouseAppException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * JPA сущность заказов.
 */
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

  @ManyToOne
  @JoinColumn(name = "company_id")
  private CompanyEntity company;

  @Builder.Default
  @Column(name = "total_amount")
  private Long totalAmount = 0L;

  @CreatedDate
  @Column(name = "created_at", insertable = false, updatable = false)
  private LocalDate createdAt;

  @Builder.Default
  @OneToMany(mappedBy = "order",
      fetch = FetchType.EAGER,
      cascade = CascadeType.ALL,
      orphanRemoval = true)
  private Set<OrdersGoodsEntity> ordersGoods = new HashSet<>();

  public void addOrdersGoods(OrdersGoodsEntity ordersGoods) {
    ordersGoods.setOrder(this);
    this.ordersGoods.add(ordersGoods);
    totalAmount = this.ordersGoods.stream()
        .map(OrdersGoodsEntity::getSum)
        .reduce(0L, Long::sum);
  }

  public void removeOrdersGoodsById(Long ordersGoodId) {
    var order = ordersGoods.stream()
        .filter(ordersGoodsEntity -> ordersGoodsEntity.getId().equals(ordersGoodId))
        .findFirst()
        .orElseThrow(() -> new WarehouseAppException("Позиция не найдена в заказе. Проверьте наличие позиции"));
    order.setOrder(null);
    this.ordersGoods.remove(order);
    totalAmount -= order.getSum();
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
    OrderEntity that = (OrderEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy proxy
        ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}

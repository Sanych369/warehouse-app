package ru.damrin.app.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

import java.util.Objects;

/**
 * JPA сущность для связки товаров к заказу.
 */
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders_goods")
public class OrdersGoodsEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "good_name")
  private String goodName;

  @Column(name = "sum")
  private Long sum;

  @ManyToOne(cascade = CascadeType.MERGE)
  private OrderEntity order;

  @Column(name = "goods_quantity")
  private Long quantity;

  @ManyToOne(cascade = CascadeType.MERGE)
  private GoodEntity good;

  @PrePersist
  protected void prePersist() {
    this.goodName = good.getName();
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
    OrdersGoodsEntity that = (OrdersGoodsEntity) o;
    return getId() != null && Objects.equals(getId(), that.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy proxy
        ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}

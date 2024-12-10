package utils;

import ru.damrin.app.model.StoreDto;
import ru.damrin.app.model.category.CategoryDto;
import ru.damrin.app.model.company.CompanyDto;
import ru.damrin.app.model.good.GoodDto;
import ru.damrin.app.model.good.GoodOrderDto;
import ru.damrin.app.model.order.CreateOrderDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class PrepareUtil {

  public static final Long CATEGORY_ID = 1L;
  public static final Long COMPANY_ID = 2L;
  public static final Long GOOD_ID = 3L;

  public static final String CATEGORY_NAME = "CategoryName";
  public static final String COMPANY_NAME = "CompanyName";
  public static final String USER_NAME = "UserName";
  public static final String GOOD_NAME = "GoodName";

  public static final String COMPANY_ADDRESS = "CompanyAddress";
  public static final String COMPANY_PHONE = "+77777777777";
  public static final String COMPANY_EMAIL = "company@test.com";
  public static final String REASON = "reason";

  public static final BigDecimal MARKUP_PERCENTAGE = BigDecimal.TEN;
  public static final BigDecimal PURCHASE_PRICE = BigDecimal.valueOf(100);

  public static final Long SALE_PRICE = 110L;
  public static final Long GOOD_BALANCE = 10_000L;
  public static final Long ORDER_TOTAL_AMOUNT = 1_100_000L;

  public static CategoryDto prepareCategory() {
    return new CategoryDto(CATEGORY_ID, CATEGORY_NAME, MARKUP_PERCENTAGE);
  }

  public static CompanyDto prepareCompany(boolean isActive) {
    return new CompanyDto(COMPANY_ID, COMPANY_NAME, COMPANY_ADDRESS, COMPANY_PHONE, COMPANY_EMAIL, isActive);
  }

  public static GoodDto prepareGood() {
    return new GoodDto(GOOD_ID, GOOD_NAME, prepareCategory(), PURCHASE_PRICE, SALE_PRICE, GOOD_BALANCE);
  }

  public static CreateOrderDto createOrderDto() {
    return new CreateOrderDto(COMPANY_ID, Set.of(new GoodOrderDto(GOOD_ID, 100L)));
  }

  public static StoreDto prepareStoreDto(Long arrivedTotal, Long consumptionTotal) {
    return new StoreDto(GOOD_NAME, PURCHASE_PRICE, arrivedTotal, consumptionTotal, REASON, LocalDateTime.now());
  }
}

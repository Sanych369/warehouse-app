package ru.damrin.app.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.Sort.Order.asc;
import static org.springframework.data.domain.Sort.Order.desc;

@Service
public class SortService {

  public Sort getSortOrderForCategory(String sort) {
    return switch (sort) {
      case "name_desc" -> Sort.by(desc("name"));
      case "markup_asc" -> Sort.by(asc("markupPercentage"));
      case "markup_desc" -> Sort.by(desc("markupPercentage"));
      default -> Sort.by(asc("name"));
    };
  }

  public Sort getSortOrderForGoods(String sort) {
    return switch (sort) {
      case "name_desc" -> Sort.by(desc("name"));
      case "category_asc" -> Sort.by(asc("category"));
      case "category_desc" -> Sort.by(desc("category"));
      case "purchase_price_asc" -> Sort.by(asc("purchasePrice"));
      case "purchase_price_desc" -> Sort.by(desc("purchasePrice"));
      case "sale_price_asc" -> Sort.by(asc("salePrice"));
      case "sale_price_desc" -> Sort.by(desc("salePrice"));
      case "balance__asc" -> Sort.by(asc("balance"));
      case "balance_desc" -> Sort.by(desc("balance"));
      default -> Sort.by(asc("name"));
    };
  }

  public Sort getSortOrderForUsers(String sort) {
    return switch (sort) {
      case "name_desc" -> Sort.by(Sort.Direction.DESC, "name");
      case "surname_asc" -> Sort.by(Sort.Direction.ASC, "surname");
      case "surname_desc" -> Sort.by(Sort.Direction.DESC, "surname");
      case "position_asc" -> Sort.by(Sort.Direction.ASC, "position");
      case "position_desc" -> Sort.by(Sort.Direction.DESC, "position");
      case "email_asc" -> Sort.by(Sort.Direction.ASC, "email");
      case "email_desc" -> Sort.by(Sort.Direction.DESC, "email");
      default -> Sort.by(Sort.Direction.ASC, "name");
    };
  }

  public Sort getSortOrderForCompanies(String sort) {
    return switch (sort) {
      case "name_desc" -> Sort.by(Sort.Direction.DESC, "name");
      case "address_asc" -> Sort.by(Sort.Direction.ASC, "address");
      case "address_desc" -> Sort.by(Sort.Direction.DESC, "address");
      case "phone_asc" -> Sort.by(Sort.Direction.ASC, "phone");
      case "phone_desc" -> Sort.by(Sort.Direction.DESC, "phone");
      case "email_asc" -> Sort.by(Sort.Direction.ASC, "email");
      case "email_desc" -> Sort.by(Sort.Direction.DESC, "email");
      default -> Sort.by(Sort.Direction.ASC, "name");
    };
  }

  public Sort getSortOrderForOrders(String sort) {
    return switch (sort) {
      case "manager_asc" -> Sort.by(Sort.Direction.ASC, "user.name");
      case "manager_desc" -> Sort.by(Sort.Direction.DESC, "user.name");
      case "company_asc" -> Sort.by(Sort.Direction.ASC, "company.name");
      case "company_desc" -> Sort.by(Sort.Direction.DESC, "company.name");
      case "date_desc" -> Sort.by(Sort.Direction.DESC, "createdAt");
      case "total_asc" -> Sort.by(Sort.Direction.ASC, "totalAmount");
      case "total_desc" -> Sort.by(Sort.Direction.DESC, "totalAmount");
      default -> Sort.by(Sort.Direction.ASC, "createdAt");
    };
  }

  public Sort getSortOrderForStoreHistory(String sort) {
    return switch (sort) {
      case "good_name_asc" -> Sort.by(Sort.Direction.ASC, "goodName");
      case "good_name_desc" -> Sort.by(Sort.Direction.DESC, "goodName");
      case "purchase_price_asc" -> Sort.by(Sort.Direction.ASC, "purchasePrice");
      case "purchase_price_desc" -> Sort.by(Sort.Direction.DESC, "purchasePrice");
      case "arrived_total_asc" -> Sort.by(Sort.Direction.ASC, "arrivedTotal");
      case "arrived_total_desc" -> Sort.by(Sort.Direction.DESC, "arrivedTotal");
      case "consumption_total_asc" -> Sort.by(Sort.Direction.ASC, "consumptionTotal");
      case "consumption_total_desc" -> Sort.by(Sort.Direction.DESC, "consumptionTotal");
      case "reason_asc" -> Sort.by(Sort.Direction.ASC, "reason");
      case "reason_desc" -> Sort.by(Sort.Direction.DESC, "reason");
      case "date_desc" -> Sort.by(Sort.Direction.DESC, "createdAt");
      default -> Sort.by(Sort.Direction.ASC, "createdAt");
    };
  }
}

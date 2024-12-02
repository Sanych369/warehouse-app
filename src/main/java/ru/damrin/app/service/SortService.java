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
}

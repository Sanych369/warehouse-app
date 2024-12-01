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
}

package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.damrin.app.db.repository.CategoryEntityRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class WarehouseService {

  private final CategoryEntityRepository repository;

}

package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.repository.CompanyRepository;
import ru.damrin.app.mapper.CompanyMapper;
import ru.damrin.app.model.company.CompanyDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

  private final SortService sortService;
  private final CompanyMapper companyMapper;
  private final CompanyRepository repository;

  public List<CompanyDto> getAllCompanies() {
    return repository.findAll().stream()
        .map(companyMapper::toDto)
        .toList();
  }

  public List<CompanyDto> getAllActiveCompanies() {
    return repository.findAllByIsActiveIsTrue().stream()
        .map(companyMapper::toDto)
        .toList();
  }

  public List<CompanyDto> getAllInactiveCompanies() {
    return repository.findAllByIsActiveIsFalse().stream()
        .map(companyMapper::toDto)
        .toList();
  }

  public Page<CompanyDto> getCompaniesPage(String name,
                                           String address,
                                           String phone,
                                           String email,
                                           Boolean isActive,
                                           String sort,
                                           int page,
                                           int size) {

    Sort sortOrder = sortService.getSortOrderForCompanies(sort);
    Pageable pageable = PageRequest.of(page, size, sortOrder != null ? sortOrder : Sort.unsorted());

    return repository.findByFilters(name, address, phone, email, isActive, pageable).map(companyMapper::toDto);
  }

  // при добавлении флаг не обязателен - сразу активная фирма
  public void addCompany(CompanyDto companyDto) {
    log.info("Add company: {} with address: {}", companyDto.name(), companyDto.address());
    var company = companyMapper.toEntity(companyDto);
    var entity = repository.save(company);
    log.info("Added company: {} for id {}", company, entity.getId());
  }

  //Не удаляем, делаем неактивной, иначе упадёт история заказов
  public void deactivateCompanyById(Long id) {
    log.info("Deactivate company by id: {}", id);
    var company = repository.findById(id).orElseThrow(
        () -> new WarehouseAppException("Компания не найдена"));
    company.setIsActive(false);
    repository.save(company);
  }

  // Тут нужна пропертя active bool
  public void changeCompany(CompanyDto companyDto) {
    var company = repository.findById(companyDto.id())
        .orElseThrow(() -> new WarehouseAppException("Компания не найдена"));
    var companyName = company.getName();

    log.info("Started change company with name {} process", companyName);
    companyMapper.partialUpdate(companyDto, company);
    repository.save(company);
    log.info("Company with name {} was changed", companyName);
  }
}

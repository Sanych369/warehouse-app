package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

  private final CompanyMapper companyMapper;
  private final CompanyRepository repository;

  public List<CompanyDto> getAllCompanies() {
    return repository.findAll().stream()
        .map(companyMapper::toDto)
        .toList();
  }

  public List<CompanyDto> getAllActiveCompanies() {
    return repository.findAllByActiveIsTrue().stream()
        .map(companyMapper::toDto)
        .toList();
  }

  public List<CompanyDto> getAllInactiveCompanies() {
    return repository.findAllByActiveIsFalse().stream()
        .map(companyMapper::toDto)
        .toList();
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
    var company = repository.findById(id).orElseThrow(() -> new WarehouseAppException("Company not found"));
    company.setActive(false);
    repository.save(company);
  }

  // Тут нужна пропертя active bool
  public void changeCompany(CompanyDto companyDto) {
    var company = repository.findById(companyDto.id())
        .orElseThrow(() -> new WarehouseAppException("Company not found"));
    var companyName = company.getName();

    log.info("Started change company with name {} process", companyName);
    companyMapper.partialUpdate(companyDto, company);
    repository.save(company);
    log.info("Company with name {} was changed", companyName);
  }
}

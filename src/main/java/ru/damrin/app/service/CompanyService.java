package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.db.repository.CompanyEntityRepository;
import ru.damrin.app.mapper.CompanyMapper;
import ru.damrin.app.model.company.CompanyDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyMapper companyMapper;
  private final CompanyEntityRepository repository;

  public List<CompanyDto> getAllCompanies() {
    return repository.findAll().stream()
        .map(companyMapper::toDto)
        .toList();
  }

  public void addCompany(CompanyDto companyDto) {
    log.info("Add company: {} with address: {}", companyDto.name(), companyDto.address());
    var company = companyMapper.toEntity(companyDto);
    var entity = repository.save(company);
    log.info("Added company: {} for id {}", company, entity.getId());
  }

  public void deleteCompanyById(Long id) {
    log.info("Delete company by id: {}", id);
    repository.deleteById(id);
  }

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

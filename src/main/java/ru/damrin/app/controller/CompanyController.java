package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.damrin.app.common.exception.WarehouseAppException;
import ru.damrin.app.model.company.CompanyDto;
import ru.damrin.app.service.CompanyService;

import java.util.List;

import static java.util.Objects.isNull;

/**
 * Контроллер для управления контрагентами.
 */
@Controller
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  @GetMapping("/page")
  public ResponseEntity<Page<CompanyDto>> getCompaniesPage(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String address,
      @RequestParam(required = false) String phone,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) Boolean isActive,
      @RequestParam(required = false) String sort) {

    final var companies = companyService.getCompaniesPage(name, address, phone, email, isActive, sort, page, size);
    return ResponseEntity.ok(companies);
  }

  @GetMapping("/list")
  public ResponseEntity<List<CompanyDto>> getAllCompanies() {
    return ResponseEntity.ok(companyService.getAllCompanies());
  }

  @GetMapping("/list/active")
  public ResponseEntity<List<CompanyDto>> getAllActiveCompanies() {
    return ResponseEntity.ok(companyService.getAllActiveCompanies());
  }

  @GetMapping("/list/inactive")
  public ResponseEntity<List<CompanyDto>> getAllInactiveCompanies() {
    return ResponseEntity.ok(companyService.getAllInactiveCompanies());
  }

  @PostMapping("/add")
  public ResponseEntity<Void> addCompany(@RequestBody CompanyDto companyDto) {
    checkCompanyDto(companyDto);
    companyService.addCompany(companyDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/deactivate")
  public ResponseEntity<Void> deactivateCompany(@RequestParam Long id) {
    companyService.deactivateCompanyById(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/change")
  public ResponseEntity<Void> changeCompany(@RequestBody CompanyDto companyDto) {
    checkCompanyDto(companyDto);
    companyService.changeCompany(companyDto);
    return ResponseEntity.ok().build();
  }

  private void checkCompanyDto(CompanyDto companyDto) {
    if (isNull(companyDto) || StringUtils.isEmpty(companyDto.name())
        || StringUtils.isEmpty(companyDto.address())) {
      throw new WarehouseAppException("Наименование компании и адрес не могут быть пустыми. Заполните данные");
    }
  }
}

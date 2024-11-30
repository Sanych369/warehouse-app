package ru.damrin.app.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
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

@Controller
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {

  private final CompanyService companyService;

  @GetMapping("/list")
  public ResponseEntity<List<CompanyDto>> getAllCompanies() {
    return ResponseEntity.ok(companyService.getAllCompanies());
  }

  @PostMapping("/add")
  public ResponseEntity<Void> addCategory(@RequestBody CompanyDto companyDto) {
    checkCompanyDto(companyDto);
    companyService.addCompany(companyDto);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @DeleteMapping("/delete")
  public ResponseEntity<Void> deleteCategoryById(@RequestParam Long id) {
    companyService.deleteCompanyById(id);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/change")
  public ResponseEntity<Void> changeCategory(@RequestBody CompanyDto companyDto) {
    checkCompanyDto(companyDto);
    companyService.changeCompany(companyDto);
    return ResponseEntity.ok().build();
  }

  private void checkCompanyDto(CompanyDto companyDto) {
    if (isNull(companyDto) || StringUtils.isEmpty(companyDto.name())
        || StringUtils.isEmpty(companyDto.address())) {
      throw new WarehouseAppException("Company name and address cannot be empty!");
    }
  }
}

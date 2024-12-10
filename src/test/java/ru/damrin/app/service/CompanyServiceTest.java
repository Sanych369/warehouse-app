package ru.damrin.app.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.damrin.app.db.entity.CompanyEntity;
import ru.damrin.app.db.repository.CompanyRepository;
import ru.damrin.app.mapper.CompanyMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static utils.PrepareUtil.CATEGORY_NAME;
import static utils.PrepareUtil.COMPANY_ADDRESS;
import static utils.PrepareUtil.COMPANY_EMAIL;
import static utils.PrepareUtil.COMPANY_ID;
import static utils.PrepareUtil.COMPANY_NAME;
import static utils.PrepareUtil.COMPANY_PHONE;
import static utils.PrepareUtil.prepareCompany;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

  @Spy
  private CompanyMapper mapper = Mappers.getMapper(CompanyMapper.class);

  @Mock
  private SortService sortService;

  @Mock
  private CompanyRepository repository;

  @InjectMocks
  private CompanyService companyService;

  @Captor
  private ArgumentCaptor<CompanyEntity> changedEntityCaptor;

  @Test
  void getAllCompanies() {
    when(repository.findAll()).thenReturn(List.of(new CompanyEntity()));

    companyService.getAllCompanies();

    verify(repository, times(1)).findAll();
    verify(mapper, times(1)).toDto(any());
  }

  @Test
  void getAllActiveCompanies() {
    when(repository.findAllByIsActiveIsTrue()).thenReturn(List.of(new CompanyEntity()));
    companyService.getAllActiveCompanies();

    verify(repository, times(1)).findAllByIsActiveIsTrue();
    verify(mapper, times(1)).toDto(any());
  }

  @Test
  void getAllInactiveCompanies() {
    when(repository.findAllByIsActiveIsFalse()).thenReturn(List.of(new CompanyEntity()));
    companyService.getAllInactiveCompanies();

    verify(repository, times(1)).findAllByIsActiveIsFalse();
    verify(mapper, times(1)).toDto(any());
  }

  @Test
  void getCompaniesPage() {
    when(repository.findByFilters(any(), any(), any(), any(), any(), any())).thenReturn(mock());

    companyService.getCompaniesPage(CATEGORY_NAME, COMPANY_ADDRESS, COMPANY_PHONE, COMPANY_EMAIL, true, "name_desc", 1, 10);

    verify(sortService, times(1)).getSortOrderForCompanies("name_desc");
    verify(repository, times(1)).findByFilters(any(), any(), any(), any(), any(), any());
  }

  @Test
  void addCompany() {
    when(repository.save(any())).thenReturn(mock(CompanyEntity.class));

    companyService.addCompany(prepareCompany(true));

    verify(repository, times(1)).save(any());
    verify(mapper, times(1)).toEntity(any());
  }

  @Test
  void deactivateCompanyById() {
    when(repository.save(any())).thenReturn(mock(CompanyEntity.class));

    companyService.addCompany(prepareCompany(true));

    verify(repository, times(1)).save(changedEntityCaptor.capture());
    verify(mapper, times(1)).toEntity(any());

    final var changedEntity = changedEntityCaptor.getValue();
    assertNotNull(changedEntity);
    assertEquals(COMPANY_ID, changedEntity.getId());
    assertEquals(COMPANY_NAME, changedEntity.getName());
    assertEquals(COMPANY_ADDRESS, changedEntity.getAddress());
    assertEquals(COMPANY_PHONE, changedEntity.getPhone());
    assertEquals(COMPANY_EMAIL, changedEntity.getEmail());
  }

  @Test
  void changeCompany() {
  }
}
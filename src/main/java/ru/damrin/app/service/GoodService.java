package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GoodService {

//  private final GoodMapper mapper;
//  private final GoodRepository repository;
//  private final GoodRepository goodRepository;
//
//  //Пока так, но вероятно, нужна будет пагинация
//  public List<GoodDto> findAll() {
//    return mapper.toGoodDtoList(repository.findAll());
//  }
//
//  public void addGood(GoodDto goodDto) {
//    var good = mapper.toEntity(goodDto);
//    goodRepository.save(good);
//  }
//
//  public GoodDto getGoodById(Long id) {
//    var good = goodRepository.findById(id)
//        .orElseThrow(() -> new RuntimeException("Good not found"));
//
//    return new GoodDto(
//        good.getId(),
//        good.getName(),
//        good.getSalePrice(),
////        good.getCategory().getName(),
//        good.getBalance()
//    );
//  }
//
//
//  public void updateGood(GoodDto goodDto) {
//    GoodEntity good = goodRepository.findById(goodDto.id())
//        .orElseThrow(() -> new RuntimeException("Good not found"));
//
//    good.setName(goodDto.name());
////    good.setPrice(goodDto.price());
////    good.setQuantity(goodDto.quantity());
//    goodRepository.save(good);
//  }
//
//
//  public void deleteGoodById(Long id) {
//    goodRepository.deleteById(id);
//  }
}
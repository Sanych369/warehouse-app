package ru.damrin.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.damrin.app.db.entity.GoodEntity;
import ru.damrin.app.db.repository.GoodRepository;
import ru.damrin.app.model.GoodDto;

//@Service
//@RequiredArgsConstructor
//public class GoodService {
//
//    private final GoodRepository goodRepository;
//
//    public void addGood(GoodDto goodDto) {
//
//        GoodEntity good = GoodEntity.builder()
//                .name(goodDto.name())
//                .category(goodDto.category())
//                .price(goodDto.price)
//                .quantity(goodDto.quantity())
//                .build();
//
//        goodRepository.save(good);
//    }
//
//    public GoodDto getGoodById(Long id) {
//        GoodEntity good = goodRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Good not found"));
//
//        return new GoodDto(
//                good.getId(),
//                good.getName(),
//                good.getPrice(),
//                good.getCategory().getCategory(),
//                good.getQuantity()
//        );
//    }
//
//
//    public void updateGood(GoodDto goodDto) {
//        GoodEntity good = goodRepository.findById(goodDto.id())
//                .orElseThrow(() -> new RuntimeException("Good not found"));
//
//        good.setName(goodDto.name());
//        good.setPrice(goodDto.price());
//        good.setQuantity(goodDto.quantity());
//        goodRepository.save(good);
//    }
//
//
//    public void deleteGoodById(Long id) {
//        goodRepository.deleteById(id);
//    }
//}
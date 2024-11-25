package ru.damrin.app.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import ru.damrin.app.model.GoodDto;
//import ru.damrin.app.service.GoodService;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/goods")
//@RequiredArgsConstructor
//public class GoodsController {
//
//    private final GoodService goodService;
//
//    @GetMapping("/list")
//    public ResponseEntity<List<GoodDto>> getAllGoods() {
//        List<GoodDto> goods = goodService.getAllGoods();
//        return ResponseEntity.ok(goods);
//    }
//
//    @PostMapping("/add")
//    public ResponseEntity<Void> addGood(@RequestBody GoodDto goodDto) {
//        goodService.addGood(goodDto);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }
//
//    @GetMapping("/getById")
//    public ResponseEntity<GoodDto> getGoodById(@RequestParam Long id) {
//        GoodDto good = goodService.getGoodById(id);
//        return ResponseEntity.ok(good);
//    }
//
//    @PutMapping("/edit")
//    public ResponseEntity<Void> editGood(@RequestBody GoodDto goodDto) {
//        goodService.updateGood(goodDto);
//        return ResponseEntity.ok().build();
//    }
//
//    @DeleteMapping("/delete")
//    public ResponseEntity<Void> deleteGoodById(@RequestParam Long id) {
//        goodService.deleteGoodById(id);
//        return ResponseEntity.noContent().build();
//    }
//}
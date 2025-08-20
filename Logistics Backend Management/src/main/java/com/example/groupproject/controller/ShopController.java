package com.example.groupproject.controller;

import com.example.groupproject.model.PageBean;
import com.example.groupproject.model.Result;
import com.example.groupproject.model.Shop;
import com.example.groupproject.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class ShopController {
    @Autowired
    private ShopService shopService;

    //注册店铺
    @PostMapping("/shop")
    public Result addShop(@RequestBody Shop shop){
        log.info("注册店铺：{}", shop);
        shopService.addShop(shop);
        return Result.success();
    }

    //注销店铺
    @DeleteMapping("/shop")
    public Result deleteShop(Long id){
        log.info("注销店铺:{}", id);
        shopService.deleteShop(id);
        return Result.success();
    }

    //显示所有店铺
    @GetMapping("/shop/list")
    public Result listAllShop(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer pageSize){
        log.info("显示所有店铺");
        PageBean pageBean = shopService.listAllShop(page, pageSize);
        return !pageBean.getRows().isEmpty() ? Result.success(pageBean) : Result.error("暂无店铺信息");
    }

    //ID查询店铺
    @GetMapping("/shop/{id}")
    public Result selectShopById(@PathVariable Long id){
        log.info("ID查询店铺：{}", id);
        Shop shop = shopService.selectShopById(id);
        return shop != null ? Result.success(shop) : Result.error("无ID为" + id + "的店铺");
    }

    //条件查询店铺
    @GetMapping("/shop")
    public Result listShop(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer pageSize, String name, Short shopType){
        log.info("条件查询店铺：{}, {}, {}, {}", page, pageSize, name, shopType);
        PageBean pageBean = shopService.listShop(page, pageSize, name, shopType);
        return !pageBean.getRows().isEmpty() ? Result.success(pageBean) : Result.error("无查询结果");
    }

    //修改店铺信息
    @PutMapping("/shop")
    public Result updateShop(@RequestBody Shop shop){
        log.info("修改店铺信息：{}", shop);
        shopService.updateShop(shop);
        return Result.success();
    }
}

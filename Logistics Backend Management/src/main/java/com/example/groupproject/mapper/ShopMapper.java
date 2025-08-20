package com.example.groupproject.mapper;

import com.example.groupproject.model.Shop;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopMapper {
    void addShop(Shop shop);

    void deleteShop(Long id);

    List<Shop> listAllShop();

    Shop selectShopById(Long id);

    List<Shop> listShop(String name, Short shopType);

    void updateShop(Shop shop);
}

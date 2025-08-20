package com.example.groupproject.service;

import com.example.groupproject.model.PageBean;
import com.example.groupproject.model.Shop;

import java.util.List;
public interface ShopService {
    void addShop(Shop shop);

    void deleteShop(Long id);

    Shop selectShopById(Long id);

    void updateShop(Shop shop);

    PageBean listShop(Integer page, Integer pageSize, String name, Short shopType);

    PageBean listAllShop(Integer page, Integer pageSize);
}

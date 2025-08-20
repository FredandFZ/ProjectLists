package com.example.groupproject.service.impl;

import com.example.groupproject.mapper.ShopMapper;
import com.example.groupproject.model.PageBean;
import com.example.groupproject.model.Shop;
import com.example.groupproject.service.ShopService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopMapper shopMapper;
    @Override
    public void addShop(Shop shop) {
        shop.setCreateTime(LocalDateTime.now());
        shop.setUpdateTime(LocalDateTime.now());
        shopMapper.addShop(shop);
    }

    @Override
    public void deleteShop(Long id) {
        shopMapper.deleteShop(id);
    }

    @Override
    public Shop selectShopById(Long id) {
        return shopMapper.selectShopById(id);
    }

    @Override
    public void updateShop(Shop shop) {
        shop.setUpdateTime(LocalDateTime.now());
        shopMapper.updateShop(shop);
    }

    @Override
    public PageBean listShop(Integer page, Integer pageSize, String name, Short shopType) {
        PageHelper.startPage(page, pageSize);
        List<Shop> shopList = shopMapper.listShop(name, shopType);
        Page<Shop> p = (Page<Shop>) shopList;
        return new PageBean(p.getTotal(), p.getResult());
    }

    @Override
    public PageBean listAllShop(Integer page, Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<Shop> shopList = shopMapper.listAllShop();
        Page<Shop> p = (Page<Shop>) shopList;
        return new PageBean(p.getTotal(), p.getResult());
    }
}

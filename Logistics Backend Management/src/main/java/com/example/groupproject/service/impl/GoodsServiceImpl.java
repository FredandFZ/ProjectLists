package com.example.groupproject.service.impl;

import com.example.groupproject.dto.CommentDetail;
import com.example.groupproject.dto.GoodsDetail;
import com.example.groupproject.mapper.GoodsMapper;
import com.example.groupproject.mapper.ShopMapper;
import com.example.groupproject.mapper.UserMapper;
import com.example.groupproject.model.*;
import com.example.groupproject.service.GoodsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ShopMapper shopMapper;

    private GoodsDetail generateGoodsDetail(Goods goods){
        GoodsDetail goodsDetail = new GoodsDetail();
        goodsDetail.setId(goods.getId());
        goodsDetail.setName(goods.getName());
        goodsDetail.setPrice(goods.getPrice());
        goodsDetail.setDiscount(goods.getDiscount());
        goodsDetail.setGoodsType(goods.getGoodsType());
        goodsDetail.setGoodsBrand(goods.getGoodsBrand());
        goodsDetail.setCollage(goods.getCollage());
        goodsDetail.setPicture(goods.getPicture());
        goodsDetail.setIfCheck(goods.getIfCheck());
        goodsDetail.setCreateTime(goods.getCreateTime());
        goodsDetail.setUpdateTime(goods.getUpdateTime());
        goodsDetail.setShopId(goods.getShopId());

        Shop shop = shopMapper.selectShopById(goods.getShopId());
        goodsDetail.setShopName(shop.getName());
        goodsDetail.setShopType(shop.getShopType());

        return goodsDetail;
    }

    private CommentDetail generateCommentDetail(Comment comment){
        CommentDetail commentDetail = new CommentDetail();
        commentDetail.setId(comment.getId());
        commentDetail.setContent(comment.getContent());
        commentDetail.setPicture(comment.getPicture());
        commentDetail.setIfAnonymous(comment.getIfAnonymous());
        commentDetail.setIfAppended(comment.getIfAppended());
        commentDetail.setCreateTime(comment.getCreateTime());
        commentDetail.setUpdateTime(comment.getUpdateTime());

        Goods goods = goodsMapper.selectGoodsById(comment.getGoodsId());
        commentDetail.setGoodsId(goods.getId());
        commentDetail.setGoodsName(goods.getName());
        commentDetail.setPrice(goods.getPrice());
        commentDetail.setDiscount(goods.getDiscount());
        commentDetail.setGoodsType(goods.getGoodsType());
        commentDetail.setGoodsBrand(goods.getGoodsBrand());
        commentDetail.setGoodsPicture(goods.getPicture());

        User user = userMapper.selectUserById(comment.getUserId());
        commentDetail.setUsername(user.getName());
        commentDetail.setUserId(user.getId());

        Shop shop = shopMapper.selectShopById(goods.getShopId());
        commentDetail.setShopName(shop.getName());
        commentDetail.setShopId(shop.getId());

        return commentDetail;
    }

    private String storeImage(MultipartFile image) throws IOException {
        if(image == null) return null;
        String originalFilename = image.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String extname = originalFilename.substring(index);
        String newFilename = UUID.randomUUID().toString() + extname;
        log.info("新文件名:{}", newFilename);

        String pathname = "C:\\images\\" + newFilename;
        image.transferTo(new File(pathname));
        return pathname;
    }

    @Override
    public void addGoods(String name, Double price, Double discount, String goodsType, String goodsBrand, Integer collage, Short ifCheck, Long shopId, MultipartFile picture) throws IOException {
        String pathname = storeImage(picture);

        Goods goods = new Goods();
        goods.setName(name);
        goods.setPrice(price);
        goods.setDiscount(discount);
        goods.setGoodsType(goodsType);
        goods.setGoodsBrand(goodsBrand);
        goods.setCollage(collage);
        goods.setIfCheck(ifCheck);
        goods.setShopId(shopId);
        goods.setPicture(pathname);
        goods.setIfDelete((short)0);
        goods.setCreateTime(LocalDateTime.now());
        goods.setUpdateTime(LocalDateTime.now());
        goodsMapper.addGoods(goods);
    }


    public void deleteGoods(Long id){
        goodsMapper.deleteGoods(id);
    }

    @Override
    public List<GoodsDetail> selectGoods(String name, Double priceBegin, Double priceEnd, String goodsType, String goodsBrand) {
        List<Goods> listgoods=goodsMapper.selectGoods(name,priceBegin,priceEnd,goodsType,goodsBrand);
        List<GoodsDetail> goodsDetailList = new LinkedList<>();
        for(Goods goods : listgoods){
            goodsDetailList.add(generateGoodsDetail(goods));
        }
        return goodsDetailList;
    }

    @Override
    public PageBean selectGoodsByshop(Integer page, Integer pageSize, Long shopId) {
        PageHelper.startPage(page, pageSize);
        List<Goods> goodsList = goodsMapper.selectGoodsByShop(shopId);
        Page<Goods> p = (Page<Goods>) goodsList;
        return new PageBean(p.getTotal(), p.getResult());
    }

    @Override
    public void addComment(Long userId, Long goodsId, String content, Short ifAnonymous, Long ifAppended, MultipartFile picture) throws IOException {
        String pathname = storeImage(picture);

        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setGoodsId(goodsId);
        comment.setContent(content);
        comment.setIfAnonymous(ifAnonymous);
        comment.setIfAppended(ifAppended);
        comment.setPicture(pathname);
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        goodsMapper.addComment(comment);
    }

    @Override
    public void deleteComment(Long id) {
        List<Comment> commentList = goodsMapper.selectAppendedComment(id);
        List<Long> commentIdList = new LinkedList<>();
        for(Comment comment : commentList){
            commentIdList.add(comment.getId());
        }
        commentIdList.add(id);
        goodsMapper.deleteComment(commentIdList);
    }

    @Override
    public List<CommentDetail> listComment(Long goodsId, Long userId) {
        List<Comment> commentList = goodsMapper.selectComment(goodsId, userId);
        List<CommentDetail> commentDetailList = new LinkedList<>();
        for(Comment comment : commentList){
            commentDetailList.add(generateCommentDetail(comment));
        }
        return commentDetailList;
    }

    @Override
    public void updateComment(String content, MultipartFile picture, Long id) throws IOException {
        Comment comment = new Comment();
        String pathname = storeImage(picture);
        if(pathname != null) comment.setPicture(pathname);
        comment.setContent(content);
        comment.setUpdateTime(LocalDateTime.now());
        comment.setId(id);
        goodsMapper.updateComment(comment);
    }

    @Override
    public void updateGoods(String name, Double price, Double discount, String goodsType, String goodsBrand, Integer collage, Short ifCheck, MultipartFile picture, Long id) throws IOException {
        Goods goods = new Goods();
        String pathname = storeImage(picture);
        if(pathname != null) goods.setPicture(pathname);
        goods.setName(name);
        goods.setPrice(price);
        goods.setDiscount(discount);
        goods.setGoodsType(goodsType);
        goods.setGoodsBrand(goodsBrand);
        goods.setCollage(collage);
        goods.setIfCheck(ifCheck);
        goods.setUpdateTime(LocalDateTime.now());
        goods.setId(id);
        goodsMapper.updateGoods(goods);
    }

    @Override
    public CommentDetail selectCommentById(Long id) {
        Comment comment = goodsMapper.selectCommentById(id);
        return generateCommentDetail(comment);
    }

    @Override
    public GoodsDetail selectGoodsById(Long id) {
        Goods goods = goodsMapper.selectGoodsById(id);
        return generateGoodsDetail(goods);
    }
}

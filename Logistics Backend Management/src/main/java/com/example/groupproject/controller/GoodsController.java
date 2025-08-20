package com.example.groupproject.controller;

import com.example.groupproject.dto.CommentDetail;
import com.example.groupproject.dto.GoodsDetail;
import com.example.groupproject.model.*;
import com.example.groupproject.service.GoodsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    //添加商品
    @PostMapping("/goods")
    public Result addGoods(String name, Double price, Double discount, String goodsType, String goodsBrand,@RequestParam(defaultValue = "0") Integer collage, Short ifCheck, Long shopId, MultipartFile picture) throws IOException {
        log.info("添加商品：{},{},{},{},{},{},{},{}", name, price, discount, goodsType, collage, ifCheck, shopId, picture);

        goodsService.addGoods(name, price, discount, goodsType, goodsBrand, collage, ifCheck, shopId, picture);
        return Result.success();
    }


    //条件查询
    @GetMapping("/goods")
    public Result selectGoods(String name,Double priceBegin,Double priceEnd, String goodsType,String goodsBrand){
        log.info("条件查询商品：{}，{}，{}，{}，{}", name,priceBegin,priceEnd,goodsType,goodsBrand);
        List<GoodsDetail> listgoods = goodsService.selectGoods(name,priceBegin,priceEnd,goodsType,goodsBrand);
        return !listgoods.isEmpty() ? Result.success(listgoods) : Result.error("无查询结果");
    }
    //删除商品
    @DeleteMapping("/goods")
    public Result deleteGoods(Long id){
        log.info("删除商品:{}", id);
        goodsService.deleteGoods(id);
        return Result.success();
    }
    //显示一个商店的所有商品
    @GetMapping("/goods/shop/{shopId}")
    public Result selectGoodsByShop(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "20") Integer pageSize, @PathVariable Long shopId){
        log.info("显示商店商品：{}, {}, {}", page, pageSize, shopId);
        PageBean pageBean = goodsService.selectGoodsByshop(page, pageSize, shopId);
        return !pageBean.getRows().isEmpty() ? Result.success(pageBean) : Result.error("无查询结果");
    }

    //添加评论
    @PostMapping("/comment")
    public Result addComment(Long userId, Long goodsId, String content, @RequestParam(defaultValue = "0") Short ifAnonymous, @RequestParam(defaultValue = "0") Long ifAppended, MultipartFile picture) throws IOException {
        log.info("添加评论:{}, {}, {}, {}, {}, {}", userId, goodsId, content, ifAnonymous, ifAppended, picture);
        goodsService.addComment(userId, goodsId, content, ifAnonymous, ifAppended, picture);
        return Result.success();
    }

    //删除评论
    @DeleteMapping("/comment")
    public Result deleteComment(Long id){
        log.info("删除评论:{}", id);
        goodsService.deleteComment(id);
        return Result.success();
    }

    //显示商品所有评论
    @GetMapping("/comment/shop/{goodsId}")
    public Result listShopComment(@PathVariable Long goodsId){
        log.info("显示商品所有评论:{}", goodsId);
        List<CommentDetail> commentDetailList = goodsService.listComment(goodsId, null);
        return !commentDetailList.isEmpty() ? Result.success(commentDetailList) : Result.error("暂无评论");
    }

    //显示用户所有评论
    @GetMapping("/comment/user/{userId}")
    public Result listUserComment(@PathVariable Long userId){
        log.info("显示用户所有评论:{}", userId);
        List<CommentDetail> commentDetailList = goodsService.listComment(null, userId);
        return !commentDetailList.isEmpty() ? Result.success(commentDetailList) : Result.error("暂无评论");
    }

    //修改评论
    @PutMapping("/comment")
    public Result updateComment(String content, MultipartFile picture, Long id) throws IOException {
        log.info("修改评论:{}, {}, {}", content, picture, id);
        goodsService.updateComment(content, picture, id);
        return Result.success();
    }

    //修改商品信息
    @PutMapping("/goods")
    public Result updateGoods(String name, Double price, Double discount, String goodsType, String goodsBrand, Integer collage, Short ifCheck, MultipartFile picture, Long id) throws IOException {
        log.info("修改商品信息:{}, {}, {}, {}, {}, {}, {}, {}, {}", name, price, discount, goodsType, goodsBrand, collage, ifCheck, picture, id);
        goodsService.updateGoods(name, price, discount, goodsType, goodsBrand, collage, ifCheck, picture, id);
        return Result.success();
    }

    //ID查询评论
    @GetMapping("/comment/{id}")
    public Result selectCommentById(@PathVariable Long id){
        log.info("ID查询评论:{}", id);
        CommentDetail commentDetail = goodsService.selectCommentById(id);
        return commentDetail != null ? Result.success(commentDetail) : Result.error("无查询结果");
    }

    //ID查询商品
    @GetMapping("/goods/{id}")
    public Result selectGoodsById(@PathVariable Long id){
        log.info("ID查询商品:{}", id);
        GoodsDetail goodsDetail = goodsService.selectGoodsById(id);
        return goodsDetail != null ? Result.success(goodsDetail) : Result.error("无查询结果");
    }
}

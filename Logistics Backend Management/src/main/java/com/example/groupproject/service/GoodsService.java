package com.example.groupproject.service;

import com.example.groupproject.dto.CommentDetail;
import com.example.groupproject.dto.GoodsDetail;
import com.example.groupproject.model.Comment;
import com.example.groupproject.model.Goods;
import com.example.groupproject.model.PageBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface GoodsService {

    void addGoods(String name, Double price, Double discount, String goodsType, String goodsBrand, Integer collage, Short ifCheck, Long shopId, MultipartFile picture) throws IOException;

    void deleteGoods(Long id);

    List<GoodsDetail> selectGoods(String name, Double priceBegin, Double priceEnd, String goodsType, String goodsBrand);

    PageBean selectGoodsByshop(Integer page, Integer pageSize, Long shopId);

    void addComment(Long userId, Long goodsId, String content, Short ifAnonymous, Long ifAppended, MultipartFile picture) throws IOException;

    void deleteComment(Long id);

    List<CommentDetail> listComment(Long goodsId, Long userId);

    void updateComment(String content, MultipartFile picture, Long id) throws IOException;

    void updateGoods(String name, Double price, Double discount, String goodsType, String goodsBrand, Integer collage, Short ifCheck, MultipartFile picture, Long id) throws IOException;

    CommentDetail selectCommentById(Long id);

    GoodsDetail selectGoodsById(Long id);
}

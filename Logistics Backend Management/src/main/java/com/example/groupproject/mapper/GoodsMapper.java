package com.example.groupproject.mapper;

import com.example.groupproject.model.Comment;
import com.example.groupproject.model.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {
    void addGoods(Goods goods);

    List<Goods> selectGoodsByName(String name);

    void deleteGoods(Long id);

    List<Goods> selectGoods(String name, Double priceBegin, Double priceEnd, String goodsType, String goodsBrand);

    List<Goods> selectGoodsByShop(Long shopId);

    Goods selectGoodsById(Long id);

    void addComment(Comment comment);

    void deleteComment(List<Long> commentIdList);

    List<Comment> selectComment(Long goodsId, Long userId);

    List<Comment> selectAppendedComment(Long id);

    Comment selectCommentById(Long id);

    void updateComment(Comment comment);

    void updateGoods(Goods goods);
}

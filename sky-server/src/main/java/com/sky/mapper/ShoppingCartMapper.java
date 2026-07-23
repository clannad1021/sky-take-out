package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    void addShoppingCart(ShoppingCart shoppingCart);

    ShoppingCart selectShoppingCart(ShoppingCart shoppingCart);

    void updateShoppingCart(ShoppingCart shoppingCart);

    List<ShoppingCart> selectAllShoppingCart(Long userId);


    void deleteShoppingCart(ShoppingCart shoppingCart);

    @Delete("delete from shopping_cart where user_id = #{userId}")
    void cleanShoppingCart(Long userId);
}

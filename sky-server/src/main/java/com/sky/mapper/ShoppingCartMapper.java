package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartMapper {


    void addShoppingCart(ShoppingCart shoppingCart);

    ShoppingCart selectShoppingCart(ShoppingCart shoppingCart);

    void updateShoppingCart(ShoppingCart shoppingCart);
}

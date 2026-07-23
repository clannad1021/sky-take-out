package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartSenvice;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartSenviceImpl implements ShoppingCartSenvice {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart = new ShoppingCart();
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        ShoppingCart shoppingCartOrd = shoppingCartMapper.selectShoppingCart(shoppingCart);
        //判断当前加入购物车的商品数据库中是否存在
        if (shoppingCartOrd != null) {
            shoppingCartOrd.setNumber(shoppingCartOrd.getNumber() + 1);
            shoppingCartMapper.updateShoppingCart(shoppingCartOrd);

        } else {
            if (shoppingCart.getDishId() != null) { //判断是不是菜品
                Dish dish = dishMapper.getById(shoppingCart.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());

            }else {
                Setmeal setmeal = setmealMapper.getById(shoppingCart.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.addShoppingCart(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> selectShoppingCart() {

        return shoppingCartMapper.selectAllShoppingCart(BaseContext.getCurrentId());

    }

    @Override
    public void deleteShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        shoppingCart.setUserId(BaseContext.getCurrentId());
        ShoppingCart shoppingCartOrd = shoppingCartMapper.selectShoppingCart(shoppingCart);
        Integer number = shoppingCartOrd.getNumber();

        //查询购物车中该商品的数量是否多件
        if (number >= 2) {
            shoppingCartOrd.setNumber(number - 1);
            shoppingCartMapper.updateShoppingCart(shoppingCartOrd);

        }else  {
            shoppingCartMapper.deleteShoppingCart(shoppingCart);
        }
    }

    @Override
    public void cleanShoppingCart() {

        shoppingCartMapper.cleanShoppingCart(BaseContext.getCurrentId());
    }

}

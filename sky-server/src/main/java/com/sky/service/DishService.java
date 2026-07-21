package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void addDish(DishDTO dishDTO);

    PageResult dishSelect(DishPageQueryDTO dishPageQueryDTO);

    void delectDish(List<Long> ids);

    DishVO selsctDishById(Long id);

    void updateDish(DishDTO dishDTO);
}

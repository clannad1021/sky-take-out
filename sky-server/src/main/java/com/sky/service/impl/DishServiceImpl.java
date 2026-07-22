package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addDish(dish);
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(flavor -> {
                flavor.setDishId(dishId);
            });
            dishFlavorMapper.add(flavors);
        }

    }

    @Override
    public PageResult dishSelect(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = (Page)dishMapper.dishSelect(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void delectDish(List<Long> ids) {
        List<Long> dishId = new ArrayList<>();
        //遍历要删除的菜品，
        for (Object id : ids) {
            Dish dish = dishMapper.getById((Long) id);
            SetmealDish setmeal = dishMapper.setmealGetById(id);
            if (dish.getStatus() == StatusConstant.ENABLE){ //如果有启售中的就抛出异常
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }else if ( setmealMapper.getSetmealByDishId((Long)id) != null){ //如果有关联了套餐的就抛出异常
                throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
            }
            dishId.add((Long)id);

        }


        dishMapper.deleteDish(ids);
        dishFlavorMapper.delectSetmeal(dishId);

    }

    @Override
    public DishVO selsctDishById(Long id) {
        Dish dish = dishMapper.getById(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        List<DishFlavor> flavorslist = dishFlavorMapper.selectByDishId(id);
        dishVO.getFlavors().addAll(flavorslist);
        return dishVO;
    }

    @Override
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updatedish(dish);
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(dish.getId());
        dishFlavorMapper.delectSetmeal(longs);
        //dishFlavorMapper.selectByDishId(dish.getId());
        //dishDTO.getFlavors().get(0).setDishId(dish.getId()); 错误写法只能给第一个口味绑定菜品，必须遍历
        dishDTO.getFlavors().forEach(flavor -> { //优化后
            flavor.setDishId(dish.getId());
        });
        dishFlavorMapper.add(dishDTO.getFlavors());
    }


    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    /*public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }*/
}





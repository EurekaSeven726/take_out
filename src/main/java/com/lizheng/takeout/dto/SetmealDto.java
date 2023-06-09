package com.lizheng.takeout.dto;
import com.lizheng.takeout.entity.Setmeal;
import com.lizheng.takeout.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {
    private List<SetmealDish> setmealDishes;
    private String categoryName;
}

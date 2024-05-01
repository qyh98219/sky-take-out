package com.sky.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.ICategoryService;
import com.sky.utils.ThreadLocalUtil;
import com.sky.validiton.EnumValue;
import com.sky.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName CategoryController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/1 12:34
 * @Version 1.0
 **/
@RestController
@RequestMapping("/admin/category")
@Tag(name = "分类管理")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @PostMapping()
    @ApiOperation("/分类添加")
    public Result addCategory(@RequestBody  @Validated CategoryDTO categoryDTO){
        Category category = new Category();
        category.setStatus(StatusConstant.ENABLE);
        BeanUtils.copyProperties(categoryDTO, category);

        long empId = ThreadLocalUtil.threadLocal.get();
        category.setCreateUser(empId);
        category.setUpdateUser(empId);

        categoryService.save(category);
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分类查询")
    public Result<PageResult<Category>> list(@RequestParam(defaultValue = "1",value = "page") Integer currentPage,
                                             @RequestParam(defaultValue = "10") Integer pageSize,
                                             @RequestParam(required = false) String name,
                                             @RequestParam(required = false) Integer type){

        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        if(!Objects.isNull(name) && !name.isEmpty()){
            queryWrapper.and(qw -> qw.like(Category::getName, name));
        }
        if (!Objects.isNull(type)){
            queryWrapper.and(qw -> qw.eq(Category::getType, type));
        }
        queryWrapper.orderByAsc(Category::getSort);

        IPage<Category> page = new Page<>(currentPage, pageSize);
        List<Category> list = categoryService.list(page, queryWrapper);
        PageResult<Category> pageResult = new PageResult<>();
        pageResult.setRecords(list);
        pageResult.setTotal(page.getTotal());
        return Result.success(pageResult);
    }
}

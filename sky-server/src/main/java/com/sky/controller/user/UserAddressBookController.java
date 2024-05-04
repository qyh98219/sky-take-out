package com.sky.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.sky.dto.AddressBookDTO;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.IAddressBookService;
import com.sky.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName UserAddressBookController
 * @Description TODO
 * @Author qyh
 * @Date 2024/5/4 18:40
 * @Version 1.0
 **/
@RestController
@RequestMapping("/user/addressBook")
public class UserAddressBookController {
    @Autowired
    private IAddressBookService addressBookService;

    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        Long userId = (Long) ThreadLocalUtil.get("user_user_id");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,userId);
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return Result.success(list);
    }

    @GetMapping("/default")
    public Result<AddressBook> defaultAddressBook(){
        Long userId = (Long) ThreadLocalUtil.get("user_user_id");
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        queryWrapper.eq(AddressBook::getIsDefault, false);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return Result.success(addressBook);
    }

    @PutMapping("/default")
    public Result setDefault(@RequestBody AddressBookDTO addressBookDTO){
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(AddressBook::getIsDefault, true);
        updateWrapper.eq(AddressBook::getId, addressBookDTO.getId());
        if(!addressBookService.update(updateWrapper)){
            return Result.error("操作失败");
        }
        return Result.success("操作成功");
    }

    @GetMapping("{id}")
    public Result<AddressBook> getAddressBookById(@PathVariable Integer id){
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    @PutMapping
    public Result updateAddressBookById(@RequestBody AddressBook addressBook){
        AddressBook old = addressBookService.getById(addressBook);
        addressBook.setIsDefault(old.getIsDefault());
        addressBook.setUserId(old.getUserId());
        if(!addressBookService.updateById(addressBook)){
            return Result.error("操作失败");
        }
        return Result.success("操作成功");
    }

    @DeleteMapping()
    public Result delAddressBook(Integer id){
        if(!addressBookService.removeById(id)){
            return Result.error("操作失败");
        }
        return Result.success("操作成功");
    }

    @PostMapping()
    public Result addAddressBook(@RequestBody AddressBook addressBook){
        Long userId = (Long) ThreadLocalUtil.get("user_user_id");
        addressBook.setUserId(userId);
        addressBook.setIsDefault(false);
        if(!addressBookService.save(addressBook)){
            return Result.error("操作失败");
        }
        return Result.success();
    }
}

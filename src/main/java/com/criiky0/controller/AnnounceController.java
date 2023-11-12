package com.criiky0.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.criiky0.pojo.Announce;
import com.criiky0.service.AnnounceService;
import com.criiky0.utils.Result;
import com.criiky0.utils.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/announce")
public class AnnounceController {

    private AnnounceService announceService;

    @Autowired
    public AnnounceController(AnnounceService announceService) {
        this.announceService = announceService;
    }

    @PostMapping
    public Result<HashMap<String, Announce>> addAnnounce(@RequestBody Announce announce) {
        Announce one = announceService.getOne(new LambdaQueryWrapper<>());
        HashMap<String, Announce> map = new HashMap<>();
        boolean save = announceService.save(announce);
        if (one != null) {
            if (save) {
                boolean remove = announceService.removeById(one.getId());
                if (remove) {
                    Announce single = announceService.getOne(new LambdaQueryWrapper<>());
                    map.put("announce", single);
                    return Result.ok(map);
                }
            }
        } else {
            Announce single = announceService.getOne(new LambdaQueryWrapper<>());
            if (save) {
                map.put("announce", single);
                return Result.ok(map);
            }
        }
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return Result.build(null, 400, "添加失败！");
    }

    @GetMapping("/single")
    public Result<HashMap<String,Announce>> getAnnounce(){
        Announce one = announceService.getOne(new LambdaQueryWrapper<>());
        HashMap<String, Announce> map = new HashMap<>();
        map.put("announce",one);
        return Result.ok(map);
    }
}

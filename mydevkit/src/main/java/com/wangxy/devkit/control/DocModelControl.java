package com.wangxy.devkit.control;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.wangxy.devkit.business.market.DocModel;
import com.wangxy.devkit.business.market.DocModelDao;

@RestController
@RequestMapping("odsTest")
public class DocModelControl {
    @Autowired
    private DocModelDao docService;

    @RequestMapping(value = "/match/{name}", method = RequestMethod.GET)
    public String match(@PathVariable String name) {
        List<DocModel> users = docService.matchName("借款申请表");
        return users.get(0).getDocName();
    }
}

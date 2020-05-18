package com.wangxy.exoskeleton.service.impl;

import com.wangxy.exoskeleton.entity.Pagelable;
import com.wangxy.exoskeleton.mapper.PagelableMapper;
import com.wangxy.exoskeleton.service.IPageLableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageLableServiceImpl implements IPageLableService {
    @Autowired
    PagelableMapper pagelableMapper;
    @Override
    public Pagelable getPagelable(String pageId, String lableId, String lang) {
        return pagelableMapper.selectByPrimaryKey(pageId,lableId,lang);
    }

    @Override
    public void addPagelable(Pagelable pagelable) {
        pagelableMapper.insert(pagelable);
    }
}

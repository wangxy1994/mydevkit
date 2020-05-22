package com.wangxy.exoskeleton.service;

import com.wangxy.exoskeleton.entity.Pagelable;

public interface IPageLableService {

    public Pagelable getPagelable(String pageId,String lableId,String lang);

    public void addPagelable(Pagelable pagelable);

    public int getMaxId();
    
    public Pagelable matchPagelable(String lableInfo);
    
    public Pagelable matchPagelableExactly(String lableInfo);
}

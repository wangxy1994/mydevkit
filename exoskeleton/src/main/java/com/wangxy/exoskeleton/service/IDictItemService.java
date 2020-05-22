package com.wangxy.exoskeleton.service;

import java.util.List;

import com.wangxy.exoskeleton.entity.DictItem;

public interface IDictItemService {

    public DictItem getDictItem(String dictItemCode,String dictEntryCode,String lang);

    public void addDictItem(DictItem dictItem);

    public List<DictItem> getCnDictItems(String dictEntryCode);

	List<DictItem> getDictItems(String dictEntryCode, String lang);
}

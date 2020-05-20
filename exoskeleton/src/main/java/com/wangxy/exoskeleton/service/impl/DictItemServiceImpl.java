package com.wangxy.exoskeleton.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wangxy.exoskeleton.entity.DictItem;
import com.wangxy.exoskeleton.mapper.DictItemMapper;
import com.wangxy.exoskeleton.service.IDictItemService;

@Service
public class DictItemServiceImpl implements IDictItemService {
	@Autowired
    DictItemMapper dictItemMapper;
	@Override
	public DictItem getDictItem(String dictItemCode, String dictEntryCode, String lang) {
		return dictItemMapper.selectByPrimaryKey(dictItemCode,dictEntryCode,lang);
	}

	@Override
	public void addDictItem(DictItem dictItem) {
		dictItemMapper.insert(dictItem);
	}

	@Override
	public List<DictItem> getCnDictItems(String dictEntryCode) {
		List<DictItem> nullDictItem = dictItemMapper.getDictItems(dictEntryCode, " ");
		if (nullDictItem==null||nullDictItem.size()==0) {
			return dictItemMapper.getDictItems(dictEntryCode, "zh_CN");
		}else {
			return nullDictItem;
		}
	}
    

}

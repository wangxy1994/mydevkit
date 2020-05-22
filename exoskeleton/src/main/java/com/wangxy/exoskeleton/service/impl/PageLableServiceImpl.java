package com.wangxy.exoskeleton.service.impl;

import com.wangxy.exoskeleton.entity.Pagelable;
import com.wangxy.exoskeleton.mapper.PagelableMapper;
import com.wangxy.exoskeleton.service.IPageLableService;

import java.util.List;

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

    @Override
    public int getMaxId() {
        return pagelableMapper.getMaxId();
    }

	@Override
	public Pagelable matchPagelable(String lableInfo) {
		System.out.println("lableInfo===="+lableInfo);
		List<Pagelable> exactlyList = pagelableMapper.matchExactly(lableInfo);
		if (exactlyList == null||exactlyList.size()==0) {
			List<Pagelable> matchLike = pagelableMapper.matchLike(lableInfo);
			if (matchLike == null||matchLike.size()==0) {
				return null;
			}else if (matchLike.size()==1||matchLike.size()>1) {
				if (matchLike.size()>1) {
					System.out.println("中文【"+lableInfo+"】模糊匹配到了多个");
				}
				return matchLike.get(0);
			}
		}else if (exactlyList.size()==1||exactlyList.size()>1) {
			if (exactlyList.size()>1) {
				System.out.println("中文【"+lableInfo+"】精确匹配到了多个");
			}
			return exactlyList.get(0);
		}
		return null;
	}

	@Override
	public Pagelable matchPagelableExactly(String lableInfo) {
		System.out.println("lableInfo===="+lableInfo);
		List<Pagelable> exactlyList = pagelableMapper.matchExactly(lableInfo);
		if (exactlyList == null||exactlyList.size()==0) {
			List<Pagelable> matchLike = pagelableMapper.matchLike(lableInfo);
			if (matchLike == null||matchLike.size()==0) {
				return null;
			}else if (matchLike.size()==1||matchLike.size()>1) {
				if (matchLike.size()>1) {
					System.out.println("中文【"+lableInfo+"】模糊匹配到了多个");
				}
				return matchLike.get(0);
			}
		}
		return null;
	}
}

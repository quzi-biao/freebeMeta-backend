package com.freebe.code.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.freebe.code.common.PageBean;

public class PageUtils {

	/**
	 * 请求转换
	 * @param param
	 * @return
	 */
	public static PageRequest toPageRequest(PageBean param) {
		if(null == param.getOrder()) {
			return PageRequest.of(
					(int)param.getCurrPage(),
					(int)param.getLimit());
		}
		List<Order> orders = null;
		if(param.getDesc() == null || param.getDesc()) {
			orders = Arrays.asList(new Order[] {Order.desc(param.getOrder())});
		}else {
			orders = Arrays.asList(new Order[] {Order.asc(param.getOrder())});
		}
		
		return PageRequest.of(
				(int)param.getCurrPage(),
				(int)param.getLimit(),
				Sort.by(orders));
	}
	
	/**
	 * 请求转换
	 * @param param
	 * @return
	 */
	public static PageRequest toPageRequest(PageBean param, List<Order> orders) {
		if(null == orders || orders.size() == 0) {
			return toPageRequest(param);
		}
		return PageRequest.of(
				(int)param.getCurrPage(),
				(int)param.getLimit(),
				Sort.by(orders));
	}

}

package com.taobao.finance.filter;

import com.taobao.finance.dataobject.Stock;

public interface Filter {

	public boolean filter(Stock s);
}

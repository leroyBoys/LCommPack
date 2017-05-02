package com.lgame.util.load.excel;

import java.util.Iterator;

public interface IBuilder {
	/**
	 *
	 * @param data
	 * @param beanClass
	 * @param dataFromLine 正式数据从第几行开始
	 * @param <B>
	 * @return
	 * @throws Exception
	 */
	public <B> Iterator<B> initBuild(String[][] data, Class<B> beanClass, int dataFromLine) throws Exception;
}

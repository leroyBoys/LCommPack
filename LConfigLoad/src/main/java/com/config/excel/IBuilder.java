package com.config.excel;

import java.util.Iterator;

public interface IBuilder {
	/**
	 * 第一个构建，用于构建简单的bean
	 * 
	 * @param <B>
	 *            需要构建的bean
	 * @param data
	 *            原始数据
	 * @param beanClass
	 *            bean的class
	 * 
	 * @throws Exception
	 *             构建出错时抛出错误
	 */
	public <B> Iterator<B> initBuild(String[][] data, Class<B> beanClass) throws Exception;
}

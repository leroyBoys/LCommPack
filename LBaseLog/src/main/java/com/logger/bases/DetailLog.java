/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.logger.bases;

import com.logger.LogKeys;
import com.logger.type.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.lgame.util.comm.SplitConstant.BETWEEN_ITEMS;
import static com.lgame.util.comm.SplitConstant.DELIMITER_ARGS;

/**
 *日志记录
 */
public abstract class DetailLog implements LogKeys {
/**
	 * 记录日志
	 * 
	 * @param logType 			日志的类型
	 * @param message 			需要记录的日志信息
	 */
	public static void doLogger(LogType logType, String message) {
		Logger log = LoggerFactory.getLogger(logType.getLogName());
		if (log.isInfoEnabled() && message != null) {
			log.info(message);
		}
	}
	
	/**
	 * 记录日志
	 * 
	 * @param logType 			日志的类型
	 * @param terms 			日志条目 
	 */
	protected static void log(LogType logType, LogItem... items) {
		try {
			String message = null;
			if(items.length > 0) {
				String termString = ItemHelper.termToString(items);
				message = new StringBuilder().append(LogItem.getTimeTerm()).append(BETWEEN_ITEMS).append(termString).toString();
			}
			doLogger(logType, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 记录日志
	 * 
	 * @param logType 		日志类型
	 * @param terms 		日志条目 
	 */
	protected static void log(LogType logType, LoggerGoods[] infos, LogItem... items) {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append(LogItem.getTimeTerm()).append(BETWEEN_ITEMS).append(ItemHelper.termToString(items));
			if(infos != null && infos.length > 0) {
				builder.append(BETWEEN_ITEMS);
				builder.append(INFO).append(DELIMITER_ARGS);
				for(LoggerGoods goodsInfo : infos) {
					builder.append(goodsInfo);
				}
				builder.deleteCharAt(builder.length() - 1);
			}
			doLogger(logType, builder.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

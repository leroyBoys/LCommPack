package com.config.excel;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractExcelLoader{
	private Map<String, HSSFSheet> xlsMap = new HashMap<>();


	public HSSFSheet getFile(InputStream is,String sheetname) throws IOException
	{
		Sheet sheet = xlsMap.get(sheetname);
		if(sheet !=null)
		{
			return xlsMap.get(sheetname);
		}
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		int sheets = workbook.getNumberOfSheets();
		for(int i=0;i<sheets;i++)
		{
			HSSFSheet $sheet = workbook.getSheetAt(i);
			String name = $sheet.getSheetName();
			xlsMap.put(name, $sheet);
		}
		is.close();
		return xlsMap.get(sheetname);
	}
}

package com.lgame.util.poi.user;

import com.lgame.util.comm.RegexUtils;
import com.lgame.util.comm.StringTool;
import com.lgame.util.poi.ExcelHelper;
import com.lgame.util.poi.interfac.PoiReader;
import com.lgame.util.poi.interfac.RowListener;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class UserExcelReader extends PoiReader {

    /**
     * 总行数
     */
    private int totalRows = 0;
    /**
     * 总列数
     */
    private int totalCells = 0;
    /**
     * 错误信息
     */
    private String errorInfo;

    /**
     * 构造方法
     */

    public UserExcelReader() {
    }

    /**
     * @描述：得到总行数
     * @参数：@return
     * @返回值：int
     */
    public int getTotalRows() {
        return totalRows;
    }

    /**
     * @描述：得到总列数
     * @参数：@return
     * @返回值：int
     */
    public int getTotalCells() {
        return totalCells;
    }

    /**
     * @描述：得到错误信息
     * @参数：@return
     * @返回值：String
     */
    public String getErrorInfo() {
        return errorInfo;
    }


    /**
     *
     * @param fileName
     * @param listener
     * @param endLineNum 最大读取行号，如果0则表示自动
     * @param maxColumNum 最大读取列数，如果0则表示自动
     */
    public boolean read(String fileName, String sheetName, RowListener listener, int endLineNum, int maxColumNum) {
        InputStream is = null;
        boolean isSuc = false;
        try {
            /**
             * 判断文件的类型，是2003还是2007
             */
            boolean isExcel2003 = true;
            if (ExcelHelper.isExcel2007(fileName)) {
                isExcel2003 = false;
            }

            /**
             * 调用本类提供的根据流读取的方法
             */
            File file = new File(fileName);
            is = new FileInputStream(file);
            isSuc = read(is,sheetName, isExcel2003,listener,endLineNum,maxColumNum);
            is.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }

        return isSuc;
    }

    @Override
    public boolean read(String fileName, int sheetIdex, RowListener listener, int endLineNum, int maxColumNum) {
        return read(fileName,String.valueOf(sheetIdex),listener,endLineNum,maxColumNum);
    }

    /**
     * @描述：根据流读取Excel文件
     * @参数：@param inputStream
     * @参数：@param isExcel2003
     * @参数：@return
     * @返回值：List
     */
    private boolean read(InputStream inputStream,String sheetName, boolean isExcel2003,RowListener listener,int endLineNum,int maxColumNum) throws IOException {
        /**
         * 根据版本选择创建Workbook的方式
         */

        Workbook wb = isExcel2003 ? new HSSFWorkbook(inputStream) : new XSSFWorkbook(inputStream);
        return read(wb,sheetName,listener,endLineNum,maxColumNum);
    }

    /**
     * @描述：读取数据
     * @参数：@param wb
     * @参数：@return
     * @返回值：List<List<String>>
     */
    private boolean read(Workbook wb,String sheetName,RowListener listener,int endLineNum,int maxColumNum) {

        /**
         * 得到第一个shell
         */
        Sheet sheet = null;
        if(!StringTool.isEmpty(sheetName)){
            if(RegexUtils.isInt(sheetName)){
                sheet = wb.getSheetAt(Integer.valueOf(sheetName));
            }
            if(sheet == null){
                sheet = wb.getSheet(sheetName);
                if(sheet == null){
                    errorInfo="未找到sheetName:"+sheetName;
                    return false;
                }
            }
        }else {
            sheet =  wb.getSheetAt(0);
        }

        /**
         * 得到Excel的行数
         */
        this.totalRows = sheet.getPhysicalNumberOfRows();
        if(endLineNum>0){
            this.totalRows = Math.min(endLineNum,totalRows);
        }

        /**
         * 得到Excel的列数
         */
        if (this.totalRows >= 1 && sheet.getRow(0) != null) {
            this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
        }

        if(maxColumNum>0){
            this.totalCells = Math.min(this.totalCells,maxColumNum);
        }
        /**
         * 循环Excel的行
         */
        for (int r = 0; r < this.totalRows; r++) {
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            if(maxColumNum<=0){
                this.totalCells = Math.max(this.totalCells,row.getPhysicalNumberOfCells());
            }

            String[] rowLst = new String[this.totalCells];
            /**
             * 循环Excel的列
             */
            for (short c = 0; c < this.getTotalCells(); c++) {
                Cell cell = row.getCell(c);
                if (cell != null) {
                    cell.setCellType(CellType.STRING);
                    rowLst[c] = cell.getStringCellValue();
                }
            }
            /**
             * 保存第r行的第c列
             */

            if(!listener.read(rowLst,r+1)){
                return true;
            }
        }
        listener.overDocument(this.totalRows);
        return true;
    }
}

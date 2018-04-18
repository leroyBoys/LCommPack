package com.lgame.util.excel;

import com.lgame.util.comm.StringTool;
import com.lgame.util.exception.TransformationException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class POIReadData {

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

    public POIReadData() {
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
     * @描述：验证excel文件
     * @参数：@param fileName
     * @参数：@return
     * @返回值：boolean
     */
    public boolean validateExcel(String fileName) {
        /**
         * 检查文件名是否为空或者是否是Excel格式的文件
         */
        if (fileName == null || !(POIReadData.isExcel2003(fileName) || POIReadData.isExcel2007(fileName))) {
            errorInfo = "文件名不是excel格式";
            return false;
        }
        /**
         * 检查文件是否存在
         */
        File file = new File(fileName);
        if (file == null || !file.exists()) {
            errorInfo = "文件不存在";
            return false;
        }
        return true;
    }

    public void read(String fileName) {
        read(fileName,new DefaultRowListener(),0);
    }

    public void read(String fileName,RowListener listener) {
        read(fileName,listener,0);
    }

    public boolean read(String fileName,RowListener listener,int endLineNum) {
        return read(fileName,null,listener,endLineNum,0);
    }

    public boolean read(String fileName,String sheetName,RowListener listener,int endLineNum) {
        return read(fileName,sheetName,listener,endLineNum,0);
    }
    /**
     *
     * @param fileName
     * @param listener
     * @param endLineNum 最大读取行号，如果0则表示自动
     * @param maxColumNum 最大读取列数，如果0则表示自动
     */
    public boolean read(String fileName,String sheetName,RowListener listener,int endLineNum,int maxColumNum) {
        InputStream is = null;
        boolean isSuc = false;
        try {
            /**
             * 验证文件是否合法
             */
            if (!validateExcel(fileName)) {
                System.out.println(errorInfo);
                return false;
            }
            /**
             * 判断文件的类型，是2003还是2007
             */
            boolean isExcel2003 = true;
            if (POIReadData.isExcel2007(fileName)) {
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
        Sheet sheet = wb.getSheetAt(0);
        if(!StringTool.isEmpty(sheetName)){
            sheet = wb.getSheet(sheetName);
            if(sheet == null){
                return false;
            }
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
            String[] rowLst = new String[this.totalCells];
            /**
             * 循环Excel的列
             */
            for (short c = 0; c < this.getTotalCells(); c++) {
                String cellValue = "";
                Cell cell = row.getCell(c);
                if (cell != null) {
                    /**
                     * 处理Excel的字符串
                     */
                    switch (cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_STRING:
                            cellValue = cell.getStringCellValue();
                            break;
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                Date date = cell.getDateCellValue();
                                if (date != null) {
                                    cellValue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                                } else {
                                    cellValue = "";
                                }
                            } else {
                                cellValue = new DecimalFormat("#.##").format(cell.getNumericCellValue());
                            }
                            break;
                        case HSSFCell.CELL_TYPE_FORMULA:
                            // 导入时如果为公式生成的数据则无值
                            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                // 如果是Date类型则，取得该Cell的Date值
                                Date date = cell.getDateCellValue();
                                // 把Date转换成本地格式的字符串
                                cellValue = new SimpleDateFormat("yyyy-MM-dd").format(date);
                            } else {// 如果是纯数字
                                // 取得当前Cell的数值
                                double num = new Double((double) cell.getNumericCellValue());
                                cellValue = String.valueOf(num);
                            }
                            break;
                        case HSSFCell.CELL_TYPE_BLANK:
                            break;
                        case HSSFCell.CELL_TYPE_ERROR:
                            cellValue = "";
                            break;
                        case HSSFCell.CELL_TYPE_BOOLEAN:
                            cellValue = (cell.getBooleanCellValue() == true ? "Y" : "N");
                            break;
                        default:
                            cellValue = "";
                    }

                    rowLst[c] = cellValue;
                }

                /*if (cell == null) {
                    rowLst.add(cellValue);
                    continue;
                }
                *//**
                 * 处理Excel的字符串
                 *//*
                //cellValue = cell.getStringCellValue();
                rowLst.add(cellValue);*/
            }
            /**
             * 保存第r行的第c列
             */
            if(!listener.read(rowLst,r+1)){
                return true;
            }
        }

        return true;
    }

    /**
     * @描述：main测试方法
     * @参数：@param args
     * @参数：@throws Exception
     * @返回值：void
     */
    public static void main(String[] args) throws Exception {
        POIReadData poi = new POIReadData();
        poi.read("d:/提示信息配置表.xlsx", (row, rowNum) -> {System.out.println(Arrays.toString(row));return true;});
    }

    /**
     * @描述：是否是2003的excel，返回true是2003
     * @时间：2011-8-9 下午03:20:49
     * @参数：@param fileName
     * @参数：@return
     * @返回值：boolean
     */
    public static boolean isExcel2003(String fileName) {
        return fileName.matches("^.+\\.(?i)(xls)$");
    }

    /**
     * @描述：是否是2007的excel，返回true是2007
     * @参数：@param fileName
     * @参数：@return
     * @返回值：boolean
     */
    public static boolean isExcel2007(String fileName) {
        return fileName.matches("^.+\\.(?i)(xlsx)$");
    }
}

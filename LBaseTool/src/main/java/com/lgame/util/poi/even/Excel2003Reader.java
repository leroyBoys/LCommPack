package com.lgame.util.poi.even;

import com.lgame.util.exception.TransformationException;
import com.lgame.util.poi.interfac.PoiReader;
import com.lgame.util.poi.interfac.RowListener;
import com.lgame.util.poi.module.DefaultRowListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.record.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

/**
 * Created by leroy:656515489@qq.com
 * 2018/4/20.
 */
public class Excel2003Reader  extends PoiReader implements HSSFListener {
    private int targetSheetIdex = -1;
    private String targetSheetName = null;
    private SSTRecord sstrec;
    private int curSheetIdex = -1;
    private int curRowNum;
    private short curColumIdx;
    private int allRowNum;
    private RowListener listener;
    private LinkedList<String> rowData = new LinkedList<>();
    private int endLineNum;
    private int maxColumNum;

    public static void main(String[] args) throws IOException
    {
        new Excel2003Reader().read("D:/w.xls","config",new DefaultRowListener(),0,0);
    }

    public int getTotalRows() {
        return allRowNum;
    }

    public boolean read(String fileName, String sheetName, RowListener listener, int endLineNum, int maxColumNum) {
        this.targetSheetName = sheetName;
        excute(fileName,listener,endLineNum,maxColumNum);
        return true;
    }

    @Override
    public boolean read(String fileName, int sheetIdex, RowListener listener, int endLineNum, int maxColumNum) {
        this.curSheetIdex = sheetIdex;
        excute(fileName, listener, endLineNum, maxColumNum);
        return false;
    }

    private void excute(String fileName, RowListener listener, int endLineNum, int maxColumNum){
        FileInputStream fin = null;
        InputStream din = null;
        try{
            this.listener = listener;
            this.endLineNum = endLineNum;
            this.maxColumNum = maxColumNum;
            fin = new FileInputStream(fileName);
            // create a new org.apache.poi.poifs.filesystem.Filesystem
            POIFSFileSystem poifs = new POIFSFileSystem(fin);
            // get the Workbook (excel part) stream in a InputStream
            din = poifs.createDocumentInputStream("Workbook");
            // construct out HSSFRequest object
            HSSFRequest req = new HSSFRequest();
            // lazy listen for ALL records with the listener shown above
            req.addListenerForAllRecords(this);
            // create our event factory
            HSSFEventFactory factory = new HSSFEventFactory();
            // process our events based on the document input stream
            factory.processEvents(req, din);
            listener.overDocument(curRowNum);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fin != null){
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(din != null){
                try {
                    din.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private boolean isLoadOver = false;
    protected void readNewSheet(){
        if(!isLoadOver){
            if(targetSheetIdex < 0){
                throw new TransformationException("没有找到名字为"+targetSheetName+"的sheet");
            }else if(targetSheetIdex > curSheetIdex){
                throw new TransformationException("没有找到idex为"+targetSheetIdex+"的sheet");
            }
            curSheetIdex = -1;
            isLoadOver = true;
        }
        curSheetIdex++;
        if(isCanRead()){
            curRowNum = 0;
            curColumIdx = 0;
            allRowNum = 0;
        }
    }

    private boolean isCanRead(){
        return curSheetIdex == targetSheetIdex;
    }

    protected void readCel(int row,short colum,String value){
        if(curRowNum != row){
            readNewRow(row);
        }
        readNewCel(colum,value);
    }

    protected void readNewRow(int row){
        if(!rowData.isEmpty()){
            listener.read(rowData.toArray(new String[rowData.size()]),curRowNum+1);
        }
        curRowNum = row;
        curColumIdx = 0;
        rowData.clear();
    }

    protected void readNewCel(short colum,String value){
        if(colum != curColumIdx){
            int dif = colum - curColumIdx;
            while (dif-->0){
                rowData.add("");
            }

            curColumIdx= colum;
        }
        rowData.add(value);
        curColumIdx++;
    }

    protected String getStringByDouble(double value){
        int v = (int) value;
        if(v == value){
            return String.valueOf(v);
        }
        return String.valueOf(value);
    }

    @Override
    public void processRecord(Record record) {
        switch (record.getSid())
        {
            // the BOFRecord can represent either the beginning of a sheet or the workbook
            case BOFRecord.sid:
                BOFRecord bof = (BOFRecord) record;
                if (bof.getType() == bof.TYPE_WORKBOOK)
                {
                    curSheetIdex = -1;
                    isLoadOver = false;
                   System.out.println("Encountered workbook");
                    // assigned to the class level member
                } else if (bof.getType() == bof.TYPE_WORKSHEET)
                {
                    readNewSheet();
                }else {
                    System.out.println("=======>");
                }
                break;
            case BoundSheetRecord.sid:
                BoundSheetRecord bsr = (BoundSheetRecord) record;
                curSheetIdex++;
                if(targetSheetName == null){
                    if(targetSheetIdex < 0){
                        targetSheetIdex = 0;
                    }
                }else if(bsr.getSheetname().trim().equals(targetSheetName)){
                    targetSheetIdex = curSheetIdex;
                }
                break;
            case RowRecord.sid:
                RowRecord rowrec = (RowRecord) record;
                if(isCanRead()){
                    allRowNum = rowrec.getRowNumber();
                }
                break;
            // SSTRecords store a array of unique strings used in Excel.
            case SSTRecord.sid:
               sstrec = (SSTRecord) record;
                break;
            case NumberRecord.sid:
                if(!isCanRead()){
                    break;
                }

                NumberRecord numrec = (NumberRecord) record;
                this.readCel(numrec.getRow(),numrec.getColumn(),getStringByDouble(numrec.getValue()));
                break;
            case LabelSSTRecord.sid:
                if(!isCanRead()){
                    break;
                }
                LabelSSTRecord lrec = (LabelSSTRecord) record;
                this.readCel(lrec.getRow(),lrec.getColumn(),sstrec.getString(lrec.getSSTIndex()).toString());
                //  System.out.println("=========row:"+lrec.getRow()+",colum:"+lrec.getColumn()  + sstrec.getString(lrec.getSSTIndex()));
                break;
            case BlankRecord.sid:
                if(!isCanRead()){
                    break;
                }
                BlankRecord brec = (BlankRecord) record;
                System.out.println("BlankRecord");
                break;
            case BoolErrRecord.sid: //单元格为布尔类型
                if(!isCanRead()){
                    break;
                }
                BoolErrRecord berec = (BoolErrRecord) record;
                this.readCel(berec.getRow(),berec.getColumn(),String.valueOf(berec.getBooleanValue()));
                break;

            case FormulaRecord.sid: //单元格为公式类型
                if(!isCanRead()){
                    break;
                }
                FormulaRecord frec = (FormulaRecord) record;
                this.readCel(frec.getRow(),frec.getColumn(),getStringByDouble(frec.getValue()));
                break;
            case StringRecord.sid://单元格中公式的字符串
                if(!isCanRead()){
                    break;
                }
                System.out.println("=======================>not write");
                break;
            case LabelRecord.sid:
                if(!isCanRead()){
                    break;
                }
                LabelRecord labelRecord = (LabelRecord) record;
                this.readCel(labelRecord.getRow(),labelRecord.getColumn(),labelRecord.getValue());
                break;

            case EOFRecord.sid:
                if(isCanRead()){
                    readNewRow(curRowNum);
                }
                break;
            default:
                break;
        }
    }
}


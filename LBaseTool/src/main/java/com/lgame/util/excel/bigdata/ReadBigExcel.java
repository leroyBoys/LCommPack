package com.lgame.util.excel.bigdata;
import java.io.InputStream;
import java.util.Iterator;

import com.lgame.util.excel.DefaultRowListener;
import com.lgame.util.excel.RowListener;
import com.lgame.util.exception.TransformationException;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
/**
 *  xlsx
 *  日期，boolean请使用string，否则自行处理（日期一般为时间戳，获得数据后自行转化处理）
 * Created by leroy:656515489@qq.com
 * 2018/4/17.
 */
public class ReadBigExcel {
    /**
     *
     * @param filename
     * @param listen
     * @param maxColum 读取最大列数，如果0则表示自动获得最大列数
     * @throws Exception
     */
    public void processOneSheet(String filename,RowListener listen,int maxColum) throws Exception {

        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader( pkg );
        SharedStringsTable sst = r.getSharedStringsTable();
        XMLReader parser = fetchSheetParser(sst,listen,r.getStylesTable(),maxColum);

        // To look up the Sheet Name / Sheet Order / rID,
        //  you need to process the core Workbook stream.
        // Normally it's of the form rId# or rSheet#
        InputStream sheet2 = r.getSheet("rId2");
        InputSource sheetSource = new InputSource(sheet2);
        parser.parse(sheetSource);
        sheet2.close();
    }

    /**
     *
     * @param filename
     * @param listen
     * @param maxColum 读取最大列数，如果0则表示自动获得最大列数
     * @throws Exception
     */
    public void processAllSheets(String filename, RowListener listen, int maxColum) throws Exception {
        OPCPackage pkg = OPCPackage.open(filename);
        XSSFReader r = new XSSFReader(pkg);
        SharedStringsTable sst = r.getSharedStringsTable();

        XMLReader parser = fetchSheetParser(sst,listen,r.getStylesTable(),maxColum);
        Iterator<InputStream> sheets = r.getSheetsData();
        while(sheets.hasNext()) {
            System.out.println("Processing new sheet:\n");
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            try {
                parser.parse(sheetSource);
            }catch (Exception ex){
                ex.printStackTrace();
                return;
            }
            sheet.close();
            System.out.println("");
        }
    }

    public XMLReader fetchSheetParser(SharedStringsTable sst,RowListener listen,StylesTable stylesTable,int maxColum) throws SAXException {
        XMLReader parser =  XMLReaderFactory.createXMLReader( "org.apache.xerces.parsers.SAXParser" );
        ContentHandler handler = new SheetHandler(sst,listen,stylesTable,maxColum);
        parser.setContentHandler(handler);
        return parser;
    }

    /**
     * See org.xml.sax.helpers.DefaultHandler javadocs
     */
    private static class SheetHandler extends DefaultHandler {
        private SharedStringsTable sst;
        private String lastContents;
        private boolean nextIsString;
        private StylesTable stylesTable;
        private RowListener rowListen;
        private String[] row = null;
        private int curColumIdex;//当前列索引
        private int maxColum;
        private int curRow;//当前行号

        private SheetHandler(SharedStringsTable sst,RowListener rowListen,StylesTable stylesTable,int maxColum) {
            this.sst = sst;
            this.rowListen = rowListen;
            System.out.println(sst.getUniqueCount()+"==="+sst.getCount());
            this.maxColum = maxColum;
            this.stylesTable = stylesTable;
        }

        private SheetHandler(SharedStringsTable sst,RowListener rowListen,StylesTable stylesTable) {
            this.sst = sst;
            this.stylesTable = stylesTable;
            this.rowListen = rowListen;
        }

        public void startElement(String uri, String localName, String name,
                                 Attributes attributes) throws SAXException {
            // c => cell
            if(name.equals("c")) {
                // Print the cell reference
                // Figure out if the value is an index in the SST
                String cellType = attributes.getValue("t");
                if(cellType != null && cellType.equals("s")) {
                    nextIsString = true;
                } else {
                    nextIsString = false;
                }
                String cellStyleStr = attributes.getValue("s");
                if (cellStyleStr != null) {
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    int formatIndex = style.getDataFormat();
                    String formatString = style.getDataFormatString();
                    if ("m/d/yy" == formatString) {
                        // full format is "yyyy-MM-dd hh:mm:ss.SSS";
                        formatString = "yyyy-MM-dd";
                    }
                    if (formatString == null) {
                        formatString = BuiltinFormats.getBuiltinFormat(formatIndex);
                    }
                }

                curColumIdex = this.getRowIndex(attributes.getValue("r"));
                //System.out.println("  startElement: "+attributes.getValue("r") + " - ");
            }else if(name.equals("row")){
                if(maxColum <= 0){
                    try {
                        String[] arr = attributes.getValue("spans").split(":");
                        maxColum = Integer.valueOf(arr[1]);
                    }catch (Exception ex){
                        ex.printStackTrace();
                        throw new TransformationException("请重写获得最大列数方法");
                    }
                }
                row = new String[maxColum];
                curRow++;
            }
            // Clear contents cache
            lastContents = "";
        }

        public void endElement(String uri, String localName, String name)
                throws SAXException {
            // Process the last contents as required.
            // Do now, as characters() may be called more than once

            if(nextIsString) {
                int idx = Integer.parseInt(lastContents);
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).getString();
                nextIsString = false;
            }

            // v => contents of a cell
            // Output after we've seen the string contents
            if(name.equals("v")) {
              //  System.out.println("endElement:"+lastContents);
                if(curColumIdex+1<= maxColum){
                    row[curColumIdex] = lastContents;
                }
            }else if (name.equals("row")) {//一行结束
                if(!rowListen.read(row,curRow)){
                    throw new TransformationException("错误数据过多强制停止解析");
                }
            }
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            lastContents += new String(ch, start, length);
        }


        //得到列索引，每一列c元素的r属性构成为字母加数字的形式，字母组合为列索引，数字组合为行索引，
        //如AB45,表示为第（A-A+1）*26+（B-A+1）*26列，45行
        public int getRowIndex(String rowStr){
            rowStr = rowStr.replaceAll("[^A-Z]", "");
            byte[] rowAbc = rowStr.getBytes();
            int len = rowAbc.length;
            float num = 0;
            for (int i=0;i<len;i++){
                num += (rowAbc[i]-'A'+1)*Math.pow(26,len-i-1 );
            }
            return (int) num-1;
        }
    }

    public static void main(String[] args) throws Exception {
        ReadBigExcel example = new ReadBigExcel();
        example.processAllSheets("D:/TaokeDetail-2018-04-10.xlsx",new DefaultRowListener(),10);
    }
}

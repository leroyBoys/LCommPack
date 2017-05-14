package test;

import com.lgame.util.file.FileTool;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据库表转换成javaBean对象小工具(已用了很长时间),
 * 1 bean属性按原始数据库字段经过去掉下划线,并大写处理首字母等等.
 * 2 生成的bean带了数据库的字段说明.
 * 3 各位自己可以修改此工具用到项目中去.
 */
public class ProbufJavaUtil {
    private String superClassName = "";
    private String tablename = "";
    private String headImport;
    private List<String> colnames= new ArrayList<>();
    private List<String> baiduHeads= new ArrayList<>();
    private List<String> colTypes= new ArrayList<>();
    private Set<String> importsList = new HashSet<>();

    private List<String> clsss= new ArrayList<>();

    public static void main(String[] args) {
        new ProbufJavaUtil().run("D:\\workSpace\\git\\LCommPack\\LSocket\\doc\\protoBufAuto\\proto\\NetParentOld.proto","D:\\data");
    }

    public void run(String path,String targetPath){
        init(path);

        ProbufJava probufJava = new ProbufJava(superClassName,new ArrayList<String>(),new ArrayList<String>(),null);
        probufJava.setHeadImport("package "+headImport+";");
        probufJava.setStatic(false);
        probufJava.setClsss(clsss);
        probufJava.setImportsList(importsList);

        probufJava.tableToEntity(targetPath);
    }


    public boolean init(String path){
        File file = new File(path);
        if(!file.exists()){
            return false;
        }
        importsList.add("import com.baidu.bjf.remoting.protobuf.FieldType;");
        importsList.add("import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;");

        List<String> reads = FileTool.readLines(file);
        for(String line:reads){
            if(readLine(line)){//新的clasxs
                if(!colnames.isEmpty()){
                    clsss.add(new ProbufJava(tablename,colnames,colTypes,baiduHeads).parse());
                }
                colnames.clear();
                baiduHeads.clear();
                colTypes.clear();
                continue;
            }
        }

        if(!colnames.isEmpty()){
            clsss.add(new ProbufJava(tablename,colnames,colTypes,baiduHeads).parse());
        }
        //importsList.add("import "+dataType+";");
        return true;
    }


    /**
     * 返回是否是新的class
     * @param line
     * @return
     */
    private boolean readLine(String line){
        line = line.trim();
        if(line.isEmpty()){
            return false;
        }

       if(line.startsWith("message")){
            tablename = line.replaceFirst("message","").split("\\{")[0].trim();
            return true;
        }else if(line.startsWith("repeated")){
            importsList.add("import java.util.*;");
            String dataType =getDataType(line,2);
            DataType dataObjType = oracleSqlType2JavaType(dataType);
            baiduHeads.add("@Protobuf(fieldType = FieldType."+
                    (dataObjType.isObj?"OBJECT":dataObjType.getType().toUpperCase())+
                    ", order = "+this.getOrder(line)+", required = false)");

            colTypes.add("List<"+dataObjType.getType()+">");
            colnames.add(this.getcolumName(line));
        }else if(line.startsWith("required")){

            String dataType =getDataType(line,2);
            DataType dataObjType = oracleSqlType2JavaType(dataType);
            baiduHeads.add("@Protobuf(fieldType = FieldType."+
                    (dataObjType.isObj?"OBJECT":dataObjType.getType().toUpperCase())+
                    ", order = "+this.getOrder(line)+", required = true)");

            colTypes.add(dataObjType.getType());
            colnames.add(this.getcolumName(line));
        }else if(line.startsWith("optional")){
            String dataType =getDataType(line,2);
            DataType dataObjType = oracleSqlType2JavaType(dataType);
            baiduHeads.add("@Protobuf(fieldType = FieldType."+
                    (dataObjType.isObj?"OBJECT":dataObjType.getType().toUpperCase())+
                    ", order = "+this.getOrder(line)+", required = false)");
            colTypes.add(dataObjType.getType());
            colnames.add(this.getcolumName(line));
        }else if(line.startsWith("option")){
           readOption(line);
           return false;
       }

           return false;
    }

    private String getcolumName(String line) {
        return getDataType(line,3);
    }

    private String getOrder(String line) {
        return line.split("=")[1].replace(";","");
    }

    private void readOption(String line) {
        if(line.contains("java_package")){
            headImport = getPropername(line);
            return;
        }

        superClassName = getPropername(line);
    }

    private String getPropername(String line){
        return line.split("=")[1].replaceAll("\"","").replaceAll(";","").trim();
    }

    private String getDataType(String line,int num){

        String[] strs = line.split(" ");
        boolean flg = false;
        int i = 0;
        for(String str:strs){
            if(!str.trim().isEmpty()){
                i++;

                if(i == num){
                    return str;
                }
                continue;
            }

        }

        System.out.println("=====>"+line);
        return line.split("=")[1].replaceAll("\"","");
    }

    private DataType oracleSqlType2JavaType(String dataType) {
        if (dataType.endsWith("32")) {
            return new DataType(dataType);
        } else if  (dataType.endsWith("64"))  {
            return new DataType(dataType);
        } else if (dataType.equals("bool")) {
            return new DataType("BOOL");
        }else if (dataType.equals("string")) {
            return new DataType("String");
        }else if (dataType.equals("bytes")) {
            return new DataType("bytes");
        }

        return new DataType(dataType.split("\\.")[dataType.split("\\.").length-1],true);
        //  importsList.add("import "+dataType+";");
    }


    public static class DataType{
        private String type;
        private boolean isObj = false;

        public DataType(String type, boolean isObj) {
            this.type = type;
            this.isObj = isObj;
        }
        public DataType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public boolean isObj() {
            return isObj;
        }
    }
}
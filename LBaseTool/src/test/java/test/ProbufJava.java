package test;

import com.lgame.util.comm.StringTool;
import com.lgame.util.file.FileTool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProbufJava {
    private String tablename;
    private List<String> colnames;
    private List<String> colTypes;
    private String headImport;
    private Set<String> importsList = new HashSet<>();
    private List<String> clsss= new ArrayList<>();
    private List<String> baiduHeads= new ArrayList<>();
    private boolean isStatic = true;
   /// private List<String> importsList = new ArrayList<>();

    public ProbufJava(String tablename,List<String> colnames,List<String> colTypes,List<String> baiduHeads){
        this.baiduHeads = baiduHeads;
        this.tablename = tablename;
        this.colnames = colnames;
        this.colTypes = colTypes;
    }

    public List<String> getBaiduHeads() {
        return baiduHeads;
    }

    public void setBaiduHeads(List<String> baiduHeads) {
        this.baiduHeads = baiduHeads;
    }

    public void setImportsList(Set<String> importsList) {
        this.importsList = importsList;
    }

    public void setHeadImport(String headImport) {
        this.headImport = headImport;
    }

    public void setClsss(List<String> clsss) {
        this.clsss = clsss;
    }

    public void setStatic(boolean aStatic) {
        isStatic = aStatic;
    }

    /**
     */
    public String tableToEntity(String targetPath) {
        try {
            String content = parse();
            try {
                File f = new File(targetPath);
                if(!f.exists()){
                    FileTool.createDir(targetPath);
                }

                String fileNamePath = targetPath+"/"+initcap(tablename) + ".java";
                if(!new File(fileNamePath).exists()){
                    FileTool.createFile(targetPath,initcap(tablename) + ".java");
                }

                FileWriter fw = new FileWriter(fileNamePath);
                PrintWriter pw = new PrintWriter(fw);
                pw.println(content);
                pw.flush();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    /**
     * 解析处理(生成实体类主体代码)
     */
    public String parse() {
        StringBuffer sb = new StringBuffer();
        if(StringTool.isNotNull(headImport)){
            sb.append(headImport+"\r\n");
        }

        for(String str:importsList){
            sb.append(str+"\r\n");
        }

        sb.append("\r\n");
        processColnames(sb);
        sb.append("public ");
        if(isStatic){
            sb.append("static ");
        }
        sb.append("class " + initcap(tablename) + " {\r\n");
        processAllAttrs(sb);
        processAllMethod(sb);

        processClss(sb);
        sb.append("}");
        System.out.println(sb.toString());
        return sb.toString();

    }

    private void processClss(StringBuffer sb) {
        for(String str:clsss){
            sb.append(str);
        }
        sb.append("\r\n");
    }

    /**
     * 生成所有的方法
     *
     * @param sb
     */
    private void processAllMethod(StringBuffer sb) {
        for (int i = 0; i < colnames.size(); i++) {
            sb.append("\tpublic void set" + initcap(colnames.get(i)) + "("
                    + oracleSqlType2JavaType(colTypes.get(i)) + " " + colnames.get(i)
                    + "){\r\n");
            sb.append("\t\tthis." + colnames.get(i) + "=" + colnames.get(i) + ";\r\n");
            sb.append("\t}\r\n");

            sb.append("\tpublic " + oracleSqlType2JavaType(colTypes.get(i)) + " get"
                    + initcap(colnames.get(i)) + "(){\r\n");
            sb.append("\t\treturn " + colnames.get(i) + ";\r\n");
            sb.append("\t}\r\n");
        }
    }

    /**
     * 解析输出属性
     *
     * @return
     */
    private void processAllAttrs(StringBuffer sb) {
        for (int i = 0; i < colnames.size(); i++) {
            if(baiduHeads != null && !baiduHeads.isEmpty()){
                sb.append("\t" + baiduHeads.get(i) +"\r\n");
            }
            sb.append("\tprivate " + oracleSqlType2JavaType(colTypes.get(i)) + " "
                    + colnames.get(i) + ";\r\n");
        }
        sb.append("\r\n");
    }

    /**
     * 处理列名,把空格下划线'_'去掉,同时把下划线后的首字母大写
     * 要是整个列在3个字符及以内,则去掉'_'后,不把"_"后首字母大写.
     * 同时把数据库列名,列类型写到注释中以便查看,
     * @param sb
     */
    private void processColnames(StringBuffer sb) {
        if(isStatic){
           return;
        }

        sb.append("\r\n/** \r\n");
        sb.append(" * \r\n");
        sb.append(" * auto class not modify \r\n");
        sb.append(" * \r\n");
        sb.append(" */\r\n");
    }
    /**
     * 把输入字符串的首字母改成大写
     * @param str
     * @return
     */
    private String initcap(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    private String oracleSqlType2JavaType(String dataType) {
        if (dataType.endsWith("32")) {
            return "int";
        } else if  (dataType.endsWith("64"))  {
            return "long";
        } else if (dataType.equals("bool")) {
            return "boolean";
        }else if (dataType.equals("string")) {
            return "String";
        }else if (dataType.equals("bytes")) {
            return "byte[]";
        }

        return dataType;
    }

}
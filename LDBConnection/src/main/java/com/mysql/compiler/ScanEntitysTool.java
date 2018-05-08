package com.mysql.compiler;

import com.lgame.util.PrintTool;
import com.lgame.util.compiler.JavaStringCompiler;
import com.mysql.entity.*;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/2.
 */
public class ScanEntitysTool {
    private final static  Map<Class,DBTable> columInitMap = new HashMap<>();

    private static String getSetClass(String methodContent,String className){
        StringBuilder sb = new StringBuilder("package com.mysql.compiler;\n public class  ");
        sb.append(className).append(" implements ColumInit {\n");
        sb.append("@Override\n public void  set(Object obj, Object v) {\n");
        sb.append(methodContent).append("}\n}\n");

        return sb.toString();
    }

    private static String getGetClass(String methodContent,String className){
        StringBuilder sb = new StringBuilder("package com.mysql.compiler;\n public class  ");
        sb.append(className).append(" implements RelationGetIntace {\n");
        sb.append("@Override\n public Object  get(Object obj) {\n");
        sb.append(methodContent).append("}\n}\n");

        return sb.toString();
    }

    private static String getMethod(Class method_obj,Class method_v,String fieldName){
        if(method_v == Boolean.class || method_v == boolean.class){
            if(fieldName.startsWith("is")){
                fieldName = fieldName.substring(2);
            }
        }


        char fistChar = fieldName.charAt(0);
        char toUpperCase = fistChar;
        if(fistChar >= 'a' && fistChar <= 'z'){
            toUpperCase = (char) (fistChar-32);
        }

        StringBuilder sb = new StringBuilder("((");
        sb.append(method_obj.getName()).append(")obj).");
        sb.append(fieldName.replaceFirst(String.valueOf(fistChar),"set"+toUpperCase));
        sb.append("((");
        sb.append(method_v.getName()).append(")v);");
        return sb.toString();
    }

    private static String getMethod_relation(Class method_obj,String fieldName){

        char fistChar = fieldName.charAt(0);
        char toUpperCase = fistChar;
        if(fistChar >= 'a' && fistChar <= 'z'){
            toUpperCase = (char) (fistChar-32);
        }

        StringBuilder sb = new StringBuilder("return ((");
        sb.append(method_obj.getName()).append(")obj).");
        sb.append(fieldName.replaceFirst(String.valueOf(fistChar),"get"+toUpperCase));
        sb.append("();");
        return sb.toString();
    }

    public static void scan(String pack) throws Exception {
       PrintTool.outTime("ScanEntitysTool","begin scan dbEntity");

        JavaStringCompiler javaStringCompiler = new JavaStringCompiler();

        Set<Class<?>> classs = ClassScanner.getClasses(pack);
        if(classs.isEmpty()){
            PrintTool.outTime("ScanEntitysTool","over scan dbEntity");
            return;
        }

        Field[] fields;
        String newClassName;
        Map<String, byte[]> tmpcompilers;
        ColumInit columInit;
        RelationGetIntace relationGetIntace;
        DbColum dbColum;
        DBDesc dbDesc;
        DBRelations dbRelations;
        DBRelation[] dbRelationArray;
        String columName;
        for(Class cls:classs){

            fields = cls.getDeclaredFields();
            dbDesc = (DBDesc) cls.getAnnotation(DBDesc.class);

            DBTable dbTable = columInitMap.get(cls);
            if(dbTable == null){
                dbTable = new DBTable(dbDesc.name());
                columInitMap.put(cls,dbTable);
            }

            for(Field field:fields){
                try {
                    dbColum = field.getAnnotation(DbColum.class);
                    dbRelations = field.getAnnotation(DBRelations.class);
                    if(dbColum == null && dbRelations == null){
                        continue;
                    }

                    newClassName = getKey("Set",cls,field.getName());//classFinal+""+field.getName();
                    tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                            getSetClass(getMethod(cls,field.getType(),field.getName()),newClassName));

                    columInit = (ColumInit) javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();

                    if(dbColum != null){
                        columName = dbColum.name().isEmpty()?field.getName():dbColum.name();
                        dbTable.addColum(columName,field.getName());
                        dbTable.addColumInit(columName,columInit);
                        if(dbColum.isPrimaryKey()){
                            dbTable.setIdColumName(columName);
                        }
                    }

                    if(dbRelations != null){
                        newClassName = getKey("Get",cls,field.getName());//classFinal+""+field.getName();
                        tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                                getGetClass(getMethod_relation(cls,field.getName()),newClassName));

                        relationGetIntace = (RelationGetIntace) javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();

                        dbRelationArray = dbRelations.map();
                        RelationData relationData = new RelationData(field.getName(),dbRelations,dbRelationArray.length,field,relationGetIntace,columInit);
                        for(int i =0,size=dbRelationArray.length;i<size;i++){
                            dbTable.addRelationData(dbRelationArray[i],relationData);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        PrintTool.outTime("ScanEntitysTool","over scan dbEntity");
    }

    public static void scan(Class cls) throws Exception {
        JavaStringCompiler javaStringCompiler = new JavaStringCompiler();
        Field[] fields = cls.getDeclaredFields();
        String newClassName;
        Map<String, byte[]> tmpcompilers;
        ColumInit columInit;
        DbColum dbColum;
        DBDesc dbDesc = (DBDesc) cls.getAnnotation(DBDesc.class);
        DBRelations dbRelations;
        DBRelation[] dbRelationArray;
        String columName;
        RelationGetIntace relationGetIntace;

        DBTable dbTable = columInitMap.get(cls);
        if(dbTable == null){
            dbTable = new DBTable(dbDesc.name());
            columInitMap.put(cls,dbTable);
        }else {
            return;
        }

        for(Field field:fields){
            try {
                dbColum = field.getAnnotation(DbColum.class);
                dbRelations = field.getAnnotation(DBRelations.class);
                if(dbColum == null && dbRelations == null){
                    continue;
                }

                newClassName = getKey("Set",cls,field.getName());//classFinal+""+field.getName();
                tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                        getSetClass(getMethod(cls,field.getType(),field.getName()),newClassName));

                columInit = (ColumInit) javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();

                if(dbColum != null){
                    columName = dbColum.name().isEmpty()?field.getName():dbColum.name();
                    dbTable.addColum(columName,field.getName());
                    dbTable.addColumInit(columName,columInit);
                    if(dbColum.isPrimaryKey()){
                        dbTable.setIdColumName(columName);
                    }
                }

                if(dbRelations != null){
                    newClassName = getKey("Get",cls,field.getName());//classFinal+""+field.getName();
                    tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                            getGetClass(getMethod_relation(cls,field.getName()),newClassName));

                    relationGetIntace = (RelationGetIntace) javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();

                    dbRelationArray = dbRelations.map();
                    RelationData relationData = new RelationData(field.getName(),dbRelations,dbRelationArray.length,field,relationGetIntace,columInit);
                    for(int i =0,size=dbRelationArray.length;i<size;i++){
                        dbTable.addRelationData(dbRelationArray[i],relationData);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getKey(String flag,Class clas,String columName){
        return flag+"_"+clas.getSimpleName()+"_"+columName;
    }

    public static boolean doExute(DBTable dbTable,String columName,Object obj,Object metho_v){
        try {
            return dbTable.doExute(columName,obj,metho_v);
        }catch (Exception ex){
            PrintTool.error("dbTableName:"+dbTable.getName()+"   columName:"+columName,ex);
        }
        return false;
    }

    public static DBTable getDBTable(Class cls){
        return columInitMap.get(cls);
    }
}

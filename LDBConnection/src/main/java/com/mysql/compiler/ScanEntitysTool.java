package com.mysql.compiler;

import com.lgame.util.PrintTool;
import com.lgame.util.compiler.JavaStringCompiler;
import com.mysql.entity.*;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/2.
 */
public class ScanEntitysTool {
    private final static  Map<Class,DBTable> columInitMap = new HashMap<>();

    private static String getSetClass(String methodContent,String className){
        StringBuilder sb = new StringBuilder("package com.mysql.compiler;\n @SuppressWarnings(\"unchecked\") \n public class  ");
        sb.append(className).append(" extends ColumInit {\n");
        sb.append("@Override\n public void  set(Object obj, Object v) {\n");
        sb.append(methodContent).append("}\n}\n");

        return sb.toString();
    }

    private static String getGetClass(String methodContent,String className){
        StringBuilder sb = new StringBuilder("package com.mysql.compiler;\n  @SuppressWarnings(\"unchecked\") \n public class  ");
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

    public static void scanClass(Set<Class<?>> classs){
        PrintTool.outTime("ScanEntitysTool","begin scan dbEntity");
        JavaStringCompiler javaStringCompiler = new JavaStringCompiler();

        Map<DBTable,Set<RelationData>> relations = new HashMap<>(classs.size());
        Set<RelationData> tmpSet;
        Field[] fields;
        String newClassName;
        Map<String, byte[]> tmpcompilers;
        ColumInit columInit;
        RelationGetIntace relationGetIntace;
        DbColum dbColum;
        DBDesc dbDesc;
        DBRelations dbRelations;
        RelationData relationData;
        SqlTypeToJava sqlTypeToJava;

        String columName;
        for(Class cls:classs){

            fields = cls.getDeclaredFields();
            dbDesc = (DBDesc) cls.getAnnotation(DBDesc.class);

            DBTable dbTable = columInitMap.get(cls);
            if(dbTable == null){
                dbTable = new DBTable(dbDesc.name());
                columInitMap.put(cls,dbTable);
            }

            tmpSet = new HashSet<>(fields.length);
            relations.put(dbTable,tmpSet);
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
                        sqlTypeToJava = SqlTypeToJava.get(field.getType());
                        if(sqlTypeToJava == null){
                            PrintTool.error(field.getType().getName()+" not find match sqlTypeToJava");
                            continue;
                        }
                        columInit.setSqlTypeToJava(sqlTypeToJava);
                        continue;
                    }

                    if(dbRelations != null){
                        newClassName = getKey("Get",cls,field.getName());//classFinal+""+field.getName();
                        tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                                getGetClass(getMethod_relation(cls,field.getName()),newClassName));

                        relationGetIntace = (RelationGetIntace) javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();

                        relationData = new RelationData(field.getName(),dbRelations,dbRelations.map().length,field,relationGetIntace,columInit);
                        dbTable.addRelationData(relationData);
                        tmpSet.add(relationData);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        DBTable dbTable;
        DBRelation[] dbRelationArray;
        for(Map.Entry<DBTable,Set<RelationData>> entry:relations.entrySet()){

            for(RelationData relationDa:entry.getValue()){
                dbRelationArray = relationDa.getReltaion().map();
                dbTable = columInitMap.get(relationDa.getFieldClass());
                for(int i = 0,size = dbRelationArray.length;i<size;i++){
                    relationDa.put(dbRelationArray[i].colum(),dbTable.getColumInit(dbRelationArray[i].targetColum()));
                    entry.getKey().putColumRelationMap(dbRelationArray[i].colum(),relationDa);
                }
            }
        }
        PrintTool.outTime("ScanEntitysTool","over scan dbEntity");
    }

    public static void scan(String... packs) throws Exception {
        Set<Class<?>> classs = null;

        for(String pack:packs){
            Set<Class<?>> cls = ClassScanner.getClasses(pack);
            if(cls.isEmpty()){
               continue;
            }

            if(classs == null){
                classs = cls;
            }else {
                classs.addAll(cls);
            }
        }

        if(classs == null){
            PrintTool.info(Arrays.toString(packs)+" not find db class");
            return;
        }

        scanClass(classs);
    }

    private static String getKey(String flag,Class clas,String columName){
        return flag+"_"+clas.getSimpleName()+"_"+columName;
    }

    public static DBTable getDBTable(Class cls){
        return columInitMap.get(cls);
    }
}

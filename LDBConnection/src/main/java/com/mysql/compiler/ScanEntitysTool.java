package com.mysql.compiler;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.lgame.util.PrintTool;
import com.lgame.util.comm.ReflectionTool;
import com.lgame.util.compiler.JavaStringCompiler;
import com.lgame.util.exception.TransformationException;
import com.mysql.entity.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/2.
 */
public class ScanEntitysTool {
    private final static  Map<Class,DBTable> columInitMap = new HashMap<>();
    private final static Map<Class,Map<Object,DBEnum>> dbEnumMap = new HashMap<>();

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

    private static String getMethod(Set<String> methodsSet,Class method_obj,Class method_v,String fieldName){
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

        String methodName = fieldName.replaceFirst(String.valueOf(fistChar),"set"+toUpperCase);
        if(!methodsSet.contains(methodName)){
            throw new TransformationException("method :"+methodName+" not exit!");
        }

        StringBuilder sb = new StringBuilder("((");
        sb.append(method_obj.getName()).append(")obj).");
        sb.append(methodName);
        sb.append("((");
        sb.append(method_v.getName()).append(")v);");
        return sb.toString();
    }

    private static String getMethod_relation(Set<String> methodsSet,Class method_obj,Class method_v,String fieldName){
        String prex = "get";
        if(method_v == Boolean.class || method_v == boolean.class){
            if(fieldName.startsWith("is")){
                fieldName = fieldName.substring(2);
            }
            prex = "is";
        }

        char fistChar = fieldName.charAt(0);
        char toUpperCase = fistChar;
        if(fistChar >= 'a' && fistChar <= 'z'){
            toUpperCase = (char) (fistChar-32);
        }

        String methodName = fieldName.replaceFirst(String.valueOf(fistChar),prex+toUpperCase);
        if(methodsSet != null && !methodsSet.contains(methodName)){
            throw new TransformationException("method :"+methodName+" not exit!");
        }

        StringBuilder sb = new StringBuilder("return ((");
        sb.append(method_obj.getName()).append(")obj).");
        sb.append(methodName);
        sb.append("();");
        return sb.toString();
    }

    private static String getMethod_Get(Set<String> methodsSet,Class method_obj,Class method_v,String fieldName){
        String prex = "get";
        if(method_v == Boolean.class || method_v == boolean.class){
            if(fieldName.startsWith("is")){
                fieldName = fieldName.substring(2);
            }
            prex = "is";
        }

        char fistChar = fieldName.charAt(0);
        char toUpperCase = fistChar;
        if(fistChar >= 'a' && fistChar <= 'z'){
            toUpperCase = (char) (fistChar-32);
        }

        prex = prex+toUpperCase;
        String methodName = fieldName.replaceFirst(String.valueOf(fistChar),prex);
        String dbPrex = fieldName.replaceFirst(String.valueOf(fistChar),"db_"+prex);
        if(methodsSet.contains(dbPrex)){
            methodName = dbPrex;
        }else if(!methodsSet.contains(methodName)){
            throw new TransformationException("method :"+methodName+" not exit!");
        }

        StringBuilder sb = new StringBuilder("return ((");
        sb.append(method_obj.getName()).append(")obj).");
        sb.append(methodName);
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
        Set<String> methodsSet;
        for(Class cls:classs){
            DBTable dbTable = columInitMap.get(cls);
            if(dbTable != null){
                continue;
            }
            dbDesc = (DBDesc) cls.getAnnotation(DBDesc.class);
            if(dbDesc == null){
                continue;
            }

            methodsSet = initGetMethods(cls.getMethods()) ;
            fields = cls.getDeclaredFields();

            dbTable = new DBTable(dbDesc.name());
            columInitMap.put(cls,dbTable);

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
                            getSetClass(getMethod(methodsSet,cls,field.getType(),field.getName()),newClassName));
                    columInit = (ColumInit) javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();


                    newClassName = getKey("Get",cls,field.getName());//classFinal+""+field.getName();
                    if(dbColum != null){
                        tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                                getGetClass(getMethod_Get(methodsSet,cls,field.getType(),field.getName()),newClassName));
                        relationGetIntace = (RelationGetIntace)javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();

                        columName = dbColum.name().isEmpty()?field.getName():dbColum.name();
                        dbTable.addColum(columName,field.getName());
                        dbTable.addColumInit(columName,columInit,relationGetIntace);

                        if(dbColum.isPrimaryKey()){
                            dbTable.setIdColumName(columName);
                        }

                        sqlTypeToJava = SqlTypeToJava.get(field.getType());
                        if(sqlTypeToJava == null){
                            if(field.getType().isEnum()){
                                if(ReflectionTool.isInterFace(field.getType(),DBEnum.class)){
                                    Class cc = (Class) (((ParameterizedType) field.getType().getGenericInterfaces()[0]).getActualTypeArguments())[0];
                                    if(cc == String.class){
                                        sqlTypeToJava = new SqlTypeToJava.EnumStringJava(field.getType());
                                    }else {
                                        sqlTypeToJava = new SqlTypeToJava.EnumIntJava(field.getType());
                                    }
                                    initEnumCache(field.getType());
                                }else {
                                    sqlTypeToJava = new SqlTypeToJava.EnumDefaultJava(field.getType());
                                }
                            }else {
                                PrintTool.info("warn:"+field.getType().getName()+" not find match sqlTypeToJava default SqlTypeToJava");
                                sqlTypeToJava = new SqlTypeToJava();
                            }
                        }
                        columInit.setSqlTypeToJava(sqlTypeToJava);
                        continue;
                    }

                    if(dbRelations != null){
                        tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                                getGetClass(getMethod_relation(methodsSet,cls,field.getType(),field.getName()),newClassName));

                        relationGetIntace = (RelationGetIntace)javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();
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

        ///////////redis
        for(Class cls:classs){
            RedisCache redisCache = (RedisCache) cls.getAnnotation(RedisCache.class);
            if(redisCache == null){
                continue;
            }

            try {
                dbTable = columInitMap.get(cls);

                relationGetIntace = null;
                if(dbTable == null){
                    dbTable = new DBTable(cls.getSimpleName());
                    columInitMap.put(cls,dbTable);

                }else {
                    String colname= dbTable.getColumName(redisCache.keyFieldName());
                    if(colname != null){
                        relationGetIntace = dbTable.getColumGetMap().get(colname);
                    }
                }

                if(relationGetIntace == null){
                    Field field = cls.getDeclaredField(redisCache.keyFieldName());
                    if(field == null){
                        throw new RuntimeException(cls.getName()+" cant find  field:"+redisCache.keyFieldName()+" for redisCache");
                    }

                    newClassName = getKey("Set",cls,field.getName());//classFinal+""+field.getName();
                    tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                            getGetClass(getMethod_relation(null,cls,field.getType(),field.getName()),newClassName));
                    relationGetIntace = (RelationGetIntace)javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();
                }

                dbTable.setRedisKeyGetInace(relationGetIntace);
                dbTable.setRedisCache(redisCache);
                dbTable.setSchema(RuntimeSchema.getSchema(cls));
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        PrintTool.outTime("ScanEntitysTool","over scan dbEntity");
    }

    private static Set<String> initGetMethods(Method[] methods) {
        Set<String> methodsSet = new HashSet<>(methods.length);
        for(Method method:methods){
            methodsSet.add(method.getName());
        }
        return methodsSet;
    }

    private static void initEnumCache(Class<?> type) {
        Map<Object,DBEnum> map = dbEnumMap.get(type);
        if(map != null){
            return;
        }

        map = new HashMap<>();
        dbEnumMap.put(type,map);

        try {
            Method method = type.getMethod("values");
            DBEnum dbEnums[] = (DBEnum[]) method.invoke(null, null);
            for(DBEnum dbEnum:dbEnums){
                map.put(dbEnum.getDBValue(),dbEnum);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public static Object getEnum(Class tClass, Object dbKey) {
        if(dbKey == null){
            return null;
        }
        return dbEnumMap.get(tClass).get(dbKey);
    }
}

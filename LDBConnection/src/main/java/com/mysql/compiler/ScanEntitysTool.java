package com.mysql.compiler;

import com.lgame.util.PrintTool;
import com.lgame.util.comm.ReflectionTool;
import com.lgame.util.compiler.JavaStringCompiler;
import com.mysql.entity.*;
import com.redis.entity.ByteRedisSerializer;
import com.redis.entity.MapRedisSerializer;
import com.redis.entity.RedisCache;

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
    private final static Map<Class,Map<Object,LQDBEnum>> dbEnumMap = new HashMap<>();

    private static String getSetClass(String methodContent,String className){
        StringBuilder sb = new StringBuilder("package com.mysql.compiler;\n @SuppressWarnings(\"unchecked\") \n public class  ");
        sb.append(className).append(" extends ColumInit {\n");
        sb.append("@Override\n public void  doSet(Object obj, Object v) {\n");
        sb.append(methodContent).append("}\n}\n");

        return sb.toString();
    }

    private static String getGetClass(String methodContent,String className){
        StringBuilder sb = new StringBuilder("package com.mysql.compiler;\n  @SuppressWarnings(\"unchecked\") \n public class  ");
        sb.append(className).append(" extends FieldGetProxy {\n");
        sb.append("@Override\n public Object  get(Object obj) {\n");
        sb.append(methodContent).append("}\n}\n");

        return sb.toString();
    }

    private static String getMethodContentForSet(Class method_obj,Class method_v,String fieldName){
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

        StringBuilder sb = new StringBuilder("((");
        sb.append(method_obj.getName()).append(")obj).");
        sb.append(methodName);
        sb.append("((");
        sb.append(method_v.getName()).append(")v);");
        return sb.toString();
    }

    private static String getMethodNameForGet(Class method_v,String fieldName){
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

        return fieldName.replaceFirst(String.valueOf(fistChar),prex+toUpperCase);
      /*  String methodName = fieldName.replaceFirst(String.valueOf(fistChar),prex+toUpperCase);
        if(methodsSet != null && !methodsSet.contains(methodName)){
            throw new TransformationException("method :"+methodName+" not exit!");
        }
        return methodName;*/
      /*  StringBuilder sb = new StringBuilder("return ((");
        sb.append(method_obj.getName()).append(")obj).");
        sb.append(methodName);
        sb.append("();");*/
        //return sb.toString();
      //  return getMethodContentForGet(method_obj,methodName);
    }

    public static String getMethodContentForGet(Class method_obj,String methodName){
        StringBuilder sb = new StringBuilder("return ((");
        sb.append(method_obj.getName()).append(")obj).");
        sb.append(methodName);
        sb.append("();");
        return sb.toString();
    }

    private static SqlTypeToJava getSqlTypeToJava(Field field){
        SqlTypeToJava sqlTypeToJava = SqlTypeToJava.get(field.getType());
        if(sqlTypeToJava == null){
            if(field.getType().isEnum()){
                if(ReflectionTool.isInterFace(field.getType(),LQDBEnum.class)){
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
        return sqlTypeToJava;
    }

    public static void scanClass(Set<Class<?>> classs){
        PrintTool.outTime("ScanEntitysTool","begin scan dbEntity");
        JavaStringCompiler javaStringCompiler = new JavaStringCompiler();

        Map<Class,Map<String,LQField>> classSetMap = new HashMap<>(classs.size());
        Map<Class,Map<String,FieldGetProxy.FieldGet>> fieldNameColumMap = new HashMap<>(classs.size());
        Map<DBTable,Set<RelationData>> relations = new HashMap<>(classs.size());
        Set<RelationData> tmpSet;
        Field[] fields;
        String newClassName;
        Map<String, byte[]> tmpcompilers;
        ColumInit columInit;
        FieldGetProxy fieldGetProxy;
        LQField lqField;
        LQDBTable lqdbTable;
        DBRelations dbRelations;
        RelationData relationData;
        SqlTypeToJava sqlTypeToJava;

        Map<String,FieldGetProxy.FieldGet> methdNameMap ;
        FieldGetProxy.FieldGet fieldGet;
        String columName;
        String methodName;
       // Set<String> methodsSet;
        for(Class cls:classs){
            DBTable dbTable = columInitMap.get(cls);
            if(dbTable != null){
                continue;
            }
            lqdbTable = (LQDBTable) cls.getAnnotation(LQDBTable.class);
            if(lqdbTable == null){
                continue;
            }

       //     methodsSet = initGetMethods(cls.getMethods()) ;
            fields = cls.getDeclaredFields();

            dbTable = new DBTable(lqdbTable.name());
            columInitMap.put(cls,dbTable);

            Map<String,LQField> fieldsSet = new HashMap<>(fields.length);
            classSetMap.put(cls,fieldsSet);

            methdNameMap = new HashMap<>(fields.length);
            fieldNameColumMap.put(cls,methdNameMap);

            tmpSet = new HashSet<>(fields.length);
            relations.put(dbTable,tmpSet);
            for(Field field:fields){
                try {
                    lqField = field.getAnnotation(LQField.class);
                    dbRelations = field.getAnnotation(DBRelations.class);
                    if(lqField == null && dbRelations == null){
                        continue;
                    }

                    newClassName = getKey("Set",cls,field.getName());//classFinal+""+field.getName();
                    tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                            getSetClass(getMethodContentForSet(cls,field.getType(),field.getName()),newClassName));
                    columInit = (ColumInit) javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();


                    newClassName = getKey("Get",cls,field.getName());//classFinal+""+field.getName();
                    if(lqField != null){
                        methodName = getMethodNameForGet(field.getType(),field.getName());
                        tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                                getGetClass(getMethodContentForGet(cls,methodName),newClassName));
                        fieldGetProxy = (FieldGetProxy)javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();

                        columName = lqField.name().isEmpty()?field.getName(): lqField.name();
                        if(lqField.isPrimaryKey()){
                            dbTable.setIdColumName(columName);
                        }

                        ConvertDefaultDBType defaultDBType = getConvertDefaultDBType(field,lqField);
                        fieldGet = new FieldGetProxy.FieldGet(fieldGetProxy,defaultDBType,field.getType());
                        methdNameMap.put(methodName,fieldGet);
                        dbTable.addColumInit(columName,columInit,fieldGet);
                        columInit.setSqlTypeToJava(getSqlTypeToJava(field),field.getName());
                        fieldsSet.put(columName,lqField);
                        continue;
                    }

                    if(dbRelations != null){
                        methodName = getMethodNameForGet(field.getType(),field.getName());
                        tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                                getGetClass(getMethodContentForGet(cls,methodName),newClassName));

                        fieldGetProxy = (FieldGetProxy)javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();
                        relationData = new RelationData(field.getName(),dbRelations,dbRelations.map().length,field, fieldGetProxy,columInit);
                        dbTable.addRelationData(relationData);
                        tmpSet.add(relationData);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        DBTable dbTable = null;
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
                methdNameMap =  fieldNameColumMap.get(cls);
                fieldGet = null;
                final boolean isNewInit = dbTable == null;
                if(isNewInit){
                    dbTable = new DBTable(cls.getSimpleName());
                    columInitMap.put(cls,dbTable);
                }else {
                    fieldGet = methdNameMap.get(redisCache.keyMethodName());
                }

                if(fieldGet == null){
                    methodName = redisCache.keyMethodName();
                    newClassName = getKey("metho",cls,methodName);//classFinal+""+field.getName();
                    tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                            getGetClass(getMethodContentForGet(cls,methodName),newClassName));
                    fieldGetProxy = (FieldGetProxy)javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();
                    fieldGet = new FieldGetProxy.FieldGet(fieldGetProxy,LQField.ConvertDBType.Default.getConvertDBTypeInter(),null);
                }

                dbTable.setRedisKeyGetInace(fieldGet);
                dbTable.setRedisCache(redisCache);

                if(redisCache.type() == RedisCache.Type.Serialize){
                    dbTable.setRedisSerializer(new ByteRedisSerializer(cls));
                    continue;
                }

                Map<String,LQField> sets = classSetMap.get(cls);
                Map<String,FieldGetProxy.FieldGet> alias;
                if(!isNewInit){
                    alias = new HashMap<>(sets.size());
                    for(Map.Entry<String,LQField> entry:sets.entrySet()){
                        if(!entry.getValue().redisSave()){
                            continue;
                        }
                        alias.put(entry.getKey(),dbTable.getColumGetMap().get(entry.getKey()));
                    }

                    alias = alias.size() == dbTable.getColumGetMap().size()?dbTable.getColumGetMap():alias;
                    dbTable.setRedisSerializer(new MapRedisSerializer(alias));
                    continue;
                }

                fields = cls.getDeclaredFields();
                alias = new HashMap<>(fields.length);
                for(Field field:fields){
                    lqField = field.getAnnotation(LQField.class);
                    if(lqField == null || !lqField.redisSave()){
                        continue;
                    }
                    newClassName = getKey("Set",cls,field.getName());//classFinal+""+field.getName();
                    tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                            getSetClass(getMethodContentForSet(cls,field.getType(),field.getName()),newClassName));
                    columInit = (ColumInit) javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();


                    columName = lqField.name().isEmpty()?field.getName(): lqField.name();
                    methodName = getMethodNameForGet(field.getType(),field.getName());

                    newClassName = getKey("Get",cls,field.getName());//classFinal+""+field.getName();
                    tmpcompilers = javaStringCompiler.compile(newClassName+".java",
                            getGetClass(getMethodContentForGet(cls,methodName),newClassName));
                    fieldGetProxy = (FieldGetProxy)javaStringCompiler.loadClass("com.mysql.compiler."+newClassName,tmpcompilers).newInstance();

                    ConvertDefaultDBType defaultDBType = getConvertDefaultDBType(field,lqField);
                    fieldGet = new FieldGetProxy.FieldGet(fieldGetProxy,defaultDBType,field.getType());

                    alias.put(columName,fieldGet);
                    columInit.setSqlTypeToJava(getSqlTypeToJava(field),field.getName());
                    dbTable.addColumInit(columName,columInit, fieldGet);

                    if(redisCache.keyMethodName().equals(methodName)){
                        dbTable.setRedisKeyGetInace(fieldGet);
                    }
                }

                alias = alias.size() == dbTable.getColumGetMap().size()?dbTable.getColumGetMap():alias;
                dbTable.setRedisSerializer(new MapRedisSerializer(alias));
            }catch (Exception ex){
                ex.printStackTrace();
            }

        }
        PrintTool.outTime("ScanEntitysTool","over scan dbEntity");
    }

    private static ConvertDefaultDBType getConvertDefaultDBType(Field field, LQField lqField) {
        ConvertDefaultDBType convertDefaultDBType = null;
        if(!lqField.convertDBTypeClass().isEmpty()){
            try {
               Class cls = Class.forName(lqField.convertDBTypeClass());
               convertDefaultDBType = (ConvertDefaultDBType) cls.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            convertDefaultDBType = lqField.convertDBType().getConvertDBTypeInter();

            if(Date.class == field.getType()){
                convertDefaultDBType = LQField.ConvertDBType.DateDefault.getConvertDBTypeInter();
            }else if(byte[].class == field.getType()){
                convertDefaultDBType = LQField.ConvertDBType.ByteArray.getConvertDBTypeInter();
            }
        }
        return convertDefaultDBType;
    }

    private static Set<String> initGetMethods(Method[] methods) {
        Set<String> methodsSet = new HashSet<>(methods.length);
        for(Method method:methods){
            methodsSet.add(method.getName());
        }
        return methodsSet;
    }

    private static void initEnumCache(Class<?> type) {
        Map<Object,LQDBEnum> map = dbEnumMap.get(type);
        if(map != null){
            return;
        }

        map = new HashMap<>();
        dbEnumMap.put(type,map);

        try {
            Method method = type.getMethod("values");
            LQDBEnum LQDBEnums[] = (LQDBEnum[]) method.invoke(null, null);
            for(LQDBEnum LQDBEnum : LQDBEnums){
                map.put(LQDBEnum.getDBValue(), LQDBEnum);
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

        PrintTool.outTime("111","====");

        for(Class cls:classs){
            cls.getMethods();
            for(Field field: cls.getDeclaredFields()){
                try {
                   field.getAnnotation(LQField.class);
                    field.getAnnotation(DBRelations.class);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }


        PrintTool.outTime("111","====");
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

package com.mysql.compiler;

import com.lgame.util.PrintTool;
import com.lgame.util.comm.ReflectionTool;
import com.lgame.util.compiler.JavaFile;
import com.lgame.util.compiler.JavaStringCompiler;
import com.mysql.entity.*;
import com.redis.entity.MapRedisSerializer;
import com.redis.entity.RedisCache;

import java.io.IOException;
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
        StringBuilder sb = new StringBuilder(330);
        sb.append("package com.mysql.compiler;\n @SuppressWarnings(\"unchecked\") \n public class  ");
        sb.append(className).append(" extends ColumInit {\n");
        sb.append("@Override\n public void  doSet(Object obj, Object v) {\n");
        sb.append(methodContent).append("}\n}\n");

        return sb.toString();
    }

    private static String getGetClass(String methodContent,String className){
        StringBuilder sb = new StringBuilder(330);
        sb.append("package com.mysql.compiler;\n  @SuppressWarnings(\"unchecked\") \n public class  ");
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
                PrintTool.info("warn:"+field.getType().getName()+" not find match sqlTypeToJava default SqlTypeToJava default set SqlTypeToJava");
                sqlTypeToJava = new SqlTypeToJava();
            }
        }
        return sqlTypeToJava;
    }

    private static void checkMyDbConfig(Map<Class,ClassCache> classSetMap,Set<Class<?>> classs,List<JavaFile> javaFiles){
        for(Class cls:classs){
            if(columInitMap.containsKey(cls)){
                continue;
            }

            LQDBTable lqdbTable = (LQDBTable) cls.getAnnotation(LQDBTable.class);
            if(lqdbTable == null){
                continue;
            }

            Field[] fields = cls.getDeclaredFields();
            ClassCache classCache = new ClassCache(fields.length);
            classCache.setLqdbTable(lqdbTable);
            classSetMap.put(cls,classCache);

            for(Field field:fields){
                LQField lqField = field.getAnnotation(LQField.class);
                DBRelations dbRelations = field.getAnnotation(DBRelations.class);
                if(lqField == null && dbRelations == null){
                    continue;
                }

                String setClassName = getKey("Set",cls,field.getName());//classFinal+""+field.getName();
                javaFiles.add(new JavaFile(setClassName,getSetClass(getMethodContentForSet(cls,field.getType(),field.getName()),setClassName)));

                String getClassName = getKey("Get",cls,field.getName());//classFinal+""+field.getName();
                String methodName = getMethodNameForGet(field.getType(),field.getName());
                javaFiles.add(new JavaFile(getClassName,getGetClass(getMethodContentForGet(cls,methodName),getClassName)));

                if(dbRelations == null){
                    String columName = lqField.name().isEmpty()?field.getName():lqField.name();
                    classCache.addFieldCache(field.getName(),new ClassCache.FieldCache(lqField,columName,getClassName,setClassName,field));
                }else {
                    classCache.addFieldCache(field.getName(),new ClassCache.FieldCache(dbRelations,getClassName,setClassName,field));
                }
            }
        }
    }

    private static void checkRedisConfig(Map<Class,ClassCache> classSetMap,Set<Class<?>> classs,List<JavaFile> javaFiles){
        for(Class cls:classs){
            RedisCache redisCache = (RedisCache) cls.getAnnotation(RedisCache.class);
            if(redisCache == null){
                continue;
            }

            ClassCache classCache = classSetMap.get(cls);
            final boolean isNewInit = classCache == null;
            boolean isContainRedisKeyMethod = false;
            Field[] fields = null;
            if(classCache == null){
                fields = cls.getDeclaredFields();
                classCache = new ClassCache(fields.length);
                classCache.setRedisCache(redisCache);
                classSetMap.put(cls,classCache);
            }else {
                classCache.setRedisCache(redisCache);
                isContainRedisKeyMethod = classCache.isContainMethodClass(redisCache.keyMethodName());
            }

            if(!isContainRedisKeyMethod){
                String getClassName = getKey("metho",cls,redisCache.keyMethodName());//classFinal+""+field.getName();

                classCache.setRedisKeyClassName(getClassName);
                javaFiles.add(new JavaFile(getClassName,getGetClass(getMethodContentForGet(cls,redisCache.keyMethodName()),getClassName)));
            }

            if(!isNewInit){
                continue;
            }

            for(Field field:fields){
                LQField lqField = field.getAnnotation(LQField.class);
                if(lqField == null || !lqField.redisSave()){
                    continue;
                }
                String setClassName = getKey("Set",cls,field.getName());//classFinal+""+field.getName();
                javaFiles.add(new JavaFile(setClassName,getSetClass(getMethodContentForSet(cls,field.getType(),field.getName()),setClassName)));

                String getClassName = getKey("Get",cls,field.getName());//classFinal+""+field.getName();
                String methodName = getMethodNameForGet(field.getType(),field.getName());
                javaFiles.add(new JavaFile(getClassName,getGetClass(getMethodContentForGet(cls,methodName),getClassName)));

                String columName = lqField.name().isEmpty()?field.getName():lqField.name();
                classCache.addFieldCache(field.getName(),new ClassCache.FieldCache(lqField,columName,getClassName,setClassName,field));
            }
        }
    }

    public static void scanClass(Set<Class<?>> classs) throws Exception {
        PrintTool.outTime("ScanEntitysTool","begin scan dbEntity");

        Map<Class,ClassCache> classSetMap = new HashMap<>(classs.size());
        List<JavaFile> javaFiles = new LinkedList<>();

        checkMyDbConfig(classSetMap,classs,javaFiles);
        checkRedisConfig(classSetMap,classs,javaFiles);

        ////init prox class
        JavaStringCompiler compiler = new JavaStringCompiler();
        try {
            compiler.compile(javaFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ///load class
        String packName = "com.mysql.compiler.";
        Class cls;
        ClassCache classCache;
        ColumInit init;
        FieldGetProxy fieldGetProxy;
        FieldGetProxy.FieldGet fieldGet;
        Map<DBTable,Set<RelationData>> relations = new HashMap<>(classs.size());

        for(Map.Entry<Class,ClassCache> entry:classSetMap.entrySet()){
            cls = entry.getKey();
            classCache = entry.getValue();
            if(columInitMap.containsKey(cls)){
                continue;
            }

            String name = cls.getSimpleName();
            if(classCache.getLqdbTable() != null && !classCache.getLqdbTable().name().isEmpty()){
                name = classCache.getLqdbTable().name();
            }
            DBTable dbTable = new DBTable(name);
            columInitMap.put(cls,dbTable);

            dbTable.setRedisCache(classCache.getRedisCache());
            Set<RelationData> tmpSet = new HashSet<>();
            relations.put(dbTable,tmpSet);

            Map<String,FieldGetProxy.FieldGet> getMethodClssSet = new HashMap<>();
            if(classCache.getLqdbTable() != null){
                for(Map.Entry<String,ClassCache.FieldCache> classEntry:classCache.getFieldCacheMap().entrySet()){
                    ClassCache.FieldCache fieldCache = classEntry.getValue();
                    LQField lqField = fieldCache.getLqField();

                    init = compiler.instanceClass(packName+fieldCache.getMethodSetFileName());
                    init.setSqlTypeToJava(getSqlTypeToJava(fieldCache.getField()),fieldCache.getField().getName());

                    fieldGetProxy = compiler.instanceClass(packName+fieldCache.getMethodGetFileName());
                    if(lqField != null){//colum
                        ConvertDefaultDBType defaultDBType = getConvertDefaultDBType(fieldCache.getField(),lqField);
                        fieldGet = new FieldGetProxy.FieldGet(fieldGetProxy,defaultDBType,fieldCache.getField().getType());
                        dbTable.addColumInit(fieldCache.getColumName(),init,fieldGet);

                        if(lqField.isPrimaryKey()){
                            dbTable.setIdColumName(fieldCache.getColumName());
                        }

                        getMethodClssSet.put(fieldCache.getMethodGetFileName(),fieldGet);
                    }else {
                        RelationData relationData = new RelationData(fieldCache.getDbRelations(),fieldCache.getDbRelations().map().length,fieldCache.getField(), fieldGetProxy,init);
                        dbTable.addRelationData(relationData);
                        tmpSet.add(relationData);
                    }

                }
            }

            if(classCache.getRedisCache() != null){
                fieldGet = getMethodClssSet.get(classCache.getRedisCache().keyMethodName());
                if(fieldGet == null){
                    fieldGetProxy = compiler.instanceClass(packName+classCache.getRedisKeyClassName());
                    fieldGet = new FieldGetProxy.FieldGet(fieldGetProxy,LQField.ConvertDBType.Default.getConvertDBTypeInter(),null);
                }

                dbTable.setRedisKeyGetInace(fieldGet);
                if(classCache.getRedisCache().type() == RedisCache.Type.Serialize){
                    continue;
                }

                Map<String,FieldGetProxy.FieldGet> alias = new HashMap<>();
                for(Map.Entry<String,ClassCache.FieldCache> classEntry:classCache.getFieldCacheMap().entrySet()){
                    ClassCache.FieldCache fieldCache = classEntry.getValue();
                    LQField lqField = fieldCache.getLqField();
                    if(lqField == null || !lqField.redisSave()){
                        continue;
                    }

                    fieldGet = dbTable.getColumGetMap().get(fieldCache.getColumName());
                    if(fieldGet == null){
                        init = compiler.instanceClass(packName+fieldCache.getMethodSetFileName());
                        init.setSqlTypeToJava(getSqlTypeToJava(fieldCache.getField()),fieldCache.getField().getName());

                        fieldGetProxy = compiler.instanceClass(packName+fieldCache.getMethodGetFileName());
                        ConvertDefaultDBType defaultDBType = getConvertDefaultDBType(fieldCache.getField(),lqField);
                        fieldGet = new FieldGetProxy.FieldGet(fieldGetProxy,defaultDBType,fieldCache.getField().getType());
                        dbTable.addColumInit(fieldCache.getColumName(),init,fieldGet);
                    }
                    alias.put(fieldCache.getColumName(),fieldGet);
                }

                alias = alias.size() == dbTable.getColumGetMap().size()?dbTable.getColumGetMap():alias;
                dbTable.setRedisSerializer(new MapRedisSerializer(alias));
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
        compiler.close();
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
        Object o = dbEnumMap.get(tClass).get(dbKey);
        return o;
    }
}

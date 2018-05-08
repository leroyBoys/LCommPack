package com.mysql.entity;

import com.mysql.compiler.ColumInit;
import com.mysql.compiler.RelationGetIntace;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/4.
 */
public class RelationData {
    private String fieldName;
    private Class fieldClass;
    private RelationGetIntace relationGetIntace;
    private ColumInit columInit;
    private NewInstance newInstance;
    private DBRelations.Reltaion reltaion;
    private int count;
    private Map<String,String> colums_target_map;

    public RelationData(String fieldName, DBRelations dbRelations, int count,Field field,RelationGetIntace relationGetIntace,ColumInit columInit) {
        this.fieldName = fieldName;
        this.count = count;
        this.columInit = columInit;
        this.relationGetIntace = relationGetIntace;
        colums_target_map = new HashMap<>(count);
        reltaion = dbRelations.relation();
        if(dbRelations.relation()== DBRelations.Reltaion.OneToMany){
            Type type = field.getGenericType();
            ParameterizedType types =(ParameterizedType)type;
            fieldClass = (Class) (types).getActualTypeArguments()[0];
            Class CollectionCls = (Class) types.getRawType();
            if(CollectionCls.isInterface()){
                if(CollectionCls.getSimpleName().toLowerCase().equals("set")){
                    newInstance = new NewSetInstance(CollectionCls);
                }else{
                    newInstance = new NewListInstance(CollectionCls);
                }
            }else {
                newInstance = new NewInstance(CollectionCls);
            }
        }else {
            fieldClass = field.getType();
            newInstance = new NewInstance(fieldClass);
        }
    }

    public boolean isOneToMany(){
        return reltaion == DBRelations.Reltaion.OneToMany;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class getFieldClass() {
        return fieldClass;
    }

    public int getCount() {
        return count;
    }

    public void put(String colum, String targeColum) {
        colums_target_map.put(colum,targeColum);
    }

    public Map<String, String> getColums_target_map() {
        return colums_target_map;
    }

    public RelationGetIntace getRelationGetIntace() {
        return relationGetIntace;
    }

    public NewInstance getNewInstance() {
        return newInstance;
    }

    public ColumInit getColumInit() {
        return columInit;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof RelationData)) return false;
        if(((RelationData)obj).fieldName.equals(fieldName)) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return (fieldName!=null?fieldName.hashCode():1)*31;
    }

    public Object createNew() throws Exception {
        return newInstance.create();
    }

    public String getTargetColum(String colum) {
        return colums_target_map.get(colum);
    }

    public static class NewInstance{
        protected Class cls;
        public NewInstance(Class cls){
            this.cls = cls;
        }

        public Object create() throws IllegalAccessException, InstantiationException {
            return cls.newInstance();
        }

        public void add(Object list,Object v){}
    }

    public static class NewSetInstance extends NewInstance{
        public NewSetInstance(Class cls){
            super(cls);
        }

        @Override
        public Object create() throws IllegalAccessException, InstantiationException {
            return new HashSet<>();
        }

        @Override
        public void add(Object list, Object v) {
            ((Set)list).add(v);
        }
    }

    public static class NewListInstance extends NewInstance{
        public NewListInstance(Class cls){
            super(cls);
        }
        @Override
        public Object create() throws IllegalAccessException, InstantiationException {
            return new LinkedList<>();
        }

        @Override
        public void add(Object list, Object v) {
            ((List)list).add(v);
        }
    }
}

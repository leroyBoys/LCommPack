/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lgame.util.load;

import com.lgame.util.PrintTool;
import com.lgame.util.json.JsonTool;
import com.lgame.util.load.annotation.Resource;

import java.io.*;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author penn.ma <penn.pk.ma@gmail.com>
 */
public class Storage<T> {

    private Class<T> clazz;
    private String location;
    private ResourceReader reader;
    private Getter identifier;
    //private Map<String, IndexBuilder.IndexVisitor> indexVisitors;
    private Map<Object, T> dataTable = new HashMap();
    private Map<String, Object> indexTable = new HashMap();
    private List<Object> idList = new CopyOnWriteArrayList();
    private String resourceLocation = "xmldb" + File.separator;

    public Storage(Class<T> clazz, String resourceLocation) {
        this.resourceLocation = resourceLocation;

        this.clazz = clazz;
        initialize(clazz);
    }

    private void initialize(Class clazz) {
        Resource resource = (Resource) clazz.getAnnotation(Resource.class);
        this.clazz = clazz;
        this.location = this.resourceLocation + clazz.getSimpleName() + "." + resource.suffix();//配置文件
        System.out.println("location--------------" + location);
        if(resource.suffix().equalsIgnoreCase("xml")){
            this.reader = new JsonReader();
        }else {
            this.reader = new ExcelReader();
        }

        this.identifier = GetterBuilder.createIdGetter(clazz);
    }

    public synchronized void reload() {
        InputStream input = null;
        try {
            File dir = new File(this.resourceLocation);
            final String filePath = this.location;
            String[] fileNames = dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    String path = dir+"\\"+name;
                    if(path.equalsIgnoreCase(filePath)){
                        return true;
                    }
                    return false;
                }
            });
            if (fileNames==null ||fileNames.length == 0) {
                PrintTool.error("基础数据["+this.clazz.getName()+"]所对应的资源文件["+this.location+"]不存在!");
                return;
            }
            File resource = new File(this.resourceLocation+fileNames[0]);
            if (resource == null) {
                PrintTool.error("基础数据["+this.clazz.getName()+"]所对应的资源文件["+this.location+"]不存在!");
                return;
            }
//                        InputStream input = resource.openStream();
            input =  new FileInputStream(resource);
            Iterator<T> it = this.reader.read(input, this.clazz);

            List<Object> idList_copy = new ArrayList();//id列表
            Map<Object, T> dataTable_copy = new HashMap();//数据存储
            Map<String, Object> indexTable_copy = new HashMap();//索引存储
            while (it.hasNext()) {
                T obj = it.next();
                //如果数据类继承InitializeBean，则数据类中有需要额外设置的变量
                if ((obj instanceof InitializeBean)) {
                    try {
                        ((InitializeBean) obj).afterPropertiesSet();
                    } catch (Exception e) {
                        PrintTool.error("基础数据["+this.clazz.getName()+"] 属性设置后处理出错!",e);
                    }
                }
                if (offer(obj, dataTable_copy) != null) {
                    throw new RuntimeException(String.format("重复异常: [%s]", new Object[]{JsonTool.getJsonFromBean(obj)}));
                }
                //index(obj, indexTable_copy);
                idList_copy.add(this.identifier.getValue(obj));
            }
            sort(idList_copy, indexTable_copy, dataTable_copy);

            this.idList.clear();
            this.indexTable.clear();
            this.dataTable.clear();

            this.dataTable.putAll(dataTable_copy);
            this.indexTable.putAll(indexTable_copy);
            this.idList.addAll(idList_copy);
            PrintTool.info("完成加载  {} 基础数据...", this.clazz.getName());
        } catch (IOException e) {
            PrintTool.error("基础数据["+this.clazz.getName()+"]所对应的资源文件["+this.location+"]不存在!",e);
        } catch (Exception e) {
            PrintTool.error("{}", e);
        }finally {
            if(input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Map<Object, T> getDataTable() {
        return this.dataTable;
    }

    public Map<String, Object> getIndexTable() {
        return this.indexTable;
    }

    /**
     * 返回索引名称为indexName，并且值等于indexValues的所有数据对象列表
     *
     * @param indexName
     * @param indexValues
     * @return List
     */
    public List<T> getIndex(String indexName, Object... indexValues) {
        String indexkey = getIndexKey(indexName, indexValues);
        List idList = (List) this.indexTable.get(indexkey);
        return list(idList);
    }

    public List getIndexIdList(String indexName, Object... indexValues) {
        String indexkey = getIndexKey(indexName, indexValues);
        return (List) this.indexTable.get(indexkey);
    }

    public T get(Object key) {
        return this.dataTable.get(key);
    }

    public List<T> list(List idList) {
        List<T> resultList = new ArrayList();
        if ((idList != null) && (!idList.isEmpty())) {
            for (Object id : idList) {
                T entity = get(id);
                if (entity != null) {
                    resultList.add(entity);
                }
            }
        }
        return resultList;
    }

    public Collection<T> listAll() {
        return new ArrayList(this.dataTable.values());
    }

    private String getIndexKey(String name, Object... value) {
        return ResourceServiceImpl.KeyBuilder.buildIndexKey(this.clazz, name, value);
    }

    private T offer(T value, Map<Object, T> dataTable) {
        Object key = this.identifier.getValue(value);
        T result = dataTable.put(key, value);
        return result;
    }

    /*private void index(T value, Map<String, Object> indexTable) {
        for (IndexBuilder.IndexVisitor indexVisitor : this.indexVisitors.values()) {
            if (indexVisitor.indexable(value)) {
                String indexKey = indexVisitor.getIndexKey(value);
                addToIndexList(indexKey, value, indexTable);
            }
        }
    }*/

    private void sort(List<Object> idList, Map<String, Object> indexTable, final Map<Object, T> dataTable) {
        Comparator<Object> comparator = null;
        if (Comparable.class.isAssignableFrom(this.clazz)) {
            comparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                    Comparable entity1 = (Comparable) dataTable.get(o1);
                    Comparable entity2 = (Comparable) dataTable.get(o2);
                    return entity1.compareTo(entity2);
                }
            };
        } else {
            comparator = new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (((o1 instanceof Comparable)) && ((o2 instanceof Comparable))) {
                        Comparable co1 = (Comparable) o1;
                        Comparable co2 = (Comparable) o2;
                        return co1.compareTo(co2);
                    }
                    return -1;
                }
            };
        }
        for (Object indexObj : indexTable.values()) {
            if ((indexObj instanceof List)) {
                List<?> indexedIdList = (List) indexObj;
                if ((indexedIdList != null) && (indexedIdList.size() > 1)) {
                    Collections.sort(indexedIdList, comparator);
                }
            }
        }
        Collections.sort(idList, comparator);
    }

    private void addToIndexList(String indexKey, T value, Map<String, Object> indexTable) {
        List idList = (List) indexTable.get(indexKey);
        if (idList == null) {
            idList = new ArrayList();
            indexTable.put(indexKey, idList);
        }
        Object id = this.identifier.getValue(value);
        idList.add(id);
    }
    
    public static void main(String[] args) {
        String resourceLocation = "E:\\t\\";
        final String classPath = resourceLocation+"lvexp.xml";
        File file = new File(resourceLocation);
//        System.out.println(Arrays.toString(file.list()));
//        System.out.println("===========================");
        String[] arr = file.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
//                System.out.println(dir+"================================"+name);
//                    System.out.println(classPath);
                if ((dir +"\\"+ name).equalsIgnoreCase(classPath)) {
                    
                    return true;
                }
                return false;
            }
        });
        System.out.println(Arrays.toString(arr));
//        for (File temp : file.listFiles()) {
//            System.out.println(temp.getName());
//        }
    }
}

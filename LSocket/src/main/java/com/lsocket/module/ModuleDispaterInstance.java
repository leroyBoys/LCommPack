package com.lsocket.module;

import com.lgame.util.PrintTool;
import com.lgame.util.load.ClassScanner;
import com.lsocket.handler.ModuleHandler;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

/**
 * Created by leroy:656515489@qq.com
 * 2017/5/3.
 */
@XmlRootElement(name = "dispaters")
public class ModuleDispaterInstance {

    @XmlElement(name = "dispater")
    public List<Obj> getObjList() {
        return objList;
    }

    public void setObjList(List<Obj> objList) {
        this.objList = objList;
    }

    private List<Obj> objList;
    public void load(){
        for(Obj obj:objList){
            obj.load();
        }
    }

    public static class Obj{
        @XmlAttribute(name = "clas")
        public String getClas() {
            return clas;
        }

        public Obj(){}

        public Obj(String clas){
            this.clas = clas;
        }

        public void setClas(String clas) {
            this.clas = clas;
        }

        private String clas;

        public void load(){
            try {
                if(clas.endsWith(".class")){
                    Class cls = Class.forName(clas);
                    if(ClassScanner.isInterface(cls, ModuleHandler.class)){
                        cls.newInstance();
                        System.out.println("load "+cls.getName()+"  succ!");
                    }
                    return;
                }

                Set<Class<?>> classes = ClassScanner.getClasses(clas);
                if(classes == null || classes.isEmpty()){
                    PrintTool.error("error:"+clas+"clas is empty");
                    return;
                }

                for(Class curCls:classes){
                    if(ClassScanner.isInterface(curCls, ModuleHandler.class)){
                        curCls.newInstance();
                        System.out.println("load "+curCls.getName()+"  succ!");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

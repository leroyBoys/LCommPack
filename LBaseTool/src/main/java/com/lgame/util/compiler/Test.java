package com.lgame.util.compiler;

/**
 * Created by leroy:656515489@qq.com
 * 2018/5/2.
 */
public class Test {
    JavaStringCompiler compiler;

    public void setUp() throws Exception {
        compiler = new JavaStringCompiler();
    }

    static final String SINGLE_JAVA = "/* a single java class to one file */  "
            + "package on.the.fly;                                            "
            + "import com.itranswarp.on.the.fly.*;                            "
            + "public class UserProxy extends User implements BeanProxy {     "
            + "    boolean _dirty = false;                                    "
            + "    public void setId(String id) {                             "
            + "        super.setId(id);                                       "
            + "        setDirty(true);                                        "
            + "    }                                                          "
            + "    public void setName(String name) {                         "
            + "        super.setName(name);                                   "
            + "        setDirty(true);                                        "
            + "    }                                                          "
            + "    public void setCreated(long created) {                     "
            + "        super.setCreated(created);                             "
            + "        setDirty(true);                                        "
            + "    }                                                          "
            + "    public void setDirty(boolean dirty) {                      "
            + "        this._dirty = dirty;                                   "
            + "    }                                                          "
            + "    public boolean isDirty() {                                 "
            + "        return this._dirty;                                    "
            + "    }                                                          "
            + "}                                                              ";
    public void testCompileSingleClass() throws Exception {
      /*  Map<String, byte[]> results = compiler.compile("UserProxy.java", SINGLE_JAVA);
        assertEquals(1, results.size());
        assertTrue(results.containsKey("on.the.fly.UserProxy"));
        Class<?> clazz = compiler.loadClass("on.the.fly.UserProxy", results);
        // get method:
        Method setId = clazz.getMethod("setId", String.class);
        Method setName = clazz.getMethod("setName", String.class);
        Method setCreated = clazz.getMethod("setCreated", long.class);
        // try instance:
        Object obj = clazz.newInstance();
        // get as proxy:
        BeanProxy proxy = (BeanProxy) obj;
        assertFalse(proxy.isDirty());
        // set:
        setId.invoke(obj, "A-123");
        setName.invoke(obj, "Fly");
        setCreated.invoke(obj, 123000999);
        // get as user:
        User user = (User) obj;*/
    }


}

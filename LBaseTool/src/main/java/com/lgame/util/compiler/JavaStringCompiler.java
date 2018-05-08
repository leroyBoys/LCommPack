package com.lgame.util.compiler;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
/**
 *
 * In-memory compile Java source code as String.
 * Created by leroy:656515489@qq.com
 * 2018/5/2.
 */
public class JavaStringCompiler {

    JavaCompiler compiler;
    StandardJavaFileManager stdManager;

    public JavaStringCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
        this.stdManager = compiler.getStandardFileManager(null, null, null);
    }

    /**
     * Compile a Java source file in memory.
     *
     * @param fileName
     *            Java file name, e.g. "Test.java"
     * @param source
     *            The source code as String.
     * @return The compiled results as Map that contains class name as key,
     *         class binary as value.
     * @throws IOException
     *             If compile error.
     */
    public Map<String, byte[]> compile(String fileName, String source) throws IOException {
        try (MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {
            JavaFileObject javaFileObject = manager.makeStringSource(fileName, source);
            CompilationTask task = compiler.getTask(null, manager, null, null, null, Arrays.asList(javaFileObject));
            Boolean result = task.call();
            if (result == null || !result.booleanValue()) {
                throw new RuntimeException("Compilation failed.");
            }
            return manager.getClassBytes();
        }
    }

    /**
     * Load class from compiled classes.
     *
     * @param name
     *            Full class name.
     * @param classBytes
     *            Compiled results as a Map.
     * @return The Class instance.
     * @throws ClassNotFoundException
     *             If class not found.
     * @throws IOException
     *             If load error.
     */
    public Class<?> loadClass(String name, Map<String, byte[]> classBytes) throws ClassNotFoundException, IOException {
        try (MemoryClassLoader classLoader = new MemoryClassLoader(classBytes)) {
            return classLoader.loadClass(name);
        }
    }

    static class MemoryClassLoader extends URLClassLoader {

        // class name to class bytes:
        Map<String, byte[]> classBytes = new HashMap<String, byte[]>();

        public MemoryClassLoader(Map<String, byte[]> classBytes) {
            super(new URL[0], MemoryClassLoader.class.getClassLoader());
            this.classBytes.putAll(classBytes);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            byte[] buf = classBytes.get(name);
            if (buf == null) {
                return super.findClass(name);
            }
            classBytes.remove(name);
            return defineClass(name, buf, 0, buf.length);
        }
    }

    static class MemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

        // compiled classes in bytes:
        final Map<String, byte[]> classBytes = new HashMap<String, byte[]>();

        MemoryJavaFileManager(JavaFileManager fileManager) {
            super(fileManager);
        }

        public Map<String, byte[]> getClassBytes() {
            return new HashMap<String, byte[]>(this.classBytes);
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
            classBytes.clear();
        }

        @Override
        public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind,
                                                   FileObject sibling) throws IOException {
            if (kind == JavaFileObject.Kind.CLASS) {
                return new MemoryOutputJavaFileObject(className);
            } else {
                return super.getJavaFileForOutput(location, className, kind, sibling);
            }
        }

        JavaFileObject makeStringSource(String name, String code) {
            return new MemoryInputJavaFileObject(name, code);
        }

        static class MemoryInputJavaFileObject extends SimpleJavaFileObject {

            final String code;

            MemoryInputJavaFileObject(String name, String code) {
                super(URI.create("string:///" + name), Kind.SOURCE);
                this.code = code;
            }

            @Override
            public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
                return CharBuffer.wrap(code);
            }
        }

        class MemoryOutputJavaFileObject extends SimpleJavaFileObject {
            final String name;

            MemoryOutputJavaFileObject(String name) {
                super(URI.create("string:///" + name), Kind.CLASS);
                this.name = name;
            }

            @Override
            public OutputStream openOutputStream() {
                return new FilterOutputStream(new ByteArrayOutputStream()) {
                    @Override
                    public void close() throws IOException {
                        out.close();
                        ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
                        classBytes.put(name, bos.toByteArray());
                    }
                };
            }

        }
    }
}

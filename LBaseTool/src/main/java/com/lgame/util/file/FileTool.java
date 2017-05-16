package com.lgame.util.file;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件操作类
 *
 * @author lvxiaohui
 *
 */
public class FileTool {

    public static final String ROOTPATH = FileTool.class.getResource("/").getFile();

    /**
     * 按行读文件
     *
     * @param file
     * @return
     */
    public static List<String> readNewUpdaeLines(ReadUpdateFile file,String charsetName) {

        BufferedReader bf = null;
        List<String> lines = new ArrayList<String>();
        try {
            final RandomAccessFile randomFile = new RandomAccessFile(file.getFile(),"r");
            if(file.getLastTimeFileSize() == 0){
                file.setLastTimeFileSize(randomFile.length());
                return lines;
            }

            //获得变化部分的
            randomFile.seek(file.getLastTimeFileSize());
            String tmp = "";
            while( (tmp = randomFile.readLine())!= null) {
                lines.add(new String(tmp.getBytes(charsetName)));
            }

            file.setLastTimeFileSize(randomFile.length());
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bf != null) {
                    bf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    /**
     * 按行读文件
     *
     * @param file
     * @return
     */
    public static List<String> readLines(File file) {
        BufferedReader bf = null;
        List<String> lines = new ArrayList<String>();
        try {
            bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            while (bf.ready()) {
                String name = bf.readLine().trim();
                lines.add(name);
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bf != null) {
                    bf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines;
    }

    
    public static String read(File file) {
        BufferedReader bf = null;
        StringBuilder lines = new StringBuilder();
        try {
            bf = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
            while (bf.ready()) {
                String name = bf.readLine().trim();
                lines.append(name);
            }
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bf != null) {
                    bf.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return lines.toString();
    }

    
    /**
     * 获得文件根目录
     *
     * @return
     */
    public static String getRootPath() {
        return FileTool.class.getResource("/").getPath();
    }

    /**
     * 拿到文件下的所有文件 并将所有文件添加进list集合
     *
     * @param file 文件
     * @param list 添加到此集合中
     * @throws Exception
     */
    public static void getAllFile(File file, List<File> list) throws Exception {
        if (file == null) {
            return;
        }
        if (file.isFile()) {// 判断是否是文件
            list.add(file);
        }
        File[] files = file.listFiles();// 取得文件夹中包含的文件及文件夹
        if (files == null || files.length <= 0) {
            return;// 如果没有其中没有文件或文件夹，返回
        }
        for (File file2 : files) {// 循环其下所有文件及文件夹
            getAllFile(file2, list);// 递归
        }
    }

    /**
     * 将字符串集合写进文件
     *
     * @param lines
     * @param file
     */
    public static void writeLines(File file, List<String> lines) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            for (String line : lines) {
                bw.write(line);//写进字符串
                bw.newLine();//换行
            }
            bw.flush();//写进
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 过滤每行开头是“num.”的文本
     *
     * @param path
     * @param name
     * @throws Exception
     */
    public static void formatData1(String path, String name) throws Exception {
        String oldName = path.endsWith("/") ? path + name : path + "/" + name;
        System.out.println(oldName);
        String newName = oldName.substring(0, oldName.lastIndexOf(".")) + "_" + "." + oldName.substring(oldName.lastIndexOf(".") + 1);
        System.out.println(newName);
        File file = new File(oldName);
        File newfile = new File(newName);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));

        FileOutputStream out = new FileOutputStream(newfile, true);
        String temp = null;
        //StringBuffer sb=new StringBuffer();
        temp = br.readLine();
        while (temp != null) {
            if (temp != null) {
                out.write((temp.trim().replaceFirst("^( )*[0-9]*\\.*", "") + "\r\n").getBytes("utf-8"));
            }
            temp = br.readLine();
        }
        br.close();
        out.close();
    }

    /**
     * 创建文件夹
     *
     * @param path
     */
    public static void createDir(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    /**
     * 创建新文件
     *
     * @param path
     * @param filename
     * @throws IOException
     */
    public static void createFile(String path, String filename) throws IOException {
        File file = new File(path + "/" + filename);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    /**
     * 在IO操作，利用BufferedReader比BufferedWriter效率会更高一点
     *
     * @param path
     * @return
     * @throws IOException
     */
    public String readFileAsBufferedReader(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String temp = null;
        StringBuffer sb = new StringBuffer();
        temp = br.readLine();
        while (temp != null) {
            sb.append(temp + " ");
            temp = br.readLine();
        }
        return sb.toString();
    }

    /**
     * 利用FileInputStream读取文件
     *
     * @param path
     * @return
     * @throws IOException
     */
    public String readFileAsFileInputStream(String path) throws IOException {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new FileNotFoundException();
        }
        FileInputStream fis = new FileInputStream(file);
        byte[] buf = new byte[1024];
        StringBuffer sb = new StringBuffer();
        while ((fis.read(buf)) != -1) {
            sb.append(new String(buf));
            buf = new byte[1024];//重新生成，避免和上次读取的数据重复
        }
        return sb.toString();
    }

    /**
     * 利用StringBuffer写文件
     *
     * @throws IOException
     */
    public static void WriteFile() throws IOException {
        File file = new File("/root/sms.log");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file, true);
        for (int i = 0; i < 10000; i++) {
            StringBuffer sb = new StringBuffer();
            sb.append("这是第" + i + "行:前面介绍的各种方法都不关用,为什么总是奇怪的问题 ");
            out.write(sb.toString().getBytes("utf-8"));
        }
        out.close();
    }

    /**
     * 利用PrintStream写文件
     *
     * @param fileName
     */
    public static void PrintStreamDemo(String fileName) {
        try {
            FileOutputStream out = new FileOutputStream(fileName);
            PrintStream p = new PrintStream(out);
            for (int i = 0; i < 10; i++) {
                p.println("This is " + i + " line");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 以文件流的方式复制文件
     *
     * @param oldFile
     * @param newFile
     * @throws IOException
     */
    public static void copyFile(String oldFile, String newFile) throws IOException {
        FileInputStream in = new FileInputStream(oldFile);
        File file = new File(newFile);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream out = new FileOutputStream(file);
        int c;
        byte buffer[] = new byte[1024];
        while ((c = in.read(buffer)) != -1) {
            for (int i = 0; i < c; i++) {
                out.write(buffer[i]);
            }
        }
        in.close();
        out.close();
    }

    public File file_put_contents(String file_name, InputStream is) {
        File file = new File(file_name);
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 文件重命名
     *
     * @param path
     * @param oldname
     * @param newname
     */
    public static void renameFile(String path, String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(path + "/" + oldname);
            File newfile = new File(path + "/" + newname);
            if (newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
            {
                System.out.println(newname + "已经存在！");
            } else {
                oldfile.renameTo(newfile);
            }
        }
    }

    /**
     * 转移文件目录不等同于复制文件，复制文件是复制后两个目录都存在该文件，而转移文件目录则是转移后，只有新目录中存在该文件。
     *
     * @param filename
     * @param oldpath
     * @param newpath
     * @param cover
     */
    public static void changeDirectory(String filename, String oldpath, String newpath, boolean cover) {
        if (!oldpath.equals(newpath)) {
            File oldfile = new File(oldpath + "/" + filename);
            File newfile = new File(newpath + "/" + filename);
            if (newfile.exists()) {//若在待转移目录下，已经存在待转移文件
                if (cover)//覆盖
                {
                    oldfile.renameTo(newfile);
                } else {
                    System.out.println("在新目录下已经存在：" + filename);
                }
            } else {
                oldfile.renameTo(newfile);
            }
        }
    }

    /**
     * 要利用File类的delete()方法删除目录时，必须保证该目录下没有文件或者子目录，否则删除失败，因此在实际应用中，我们要删除目录，必须利用递归删除该目录下的所有子目录和文件，然后再删除该目录。
     *
     * @param path
     */
    public static void delDir(String path) {
        File dir = new File(path);
        if (dir.exists()) {
            File[] tmp = dir.listFiles();
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i].isDirectory()) {
                    delDir(path + "/" + tmp[i].getName());
                } else {
                    tmp[i].delete();
                }
            }
            dir.delete();
        }
    }
}

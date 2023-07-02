package com.example.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * 类的描述
 *
 * @author hujiping
 * @date 2022/9/29 5:44 PM
 */
public class FileHelper {

    public static void main(String[] args) {
        try {
            readFileWithStream();

        } catch (Exception e) {

        }
    }

    /**
     * 读取文件内容
     * @param fileName file/file.json
     * @return
     */
    public static String readFile(String fileName) {

        Reader reader = null;
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(fileName);
            Resource resource = resources[0];
            InputStream stream = resource.getInputStream();

            int ch;
            reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            StringBuilder sb = new StringBuilder();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }

            return sb.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(reader != null){
                try {
                    reader.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * Scanner读取文件内容
     *
     * @throws IOException
     */
    public void readFileWithScanner() throws IOException {
        //文件内容：Hello World|Hello Zimug
        String fileName = "D:\\data\\test\\newFile4.txt";

        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            while (sc.hasNextLine()) {  //按行读取字符串
                String line = sc.nextLine();
                System.out.println(line);
            }
        }

        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            sc.useDelimiter("\\|");  //分隔符
            while (sc.hasNext()) {   //按分隔符读取字符串
                String str = sc.next();
                System.out.println(str);
            }
        }

        //sc.hasNextInt() 、hasNextFloat() 、基础数据类型等等等等。
        //文件内容：1|2
        fileName = "D:\\data\\test\\newFile5.txt";
        try (Scanner sc = new Scanner(new FileReader(fileName))) {
            sc.useDelimiter("\\|");  //分隔符
            while (sc.hasNextInt()) {   //按分隔符读取Int
                int intValue = sc.nextInt();
                System.out.println(intValue);
            }
        }
    }

    /**
     * 流的方式整行读取文件内容
     *
     * @throws IOException
     */
    public static void readFileWithStream() throws IOException {
        String fileName = "/Users/hujiping/Downloads/choujian";

        // 读取文件内容到Stream流中，按行读取
        Stream<String> lines = Files.lines(Paths.get(fileName));
//        // 转换成List<String>, 要注意java.lang.OutOfMemoryError: Java heap space
//        List<String> lines = Files.readAllLines(Paths.get(fileName),
//                StandardCharsets.UTF_8);

        // 顺序进行数据处理
        lines.forEachOrdered(System.out::println);

        // 按文件行顺序进行处理（并行处理，适合比较大的文件）
        lines.parallel().forEachOrdered(System.out::println);
    }

    /**
     * buffer缓冲区方式读取文件
     *
     * @throws IOException
     */
    public void readFileByBuffer() throws IOException {
        String fileName = "D:\\data\\test\\newFile3.txt";

        // 带缓冲的流读取，默认缓冲区8k
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }

        //java 8中这样写也可以
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }

    }


    private static Logger logger = LoggerFactory.getLogger(FileHelper.class);
    private static final int BUFFER_SIZE = 5 * 1024 * 1024; // 上传时缓存区的大小
    public final static long ONE_KB = 1024;
    public final static long ONE_MB = ONE_KB * 1024;
    public final static long ONE_GB = ONE_MB * 1024;
    public final static long ONE_TB = ONE_GB * (long) 1024;
    public final static long ONE_PB = ONE_TB * (long) 1024;

    /**
     * 将内容保存到文件中
     *
     * @param data     内容
     * @param fileName 文件名
     * @param code     内容编码
     * @return
     */
    public static boolean save(String data, String fileName, String code) {
        File f = new File(fileName);
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        FileOutputStream foss = null;
        logger.info("save to file:" + f.getAbsolutePath());
        try {
            foss = new FileOutputStream(f);
            foss.write(data.getBytes(code));
            foss.flush();
            foss.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean isImage(File file) {
        // TODO Auto-generated method stub
        BufferedImage bi;
        try {
            bi = ImageIO.read(file);
            if (bi != null) {
                return true;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 得到文件大小
     *
     * @param fileSize
     * @return
     */
    public static String getHumanReadableFileSize(long fileSize) {
        if (fileSize < 0) {
            return String.valueOf(fileSize);
        }
        String result = getHumanReadableFileSize(fileSize, ONE_PB, "PB");
        if (result != null) {
            return result;
        }

        result = getHumanReadableFileSize(fileSize, ONE_TB, "TB");
        if (result != null) {
            return result;
        }
        result = getHumanReadableFileSize(fileSize, ONE_GB, "GB");
        if (result != null) {
            return result;
        }
        result = getHumanReadableFileSize(fileSize, ONE_MB, "MB");
        if (result != null) {
            return result;
        }
        result = getHumanReadableFileSize(fileSize, ONE_KB, "KB");
        if (result != null) {
            return result;
        }
        return String.valueOf(fileSize) + "B";
    }

    private static String getHumanReadableFileSize(long fileSize, long unit,
                                                   String unitName) {
        if (fileSize == 0)
            return "0";

        if (fileSize / unit >= 1) {
            double value = fileSize / (double) unit;
            DecimalFormat df = new DecimalFormat("######.##" + unitName);
            return df.format(value);
        }
        return null;
    }

    public static String getFileExt(String fileName) {
        if (fileName == null)
            return "";

        String ext = "";
        int lastIndex = fileName.lastIndexOf(".");
        if (lastIndex >= 0) {
            ext = fileName.substring(lastIndex + 1).toLowerCase();
        }

        return ext;
    }

    /**
     * 得到不包含后缀的文件名字
     *
     * @return
     */
    public static String getRealName(String name) {
        if (name == null) {
            return "";
        }

        int endIndex = name.lastIndexOf(".");
        if (endIndex == -1) {
            return null;
        }
        return name.substring(0, endIndex);
    }

    /**
     * 复制一个文件
     *
     * @param srcFileName
     * @param dstFileName
     * @return
     */
    public static boolean copy(String srcFileName, String dstFileName) {
        File srcFile = new File(srcFileName);
        File dstFile = new File(dstFileName);
        return copy(srcFile, dstFile);
    }

    public static void mkdirParent(String filePath) {
        if (StringHelper.isNotEmpty(filePath)) {
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
        }
    }

    public static void mkdir(String path) {
        if (StringHelper.isNotEmpty(path)) {
            File file = new File(path);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
    }

    /**
     * 复制一个文件
     *
     * @param srcFile
     * @param dstFile
     * @return
     */
    public static boolean copy(File srcFile, File dstFile) {
        boolean result = false;
        InputStream in = null;
        OutputStream out = null;
        try {
            if (!dstFile.getParentFile().exists()) {
                dstFile.getParentFile().mkdirs();
            }
            if (!dstFile.exists()) {
                dstFile.createNewFile();
            }
            System.out.println(srcFile + "->" + dstFile);
            if (!srcFile.exists()) {
                return false;
            }
            in = new BufferedInputStream(new FileInputStream(srcFile),
                    BUFFER_SIZE);
            out = new BufferedOutputStream(new FileOutputStream(dstFile),
                    BUFFER_SIZE);
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = in.read(buffer)) > 0) {
                for (int i = 0; i < len; i++) {
                    // System.out.println(buffer[i] + "->" + (buffer[i] ^
                    // 0xFF));
                    // buffer[i] = (byte) (buffer[i] ^ 0xFF);
                }
                out.write(buffer, 0, len);
                // System.out.println("...");
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 列出所有文件
     *
     * @param dir
     * @return
     */
    public static List<File> listFiles(File dir) {
        List<File> files = new ArrayList<File>(16);
        if (dir.isFile()) {
            files.add(dir);
        } else {
            File[] fs = dir.listFiles();
            if (fs != null) {
                for (File f : fs) {
                    files.addAll(listFiles(f));
                }
            }
        }
        return files;
    }

    /**
     * 获取文件路径
     *
     * @param path
     * @param depth-指定目录深度 <0，代表从左到右
     * @return
     */
    public static String getPath(String path, int depth) {
        return getPath(path, depth);
    }

    /**
     * 获取文件路径
     *
     * @param path
     * @param depth-指定目录深度 <0，代表从左到右
     * @return
     */
    public static String getPath(String path, int depth, boolean isLinux) {
        String regex = "/";
        if (path.contains("\\")) {
            regex = "\\";
        }
        List<String> ss = StringHelper.splitToList(path, regex);
        if (isLinux) {
            regex = "/";
        }
        int len = ss.size();
        if (ss != null && ss.size() > 0) {
            String tmp = "";
            if (depth >= 0) {
                for (int i = 0; i < len && i < depth; i++) {
                    tmp += ss.get(i);
                    if (i < len - 1 && i < depth - 1) {
                        tmp += regex;
                    }
                }
            } else {
                for (int i = len - 1; i > 0 && i > (len - 1 + depth); i--) {
                    tmp = ss.get(i) + tmp;
                    if (i > 1 && i > (len + depth)) {
                        tmp = regex + tmp;
                    }
                }
            }
            return tmp;
        }
        return path;
    }

    /**
     * 获取文件路径
     *
     * @param path
     * @param depth-指定目录深度 <0，代表从左到右
     * @return
     */
    public static String getLinuxPath(String path, int depth) {
        return getPath(path, depth, true);
    }

    public static String loadFile(String file) {
        InputStream inputStream = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        StringBuilder sb = new StringBuilder(1024);
        try {
            inputStream = FileHelper.class.getClassLoader().getResourceAsStream(file);
            reader = new InputStreamReader(inputStream, Charset.forName("utf-8"));
            bufferedReader = new BufferedReader(reader);

            String line;
            do {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
                sb.append("\n");
            } while (line != null);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (reader != null) reader.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException ex) {
                //do nothing
            }
        }

        return sb.toString();
    }

    public static List<String> loadFileToList(String file) {
        return loadFileToList(file, true);
    }

    public static List<String> loadFileToList(String file, boolean trimFlg) {
        InputStream inputStream = null;
        InputStreamReader reader = null;
        BufferedReader bufferedReader = null;
        List<String> lines = new ArrayList<String>();
        try {
            inputStream = FileHelper.class.getClassLoader().getResourceAsStream(file);
            if (inputStream == null) {
                inputStream = new FileInputStream(new File(file));
            }
            reader = new InputStreamReader(inputStream, Charset.forName("utf-8"));
            bufferedReader = new BufferedReader(reader);

            String line;
            do {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                if (trimFlg) {
                    line = line.trim();
                }
                lines.add(line);
            } while (line != null);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (reader != null) reader.close();
                if (inputStream != null) inputStream.close();
            } catch (IOException ex) {
                //do nothing
            }
        }

        return lines;
    }

    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getPrefix(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}

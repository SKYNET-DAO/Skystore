package bitchat.android.com.bitstore.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import bitchat.android.com.bitstore.R;
import bitchat.android.com.bitstore.bean.FileBean;
import com.android.base.utils.UIUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.orhanobut.logger.Logger;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;


public class FileUtils {

    public static final String ROOT_DIR = "Android/data/"
            + UIUtils.getPackageName();
    public static final String DOWNLOAD_DIR = "download";
    public static final String CACHE_DIR = "cache";
    public static final String ICON_DIR = "icon";


    public static final int TYPE_DOC = 0;

    public static final int TYPE_APK = 1;

    public static final int TYPE_ZIP = 2;

    public static final int TYPE_GZ = 3;




    public static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }


    public static String getDownloadDir() {
        return getDir(DOWNLOAD_DIR);
    }


    public static String getCacheDir() {
        return getDir(CACHE_DIR);
    }


    public static String getIconDir() {
        return getDir(ICON_DIR);
    }


    public static String getDir(String name) {
        StringBuilder sb = new StringBuilder();
        if (isSDCardAvailable()) {
            sb.append(getExternalStoragePath());
        } else {
            sb.append(getCachePath());
        }
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        if (createDirs(path)) {
            return path;
        } else {
            return null;
        }
    }


    public static String getExternalStoragePath() {
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
        sb.append(File.separator);
        sb.append(ROOT_DIR);
        sb.append(File.separator);
        return sb.toString();
    }


    public static String getCachePath() {
        File f = UIUtils.getContext().getCacheDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + "/";
        }
    }


    public static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }


    public static boolean copyFile(String srcPath, String destPath,
                                   boolean deleteSrc) {
        File srcFile = new File(srcPath);
        File destFile = new File(destPath);
        return copyFile(srcFile, destFile, deleteSrc);
    }


    public static boolean copyFile(File srcFile, File destFile,
                                   boolean deleteSrc) {
        if (!srcFile.exists() || !srcFile.isFile()) {
            return false;
        }
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];
            int i = -1;
            while ((i = in.read(buffer)) > 0) {
                out.write(buffer, 0, i);
                out.flush();
            }
            if (deleteSrc) {
                srcFile.delete();
            }
        } catch (Exception e) {
            Logger.e(e.getMessage());
            return false;
        } finally {
            IOUtils.close(out);
            IOUtils.close(in);
        }
        return true;
    }


    public static boolean isWriteable(String path) {
        try {
            if (StringUtils.isEmpty(path)) {
                return false;
            }
            File f = new File(path);
            return f.exists() && f.canWrite();
        } catch (Exception e) {
            LogUtils.e(e);
            return false;
        }
    }


    public static void chmod(String path, String mode) {
        try {
            String command = "chmod " + mode + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }


    public static boolean writeFile(InputStream is, String path,
                                    boolean recreate) {
        boolean res = false;
        File f = new File(path);
        FileOutputStream fos = null;
        try {
            if (recreate && f.exists()) {
                f.delete();
            }
            if (!f.exists() && null != is) {
                File parentFile = new File(f.getParent());
                parentFile.mkdirs();
                int count = -1;
                byte[] buffer = new byte[1024];
                fos = new FileOutputStream(f);
                while ((count = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }
                res = true;
            }
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            IOUtils.close(fos);
            IOUtils.close(is);
        }
        return res;
    }


    public static boolean writeFile(byte[] content, String path, boolean append) {
        boolean res = false;
        File f = new File(path);
        RandomAccessFile raf = null;
        try {
            if (f.exists()) {
                if (!append) {
                    f.delete();
                    f.createNewFile();
                }
            } else {
                f.createNewFile();
            }
            if (f.canWrite()) {
                raf = new RandomAccessFile(f, "rw");
                raf.seek(raf.length());
                raf.write(content);
                res = true;
            }
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            IOUtils.close(raf);
        }
        return res;
    }


    public static boolean writeFile(String content, String path, boolean append) {
        return writeFile(content.getBytes(), path, append);
    }


    public static void writeProperties(String filePath, String key,
                                       String value, String comment) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(filePath)) {
            return;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        File f = new File(filePath);
        try {
            if (!f.exists() || !f.isFile()) {
                f.createNewFile();
            }
            fis = new FileInputStream(f);
            Properties p = new Properties();
            p.load(fis);
            p.setProperty(key, value);
            fos = new FileOutputStream(f);
            p.store(fos, comment);
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            IOUtils.close(fis);
            IOUtils.close(fos);
        }
    }


    public static String readProperties(String filePath, String key,
                                        String defaultValue) {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(filePath)) {
            return null;
        }
        String value = null;
        FileInputStream fis = null;
        File f = new File(filePath);
        try {
            if (!f.exists() || !f.isFile()) {
                f.createNewFile();
            }
            fis = new FileInputStream(f);
            Properties p = new Properties();
            p.load(fis);
            value = p.getProperty(key, defaultValue);
        } catch (IOException e) {
            LogUtils.e(e);
        } finally {
            IOUtils.close(fis);
        }
        return value;
    }


    public static void writeMap(String filePath, Map<String, String> map,
                                boolean append, String comment) {
        if (map == null || map.size() == 0 || StringUtils.isEmpty(filePath)) {
            return;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        File f = new File(filePath);
        try {
            if (!f.exists() || !f.isFile()) {
                f.createNewFile();
            }
            Properties p = new Properties();
            if (append) {
                fis = new FileInputStream(f);
                p.load(fis);
            }
            p.putAll(map);
            fos = new FileOutputStream(f);
            p.store(fos, comment);
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            IOUtils.close(fis);
            IOUtils.close(fos);
        }
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    public static Map<String, String> readMap(String filePath,
                                              String defaultValue) {
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        Map<String, String> map = null;
        FileInputStream fis = null;
        File f = new File(filePath);
        try {
            if (!f.exists() || !f.isFile()) {
                f.createNewFile();
            }
            fis = new FileInputStream(f);
            Properties p = new Properties();
            p.load(fis);
            map = new HashMap<String, String>((Map) p);
        } catch (Exception e) {
            LogUtils.e(e);
        } finally {
            IOUtils.close(fis);
        }
        return map;
    }


    public static boolean copy(String src, String des, boolean delete) {
        File file = new File(src);
        if (!file.exists()) {
            return false;
        }
        File desFile = new File(des);
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(file);
            out = new FileOutputStream(desFile);
            byte[] buffer = new byte[1024];
            int count = -1;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
                out.flush();
            }
        } catch (Exception e) {
            LogUtils.e(e);
            return false;
        } finally {
            IOUtils.close(in);
            IOUtils.close(out);
        }
        if (delete) {
            file.delete();
        }
        return true;
    }


    public static String getDirFromPath(String filepath) {
        if ((filepath != null) && (filepath.length() > 0)) {
            int sep = filepath.lastIndexOf('/');
            if ((sep > -1) && (sep < filepath.length() - 1)) {
                return filepath.substring(0, sep + 1);
            }
        }
        return filepath;
    }


    public static String getFileNameFromPath(String filepath) {
        if ((filepath != null) && (filepath.length() > 0)) {
            int sep = filepath.lastIndexOf('/');
            if ((sep > -1) && (sep < filepath.length() - 1)) {
                return filepath.substring(sep + 1);
            }
        }
        return filepath;
    }


    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }


    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return "";
    }

    public static String formatSize(float size) {
        long kb = 1024;
        long mb = (kb * 1024);
        long gb = (mb * 1024);
        if (size < kb) {
            return String.format("%d B", (int) size);
        } else if (size < mb) {
            return String.format("%.2f KB", size / kb); 
        } else if (size < gb) {
            return String.format("%.2f MB", size / mb);
        } else {
            return String.format("%.2f GB", size / gb);
        }
    }

    public static String formateFileSize(double filesize) {
        double kiloByte = filesize / 1024;
        if (kiloByte < 1) {
            return filesize + " B";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " M";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " GB";
        }
        double petaBytes = teraBytes / 1024;
        if (petaBytes < 1) {
            BigDecimal result4 = new BigDecimal(Double.toString(teraBytes));
            return result4.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " TB";
        }

        double exaBytes = petaBytes / 1024;
        if (exaBytes < 1) {
            BigDecimal result5 = new BigDecimal(Double.toString(petaBytes));
            return result5.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " PB";
        }

        double zettaBytes = exaBytes / 1024;
        if (zettaBytes < 1) {
            BigDecimal result6 = new BigDecimal(Double.toString(exaBytes));
            return result6.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " EB";
        }
        double yottaBytes = zettaBytes / 1024;
        if (yottaBytes < 1) {
            BigDecimal result7 = new BigDecimal(Double.toString(zettaBytes));
            return result7.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + " ZB";
        }
        BigDecimal result8 = new BigDecimal(yottaBytes);
        return result8.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + " YB";
    }



    public static byte[] FileToByte(String filePath)

    {
        byte[] buffer = null;

        try
        {
            File file = new File(filePath);

            FileInputStream fis = new FileInputStream(file);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            byte[] b = new byte[1024];

            int n;
            while ((n = fis.read(b)) != -1)

            {
                bos.write(b, 0, n);

            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();

        }
        catch (FileNotFoundException e)

        {
            e.printStackTrace();

        }
        catch (IOException e)

        {
            e.printStackTrace();

        }
        return buffer;

    }

    public static void FileToByte(byte[] buf, String filePath, String fileName)

    {
        BufferedOutputStream bos = null;

        FileOutputStream fos = null;

        File file = null;

        try
        {
            File dir = new File(filePath);

            if (!dir.exists() && dir.isDirectory())

            {
                dir.mkdirs();

            }
            file = new File(filePath + File.separator + fileName);

            fos = new FileOutputStream(file);

            bos = new BufferedOutputStream(fos);

            bos.write(buf);

        }
        catch (Exception e)

        {
            e.printStackTrace();

        }
        finally
        {
            if (bos != null)

            {
                try
                {
                    bos.close();
                }
                catch (IOException e)

                {
                    e.printStackTrace();

                }
            }
            if (fos != null)

            {
                try
                {
                    fos.close();
                }
                catch (IOException e)

                {
                    e.printStackTrace();

                }
            }
        }
    }


    public static Uri Byte2File(byte[] bytes, String path) throws IOException {
        File file = new File(path);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes, 0, bytes.length);
        fos.flush();
        fos.close();
        return Uri.fromFile(file);
    }



    public File drawableToFile(Context mContext, int drawableId, File fileName){
//        InputStream is = view.getContext().getResources().openRawResource(R.drawable.logo);
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), drawableId);

//        Bitmap bitmap = BitmapFactory.decodeStream(is);

        String defaultPath = mContext.getFilesDir()
                .getAbsolutePath() + "/defaultGoodInfo";
        File file = new File(defaultPath);
        if (!file.exists()) {
            file.mkdirs();
        } else {
            return null;
        }
        String defaultImgPath = defaultPath + "/"+fileName;
        file = new File(defaultImgPath);
        try {
            file.createNewFile();

            FileOutputStream fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 20, fOut);
//            is.close();
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }





    private static List<FileBean> getFilesByType(Context context,int fileType) {
        List<FileBean> files = new ArrayList<>();
        // 扫描files文件库
        Cursor c = null;
        try {
            c = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), new String[]{"_id", "_data", "_size","_display_name","title"}, null, null, null);
            int dataindex = c.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            int sizeindex = c.getColumnIndex(MediaStore.Files.FileColumns.SIZE);
            int titleindex = c.getColumnIndex(MediaStore.Files.FileColumns.TITLE);


            while (c.moveToNext()) {
                String path = c.getString(dataindex);
                String title=c.getString(titleindex);

                if (FileUtils.getFileType(path) == fileType) {
                    if (!FileUtils.isExists(path)) {
                        continue;
                    }
                    long size = c.getLong(sizeindex);
                    FileBean fileBean = new FileBean(context,path,title,size,FileUtils.getFileIconByPath(path));
                    files.add(fileBean);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return files;
    }



    public static int getFileType(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".xls") || path.endsWith(".xlsx")
                || path.endsWith(".ppt") || path.endsWith(".pptx")) {
            return TYPE_DOC;
        }else if (path.endsWith(".apk")) {
            return TYPE_APK;
        }else if (path.endsWith(".zip") || path.endsWith(".rar") || path.endsWith(".tar")) {
            return TYPE_ZIP;
        }else if( path.endsWith(".gz")){
            return TYPE_GZ;
        }
        else{
            return -1;
        }
    }



    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }



    public static int getFileIconByPath(String path){
        path = path.toLowerCase();
        int iconId = R.mipmap.unknow_file_icon;
        if (path.endsWith(".txt")){
            iconId = R.mipmap.type_txt;
        }else if(path.endsWith(".doc") || path.endsWith(".docx")){
            iconId = R.mipmap.type_doc;
        }else if(path.endsWith(".xls") || path.endsWith(".xlsx")){
            iconId = R.mipmap.type_xls;
        }else if(path.endsWith(".ppt") || path.endsWith(".pptx")){
            iconId = R.mipmap.type_ppt;
        }else if(path.endsWith(".xml")){
            iconId = R.mipmap.type_xml;
        }else if(path.endsWith(".htm") || path.endsWith(".html")){
            iconId = R.mipmap.type_html;
        }else if(path.endsWith(".gz")){
            iconId = R.mipmap.type_gz;
        }
        return iconId;
    }


    public static Flowable<List<FileBean>> getLocalApps(Context context){

       return Flowable.just(context)
                .observeOn(Schedulers.io())
                .map(context1 ->getFilesByType(context1,TYPE_APK))
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static Flowable<List<FileBean>> getLocalGzs(Context context){

        return Flowable.just(context)
                .observeOn(Schedulers.io())
                .map(context1 ->getFilesByType(context1,TYPE_GZ))
                .observeOn(AndroidSchedulers.mainThread());
    }


}
package com.beinet.firstpg.zipDemo;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class ZipHelper {
    private ZipHelper() {

    }

    /**
     * 压缩目录和子目录
     *
     * @param zipTargetName 要保存到的压缩文件全路径
     * @param dir           要压缩的目录
     * @param overwrite     目标文件存在时，是否覆盖，不覆盖将抛出异常
     * @return 压缩文件路径
     * @throws IOException 可能的IO异常
     */
    public static String zipDir(String zipTargetName, String dir, boolean overwrite) throws IOException {
        File objDir = new File(dir);
        if (!objDir.exists()) {
            throw new IllegalArgumentException("Dir isn't exists: " + dir);
        }
        if (!objDir.isDirectory()) {
            throw new IllegalArgumentException("It isn't a dir: " + dir);
        }
        return zipFiles(zipTargetName, objDir.listFiles(), overwrite, objDir.getAbsolutePath());
    }

    /**
     * 压缩指定的文件和目录列表
     *
     * @param zipTargetName 要保存到的压缩文件全路径
     * @param files         要压缩的文件或目录清单
     * @param overwrite     目标文件存在时，是否覆盖
     * @return 压缩文件路径
     * @throws IOException 可能的IO异常
     */
    public static String zipFiles(String zipTargetName, String[] files, boolean overwrite) throws IOException {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("Files can't be empty.");
        }
        File[] arr = new File[files.length];
        for (int i = 0, j = files.length; i < j; i++) {
            arr[i] = new File(files[i]);
            if (!arr[i].exists()) {
                throw new IllegalArgumentException("File isn't exists: " + files[i]);
            }
//            if (!arr[i].isFile()) {
//                throw new IllegalArgumentException("It isn't a file: " + files[i]);
//            }
        }
        return zipFiles(zipTargetName, arr, overwrite, null);
    }

    public static String unZip(String zipFileName, String targetDir) throws IOException {
        File zipFile = new File(zipFileName);
        if (!zipFile.exists()) {
            throw new IllegalArgumentException("Zip file not exists: " + zipFileName);
        }
        File dir = new File(targetDir);

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    continue;
                }
                File currentFild = new File(dir, zipEntry.getName());
                createDirectory(currentFild.getAbsolutePath(), true);

                try (FileOutputStream fos = new FileOutputStream(currentFild)) {
                    int len;
                    byte[] b = new byte[1024];
                    while ((len = zis.read(b)) > 0) {
                        fos.write(b, 0, len);
                    }
                    fos.flush();
                }
            }
        }
        return dir.getAbsolutePath();
    }

    /**
     * 压缩文件列表
     *
     * @param zipTargetName 压缩文件目标
     * @param files         要压缩的文件或目录列表
     * @param overwrite     目标压缩文件存在时，是否要覆盖
     * @param parentDir     不为空时，表示压缩到文件里的目录要截断
     * @return 压缩文件路径
     * @throws IOException 可能的IO异常
     */
    private static String zipFiles(String zipTargetName, File[] files, boolean overwrite, String parentDir) throws IOException {
        File zipFile = new File(zipTargetName);
        String ret = zipFile.getAbsolutePath();
        if (zipFile.exists()) {
            if (!overwrite) {
                throw new IllegalArgumentException("Zip file already exists: " + zipTargetName);
            }
            zipFile.delete();
        } else {
            createDirectory(ret, true);
        }
        zipFile.createNewFile();
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            zipFiles(zos, files, parentDir);
        }
        return ret;
    }

    private static void zipFiles(ZipOutputStream zos, File[] files, String parentDir) throws IOException {
        if (files == null) {
            return;
        }
        for (File itemFile : files) {
            // 递归目录
            if (itemFile.isDirectory()) {
                zipFiles(zos, itemFile.listFiles(), parentDir);
                continue;
            }
            if (!itemFile.isFile()) {
                continue;
            }
            try (FileInputStream fis = new FileInputStream(itemFile)) {
                // 带目录和文件名压缩
                zos.putNextEntry(new ZipEntry(getFileName(itemFile, parentDir)));
                int len;
                byte[] b = new byte[1024];
                while ((len = fis.read(b)) > 0) {
                    zos.write(b, 0, len);
                }
            }
        }
    }

    /**
     * 确认指定路径的目录是否存在，不存在时创建
     *
     * @param path       文件或目录
     * @param pathIsFile 路径是文件还是目录，如果是文件，要获取文件所在目录
     */
    private static void createDirectory(String path, boolean pathIsFile) {
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("Path can't be empty");
        }
        path = replaceSeparator(path);
        if (pathIsFile) {
            int idx = path.lastIndexOf('/');
            if (idx <= 0) {
                return;
            }
            path = path.substring(0, idx);
        }
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private static String replaceSeparator(String str) {
        return str.replace("\\", "/").replace("\\\\", "/");
    }

    private static String getFileName(File itemFile, String parentDir) {
        // 移除Windows系统的盘符
        String inFileName = itemFile.getAbsolutePath();
        if (StringUtils.isEmpty(parentDir)) {
            int idx = inFileName.indexOf(':');
            if (idx >= 0)
                inFileName = inFileName.substring(idx + 1);
        } else {
            inFileName = inFileName.replace(parentDir, "");
        }
        if (inFileName.length() > 0 && (inFileName.charAt(0) == '/' || inFileName.charAt(0) == '\\')) {
            inFileName = inFileName.substring(1);
        }
        return inFileName;
    }
}

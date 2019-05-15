package com.beinet.firstmaven;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.metadata.Sheet;

import java.io.*;
import java.util.List;

public class ExcelHelper {

    /**
     * 读取指定的Excel文件内容并返回。
     *
     * @param fileName Excel文件路径
     * @return 指定Sheet的行列数据
     * @throws IOException 文件读取异常
     */
    public static List<Object> readExcel(String fileName) throws IOException {
        return readExcel(fileName, 1);
    }

    /**
     * 读取指定的Excel文件内容并返回。
     *
     * @param fileName Excel文件路径
     * @param sheetNo  要读取哪一个Sheet，默认值1，表示第1个Sheet
     * @return 指定Sheet的行列数据
     * @throws IOException 文件读取异常
     */
    public static List<Object> readExcel(String fileName, int sheetNo) throws IOException {
        return readExcel(fileName, 1, 0);
    }

    /**
     * 读取指定的Excel文件内容并返回。
     *
     * @param fileName   Excel文件路径
     * @param sheetNo    要读取哪一个Sheet，默认值1，表示第1个Sheet
     * @param startRowNo 从第几行开始读取，默认值0，表示第1行开始读取
     * @return 指定Sheet的行列数据
     * @throws IOException 文件读取异常
     */
    public static List<Object> readExcel(String fileName, int sheetNo, int startRowNo) throws IOException {
        if (sheetNo < 1)
            sheetNo = 1;
        if (startRowNo < 0)
            startRowNo = 0;
        try (InputStream is = getFileStream(fileName)) {
            return EasyExcelFactory.read(is, new Sheet(sheetNo, startRowNo));
        }
    }
    /**
     * 读取Excel文件流返回
     * @param fileName excel文件
     * @return 文件流，记得关闭
     * @throws FileNotFoundException 文件读取异常
     */
    private static InputStream getFileStream(String fileName) throws FileNotFoundException {
        if (fileName == null)
            throw new IllegalArgumentException("fileName can't bu null.");
        fileName = fileName.trim();
        if (fileName.length() == 0)
            throw new IllegalArgumentException("fileName can't bu empty.");
        int idx = fileName.lastIndexOf('.');
        if (idx <= 0)
            throw new IllegalArgumentException("file ext must be xls or xlsx");
        String ext = fileName.substring(idx).toLowerCase();
        if (!ext.equals(".xls") && !ext.equals(".xlsx"))
            throw new IllegalArgumentException("file ext must be xls or xlsx.");

        return new BufferedInputStream(new FileInputStream(fileName));
    }
}

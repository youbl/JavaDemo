package com.beinet.firstpg.zipDemo;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class ZipController {
    final String zipfile = "d:\\aaaaa\\bbb\\cc.zip";

    @GetMapping(value = "/zip")
    public String testZip() throws IOException {
        return ZipHelper.zipDir(zipfile, "E:\\mine\\JavaDemo\\src\\main\\", true);
    }


    @GetMapping(value = "/unzip")
    public String testUnZip() throws IOException {
        return ZipHelper.unZip(zipfile, "D:\\aaaaa\\cccmain\\");
    }


    @GetMapping(value = "/down")//, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> testDownload(HttpServletResponse response) throws IOException {
        // 设置Header
        String type = new MimetypesFileTypeMap().getContentType(zipfile);
        File file = new File(zipfile);
        String downName = new String(file.getName().getBytes("utf-8"), "iso-8859-1");

        // 如下2种工作方式：setContentType和setHeader都不能正常工作，前端得到的都是 text/html
        // 如果不用HttpHeaders，只能设置GetMapping那里的 produces
//        response.setHeader("Content-Disposition", "attachment;filename=" + downName);
//        response.setContentType(type);
//        response.setHeader("Content-Type", type);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment;filename=" + downName);
        headers.add("Content-Type", type);

        // 输出二进制流
        byte[] arr;
        try (FileInputStream fis = new FileInputStream(zipfile)) {
            arr = IOUtils.toByteArray(fis);
        }
        return new ResponseEntity(arr, headers, HttpStatus.OK);
    }
}

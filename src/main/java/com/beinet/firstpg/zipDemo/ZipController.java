package com.beinet.firstpg.zipDemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ZipController {
    @GetMapping(value = "/zip")
    public String testZip() throws IOException {
        String zipfile = "d:\\aaaaa\\bbb\\cc.zip";
        return ZipHelper.zipDir(zipfile, "E:\\mine\\JavaDemo\\src\\main\\", true);
    }


    @GetMapping(value = "/unzip")
    public String testUnZip() throws IOException {
        String zipfile = "d:\\aaaaa\\bbb\\cc.zip";
        return ZipHelper.unZip(zipfile, "D:\\aaaaa\\cccmain\\");
    }
}

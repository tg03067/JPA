package com.green.greengram.common;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class CustomFileUtilsTest {

    @Test
    void deleteFolder2() {
        CustomFileUtils customFileUtils = new CustomFileUtils("");

        String delFolderPath = "D:\\download\\delFolder2";
        File delFolder = new File(delFolderPath);
        delFolder.delete();
    }

    @Test
    void deleteFolder3() {
        String delFolderPath = "D:\\download\\delFolder";
        File delFolder = new File(delFolderPath);
        delFolder.delete(); // 삭제가 안 됨 (폴더안에 파일, 디렉토리 하나라도 있으면 삭제가 안 됨.)
    }

    @Test
    void deleteFolder() {
        CustomFileUtils customFileUtils = new CustomFileUtils("");
        String delFolderPath = "D:\\download\\delFolder";
        customFileUtils.deleteFolder(delFolderPath);
    }
}
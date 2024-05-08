package com.green.greengram.common;

import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
@Getter
@ToString
public class CustomFileUtils {
    private final String uploadPath;

    public CustomFileUtils(@Value("${file.dir}") String uploadPath) {
        this.uploadPath = uploadPath;
    }
    public String makeFolders(String path){
        File folder = new File(uploadPath, path);
        folder.mkdirs();
        return folder.getAbsolutePath();
    }
    public String getExt(String fileName){
        int idx = fileName.lastIndexOf(".");
        return fileName.substring(idx);
    }
    public String makeRandomFileName(){
        return UUID.randomUUID().toString();
    }
    public String makeRandomFileName(String fileName){
        return makeRandomFileName() + getExt(fileName);
    }
    public String makeRandomFileName(MultipartFile mf){
        return mf == null || mf.isEmpty() ? null : makeRandomFileName(mf.getOriginalFilename());
    }

    public void transferTo(MultipartFile mf, String target) throws Exception{
        File saveFile = new File(uploadPath, target);
        mf.transferTo(saveFile);
    }
}

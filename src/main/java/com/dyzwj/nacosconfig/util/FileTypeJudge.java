package com.dyzwj.nacosconfig.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

//文件类型判断类
public final class FileTypeJudge {

    /**
     * Constructor
     */
    private FileTypeJudge() {
    }

    /**
     * 将文件头转换成16进制字符串
     *
     * @param 原生byte
     * @return 16进制字符串
     */
    private static String bytesToHexString(byte[] src) {

        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 得到文件头
     *
     * @param filePath
     *            文件路径
     * @return 文件头
     * @throws IOException
     */
    private static String getFileContent(InputStream is) throws IOException {

        byte[] b = new byte[28];

        InputStream inputStream = null;

        try {
            is.read(b, 0, 28);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw e;
                }
            }
        }
        return bytesToHexString(b);
    }

    /**
     * 判断文件类型
     *
     * @param filePath
     *            文件路径
     * @return 文件类型
     */
    public static FileType getType(InputStream is) throws IOException {

        String fileHead = getFileContent(is);
        if (fileHead == null || fileHead.length() == 0) {
            return null;
        }
        fileHead = fileHead.toUpperCase();
        FileType[] fileTypes = FileType.values();

        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                return type;
            }
        }

        return null;
    }
    /**
     *
     * @param value 表示文件类型
     * @return 1 表示图片,2 表示文档,3 表示视频,4 表示种子,5 表示音乐,7 表示其它
     * @return
     */
    public static Integer isFileType(FileType value) {
        Integer type = 7;// 其他
        // 图片
        FileType[] pics = { FileType.JPEG, FileType.PNG, FileType.GIF, FileType.TIFF, FileType.BMP, FileType.DWG, FileType.PSD };

        FileType[] docs = { FileType.RTF, FileType.XML, FileType.HTML, FileType.CSS, FileType.JS, FileType.EML, FileType.DBX, FileType.PST, FileType.XLS_DOC, FileType.XLSX_DOCX, FileType.VSD,
                FileType.MDB, FileType.WPS, FileType.WPD, FileType.EPS, FileType.PDF, FileType.QDF, FileType.PWL, FileType.ZIP, FileType.RAR, FileType.JSP, FileType.JAVA, FileType.CLASS,
                FileType.JAR, FileType.MF, FileType.EXE, FileType.CHM };

        FileType[] videos = { FileType.AVI, FileType.RAM, FileType.RM, FileType.MPG, FileType.MOV, FileType.ASF, FileType.MP4, FileType.FLV, FileType.MID };

        FileType[] tottents = { FileType.TORRENT };

        FileType[] audios = { FileType.WAV, FileType.MP3 };

        FileType[] others = {};

        // 图片
        for (FileType fileType : pics) {
            if (fileType.equals(value)) {
                type = 1;
            }
        }
        // 文档
        for (FileType fileType : docs) {
            if (fileType.equals(value)) {
                type = 2;
            }
        }
        // 视频
        for (FileType fileType : videos) {
            if (fileType.equals(value)) {
                type = 3;
            }
        }
        // 种子
        for (FileType fileType : tottents) {
            if (fileType.equals(value)) {
                type = 4;
            }
        }
        // 音乐
        for (FileType fileType : audios) {
            if (fileType.equals(value)) {
                type = 5;
            }
        }
        return type;
    }
    public static boolean isDoc(FileType value) {
        return FileType.XLS_DOC.equals(value);
    }
    public static  boolean isDocx(FileType value) {
        return FileType.XLSX_DOCX.equals(value);
    }



    public static void main(String args[]) throws Exception {
        System.out.println(FileTypeJudge.isFileType(FileTypeJudge.getType(new FileInputStream(new File("C:\\Users\\ituser1\\Desktop\\123.pptx")))));
//    	for (FileType type : FileType.values()) {
//            System.out.print(type + "\t");
//        }
    }
}

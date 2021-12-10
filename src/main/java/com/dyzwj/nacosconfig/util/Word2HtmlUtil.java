package com.dyzwj.nacosconfig.util;

import fr.opensagres.poi.xwpf.converter.core.BasicURIResolver;
import fr.opensagres.poi.xwpf.converter.core.FileImageExtractor;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.FileItemFactory;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

@Component
public class Word2HtmlUtil {



    public String upload(MultipartFile multipartFile){
        try {
            //获取文件名
            String fileName = multipartFile.getOriginalFilename();
            //获取文件后缀
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            if(".docx".equals(prefix.toLowerCase())){
                return uploadDocXFile(multipartFile);
            }else if(".doc".equals(prefix.toLowerCase())){
                return uploadDocFile(multipartFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * doc
     * 上传Word文档，返回解析后的Html
     */
    public String uploadDocFile(MultipartFile file) throws Exception{
        String filePath = System.getProperty("java.io.tmpdir");
        String targetFileName = filePath +"/"+ "temp.html";
        File target = new File(targetFileName);
        target.getParentFile().mkdirs();
        //将上传的文件传入Document转换
        HWPFDocument wordDocument = new HWPFDocument(file.getInputStream());
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(document);
        // word文档转Html文档
        wordToHtmlConverter.processDocument(wordDocument);
        Document htmlDocument = wordToHtmlConverter.getDocument();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(new File(targetFileName));
        //将读取到的图片上传并添加链接地址
//        wordToHtmlConverter.setPicturesManager((imageStream, pictureType, name, width, height) -> {
//            try {
//                String imageUrl = uploadImages(imageStream);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return imageUrl;
//        });
        //生成html文件
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        // 读取并过滤文件格式
        String htmlContent = splitContext(targetFileName);
        // 删除生成的html文件
        File files = new File(targetFileName);
        files.delete();
        return htmlContent;
    }

    /**
     * 过滤html文件内容
     * @param filePath
     * @return
     */
    public static String splitContext(String filePath) {
        File file = new File(filePath);
        BufferedReader reader = null;
        try {
            String tempString;
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((tempString = reader.readLine()) != null) {
                sb.append(tempString);
            }
            reader.close();
            // 将文件中的双引号替换为单引号
            String content = sb.toString().replaceAll("\"","\'");
            return content;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return "";
    }

    /**
     * 上传docx文档，返回解析后的Html
     */
    public String uploadDocXFile(MultipartFile file) throws Exception{
        // 将上传的文件传入Document转换
        XWPFDocument docxDocument = new XWPFDocument(file.getInputStream());
        XHTMLOptions options = XHTMLOptions.create();
        // 设置图片存储路径
        String path = System.getProperty("java.io.tmpdir");
        String firstImagePathStr = path + "/" + System.currentTimeMillis();
        options.setExtractor(new FileImageExtractor(new File(firstImagePathStr)));
        options.URIResolver(new BasicURIResolver(firstImagePathStr));
        // 转换html
        ByteArrayOutputStream htmlStream = new ByteArrayOutputStream();
        XHTMLConverter.getInstance().convert(docxDocument, htmlStream, options);
        String htmlStr = htmlStream.toString();
        // 将image文件转换为base64并替换到html字符串里
        String middleImageDirStr = "/word/media";
        String imageDirStr = firstImagePathStr + middleImageDirStr;
        File imageDir = new File(imageDirStr);
        String[] imageList = imageDir.list();
//        String downloadPath;
//        if (imageList != null) {
//            for (int i = 0; i < imageList.length; i++) {
//                String oneImagePathStr = imageDirStr + "/" + imageList[i];
//                MultipartFile multipartFile = getMulFileByPath(oneImagePathStr);
//                String imageUrl = uploadImages(multipartFile);
//                // 也可以直接转成Base64格式处理，如下：
//                // String imageBase64Str = new String(Base64.encodeBase64(FileUtils.readFileToByteArray(oneImageFile)), "UTF-8");
//                //修改文档中的图片信息
//                htmlStr = htmlStr.replace(oneImagePathStr, imageUrl);
//            }
//        }
        //删除图片路径
        File firstImagePath = new File(firstImagePathStr);
        return htmlStr;
    }

    /**
     * 获取MultipartFile文件
     * @param picPath
     * @return
     */
//    private static MultipartFile getMulFileByPath(String picPath) {
//        FileItem fileItem = createFileItem(picPath);
//        MultipartFile mfile = new CommonsMultipartFile(fileItem);
//        return mfile;
//    }
    private static FileItem createFileItem(String filePath)
    {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String textFieldName = "textField";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "text/plain", true,
                "MyFileName" + extFile);
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try
        {
            FileInputStream fis = new FileInputStream(newfile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192))
                    != -1)
            {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return item;
    }

}

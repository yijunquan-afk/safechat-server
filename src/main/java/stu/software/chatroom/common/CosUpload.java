package stu.software.chatroom.common;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.region.Region;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author yjq
 * @version 1.0
 * @date 2022/4/28 23:00
 */
@RestController
@RequestMapping("/upload")
public class CosUpload {

    /**
     * 上传文件到腾讯云cos
     *
     * @param multipartFile 文件
     * @param prefix        前缀：自定义：比如上传图片加个前缀image
     * @return 上传结果
     * @throws IOException
     */
    @PostMapping("")
    @ResponseBody
    public Result uploadImg(@RequestParam("file") MultipartFile multipartFile, @RequestParam("prefix") String prefix) throws IOException {
        File f = null;
        try {
            f = File.createTempFile("tmp", null);
            multipartFile.transferTo(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //命名规则
        String imgName = multipartFile.getOriginalFilename();
        String[] arrays = new String[20];
        arrays = imgName.split("\\.");
        String filename = prefix + Utils.imgNameGenerator() + "." + arrays[arrays.length - 1];
        String path = SimpleUploadFileFromLocal(f, filename);
        f.deleteOnExit();
        return Result.success("上传成功", path);
    }

    /**
     * 上传文件
     * @param fileInputStream
     * @param name
     * @return 文件链接
     */
    public String SimpleUploadFileFromLocal(File fileInputStream, String name) {
        String etag = "";
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials("", "");
        // 2 设置bucket的区域
        ClientConfig clientConfig = new ClientConfig(new Region(""));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket名需包含appid
        String bucketName = "";
        String key = "/img/" + name;
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, fileInputStream);
        // 设置存储类型, 默认是标准(Standard), 低频(standard_ia), 近线(nearline)
        putObjectRequest.setStorageClass(StorageClass.Standard_IA);
        try {
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            // putobjectResult会返回文件的etag
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        }
        // 关闭客户端
        cosclient.shutdown();
        return etag + key;//返回图片的链接
    }
}

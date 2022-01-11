package com.cnegs.test;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @Author Cnegs
 * @Date 2022/1/10 22:42
 */
@RunWith(SpringRunner.class)
public class QiniuYunTest {

        @Test
         public void test(){
            //构造一个带指定 Region 对象的配置类
            Configuration cfg = new Configuration(Zone.autoZone());
//...其他参数参考类注释

            UploadManager uploadManager = new UploadManager(cfg);
//...生成上传凭证，然后准备上传
            String accessKey = "f4aTq90vmUnwIMUvt68-Wm7GdklceOJq_z7ARQY-";
            String secretKey = "3Q8JPHg-KfVctudJMZAwDOBZVfEmGo2a2zjg2waH";
            String bucket = "cnegshealth-space";
//如果是Windows情况下，格式是 D:\\qiniu\\test.png
            String localFilePath = "F:\\壁纸\\城市风景\\200420120121-1.jpg";
//默认不指定key的情况下，以文件内容的hash值作为文件名
            String key = "xiaolu.jpg";

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(localFilePath, key, upToken);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }

        }

}

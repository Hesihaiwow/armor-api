package com.zhixinhuixue.armor.source;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Akuma on 16/2/18.
 */
public class FileAction  {


    @Autowired
    private FastdfsProperty fastdfs;

    private static final Logger logger = LoggerFactory.getLogger(FileAction.class);

    /**
     * 上传图片
     *
     * @param uploadFile
     * @return
     * @throws MyException
     * @throws IOException
     */
    @RequestMapping(value = "image", produces = "application/json; charset=utf-8")
    public
    @ResponseBody
    String images(@RequestParam(value = "uploadFile") MultipartFile uploadFile) {
        String suffix = "." + getUploadSuffix(uploadFile.getOriginalFilename());
        if (!ZSYStatic.filterImg(suffix)) {
            return ZSYResult.fail().msg("只能上传图片格式").build();
        }

        try {
            JSONObject result = new JSONObject();
            result.put("url", upload(uploadFile));
            return ZSYResult.success().data(result).build();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return ZSYResult.fail().build();
    }

    /**
     * 上传 文件
     *
     * @return
     */
    @RequestMapping(value = "file", produces = "application/json; charset=utf-8")
    @ResponseBody
    private String uploadFile(@RequestParam(value = "uploadFile") MultipartFile multipartFile) {
        try {
            if (multipartFile.getBytes().length / (1024 * 1024) > 10) {
                return ZSYResult.fail().msg("上传的文件大小超过最大限时(10MB)").build();
            }
            JSONObject result = new JSONObject();
            result.put("url", upload(multipartFile));
            result.put("filename", multipartFile.getOriginalFilename());
            result.put("size", multipartFile.getBytes().length / 1024);
            return ZSYResult.success().data(result).build();
        } catch (IOException | MyException e) {
            e.printStackTrace();
        }
        return ZSYResult.fail().build();
    }

    /**
     * 上传到 DFS
     *
     * @param uploadFile
     * @return
     * @throws IOException
     * @throws MyException
     */
    private String upload(MultipartFile uploadFile) throws IOException, MyException {
//            ClientGlobal.setG_connect_timeout(fastdfs.getConnectTimeout());
//            ClientGlobal.setG_network_timeout(fastdfs.getNetworkTimeout());
        ClientGlobal.setG_charset(fastdfs.getCharset());
        ClientGlobal.setG_tracker_http_port(fastdfs.getTrackerHttpPort());
        InetSocketAddress[] tracker_servers = new InetSocketAddress[fastdfs.getTrackerServer().length];

        for (int i = 0; i < fastdfs.getTrackerServer().length; ++i) {
            String[] parts = fastdfs.getTrackerServer()[i].split("\\:", 2);
            if (parts.length != 2) {
                throw new MyException("the value of item \"tracker_server\" is invalid, the correct format is host:port");
            }

            tracker_servers[i] = new InetSocketAddress(parts[0].trim(), Integer.parseInt(parts[1].trim()));
        }
        ClientGlobal.setG_tracker_group(new TrackerGroup(tracker_servers));
        ClientGlobal.setG_anti_steal_token(fastdfs.isAntiStealToken());

        TrackerClient tracker = new TrackerClient();
        TrackerServer trackerServer = tracker.getConnection();
        StorageServer storageServer = null;

        StorageClient storageClient = new StorageClient(trackerServer, storageServer);


        String fileIds[] = storageClient.upload_file(uploadFile.getBytes(), getUploadSuffix(uploadFile.getOriginalFilename()), null);
        logger.info("组名：" + fileIds[0]);
        logger.info("路径: " + fileIds[1]);

        return getUploadPath("/" + fileIds[0] + "/" + fileIds[1]);
    }



    /**
     * 获取上传文件后缀名
     *
     * @param uploadName
     * @return
     */
    public String getUploadSuffix(String uploadName) {
        return uploadName.substring(uploadName.lastIndexOf(".") + 1);
    }

    /**
     * 获取上传文件路径
     *
     * @param path
     * @return
     */
    public String getUploadPath(String path) {
        return fastdfs.getDomain() + path;
    }



}

package com.find.dog.utils;

import android.util.Log;

import com.sina.cloudstorage.SCSClientException;
import com.sina.cloudstorage.SCSServiceException;
import com.sina.cloudstorage.auth.AWSCredentials;
import com.sina.cloudstorage.auth.BasicAWSCredentials;
import com.sina.cloudstorage.event.ProgressEvent;
import com.sina.cloudstorage.event.ProgressListener;
import com.sina.cloudstorage.services.scs.SCS;
import com.sina.cloudstorage.services.scs.SCSClient;
import com.sina.cloudstorage.services.scs.model.AccessControlList;
import com.sina.cloudstorage.services.scs.model.Bucket;
import com.sina.cloudstorage.services.scs.model.ObjectMetadata;
import com.sina.cloudstorage.services.scs.model.Permission;
import com.sina.cloudstorage.services.scs.model.PutObjectResult;
import com.sina.cloudstorage.services.scs.model.S3Object;
import com.sina.cloudstorage.services.scs.model.UserIdGrantee;
import com.sina.cloudstorage.services.scs.transfer.TransferManager;
import com.sina.cloudstorage.services.scs.transfer.Upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangzhongwei on 2017/7/19.
 */

public class SCSResultUtil {
    private static SCSResultUtil mSCSResultUtil;
    private SCS conn;

    public static SCSResultUtil getInstance() {
        if (mSCSResultUtil == null) {
            mSCSResultUtil = new SCSResultUtil();
        }
        return mSCSResultUtil;
    }

    public SCSResultUtil(){
        String accessKey = "2w2zequPAZNu6X97bbQt";
        String secretKey = "32d4f39ccfb20c10d0cd4f1948656a0404d980f4";
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        conn = new SCSClient(credentials);
    }


    /**
     * 生成url
     * @param bucketName bucket名称
     * @param url_path 文件路径
     */
    public void generateUrl(String bucketName,String url_path){
        Date expiration = new Date();       //过期时间
        long epochMillis = expiration.getTime();
        epochMillis += 60*5*1000;
        expiration = new Date(epochMillis);

        URL presignedUrl = conn.generatePresignedUrl(bucketName, url_path, expiration, false);
        System.out.println(presignedUrl);
    }

    /**
     * 获取所有bucket
     */
    public void getAllBuckets(){
        List<Bucket> list = conn.listBuckets();
        System.out.println("====getAllBuckets===="+list);
    }

    /**
     * 创建bucket
     */
    public void createBucket(String bucket_name){
        Bucket bucket = conn.createBucket(bucket_name);
        System.out.println(bucket);
    }
    /**
     * 删除bucket
     */
    public void deleteBucket(String create_a_bucket){
        conn.deleteBucket(create_a_bucket);
    }
    /**
     * 获取bucket ACL
     */
    public void getBucketAcl(String create_a_bucket){
        AccessControlList acl = conn.getBucketAcl(create_a_bucket);
        System.out.println(acl);
    }
    /**
     * 设置bucket acl
     */
    public void putBucketAcl(String UserId,String create_a_bucket){
        AccessControlList acl = new AccessControlList();
        acl.grantPermissions(UserIdGrantee.CANONICAL, Permission.Read, Permission.ReadAcp);
        acl.grantPermissions(UserIdGrantee.ANONYMOUSE,
                Permission.ReadAcp,
                Permission.Write,
                Permission.WriteAcp);
        acl.grantPermissions(new UserIdGrantee(UserId),
                Permission.Read,
                Permission.ReadAcp,
                Permission.Write,
                Permission.WriteAcp);

        conn.setBucketAcl(create_a_bucket, acl);
    }

    //获取object metadata  ---------------------------------------获取object
    /**
     *获取object metadata
     * @param bucket_name
     * @param file "/test/file.txt"
     */
    public void getObjectMeta(String bucket_name,String file){
        ObjectMetadata objectMetadata = conn.getObjectMetadata(bucket_name, file);
        System.out.println(objectMetadata.getUserMetadata());
        System.out.println(objectMetadata.getContentLength());
        System.out.println(objectMetadata.getRawMetadata());
        System.out.println(objectMetadata.getETag());
    }

    /**
     * 下载object
     *  //断点续传
     *  GetObjectRequest rangeObjectRequest = new GetObjectRequest("test11", "/test/file.txt");
     *  rangeObjectRequest.setRange(0, 10); // retrieve 1st 10 bytes.
     *  S3Object objectPortion = conn.getObject(rangeObjectRequest);
     *
     *  InputStream objectData = objectPortion.getObjectContent();
     *  // "Process the objectData stream.
     *  objectData.close();
     */
    public void getObject(){
        //SDKGlobalConfiguration.setGlobalTimeOffset(-60*5);//自定义全局超时时间5分钟以后(可选项)
        S3Object s3Obj = conn.getObject("test11", "/test/file.txt");
        InputStream in = s3Obj.getObjectContent();
        byte[] buf = new byte[1024];
        OutputStream out = null;
        try {
            out = new FileOutputStream(new File("dage1.txt"));
            int count;
            while( (count = in.read(buf)) != -1)
            {
                if( Thread.interrupted() )
                {
                    throw new InterruptedException();
                }
                out.write(buf, 0, count);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            //SDKGlobalConfiguration.setGlobalTimeOffset(0);//还原超时时间
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     *上传文件
     * @param bucket_name bucket名称
     * @param url_path 文件上传路径
     * @param file 本地文件
     */
    public void putObject(String bucket_name,String url_path,String file){
        PutObjectResult putObjectResult = conn.putObject(bucket_name,url_path,new File(file));
        Log.d("H","上传---"+putObjectResult);
    }

    /**
     * 上传文件 自定义请求头
     */
    public void putObjectWithCustomRequestHeader(){
        //自定义请求头k-v
        Map<String, String> requestHeader = new HashMap<String, String>();
        requestHeader.put("x-sina-additional-indexed-key", "stream/test111.txt");
        PutObjectResult putObjectResult = conn.putObject("bucket名称", "ssk/a/",
                new File("本地文件"), requestHeader);
        System.out.println(putObjectResult);//服务器响应结果
    }

    /**
     * 秒传
     */
    public void putObjectRelax(long length){
        conn.putObjectRelax("bucket名称","文件上传路径","被秒传文件的sina_sha1值",length);
    }

    /**
     * 拷贝object
     */
    public void copyObject(){
        conn.copyObject("源bucket名称", "源文件路径", "目标bucket名称", "目标文件路径");
    }

    /**
     * 设置object metadata
     */
    @SuppressWarnings("serial")
    public void putObjectMeta(){
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setUserMetadata(new HashMap<String,String>(){{
            put("aaa","1111");
            put("bbb","222");
            put("ccc","3333");
            put("asdfdsaf","vvvvvv");
        }});
        conn.setObjectMetadata("bucket名称", "文件路径", objectMetadata);
    }

    /**
     * 删除Object
     * @param bucket_name bucket名称
     * @param url_path 文件路径
     */
    public void deleteObject(String bucket_name,String url_path){
        conn.deleteObject(bucket_name, url_path);
    }

    /**
     * 获取object acl
     */
    public void getObjectAcl(){
        AccessControlList acl = conn.getObjectAcl("bucket名称", "文件路径");
        System.out.println(acl);
    }

    /**
     * 设置object acl
     */
    public void putObjectAcl(){
        AccessControlList acl = new AccessControlList();
        acl.grantPermissions(UserIdGrantee.CANONICAL, Permission.Read,Permission.ReadAcp);
        acl.grantPermissions(UserIdGrantee.ANONYMOUSE,Permission.ReadAcp,Permission.Write,Permission.WriteAcp);
        acl.grantPermissions(new UserIdGrantee("UserId"), Permission.Read,Permission.ReadAcp,Permission.Write,Permission.WriteAcp);

        conn.setObjectAcl("bucket名称", "文件路径", acl);
    }

//    /**
//     * 分片上传文件
//     * @throws Exception
//     */
//    public void multipartsUpload() throws Exception{
//        //初始化上传任务
//        InitiateMultipartUploadResult initiateMultipartUploadResult = conn.initiateMultipartUpload("bucket名称", "文件路径");
//
//        if(initiateMultipartUploadResult!=null){
//            //分片上传
//            List<PartETag> partETags = null;
//            PutObjectRequest putObjectRequest = new PutObjectRequest(initiateMultipartUploadResult.getBucketName(),
//                    initiateMultipartUploadResult.getKey(), new File("本地待上传文件"));
//            try {
//                long optimalPartSize = 5 * 1024 * 1024; //分片大小5M
//                UploadPartRequestFactory requestFactory = new UploadPartRequestFactory(putObjectRequest, initiateMultipartUploadResult.getUploadId()
//                        , optimalPartSize);
//
//                partETags = uploadPartsInSeries(requestFactory);
//            } catch (Exception e) {
//                throw e;
//            } finally {
//                if (putObjectRequest.getInputStream() != null) {
//                    try {
//                        putObjectRequest.getInputStream().close();
//                    } catch (Exception e) {
//                        throw e;
//                    }
//                }
//            }
//
//            //分片列表
//            PartListing partList = conn.listParts(new ListPartsRequest(initiateMultipartUploadResult.getBucketName(),
//                    initiateMultipartUploadResult.getKey(),
//                    initiateMultipartUploadResult.getUploadId()));
//            System.out.println("已上传的文件分片列表:\n"+partList);
//
//            //分片合并，完成上传
//            ObjectMetadata objectMetadata = conn.completeMultipartUpload(new CompleteMultipartUploadRequest(putObjectRequest.getBucketName(),
//                    putObjectRequest.getKey(), initiateMultipartUploadResult.getUploadId(), partETags));
//
//            System.out.println("合并文件结果:\n");
//            System.out.println(objectMetadata.getUserMetadata());
//            System.out.println(objectMetadata.getContentLength());
//            System.out.println(objectMetadata.getRawMetadata());
//            System.out.println(objectMetadata.getETag());
//        }
//
//    }

    /* TransferManager */
    public void putObjectByTransferManager(){
        TransferManager tf = new TransferManager(conn);
        Upload myUpload = tf.upload("bucket名称", "文件路径", new File("待上传文件本地路径"));

        // You can poll your transfer's status to check its progress
        if (myUpload.isDone() == false) {
            System.out.println("Transfer: " + myUpload.getDescription());
            System.out.println("  - State: " + myUpload.getState());
            System.out.println("  - Progress: "
                    + myUpload.getProgress().getBytesTransferred());
        }

        // Transfers also allow you to set a <code>ProgressListener</code> to
        // receive
        // asynchronous notifications about your transfer's progress.
        myUpload.addProgressListener(new ProgressListener(){
            @Override
            public void progressChanged(ProgressEvent progressEvent) {
                System.out.println(progressEvent);
            }
        });

        // Or you can block the current thread and wait for your transfer to
        // to complete. If the transfer fails, this method will throw an
        // SCSClientException or SCSServiceException detailing the reason.
        try {
            myUpload.waitForCompletion();
        } catch (SCSServiceException e) {
            e.printStackTrace();
        } catch (SCSClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}

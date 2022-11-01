package HEIG.vd;

import HEIG.vd.interfaces.IDataObjectHelper;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.*;

import static com.amazonaws.regions.Regions.EU_WEST_2;

public class AwsDataObjectHelperImpl implements IDataObjectHelper {
    @Override
    public void create(String objectName) {

    }

    static public boolean isObjectExist(AmazonS3 s3, String bucketUrl, String objectName){
        return s3.doesObjectExist(bucketUrl, objectName);
    }

    public static boolean isBucketExist(AmazonS3 s3, String bucketUrl) {
        return s3.doesBucketExistV2(bucketUrl);

    }

    private String bucketUrl;
    AmazonS3 cloudClient;

    AwsDataObjectHelperImpl(String bucketUrl){
        this.bucketUrl = bucketUrl;
        cloudClient = AmazonS3ClientBuilder.standard()
                .withRegion(EU_WEST_2)
                .build();

    }

    public void getInfo(){
        System.out.print(this.bucketUrl);
        System.out.print(cloudClient);
    }

    public boolean exists(String url){
        if(url.contains("/")){
            String objectName = url.substring(url.lastIndexOf("/") + 1);
            return AwsDataObjectHelperImpl.isObjectExist(cloudClient, bucketUrl, objectName);
        } else {
            return AwsDataObjectHelperImpl.isBucketExist(cloudClient, bucketUrl);
        }

    }

    public void createObject(String bucketUrl){
        if(!exists(bucketUrl)){
            try {
                cloudClient.createBucket(bucketUrl);
            } catch (AmazonS3Exception e) {
                System.err.println(e.getErrorMessage());
            }
        }
    }

    public void createObject(String objectUrl, String path){
        if(!exists(objectUrl)){
            String objectName = objectUrl.substring(objectUrl.lastIndexOf("/") + 1);

            try {
                File object = new File(path);
                InputStream in = new FileInputStream(object);

                ObjectMetadata om = new ObjectMetadata();
                om.setContentType("images/jpg");
                om.setContentLength(object.length());

                PutObjectRequest objectRequest =  new PutObjectRequest(bucketUrl, objectName, in, om);
                cloudClient.putObject(objectRequest);

            } catch (AmazonServiceException e) {
                System.err.println(e.getErrorMessage());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


}

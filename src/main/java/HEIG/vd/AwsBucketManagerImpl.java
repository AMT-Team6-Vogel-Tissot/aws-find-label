package HEIG.vd;

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
import java.util.List;

import static com.amazonaws.regions.Regions.EU_WEST_2;


public class AwsBucketManagerImpl
{

    private String bucketUrl;
    AmazonS3 cloudClient;

    AwsBucketManagerImpl(String bucketUrl){
        this.bucketUrl = bucketUrl;
        cloudClient = AmazonS3ClientBuilder.standard()
                .withRegion(EU_WEST_2)
                .build();

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
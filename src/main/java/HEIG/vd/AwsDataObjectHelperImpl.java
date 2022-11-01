package HEIG.vd;

import HEIG.vd.interfaces.IDataObjectHelper;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;

import java.util.List;

public class AwsDataObjectHelperImpl implements IDataObjectHelper {

    AmazonS3 profile;

    @Override
    public void create(String objectName) {

    }

    AwsDataObjectHelperImpl(AmazonS3 profile){

        this.profile = profile;

    }

    public void ListBuckets() {
        List<Bucket> buckets = profile.listBuckets();
        System.out.println("Your {S3} buckets are:");
        for (Bucket b : buckets) {
            System.out.println("* " + b.getName());
        }
    }

/*
    static public boolean Exist(AmazonS3 s3, String bucketUrl, String objectName){
        return s3.doesObjectExist(bucketUrl, objectName);
    }

    AwsDataObjectHelperImpl(ProfileCredentialsProvider profile){
       this.bucketUrl = bucketUrl;
        cloudClient = AmazonS3ClientBuilder.standard()
                .withRegion(EU_WEST_2)
                .credentialsProvider(ProfileCredentialsProvider.create("profile_name"))
                .build();

    }

    @Override
    public void create(String objectName){
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

*/
}

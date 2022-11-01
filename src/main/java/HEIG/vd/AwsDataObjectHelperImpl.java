package HEIG.vd;

import HEIG.vd.interfaces.IDataObjectHelper;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import java.util.List;

public class AwsDataObjectHelperImpl implements IDataObjectHelper {

    AmazonS3 profile;

    public AwsDataObjectHelperImpl(AmazonS3 profile){
        this.profile = profile;
    }
    @Override
    public void create(String objectName) {

    }

    public boolean existBucket(String name){
        return profile.doesBucketExistV2(name);
    }

    public boolean existObject(String nameBucket, String nameObject){
        return profile.doesObjectExist(nameBucket, nameObject);
    }

    public void ListBuckets() {
        List<Bucket> buckets = profile.listBuckets();
        System.out.println("Your {S3} buckets are:");
        for (Bucket b : buckets) {
            System.out.println("* " + b.getName());
        }
    }

    public void ListObjects(String nameBucket){

        ObjectListing objects = profile.listObjects(nameBucket);
        List<S3ObjectSummary> summary = objects.getObjectSummaries();

        for(S3ObjectSummary s : summary){
            System.out.println("Object Id :-" + s.getKey());
        }
    }

    /*
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

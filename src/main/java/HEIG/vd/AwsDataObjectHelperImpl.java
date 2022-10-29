package HEIG.vd;

import HEIG.vd.interfaces.IDataObjectHelper;
import com.amazonaws.services.s3.AmazonS3;

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


}

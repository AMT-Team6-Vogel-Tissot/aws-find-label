package HEIG.vd;

import HEIG.vd.interfaces.ICloudClient;

import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import static software.amazon.awssdk.regions.Region.EU_WEST_2;

public class AwsCloudClient implements ICloudClient {

    AwsDataObjectHelperImpl dataObject;
    AwsLabelDetectorHelperImpl labelDetector;

    private final String bucketUrl;

    AwsCloudClient(String bucketUrl){

        S3Client cloudClient = S3Client.builder()
                .region(EU_WEST_2)
                .credentialsProvider(ProfileCredentialsProvider.builder().profileName("amt06").build())
                .build();

        this.bucketUrl = bucketUrl;
        dataObject = new AwsDataObjectHelperImpl(cloudClient);
        labelDetector = new AwsLabelDetectorHelperImpl(cloudClient);
    }


    public AwsCloudClient getInstance() {
        return this;
    }

    public String getBucketUrl(){
        return bucketUrl;
    }
}

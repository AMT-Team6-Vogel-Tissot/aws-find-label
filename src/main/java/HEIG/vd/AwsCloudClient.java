package HEIG.vd;

import HEIG.vd.interfaces.ICloudClient;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import static com.amazonaws.regions.Regions.EU_WEST_2;

public class AwsCloudClient implements ICloudClient {

    AwsDataObjectHelperImpl dataObject;
    AwsLabelDetectorHelperImpl labelDetector;

    AwsCloudClient(String profileName){
        AWSCredentialsProvider provider = new ProfileCredentialsProvider(profileName);

        AmazonS3 cloudClient = AmazonS3ClientBuilder.standard()
                .withRegion(EU_WEST_2)
                .withCredentials(provider)
                .build();

        dataObject = new AwsDataObjectHelperImpl(cloudClient);
        labelDetector = new AwsLabelDetectorHelperImpl(cloudClient);
    }

    @Override
    public AwsCloudClient getInstance() {
        return this;
    }
}

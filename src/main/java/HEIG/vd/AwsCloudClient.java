package HEIG.vd;

import HEIG.vd.interfaces.ICloudClient;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.S3Client;
import static software.amazon.awssdk.regions.Region.EU_WEST_2;

public class AwsCloudClient implements ICloudClient {

    private AwsDataObjectHelperImpl dataObject;
    private AwsLabelDetectorHelperImpl labelDetector;

    private static AwsCloudClient INSTANCIED = null;

    private final String bucketUrl;

    private AwsCloudClient(String bucketUrl){

        Dotenv env = Dotenv.configure()
                .ignoreIfMissing()
                .systemProperties()
                .load();

        StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider
                .create(AwsBasicCredentials.create(env.get("AWS_ACCESS_KEY"),
                        env.get("AWS_SECRET_KEY")));

        S3Client cloudClient = S3Client.builder()
                .region(EU_WEST_2)
                .credentialsProvider(credentialsProvider)
                .build();

        this.bucketUrl = bucketUrl;
        dataObject = new AwsDataObjectHelperImpl(cloudClient);
        labelDetector = new AwsLabelDetectorHelperImpl(cloudClient);
    }

    public static AwsCloudClient getInstance(String bucketName) {
        if(INSTANCIED == null){
            INSTANCIED = new AwsCloudClient(bucketName);
        }
        return INSTANCIED;
    }

    public AwsDataObjectHelperImpl getDataObject() {
        return dataObject;
    }

    public AwsLabelDetectorHelperImpl getLabelDetector() {
        return labelDetector;
    }

    public String getBucketUrl(){
        return bucketUrl;
    }
}

package HEIG.vd;

public class AwsBucketManagerImpl {
    public static void main(String[] args) {

        AwsCloudClient client = new AwsCloudClient();

        client.dataObject.ListBuckets();

    }
}
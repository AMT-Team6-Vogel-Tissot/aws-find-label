package HEIG.vd;

public class AwsBucketManagerImpl {
    public static void main(String[] args) {

        AwsCloudClient client = new AwsCloudClient();

        client.dataObject.ListBuckets();

        System.out.println(client.dataObject.existObject("amt.team06.diduno.education","test"));
        System.out.println(client.dataObject.existBucket("amt.team06.diduno.education"));

        client.dataObject.ListObjects("amt.team06.diduno.education");

    }
}
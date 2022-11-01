package HEIG.vd;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AwsBucketManagerImpl {
    public static void main(String[] args) {

        AwsCloudClient client = new AwsCloudClient();

        client.dataObject.ListBuckets();

        System.out.println(client.dataObject.existObject("amt.team06.diduno.education","test"));
        System.out.println(client.dataObject.existBucket("amt.team03.diduno.education"));

        client.dataObject.ListObjects("amt.team06.diduno.education");

        client.dataObject.create("test", Paths.get("./src/file/file.txt"));

        client.dataObject.ListObjects("amt.team06.diduno.education");

    }
}
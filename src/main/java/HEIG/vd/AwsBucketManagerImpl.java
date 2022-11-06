package HEIG.vd;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AwsBucketManagerImpl {
    public static void main(String[] args) {

        AwsCloudClient client = new AwsCloudClient("amt.team06.diduno.education");

        //URL u = client.dataObject.createObject(client.getBucketUrl(),"3", Paths.get("./filesTest/file1.jpg"));

        System.out.println(client.dataObject.listObjects(client.getBucketUrl()));
    }
}
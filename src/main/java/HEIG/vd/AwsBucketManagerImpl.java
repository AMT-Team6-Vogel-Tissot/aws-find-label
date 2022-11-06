package HEIG.vd;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class AwsBucketManagerImpl {
    public static void main(String[] args) throws IOException {

        AwsCloudClient client = AwsCloudClient.getInstance("amt.team06.diduno.education");
        AwsDataObjectHelperImpl dataObjectHelp = client.getDataObject();
        AwsLabelDetectorHelperImpl labelDetectorHelp = client.getLabelDetector();

        System.out.println(dataObjectHelp.listObjects(client.getBucketUrl()));

        byte[] fileContent = Files.readAllBytes(Path.of("./filesTest/file1.jpg"));
        URL u = dataObjectHelp.createObject(client.getBucketUrl(),"testFile", fileContent);


        System.out.println(dataObjectHelp.listObjects(client.getBucketUrl()));

        Map<String, String> labelsConfidence = labelDetectorHelp.execute(client.getBucketUrl(),"testFile", new int[]{1, 2});

        System.out.println(dataObjectHelp.listObjects(client.getBucketUrl()));

        System.out.println(new String(dataObjectHelp.getObject(client.getBucketUrl(), "testFile-result")));

        dataObjectHelp.removeObject(client.getBucketUrl(), "testFile");
        dataObjectHelp.removeObject(client.getBucketUrl(), "testFile-result");
    }
}
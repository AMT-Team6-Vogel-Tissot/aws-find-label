package HEIG.vd;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {

        String objectName = "testFile";
        Path path = Path.of("./filesTest/file1.jpg");

        System.out.println("Loading des credentials...");
        AwsCloudClient client = AwsCloudClient.getInstance("amt.team06.diduno.education");
        AwsDataObjectHelperImpl dataObjectHelp = client.getDataObject();
        AwsLabelDetectorHelperImpl labelDetectorHelp = client.getLabelDetector();


        System.out.println("Lecture du fichier se trouvant au chemin : " + path);
        byte[] fileContent = Files.readAllBytes(path);

        System.out.println("Création de l'objet dans AWS S3...");
        URL u = dataObjectHelp.createObject(client.getBucketUrl(),objectName, fileContent);

        System.out.println("Lien pour accéder à l'objet (disponible 10min) : " + u);

        System.out.println("Détection des labels de l'image et création du fichier " + objectName + "-result...");
        Map<String, String> labelsConfidence = labelDetectorHelp.execute(client.getBucketUrl(), objectName, new int[]{1, 2});

        System.out.println("Affichage des labels : " + labelsConfidence);

        System.out.println("Affichage des labels récupéré depuis le fichier" +  objectName + "-result : ");

        System.out.println(new String(dataObjectHelp.getObject(client.getBucketUrl(), objectName + "-result")));

    }
}
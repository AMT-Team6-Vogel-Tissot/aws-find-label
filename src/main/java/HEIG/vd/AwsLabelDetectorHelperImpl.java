package HEIG.vd;

import HEIG.vd.interfaces.ILabelDetector;
import com.google.gson.Gson;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Question à poser :
// ça veut dire quoi : intégrée à la requête api (encodée dans la request api),

public class AwsLabelDetectorHelperImpl implements ILabelDetector {

    S3Client profile;
    RekognitionClient rekClient;

    int LIMIT_MAX_LABELS = 10;

    public AwsLabelDetectorHelperImpl(S3Client profile){
        this.profile = profile;
        rekClient = RekognitionClient
                .builder()
                .build();
    }

    @Override
    public Map<String, String> execute(String nameBucket, String nameObject, int[] params) {

        AwsCloudClient client = AwsCloudClient.getInstance(nameBucket);
        Map<String, String> labelsConfidence = new HashMap<>();

        String nameObjectResult = nameObject + "-result";

        Gson gson = new Gson();

        if(!client.getDataObject().existObject(nameBucket, nameObjectResult)){
            try {
                S3Object s3Object = S3Object
                        .builder()
                        .bucket(nameBucket)
                        .name(nameObject)
                        .build();

                Image myImage = Image
                        .builder()
                        .s3Object(s3Object)
                        .build();

                DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest
                        .builder()
                        .image(myImage)
                        .maxLabels(LIMIT_MAX_LABELS)
                        .build();

                DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);

                List<Label> labels = labelsResponse.labels();

                for (Label label: labels) {
                    labelsConfidence.put(label.name(), label.confidence().toString());
                }

                String json = gson.toJson(labelsConfidence);

                client.getDataObject().createObject(nameBucket, nameObjectResult, json.getBytes());

            } catch (RekognitionException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }

        return labelsConfidence;
    }
}

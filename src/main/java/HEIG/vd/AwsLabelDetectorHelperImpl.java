package HEIG.vd;

import HEIG.vd.interfaces.ILabelDetector;
import HEIG.vd.utils.GetEnvVal;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AwsLabelDetectorHelperImpl implements ILabelDetector {

    private final RekognitionClient rekClient;

    private final String nameBucket;

    public AwsLabelDetectorHelperImpl(StaticCredentialsProvider credentialsProvider, Region region){

        rekClient = RekognitionClient
                .builder()
                .credentialsProvider(credentialsProvider)
                .region(region)
                .build();

        nameBucket = GetEnvVal.getEnvVal("BUCKET");
    }

    @Override
    public Map<String, String> executeBase64(String base64Image, int maxLabels, float minConfidence) {
        byte[] imageBytes = Base64.getDecoder().decode(base64Image);

        Image image = Image
                .builder()
                .bytes(SdkBytes.fromByteArray(imageBytes))
                .build();

        return executeRekognition(image, maxLabels, minConfidence);
    }

    public Map<String, String> execute(String nameObject, int maxLabels, float minConfidence) {

        AwsCloudClient client = AwsCloudClient.getInstance();
        String nameObjectResult = nameObject + "-result";
        Map<String, String> result;
        Gson gson = new Gson();

        if (!client.getDataObject().exist(nameObjectResult)) {

            S3Object s3Object = S3Object
                    .builder()
                    .bucket(nameBucket)
                    .name(nameObject)
                    .build();

            Image myImage = Image
                    .builder()
                    .s3Object(s3Object)
                    .build();

            result = executeRekognition(myImage, maxLabels, minConfidence);

            String json = gson.toJson(result);

            client.getDataObject().create(nameObjectResult, json.getBytes());

            return result;

        } else {
            String labels = new String(client.getDataObject().get(nameObjectResult));
            Type mapType = new TypeToken<Map<String, String>>() {}.getType();

            return gson.fromJson(labels, mapType);
        }

    }

    private Map<String, String> executeRekognition(Image image, int maxLabels, float minConfidence) {

        Map<String, String> labelsConfidence = new HashMap<>();

        try {

            DetectLabelsRequest detectLabelsRequest = DetectLabelsRequest
                    .builder()
                    .image(image)
                    .maxLabels(maxLabels)
                    .minConfidence(minConfidence)
                    .build();

            DetectLabelsResponse labelsResponse = rekClient.detectLabels(detectLabelsRequest);

            List<Label> labels = labelsResponse.labels();

            for (Label label: labels) {
                labelsConfidence.put(label.name(), label.confidence().toString());
            }

        } catch (RekognitionException e) {
            System.out.println(e.getMessage());
        }

        return labelsConfidence;
    }
}

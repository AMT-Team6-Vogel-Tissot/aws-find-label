package HEIG.vd;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Map;

public class AwsLabelDetectorHelperImplTest {

    private final AwsCloudClient bucketManager = AwsCloudClient.getInstance();
    private final AwsDataObjectHelperImpl dataObjectHelper = bucketManager.getDataObject();
    private final AwsLabelDetectorHelperImpl labelDetectorHelper = bucketManager.getLabelDetector();

    private final String pathToTestFolder = "./filesTest/";
    private final String image1 = "file1.jpg";
    private final String image1Result = image1 + "-result";
    private final Path path1 = Path.of(this.pathToTestFolder, this.image1);

    private final Map<String, String> correctMapNormalCase = Map.of(
            "Building","98.851166","Campus","83.2322",
            "School","69.49009","Candle","65.13202","Office Building","98.851166",
            "Outdoors","92.17462","City","57.828415","Aerial View","63.52579",
            "Person","74.04278","Convention Center","57.032322"
    );

    private final Map<String, String> correctMapChangeNbLabelsCase = Map.of(
            "Building","98.851166","Campus","83.2322", "Office Building","98.851166",
            "Outdoors","92.17462", "Person", "74.04278"
    );

    private final Map<String, String> correctMapChangeMinConfidenceCase = Map.of(
            "Building","98.851166","Campus","83.2322","Architecture", "98.851166",
            "Office Building","98.851166", "Outdoors","92.17462"
    );

    private final String correctMinValNormalCase = "57.032322";
    private final String correctMinValChangeNbLabelsCase = "74.04278";
    private final String correctMinValChangeMinConfidenceCase = "83.2322";
    private final int correctMaxLabelsMinConfidenceCase = 5;
    private final int maxLabelsNormalCase = 10;
    private final int minConfidenceNormalCase = 50;
    private final int maxLabelsChangeNbLabelsCase = 5;
    private final int minConfidenceChangeNbLabelsCase = 80;

    @AfterEach
    public void cleanUpEach(){
        if(this.dataObjectHelper.exist(this.image1)){
            this.dataObjectHelper.delete(this.image1);
        }
        if(this.dataObjectHelper.exist(this.image1Result)){
            this.dataObjectHelper.delete(this.image1Result);
        }
    }

    @Test
    public void Execute_RecoverLabelsWithResultObjectNotExisting_Success() throws IOException {
        // given
        Map<String, String> mapResult;
        assertFalse(this.dataObjectHelper.exist(this.image1));
        assertFalse(this.dataObjectHelper.exist(this.image1Result));
        assertTrue(this.dataObjectHelper.exist());
        String minVal = "101";

        this.dataObjectHelper.create(this.image1, Files.readAllBytes(this.path1));

        // when
        mapResult = this.labelDetectorHelper.execute(this.image1, maxLabelsNormalCase, minConfidenceNormalCase);
        for (Map.Entry<String, String> val : mapResult.entrySet()) {
            if(Float.parseFloat(minVal) > Float.parseFloat(val.getValue())){
                minVal = val.getValue();
            }
        }

        // then
        assertTrue(this.dataObjectHelper.exist(this.image1Result));
        assertEquals(this.correctMapNormalCase, mapResult);
        assertEquals(this.maxLabelsNormalCase, mapResult.size());
        assertEquals(this.correctMinValNormalCase, minVal);
    }

    @Test
    public void Execute_RecoverLabelsWithLimitNumberLabels5_Success() throws IOException {
        // given
        Map<String, String> mapResult;
        assertFalse(this.dataObjectHelper.exist(this.image1));
        assertFalse(this.dataObjectHelper.exist(this.image1Result));
        assertTrue(this.dataObjectHelper.exist());
        String minVal = "101";

        this.dataObjectHelper.create(this.image1, Files.readAllBytes(this.path1));

        // when
        mapResult = this.labelDetectorHelper.execute(this.image1, maxLabelsChangeNbLabelsCase, minConfidenceNormalCase);
        for (Map.Entry<String, String> val : mapResult.entrySet()) {
            if(Float.parseFloat(minVal) > Float.parseFloat(val.getValue())){
                minVal = val.getValue();
            }
        }

        // then
        assertTrue(this.dataObjectHelper.exist(this.image1Result));
        assertEquals(this.correctMapChangeNbLabelsCase, mapResult);
        assertEquals(this.maxLabelsChangeNbLabelsCase, mapResult.size());
        assertEquals(this.correctMinValChangeNbLabelsCase, minVal);
    }

    @Test
    public void Execute_RecoverLabelsWithLimitMinConfidence80_Success() throws IOException {
        // given
        Map<String, String> mapResult;
        assertFalse(this.dataObjectHelper.exist(this.image1));
        assertFalse(this.dataObjectHelper.exist(this.image1Result));
        assertTrue(this.dataObjectHelper.exist());
        String minVal = "101";

        this.dataObjectHelper.create(this.image1, Files.readAllBytes(this.path1));

        // when
        mapResult = this.labelDetectorHelper.execute(this.image1, maxLabelsNormalCase, minConfidenceChangeNbLabelsCase);
        for (Map.Entry<String, String> val : mapResult.entrySet()) {
            if(Float.parseFloat(minVal) > Float.parseFloat(val.getValue())){
                minVal = val.getValue();
            }
        }

        // then
        assertTrue(this.dataObjectHelper.exist(this.image1Result));
        assertEquals(this.correctMapChangeMinConfidenceCase, mapResult);
        assertEquals(this.correctMaxLabelsMinConfidenceCase, mapResult.size());
        assertEquals(this.correctMinValChangeMinConfidenceCase, minVal);
    }

    @Test
    public void Execute_RecoverLabelsFromResultFile_Success() throws IOException {
        // given
        Map<String, String> mapResult;
        assertFalse(this.dataObjectHelper.exist(this.image1));
        assertFalse(this.dataObjectHelper.exist(this.image1Result));
        assertTrue(this.dataObjectHelper.exist());
        String minVal = "101";

        this.dataObjectHelper.create(this.image1, Files.readAllBytes(this.path1));
        this.labelDetectorHelper.execute(this.image1, maxLabelsNormalCase, minConfidenceNormalCase);

        // when
        mapResult = this.labelDetectorHelper.execute(this.image1, maxLabelsNormalCase, minConfidenceNormalCase);
        for (Map.Entry<String, String> val : mapResult.entrySet()) {
            if(Float.parseFloat(minVal) > Float.parseFloat(val.getValue())){
                minVal = val.getValue();
            }
        }

        // then
        assertTrue(this.dataObjectHelper.exist(this.image1Result));
        assertEquals(this.correctMapNormalCase, mapResult);
        assertEquals(this.maxLabelsNormalCase, mapResult.size());
        assertEquals(this.correctMinValNormalCase, minVal);
    }

    @Test
    public void ExecuteBase64_RecoverLabelsFromBase64Image_Success() throws IOException {
        // given
        Map<String, String> mapResult;
        assertFalse(this.dataObjectHelper.exist(this.image1));
        assertFalse(this.dataObjectHelper.exist(this.image1Result));
        assertTrue(this.dataObjectHelper.exist());
        String minVal = "101";

        String imageBase64 = Base64.getEncoder().encodeToString(Files.readAllBytes(this.path1));

        // when
        mapResult = this.labelDetectorHelper.executeBase64(imageBase64, maxLabelsNormalCase, minConfidenceNormalCase);
        for (Map.Entry<String, String> val : mapResult.entrySet()) {
            if(Float.parseFloat(minVal) > Float.parseFloat(val.getValue())){
                minVal = val.getValue();
            }
        }

        // then
        assertFalse(this.dataObjectHelper.exist(this.image1Result));
        assertEquals(this.correctMapNormalCase, mapResult);
        assertEquals(this.maxLabelsNormalCase, mapResult.size());
        assertEquals(this.correctMinValNormalCase, minVal);
    }
}

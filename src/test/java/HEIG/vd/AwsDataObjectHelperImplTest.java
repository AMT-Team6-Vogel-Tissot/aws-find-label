package HEIG.vd;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AwsDataObjectHelperImplTest {

    private final AwsCloudClient bucketManager = AwsCloudClient.getInstance();

    private final String bucketName = bucketManager.getBucketUrl();
    private final AwsDataObjectHelperImpl dataObjectHelper = bucketManager.getDataObject();
    private final String pathToTestFolder = "./filesTest/";
    private final String image1 = "file1.jpg";
    private final String image2 = "file2.jpg";
    private final String fileText = "file.txt";
    private final String newImageName = "file3.jpg";

    private final Path path1 = Path.of(this.pathToTestFolder, this.image1);
    private final Path path2 = Path.of(this.pathToTestFolder, this.image2);


    @AfterEach
    public void cleanUpEach(){
        if(dataObjectHelper.exist(this.image1)){
            this.dataObjectHelper.delete(this.image1);
        }
        if(dataObjectHelper.exist(this.image2)){
            this.dataObjectHelper.delete(this.image2);
        }
        if(dataObjectHelper.exist(this.newImageName)){
            this.dataObjectHelper.delete(this.newImageName);
        }
        if(dataObjectHelper.exist(this.fileText)){
            this.dataObjectHelper.delete(this.fileText);
        }
    }


    @Disabled
    @Test
    public void ListBucket_Success() {

        // given
        String listBuckets = "amt.team01.diduno.education\n" +
                "amt.team02.diduno.education\n" +
                "amt.team03.diduno.education\n" +
                "amt.team04.diduno.education\n" +
                "amt.team05.diduno.education\n" +
                "amt.team06.diduno.education\n" +
                "amt.team07.diduno.education\n" +
                "amt.team08.diduno.education\n" +
                "amt.team09.diduno.education\n" +
                "amt.team10.diduno.education\n" +
                "amt.team11.diduno.education\n" +
                "amt.team98.diduno.education\n" +
                "amt.team99.diduno.education\n" +
                "raid0toraid5.diduno.education\n" +
                "raid1toraid6.diduno.education\n" +
                "raid5toraid0.diduno.education\n" +
                "raid6toraid1.diduno.education\n" +
                "sto1.team.00\n" +
                "test.bucket.sto1\n";
        String actualResult;

        // when
        actualResult = dataObjectHelper.list();

        // then
        assertEquals(listBuckets, actualResult);
    }


    //TODO REVIEW Rewrite test signature (method_scenario_expectedResult)
    @Test
    public void List_Success() throws IOException {
        // given
        String listObjects = "file1.jpg\n" + "file2.jpg\n";


        this.dataObjectHelper.create(this.image1, Files.readAllBytes(path1));
        this.dataObjectHelper.create(this.image2, Files.readAllBytes(path2));
        String actualResult;

        // when
        actualResult = dataObjectHelper.list();

        this.dataObjectHelper.delete(this.image1);
        this.dataObjectHelper.delete(this.image2);

        // then
        assertEquals(listObjects, actualResult);
    }

    @Test
    public void Exist_bucketExist() {
        // given
        boolean actualResult;

        // when
        actualResult = dataObjectHelper.exist();

        // then
        assertTrue(actualResult);
    }

    @Test
    public void Exist_ObjectIsPresent_Success() throws IOException {
        // given
        Path path = Path.of(this.pathToTestFolder, this.image1);
        this.dataObjectHelper.create(this.image1, Files.readAllBytes(path));
        boolean actualResult;

        // when
        actualResult = this.dataObjectHelper.exist(this.image1);

        // then
        this.dataObjectHelper.delete(this.image1);
        assertTrue(actualResult);
    }

    @Test
    public void Exist_ObjectIsNotPresent_Success() {
        // given
        assertTrue(this.dataObjectHelper.exist());
        boolean actualResult;

        // when
        actualResult = this.dataObjectHelper.exist(this.image1);

        // then
        assertFalse(actualResult);
    }

    @Test
    public void Create_CreateObjectWithExistingBucket_Success() throws IOException {
        // given
        assertTrue(this.dataObjectHelper.exist());
        assertFalse(this.dataObjectHelper.exist(this.image1));
        Path path = Path.of(this.pathToTestFolder, this.image1);

        // when
        this.dataObjectHelper.create(this.image1, Files.readAllBytes(path));

        // then
        assertTrue(this.dataObjectHelper.exist(this.image1));
        this.dataObjectHelper.delete(this.image1);
    }

    @Test
    public void Delete_RemoveNotExistingObject_Success() {
        // given
        assertTrue(this.dataObjectHelper.exist());

        // when
        this.dataObjectHelper.delete(this.image1);

        // then
        assertFalse(this.dataObjectHelper.exist(this.image1));
    }

    @Test
    public void Delete_NotEmptyBucket_Success() throws IOException {
        // given
        Path path = Path.of(this.pathToTestFolder, this.image1);
        this.dataObjectHelper.create(this.image1, Files.readAllBytes(path));

        assertTrue(this.dataObjectHelper.exist());
        assertTrue(this.dataObjectHelper.exist(this.image1));

        // when
        this.dataObjectHelper.delete(this.image1);

        // then
        assertFalse(this.dataObjectHelper.exist(this.image1));
    }

    @Test
    public void Update_UpdateExistingObjectContent() throws IOException {
        // given
        assertTrue(this.dataObjectHelper.exist());
        Path path = Path.of(this.pathToTestFolder, this.image1);
        Path path2 = Path.of(this.pathToTestFolder, this.fileText);
        String exceptedValue = "Test file";

        this.dataObjectHelper.create(this.image1, Files.readAllBytes(path));
        assertTrue(this.dataObjectHelper.exist(this.image1));

        // when
        this.dataObjectHelper.update(this.image1, Files.readAllBytes(path2));
        String value = new String(this.dataObjectHelper.get(this.image1));

        // then
        assertTrue(this.dataObjectHelper.exist(this.image1));
        assertEquals(exceptedValue, value);

        this.dataObjectHelper.delete(this.image1);
    }

    @Test
    public void Update_UpdateExistingObjectNameAndContent() throws IOException {
        // given
        assertTrue(this.dataObjectHelper.exist());
        Path path = Path.of(this.pathToTestFolder, this.image1);
        Path path2 = Path.of(this.pathToTestFolder, this.fileText);
        String exceptedValue = "Test file";

        this.dataObjectHelper.create(this.image1, Files.readAllBytes(path));
        assertTrue(this.dataObjectHelper.exist(this.image1));

        // when
        this.dataObjectHelper.update(this.image1, Files.readAllBytes(path2), this.newImageName);
        String value = new String(this.dataObjectHelper.get(this.newImageName));

        // then
        assertFalse(this.dataObjectHelper.exist(this.image1));
        assertTrue(this.dataObjectHelper.exist(this.newImageName));
        assertEquals(exceptedValue, value);

        this.dataObjectHelper.delete(this.newImageName);
    }

    @Test
    public void Get_GetExistingObject_Success() throws IOException {
        // given
        assertTrue(this.dataObjectHelper.exist());
        Path path = Path.of(this.pathToTestFolder, this.fileText);
        this.dataObjectHelper.create(this.fileText, Files.readAllBytes(path));
        assertTrue(this.dataObjectHelper.exist(this.fileText));
        String exceptedValue = "Test file";

        // when
        String value = new String(this.dataObjectHelper.get(this.fileText));

        // then
        assertEquals(exceptedValue, value);
        this.dataObjectHelper.delete(this.fileText);
    }

    @Test
    public void Get_GetNotExistingObject_Success() throws IOException {
        // given
        assertTrue(this.dataObjectHelper.exist());

        // when
        byte[] value = this.dataObjectHelper.get(this.fileText);

        // then
        assertNull(value);
    }

    // TODO add labelization tests

}

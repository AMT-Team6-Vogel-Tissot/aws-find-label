package HEIG.vd;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

//TODO NGY recheck the test coverage (iceScrum https://icescrum.cpnv.ch/p/AMTLABO/#/planning/2619/story/5856/tests)
public class AwsDataObjectHelperImplTest {

    private final AwsCloudClient bucketManager = AwsCloudClient.getInstance();

    private final String bucketName = bucketManager.getBucketUrl();
    private final AwsDataObjectHelperImpl dataObjectHelper = bucketManager.getDataObject();
    private final String pathToTestFolder = "./filesTest/";
    private final String image1 = "file1.jpg";
    private final String image2 = "file2.jpg";
    private final String fileText = "file.txt";
    private final String newImageName = "file3.jpg";
/*
    @Test
    public void ListBucket_Success(){

        //given
        String listBuckets =
                "amt.team01.diduno.education\n" +
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

        //when
        actualResult = dataObjectHelper.listBuckets();

        //then
        assertEquals(listBuckets, actualResult);
    }
*/
    @Test
    public void ListObjects_Success() throws IOException {
        //given
        String listObjects = "file1.jpg\n" + "file2.jpg\n";
        Path path1 = Path.of(this.pathToTestFolder, this.image1);
        Path path2 = Path.of(this.pathToTestFolder, this.image2);

        this.dataObjectHelper.createObject(this.image1, Files.readAllBytes(path1));
        this.dataObjectHelper.createObject(this.image2, Files.readAllBytes(path2));
        String actualResult;

        //when
        actualResult = dataObjectHelper.listObjects();

        this.dataObjectHelper.removeObject(this.image1);
        this.dataObjectHelper.removeObject(this.image2);

        //then
        assertEquals(listObjects, actualResult);
    }

    //TODO NGY Method signature is not bdd
    @Test
    public void ExistBucket_bucketExist(){
        //given
        boolean actualResult;

        //when
        actualResult = dataObjectHelper.existBucket(bucketName);

        //then
        assertTrue(actualResult);
    }

    //TODO NGY Method signature is not bdd
    @Test
    public void ExistBucket_bucketDoesntExist(){
        //given
        boolean actualResult;

        //when
        actualResult = dataObjectHelper.existBucket("aled");

        //then
        assertFalse(actualResult);
    }

    //TODO NGY This test is not required
    @Test
    public void ExistObject_ObjectIsPresent_Success() throws IOException {
        //given
        Path path = Path.of(this.pathToTestFolder, this.image1);
        this.dataObjectHelper.createObject(this.image1, Files.readAllBytes(path));
        boolean actualResult;

        //when
        actualResult = this.dataObjectHelper.existObject(this.image1);

        //then
        this.dataObjectHelper.removeObject(this.image1);
        assertTrue(actualResult);
    }

    @Test
    public void ExistObject_ObjectIsNotPresent_Success() {
        //given
        assertTrue(this.dataObjectHelper.existBucket(this.bucketName));
        boolean actualResult;

        //when
        actualResult = this.dataObjectHelper.existObject(this.image1);

        //then
        assertFalse(actualResult);
    }

    @Test
    public void CreateObject_CreateObjectWithExistingBucket_Success() throws IOException {
        //given
        assertTrue(this.dataObjectHelper.existBucket(this.bucketName));
        assertFalse(this.dataObjectHelper.existObject(this.image1));
        Path path = Path.of(this.pathToTestFolder, this.image1);

        //when
        this.dataObjectHelper.createObject(this.image1, Files.readAllBytes(path));

        //then
        assertTrue(this.dataObjectHelper.existObject(this.image1));

        //TODO NGY remove object must be moved in Tear down method
        this.dataObjectHelper.removeObject(this.image1);
    }

    @Test
    public void RemoveObject_RemoveNotExistingObject_Success() {
        //given
        assertTrue(this.dataObjectHelper.existBucket(this.bucketName));

        //when
        this.dataObjectHelper.removeObject(this.image1);

        //then
        assertFalse(this.dataObjectHelper.existObject(this.image1));
    }


    @Test
    public void RemoveObject_NotEmptyBucket_Success() throws IOException {
        //given
        Path path = Path.of(this.pathToTestFolder, this.image1);
        this.dataObjectHelper.createObject(this.image1, Files.readAllBytes(path));

        assertTrue(this.dataObjectHelper.existBucket(this.bucketName));
        assertTrue(this.dataObjectHelper.existObject(this.image1));

        //when
         this.dataObjectHelper.removeObject(this.image1);

        //then
        assertFalse(this.dataObjectHelper.existObject(this.image1));
    }

    @Test
    public void UpdateObject_UpdateExistingObjectName() throws IOException {
        //given
        assertTrue(this.dataObjectHelper.existBucket(this.bucketName));
        Path path = Path.of(this.pathToTestFolder, this.image1);
        this.dataObjectHelper.createObject(this.image1, Files.readAllBytes(path));

        assertTrue(this.dataObjectHelper.existObject(this.image1));

        //when
        this.dataObjectHelper.updateObject(this.image1, this.newImageName);

        //then
        assertFalse(this.dataObjectHelper.existObject(this.image1));
        assertTrue(this.dataObjectHelper.existObject(this.newImageName));

        //TODO NGY remove object must be moved in Tear down method
        this.dataObjectHelper.removeObject(this.newImageName);
    }

    @Test
    public void GetObject_GetExistingObject_Success() throws IOException {
        //given
        assertTrue(this.dataObjectHelper.existBucket(this.bucketName));
        Path path = Path.of(this.pathToTestFolder, this.fileText);
        this.dataObjectHelper.createObject(this.fileText, Files.readAllBytes(path));
        assertTrue(this.dataObjectHelper.existObject(this.fileText));
        String exceptedValue = "Test file";

        //when
        String value = new String(this.dataObjectHelper.getObject(this.fileText));

        //then
        assertEquals(exceptedValue, value);

        //TODO NGY remove object must be moved in Tear down method
        this.dataObjectHelper.removeObject(this.fileText);
    }

    @Test
    public void GetObject_GetNotExistingObject_Success() throws IOException {
        //given
        assertTrue(this.dataObjectHelper.existBucket(this.bucketName));

        //when
        byte[] value = this.dataObjectHelper.getObject(this.fileText);

        //then
        //TODO oooppsss. Why did you test "Null". If you get bytes, it should be not null ?
        assertNull(value);
    }

}

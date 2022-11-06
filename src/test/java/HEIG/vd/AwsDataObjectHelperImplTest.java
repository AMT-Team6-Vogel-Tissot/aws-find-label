package HEIG.vd;

import static org.junit.Assert.*;

import org.junit.Test;

import java.nio.file.Path;

public class AwsDataObjectHelperImplTest {

    private final AwsCloudClient bucketManager = new AwsCloudClient("amt.team06.diduno.education");

    private final String bucketName = bucketManager.getBucketUrl();
    private String pathToTestFolder = "./filesTest/";
    private final String image1 = "file1.jpg";
    private final String image2 = "file2.jpg";
    private final String newImageName = "file3.jpg";

    @Test
    public void ListBucket_Success(){

        //given
        String listBuckets =
                "amt.team01.diduno.education\n" +
                "amt.team02.diduno.education\n" +
                "amt.team03.diduno.education\n" +
                "amt.team04.diduno.education\n" +
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
        actualResult = bucketManager.dataObject.listBuckets();

        //then
        assertEquals(listBuckets, actualResult);
    }

    @Test
    public void ListObjects_Success(){
        //given
        String listObjects = "file1.jpg\n" + "file2.jpg\n";
        Path path1 = Path.of(this.pathToTestFolder, this.image1);
        Path path2 = Path.of(this.pathToTestFolder, this.image2);

        this.bucketManager.dataObject.createObject(this.bucketName, this.image1, path1);
        this.bucketManager.dataObject.createObject(this.bucketName, this.image2, path2);
        String actualResult;

        //when
        actualResult = bucketManager.dataObject.listObjects(bucketName);

        this.bucketManager.dataObject.removeObject(this.bucketName, this.image1);
        this.bucketManager.dataObject.removeObject(this.bucketName, this.image2);

        //then
        assertEquals(listObjects, actualResult);
    }

    @Test
    public void ExistBucket_bucketExist(){
        //given
        boolean actualResult;

        //when
        actualResult = bucketManager.dataObject.existBucket(bucketName);

        //then
        assertTrue(actualResult);
    }

    @Test
    public void ExistBucket_bucketDoesntExist(){
        //given
        boolean actualResult;

        //when
        actualResult = bucketManager.dataObject.existBucket("aled");

        //then
        assertFalse(actualResult);
    }

    @Test
    public void ExistObject_ObjectIsPresent_Success() {
        //given
        Path path = Path.of(this.pathToTestFolder, this.image1);
        this.bucketManager.dataObject.createObject(this.bucketName, this.image1, path);
        boolean actualResult;

        //when
        actualResult = this.bucketManager.dataObject.existObject(this.bucketName, this.image1);

        //then
        this.bucketManager.dataObject.removeObject(this.bucketName, this.image1);
        assertTrue(actualResult);
    }

    @Test
    public void ExistObject_ObjectIsNotPresent_Success() {
        //given
        assertTrue(this.bucketManager.dataObject.existBucket(this.bucketName));
        boolean actualResult;

        //when
        actualResult = this.bucketManager.dataObject.existObject(this.bucketName, this.image1);

        //then
        assertFalse(actualResult);
    }

    @Test
    public void CreateObject_CreateObjectWithExistingBucket_Success() {
        //given
        assertTrue(this.bucketManager.dataObject.existBucket(this.bucketName));
        assertFalse(this.bucketManager.dataObject.existObject(this.bucketName, this.image1));
        Path path = Path.of(this.pathToTestFolder, this.image1);

        //when
        this.bucketManager.dataObject.createObject(this.bucketName, this.image1, path);

        //then
        assertTrue(this.bucketManager.dataObject.existObject(this.bucketName, this.image1));
        this.bucketManager.dataObject.removeObject(this.bucketName, this.image1);
    }

    @Test
    public void RemoveObject_RemoveNotExistingObject_Success() {
        //given
        assertTrue(this.bucketManager.dataObject.existBucket(this.bucketName));

        //when
        this.bucketManager.dataObject.removeObject(this.bucketName, this.image1);

        //then
        assertFalse(this.bucketManager.dataObject.existObject(this.bucketName, this.image1));
    }


    @Test
    public void RemoveObject_NotEmptyBucket_Success() {
        //given
        Path path = Path.of(this.pathToTestFolder, this.image1);
        this.bucketManager.dataObject.createObject(this.bucketName, this.image1, path);

        assertTrue(this.bucketManager.dataObject.existBucket(this.bucketName));
        assertTrue(this.bucketManager.dataObject.existObject(this.bucketName, this.image1));

        //when
         this.bucketManager.dataObject.removeObject(this.bucketName, this.image1);

        //then
        assertFalse(this.bucketManager.dataObject.existObject(this.bucketName, this.image1));
    }

    @Test
    public void UpdateObject_UpdateExistingObjectName(){
        //given
        assertTrue(this.bucketManager.dataObject.existBucket(this.bucketName));
        Path path = Path.of(this.pathToTestFolder, this.image1);
        this.bucketManager.dataObject.createObject(this.bucketName, this.image1, path);

        assertTrue(this.bucketManager.dataObject.existObject(this.bucketName, this.image1));

        //when
        this.bucketManager.dataObject.updateObject(this.bucketName, this.image1, this.newImageName);

        //then
        assertFalse(this.bucketManager.dataObject.existObject(this.bucketName, this.image1));
        assertTrue(this.bucketManager.dataObject.existObject(this.bucketName, this.newImageName));

        this.bucketManager.dataObject.removeObject(this.bucketName, this.newImageName);
    }

}

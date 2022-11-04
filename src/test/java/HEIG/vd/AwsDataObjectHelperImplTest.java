package HEIG.vd;

import static org.junit.Assert.*;

import com.amazonaws.services.s3.model.Bucket;
import org.junit.Test;

import java.util.List;

public class AwsDataObjectHelperImplTest {

    private AwsCloudClient bucketManager = new AwsCloudClient("default");
    private String domain = "aws.dev.actualit.info";
    private String bucketName = "amt.team06.diduno.education";
    private String bucketUrl = "s3://" + bucketName ;
    private String pathToTestFolder = "/home/maelle/Pictures/";
    private String imageName = "2020-slim.png";
    private String newName = "2021-slim.png";

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
                "test.bucket.sto1";
        String actualResult;

        //when
        actualResult = bucketManager.dataObject.listBuckets();

        //then
        assertEquals(listBuckets, actualResult);
    }

    @Test
    public void ListObjects_Success(){
        //given
        String listObjects = "test\n" + "test2";
        String file = this.pathToTestFolder + "/" + this.imageName;

        this.bucketManager.dataObject.createObject(this.imageName, file);
        this.bucketManager.dataObject.createObject(this.imageName + "2", file);
        String actualResult;

        //when
        actualResult = bucketManager.dataObject.listObjects();

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
        String file = this.pathToTestFolder + "/" + this.imageName;
        this.bucketManager.dataObject.createObject(this.imageName, file);
        boolean actualResult;

        //when
        actualResult = this.bucketManager.dataObject.existObject(this.bucketUrl, this.imageName);

        //then
        assertTrue(actualResult);
    }

    @Test
    public void ExistObject_ObjectIsNotPresent_Success() {
        //given
        assertTrue(this.bucketManager.dataObject.existBucket(this.bucketUrl));
        boolean actualResult;

        //when
        actualResult = this.bucketManager.dataObject.existObject(this.bucketUrl, this.imageName);

        //then
        assertFalse(actualResult);
    }

    @Test
    public void CreateObject_CreateObjectWithExistingBucket_Success() {
        //given
        assertTrue(this.bucketManager.dataObject.existBucket(this.bucketUrl));
        assertFalse(this.bucketManager.dataObject.existObject(this.bucketUrl, this.imageName));
        String file = this.pathToTestFolder + "/" + this.imageName;

        //when
        this.bucketManager.dataObject.createObject(this.imageName, file);

        //then
        assertTrue(this.bucketManager.dataObject.existObject(this.bucketUrl, this.imageName));
    }

    @Test
    public void RemoveObject_RemoveNotExistingObject_Success() {
        //given
        assertTrue(this.bucketManager.dataObject.existBucket(this.bucketUrl));

        //when
        this.bucketManager.dataObject.removeObject(this.bucketUrl + "/" + this.imageName);

        //then
        assertFalse(this.bucketManager.dataObject.existObject(this.bucketUrl, this.imageName));
    }

    @Test
    public void RemoveObject_NotEmptyBucket_Success() {
        //given
        String file = this.pathToTestFolder + "/" + this.imageName;
        this.bucketManager.dataObject.createObject(this.imageName, file);

        assertTrue(this.bucketManager.dataObject.existBucket(this.bucketUrl));
        assertTrue(this.bucketManager.dataObject.existObject(this.bucketUrl, this.imageName));

        //when
         this.bucketManager.dataObject.removeObject(this.bucketUrl, this.imageName);

        //then
        assertFalse(this.bucketManager.dataObject.existObject(this.bucketUrl, this.imageName));
    }

    @Test
    public void UpdateObject_UpdateExistingObjectName(){
        //given
        assertTrue(this.bucketManager.dataObject.existBucket(this.bucketUrl));
        String file = this.pathToTestFolder + "/" + this.imageName;
        this.bucketManager.dataObject.createObject(this.imageName, file);

        assertTrue(this.bucketManager.dataObject.existObject(this.bucketUrl, this.imageName));

        //when
        this.bucketManager.dataObject.updateObject(this.bucketUrl, this.imageName, this.newName);

        //then
        assertTrue(this.bucketManager.dataObject.existObject(this.bucketUrl, this.newName));
        assertFalse(this.bucketManager.dataObject.existObject(this.bucketUrl, this.imageName));
    }

    @Test
    public void UpdateObject_UpdateNoneExistingObjectName(){
        //given
        assertTrue(this.bucketManager.dataObject.existBucket(this.bucketUrl));

        //when
        this.bucketManager.dataObject.updateObject(this.bucketUrl, this.imageName, this.newName);

        //then
        assertFalse(this.bucketManager.dataObject.existObject(this.bucketUrl, this.newName));
        assertTrue(this.bucketManager.dataObject.existObject(this.bucketUrl, this.imageName));
    }

}

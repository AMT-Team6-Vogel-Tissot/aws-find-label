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

    private String imageName = "/home/maelle/Pictures/2020-slim.png";
    private String pathToTestFolder;
    private String fullPathToImage;
    private String prefixObjectDownloaded;

    private String newBucket = "test-s1";


@Test
    public void ListBucket(){

        bucketManager.dataObject.ListBuckets();
    }

    @Test
    public void bucketExist(){
        assertTrue(bucketManager.dataObject.existBucket(bucketName));
    }

    @Test
    public void bucketDoesntExist(){
        assertEquals(false, bucketManager.dataObject.existBucket("aled"));
    }

    @Test
    public void CreateObject_CreateNewBucket_Success() {
        //given
        assertFalse(bucketManager.dataObject.existBucket(newBucket));

        //when
        this.bucketManager.dataObject.create(newBucket);

        //then
        assertTrue(this.bucketManager.dataObject.existBucket(newBucket));
    }

/*
    @Test
    public void CreateObject_CreateObjectWithExistingBucket_Success() {
        //given
        String fileName = this.imageName;
        String objectUrl = this.bucketUrl + "/" + this.imageName;
        this.bucketManager.dataObject.create(this.bucketUrl);
        assertTrue(this.bucketManager.dataObject.existBucket(this.bucketUrl));
        assertFalse(this.bucketManager.dataObject.existObject(objectUrl));

        //when
        this.bucketManager.createObject(objectUrl, this.pathToTestFolder + "//" + fileName);

        //then
        assertTrue(this.bucketManager.exists(objectUrl));
    }

    @Test
    public void CreateObject_CreateObjectBucketNotExist_Success() {
        //given
        String fileName = this.imageName;
        String objectUrl = this.bucketUrl + "/" + this.imageName;
        assertFalse(this.bucketManager.exists(this.bucketUrl));
        assertFalse(this.bucketManager.exists(objectUrl));

        //when
        this.bucketManager.createObject(objectUrl, this.pathToTestFolder + "//" + fileName);

        //then
        assertTrue(this.bucketManager.exists(objectUrl));
    }


    @Test
    public void Exists_NominalCase_Success() {
        //given
        this.bucketManager.createObject(this.bucketUrl);

        boolean actualResult;

        //when
        actualResult = this.bucketManager.exists(bucketUrl);

        //then
        assertTrue(actualResult);
    }

    @Test
    public void Exists_ObjectNotExistBucket_Success() {
        //given
        String notExistingBucket = "notExistingBucket" + this.domain;
        boolean actualResult;

        //when
        actualResult = this.bucketManager.exists(notExistingBucket);

        //then
        assertFalse(actualResult);
    }

    @Test
    public void Exists_ObjectNotExistFile_Success() {
        //given
        this.bucketManager.createObject(this.bucketUrl);
        String notExistingFile = bucketUrl + "//" + "notExistingFile.jpg";
        assertTrue(this.bucketManager.exists(bucketUrl));
        boolean actualResult;

        //when
        actualResult = this.bucketManager.exists(notExistingFile);

        //then
        assertFalse(actualResult);
    }


    @Test
    public void RemoveObject_EmptyBucket_Success() {
        //given
        this.bucketManager.createObject(this.bucketUrl);
        assertTrue(this.bucketManager.exists(bucketUrl));

        //when
        this.bucketManager.removeObject(this.bucketUrl);

        //then
        assertFalse(this.bucketManager.exists(bucketUrl));
    }

    @Test
    public void RemoveObject_NotEmptyBucket_Success() {
        //given
        String fileName = this.imageName;
        String objectUrl = this.bucketUrl + "/" + this.imageName;
         this.bucketManager.createObject(this.bucketUrl);
         this.bucketManager.createObject(objectUrl, this.pathToTestFolder + "//" + fileName);

        assertTrue(this.bucketManager.exists(bucketUrl));
        assertTrue(this.bucketManager.exists(objectUrl));

        //when
         this.bucketManager.removeObject(this.bucketUrl);

        //then
        assertFalse(this.bucketManager.exists(bucketUrl));
    }*/


}

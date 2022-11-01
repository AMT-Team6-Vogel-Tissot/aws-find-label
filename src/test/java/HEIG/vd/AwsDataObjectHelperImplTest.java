package HEIG.vd;

import static org.junit.Assert.*;

import org.junit.Test;

public class AwsDataObjectHelperImplTest {

    private AwsDataObjectHelperImpl bucketManager = new AwsDataObjectHelperImpl(this.bucketUrl);
    private String domain = "aws.dev.actualit.info";
    private String bucketName = "amt.team06.diduno.education";
    private String bucketUrl = "s3://" + bucketName;

    private String imageName;
    private String pathToTestFolder;
    private String fullPathToImage;
    private String prefixObjectDownloaded;

    private String objToCopy = "/home/maelle/Pictures/2020-slim.png";


    @Test
    public void CreateObject_CreateNewBucket_Success() {
        bucketManager.getInfo();
        //given
        assertFalse(this.bucketManager.exists(bucketUrl));

        //when
        this.bucketManager.createObject(objToCopy);

        //then
        assertTrue(this.bucketManager.exists(bucketUrl + "/" + objToCopy));
    }

    @Test
    public void CreateObject_CreateObjectWithExistingBucket_Success() {
        //given
        String fileName = this.imageName;
        String objectUrl = this.bucketUrl + "/" + this.imageName;
        this.bucketManager.createObject(this.bucketUrl);
        assertTrue(this.bucketManager.exists(this.bucketUrl));
        assertFalse(this.bucketManager.exists(objectUrl));

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

    /*
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

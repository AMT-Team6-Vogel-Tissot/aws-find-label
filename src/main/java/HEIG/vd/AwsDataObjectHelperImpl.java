package HEIG.vd;

import HEIG.vd.interfaces.IDataObjectHelper;
import HEIG.vd.utils.GetEnvVal;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.URL;
import java.time.Duration;
import java.util.List;


public class AwsDataObjectHelperImpl implements IDataObjectHelper {
    private final S3Presigner presigner;
    private final S3Client cloudClient;

    private final String nameBucket;

    public AwsDataObjectHelperImpl(StaticCredentialsProvider credentialsProvider, Region region){

        cloudClient = S3Client
                .builder()
                .credentialsProvider(credentialsProvider)
                .region(region)
                .build();

        presigner = S3Presigner
                .builder()
                .credentialsProvider(credentialsProvider)
                .region(region)
                .build();

        nameBucket = GetEnvVal.getEnvVal("BUCKET");
    }

    public boolean existBucket(String name){
        HeadBucketRequest hbReq = HeadBucketRequest
                .builder()
                .bucket(name)
                .build();

        try{
            cloudClient.headBucket(hbReq);
            return true;
        }catch (S3Exception e){
            return false;
        }

    }


    public boolean existObject(String nameObject){
        GetObjectRequest goReq = GetObjectRequest
                .builder()
                .bucket(nameBucket)
                .key(nameObject)
                .build();

        try {
            cloudClient.getObject(goReq);
            return true;
        } catch (S3Exception e){
            return false;
        }
    }

    public String listBuckets() {
        StringBuilder str = new StringBuilder();

        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = cloudClient.listBuckets(listBucketsRequest);

        for (Bucket b : listBucketsResponse.buckets()) {
            str.append(b.name()).append("\n");
        }

        return str.toString();
    }

    public String listObjects(){

        StringBuilder str = new StringBuilder();

        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(nameBucket)
                    .build();

            ListObjectsResponse res = cloudClient.listObjects(listObjects);

            List<S3Object> objects = res.contents();
            for (S3Object myValue : objects) {
                str.append(myValue.key()).append("\n");
            }
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return str.toString();
    }

    @Override
    public URL createObject(String objectName, byte[] contentFile) {
        if(existBucket(nameBucket) && !existObject(objectName)){

            try{

                PutObjectRequest poReq = PutObjectRequest
                        .builder()
                        .bucket(nameBucket)
                        .key(objectName)
                        .build();

                cloudClient.putObject(poReq, RequestBody.fromBytes(contentFile));

                GetObjectRequest goReq = GetObjectRequest
                        .builder()
                        .bucket(nameBucket)
                        .key(objectName)
                        .build();

                GetObjectPresignRequest goPreReq = GetObjectPresignRequest
                        .builder()
                        .signatureDuration(Duration.ofMinutes(10))
                        .getObjectRequest(goReq)
                        .build();

                PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(goPreReq);

                return presignedRequest.url();

            } catch (S3Exception e){
                System.err.println(e.awsErrorDetails().errorMessage());
                System.exit(1);
            }

        }

        return null;
    }

    public void removeObject(String objectName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(nameBucket)
                .key(objectName)
                .build();

        cloudClient.deleteObject(deleteObjectRequest);
    }

    public URL updateObject(String image1, String newImageName) {

        CopyObjectRequest coReq = CopyObjectRequest
                .builder()
                .sourceBucket(nameBucket)
                .sourceKey(image1)
                .destinationBucket(nameBucket)
                .destinationKey(newImageName)
                .build();

        try{
            cloudClient.copyObject(coReq);

            removeObject(image1);

            GetObjectRequest goReq = GetObjectRequest
                    .builder()
                    .bucket(nameBucket)
                    .key(newImageName)
                    .build();

            GetObjectPresignRequest goPreReq = GetObjectPresignRequest
                    .builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .getObjectRequest(goReq)
                    .build();

            PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(goPreReq);

            return presignedRequest.url();
        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }

        return null;
    }

    public byte[] getObject(String objectName){
        byte[] data = null;

        if(existBucket(nameBucket) && existObject(objectName)) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(nameBucket)
                    .key(objectName)
                    .build();

            ResponseBytes<GetObjectResponse> objectBytes = cloudClient.getObjectAsBytes(getObjectRequest);

            data = objectBytes.asByteArray();
        }

        return data;
    }
}

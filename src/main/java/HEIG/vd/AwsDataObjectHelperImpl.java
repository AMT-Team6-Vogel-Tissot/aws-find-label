package HEIG.vd;

import HEIG.vd.interfaces.IDataObjectHelper;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.net.URL;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;


public class AwsDataObjectHelperImpl implements IDataObjectHelper {

    private S3Client profile;

    public AwsDataObjectHelperImpl(S3Client profile){
        this.profile = profile;
    }

    public boolean existBucket(String name){
        HeadBucketRequest hbReq = HeadBucketRequest
                .builder()
                .bucket(name)
                .build();

        try{
            profile.headBucket(hbReq);
            return true;
        }catch (S3Exception e){
            return false;
        }

    }


    public boolean existObject(String nameBucket, String nameObject){
        GetObjectRequest goReq = GetObjectRequest
                .builder()
                .bucket(nameBucket)
                .key(nameObject)
                .build();

        try {
            profile.getObject(goReq);
            return true;
        } catch (S3Exception e){
            return false;
        }
    }

    public String listBuckets() {
        StringBuilder str = new StringBuilder();

        ListBucketsRequest listBucketsRequest = ListBucketsRequest.builder().build();
        ListBucketsResponse listBucketsResponse = profile.listBuckets(listBucketsRequest);

        for (Bucket b : listBucketsResponse.buckets()) {
            str.append(b.name()).append("\n");
        }

        return str.toString();
    }

    public String listObjects(String bucketName){

        StringBuilder str = new StringBuilder();

        try {
            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .build();

            ListObjectsResponse res = profile.listObjects(listObjects);

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
    public URL createObject(String bucketName, String objectName, Path path) {
        if(existBucket(bucketName) && !existObject(bucketName, objectName)){

            try{
                S3Presigner presigner = S3Presigner.create();

                PutObjectRequest poReq = PutObjectRequest
                        .builder()
                        .bucket(bucketName)
                        .key(objectName)
                        .build();

                profile.putObject(poReq, path);

                GetObjectRequest goReq = GetObjectRequest
                        .builder()
                        .bucket(bucketName)
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

    public void removeObject(String bucketName, String objectName) {
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(objectName)
                .build();

        profile.deleteObject(deleteObjectRequest);
    }

    public URL updateObject(String bucketName, String image1, String newImageName) {

        S3Presigner presigner = S3Presigner.create();

        CopyObjectRequest coReq = CopyObjectRequest
                .builder()
                .sourceBucket(bucketName)
                .sourceKey(image1)
                .destinationBucket(bucketName)
                .destinationKey(newImageName)
                .build();

        try{
            profile.copyObject(coReq);

            removeObject(bucketName, image1);

            GetObjectRequest goReq = GetObjectRequest
                    .builder()
                    .bucket(bucketName)
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
}

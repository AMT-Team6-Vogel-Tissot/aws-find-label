package HEIG.vd;

import HEIG.vd.interfaces.ILabelDetector;
import software.amazon.awssdk.services.s3.S3Client;

public class AwsLabelDetectorHelperImpl implements ILabelDetector {

    S3Client profile;

    public AwsLabelDetectorHelperImpl(S3Client profile){
        this.profile = profile;
    }

    @Override
    public String execute(String imageUri, int[] params) {
        return null;
    }
}

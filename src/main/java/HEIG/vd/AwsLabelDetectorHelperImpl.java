package HEIG.vd;

import HEIG.vd.interfaces.ILabelDetector;
import com.amazonaws.services.s3.AmazonS3;

public class AwsLabelDetectorHelperImpl implements ILabelDetector {

    AmazonS3 profile;

    public AwsLabelDetectorHelperImpl(AmazonS3 profile){
        this.profile = profile;
    }

    @Override
    public String execute(String imageUri, int[] params) {
        return null;
    }
}

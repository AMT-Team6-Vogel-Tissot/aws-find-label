package HEIG.vd;

import HEIG.vd.interfaces.ICloudClient;

public class AwsCloudClient implements ICloudClient {
    @Override
    public AwsCloudClient getInstance() {
        return this;
    }
}

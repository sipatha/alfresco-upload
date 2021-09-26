package za.sipatha.upload.ex;

public class AlfrescoUploadException extends RuntimeException {

    public AlfrescoUploadException(String message) {
        super(message);
    }

    public AlfrescoUploadException(Throwable throwable) {
        super(throwable);
    }

}

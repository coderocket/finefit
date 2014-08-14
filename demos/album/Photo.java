
public class Photo {

    public enum Status {Old, New};

    Status status;
    String image; // represents the bitmap image 

    public Photo(String image) { status = Status.New; this.image = image; }

    public Status getStatus() { return status; }
    public void setStatus(Status s) {  status = s; }
    public String getImage() { return image; }

    public String toString() { return image; }
}
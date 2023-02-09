package fapp;

public class Files implements Details{
    private String name;
    private long size;
    private String extension;

    public Files(String name, long size, String extension) {
        this.name = name;
        this.size = size;
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String showDetails() {
        return "      -" + getName() + " (" + getSize() + " KB)\n";
    }
}

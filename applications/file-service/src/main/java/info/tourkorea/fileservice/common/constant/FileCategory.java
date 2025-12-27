package info.tourkorea.fileservice.common.constant;

public enum FileCategory {
    PROFILE("profile"),
    ARTICLE("article")
    ;

    private final String category;

    FileCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}

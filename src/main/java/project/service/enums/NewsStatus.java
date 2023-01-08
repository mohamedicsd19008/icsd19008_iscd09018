package project.service.enums;

public enum NewsStatus {
    CREATED(4),
    SUBMITTED(3),
    APPROVED(2),
    PUBLISHED(1);

    private int statusWeight;

    NewsStatus(int statusWeight) {
        this.statusWeight = statusWeight;
    }

    public int getStatusWeight() {
        return statusWeight;
    }
}
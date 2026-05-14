package model;

public class ResourceInteraction {

    private int id;

    private int studentId;

    private int resourceId;

    private String interactionType;

    public int getId() {

        return id;
    }

    public void setId(int id) {

        this.id = id;
    }

    public int getStudentId() {

        return studentId;
    }

    public void setStudentId(int studentId) {

        this.studentId = studentId;
    }

    public int getResourceId() {

        return resourceId;
    }

    public void setResourceId(int resourceId) {

        this.resourceId = studentId;
    }

    public String getInteractionType() {

        return interactionType;
    }

    public void setInteractionType(
            String interactionType
    ) {

        this.interactionType =
                interactionType;
    }
}
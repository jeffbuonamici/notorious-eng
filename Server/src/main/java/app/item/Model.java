package app.item;

import weka.classifiers.Classifier;

public class Model {
    private String modelName;
    private String modelID;
    private String description;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelID() {
        return modelID;
    }

    public void setModelID(String modelID) {
        this.modelID = modelID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Model{" +
                "modelID=" + modelID +
                ", model name=" + modelName +
                ", description=" + description +
                '}';
    }
}
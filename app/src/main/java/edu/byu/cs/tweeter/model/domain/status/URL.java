package edu.byu.cs.tweeter.model.domain.status;

import java.io.Serializable;

public class URL implements Serializable {
    private final String urlText;
    private final Integer positionInString;
    private final Integer length;

    public URL(String urlText, Integer positionInString, Integer length) {
        this.urlText = urlText;
        this.positionInString = positionInString;
        this.length = length;
    }

    public Integer getPositionInString() {
        return positionInString;
    }

    public String getUrlText() {
        return urlText;
    }

    public Integer getLength() {
        return length;
    }
}

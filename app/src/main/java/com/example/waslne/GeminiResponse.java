package com.example.waslne;

import com.google.gson.annotations.SerializedName;

public class GeminiResponse {

    //JSON
    @SerializedName("candidates")
    private Candidate[] candidates;

    public Candidate[] getCandidates() {
        return candidates;
    }

    public static class Candidate {
        @SerializedName("content")
        private Content content;

        public Content getContent() {
            return content;
        }
    }

    public static class Content {
        @SerializedName("parts")
        // parts (array) of 'Part' objects within  'Content'.
        private Part[] parts;

        public Part[] getParts() {
            return parts;
        }
    }

    public static class Part {
        @SerializedName("text")
        private String text;

        public String getText() {
            return text;
        }
    }
}
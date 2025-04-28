package com.example.waslne;

import com.google.gson.annotations.SerializedName;

public class GeminiRequest {
    @SerializedName("contents")  //  Use @SerializedName
    private Content[] contents;

    public GeminiRequest(String message) {
        this.contents = new Content[]{
                new Content(message)
        };
    }

    public Content[] getContents() {
        return contents;
    }

    public static class Content {
        @SerializedName("parts")
        private Part[] parts;

        public Content(String text) {
            this.parts = new Part[]{
                    new Part(text)
            };
        }

        public Part[] getParts() {
            return parts;
        }
    }

    public static class Part {
        @SerializedName("text")
        private String text;

        public Part(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}

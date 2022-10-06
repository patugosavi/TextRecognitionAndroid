package com.example.textrecognition;

import java.util.List;

public interface TextExtractCallback {
    void onGetExtractText(List<String> textList);
}

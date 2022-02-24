package com.common.adapter.utils;

import java.util.ArrayList;
import java.util.List;

public class MockUtils {

    public List<String> getMockStringList(int size) {
        List<String> stringList = new ArrayList<>();
        String content = "mock data";
        for (int i = 0; i < size; i++) {
            stringList.add(i + "--->" + content);
        }
        return stringList;
    }
}

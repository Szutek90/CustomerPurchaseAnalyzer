package com.app.file_operator;

import java.util.Map;

public interface FileOperator<T, U, R> {
    Map<T, Map<U, R>> readFile(String filename);
}

package com.redCross.model;

import lombok.Data;

import java.io.ByteArrayOutputStream;

@Data
public class ExcelPictureModel {
    private int column;
    private int row;
    private ByteArrayOutputStream photo;
}


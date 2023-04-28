package org.example.objects;

import org.example.enums.GeneratorType;

public class GeneratorRequest {
    private final int rows;
    private final int columns;
    private final GeneratorType generatorType;

    public GeneratorRequest(int rows, int columns, GeneratorType generatorType) {
        this.rows = rows;
        this.columns = columns;
        this.generatorType = generatorType;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public GeneratorType getGeneratorType() {
        return generatorType;
    }
}

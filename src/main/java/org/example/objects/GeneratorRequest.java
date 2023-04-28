package org.example.objects;

import org.example.enums.GeneratorType;
import org.example.enums.RandomType;

public class GeneratorRequest {
    private final int rows;
    private final int columns;
    private final GeneratorType generatorType;
    private final RandomType randomType;

    public GeneratorRequest(int rows, int columns, GeneratorType generatorType, RandomType randomType) {
        this.rows = rows;
        this.columns = columns;
        this.generatorType = generatorType;
        this.randomType = randomType;
    }

    public GeneratorRequest(int rows, int columns, GeneratorType generatorType) {
        this.rows = rows;
        this.columns = columns;
        this.generatorType = generatorType;
        this.randomType = RandomType.RANDOM;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public RandomType getRandomType() {
        return randomType;
    }

    public GeneratorType getGeneratorType() {
        return generatorType;
    }
}

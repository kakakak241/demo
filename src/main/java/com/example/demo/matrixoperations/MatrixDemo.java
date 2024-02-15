package com.example.demo.matrixoperations;

public class MatrixDemo {
    public static void main(String[] args) {
        int rows = 3;
        int cols = 3;
        int[][] matrixA = MatrixOperations.createMatrix(rows, cols);
        int[][] matrixB = MatrixOperations.createMatrix(rows, cols);

        MatrixOperations.fillMatrixWithRandomNumbers(matrixA);
        MatrixOperations.fillMatrixWithRandomNumbers(matrixB);

        System.out.println("Matrix A:");
        MatrixOperations.printMatrix(matrixA);

        System.out.println();

        System.out.println("Matrix B:");
        MatrixOperations.printMatrix(matrixB);

        System.out.println();

        int[][] sumResult = MatrixOperations.sumMatrices(matrixA, matrixB);
        System.out.println("Sum of A and B:");
        MatrixOperations.printMatrix(sumResult);

        System.out.println();

        int[][] subtractResult = MatrixOperations.subtractMatrices(matrixA, matrixB);
        System.out.println("Subtraction of A and B:");
        MatrixOperations.printMatrix(subtractResult);

        System.out.println();

        int[][] multiplyResult = MatrixOperations.multiplyMatrices(matrixA, matrixB);
        System.out.println("Multiplication of A and B:");
        MatrixOperations.printMatrix(multiplyResult);
    }
}

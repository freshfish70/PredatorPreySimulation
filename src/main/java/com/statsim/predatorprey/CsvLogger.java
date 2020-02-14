package com.statsim.predatorprey;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CsvLogger {

    private FileWriter outputFile;

    private CSVWriter csvWriter;

    public CsvLogger(String filePath, String[] headers) throws IOException {
        this.createFile(filePath);
        this.createCsvWriter(this.outputFile);
        this.setHeaders(headers);
    }

    /**
     * Sets the headers to the CSV file
     * @param csvHeaders headers for file
     */
    public void setHeaders(String[] csvHeaders) {
        this.log(csvHeaders);
    }

    /**
     * Creates the desired file on path
     *
     * @param filePath file to create with path
     */
    private void createFile(String filePath) throws IOException {
        this.outputFile = new FileWriter(new File(filePath));
    }

    /**
     * Creates a new CSVWriter with the output file we want to write to
     *
     * @param file file to write CSV data on
     */
    private void createCsvWriter(FileWriter file) {
        this.csvWriter = new CSVWriter(this.outputFile);
    }

    /**
     * Writes data to opened file
     * @param data data to write
     */
    public void log(String[] data) {
        this.csvWriter.writeNext(data);
    }

    public void close() throws IOException {
        this.csvWriter.close();
    }
}

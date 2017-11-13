package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;
    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     *
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column   Column that should be searched.
     * @param value Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {

        // load data, if not already loaded
        loadData();

        // creates an ArrayList called "jobs"
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        // for loop iterates over every "row" HashMap in allJobs
        for (HashMap<String, String> row : allJobs) {

            // creates string "aValue" filled with the value of the "column" Key
            String aValue = row.get(column);

            // if value of "column" Key is the value entered into the search field, add this job to the jobs ArrayList
            if (aValue.contains(value)) {
                jobs.add(row);
            }
        }

        return jobs;
    }

    public static ArrayList<HashMap<String, String>> findbyValue(String value) {

        // load data, if not already loaded
        loadData();

        // creates ArrayList called "jobs"
        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();

        // iterates over every Hashmap in allJobs
        for (HashMap<String, String> job : allJobs) {


            // iterates over each Key in an individual hashmap in allJobs ArrayList
            for (Map.Entry<String, String> jobKey : job.entrySet()) {

                String k = jobKey.getKey();
                String v = jobKey.getValue();

                value = value.toLowerCase();
                k = k.toLowerCase();
                v = v.toLowerCase();

                // if any of the keys in this job hashmap have a value equal to the value searched, add the job to the "jobs" ArrayList
                if (v.contains(value) || k.contains(value)) {
                    jobs.add(job);
                }

            }

        }
        return jobs;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);

            allJobs = new ArrayList<>();

            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();

                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }

                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }
    }

}

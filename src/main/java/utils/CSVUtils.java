package utils;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.testng.Assert;
import java.io.FileReader;

public class CSVUtils extends LoggerUtils {

    public static int getNumberOfRows(String sPathToFile) {

        CSVReader csvReader = null;
        int count = 0;
        try {
            csvReader = new CSVReaderBuilder(new FileReader(sPathToFile)).build();
            while (csvReader.readNext() != null) {
                count++;
            }
        } catch (Exception e) {
            Assert.fail("Reading content of CSV file '" + sPathToFile + "' failed! Message: " + e.getMessage());
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (Exception e) {
                    Assert.fail("Closing CSV file '" + sPathToFile + "' failed! Message: " + e.getMessage());
                }
            }
        }
        return count;
    }

    public static String[] getRow(String sPathToFile, int rowNumber) {
        CSVReader csvReader = null;
        String[] row = null;
        int numberOfRows = getNumberOfRows(sPathToFile);
        Assert.assertTrue(rowNumber < numberOfRows, "Cannot read row " + rowNumber + ". CSV file '" + sPathToFile + "' has " + numberOfRows + "row(s)");
        try {
            csvReader = new CSVReaderBuilder(new FileReader(sPathToFile)).withSkipLines(rowNumber).build();
            row = csvReader.readNext();
        } catch (Exception e) {
            Assert.fail("Reading content of CSV file '" + sPathToFile + "' failed! Message: " + e.getMessage());
        } finally {
            if (csvReader != null) {
                try {
                    csvReader.close();
                } catch (Exception e) {
                    Assert.fail("Closing CSV file '" + sPathToFile + "' failed! Message: " + e.getMessage());
                }
            }
        }
        return row;
    }

    public static int getPositionOfSpecifiedColumn(String sPathToFile, String sColumnName) {
        String[] row = getRow(sPathToFile, 0);
        int position = 0;
        boolean bFound = false;
        while (position < row.length) {
            if (row[position].equals(sColumnName)) {
                bFound = true;
                break;
            }
            position++;
        }
        Assert.assertTrue(bFound, "There is no column with name '" + sColumnName + "' in CSV file '" + sPathToFile + "'!");
        return position;
    }

    public static String getValueFromSpecifiedCellInSpecifiedRow(String sPathToFile, int rowNumber, int cellPosition) {
        String[] row = getRow(sPathToFile, rowNumber);
        int numberOfColumns = row.length;
        Assert.assertTrue(cellPosition < numberOfColumns, "Cannot get cell value at position " + cellPosition + ", since there is only " + numberOfColumns + " columns");
        return row[cellPosition];
    }

    public static int getRowPositionBySpecifiedValueByColumnPosition (String sPathToFile, String sFieldValue, int columnPosition) {
        int numberOfRows = getNumberOfRows(sPathToFile);
        int position = 0;
        boolean bFound = false;
        while(position < numberOfRows) {
            String sCurrentFieldValue = getValueFromSpecifiedCellInSpecifiedRow(sPathToFile, position, columnPosition);
            if (sCurrentFieldValue.equals(sFieldValue)) {
                bFound = true;
                break;
            }
            position++;
        }
        Assert.assertTrue(bFound, "There is no row with cell value '" + sFieldValue + "' in column " + columnPosition + " in CSV file '" + sPathToFile + "'!");
        return position;
    }

    public static String getCellValueBySpecifiedRowAndSpecifiedColumn(String sPathToFile, String sRowName, String sColumnName) {
        int columnNumber = getPositionOfSpecifiedColumn(sPathToFile, sColumnName);
        int rowNumber = getRowPositionBySpecifiedValueByColumnPosition(sPathToFile, sRowName, 0);
        return getValueFromSpecifiedCellInSpecifiedRow(sPathToFile, rowNumber, columnNumber);
    }

}

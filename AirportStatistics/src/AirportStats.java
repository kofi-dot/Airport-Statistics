import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class AirportStats {

        public static void main(String[] args) throws FileNotFoundException {
            Scanner console = new Scanner(System.in);
            String[] airportData;

            if ((airportData = validCode(console)) != null) {
                String fileName = validFile(console);
                File dataFile = new File(fileName);

                processAirportData(dataFile, airportData[0], airportData[1]);
            } else {
                System.out.println("Airport not found.");
            }
        }

        /**
         *
         * @param console User inputs airport code or name
         * @return A string array representing the code and name of the airport
         * @throws FileNotFoundException
         */
        public static String[] validCode(Scanner console) throws FileNotFoundException {
            System.out.print("Enter airport name or code: ");
            String input = console.nextLine().toLowerCase().trim();
            Scanner fileScnr = new Scanner(new File("airports-code.csv"));

            while (fileScnr.hasNextLine()) {
                String line = fileScnr.nextLine().trim();
                String[] codeNums = line.split(",");

                if (codeNums.length == 2) {
                    String code = codeNums[0].toLowerCase().trim();
                    String name = codeNums[1].toLowerCase().trim();

                    if (code.contains(input) || name.contains(input)) {
                        return codeNums;
                    }
                }
            }
            return null;
        }

        /**
         *
         * @param console User inputs name of file
         * @return file name
         * @throws FileNotFoundException
         */
        public static String validFile(Scanner console) throws FileNotFoundException {
            System.out.print("Enter data file name: ");
            String fileName = console.nextLine().trim();
            File file = new File(fileName);

            while (!file.exists()) {
                System.out.print("File does not exist, try again: ");
                fileName = console.nextLine().trim();
                file = new File(fileName);
            }

            System.out.println(fileName + " successfully found.");
            return fileName;
        }

        /**
         * No parameters, just a printing method
         */
        public static void printHeader() {
            System.out.println("\n============================================================");
            System.out.println("Year     Cancelled       Delayed      Diverted       On Time");
            System.out.println("============================================================");
        }

        /**
         *
         * @param file File entered by user
         * @param airportCode Three-letter code corresponding to name of airport
         * @param airportName Actual name of airport
         * @throws FileNotFoundException
         *
         *
         */
        public static void processAirportData(File file, String airportCode, String airportName)
                throws FileNotFoundException {
            Scanner fileScnr = new Scanner(file); int[] yearlyData = new int[4];
            String currentYear = "";
            int totalCancelled = 0, totalDelayed = 0, totalDiverted = 0, totalOnTime = 0;

            System.out.println("\n" + airportCode.toUpperCase() + " | " + airportName);
            System.out.println("Flight Statistics"); printHeader();

            while (fileScnr.hasNextLine()) {
                String line = fileScnr.nextLine().trim(); String[] data = line.split(",");

                if (data.length != 6 || !data[0].equalsIgnoreCase(airportCode)) continue;

                String year = data[1].split("/")[0];
                int cancelled = Integer.parseInt(data[2]), delayed = Integer.parseInt(data[3]);
                int diverted = Integer.parseInt(data[4]), onTime = Integer.parseInt(data[5]);

                if (!year.equals(currentYear) && !currentYear.isEmpty()) {
                    printYearStats(currentYear, yearlyData); yearlyData = new int[4];
                }

                currentYear = year; yearlyData[0] += cancelled; yearlyData[1] += delayed;
                yearlyData[2] += diverted; yearlyData[3] += onTime;

                totalCancelled += cancelled; totalDelayed += delayed; totalDiverted += diverted;
                totalOnTime += onTime;
            }

            if (!currentYear.isEmpty()) printYearStats(currentYear, yearlyData);

            printTotals(totalCancelled, totalDelayed, totalDiverted, totalOnTime);
        }

        /**
         *
         * @param year Year corresponding to the data line
         * @param yearlyData Data after each year, cancelled, diverted, etc.
         */
        public static void printYearStats(String year, int[] yearlyData) {
            System.out.printf("%-4s%,14d%,14d%,14d%,14d%n",
                    year,
                    yearlyData[0],
                    yearlyData[1],
                    yearlyData[2],
                    yearlyData[3]);
        }

        /**
         *
         * @param cancelled Number of flights cancelled
         * @param delayed Number of flights delayed
         * @param diverted Number of flights diverted
         * @param onTime Number of flights on time
         */
        public static void printTotals(int cancelled, int delayed, int diverted, int onTime) {
            int totalFlights = cancelled + delayed + diverted + onTime;

            System.out.println("============================================================");
            System.out.printf("%,18d%,14d%,14d%,14d%n",
                    cancelled,
                    delayed,
                    diverted,
                    onTime);
            System.out.printf("%-4s%13.1f%%%13.1f%%%13.1f%%%13.1f%%%n",
                    "",
                    (cancelled * 100.0) / totalFlights,
                    (delayed * 100.0) / totalFlights,
                    (diverted * 100.0) / totalFlights,
                    (onTime * 100.0) / totalFlights);
        }
    }

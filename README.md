GETTING STARTED: You must start from some combination of the CSV Sprint code that you and your partner ended up with. Please move your code directly into this repository so that the pom.xml, /src folder, etc, are all at this base directory.

IMPORTANT NOTE: In order to run the server, run mvn package in your terminal then ./run (using Git Bash for Windows users). This will be the same as the first Sprint. Take notice when transferring this run sprint to your Sprint 2 implementation that the path of your Server class matches the path specified in the run script. Currently, it is set to execute Server at edu/brown/cs/student/main/server/Server. Running through terminal will save a lot of computer resources (IntelliJ is pretty intensive!) in future sprints.

Project Details
Project Name: Server
Repo Link: https://github.com/cs0320-s24/server-fish444_b3nkang
Design Choices
This project utilizes four handlers to cover four endpoints for our server, respectively

loadcsv, which takes a csv from a given filepath, preparing it for use for future endpoints,
viewcsv, which displays the csv data in its response,
searchcsv, which takes in a search term and optional specificity arguments to find which rows a given term may be in,
broadband, which takes in a state and county parameter and returns the %age of inhabitants with broadband access.
and BroadbandProxy, which is a proxy class that implements List of List of Strings that proxies the ACS querys.

Errors/Bugs
Various defensive programming measures are taken in this implementation, including the use of path normalization to prevent unauthorized access to files in loadcsv, proxies, caching, and more. Error handling has been implemented so as to not crash the server, but return an informative response of the nature of the error - this goes for loadcsv, viewcsv, searchcsv, and broadband.

However, as this is a continued implementation of CSV, the issues which previously plagued CSV still exist on this current implementation. This primarily means that for CSVs with malformed data, if a row has null or unset values at the start or end of a row that are not 'wrapped' with non-null/unset values, when the readReader() method in Parser attempts to split and process the CSV data, it will often eliminate the null altogether, which results in a returned grid of a dimension where some rows are not of the full grid length. There is an error handler for this, but the issue itself still persists.

Tests
[TO COMPLETE FOR FISH]

How to
You must load a CSV first with http://localhost:3232/loadcsv?filepath=data/prod/YourFileNameHere.csv.

If successful, you can use the other two CSV-based endpoints:

viewcsv returns a JSON string of the format of the csv, which you can call with http://localhost:3232/viewcsv.

searchcsv returns a JSON string of the rows within the csv with a certain term, with flexibility for searching within a specific row, which can be done with http://localhost:3232/searchcsv?searchvalue=SearchTermHere&header=BooleanIsThereHeader&columnidentifier=ColumnIndexOrHeaderName.

Lastly, you can retrieve the %age of Americans within a given county in the US with broadband access with http://localhost:3232/broadband?state=StateName&county=CountyName.

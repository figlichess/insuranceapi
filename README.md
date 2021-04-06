# Candidate homework

## Purpose of this is to test candidate’s following skills:
* Algorithmic
    (design patterns, OOP);
* Java syntax
    (Java 8 or higher);
* Fluency in different technologies
    (in-memory-db, front-end/back-end frameworks etc);
* Understanding and implementing business logic;

## Remarks
1.	Tasks ordering is not set in stone - i.e. even when doing first task it should still be covered by unit-tests. 
2.	For a junior/medium level developer it might make sense to start from first, but it is not strictly required.
3.	All work should be committed to git and published in public repository - so that later commit history can be viewed. Work like you would in a multi-developer team. 
4.	If you have completed a task (or when starting a task) create a branch with a name "task X" and leave the final running state there.
5.  Add readme.md, where is step-by-step described how to run your app. 

## Tasks description:
1. Build a CLI app to calculate car casco payment and write results to file. Take into account only risk coefficients vehicle age, make and current value:
    * annual and monthly fee;
    * all car producers have a make_risk of 1, if not listed in data.json (data.make_coefficients);
    * if vehicle producer is not listed in average purchase price data, skip calculations
2. Repeat step 1 and calculate car casco payment, when previous indemnity is added as risk;
3. Import original datasets into in memory database:
    * Based on db data repeat step 1 and save results in db;
4. Convert CLI app to web app and display:
    * all available risks. Highlight risks, which are included in calculation;
    * calculated data: id, annual payment, car producer, year of production, mileage, previous indemnity;
5. Add check-box that can change view:
    * If check-box is checked, add to view monthly payment column;
6. Group results showing only 10 results per page;
7. Add check-box that takes into account whether indemnity risk is used in casco payment calculation or not;
8. Create a functionality to sort results by column name;
9. CRUD for managing cars:
    * functionality to modify or delete cars in list;
    * functionality to add new cars;
10. CRUD for managing risks:
    * functionality to modify or delete risk-parameter key-pair in list;
    * functionality to add new risk to formula and take it into account during calculation;
11. Implement unit tests;

## Initial data provided:
1. Datasets:
    * vehicles.csv;
    * data.json;

2. Formulas:
    * formulas.md;

## Running:
InsuranceApplication should be run using three arguments csv input file for vehicles, json file for risks 
and output file destination to write the results. First two included in the project. 
You can use "src/main/resources/input/vehicles.csv" "src/main/resources/input/data.json" "src/main/resources/output/TestWriter.csv" as arguments
as it is configured in dockerfile by default.

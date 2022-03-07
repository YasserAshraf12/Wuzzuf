# SpringJavaProject
## Description 

Application use Wuzzuf Jobs data as dataset load it , clean it and remove duplicates,
 handle years of experience feature ,then get some Insights about the data and plot some charts for Insights 
 ,finally , Make a web service to provide this using spring boot framework. 

## Functions
- Display tabular data.
- Remove Nulls and duplicates.
- Getting Structure and Summary of Data .
- Get Most Popular Job Titles .
- Get Most Popular Jobs per Company .
- Get Most Popular Areas .
- Get The Most Common Required Skills.
- Display pie chart to the most popular job titles .
- Display bar chart to the most.
- Display bar chart to the most.
- Factorize "yearExp" feature.


## Dependency 

SpringJava Application is managed using Maven.

- Spark dependency
- Xchart dependency
- Spring dependency


## Statistics Routes 

Spring Application is run on port 8080 

1. Summary [http://localhost:8080/job/summary]
2. Structure [http://localhost:8080/job/structure]
3. Most populat Job Titles [http://localhost:8080/job/popularTitles]
4. Most Popular Jobs per Company [http://localhost:8080/job/popularJobsCompany]
5. Most Required Skills [http://localhost:8080/job/popularSkills]
6. Most Popular Areas [http://localhost:8080/job/popularAreas]
7. Printing Top of data [http://localhost:8080/job/print]
8. Viewing 3 in Chart [http://localhost:8080/job/[http://localhost:8080/job/popularJobsChart]
9. Viewing 4 in Chart [http://localhost:8080/job/popularJobsCompanyChart]
10. Viewing 6 in Chart [http://localhost:8080/job/popularAreasChart]



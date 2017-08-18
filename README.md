MarkupProject
=============

How to get the application up and running
-----------------------------------------
1. Download the zip file from GitHub. This will give you access to all of the folders.
2. The create table statements as well as the query to find the average score across each key are located in the 'schema' folder. The 'Users.sql' script will be the first thing that needs to be run. This can be run straight from the MySQL terminal. This will make sure that the table is created, so the Java program can run successfully.
3. Before running the Java file, you need to update the url, username, and password variables, which are located in the connect method. This will allow you to connect to a host. If you would like to connect to your local machine, then you just need to update the password variable (MySQL password).
4. Run the Java program either through the terminal or through an IDE. 
5. The first thing that the program asks for is the file that you would like to parse through. You should enter one of the file names (ex. bob_2013_02_10). NOTE: the '.html' will be put on by the program.
6. After this, you will be asked who you would like to retrieve scores from. You should enter only the name that is in any of the files (ex. bob, cari, or john). 
7. Once the program is completed, you may go back into the 'schema' folder and run the 'Users.sql' script. This will allow you to see the average of the scores across each key.

Info
----
Create a class in the langauge of your choice that will read HTML content input and score and give 
an arbitrary score based on a set of rules. The content should be assigned a unique id based on the prefix described below. 
Changes to the content can be re-run over time to determine improvement or regression of the score. Each unique run should be stored with the date and time it was run along with the score received for the content.

You may use external libraries if you feel they will help, but you must place them in the appropriate folder based on the project layout section.

Code Requirements
-----------------
* Accept HTML Content Input
* Accept unique id for HTML Content to score (filename prefix)
* Score HTML content using the scoring guide
* Save results to a MySQL database
* Method: Retrieve scores for a unique id
* Method: Retrieve all scores run in the system for a custom date range
* Method: Retrieve highest scored unique id 
* Method: Retrieve lowest scored unique id
* Additionally you should write one query that will find the average score for all runs **__see project layout below__**

## Bonus
* Tag names are case-insensitive (ie: Html is the same as html)
* Parse multiple sections of the HTML content at the same time for performance

Scoring Rules
-------------
Each starting tag should below has been assigned a score. Each tag in the content should be added to or subtracted from the total score.

(We will assume for this project our html code creator created valid html)

| TagName | Score Modifier | TagName | Score Modifier |
| ------- | :------------: | ------- | -------------- |
| div     | 3              | font    | -1             |
| p       | 1              | center  | -2             |
| h1      | 3              | big     | -2             |
| h2      | 2              | strike  | -1             |
| html    | 5              | tt      | -2             |
| body    | 5              | frameset| -5             |
| header  | 10             | frame   | -5             |
| footer  | 10             |

example:

````
<html>
    <body>
      <p>foo</p>
      <p>bar</p>
      <div text-align='center'>
        <big>hello world</big>
      </div>
    </body>
</html>
````

2 p tags = 2 x 1 <br>
1 body tag = 1 x 5 <br> 
1 html tag = 1 x 5 <br>
1 div tag = 1 x 3 <br>
1 big tag = 1 x -2 <br>
**Total Score: 13**


Project Layout
--------------
####/data

* Contains the HTML content data to parse, format: (keyname_yyyy_mm_dd)

ie: 
* dougs_2012_02_04.html 
* dougs_2012_04_01.html 
* dougs_2012_07_01.html

####/src

* Your code goes in here.

####/schema

* Your create table statements for MySQL. 
* Your query to find the average score across each key. (see data example below)

ie: 

key | avgScore 
|---|--------|
dougs | 10.35 
bobs  | 8.03    

####/vendor

* Place any external libraries not written by you in the /vendor folder

Instructions
------------
* Fork this repo into your own github account.  
* Begin working on the project and commit your code to your forked repo.
* When you are finished. Email your RedVentures recruiter or submit a pull request on the project. 



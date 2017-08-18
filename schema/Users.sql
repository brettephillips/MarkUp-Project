-- Creates the database and table where the scores will be saved
CREATE DATABASE Users;
USE Users;
CREATE TABLE FINALSCORES(fileName VARCHAR(30) NOT NULL, 
                         date_saved TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         name VARCHAR(25) NOT NULL,
                         score INT NOT NULL,
                         CONSTRAINT FINALSCORES_pk PRIMARY KEY(fileName, score)
                        );
                        
-- Query that will find the average score across each key
SELECT name AS theKey,
       AVG(score) AS avgScore
FROM FINALSCORES
GROUP BY name;
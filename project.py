# import library

import pyodbc
import pandas as pd
import numpy as np

# Connect to database
myconn = pyodbc.connect('Driver={SQL Server};'
                        "Server=MARYTHINKPAD\SQLEXPRESS;"
                        "Database=airbnb;"
                        "Trusted Connection=yes;")
cursor = myconn.cursor()
FileName = pd.read_csv('Airbnb_data.csv')
FileName["construction_year"].fillna("-1", inplace=True)
FileName["rules"].fillna("NULL", inplace=True)
FileName["property_name"].fillna("NULL", inplace=True)
FileName["last_review_date"].fillna("1900-10-10", inplace=True)
FileName["reviews_per_month"].fillna("-1", inplace=True)
FileName["rating"].fillna("-1", inplace=True)
FileName["number_of_reviews"].fillna("-1", inplace=True)
FileName["service_fee"].fillna("0", inplace=True)
FileName["instant_booking"].fillna("NULL", inplace=True)
FileName["cancellation"].fillna("NULL", inplace=True)
FileName["neighbourhood"].fillna("NULL", inplace=True)
FileName["country"].fillna("NULL", inplace=True)
FileName["neighbourhood_group"].fillna("NULL", inplace=True)
FileName["longitude"].fillna("300", inplace=True)
FileName["latitude"].fillna("300", inplace=True)
FileName["verification_status"].fillna("NULL", inplace=True)
FileName["min_nights"].fillna("-1", inplace=True)
FileName["days_per_year"].fillna("-1", inplace=True)
FileName["host_name"].fillna("NULL", inplace=True)
FileName["guest_name"].fillna("NULL", inplace=True)
FileName["email"].fillna("NULL", inplace=True)

# CREATE TABLE
newtable = """
    DROP TABLE IF EXISTS availability;
    DROP TABLE IF EXISTS booked_by;
    DROP TABLE IF EXISTS guest;
    DROP TABLE IF EXISTS price;
    DROP TABLE IF EXISTS policies;
    DROP TABLE IF EXISTS location;
    DROP TABLE IF EXISTS hosted_by;
    DROP TABLE IF EXISTS host;
    DROP TABLE IF EXISTS review;
    DROP TABLE IF EXISTS property;
    
    CREATE TABLE property(
    pID int NOT NULL,
    property_name varchar(MAX) NULL,
    room_type varchar(100) NULL,
    construction_year int NULL,
    rules varchar(MAX) NULL,
    PRIMARY KEY (pID)
    );
    
    CREATE TABLE review(
    pID int NOT NULL,
    reviews_per_month DECIMAL(5,2) NULL,
    last_review_date DATE NULL,
    rating int NULL,
    number_of_reviews int NULL,
    PRIMARY KEY (pID),
    FOREIGN KEY (pID) REFERENCES property(pID)
    );
    
    CREATE TABLE price(
    pID int NOT NULL,
    base_price money NOT NULL,
    service_fee money DEFAULT 0.0 NOT NULL,
    PRIMARY KEY (pID, base_price, service_fee),
    FOREIGN KEY (pID) REFERENCES property(pID)
    );
    
    CREATE TABLE policies(
    pID int NOT NULL,
    instant_booking varchar (6) NULL,
    cancellation varchar (100) NULL,
    PRIMARY KEY (pID),
    FOREIGN KEY (pID) REFERENCES property(pID)
    );
    
    CREATE TABLE location(
    pID int NOT NULL,
    neighbourhood_group varchar(100) NULL,
    longitude DECIMAL(8,5),
    latitude DECIMAL(8,5),
    country varchar(100), 
    neighbourhood varchar(100),
    PRIMARY KEY (pID),
    FOREIGN KEY (pID) REFERENCES property(pID)
    );
    
    CREATE TABLE host(
    hID BIGINT NOT NULL,
    host_name varchar(100) NULL,
    verification_status varchar(30) NULL,
    PRIMARY KEY (hID)
    );
    
    CREATE TABLE hosted_by(
    pID int NOT NULL,
    hID bigint NOT NULL,
    PRIMARY KEY (pID, hID),
    FOREIGN KEY (hID) REFERENCES host(hID),
    FOREIGN KEY (pID) REFERENCES property(pID)
    );
    
    CREATE TABLE guest(
    gID int NOT NULL,
    guest_name varchar(100) NULL,
    email varchar(300) NULL,
    PRIMARY KEY (gID)
    );
    
    CREATE TABLE booked_by(
    pID int NOT NULL,
    gid int NOT NULL,
    PRIMARY KEY (pID, gid),
    FOREIGN KEY (gID) REFERENCES guest(gID),
    FOREIGN KEY (pID) REFERENCES property(pID)
    )
    
    CREATE TABLE availability(
    pID int NOT NULL,
    min_nights int NULL,
    days_per_year int NULL,
    PRIMARY KEY (pID),
    FOREIGN KEY (pID) REFERENCES property(pID)
    );
    """
# try:
#     myconn.execute(newtable)
#     print("Great Table Created successfully")
# except:
#     print("Attempt Unsuccessful")
# myconn.commit()

# insert bulk data into the table , use parameter ? to prevent sql injection
# for index, row in FileName.iterrows():
#     # print(type(row.pID))
#     cursor.execute("INSERT INTO property (pID, property_name, room_type, construction_year, rules) values(?, ?, ?, ?, ?)", row.pID, row.property_name, row.room_type, row.construction_year, row.rules)
#     cursor.execute("INSERT INTO review (pID, reviews_per_month, last_review_date, rating, number_of_reviews) values(?,?, ?, ?, ?)", row.pID, row.reviews_per_month, row.last_review_date, row.rating, row.number_of_reviews)
#     cursor.execute("INSERT INTO price (pID, base_price, service_fee) values(?,?, ?)", row.pID, row.base_price, row.service_fee)
#     cursor.execute("INSERT INTO policies (pID, instant_booking, cancellation) values(?,?, ?)", row.pID, row.instant_booking, row.cancellation)
#     cursor.execute("INSERT INTO location (pID, neighbourhood_group, longitude, latitude, country, neighbourhood) values(?,?, ?, ?,?,?)", row.pID, row.neighbourhood_group, row.longitude, row.latitude, row.country, row.neighbourhood)
#     cursor.execute("INSERT INTO host (hID, host_name, verification_status) values(?,?,?)", row.hID, row.host_name, row.verification_status)
#     cursor.execute("INSERT INTO hosted_by (pID, hID) values(?,?)", row.pID, row.hID)
#     cursor.execute("INSERT INTO availability (pID, min_nights, days_per_year) values(?,?, ?)", row.pID, row.min_nights, row.days_per_year)
#     cursor.execute("INSERT INTO guest (gID, guest_name, email) values(?,?, ?)", row.gID, row.guest_name, row.email)
#
#     myconn.commit()
#     print(str(index + 1) + ' Rows inserted')

# select number of record from the table to confirm insertiion
count = cursor.execute("select count(*) from property")
rst = cursor.fetchval()
print('there are ' + str(rst) + ' rows in the property table')

#UPDATE TABLE
updatetable = """
    UPDATE property set construction_year = NULL where construction_year = -1
    UPDATE property set rules = NULL where rules like 'NULL'
    UPDATE property set property_name = NULL where property_name like 'NULL'
    UPDATE review set last_review_date = NULL where last_review_date = '1900-10-10'
    UPDATE review set rating = NULL where rating = '-1'
    UPDATE review set number_of_reviews = NULL where number_of_reviews = '-1'
    UPDATE price set service_fee = NULL where service_fee = '0'
    UPDATE policies set instant_booking = NULL where instant_booking like 'NULL'
    UPDATE policies set cancellation = NULL where cancellation like 'NULL'
    UPDATE location set neighbourhood = NULL where neighbourhood like 'NULL'
    UPDATE location set neighbourhood_group = NULL where neighbourhood_group like 'NULL'
    UPDATE location set longitude = NULL where longitude = '300'
    UPDATE location set latitude = NULL where latitude = '300'
    UPDATE host set host_name = NULL where host_name like 'NULL'
    UPDATE host set verification_status = NULL where verification_status like 'NULL'
    UPDATE availability set min_nights = NULL where min_nights = '-1'
    UPDATE availability set days_per_year = NULL where days_per_year = '-1'
    UPDATE guest set guest_name = NULL where guest_name like 'NULL'
    UPDATE guest set email = NULL where email like 'NULL'
"""

try:
    myconn.execute(updatetable)
    print("Great Table UPDATED successfully")
except:
    print("Attempt Unsuccessful")
myconn.commit()
# close the connection
myconn.close()

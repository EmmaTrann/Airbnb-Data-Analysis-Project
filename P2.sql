
--1. Display the relationship between price and rating
Select rating, (base_price + service_fee) as total_price
from review
join price p on review.pID = p.pID
where rating is not NULL
order by rating desc;

--2. Display all the property listings in a given price range
--price range specify by user
select distinct property.pID as property_ID, property_name, (base_price + service_fee) as total_price
from property
join price p on property.pID = p.pID
where base_price + service_fee between 550.50 and 600 -- the 550.50 and 600 just random number I picked
order by base_price + service_fee asc; --optional for asc or desc

--3. Display all the property listings in a given rating range with its location and price
select distinct rating, property.pID as property_ID, property_name, room_type,
                l.neighbourhood, l.country, (base_price + service_fee) as total_price
from property
join review r on property.pid = r.pid
join price p on property.pID = p.pID
join location l on property.pID = l.pID
where r.rating in (5); -- the rating will be specify by user same way as when user specify price range

--4. Count and display asc the number of property listings in each neighbourhood
Select neighbourhood, Count(*) as number_of_listing
from location
where neighbourhood is not null
GROUP BY neighbourhood
order by number_of_listing asc;

--5.1 Count the number of listing a guest booked under their name and email
Select guest_name, email, count(p.pID)
from guest
join booked_by bb on guest.gID = bb.gid
join property p on p.pID = bb.pID
group by guest_name, email;

--5.2 Display the list of property booked with the guest that booked it
Select guest_name, guest.email, p.pID, p.property_name, p.room_type
from guest
join booked_by bb on guest.gID = bb.gid
join property p on p.pID = bb.pID
where guest_name in ('Aka') --allow user to put in the name they want


peek-challenge
==============

Java/GAE implementation of peek.com API challenge https://github.com/gadabout/passport

Live version here: http://peekchallenge.appspot.com

Notes
===========
Uses Google App Engine as web application platform including the High Replication Datastore, fronted by Memcache. 

Dependancies
==============================
1. Datastore interface: https://code.google.com/p/objectify-appengine/
2. JSON serialization: https://github.com/FasterXML/jackson

Assumptions
===========
1. timeslots don't span days (i.e when evaluation if timeslots overlap, no consideration of a timeslot that is multi-day or midnight cruies, etc.)

2. timeslots are set up and reasonbly stabe before boat allocation and bookings. 

This may not be valid in real life, but I took a few shortcuts for this exercise and didn't lock down all data manipulation inside proper transaction logic. Specifically if timeslots were being manipulated concurrently with bookings, there some small potential for error. Fixing this would require a bit more work on the data model to get datastore entities into the same entity group. The more common case of multiple bookings being attempted concurrently is protected.



Instructions to run
==================

1. Requires Google App Engine SDK for Java https://developers.google.com/appengine/downloads
2. <add command line instruction here for starting development server>

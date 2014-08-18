peek-challenge
==============

Java/GAE implementation of peek.com API challenge https://github.com/gadabout/passport

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
2. 

Instructions to run
==================

1. Requires Google App Engine SDK for Java https://developers.google.com/appengine/downloads
2. 

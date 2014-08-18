peek-challenge
==============

Java/GAE implementation of peek.com API challenge https://github.com/gadabout/passport

Live version here: http://peekchallenge.appspot.com

### Notes
Uses Google App Engine as web application platform including the High Replication Datastore, fronted by Memcache. 

### Dependancies
1. Datastore interface: https://code.google.com/p/objectify-appengine/
2. JSON serialization: https://github.com/FasterXML/jackson

#### Implementation Notes

This implementation used Google's High Replication Datastore which is a distributed, "eventually consistent", NoSQL datastore. This choice was made primarily to remove dependancies and allow this demo to run without lots of database setup. There are, however, scalability benefits to this approach which could make a difference at large scale.

This implementation uses an "eventually consistent" data model meaning that it's possible for a query to not "see" writes by other clients for a few seconds. The automated "Test Case 1" setup can expose this because it adds two boats and immediately queries them. If running on the [development server](https://developers.google.com/appengine/docs/java/tools/devserver) you may see this because it simulates some percentage of inconsistent reads and you may need to refresh the client after running through the setup. This is unlikely to be a problem if API calls are being made in response to user activities vs. an automated setup. It's possible to reduce the simulated likelyhood of this using a JVM flag, -Ddatastore.default_high_rep_job_policy_unapplied_job_pct. I set this flag to value of 1 for my testing.

For the API calls that modify data, transactions are used to ensure that mulitple entities are updated in consisten manor. There is one case not really covered by transaction which is that when assignments or bookings are being made, there is slim possibility that the underlying list of available time slots could change introducing (or alleviating) boat availability. If left as it, there would have to be a rule that time slots are set up in advance of assignments and bookings which could be enforced by some other application contraint. This seemed like a reasonable tradeoff for this exercise. If not acceptable in a real application, the data model could be modified to ensure that all the required read and writes could live in a transaction context. There would be a performance penalty for this. Alternative solutions are also possible if further assumptions are made. For example, timeslots could be store in a more strictly "per day" manor instead of just a big list queryable by start time. The "per day" approach could remove the need for queries and therefore allow cross entity transactions.

### Ideas for Improvement
1. Consider using the plural in RESTful API endpoints. For example, instead of having "boat" and "boats", just have "boats". A POST to "boats" adds a boat, a GET reads the list of boats. A specific boat could then be referenced by the semantic "boats/boat-id".
2. In case of a conflict of boat allocation as in Test Case 2, it might be better to reflect that a boat is no longer available by not returning it in the "boats" field of the timeslots response. 




### Instructions to run
1. Requires Google App Engine SDK for Java https://developers.google.com/appengine/downloads
2. --add command line instruction here for starting development server

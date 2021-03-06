peek-challenge
==============

Java/GAE implementation of peek.com API challenge https://github.com/gadabout/passport

Live version here: http://peekchallenge.appspot.com


#### Implementation Notes

This implementation used Google's High Replication Datastore (HRD) which is a distributed, "eventually consistent", NoSQL datastore. This choice was made primarily to remove dependancies and allow this demo to run with minimal setup. There are, however, scalability benefits to this approach.

Th HRD uses an "eventually consistent" data model meaning that it's possible for a query to not "see" writes by other clients for a few seconds. The automated "Test Case 1" setup can expose this because it adds two boats and immediately queries them. If running on the development server you may see this show up as "Unknown Boat" because the development server simulates some percentage of inconsistent reads and you may need to refresh the client after running through the setup. This is unlikely to be a problem if API calls are being made in response to user activities vs. an automated setup. It's possible to reduce the simulated likelyhood of this using a JVM flag, datastore.default_high_rep_job_policy_unapplied_job_pct. I set this flag to value of 1 (i.e. 1 percent) for my testing.

#### Q & A
Q: What complications can you foresee while doing this exercise? 
A: The data model must stay consitent when bookings or assignments are made. Transactions should be used when modifying multiple entities that relate to one another.

Q: Would your solution scale?
A: Yes!

#### Ideas for Improvement

Improvements to the challenge

1. Consider using the plural in RESTful API endpoints. For example, instead of having "boat" and "boats", just have "boats". A POST to "boats" adds a boat, a GET reads the list of boats. A specific boat could then be referenced by the semantic "boats/boat-id".
2. In case of a conflict of boat allocation as in Test Case 2, it might be better to reflect that a boat is no longer available by not returning it in the "boats" field of the timeslots response. 

Improvements to this implementation

1. More efficient allocation of bookings to available boats. This implementation tries to use a boat that is already going in the timeslot availble. This tends to avoid conflicts like in test case 2, however, it does not always maximize the largest available booking. Instead, more knowledge of time slot conflict could be added to the booking to boat allocation to keep larger blocks of seats available for booking.

#### Dependancies
1. Datastore interface (also adds caching): https://code.google.com/p/objectify-appengine/
2. JSON serialization: https://github.com/FasterXML/jackson

#### Setup/Running Instructions
If you want to just try it, there is a live deployed version available here: http://peekchallenge.appspot.com 
You will need to change the API_HOST value in the client config to point to the live instance. There is a simple page at the root level of the implementation which allows all data to be wiped clean. 

Alternatively, the code can be run locally using the [development server](https://developers.google.com/appengine/docs/java/tools/devserver) provided with the [App Engine Java SDK](https://developers.google.com/appengine/downloads#Google_App_Engine_SDK_for_Java). The only way I've run it is by using the [Google Plugin for Eclipse](https://developers.google.com/appengine/docs/java/tools/eclipse) which includes the SDK. Eclipse Java EE IDE for Web Developers (Version: Kepler Service Release 2) was used for development and test along with version 1.9.9 of the SDK. I used the following flags to run locally:

Dev server:
**--port=3000** --disable_update_check --address=0.0.0.0 /Users/awaddell/git/peek-challenge/peek-challenge/war

JVM:
**-Ddatastore.default_high_rep_job_policy_unapplied_job_pct=1** -javaagent:/Users/awaddell/kepler/eclipse/plugins/com.google.appengine.eclipse.sdkbundle_1.9.9/appengine-java-sdk-1.9.9/lib/agent/appengine-agent.jar -Xmx512m -Xbootclasspath/p:/Users/awaddell/kepler/eclipse/plugins/com.google.appengine.eclipse.sdkbundle_1.9.5/appengine-java-sdk-1.9.5/lib/override/appengine-dev-jdk-overrides.jar





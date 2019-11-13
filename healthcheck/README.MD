# service to monitor remote running service
This is always running running service (#http://localhost:8080/healthcheck) which will hit remote service every second to check status.
To get the health analytics, last 60 hits are checked.
If a service is able to connect  successfully,then its entry is put in a map LinkedHashMap<Long,Boolean> timeEntryMap;
- entry being time as key and Boolean true is success else False. 
Also an entry is put in another map HashMap<Boolean, Set<Long>> statusCheckMap;
- entry being True for success and value as set of time units with success, else false entry with value as timeUnits of unsuccessfull service calls to remote service.

Once the map has 60 entries 
- first entry is removed from timeEntryMap and its corressponding
value is checked in  statusCheckMap .if the value set contains the time unit,then it is removed from the set. 
This helps in figuring out number of successfull calls to remote servicce in last 60 seconds

#In case of remote service being unavailable ,I have tried to use Spring retry to try a few time and then fall back to recovery method which will used to send service unavailable message..
#Unfortunately Retry is not working at the moment and due to shortage of time , I have left the solution here . Hopefully we can have a discussion on this if solution is accepted :)

Thanks.
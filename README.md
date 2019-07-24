# jde-e1-component-java

> Integration component for JD Edwards EnterpriseOne for elastic.io connector

## Description
JD Edwards EnterpriseOne component for the [elastic.io platform](http://www.elastic.io;).
JD Edwards EnterpriseOne component is designed for JD Edwards EnterpriseOne integration.
### Purpose
The component is providing interaction with JD Edwards EnterpriseOne instance.
### How works
Current version of the JD Edwards EnterpriseOne is using in-built libraries of JD Edwards EnterpriseOne.
### Requirements
The component was tested with JD Edwards EnterpriseOne 9.
## Credentials
![image](https://user-images.githubusercontent.com/40201204/47240036-bf9a3900-d3ef-11e8-866a-c15f59512c5c.png)
### Server
In the Server field please provide hostname of the server or IP-address, e.g. `acme.com` or `192.168.0.1`
### Port
In the Port field please provide port of the server, e.g. `6017`
### User
In the User field please provide a username that has permissions to interact with the instance.
### Password
In the Password field please provide a password of the user that has permissions to interact with the instance.
### Environment
In the Environment field please provide a name of the instance environment.
### Function
In the Function field please provide name of any existing function for initialization of connection.
## Actions
### Execute Function
> Execute Function with parameters
![image](https://user-images.githubusercontent.com/40201204/47239527-3f270880-d3ee-11e8-9445-07c13ce58b20.png)
#### Function
In the Function field please provide name of any existing function.
#### Input json schema
The input json schema generates automatically for provided function.
#### Output json schema location
The output json schema generates automatically for provided function.
## Additional info
Important! The interaction with JD Edwards EnterpriseOne instance is exclusive. There will be locked connection port at server's side during process of interaction. Therefore, you should be very careful with real-time flows - they are blocking any other interactions with the instance until stop of them. 

## Known issues
No known issues are there yet.

## License
Apache-2.0 © [elastic.io GmbH](https://www.elastic.io "elastic.io GmbH")

## <System> API and Documentation links
[elastic.io iPaaS Documentation](https://support.elastic.io/support/home "elastic.io iPaaS Documentation")
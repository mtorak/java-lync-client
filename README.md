java-lync-client
================

A java client for Microsoft Lync 2013 Server Unified Communications Web API(UCWA) which is a REST API that exposes Lync Server 2013 Instant Messaging and Presence capabilities.

<h3>--WARNING--</h3>
-----------------------
<ol>
<li>This code is all free, copy/edit/redistribute as you wish ;)</li>
<li>This is just a raw implementation, an experimental work. There could be mistakes/better ways of some implementation etc. Just use this as a working sample code and improve according to your needs.</li>
</ol>

<p>
The base class for this UCWA REST client is LyncClient. Lync server authentication is done with a single user account and the token sent from lync server is used for all requests. LyncClient holds an authenticationMap to store the token. LyncGatewayServlet holds an instance of LyncClient and passes presence/contact note/instant message request to this client.
<p>
<br/>
<p>
<b>Here is basic execution logic: </b>
<ol>
<li>LyncClient.authenticationMap is peeked when a request come, if there is a token there then it is used as Authorization header for the http request which is sent to Lync Server 2013. If not then LyncClient.authenticate() method is called which makes authentication requests and puts the provided token to the authenticationMap. This token has an 8 hour of validity.(it may be a configurable value ob the server)</li>
<li>If the authentication token is timed out, then Lync 2013 server responds with an HTTP Error 401 Unauthorized response. In this case authenticate() is called and old token is replaced with the new one. Authentication requests are sent by only one thread, because all the lync operations are done with a single token. So this parts of code are thread safe.(To enable multi-user support: for each sip there should be a different token, edit this part if you need a multi-user java client for lync 2013 ucwa api. But in our case we only needed to get presence and contact note of users via a single lync account.)</li>
<li>UCWA REST API request are sent via Apache HttpClient API version 4.2.1. PoolingClientConnectionManager of this api is used to manage http connections. </li>
<li>LyncGatewayServlet is used as a proxy to send lync presence/contact note/instant message etc. requests. All the responses are in JSON format.</li></ol></p>
<p>
<b>Example usage:</b><br/>
<b>url:</b> http://somePcIp/someProject/lyncGateway?sipForPresence=john.doe@somecompany.com.tr<br/>
<b>json response:</b> {"ResponseCode":"200","Sip":"sipForPresence=john.doe@somecompany.com.tr","Presence":"Online"}
<br/>
<b>url:</b> http://somePcIp/someProject/lyncGateway?sipForContactNote=john.doe@somecompany.com.tr<br/>
<b>json response:</b> {"ContactNote":"test","ResponseCode":"200","Sip":"john.doe@somecompany.com.tr"}
</p>


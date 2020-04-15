tiles3 and springmvc 3
http://dhruvgairola.blogspot.com/2013/03/spring-mvc-with-apache-tiles-3.html
https://github.com/michaelisvy/mvc-layout-samples

https://developer.okta.com/docs/guides/implement-auth-code/exchange-code-token/



client_id:client_secret
https://www.baeldung.com/java-base64-encode-and-decode

String originalInput = "test input";
String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());



PKCE url sample from snippets.awsdhenton.com


https://dev-862436.okta.com/oauth2/default/v1/authorize
?
client_id=0oa50kbe3200zNvhk4x6
&code_challenge=lNu21DhuSLRwosREqbN9htt6s6j3qW0HToWDIgkmwMc
&code_challenge_method=S256
&nonce=MZsNqiSwsCDtyUYNo66nGL53z9JR7aPT0ciwKSAKpCslZe2Yz5WliE1Lgi8LBbDE
&redirect_uri=https%3A%2F%2Fsnippets.awsdhenton.com%2Fcallback
&response_type=code
&state=Iec7lC20zw3zr4UdwOR6hDE1wNqxqZfwP8cUcbcQICuPYHJvKKadbv6o0AbuLi5P
&scope=openid%20email%20profile%20groups

// this is what you will have to handle

https://snippets.awsdhenton.com/callback?code=cFPtly7BIcJTQ5KbkOt4&state=Iec7lC20zw3zr4UdwOR6hDE1wNqxqZfwP8cUcbcQICuPYHJvKKadbv6o0AbuLi5P



POST to 

https://dev-862436.okta.com/oauth2/default/v1/token

headers:

content-type: application/x-www-form-urlencoded
accept: application/json
:authority: dev-862436.okta.com
:method: POST
:path: /oauth2/default/v1/token
:scheme: https
accept: application/json
accept-encoding: gzip, deflate, br
accept-language: en-US,en;q=0.9,la;q=0.8
content-length: 206
content-type: application/x-www-form-urlencoded
origin: https://snippets.awsdhenton.com
referer: https://snippets.awsdhenton.com/
sec-fetch-dest: empty
sec-fetch-mode: cors
sec-fetch-site: cross-site
user-agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36
x-okta-user-agent-extended: okta-auth-js-3.0.1

Form data

client_id: 0oa50kbe3200zNvhk4x6
redirect_uri: https://snippets.awsdhenton.com/callback
grant_type: authorization_code
code: cFPtly7BIcJTQ5KbkOt4
code_verifier: 7c64f645a56c0378653e20bf9c85ce9345af25c60e7

returns

{"token_type":"Bearer","expires_in":28800,"access_token":"eyJraWQiOiJkSDlGcXE0VkVaeEVHeVJFTUVBdnVBOWdfQjYzX0kwWHlDRVFULURIbmxRIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULkMtS2NmYThqUW82YnJvUFVSU0lnV3pYdTB1NnJnSFFZMmdzdklJbXNrZ1EiLCJpc3MiOiJodHRwczovL2Rldi04NjI0MzYub2t0YS5jb20vb2F1dGgyL2RlZmF1bHQiLCJhdWQiOiJhcGk6Ly9kZWZhdWx0IiwiaWF0IjoxNTg2NTMyMTIwLCJleHAiOjE1ODY1NjA5MjAsImNpZCI6IjBvYTUwa2JlMzIwMHpOdmhrNHg2IiwidWlkIjoiMDB1MmphaHJiRnZ4YXFlTWc0eDYiLCJzY3AiOlsiZW1haWwiLCJncm91cHMiLCJvcGVuaWQiLCJwcm9maWxlIl0sInN1YiI6ImF3c2RoZW50b25AZ21haWwuY29tIn0.Z6zPQb51_bFavqGeEPWz-5NGC-9qpqEgGRQ70gf_wZ9KbHU9pMYq_JjGbnBetFIZzQn-2Om-kqz4QqQHjmYoUXjC5d3lCL2sIMm14kFJ0TjT9hbgnBLesNx8rkDQ172ys1vMtoQIo3ePJXcRlIBwA_x6DDBSlgwZiibbTXnY3N6leHwtil3NmfilFfpmhlsi6kXvFvScxcsd5oGHgQCyyZpONtSGQ_aWaWtfNbfO_rQyQME5uqkosj0pDvoxP-ox59nq71X_5gZ8bHI9N9UqL6vo_S2_nGKd02L0Wf41F8gcrtDAibVrWZom8H2oBX-ZCI0G3m4eznhizadjix6awA","scope":"email groups openid profile","id_token":"eyJraWQiOiJkSDlGcXE0VkVaeEVHeVJFTUVBdnVBOWdfQjYzX0kwWHlDRVFULURIbmxRIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiIwMHUyamFocmJGdnhhcWVNZzR4NiIsIm5hbWUiOiJEb24gSGVudG9uIiwiZW1haWwiOiJhd3NkaGVudG9uQGdtYWlsLmNvbSIsInZlciI6MSwiaXNzIjoiaHR0cHM6Ly9kZXYtODYyNDM2Lm9rdGEuY29tL29hdXRoMi9kZWZhdWx0IiwiYXVkIjoiMG9hNTBrYmUzMjAwek52aGs0eDYiLCJpYXQiOjE1ODY1MzIxMjAsImV4cCI6MTU4NjUzNTcyMCwianRpIjoiSUQuZGd0SE11ckdNc2xLdGg1cWRfUWp4dndPRTNHbU44R2YtQ1RoM3YwQlZ6USIsImFtciI6WyJwd2QiXSwiaWRwIjoiMDBvMmphaG8zVVBrTmJnQ2g0eDYiLCJub25jZSI6Ik1ac05xaVN3c0NEdHlVWU5vNjZuR0w1M3o5SlI3YVBUMGNpd0tTQUtwQ3NsWmUyWXo1V2xpRTFMZ2k4TEJiREUiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJhd3NkaGVudG9uQGdtYWlsLmNvbSIsImF1dGhfdGltZSI6MTU4NjUzMjExOSwiYXRfaGFzaCI6InozN0hKZUZNNEtFUEhCWUhmSEZYZWcifQ.gmX7tkxN3vJfJWGXJ3tCbAzP6oGXj6AX_lhj94O3R49QDoDItv2tDeRRUrB0ltWiIa6ivX7Urr_pLHG4tEsoyK-dP3Q1U9Q9f9205A_tXw3U9_qzSwetdSF7rz-iMWEX2uo8W4uHDuyh-TnAejQNwax-hQNCYmpdBLLLEBLWKZzw_LR6gCnAqDXZws4mYxyzrgGIKCjF2QPWaIooALm0984DPc7eOMqCTTs3uyTOOful_HxDqMigkY_0DxpI-Ian1yliGZHUyluCBJoEn6rOKKRe_rQGYq5l_DjtBv_74DaHraMXDaVT0AFKlbT0PeZ-jMQuhzjAbakPfWoI69Ym8A"}


 






NOTE:

Okta app must be a SPA app for PKCE to work here.



## Sample ID Token when email and profile in scopes

```json

{
  "sub": "00u58ldrhSdMUVHdM4x6",
  "name": "displayName",
  "email": "freduser5000@gmail.com",
  "ver": 1,
  "iss": "https://dev-862436.okta.com",
  "aud": "0oa50kbe3200zNvhk4x6",
  "iat": 1586777928,
  "exp": 1586781528,
  "jti": "ID.IKJXZJi2P13_Ky1FYrvR7lXIcsEL2sPDO6QNbjjIcfE",
  "amr": [
    "pwd"
  ],
  "idp": "00o2jaho3UPkNbgCh4x6",
  "nonce": "f5867756-01a8-4808-a58f-a609f81c2606",
  "preferred_username": "freduser5000@gmail.com",
  "auth_time": 1586776137,
  "at_hash": "Kbgco6MWrgU5TvhD5UeroQ"
}

```

## Sample Access Token

```json

{
  "ver": 1,
  "jti": "AT.8yDacDAmZNHLkNcKadWNUqTnfuEhr8GrLkyzSvAEDUU",
  "iss": "https://dev-862436.okta.com",
  "aud": "https://dev-862436.okta.com",
  "sub": "freduser5000@gmail.com",
  "iat": 1586777051,
  "exp": 1586780651,
  "cid": "0oa50kbe3200zNvhk4x6",
  "uid": "00u58ldrhSdMUVHdM4x6",
  "scp": [
    "openid",
    "offline_access",
    "groups",
    "profile"
  ]
}


```


## Well known endpoints

https://dev-862436.okta.com/oauth2/default/.well-known/openid-configuration

https://dev-862436.okta.com/oauth2/default/.well-known/oauth-authorization-server


## why the tokens dont contain groups

https://support.okta.com/help/s/article/Difference-Between-Okta-as-An-Authorization-Server-vs-Custom-Authorization-Server

You can authorize, get tokens and get user info against two different endpoints:

* oauth2/v1/authorize 
* oauth2/default/v1/authorize
* oauth2/default/v1/token
* oauth2/v1/token

the default word above is oktas authorizationServerId, access codes from auth server can only
be used for the corresponding token call

the difference between the "no id" and "id" calls is that ID BASED CALLS will
yield FAT tokens, ie will contain things like custom claims, eg groups
the other case where the serverId is not specified, are a standard stripped down
bare bones OIDC, against okta itself, not just 'your stuff'



# PassVault
This is a full fledged password manager that generates secure passwords and stores them along side relevant fields like username and website.. It has provisions for multiple users, and multiple passwords per user.'

## Steps to run the code : 
1. Download and extract the code to a folder
2. Open the folder as a project in an IDE (Avoid VS Code because of JavaFX dependencies)
3. (Optional) If you want to use the OTP email system for reset the user password for the app, you have to procure your own client id, secret and refresh token from <a href="https://console.cloud.google.com">here</a>.
   > Create a project, enable Gmail api in it, add credentials and select web application to get your client id and secret.
   > For the refresh token, visit <a href="https://developers.google.com/oauthplayground/">here</a> and select Gmail API. In settings select "User your own OAuth credentials" and fill yours respectively.
   > Click authorize apis, then exchange authorization code for tokens and then copy the generated refresh token from the response.
   > Fill in the respective fields in src/main/java/passvault/passvault/utils/EmailService.java, and your email associated with the project in Google Cloud Console. The email must be registered as a tester(sender email).
4. Run MainApp, if any JavaFX runtime components are missing, download them from <a href="https://openjfx.io/">here</a> and include them in the classpath.  

## Contributors
### Smeet Patil
### Soham Pawar

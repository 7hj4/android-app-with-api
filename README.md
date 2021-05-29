# android-app-with-api

# Some clarifications about the application

 A simple application is a login to register a new account
 
 The application is connected to a web server api php chack folder api 
 
 After registering a new account or logging in, the profile data is stored  temporarily using SharedPreferences
 
 Read more check  url : https://developer.android.com/reference/android/content/SharedPreferences
 
 The (HttpURLConnection) library was used to connect to my api
 
 Read more check url : https://developer.android.com/reference/java/net/HttpURLConnection
 
 # Talk about design
 
 Adobe xd software was used for designing the user interface
 
 The design can be viewed on file => desgin_app.xd
 
 # Some references to reduce reverse engineering
 
Initially, you can use ProGuard

Read more : https://blog.mindorks.com/applying-proguard-in-an-android-application

You can complicate the attacker even more. Here are some steps to for reverse engineering protection for your application

Read more : https://www.multidots.com/how-to-prevent-reverse-engineering-of-your-mobile-apps/


# about rest api in php  

The rate limit must be used for login

I did not use it because the code was initially intended for the purpose of learning, not a real project. I mentioned this for a tip

On the New Account Registration page, you must use captcha or rate limit 

Read more : https://www.loginworks.com/blogs/add-captcha-android-app/








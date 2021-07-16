# TornCity Stock Tool
An android app that you can use to monitor stocks and create triggers that notify you when stocks have reached certain prices. It makes periodic queries from the [Torn City](https://www.torn.com/) API to gather stock data.

## Implementation Details
* Created on Android Studio using Java
* Using Retrofit2 to query the data and Gson to convert it into a Java Object
* Using Room as an abstraction tool over an SQLite database to create a more robust system with less boiler plate code
* Strictly following the MVVM architecture to maintain a separation of concerns between UI, business logic and databases
* Using a Service to run queries and compare the retrieved data with existing triggers, which will work while the app itself is closed

## Step 1
Go to the menu bar to set your API key

<img src="https://user-images.githubusercontent.com/82872666/125926258-b1a54a95-0d08-4722-95a4-e372070113f6.gif" alt="Setting Api Key" width="250"/>

## Step 2
After entering a valid API key, you can now view all the stocks and create triggers for them by tapping on the corresponding stock. You can also view existing triggers and edit or delete them if required.
 
<img src="https://user-images.githubusercontent.com/82872666/125929501-e3b47fdd-fb8a-4b0d-993d-5d43b8a650b5.gif" alt="Creating, editing and deleting triggers" width="250"/>

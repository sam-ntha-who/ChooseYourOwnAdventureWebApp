# The DIY Storytime Web App
Welcome to DIY Storytime - a web app that allows users to create their own Choose Your Own Adventure story. This is the Final Project for Grand Circus Java Bootcamp. Our RESTful API: https://github.com/sam-ntha-who/ChooseYourOwnAdventureRestApi.git

The DIY Storytime web app makes use of a RESTful API to communicate with a No-SQL database to perform all CRUD (Create/Read/Update/Delete) functionality to both Scene and Story collections. It ensures that stories can be played, written, edited and deleted with ease. As this web app is for creating Choose Your Own Adventure style stories, the data is arranged in a tree so that story paths can be easily nested within the scenes they are accessed from. 

This app consumes three APIs - a RESTful API of our own creation, that performs all database functionality. The other two APIs are allow for appropriate photos to be displayed for each story and scene: TwinWord API (https://www.twinword.com/api/) uses text analysis to read scene descriptions and determine keywords, while Pexels API (https://www.pexels.com/api/) is responsible for retrieving relevant photos based on the TwinWord response.

# The API

Our API performs all of the crud functionality via HTTP Requests from the web app and upon initialization copies the stories and scenes from a master collections into the collections that the user can create, read, update & delete. It assembles the tree structure whenever something is changed via the play function and caluclates the pathlengths and boolean for longest and shortest paths so that the app is updated dynamically. 

# Choose Your Own Adventure?

An interactive book or visual novel (or web app) written from a second-person point of view that gamifies the reading proces, with the reader assuming the role of the protagonist and making choices that determine the main character's actions and the plot's outcome. 
